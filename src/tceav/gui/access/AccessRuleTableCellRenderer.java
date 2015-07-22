/*
 * AccessRuleTableHearderRenderer.java
 *
 * Created on 27 June 2007, 13:58
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tceav.gui.access;

import javax.swing.table.*;
import javax.swing.*;
import java.awt.Component;
import tceav.manager.compare.CompareInterface;
import tceav.gui.tools.table.TableShadedRenderer;
import tceav.resources.*;

/**
 *
 * @author nzr4dl
 */
public class AccessRuleTableCellRenderer extends TableShadedRenderer implements TableCellRenderer {
    
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
    
    private boolean compareMode;
    
    public AccessRuleTableCellRenderer() {
        this(false);
        //System.out.println("Override Constructor Called");
    }
    
    public AccessRuleTableCellRenderer(boolean compareMode) {
        super();
        this.compareMode = compareMode;
    }
     
    /** Creates a new instance of AccessRuleTableHearderRenderer */
    public Component getTableCellRendererComponent(JTable table,
            Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        String s = (String)value;
        
        if(column > 1) {
            setHorizontalAlignment(SwingConstants.CENTER);
            setVerticalAlignment(SwingConstants.CENTER);
            if(s.equals("Y"))
                setIcon(yesIcon);
            else if(s.equals("N"))
                setIcon(noIcon);
            else
                setIcon(null);
            setValue(null);
        } else {
            setHorizontalAlignment(SwingConstants.LEFT);
            setIcon(null);
            setText(s);
        }
        
        if(!compareMode)
            return this;
        
        
        int result = ((AccessRuleTableModel)table.getModel()).getAccessRule().get(row).getComparison();
        switch(result) {
            case CompareInterface.NOT_EQUAL:
                if (isSelected) {
                    setForeground(CompareInterface.NOT_EQUAL_COLOR);
                    //setBackground(table.getSelectionBackground());
                } else {
                    setBackground(CompareInterface.NOT_EQUAL_COLOR);
                    setForeground(table.getForeground());
                }
                break;
            case CompareInterface.NOT_FOUND:
                if (isSelected) {
                    setForeground(CompareInterface.NOT_FOUND_COLOR);
                    //setBackground(table.getSelectionBackground());
                } else {
                    setBackground(CompareInterface.NOT_FOUND_COLOR);
                    setForeground(table.getForeground());
                }
                break;
            case CompareInterface.EQUAL:
            default:
                if (isSelected) {
                    setForeground(table.getSelectionForeground());
                    //setBackground(table.getSelectionBackground());
                } else {
                    setForeground(table.getForeground());
                    //setBackground(table.getBackground());
                }
                break;
        }

        return this;
    }
    
}