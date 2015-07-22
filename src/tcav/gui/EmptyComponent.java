/*
 * EmptyComponent.java
 *
 * Created on 5 September 2007, 14:18
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tcav.gui;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.border.BevelBorder;
import javax.swing.ImageIcon;
import java.awt.Color;
import java.awt.BorderLayout;
import tcav.ResourceLocator;

/**
 *
 * @author nzr4dl
 */
public class EmptyComponent extends JPanel implements TabbedPanel  {
    
    /** Creates a new instance of EmptyComponent */
    public EmptyComponent() {
        ImageIcon iconBanner = new ImageIcon();
        try {
            iconBanner = new ImageIcon(ResourceLocator.getAppImage("logoBanner.gif"));
        } catch (Exception e) {
            System.out.println("Couldn't load images: " + e);
        }
        
        this.setLayout(new BorderLayout());
        this.add("Center", new JLabel(iconBanner));
        this.setBackground(Color.WHITE);
        this.setBorder(new BevelBorder(BevelBorder.LOWERED));
    }
    
    public boolean isEmptyPanel() {
        return true;
    }
    
}
