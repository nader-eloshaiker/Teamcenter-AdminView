/*
 * ruleTreeReader.java
 *
 * Created on 18 June 2007, 09:36
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package tceav.manager.access;

import java.io.*;
import java.util.*;
import tceav.utils.ArrayListSorter;
import tceav.manager.ManagerAdapter;

/**
 *
 * @author NZR4DL
 */
public class AccessManager extends ManagerAdapter {

    private MetaData metaData;
    private AccessControlHeader acHeader;
    private AccessRuleList arList;
    private ArrayList<AccessRule> unusedRules;
    private RuleTreeNode rootTreeNode;
    private ArrayList<RuleTreeNode> treeNodeList;
    private File file;

    /** Creates a new instance of ruleTreeReader */
    public AccessManager() {
        arList = new AccessRuleList();
        unusedRules = new ArrayList<AccessRule>();
        treeNodeList = new ArrayList<RuleTreeNode>();
        rootTreeNode = new RuleTreeNode();
        conditionsList = new Vector<String>();
    }

    public String getManagerType() {
        return ACCESS_MANAGER_TYPE;
    }

    public File getFile() {
        return file;
    }

    public boolean isValid() {
        if (rootTreeNode == null) {
            return false;
        } else if (!rootTreeNode.isValid()) {
            return false;
        } else if (arList.size() == 0) {
            return false;
        } else {
            return true;
        }
    }

    public AccessRule getUnusedRule(int index) {
        return unusedRules.get(index);
    }

    public ArrayList<AccessRule> getUnusedRules() {
        return unusedRules;
    }

    public AccessControlHeader getAccessControlColumns() {
        return acHeader;
    }

    public RuleTreeNode getAccessTree() {
        return rootTreeNode;
    }

    public ArrayList<RuleTreeNode> getAccessTreeList() {
        return treeNodeList;
    }

    public AccessRuleList getAccessRuleList() {
        return arList;
    }

    public MetaData getMetaData() {
        return metaData;
    }

    public void readFile(File file) throws IOException {
        final int MODE_METADATA_AND_ACCESS_CONTROL_HEADER = 0;
        final int MODE_ACCESS_CONTROL = 1;
        final int MODE_RULE_TREE = 2;
        final int MODE_UNEXPECTED_EOF = 3;
        final int MODE_CORRUPTED_FILE = 4;

        this.file = file;

        String thisLine;
        String ruleMetaData = "";
        int readMode = MODE_METADATA_AND_ACCESS_CONTROL_HEADER;
        int ruleTreeIndex = 0;
        AccessRule accessRule = new AccessRule();

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
                            metaData = new MetaData(ruleMetaData);
                            if (thisLine.length() != 0) {
                                acHeader = new AccessControlHeader(thisLine);
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
                                arList.add(accessRule);
                                readMode = MODE_RULE_TREE;
                            } else {
                                if (accessRule.toString() != null) {
                                    arList.add(accessRule);
                                }
                                accessRule = new AccessRule();
                                accessRule.setRule(thisLine);
                            }
                        } else {
                            readMode = MODE_UNEXPECTED_EOF;
                        }
                    } else {
                        AccessControl acEntry = new AccessControl(thisLine);
                        accessRule.add(acEntry);
                    }
                    break;

                case MODE_RULE_TREE:
                    if (thisLine.length() != 0) {
                        newNode = new RuleTreeNode(thisLine);
                        treeNodeList.add(newNode);

                        // Build Conditions List
                        if (conditionsList.indexOf(newNode.getCondition()) == -1) {
                            conditionsList.add(newNode.getCondition());
                            ArrayListSorter.sortStringArray(conditionsList);
                        }

                        // Attach AccessRule
                        if (newNode.getAccessRuleName() != null) {
                            newNode.setAccessRule(arList.get(newNode.getAccessRuleName()));
                        }

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
            }

        } // end while

        br.close();
        findUnusedRules();
    }
    
    
    private Vector<String> conditionsList;

    public Vector<String> getConditions() {
        return conditionsList;
    }

    @Override
    public String toString() {
        String s;
        AccessRule ar;
        AccessControl acEntry;
        RuleTreeNode amItem;

        s = "MetaData:\n" + metaData.toString() + "\n" +
                "Access Control acHeader:\n" + acHeader.toString() + "\n" +
                "Access Rules:\n";

        for (int i = 0; i < arList.size(); i++) {
            ar = arList.get(i);
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

    private void findUnusedRules() {
        if (arList.size() <= 0) {
            return;
        }
        for (int i = 0; i < arList.size(); i++) {
            if (arList.get(i).getRuleTreeReferences().size() == 0) {
                if (arList.get(i).getRuleType().equals("RULETREE")) {
                    unusedRules.add(arList.get(i));
                }
            }
        }
    }
}
