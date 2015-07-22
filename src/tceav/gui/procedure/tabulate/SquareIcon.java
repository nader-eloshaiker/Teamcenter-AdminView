/*
 * SquareIcon.java
 *
 * Created on 10 May 2008, 09:14
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tceav.gui.procedure.tabulate;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import javax.swing.Icon;

/**
 *
 * @author NZR4DL
 */

public class SquareIcon implements Icon {

    private final int length = 10;
    private Color color;
    
    
    public SquareIcon(int[] color) {
        this.color = new Color(color[0], color[1], color[2]);
    }
    
    public int getIconWidth() {
        return length;
    }
    
    
    public int getIconHeight() {
        return length;
    }
    
    
    public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2d = (Graphics2D)g;
        Rectangle square = new Rectangle(x, y, length, length);

        g2d.setColor(color);
        g2d.fill(square);
        
    }
    
}

