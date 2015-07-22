/*
 * EmptyComponent.java
 *
 * Created on 5 September 2007, 14:18
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tceav.gui;

import javax.swing.border.BevelBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import tceav.resources.ImageEnum;
import tceav.resources.ResourceLoader;
import tceav.resources.ResourceStrings;

/**
 *
 * @author nzr4dl
 */
public class EmptyComponent extends TabbedPanel {
    
    
    /** Creates a new instance of EmptyComponent */
    public EmptyComponent() {
        ImageIcon iconBanner = new ImageIcon();
        try {
            iconBanner = ResourceLoader.getImage(ImageEnum.appLogoBanner);
        } catch (Exception e) {
            System.out.println("Couldn't load images: " + e);
        }
        JLabel label = new JLabel(iconBanner, JLabel.CENTER);
        this.setLayout(new BorderLayout());
        this.add(label, BorderLayout.CENTER);
        this.setBackground(Color.WHITE);
        this.setBorder(new BevelBorder(BevelBorder.LOWERED));
    }
    
    private JPanel statusBar;
    
    public JComponent getStatusBar() {
        if(statusBar != null)
            return statusBar;

        JLabel textVersion = new JLabel(" "+ResourceStrings.getVersion()+" ");
        textVersion.setBorder(new BevelBorder(BevelBorder.LOWERED));

        JLabel textBuild = new JLabel(" "+ResourceStrings.getBuild()+" ");
        textBuild.setBorder(new BevelBorder(BevelBorder.LOWERED));
        JPanel panelBuild = new JPanel();

        JLabel text = new JLabel("   Supporting the TeamCenter Community");
        
        statusBar = new JPanel();
        statusBar.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        statusBar.add(new JLabel(" Version:"));
        statusBar.add(textVersion);
        statusBar.add(new JLabel("   Build:"));
        statusBar.add(textBuild);
        statusBar.add(text);
        
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
    
    public String getTitle() {
        return "empty";
    }
    
}
