/*
 * AccessControl.java
 *
 * Created on 18 June 2007, 11:12
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tcav.manager.access;

import tcav.manager.compare.CompareInterface;

/**
 *
 * @author NZR4DL
 */
public class AccessControl implements CompareInterface {
    
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
    
    public String getTypeOfAccessor() {
        return accessControl[0];
    }
    
    public String getIdOfAccessor() {
        return accessControl[1];
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
        AccessControl c = (AccessControl)o;
        
        if(accessControl.length != c.getAccessControl().length)
            return CompareInterface.NOT_FOUND;
        
        if (getTypeOfAccessor().equals(c.getTypeOfAccessor()) && getIdOfAccessor().equals(c.getIdOfAccessor())){
            
            for(int i=2; i<accessControl.length; i++) {
                if(!accessControl[i].equals(c.getAccess(i)))
                    return CompareInterface.NOT_EQUAL;
            }
            
            return CompareInterface.EQUAL;
        }
        
        return CompareInterface.NOT_FOUND;
    }
    
}
