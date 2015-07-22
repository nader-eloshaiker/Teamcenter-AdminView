/*
 * CellRenderer.java
 *
 * Created on 11 May 2008, 13:28
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tceav.gui.procedure.tabulate;

import tceav.gui.TableShadedRenderer;
import javax.swing.table.*;
import javax.swing.*;
import java.awt.*;
import tceav.resources.*;

/**
 *
 * @author NZR4DL
 */
public class CellRenderer extends SyncedRenderer implements TableCellRenderer {
    
    static protected ImageIcon yesIcon;
    
    
    static
    {
        try {
            yesIcon = ResourceLoader.getImage(ImageEnum.amYes);
        } catch (Exception e) {
            System.out.println("Couldn't load images: " + e);
        }
    }
    
    public CellRenderer(JTable oppositeTable) {
        super(oppositeTable);
    }
    
    
    public Component getTableCellRendererComponent(JTable table,
            Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        
        String s = (String)value;
        
        
        setHorizontalAlignment(SwingConstants.CENTER);
        setVerticalAlignment(SwingConstants.CENTER);
        
        if(s.equals("y")) {
            setValue(null);
            setIcon(yesIcon);
        } else {
            setIcon(null);
            setValue(null);
        }
        
        return this;
    }
    
}
