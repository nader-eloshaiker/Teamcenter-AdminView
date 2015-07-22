/*
 * AccessControlHeader.java
 *
 * Created on 18 June 2007, 11:26
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package tceav.manager.access;

/**
 *
 * @author NZR4DL
 */
public class AccessControlHeader {
    
    private AccessControlHeaderItem[] columns;
    
    /**
     * Creates a new instance of AccessControlHeader
     */
    public AccessControlHeader(String str) {
        String[] s = str.split("!");
        columns = new AccessControlHeaderItem[s.length+2];
        columns[0] = new AccessControlHeaderItem(AccessControlHeaderEnum.AccessorType);
        columns[1] = new AccessControlHeaderItem(AccessControlHeaderEnum.AccessorID);
        
        for (int i=0; i<s.length; i++)
            columns[i+2] = new AccessControlHeaderItem(AccessControlHeaderEnum.fromValue(s[i]));
        
    }
    
    public AccessControlHeaderItem getColumn(int index) {
        return columns[index];
    }
    
    public AccessControlHeaderItem[] getColumns() {
        return columns;
    }
    
    public int size() {
        return columns.length;
    }
    
    @Override
    public String toString() {
        String s = "";
        for(int i=0; i<columns.length; i++)
            s += columns[i].value()+"!";
        return s;
    }
    
    public String generateExportString() {
        String s = "";
        for(int i=2; i<columns.length; i++)
            s += columns[i].value()+"!";
        return s;
    }
}
