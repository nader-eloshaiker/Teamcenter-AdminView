/*
 * RowHeaderRenderer.java
 *
 * Created on 13 May 2008, 17:30
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tcav.gui.procedure.tabulate;

import tcav.gui.TableShadedRenderer;
import javax.swing.table.*;
import javax.swing.*;
import java.awt.*;
/**
 *
 * @author nzr4dl
 */
public class RowHeaderRenderer extends SyncedRenderer implements TableCellRenderer {
    
    public RowHeaderRenderer(JTable oppositeTable) {
        super(oppositeTable);
    }
    
    public Component getTableCellRendererComponent(JTable table,
            Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        
        String s = (String)value;
        
        setHorizontalAlignment(SwingConstants.LEFT);
        setVerticalAlignment(SwingConstants.CENTER);
        
        if(!s.startsWith(ColumnHeaderEntry.ARGUMENT_PREFIX))
            setFont(getFont().deriveFont(Font.BOLD));
        else
            setFont(getFont().deriveFont(Font.PLAIN));
        
        return this;
    }
    
}
