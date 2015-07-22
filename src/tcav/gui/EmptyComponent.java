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
import java.awt.FlowLayout;
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
        if(statusBar != null)
            return statusBar;

        JLabel textVersion = new JLabel(" "+ResourceStrings.getVersion()+" ");
        textVersion.setBorder(new BevelBorder(BevelBorder.LOWERED));
        JPanel panelVersion = new JPanel();
        panelVersion.setLayout(new BorderLayout());
        panelVersion.add(new JLabel(" Version:"), BorderLayout.WEST);
        panelVersion.add(textVersion, BorderLayout.CENTER);

        JLabel textBuild = new JLabel(" "+ResourceStrings.getBuild()+" ");
        textBuild.setBorder(new BevelBorder(BevelBorder.LOWERED));
        JPanel panelBuild = new JPanel();
        panelBuild.setLayout(new BorderLayout());
        panelBuild.add(new JLabel("  Build:"), BorderLayout.WEST);
        panelBuild.add(textBuild, BorderLayout.CENTER);

        JLabel text = new JLabel(ResourceStrings.getApplicationName()+
                " version: "+ResourceStrings.getVersion()+
                " Build: "+ ResourceStrings.getBuild());
        text.setBorder(new BevelBorder(BevelBorder.LOWERED));;
        
        statusBar = new JPanel();
        statusBar.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        statusBar.add(panelVersion);
        statusBar.add(panelBuild);
        
        return statusBar;
    }
    
    JPanel toolBar;
    
    public JComponent getToolBar() {
        if(toolBar != null)
            return toolBar;
        
        toolBar = new JPanel();
        return toolBar;
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
