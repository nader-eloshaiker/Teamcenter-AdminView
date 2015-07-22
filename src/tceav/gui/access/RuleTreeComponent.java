/*
 * RuleTreeComponent.java
 *
 * Created on 25 January 2008, 14:14
 *
 * To change tree template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package tceav.gui.access;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ToolTipManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.tree.TreePath;
import tceav.gui.AdminViewFrame;
import tceav.gui.tools.GUIutilities;
import tceav.gui.tools.search.SearchTreeComponent;
import tceav.gui.tools.tree.JTreeAdvanced;
import tceav.gui.tools.tree.toolbar.TreeCopyRuleTreeAdaptor;
import tceav.gui.tools.tree.toolbar.TreeToolBar;
import tceav.manager.access.AccessManager;
import tceav.manager.access.RuleTreeNode;

/**
 *
 * @author NZR4DL
 */
public class RuleTreeComponent extends JPanel {

    private JTreeAdvanced tree;
    private AdminViewFrame parentFrame;

    /** Creates a new instance of RuleTreeComponent */
    public RuleTreeComponent(AdminViewFrame parent, AccessManager am) {
        this(parent, am, null);
    }

    public RuleTreeComponent(AdminViewFrame parent, AccessManager am, String title) {
        this(parent, am, title, false);
    }

    public RuleTreeComponent(AdminViewFrame parent, AccessManager am, String title, boolean compareMode) {
        super();
        parentFrame = parent;
        tree = new JTreeAdvanced(new RuleTreeModel(am.getAccessTree()));
        //tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.setCellRenderer(new RuleTreeNodeRenderer(compareMode));
        if (tree.getRowHeight() < 18) {
            tree.setRowHeight(18);
        }

        ToolTipManager.sharedInstance().registerComponent(tree);

        JScrollPane scrollTree = new JScrollPane();
        scrollTree.setPreferredSize(new Dimension(550, 340));
        scrollTree.setBorder(new BevelBorder(BevelBorder.LOWERED));
        scrollTree.getViewport().add(tree);

        searchRuleTree = new SearchTreeComponent() {

            public boolean compare(TreePath path, String type, String value) {
                RuleTreeNode amItem = (RuleTreeNode) path.getLastPathComponent();

                if ((!type.equals("")) && (!value.equals(""))) {
                    return isMatched(amItem.getCondition(), type) & ((isMatched(amItem.getValue(), value) | isMatched(amItem.getAccessRuleName(), value)));
                } else if (!type.equals("")) {
                    return isMatched(amItem.getCondition(), type);
                } else if (!value.equals("")) {
                    return isMatched(amItem.getValue(), value) | isMatched(amItem.getAccessRuleName(), value);
                } else {
                    return false;
                }
            }
        };

        buttonRuleTreeFindNext = createButton("Show Next", "Show the next occurrence", false);
        buttonRuleTreeFindNext.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                searchRuleTree.searchNext(tree);
                int k = searchRuleTree.getResultIndex() + 1;
                buttonRuleTreeFind.setText(k + " / " + searchRuleTree.getResultSize());
            }
        });

        FindActionListener findActionListener = new FindActionListener();
        buttonRuleTreeFind = createButton("Find", "Search for all occurrences", true);
        buttonRuleTreeFind.addActionListener(findActionListener);

        textSearchValue = new JTextField();
        //textSearchValue.setOpaque(false);
        textSearchValue.setToolTipText("Ruletree Value: * ? [ - ] accepted");
        textSearchValue.setColumns(6);
        textSearchValue.addActionListener(findActionListener);

        buttonRuleTreeFindClear = createButton("Clear", "Clear search results", false);
        buttonRuleTreeFindClear.addActionListener(new ActionListener() {

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
        if (am.getConditions().size() == 0) {
            boxSearchCondition.setEnabled(false);
            boxSearchCondition.addItem("Condition");
        } else {
            boxSearchCondition.addItem("");
            for (int x = 0; x < am.getConditions().size(); x++) {
                boxSearchCondition.addItem(am.getConditions().get(x));
            }
        }

        TreeToolBar toolbar = new TreeToolBar(tree, parentFrame, new TreeCopyRuleTreeAdaptor());
        toolbar.addSeparator();
        toolbar.add(new JLabel("Search:"));
        toolbar.add(boxSearchCondition);
        toolbar.addSeparator();
        toolbar.add(textSearchValue);
        toolbar.addSeparator();
        toolbar.add(buttonRuleTreeFindClear);
        toolbar.add(buttonRuleTreeFindNext);
        toolbar.add(buttonRuleTreeFind);

        String borderName = "Access Tree";
        if (title != null) {
            borderName = borderName.concat(": " + title);
        }

        setLayout(new BorderLayout(GUIutilities.GAP_COMPONENT, GUIutilities.GAP_COMPONENT));
        setBorder(new CompoundBorder(
                new TitledBorder(new EtchedBorder(), borderName),
                new EmptyBorder(GUIutilities.GAP_MARGIN, GUIutilities.GAP_MARGIN, GUIutilities.GAP_MARGIN, GUIutilities.GAP_MARGIN)));
        add(toolbar, BorderLayout.SOUTH);
        add(scrollTree, BorderLayout.CENTER);

    }

    public JTreeAdvanced getTree() {
        return tree;
    }
    private JTextField textSearchValue;
    private JComboBox boxSearchCondition;
    private JButton buttonRuleTreeFindNext;
    private JButton buttonRuleTreeFind;
    private JButton buttonRuleTreeFindClear;
    private SearchTreeComponent searchRuleTree;

    private JButton createButton(String name, String toolTip, boolean enabled) {
        JButton button = new JButton(name);
        button.setEnabled(enabled);
        button.setOpaque(false);
        button.setToolTipText(toolTip);

        return button;
    }

    class FindActionListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            new Thread() {

                @Override
                public void run() {
                    String conditionString = "";
                    String valueString = "";

                    if (boxSearchCondition.getSelectedIndex() > 0) {
                        conditionString = (String) boxSearchCondition.getSelectedItem();
                    }
                    valueString = textSearchValue.getText();

                    if ((conditionString.equals("")) &&
                            ((valueString == null) || (valueString.equals("")))) {
                        JOptionPane.showMessageDialog(parentFrame, "Search requires either a condition, value/ACL or any combination.", "No Search Criteria", JOptionPane.ERROR_MESSAGE);
                    } else {
                        searchRuleTree.search(tree, conditionString, valueString);

                        if (searchRuleTree.getResultSize() == 0) {
                            JOptionPane.showMessageDialog(parentFrame, "No matches found", "No Matches Found", JOptionPane.WARNING_MESSAGE);
                            buttonRuleTreeFindNext.setEnabled(false);
                            buttonRuleTreeFindClear.setEnabled(false);
                            buttonRuleTreeFind.setEnabled(true);
                            boxSearchCondition.setEnabled(true);
                            textSearchValue.setEnabled(true);
                        } else {
                            int k = searchRuleTree.getResultIndex() + 1;
                            buttonRuleTreeFind.setText(k + " / " + searchRuleTree.getResultSize());
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
    }
}
