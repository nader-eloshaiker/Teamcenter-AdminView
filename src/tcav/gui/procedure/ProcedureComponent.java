/*
 * ProcedureComponent.java
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
public class ProcedureComponent extends JPanel implements TabbedPanel {
    
    protected JTreeAdvanced treeWorkflowProcess;
    protected JFrame parentFrame;
    protected ProcedureManager pm;
    protected JSplitPane splitPane1;
    protected ProcedureTreeModel modelProcedure;
    
    /**
     * Creates a new instance of ProcedureComponent
     */
    public ProcedureComponent(JFrame parentFrame, ProcedureManager pm) {
        this.pm = pm;
        this.parentFrame = parentFrame;
        
        splitPane1 = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                true,
                Utilities.createPanelMargined(createWorkflowProcessPanel()),
                createNodeDetailsPanel());
        splitPane1.setResizeWeight(1.0);
        splitPane1.setBorder(null);
        splitPane1.setDividerLocation(Settings.getPMSplitLocation1());
        splitPane1.setOneTouchExpandable(true);
        ((BasicSplitPaneUI)splitPane1.getUI()).getDivider().addComponentListener(new ComponentAdapter(){
            public void componentMoved(ComponentEvent e){
                Settings.setPMSplitLocation1(splitPane1.getDividerLocation());
            }
        });
        this.setLayout(new BorderLayout());
        this.add("Center",splitPane1);
        
    }
    
    protected JButton buttonWorkflowFindNext;
    protected JButton buttonWorkflowFind;
    protected JButton buttonWorkflowFindClear;
    protected JComboBox boxSearchWorkflowType;
    protected JTextField textSearchValue;
    protected JRadioButton radioProcedureDependantTasks;
    protected JRadioButton radioProcedureSubWorkflow;
    protected SearchTreeComponent searchProcedures;
    
    public JComponent createWorkflowProcessPanel() {
        
        // Workflow Process Tree
        treeWorkflowProcess = new JTreeAdvanced(new ProcedureTreeModel(pm, Settings.getPMProcedureMode()));
        treeWorkflowProcess.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        treeWorkflowProcess.setLargeModel(true);
        treeWorkflowProcess.setCellRenderer(new ProcedureTreeCellRenderer());
        treeWorkflowProcess.addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent e) {
                TreePath path = e.getPath();
                NodeReference nr = (NodeReference)path.getLastPathComponent();
                updateNodeDetails(nr);
            }
        });
        JScrollPane scrollTreeWorkflowProcess = new JScrollPane();
        scrollTreeWorkflowProcess.setBorder(new BevelBorder(BevelBorder.LOWERED));
        scrollTreeWorkflowProcess.getViewport().add(treeWorkflowProcess);
        
        JButton buttonExpandBelow = new JButton();
        buttonExpandBelow.setToolTipText("Expand Below");
        buttonExpandBelow.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                new Thread() {
                    public void run() {
                        Utilities.expandTreeBranch(treeWorkflowProcess, parentFrame);
                    }
                }.start();
            }
        });
        JButton buttonExpandAll = new JButton();
        buttonExpandAll.setToolTipText("Expand All");
        buttonExpandAll.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                new Thread() {
                    public void run() {
                        Utilities.expandTree(treeWorkflowProcess, parentFrame);
                    }
                }.start();
            }
        });
        JButton buttonCollapseBelow = new JButton();
        buttonCollapseBelow.setToolTipText("Collapse Below");
        buttonCollapseBelow.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                new Thread() {
                    public void run() {
                        Utilities.collapseTreeBranch(treeWorkflowProcess, parentFrame);
                    }
                }.start();
            }
        });
        JButton buttonCollapseAll = new JButton();
        buttonCollapseAll.setToolTipText("Collapse Below");
        buttonCollapseAll.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                new Thread() {
                    public void run() {
                        Utilities.collapseTree(treeWorkflowProcess, parentFrame);
                    }
                }.start();
            }
        });
        
        radioProcedureDependantTasks = new JRadioButton("Dependant Tasks");
        radioProcedureDependantTasks.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                Settings.setPMProcedureMode(ProcedureTreeModel.MODE_DEPENDANT_TASKS);
                treeWorkflowProcess.setModel(new ProcedureTreeModel(pm, Settings.getPMProcedureMode()));
                //modelProcedure.setProcedureMode(ProcedureTreeModel.MODE_DEPENDANT_TASKS);
                //treeWorkflowProcess.reloadModel();
                //treeWorkflowProcess.validate();
            }
        });
        radioProcedureSubWorkflow = new JRadioButton("Sub Workflows");
        radioProcedureSubWorkflow.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                Settings.setPMProcedureMode(ProcedureTreeModel.MODE_SUB_WORKFLOWS);
                treeWorkflowProcess.setModel(new ProcedureTreeModel(pm, Settings.getPMProcedureMode()));
                //modelProcedure.setProcedureMode(ProcedureTreeModel.MODE_SUB_WORKFLOWS);
                //treeWorkflowProcess.reloadModel();
                //treeWorkflowProcess.validate();
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
        
        searchProcedures = new SearchTreeComponent(){
            public boolean compare(TreePath parent, String type, String value) {
                NodeReference nr = (NodeReference)parent.getLastPathComponent();
                Boolean matched = false;
                
                if((!type.equals("")) && (!value.equals("")) )
                    return isMatched(nr.getClassType().value(), type) & isMatched(nr.getName(), value);
                else if(!type.equals(""))
                    return isMatched(nr.getClassType().value(), type);
                else if(!value.equals(""))
                    return isMatched(nr.getName(), value);
                else
                    return false;
            }
        };
        
        buttonWorkflowFind = new JButton("Find");
        buttonWorkflowFind.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                new Thread() {
                    public void run() {
                        String workflowString = "";
                        String valueString = "";
                        
                        if(boxSearchWorkflowType.getSelectedIndex() > 0)
                            workflowString = ProcedureManager.WORKFLOW_TYPES[boxSearchWorkflowType.getSelectedIndex() - 1].value();
                        valueString = textSearchValue.getText();
                        
                        if( (workflowString.equals(""))  &&
                                ((valueString == null) || (valueString.equals(""))))
                            JOptionPane.showMessageDialog(parentFrame, "Search requires either a Workflow Type, value/name or any combination.", "No Search Criteria", JOptionPane.ERROR_MESSAGE);
                        else {
                            searchProcedures.search(treeWorkflowProcess, workflowString, valueString);
                            
                            if (searchProcedures.getResultSize() == 0) {
                                JOptionPane.showMessageDialog(parentFrame, "No matches found", "No Matches Found", JOptionPane.WARNING_MESSAGE);
                                buttonWorkflowFindNext.setEnabled(false);
                                buttonWorkflowFindClear.setEnabled(false);
                                buttonWorkflowFind.setEnabled(true);
                                boxSearchWorkflowType.setEnabled(true);
                                textSearchValue.setEnabled(true);
                            } else {
                                int k = searchProcedures.getResultIndex() + 1;
                                buttonWorkflowFind.setText(k+" / "+searchProcedures.getResultSize());
                                buttonWorkflowFindNext.setEnabled(true);
                                buttonWorkflowFindClear.setEnabled(true);
                                buttonWorkflowFind.setEnabled(false);
                                boxSearchWorkflowType.setEnabled(false);
                                textSearchValue.setEnabled(false);
                            }
                        }
                    }
                }.start();
            }
        });
        
        buttonWorkflowFindNext = new JButton("Find Next");
        buttonWorkflowFindNext.setEnabled(false);
        buttonWorkflowFindNext.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                searchProcedures.searchNext(treeWorkflowProcess);
                int k = searchProcedures.getResultIndex() + 1;
                buttonWorkflowFind.setText(k+" / "+ searchProcedures.getResultSize());
            }
        });
        
        buttonWorkflowFindClear = new JButton("Clear");
        buttonWorkflowFindClear.setEnabled(false);
        buttonWorkflowFindClear.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                buttonWorkflowFind.setText("Find");
                buttonWorkflowFindNext.setEnabled(false);
                buttonWorkflowFindClear.setEnabled(false);
                buttonWorkflowFind.setEnabled(true);
                boxSearchWorkflowType.setEnabled(true);
                textSearchValue.setEnabled(true);
                boxSearchWorkflowType.setSelectedIndex(0);
                textSearchValue.setText("");
                searchProcedures.resetResults();
            }
        });
        
        boxSearchWorkflowType = new JComboBox();
        boxSearchWorkflowType.setToolTipText("Workflow Type");
        if (pm.getWorkflowTemplates().size() == 0) {
            boxSearchWorkflowType.setEnabled(false);
            boxSearchWorkflowType.addItem("Workflow Type");
        } else {
            boxSearchWorkflowType.addItem("");
            for(int i=0; i<ProcedureManager.WORKFLOW_TYPES_NAMES.length; i++) {
                boxSearchWorkflowType.addItem(ProcedureManager.WORKFLOW_TYPES_NAMES[i]);
            }
        }
        
        textSearchValue = new JTextField();
        textSearchValue.setToolTipText("Workflow Name/Value: * ? [ - ] accepted");
        textSearchValue.setColumns(6);
        
        JToolBar toolBarWorkflowTreeTop = new JToolBar();
        toolBarWorkflowTreeTop.setMargin(new Insets(
                Utilities.GAP_INSET,
                Utilities.GAP_INSET,
                Utilities.GAP_INSET,
                Utilities.GAP_INSET));
        toolBarWorkflowTreeTop.setFloatable(false);
        toolBarWorkflowTreeTop.add(buttonExpandAll);
        toolBarWorkflowTreeTop.add(buttonExpandBelow);
        toolBarWorkflowTreeTop.add(buttonCollapseAll);
        toolBarWorkflowTreeTop.add(buttonCollapseBelow);
        toolBarWorkflowTreeTop.addSeparator();
        toolBarWorkflowTreeTop.add(new JLabel("Default View:"));
        toolBarWorkflowTreeTop.add(radioProcedureDependantTasks);
        toolBarWorkflowTreeTop.add(radioProcedureSubWorkflow);
        
        JToolBar toolBarWorkflowTreeBottom = new JToolBar();
        toolBarWorkflowTreeBottom.setMargin(new Insets(
                Utilities.GAP_INSET,
                Utilities.GAP_INSET,
                Utilities.GAP_INSET,
                Utilities.GAP_INSET));
        toolBarWorkflowTreeBottom.setFloatable(false);
        toolBarWorkflowTreeBottom.add(boxSearchWorkflowType);
        toolBarWorkflowTreeBottom.addSeparator();
        toolBarWorkflowTreeBottom.add(textSearchValue);
        toolBarWorkflowTreeBottom.addSeparator();
        toolBarWorkflowTreeBottom.add(buttonWorkflowFindClear);
        toolBarWorkflowTreeBottom.add(buttonWorkflowFindNext);
        toolBarWorkflowTreeBottom.add(buttonWorkflowFind);
        
        JPanel panelWorkflowProcess =  new JPanel();
        panelWorkflowProcess.setLayout(new BorderLayout(Utilities.GAP_COMPONENT,Utilities.GAP_COMPONENT));
        panelWorkflowProcess.add("North",toolBarWorkflowTreeTop);
        panelWorkflowProcess.add("Center",scrollTreeWorkflowProcess);
        panelWorkflowProcess.add("South",toolBarWorkflowTreeBottom);
        
        
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(Utilities.GAP_COMPONENT,Utilities.GAP_COMPONENT));
        panel.setBorder(new TitledBorder(new EtchedBorder(),"Workflow Templates: PROCESS"));
        panel.add(Utilities.createPanelMargined(panelWorkflowProcess));
        
        return panel;
    }
    
    public boolean isEmptyPanel(){
        return (pm.getWorkflowTemplates().size() == 0);
    }
    
    protected JTreeAdvanced treeAttributes;
    protected JTableAdvanced tableProcedure;
    protected JTableAdvanced tableAttribute;
    protected JRadioButton radioExpand;
    protected JRadioButton radioCollapse;
    protected JSplitPane splitPane2;
    
    private JComponent createNodeDetailsPanel() {
        treeAttributes = new JTreeAdvanced(new AttributeTreeModel());
        treeAttributes.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        treeAttributes.setLargeModel(true);
        treeAttributes.setCellRenderer(new ProcedureTreeCellRenderer());
        treeAttributes.addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent e) {
                TreePath path = e.getPath();
                NodeReference nr = (NodeReference)path.getLastPathComponent();
                updateAttribDetails(nr);
            }
        });
        
        JScrollPane scrollTreeAttributes = new JScrollPane();
        scrollTreeAttributes.setPreferredSize(new Dimension(200,220));
        scrollTreeAttributes.setBorder(new BevelBorder(BevelBorder.LOWERED));
        scrollTreeAttributes.getViewport().add(treeAttributes);
        
        JButton buttonExpandAll = new JButton();
        buttonExpandAll.setToolTipText("Expand All");
        buttonExpandAll.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                new Thread() {
                    public void run() {
                        Utilities.expandTree(treeAttributes, parentFrame);
                    }
                }.start();
            }
        });
        JButton buttonExpandBelow = new JButton();
        buttonExpandBelow.setToolTipText("Expand Below");
        buttonExpandBelow.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                new Thread() {
                    public void run() {
                        Utilities.expandTreeBranch(treeAttributes, parentFrame);
                    }
                }.start();
            }
        });
        JButton buttonCollapseAll = new JButton();
        buttonCollapseAll.setToolTipText("Collapse All");
        buttonCollapseAll.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                new Thread() {
                    public void run() {
                        Utilities.collapseTree(treeAttributes, parentFrame);
                    }
                }.start();
            }
        });
        JButton buttonCollapseBelow = new JButton();
        buttonCollapseBelow.setToolTipText("Collapse Below");
        buttonCollapseBelow.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                new Thread() {
                    public void run() {
                        Utilities.collapseTreeBranch(treeAttributes, parentFrame);
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
        
        radioExpand = new JRadioButton("Expanded");
        radioExpand.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                Settings.setPMExpandedView(radioExpand.isSelected());
            }
        });
        radioCollapse = new JRadioButton("Collapsed");
        radioCollapse.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                Settings.setPMExpandedView(!radioCollapse.isSelected());
            }
        });
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(radioExpand);
        buttonGroup.add(radioCollapse);
        buttonGroup.setSelected(radioExpand.getModel(),Settings.getPMExpandedView());
        buttonGroup.setSelected(radioCollapse.getModel(),!Settings.getPMExpandedView());
        
        JToolBar toolBarAttributeTree = new JToolBar();
        toolBarAttributeTree.setMargin(new Insets(
                Utilities.GAP_INSET,
                Utilities.GAP_INSET,
                Utilities.GAP_INSET,
                Utilities.GAP_INSET));
        toolBarAttributeTree.setFloatable(false);
        toolBarAttributeTree.add(buttonExpandAll);
        toolBarAttributeTree.add(buttonExpandBelow);
        toolBarAttributeTree.add(buttonCollapseAll);
        toolBarAttributeTree.add(buttonCollapseBelow);
        toolBarAttributeTree.addSeparator();
        toolBarAttributeTree.add(new JLabel("Default View:"));
        toolBarAttributeTree.add(radioExpand);
        toolBarAttributeTree.add(radioCollapse);
        
        
        JPanel panelAttributeInner =  new JPanel();
        panelAttributeInner.setLayout(new BorderLayout(Utilities.GAP_COMPONENT,Utilities.GAP_COMPONENT));
        panelAttributeInner.add("Center",scrollTreeAttributes);
        panelAttributeInner.add("North",toolBarAttributeTree);
        
        
        JPanel panelAttributes = new JPanel();
        panelAttributes.setLayout(new BorderLayout(Utilities.GAP_COMPONENT,Utilities.GAP_COMPONENT));
        panelAttributes.setBorder(new TitledBorder(new EtchedBorder(),"Attributes"));
        panelAttributes.add("Center",Utilities.createPanelMargined(panelAttributeInner));
        
        tableAttribute = new JTableAdvanced(new NodeTableModel());
        JScrollPane scrollTableAttribute = new JScrollPane();
        scrollTableAttribute.setPreferredSize(new Dimension(200,220));
        scrollTableAttribute.setBorder(new BevelBorder(BevelBorder.LOWERED));
        scrollTableAttribute.getViewport().add(tableAttribute);
        
        JPanel panelAttributeDetails = new JPanel();
        panelAttributeDetails.setLayout(new BorderLayout(Utilities.GAP_COMPONENT,Utilities.GAP_COMPONENT));
        panelAttributeDetails.setBorder(new TitledBorder(new EtchedBorder(),"Details"));
        panelAttributeDetails.add("Center",Utilities.createPanelMargined(scrollTableAttribute));
        
        
        splitPane2 = new JSplitPane(
                JSplitPane.VERTICAL_SPLIT,
                true,
                Utilities.createPanelMargined(panelAttributes),
                Utilities.createPanelMargined(panelAttributeDetails));
        splitPane2.setResizeWeight(1.0);
        splitPane2.setDividerLocation(Settings.getPMSplitLocation2());
        splitPane2.setOneTouchExpandable(true);
        splitPane2.setBorder(null);
        ((BasicSplitPaneUI)splitPane2.getUI()).getDivider().addComponentListener(new ComponentAdapter(){
            public void componentMoved(ComponentEvent e){
                Settings.setPMSplitLocation2(splitPane2.getDividerLocation());
            }
        });
        
        return splitPane2;
    }
    
    private void updateNodeDetails(NodeReference nr) {
        tableAttribute.setModel(new NodeTableModel(nr, pm));
        Utilities.packColumns(tableAttribute, 2);
        treeAttributes.setModel(new AttributeTreeModel(nr,pm));
        if(radioExpand.isSelected())
            Utilities.expandTree(treeAttributes, parentFrame);
    }
    
    private void updateAttribDetails(NodeReference nr){
        tableAttribute.setModel(new NodeTableModel(nr, pm));
        Utilities.packColumns(tableAttribute, 2);
    }
    
}
