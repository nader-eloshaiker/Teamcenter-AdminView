/*
 * AccessRuleTableHearderRenderer.java
 *
 * Created on 27 June 2007, 13:58
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.gm.tcae.ruletree.gui;

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
            yesIcon = new ImageIcon(RuleTreeNodeRenderer.class.getResource("images/acl/yes.gif"));
            noIcon = new ImageIcon(RuleTreeNodeRenderer.class.getResource("images/acl/no.gif"));
        } catch (Exception e) {
            System.out.println("Couldn't load images: " + e);
        }
    }
    /** Creates a new instance of AccessRuleTableHearderRenderer */
    public Component getTableCellRendererComponent(JTable table,
            Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        
        String s = (String)value;
        
        if(s.equals("Y")) {
            setHorizontalAlignment(CENTER);
            setIcon(yesIcon);
        } else if(s.equals("N")) {
            setHorizontalAlignment(CENTER);
            setIcon(noIcon);
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