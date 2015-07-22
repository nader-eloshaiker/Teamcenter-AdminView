/*
 * NamedRuleTableHearderRenderer.java
 *
 * Created on 11 July 2007, 20:59
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tcav.gui.ruletree;

import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import java.awt.Component;
import tcav.ResourceLocator;

/**
 *
 * @author NZR4DL
 */
public class NamedRuleTableHearderRenderer implements TableCellRenderer {
    
    static protected ImageIcon iconType;
    static protected ImageIcon iconCount;
    
    static
    {
        try {
            iconType = new ImageIcon(ResourceLocator.getRultreeImage("NamedAclType.gif"));
            iconCount = new ImageIcon(ResourceLocator.getRultreeImage("NamedAclCount.gif"));
        } catch (Exception e) {
            System.out.println("Couldn't load images: " + e);
        }
    }
    
    /** Creates a new instance of AccessRuleTableHearderRenderer */
    public Component getTableCellRendererComponent(JTable table,
            Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        
        String s = (String)value;
        JTableHeader header = table.getTableHeader();
        TableCellRenderer temp = header.getDefaultRenderer();
        DefaultTableCellRenderer cell = (DefaultTableCellRenderer)temp.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        
        Border border = cell.getBorder();
        cell.setBorder(new CompoundBorder(border, new EmptyBorder(1,1,1,1)));
        
        if(column == 0) {
            cell.setText(null);
            cell.setIcon(iconType);
            cell.setToolTipText(s);
            cell.setHorizontalAlignment(SwingConstants.CENTER);
        } else if(column == 1) {
            cell.setText(null);
            cell.setIcon(iconCount);
            cell.setToolTipText(s);
            cell.setHorizontalAlignment(SwingConstants.CENTER);
        } else {
            cell.setText(s);
            cell.setIcon(null);
            cell.setToolTipText(s);
            cell.setHorizontalAlignment(SwingConstants.LEFT);
        }
        return cell;
    }
    
}
