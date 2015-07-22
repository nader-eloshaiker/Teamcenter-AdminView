/*
 * RowHeaderCellRenderer.java
 *
 * Created on 13 May 2008, 17:31
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tcav.gui.procedure.analysis;

import tcav.resources.*;
import tcav.gui.TableShadedRenderer;
import javax.swing.table.*;
import javax.swing.*;
import java.awt.*;

/**
 *
 * @author nzr4dl
 */
public class RowHeaderCellRenderer extends TableShadedRenderer implements TableCellRenderer {
    
    public RowHeaderCellRenderer getRenderer() {
        super.getTableCellRendererComponent(new JTable(), "", false, false, 0, 0);
        return this;
    }
    
    public Component getTableCellRendererComponent(JTable table,
            Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        
        String s = (String)value;
        
        if(!s.startsWith(" "))
            this.setFont(this.getFont().deriveFont(Font.BOLD));
        setHorizontalAlignment(SwingConstants.LEFT);
        setVerticalAlignment(SwingConstants.BOTTOM);
        
        return this;
    }
}
