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
import javax.swing.table.JTableHeader;
import javax.swing.border.EmptyBorder;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.UIManager;
import java.awt.Component;
import tcav.ResourceLocator;
import tcav.ResourceLocator;
import tcav.ruletree.AccessControlColumnsEntry;

/**
 *
 * @author nzr4dl
 */
public class AccessRuleTableHearderRenderer extends DefaultTableCellRenderer{

    ImageIcon icon;
    /** Creates a new instance of AccessRuleTableHearderRenderer */
    public Component getTableCellRendererComponent(JTable table,
            Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        
        AccessControlColumnsEntry accEntry = (AccessControlColumnsEntry)value;
        JTableHeader header = table.getTableHeader();
        
        if (header != null) {
            setForeground(header.getForeground());
            setBackground(header.getBackground());
            setFont(header.getFont());
        }
        setBorder(new CompoundBorder(
                UIManager.getBorder("TableHeader.cellBorder"), 
                new EmptyBorder(1,1,1,1)));

      	try {
            icon = new ImageIcon(ResourceLocator.getRultreeColumnImage(accEntry.getIconName()));
	} catch (Exception e) {
	    System.out.println("Couldn't load images: " + e);
	}
        
        setIcon(icon);
        setToolTipText(accEntry.getDescription());
        setHorizontalAlignment(SwingConstants.CENTER);

        return this;
    }
    
}