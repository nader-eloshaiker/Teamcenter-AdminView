/*
 * AccessManagerComponent.java
 *
 * Created on 29 June 2007, 11:07
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.gm.tcae.ruletree.gui;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import javax.swing.table.*;
import javax.swing.tree.*;
import com.gm.tcae.ruletree.acl.*;

/**
 *
 * @author NZR4DL
 */
public class AccessManagerComponent extends JPanel {
    
    protected JTree ruletreeComponent;
    protected JTable namedAclComponent;
    protected JTable accessRuleComponent;
    protected AccessRuleTableModel accessRuleDataModel;
    protected DefaultTreeModel ruletreeModel;
    protected NamedRuleSortedTableModel namedRuleDataModel;
    
    protected JComboBox listUnusedNamedACL;
    protected JComboBox boxFirstSort;
    protected JComboBox boxSecondSort;
    protected JComboBox boxThirdSort;
    protected JCheckBox checkAscending;
    
    protected boolean eventAllowed = true;
    
    private AccessManager accessManager;
    private static int GAP_COMPONENT = 5;
    private static int GAP_MARGIN = 5;
    
    /**
     * Creates a new instance of AccessManagerComponent
     */
    public AccessManagerComponent(AccessManager am) {
        super();
        this.accessManager = am;
        
        createAccessRuleComponent();
        createRuletreeComponent();
        createNamedAclTable();
        
        
        /* Enable tool tips for the ruletreeComponent, without this tool tips will not be picked up. */
        ToolTipManager.sharedInstance().registerComponent(ruletreeComponent);
        
        /* Rule Tree Panel */
        JScrollPane treeScroll = new JScrollPane();
        treeScroll.setPreferredSize(new Dimension(550,380));
        treeScroll.setBorder(new BevelBorder(BevelBorder.LOWERED));
        treeScroll.getViewport().add(ruletreeComponent);
        JPanel panelRuleTree =  new JPanel(true);
        panelRuleTree.setLayout(new GridLayout(1,1));
        panelRuleTree.setBorder(new TitledBorder(new EtchedBorder(),"Access Manager Tree"));
        panelRuleTree.add(createMarginedPanel(treeScroll));
        

        
        /* Acccess Control Panel */
        JScrollPane accessRuleComponentScroll = new JScrollPane();
        accessRuleComponentScroll.setPreferredSize(new Dimension(900,150));
        accessRuleComponentScroll.getViewport().add(accessRuleComponent);
        accessRuleComponentScroll.setBorder(new BevelBorder(BevelBorder.LOWERED));
        JPanel panelRuleTable =  new JPanel(true);
        panelRuleTable.setLayout(new GridLayout(1,1));
        panelRuleTable.setBorder(new TitledBorder(new EtchedBorder(),"Access Control"));
        panelRuleTable.add(createMarginedPanel(accessRuleComponentScroll));
        
        /* Rules Panel */
        JPanel panelRule =  new JPanel(true);
        panelRule.setLayout(new BorderLayout(GAP_MARGIN,GAP_MARGIN));
        panelRule.add("East", createNamedACLPanel());
        panelRule.add("Center",panelRuleTree);
        
        /* And show it. */
        this.setLayout(new BorderLayout(GAP_MARGIN,GAP_MARGIN));
        this.setBorder(new EmptyBorder(GAP_MARGIN,GAP_MARGIN,GAP_MARGIN,GAP_MARGIN));
        this.add("Center", panelRule);
        this.add("South",panelRuleTable);
        
    }
    
    public AccessManagerComponent() {
        this(new AccessManager());
    }
    
    
    public boolean isAccessManagerLoaded() {
        return (accessManager.getAccessManagerTreeSize() > 0);
    }
    
    public AccessManager getAccessManager() {
        return accessManager;
    }
    
    private void updateAccessRuleComponent(AccessRule ar) {
        accessRuleDataModel = new AccessRuleTableModel(accessManager.getAccessControlColumns(),ar);
        accessRuleComponent.setModel(accessRuleDataModel);
        accessRuleComponent.setRowSelectionAllowed(true);
        TableColumn column;
        for (int i=0; i<accessRuleDataModel.getColumnCount(); i++){
            column = accessRuleComponent.getColumnModel().getColumn(i);
            column.setHeaderValue(accessRuleDataModel.getColumn(i));
            column.setHeaderRenderer(new AccessRuleTableHearderRenderer());
            column.setCellRenderer(new AccessRuleTableCellRenderer());
            if(i == 0 || i == 1) {
                column.setResizable(true);
                column.setPreferredWidth(80);
            } else {
                column.setResizable(false);
                column.setPreferredWidth(28);
                column.setMaxWidth(28);
                column.setMinWidth(28);
                column.setWidth(28);
            }
        }
    }
    
    public void updateRuletreeComponent() {
        DefaultMutableTreeNode root;
        if(accessManager.getAccessManagerTreeSize()!=0)
            ruletreeModel.setRoot(TreeNodeBuilder.getTreeNodes(accessManager.getAccessManagerTree()));
    }
    
    private void createAccessRuleComponent() {
        accessRuleComponent = new JTable();
    }
    
    private void createRuletreeComponent() {
        
        DefaultMutableTreeNode root;
        if(accessManager.getAccessManagerTreeSize()!=0)
            root = TreeNodeBuilder.getTreeNodes(accessManager.getAccessManagerTree());
        else
            root = null;
        ruletreeModel = new DefaultTreeModel(root);
        ruletreeComponent = new JTree(ruletreeModel);
        ruletreeComponent.setCellRenderer(new RuleTreeNodeRenderer());
        ruletreeComponent.addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent e) {
                //if(eventAllowed){
                //eventAllowed = false;
                
                TreePath path = e.getPath();
                DefaultMutableTreeNode  nodes = (DefaultMutableTreeNode)path.getLastPathComponent();
                AccessManagerItem amItem = (AccessManagerItem)nodes.getUserObject();
                if(e.isAddedPath(path) && amItem.getAccessRuleListIndex() != -1 ){
                    int index = namedRuleDataModel.getModelIndex(amItem.getAccessRuleListIndex());
                    namedAclComponent.setRowSelectionInterval(index,index);
                    namedAclComponent.getSelectionModel().setAnchorSelectionIndex(index);
                    namedAclComponent.scrollRectToVisible(
                            namedAclComponent.getCellRect(
                            namedAclComponent.getSelectionModel().getAnchorSelectionIndex(),
                            namedAclComponent.getColumnModel().getSelectionModel().getAnchorSelectionIndex(),
                            false)
                            );
                } else
                    accessRuleDataModel.setAccessRule(null);
                
                //eventAllowed = true;
                //}
            }
        });
    }
    
    private void createNamedAclTable() {
        namedAclComponent = new JTable();
        namedRuleDataModel = new NamedRuleSortedTableModel(accessManager.getAccessRuleList());
        namedRuleDataModel.sortByColumns(new int[]{0,2});
        namedAclComponent.setModel(namedRuleDataModel);
        namedAclComponent.setRowSelectionAllowed(true);
        namedAclComponent.setSelectionMode(0);
        namedAclComponent.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        namedAclComponent.setIntercellSpacing(new Dimension(0,0));
        namedAclComponent.setShowGrid(false);
        TableColumn column;
        for (int i=0; i<namedRuleDataModel.getColumnCount(); i++){
            column = namedAclComponent.getColumnModel().getColumn(i);
            column.setHeaderRenderer(new NamedRuleTableHearderRenderer());
            column.setCellRenderer(new NamedRuleTableCellRenderer());
            if(i == 0 || i == 1) {
                column.setResizable(false);
                column.setPreferredWidth(28);
                column.setMaxWidth(28);
                column.setMinWidth(28);
                column.setWidth(28);
            } else {
                column.setResizable(true);
                column.setPreferredWidth(80);
            }
        }
        
        namedAclComponent.getSelectionModel().addListSelectionListener( new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                //if(eventAllowed) {
                //eventAllowed = false;
                //ruletreeComponent.setSelectionRows(accessManager.getAccessRuleList().elementAt(namedAclComponent.getSelectedRow()).getTreeIndex());
                //eventAllowed = true;
                //}
                updateAccessRuleComponent(accessManager.getAccessRuleList().elementAt(namedRuleDataModel.getDataIndex(namedAclComponent.getSelectedRow())));
            }
        });
    }
    
    private JProgressBar createNamedACLDetailsComponent(int minValue, int maxValue, int value, String maxTitle) {
        /*
        JSlider slider = new JSlider(minValue,maxValue,value);
        slider.setPaintTicks(true);
        slider.setMajorTickSpacing(maxValue);
        slider.setPaintLabels(true);
        if(maxValue != 0) {
            Hashtable<Integer, JLabel> table = new Hashtable<Integer,JLabel>();
            table.put(new Integer(minValue), new JLabel(String.valueOf(minValue)));
            table.put(new Integer(value), new JLabel(String.valueOf(value)));
            table.put(new Integer(maxValue), new JLabel(String.valueOf(maxValue)));
            slider.setLabelTable(table);
        }
         */
        JProgressBar jp = new JProgressBar(minValue,maxValue);
        jp.setValue(value);
        jp.setToolTipText("Total "+maxTitle+": " + maxValue);
        jp.setString(String.valueOf(value));
        jp.setStringPainted(true);
        return jp;
        
    }
    
    public JPanel createNamedACLPanel() {
        /* Named ACL List Panel */
        JScrollPane namedAclComponentScroll = new JScrollPane();
        namedAclComponentScroll.setPreferredSize(new Dimension(400,250));
        namedAclComponentScroll.getViewport().add(namedAclComponent);
        namedAclComponentScroll.setBorder(new BevelBorder(BevelBorder.LOWERED));
        JPanel panelRuleList =  new JPanel(true);
        panelRuleList.setLayout(new GridLayout(1,1));
        panelRuleList.add(namedAclComponentScroll);
        
        
        JPanel panelNamedACLDetails = new JPanel(true);
        panelNamedACLDetails.setLayout(new BorderLayout(GAP_COMPONENT,GAP_COMPONENT));
        JPanel panelNamedACLDetailsLeft = new JPanel(true);
        panelNamedACLDetailsLeft.setLayout(new GridLayout(2,1,GAP_COMPONENT,GAP_COMPONENT));
        panelNamedACLDetailsLeft.add(new JLabel("RuleTree Access Rules"));
        panelNamedACLDetailsLeft.add(new JLabel("Workflow Access Rules"));
        JPanel panelNamedACLDetailsRight = new JPanel(true);
        panelNamedACLDetailsRight.setLayout(new GridLayout(2,1,GAP_COMPONENT,GAP_COMPONENT));
        panelNamedACLDetailsRight.add(createNamedACLDetailsComponent(
                0,
                accessManager.getAccessRuleList().size(),
                accessManager.getAccessRuleList().getTreeTypeSize(),
                "Access Rules"));
        panelNamedACLDetailsRight.add(createNamedACLDetailsComponent(
                0,
                accessManager.getAccessRuleList().size(),
                accessManager.getAccessRuleList().getWorkFlowTypeSize(),
                "Access Rules"));
        panelNamedACLDetails.add("West",panelNamedACLDetailsLeft);
        panelNamedACLDetails.add("Center",panelNamedACLDetailsRight);
        JPanel panelNamedACLMissing = new JPanel(true);
        panelNamedACLMissing.setLayout(new BorderLayout(GAP_COMPONENT,GAP_COMPONENT));
        panelNamedACLMissing.add("West",new JLabel("Total Unused Access Rules"));
        panelNamedACLMissing.add("Center",createNamedACLDetailsComponent(
                0,
                accessManager.getAccessRuleList().size(),
                accessManager.getUnusedRulesSize(),
                "Access Rules"));
        listUnusedNamedACL = new JComboBox();
        if (accessManager.getUnusedRulesSize() == 0)
            listUnusedNamedACL.setEnabled(false);
        else
            for(int z=0; z<accessManager.getUnusedRulesSize(); z++)
                listUnusedNamedACL.addItem(accessManager.getAccessRuleList().elementAt(accessManager.unusedElementAt(z)).getRuleName());
        listUnusedNamedACL.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int unusedIndex = listUnusedNamedACL.getSelectedIndex();
                int unsortedIndex = accessManager.unusedElementAt(unusedIndex);
                int sortedIndex = namedRuleDataModel.getModelIndex(unsortedIndex);
                namedAclComponent.setRowSelectionInterval(sortedIndex,sortedIndex);
                namedAclComponent.getSelectionModel().setAnchorSelectionIndex(sortedIndex);
                namedAclComponent.scrollRectToVisible(
                        namedAclComponent.getCellRect(
                        namedAclComponent.getSelectionModel().getAnchorSelectionIndex(),
                        namedAclComponent.getColumnModel().getSelectionModel().getAnchorSelectionIndex(),
                        false));
            }
        });
        JPanel panelNamedACLMissingFull = new JPanel(true);
        panelNamedACLMissingFull.setLayout(new GridLayout(2,1,GAP_COMPONENT,GAP_COMPONENT));
        panelNamedACLMissingFull.add("Center",panelNamedACLMissing);
        panelNamedACLMissingFull.add("South",listUnusedNamedACL);
        
        
        boxFirstSort = new JComboBox(NamedRuleSortedTableModel.SORT_COLUMN_SELECTION);
        boxFirstSort.setSelectedIndex(namedRuleDataModel.getSortColumn(0));
        boxSecondSort = new JComboBox(NamedRuleSortedTableModel.SORT_COLUMN_SELECTION);
        boxSecondSort.setSelectedIndex(namedRuleDataModel.getSortColumn(1));
        boxThirdSort = new JComboBox(NamedRuleSortedTableModel.SORT_COLUMN_SELECTION);
        boxThirdSort.setSelectedIndex(namedRuleDataModel.getSortColumn(2));
        checkAscending = new JCheckBox("Ascending",namedRuleDataModel.isAscending());
        JButton buttonSortNamedACL = new JButton("Sort");
        buttonSortNamedACL.addActionListener(new ActionListener() {
             public void actionPerformed(ActionEvent e) {
                 int[] sort = new int[3];
                 sort[0] = boxFirstSort.getSelectedIndex();
                 sort[1] = boxSecondSort.getSelectedIndex();
                 sort[2] = boxThirdSort.getSelectedIndex();
                 namedRuleDataModel.sortByColumns(sort, checkAscending.isSelected());
                 namedRuleDataModel.fireTableDataChanged();
             }
        });
        
        JPanel panelNamedACLSort = new JPanel(true);
        panelNamedACLSort.setLayout(new GridLayout(2,3,GAP_COMPONENT,GAP_COMPONENT));
        panelNamedACLSort.add(boxFirstSort);
        panelNamedACLSort.add(boxSecondSort);
        panelNamedACLSort.add(boxThirdSort);
        panelNamedACLSort.add(new JPanel(true));
        panelNamedACLSort.add(checkAscending);
        panelNamedACLSort.add(buttonSortNamedACL);
        
        
        
        JTabbedPane tabNamedAcl = new JTabbedPane();
        tabNamedAcl.add("Details",createMarginedPanel(panelNamedACLDetails));
        tabNamedAcl.add("Unused ACLs",createMarginedPanel(panelNamedACLMissingFull));
        tabNamedAcl.add("Sort",createMarginedPanel(panelNamedACLSort));
        tabNamedAcl.add("Filter",new JButton("hello"));
        
        
        JPanel panelACL = new JPanel(true);
        panelACL.setLayout(new BorderLayout(GAP_COMPONENT,GAP_COMPONENT));
        panelACL.add("Center",panelRuleList);
        panelACL.add("South",tabNamedAcl);
        
        JPanel panel = new JPanel(true);
        panel.setLayout(new GridLayout(1,1));
        panel.setBorder(new TitledBorder(new EtchedBorder(), "Named ACL"));
        panel.add(createMarginedPanel(panelACL));
        
        return panel;
        
    }
    
    private JPanel createMarginedPanel(JComponent component) {
        JPanel SpacedPanel = new JPanel(true);
        SpacedPanel.setLayout(new GridLayout(1,1,GAP_COMPONENT,GAP_COMPONENT));
        SpacedPanel.setBorder(new EmptyBorder(GAP_MARGIN,GAP_MARGIN,GAP_MARGIN,GAP_MARGIN));
        SpacedPanel.add(component);

        return SpacedPanel;
    }
    
}
