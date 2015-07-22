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
import tcav.ResourceLocator;

/**
 *
 * @author NZR4DL
 */
public class NamedRuleTableCellRenderer extends DefaultTableCellRenderer implements TableCellRenderer{
    
    static protected ImageIcon typeRuletreeIcon;
    static protected ImageIcon typeWorkflowIcon;
    
    static
    {
        try {
            typeRuletreeIcon = new ImageIcon(ResourceLocator.getRultreeImage("NamedAclRuletree.gif"));
            typeWorkflowIcon = new ImageIcon(ResourceLocator.getRultreeImage("NamedAclWorkflow.gif"));
        } catch (Exception e) {
            System.out.println("Couldn't load images: " + e);
        }
    }
    
    /** Creates a new instance of AccessRuleTableHearderRenderer */
    public Component getTableCellRendererComponent(JTable table,
            Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        
        String s = value.toString();
        
        switch (column) {
            case 0:
                if(s.equals("WORKFLOW")) {
                    setHorizontalAlignment(CENTER);
                    setIcon(typeWorkflowIcon);
                    setToolTipText("Rule Type: WorkFlow");
                } else if(s.equals("RULETREE")) {
                    setHorizontalAlignment(CENTER);
                    setIcon(typeRuletreeIcon);
                    setToolTipText("Rule Type: Rultree");
                } break;
            case 1:
                setHorizontalAlignment(CENTER);
                setText(s);
                setToolTipText("Instances in RuleTree: "+s);
                break;
            default:
                setText(s);
        }
        
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
