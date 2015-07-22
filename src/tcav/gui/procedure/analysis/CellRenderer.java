/*
 * CellRenderer.java
 *
 * Created on 11 May 2008, 13:28
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tcav.gui.procedure.analysis;

import tcav.resources.*;
import tcav.gui.TableShadedRenderer;
import javax.swing.table.*;
import javax.swing.*;
import java.awt.*;

/**
 *
 * @author NZR4DL
 */
public class CellRenderer extends TableShadedRenderer implements TableCellRenderer {
    
    static protected ImageIcon yesIcon;
    
    
    static
    {
        try {
            yesIcon = ResourceLoader.getImage(ImageEnum.amYes);
        } catch (Exception e) {
            System.out.println("Couldn't load images: " + e);
        }
    }
    
    public Component getTableCellRendererComponent(JTable table,
            Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        
        String s = value.toString();
        
        if(column == 0) {
            if(!s.startsWith(" "))
                this.setFont(this.getFont().deriveFont(Font.BOLD));
            setHorizontalAlignment(SwingConstants.LEFT);
            setVerticalAlignment(SwingConstants.BOTTOM);
            setIcon(null);
        } else {
            setValue(null);
            setHorizontalAlignment(SwingConstants.CENTER);
            setVerticalAlignment(SwingConstants.CENTER);
            if(s.equals("y"))
                setIcon(yesIcon);
            else
                setIcon(null);
        }
        
        return this;
    }
}
