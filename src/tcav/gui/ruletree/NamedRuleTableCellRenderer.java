/*
 * NamedRuleTableCellRenderer.java
 *
 * Created on 11 July 2007, 20:33
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tcav.gui.ruletree;

import javax.swing.table.*;
import javax.swing.*;
import java.awt.*;
import tcav.resources.*;

/**
 *
 * @author NZR4DL
 */
public class NamedRuleTableCellRenderer implements TableCellRenderer{
    
    static protected ImageIcon typeRuletreeIcon;
    static protected ImageIcon typeWorkflowIcon;
    
    static
    {
        try {
            typeRuletreeIcon = ResourceLoader.getImage(ImageEnum.amNamedAclType);
            typeWorkflowIcon = ResourceLoader.getImage(ImageEnum.amWorkflowType);
        } catch (Exception e) {
            System.out.println("Couldn't load images: " + e);
        }
    }
    
    /** Creates a new instance of AccessRuleTableHearderRenderer */
    public Component getTableCellRendererComponent(JTable table,
            Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        
        TableCellRenderer temp = table.getDefaultRenderer(String.class);
        DefaultTableCellRenderer cell = (DefaultTableCellRenderer)temp.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        String s = value.toString();
        
        switch (column) {
            case 0:
                if(s.equals("WORKFLOW"))
                    cell.setIcon(typeWorkflowIcon);
                else if(s.equals("RULETREE"))
                    cell.setIcon(typeRuletreeIcon);
                cell.setToolTipText("Rule Type: "+s);
                cell.setText(null);
                cell.setHorizontalAlignment(SwingConstants.CENTER);
                break;
            case 1:
                cell.setIcon(null);
                cell.setHorizontalAlignment(SwingConstants.CENTER);
                cell.setText(s);
                cell.setToolTipText("Instances in RuleTree: "+s);
                break;
            default:
                cell.setHorizontalAlignment(SwingConstants.LEFT);
                cell.setIcon(null);
                cell.setText(s);
                cell.setToolTipText(s);
                break;
        }
        
        return cell;
    }
}
