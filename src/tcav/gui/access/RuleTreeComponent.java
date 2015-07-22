/*
 * RuleTreeComponent.java
 *
 * Created on 25 January 2008, 14:14
 *
 * To change tree template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tcav.gui.access;

import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;
import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.event.*;
import tcav.gui.*;
import tcav.manager.access.AccessManager;
import tcav.manager.access.RuleTreeNode;
import tcav.resources.*;

/**
 *
 * @author NZR4DL
 */
public class RuleTreeComponent extends JPanel {
    
    private JTreeAdvanced tree;
    private JFrame parentFrame;
    
    /** Creates a new instance of RuleTreeComponent */
    public RuleTreeComponent(JFrame parent, AccessManager am) {
        this(parent, am, null);
    }
    
    public RuleTreeComponent(JFrame parent, AccessManager am, String title) {
        super();
        parentFrame = parent;
        tree = new JTreeAdvanced(new RuleTreeModel(am.getAccessTree()));
        //tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.setCellRenderer(new RuleTreeNodeRenderer());
        if(tree.getRowHeight() < 18)
            tree.setRowHeight(18);
        
        ToolTipManager.sharedInstance().registerComponent(tree);
        
        JScrollPane treeScroll = new JScrollPane();
        treeScroll.setPreferredSize(new Dimension(550,340));
        treeScroll.setBorder(new BevelBorder(BevelBorder.LOWERED));
        treeScroll.getViewport().add(tree);
        
        JButton buttonExpandAll = createButton("Expand All", ImageEnum.utilExpandAll);
        buttonExpandAll.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                new Thread() {
                    public void run() {
                        GUIutilities.expandTree(tree, parentFrame);
                    }
                }.start();
            }
        });

        JButton buttonExpandBelow = createButton("Expand Below", ImageEnum.utilExpand);
        buttonExpandBelow.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                new Thread() {
                    public void run() {
                        GUIutilities.expandTreeBranch(tree, parentFrame);
                    }
                }.start();
            }
        });
        
        JButton buttonCollapseAll = createButton("Collapse All", ImageEnum.utilCollapseAll);
        buttonCollapseAll.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                new Thread() {
                    public void run() {
                        GUIutilities.collapseTree(tree, parentFrame);
                    }
                }.start();
            }
        });
        
        JButton buttonCollapseBelow = createButton("Collapse Below", ImageEnum.utilCollapse);
        buttonCollapseBelow.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                new Thread() {
                    public void run() {
                        GUIutilities.collapseTreeBranch(tree, parentFrame);
                    }
                }.start();
            }
        });
        
        searchRuleTree = new SearchTreeComponent() {
            public boolean compare(TreePath path, String type, String value) {
                RuleTreeNode amItem = (RuleTreeNode)path.getLastPathComponent();
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
        
        buttonRuleTreeFindNext = createButton("Show Next", "Show the next occurrence", false);
        buttonRuleTreeFindNext.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                searchRuleTree.searchNext(tree);
                int k = searchRuleTree.getResultIndex() + 1;
                buttonRuleTreeFind.setText(k+" / "+searchRuleTree.getResultSize());
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
        if (am.getConditions().size() == 0) {
            boxSearchCondition.setEnabled(false);
            boxSearchCondition.addItem("Condition");
        } else {
            boxSearchCondition.addItem("");
            for(int x=0; x<am.getConditions().size(); x++)
                boxSearchCondition.addItem(am.getConditions().get(x));
        }
        
        
        JToolBar toolBarRuletree = new JToolBar();
        toolBarRuletree.setMargin(new Insets(
                GUIutilities.GAP_INSET,
                GUIutilities.GAP_INSET,
                GUIutilities.GAP_INSET,
                GUIutilities.GAP_INSET));
        toolBarRuletree.setFloatable(false);
        toolBarRuletree.add(buttonExpandAll);
        toolBarRuletree.add(buttonCollapseAll);
        toolBarRuletree.add(buttonExpandBelow);
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
        panelRuleTree.setLayout(new BorderLayout(GUIutilities.GAP_COMPONENT,GUIutilities.GAP_COMPONENT));
        panelRuleTree.add("Center",treeScroll);
        panelRuleTree.add("South",toolBarRuletree);
        
        String borderName = "Access Tree";
        if(title != null)
            borderName = borderName.concat(": "+title);
        
        this.setLayout(new GridLayout(1,1,GUIutilities.GAP_COMPONENT,GUIutilities.GAP_COMPONENT));
        this.setBorder(new TitledBorder(new EtchedBorder(), borderName));
        this.add(GUIutilities.createPanelMargined(panelRuleTree));
        
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
    
    private JButton createButton(String toolTip, ImageEnum image) {
        JButton button = new JButton();
        button.setOpaque(false);
        button.setToolTipText(toolTip);
        ImageIcon icon = new ImageIcon();
        try {
            icon = ResourceLoader.getImage(image);
        } catch (Exception e) {
            System.out.println("Couldn't load images: " + e);
        }
        button.setIcon(icon);
                
        return button;
    }
    
    class FindActionListener implements ActionListener {
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
    }
    
}
