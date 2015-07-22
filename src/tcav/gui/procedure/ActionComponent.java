/*
 * ActionComponent.java
 *
 * Created on 8 February 2008, 13:17
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
public class ActionComponent extends JComponent {
    
    private JTreeAdvanced tree;
    private JRadioButton radioExpandActions;
    private JRadioButton radioCollapseActions;
    private JFrame parentFrame;
    
    
    public ActionComponent(JFrame parentFrame, ProcedureManager pm) {
        super();
        this.parentFrame = parentFrame;
        
        // Action Tree
        tree = new JTreeAdvanced(new ActionTreeModel());
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.setCellRenderer(new ActionlRenderer());

        ToolTipManager.sharedInstance().registerComponent(tree);

        JScrollPane scrolltree = new JScrollPane();
        scrolltree.setBorder(new BevelBorder(BevelBorder.LOWERED));
        scrolltree.getViewport().add(tree);
        
        radioExpandActions = new JRadioButton("Expanded");
        radioExpandActions.setOpaque(false);
        radioExpandActions.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                Settings.setPmActionExpandedView(radioExpandActions.isSelected());
            }
        });
        
        
        radioCollapseActions = new JRadioButton("Collapsed");
        radioCollapseActions.setOpaque(false);
        radioCollapseActions.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                Settings.setPmActionExpandedView(!radioCollapseActions.isSelected());
            }
        });
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(radioExpandActions);
        buttonGroup.add(radioCollapseActions);
        buttonGroup.setSelected(radioExpandActions.getModel(),Settings.isPmActionExpandedView());
        buttonGroup.setSelected(radioCollapseActions.getModel(),!Settings.isPmActionExpandedView());
        
        JToolBar toolbar = new GUIutilities().createTreeExpandToolbar(tree, parentFrame);
        toolbar.addSeparator();
        toolbar.add(new JLabel("Default View:"));
        toolbar.add(radioExpandActions);
        toolbar.add(radioCollapseActions);
        
        this.setBorder(new CompoundBorder(
                new TitledBorder(new EtchedBorder(),"Actions"),
                new EmptyBorder(GUIutilities.GAP_MARGIN,GUIutilities.GAP_MARGIN,GUIutilities.GAP_MARGIN,GUIutilities.GAP_MARGIN)));
        this.setLayout(new BorderLayout(GUIutilities.GAP_COMPONENT,GUIutilities.GAP_COMPONENT));
        this.add("Center",scrolltree);
        this.add("South",toolbar);
    }
    
    public JTreeAdvanced getTree() {
        return tree;
    }
    
    public void updateTree(IdBase procedure) {
        tree.setModel(new ActionTreeModel(procedure));
        if(Settings.isPmActionExpandedView())
            GUIutilities.expandTree(tree, parentFrame);
    }
}
