/*
 * ProcedureManagerComponent.java
 *
 * Created on 20 July 2007, 11:46
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tcav.gui.procedure;

import tcav.*;
import tcav.gui.*;
import tcav.procedure.*;
import tcav.utils.*;
import tcav.xml.*;
import tcav.plmxmlpdm.base.*;
import tcav.plmxmlpdm.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import javax.swing.tree.*;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentAdapter;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Insets;
import java.util.ArrayList;
/**
 *
 * @author nzr4dl
 */
public class ProcedureManagerComponent extends JPanel implements TabbedPanel {
    
    protected JFrame parentFrame;
    protected ProcedureManager pm;
    protected JSplitPane splitPane1;
    protected ProcedureTreeModel modelProcedure;
    
    /**
     * Creates a new instance of ProcedureManagerComponent
     */
    public ProcedureManagerComponent(JFrame parentFrame, ProcedureManager pm) {
        this.pm = pm;
        this.parentFrame = parentFrame;
        
        JPanel panelLeft = new JPanel();
        panelLeft.setLayout(new GridLayout(2,1,GUIutilities.GAP_COMPONENT,GUIutilities.GAP_COMPONENT));
        panelLeft.add(GUIutilities.createPanelMargined(createWorkflowProcessPanel()));
        panelLeft.add(GUIutilities.createPanelMargined(createActionPanel()));
        
        JPanel panelRight = new JPanel();
        panelRight.setLayout(new BorderLayout());
        panelRight.add("Center",GUIutilities.createPanelMargined(createAttributesPanel()));
        panelRight.add("South", GUIutilities.createPanelMargined(createXMLPanel()));
        
        splitPane1 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, panelLeft, panelRight);
        splitPane1.setResizeWeight(1.0);
        splitPane1.setBorder(null);
        splitPane1.setDividerLocation(Settings.getPMSplitLocation());
        splitPane1.setOneTouchExpandable(true);
        ((BasicSplitPaneUI)splitPane1.getUI()).getDivider().addComponentListener(new ComponentAdapter(){
            public void componentMoved(ComponentEvent e){
                Settings.setPMSplitLocation(splitPane1.getDividerLocation());
            }
        });
        
        ToolTipManager.sharedInstance().registerComponent(treeWorkflowProcess);
        ToolTipManager.sharedInstance().registerComponent(treeAction);
        ToolTipManager.sharedInstance().registerComponent(treeAttributes);
        
        this.setLayout(new BorderLayout());
        this.add("Center",splitPane1);
        
    }
    
    protected JTreeAdvanced treeAction;
    protected JRadioButton radioExpandActions;
    protected JRadioButton radioCollapseActions;
    
    
    public JComponent createActionPanel() {
        // Action Tree
        treeAction = new JTreeAdvanced(new ActionTreeModel());
        treeAction.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        treeAction.setCellRenderer(new ActionTreeCellRenderer());
        treeAction.addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent e) {
                TreePath path = e.getPath();
                IdBase procedure = (IdBase)path.getLastPathComponent();
                updateAttributeTree(procedure);
            }
        });
        JScrollPane scrollTreeAction = new JScrollPane();
        scrollTreeAction.setBorder(new BevelBorder(BevelBorder.LOWERED));
        scrollTreeAction.getViewport().add(treeAction);
        
        radioExpandActions = new JRadioButton("Expanded");
        radioExpandActions.setOpaque(false);
        radioExpandActions.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                Settings.setPMActionExpandedView(radioExpandActions.isSelected());
            }
        });
        
        
        radioCollapseActions = new JRadioButton("Collapsed");
        radioCollapseActions.setOpaque(false);
        radioCollapseActions.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                Settings.setPMActionExpandedView(!radioCollapseActions.isSelected());
            }
        });
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(radioExpandActions);
        buttonGroup.add(radioCollapseActions);
        buttonGroup.setSelected(radioExpandActions.getModel(),Settings.getPMActionExpandedView());
        buttonGroup.setSelected(radioCollapseActions.getModel(),!Settings.getPMActionExpandedView());
        
        JToolBar toolbar = createTreeToolbar(treeAction);
        toolbar.addSeparator();
        toolbar.add(new JLabel("Default View:"));
        toolbar.add(radioExpandActions);
        toolbar.add(radioCollapseActions);
        
        JPanel panelActions = new JPanel();
        panelActions.setBorder(new CompoundBorder(
                new TitledBorder(new EtchedBorder(),"Actions"),
                new EmptyBorder(GUIutilities.GAP_MARGIN,GUIutilities.GAP_MARGIN,GUIutilities.GAP_MARGIN,GUIutilities.GAP_MARGIN)));
        panelActions.setLayout(new BorderLayout(GUIutilities.GAP_COMPONENT,GUIutilities.GAP_COMPONENT));
        panelActions.add("Center",scrollTreeAction);
        panelActions.add("South",toolbar);
        
        return panelActions;
    }
    
    protected JTreeAdvanced treeWorkflowProcess;
    protected JRadioButton radioProcedureDependantTasks;
    protected JRadioButton radioProcedureSubWorkflow;
    
    public JComponent createWorkflowProcessPanel() {
        
        // Workflow Process Tree
        treeWorkflowProcess = new JTreeAdvanced(new ProcedureTreeModel(pm.getWorkflowProcesses(), pm.getSite(), Settings.getPMProcedureMode()));
        treeWorkflowProcess.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        treeWorkflowProcess.setCellRenderer(new TagTreeCellRenderer());
        treeWorkflowProcess.addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent e) {
                TreePath path = e.getPath();
                IdBase procedure = (IdBase)path.getLastPathComponent();
                updateAttributeTree(procedure);
                updateActionTree(procedure);
            }
        });
        
        JScrollPane scrollTreeWorkflowProcess = new JScrollPane();
        scrollTreeWorkflowProcess.setBorder(new BevelBorder(BevelBorder.LOWERED));
        scrollTreeWorkflowProcess.getViewport().add(treeWorkflowProcess);
        
        
        radioProcedureDependantTasks = new JRadioButton("Dependant Tasks");
        radioProcedureDependantTasks.setOpaque(false);
        radioProcedureDependantTasks.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                Settings.setPMProcedureMode(ProcedureTreeModel.MODE_DEPENDANT_TASKS);
                treeWorkflowProcess.setModel(new ProcedureTreeModel(pm.getWorkflowProcesses(), pm.getSite(), Settings.getPMProcedureMode()));
            }
        });
        radioProcedureSubWorkflow = new JRadioButton("Sub Workflows");
        radioProcedureSubWorkflow.setOpaque(false);
        radioProcedureSubWorkflow.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                Settings.setPMProcedureMode(ProcedureTreeModel.MODE_SUB_WORKFLOWS);
                treeWorkflowProcess.setModel(new ProcedureTreeModel(pm.getWorkflowProcesses(), pm.getSite(), Settings.getPMProcedureMode()));
            }
        });
        ButtonGroup buttonGroupProcedureMode = new ButtonGroup();
        buttonGroupProcedureMode.add(radioProcedureDependantTasks);
        buttonGroupProcedureMode.add(radioProcedureSubWorkflow);
        buttonGroupProcedureMode.setSelected(
                radioProcedureDependantTasks.getModel(),
                (Settings.getPMProcedureMode() == ProcedureTreeModel.MODE_DEPENDANT_TASKS));
        buttonGroupProcedureMode.setSelected(
                radioProcedureSubWorkflow.getModel(),
                (Settings.getPMProcedureMode() == ProcedureTreeModel.MODE_SUB_WORKFLOWS));
        
        
        JToolBar toolBarWorkflowView = createTreeToolbar(treeWorkflowProcess);
        toolBarWorkflowView.addSeparator();
        toolBarWorkflowView.add(new JLabel("Default View:"));
        toolBarWorkflowView.add(radioProcedureDependantTasks);
        toolBarWorkflowView.add(radioProcedureSubWorkflow);
        
        
        JPanel panelWorkflowProcess =  new JPanel();
        panelWorkflowProcess.setLayout(new BorderLayout(GUIutilities.GAP_COMPONENT,GUIutilities.GAP_COMPONENT));
        panelWorkflowProcess.setBorder(new CompoundBorder(
                new TitledBorder(new EtchedBorder(),"Workflow Templates"),
                new EmptyBorder(GUIutilities.GAP_MARGIN,GUIutilities.GAP_MARGIN,GUIutilities.GAP_MARGIN,GUIutilities.GAP_MARGIN)));
        panelWorkflowProcess.add("South",toolBarWorkflowView);
        panelWorkflowProcess.add("Center",scrollTreeWorkflowProcess);
        
        
        return panelWorkflowProcess;
    }
    
    public boolean isEmptyPanel(){
        return (pm.getWorkflowProcesses().size() == 0);
    }
    
    protected JTreeAdvanced treeAttributes;
    protected JRadioButton radioExpandAttributes;
    protected JRadioButton radioCollapseAttributes;
    
    private JComponent createAttributesPanel() {
        treeAttributes = new JTreeAdvanced(new AttributeTreeModel());
        treeAttributes.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        treeAttributes.setLargeModel(true);
        treeAttributes.setCellRenderer(new TagTreeCellRenderer());
        treeAttributes.addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent e) {
                TreePath path = e.getPath();
                IdBase procedure = (IdBase)path.getLastPathComponent();
                updateXMLTable(procedure);
            }
        });
        
        JScrollPane scrollTreeAttributes = new JScrollPane();
        scrollTreeAttributes.setPreferredSize(new Dimension(200,220));
        scrollTreeAttributes.setBorder(new BevelBorder(BevelBorder.LOWERED));
        scrollTreeAttributes.getViewport().add(treeAttributes);
        
        
        radioExpandAttributes = new JRadioButton("Expanded");
        radioExpandAttributes.setOpaque(false);
        radioExpandAttributes.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                Settings.setPMWorkflowExpandedView(radioExpandAttributes.isSelected());
            }
        });
        radioCollapseAttributes = new JRadioButton("Collapsed");
        radioCollapseAttributes.setOpaque(false);
        radioCollapseAttributes.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                Settings.setPMWorkflowExpandedView(!radioCollapseAttributes.isSelected());
            }
        });
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(radioExpandAttributes);
        buttonGroup.add(radioCollapseAttributes);
        buttonGroup.setSelected(radioExpandAttributes.getModel(),Settings.getPMWorkflowExpandedView());
        buttonGroup.setSelected(radioCollapseAttributes.getModel(),!Settings.getPMWorkflowExpandedView());
        
        JToolBar toolBarAttributeView = createTreeToolbar(treeAttributes);
        toolBarAttributeView.addSeparator();
        toolBarAttributeView.add(new JLabel("Default View:"));
        toolBarAttributeView.add(radioExpandAttributes);
        toolBarAttributeView.add(radioCollapseAttributes);
        
        
        JPanel panelAttributeInner =  new JPanel();
        panelAttributeInner.setLayout(new BorderLayout(GUIutilities.GAP_COMPONENT,GUIutilities.GAP_COMPONENT));
        panelAttributeInner.add("Center",scrollTreeAttributes);
        panelAttributeInner.add("South",toolBarAttributeView);
        
        
        JPanel panelAttributes = new JPanel();
        panelAttributes.setLayout(new BorderLayout(GUIutilities.GAP_COMPONENT,GUIutilities.GAP_COMPONENT));
        panelAttributes.setBorder(new CompoundBorder(
                new TitledBorder(new EtchedBorder(),"Attributes"),
                new EmptyBorder(GUIutilities.GAP_MARGIN,GUIutilities.GAP_MARGIN,GUIutilities.GAP_MARGIN,GUIutilities.GAP_MARGIN)));
        
        panelAttributes.add("Center",GUIutilities.createPanelMargined(panelAttributeInner));
        
        return panelAttributes;
    }
    
    protected JTableAdvanced tableXML;
    
    private JComponent createXMLPanel() {
        tableXML = new JTableAdvanced(new XMLTableModel());
        JScrollPane scrolltableXML = new JScrollPane();
        scrolltableXML.setPreferredSize(new Dimension(200,220));
        scrolltableXML.setBorder(new BevelBorder(BevelBorder.LOWERED));
        scrolltableXML.getViewport().add(tableXML);
        
        JPanel panelAttributeDetails = new JPanel();
        panelAttributeDetails.setLayout(new BorderLayout(GUIutilities.GAP_COMPONENT,GUIutilities.GAP_COMPONENT));
        panelAttributeDetails.setBorder(new CompoundBorder(
                new TitledBorder(new EtchedBorder(),"Details"),
                new EmptyBorder(GUIutilities.GAP_MARGIN,GUIutilities.GAP_MARGIN,GUIutilities.GAP_MARGIN,GUIutilities.GAP_MARGIN)));
        panelAttributeDetails.add("Center",scrolltableXML);
        
        return panelAttributeDetails;
    }
    
    private void updateAttributeTree(IdBase procedure) {
        updateXMLTable(procedure);
        treeAttributes.setModel(new AttributeTreeModel(procedure));
        if(Settings.getPMWorkflowExpandedView())
            GUIutilities.expandTree(treeAttributes, parentFrame);
    }
    
    private void updateXMLTable(IdBase procedure){
        tableXML.setModel(new XMLTableModel(procedure));
        GUIutilities.packColumns(tableXML, 2);
    }
    
    private void updateActionTree(IdBase procedure) {
        treeAction.setModel(new ActionTreeModel(procedure));
        if(Settings.getPMActionExpandedView())
            GUIutilities.expandTree(treeAction, parentFrame);
    }
    
    public JToolBar createTreeToolbar(JTreeAdvanced tree) {
        JButton buttonExpandBelow = new JButton();
        buttonExpandBelow.setOpaque(false);
        buttonExpandBelow.setToolTipText("Expand Below");
        buttonExpandBelow.addActionListener(new TreeToolBarExpandAction(tree){
            public void actionPerformed(ActionEvent e) {
                new Thread() {
                    public void run() {
                        GUIutilities.expandTreeBranch(tree, parentFrame);
                    }
                }.start();
            }
        });
        JButton buttonExpandAll = new JButton();
        buttonExpandAll.setOpaque(false);
        buttonExpandAll.setToolTipText("Expand All");
        buttonExpandAll.addActionListener(new TreeToolBarExpandAction(tree){
            public void actionPerformed(ActionEvent e) {
                new Thread() {
                    public void run() {
                        GUIutilities.expandTree(tree, parentFrame);
                    }
                }.start();
            }
        });
        JButton buttonCollapseBelow = new JButton();
        buttonCollapseBelow.setOpaque(false);
        buttonCollapseBelow.setToolTipText("Collapse Below");
        buttonCollapseBelow.addActionListener(new TreeToolBarExpandAction(tree){
            public void actionPerformed(ActionEvent e) {
                new Thread() {
                    public void run() {
                        GUIutilities.collapseTreeBranch(tree, parentFrame);
                    }
                }.start();
            }
        });
        JButton buttonCollapseAll = new JButton();
        buttonCollapseAll.setOpaque(false);
        buttonCollapseAll.setToolTipText("Collapse Below");
        buttonCollapseAll.addActionListener(new TreeToolBarExpandAction(tree){
            public void actionPerformed(ActionEvent e) {
                new Thread() {
                    public void run() {
                        GUIutilities.collapseTree(tree, parentFrame);
                    }
                }.start();
            }
        });
        
        ImageIcon iconExpandAll = new ImageIcon();
        ImageIcon iconExpandBelow = new ImageIcon();
        ImageIcon iconCollapseAll = new ImageIcon();
        ImageIcon iconCollapseBelow = new ImageIcon();
        try {
            iconExpandAll = new ImageIcon(ResourceLocator.getButtonImage("Expand-All.gif"));
            iconExpandBelow = new  ImageIcon(ResourceLocator.getButtonImage("Expand-Below.gif"));
            iconCollapseAll = new  ImageIcon(ResourceLocator.getButtonImage("Collapse-All.gif"));
            iconCollapseBelow = new  ImageIcon(ResourceLocator.getButtonImage("Collapse-Below.gif"));
        } catch (Exception e) {
            System.out.println("Couldn't load images: " + e);
        }
        
        buttonExpandAll.setIcon(iconExpandAll);
        buttonExpandBelow.setIcon(iconExpandBelow);
        buttonCollapseAll.setIcon(iconCollapseAll);
        buttonCollapseBelow.setIcon(iconCollapseBelow);
        
        
        JToolBar toolbar = new JToolBar();
        toolbar.setMargin(new Insets(
                GUIutilities.GAP_INSET,
                GUIutilities.GAP_INSET,
                GUIutilities.GAP_INSET,
                GUIutilities.GAP_INSET));
        //toolbar.setFloatable(false);
        toolbar.add(buttonExpandAll);
        toolbar.add(buttonExpandBelow);
        toolbar.add(buttonCollapseAll);
        toolbar.add(buttonCollapseBelow);
        
        return toolbar;
    }
    
    class TreeToolBarExpandAction extends AbstractAction {
        JTreeAdvanced tree;
        protected TreeToolBarExpandAction(JTreeAdvanced tree) {
            super("Tree ToolBar Expand Action");
            this.tree = tree;
        }
        
        public void actionPerformed(ActionEvent e) {
            
        }
    }
    
}
