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
    private JTableAdvanced table;
    
    /** Creates a new instance of AnalysisComponent */
    public AnalysisComponent(ProcedureManager pm, JFrame parentFrame) {
        super();
        table = new JTableAdvanced(new DataModel(pm));
        table.getTableHeader().setReorderingAllowed(false);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        TableColumn column;
        CellRenderer renderer = new CellRenderer();
        
        for (int i=0; i<table.getColumnCount(); i++){
            column = table.getColumnModel().getColumn(i);
            column.setHeaderRenderer(new ColumnHeaderRenderer());
            column.setCellRenderer(renderer);
            packColumn(i);
        }
        
        
        JPanel panelTable = new JPanel();
        panelTable.setLayout(new BorderLayout());
        panelTable.add(table.getTableHeader(), BorderLayout.NORTH);
        panelTable.add(table, BorderLayout.CENTER);
        
        JScrollPane scrolltable = new JScrollPane(table);
        scrolltable.setPreferredSize(new Dimension(800,600));
        scrolltable.setBorder(new BevelBorder(BevelBorder.LOWERED));
        //scrolltable.getViewport().add(table);
        
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(GUIutilities.GAP_COMPONENT,GUIutilities.GAP_COMPONENT));
        panel.setBorder(new EmptyBorder(GUIutilities.GAP_OUTER_BORDER,GUIutilities.GAP_OUTER_BORDER,GUIutilities.GAP_OUTER_BORDER,GUIutilities.GAP_OUTER_BORDER));
        panel.add(scrolltable, BorderLayout.CENTER);
        
        JDialog dialog = new JDialog(parentFrame, "Procedure Analysis", true);
        Container contentPane = dialog.getContentPane();
        contentPane.setLayout(new BorderLayout(GUIutilities.GAP_COMPONENT,GUIutilities.GAP_COMPONENT));
        contentPane.add(panel, BorderLayout.CENTER);
        
        dialog.setComponentOrientation(table.getComponentOrientation());
        //dialog.setResizable(false);
        dialog.pack();
        dialog.setLocationRelativeTo(parentFrame);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setVisible(true);
        
        
    }
    
    public void packColumn(int columnIndex) {
        TableColumn column = table.getColumnModel().getColumn(columnIndex);
        int width = 0;
        String s;
        
        JLabel label = new JLabel();
        if(columnIndex == 0) {
            for (int r=0; r<table.getRowCount(); r++) {
                s = table.getValueAt(r, columnIndex).toString();
                label.setText(s);
                if(!s.startsWith(" "))
                    label.setFont(label.getFont().deriveFont(Font.BOLD));
                else
                    label.setFont(label.getFont().deriveFont(Font.PLAIN));
                width = Math.max(width, label.getPreferredSize().width);
            }
        } else {
            DataModel model = (DataModel)table.getModel();
            s = model.getColumn(columnIndex).toString();
            if(!s.startsWith(ColumnHeaderEntry.ARGUEMENT))
                label.setFont(label.getFont().deriveFont(Font.BOLD));
            label.setIcon(new RotatedTextIcon(RotatedTextIcon.LEFT, label.getFont(), s));
            width = Math.max(label.getPreferredSize().width, 16);
        }
        // Add margin
        width += 10;
        
        // Set the width
        column.setResizable(false);
        column.setPreferredWidth(width);
        column.setMaxWidth(width);
        column.setMinWidth(width);
        column.setWidth(width);
        
    }
    
}
