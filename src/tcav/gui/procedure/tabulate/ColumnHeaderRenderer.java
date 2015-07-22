/*
 * ColumnHeaderRenderer.java
 *
 * Created on 10 May 2008, 08:38
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tcav.gui.procedure.tabulate;

import java.awt.ComponentOrientation;
import java.awt.Component;
import java.awt.Font;
import javax.swing.JTable;
import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import java.awt.GridLayout;
import tcav.gui.GUIutilities;
import tcav.gui.RotatedTextIcon;
import javax.swing.SwingConstants;

/**
 *
 * @author NZR4DL
 */
public class ColumnHeaderRenderer implements TableCellRenderer {
    
   
    public Component getTableCellRendererComponent(JTable table,
            Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        
        
        //ColumnHeaderEntry entry = (ColumnHeaderEntry)value;
        String entry = (String)value;
        
        TableCellRenderer temp = table.getTableHeader().getDefaultRenderer();
        DefaultTableCellRenderer cell = (DefaultTableCellRenderer)temp.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        
        
        cell.setBorder(new CompoundBorder(
                cell.getBorder(),
                new EmptyBorder(GUIutilities.GAP_INSETS_HEADER)));
        
        cell.setVerticalAlignment(SwingConstants.BOTTOM);
        cell.setHorizontalAlignment(SwingConstants.CENTER);
        cell.setToolTipText(entry);

        if(!entry.startsWith(ColumnHeaderEntry.ARGUMENT_PREFIX))
            cell.setFont(cell.getFont().deriveFont(Font.BOLD));
        else
            cell.setFont(cell.getFont().deriveFont(Font.PLAIN));
        
        if(table.getColumnCount() > 1) {
            cell.setText(null);
            cell.setIcon(new RotatedTextIcon(RotatedTextIcon.LEFT, cell.getFont(), entry));
        } else {
            cell.setText(entry);
            cell.setIcon(null);
        }
        
        return cell;
    }
}
