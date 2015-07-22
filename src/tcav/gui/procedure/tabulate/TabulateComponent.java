/*
 * TabulateComponent.java
 *
 * Created on 9 May 2008, 14:48
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tcav.gui.procedure.tabulate;

import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.*;
import javax.swing.border.*;
import java.awt.*;
import tcav.gui.*;
import tcav.utils.CustomFileFilter;
import tcav.resources.*;
import tcav.manager.procedure.ProcedureManager;
import java.io.*;

/**
 *
 * @author nzr4dl
 */
public class TabulateComponent extends TabbedPanel {
    
    private JDialog dialog;
    private JTableAdvanced table;
    private JTableAdvanced tableRowHeader;
    private String title;
    private ProcedureManager pm;
    private JFrame parentFrame;
    
    /**
     * Creates a new instance of TabulateComponent
     */
    public TabulateComponent(ProcedureManager pm, JFrame parentFrame) {
        super();
        this.pm = pm;
        this.parentFrame = parentFrame;
        title = pm.getFile().getName()+" Table";
        
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
        
        
        this.setLayout(new BorderLayout(GUIutilities.GAP_COMPONENT,GUIutilities.GAP_COMPONENT));
        this.setBorder(new EmptyBorder(GUIutilities.GAP_OUTER_BORDER,GUIutilities.GAP_OUTER_BORDER,GUIutilities.GAP_OUTER_BORDER,GUIutilities.GAP_OUTER_BORDER));
        this.add(scrolltable, BorderLayout.CENTER);
        
    }
    
    public JComponent getStatusBar() {
        return new JPanel();
    }
    
    private JToolBar toolBar;
    
    public JComponent getToolBar() {
        if(toolBar != null)
            return toolBar;
        
        JButton buttonExport = new JButton("Export");
        buttonExport.setOpaque(false);
        buttonExport.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                JFileChooser fc = createFileChooser();
                int result = fc.showSaveDialog(parentFrame);
                if(result == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    
                    if(file.isDirectory()) {
                        JOptionPane.showMessageDialog(parentFrame, "You have selected a directory!", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    if(file.getName().equals("")) {
                        JOptionPane.showMessageDialog(parentFrame, "No file name specified!", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    
                    if (file.exists()) {
                        if(!file.canWrite()) {
                            JOptionPane.showMessageDialog(parentFrame, "You do not have write access!", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        
                        int option = JOptionPane.showConfirmDialog(parentFrame, "File already exists, overwrite?", "Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                        if(option == JOptionPane.YES_OPTION) {
                            file.delete();
                            export(file);
                        }
                    } else
                        export(file);
                }
            }
        });
        
        toolBar = new JToolBar();
        toolBar.add(buttonExport);
        
        return toolBar;
    }
    
    public String getTitle() {
        return title;
    }
    
    private ImageIcon iconTabulate;
    
    public ImageIcon getIcon() {
        if(iconTabulate == null) {
            try {
                iconTabulate = ResourceLoader.getImage(ImageEnum.pmTabulate);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(parentFrame, ex.getMessage(), "Error Load Images", JOptionPane.ERROR_MESSAGE);
            }
        }
        return iconTabulate;
    }
    
    private void export(File file) {
        RowHeaderModel rowHeader = (RowHeaderModel) tableRowHeader.getModel();
        DataModel dataModel = (DataModel) table.getModel();
        
        try {
            FileOutputStream fos = new FileOutputStream(file);
            OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
            BufferedWriter bw = new BufferedWriter(osw);
            
            String s;
            
            //Header
            s = rowHeader.getColumnExportName(0);
            for(int i=0; i<dataModel.getColumnCount(); i++)
                s = s+","+dataModel.getColumn(i).exportString();
            
            bw.write(s+"\n");
            
            for(int row=0; row<dataModel.getRowCount(); row++) {
                s = rowHeader.getExportValueAt(row, 0);
                for(int col=0; col<dataModel.getColumnCount(); col++) {
                    s = s+","+dataModel.getValueAt(row, col);
                }
                bw.write(s+"\n");
            }
            
            bw.close();
        } catch(Exception e) {
            JOptionPane.showMessageDialog(parentFrame, e.getMessage(), "Save Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    JFileChooser fileChooser;
    
    private JFileChooser createFileChooser() {
        if(fileChooser != null)
            return fileChooser;
        
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setCurrentDirectory(pm.getFile());
        fileChooser.addChoosableFileFilter(new CustomFileFilter(
                new String[]{"csv","txt"},"Delimited File (*.csv; *.txt)"));
        
        return fileChooser;
    }
    
    
    private void packRowHeader() {
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
    
    private void packColumn(int columnIndex) {
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
                table.setRowSelectionInterval(fixedSelectedIndex, fixedSelectedIndex);
                
            } else {
                int selectedIndex = table.getSelectedRow();
                tableRowHeader.setRowSelectionInterval(selectedIndex, selectedIndex);
            }
        }
    }
    
    
}
