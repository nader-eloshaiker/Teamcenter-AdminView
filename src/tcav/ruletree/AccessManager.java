/*
 * ruleTreeReader.java
 *
 * Created on 18 June 2007, 09:36
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tcav.ruletree;

import java.io.*;
import java.util.*;
import tcav.utils.ArrayListSorter;
/**
 *
 * @author NZR4DL
 */
public class AccessManager {
    
    private MetaData metaData;
    private AccessControlHeader acHeader;
    private AccessRuleList arList;
    private ArrayList<AccessRule> unusedRules;
    private RuleTreeNode rootTreeNode;
    
    
    /** Creates a new instance of ruleTreeReader */
    public AccessManager() {
        arList = new AccessRuleList();
        unusedRules = new ArrayList<AccessRule>();
        rootTreeNode = new RuleTreeNode();
    }
    
    public boolean isValid() {
        if(rootTreeNode == null)
            return false;
        else if (!rootTreeNode.isValid())
            return false;
        else if (arList.size() == 0)
            return false;
        else
            return true;
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
    
    public RuleTreeNode getAccessManagerTree() {
        return rootTreeNode;
    }
    
    public AccessRuleList getAccessRuleList() {
        return arList;
    }
    
    public MetaData getMetaData() {
        return metaData;
    }
    
    public void readFile(File file) throws IOException {
        final int MODE_METADATA = 0;
        final int MODE_ACCESS_CONTROL_acHeader = 1;
        final int MODE_ACCESS_CONTROL = 2;
        final int MODE_RULE_TREE = 3;
        
        String thisLine;
        String ruleMetaData = "";
        int readMode = MODE_METADATA;
        int ruleTreeIndex = 0;
        boolean toggleNewRule = true;
        boolean toggleTopTreeNode = true;
        boolean crDetected = false;
        AccessRule accessRule = new AccessRule();
        RuleTreeNode amItem;
        
        RuleTreeNode newNode;
        RuleTreeNode currentNode = new RuleTreeNode();
        int currentIndent = 0;
        int newIndent = 0;
        int indentVariance = 0;
        int parentNodeIndex = 0;
        
        
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        
        if(file.length()==0)
            throw (new IOException("Empty File"));
        
        while ((thisLine = br.readLine( )) != null) {
            
            switch (readMode) {
                case MODE_METADATA:
                    if(thisLine.startsWith("#")){
                        
                        if (ruleMetaData.length()>0)
                            ruleMetaData += "\n";
                        
                        ruleMetaData += thisLine;
                        break;
                    } else {
                        //All meta data has been imported,
                        //This line contains acHeader, don't break.
                        readMode = MODE_ACCESS_CONTROL_acHeader;
                        metaData = new MetaData(ruleMetaData);
                    }
                    
                case MODE_ACCESS_CONTROL_acHeader:
                    if (thisLine.length() != 0){
                        acHeader = new AccessControlHeader(thisLine);
                        readMode = MODE_ACCESS_CONTROL;
                    }
                    break;
                    
                case MODE_ACCESS_CONTROL:
                    if (thisLine.length() == 0){
                        //is this the second consecutive cr?
                        if (crDetected == true){
                            //End of Rules, Tree begins
                            readMode = MODE_RULE_TREE;
                            crDetected = false;
                        } else
                            crDetected = true;
                        
                        toggleNewRule = true;
                        if (accessRule.toString() != null)
                            arList.add(accessRule);
                        
                    } else {
                        crDetected = false;
                        if(toggleNewRule) {
                            accessRule = new AccessRule();
                            accessRule.setRule(thisLine);
                            toggleNewRule = false;
                        } else {
                            AccessControl acEntry = new AccessControl(thisLine);
                            accessRule.add(acEntry);
                        }
                    }
                    break;
                    
                case MODE_RULE_TREE:
                    if (thisLine.length() != 0) {
                        newNode = new RuleTreeNode(thisLine);

                        // Build Conditions List
                        if(conditionsList.indexOf(newNode.getCondition()) == -1) {
                            conditionsList.add(newNode.getCondition());
                            ArrayListSorter.sortStringArray(conditionsList);
                        }
                        
                        // Attach AccessRule
                        if(newNode.getAccessRuleName() != null) {
                            newNode.setAccessRule(arList.get(newNode.getAccessRuleName()));
                        }
                        
                        // Build Tree Node
                        if(newNode.getIndentLevel() == 0) {
                            rootTreeNode = newNode;
                        } else if(newNode.getIndentLevel() > currentNode.getIndentLevel()) {
                            currentNode.addChild(newNode);
                        } else if(newNode.getIndentLevel() == currentNode.getIndentLevel()) {
                            currentNode.getParent().addChild(newNode);
                        } else if(newNode.getIndentLevel() < currentNode.getIndentLevel()) {
                            indentVariance = currentNode.getIndentLevel() - newNode.getIndentLevel() + 1;
                            
                            for (int j=1; j<indentVariance; j++)
                                currentNode = currentNode.getParent();
                            
                            currentNode.getParent().addChild(newNode);
                        }

                        currentNode = newNode;
                        ruleTreeIndex++;
                    }
                    break;
            }
            
        } // end while
        
        br.close();
        findUnusedRules();
    }
    
    private Vector<String> conditionsList = new Vector<String>();;
    
    public Vector<String> getConditions() {
        return conditionsList;
    }

    public String toString() {
        String s;
        AccessRule ar;
        AccessControl acEntry;
        RuleTreeNode amItem;
        
        s = "MetaData:\n" + metaData.toString() + "\n" +
                "Access Control acHeader:\n" + acHeader.toString() + "\n" +
                "Access Rules:\n";
        
        for (int i=0; i<arList.size(); i++){
            ar = arList.get(i);
            s += ar.toString()+"\n";
            
            for(int j=0; j<ar.size(); j++){
                acEntry = ar.get(j);
                s += acEntry.toString()+"\n";
            }
            
            s += "\n\n";
        }
        
        for (Enumeration e=rootTreeNode.nodes(); e.hasMoreElements();) {
            amItem = (RuleTreeNode)e;
            s += amItem.toString()+"\n";
        }
        
        return s;
    }
    
    private void findUnusedRules() {
        if (arList.size()<=0)
            return;
        
        for (int i=0; i<arList.size(); i++)
            if(arList.get(i).getRuleTreeReferences().size() == 0)
                if(arList.get(i).getRuleType().equals("RULETREE"))
                    unusedRules.add(arList.get(i));
    }
    
}
