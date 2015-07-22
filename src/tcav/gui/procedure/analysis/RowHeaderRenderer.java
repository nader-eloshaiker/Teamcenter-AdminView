/*
 * RowHeaderRenderer.java
 *
 * Created on 13 May 2008, 17:30
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tcav.gui.procedure.analysis;

import java.awt.ComponentOrientation;
import java.awt.Component;
import java.awt.Font;
import javax.swing.JTable;
import javax.swing.JLabel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import java.awt.GridLayout;
import tcav.gui.RotatedTextIcon;
import javax.swing.SwingConstants;

/**
 *
 * @author nzr4dl
 */
public class RowHeaderRenderer implements TableCellRenderer {
    
    public static DefaultTableCellRenderer getRenderer() {
        JTable table = new JTable();
        TableCellRenderer temp = table.getTableHeader().getDefaultRenderer();
        DefaultTableCellRenderer cell = (DefaultTableCellRenderer)temp.getTableCellRendererComponent(table, "", false, false, 0, 0);
        return cell;
    }
    
    public Component getTableCellRendererComponent(JTable table,
            Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        
        
        //ColumnHeaderEntry entry = (ColumnHeaderEntry)value;
        String entry = (String)value;
        
        TableCellRenderer temp = table.getTableHeader().getDefaultRenderer();
        DefaultTableCellRenderer cell = (DefaultTableCellRenderer)temp.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        
        if(!entry.startsWith(" "))
            cell.setFont(cell.getFont().deriveFont(Font.BOLD));
        else
            cell.setFont(cell.getFont().deriveFont(Font.PLAIN));
        
        cell.setText(entry);
        cell.setVerticalAlignment(SwingConstants.BOTTOM);
        cell.setToolTipText(entry);
        
        return cell;
    }
}
