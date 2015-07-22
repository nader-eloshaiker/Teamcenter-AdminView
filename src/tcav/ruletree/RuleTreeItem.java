/*
 * RuleTreeItem.java
 *
 * Created on 19 June 2007, 11:47
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tcav.ruletree;

/**
 *
 * @author NZR4DL
 */
public class RuleTreeItem extends Object {
    
    private String condition;
    private String value;
    private String accessRuleName;
    private int indentLevel;
    private int accessRuleListIndex = -1;
    private String[] parameters;
    private int[] ancestorsIndex;
    
    /**
     * Creates a new instance of RuleTreeItem
     */
    public RuleTreeItem(String s) {
        
        String[] s1 = s.split("    ");
        String[] s2 = s1[s1.length-1].split("->");
        
        indentLevel = s1.length-1;
        
        condition = s2[0].substring(0,s2[0].indexOf("("));
        value = s2[0].substring(s2[0].indexOf("(")+2,s2[0].indexOf(")")-1);
        
        if(s2.length > 1){
            if ( s2[1].indexOf(',') > 0 ) {
                String[] s3 = s2[1].split(" , ");
                parameters = new String[s3.length-1];
                for(int i=1; i<s3.length; i++)
                    parameters[i-1] = s3[i];
                accessRuleName = s3[0];
            } else
                accessRuleName = s2[1];
        }
    }
    
    public int[] getAncestors() {
        if(ancestorsIndex == null)
            ancestorsIndex = new int[0];
        return ancestorsIndex;
    }
    
    public int getAncestorsSize() {
        return ancestorsIndex.length;
    }
    
    public void setAncestors(int[] ancestorsIndex) {
        this.ancestorsIndex = ancestorsIndex;
    }
    
    public String[] getParamters() {
        return parameters;
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
        
        if (parameters != null)
            for(int k=0; k<parameters.length; k++)
                s += " , "+parameters[k];
        
        return s;
    }
    
}
