/*
 * AdminViewFrame.java
 *
 * Created on 20 June 2007, 15:17
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tcav.gui;

import tcav.gui.ruletree.AccessManagerComponent;
import tcav.gui.procedure.ProcedureManagerComponent;
import tcav.ruletree.AccessManager;
import tcav.procedure.ProcedureManager;
import tcav.utils.CustomFileFilter;
import tcav.ResourceLocator;
import tcav.Settings;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.URL;
import org.xml.sax.InputSource;

/**
 *
 * @author NZR4DL
 */
public class AdminViewFrame extends JFrame{
    
    /** Window for showing Tree. */
    protected JFrame parentFrame;
    protected JTabbedPane tabbedpane;
    protected ImageIcon iconRuleTree;
    protected ImageIcon iconProcedure;
    protected ImageIcon iconClose;
    protected ImageIcon iconExit;
    protected ImageIcon iconApp;
    
    /**
     * Creates a new instance of AdminViewFrame
     */
    public AdminViewFrame() {
        super("TcAV");
        parentFrame = this;
        
        try {
            Settings.load();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Load Settings Error", JOptionPane.ERROR_MESSAGE);
        }
        
        try {
            if (Settings.getUserInterface().equals("")) {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                Settings.setUserInterface(UIManager.getCrossPlatformLookAndFeelClassName());
            } else
                UIManager.setLookAndFeel(Settings.getUserInterface());
            // If you want the System L&F instead, comment out the above line and
            // uncomment the following:
            // UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "GUI Error", JOptionPane.ERROR_MESSAGE);
        }
        
        try {
            iconRuleTree = new ImageIcon(ResourceLocator.getButtonImage("RuleTree.gif"));
            iconProcedure = new ImageIcon(ResourceLocator.getButtonImage("Procedure.gif"));
            iconClose = new ImageIcon(ResourceLocator.getButtonImage("Close.gif"));
            iconExit  = new ImageIcon(ResourceLocator.getButtonImage("Exit.gif"));
            iconApp = new ImageIcon(ResourceLocator.getAppImage("logoIcon.gif"));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error Load Images", JOptionPane.ERROR_MESSAGE);
        }
        
        JMenuBar menuBar = constructMenuBar();
        JToolBar toolbar = constructToolBar();
        
        tabbedpane = new JTabbedPane();
        tabbedpane.addTab("TcAV", iconApp, new EmptyComponent());
        
        this.getContentPane().add("Center", tabbedpane);
        this.getContentPane().add("North", toolbar);
        this.setJMenuBar(menuBar);
        this.setIconImage(iconApp.getImage());
        this.addWindowListener( new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                actionExit();
            }
        });
        
        this.pack();
        this.setSize(new Dimension(Settings.getFrameSizeX(),Settings.getFrameSizeY()));
        this.setLocation(
                Settings.getFrameLocationX(),
                Settings.getFrameLocationY());
        this.setVisible(true);
        this.setTitle(ResourceLocator.getApplicationName()+" v"+ResourceLocator.getVersion());
    }
    
    public JFileChooser createFileChooser(String path) {
        JFileChooser fc = new JFileChooser();
        fc.setCurrentDirectory(new File(path));
        return fc;
    }
    
    public JFrame getFrame() {
        return this;
    }
    
    private JToolBar constructToolBar() {
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
        
        JButton buttonClose = new JButton("Close Tab");
        buttonClose.setOpaque(false);
        buttonClose.setIcon(iconClose);
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
        
        toolbar.setMargin(new Insets(
                GUIutilities.GAP_INSET,
                GUIutilities.GAP_INSET,
                GUIutilities.GAP_INSET,
                GUIutilities.GAP_INSET));
        toolbar.add(buttonOpenRuleTree);
        toolbar.add(buttonOpenProcedure);
        toolbar.addSeparator();
        toolbar.add(buttonClose);
        toolbar.add(buttonExit);
        
        return toolbar;
    }
    
    protected JCheckBoxMenuItem menuItemSaveSettingsOnExit;
    
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
        
        menuItem = menu.add(new JMenuItem("Close Tab", 'C'));
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, Event.CTRL_MASK));
        menuItem.setIcon(iconClose);
        menuItem.addActionListener(new ActionListener() {
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
        menuItemSaveSettingsOnExit.setSelected(Settings.getSaveSettingsOnExit());
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
            if(lafInfo[counter].getClassName().equals(UIManager.getLookAndFeel().getClass().getName()))
                lafMenuGroup.setSelected(menuItem.getModel(),true);
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
                try {
                    url = tcav.ResourceLocator.getChangeLog();
                    html = new JEditorPane(url);
                } catch (Exception ex) {
                    System.err.println("Failed to open file");
                    url = null;
                }
                if(html != null) {
                    JScrollPane scroll = new JScrollPane();
                    scroll.setPreferredSize(new Dimension(500,500));
                    scroll.getViewport().add(html);
                    scroll.setBorder(new BevelBorder(BevelBorder.LOWERED));
                    JPanel panel = new JPanel(true);
                    panel.setLayout(new GridLayout(1,1));
                    panel.setBorder(new TitledBorder("Change Log"));
                    JOptionPane.showMessageDialog(
                            getFrame(),scroll);
                }
            }
        });
        
        menuItem = menu.add(new JMenuItem("About", 'A'));
        menuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(
                        getFrame(),
                        ResourceLocator.getAboutInfo());
            }
        });
        
        return menuBar;
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
            LookAndFeel newLAF = (LookAndFeel)(lnfClass.newInstance());
            return newLAF.isSupportedLookAndFeel();
        } catch(Exception e) { // If ANYTHING weird happens, return false
            return false;
        }
    }
    
    private void actionSaveSettings() {
        try{
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
        tabbedpane.remove(tabbedpane.getSelectedIndex());
        if (tabbedpane.getTabCount() == 0)
            tabbedpane.addTab("TcAV", iconApp, new EmptyComponent());
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
            public void run() {
                JFileChooser fc = createFileChooser(Settings.getAMLoadPath());
                fc.addChoosableFileFilter(new CustomFileFilter(
                        new String[]{"txt",""},"Text File (*.txt; *.)"));
                int result = fc.showOpenDialog(getFrame());
                if(result == JFileChooser.APPROVE_OPTION) {
                    try {
                        Settings.setAMLoadPath(fc.getCurrentDirectory().getPath());
                        AccessManager am = new AccessManager();
                        
                        try {
                            am.readFile(fc.getSelectedFile());
                        } catch (Exception ex) {
                            throw new Exception("Corrupted File: "+fc.getSelectedFile().getName());
                        }
                        
                        if(am.getAccessManagerTree().size() == 0) {
                            throw new Exception("No rule tree found in file "+fc.getSelectedFile().getName());
                        }
                        
                        for(int index=0; index<tabbedpane.getTabCount(); index++) {
                            if(((TabbedPanel)tabbedpane.getComponentAt(index)).isEmptyPanel()) {
                                tabbedpane.remove(index);
                            }
                        }
                        
                        AccessManagerComponent amComponent = new AccessManagerComponent(parentFrame, am);
                        tabbedpane.addTab(fc.getSelectedFile().getName(), iconRuleTree, amComponent);
                        tabbedpane.setSelectedComponent(amComponent);
                        
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(parentFrame, ex.getMessage(), "Ruletree File Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
                System.gc();
            }
        }.start();
    }
    
    private void actionLoadProcedure() {
        new Thread() {
            public void run() {
                JFileChooser fc = createFileChooser(Settings.getPMLoadPath());
                fc.addChoosableFileFilter(new CustomFileFilter(
                        new String[]{"xml","plmxml"},"XML File (*.xml; *.plmxml)"));
                int result = fc.showOpenDialog(getFrame());
                if(result == JFileChooser.APPROVE_OPTION) {
                    try {
                        ProcedureManager pm;
                        Settings.setPMLoadPath(fc.getCurrentDirectory().getPath());
                        pm = new ProcedureManager(parentFrame);
                        try {
                            pm.readFile(fc.getSelectedFile());
                        } catch (Exception ex) {
                            throw new Exception("Corrupted File: "+fc.getSelectedFile().getName());
                        }
                        
                        if(pm.getWorkflowProcesses().size() == 0) {
                            throw new Exception("No workflow processes found in file"+fc.getSelectedFile().getName());
                        }
                        
                        for(int index=0; index<tabbedpane.getTabCount(); index++) {
                            if(((TabbedPanel)tabbedpane.getComponentAt(index)).isEmptyPanel()) {
                                tabbedpane.remove(index);
                            }
                        }
                        
                        ProcedureManagerComponent wfComponent = new ProcedureManagerComponent(parentFrame, pm);
                        tabbedpane.addTab(fc.getSelectedFile().getName(), iconProcedure, wfComponent);
                        tabbedpane.setSelectedComponent(wfComponent);
                        
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(parentFrame, ex.getMessage(), "Procedure File Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
                System.gc();
            }
        }.start();
    }
}