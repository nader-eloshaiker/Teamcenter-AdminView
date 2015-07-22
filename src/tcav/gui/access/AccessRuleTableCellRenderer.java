/*
 * AccessRuleTableHearderRenderer.java
 *
 * Created on 27 June 2007, 13:58
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tcav.gui.access;

import tcav.resources.*;
import javax.swing.table.*;
import javax.swing.*;
import java.awt.Component;
import tcav.manager.compare.CompareInterface;

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
            yesIcon = ResourceLoader.getImage(ImageEnum.amYes);
            noIcon = ResourceLoader.getImage(ImageEnum.amNo);
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
            cell.setVerticalAlignment(SwingConstants.CENTER);
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
        
        int result = ((AccessRuleTableModel)table.getModel()).getAccessRule().get(row).getComparison();
        switch(result) {
            case CompareInterface.NOT_EQUAL:
                if (isSelected) {
                    cell.setForeground(CompareInterface.NOT_EQUAL_COLOR);
                    cell.setBackground(table.getSelectionBackground());
                } else {
                    cell.setBackground(CompareInterface.NOT_EQUAL_COLOR);
                    cell.setForeground(table.getForeground());
                }
                break;
            case CompareInterface.NOT_FOUND:
                if (isSelected) {
                    cell.setForeground(CompareInterface.NOT_FOUND_COLOR);
                    cell.setBackground(table.getSelectionBackground());
                } else {
                    cell.setBackground(CompareInterface.NOT_FOUND_COLOR);
                    cell.setForeground(table.getForeground());
                }
                break;
            case CompareInterface.EQUAL:
            default:
                if (isSelected) {
                    cell.setForeground(table.getSelectionForeground());
                    cell.setBackground(table.getSelectionBackground());
                } else {
                    cell.setForeground(table.getForeground());
                    cell.setBackground(table.getBackground());
                }
                break;
        }

        return cell;
    }
    
}