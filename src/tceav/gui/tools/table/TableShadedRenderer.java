/*
 * TableshadedColorRenderer.java
 *
 * Created on 17 April 2008, 11:52
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tceav.gui.tools.table;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
/**
 *
 * @author nzr4dl
 */
public class TableShadedRenderer extends DefaultTableCellRenderer  implements TableCellRenderer {
    
    private final int SHADE_LEVEL = 8;
    
    /** Creates a new instance of TableShadedRenderer */
    public TableShadedRenderer() {
        super();
    }
    
    private Color darker(Color c, int factor) {
        return new Color(
                Math.max((c.getRed() - (SHADE_LEVEL*factor)), 0),
                Math.max((c.getGreen() - (SHADE_LEVEL*factor)), 0),
                Math.max((c.getBlue() - (SHADE_LEVEL*factor)), 0));
    }
    
    private Color brighter(Color c, int factor) {
        return new Color(Math.min((c.getRed() + (SHADE_LEVEL*factor)), 255),
                         Math.min((c.getGreen() + (SHADE_LEVEL*factor)), 255),
                         Math.min((c.getBlue() + (SHADE_LEVEL*factor)), 255));
    }
    
    private void setBackgroundShading(JTable table, int row, boolean isSelected) {
        Color normalColor = table.getBackground();
        Color shadedColor = getShadedColor(normalColor);

        if(isSelected) {
            setBackground(table.getSelectionBackground());
        } else {
            if(row % 2 == 0)
                setBackground(shadedColor);
            else
                setBackground(normalColor);
        }
        
    }
    
    public Color getShadedColor(Color baseline) {
        return getShadedColor(baseline, 1);
    }
    
    public Color getShadedColor(Color baseline, int factor) {
        Color c;
        
        if(baseline.getBlue()+baseline.getRed()+baseline.getGreen() > 383)
            c = darker(baseline, factor);
        else
            c = brighter(baseline, factor);
        
        return c;
    }

    public Component getTableCellRendererComponent(JTable table,
            Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        
        setBackgroundShading(table, row, isSelected);
        
        return this;
    }
}
