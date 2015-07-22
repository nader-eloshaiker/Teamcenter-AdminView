/*
 * RowOneHeaderRenderer.java
 *
 * Created on 25 May 2008, 11:10
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tceav.gui.procedure.tabulate;

/**
 *
 * @author NZR4DL
 */
import java.awt.Component;
import java.awt.Font;
import javax.swing.JTable;
import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import tceav.gui.tools.GUIutilities;
import tceav.gui.tools.RotatedTextIcon;
import javax.swing.SwingConstants;

/**
 *
 * @author NZR4DL
 */
public class FreezeRowHeaderRenderer implements TableCellRenderer {
    
    
    public Component getTableCellRendererComponent(JTable table,
            Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        
        
        TableCellRenderer temp = table.getTableHeader().getDefaultRenderer();
        DefaultTableCellRenderer cell = (DefaultTableCellRenderer)temp.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        
        cell.setBorder(new CompoundBorder(
                cell.getBorder(),
                new EmptyBorder(GUIutilities.GAP_INSETS_HEADER)));
        
        cell.setVerticalAlignment(SwingConstants.BOTTOM);
        cell.setHorizontalAlignment(SwingConstants.CENTER);
        cell.setToolTipText((String)value);
        cell.setFont(cell.getFont().deriveFont(Font.BOLD));
        
        return cell;
    }
}
