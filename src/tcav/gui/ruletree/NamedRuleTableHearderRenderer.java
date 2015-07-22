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
import javax.swing.table.JTableHeader;
import javax.swing.border.EmptyBorder;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.UIManager;
import java.awt.Component;
import tcav.ResourceLocator;

/**
 *
 * @author NZR4DL
 */
public class NamedRuleTableHearderRenderer extends DefaultTableCellRenderer {
    
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
        
        if (header != null) {
            setForeground(header.getForeground());
            setBackground(header.getBackground());
            setFont(header.getFont());
        }
        setBorder(new CompoundBorder(
                UIManager.getBorder("TableHeader.cellBorder"), 
                new EmptyBorder(1,1,1,1)));
        
        if(column == 0) {
            setIcon(iconType);
            setToolTipText(s);
            setHorizontalAlignment(SwingConstants.CENTER);
        } else if(column == 1) {
            setIcon(iconCount);
            setToolTipText(s);
            setHorizontalAlignment(SwingConstants.CENTER);
        } else
            setText(s);
        
        return this;
    }
    
}
