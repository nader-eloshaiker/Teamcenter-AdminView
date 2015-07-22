/*
 * TabbedPanel.java
 *
 * Created on 21 August 2007, 11:06
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tceav.gui;

import javax.swing.JComponent;
import javax.swing.ImageIcon;
import javax.swing.JPanel;


/**
 *
 * @author nzr4dl
 */
public abstract class TabbedPanel extends JPanel {
    
    abstract public JComponent getStatusBar();
    
    abstract public JComponent getToolBar();
    
    abstract public String getTitle();
    
    abstract public ImageIcon getIcon();
    
}
