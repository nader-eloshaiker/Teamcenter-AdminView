/*
 * AccessRuleTableHearderRenderer.java
 *
 * Created on 27 June 2007, 13:58
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tcav.gui.ruletree;

import tcav.ResourceLocator;
import javax.swing.table.*;
import javax.swing.*;
import java.awt.Component;

/**
 *
 * @author nzr4dl
 */
public class AccessRuleTableCellRenderer implements TableCellRenderer{
    
    static protected ImageIcon yesIcon;
    static protected ImageIcon noIcon;
    
    
    static
    {
        try {
            yesIcon = new ImageIcon(ResourceLocator.getRultreeImage("Yes.gif"));
            noIcon = new ImageIcon(ResourceLocator.getRultreeImage("No.gif"));
        } catch (Exception e) {
            System.out.println("Couldn't load images: " + e);
        }
    }
    /** Creates a new instance of AccessRuleTableHearderRenderer */
    public Component getTableCellRendererComponent(JTable table,
            Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        
        TableCellRenderer temp = table.getDefaultRenderer(String.class);
        DefaultTableCellRenderer cell = (DefaultTableCellRenderer)temp.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        String s = (String)value;
        
        if(column > 1) {
            cell.setHorizontalAlignment(SwingConstants.CENTER);
            if(s.equals("Y"))
                cell.setIcon(yesIcon);
            else if(s.equals("N"))
                cell.setIcon(noIcon);
            else
                cell.setIcon(null);
            cell.setText(null);
        } else {
            cell.setHorizontalAlignment(SwingConstants.LEFT);
            cell.setIcon(null);
            cell.setText(s);
        }
        
        return cell;
    }
    
}