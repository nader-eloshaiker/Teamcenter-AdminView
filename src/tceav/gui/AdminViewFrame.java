/*
 * AdminViewFrame.java
 *
 * Created on 20 June 2007, 15:17
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package tceav.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.net.URL;
import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.LookAndFeel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import tceav.gui.compare.CompareAccessManagerComponent;
import tceav.gui.compare.CompareTabChooser;
import tceav.gui.tools.ClipboardManager;
import tceav.gui.access.AccessManagerComponent;
import tceav.gui.procedure.ProcedureManagerComponent;
import tceav.gui.tools.CustomFileFilter;
import tceav.gui.tools.GUIutilities;
import tceav.resources.ImageEnum;
import tceav.resources.ResourceLoader;
import tceav.resources.ResourceStrings;
import tceav.manager.ManagerAdapter;
import tceav.manager.access.AccessManager;
import tceav.manager.procedure.ProcedureManager;
import tceav.manager.compare.CompareAccessManager;
import tceav.Settings;

/**
 *
 * @author NZR4DL
 */
public class AdminViewFrame extends JFrame {

    /** Window for showing Tree. */
    private AdminViewFrame parentFrame;
    private JTabbedPane tabbedpane;
    private EmptyComponent emptyPane;
    private ImageIcon iconProcedure;
    private ImageIcon iconClose;
    private ImageIcon iconExit;
    private ImageIcon iconApp;
    private ImageIcon iconRuleTree;
    private ImageIcon iconCompare;
    private JPanel mainPanel;
    private final String TABPANE = "TABPANE";
    private final String EMPTYPANE = "EMPTYPANE";
    private ClipboardManager clipboard;

    /**
     * Creates a new instance of AdminViewFrame
     */
    public AdminViewFrame() {
        super("TcAV");
        parentFrame = this;

        try {
            Settings.load();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(parentFrame, ex.getMessage(), "Load Settings Error", JOptionPane.ERROR_MESSAGE);
        }

        try {
            if (Settings.getUserInterface().equals("")) {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                Settings.setUserInterface(UIManager.getCrossPlatformLookAndFeelClassName());
            } else {
                UIManager.setLookAndFeel(Settings.getUserInterface());
            // If you want the System L&F instead, comment out the above line and
            // uncomment the following:
            // UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(parentFrame, ex.getMessage(), "GUI Error", JOptionPane.ERROR_MESSAGE);
        }

        try {
            iconRuleTree = ResourceLoader.getImage(ImageEnum.amRuletree);
            iconProcedure = ResourceLoader.getImage(ImageEnum.pmWorkflow);
            iconClose = ResourceLoader.getImage(ImageEnum.utilClose);
            iconExit = ResourceLoader.getImage(ImageEnum.utilExit);
            iconApp = ResourceLoader.getImage(ImageEnum.appIcon);
            iconCompare = ResourceLoader.getImage(ImageEnum.utilCompare);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(parentFrame, ex.getMessage(), "Error Load Images", JOptionPane.ERROR_MESSAGE);
        }

        JMenuBar menuBar = constructMenuBar();
        JPanel toolbar = constructToolBar();
        JPanel statusBar = constructStatusBar();

        tabbedpane = new JTabbedPane();
        tabbedpane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        tabbedpane.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                if (tabbedpane.getTabCount() > 1) {
                    showBarComponent((TabbedPanel) tabbedpane.getSelectedComponent());
                }
            }
        });
        emptyPane = new EmptyComponent();

        mainPanel = new JPanel();
        mainPanel.setLayout(new CardLayout());
        mainPanel.add(tabbedpane, TABPANE);
        mainPanel.add(emptyPane, EMPTYPANE);
        ((CardLayout) mainPanel.getLayout()).show(mainPanel, EMPTYPANE);
        addBarComponent(emptyPane);
        showBarComponent(emptyPane);


        //this.getContentPane().setLayout(new BorderLayout(1,1));
        this.getContentPane().add(mainPanel, BorderLayout.CENTER);
        this.getContentPane().add(toolbar, BorderLayout.NORTH);
        this.getContentPane().add(statusBar, BorderLayout.SOUTH);
        this.setJMenuBar(menuBar);
        this.setIconImage(iconApp.getImage());
        this.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                actionExit();
            }
        });

        this.pack();
        this.setSize(new Dimension(Settings.getFrameSizeX(), Settings.getFrameSizeY()));
        this.setLocation(
                Settings.getFrameLocationX(),
                Settings.getFrameLocationY());
        this.setVisible(true);
        this.setTitle(ResourceStrings.getApplicationNameShort());

        clipboard = new ClipboardManager();
    }

    public ClipboardManager getClipboardManager() {
        return clipboard;
    }

    public JFileChooser createFileChooser(String type) {
        String path = "";
        JFileChooser fc = new JFileChooser();

        if (type.equals(ManagerAdapter.ACCESS_MANAGER_TYPE)) {
            path = Settings.getAmLoadPath();
            fc.setCurrentDirectory(new File(path));
            fc.addChoosableFileFilter(new CustomFileFilter(
                    new String[]{"txt", ""}, "2007/v9/v8.. Access Manager File (*.txt; *.)"));
            fc.addChoosableFileFilter(new CustomFileFilter(
                    new String[]{"xml"}, "2008 and higher Access Manager File (*.xml; *.)"));
            fc.addChoosableFileFilter(new CustomFileFilter(
                    new String[]{"xml","txt", ""}, "Access Manager File (*.xml; *.txt; *.)"));
        } else if (type.equals(ManagerAdapter.PROCEDURE_MANAGER_TYPE)) {
            path = Settings.getPmLoadPath();
            fc.setCurrentDirectory(new File(path));
            fc.addChoosableFileFilter(new CustomFileFilter(
                    new String[]{"xml"}, "XML File (*.xml)"));
        }

        return fc;
    }

    public JFrame getFrame() {
        return this;
    }
    private JPanel statusBarPanel;

    private JPanel constructStatusBar() {
        statusBarPanel = new JPanel();
        statusBarPanel.setBorder(new EmptyBorder(1, 1, 1, 1));
        statusBarPanel.setLayout(new CardLayout());
        return statusBarPanel;
    }

    private void addBarComponent(TabbedPanel tab) {
        //((CardLayout) statusBarPanel.getLayout()).addLayoutComponent(tab.getStatusBar(), Integer.toString(tab.hashCode()));
        statusBarPanel.add(tab.getStatusBar(), Integer.toString(tab.hashCode()));
        //((CardLayout) toolBarPanel.getLayout()).addLayoutComponent(tab.getToolBar(), Integer.toString(tab.hashCode()));
        toolBarPanel.add(tab.getToolBar(), Integer.toString(tab.hashCode()));
    }

    private void removeBarComponent(TabbedPanel tab) {
        //((CardLayout) statusBarPanel.getLayout()).removeLayoutComponent(tab.getStatusBar());
        statusBarPanel.remove(tab.getStatusBar());
        //((CardLayout) toolBarPanel.getLayout()).removeLayoutComponent(tab.getToolBar());
        toolBarPanel.remove(tab.getToolBar());
    }

    private void showBarComponent(TabbedPanel tab) {
        ((CardLayout) statusBarPanel.getLayout()).show(statusBarPanel, Integer.toString(tab.hashCode()));
        ((CardLayout) toolBarPanel.getLayout()).show(toolBarPanel, Integer.toString(tab.hashCode()));
    }

    public void addTabbedPane(TabbedPanel tab) {
        ((CardLayout) mainPanel.getLayout()).show(mainPanel, TABPANE);
        tabbedpane.addTab(tab.getTitle(), tab.getIcon(), tab);
        tabbedpane.setSelectedComponent(tab);
        addBarComponent(tab);
        showBarComponent(tab);

        buttonClose.setEnabled(true);
        menuClose.setEnabled(true);

    }

    private void removeTabbedPane(TabbedPanel tab) {
        if (tabbedpane.getTabCount() <= 0) {
            return;
        }
        removeBarComponent(tab);
        tabbedpane.remove(tabbedpane.getSelectedIndex());
        if (tabbedpane.getTabCount() == 0) {
            ((CardLayout) mainPanel.getLayout()).show(mainPanel, EMPTYPANE);
            showBarComponent(emptyPane);
            buttonClose.setEnabled(false);
            menuClose.setEnabled(false);
        }
    }
    private JButton buttonClose;
    private JButton buttonCompare;
    private JPanel toolBarPanel;

    private JPanel constructToolBar() {
        JToolBar toolbar = new JToolBar("Main ToolBar");

        JButton buttonOpenRuleTree = new JButton("Load Tree");
        buttonOpenRuleTree.setOpaque(false);
        buttonOpenRuleTree.setIcon(iconRuleTree);
        buttonOpenRuleTree.setHorizontalTextPosition(SwingConstants.RIGHT);
        buttonOpenRuleTree.setToolTipText("Import TcAE Ruletree File");
        buttonOpenRuleTree.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                actionLoadRuleTree();
            }
        });

        JButton buttonOpenProcedure = new JButton("Load Procedure");
        buttonOpenProcedure.setOpaque(false);
        buttonOpenProcedure.setIcon(iconProcedure);
        buttonOpenProcedure.setHorizontalTextPosition(SwingConstants.RIGHT);
        buttonOpenProcedure.setToolTipText("Import TcAE Procedure File");
        buttonOpenProcedure.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                actionLoadProcedure();
            }
        });

        buttonCompare = new JButton("Compare");
        buttonCompare.setOpaque(false);
        buttonCompare.setIcon(iconCompare);
        buttonCompare.setHorizontalTextPosition(SwingConstants.RIGHT);
        buttonCompare.setToolTipText("Compare tabbs");
        buttonCompare.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                actionCompare();
            }
        });

        buttonClose = new JButton("Close Tab");
        buttonClose.setOpaque(false);
        buttonClose.setIcon(iconClose);
        buttonClose.setEnabled(false);
        buttonClose.setHorizontalTextPosition(SwingConstants.RIGHT);
        buttonClose.setToolTipText("Close the current tabb");
        buttonClose.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                actionCloseTab();
            }
        });

        JButton buttonExit = new JButton("Exit");
        buttonExit.setOpaque(false);
        buttonExit.setIcon(iconExit);
        buttonExit.setHorizontalTextPosition(SwingConstants.RIGHT);
        buttonExit.setToolTipText("End this application");
        buttonExit.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                actionExit();
            }
        });

        toolbar.setMargin(GUIutilities.GAP_INSETS);
        toolbar.add(buttonOpenRuleTree);
        toolbar.add(buttonOpenProcedure);
        toolbar.addSeparator();
        toolbar.add(buttonCompare);
        toolbar.addSeparator();
        toolbar.add(buttonClose);
        toolbar.add(buttonExit);

        toolBarPanel = new JPanel();
        toolBarPanel.setLayout(new CardLayout());
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(0, 0));//FlowLayout(FlowLayout.LEFT,0,0));

        panel.add(toolbar, BorderLayout.WEST);//, BorderLayout.WEST);

        panel.add(toolBarPanel, BorderLayout.CENTER);

        return panel;
    }
    private JCheckBoxMenuItem menuItemSaveSettingsOnExit;
    private JMenuItem menuClose;
    private JMenuItem menuCompare;

    /** Construct a menu. */
    private JMenuBar constructMenuBar() {
        JMenu menu;
        JMenuBar menuBar = new JMenuBar();
        JMenuItem menuItem;



        /* Good ol exit. */
        menu = new JMenu("File");
        menu.setMnemonic('F');
        menuBar.add(menu);

        menuItem = menu.add(new JMenuItem("Load RuleTree", 'R'));
        menuItem.setAccelerator(KeyStroke.getKeyStroke('R', Event.CTRL_MASK));
        menuItem.setIcon(iconRuleTree);
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                actionLoadRuleTree();
            }
        });

        menuItem = menu.add(new JMenuItem("Load Procedure", 'P'));
        menuItem.setAccelerator(KeyStroke.getKeyStroke('P', Event.CTRL_MASK));
        menuItem.setIcon(iconProcedure);
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                actionLoadProcedure();
            }
        });

        menu.addSeparator();

        menuClose = menu.add(new JMenuItem("Close Tab", 'C'));
        menuClose.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, Event.CTRL_MASK));
        menuClose.setIcon(iconClose);
        menuClose.setEnabled(false);
        menuClose.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                actionCloseTab();
            }
        });

        menuItem = menu.add(new JMenuItem("Exit", 'E'));
        menuItem.setIcon(iconExit);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, Event.ALT_MASK));
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                actionExit();
            }
        });

        /* Tools */
        menu = new JMenu("Tools");
        menu.setMnemonic('T');
        menuBar.add(menu);

        menuCompare = menu.add(new JMenuItem("Compare", 'o'));
        menuCompare.setIcon(iconCompare);
        menuCompare.setAccelerator(KeyStroke.getKeyStroke('O', Event.CTRL_MASK));
        menuCompare.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                actionCompare();
            }
        });


        /* Edit. */
        menu = new JMenu("Edit");
        menu.setMnemonic('E');
        menuBar.add(menu);

        menuItem = menu.add(new JMenuItem("Save Settings", 'S'));
        menuItem.setAccelerator(KeyStroke.getKeyStroke('S', Event.CTRL_MASK));
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                actionSaveSettings();
            }
        });
        menu.addSeparator();
        menuItemSaveSettingsOnExit = new JCheckBoxMenuItem("Save Settings on Exit");
        menuItemSaveSettingsOnExit.setSelected(Settings.isSaveSettingsOnExit());
        menuItem = menu.add(menuItemSaveSettingsOnExit);
        menuItem.setMnemonic('X');
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Settings.setSaveSettingsOnExit(menuItemSaveSettingsOnExit.isSelected());
                actionSaveSettings();
            }
        });

        /* Look and Feel */
        menu = new JMenu("Interface");
        menuBar.add(menu);
        menu.setMnemonic('I');

        UIManager.LookAndFeelInfo[] lafInfo = UIManager.getInstalledLookAndFeels();
        ButtonGroup lafMenuGroup = new ButtonGroup();

        for (int counter = 0; counter < lafInfo.length; counter++) {
            menuItem = (JRadioButtonMenuItem) menu.add(new JRadioButtonMenuItem(lafInfo[counter].getName()));
            lafMenuGroup.add(menuItem);
            if (lafInfo[counter].getClassName().equals(UIManager.getLookAndFeel().getClass().getName())) {
                lafMenuGroup.setSelected(menuItem.getModel(), true);
            }
            menuItem.addActionListener(new ChangeLookAndFeelAction(lafInfo[counter].getClassName()));
            menuItem.setEnabled(isAvailableLookAndFeel(lafInfo[counter].getClassName()));
        }

        /* Misc. */
        menu = new JMenu("Help");
        menu.setMnemonic('H');
        menuBar.add(menu);

        menuItem = menu.add(new JMenuItem("Change Log", 'C'));
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                URL url = null;
                JEditorPane html = new JEditorPane();
                html.setEditable(false);
                try {
                    url = tceav.resources.ResourceStrings.getChangeLog();
                    html = new JEditorPane(url);
                    html.setEditable(false);
                } catch (Exception ex) {
                    System.err.println("Failed to open file");
                    url = null;
                }
                if (html != null) {
                    JScrollPane scroll = new JScrollPane();
                    scroll.setPreferredSize(new Dimension(500, 500));
                    scroll.getViewport().add(html);
                    scroll.setBorder(new BevelBorder(BevelBorder.LOWERED));
                    JOptionPane.showMessageDialog(getFrame(), scroll, "Change Log", JOptionPane.PLAIN_MESSAGE, null);
                }
            }
        });

        menuItem = menu.add(new JMenuItem("License", 'L'));
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                URL url = null;
                JEditorPane html = new JEditorPane();
                html.setEditable(false);
                try {
                    url = tceav.resources.ResourceStrings.getLicense();
                    html = new JEditorPane(url);
                    html.setEditable(false);
                } catch (Exception ex) {
                    System.err.println("Failed to open file");
                    url = null;
                }
                if (html != null) {
                    JScrollPane scroll = new JScrollPane();
                    scroll.setPreferredSize(new Dimension(500, 500));
                    scroll.getViewport().add(html);
                    scroll.setBorder(new BevelBorder(BevelBorder.LOWERED));
                    JOptionPane.showMessageDialog(getFrame(), scroll, "License", JOptionPane.PLAIN_MESSAGE, null);
                }
            }
        });

        menuItem = menu.add(new JMenuItem("About", 'A'));
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                actionAbout();
            }
        });

        return menuBar;
    }
    private JDialog aboutDialog;

    protected void actionAbout() {
        JLabel labelVersion = new JLabel("Version:");
        labelVersion.setHorizontalAlignment(SwingConstants.RIGHT);
        JTextField textVersion = new JTextField(ResourceStrings.getVersion());
        textVersion.setEditable(false);
        textVersion.setBorder(null);

        JLabel labelBuild = new JLabel("Build:");
        labelBuild.setHorizontalAlignment(SwingConstants.RIGHT);
        JTextField textBuild = new JTextField(ResourceStrings.getBuild());
        textBuild.setEditable(false);
        textBuild.setBorder(null);

        JLabel labelDate = new JLabel("Release Date:");
        labelDate.setHorizontalAlignment(SwingConstants.RIGHT);
        JTextField textDate = new JTextField(ResourceStrings.getReleaseDate());
        textDate.setEditable(false);
        textDate.setBorder(null);

        JLabel labelDevName = new JLabel("Developed By:");
        labelDevName.setHorizontalAlignment(SwingConstants.RIGHT);
        JTextField textDevName = new JTextField(ResourceStrings.getDeveloperName());
        textDevName.setEditable(false);
        textDevName.setBorder(null);

        JLabel labelDevEmail = new JLabel("Email:");
        labelDevEmail.setHorizontalAlignment(SwingConstants.RIGHT);
        JTextField textDevEmail = new JTextField(ResourceStrings.getDeveloperEmail());
        textDevEmail.setEditable(false);
        textDevEmail.setBorder(null);

        JLabel labelWebsite = new JLabel("Website:");
        labelWebsite.setHorizontalAlignment(SwingConstants.RIGHT);
        JTextField textWebsite = new JTextField(ResourceStrings.getWebsite());
        textWebsite.setEditable(false);
        textWebsite.setBorder(null);



        JPanel panelInfoLeft = new JPanel();
        panelInfoLeft.setLayout(new GridLayout(9, 1));
        panelInfoLeft.add(labelVersion);
        panelInfoLeft.add(labelBuild);
        panelInfoLeft.add(labelDate);
        panelInfoLeft.add(new JLabel());
        panelInfoLeft.add(labelDevName);
        panelInfoLeft.add(labelDevEmail);
        panelInfoLeft.add(new JLabel());
        panelInfoLeft.add(labelWebsite);
        panelInfoLeft.add(new JLabel());

        JPanel panelInfoRight = new JPanel();
        panelInfoRight.setLayout(new GridLayout(9, 1));
        panelInfoRight.add(textVersion);
        panelInfoRight.add(textBuild);
        panelInfoRight.add(textDate);
        panelInfoRight.add(new JLabel());
        panelInfoRight.add(textDevName);
        panelInfoRight.add(textDevEmail);
        panelInfoRight.add(new JLabel());
        panelInfoRight.add(textWebsite);
        panelInfoRight.add(new JLabel());

        JPanel panelInfo = new JPanel();
        panelInfo.setLayout(new BorderLayout(4, 4));
        panelInfo.add(panelInfoLeft, BorderLayout.WEST);
        panelInfo.add(panelInfoRight, BorderLayout.CENTER);

        try {
            ImageIcon icon = ResourceLoader.getImage(ImageEnum.appLogo);
            JLabel logoLabel = new JLabel(icon);
            logoLabel.setVerticalAlignment(SwingConstants.TOP);
            panelInfo.add(logoLabel, BorderLayout.EAST);
        } catch (Exception e) {
            System.out.println("Couldn't load images: " + e);
        }




        JTextArea textSupporters = new JTextArea(5, 20);
        textSupporters.setEditable(false);
        textSupporters.setText(ResourceStrings.getProjectSupporters());
        textSupporters.setFont(labelDevName.getFont());
        textSupporters.setOpaque(false);
        JScrollPane scrollSupport = new JScrollPane();
        scrollSupport.getViewport().add(textSupporters);

        JPanel panelSupport = new JPanel();
        panelSupport.setLayout(new BorderLayout(4, 4));
        panelSupport.add(new JLabel("Project Supporters:"), BorderLayout.NORTH);
        panelSupport.add(scrollSupport, BorderLayout.CENTER);



        JPanel panelAbout = new JPanel();
        panelAbout.setLayout(new BorderLayout());
        panelAbout.add(panelInfo, BorderLayout.CENTER);
        panelAbout.add(panelSupport, BorderLayout.SOUTH);


        JButton button = new JButton("Close");
        button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                aboutDialog.dispose();
            }
        });

        JPanel closePanel = new JPanel();
        closePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        closePanel.add(button);

        JLabel labelApp = new JLabel("TcE Admin View");
        labelApp.setFont(labelApp.getFont().deriveFont(Font.BOLD, labelApp.getFont().getSize2D() + 4));

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        titlePanel.add(labelApp);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(titlePanel, BorderLayout.NORTH);
        panel.add(panelAbout, BorderLayout.CENTER);
        panel.add(closePanel, BorderLayout.SOUTH);
        panel.setBorder(new EmptyBorder(4, 4, 4, 4));


        aboutDialog = new JDialog(this, "About", true);
        aboutDialog.setComponentOrientation(this.getComponentOrientation());
        aboutDialog.getContentPane().add(panel, BorderLayout.CENTER);
        aboutDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        aboutDialog.pack();
        aboutDialog.setResizable(false);
        aboutDialog.setLocationRelativeTo(this);
        aboutDialog.setVisible(true);
    }

    protected void actionUserInterface(String laf) {
        try {
            UIManager.setLookAndFeel(laf);
            SwingUtilities.updateComponentTreeUI(parentFrame);
            Settings.setUserInterface(laf);
        // If you want the System L&F instead, comment out the above line and
        // uncomment the following:
        // UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(parentFrame, ex.getMessage(), "GUI Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    class ChangeLookAndFeelAction extends AbstractAction {

        String laf;

        protected ChangeLookAndFeelAction(String laf) {
            super("ChangeTheme");
            this.laf = laf;
        }

        public void actionPerformed(ActionEvent e) {
            actionUserInterface(laf);
        }
    }

    /**
     * A utility function that layers on top of the LookAndFeel's
     * isSupportedLookAndFeel() method. Returns true if the LookAndFeel
     * is supported. Returns false if the LookAndFeel is not supported
     * and/or if there is any kind of error checking if the LookAndFeel
     * is supported.
     *
     * The L&F menu will use this method to detemine whether the various
     * L&F options should be active or inactive.
     *
     */
    protected boolean isAvailableLookAndFeel(String laf) {
        try {
            Class lnfClass = Class.forName(laf);
            LookAndFeel newLAF = (LookAndFeel) (lnfClass.newInstance());
            return newLAF.isSupportedLookAndFeel();
        } catch (Exception e) { // If ANYTHING weird happens, return false

            return false;
        }
    }

    private void actionSaveSettings() {
        try {
            Settings.setFrameSizeX(parentFrame.getSize().width);
            Settings.setFrameSizeY(parentFrame.getSize().height);
            Settings.setFrameLocationX(parentFrame.getLocation().x);
            Settings.setFrameLocationY(parentFrame.getLocation().y);
            Settings.store();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Save Settings Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actionCloseTab() {
        removeTabbedPane((TabbedPanel) tabbedpane.getSelectedComponent());
        System.gc();
    }

    private void actionExit() {
        Settings.setFrameSizeX(this.getSize().width);
        Settings.setFrameSizeY(this.getSize().height);
        Settings.setFrameLocationX(this.getLocation().x);
        Settings.setFrameLocationY(this.getLocation().y);
        actionSaveSettings();
        System.exit(0);
    }

    private void actionLoadRuleTree() {
        new Thread() {

            @Override
            public void run() {
                JFileChooser fc = createFileChooser(ManagerAdapter.ACCESS_MANAGER_TYPE);
                int result = fc.showOpenDialog(getFrame());
                if (result == JFileChooser.APPROVE_OPTION) {
                    Settings.setAmLoadPath(fc.getCurrentDirectory().getPath());
                    AccessManager am = new AccessManager(parentFrame);
                    try {
                        am.readFile(fc.getSelectedFile());
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(parentFrame,
                                "Corrupted File: " + fc.getSelectedFile().getName() + "\n " + ex.getMessage(),
                                "Ruletree File Error",
                                JOptionPane.ERROR_MESSAGE);
                        System.gc();
                        return;
                    }

                    if (!am.isValid()) {
                        JOptionPane.showMessageDialog(parentFrame,
                                "No rule tree found in file " + fc.getSelectedFile().getName(),
                                "Ruletree File Error",
                                JOptionPane.ERROR_MESSAGE);
                        System.gc();
                        return;
                    }

                    AccessManagerComponent amComponent = new AccessManagerComponent(parentFrame, am);
                    addTabbedPane(amComponent);

                }
                System.gc();
            }
        }.start();
    }

    private void actionLoadProcedure() {
        new Thread() {

            @Override
            public void run() {
                JFileChooser fc = createFileChooser(ManagerAdapter.PROCEDURE_MANAGER_TYPE);
                int result = fc.showOpenDialog(getFrame());
                if (result == JFileChooser.APPROVE_OPTION) {
                    Settings.setPmLoadPath(fc.getCurrentDirectory().getPath());
                    ProcedureManager pm = new ProcedureManager(parentFrame);
                    try {
                        pm.readFile(fc.getSelectedFile());
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(parentFrame,
                                "Corrupted File: " + fc.getSelectedFile().getName() + "\n " + ex.getMessage(),
                                "Procedure File Error",
                                JOptionPane.ERROR_MESSAGE);
                        System.gc();
                        return;
                    }

                    if (!pm.isValid()) {
                        JOptionPane.showMessageDialog(parentFrame,
                                "No workflow processes found in file" + fc.getSelectedFile().getName(),
                                "Procedure File Error",
                                JOptionPane.ERROR_MESSAGE);
                        System.gc();
                        return;
                    }

                    ProcedureManagerComponent proComponent = new ProcedureManagerComponent(parentFrame, pm);
                    addTabbedPane(proComponent);

                }
                System.gc();
            }
        }.start();
    }

    private void actionCompare() {
        new Thread() {

            @Override
            public void run() {
                CompareTabChooser chooser = new CompareTabChooser(parentFrame);

                int result = JOptionPane.showConfirmDialog(getFrame(), chooser, "Compare Managers", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null);

                if (result == JOptionPane.CANCEL_OPTION) {
                    return;
                }
                if (chooser.getSelectionMode().equals(ManagerAdapter.PROCEDURE_MANAGER_TYPE)) {
                    JOptionPane.showMessageDialog(parentFrame, "The ability to compare procedures has not yet been implemented.", "Unsupport Feature", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if ((chooser.getSelectedFiles()[0] == null) || (chooser.getSelectedFiles()[1] == null)) {
                    JOptionPane.showMessageDialog(parentFrame, "You need to select 1st and 2nd file.", "Selection Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (chooser.getSelectionMode().equals(ManagerAdapter.ACCESS_MANAGER_TYPE)) {
                    File files[] = chooser.getSelectedFiles();
                    AccessManager[] am = new AccessManager[files.length];
                    for (int k = 0; k < files.length; k++) {
                        am[k] = new AccessManager(parentFrame);

                        try {
                            am[k].readFile(files[k]);
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(parentFrame,
                                    "Corrupted File: " + files[k].getName() + "\n " + ex.getMessage(),
                                    "Ruletree File Error",
                                    JOptionPane.ERROR_MESSAGE);
                            System.gc();
                            return;
                        }

                        if (!am[k].isValid()) {
                            JOptionPane.showMessageDialog(parentFrame,
                                    "No rule tree found in file " + files[k].getName(),
                                    "Ruletree File Error",
                                    JOptionPane.ERROR_MESSAGE);
                            System.gc();
                            return;
                        }
                    }

                    CompareAccessManager cam = new CompareAccessManager(am);

                    if (!cam.isValid()) {
                        JOptionPane.showMessageDialog(parentFrame,
                                "Incompatible Comparison" + cam.getAccessManagers()[0].getFile().getName() + "::" + cam.getAccessManagers()[1].getFile().getName(),
                                "Comparison Error",
                                JOptionPane.ERROR_MESSAGE);
                        System.gc();
                        return;
                    }
                    CompareAccessManagerComponent camComponent = new CompareAccessManagerComponent(parentFrame, cam);
                    addTabbedPane(camComponent);

                }
            }
        }.start();
    }
}