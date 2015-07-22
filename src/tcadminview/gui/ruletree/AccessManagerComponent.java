package tcadminview.gui.ruletree;
/*
 * AccessManagerComponent.java
 *
 * Created on 29 June 2007, 11:07
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

import tcadminview.gui.*;
import tcadminview.gui.Utilities;
import tcadminview.ruletree.AccessManager;
import tcadminview.ruletree.AccessManagerItem;
import tcadminview.ruletree.AccessRule;
import tcadminview.utils.*;
import tcadminview.ResourceLocator;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import javax.swing.table.*;
import javax.swing.tree.*;

/**
 *
 * @author NZR4DL
 */
public class AccessManagerComponent extends JPanel implements TabbedPanel {
    
    protected JFrame parentFrame;
    protected JTree treeRuleTree;
    protected JTableAdvanced tableNamedACL;
    protected JTableAdvanced tableAccessRule;
    protected AccessRuleTableModel tableDataAccessRule;
    protected DefaultTreeModel treeDataRuleTree;
    protected NamedRuleFilterSortTableModel tableDataFilterSortNamedACL;
    
    
    protected JComboBox listUnusedNamedACL;
    protected JComboBox boxFirstSort;
    protected JComboBox boxSecondSort;
    protected JComboBox boxThirdSort;
    protected JCheckBox checkAscending;
    protected JTextField textFilterName;
    protected JTextField textFilterInstanceCount;
    protected JComboBox boxfilterType;
    protected JTree treeReferences;
    
    
    protected JTextField textSearchValue;
    protected JComboBox boxSearchCondition;
    protected JButton buttonRuleTreeFindNext;
    protected JButton buttonRuleTreeFind;
    protected JButton buttonRuleTreeFindClear;
    
    
    private AccessManager accessManager;
    private ArrayList<TreePath> ruleTreeSearchResults = new ArrayList<TreePath>();
    private int ruleTreeSearchIndex = 0;
    
    /**
     * Creates a new instance of AccessManagerComponent
     */
    public AccessManagerComponent(JFrame parentFrame, AccessManager am) {
        super();
        this.parentFrame =parentFrame;
        this.accessManager = am;
        
        createTableAccessControl();
        treeRuleTree = createTreeRuleTree();
        tableNamedACL = createTableNamedACL();
        
        /* Enable tool tips for the treeRuleTree, without this tool tips will not be picked up. */
        ToolTipManager.sharedInstance().registerComponent(treeRuleTree);
        
        /* Acccess Control Panel */
        JScrollPane accessRuleComponentScroll = new JScrollPane();
        accessRuleComponentScroll.setPreferredSize(new Dimension(980,150));
        accessRuleComponentScroll.getViewport().add(tableAccessRule);
        accessRuleComponentScroll.setBorder(new BevelBorder(BevelBorder.LOWERED));
        JPanel panelRuleTable =  new JPanel();
        panelRuleTable.setLayout(new GridLayout(1,1));
        panelRuleTable.setBorder(new TitledBorder(new EtchedBorder(),"Access Control"));
        panelRuleTable.add(Utilities.createPanelMargined(accessRuleComponentScroll));
        
        /* Rules Panel */
        JPanel panelRule =  new JPanel();
        panelRule.setLayout(new BorderLayout(Utilities.GAP_MARGIN,Utilities.GAP_MARGIN));
        panelRule.add("East", createPanelNamedACL());
        panelRule.add("Center",createPanelRuleTree());
        
        JSplitPane splitPane = new JSplitPane(
                JSplitPane.VERTICAL_SPLIT,
                true,
                Utilities.createPanelMargined(panelRule),
                Utilities.createPanelMargined(panelRuleTable));
        splitPane.setResizeWeight(0.6);
        splitPane.setOneTouchExpandable(true);
        
        /* And show it. */
        this.setLayout(new BorderLayout());//Utilities.GAP_MARGIN,Utilities.GAP_MARGIN));
        this.add("Center",splitPane);
        
    }
    
    public AccessManagerComponent(JFrame parentFrame) {
        this(parentFrame, new AccessManager());
    }
    
    public boolean isEmptyPanel() {
        return (accessManager.getAccessManagerTreeSize() == 0);
    }
    
    public AccessManager getAccessManager() {
        return accessManager;
    }
    
    private void updateTableAccessControl() {
        updateTableAccessControl(new AccessRule());
    }
    
    private void updateTableAccessControl(AccessRule ar) {
        tableDataAccessRule = new AccessRuleTableModel(accessManager.getAccessControlColumns(),ar);
        tableAccessRule.setModel(tableDataAccessRule);
        tableAccessRule.setRowSelectionAllowed(true);
        TableColumn column;
        for (int i=0; i<tableAccessRule.getColumnCount(); i++){
            column = tableAccessRule.getColumnModel().getColumn(i);
            column.setHeaderValue(tableDataAccessRule.getColumn(i));
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
    
    private void updateReferences(AccessRule ar) {
        DefaultMutableTreeNode root;
        if(ar.getTreeIndexSize() > 0)
            root = RuleTreeNodeBuilder.getRuleTreePathsForRule(accessManager.getAccessManagerTree(),ar);
        else
            root = null;
        
        ((DefaultTreeModel)treeReferences.getModel()).setRoot(root);
        treeReferences.repaint();
    }
    
    private void createTableAccessControl() {
        tableAccessRule = new JTableAdvanced();
        updateTableAccessControl();
    }
    
    private JTree createTreeRuleTree() {
        /*
        DefaultMutableTreeNode root;
        if(accessManager.getAccessManagerTreeSize()!=0)
            root = RuleTreeNodeBuilder.getRuleTreeNodes(accessManager.getAccessManagerTree());
        else
            root = null;
        treeDataRuleTree = new DefaultTreeModel(root);
         **/
        JTree tree = new JTree(new RuleTreeModel(accessManager.getAccessManagerTree()));
        tree.setCellRenderer(new RuleTreeNodeRenderer());
        tree.addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent e) {
                TreePath path = e.getPath();
                AccessManagerItem amItem = (AccessManagerItem)path.getLastPathComponent();//nodes.getUserObject();
                if(e.isAddedPath(path) && amItem.getAccessRuleListIndex() != -1 ){
                    int index = tableDataFilterSortNamedACL.getModelIndex(amItem.getAccessRuleListIndex());
                    if(index > -1) {
                        tableNamedACL.setRowSelectionInterval(index,index);
                        tableNamedACL.getSelectionModel().setAnchorSelectionIndex(index);
                        tableNamedACL.scrollRectToVisible(
                                tableNamedACL.getCellRect(
                                tableNamedACL.getSelectionModel().getAnchorSelectionIndex(),
                                tableNamedACL.getColumnModel().getSelectionModel().getAnchorSelectionIndex(),
                                false)
                                );
                    } else
                        updateTableAccessControl(accessManager.getAccessRuleList().elementAt(amItem.getAccessRuleListIndex()));
                } else
                    updateTableAccessControl();
                
            }
        });
        
        return tree;
    }
    
    private JTableAdvanced createTableNamedACL() {
        JTableAdvanced table = new JTableAdvanced();
        tableDataFilterSortNamedACL = new NamedRuleFilterSortTableModel(accessManager.getAccessRuleList());
        tableDataFilterSortNamedACL.setSort(new int[]{0,2},true);
        table.setModel(tableDataFilterSortNamedACL);
        table.setRowSelectionAllowed(true);
        table.setSelectionMode(0);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        table.setIntercellSpacing(new Dimension(0,0));
        table.setShowGrid(false);
        TableColumn column;
        for (int i=0; i<tableDataFilterSortNamedACL.getColumnCount(); i++){
            column = table.getColumnModel().getColumn(i);
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
        
        table.getSelectionModel().addListSelectionListener( new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                /* is something selected */
                int i = tableNamedACL.getSelectedRow();
                if (i > -1)
                    i = tableDataFilterSortNamedACL.getDataIndex(tableNamedACL.getSelectedRow());
                else
                    updateTableAccessControl();
                
                /* can it be displayed if a filter has been applied */
                if (i > -1) {
                    updateTableAccessControl(accessManager.getAccessRuleList().elementAt(i));
                    updateReferences(accessManager.getAccessRuleList().elementAt(i));
                } else
                    updateTableAccessControl();
            }
        });
        return table;
    }
    
    private boolean findNextRuleTreeItem() {
        ruleTreeSearchIndex++;
        if(ruleTreeSearchIndex >= ruleTreeSearchResults.size())
            ruleTreeSearchIndex = 0;
        
        if(ruleTreeSearchResults.size() > 0) {
            treeRuleTree.expandPath(ruleTreeSearchResults.get(ruleTreeSearchIndex));
            treeRuleTree.setSelectionPath(ruleTreeSearchResults.get(ruleTreeSearchIndex));
            treeRuleTree.scrollPathToVisible(ruleTreeSearchResults.get(ruleTreeSearchIndex));
            return true;
        } else
            return false;
    }
    
    private boolean findRuleTreeItem(String condition, String value) {
        ruleTreeSearchResults = new ArrayList<TreePath>();
        ruleTreeSearchIndex = 0;
        findRuleTreeItem(treeRuleTree, treeRuleTree.getPathForRow(0), condition, value);
        if(ruleTreeSearchResults.size() > 0) {
            treeRuleTree.expandPath(ruleTreeSearchResults.get(ruleTreeSearchIndex));
            treeRuleTree.setSelectionPath(ruleTreeSearchResults.get(ruleTreeSearchIndex));
            treeRuleTree.scrollPathToVisible(ruleTreeSearchResults.get(ruleTreeSearchIndex));
            return true;
        } else
            return false;
    }
    
    /*
     * Both condition and value cannot be empty, either one or the other.
     */
    private void findRuleTreeItem(JTree tree, TreePath parent, String condition, String value) {
        // Traverse children
        AccessManagerItem amItem = (AccessManagerItem)parent.getLastPathComponent();
        Boolean matched = false;
        
        if(!matched)
            matched = isMatched(amItem.getCondition(), condition);
        
        if(!matched)
            matched = isMatched(amItem.getValue(), value);
        
        if(!matched)
            matched = isMatched(amItem.getAccessRuleName(), value);
        
        if(matched)
            ruleTreeSearchResults.add(parent);
        
        int childCount = tree.getModel().getChildCount(amItem);
        //if (node.getChildCount() >= 0) {
        if(childCount > 0) {
            //for (Enumeration e=node.children(); e.hasMoreElements(); ) {
            for (int e=0; e<childCount; e++ ) {
                TreePath path = parent.pathByAddingChild(tree.getModel().getChild(amItem, e));
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
    
    
    private JPanel createPanelRuleTree() {
        JScrollPane treeScroll = new JScrollPane();
        treeScroll.setPreferredSize(new Dimension(550,340));
        treeScroll.setBorder(new BevelBorder(BevelBorder.LOWERED));
        treeScroll.getViewport().add(treeRuleTree);
        
        JButton buttonExpandAll = new JButton();
        buttonExpandAll.setToolTipText("Expand All");
        buttonExpandAll.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                Utilities.expandTree(treeRuleTree);
            }
        });
        JButton buttonExpandBelow = new JButton();
        buttonExpandBelow.setToolTipText("Expand Below");
        buttonExpandBelow.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                Utilities.expandTreeBranch(treeRuleTree);
            }
        });
        JButton buttonCollapseAll = new JButton();
        buttonCollapseAll.setToolTipText("Collapse All");
        buttonCollapseAll.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                Utilities.collapseTree(treeRuleTree);
            }
        });
        JButton buttonCollapseBelow = new JButton();
        buttonCollapseBelow.setToolTipText("Collapse Below");
        buttonCollapseBelow.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                Utilities.collapseTreeBranch(treeRuleTree);
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
        
        buttonRuleTreeFindNext = new JButton("Find Next");
        buttonRuleTreeFindNext.setEnabled(false);
        buttonRuleTreeFindNext.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                findNextRuleTreeItem();
                int k = ruleTreeSearchIndex + 1;
                buttonRuleTreeFind.setText(k+" / "+ruleTreeSearchResults.size());
            }
        });
        
        buttonRuleTreeFind = new JButton("Find");
        buttonRuleTreeFind.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                String conditionString = "";
                String valueString = "";
                
                if(boxSearchCondition.getSelectedIndex() > 0)
                    conditionString = (String)boxSearchCondition.getSelectedItem();
                valueString = textSearchValue.getText();
                
                if( (conditionString.equals("")) &&
                        ((valueString == null) || (valueString.equals(""))) )
                    JOptionPane.showMessageDialog(parentFrame, "Search requires either a condition, value/ACL or any combination.", "No Search Criteria", JOptionPane.ERROR_MESSAGE);
                else {
                    findRuleTreeItem(conditionString, valueString);
                    
                    if (ruleTreeSearchResults.size() == 0) {
                        JOptionPane.showMessageDialog(parentFrame, "No matches found", "No Matches Found", JOptionPane.WARNING_MESSAGE);
                        buttonRuleTreeFindNext.setEnabled(false);
                        buttonRuleTreeFindClear.setEnabled(false);
                        buttonRuleTreeFind.setEnabled(true);
                        boxSearchCondition.setEnabled(true);
                        textSearchValue.setEnabled(true);
                    } else {
                        int k = ruleTreeSearchIndex + 1;
                        buttonRuleTreeFind.setText(k+" / "+ruleTreeSearchResults.size());
                        buttonRuleTreeFindNext.setEnabled(true);
                        buttonRuleTreeFindClear.setEnabled(true);
                        buttonRuleTreeFind.setEnabled(false);
                        boxSearchCondition.setEnabled(false);
                        textSearchValue.setEnabled(false);
                    }
                }
            }
        });
        
        buttonRuleTreeFindClear = new JButton("Clear");
        buttonRuleTreeFindClear.setEnabled(false);
        buttonRuleTreeFindClear.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                buttonRuleTreeFind.setText("Find");
                buttonRuleTreeFindNext.setEnabled(false);
                buttonRuleTreeFindClear.setEnabled(false);
                buttonRuleTreeFind.setEnabled(true);
                boxSearchCondition.setEnabled(true);
                textSearchValue.setEnabled(true);
                boxSearchCondition.setSelectedIndex(0);
                textSearchValue.setText("");
                ruleTreeSearchResults = new ArrayList<TreePath>();
                ruleTreeSearchIndex = 0;
                treeRuleTree.clearSelection();
            }
        });
        
        boxSearchCondition = new JComboBox();
        boxSearchCondition.setToolTipText("Ruletree Condition");
        if (accessManager.getAccessManagerTree().getConditionSize() == 0) {
            boxSearchCondition.setEnabled(false);
            boxSearchCondition.addItem("Condition");
        } else {
            boxSearchCondition.addItem("");
            for(int x=0; x<accessManager.getAccessManagerTree().getConditionSize(); x++)
                boxSearchCondition.addItem(accessManager.getAccessManagerTree().getCondition(x));
        }
        
        textSearchValue = new JTextField();
        textSearchValue.setToolTipText("Ruletree Value: * ? [ - ] accepted");
        textSearchValue.setColumns(6);
        
        
        JToolBar toolBarRuletree = new JToolBar();
        toolBarRuletree.setMargin(new Insets(
                Utilities.GAP_INSET,
                Utilities.GAP_INSET,
                Utilities.GAP_INSET,
                Utilities.GAP_INSET));
        toolBarRuletree.setFloatable(false);
        toolBarRuletree.add(buttonExpandAll);
        toolBarRuletree.add(buttonExpandBelow);
        toolBarRuletree.add(buttonCollapseAll);
        toolBarRuletree.add(buttonCollapseBelow);
        toolBarRuletree.addSeparator();
        toolBarRuletree.add(new JLabel("Search:"));
        toolBarRuletree.add(boxSearchCondition);
        toolBarRuletree.addSeparator();
        toolBarRuletree.add(textSearchValue);
        toolBarRuletree.addSeparator();
        toolBarRuletree.add(buttonRuleTreeFind);
        toolBarRuletree.add(buttonRuleTreeFindNext);
        toolBarRuletree.add(buttonRuleTreeFindClear);
        
        JPanel panelRuleTree =  new JPanel();
        panelRuleTree.setLayout(new BorderLayout(Utilities.GAP_COMPONENT,Utilities.GAP_COMPONENT));
        panelRuleTree.add("Center",treeScroll);
        panelRuleTree.add("South",toolBarRuletree);
        
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1,1,Utilities.GAP_COMPONENT,Utilities.GAP_COMPONENT));
        panel.setBorder(new TitledBorder(new EtchedBorder(),"Access Manager Tree"));
        panel.add(Utilities.createPanelMargined(panelRuleTree));
        
        return panel;
    }
    
    private JPanel createPanelNamedACL() {
        /* Named ACL List Panel */
        JScrollPane namedAclComponentScroll = new JScrollPane();
        namedAclComponentScroll.setPreferredSize(new Dimension(400,250));
        namedAclComponentScroll.getViewport().add(tableNamedACL);
        namedAclComponentScroll.setBorder(new BevelBorder(BevelBorder.LOWERED));
        JPanel panelRuleList =  new JPanel();
        panelRuleList.setLayout(new GridLayout(1,1));
        panelRuleList.add(namedAclComponentScroll);
        
        
        JPanel panelNamedACLDetails = new JPanel();
        panelNamedACLDetails.setLayout(new BorderLayout(Utilities.GAP_COMPONENT,Utilities.GAP_COMPONENT));
        JPanel panelNamedACLDetailsLeft = new JPanel();
        panelNamedACLDetailsLeft.setLayout(new GridLayout(3,1,Utilities.GAP_COMPONENT,Utilities.GAP_COMPONENT));
        panelNamedACLDetailsLeft.add(new JLabel("RuleTree Named ACLs"));
        panelNamedACLDetailsLeft.add(new JLabel("Workflow Named ACLs"));
        panelNamedACLDetailsLeft.add(new JPanel());
        JPanel panelNamedACLDetailsRight = new JPanel();
        panelNamedACLDetailsRight.setLayout(new GridLayout(3,1,Utilities.GAP_COMPONENT,Utilities.GAP_COMPONENT));
        panelNamedACLDetailsRight.add(Utilities.createProgressBar(
                0,
                accessManager.getAccessRuleList().size(),
                accessManager.getAccessRuleList().getTreeTypeSize(),
                "Access Rules"));
        panelNamedACLDetailsRight.add(Utilities.createProgressBar(
                0,
                accessManager.getAccessRuleList().size(),
                accessManager.getAccessRuleList().getWorkFlowTypeSize(),
                "Access Rules"));
        panelNamedACLDetailsRight.add(new JPanel());
        panelNamedACLDetails.add("West",panelNamedACLDetailsLeft);
        panelNamedACLDetails.add("Center",panelNamedACLDetailsRight);
        
        
        JPanel panelNamedACLMissing = new JPanel();
        panelNamedACLMissing.setLayout(new BorderLayout(Utilities.GAP_COMPONENT,Utilities.GAP_COMPONENT));
        panelNamedACLMissing.add("West",new JLabel("Total Unused Named ACLs"));
        panelNamedACLMissing.add("Center",Utilities.createProgressBar(
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
                int sortedIndex = tableDataFilterSortNamedACL.getModelIndex(unsortedIndex);
                tableNamedACL.setRowSelectionInterval(sortedIndex,sortedIndex);
                tableNamedACL.getSelectionModel().setAnchorSelectionIndex(sortedIndex);
                tableNamedACL.scrollRectToVisible(
                        tableNamedACL.getCellRect(
                        tableNamedACL.getSelectionModel().getAnchorSelectionIndex(),
                        tableNamedACL.getColumnModel().getSelectionModel().getAnchorSelectionIndex(),
                        false));
            }
        });
        
        
        JPanel panelNamedACLMissingFull = new JPanel();
        panelNamedACLMissingFull.setLayout(new GridLayout(3,1,Utilities.GAP_COMPONENT,Utilities.GAP_COMPONENT));
        panelNamedACLMissingFull.add(panelNamedACLMissing);
        panelNamedACLMissingFull.add(listUnusedNamedACL);
        panelNamedACLMissingFull.add(new JPanel());
        
        
        boxFirstSort = new JComboBox(NamedRuleFilterSortTableModel.SORT_COLUMN_SELECTION);
        boxFirstSort.setSelectedIndex(tableDataFilterSortNamedACL.getSort(0));
        boxSecondSort = new JComboBox(NamedRuleFilterSortTableModel.SORT_COLUMN_SELECTION);
        boxSecondSort.setSelectedIndex(tableDataFilterSortNamedACL.getSort(1));
        boxThirdSort = new JComboBox(NamedRuleFilterSortTableModel.SORT_COLUMN_SELECTION);
        boxThirdSort.setSelectedIndex(tableDataFilterSortNamedACL.getSort(2));
        checkAscending = new JCheckBox("Ascending",tableDataFilterSortNamedACL.isAscending());
        JButton buttonSortNamedACL = new JButton("Sort");
        buttonSortNamedACL.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int[] sort = new int[3];
                sort[0] = boxFirstSort.getSelectedIndex();
                sort[1] = boxSecondSort.getSelectedIndex();
                sort[2] = boxThirdSort.getSelectedIndex();
                tableDataFilterSortNamedACL.setSort(sort, checkAscending.isSelected());
                tableDataFilterSortNamedACL.fireTableDataChanged();
                tableNamedACL.repaint();
            }
        });
        
        JPanel panelNamedACLSortTop = new JPanel();
        panelNamedACLSortTop.setLayout(new GridLayout(2,3,Utilities.GAP_COMPONENT,Utilities.GAP_COMPONENT));
        panelNamedACLSortTop.add(new JLabel("Sort By"));
        panelNamedACLSortTop.add(new JLabel("Then By"));
        panelNamedACLSortTop.add(new JLabel("Finally By"));
        panelNamedACLSortTop.add(boxFirstSort);
        panelNamedACLSortTop.add(boxSecondSort);
        panelNamedACLSortTop.add(boxThirdSort);
        JPanel panelNamedACLSortBottom = new JPanel();
        panelNamedACLSortBottom.setLayout(new FlowLayout(FlowLayout.RIGHT,Utilities.GAP_COMPONENT,Utilities.GAP_COMPONENT));
        panelNamedACLSortBottom.add(checkAscending);
        panelNamedACLSortBottom.add(buttonSortNamedACL);
        JPanel panelNamedACLSort = new JPanel();
        panelNamedACLSort.setLayout(new BorderLayout(Utilities.GAP_COMPONENT,Utilities.GAP_COMPONENT));
        panelNamedACLSort.add("North",panelNamedACLSortTop);
        panelNamedACLSort.add("Center",panelNamedACLSortBottom);
        
        
        boxfilterType = new JComboBox();
        boxfilterType.addItem(null);
        for(int i=0; i<accessManager.getAccessRuleList().getTypeSize(); i++)
            boxfilterType.addItem(accessManager.getAccessRuleList().getType(i));
        boxfilterType.setSelectedItem(tableDataFilterSortNamedACL.getFilterPattern(tableDataFilterSortNamedACL.TYPE_COLUMN));
        textFilterInstanceCount = new JTextField();
        textFilterInstanceCount.setToolTipText("Must be a number");
        textFilterInstanceCount.setText(tableDataFilterSortNamedACL.getFilterPattern(tableDataFilterSortNamedACL.INSTANCES_COLUMN));
        textFilterName = new JTextField();
        textFilterName.setToolTipText("* ? [ - ] accepted");
        textFilterName.setText(tableDataFilterSortNamedACL.getFilterPattern(tableDataFilterSortNamedACL.NAME_COLUMN));
        JButton buttonFilter = new JButton("Filter");
        buttonFilter.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                int[] filterColumns = new int[]{0,1,2};
                String[] filterPatterns = new String[]{
                    (String)boxfilterType.getSelectedItem(),
                    textFilterInstanceCount.getText(),
                    textFilterName.getText()
                };
                
                if( ((filterPatterns[0] == null) || (filterPatterns[0].equals("")))
                &&
                        ((filterPatterns[1] == null) || (filterPatterns[1].equals("")))
                        &&
                        ((filterPatterns[2] == null) || (filterPatterns[2].equals("")))
                        )
                    JOptionPane.showMessageDialog(parentFrame, "Filtering requires either a Type, Instance Count, Name or any combination.", "No Filter Criteria", JOptionPane.ERROR_MESSAGE);
                else {
                    
                    tableNamedACL.clearSelection();
                    updateTableAccessControl();
                    tableDataFilterSortNamedACL.setFilter(filterColumns,filterPatterns);
                    tableDataFilterSortNamedACL.fireTableDataChanged();
                    tableNamedACL.repaint();
                }
            }
        });
        JButton buttonReset = new JButton("Reset");
        buttonReset.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                boxfilterType.setSelectedItem(null);
                textFilterInstanceCount.setText(null);
                textFilterName.setText(null);
                tableDataFilterSortNamedACL.resetFilter();
                tableDataFilterSortNamedACL.fireTableDataChanged();
                tableNamedACL.repaint();
            }
        });
        
        JPanel panelNamedACLFilterTop = new JPanel();
        panelNamedACLFilterTop.setLayout(new GridLayout(2,3,Utilities.GAP_COMPONENT,Utilities.GAP_COMPONENT));
        panelNamedACLFilterTop.add(new JLabel("Match Type"));
        panelNamedACLFilterTop.add(new JLabel("Match Count"));
        panelNamedACLFilterTop.add(new JLabel("Match Name"));
        panelNamedACLFilterTop.add(boxfilterType);
        panelNamedACLFilterTop.add(textFilterInstanceCount);
        panelNamedACLFilterTop.add(textFilterName);
        JPanel panelNamedACLFilterBottom = new JPanel();
        panelNamedACLFilterBottom.setLayout(new FlowLayout(FlowLayout.RIGHT,Utilities.GAP_COMPONENT,Utilities.GAP_COMPONENT));
        panelNamedACLFilterBottom.add(buttonReset);
        panelNamedACLFilterBottom.add(buttonFilter);
        JPanel panelNamedACLFilter = new JPanel();
        panelNamedACLFilter.setLayout(new BorderLayout(Utilities.GAP_COMPONENT,Utilities.GAP_COMPONENT));
        panelNamedACLFilter.add("North",panelNamedACLFilterTop);
        panelNamedACLFilter.add("Center",panelNamedACLFilterBottom);
        
        
        treeReferences = new JTree(new String[]{"Ruletree Reference","Nader Eloshaiker"});
        treeReferences.setRootVisible(false);
        treeReferences.setCellRenderer(new RuleTreeReferencesNodeRenderer());
        JScrollPane scrollReferences = new JScrollPane();
        scrollReferences.setPreferredSize(new Dimension(100,100));
        scrollReferences.getViewport().add(treeReferences);
        scrollReferences.setBorder(new BevelBorder(BevelBorder.LOWERED));
        JPanel panelNamedACLReferences = new JPanel();
        panelNamedACLReferences.setLayout(new GridLayout(1,1,Utilities.GAP_COMPONENT,Utilities.GAP_COMPONENT));
        panelNamedACLReferences.add(scrollReferences);
        
        
        JTabbedPane tabNamedAcl = new JTabbedPane();
        tabNamedAcl.add("Details",Utilities.createPanelMargined(panelNamedACLDetails));
        tabNamedAcl.add("Unused ACLs",Utilities.createPanelMargined(panelNamedACLMissingFull));
        tabNamedAcl.add("Sort",Utilities.createPanelMargined(panelNamedACLSort));
        tabNamedAcl.add("Filter",Utilities.createPanelMargined(panelNamedACLFilter));
        tabNamedAcl.add("References",panelNamedACLReferences);
        
        
        JPanel panelACL = new JPanel();
        panelACL.setLayout(new BorderLayout(Utilities.GAP_COMPONENT,Utilities.GAP_COMPONENT));
        panelACL.add("Center",panelRuleList);
        panelACL.add("South",tabNamedAcl);
        
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1,1,Utilities.GAP_COMPONENT,Utilities.GAP_COMPONENT));
        panel.setBorder(new TitledBorder(new EtchedBorder(), "Named ACL"));
        panel.add(Utilities.createPanelMargined(panelACL));
        
        return panel;
        
    }
    
    
}
