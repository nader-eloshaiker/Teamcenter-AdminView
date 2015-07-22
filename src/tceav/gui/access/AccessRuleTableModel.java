package tceav.gui.access;
/*
 * AccessRuleTableModel.java
 *
 * Created on 27 June 2007, 11:13
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

import tceav.manager.access.AccessControlHeader;
import tceav.manager.access.AccessControlHeaderItem;
import tceav.manager.access.NamedAcl;
import javax.swing.table.*;

/**
 *
 * @author nzr4dl
 */
public class AccessRuleTableModel extends AbstractTableModel implements TableModel {
    
    private AccessControlHeader acHeader;
    private NamedAcl accessRule;
    
    /** Creates a new instance of AccessRuleTableModel */
    public AccessRuleTableModel(AccessControlHeader acHeader, NamedAcl ar) {
        this.acHeader = acHeader;
        this.accessRule = ar;
    }
    
    public AccessRuleTableModel(AccessControlHeader acHeader) {
        this(acHeader, new NamedAcl());
    }

    
    public NamedAcl getAccessRule() {
        return accessRule;
    }

    /* This method does not work correctly due to a Java Table update issue
     * Therefore need to build a new model for very new rule selection.
     */
    public void setAccessRule(NamedAcl ar) {
        if(ar != null)
            accessRule = ar;
        else
            accessRule = new NamedAcl();
    }
    
    public int getColumnCount() { 
        if (accessRule == null)
            return 0;
        else
            return acHeader.size(); 
    }
    
    public int getRowCount() { 
        if (accessRule == null)
            return 0;
        else
            return accessRule.size();
    }
    
    public Object getValueAt(int row, int col) {
        if((col < getColumnCount()) && (row < getRowCount())){
            return accessRule.get(row).getAccessControlAtIndex(col);
        } else
            return null;
    }
    
    public AccessControlHeader getColumns() {
        return acHeader;
    }
    
    public AccessControlHeaderItem getColumn(int index) {
        return acHeader.get(index);
    }

    @Override
    public String getColumnName(int col) {
        return acHeader.get(col).value();
    }
    
    @Override
    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }
    
    @Override
    public boolean isCellEditable(int row, int col) {
        return false;
    }
    
    @Override
    public void setValueAt(Object aValue, int row, int col) { 

    }
    
}
