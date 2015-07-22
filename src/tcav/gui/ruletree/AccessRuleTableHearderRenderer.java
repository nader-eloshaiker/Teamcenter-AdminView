/*
 * AccessRuleTableHearderRenderer.java
 *
 * Created on 27 June 2007, 13:58
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tcav.gui.ruletree;

import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.border.EmptyBorder;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.JLabel;
import java.awt.Component;
import tcav.ResourceLocator;
import tcav.ruletree.AccessControlColumnsEntry;

/**
 *
 * @author nzr4dl
 */
public class AccessRuleTableHearderRenderer implements TableCellRenderer{

    ImageIcon icon;
    /** Creates a new instance of AccessRuleTableHearderRenderer */
    public Component getTableCellRendererComponent(JTable table,
            Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        
        AccessControlColumnsEntry accEntry = (AccessControlColumnsEntry)value;
        JTableHeader header = table.getTableHeader();
        TableCellRenderer temp = header.getDefaultRenderer();
        DefaultTableCellRenderer cell = (DefaultTableCellRenderer)temp.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        
      	try {
            icon = new ImageIcon(ResourceLocator.getRultreeColumnImage(accEntry.getIconName()));
	} catch (Exception e) {
	    System.out.println("Couldn't load images: " + e);
	}
        
        Border border = cell.getBorder();
        cell.setBorder(new CompoundBorder(border, new EmptyBorder(1,1,1,1)));
        
        cell.setIcon(icon);
        cell.setText(null);
        
        cell.setToolTipText(accEntry.getDescription());
        cell.setHorizontalAlignment(SwingConstants.CENTER);

        return cell;
    }
    
}