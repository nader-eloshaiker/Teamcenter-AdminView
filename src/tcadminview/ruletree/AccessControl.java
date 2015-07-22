/*
 * AccessControl.java
 *
 * Created on 18 June 2007, 11:12
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tcadminview.ruletree;

/**
 *
 * @author NZR4DL
 */
public class AccessControl {
    
    private String[] accessControl;
    
    /** Creates a new instance of AclRuleElement */
    public AccessControl(String ruleEntry) {
        accessControl = ruleEntry.split("!");
    }
    
    /* Gets the control access
     * @param index location of the control
     * @return state "Y" = True, "N" = False, " " = Not set
     */
    public String getAccess(int index) {
        return accessControl[index];
    }
    
    /* Sets the control access
     * @param index location of the control
     * @param state "Y" = True, "N" = False, " " = Not set
     */
    public void setAccess(int index, String state) {
        if(state.equals("Y"))
            accessControl[index] = "Y";
        else if(state.equals("N"))
            accessControl[index] = "N";
        else if(state.equals(" "))
            accessControl[index] = " ";
    }
    
    public String getTypeOfAccessor() {
        return accessControl[0];
    }
    
    public void setTypeOfAccessor(String s) {
        accessControl[0] = s;
    }
    
    public String getIdOfAccessor() {
        return accessControl[1];
    }
    
    public void setIdOfAccessor(String s) {
        accessControl[1] = s;
    }
    
    public String[] getAccessControl() {
        return accessControl;
    }
    
    public String toString() {
        String s = "";
        for(int i=0; i<accessControl.length; i++)
            s += accessControl[i]+"!";
        return s;
    }
    
    public String generateExportString() {
        return toString();
    }
    
}
