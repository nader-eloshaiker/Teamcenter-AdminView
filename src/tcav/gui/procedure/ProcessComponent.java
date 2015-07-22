/*
 * ProcessComponent.java
 *
 * Created on 8 February 2008, 11:34
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
import tcav.manager.procedure.ProcedureManager;

/**
 *
 * @author nzr4dl
 */
public class ProcessComponent extends JComponent {
    
    private JTreeAdvanced tree;
    private JRadioButton radioDependantTasks;
    private JRadioButton radioSubWorkflow;
    private ProcedureManager procedureManager;
    
    public ProcessComponent(JFrame parentFrame, ProcedureManager pm) {
        super();
        this.procedureManager = pm;
        
        // Workflow Process Tree
        tree = new JTreeAdvanced(new ProcedureTreeModel(pm.getWorkflowProcesses(), pm.getSite(), Settings.getPMProcedureMode()));
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.setCellRenderer(new ProcessRenderer());
        if(tree.getRowHeight() < 18)
            tree.setRowHeight(18);
        
        ToolTipManager.sharedInstance().registerComponent(tree);

        
        JScrollPane scrolltree = new JScrollPane();
        scrolltree.setBorder(new BevelBorder(BevelBorder.LOWERED));
        scrolltree.getViewport().add(tree);
        
        
        radioDependantTasks = new JRadioButton("Dependant Tasks");
        radioDependantTasks.setOpaque(false);
        radioDependantTasks.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                Settings.setPMProcedureMode(ProcedureTreeModel.MODE_DEPENDANT_TASKS);
                tree.setModel(new ProcedureTreeModel(procedureManager.getWorkflowProcesses(), procedureManager.getSite(), Settings.getPMProcedureMode()));
            }
        });
        radioSubWorkflow = new JRadioButton("Sub Workflows");
        radioSubWorkflow.setOpaque(false);
        radioSubWorkflow.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                Settings.setPMProcedureMode(ProcedureTreeModel.MODE_SUB_WORKFLOWS);
                tree.setModel(new ProcedureTreeModel(procedureManager.getWorkflowProcesses(), procedureManager.getSite(), Settings.getPMProcedureMode()));
            }
        });
        ButtonGroup buttonGroupProcedureMode = new ButtonGroup();
        buttonGroupProcedureMode.add(radioDependantTasks);
        buttonGroupProcedureMode.add(radioSubWorkflow);
        buttonGroupProcedureMode.setSelected(
                radioDependantTasks.getModel(),
                (Settings.getPMProcedureMode() == ProcedureTreeModel.MODE_DEPENDANT_TASKS));
        buttonGroupProcedureMode.setSelected(
                radioSubWorkflow.getModel(),
                (Settings.getPMProcedureMode() == ProcedureTreeModel.MODE_SUB_WORKFLOWS));
        
        
        JToolBar toolBarWorkflowView = new GUIutilities().createTreeExpandToolbar(tree, parentFrame);
        toolBarWorkflowView.addSeparator();
        toolBarWorkflowView.add(new JLabel("Default View:"));
        toolBarWorkflowView.add(radioDependantTasks);
        toolBarWorkflowView.add(radioSubWorkflow);
        
        
        this.setLayout(new BorderLayout(GUIutilities.GAP_COMPONENT,GUIutilities.GAP_COMPONENT));
        this.setBorder(new CompoundBorder(
                new TitledBorder(new EtchedBorder(),"Workflow Templates"),
                new EmptyBorder(GUIutilities.GAP_MARGIN,GUIutilities.GAP_MARGIN,GUIutilities.GAP_MARGIN,GUIutilities.GAP_MARGIN)));
        this.add("South",toolBarWorkflowView);
        this.add("Center",scrolltree);
        
    }
    
    public JTreeAdvanced getTree() {
        return tree;
    }
    
}
