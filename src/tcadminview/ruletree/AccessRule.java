/*
 * AccessRule.java
 *
 * Created on 18 June 2007, 11:11
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tcadminview.ruletree;
import java.util.*;

/**
 *
 * @author NZR4DL
 */
public class AccessRule extends Vector<AccessControl> {
    
    private String ruleName;
    private String ruleType;
    private Vector<Integer> treeIndex = new Vector<Integer>();
    
    /**
     * Creates a new instance of AccessRule
     */
    public AccessRule() {
        super(10);
    }
    
    public void setRuleDetails(String accessRule) {
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
    
    public AccessControl elementAt(int index) {
        return (AccessControl)super.elementAt(index);
    }
    
    public void addTreeIndex(int i) {
        treeIndex.addElement(i);
    }
    
    public int getTreeIndexAt(int index) {
        return treeIndex.elementAt(index);
    }
    
    public int[] getTreeIndex() {
        Object o[] = treeIndex.toArray();
        int i[] = new int[treeIndex.size()];
        for (int k=0; k<i.length; k++) {
            i[k] = Integer.parseInt(o[k].toString()) ;
        }
        return i;
    }
    
    public int getTreeIndexSize() {
        return treeIndex.size();
    }
    
}
