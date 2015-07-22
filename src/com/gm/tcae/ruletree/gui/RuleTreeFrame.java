/*
 * RuleTreeFrame.java
 *
 * Created on 20 June 2007, 15:17
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.gm.tcae.ruletree.gui;

import com.gm.tcae.ruletree.acl.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.URL;

/**
 *
 * @author NZR4DL
 */
public class RuleTreeFrame extends JFrame{
    
    /** Window for showing Tree. */
    // protected JFrame this;
    protected JTabbedPane tabbedpane;
    protected ImageIcon icon;
    protected File path;
    
    
    /** Creates a new instance of RuleTreeFrame */
    public RuleTreeFrame() {
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
            icon = new ImageIcon(RuleTreeFrame.class.getResource("images/acl/ruletree.gif"));
        } catch (Exception e) {
            System.out.println("Couldn't load images: " + e);
        }
        
        tabbedpane = new JTabbedPane();
        JPanel panel = new JPanel(true);
        panel.setLayout(new BorderLayout());
        panel.add("Center", tabbedpane);
        
        // Preload a file
        /*
        File newFile = new File("C:\\Java\\tree_02_Jul_2007.txt");
        AccessManager am = new AccessManager();
                    try {
                        am.importRuleTree(newFile);
                    }catch (IOException ex) {System.err.println(ex);}
                    AccessManagerComponent amComponent = new AccessManagerComponent(am);
                    tabbedpane.addTab(newFile.getName(),
                            new ImageIcon(RuleTreeFrame.class.getResource("images/acl/ruletree.gif")),
                            amComponent);
                    tabbedpane.setSelectedComponent(amComponent);
         /*/
        AccessManagerComponent amComponent = new AccessManagerComponent();
        tabbedpane.addTab("Blank", icon, amComponent);
        /**/
        
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
        this.setVisible(true);
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
        
        JButton buttonOpen = new JButton("Load Tree");
        buttonOpen.setToolTipText("Import TcAE ruletree");
        buttonOpen.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                actionLoadTree();
            }
        });
        
        JButton buttonClose = new JButton("Close Tree");
        buttonClose.setToolTipText("Close the current tabb");
        buttonClose.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                actionCloseTree();
            }
        });
        
        JButton buttonExit = new JButton("Exit");
        buttonExit.setToolTipText("End this application");
        buttonExit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        
        toolbar.add(buttonOpen);
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
        
        menuItem = menu.add(new JMenuItem("Load Tree"));
        menuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                actionLoadTree();
            }
        });
        
        menuItem = menu.add(new JMenuItem("Close Tree"));
        menuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                actionCloseTree();
            }
        });
        
        menuItem = menu.add(new JMenuItem("Exit"));
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
                    url = com.gm.tcae.ruletree.Resources.getChangeLog();
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
                        "<html><p>Rule Tree Viewer</p>"+ "<p></p>" +
                        "<p>Version: 0.1.3 Alpha </p>" +
                        "<p>Release Date: 13-Jul-07</p>"+  "<p></p>" +
                        "<p>Developed by: Nader Eloshaiker<br> General Motors - Holden</p>" +
                        "</html>");
            }
        });
        
        return menuBar;
    }
    
    private void actionCloseTree() {
        tabbedpane.remove(tabbedpane.getSelectedIndex());
        if (tabbedpane.getTabCount() == 0) {
            AccessManagerComponent amComponent = new AccessManagerComponent();
            tabbedpane.addTab("Blank", icon, amComponent);
        }
    }
    
    private void actionLoadTree() {
        JFileChooser fc = createFileChooser();
        int result = fc.showOpenDialog(getFrame());
        if(result == JFileChooser.APPROVE_OPTION) {
            path = fc.getCurrentDirectory();
            AccessManager am = new AccessManager();
            try {
                am.importRuleTree(fc.getSelectedFile());
            }catch (IOException ex) {System.err.println(ex);}
            
            for(int index=0; index<tabbedpane.getTabCount(); index++) {
                if(!((AccessManagerComponent)tabbedpane.getComponentAt(index)).isAccessManagerLoaded()) {
                    tabbedpane.remove(index);
                }
            }
            
            AccessManagerComponent amComponent = new AccessManagerComponent(am);
            tabbedpane.addTab(fc.getSelectedFile().getName(), icon, amComponent);
            tabbedpane.setSelectedComponent(amComponent);
        }
    }
    
}
