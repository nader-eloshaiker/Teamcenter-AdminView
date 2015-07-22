/*
 * GUITools.java
 *
 * Created on 20 July 2007, 13:28
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tcadminview.utils;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
/**
 *
 * @author nzr4dl
 */
public class GUITools {
    public static int GAP_COMPONENT = 5;
    public static int GAP_MARGIN = 5;
    
    /**
     * Creates a new instance of GUITools
     */
    public GUITools() {
    }
    
    public static JPanel createPanelMargined(JComponent component) {
        JPanel SpacedPanel = new JPanel();
        SpacedPanel.setLayout(new GridLayout(1,1,GAP_COMPONENT,GAP_COMPONENT));
        SpacedPanel.setBorder(new EmptyBorder(GAP_MARGIN,GAP_MARGIN,GAP_MARGIN,GAP_MARGIN));
        SpacedPanel.add(component);
        
        return SpacedPanel;
    }
    
    public static JProgressBar createProgressBar(int minValue, int maxValue, int value, String maxTitle) {
        JProgressBar jp = new JProgressBar(minValue,maxValue);
        jp.setValue(value);
        jp.setToolTipText(value +" from a total of "+maxTitle+": " + maxValue);
        jp.setString(String.valueOf(value+" / "+maxValue));
        jp.setStringPainted(true);
        return jp;
        
    }
}
