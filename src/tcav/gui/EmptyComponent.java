/*
 * EmptyComponent.java
 *
 * Created on 5 September 2007, 14:18
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tcav.gui;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.Color;
import java.awt.BorderLayout;
import java.io.File;

import tcav.resources.*;
import tcav.manager.AbstractManager;
import tcav.manager.empty.EmptyManager;

/**
 *
 * @author nzr4dl
 */
public class EmptyComponent extends TabbedPanel {
    
    private EmptyManager manager;

    
    /** Creates a new instance of EmptyComponent */
    public EmptyComponent() {
        manager = new EmptyManager();
        ImageIcon iconBanner = new ImageIcon();
        try {
            iconBanner = ResourceLoader.getImage(ImageEnum.appLogoBanner);
        } catch (Exception e) {
            System.out.println("Couldn't load images: " + e);
        }
        JLabel label = new JLabel(iconBanner);
        //label.setEnabled(false);
        this.setLayout(new BorderLayout());
        this.add("East", label);
        //this.setBackground(Color.WHITE);
        this.setBorder(new BevelBorder(BevelBorder.LOWERED));
    }
    
    private JPanel statusBar;
    
    public JComponent getStatusBar() {
        if(statusBar == null) {
            JLabel text = new JLabel(ResourceStrings.getApplicationName()+
                    " version: "+ResourceStrings.getVersion()+
                    " Build: "+ ResourceStrings.getBuild());
            text.setBorder(new BevelBorder(BevelBorder.LOWERED));;
            
            statusBar = new JPanel();
            statusBar.setLayout(new BorderLayout());
            statusBar.add("Center", text);
        }
        return statusBar;
    }
    
    private ImageIcon iconApp;
    
    public ImageIcon getIcon() {
        if(iconApp == null){
            try {
                iconApp = ResourceLoader.getImage(ImageEnum.appLogo);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error Load Images", JOptionPane.ERROR_MESSAGE);
            }
        }
        return iconApp;
        
    }
    
    public AbstractManager getManager() {
        return manager;
    }
}
