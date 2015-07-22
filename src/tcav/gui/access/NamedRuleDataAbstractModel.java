/*
 * NamedRuleDataAbstractModel.java
 *
 * Created on 18 March 2008, 14:23
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tcav.gui.access;

import javax.swing.table.*;

/**
 *
 * @author nzr4dl
 */
public abstract class NamedRuleDataAbstractModel extends AbstractTableModel {
    
    private final String[] columns = new String[]{"Type","Count","Named ACL"};
    
    
    public int getColumnCount() {
        return 3;
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
    

    
}
