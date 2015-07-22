/*
 * AdminViewFrame.java
 *
 * Created on 20 June 2007, 15:17
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tcadminview.gui;

import tcadminview.gui.ruletree.AccessManagerComponent;
import tcadminview.gui.procedure.ProcedureComponent;
import tcadminview.ruletree.AccessManager;
import tcadminview.procedure.ProcedureManager;
import tcadminview.ResourceLocator;
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
    // protected JFrame this;
    protected JTabbedPane tabbedpane;
    protected ImageIcon iconRuleTree;
    protected ImageIcon iconProcedure;
    protected ImageIcon iconClose;
    protected ImageIcon iconExit;
    protected File path;
    
    
    /**
     * Creates a new instance of AdminViewFrame
     */
    public AdminViewFrame() {
        super("Rule Tree");
        
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            // If you want the System L&F instead, comment out the above line and
            // uncomment the following:
            // UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception exc) {
            System.err.println("Error loading L&F: " + exc);
        }
        
        try {
            iconRuleTree = new ImageIcon(ResourceLocator.getButtonImage("RuleTree.gif"));
            iconProcedure = new ImageIcon(ResourceLocator.getButtonImage("Procedure.gif"));
            iconClose = new ImageIcon(ResourceLocator.getButtonImage("Close.gif"));
            iconExit  = new ImageIcon(ResourceLocator.getButtonImage("Exit.gif"));
        } catch (Exception e) {
            System.out.println("Couldn't load images: " + e);
        }
        
        tabbedpane = new JTabbedPane();
        JPanel panel = new JPanel(true);
        panel.setLayout(new BorderLayout());
        panel.add("Center", tabbedpane);
        
        AccessManagerComponent amComponent = new AccessManagerComponent(this);
        tabbedpane.addTab("Blank", iconRuleTree, amComponent);
        
        JMenuBar menuBar = constructMenuBar();
        JToolBar toolbar = constructToolBar();
        
        this.getContentPane().add("Center", panel);
        this.getContentPane().add("North", toolbar);
        this.setJMenuBar(menuBar);
        this.setBackground(Color.lightGray);
        this.addWindowListener( new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        
        this.pack();
        this.setSize(new Dimension(1024,740));
        this.setVisible(true);
        this.setTitle(ResourceLocator.getApplicationName()+" v"+ResourceLocator.getVersion());
    }
    
    public JFileChooser createFileChooser() {
        JFileChooser fc = new JFileChooser();
        if(path != null)
            fc.setCurrentDirectory(path);
        return fc;
    }
    
    public JFrame getFrame() {
        return this;
    }
    
    private JToolBar constructToolBar() {
        JToolBar toolbar = new JToolBar("Main ToolBar");
        
        JButton buttonOpenRuleTree = new JButton("Load Tree");
        buttonOpenRuleTree.setIcon(iconRuleTree);
        buttonOpenRuleTree.setHorizontalTextPosition(SwingConstants.RIGHT);
        buttonOpenRuleTree.setToolTipText("Import TcAE ruletree");
        buttonOpenRuleTree.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                actionLoadRuleTree();
            }
        });
        
        JButton buttonOpenProcedure = new JButton("Load Procedure");
        buttonOpenProcedure.setIcon(iconProcedure);
        buttonOpenProcedure.setHorizontalTextPosition(SwingConstants.RIGHT);
        buttonOpenProcedure.setToolTipText("Import TcAE ruletree");
        buttonOpenProcedure.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                actionLoadProcedure();
            }
        });
        
        JButton buttonClose = new JButton("Close Tab");
        buttonClose.setIcon(iconClose);
        buttonClose.setHorizontalTextPosition(SwingConstants.RIGHT);
        buttonClose.setToolTipText("Close the current tabb");
        buttonClose.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                actionCloseTab();
            }
        });
        
        JButton buttonExit = new JButton("Exit");
        buttonExit.setIcon(iconExit);
        buttonExit.setHorizontalTextPosition(SwingConstants.RIGHT);
        buttonExit.setToolTipText("End this application");
        buttonExit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        
        toolbar.setMargin(new Insets(
                Utilities.GAP_INSET,
                Utilities.GAP_INSET,
                Utilities.GAP_INSET,
                Utilities.GAP_INSET));
        toolbar.add(buttonOpenRuleTree);
        toolbar.add(buttonOpenProcedure);
        toolbar.addSeparator();
        toolbar.add(buttonClose);
        toolbar.add(buttonExit);
        
        return toolbar;
    }
    
    /** Construct a menu. */
    private JMenuBar constructMenuBar() {
        JMenu menu;
        JMenuBar menuBar = new JMenuBar();
        JMenuItem menuItem;
        
        
        /* Good ol exit. */
        menu = new JMenu("File");
        menuBar.add(menu);
        
        menuItem = menu.add(new JMenuItem("Load RuleTree"));
        menuItem.setIcon(iconRuleTree);
        menuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                actionLoadRuleTree();
            }
        });
        
        menuItem = menu.add(new JMenuItem("LoadProcedure"));
        menuItem.setIcon(iconProcedure);
        menuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                actionLoadProcedure();
            }
        });
        
        menuItem = menu.add(new JMenuItem("Close Tab"));
        menuItem.setIcon(iconClose);
        menuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                actionCloseTab();
            }
        });
        
        menuItem = menu.add(new JMenuItem("Exit"));
        menuItem.setIcon(iconExit);
        menuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        
        /* Tree related stuff. */
        menu = new JMenu("Help");
        menuBar.add(menu);
        
        menuItem = menu.add(new JMenuItem("Change Log"));
        menuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                URL url = null;
                JEditorPane html = new JEditorPane();
                try {
                    url = tcadminview.ResourceLocator.getChangeLog();
                    System.out.println(url.getPath());
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
        
        menuItem = menu.add(new JMenuItem("About"));
        menuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(
                        getFrame(),
                        ResourceLocator.getAboutInfo());
            }
        });
        
        return menuBar;
    }
    
    private void actionCloseTab() {
        tabbedpane.remove(tabbedpane.getSelectedIndex());
        if (tabbedpane.getTabCount() == 0) {
            AccessManagerComponent amComponent = new AccessManagerComponent(this);
            tabbedpane.addTab("Blank", iconRuleTree, amComponent);
        }
    }
    
    private void actionLoadRuleTree() {
        JFileChooser fc = createFileChooser();
        int result = fc.showOpenDialog(getFrame());
        if(result == JFileChooser.APPROVE_OPTION) {
            path = fc.getCurrentDirectory();
            try {
                AccessManager am = new AccessManager();
                
                try {
                    am.importRuleTree(fc.getSelectedFile());
                } catch (Exception ex) {
                    throw new Exception("Corrupted rule tree: "+fc.getSelectedFile().getName());
                }
                
                if(am.getAccessManagerTreeSize() == 0) {
                    throw new Exception("No rule tree found in file "+fc.getSelectedFile().getName());
                }
                
                for(int index=0; index<tabbedpane.getTabCount(); index++) {
                    if(((TabbedPanel)tabbedpane.getComponentAt(index)).isEmptyPanel()) {
                        tabbedpane.remove(index);
                    }
                }
                
                AccessManagerComponent amComponent = new AccessManagerComponent(this, am);
                tabbedpane.addTab(fc.getSelectedFile().getName(), iconRuleTree, amComponent);
                tabbedpane.setSelectedComponent(amComponent);
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Ruletree File Error", JOptionPane.ERROR_MESSAGE);
            }
                
        }
    }
    
    private void actionLoadProcedure() {
        JFileChooser fc = createFileChooser();
        int result = fc.showOpenDialog(getFrame());
        if(result == JFileChooser.APPROVE_OPTION) {
            path = fc.getCurrentDirectory();
            
            try {
                ProcedureManager pm = new ProcedureManager(this);
                pm.importXML(fc.getSelectedFile());
                
                if(pm.getWorkflowTemplates().size() == 0) {
                    throw new Exception("No workflow templates found in file"+fc.getSelectedFile().getName());
                }
                
                for(int index=0; index<tabbedpane.getTabCount(); index++) {
                    if(((TabbedPanel)tabbedpane.getComponentAt(index)).isEmptyPanel()) {
                        tabbedpane.remove(index);
                    }
                }

                ProcedureComponent wfComponent = new ProcedureComponent(this, pm);
                tabbedpane.addTab(fc.getSelectedFile().getName(), iconProcedure, wfComponent);
                tabbedpane.setSelectedComponent(wfComponent);
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Procedure File Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}