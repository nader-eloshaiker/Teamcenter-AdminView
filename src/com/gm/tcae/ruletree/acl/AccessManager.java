/*
 * ruleTreeReader.java
 *
 * Created on 18 June 2007, 09:36
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.gm.tcae.ruletree.acl;

import java.io.*;
import java.util.*;
/**
 *
 * @author NZR4DL
 */
public class AccessManager {
    
    private MetaData metaData;
    private AccessControlColumns columns;
    private AccessRuleList arList;
    private AccessManagerTree amTree;
    private Vector<Integer> accessRuleUnusedIndex;
    
    
    /** Creates a new instance of ruleTreeReader */
    public AccessManager() {
        metaData = new MetaData();
        arList = new AccessRuleList();
        amTree = new AccessManagerTree();
        columns = new AccessControlColumns();
        accessRuleUnusedIndex = new Vector<Integer>();
    }
    
    public int unusedElementAt(int index) {
        Integer i = accessRuleUnusedIndex.elementAt(index);
        return i.intValue();
    }
    
    public int getAccessRulesSize() {
        return arList.size();
    }
    
    public int getAccessManagerTreeSize() {
        return amTree.size();
    }
    
    public int getUnusedRulesSize() {
        return accessRuleUnusedIndex.size();
    }
    
    public AccessControlColumns getAccessControlColumns() {
        return columns;
    }
    
    public AccessManagerTree getAccessManagerTree() {
        return amTree;
    }
    
    public AccessRuleList getAccessRuleList() {
        return arList;
    }
    
    public MetaData getMetaData() {
        return metaData;
    }
    
    public void importRuleTree(File file) throws IOException {
        final int MODE_METADATA = 0;
        final int MODE_ACCESS_CONTROL_COLUMNS = 1;
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
        AccessManagerItem amItem;
        
        
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
                        //This line contains Columns, don't break.
                        readMode = MODE_ACCESS_CONTROL_COLUMNS;
                        metaData.setMetaData(ruleMetaData);
                    }
                    
                case MODE_ACCESS_CONTROL_COLUMNS:
                    if (thisLine.length() != 0){
                        columns.setAccessControlColumns(thisLine);
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
                        amItem = new AccessManagerItem(thisLine);
                        amItem.setAccessRuleListIndex(arList.indexOf(amItem.getAccessRuleName()));
                        amTree.addElement(amItem);
                        
                        if(amItem.getAccessRuleName() != null) {
                            accessRule = arList.get(arList.indexOf(amItem.getAccessRuleName()));
                            accessRule.addTreeIndex(ruleTreeIndex);
                        }
                        
                        ruleTreeIndex++;
                    }
                    break;
            }
            
        } // end while
        
        br.close();
        findUnusedRules();
    }
    
    public String toString() {
        String s;
        AccessRule ar;
        AccessControl acEntry;
        AccessManagerItem amItem;
        
        s = "MetaData:\n" + metaData.toString() + "\n" +
                "Access Control Columns:\n" + columns.toString() + "\n" +
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
        
        for (int k=0; k<amTree.size(); k++) {
            amItem = amTree.elementAt(k);
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
