/*
 * AccessControlHeader.java
 *
 * Created on 18 June 2007, 11:26
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package tceav.manager.access;

import tceav.resources.ImageEnum;

/**
 *
 * @author NZR4DL
 */
public class AccessControlHeader {
    
    private AccessControlHeaderItem[] column;
    
    /**
     * Creates a new instance of AccessControlHeader
     */
    public AccessControlHeader(String str) {
        String[] s = str.split("!");
        column = new AccessControlHeaderItem[s.length+2];
        column[0] = new AccessControlHeaderItem(AccessControlHeaderEnum.AccessorType);
        column[1] = new AccessControlHeaderItem(AccessControlHeaderEnum.AccessorID);
        
        for (int i=0; i<s.length; i++)
            column[i+2] = new AccessControlHeaderItem(AccessControlHeaderEnum.fromValue(s[i]));
        
    }
    
    public AccessControlHeaderItem getColumn(int index) {
        return column[index];
    }
    
    public AccessControlHeaderItem[] getColumns() {
        return column;
    }
    
    public int size() {
        return column.length;
    }
    
    public String toString() {
        String s = "";
        for(int i=0; i<column.length; i++)
            s += column[i].value()+"!";
        return s;
    }
    
    public String generateExportString() {
        String s = "";
        for(int i=2; i<column.length; i++)
            s += column[i].value()+"!";
        return s;
    }
}
