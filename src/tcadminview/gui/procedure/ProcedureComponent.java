/*
 * ProcedureComponent.java
 *
 * Created on 20 July 2007, 11:46
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tcadminview.gui.procedure;

import tcadminview.ResourceLocator;
import tcadminview.utils.PatternMatch;
import tcadminview.gui.*;
import tcadminview.xml.DOMUtil;
import tcadminview.procedure.*;
import tcadminview.plmxmlpdm.type.*;
import tcadminview.plmxmlpdm.type.element.*;
import tcadminview.plmxmlpdm.classtype.*;
import tcadminview.plmxmlpdm.base.AttribOwnerBase;
import tcadminview.plmxmlpdm.TagTypeEnum;
import org.xml.sax.InputSource;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import javax.swing.tree.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
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
    protected JTree treeWorkflowProcess;
    protected JFrame parentFrame;
    protected ProcedureManager pm;
    
    
    
    /**
     * Creates a new instance of ProcedureComponent
     */
    public ProcedureComponent(JFrame parentFrame, ProcedureManager pm){
        this.pm = pm;
        this.parentFrame = parentFrame;
        
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1,2,Utilities.GAP_COMPONENT,Utilities.GAP_COMPONENT));
        panel.add(createWorkflowProcessPanel());
        panel.add(createNodeDetailsPanel());
        
        this.setLayout(new BorderLayout(Utilities.GAP_COMPONENT,Utilities.GAP_COMPONENT));
        this.add("Center",Utilities.createPanelMargined(panel));
    }
    
    private int workflowSearchIndex;
    private ArrayList<TreePath> workflowSearchResults;
    
    private boolean findNextRuleTreeItem() {
        workflowSearchIndex++;
        if(workflowSearchIndex >= workflowSearchResults.size())
            workflowSearchIndex = 0;
        
        if(workflowSearchResults.size() > 0) {
            treeWorkflowProcess.expandPath(workflowSearchResults.get(workflowSearchIndex));
            treeWorkflowProcess.setSelectionPath(workflowSearchResults.get(workflowSearchIndex));
            treeWorkflowProcess.scrollPathToVisible(workflowSearchResults.get(workflowSearchIndex));
            return true;
        } else
            return false;
    }
    
    private boolean findRuleTreeItem(String condition, String value) {
        workflowSearchResults = new ArrayList<TreePath>();
        workflowSearchIndex = 0;
        findRuleTreeItem(treeWorkflowProcess, treeWorkflowProcess.getPathForRow(0), condition, value);
        if(workflowSearchResults.size() > 0) {
            treeWorkflowProcess.expandPath(workflowSearchResults.get(workflowSearchIndex));
            treeWorkflowProcess.setSelectionPath(workflowSearchResults.get(workflowSearchIndex));
            treeWorkflowProcess.scrollPathToVisible(workflowSearchResults.get(workflowSearchIndex));
            return true;
        } else
            return false;
    }
    
    /*
     * Both condition and value cannot be empty, either one or the other.
     */
    private void findRuleTreeItem(JTree tree, TreePath parent, String condition, String value) {
        // Traverse children
        NodeReference nr = (NodeReference)parent.getLastPathComponent();
        Boolean matched = false;
        
        if(!matched)
            matched = isMatched(nr.getClassType().value(), condition);
        
        if(!matched)
            matched = isMatched(nr.getName(), value);
        
        if(matched)
            workflowSearchResults.add(parent);
        
        int childCount = tree.getModel().getChildCount(nr);
        //if (node.getChildCount() >= 0) {
        if(childCount > 0) {
            //for (Enumeration e=node.children(); e.hasMoreElements(); ) {
            for (int e=0; e<childCount; e++ ) {
                TreePath path = parent.pathByAddingChild(tree.getModel().getChild(nr, e));
                findRuleTreeItem(tree, path, condition, value);
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
    
    protected JButton buttonWorkflowFindNext;
    protected JButton buttonWorkflowFind;
    protected JButton buttonWorkflowFindClear;
    protected JComboBox boxSearchWorkflowType;
    protected JTextField textSearchValue;
    
    public JPanel createWorkflowProcessPanel() {
        
        // Workflow Process Tree
        treeWorkflowProcess = new JTree(new ProcedureTreeModel(pm));
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
                Utilities.expandTreeBranch(treeWorkflowProcess);
            }
        });
        JButton buttonCollapseBelow = new JButton();
        buttonCollapseBelow.setToolTipText("Collapse Below");
        buttonCollapseBelow.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                Utilities.collapseTreeBranch(treeWorkflowProcess);
            }
        });
        
        ImageIcon iconExpandBelow = new ImageIcon();
        ImageIcon iconCollapseBelow = new ImageIcon();
        try {
            iconExpandBelow = new  ImageIcon(ResourceLocator.getButtonImage("Expand-Below.gif"));
            iconCollapseBelow = new  ImageIcon(ResourceLocator.getButtonImage("Collapse-Below.gif"));
        } catch (Exception e) {
            System.out.println("Couldn't load images: " + e);
        }
        
        buttonExpandBelow.setIcon(iconExpandBelow);
        buttonCollapseBelow.setIcon(iconCollapseBelow);
        
        buttonWorkflowFindNext = new JButton("Find Next");
        buttonWorkflowFindNext.setEnabled(false);
        buttonWorkflowFindNext.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                findNextRuleTreeItem();
                int k = workflowSearchIndex + 1;
                buttonWorkflowFind.setText(k+" / "+workflowSearchResults.size());
            }
        });
        
        buttonWorkflowFind = new JButton("Find");
        buttonWorkflowFind.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                String workflowString = "";
                String valueString = "";
                
                if(boxSearchWorkflowType.getSelectedIndex() > 0)
                    workflowString = ProcedureManager.WORKFLOW_TYPES[boxSearchWorkflowType.getSelectedIndex() - 1].value();
                valueString = textSearchValue.getText();
                
                if( (workflowString.equals(""))  &&
                        ((valueString == null) || (valueString.equals(""))))
                    JOptionPane.showMessageDialog(parentFrame, "Search requires either a Workflow Type, value/name or any combination.", "No Search Criteria", JOptionPane.ERROR_MESSAGE);
                else {
                    findRuleTreeItem(workflowString, valueString);
                    
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
        
        JToolBar toolBarWorkflowTree = new JToolBar();
        toolBarWorkflowTree.setMargin(new Insets(
                Utilities.GAP_INSET,
                Utilities.GAP_INSET,
                Utilities.GAP_INSET,
                Utilities.GAP_INSET));
        toolBarWorkflowTree.setFloatable(false);
        toolBarWorkflowTree.add(buttonExpandBelow);
        toolBarWorkflowTree.add(buttonCollapseBelow);
        toolBarWorkflowTree.addSeparator();
        toolBarWorkflowTree.add(boxSearchWorkflowType);
        toolBarWorkflowTree.addSeparator();
        toolBarWorkflowTree.add(textSearchValue);
        toolBarWorkflowTree.addSeparator();
        toolBarWorkflowTree.add(buttonWorkflowFind);
        toolBarWorkflowTree.add(buttonWorkflowFindNext);
        toolBarWorkflowTree.add(buttonWorkflowFindClear);
        
        JPanel panelWorkflowProcess =  new JPanel();
        panelWorkflowProcess.setLayout(new BorderLayout(Utilities.GAP_COMPONENT,Utilities.GAP_COMPONENT));
        panelWorkflowProcess.add("Center",scrollTreeWorkflowProcess);
        panelWorkflowProcess.add("South",toolBarWorkflowTree);
        
        
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(Utilities.GAP_COMPONENT,Utilities.GAP_COMPONENT));
        panel.setBorder(new TitledBorder(new EtchedBorder(),"Workflow Templates: PROCESS"));
        panel.add(Utilities.createPanelMargined(panelWorkflowProcess));
        
        return panel;
    }
    
    public boolean isEmptyPanel(){
        return (pm.getWorkflowTemplates().size() == 0);
    }
    
    protected JTree treeAttributes;
    protected JTableAdvanced tableProcedure;
    protected JTableAdvanced tableAttribute;
    protected JRadioButton radioExpand;
    
    private JPanel createNodeDetailsPanel() {
        treeAttributes = new JTree(new AttributeTreeModel());
        treeAttributes.setRootVisible(true);
        treeAttributes.addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent e) {
                TreePath path = e.getPath();
                NodeReference nr = (NodeReference)path.getLastPathComponent();
                updateAttribDetails(nr);
            }
        });
        treeAttributes.setCellRenderer(new ProcedureTreeCellRenderer());
        JScrollPane scrollTreeAttributes = new JScrollPane();
        scrollTreeAttributes.setPreferredSize(new Dimension(200,220));
        scrollTreeAttributes.setBorder(new BevelBorder(BevelBorder.LOWERED));
        scrollTreeAttributes.getViewport().add(treeAttributes);
        
        JButton buttonExpandAll = new JButton();
        buttonExpandAll.setToolTipText("Expand All");
        buttonExpandAll.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                Utilities.expandTree(treeAttributes);
            }
        });
        JButton buttonExpandBelow = new JButton();
        buttonExpandBelow.setToolTipText("Expand Below");
        buttonExpandBelow.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                Utilities.expandTreeBranch(treeAttributes);
            }
        });
        JButton buttonCollapseAll = new JButton();
        buttonCollapseAll.setToolTipText("Collapse All");
        buttonCollapseAll.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                Utilities.collapseTree(treeAttributes);
            }
        });
        JButton buttonCollapseBelow = new JButton();
        buttonCollapseBelow.setToolTipText("Collapse Below");
        buttonCollapseBelow.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                Utilities.collapseTreeBranch(treeAttributes);
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
        JRadioButton radioCollapse = new JRadioButton("Collapsed");
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(radioExpand);
        buttonGroup.add(radioCollapse);
        buttonGroup.setSelected(radioExpand.getModel(),true);
        
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
        panelAttributeInner.add("South",toolBarAttributeTree);
        
        
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
        
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(Utilities.GAP_COMPONENT,Utilities.GAP_COMPONENT));
        panel.add("Center",panelAttributes);
        panel.add("South",panelAttributeDetails);
        
        return panel;
    }
    
    private void updateNodeDetails(NodeReference nr) {
        tableAttribute.setModel(new NodeTableModel(nr, pm));
        Utilities.packColumns(tableAttribute, 2);
        treeAttributes.setModel(new AttributeTreeModel(nr,pm));
        if(radioExpand.isSelected())
            Utilities.expandTree(treeAttributes);
    }
    
    private void updateAttribDetails(NodeReference nr){
        tableAttribute.setModel(new NodeTableModel(nr, pm));
        Utilities.packColumns(tableAttribute, 2);
    }
    
}
