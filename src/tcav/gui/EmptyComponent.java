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
import tcav.resources.*;
import java.io.File;

/**
 *
 * @author nzr4dl
 */
public class EmptyComponent extends JPanel implements TabbedPanel  {
    
    /** Creates a new instance of EmptyComponent */
    public EmptyComponent() {
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
    
    private File file = new File("TcAV");
    
    public File getFile() {
        return file;
    }
    
    public JComponent getComponent() {
        return this;
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
}
