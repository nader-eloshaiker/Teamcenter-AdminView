/*
 * Utilities.java
 *
 * Created on 20 July 2007, 13:28
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tcav.gui;

import javax.swing.*;
import javax.swing.table.*;
import javax.swing.tree.*;
import javax.swing.border.*;
import java.awt.*;
import tcav.gui.procedure.NodeReference;
/**
 *
 * @author nzr4dl
 */
public class Utilities {
    public final static int GAP_COMPONENT = 4;
    public final static int GAP_INSET = 1;
    public final static int GAP_MARGIN = 4;
    public static int progressCounter = 0;
    public static int progressLimit = 0;
    
    /**
     * Creates a new instance of Utilities
     */
    public Utilities() {
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
            packColumn(table, c, 2);
        }
    }
    
    // Sets the preferred width of the visible column specified by columnIndex. The column
    // will be just wide enough to show the column head and the widest cell in the column.
    // margin pixels are added to the left and right
    // (resulting in an additional width of 2*margin pixels).
    private static void packColumn(JTable table, int columnIndex, int margin) {
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
            int totalWidth;
            int tableWidth = table.getPreferredSize().width;
            int parentWidth = table.getParent().getWidth();
            int newWidth = table.getPreferredSize().width;
            
            if(parentWidth > tableWidth){
                newWidth += 2*margin;
                col.setPreferredWidth(width + (parentWidth - tableWidth));
            }
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
                if(!tree.getModel().isLeaf(path.getLastPathComponent())){
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
    
    public static void expandWorkflow(JTreeAdvanced tree, JFrame parentFrame, int procedureType) {
        progressCounter = 0;
        progressLimit = 0;
        ProgressMonitor monitor = new ProgressMonitor(
                parentFrame,
                "Expanding Tree",
                "",
                0,
                0);
        setCascadeTreeExpansionWorkflow(tree, tree.getSelectionPath(), true, monitor, procedureType);
        monitor.close();
    }
    
    public static void setCascadeTreeExpansionWorkflow(JTreeAdvanced tree, TreePath parent, boolean expand, ProgressMonitor monitor, int procedureType) {
        NodeReference nrParent = (NodeReference)parent.getLastPathComponent();
        int childCount = tree.getModel().getChildCount(parent.getLastPathComponent());
        progressLimit += childCount;
        monitor.setMaximum(progressLimit);
        if(childCount > 0) {
            for (int e=0; e<childCount; e++ ) {
                progressCounter++;
                monitor.setProgress(progressCounter);
                monitor.setNote(progressCounter+" "+tree.getModel().getChild(parent.getLastPathComponent(),e).toString());
                
                TreePath path = parent.pathByAddingChild(tree.getModel().getChild(parent.getLastPathComponent(), e));
                NodeReference nrChild = (NodeReference)path.getLastPathComponent();
                if(monitor.isCanceled())
                    break;
                if( (!tree.getModel().isLeaf(path.getLastPathComponent())) &&
                        ( (nrChild.getProcedureType() == procedureType) || (nrChild.getProcedureType() == NodeReference.PROCEDURE_WORKFLOW_PROCESS)) )
                    setCascadeTreeExpansionWorkflow(tree, path, expand, monitor, procedureType);
            }
        }
        
        // Expansion or collapse must be done bottom-up
        if(nrParent.getProcedureType() == procedureType) {
            if (expand)
                tree.expandPath(parent);
            else
                tree.collapsePath(parent);
        }
    }
    
}
