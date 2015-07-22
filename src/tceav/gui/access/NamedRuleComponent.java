/*
 * NamedRuleComponent.java
 *
 * Created on 25 January 2008, 14:24
 *
 * To change table template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package tceav.gui.access;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import javax.swing.border.*;
import javax.swing.tree.*;
import tceav.gui.tools.GUIutilities;
import tceav.gui.tools.search.SearchTableComponent;
import tceav.gui.tools.table.JTableAdvanced;
import tceav.gui.tools.tree.JTreeAdvanced;
import tceav.manager.access.AccessManager;
import tceav.manager.access.NamedAcl;
import tceav.Settings;
import tceav.gui.*;
import tceav.resources.*;
import tceav.manager.compare.CompareInterface;

/**
 *
 * @author NZR4DL
 */
public class NamedRuleComponent extends JPanel {

    private JTableAdvanced table;
    private NamedRuleDataModel data;
    private NamedRuleDataFilterSort dataSort;
    private NamedRuleDataFilterCompare dataCompareFilter;
    private NamedRuleDataFilterSearch dataFilter;
    private AccessManager am;
    private AdminViewFrame parentFrame;
    private boolean compareMode;
    private final int TAB_MAX_COUNT = 6;

    /**
     * Creates a new instance of NamedRuleComponent
     */
    public NamedRuleComponent(AdminViewFrame parent, AccessManager am) {
        this(parent, am, null);
    }

    public NamedRuleComponent(AdminViewFrame parent, AccessManager am, String title) {
        this(parent, am, null, false);
    }

    public NamedRuleComponent(AdminViewFrame parent, AccessManager am, String title, boolean mode) {
        super();
        this.am = am;
        this.parentFrame = parent;
        this.compareMode = mode;
        table = new JTableAdvanced();

        data = new NamedRuleDataModel(am.getAccessRuleList());
        dataSort = new NamedRuleDataFilterSort(data);
        if (compareMode) {

            dataCompareFilter = new NamedRuleDataFilterCompare(dataSort);
            dataCompareFilter.setFilterEqual(Settings.getAmCmpFilterEqual() == COMPARE_SHOW_INDEX);
            dataCompareFilter.setFilterNotEqual(Settings.getAmCmpFilterNotEqual() == COMPARE_SHOW_INDEX);
            dataCompareFilter.setFilterNotFound(Settings.getAmCmpFilterNotFound() == COMPARE_SHOW_INDEX);

            dataFilter = new NamedRuleDataFilterSearch(dataCompareFilter);
            dataFilter.setCompareMode(true);
            dataSort.setSort(Settings.getAmCmpRuleSort(), Settings.isAmCmpRuleSortAscending());
        } else {
            dataFilter = new NamedRuleDataFilterSearch(dataSort);
            dataSort.setSort(Settings.getAmRuleSort(), Settings.isAmRuleSortAscending());
        }

        applyFilter();

        table.setModel(dataFilter);
        table.setRowSelectionAllowed(true);
        table.getTableHeader().setReorderingAllowed(false);
        table.setSelectionMode(0);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setShowGrid(false);
        TableColumn column;
        for (int i = 0; i < dataFilter.getColumnCount(); i++) {
            column = table.getColumnModel().getColumn(i);
            column.setHeaderRenderer(new NamedRuleTableHearderRenderer());
            column.setCellRenderer(new NamedRuleTableCellRenderer(mode));
            if (i == NamedRuleDataFilterInterface.TYPE_COLUMN ||
                    i == NamedRuleDataFilterInterface.INSTANCES_COLUMN ||
                    i == NamedRuleDataFilterInterface.COMPARE_COLUMN) {
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

        JScrollPane scrollTable = new JScrollPane();
        scrollTable.setPreferredSize(new Dimension(400, 250));
        scrollTable.getViewport().add(table);
        scrollTable.setBorder(new BevelBorder(BevelBorder.LOWERED));

        tabNamed = new JTabbedPane();
        tabNamed.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        tabNamed.add("Details", GUIutilities.createPanelMargined(createTabDetails()));
        tabNamed.add("Unused", GUIutilities.createPanelMargined(createTabMissing()));
        tabNamed.add("Sort", GUIutilities.createPanelMargined(createTabSort()));
        tabNamed.add("Filter", GUIutilities.createPanelMargined(createTabFilter()));
        tabNamed.add("Search", GUIutilities.createPanelMargined(createTabSearch()));
        tabNamed.add("Tree", createTabReferences());
        if (mode) {
            tabNamed.add("Compare Filter", GUIutilities.createPanelMargined(createTabCompare()));
            tabNamed.setSelectedIndex(Settings.getAmCmpRuleTab());
        } else {
            tabNamed.setSelectedIndex(Settings.getAmRuleTab());
        }
        tabNamed.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                if (!compareMode) {
                    Settings.setAmRuleTab(tabNamed.getSelectedIndex());
                } else {
                    Settings.setAmCmpRuleTab(tabNamed.getSelectedIndex());
                }
            }
        });


        String borderName = "Named ACL";
        if (title != null) {
            borderName = borderName.concat(": " + title);
        }

        setLayout(new BorderLayout(GUIutilities.GAP_COMPONENT, GUIutilities.GAP_COMPONENT));
        setBorder(new CompoundBorder(
                new TitledBorder(new EtchedBorder(), "Workflows"),
                new EmptyBorder(GUIutilities.GAP_MARGIN, GUIutilities.GAP_MARGIN, GUIutilities.GAP_MARGIN, GUIutilities.GAP_MARGIN)));
        add(tabNamed, BorderLayout.SOUTH);
        add(scrollTable, BorderLayout.CENTER);
    }

    private void applyFilter() {
        getModel().applyFilter();
        getModel().fireTableDataChanged();
        table.repaint();
    }

    public NamedRuleDataFilterAdapter getModel() {
        return dataFilter;
    }

    public JTableAdvanced getTable() {
        return table;
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
                am.getAccessRuleList().getACLTypes().size() + 1,
                1,
                GUIutilities.GAP_COMPONENT,
                GUIutilities.GAP_COMPONENT));
        for (int i = 0; i < am.getAccessRuleList().getACLTypes().size(); i++) {
            panelNamedDetailsLeft.add(new JLabel(am.getAccessRuleList().getACLTypes().get(i) + " ACLs"));
        }
        panelNamedDetailsLeft.add(new JPanel());
        JPanel panelNamedDetailsRight = new JPanel();
        panelNamedDetailsRight.setLayout(new GridLayout(
                am.getAccessRuleList().getACLTypes().size() + 1,
                1,
                GUIutilities.GAP_COMPONENT,
                GUIutilities.GAP_COMPONENT));
        for (int i = 0; i < am.getAccessRuleList().getACLTypes().size(); i++) {
            panelNamedDetailsRight.add(GUIutilities.createProgressBar(
                    0,
                    am.getAccessRuleList().size(),
                    am.getAccessRuleList().getACLTypeSize(am.getAccessRuleList().getACLTypes().get(i)),
                    "Access Rules"));
        }
        panelNamedDetailsRight.add(new JPanel());
        JPanel panelNamedDetails = new JPanel();
        panelNamedDetails.setLayout(new BorderLayout(GUIutilities.GAP_COMPONENT, GUIutilities.GAP_COMPONENT));
        panelNamedDetails.add("West", panelNamedDetailsLeft);
        panelNamedDetails.add("Center", panelNamedDetailsRight);

        return panelNamedDetails;
    }

    private JPanel createTabMissing() {
        JPanel panelNamedMissing = new JPanel();
        panelNamedMissing.setLayout(new BorderLayout(GUIutilities.GAP_COMPONENT, GUIutilities.GAP_COMPONENT));
        panelNamedMissing.add("West", new JLabel("Total Unused Named ACLs"));
        panelNamedMissing.add("Center", GUIutilities.createProgressBar(
                0,
                am.getAccessRuleList().size(),
                am.getUnusedRules().size(),
                "Access Rules"));
        listUnusedNamed = new JComboBox();
        if (am.getUnusedRules().size() == 0) {
            listUnusedNamed.setEnabled(false);
        } else {
            for (int z = 0; z < am.getUnusedRules().size(); z++) {
                listUnusedNamed.addItem(am.getUnusedRule(z));
            }
        }
        listUnusedNamed.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                NamedAcl ar = (NamedAcl) listUnusedNamed.getSelectedItem();
                int tableIndex = dataFilter.indexOfRuleName(ar.getRuleName());
                table.setRowSelectionInterval(tableIndex, tableIndex);
                table.getSelectionModel().setAnchorSelectionIndex(tableIndex);
                table.scrollRectToVisible(
                        table.getCellRect(
                        table.getSelectionModel().getAnchorSelectionIndex(),
                        table.getColumnModel().getSelectionModel().getAnchorSelectionIndex(),
                        false));
            }
        });


        JPanel panelNamedMissingFull = new JPanel();
        panelNamedMissingFull.setLayout(new GridLayout(3, 1, GUIutilities.GAP_COMPONENT, GUIutilities.GAP_COMPONENT));
        panelNamedMissingFull.add(panelNamedMissing);
        panelNamedMissingFull.add(listUnusedNamed);
        panelNamedMissingFull.add(new JPanel());

        return panelNamedMissingFull;
    }

    private JPanel createTabSort() {
        boxFirstSort = new JComboBox(dataSort.getSortColumns());
        boxFirstSort.setSelectedIndex(dataSort.getSort(0));
        boxSecondSort = new JComboBox(dataSort.getSortColumns());
        boxSecondSort.setSelectedIndex(dataSort.getSort(1));
        boxThirdSort = new JComboBox(dataSort.getSortColumns());
        boxThirdSort.setSelectedIndex(dataSort.getSort(2));
        checkAscending = new JCheckBox("Ascending", dataSort.isAscending());
        JButton buttonSortNamed = new JButton("Sort");
        buttonSortNamed.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                int[] sort = new int[3];
                sort[0] = boxFirstSort.getSelectedIndex();
                sort[1] = boxSecondSort.getSelectedIndex();
                sort[2] = boxThirdSort.getSelectedIndex();
                if (dataSort.isCompare()) {
                    Settings.setAmCmpRuleSort(sort);
                    Settings.setAmCmpRuleSortAscending(checkAscending.isSelected());

                } else {
                    Settings.setAmRuleSort(sort);
                    Settings.setAmRuleSortAscending(checkAscending.isSelected());
                }
                dataSort.setSort(sort, checkAscending.isSelected());
                applyFilter();
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
        panelNamedSortTop.setLayout(new GridLayout(2, 3, GUIutilities.GAP_COMPONENT, GUIutilities.GAP_COMPONENT));
        panelNamedSortTop.add(new JLabel("Sort By"));
        panelNamedSortTop.add(new JLabel("Then By"));
        panelNamedSortTop.add(new JLabel("Finally By"));
        panelNamedSortTop.add(boxFirstSort);
        panelNamedSortTop.add(boxSecondSort);
        panelNamedSortTop.add(boxThirdSort);
        JPanel panelNamedSortBottom = new JPanel();
        panelNamedSortBottom.setLayout(new FlowLayout(FlowLayout.RIGHT, GUIutilities.GAP_COMPONENT, GUIutilities.GAP_COMPONENT));
        panelNamedSortBottom.add(checkAscending);
        panelNamedSortBottom.add(buttonSortNamed);
        JPanel panelNamedSort = new JPanel();
        panelNamedSort.setLayout(new BorderLayout());
        panelNamedSort.add("North", panelNamedSortTop);
        panelNamedSort.add("Center", panelNamedSortBottom);

        return panelNamedSort;
    }

    private JPanel createTabSearch() {
        search = new SearchTableComponent() {

            public boolean compare(int row, String type, String value) {
                boolean matched = false;
                NamedAcl ar = getModel().getAccessRule(row);
                for (int j = 0; j < ar.size(); j++) {
                    if ((!type.equals("")) && (!value.equals(""))) {
                        matched = isMatched(ar.get(j).getTypeOfAccessor(), type) & isMatched(ar.get(j).getIdOfAccessor(), value);
                    } else if (!type.equals("")) {
                        matched = isMatched(ar.get(j).getTypeOfAccessor(), type);
                    } else if (!value.equals("")) {
                        matched = isMatched(ar.get(j).getIdOfAccessor(), value);
                    }

                    if (matched) {
                        break;
                    }
                }

                return matched;
            }
        };

        boxTypeAccessor = new JComboBox();
        boxTypeAccessor.addItem(null);
        for (int i = 0; i < am.getAccessRuleList().getAccessorTypes().size(); i++) {
            boxTypeAccessor.addItem(am.getAccessRuleList().getAccessorTypes().get(i));
        }

        SearchActionListener searchActionListener = new SearchActionListener();
        textAccessorID = new JTextField();
        textAccessorID.setToolTipText("* ? [ - ] accepted");
        textAccessorID.addActionListener(searchActionListener);
        labelSearchResult = new JLabel("Result: -- / --");
        buttonNamedSearch = new JButton("Find");
        buttonNamedSearch.addActionListener(searchActionListener);
        buttonNamedSearchNext = new JButton("Find Next");
        buttonNamedSearchNext.setEnabled(false);
        buttonNamedSearchNext.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                search.searchNext(table);
                int k = search.getResultIndex() + 1;
                labelSearchResult.setText("Result: " + k + " / " + search.getResultSize());
            }
        });
        buttonNamedSearchReset = new JButton("Clear");
        buttonNamedSearchReset.setEnabled(false);
        buttonNamedSearchReset.addActionListener(new ActionListener() {

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
        panelSearchTop.setLayout(new GridLayout(2, 2, GUIutilities.GAP_COMPONENT, GUIutilities.GAP_COMPONENT));
        panelSearchTop.add(new JLabel("Type of Accessor:", iconAccessorType, JLabel.LEFT));
        panelSearchTop.add(new JLabel("ID of Accessor:", iconAccessorId, JLabel.LEFT));
        panelSearchTop.add(boxTypeAccessor);
        panelSearchTop.add(textAccessorID);
        JPanel panelSearchBottom = new JPanel();
        panelSearchBottom.setLayout(new FlowLayout(FlowLayout.RIGHT, GUIutilities.GAP_COMPONENT, GUIutilities.GAP_COMPONENT));
        panelSearchBottom.add(labelSearchResult);
        panelSearchBottom.add(buttonNamedSearchReset);
        panelSearchBottom.add(buttonNamedSearchNext);
        panelSearchBottom.add(buttonNamedSearch);
        JPanel panelSearch = new JPanel();
        panelSearch.setLayout(new BorderLayout());
        panelSearch.add("North", panelSearchTop);
        panelSearch.add("Center", panelSearchBottom);

        return panelSearch;
    }

    class SearchActionListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            new Thread() {

                @Override
                public void run() {
                    String acessorType = "";
                    String accessorId = "";

                    if (boxTypeAccessor.getSelectedIndex() > 0) {
                        acessorType = (String) boxTypeAccessor.getSelectedItem();
                    }
                    accessorId = textAccessorID.getText();

                    if ((acessorType.equals("")) &&
                            ((accessorId == null) || (accessorId.equals("")))) {
                        JOptionPane.showMessageDialog(parentFrame, "Search requires either a Accessor Type, Accessor ID or any combination.", "No Search Criteria", JOptionPane.ERROR_MESSAGE);
                    } else {
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
                            labelSearchResult.setText("Result: " + k + " / " + search.getResultSize());
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
        treeReferences = new JTreeAdvanced(new RuleTreeReferencesModel(new NamedAcl()));
        treeReferences.setRootVisible(false);
        treeReferences.setShowsRootHandles(true);
        treeReferences.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        JScrollPane scrollReferences = new JScrollPane();
        scrollReferences.setPreferredSize(new Dimension(20, 20));
        scrollReferences.getViewport().add(treeReferences);
        scrollReferences.setBorder(new BevelBorder(BevelBorder.LOWERED));
        JPanel panelNamedReferences = new JPanel();
        panelNamedReferences.setLayout(new GridLayout(1, 1, GUIutilities.GAP_COMPONENT, GUIutilities.GAP_COMPONENT));
        panelNamedReferences.add(scrollReferences);

        ToolTipManager.sharedInstance().registerComponent(treeReferences);

        return panelNamedReferences;
    }
    JButton buttonFilterCompare;
    JComboBox boxCompareEqual;
    JComboBox boxCompareNotEqual;
    JComboBox boxCompareNotFound;
    private static final String[] COMPARE_SELECTION = {"Show", "Hide"};
    public static final int COMPARE_SHOW_INDEX = 0;
    public static final int COMPARE_HIDE_INDEX = 1;

    private JPanel createTabCompare() {

        ImageIcon iconFind = new ImageIcon();
        try {
            iconFind = ResourceLoader.getImage(ImageEnum.utilFind);
        } catch (Exception e) {
            System.out.println("Couldn't load images: " + e);
        }
        buttonFilterCompare = new JButton("Filter");
        buttonFilterCompare.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                dataCompareFilter.setFilterEqual(boxCompareEqual.getSelectedIndex() == COMPARE_SHOW_INDEX);
                dataCompareFilter.setFilterNotEqual(boxCompareNotEqual.getSelectedIndex() == COMPARE_SHOW_INDEX);
                dataCompareFilter.setFilterNotFound(boxCompareNotFound.getSelectedIndex() == COMPARE_SHOW_INDEX);
                Settings.setAmCmpFilterEqual(boxCompareEqual.getSelectedIndex());
                Settings.setAmCmpFilterNotEqual(boxCompareNotEqual.getSelectedIndex());
                Settings.setAmCmpFilterNotFound(boxCompareNotFound.getSelectedIndex());
                applyFilter();
            }
        });
        buttonFilterCompare.setIcon(iconFind);

        boxCompareEqual = new JComboBox(COMPARE_SELECTION);
        boxCompareEqual.setSelectedIndex(Settings.getAmCmpFilterEqual());
        boxCompareNotEqual = new JComboBox(COMPARE_SELECTION);
        boxCompareNotEqual.setSelectedIndex(Settings.getAmCmpFilterNotEqual());
        boxCompareNotFound = new JComboBox(COMPARE_SELECTION);
        boxCompareNotFound.setSelectedIndex(Settings.getAmCmpFilterNotFound());

        JPanel panelTop = new JPanel();
        panelTop.setLayout(new GridLayout(2, 3, GUIutilities.GAP_COMPONENT, GUIutilities.GAP_COMPONENT));
        panelTop.add(new JLabel(CompareInterface.EQUAL_LABEL));
        panelTop.add(new JLabel(CompareInterface.NOT_EQUAL_LABEL));
        panelTop.add(new JLabel(CompareInterface.NOT_FOUND_LABEL));
        panelTop.add(boxCompareEqual);
        panelTop.add(boxCompareNotEqual);
        panelTop.add(boxCompareNotFound);

        JPanel panelBottom = new JPanel();
        panelBottom.setLayout(new FlowLayout(FlowLayout.RIGHT, GUIutilities.GAP_COMPONENT, GUIutilities.GAP_COMPONENT));
        panelBottom.add(buttonFilterCompare);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(panelTop, BorderLayout.NORTH);
        panel.add(panelBottom, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createTabFilter() {
        boxfilterType = new JComboBox();
        boxfilterType.addItem(null);

        for (int i = 0; i < am.getAccessRuleList().getACLTypes().size(); i++) {
            boxfilterType.addItem(am.getAccessRuleList().getACLTypes().get(i));
        }

        boxfilterType.setSelectedItem(dataFilter.getFilterPattern(NamedRuleDataFilterInterface.TYPE_COLUMN));
        FilterActionListener filterActionListener = new FilterActionListener();
        textFilterInstanceCount = new JTextField();
        textFilterInstanceCount.setToolTipText("Must be a number");
        textFilterInstanceCount.setText(dataFilter.getFilterPattern(NamedRuleDataFilterInterface.INSTANCES_COLUMN));
        textFilterInstanceCount.addActionListener(filterActionListener);
        textFilterName = new JTextField();
        textFilterName.setToolTipText("* ? [ - ] accepted");
        textFilterName.setText(dataFilter.getFilterPattern(NamedRuleDataFilterInterface.NAME_COLUMN));
        textFilterName.addActionListener(filterActionListener);
        JButton buttonFilter = new JButton("Filter");
        buttonFilter.addActionListener(filterActionListener);
        JButton buttonReset = new JButton("Clear");
        buttonReset.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                boxfilterType.setEnabled(true);
                textFilterInstanceCount.setEnabled(true);
                textFilterName.setEnabled(true);
                boxfilterType.setSelectedItem(null);
                textFilterInstanceCount.setText(null);
                textFilterName.setText(null);
                dataFilter.resetFilter();
                applyFilter();
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

        JPanel panelTop = new JPanel();
        panelTop.setLayout(new GridLayout(2, 3, GUIutilities.GAP_COMPONENT, GUIutilities.GAP_COMPONENT));
        panelTop.add(new JLabel("Match Type"));
        panelTop.add(new JLabel("Match Count"));
        panelTop.add(new JLabel("Match Name"));
        panelTop.add(boxfilterType);
        panelTop.add(textFilterInstanceCount);
        panelTop.add(textFilterName);

        JPanel panelBottom = new JPanel();
        panelBottom.setLayout(new FlowLayout(FlowLayout.RIGHT, GUIutilities.GAP_COMPONENT, GUIutilities.GAP_COMPONENT));
        panelBottom.add(buttonReset);
        panelBottom.add(buttonFilter);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add("North", panelTop);
        panel.add("Center", panelBottom);

        return panel;
    }

    class FilterActionListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            int[] filterColumns = new int[]{0, 1, 2};
            String[] filterPatterns = new String[]{
                (String) boxfilterType.getSelectedItem(),
                textFilterInstanceCount.getText(),
                textFilterName.getText()
            };

            if (((filterPatterns[0] == null) || (filterPatterns[0].equals(""))) &&
                    ((filterPatterns[1] == null) || (filterPatterns[1].equals(""))) &&
                    ((filterPatterns[2] == null) || (filterPatterns[2].equals("")))) {
                JOptionPane.showMessageDialog(parentFrame, "Filtering requires either a Type, Instance Count, Name or any combination.", "No Filter Criteria", JOptionPane.ERROR_MESSAGE);
            } else {

                table.clearSelection();
                boxfilterType.setEnabled(false);
                textFilterInstanceCount.setEnabled(false);
                textFilterName.setEnabled(false);
                dataFilter.setFilter(filterColumns, filterPatterns);
                applyFilter();
            }
        }
    }

    public void updateReferences(int index) {
        NamedAcl ar = getModel().getAccessRule(index);
        treeReferences.setModel(new RuleTreeReferencesModel(ar));
        treeReferences.repaint();
    }
}
