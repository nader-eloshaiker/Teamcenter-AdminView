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
    //private RuleTree ruleTree;
    private ArrayList<RuleTreeItem> ruleTree;
    private Vector<Integer> accessRuleUnusedIndex;
    
    
    /** Creates a new instance of ruleTreeReader */
    public AccessManager() {
        metaData = new MetaData();
        arList = new AccessRuleList();
        //ruleTree = new RuleTree();
        ruleTree = new ArrayList<RuleTreeItem>();
        acHeader = new AccessControlHeader();
        accessRuleUnusedIndex = new Vector<Integer>();
    }
    
    public int unusedElementAt(int index) {
        Integer i = accessRuleUnusedIndex.elementAt(index);
        return i.intValue();
    }
    
    public int getUnusedRulesSize() {
        return accessRuleUnusedIndex.size();
    }
    
    public AccessControlHeader getAccessControlColumns() {
        return acHeader;
    }
    
    public ArrayList<RuleTreeItem> getAccessManagerTree() {
        return ruleTree;
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
        RuleTreeItem amItem;
        
        int currentIndent = 0;
        int newIndent = 0;
        int indentVariance = 0;
        int parentNodeIndex = 0;
        Stack<Integer> parentalIndex = new Stack<Integer>();
        
        
        
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
                        metaData.setMetaData(ruleMetaData);
                    }
                    
                case MODE_ACCESS_CONTROL_acHeader:
                    if (thisLine.length() != 0){
                        acHeader.setAccessControlColumns(thisLine);
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
                            arList.addElement(accessRule);
                        
                    } else {
                        crDetected = false;
                        if(toggleNewRule) {
                            accessRule = new AccessRule();
                            accessRule.setRuleDetails(thisLine);
                            toggleNewRule = false;
                        } else {
                            AccessControl acEntry = new AccessControl(thisLine);
                            accessRule.addElement(acEntry);
                        }
                    }
                    break;
                    
                case MODE_RULE_TREE:
                    if (thisLine.length() != 0) {
                        amItem = new RuleTreeItem(thisLine);

                        if(conditionsList.indexOf(amItem.getCondition()) == -1) {
                            conditionsList.add(amItem.getCondition());
                            ArrayListSorter.sortStringArray(conditionsList);
                        }
                        
                        if(amItem.getAccessRuleName() != null) {
                            amItem.setAccessRuleListIndex(arList.indexOf(amItem.getAccessRuleName()));
                            accessRule = arList.get(amItem.getAccessRuleListIndex());
                            accessRule.addTreeIndex(ruleTreeIndex);
                        }
                        
                        newIndent = amItem.getIndentLevel();
                        if (newIndent >  currentIndent) {
                            parentalIndex.push(ruleTreeIndex-1);
                        } else if (newIndent <  currentIndent) {
                            indentVariance = currentIndent - newIndent;
                            for (int j=0; j<indentVariance; j++)
                                parentalIndex.pop();
                        }
                        currentIndent = newIndent;
                        amItem.setAncestors(getArray(parentalIndex));
                        
                        ruleTree.add(amItem);
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
    
    public int[] getArray(Stack<Integer> stack) {
        int[] array = new int[stack.size()];
        for(int i=0; i<stack.size(); i++)
            array[i] = stack.elementAt(i);
        return array;
    }
    
    public String toString() {
        String s;
        AccessRule ar;
        AccessControl acEntry;
        RuleTreeItem amItem;
        
        s = "MetaData:\n" + metaData.toString() + "\n" +
                "Access Control acHeader:\n" + acHeader.toString() + "\n" +
                "Access Rules:\n";
        
        for (int i=0; i<arList.size(); i++){
            ar = arList.elementAt(i);
            s += ar.toString()+"\n";
            
            for(int j=0; j<ar.size(); j++){
                acEntry = ar.elementAt(j);
                s += acEntry.toString()+"\n";
            }
            
            s += "\n\n";
        }
        
        for (int k=0; k<ruleTree.size(); k++) {
            amItem = ruleTree.get(k);
            s += amItem.toString()+"\n";
        }
        
        return s;
    }
    
    private void findUnusedRules() {
        if (arList.size()<=0)
            return;
        
        for (int i=0; i<arList.size(); i++)
            if(arList.elementAt(i).getTreeIndexSize() == 0)
                if(arList.elementAt(i).getRuleType().equals("RULETREE"))
                    accessRuleUnusedIndex.addElement(i);
        
    }
    
}
