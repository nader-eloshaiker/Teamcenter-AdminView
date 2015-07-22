/*
 * AttributeComponent.java
 *
 * Created on 8 February 2008, 13:38
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tcav.gui.procedure;

import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.border.*;
import java.awt.event.*;
import java.awt.*;
import tcav.gui.*;
import tcav.Settings;
import tcav.manager.procedure.plmxmlpdm.base.IdBase;
import tcav.manager.procedure.ProcedureManager;

/**
 *
 * @author nzr4dl
 */
public class AttributeComponent extends JComponent {
    
    private JTreeAdvanced tree;
    private JRadioButton radioExpandAttributes;
    private JRadioButton radioCollapseAttributes;
    private JFrame parentFrame;
    
    public AttributeComponent(JFrame parentFrame, ProcedureManager pm) {
        super();
        this.parentFrame = parentFrame;
        
        tree = new JTreeAdvanced(new AttributeTreeModel());
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.setLargeModel(true);
        tree.setCellRenderer(new AttributelRenderer());
        if(tree.getRowHeight() < 18)
            tree.setRowHeight(18);
        
        ToolTipManager.sharedInstance().registerComponent(tree);
        
        JScrollPane scrolltree = new JScrollPane();
        scrolltree.setPreferredSize(new Dimension(200,220));
        scrolltree.setBorder(new BevelBorder(BevelBorder.LOWERED));
        scrolltree.getViewport().add(tree);
        
        
        radioExpandAttributes = new JRadioButton("Expanded");
        radioExpandAttributes.setOpaque(false);
        radioExpandAttributes.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                Settings.setPmWorkflowExpandedView(radioExpandAttributes.isSelected());
            }
        });
        radioCollapseAttributes = new JRadioButton("Collapsed");
        radioCollapseAttributes.setOpaque(false);
        radioCollapseAttributes.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                Settings.setPmWorkflowExpandedView(!radioCollapseAttributes.isSelected());
            }
        });
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(radioExpandAttributes);
        buttonGroup.add(radioCollapseAttributes);
        buttonGroup.setSelected(radioExpandAttributes.getModel(),Settings.isPmWorkflowExpandedView());
        buttonGroup.setSelected(radioCollapseAttributes.getModel(),!Settings.isPmWorkflowExpandedView());
        
        JToolBar toolBarAttributeView = new GUIutilities().createTreeExpandToolbar(tree, parentFrame);
        toolBarAttributeView.addSeparator();
        toolBarAttributeView.add(new JLabel("Default View:"));
        toolBarAttributeView.add(radioExpandAttributes);
        toolBarAttributeView.add(radioCollapseAttributes);
        
        
        JPanel panelAttributeInner =  new JPanel();
        panelAttributeInner.setLayout(new BorderLayout(GUIutilities.GAP_COMPONENT,GUIutilities.GAP_COMPONENT));
        panelAttributeInner.add("Center",scrolltree);
        panelAttributeInner.add("South",toolBarAttributeView);
        
        
        this.setLayout(new BorderLayout(GUIutilities.GAP_COMPONENT,GUIutilities.GAP_COMPONENT));
        this.setBorder(new CompoundBorder(
                new TitledBorder(new EtchedBorder(),"Attributes"),
                new EmptyBorder(GUIutilities.GAP_MARGIN,GUIutilities.GAP_MARGIN,GUIutilities.GAP_MARGIN,GUIutilities.GAP_MARGIN)));
        
        this.add("Center",GUIutilities.createPanelMargined(panelAttributeInner));
        
    }
    
    public JTreeAdvanced getTree() {
        return tree;
    }
    
    public void updateTree(IdBase procedure) {
        tree.setModel(new AttributeTreeModel(procedure));
        if(Settings.isPmWorkflowExpandedView())
            GUIutilities.expandTree(tree, parentFrame);
    }
    /*
    public boolean equals(idBase procedure) {
        return ((IdBase)tree.getModel().getRoot()).getId().equals()
    }
    */
}
