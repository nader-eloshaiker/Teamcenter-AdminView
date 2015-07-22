/*
 * SyncedRenderer.java
 *
 * Created on 19 May 2008, 15:12
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tceav.gui.procedure.tabulate;

import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author nzr4dl
 */
public class SyncedRenderer extends TableShadedWorkflowRenderer implements TableCellRenderer {
    
    private JTable oppositeTable;
    
    public SyncedRenderer(JTable oppositeTable) {
        super();
        this.oppositeTable = oppositeTable;
    }
    
    public Component getTableCellRendererComponent(JTable table,
            Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        
        //setSelection(table, row, isSelected, hasFocus);

        super.getTableCellRendererComponent(table, value, isSelected(row, isSelected, hasFocus), hasFocus, row, column);
        
        return this;
    }
    
    private void setSelection(JTable table, int row, boolean isSelected, boolean hasFocus) {
        if(hasFocus && isSelected)
            oppositeTable.clearSelection();
        else if(isSelected(row, oppositeTable.getSelectedRows()) && !isSelected) {
            table.getSelectionModel().setSelectionInterval(row, row);
            System.out.println(row);
        }
    }
    
    private boolean isSelected(int row, boolean isSelected, boolean hasFocus) {
        if(hasFocus && isSelected) {
            oppositeTable.clearSelection();
            return true;
        }
        
        return (isSelected || isSelected(row, oppositeTable.getSelectedRows()));
    }
    
    private boolean isSelected(int row, int[] selection) {
        for(int i=0; i<selection.length; i++)
            if(selection[i] == row)
                return true;
        
        return false;
    }
}
