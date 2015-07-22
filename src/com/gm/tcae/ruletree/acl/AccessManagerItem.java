/*
 * AccessManagerItem.java
 *
 * Created on 19 June 2007, 11:47
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.gm.tcae.ruletree.acl;

/**
 *
 * @author NZR4DL
 */
public class AccessManagerItem extends Object {
    
    private String condition;
    private String value;
    private String accessRuleName;
    private int indentLevel;
    private int accessRuleListIndex = -1;
    private boolean collapsed = false;
    
    /** Creates a new instance of AccessManagerItem */
    public AccessManagerItem(String s) {
        
        String[] s1 = s.split("    ");
        String[] s2 = s1[s1.length-1].split("->");
        
        indentLevel = s1.length-1;
        
        condition = s2[0].substring(0,s2[0].indexOf("("));
        value = s2[0].substring(s2[0].indexOf("(")+2,s2[0].indexOf(")")-1);
        
        if ( s2.length > 1 ) {
            collapsed = s2[1].endsWith(" , Collapsed");
            if (collapsed)
                accessRuleName = s2[1].substring(0,s2[1].length()-12);
            else
                accessRuleName = s2[1];
        }
        else {
            s2[0].endsWith(" , Collapsed");
        }
            
        
    }
    
    public boolean isCollapsed() {
        return collapsed;
    }
    
    public int getAccessRuleListIndex() {
        return accessRuleListIndex;
    }
    
    public void setAccessRuleListIndex(int index) {
        accessRuleListIndex = index;
    }
    
    public String getCondition(){
        return condition;
    }
    
    public String getValue() {
        return value;
    }
    
    public String getAccessRuleName() {
        return accessRuleName;
    }
    
    public int getIndentLevel() {
        return indentLevel;
    }
    
    public String toString() {
        String s = condition+"( "+value+" )";
        
        if (accessRuleName != null)
            s += " -> "+accessRuleName;

        return s;
    }
    
    public String generateExportString() {
        String s = "";
        
        for(int l=0; l<indentLevel; l++)
            s += "    ";
        
        s += toString();
        
        if (isCollapsed())
            s += " , Collapsed";
        return s;
    }
    
}
