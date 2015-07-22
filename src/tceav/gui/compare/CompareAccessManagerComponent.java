/*
 * CompareAccessManagerComponent.java
 *
 * Created on 7 March 2008, 19:51
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package tceav.gui.compare;

import tceav.gui.tools.GUIutilities;
import tceav.Settings;
import tceav.gui.*;
import tceav.gui.access.*;
import tceav.manager.compare.*;
import tceav.resources.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import javax.swing.plaf.basic.BasicSplitPaneUI;

/**
 *
 * @author NZR4DL
 */
public class CompareAccessManagerComponent extends TabbedPanel {

    private AdminViewFrame parentFrame;
    private RuleTreeComponent[] ruleTree;
    private NamedRuleComponent[] namedRule;
    private AccessRuleComponent accessControl;
    private JSplitPane splitPane;
    private JPanel panelTab;
    private final int compareCount = 2;
    public static final String MODE_ACL = "Named ACL";
    public static final String MODE_TREE = "Rule Tree";
    private CompareAccessManager cam;

    /** Creates a new instance of CompareAccessManagerComponent */
    public CompareAccessManagerComponent(AdminViewFrame parent, CompareAccessManager cam) {
        super();
        this.parentFrame = parent;
        this.cam = cam;

        ruleTree = new RuleTreeComponent[compareCount];
        namedRule = new NamedRuleComponent[compareCount];

        accessControl = new AccessRuleComponent(cam.getAccessManagers()[0], true);

        panelTab = new JPanel();
        panelTab.setLayout(new CardLayout());
        panelTab.add(buildRuletreeComponent(), MODE_TREE);
        panelTab.add(buildNamedAclComponent(), MODE_ACL);
        ((CardLayout) panelTab.getLayout()).show(panelTab, Settings.getAmCmpDisplayMode());

        attachListeners();


        splitPane = new JSplitPane(
                JSplitPane.VERTICAL_SPLIT, true, panelTab,
                GUIutilities.createPanelMargined(accessControl));
        splitPane.setDividerLocation(Settings.getAmSplitLocation());
        splitPane.setResizeWeight(1.0);
        splitPane.setOneTouchExpandable(true);
        splitPane.setBorder(null);
        ((BasicSplitPaneUI) splitPane.getUI()).getDivider().addComponentListener(new ComponentAdapter() {

            @Override
            public void componentMoved(ComponentEvent e) {
                Settings.setAmSplitLocation(splitPane.getDividerLocation());
            }
        });

        /* And show it. */
        this.setLayout(new BorderLayout());
        this.add(splitPane, BorderLayout.CENTER);

    }
    private JToolBar toolBar;
    private JCheckBox checkSync;

    public JToolBar getToolBar() {

        if (toolBar != null) {
            return toolBar;
        }

        JRadioButton ruletreeButton = new JRadioButton("Rule Tree");
        ruletreeButton.setOpaque(false);
        ruletreeButton.setVerticalTextPosition(JRadioButton.CENTER);
        ruletreeButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                ((CardLayout) panelTab.getLayout()).show(panelTab, MODE_TREE);
                Settings.setAmCmpDisplayMode(MODE_TREE);
            }
        });
        JRadioButton namedAclButton = new JRadioButton("Named ACL");
        namedAclButton.setOpaque(false);
        namedAclButton.setVerticalTextPosition(JRadioButton.CENTER);
        namedAclButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                ((CardLayout) panelTab.getLayout()).show(panelTab, MODE_ACL);
                Settings.setAmCmpDisplayMode(MODE_ACL);
            }
        });

        checkSync = new JCheckBox("Sync Selection", Settings.isAmCmpSyncSelection());
        checkSync.setOpaque(false);
        checkSync.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                for (int i = 0; i < compareCount; i++) {
                    ruleSelectionListener[i].setSync(checkSync.isSelected());
                    treeSelectionListener[i].setSync(checkSync.isSelected());
                    Settings.setAmCmpSyncSelection(checkSync.isSelected());
                }
            }
        });

        ButtonGroup group = new ButtonGroup();
        group.add(ruletreeButton);
        group.add(namedAclButton);
        namedAclButton.setSelected(Settings.getAmCmpDisplayMode().equals(MODE_ACL));
        ruletreeButton.setSelected(Settings.getAmCmpDisplayMode().equals(MODE_TREE));


        toolBar = new JToolBar();
        toolBar.add(new JLabel(" Mode: "));
        toolBar.add(ruletreeButton);
        toolBar.add(namedAclButton);
        toolBar.addSeparator();
        toolBar.add(checkSync);

        return toolBar;
    }

    public JPanel buildNamedAclComponent() {
        JPanel panelACL = new JPanel();
        panelACL.setLayout(new GridLayout(1, compareCount));

        for (int index = 0; index < compareCount; index++) {
            namedRule[index] = new NamedRuleComponent(parentFrame, cam.getAccessManagers()[index], cam.getAccessManagers()[index].getFile().getName(), true);
            panelACL.add(GUIutilities.createPanelMargined(namedRule[index]));
        }

        return panelACL;
    }

    public JPanel buildRuletreeComponent() {
        JPanel panelRule = new JPanel();
        panelRule.setLayout(new GridLayout(1, compareCount));

        for (int index = 0; index < compareCount; index++) {
            ruleTree[index] = new RuleTreeComponent(parentFrame, cam.getAccessManagers()[index], cam.getAccessManagers()[index].getFile().getName(), true);
            panelRule.add(GUIutilities.createPanelMargined(ruleTree[index]));
        }

        return panelRule;
    }
    private NamedRuleSelectionListener[] ruleSelectionListener;
    private RuleTreeSelectionListener[] treeSelectionListener;

    public void attachListeners() {
        ruleSelectionListener = new NamedRuleSelectionListener[compareCount];
        treeSelectionListener = new RuleTreeSelectionListener[compareCount];
        for (int index = 0; index < compareCount; index++) {
            ruleSelectionListener[index] = new NamedRuleSelectionListener(index, namedRule, ruleTree, accessControl, Settings.isAmCmpSyncSelection());
            treeSelectionListener[index] = new RuleTreeSelectionListener(index, namedRule, ruleTree, accessControl, Settings.isAmCmpSyncSelection());
            namedRule[index].getTable().getSelectionModel().addListSelectionListener(ruleSelectionListener[index]);
            ruleTree[index].getTree().addTreeSelectionListener(treeSelectionListener[index]);
        }
    }

    private String getStr(int value) {
        String s = Integer.toString(value);
        if (s.length() < 3) {
            for (int i = s.length(); i < 3; i++) {
                s = '0' + s;
            }
        }
        return s;
    }
    private JPanel statusBar;

    public JComponent getStatusBar() {
        if (statusBar != null) {
            return statusBar;
        }

        CompareResult cmp1 = cam.getCompareResult(CompareAccessManager.ACL_TYPE, 0);
        CompareResult cmp2 = cam.getCompareResult(CompareAccessManager.ACL_TYPE, 1);

        JLabel labelNotFound1Rule = new JLabel(" " + getStr(cmp1.getResult(CompareInterface.NOT_FOUND)) + " ");
        labelNotFound1Rule.setOpaque(true);
        labelNotFound1Rule.setBorder(new BevelBorder(BevelBorder.LOWERED));
        labelNotFound1Rule.setForeground(CompareInterface.NOT_FOUND_COLOR_FOREGROUND);
        labelNotFound1Rule.setBackground(CompareInterface.NOT_FOUND_COLOR);

        JLabel labelNotEqual1Rule = new JLabel(" " + getStr(cmp1.getResult(CompareInterface.NOT_EQUAL)) + " ");
        labelNotEqual1Rule.setOpaque(true);
        labelNotEqual1Rule.setBorder(new BevelBorder(BevelBorder.LOWERED));
        labelNotEqual1Rule.setForeground(CompareInterface.NOT_EQUAL_COLOR_FOREGROUND);
        labelNotEqual1Rule.setBackground(CompareInterface.NOT_EQUAL_COLOR);

        JLabel labelNotFound2Rule = new JLabel(" " + getStr(cmp2.getResult(CompareInterface.NOT_FOUND)) + " ");
        labelNotFound2Rule.setOpaque(true);
        labelNotFound2Rule.setBorder(new BevelBorder(BevelBorder.LOWERED));
        labelNotFound2Rule.setForeground(CompareInterface.NOT_FOUND_COLOR_FOREGROUND);
        labelNotFound2Rule.setBackground(CompareInterface.NOT_FOUND_COLOR);

        JLabel labelNotEqual2Rule = new JLabel(" " + getStr(cmp2.getResult(CompareInterface.NOT_EQUAL)) + " ");
        labelNotEqual2Rule.setOpaque(true);
        labelNotEqual2Rule.setBorder(new BevelBorder(BevelBorder.LOWERED));
        labelNotEqual2Rule.setForeground(CompareInterface.NOT_EQUAL_COLOR_FOREGROUND);
        labelNotEqual2Rule.setBackground(CompareInterface.NOT_EQUAL_COLOR);


        cmp1 = cam.getCompareResult(CompareAccessManager.TREE_TYPE, 0);
        cmp2 = cam.getCompareResult(CompareAccessManager.TREE_TYPE, 1);

        JLabel labelNotFound1Tree = new JLabel(" " + getStr(cmp1.getResult(CompareInterface.NOT_FOUND)) + " ");
        labelNotFound1Tree.setOpaque(true);
        labelNotFound1Tree.setBorder(new BevelBorder(BevelBorder.LOWERED));
        labelNotFound1Tree.setForeground(CompareInterface.NOT_FOUND_COLOR_FOREGROUND);
        labelNotFound1Tree.setBackground(CompareInterface.NOT_FOUND_COLOR);

        JLabel labelNotEqual1Tree = new JLabel(" " + getStr(cmp1.getResult(CompareInterface.NOT_EQUAL)) + " ");
        labelNotEqual1Tree.setOpaque(true);
        labelNotEqual1Tree.setBorder(new BevelBorder(BevelBorder.LOWERED));
        labelNotEqual1Tree.setForeground(CompareInterface.NOT_EQUAL_COLOR_FOREGROUND);
        labelNotEqual1Tree.setBackground(CompareInterface.NOT_EQUAL_COLOR);

        JLabel labelNotFound2Tree = new JLabel(" " + getStr(cmp2.getResult(CompareInterface.NOT_FOUND)) + " ");
        labelNotFound2Tree.setOpaque(true);
        labelNotFound2Tree.setBorder(new BevelBorder(BevelBorder.LOWERED));
        labelNotFound2Tree.setForeground(CompareInterface.NOT_FOUND_COLOR_FOREGROUND);
        labelNotFound2Tree.setBackground(CompareInterface.NOT_FOUND_COLOR);

        JLabel labelNotEqual2Tree = new JLabel(" " + getStr(cmp2.getResult(CompareInterface.NOT_EQUAL)) + " ");
        labelNotEqual2Tree.setOpaque(true);
        labelNotEqual2Tree.setBorder(new BevelBorder(BevelBorder.LOWERED));
        labelNotEqual2Tree.setForeground(CompareInterface.NOT_EQUAL_COLOR_FOREGROUND);
        labelNotEqual2Tree.setBackground(CompareInterface.NOT_EQUAL_COLOR);


        JLabel labelNotFound = new JLabel(" " + CompareInterface.NOT_FOUND_LABEL + " ");
        labelNotFound.setOpaque(true);
        labelNotFound.setBorder(new BevelBorder(BevelBorder.LOWERED));
        labelNotFound.setForeground(CompareInterface.NOT_FOUND_COLOR_FOREGROUND);
        labelNotFound.setBackground(CompareInterface.NOT_FOUND_COLOR);

        JLabel labelNotEqual = new JLabel(" " + CompareInterface.NOT_EQUAL_LABEL + " ");
        labelNotEqual.setOpaque(true);
        labelNotEqual.setBorder(new BevelBorder(BevelBorder.LOWERED));
        labelNotEqual.setForeground(CompareInterface.NOT_EQUAL_COLOR_FOREGROUND);
        labelNotEqual.setBackground(CompareInterface.NOT_EQUAL_COLOR);




        JPanel panel1Rule = new JPanel();
        panel1Rule.setLayout(new GridLayout(1, 2, 2, 0));
        panel1Rule.add(labelNotFound1Rule);
        panel1Rule.add(labelNotEqual1Rule);

        JPanel panel1Tree = new JPanel();
        panel1Tree.setLayout(new GridLayout(1, 2, 2, 0));
        panel1Tree.add(labelNotFound1Tree);
        panel1Tree.add(labelNotEqual1Tree);


        JPanel panel2Rule = new JPanel();
        panel2Rule.setLayout(new GridLayout(1, 2, 2, 0));
        panel2Rule.add(labelNotFound2Rule);
        panel2Rule.add(labelNotEqual2Rule);

        JPanel panel2Tree = new JPanel();
        panel2Tree.setLayout(new GridLayout(1, 2, 2, 0));
        panel2Tree.add(labelNotFound2Tree);
        panel2Tree.add(labelNotEqual2Tree);

        JPanel panelKey = new JPanel();
        panelKey.setLayout(new GridLayout(1, 2, 2, 0));
        panelKey.add(labelNotFound);
        panelKey.add(labelNotEqual);


        JLabel labelFile1 = new JLabel("    " + cam.getAccessManagers()[0].getFile().getName() + " ");
        labelFile1.setFont(labelFile1.getFont().deriveFont(Font.BOLD));

        JLabel labelFile2 = new JLabel("    " + cam.getAccessManagers()[1].getFile().getName() + " ");
        labelFile2.setFont(labelFile2.getFont().deriveFont(Font.BOLD));

        JPanel panel1 = new JPanel();
        panel1.setLayout(new FlowLayout(FlowLayout.LEFT, 2, 1));
        panel1.add(labelFile1);
        panel1.add(new JLabel("  ACL:"));
        panel1.add(panel1Rule);
        panel1.add(new JLabel("  Tree:"));
        panel1.add(panel1Tree);

        JPanel panel2 = new JPanel();
        panel2.setLayout(new FlowLayout(FlowLayout.LEFT, 2, 1));
        panel2.add(labelFile2);
        panel2.add(new JLabel("  ACL:"));
        panel2.add(panel2Rule);
        panel2.add(new JLabel("  Tree:"));
        panel2.add(panel2Tree);

        JPanel panelRight = new JPanel();
        panelRight.setLayout(new GridLayout(1, 2));
        panelRight.add(panel1);
        panelRight.add(panel2);

        JPanel panelLeft = new JPanel();
        panelLeft.setLayout(new FlowLayout(FlowLayout.LEFT, 2, 1));
        panelLeft.add(new JLabel(" Key:"));
        panelLeft.add(panelKey);

        statusBar = new JPanel();
        statusBar.setLayout(new BorderLayout());
        statusBar.add(panelLeft, BorderLayout.WEST);
        statusBar.add(panelRight, BorderLayout.CENTER);

        return statusBar;
    }

    public String getTitle() {
        return cam.getAccessManagers()[0].getFile().getName() + "::" + cam.getAccessManagers()[1].getFile().getName();
    }
    private ImageIcon iconRuleTree;

    public ImageIcon getIcon() {
        if (iconRuleTree == null) {
            try {
                iconRuleTree = ResourceLoader.getImage(ImageEnum.amRuletree);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error Load Images", JOptionPane.ERROR_MESSAGE);
            }
        }
        return iconRuleTree;
    }
}
