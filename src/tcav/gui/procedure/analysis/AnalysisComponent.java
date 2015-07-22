/*
 * AnalysisComponent.java
 *
 * Created on 9 May 2008, 14:48
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tcav.gui.procedure.analysis;

import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.*;
import javax.swing.border.*;
import java.awt.*;
import tcav.gui.*;
import tcav.manager.procedure.ProcedureManager;

/**
 *
 * @author nzr4dl
 */
public class AnalysisComponent extends JComponent {
    
    private JDialog dialog;
    private JTableAdvanced table;
    private JTableAdvanced tableRowHeader;
    
    /** Creates a new instance of AnalysisComponent */
    public AnalysisComponent(ProcedureManager pm, JFrame parentFrame) {
        super();
        
        DataModel data = new DataModel(pm);
        table = new JTableAdvanced(data);
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setResizingAllowed(false);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        //table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        //table.getSelectionModel().addListSelectionListener(new SelectionListener(false));
        
        tableRowHeader = new JTableAdvanced(data.createRowHeaderModel());
        tableRowHeader.getTableHeader().setReorderingAllowed(false);
        tableRowHeader.getTableHeader().setResizingAllowed(false);
        tableRowHeader.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        //tableRowHeader.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        //tableRowHeader.getSelectionModel().addListSelectionListener(new SelectionListener(true));
        
        
        TableColumn column;
        CellRenderer renderer = new CellRenderer();
        ColumnHeaderRenderer rendererCol = new ColumnHeaderRenderer();
        
        for (int i=0; i<table.getColumnCount(); i++) {
            column = table.getColumnModel().getColumn(i);
            column.setHeaderRenderer(rendererCol);
            column.setCellRenderer(renderer);
            packColumn(i);
        }
        column = tableRowHeader.getColumnModel().getColumn(0);
        column.setHeaderRenderer(new RowHeaderRenderer());
        column.setCellRenderer(new RowHeaderRenderer());
        packRowHeader();
        
        JPanel panelTable = new JPanel();
        panelTable.setLayout(new BorderLayout());
        panelTable.add(table.getTableHeader(), BorderLayout.NORTH);
        panelTable.add(table, BorderLayout.CENTER);
        
        JScrollPane scrolltable = new JScrollPane(table);
        //scrolltable.setColumnHeaderView(table.getTableHeader());
        scrolltable.setPreferredSize(new Dimension(800,600));
        scrolltable.setBorder(new BevelBorder(BevelBorder.LOWERED));
        JViewport viewport = new JViewport();
        viewport.setView(tableRowHeader);
        viewport.setPreferredSize(tableRowHeader.getPreferredSize());
        scrolltable.setRowHeaderView(viewport);
        scrolltable.setCorner(JScrollPane.UPPER_LEFT_CORNER, tableRowHeader.getTableHeader());
        
        
        JButton buttonClose = new JButton("Close Window");
        buttonClose.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                dialog.setVisible(false);
                dialog.dispose();
                dialog = null;
            }
        });
        
        JButton buttonExport = new JButton("Export");
        JPanel panelOptions = new JPanel();
        panelOptions.setLayout(new FlowLayout(FlowLayout.CENTER, GUIutilities.GAP_COMPONENT, GUIutilities.GAP_COMPONENT));
        panelOptions.add(buttonExport);
        panelOptions.add(buttonClose);
        
        
        this.setLayout(new BorderLayout(GUIutilities.GAP_COMPONENT,GUIutilities.GAP_COMPONENT));
        this.setBorder(new EmptyBorder(GUIutilities.GAP_OUTER_BORDER,GUIutilities.GAP_OUTER_BORDER,GUIutilities.GAP_OUTER_BORDER,GUIutilities.GAP_OUTER_BORDER));
        this.add(scrolltable, BorderLayout.CENTER);
        this.add(panelOptions, BorderLayout.SOUTH);
        
        dialog = new JDialog(parentFrame, "Procedure Analysis", true);
        Container contentPane = dialog.getContentPane();
        contentPane.setLayout(new BorderLayout(GUIutilities.GAP_COMPONENT,GUIutilities.GAP_COMPONENT));
        contentPane.add(this, BorderLayout.CENTER);
        
        dialog.setComponentOrientation(table.getComponentOrientation());
        dialog.pack();
        dialog.setLocationRelativeTo(parentFrame);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setVisible(true);
    }
    
    public void packRowHeader() {
        TableColumn column = tableRowHeader.getColumnModel().getColumn(0);
        int width = 0;
        int height = 0;
        String s;
        
        DefaultTableCellRenderer label = RowHeaderRenderer.getRenderer();
        
        for (int r=0; r<table.getRowCount(); r++) {
            s = (String)tableRowHeader.getValueAt(r, 0);
            label.setText(s);
            if(!s.startsWith(" "))
                label.setFont(label.getFont().deriveFont(Font.BOLD));
            else
                label.setFont(label.getFont().deriveFont(Font.PLAIN));
            width = Math.max(width, label.getPreferredSize().width);
            height = Math.max(height, label.getPreferredSize().height);
        }
        
        width += 10;
        
        // Set the width
        column.setPreferredWidth(width);
        column.setMaxWidth(width);
        column.setMinWidth(width);
        column.setWidth(width);

        tableRowHeader.setRowHeight(height);
        table.setRowHeight(height);
    }
    
    public void packColumn(int columnIndex) {
        TableColumn column = table.getColumnModel().getColumn(columnIndex);
        int width = 16; //icon size
        String s;
        
        JLabel label = new JLabel();
        DataModel model = (DataModel)table.getModel();
        s = model.getColumn(columnIndex).toString();
        if(!s.startsWith(ColumnHeaderEntry.ARGUMENT_PREFIX))
            label.setFont(label.getFont().deriveFont(Font.BOLD));
        label.setIcon(new RotatedTextIcon(RotatedTextIcon.LEFT, label.getFont(), s));
        width = Math.max(label.getPreferredSize().width, width);
        
        // Add margin
        width += 10;
        
        // Set the width
        column.setPreferredWidth(width);
        column.setMaxWidth(width);
        column.setMinWidth(width);
        column.setWidth(width);
    }
    
    private class SelectionListener implements ListSelectionListener {
        boolean	isFixedTable	= true;
        
        public SelectionListener(boolean isFixedTable) {
            this.isFixedTable = isFixedTable;
        }
        
        public void valueChanged(ListSelectionEvent e) {
            if (isFixedTable) {
                int fixedSelectedIndex = tableRowHeader.getSelectedRow();
                if(fixedSelectedIndex > table.getRowCount() || fixedSelectedIndex < 0)
                    return;
                else if(fixedSelectedIndex == 0)
                    table.clearSelection();
                else
                    table.setRowSelectionInterval(fixedSelectedIndex, fixedSelectedIndex);
            } else {
                int selectedIndex = table.getSelectedRow();
                
                if(selectedIndex > tableRowHeader.getRowCount() || selectedIndex < 0)
                    return;
                else if(selectedIndex == 0)
                    tableRowHeader.clearSelection();
                else
                    tableRowHeader.setRowSelectionInterval(selectedIndex, selectedIndex);
            }
        }
    }
    
    
}
