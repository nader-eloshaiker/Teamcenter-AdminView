package tceav.gui.access;

import tceav.gui.tools.GUIutilities;
import tceav.manager.access.AccessManager;
import tceav.gui.*;
import tceav.resources.*;
import tceav.Settings;
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
public class AccessManagerComponent extends TabbedPanel {

    private AdminViewFrame parentFrame;
    private RuleTreeComponent ruletree;
    private NamedRuleComponent namedACL;
    private AccessRuleComponent accessControl;
    private JSplitPane splitPane;
    private AccessManager am;
    private NamedRuleSelectionListener ruleSelectionListener;
    private RuleTreeSelectionListener treeSelectionListener;

    /**
     * Creates a new instance of AccessManagerComponent
     */
    public AccessManagerComponent(AdminViewFrame parent, AccessManager am) {
        super();
        this.parentFrame = parent;
        this.am = am;

        accessControl = new AccessRuleComponent(am);

        namedACL = new NamedRuleComponent(parentFrame, am);
        ruletree = new RuleTreeComponent(parentFrame, am);
        ruleSelectionListener = new NamedRuleSelectionListener(namedACL, ruletree, accessControl, Settings.isAmSyncSelection());
        namedACL.getTable().getSelectionModel().addListSelectionListener(ruleSelectionListener);
        treeSelectionListener = new RuleTreeSelectionListener(namedACL, accessControl, Settings.isAmSyncSelection());
        ruletree.getTree().addTreeSelectionListener(treeSelectionListener);

        /* Rules Panel */
        JPanel panelRule = new JPanel();
        panelRule.setLayout(new BorderLayout(GUIutilities.GAP_MARGIN, GUIutilities.GAP_MARGIN));
        panelRule.add(GUIutilities.createPanelMargined(namedACL), BorderLayout.EAST);
        panelRule.add(GUIutilities.createPanelMargined(ruletree), BorderLayout.CENTER);

        splitPane = new JSplitPane(
                JSplitPane.VERTICAL_SPLIT, true, panelRule,
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

    public String getTitle() {
        return am.getFile().getName();
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
    private JToolBar toolBar;
    private JCheckBox checkSync;

    public JToolBar getToolBar() {

        if (toolBar != null) {
            return toolBar;
        }

        checkSync = new JCheckBox("Sync Selection", Settings.isAmCmpSyncSelection());
        checkSync.setOpaque(false);
        checkSync.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                ruleSelectionListener.setSync(checkSync.isSelected());
                treeSelectionListener.setSync(checkSync.isSelected());
                Settings.setAmSyncSelection(checkSync.isSelected());
            }
        });


        toolBar = new JToolBar();
        toolBar.add(checkSync);

        return toolBar;
    }
    private JPanel statusBar;

    public JComponent getStatusBar() {
        if (statusBar != null) {
            return statusBar;
        }

        JLabel textFile = new JLabel(" " + am.getFile().getParent() + " ");
        textFile.setBorder(new BevelBorder(BevelBorder.LOWERED));
        JPanel panelFile = new JPanel();
        panelFile.setLayout(new BorderLayout());
        panelFile.add(new JLabel(" Path:"), BorderLayout.WEST);
        panelFile.add(textFile, BorderLayout.CENTER);

        statusBar = new JPanel();
        statusBar.setLayout(new BorderLayout());
        statusBar.add(panelFile, BorderLayout.WEST);

        if (am.getMetaData().isLegacey()) {
            JLabel textAuthor = new JLabel(" " + am.getMetaData().getUserDetails() + " ");
            textAuthor.setBorder(new BevelBorder(BevelBorder.LOWERED));
            JPanel panelAuthor = new JPanel();
            panelAuthor.setLayout(new BorderLayout());
            panelAuthor.add(new JLabel("  Author:"), BorderLayout.WEST);
            panelAuthor.add(textAuthor, BorderLayout.CENTER);

            JLabel textDate = new JLabel(" " + am.getMetaData().getDate() + " ");
            textDate.setBorder(new BevelBorder(BevelBorder.LOWERED));
            JPanel panelDate = new JPanel();
            panelDate.setLayout(new BorderLayout());
            panelDate.add(new JLabel("  Date:"), BorderLayout.WEST);
            panelDate.add(textDate, BorderLayout.CENTER);

            JLabel textTime = new JLabel(" " + am.getMetaData().getTime() + " ");
            textTime.setBorder(new BevelBorder(BevelBorder.LOWERED));
            JPanel panelTime = new JPanel();
            panelTime.setLayout(new BorderLayout());
            panelTime.add(new JLabel("  Time:"), BorderLayout.WEST);
            panelTime.add(textTime, BorderLayout.CENTER);

            JPanel panelDateTime = new JPanel();
            panelDateTime.setLayout(new BorderLayout());
            panelDateTime.add(panelDate, BorderLayout.WEST);
            panelDateTime.add(panelTime, BorderLayout.EAST);

            statusBar.add(panelAuthor, BorderLayout.CENTER);
            statusBar.add(panelDateTime, BorderLayout.EAST);
        }

        return statusBar;
    }
}
