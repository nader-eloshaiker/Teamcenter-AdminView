/*
 * AccessRule.java
 *
 * Created on 18 June 2007, 11:11
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tceav.manager.access;

import java.util.ArrayList;
import tceav.manager.compare.CompareInterface;

/**
 *
 * @author NZR4DL
 */
public class AccessRule extends ArrayList<AccessControl>  implements CompareInterface {
    
    private String ruleName;
    private String ruleType;
    private ArrayList<RuleTreeNode> ruleTreeReference;
    
    /**
     * Creates a new instance of AccessRule
     */
    public AccessRule() {
    }
    
    public void setRule(String accessRule) {
        String[] temp = accessRule.split("!");
        ruleName = temp[0];
        ruleType = temp[1];
    }
    
    public String getRuleName() {
        return ruleName;
    }
    
    public String getRuleType() {
        return ruleType;
    }
    
    public String toString() {
        return ruleName;
    }
    
    public String generateExportString() {
        return ruleName+"!"+ruleType;
    }

    public ArrayList<RuleTreeNode> getRuleTreeReferences() {
        if(ruleTreeReference == null)
            ruleTreeReference = new ArrayList<RuleTreeNode>();
        
        return ruleTreeReference;
    }
    
    /********************
     * Comapare interface 
     ********************/
    
    private int compare_result = CompareInterface.EQUAL;
    
    public int getComparison() {
        return compare_result;
    }
    
    public void setComparison(int compare_result) {
        this.compare_result = compare_result;
    }
    
    public int compare(Object o) {
        AccessRule a = (AccessRule)o;
        
        if(!getRuleName().equals(a.getRuleName()))
            return CompareInterface.NOT_FOUND;
        
        if(!getRuleType().equals(a.getRuleType()))
            return CompareInterface.NOT_FOUND;
        
        if((this.size() == 0) && (a.size() == 0))
            return CompareInterface.EQUAL;
        
        int compare;
        int compareOverall = CompareInterface.EQUAL;
        AccessControl c;
        
        for(int i=0; i<this.size(); i++) {
            c = this.get(i);
            compare = CompareInterface.NOT_FOUND;
            
            for(int k=0; k<a.size(); k++){
                compare = c.compare(a.get(k));
                if(compare != CompareInterface.NOT_FOUND)
                    break;
            }
            
            c.setComparison(compare);
            if(compare != CompareInterface.EQUAL)
                compareOverall = CompareInterface.NOT_EQUAL;
            
        }
        
        if((compareOverall == CompareInterface.EQUAL) && (this.size()< a.size()))
            compareOverall = CompareInterface.NOT_EQUAL;
        
        return compareOverall;
        
    }
    
}
