/*
 * AccessRule.java
 *
 * Created on 18 June 2007, 11:11
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tcav.ruletree;
import java.util.*;

/**
 *
 * @author NZR4DL
 */
public class AccessRule extends ArrayList<AccessControl> {
    
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
    
}
