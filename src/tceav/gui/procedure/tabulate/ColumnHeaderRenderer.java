/*
 * ColumnHeaderRenderer.java
 *
 * Created on 10 May 2008, 08:38
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tceav.gui.procedure.tabulate;

import java.awt.Component;
import java.awt.Font;
import javax.swing.JTable;
import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import tceav.gui.GUIutilities;
import tceav.gui.RotatedTextIcon;
import javax.swing.SwingConstants;

/**
 *
 * @author NZR4DL
 */
public class ColumnHeaderRenderer implements TableCellRenderer {
    
    
    public Component getTableCellRendererComponent(JTable table,
            Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        
        
        String entry = (String)value;
        
        TableCellRenderer temp = table.getTableHeader().getDefaultRenderer();
        DefaultTableCellRenderer cell = (DefaultTableCellRenderer)temp.getTableCellRendererComponent(table, null, isSelected, hasFocus, row, column);
        ColumnHeaderEntry che = ((DataModel)table.getModel()).getColumns().get(column);
        
        cell.setText(null);
        cell.setBorder(new CompoundBorder(
                cell.getBorder(),
                new EmptyBorder(GUIutilities.GAP_INSETS_HEADER)));
        
        cell.setVerticalAlignment(SwingConstants.BOTTOM);
        cell.setHorizontalAlignment(SwingConstants.CENTER);
        cell.setToolTipText(entry);
        
        if(che.isHandler())
            cell.setFont(cell.getFont().deriveFont(Font.BOLD));
        else if(che.isBusinessRule())
            cell.setFont(cell.getFont().deriveFont(Font.ITALIC));
        else
            cell.setFont(cell.getFont().deriveFont(Font.PLAIN));
        
        cell.setIcon(new RotatedTextIcon(RotatedTextIcon.LEFT, cell.getFont(), entry));
        
        return cell;
    }
}
