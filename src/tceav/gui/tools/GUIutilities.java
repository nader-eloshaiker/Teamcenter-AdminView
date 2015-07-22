/*
 * GUIutilities.java
 *
 * Created on 20 July 2007, 13:28
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tceav.gui.tools;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Insets;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;



/**
 *
 * @author nzr4dl
 */
public class GUIutilities {
    public final static int GAP_COMPONENT = 4;
    public final static Insets GAP_INSETS = new Insets(1,1,1,1);
    public final static Insets GAP_INSETS_HEADER = new Insets(2,2,2,2);
    public final static int GAP_MARGIN = 2;
    public final static int GAP_OUTER_BORDER = 8;
    
    
    /**
     * Creates a new instance of GUIutilities
     */
    public GUIutilities() {
    }
    
    public static JPanel createPanelMargined(JComponent component) {
        JPanel SpacedPanel = new JPanel();
        SpacedPanel.setLayout(new BorderLayout());
        SpacedPanel.setBorder(new EmptyBorder(GAP_MARGIN,GAP_MARGIN,GAP_MARGIN,GAP_MARGIN));
        SpacedPanel.add(component, BorderLayout.CENTER);
        
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
    
}
