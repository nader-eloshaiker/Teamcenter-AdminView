/*
 * NamedRuleTableCellRenderer.java
 *
 * Created on 11 July 2007, 20:33
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tceav.gui.access;

import javax.swing.table.*;
import javax.swing.*;
import java.awt.*;
import tceav.manager.compare.CompareInterface;
import tceav.gui.tools.table.TableShadedRenderer;
import tceav.resources.*;

/**
 *
 * @author NZR4DL
 */
public class NamedRuleTableCellRenderer extends TableShadedRenderer implements TableCellRenderer {
    
    static protected ImageIcon typeRuletreeIcon;
    static protected ImageIcon typeWorkflowIcon;
    static protected ImageIcon iconEqual;
    static protected ImageIcon iconNotEqual;
    static protected ImageIcon iconNotFound;
    
    static
    {
        try {
            typeRuletreeIcon = ResourceLoader.getImage(ImageEnum.amNamedAclType);
            typeWorkflowIcon = ResourceLoader.getImage(ImageEnum.amWorkflowType);
            iconEqual = ResourceLoader.getImage(ImageEnum.amcmpEqual);
            iconNotEqual = ResourceLoader.getImage(ImageEnum.amcmpNotEqual);
            iconNotFound = ResourceLoader.getImage(ImageEnum.amcmpNotFound);
        } catch (Exception e) {
            System.out.println("Couldn't load images: " + e);
        }
    }
    
    private boolean compareMode;
    
    
    public NamedRuleTableCellRenderer() {
        this(false);
    }
    
    public NamedRuleTableCellRenderer(boolean compareMode) {
        super();
        this.compareMode = compareMode;
    }

    /* Creates a new instance of AccessRuleTableHearderRenderer */
    public Component getTableCellRendererComponent(JTable table,
            Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        
        String s = value.toString();
        
        switch (column) {
            case NamedRuleDataFilterInterface.TYPE_COLUMN:
                if(s.equals("WORKFLOW"))
                    setIcon(typeWorkflowIcon);
                else if(s.equals("RULETREE"))
                    setIcon(typeRuletreeIcon);
                setToolTipText("Rule Type: "+s);
                setValue(null);
                setHorizontalAlignment(SwingConstants.CENTER);
                break;
            case NamedRuleDataFilterInterface.INSTANCES_COLUMN:
                setIcon(null);
                setHorizontalAlignment(SwingConstants.CENTER);
                //setValue(s);
                setToolTipText("Instances in RuleTree: "+s);
                break;
            case NamedRuleDataFilterInterface.COMPARE_COLUMN:
                if(s.equals(CompareInterface.NOT_EQUAL_STRING)) {
                    setIcon(iconNotEqual);
                    setToolTipText(CompareInterface.NOT_EQUAL_LABEL);
                } else if(s.equals(CompareInterface.NOT_FOUND_STRING)) {
                    setIcon(iconNotFound);
                    setToolTipText(CompareInterface.NOT_FOUND_LABEL);
                } else if(s.equals(CompareInterface.EQUAL_STRING)) {
                    setIcon(null);
                    setToolTipText(CompareInterface.EQUAL_LABEL);
                }
                setValue(null);
                setHorizontalAlignment(SwingConstants.CENTER);
                break;
            default:
                setHorizontalAlignment(SwingConstants.LEFT);
                setIcon(null);
                //setValue(s);
                setToolTipText(s);
                break;
        }
        
        return this;
    }
}
