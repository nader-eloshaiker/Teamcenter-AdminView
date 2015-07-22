/*
 * NamedRuleComponent.java
 *
 * Created on 25 January 2008, 14:24
 *
 * To change table template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tcav.gui.access;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import javax.swing.border.*;
import javax.swing.tree.*;
import tcav.gui.*;
import tcav.manager.access.AccessManager;
import tcav.manager.access.AccessRule;
import tcav.Settings;
import tcav.resources.*;
import tcav.manager.compare.*;
/**
 *
 * @author NZR4DL
 */
public class NamedRuleComponent extends JPanel {
    
    private JTableAdvanced table;
    private NamedRuleDataModel data;
    private NamedRuleDataStreamSort dataSort;
    private NamedRuleDataStreamCompareFilter dataCompareFilter;
    private NamedRuleDataStreamFilter dataFilter;
    private AccessManager am;
    private JFrame parentFrame;
    
    private final int TAB_MAX_COUNT = 6;
    
    /**
     * Creates a new instance of NamedRuleComponent
     */
    public NamedRuleComponent(JFrame parent, AccessManager am) {
        this(parent, am, null);
    }
    
    public NamedRuleComponent(JFrame parent, AccessManager am, String title) {
        super();
        this.am = am;
        this.parentFrame = parent;
        table = new JTableAdvanced();
        
        data = new NamedRuleDataModel(am.getAccessRuleList());
        dataSort = new NamedRuleDataStreamSort(data);
        dataSort.setSort(Settings.getAMACLSort(),Settings.getAMACLSortAscending());
        dataCompareFilter = new NamedRuleDataStreamCompareFilter(dataSort);
        dataFilter = new NamedRuleDataStreamFilter(dataCompareFilter);
        applyModelChanges();
        
        table.setModel(dataFilter);
        table.setRowSelectionAllowed(true);
        table.getTableHeader().setReorderingAllowed(false);
        table.setSelectionMode(0);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        table.setIntercellSpacing(new Dimension(0,0));
        table.setShowGrid(false);
        TableColumn column;
        for (int i=0; i<dataFilter.getColumnCount(); i++){
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
        
        JScrollPane namedComponentScroll = new JScrollPane();
        namedComponentScroll.setPreferredSize(new Dimension(400,250));
        namedComponentScroll.getViewport().add(table);
        namedComponentScroll.setBorder(new BevelBorder(BevelBorder.LOWERED));
        JPanel panelRuleList =  new JPanel();
        panelRuleList.setLayout(new GridLayout(1,1));
        panelRuleList.add(namedComponentScroll);
        
        tabNamed = new JTabbedPane();
        tabNamed.add("Details",GUIutilities.createPanelMargined(createTabDetails()));
        tabNamed.add("Unused",GUIutilities.createPanelMargined(createTabMissing()));
        tabNamed.add("Sort",GUIutilities.createPanelMargined(createTabSort()));
        tabNamed.add("Filter",GUIutilities.createPanelMargined(createTabFilter()));
        tabNamed.add("Search",GUIutilities.createPanelMargined(createTabSearch()));
        tabNamed.add("Tree",createTabReferences());
        tabNamed.setSelectedIndex(Settings.getAMACLTab());
        tabNamed.addChangeListener(new ChangeListener(){
            public void stateChanged(ChangeEvent e) {
                if(tabNamed.getSelectedIndex() < TAB_MAX_COUNT)
                    Settings.setAMACLTab(tabNamed.getSelectedIndex());
            }
        });
        
        
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(GUIutilities.GAP_COMPONENT,GUIutilities.GAP_COMPONENT));
        panel.add("Center",panelRuleList);
        panel.add("South",tabNamed);
        
        String borderName = "Named ACL";
        if(title != null)
            borderName = borderName.concat(": "+title);
        
        this.setLayout(new GridLayout(1,1,GUIutilities.GAP_COMPONENT,GUIutilities.GAP_COMPONENT));
        this.setBorder(new TitledBorder(new EtchedBorder(), borderName));
        this.add(GUIutilities.createPanelMargined(panel));
    }
    
    private NamedRuleDataStreamAbstract getFinalModel() {
        return dataFilter;
    }
    
    private void applyModelChanges() {
        dataSort.apply();
        dataCompareFilter.apply();
        dataFilter.apply();
        
        dataSort.fireTableDataChanged();
        dataCompareFilter.fireTableDataChanged();
        dataFilter.fireTableDataChanged();
        
        table.repaint();
    }
    
    public NamedRuleDataStreamAbstract getModel() {
        return getFinalModel();
    }
    
    public JTableAdvanced getTable() {
        return table;
    }
    
    public void attachCompareTab(CompareResult result) {
        tabNamed.add("Compare",GUIutilities.createPanelMargined(createTabCompare(result)));
        tabNamed.setSelectedIndex(TAB_MAX_COUNT);
    }
    
    protected JComboBox listUnusedNamed;
    protected JComboBox boxFirstSort;
    protected JComboBox boxSecondSort;
    protected JComboBox boxThirdSort;
    protected JCheckBox checkAscending;
    protected JTextField textFilterName;
    protected JTextField textFilterInstanceCount;
    protected JComboBox boxfilterType;
    protected JTreeAdvanced treeReferences;
    protected JTabbedPane tabNamed;
    protected JComboBox boxTypeAccessor;
    protected JTextField textAccessorID;
    protected JLabel labelSearchResult;
    protected JButton buttonNamedSearchNext;
    protected JButton buttonNamedSearchReset;
    protected JButton buttonNamedSearch;
    protected SearchTableComponent search;
    
    private JPanel createTabDetails() {
        JPanel panelNamedDetailsLeft = new JPanel();
        panelNamedDetailsLeft.setLayout(new GridLayout(
                am.getAccessRuleList().getACLTypes().size()+1,
                1,
                GUIutilities.GAP_COMPONENT,
                GUIutilities.GAP_COMPONENT));
        for(int i=0; i<am.getAccessRuleList().getACLTypes().size(); i++){
            panelNamedDetailsLeft.add(new JLabel(am.getAccessRuleList().getACLTypes().get(i)+" ACLs"));
        }
        panelNamedDetailsLeft.add(new JPanel());
        JPanel panelNamedDetailsRight = new JPanel();
        panelNamedDetailsRight.setLayout(new GridLayout(
                am.getAccessRuleList().getACLTypes().size()+1,
                1,
                GUIutilities.GAP_COMPONENT,
                GUIutilities.GAP_COMPONENT));
        for(int i=0; i<am.getAccessRuleList().getACLTypes().size(); i++){
            panelNamedDetailsRight.add(GUIutilities.createProgressBar(
                    0,
                    am.getAccessRuleList().size(),
                    am.getAccessRuleList().getACLTypeSize(am.getAccessRuleList().getACLTypes().get(i)),
                    "Access Rules"));
        }
        panelNamedDetailsRight.add(new JPanel());
        JPanel panelNamedDetails = new JPanel();
        panelNamedDetails.setLayout(new BorderLayout(GUIutilities.GAP_COMPONENT,GUIutilities.GAP_COMPONENT));
        panelNamedDetails.add("West",panelNamedDetailsLeft);
        panelNamedDetails.add("Center",panelNamedDetailsRight);
        
        return panelNamedDetails;
    }
    
    private JPanel createTabMissing() {
        JPanel panelNamedMissing = new JPanel();
        panelNamedMissing.setLayout(new BorderLayout(GUIutilities.GAP_COMPONENT,GUIutilities.GAP_COMPONENT));
        panelNamedMissing.add("West",new JLabel("Total Unused Named ACLs"));
        panelNamedMissing.add("Center",GUIutilities.createProgressBar(
                0,
                am.getAccessRuleList().size(),
                am.getUnusedRules().size(),
                "Access Rules"));
        listUnusedNamed = new JComboBox();
        if (am.getUnusedRules().size() == 0)
            listUnusedNamed.setEnabled(false);
        else {
            for(int z=0; z<am.getUnusedRules().size(); z++)
                listUnusedNamed.addItem(am.getUnusedRule(z));
        }
        listUnusedNamed.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AccessRule ar = (AccessRule)listUnusedNamed.getSelectedItem();
                int tableIndex = dataFilter.indexOfRuleName(ar.getRuleName());
                table.setRowSelectionInterval(tableIndex,tableIndex);
                table.getSelectionModel().setAnchorSelectionIndex(tableIndex);
                table.scrollRectToVisible(
                        table.getCellRect(
                        table.getSelectionModel().getAnchorSelectionIndex(),
                        table.getColumnModel().getSelectionModel().getAnchorSelectionIndex(),
                        false));
            }
        });
        
        
        JPanel panelNamedMissingFull = new JPanel();
        panelNamedMissingFull.setLayout(new GridLayout(3,1,GUIutilities.GAP_COMPONENT,GUIutilities.GAP_COMPONENT));
        panelNamedMissingFull.add(panelNamedMissing);
        panelNamedMissingFull.add(listUnusedNamed);
        panelNamedMissingFull.add(new JPanel());
        
        return panelNamedMissingFull;
    }
    
    private JPanel createTabSort() {
        boxFirstSort = new JComboBox(NamedRuleDataStreamSort.SORT_COLUMN_SELECTION);
        boxFirstSort.setSelectedIndex(dataSort.getSort(0));
        boxSecondSort = new JComboBox(NamedRuleDataStreamSort.SORT_COLUMN_SELECTION);
        boxSecondSort.setSelectedIndex(dataSort.getSort(1));
        boxThirdSort = new JComboBox(NamedRuleDataStreamSort.SORT_COLUMN_SELECTION);
        boxThirdSort.setSelectedIndex(dataSort.getSort(2));
        checkAscending = new JCheckBox("Ascending",dataSort.isAscending());
        JButton buttonSortNamed = new JButton("Sort");
        buttonSortNamed.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int[] sort = new int[3];
                sort[0] = boxFirstSort.getSelectedIndex();
                sort[1] = boxSecondSort.getSelectedIndex();
                sort[2] = boxThirdSort.getSelectedIndex();
                Settings.setAMACLSort(sort);
                Settings.setAMACLSortAscending(checkAscending.isSelected());
                dataSort.setSort(sort, checkAscending.isSelected());
                applyModelChanges();
            }
        });
        
        ImageIcon iconFind = new ImageIcon();
        try {
            iconFind = ResourceLoader.getImage(ImageEnum.utilFind);
        } catch (Exception e) {
            System.out.println("Couldn't load images: " + e);
        }
        buttonSortNamed.setIcon(iconFind);
        
        JPanel panelNamedSortTop = new JPanel();
        panelNamedSortTop.setLayout(new GridLayout(2,3,GUIutilities.GAP_COMPONENT,GUIutilities.GAP_COMPONENT));
        panelNamedSortTop.add(new JLabel("Sort By"));
        panelNamedSortTop.add(new JLabel("Then By"));
        panelNamedSortTop.add(new JLabel("Finally By"));
        panelNamedSortTop.add(boxFirstSort);
        panelNamedSortTop.add(boxSecondSort);
        panelNamedSortTop.add(boxThirdSort);
        JPanel panelNamedSortBottom = new JPanel();
        panelNamedSortBottom.setLayout(new FlowLayout(FlowLayout.RIGHT,GUIutilities.GAP_COMPONENT,GUIutilities.GAP_COMPONENT));
        panelNamedSortBottom.add(checkAscending);
        panelNamedSortBottom.add(buttonSortNamed);
        JPanel panelNamedSort = new JPanel();
        panelNamedSort.setLayout(new BorderLayout());
        panelNamedSort.add("North",panelNamedSortTop);
        panelNamedSort.add("Center",panelNamedSortBottom);
        
        return panelNamedSort;
    }
    
    private JPanel createTabFilter() {
        boxfilterType = new JComboBox();
        boxfilterType.addItem(null);
        
        for(int i=0; i<am.getAccessRuleList().getACLTypes().size(); i++)
            boxfilterType.addItem(am.getAccessRuleList().getACLTypes().get(i));
        
        boxfilterType.setSelectedItem(dataFilter.getFilterPattern(NamedRuleDataStreamInterface.TYPE_COLUMN));
        FilterActionListener filterActionListener = new FilterActionListener();
        textFilterInstanceCount = new JTextField();
        textFilterInstanceCount.setToolTipText("Must be a number");
        textFilterInstanceCount.setText(dataFilter.getFilterPattern(NamedRuleDataStreamInterface.INSTANCES_COLUMN));
        textFilterInstanceCount.addActionListener(filterActionListener);
        textFilterName = new JTextField();
        textFilterName.setToolTipText("* ? [ - ] accepted");
        textFilterName.setText(dataFilter.getFilterPattern(NamedRuleDataStreamInterface.NAME_COLUMN));
        textFilterName.addActionListener(filterActionListener);
        JButton buttonFilter = new JButton("Filter");
        buttonFilter.addActionListener(filterActionListener);
        JButton buttonReset = new JButton("Clear");
        buttonReset.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                boxfilterType.setEnabled(true);
                textFilterInstanceCount.setEnabled(true);
                textFilterName.setEnabled(true);
                boxfilterType.setSelectedItem(null);
                textFilterInstanceCount.setText(null);
                textFilterName.setText(null);
                dataFilter.resetFilter();
                applyModelChanges();
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
        
        JPanel panelNamedFilterTop = new JPanel();
        panelNamedFilterTop.setLayout(new GridLayout(2,3,GUIutilities.GAP_COMPONENT,GUIutilities.GAP_COMPONENT));
        panelNamedFilterTop.add(new JLabel("Match Type"));
        panelNamedFilterTop.add(new JLabel("Match Count"));
        panelNamedFilterTop.add(new JLabel("Match Name"));
        panelNamedFilterTop.add(boxfilterType);
        panelNamedFilterTop.add(textFilterInstanceCount);
        panelNamedFilterTop.add(textFilterName);
        JPanel panelNamedFilterBottom = new JPanel();
        panelNamedFilterBottom.setLayout(new FlowLayout(FlowLayout.RIGHT,GUIutilities.GAP_COMPONENT,GUIutilities.GAP_COMPONENT));
        panelNamedFilterBottom.add(buttonReset);
        panelNamedFilterBottom.add(buttonFilter);
        JPanel panelNamedFilter = new JPanel();
        panelNamedFilter.setLayout(new BorderLayout());
        panelNamedFilter.add("North",panelNamedFilterTop);
        panelNamedFilter.add("Center",panelNamedFilterBottom);
        
        return panelNamedFilter;
    }
    
    class FilterActionListener implements ActionListener {
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
                boxfilterType.setEnabled(false);
                textFilterInstanceCount.setEnabled(false);
                textFilterName.setEnabled(false);
                dataFilter.setFilter(filterColumns,filterPatterns);
                applyModelChanges();
            }
        }
    }
    
    private JPanel createTabSearch() {
        search = new SearchTableComponent(){
            public boolean compare(int row, String type, String value) {
                boolean matched = false;
                AccessRule ar = getFinalModel().getAccessRule(row);
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
        
        SearchActionListener searchActionListener = new SearchActionListener();
        textAccessorID = new JTextField();
        textAccessorID.setToolTipText("* ? [ - ] accepted");
        textAccessorID.addActionListener(searchActionListener);
        labelSearchResult = new JLabel("Result: -- / --");
        buttonNamedSearch = new JButton("Find");
        buttonNamedSearch.addActionListener(searchActionListener);
        buttonNamedSearchNext = new JButton("Find Next");
        buttonNamedSearchNext.setEnabled(false);
        buttonNamedSearchNext.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                search.searchNext(table);
                int k = search.getResultIndex() + 1;
                labelSearchResult.setText("Result: "+k+" / "+search.getResultSize());
            }
        });
        buttonNamedSearchReset = new JButton("Clear");
        buttonNamedSearchReset.setEnabled(false);
        buttonNamedSearchReset.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                labelSearchResult.setText("Result: -- / --");
                buttonNamedSearchNext.setEnabled(false);
                buttonNamedSearchReset.setEnabled(false);
                buttonNamedSearch.setEnabled(true);
                boxTypeAccessor.setEnabled(true);
                textAccessorID.setEnabled(true);
                boxTypeAccessor.setSelectedIndex(0);
                textAccessorID.setText("");
                search.resetResults();
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
        buttonNamedSearch.setIcon(iconFind);
        buttonNamedSearchReset.setIcon(iconReset);
        
        
        JPanel panelSearchTop = new JPanel();
        panelSearchTop.setLayout(new GridLayout(2,2,GUIutilities.GAP_COMPONENT,GUIutilities.GAP_COMPONENT));
        panelSearchTop.add(new JLabel("Type of Accessor:", iconAccessorType, JLabel.LEFT));
        panelSearchTop.add(new JLabel("ID of Accessor:", iconAccessorId, JLabel.LEFT));
        panelSearchTop.add(boxTypeAccessor);
        panelSearchTop.add(textAccessorID);
        JPanel panelSearchBottom = new JPanel();
        panelSearchBottom.setLayout(new FlowLayout(FlowLayout.RIGHT,GUIutilities.GAP_COMPONENT,GUIutilities.GAP_COMPONENT));
        panelSearchBottom.add(labelSearchResult);
        panelSearchBottom.add(buttonNamedSearchReset);
        panelSearchBottom.add(buttonNamedSearchNext);
        panelSearchBottom.add(buttonNamedSearch);
        JPanel panelSearch = new JPanel();
        panelSearch.setLayout(new BorderLayout());
        panelSearch.add("North",panelSearchTop);
        panelSearch.add("Center",panelSearchBottom);
        
        return panelSearch;
    }
    
    class SearchActionListener implements ActionListener {
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
                        search.search(table, acessorType, accessorId);
                        
                        if (search.getResultSize() == 0) {
                            JOptionPane.showMessageDialog(parentFrame, "No matches found", "No Matches Found", JOptionPane.WARNING_MESSAGE);
                            buttonNamedSearchNext.setEnabled(false);
                            buttonNamedSearchReset.setEnabled(false);
                            buttonNamedSearch.setEnabled(true);
                            boxTypeAccessor.setEnabled(true);
                            textAccessorID.setEnabled(true);
                        } else {
                            int k = search.getResultIndex() + 1;
                            labelSearchResult.setText("Result: "+k+" / "+search.getResultSize());
                            buttonNamedSearchNext.setEnabled(true);
                            buttonNamedSearchReset.setEnabled(true);
                            buttonNamedSearch.setEnabled(false);
                            boxTypeAccessor.setEnabled(false);
                            textAccessorID.setEnabled(false);
                        }
                    }
                }
            }.start();
        }
    }
    
    private JPanel createTabReferences() {
        treeReferences = new JTreeAdvanced(new RuleTreeReferencesModel(new AccessRule()));
        treeReferences.setRootVisible(false);
        treeReferences.setShowsRootHandles(true);
        treeReferences.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        JScrollPane scrollReferences = new JScrollPane();
        scrollReferences.setPreferredSize(new Dimension(20,20));
        scrollReferences.getViewport().add(treeReferences);
        scrollReferences.setBorder(new BevelBorder(BevelBorder.LOWERED));
        JPanel panelNamedReferences = new JPanel();
        panelNamedReferences.setLayout(new GridLayout(1,1,GUIutilities.GAP_COMPONENT,GUIutilities.GAP_COMPONENT));
        panelNamedReferences.add(scrollReferences);
        
        ToolTipManager.sharedInstance().registerComponent(treeReferences);
        
        return panelNamedReferences;
    }
    
    JButton buttonFilterCompare;
    JCheckBox checkCompareEqual;
    JCheckBox checkCompareNotEqual;
    JCheckBox checkCompareNotFound;
    
    private JPanel createTabCompare(CompareResult result) {
        checkCompareEqual = new JCheckBox(CompareInterface.EQUAL_LABEL, dataCompareFilter.isFilterEqual());
        checkCompareNotEqual = new JCheckBox(CompareInterface.NOT_EQUAL_LABEL, dataCompareFilter.isFilterNotEqual());
        checkCompareNotFound = new JCheckBox(CompareInterface.NOT_FOUND_LABEL, dataCompareFilter.isFilterNotFound());
        buttonFilterCompare = new JButton("Filter");
        buttonFilterCompare.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dataCompareFilter.setFilterEqual(checkCompareEqual.isSelected());
                dataCompareFilter.setFilterNotEqual(checkCompareNotEqual.isSelected());
                dataCompareFilter.setFilterNotFound(checkCompareNotFound.isSelected());
                applyModelChanges();
            }
        });
        
        ImageIcon iconFind = new ImageIcon();
        try {
            iconFind = ResourceLoader.getImage(ImageEnum.utilFind);
        } catch (Exception e) {
            System.out.println("Couldn't load images: " + e);
        }
        buttonFilterCompare.setIcon(iconFind);
        
        JPanel panel1Top = new JPanel();
        panel1Top.setLayout(new GridLayout(1,3));//,GUIutilities.GAP_COMPONENT,GUIutilities.GAP_COMPONENT));
        panel1Top.add(checkCompareEqual);
        panel1Top.add(checkCompareNotEqual);
        panel1Top.add(checkCompareNotFound);
        JPanel panel1Bottom = new JPanel();
        panel1Bottom.setLayout(new FlowLayout(FlowLayout.RIGHT,GUIutilities.GAP_COMPONENT,0));
        panel1Bottom.add(buttonFilterCompare);
        JPanel panel1 = new JPanel();
        panel1.setBorder(new TitledBorder(new EtchedBorder(), "Compare Filter:"));
        panel1.setLayout(new BorderLayout());
        panel1.add(panel1Top, BorderLayout.NORTH);
        panel1.add(panel1Bottom, BorderLayout.CENTER);
        
        
        JLabel labelEqual = new JLabel(Integer.toString(result.getResult(CompareInterface.EQUAL)));
        JLabel labelNotEqual = new JLabel(Integer.toString(result.getResult(CompareInterface.NOT_EQUAL)));
        JLabel labelNotFound = new JLabel(Integer.toString(result.getResult(CompareInterface.NOT_FOUND)));
        labelEqual.setHorizontalAlignment(JLabel.CENTER);
        labelNotEqual.setHorizontalAlignment(JLabel.CENTER);
        labelNotFound.setHorizontalAlignment(JLabel.CENTER);
        JLabel labelTitleEqual = new JLabel(CompareInterface.EQUAL_LABEL+":");
        JLabel labelTitleNotEqual = new JLabel(CompareInterface.NOT_EQUAL_LABEL+":");
        JLabel labelTitleNotFound = new JLabel(CompareInterface.NOT_FOUND_LABEL+":");
        labelTitleEqual.setHorizontalAlignment(JLabel.CENTER);
        labelTitleNotEqual.setHorizontalAlignment(JLabel.CENTER);
        labelTitleNotFound.setHorizontalAlignment(JLabel.CENTER);
        JPanel panel2Bottom = new JPanel();
        panel2Bottom.setLayout(new GridLayout(2,3));//,GUIutilities.GAP_COMPONENT,GUIutilities.GAP_COMPONENT));
        panel2Bottom.add(labelTitleEqual);
        panel2Bottom.add(labelTitleNotEqual);
        panel2Bottom.add(labelTitleNotFound);
        panel2Bottom.add(labelEqual);
        panel2Bottom.add(labelNotEqual);
        panel2Bottom.add(labelNotFound);
        JPanel panel2 = new JPanel();
        panel2.setLayout(new BorderLayout());
        panel2.setBorder(new TitledBorder(new EtchedBorder(), "Compare Statistics:"));
        panel2.add(panel2Bottom, BorderLayout.CENTER);
        
        
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(panel1, BorderLayout.WEST);
        panel.add(panel2, BorderLayout.CENTER);
        
        return panel;
    }
    
    public void updateReferences(int index) {
        AccessRule ar = getFinalModel().getAccessRule(index);
        treeReferences.setModel(new RuleTreeReferencesModel(ar));
        treeReferences.repaint();
    }
}
