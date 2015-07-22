/*
 * NamedRuleDataAbstractModel.java
 *
 * Created on 18 March 2008, 14:23
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tceav.gui.access;

import javax.swing.table.*;

/**
 *
 * @author nzr4dl
 */
public abstract class NamedRuleDataAdapterModel extends AbstractTableModel {
    
    private final String[] columns = new String[]{"Type","Count","Named ACL"};
    private final String[] columnsCompare = new String[]{"Type","Count","Named ACL","Compare"};
    private boolean compareMode = false;
    
    
    public boolean isCompare() {
        return compareMode;
    }
    
    public void setCompareMode(boolean compareMode) {
        this.compareMode = compareMode;
    }
    
    public int getColumnCount() {
        return getColumns().length;
    }
    
    public String[] getColumns() {
        if(isCompare())
            return columnsCompare;
        else
            return columns;
    }
    
    public String getColumn(int col) {
        return getColumns()[col];
    }
    
    public String getColumnName(int col) {
        return getColumn(col);
    }
    
    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }
    
    
    
}
