/*
 * GUIutilities.java
 *
 * Created on 20 July 2007, 13:28
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tceav.gui;

import javax.swing.*;
import javax.swing.table.*;
import javax.swing.tree.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import tceav.resources.*;

/**
 *
 * @author nzr4dl
 */
public class GUIutilities {
    public final static int GAP_COMPONENT = 4;
    public final static Insets GAP_INSETS = new Insets(1,1,1,1);
    public final static Insets GAP_INSETS_HEADER = new Insets(2,2,2,2);
    public final static int GAP_MARGIN = 4;
    public final static int GAP_OUTER_BORDER = 8;
    public static int progressCounter = 0;
    public static int progressLimit = 0;
    
    /**
     * Creates a new instance of GUIutilities
     */
    public GUIutilities() {
    }
    
    public static JPanel createPanelMargined(JComponent component) {
        JPanel SpacedPanel = new JPanel();
        SpacedPanel.setLayout(new GridLayout(1,1,GAP_COMPONENT,GAP_COMPONENT));
        SpacedPanel.setBorder(new EmptyBorder(GAP_MARGIN,GAP_MARGIN,GAP_MARGIN,GAP_MARGIN));
        SpacedPanel.add("Center",component);
        
        return SpacedPanel;
    }
    
    public static JProgressBar createProgressBar(int minValue, int maxValue, int value, String maxTitle) {
        JProgressBar jp = new JProgressBar(minValue,maxValue);
        jp.setValue(value);
        jp.setToolTipText(value +" from a total of "+maxTitle+": " + maxValue);
        jp.setString(String.valueOf(value+" / "+maxValue));
        jp.setStringPainted(true);
        return jp;
        
    }
    
    public static void packColumns(JTable table, int margin) {
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        for (int c=0; c<table.getColumnCount(); c++) {
            packAllColumn(table, c, 2);
        }
    }
    
    // Sets the preferred width of the visible column specified by columnIndex. The column
    // will be just wide enough to show the column head and the widest cell in the column.
    // margin pixels are added to the left and right
    // (resulting in an additional width of 2*margin pixels).
    private static void packAllColumn(JTable table, int columnIndex, int margin) {
        TableModel model = table.getModel();
        DefaultTableColumnModel colModel = (DefaultTableColumnModel)table.getColumnModel();
        TableColumn col = colModel.getColumn(columnIndex);
        int width = 0;
        
        // Get width of column header
        TableCellRenderer renderer = col.getHeaderRenderer();
        if (renderer == null) {
            renderer = table.getTableHeader().getDefaultRenderer();
        }
        Component comp = renderer.getTableCellRendererComponent(
                table, col.getHeaderValue(), false, false, 0, 0);
        width = comp.getPreferredSize().width;
        
        // Get maximum width of column data
        for (int r=0; r<table.getRowCount(); r++) {
            renderer = table.getCellRenderer(r, columnIndex);
            comp = renderer.getTableCellRendererComponent(
                    table, table.getValueAt(r, columnIndex), false, false, r, columnIndex);
            width = Math.max(width, comp.getPreferredSize().width);
        }
        
        // Add margin
        width += 2*margin;
        
        // Set the width
        col.setPreferredWidth(width);
        
        
        if(table.getColumnCount() == columnIndex+1){
            int tableWidth = table.getPreferredSize().width;
            int parentWidth = table.getParent().getWidth();
            
            if(parentWidth > tableWidth){
                col.setPreferredWidth(width + (parentWidth - tableWidth));
            }
        }
        
    }
    
    public JToolBar createTreeExpandToolbar(JTreeAdvanced tree, JFrame parentFrame) {
        JButton buttonExpandBelow = new JButton();
        buttonExpandBelow.setOpaque(false);
        buttonExpandBelow.setToolTipText("Expand Below");
        buttonExpandBelow.addActionListener(new TreeToolBarExpandAction(tree, parentFrame){
            public void actionPerformed(ActionEvent e) {
                new Thread() {
                    public void run() {
                        GUIutilities.expandTreeBranch(tree, parent);
                    }
                }.start();
            }
        });
        JButton buttonExpandAll = new JButton();
        buttonExpandAll.setOpaque(false);
        buttonExpandAll.setToolTipText("Expand All");
        buttonExpandAll.addActionListener(new TreeToolBarExpandAction(tree, parentFrame){
            public void actionPerformed(ActionEvent e) {
                new Thread() {
                    public void run() {
                        GUIutilities.expandTree(tree, parent);
                    }
                }.start();
            }
        });
        JButton buttonCollapseBelow = new JButton();
        buttonCollapseBelow.setOpaque(false);
        buttonCollapseBelow.setToolTipText("Collapse Below");
        buttonCollapseBelow.addActionListener(new TreeToolBarExpandAction(tree, parentFrame){
            public void actionPerformed(ActionEvent e) {
                new Thread() {
                    public void run() {
                        GUIutilities.collapseTreeBranch(tree, parent);
                    }
                }.start();
            }
        });
        JButton buttonCollapseAll = new JButton();
        buttonCollapseAll.setOpaque(false);
        buttonCollapseAll.setToolTipText("Collapse Below");
        buttonCollapseAll.addActionListener(new TreeToolBarExpandAction(tree, parentFrame){
            public void actionPerformed(ActionEvent e) {
                new Thread() {
                    public void run() {
                        GUIutilities.collapseTree(tree, parent);
                    }
                }.start();
            }
        });
        
        ImageIcon iconExpandAll = new ImageIcon();
        ImageIcon iconExpandBelow = new ImageIcon();
        ImageIcon iconCollapseAll = new ImageIcon();
        ImageIcon iconCollapseBelow = new ImageIcon();
        try {
            iconExpandAll = ResourceLoader.getImage(ImageEnum.utilExpandAll);
            iconExpandBelow = ResourceLoader.getImage(ImageEnum.utilExpand);
            iconCollapseAll = ResourceLoader.getImage(ImageEnum.utilCollapseAll);
            iconCollapseBelow = ResourceLoader.getImage(ImageEnum.utilCollapse);
        } catch (Exception e) {
            System.out.println("Couldn't load images: " + e);
        }
        
        buttonExpandAll.setIcon(iconExpandAll);
        buttonExpandBelow.setIcon(iconExpandBelow);
        buttonCollapseAll.setIcon(iconCollapseAll);
        buttonCollapseBelow.setIcon(iconCollapseBelow);
        
        
        JToolBar toolbar = new JToolBar();
        toolbar.setMargin(GUIutilities.GAP_INSETS);
        toolbar.setFloatable(false);
        toolbar.add(buttonExpandAll);
        toolbar.add(buttonCollapseAll);
        toolbar.add(buttonExpandBelow);
        toolbar.add(buttonCollapseBelow);
        
        return toolbar;
    }
    
    class TreeToolBarExpandAction extends AbstractAction {
        protected JTreeAdvanced tree;
        protected JFrame parent;
        
        protected TreeToolBarExpandAction(JTreeAdvanced tree, JFrame parent) {
            super("Tree ToolBar Expand Action");
            this.tree = tree;
            this.parent = parent;
        }
        
        public void actionPerformed(ActionEvent e) {
            
        }
    }
    
    
    public static void collapseTree(JTreeAdvanced tree, JFrame parentFrame){
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
    
    public static void collapseTreeBranch(JTreeAdvanced tree, JFrame parentFrame){
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
    
    public static void expandTree(JTreeAdvanced tree, JFrame parentFrame) {
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
    
    public static void expandTreeBranch(JTreeAdvanced tree, JFrame parentFrame){
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
    
    public static void setCascadeTreeExpansion(JTreeAdvanced tree, TreePath parent, boolean expand, ProgressMonitor monitor) {
        int childCount = tree.getModel().getChildCount(parent.getLastPathComponent());
        progressLimit += childCount;
        monitor.setMaximum(progressLimit);
        if(childCount > 0) {
            for (int e=0; e<childCount; e++ ) {
                progressCounter++;
                monitor.setProgress(progressCounter);
                monitor.setNote(progressCounter+" "+tree.getModel().getChild(parent.getLastPathComponent(),e).toString());
                TreePath path = parent.pathByAddingChild(tree.getModel().getChild(parent.getLastPathComponent(), e));
                if(monitor.isCanceled())
                    break;
                
                setCascadeTreeExpansion(tree, path, expand, monitor);
            }
        }
        
        if (expand) {
            tree.expandPath(parent);
        } else {
            tree.collapsePath(parent);
        }
    }
}
