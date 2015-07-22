/*
 * NamedRuleComponent.java
 *
 * Created on 25 January 2008, 14:24
 *
 * To change table template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tcav.gui.ruletree;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import javax.swing.border.*;
import javax.swing.tree.*;
import tcav.gui.*;
import tcav.ruletree.*;
import tcav.Settings;
import tcav.resources.*;
/**
 *
 * @author NZR4DL
 */
public class NamedRuleComponent extends JPanel {
    
    private JTableAdvanced table;
    private NamedRuleFilterSortTableModel dataModel;
    private AccessManager am;
    private JFrame parentFrame;
    
    /**
     * Creates a new instance of NamedRuleComponent
     */
    public NamedRuleComponent(JFrame parent, AccessManager am) {
        super();
        this.am = am;
        this.parentFrame = parent;
        table = new JTableAdvanced();
        dataModel = new NamedRuleFilterSortTableModel(am.getAccessRuleList());
        dataModel.setSort(Settings.getAMACLSort(),Settings.getAMACLSortAscending());
        table.setModel(dataModel);
        table.setRowSelectionAllowed(true);
        table.getTableHeader().setReorderingAllowed(false);
        table.setSelectionMode(0);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        table.setIntercellSpacing(new Dimension(0,0));
        table.setShowGrid(false);
        TableColumn column;
        for (int i=0; i<dataModel.getColumnCount(); i++){
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
        
        JScrollPane namedAclComponentScroll = new JScrollPane();
        namedAclComponentScroll.setPreferredSize(new Dimension(400,250));
        namedAclComponentScroll.getViewport().add(table);
        namedAclComponentScroll.setBorder(new BevelBorder(BevelBorder.LOWERED));
        JPanel panelRuleList =  new JPanel();
        panelRuleList.setLayout(new GridLayout(1,1));
        panelRuleList.add(namedAclComponentScroll);
        
        tabNamedAcl = new JTabbedPane();
        tabNamedAcl.add("Details",GUIutilities.createPanelMargined(createACLTabDetails()));
        tabNamedAcl.add("Unused",GUIutilities.createPanelMargined(createACLTabMissing()));
        tabNamedAcl.add("Sort",GUIutilities.createPanelMargined(createACLTabSort()));
        tabNamedAcl.add("Filter",GUIutilities.createPanelMargined(createACLTabFilter()));
        tabNamedAcl.add("Search",GUIutilities.createPanelMargined(createACLTabSearch()));
        tabNamedAcl.add("Tree",createACLTabReferences());
        tabNamedAcl.setSelectedIndex(Settings.getAMACLTab());
        tabNamedAcl.addChangeListener(new ChangeListener(){
            public void stateChanged(ChangeEvent e) {
                Settings.setAMACLTab(tabNamedAcl.getSelectedIndex());
            }
        });
        
        
        JPanel panelACL = new JPanel();
        panelACL.setLayout(new BorderLayout(GUIutilities.GAP_COMPONENT,GUIutilities.GAP_COMPONENT));
        panelACL.add("Center",panelRuleList);
        panelACL.add("South",tabNamedAcl);
        
        this.setLayout(new GridLayout(1,1,GUIutilities.GAP_COMPONENT,GUIutilities.GAP_COMPONENT));
        this.setBorder(new TitledBorder(new EtchedBorder(), "Named ACL"));
        this.add(GUIutilities.createPanelMargined(panelACL));
    }
    
    public NamedRuleFilterSortTableModel getModel() {
        return dataModel;
    }
    
    public JTableAdvanced getTable() {
        return table;
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
    
    private JPanel createACLTabDetails() {
        JPanel panelNamedACLDetailsLeft = new JPanel();
        panelNamedACLDetailsLeft.setLayout(new GridLayout(
                am.getAccessRuleList().getACLTypes().size()+1,
                1,
                GUIutilities.GAP_COMPONENT,
                GUIutilities.GAP_COMPONENT));
        for(int i=0; i<am.getAccessRuleList().getACLTypes().size(); i++){
            panelNamedACLDetailsLeft.add(new JLabel(am.getAccessRuleList().getACLTypes().get(i)+" ACLs"));
        }
        panelNamedACLDetailsLeft.add(new JPanel());
        JPanel panelNamedACLDetailsRight = new JPanel();
        panelNamedACLDetailsRight.setLayout(new GridLayout(
                am.getAccessRuleList().getACLTypes().size()+1,
                1,
                GUIutilities.GAP_COMPONENT,
                GUIutilities.GAP_COMPONENT));
        for(int i=0; i<am.getAccessRuleList().getACLTypes().size(); i++){
            panelNamedACLDetailsRight.add(GUIutilities.createProgressBar(
                    0,
                    am.getAccessRuleList().size(),
                    am.getAccessRuleList().getACLTypeSize(am.getAccessRuleList().getACLTypes().get(i)),
                    "Access Rules"));
        }
        panelNamedACLDetailsRight.add(new JPanel());
        JPanel panelNamedACLDetails = new JPanel();
        panelNamedACLDetails.setLayout(new BorderLayout(GUIutilities.GAP_COMPONENT,GUIutilities.GAP_COMPONENT));
        panelNamedACLDetails.add("West",panelNamedACLDetailsLeft);
        panelNamedACLDetails.add("Center",panelNamedACLDetailsRight);
        
        return panelNamedACLDetails;
    }
    
    private JPanel createACLTabMissing() {
        JPanel panelNamedACLMissing = new JPanel();
        panelNamedACLMissing.setLayout(new BorderLayout(GUIutilities.GAP_COMPONENT,GUIutilities.GAP_COMPONENT));
        panelNamedACLMissing.add("West",new JLabel("Total Unused Named ACLs"));
        panelNamedACLMissing.add("Center",GUIutilities.createProgressBar(
                0,
                am.getAccessRuleList().size(),
                am.getUnusedRules().size(),
                "Access Rules"));
        listUnusedNamedACL = new JComboBox();
        if (am.getUnusedRules().size() == 0)
            listUnusedNamedACL.setEnabled(false);
        else {
            for(int z=0; z<am.getUnusedRules().size(); z++)
                listUnusedNamedACL.addItem(am.getUnusedRule(z));
        }
        listUnusedNamedACL.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AccessRule ar = (AccessRule)listUnusedNamedACL.getSelectedItem();
                int sortedIndex = getModel().indexOfRuleName(ar.getRuleName());
                table.setRowSelectionInterval(sortedIndex,sortedIndex);
                table.getSelectionModel().setAnchorSelectionIndex(sortedIndex);
                table.scrollRectToVisible(
                        table.getCellRect(
                        table.getSelectionModel().getAnchorSelectionIndex(),
                        table.getColumnModel().getSelectionModel().getAnchorSelectionIndex(),
                        false));
            }
        });
        
        
        JPanel panelNamedACLMissingFull = new JPanel();
        panelNamedACLMissingFull.setLayout(new GridLayout(3,1,GUIutilities.GAP_COMPONENT,GUIutilities.GAP_COMPONENT));
        panelNamedACLMissingFull.add(panelNamedACLMissing);
        panelNamedACLMissingFull.add(listUnusedNamedACL);
        panelNamedACLMissingFull.add(new JPanel());
        
        return panelNamedACLMissingFull;
    }
    
    private JPanel createACLTabSort() {
        boxFirstSort = new JComboBox(NamedRuleFilterSortTableModel.SORT_COLUMN_SELECTION);
        boxFirstSort.setSelectedIndex(getModel().getSort(0));
        boxSecondSort = new JComboBox(NamedRuleFilterSortTableModel.SORT_COLUMN_SELECTION);
        boxSecondSort.setSelectedIndex(getModel().getSort(1));
        boxThirdSort = new JComboBox(NamedRuleFilterSortTableModel.SORT_COLUMN_SELECTION);
        boxThirdSort.setSelectedIndex(getModel().getSort(2));
        checkAscending = new JCheckBox("Ascending",getModel().isAscending());
        JButton buttonSortNamedACL = new JButton("Sort");
        buttonSortNamedACL.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int[] sort = new int[3];
                sort[0] = boxFirstSort.getSelectedIndex();
                sort[1] = boxSecondSort.getSelectedIndex();
                sort[2] = boxThirdSort.getSelectedIndex();
                Settings.setAMACLSort(sort);
                Settings.setAMACLSortAscending(checkAscending.isSelected());
                getModel().setSort(sort, checkAscending.isSelected());
                getModel().fireTableDataChanged();
                table.repaint();
            }
        });
        
        ImageIcon iconFind = new ImageIcon();
        try {
            iconFind = ResourceLoader.getImage(ImageEnum.utilFind);
        } catch (Exception e) {
            System.out.println("Couldn't load images: " + e);
        }
        buttonSortNamedACL.setIcon(iconFind);
        
        JPanel panelNamedACLSortTop = new JPanel();
        panelNamedACLSortTop.setLayout(new GridLayout(2,3,GUIutilities.GAP_COMPONENT,GUIutilities.GAP_COMPONENT));
        panelNamedACLSortTop.add(new JLabel("Sort By"));
        panelNamedACLSortTop.add(new JLabel("Then By"));
        panelNamedACLSortTop.add(new JLabel("Finally By"));
        panelNamedACLSortTop.add(boxFirstSort);
        panelNamedACLSortTop.add(boxSecondSort);
        panelNamedACLSortTop.add(boxThirdSort);
        JPanel panelNamedACLSortBottom = new JPanel();
        panelNamedACLSortBottom.setLayout(new FlowLayout(FlowLayout.RIGHT,GUIutilities.GAP_COMPONENT,GUIutilities.GAP_COMPONENT));
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
        boxfilterType.setSelectedItem(getModel().getFilterPattern(getModel().TYPE_COLUMN));
        textFilterInstanceCount = new JTextField();
        textFilterInstanceCount.setToolTipText("Must be a number");
        textFilterInstanceCount.setText(getModel().getFilterPattern(getModel().INSTANCES_COLUMN));
        textFilterName = new JTextField();
        textFilterName.setToolTipText("* ? [ - ] accepted");
        textFilterName.setText(getModel().getFilterPattern(getModel().NAME_COLUMN));
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
                    
                    table.clearSelection();
                    //tableAccessRule.updateTable();
                    getModel().setFilter(filterColumns,filterPatterns);
                    getModel().fireTableDataChanged();
                    table.repaint();
                }
            }
        });
        JButton buttonReset = new JButton("Clear");
        buttonReset.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                boxfilterType.setSelectedItem(null);
                textFilterInstanceCount.setText(null);
                textFilterName.setText(null);
                getModel().resetFilter();
                getModel().fireTableDataChanged();
                table.repaint();
            }
        });
        
        ImageIcon iconFind = new ImageIcon();
        ImageIcon iconReset = new ImageIcon();
        try {
            iconFind = ResourceLoader.getImage(ImageEnum.utilFind);
            iconReset = ResourceLoader.getImage(ImageEnum.utilClear);
        } catch (Exception e) {
            System.out.println("Couldn't load images: " + e);
        }
        buttonFilter.setIcon(iconFind);
        buttonReset.setIcon(iconReset);
        
        JPanel panelNamedACLFilterTop = new JPanel();
        panelNamedACLFilterTop.setLayout(new GridLayout(2,3,GUIutilities.GAP_COMPONENT,GUIutilities.GAP_COMPONENT));
        panelNamedACLFilterTop.add(new JLabel("Match Type"));
        panelNamedACLFilterTop.add(new JLabel("Match Count"));
        panelNamedACLFilterTop.add(new JLabel("Match Name"));
        panelNamedACLFilterTop.add(boxfilterType);
        panelNamedACLFilterTop.add(textFilterInstanceCount);
        panelNamedACLFilterTop.add(textFilterName);
        JPanel panelNamedACLFilterBottom = new JPanel();
        panelNamedACLFilterBottom.setLayout(new FlowLayout(FlowLayout.RIGHT,GUIutilities.GAP_COMPONENT,GUIutilities.GAP_COMPONENT));
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
                AccessRule ar = getModel().getAccessRule(row);
                for(int j=0; j<ar.size(); j++) {
                    if((!type.equals("")) && (!value.equals("")) )
                        matched = isMatched(ar.get(j).getTypeOfAccessor(), type) & isMatched(ar.get(j).getIdOfAccessor(), value);
                    else if(!type.equals(""))
                        matched = isMatched(ar.get(j).getTypeOfAccessor(), type);
                    else if(!value.equals(""))
                        matched = isMatched(ar.get(j).getIdOfAccessor(), value);
                    
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
                            searchACL.search(table, acessorType, accessorId);
                            
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
                searchACL.searchNext(table);
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
            iconFind = ResourceLoader.getImage(ImageEnum.utilFind);
            iconReset = ResourceLoader.getImage(ImageEnum.utilClear);
            iconAccessorType = ResourceLoader.getImage(ImageEnum.aclAccessorType);
            iconAccessorId = ResourceLoader.getImage(ImageEnum.aclAccessorID);
        } catch (Exception e) {
            System.out.println("Couldn't load images: " + e);
        }
        buttonNamedACLSearch.setIcon(iconFind);
        buttonNamedACLSearchReset.setIcon(iconReset);
        
        
        JPanel panelSearchTop = new JPanel();
        panelSearchTop.setLayout(new GridLayout(2,2,GUIutilities.GAP_COMPONENT,GUIutilities.GAP_COMPONENT));
        panelSearchTop.add(new JLabel("Type of Accessor:", iconAccessorType, JLabel.LEFT));
        panelSearchTop.add(new JLabel("ID of Accessor:", iconAccessorId, JLabel.LEFT));
        panelSearchTop.add(boxTypeAccessor);
        panelSearchTop.add(textAccessorID);
        JPanel panelSearchBottom = new JPanel();
        panelSearchBottom.setLayout(new FlowLayout(FlowLayout.RIGHT,GUIutilities.GAP_COMPONENT,GUIutilities.GAP_COMPONENT));
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
        treeReferences = new JTreeAdvanced(new RuleTreeReferencesModel(new AccessRule()));
        treeReferences.setRootVisible(false);
        treeReferences.setShowsRootHandles(true);
        treeReferences.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        JScrollPane scrollReferences = new JScrollPane();
        scrollReferences.setPreferredSize(new Dimension(20,20));
        scrollReferences.getViewport().add(treeReferences);
        scrollReferences.setBorder(new BevelBorder(BevelBorder.LOWERED));
        JPanel panelNamedACLReferences = new JPanel();
        panelNamedACLReferences.setLayout(new GridLayout(1,1,GUIutilities.GAP_COMPONENT,GUIutilities.GAP_COMPONENT));
        panelNamedACLReferences.add(scrollReferences);
        
        ToolTipManager.sharedInstance().registerComponent(treeReferences);
        
        return panelNamedACLReferences;
    }
    
    public void updateReferences(int index) {
        AccessRule ar = getModel().getAccessRule(index);
        treeReferences.setModel(new RuleTreeReferencesModel(ar));
        treeReferences.repaint();
    }
}
