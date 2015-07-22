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
import java.io.File;

/**
 *
 * @author nzr4dl
 */
public interface TabbedPanel {
    
    public JComponent getStatusBar();
    
    public JComponent getComponent();
    
    public File getFile();
    
    public ImageIcon getIcon();
    
}
