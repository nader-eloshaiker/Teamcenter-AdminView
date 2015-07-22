/*
 * AccessRuleTableModel.java
 *
 * Created on 27 June 2007, 11:13
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.gm.tcae.ruletree.gui;

import javax.swing.table.*;
import com.gm.tcae.ruletree.acl.*;

/**
 *
 * @author nzr4dl
 */
public class AccessRuleTableModel extends AbstractTableModel implements TableModel {
    
    private AccessControlColumns acColumns;
    private AccessRule accessRule;
    
    /** Creates a new instance of AccessRuleTableModel */
    public AccessRuleTableModel(AccessControlColumns acc, AccessRule ar) {
        this.acColumns = acc;
        this.accessRule = ar;
    }
    
    public AccessRuleTableModel(AccessControlColumns acc) {
        this(acc, null);
    }
    
    public AccessRule getAccessRule() {
        return accessRule;
    }

    /* This method does not work correctly due to a Java Table update issue
     * Therefore need to build a new model for very new rule selection.
     */
    public void setAccessRule(AccessRule ar) {
        accessRule = ar;
        fireTableDataChanged();
    }
    
    public int getColumnCount() { 
        if (accessRule == null)
            return 0;
        else
            return acColumns.getSize(); 
    }
    
    public int getRowCount() { 
        if (accessRule == null)
            return 0;
        else
            return accessRule.size();
    }
    
    public Object getValueAt(int row, int col) {
        if((col < getColumnCount()) && (row < getRowCount())){
            return accessRule.elementAt(row).getAccessControl()[col];
        } else
            return null;
    }
    
    public AccessControlColumnsEntry[] getColumns() {
        return acColumns.getColumns();
    }
    
    public AccessControlColumnsEntry getColumn(int index) {
        return acColumns.getColumns()[index];
    }

    public String getColumnName(int col) {
        return acColumns.getName(col);
    }
    
    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }
    
    public boolean isCellEditable(int row, int col) {
        return false;
    }
    
    public void setValueAt(Object aValue, int row, int col) { 

    }
    
}
