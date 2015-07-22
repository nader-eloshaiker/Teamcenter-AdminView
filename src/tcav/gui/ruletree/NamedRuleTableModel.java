/*
 * NamedRuleTableModel.java
 *
 * Created on 11 July 2007, 11:03
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tcav.gui.ruletree;

import tcav.ruletree.AccessRuleList;
import javax.swing.table.*;

/**
 *
 * @author nzr4dl
 */
public class NamedRuleTableModel extends AbstractTableModel implements TableModel {
    
    protected AccessRuleList accessRuleList;
    private final String[] columns = new String[]{"Type","Count","Named ACL"};
    
    protected final int TYPE_COLUMN = 0;
    protected final int INSTANCES_COLUMN = 1;
    protected final int NAME_COLUMN = 2;
    protected final int TOTAL_COLUMNS = 3;
    /**
     * Creates a new instance of NamedRuleTableModel
     */
    public NamedRuleTableModel(AccessRuleList accessRuleList) {
        this.accessRuleList = accessRuleList;
    }
    
    public int getColumnCount() {
        return 3;
    }
    
    public int getRowCount() {
        return accessRuleList.size();
    }
    
    public Object getValueAt(int row, int col) {
            switch(col) {
                case TYPE_COLUMN:
                    return getRuleType(row);
                case INSTANCES_COLUMN:
                    return getInstanceCount(row);
                case NAME_COLUMN:
                    return getRuleName(row);
                default:
                    return null;
            }
    }
    
    public String getRuleType(int row) {
        return accessRuleList.elementAt(row).getRuleType();
    }
    
    public int getInstanceCount(int row) {
        return accessRuleList.elementAt(row).getTreeIndexSize();
    }
    
    public String getRuleName(int row) {
        return accessRuleList.elementAt(row).getRuleName();
    }
    
    public String[] getColumns() {
        return columns;
    }
    
    public String getColumn(int col) {
        return columns[col];
    }
    
    public String getColumnName(int col) {
        return columns[col];
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
