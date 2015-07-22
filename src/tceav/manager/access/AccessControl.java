/*
 * AccessControl.java
 *
 * Created on 18 June 2007, 11:12
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package tceav.manager.access;

import tceav.manager.compare.CompareInterface;

/**
 *
 * @author NZR4DL
 */
public class AccessControl implements CompareInterface {

    private String[] accessControl;
    AccessControlHeaderItem[] columns;

    /** Creates a new instance of AclRuleElement */
    public AccessControl(String ruleEntry) {
        accessControl = ruleEntry.split("!");
    }

    private int indexOfColumn(AccessControlHeaderItem a) {
        for (int i = 0; i < columns.length; i++) {
            if (a.equals(columns[i])) {
                return i;
            }
        }
        return -1;
    }

    public void setColumns(AccessControlHeaderItem[] c) {
        columns = c;
    }

    /* Gets the control access
     * @param index location of the control
     * @return state "Y" = True, "N" = False, " " = Not set
     */
    public String getAccess(int index) {
        return accessControl[index];
    }

    public String getAccessForColumn(AccessControlHeaderItem a) {
        int index = indexOfColumn(a);

        if(index == -1)
            return null;
        else
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

    @Override
    public String toString() {
        String s = "";
        for (int i = 0; i < accessControl.length; i++) {
            s += accessControl[i] + "!";
        }
        return s;
    }

    public String generateExportString() {
        return toString();
    }
    /**********************
     * Comapare interface *
     **********************/
    private int compare_result = CompareInterface.EQUAL;

    public int getComparison() {
        return compare_result;
    }

    public void setComparison(int compare_result) {
        this.compare_result = compare_result;
    }

    public int compare(Object o) {
        AccessControl c = (AccessControl) o;
        String value;
        
        if (getTypeOfAccessor().equals(c.getTypeOfAccessor()) && getIdOfAccessor().equals(c.getIdOfAccessor())) {

            for (int i = 2; i < accessControl.length; i++) {
                value = c.getAccessForColumn(columns[i]);
                if(value == null)
                    continue;
                else if (!accessControl[i].equals(value)) {
                    return CompareInterface.NOT_EQUAL;
                }
            }

            return CompareInterface.EQUAL;

        } else {
            return CompareInterface.NOT_FOUND;
        }
    }

}
