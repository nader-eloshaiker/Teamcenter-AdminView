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
public class AccessRuleTableCellRenderer extends DefaultTableCellRenderer implements TableCellRenderer{
    
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
        
        String s = (String)value;
        
        if(column > 1) {
            setHorizontalAlignment(CENTER);
            if(s.equals("Y"))
                setIcon(yesIcon);
            else if(s.equals("N"))
                setIcon(noIcon);
            else
                setIcon(null);
        } else
            setText(s);
        
        if (isSelected) {
            setForeground(table.getSelectionForeground());
            super.setBackground(table.getSelectionBackground());
        } else {
            setForeground(table.getForeground());
            setBackground(table.getBackground());
        }
        
        return this;
    }
    
}