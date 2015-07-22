/*
 * ruleTreeReader.java
 *
 * Created on 18 June 2007, 09:36
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package tceav.manager.access;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.ProgressMonitor;
import javax.swing.ProgressMonitorInputStream;
import tceav.gui.AdminViewFrame;
import tceav.manager.ManagerAdapter;
import tceav.xml.DOMReader;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author NZR4DL
 */
public class AccessManager extends ManagerAdapter {

    private MetaData metaData;
    private ArrayList<NamedAcl> unusedRules;
    private RuleTreeNode rootTreeNode;
    private ArrayList<RuleTreeNode> treeNodeList;
    private File file;
    private AdminViewFrame parentFrame;
    private NamedAclList namedAclList;
    private ArrayList<String> conditionsList;

    /** Creates a new instance of ruleTreeReader */
    public AccessManager(AdminViewFrame parentFrame) {
        this.parentFrame = parentFrame;
        namedAclList = new NamedAclList();
        unusedRules = new ArrayList<NamedAcl>();
        treeNodeList = new ArrayList<RuleTreeNode>();
        rootTreeNode = new RuleTreeNode();
        conditionsList = new ArrayList<String>();
        metaData = new MetaData();
    }

    public String getManagerType() {
        return ACCESS_MANAGER_TYPE;
    }

    public File getFile() {
        return file;
    }

    public ArrayList<String> getConditions() {
        return conditionsList;
    }

    public boolean isValid() {
        if (rootTreeNode == null) {
            return false;
        } else if (!rootTreeNode.isValid()) {
            return false;
        } else if (namedAclList.size() == 0) {
            return false;
        } else {
            return true;
        }
    }

    public NamedAcl getUnusedRule(int index) {
        return unusedRules.get(index);
    }

    public ArrayList<NamedAcl> getUnusedRules() {
        return unusedRules;
    }

    public RuleTreeNode getAccessTree() {
        return rootTreeNode;
    }

    public ArrayList<RuleTreeNode> getAccessTreeList() {
        return treeNodeList;
    }

    public NamedAclList getAccessRuleList() {
        return namedAclList;
    }

    public MetaData getMetaData() {
        return metaData;
    }

    private void findUnusedRules() {
        if (namedAclList.size() <= 0) {
            return;
        }
        for (int i = 0; i < namedAclList.size(); i++) {
            if (namedAclList.get(i).getRuleTreeReferences().isEmpty()) {
                if (namedAclList.get(i).getRuleType().equals("RULETREE")) {
                    unusedRules.add(namedAclList.get(i));
                }
            }
        }
    }

    public void readFile(File file) throws Exception {

        this.file = file;

        if (file.length() == 0) {
            throw (new IOException("Empty File"));
        }

        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String firstLine = br.readLine();
        br.close();


        if (file.getName().endsWith("xml")) {
            readXmlFile();
        } else {
            if (firstLine != null) {
                if (firstLine.startsWith("<?xml")) {
                    readXmlFile();
                } else if (firstLine.startsWith("#AM")) {
                    readTextFile();
                }
            } else {
                throw (new IOException("Unrecognised File Format"));
            }
        }

        findUnusedRules();
    }

    private void readXmlFile() throws Exception {
        FileInputStream fis = new FileInputStream(file);
        DOMReader domUtil;
        try {
            ProgressMonitorInputStream pmi = new ProgressMonitorInputStream(
                    parentFrame, "Reading " + file.getName(), fis);
            domUtil = new DOMReader(pmi);

        } catch (Exception exc) {
            throw new Exception("Error reading XML: " + exc);
        }
        mapXML(domUtil.getRootNode());
    }

    private void readTextFile() throws Exception {
        final int MODE_METADATA_AND_ACCESS_CONTROL_HEADER = 0;
        final int MODE_ACCESS_CONTROL = 1;
        final int MODE_RULE_TREE = 2;
        final int MODE_UNEXPECTED_EOF = 3;
        final int MODE_CORRUPTED_FILE = 4;

        String thisLine;
        String ruleMetaData = new String();
        int readMode = MODE_METADATA_AND_ACCESS_CONTROL_HEADER;
        int ruleTreeIndex = 0;

        RuleTreeNode newNode;
        RuleTreeNode currentNode = new RuleTreeNode();
        int indentVariance = 0;


        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);

        if (file.length() == 0) {
            throw (new IOException("Empty File"));
        }

        while ((thisLine = br.readLine()) != null) {

            switch (readMode) {
                case MODE_METADATA_AND_ACCESS_CONTROL_HEADER:
                    if (thisLine.startsWith("#")) {

                        if (ruleMetaData.length() > 0) {
                            ruleMetaData += "\n";
                        }

                        ruleMetaData += thisLine;

                    } else {
                        if (ruleMetaData != null) {
                            metaData.loadLegacey(ruleMetaData);

                            if (thisLine.length() != 0) {
                                namedAclList.createAccessControlColumns(thisLine);
                                readMode = MODE_ACCESS_CONTROL;
                            } else {
                                readMode = MODE_CORRUPTED_FILE;
                            }
                        } else {
                            readMode = MODE_CORRUPTED_FILE;
                        }
                    }
                    break;

                case MODE_ACCESS_CONTROL:
                    if (thisLine.length() == 0) {

                        thisLine = br.readLine();

                        if (thisLine != null) {

                            if (thisLine.length() == 0) {
                                readMode = MODE_RULE_TREE;
                            } else {
                                namedAclList.addNewACL(thisLine);
                            }

                        } else {
                            readMode = MODE_UNEXPECTED_EOF;
                        }
                    } else {
                        namedAclList.addNewAccessControl(thisLine);
                    }
                    break;

                case MODE_RULE_TREE:
                    if (thisLine.length() != 0) {
                        newNode = new RuleTreeNode(thisLine, namedAclList, conditionsList);
                        treeNodeList.add(newNode);

                        // Build Tree Node
                        if (newNode.getIndentLevel() == 0) {
                            rootTreeNode = newNode;
                        } else if (newNode.getIndentLevel() > currentNode.getIndentLevel()) {
                            currentNode.addChild(newNode);
                        } else if (newNode.getIndentLevel() == currentNode.getIndentLevel()) {
                            currentNode.getParent().addChild(newNode);
                        } else if (newNode.getIndentLevel() < currentNode.getIndentLevel()) {
                            indentVariance = currentNode.getIndentLevel() - newNode.getIndentLevel() + 1;

                            for (int j = 1; j < indentVariance; j++) {
                                currentNode = currentNode.getParent();
                            }
                            currentNode.getParent().addChild(newNode);
                        }

                        currentNode = newNode;
                        ruleTreeIndex++;
                    }
                    break;

                case MODE_UNEXPECTED_EOF:
                    throw new IOException("Unexpected End Of File");

                case MODE_CORRUPTED_FILE:
                    throw new IOException("Corrupted File");

                default:
                    break;
            }

        } // end while

        br.close();

    }

    private void mapXML(Node parentNode) throws Exception {
        Node currentNode;
        NodeList rootlist = parentNode.getChildNodes();
        AccessTagTypeEnum tagType;


        if (AccessTagTypeEnum.fromValue(parentNode.getNodeName()) != AccessTagTypeEnum.TcDataAccessConfig) {
            throw (new Exception("Not a Teamcenter Engineering Rule Tree"));
        }

        for (int i = 0; i < rootlist.getLength(); i++) {
            currentNode = parentNode.getChildNodes().item(i);
            tagType = AccessTagTypeEnum.fromValue(currentNode.getNodeName());

            if (tagType == AccessTagTypeEnum.NamedACLs) {
                mapNamedACLsFromXML(currentNode.getChildNodes());
            } else if (tagType == AccessTagTypeEnum.RuleTree) {
                mapRuleTreeFromXML(currentNode.getChildNodes());
            }
        }
    }

    private void mapNamedACLsFromXML(NodeList nodeList) {
        AccessTagTypeEnum tagType;
        Node currentNode;

        try {
            ProgressMonitor progressMonitor = new ProgressMonitor(
                    parentFrame,
                    "Mapping XML Tags to Named ACLs",
                    "",
                    0,
                    nodeList.getLength() - 1);


            for (int nodeIndex = 0; nodeIndex < nodeList.getLength(); nodeIndex++) {

                if (progressMonitor.isCanceled()) {
                    progressMonitor.close();
                    return;
                }

                currentNode = nodeList.item(nodeIndex);
                tagType = AccessTagTypeEnum.fromValue(currentNode.getNodeName());

                progressMonitor.setProgress(nodeIndex);

                if (tagType == AccessTagTypeEnum.NamedACL) {
                    NamedAcl n = namedAclList.addNewACL(currentNode);

                    if (n != null) {
                        progressMonitor.setNote(n.getRuleName());
                    }
                }


            }

        } catch (Exception ex) {
            System.err.println("Map XML Error: " + ex.getMessage());
        }
    }

    private void mapRuleTreeFromXML(NodeList nodeList) {
        AccessTagTypeEnum tagType;
        Node currentNode;

        try {

            for (int nodeIndex = 0; nodeIndex < nodeList.getLength(); nodeIndex++) {

                currentNode = nodeList.item(nodeIndex);
                tagType = AccessTagTypeEnum.fromValue(currentNode.getNodeName());

                if (tagType == AccessTagTypeEnum.TreeNode) {
                    rootTreeNode = new RuleTreeNode(currentNode, namedAclList, conditionsList);
                    break;
                }
            }

        } catch (Exception ex) {
            System.err.println("Map XML Error: " + ex.getMessage());
        }
    }

    @Override
    public String toString() {
        String s;
        NamedAcl ar;
        AccessControl acEntry;
        RuleTreeNode amItem;

        s = "MetaData:\n" + metaData.toString() + "\n"
                + "Access Control acHeader:\n" + namedAclList.getAccessControlColumns().toString() + "\n"
                + "Access Rules:\n";

        for (int i = 0; i < namedAclList.size(); i++) {
            ar = namedAclList.get(i);
            s += ar.toString() + "\n";

            for (int j = 0; j < ar.size(); j++) {
                acEntry = ar.get(j);
                s += acEntry.toString() + "\n";
            }

            s += "\n\n";
        }

        for (int i = 0; i < treeNodeList.size(); i++) {
            s += treeNodeList.get(i).toString() + "\n";
        }

        return s;
    }
}
