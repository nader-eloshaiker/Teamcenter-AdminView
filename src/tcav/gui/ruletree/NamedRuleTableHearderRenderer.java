/*
 * NamedRuleTableHearderRenderer.java
 *
 * Created on 11 July 2007, 20:59
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tcav.gui.ruletree;

import javax.swing.table.*;
import javax.swing.*;
import java.awt.Component;
import java.awt.Insets;
import tcav.ResourceLocator;

/**
 *
 * @author NZR4DL
 */
public class NamedRuleTableHearderRenderer extends JButton implements TableCellRenderer{
    
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
        
        if(column == 0) {
            setIcon(iconType);
            setToolTipText(s);
        } else if(column == 1) {
            setIcon(iconCount);
            setToolTipText(s);
        } else
            setText(s);
        
        setMargin(new Insets(0, 0, 0, 0));
        

        return this;
    }
    
}
