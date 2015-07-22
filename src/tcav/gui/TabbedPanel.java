/*
 * TabbedPanel.java
 *
 * Created on 21 August 2007, 11:06
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tcav.gui;

import javax.swing.JComponent;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import java.io.File;

import tcav.manager.AbstractManager;

/**
 *
 * @author nzr4dl
 */
public abstract class TabbedPanel extends JPanel {
    
    abstract public JComponent getStatusBar();
    
    abstract public AbstractManager getManager();
    
    abstract public ImageIcon getIcon();
    
}
