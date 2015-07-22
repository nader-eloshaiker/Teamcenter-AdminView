/*
 * ProcedureComponent.java
 *
 * Created on 20 July 2007, 11:46
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tcav.gui.procedure;

import javax.naming.ldap.StartTlsRequest;
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
    
    protected JTable tableDepTaskTemplateRef;
    protected JTable tableWorkflowTemplatesProcess;
    protected JTable tableWorkflowTemplatesSub;
    protected JTable tableActionsRef;
    protected JTable tableSubTemplateRef;
    protected JTreeAdvanced treeWorkflowProcess;
    protected JFrame parentFrame;
    protected ProcedureManager pm;
    protected JSplitPane splitPane1;
    
    
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
    
    public JComponent createWorkflowProcessPanel() {
        
        // Workflow Process Tree
        treeWorkflowProcess = new JTreeAdvanced(new ProcedureTreeModel(pm));
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
        JButton buttonExpandDependTask = new JButton("Dependant Task");
        buttonExpandDependTask.setHorizontalTextPosition(SwingConstants.RIGHT);
        buttonExpandDependTask.setToolTipText("Show all Dependant Workflow Tasks");
        buttonExpandDependTask.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                new Thread() {
                    public void run() {
                        Utilities.expandWorkflow(treeWorkflowProcess, parentFrame, NodeReference.PROCEDURE_DEPENDANT_TASKS);
                    }
                }.start();
            }
        });
        JButton buttonExpandSubTask = new JButton("Sub Workflow");
        buttonExpandSubTask.setHorizontalTextPosition(SwingConstants.RIGHT);
        buttonExpandSubTask.setToolTipText("Show all Sub Workflows");
        buttonExpandSubTask.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                new Thread() {
                    public void run() {
                        Utilities.expandWorkflow(treeWorkflowProcess, parentFrame, NodeReference.PROCEDURE_SUB_WORKFLOW);
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
        buttonExpandDependTask.setIcon(iconExpandBelow);
        buttonExpandSubTask.setIcon(iconExpandBelow);
        buttonCollapseAll.setIcon(iconCollapseAll);
        buttonCollapseBelow.setIcon(iconCollapseBelow);
        
        buttonWorkflowFindNext = new JButton("Find Next");
        buttonWorkflowFindNext.setEnabled(false);
        buttonWorkflowFindNext.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                findNextProcedure(treeWorkflowProcess);
                int k = workflowSearchIndex + 1;
                buttonWorkflowFind.setText(k+" / "+workflowSearchResults.size());
            }
        });
        
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
                            findProcedure(treeWorkflowProcess, workflowString, valueString);
                            
                            if (workflowSearchResults.size() == 0) {
                                JOptionPane.showMessageDialog(parentFrame, "No matches found", "No Matches Found", JOptionPane.WARNING_MESSAGE);
                                buttonWorkflowFindNext.setEnabled(false);
                                buttonWorkflowFindClear.setEnabled(false);
                                buttonWorkflowFind.setEnabled(true);
                                boxSearchWorkflowType.setEnabled(true);
                                textSearchValue.setEnabled(true);
                            } else {
                                int k = workflowSearchIndex + 1;
                                buttonWorkflowFind.setText(k+" / "+workflowSearchResults.size());
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
                workflowSearchResults = new ArrayList<TreePath>();
                workflowSearchIndex = 0;
                treeWorkflowProcess.clearSelection();
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
        toolBarWorkflowTreeTop.add(buttonExpandDependTask);
        toolBarWorkflowTreeTop.add(buttonExpandSubTask);
        
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
        panelAttributeDetails.setBorder(new TitledBorder(new EtchedBorder(),"Attribute Details"));
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
    
    private int workflowSearchIndex;
    private ArrayList<TreePath> workflowSearchResults;
    private final int SEARCH_CONDITION_VALUE = 0;
    private final int SEARCH_CONDITION = 1;
    private final int SEARCH_VALUE = 2;
    
    private boolean findNextProcedure(JTreeAdvanced tree) {
        workflowSearchIndex++;
        if(workflowSearchIndex >= workflowSearchResults.size())
            workflowSearchIndex = 0;
        
        if(workflowSearchResults.size() > 0) {
            tree.expandPath(workflowSearchResults.get(workflowSearchIndex));
            tree.setSelectionPath(workflowSearchResults.get(workflowSearchIndex));
            tree.scrollPathToVisible(workflowSearchResults.get(workflowSearchIndex));
            return true;
        } else
            return false;
    }
    
    private boolean findProcedure(JTreeAdvanced tree, String condition, String value) {
        workflowSearchResults = new ArrayList<TreePath>();
        workflowSearchIndex = 0;
        if((!condition.equals("")) && (!value.equals("")) )
            findProcedureItems(
                    tree,
                    tree.getPathForRow(0),
                    SEARCH_CONDITION_VALUE,
                    condition,
                    value);
        else if(!condition.equals(""))
            findProcedureItems(
                    tree,
                    tree.getPathForRow(0),
                    SEARCH_CONDITION,
                    condition,
                    value);
        else if(!value.equals(""))
            findProcedureItems(
                    tree,
                    tree.getPathForRow(0),
                    SEARCH_VALUE,
                    condition,
                    value);
        if(workflowSearchResults.size() > 0) {
            tree.expandPath(workflowSearchResults.get(workflowSearchIndex));
            tree.setSelectionPath(workflowSearchResults.get(workflowSearchIndex));
            tree.scrollPathToVisible(workflowSearchResults.get(workflowSearchIndex));
            return true;
        } else
            return false;
    }
    
    /*
     * Both condition and value cannot be empty, either one or the other.
     */
    private void findProcedureItems(JTreeAdvanced tree, TreePath parent, int searchBias, String condition, String value) {
        // Traverse children
        NodeReference nr = (NodeReference)parent.getLastPathComponent();
        Boolean matched = false;
        switch(searchBias) {
            case SEARCH_CONDITION_VALUE:
                matched = isMatched(nr.getClassType().value(), condition) & isMatched(nr.getName(), value);
                break;
            case SEARCH_CONDITION:
                matched = isMatched(nr.getClassType().value(), condition);
                break;
            case SEARCH_VALUE:
                matched = isMatched(nr.getName(), value);
                break;
            default:
                matched = false;
        }
        if(matched)
            workflowSearchResults.add(parent);
        
        int childCount = tree.getModel().getChildCount(nr);
        if(childCount > 0) {
            for (int e=0; e<childCount; e++ ) {
                TreePath path = parent.pathByAddingChild(tree.getModel().getChild(nr, e));
                findProcedureItems(tree, path, searchBias, condition, value);
            }
        }
    }
    
    private boolean isMatched(String s, String pattern) {
        if((pattern != null) && (!pattern.equals(""))){
            if((s != null) && (!s.equals(""))){
                return PatternMatch.isStringMatch(s, pattern);
            } else
                return false;
        } else
            return false;
    }
}
