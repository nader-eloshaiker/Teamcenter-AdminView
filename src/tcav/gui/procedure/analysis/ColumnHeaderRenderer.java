/*
 * ColumnHeaderRenderer.java
 *
 * Created on 10 May 2008, 08:38
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
 * @author NZR4DL
 */
public class ColumnHeaderRenderer implements TableCellRenderer {
    
    /** Creates a new instance of ColumnHeaderRenderer */
    public ColumnHeaderRenderer() {
    }
    
    public Component getTableCellRendererComponent(JTable table,
            Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        
        
        //ColumnHeaderEntry entry = (ColumnHeaderEntry)value;
        String entry = value.toString();
        
        TableCellRenderer temp = table.getTableHeader().getDefaultRenderer();
        DefaultTableCellRenderer cell = (DefaultTableCellRenderer)temp.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        
        if(column == 0) {
            cell.setText(entry);
            cell.setIcon(null);
        } else {
            cell.setText(null);

            if(!entry.startsWith(ColumnHeaderEntry.ARGUEMENT))
                cell.setFont(cell.getFont().deriveFont(Font.BOLD));
            else
                cell.setFont(cell.getFont().deriveFont(Font.PLAIN));
            
            cell.setIcon(new RotatedTextIcon(RotatedTextIcon.LEFT, cell.getFont(), entry));
        }
        cell.setVerticalAlignment(SwingConstants.BOTTOM);
        cell.setHorizontalAlignment(SwingConstants.CENTER);
        cell.setToolTipText(entry);
        
        return cell;
    }
}
