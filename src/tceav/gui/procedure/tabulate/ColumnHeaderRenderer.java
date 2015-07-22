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
    private DefaultTableCellRenderer cell;
    
    public DefaultTableCellRenderer getCell() {
        return cell;
    }
    
    public Component getTableCellRendererComponent(JTable table,
            Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        
        
        //String entry = (String)value;
        
        TableCellRenderer temp = table.getTableHeader().getDefaultRenderer();
        cell = (DefaultTableCellRenderer)temp.getTableCellRendererComponent(table, null, isSelected, hasFocus, row, column);
        ColumnHeaderEntry che = ((DataModel)table.getModel()).getColumns().get(column);
        
        cell.setText(null);
        cell.setBorder(new CompoundBorder(
                cell.getBorder(),
                new EmptyBorder(GUIutilities.GAP_INSETS_HEADER)));
        
        cell.setVerticalAlignment(SwingConstants.BOTTOM);
        cell.setHorizontalAlignment(SwingConstants.CENTER);
        //cell.setToolTipText(entry);
        
        /*
        if(che.isHandler())
            cell.setFont(cell.getFont().deriveFont(Font.BOLD));
        else if(che.isRule())
            cell.setFont(cell.getFont().deriveFont(Font.ITALIC));
        else
            cell.setFont(cell.getFont().deriveFont(Font.PLAIN));
         
        cell.setIcon(new RotatedTextIcon(RotatedTextIcon.LEFT, cell.getFont(), entry));
         */
        Font font = cell.getFont();
        
        if(che.isRuleClassicifaction()){
            if(che.isRule())
                cell.setIcon(new RotatedTextIcon(
                        RotatedTextIcon.LEFT,
                        new Font[] {font.deriveFont(Font.ITALIC)},
                        che.toStringArray()));
            else if(che.isHandler())
                cell.setIcon(new RotatedTextIcon(
                        RotatedTextIcon.LEFT,
                        new Font[] {font.deriveFont(Font.ITALIC), font.deriveFont(Font.BOLD)},
                        che.toStringArray()));
            else if(che.isArgument())
                cell.setIcon(new RotatedTextIcon(
                        RotatedTextIcon.LEFT,
                        new Font[] {font.deriveFont(Font.ITALIC), font.deriveFont(Font.BOLD), font.deriveFont(Font.PLAIN)},
                        che.toStringArray()));
            else
                cell.setIcon(new RotatedTextIcon(
                        RotatedTextIcon.LEFT,
                        font.deriveFont(Font.PLAIN),
                        che.toString()));
            
        } else {
            if(che.isHandler())
                cell.setIcon(new RotatedTextIcon(
                        RotatedTextIcon.LEFT,
                        new Font[] {font.deriveFont(Font.BOLD)},
                        che.toStringArray()));
            else if(che.isArgument())
                cell.setIcon(new RotatedTextIcon(
                        RotatedTextIcon.LEFT,
                        new Font[] {font.deriveFont(Font.BOLD), font.deriveFont(Font.PLAIN)},
                        che.toStringArray()));
            else
                cell.setIcon(new RotatedTextIcon(
                        RotatedTextIcon.LEFT,
                        font.deriveFont(Font.PLAIN),
                        che.toString()));
        } 
        
        return cell;
    }
}
