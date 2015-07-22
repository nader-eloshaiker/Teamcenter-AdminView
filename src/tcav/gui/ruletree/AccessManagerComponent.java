package tcav.gui.ruletree;

import javax.naming.ldap.StartTlsRequest;
import tcav.gui.*;
import tcav.utils.PatternMatch;
import tcav.ruletree.AccessManager;
import tcav.ruletree.AccessManagerItem;
import tcav.ruletree.AccessRule;
import tcav.ResourceLocator;
import tcav.Settings;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import javax.swing.table.*;
import javax.swing.tree.*;
import javax.swing.plaf.basic.BasicSplitPaneUI;

/**
 *
 * @author NZR4DL
 */
public class AccessManagerComponent extends JPanel implements TabbedPanel {
    
    protected JFrame parentFrame;
    protected JTreeAdvanced treeRuleTree;
    protected JTableAdvanced tableNamedACL;
    protected JTableAdvanced tableAccessRule;
    protected AccessRuleTableModel tableDataAccessRule;
    protected DefaultTreeModel treeDataRuleTree;
    protected NamedRuleFilterSortTableModel tableDataFilterSortNamedACL;
    
    protected JSplitPane splitPane;
    
    private AccessManager am;
    
    /**
     * Creates a new instance of AccessManagerComponent
     */
    public AccessManagerComponent(JFrame parentFrame, AccessManager am) {
        super();
        this.parentFrame = parentFrame;
        this.am = am;
        
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
        
        splitPane = new JSplitPane(
                JSplitPane.VERTICAL_SPLIT,
                true,
                Utilities.createPanelMargined(panelRule),
                Utilities.createPanelMargined(panelRuleTable));
        splitPane.setDividerLocation(Settings.getAMSplitLocation());
        splitPane.setResizeWeight(1.0);
        splitPane.setOneTouchExpandable(true);
        splitPane.setBorder(null);
        ((BasicSplitPaneUI)splitPane.getUI()).getDivider().addComponentListener(new ComponentAdapter(){
            public void componentMoved(ComponentEvent e){
                Settings.setAMSplitLocation(splitPane.getDividerLocation());
            }
        });
        
        /* And show it. */
        this.setLayout(new BorderLayout());//Utilities.GAP_MARGIN,Utilities.GAP_MARGIN));
        this.add("Center",splitPane);
        
    }
    
    public AccessManagerComponent(JFrame parentFrame) {
        this(parentFrame, new AccessManager());
    }
    
    public boolean isEmptyPanel() {
        return (am.getAccessManagerTree().size() == 0);
    }
    
    public AccessManager getAccessManager() {
        return am;
    }
    
    private void updateTableAccessControl() {
        updateTableAccessControl(new AccessRule());
    }
    
    private void updateTableAccessControl(AccessRule ar) {
        tableDataAccessRule = new AccessRuleTableModel(am.getAccessControlColumns(),ar);
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
    
    private void createTableAccessControl() {
        tableAccessRule = new JTableAdvanced();
        updateTableAccessControl();
    }
    
    private JTreeAdvanced createTreeRuleTree() {
        JTreeAdvanced tree = new JTreeAdvanced(new RuleTreeModel(am.getAccessManagerTree()));
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.setCellRenderer(new RuleTreeNodeRenderer());
        //tree.setLargeModel(true);
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
                        updateTableAccessControl(am.getAccessRuleList().elementAt(amItem.getAccessRuleListIndex()));
                } else
                    updateTableAccessControl();
                
            }
        });
        
        return tree;
    }
    
    private JTableAdvanced createTableNamedACL() {
        JTableAdvanced table = new JTableAdvanced();
        tableDataFilterSortNamedACL = new NamedRuleFilterSortTableModel(am.getAccessRuleList());
        tableDataFilterSortNamedACL.setSort(Settings.getAMACLSort(),Settings.getAMACLSortAscending());
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
                    updateTableAccessControl(am.getAccessRuleList().elementAt(i));
                    updateReferences(am.getAccessRuleList().elementAt(i));
                } else
                    updateTableAccessControl();
            }
        });
        return table;
    }
    
    protected JTextField textSearchValue;
    protected JComboBox boxSearchCondition;
    protected JButton buttonRuleTreeFindNext;
    protected JButton buttonRuleTreeFind;
    protected JButton buttonRuleTreeFindClear;
    protected SearchTreeComponent searchRuleTree;
    
    private JPanel createPanelRuleTree() {
        JScrollPane treeScroll = new JScrollPane();
        treeScroll.setPreferredSize(new Dimension(550,340));
        treeScroll.setBorder(new BevelBorder(BevelBorder.LOWERED));
        treeScroll.getViewport().add(treeRuleTree);
        
        JButton buttonExpandAll = new JButton();
        buttonExpandAll.setOpaque(false);
        buttonExpandAll.setToolTipText("Expand All");
        buttonExpandAll.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                new Thread() {
                    public void run() {
                        Utilities.expandTree(treeRuleTree, parentFrame);
                    }
                }.start();
            }
        });
        JButton buttonExpandBelow = new JButton();
        buttonExpandBelow.setOpaque(false);
        buttonExpandBelow.setToolTipText("Expand Below");
        buttonExpandBelow.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                new Thread() {
                    public void run() {
                        Utilities.expandTreeBranch(treeRuleTree, parentFrame);
                    }
                }.start();
            }
        });
        JButton buttonCollapseAll = new JButton();
        buttonCollapseAll.setOpaque(false);
        buttonCollapseAll.setToolTipText("Collapse All");
        buttonCollapseAll.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                new Thread() {
                    public void run() {
                        Utilities.collapseTree(treeRuleTree, parentFrame);
                    }
                }.start();
            }
        });
        JButton buttonCollapseBelow = new JButton();
        buttonCollapseBelow.setOpaque(false);
        buttonCollapseBelow.setToolTipText("Collapse Below");
        buttonCollapseBelow.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                new Thread() {
                    public void run() {
                        Utilities.collapseTreeBranch(treeRuleTree, parentFrame);
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
        
        searchRuleTree = new SearchTreeComponent() {
            public boolean compare(TreePath path, String type, String value) {
                AccessManagerItem amItem = (AccessManagerItem)path.getLastPathComponent();
                Boolean matched = false;
                
                if((!type.equals("")) && (!value.equals("")) )
                    return isMatched(amItem.getCondition(), type) & ( (isMatched(amItem.getValue(), value) | isMatched(amItem.getAccessRuleName(), value)) );
                else if(!type.equals(""))
                    return isMatched(amItem.getCondition(), type);
                else if(!value.equals(""))
                    return isMatched(amItem.getValue(), value) | isMatched(amItem.getAccessRuleName(), value);
                else
                    return false;
            }
        };
        
        buttonRuleTreeFindNext = new JButton("Find Next");
        buttonRuleTreeFindNext.setOpaque(false);
        buttonRuleTreeFindNext.setEnabled(false);
        buttonRuleTreeFindNext.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                searchRuleTree.searchNext(treeRuleTree);
                int k = searchRuleTree.getResultIndex() + 1;
                buttonRuleTreeFind.setText(k+" / "+searchRuleTree.getResultSize());
            }
        });
        
        buttonRuleTreeFind = new JButton("Find");
        buttonRuleTreeFind.setOpaque(false);
        buttonRuleTreeFind.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                new Thread() {
                    public void run() {
                        String conditionString = "";
                        String valueString = "";
                        
                        if(boxSearchCondition.getSelectedIndex() > 0)
                            conditionString = (String)boxSearchCondition.getSelectedItem();
                        valueString = textSearchValue.getText();
                        
                        if( (conditionString.equals("")) &&
                                ((valueString == null) || (valueString.equals(""))) )
                            JOptionPane.showMessageDialog(parentFrame, "Search requires either a condition, value/ACL or any combination.", "No Search Criteria", JOptionPane.ERROR_MESSAGE);
                        else {
                            searchRuleTree.search(treeRuleTree, conditionString, valueString);
                            
                            if (searchRuleTree.getResultSize() == 0) {
                                JOptionPane.showMessageDialog(parentFrame, "No matches found", "No Matches Found", JOptionPane.WARNING_MESSAGE);
                                buttonRuleTreeFindNext.setEnabled(false);
                                buttonRuleTreeFindClear.setEnabled(false);
                                buttonRuleTreeFind.setEnabled(true);
                                boxSearchCondition.setEnabled(true);
                                textSearchValue.setEnabled(true);
                            } else {
                                int k = searchRuleTree.getResultIndex() + 1;
                                buttonRuleTreeFind.setText(k+" / "+searchRuleTree.getResultSize());
                                buttonRuleTreeFindNext.setEnabled(true);
                                buttonRuleTreeFindClear.setEnabled(true);
                                buttonRuleTreeFind.setEnabled(false);
                                boxSearchCondition.setEnabled(false);
                                textSearchValue.setEnabled(false);
                            }
                        }
                    }
                }.start();
            }
        });
        
        buttonRuleTreeFindClear = new JButton("Clear");
        buttonRuleTreeFindClear.setOpaque(false);
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
                searchRuleTree.resetResults();
            }
        });
        
        boxSearchCondition = new JComboBox();
        boxSearchCondition.setOpaque(false);
        boxSearchCondition.setToolTipText("Ruletree Condition");
        if (am.getAccessManagerTree().getConditions().size() == 0) {
            boxSearchCondition.setEnabled(false);
            boxSearchCondition.addItem("Condition");
        } else {
            boxSearchCondition.addItem("");
            for(int x=0; x<am.getAccessManagerTree().getConditions().size(); x++)
                boxSearchCondition.addItem(am.getAccessManagerTree().getConditions().get(x));
        }
        
        textSearchValue = new JTextField();
        //textSearchValue.setOpaque(false);
        textSearchValue.setToolTipText("Ruletree Value: * ? [ - ] accepted");
        textSearchValue.setColumns(6);
        
        
        JToolBar toolBarRuletree = new JToolBar();
        toolBarRuletree.setMargin(new Insets(
                Utilities.GAP_INSET,
                Utilities.GAP_INSET,
                Utilities.GAP_INSET,
                Utilities.GAP_INSET));
        //toolBarRuletree.setFloatable(false);
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
        toolBarRuletree.add(buttonRuleTreeFindClear);
        toolBarRuletree.add(buttonRuleTreeFindNext);
        toolBarRuletree.add(buttonRuleTreeFind);
        
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
    
    protected JComboBox listUnusedNamedACL;
    protected JComboBox boxFirstSort;
    protected JComboBox boxSecondSort;
    protected JComboBox boxThirdSort;
    protected JCheckBox checkAscending;
    protected JTextField textFilterName;
    protected JTextField textFilterInstanceCount;
    protected JComboBox boxfilterType;
    protected JTreeAdvanced treeReferences;
    protected JTabbedPane tabNamedAcl;
    protected JComboBox boxTypeAccessor;
    protected JTextField textAccessorID;
    protected JLabel labelACLSearchResult;
    protected JButton buttonNamedACLSearchNext;
    protected JButton buttonNamedACLSearchReset;
    protected JButton buttonNamedACLSearch;
    protected SearchTableComponent searchACL;
    
    
    private JPanel createPanelNamedACL() {
        
        JScrollPane namedAclComponentScroll = new JScrollPane();
        namedAclComponentScroll.setPreferredSize(new Dimension(400,250));
        namedAclComponentScroll.getViewport().add(tableNamedACL);
        namedAclComponentScroll.setBorder(new BevelBorder(BevelBorder.LOWERED));
        JPanel panelRuleList =  new JPanel();
        panelRuleList.setLayout(new GridLayout(1,1));
        panelRuleList.add(namedAclComponentScroll);
        
        tabNamedAcl = new JTabbedPane();
        tabNamedAcl.add("Details",Utilities.createPanelMargined(createACLTabDetails()));
        tabNamedAcl.add("Unused",Utilities.createPanelMargined(createACLTabMissing()));
        tabNamedAcl.add("Sort",Utilities.createPanelMargined(createACLTabSort()));
        tabNamedAcl.add("Filter",Utilities.createPanelMargined(createACLTabFilter()));
        tabNamedAcl.add("Search",Utilities.createPanelMargined(createACLTabSearch()));
        tabNamedAcl.add("Tree",createACLTabReferences());
        tabNamedAcl.setSelectedIndex(Settings.getAMACLTab());
        tabNamedAcl.addChangeListener(new ChangeListener(){
            public void stateChanged(ChangeEvent e) {
                Settings.setAMACLTab(tabNamedAcl.getSelectedIndex());
            }
        });
        
        
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
    
    private JPanel createACLTabDetails() {
        JPanel panelNamedACLDetailsLeft = new JPanel();
        panelNamedACLDetailsLeft.setLayout(new GridLayout(
                am.getAccessRuleList().getACLTypes().size()+1,
                1,
                Utilities.GAP_COMPONENT,
                Utilities.GAP_COMPONENT));
        for(int i=0; i<am.getAccessRuleList().getACLTypes().size(); i++){
            panelNamedACLDetailsLeft.add(new JLabel(am.getAccessRuleList().getACLTypes().get(i)+" ACLs"));
        }
        panelNamedACLDetailsLeft.add(new JPanel());
        JPanel panelNamedACLDetailsRight = new JPanel();
        panelNamedACLDetailsRight.setLayout(new GridLayout(
                am.getAccessRuleList().getACLTypes().size()+1,
                1,
                Utilities.GAP_COMPONENT,
                Utilities.GAP_COMPONENT));
        for(int i=0; i<am.getAccessRuleList().getACLTypes().size(); i++){
            panelNamedACLDetailsRight.add(Utilities.createProgressBar(
                    0,
                    am.getAccessRuleList().size(),
                    am.getAccessRuleList().getACLTypeSize(am.getAccessRuleList().getACLTypes().get(i)),
                    "Access Rules"));
        }
        panelNamedACLDetailsRight.add(new JPanel());
        JPanel panelNamedACLDetails = new JPanel();
        panelNamedACLDetails.setLayout(new BorderLayout(Utilities.GAP_COMPONENT,Utilities.GAP_COMPONENT));
        panelNamedACLDetails.add("West",panelNamedACLDetailsLeft);
        panelNamedACLDetails.add("Center",panelNamedACLDetailsRight);
        
        return panelNamedACLDetails;
    }
    
    private JPanel createACLTabMissing() {
        JPanel panelNamedACLMissing = new JPanel();
        panelNamedACLMissing.setLayout(new BorderLayout(Utilities.GAP_COMPONENT,Utilities.GAP_COMPONENT));
        panelNamedACLMissing.add("West",new JLabel("Total Unused Named ACLs"));
        panelNamedACLMissing.add("Center",Utilities.createProgressBar(
                0,
                am.getAccessRuleList().size(),
                am.getUnusedRulesSize(),
                "Access Rules"));
        listUnusedNamedACL = new JComboBox();
        if (am.getUnusedRulesSize() == 0)
            listUnusedNamedACL.setEnabled(false);
        else
            for(int z=0; z<am.getUnusedRulesSize(); z++)
                listUnusedNamedACL.addItem(am.getAccessRuleList().elementAt(am.unusedElementAt(z)).getRuleName());
        listUnusedNamedACL.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int unusedIndex = listUnusedNamedACL.getSelectedIndex();
                int unsortedIndex = am.unusedElementAt(unusedIndex);
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
        
        return panelNamedACLMissingFull;
    }
    
    private JPanel createACLTabSort() {
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
                Settings.setAMACLSort(sort);
                Settings.setAMACLSortAscending(checkAscending.isSelected());
                tableDataFilterSortNamedACL.setSort(sort, checkAscending.isSelected());
                tableDataFilterSortNamedACL.fireTableDataChanged();
                tableNamedACL.repaint();
            }
        });
        
        ImageIcon iconFind = new ImageIcon();
        try {
            iconFind = new ImageIcon(ResourceLocator.getButtonImage("Find.gif"));
        } catch (Exception e) {
            System.out.println("Couldn't load images: " + e);
        }
        buttonSortNamedACL.setIcon(iconFind);
        
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
        panelNamedACLSort.setLayout(new BorderLayout());
        panelNamedACLSort.add("North",panelNamedACLSortTop);
        panelNamedACLSort.add("Center",panelNamedACLSortBottom);
        
        return panelNamedACLSort;
    }
    
    private JPanel createACLTabFilter() {
        boxfilterType = new JComboBox();
        boxfilterType.addItem(null);
        for(int i=0; i<am.getAccessRuleList().getACLTypes().size(); i++)
            boxfilterType.addItem(am.getAccessRuleList().getACLTypes().get(i));
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
                
                if( ((filterPatterns[0] == null) || (filterPatterns[0].equals(""))) &&
                        ((filterPatterns[1] == null) || (filterPatterns[1].equals(""))) &&
                        ((filterPatterns[2] == null) || (filterPatterns[2].equals(""))))
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
        JButton buttonReset = new JButton("Clear");
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
        
        ImageIcon iconFind = new ImageIcon();
        ImageIcon iconReset = new ImageIcon();
        try {
            iconFind = new ImageIcon(ResourceLocator.getButtonImage("Find.gif"));
            iconReset = new  ImageIcon(ResourceLocator.getButtonImage("Clear.gif"));
        } catch (Exception e) {
            System.out.println("Couldn't load images: " + e);
        }
        buttonFilter.setIcon(iconFind);
        buttonReset.setIcon(iconReset);
        
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
        panelNamedACLFilter.setLayout(new BorderLayout());
        panelNamedACLFilter.add("North",panelNamedACLFilterTop);
        panelNamedACLFilter.add("Center",panelNamedACLFilterBottom);
        
        return panelNamedACLFilter;
    }
    
    private JPanel createACLTabSearch() {
        searchACL = new SearchTableComponent(){
            public boolean compare(int row, String type, String value) {
                boolean matched = false;
                int index = tableDataFilterSortNamedACL.getDataIndex(row);
                AccessRule ar = am.getAccessRuleList().elementAt(index);
                for(int j=0; j<ar.size(); j++) {
                    if((!type.equals("")) && (!value.equals("")) )
                        matched = isMatched(ar.elementAt(j).getTypeOfAccessor(), type) & isMatched(ar.elementAt(j).getIdOfAccessor(), value);
                    else if(!type.equals(""))
                        matched = isMatched(ar.elementAt(j).getTypeOfAccessor(), type);
                    else if(!value.equals(""))
                        matched = isMatched(ar.elementAt(j).getIdOfAccessor(), value);
                    
                    if (matched)
                        break;
                }
                
                return matched;
            }
        };
        
        boxTypeAccessor = new JComboBox();
        boxTypeAccessor.addItem(null);
        for(int i=0; i<am.getAccessRuleList().getAccessorTypes().size(); i++)
            boxTypeAccessor.addItem(am.getAccessRuleList().getAccessorTypes().get(i));
        textAccessorID = new JTextField();
        textAccessorID.setToolTipText("* ? [ - ] accepted");
        labelACLSearchResult = new JLabel("Result: -- / --");
        buttonNamedACLSearch = new JButton("Find");
        buttonNamedACLSearch.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                new Thread() {
                    public void run() {
                        String acessorType = "";
                        String accessorId = "";
                        
                        if(boxTypeAccessor.getSelectedIndex() > 0)
                            acessorType = (String)boxTypeAccessor.getSelectedItem();
                        accessorId = textAccessorID.getText();
                        
                        if( (acessorType.equals("")) &&
                                ((accessorId == null) || (accessorId.equals(""))) )
                            JOptionPane.showMessageDialog(parentFrame, "Search requires either a Accessor Type, Accessor ID or any combination.", "No Search Criteria", JOptionPane.ERROR_MESSAGE);
                        else {
                            searchACL.search(tableNamedACL, acessorType, accessorId);
                            
                            if (searchACL.getResultSize() == 0) {
                                JOptionPane.showMessageDialog(parentFrame, "No matches found", "No Matches Found", JOptionPane.WARNING_MESSAGE);
                                buttonNamedACLSearchNext.setEnabled(false);
                                buttonNamedACLSearchReset.setEnabled(false);
                                buttonNamedACLSearch.setEnabled(true);
                                boxTypeAccessor.setEnabled(true);
                                textAccessorID.setEnabled(true);
                            } else {
                                int k = searchACL.getResultIndex() + 1;
                                labelACLSearchResult.setText("Result: "+k+" / "+searchACL.getResultSize());
                                buttonNamedACLSearchNext.setEnabled(true);
                                buttonNamedACLSearchReset.setEnabled(true);
                                buttonNamedACLSearch.setEnabled(false);
                                boxTypeAccessor.setEnabled(false);
                                textAccessorID.setEnabled(false);
                            }
                        }
                    }
                }.start();
            }
        });
        buttonNamedACLSearchNext = new JButton("Find Next");
        buttonNamedACLSearchNext.setEnabled(false);
        buttonNamedACLSearchNext.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                searchACL.searchNext(tableNamedACL);
                int k = searchACL.getResultIndex() + 1;
                labelACLSearchResult.setText("Result: "+k+" / "+searchACL.getResultSize());
            }
        });
        buttonNamedACLSearchReset = new JButton("Clear");
        buttonNamedACLSearchReset.setEnabled(false);
        buttonNamedACLSearchReset.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                labelACLSearchResult.setText("Result: -- / --");
                buttonNamedACLSearchNext.setEnabled(false);
                buttonNamedACLSearchReset.setEnabled(false);
                buttonNamedACLSearch.setEnabled(true);
                boxTypeAccessor.setEnabled(true);
                textAccessorID.setEnabled(true);
                boxTypeAccessor.setSelectedIndex(0);
                textAccessorID.setText("");
                searchACL.resetResults();
            }
        });
        
        ImageIcon iconAccessorType = new ImageIcon();
        ImageIcon iconAccessorId = new ImageIcon();
        ImageIcon iconFind = new ImageIcon();
        ImageIcon iconReset = new ImageIcon();
        try {
            iconFind = new ImageIcon(ResourceLocator.getButtonImage("Find.gif"));
            iconReset = new  ImageIcon(ResourceLocator.getButtonImage("Clear.gif"));
            iconAccessorType = new  ImageIcon(ResourceLocator.getRultreeColumnImage("AccessorType.gif"));;
            iconAccessorId = new  ImageIcon(ResourceLocator.getRultreeColumnImage("AccessorId.gif"));;
        } catch (Exception e) {
            System.out.println("Couldn't load images: " + e);
        }
        buttonNamedACLSearch.setIcon(iconFind);
        buttonNamedACLSearchReset.setIcon(iconReset);
        
        
        JPanel panelSearchTop = new JPanel();
        panelSearchTop.setLayout(new GridLayout(2,2,Utilities.GAP_COMPONENT,Utilities.GAP_COMPONENT));
        panelSearchTop.add(new JLabel("Type of Accessor:", iconAccessorType, JLabel.LEFT));
        panelSearchTop.add(new JLabel("ID of Accessor:", iconAccessorId, JLabel.LEFT));
        panelSearchTop.add(boxTypeAccessor);
        panelSearchTop.add(textAccessorID);
        JPanel panelSearchBottom = new JPanel();
        panelSearchBottom.setLayout(new FlowLayout(FlowLayout.RIGHT,Utilities.GAP_COMPONENT,Utilities.GAP_COMPONENT));
        panelSearchBottom.add(labelACLSearchResult);
        panelSearchBottom.add(buttonNamedACLSearchReset);
        panelSearchBottom.add(buttonNamedACLSearchNext);
        panelSearchBottom.add(buttonNamedACLSearch);
        JPanel panelSearch = new JPanel();
        panelSearch.setLayout(new BorderLayout());
        panelSearch.add("North",panelSearchTop);
        panelSearch.add("Center",panelSearchBottom);
        
        return panelSearch;
    }
    
    private JPanel createACLTabReferences() {
        treeReferences = new JTreeAdvanced(new String[]{"Ruletree Reference","Nader Eloshaiker"});
        treeReferences.setRootVisible(false);
        treeReferences.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        treeReferences.setCellRenderer(new RuleTreeReferencesNodeRenderer());
        JScrollPane scrollReferences = new JScrollPane();
        scrollReferences.setPreferredSize(new Dimension(20,20));
        scrollReferences.getViewport().add(treeReferences);
        scrollReferences.setBorder(new BevelBorder(BevelBorder.LOWERED));
        JPanel panelNamedACLReferences = new JPanel();
        panelNamedACLReferences.setLayout(new GridLayout(1,1,Utilities.GAP_COMPONENT,Utilities.GAP_COMPONENT));
        panelNamedACLReferences.add(scrollReferences);
        
        return panelNamedACLReferences;
    }
    
    private void updateReferences(AccessRule ar) {
        DefaultMutableTreeNode root;
        if(ar.getTreeIndexSize() > 0)
            root = RuleTreeNodeBuilder.getRuleTreePathsForRule(am.getAccessManagerTree(),ar);
        else
            root = null;
        
        ((DefaultTreeModel)treeReferences.getModel()).setRoot(root);
        treeReferences.repaint();
    }
}
