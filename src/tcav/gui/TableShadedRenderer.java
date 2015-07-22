/*
 * TableShadedRenderer.java
 *
 * Created on 17 April 2008, 11:52
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tcav.gui;

import javax.swing.table.*;
import javax.swing.*;
import java.awt.Color;
import java.awt.Component;
/**
 *
 * @author nzr4dl
 */
public class TableShadedRenderer extends DefaultTableCellRenderer  implements TableCellRenderer {
    
    /** Creates a new instance of TableShadedRenderer */
    public TableShadedRenderer() {
        super();
    }
    
    private final int FACTOR = 8;
    
    private Color darker(Color c) {
        return new Color(
                Math.max((c.getRed() - FACTOR), 0),
                Math.max((c.getGreen() - FACTOR), 0),
                Math.max((c.getBlue() - FACTOR), 0));
    }
    
    private Color brighter(Color c) {
        return new Color(Math.min((c.getRed() + FACTOR), 255),
                         Math.min((c.getGreen() + FACTOR), 255),
                         Math.min((c.getBlue() + FACTOR), 255));
    }
    
    private void setBackgroundShading(JTable table, int row, boolean isSelected) {
        Color shaded;
        Color normal = table.getBackground();
        
        if(normal.getBlue()+normal.getRed()+normal.getGreen() > 383)
            shaded = darker(normal);
        else
            shaded = brighter(normal);
        
        
        if(isSelected) {
            setBackground(table.getSelectionBackground());
        } else {
            if(row % 2 == 0)
                setBackground(shaded);
            else
                setBackground(normal);
        }
        
    }

    public Component getTableCellRendererComponent(JTable table,
            Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        
        setBackgroundShading(table, row, isSelected);
        
        return this;
    }
}
