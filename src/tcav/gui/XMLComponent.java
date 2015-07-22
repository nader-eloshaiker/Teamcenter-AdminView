/*
 * XMLComponent.java
 *
 * Created on 21 August 2007, 12:56
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tcav.gui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.Dimension;
import tcav.gui.procedure.*;
import tcav.xml.DOMUtil;

/**
 *
 * @author nzr4dl
 */
public class XMLComponent extends JComponent {
    
    protected JTree treeXML;
    protected XMLTreeModel treeModelXML;
    
    /** Creates a new instance of XMLComponent */
    public XMLComponent(DOMUtil domUtil) {
        
        treeModelXML = new XMLTreeModel(domUtil.getRootNode());
        treeXML = new JTree(treeModelXML);
        treeXML.setCellRenderer(new XMLTreeCellRenderer());
        JScrollPane scrollTreeXML = new JScrollPane();
        scrollTreeXML.setPreferredSize(new Dimension(550,340));
        scrollTreeXML.setBorder(new BevelBorder(BevelBorder.LOWERED));
        scrollTreeXML.getViewport().add(treeXML);
        this.setLayout(new GridLayout(1,1,GUIutilities.GAP_COMPONENT,GUIutilities.GAP_COMPONENT));
        this.setBorder(new TitledBorder(new EtchedBorder(),"PLMXML Debug Window"));
        this.add(GUIutilities.createPanelMargined(scrollTreeXML));
    }
    
}
