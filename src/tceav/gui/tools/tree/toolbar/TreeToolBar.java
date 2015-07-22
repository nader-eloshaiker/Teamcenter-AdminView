/*
 * TreeToolbar.java
 *
 * Created on 10 November 2008, 12:56
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tceav.gui.tools.tree.toolbar;

import javax.swing.tree.*;
import javax.swing.*;
import java.awt.event.*;
import tceav.gui.*;
import tceav.gui.tools.ClipboardManager;
import tceav.gui.tools.GUIutilities;
import tceav.gui.tools.tree.JTreeAdvanced;
import tceav.resources.*;

/**
 *
 * @author nzr4dl-e
 */
public class TreeToolBar extends JToolBar {
    
    private ClipboardManager clipboard;
    private TreeCopyModel copyModel;
    private JTreeAdvanced tree;
    private AdminViewFrame parentFrame;
    public int progressCounter;
    public int progressLimit;
    
    
    public TreeToolBar(JTreeAdvanced tree, AdminViewFrame parentFrame, TreeCopyModel copyModel) {
        this.copyModel = copyModel;
        this.parentFrame = parentFrame;
        this.tree = tree;
        clipboard = parentFrame.getClipboardManager();
        progressCounter = 0;
        progressLimit = 0;
        
        JButton buttonExpandBelow = new JButton();
        buttonExpandBelow.setOpaque(false);
        buttonExpandBelow.setToolTipText("Expand Below");
        buttonExpandBelow.addActionListener(new ActionListener() {//new TreeToolBarExpandAction(tree, parentFrame){
            public void actionPerformed(ActionEvent e) {
                new Thread() {
                    @Override
                    public void run() {
                        expandTreeBranch();
                    }
                }.start();
            }
        });
        JButton buttonExpandAll = new JButton();
        buttonExpandAll.setOpaque(false);
        buttonExpandAll.setToolTipText("Expand All");
        buttonExpandAll.addActionListener(new ActionListener() {//new TreeToolBarExpandAction(tree, parentFrame){
            public void actionPerformed(ActionEvent e) {
                new Thread() {
                    @Override
                    public void run() {
                        expandTree();
                    }
                }.start();
            }
        });
        JButton buttonCollapseBelow = new JButton();
        buttonCollapseBelow.setOpaque(false);
        buttonCollapseBelow.setToolTipText("Collapse Below");
        buttonCollapseBelow.addActionListener(new ActionListener() {//new TreeToolBarExpandAction(tree, parentFrame){
            public void actionPerformed(ActionEvent e) {
                new Thread() {
                    @Override
                    public void run() {
                        collapseTreeBranch();
                    }
                }.start();
            }
        });
        JButton buttonCollapseAll = new JButton();
        buttonCollapseAll.setOpaque(false);
        buttonCollapseAll.setToolTipText("Collapse Below");
        buttonCollapseAll.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new Thread() {
                    @Override
                    public void run() {
                        collapseTree();
                    }
                }.start();
            }
        });
        
        ActionListener copyAction = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                copyToClipboard();
            }
        };
        
        KeyStroke copy = KeyStroke.getKeyStroke(KeyEvent.VK_C,ActionEvent.CTRL_MASK,false);
        tree.registerKeyboardAction(copyAction, "Copy", copy,JComponent.WHEN_FOCUSED);

        JButton buttonCopyToClipboard = new JButton();
        buttonCopyToClipboard.setOpaque(false);
        buttonCopyToClipboard.setToolTipText("Copy to Clipboard (CTRL-C)");
        buttonCopyToClipboard.addActionListener(copyAction);
        
        ImageIcon iconExpandAll = new ImageIcon();
        ImageIcon iconExpandBelow = new ImageIcon();
        ImageIcon iconCollapseAll = new ImageIcon();
        ImageIcon iconCollapseBelow = new ImageIcon();
        ImageIcon iconCopyToClipboard = new ImageIcon();
        try {
            iconExpandAll = ResourceLoader.getImage(ImageEnum.utilExpandAll);
            iconExpandBelow = ResourceLoader.getImage(ImageEnum.utilExpand);
            iconCollapseAll = ResourceLoader.getImage(ImageEnum.utilCollapseAll);
            iconCollapseBelow = ResourceLoader.getImage(ImageEnum.utilCollapse);
            iconCopyToClipboard = ResourceLoader.getImage(ImageEnum.utilClipboardCopy);
        } catch (Exception e) {
            System.out.println("Couldn't load images: " + e);
        }
        
        buttonExpandAll.setIcon(iconExpandAll);
        buttonExpandBelow.setIcon(iconExpandBelow);
        buttonCollapseAll.setIcon(iconCollapseAll);
        buttonCollapseBelow.setIcon(iconCollapseBelow);
        buttonCopyToClipboard.setIcon(iconCopyToClipboard);
        
        
        setMargin(GUIutilities.GAP_INSETS);
        setFloatable(false);
        add(buttonExpandAll);
        add(buttonCollapseAll);
        add(buttonExpandBelow);
        add(buttonCollapseBelow);
        addSeparator();
        add(buttonCopyToClipboard);
        
    }
    
    public void copyToClipboard() {
        TreePath[] treePaths = tree.getSelectionPaths();
        if(treePaths == null || treePaths.length == 0) {
            return;
        }
        
        String s = copyModel.getEntryAsString(treePaths[0]);
        
        for(int i=1; i<treePaths.length; i++){
            s += "\n"+copyModel.getEntryAsString(treePaths[i]);
        }
        
        clipboard.copyToClipboard(s);
    }
    
    public void collapseTree() {
        tree.clearToggledPaths();
        progressCounter = 0;
        progressLimit = 0;
        ProgressMonitor monitor = new ProgressMonitor(
                parentFrame,
                "Collapsing Tree",
                "",
                0,
                0);
        setCascadeTreeExpansion(tree,tree.getPathForRow(0),false, monitor);
        monitor.close();
        tree.expandRow(0);
    }
    
    public void collapseTreeBranch() {
        if(!tree.isSelectionEmpty()){
            progressCounter = 0;
            progressLimit = 0;
            tree.clearToggledPaths();
            ProgressMonitor monitor = new ProgressMonitor(
                    parentFrame,
                    "Collapsing Branch",
                    "",
                    0,
                    0);
            setCascadeTreeExpansion(tree,tree.getSelectionPath(),false, monitor);
            monitor.close();
        }
    }
    
    public void expandTree() {
        progressCounter = 0;
        progressLimit = 0;
        ProgressMonitor monitor = new ProgressMonitor(
                parentFrame,
                "Expanding Tree",
                "",
                0,
                0);
        setCascadeTreeExpansion(tree, tree.getPathForRow(0), true, monitor);
        monitor.close();
    }
    
    public void expandTreeBranch() {
        if(!tree.isSelectionEmpty()) {
            progressCounter = 0;
            progressLimit = 0;
            ProgressMonitor monitor = new ProgressMonitor(
                    parentFrame,
                    "Expanding Branch",
                    "",
                    0,
                    0);
            setCascadeTreeExpansion(tree,tree.getSelectionPath(),true, monitor);
            monitor.close();
        }
    }
    
    public void setCascadeTreeExpansion(JTreeAdvanced tree, TreePath parent, boolean expand, ProgressMonitor monitor) {
        int childCount = tree.getModel().getChildCount(parent.getLastPathComponent());
        progressLimit += childCount;
        monitor.setMaximum(progressLimit);
        Object child;
        
        if(childCount > 0) {
            for (int e=0; e<childCount; e++ ) {
                progressCounter++;
                child = tree.getModel().getChild(parent.getLastPathComponent(),e);
                if(child != null) {
                    monitor.setProgress(progressCounter);
                    monitor.setNote(progressCounter+" "+tree.getModel().getChild(parent.getLastPathComponent(),e).toString());
                    TreePath path = parent.pathByAddingChild(tree.getModel().getChild(parent.getLastPathComponent(), e));
                    if(monitor.isCanceled())
                        break;
                
                    setCascadeTreeExpansion(tree, path, expand, monitor);
                }
            }
        }
        
        if (expand) {
            tree.expandPath(parent);
        } else {
            tree.collapsePath(parent);
        }
    }
    
}
