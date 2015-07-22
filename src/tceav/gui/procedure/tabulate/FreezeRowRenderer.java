/*
 * RowOneRenderer.java
 *
 * Created on 13 May 2008, 17:30
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tceav.gui.procedure.tabulate;

import tceav.gui.TableShadedRenderer;
import javax.swing.table.*;
import javax.swing.*;
import java.awt.*;
/**
 *
 * @author nzr4dl
 */
public class FreezeRowRenderer extends SyncedRenderer implements TableCellRenderer {
    
    public FreezeRowRenderer(JTable oppositeTable) {
        super(oppositeTable);
    }
    
    public Component getTableCellRendererComponent(JTable table,
            Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        
        String s = (String)value;
        
        setHorizontalAlignment(SwingConstants.LEFT);
        setVerticalAlignment(SwingConstants.CENTER);
        
        FreezeRowTableData model = (FreezeRowTableData)table.getModel();
        
        if(model.isRootNode(row))
            setFont(getFont().deriveFont(Font.BOLD));
        else
            setFont(getFont().deriveFont(Font.PLAIN));
        
        return this;
    }
    
}
