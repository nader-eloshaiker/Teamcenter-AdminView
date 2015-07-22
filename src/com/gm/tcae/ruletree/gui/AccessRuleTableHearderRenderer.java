/*
 * AccessRuleTableHearderRenderer.java
 *
 * Created on 27 June 2007, 13:58
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.gm.tcae.ruletree.gui;

import javax.swing.table.TableCellRenderer;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.ImageIcon;
import java.awt.Component;
import java.awt.Insets;
import com.gm.tcae.ruletree.acl.AccessControlColumnsEntry;

/**
 *
 * @author nzr4dl
 */
public class AccessRuleTableHearderRenderer extends JButton implements TableCellRenderer{

    ImageIcon icon;
    /** Creates a new instance of AccessRuleTableHearderRenderer */
    public Component getTableCellRendererComponent(JTable table,
            Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        
        AccessControlColumnsEntry accEntry = (AccessControlColumnsEntry)value;

      	try {
            icon = new ImageIcon(AccessRuleTableHearderRenderer.class.getResource("images/acl-columns/"+accEntry.getIconName()));
	} catch (Exception e) {
	    System.out.println("Couldn't load images: " + e);
	}
        
        super.setIcon(icon);
        super.setMargin(new Insets(0, 0, 0, 0));
        super.setToolTipText(accEntry.getDescription());

        return this;
    }
    
}