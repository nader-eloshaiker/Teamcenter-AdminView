/*
 * WorkflowComponent.java
 *
 * Created on 8 February 2008, 11:34
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tceav.gui.procedure;

import java.awt.BorderLayout;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.ToolTipManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.tree.TreeSelectionModel;
import tceav.gui.AdminViewFrame;
import tceav.gui.tools.GUIutilities;
import tceav.gui.tools.tree.JTreeAdvanced;
import tceav.gui.tools.tree.toolbar.TreeCopyProcedureAdaptor;
import tceav.gui.tools.tree.toolbar.TreeToolBar;
import tceav.manager.procedure.ProcedureManager;

/**
 *
 * @author nzr4dl
 */
public class WorkflowComponent extends JComponent {
    
    private JTreeAdvanced tree;
    private ProcedureManager procedureManager;
    
    public WorkflowComponent(AdminViewFrame parentFrame, ProcedureManager pm) {
        super();
        this.procedureManager = pm;
        
        // Workflow Process Tree
        tree = new JTreeAdvanced(new WorkflowTreeData(pm.getWorkflowProcesses(), pm.getSite()));
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.setCellRenderer(new WorkflowRenderer());
        if(tree.getRowHeight() < 18)
            tree.setRowHeight(18);
        
        ToolTipManager.sharedInstance().registerComponent(tree);
        
        
        JScrollPane scrolltree = new JScrollPane();
        scrolltree.setBorder(new BevelBorder(BevelBorder.LOWERED));
        scrolltree.getViewport().add(tree);
        
        
        TreeToolBar toolbar = new TreeToolBar(tree, parentFrame, new TreeCopyProcedureAdaptor());
        
        setLayout(new BorderLayout(GUIutilities.GAP_COMPONENT,GUIutilities.GAP_COMPONENT));
        setBorder(new CompoundBorder(
                new TitledBorder(new EtchedBorder(),"Workflows"),
                new EmptyBorder(GUIutilities.GAP_MARGIN,GUIutilities.GAP_MARGIN,GUIutilities.GAP_MARGIN,GUIutilities.GAP_MARGIN)));
        add(toolbar, BorderLayout.SOUTH);
        add(scrolltree, BorderLayout.CENTER);
        
    }
    
    public JTreeAdvanced getTree() {
        return tree;
    }
    
}
