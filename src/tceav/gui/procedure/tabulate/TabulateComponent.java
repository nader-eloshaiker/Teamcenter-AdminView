/*
 * TabulateComponent.java
 *
 * Created on 9 May 2008, 14:48
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tceav.gui.procedure.tabulate;

import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import javax.swing.border.*;
import java.awt.*;
import tceav.Settings;
import tceav.utils.CustomFileFilter;
import tceav.manager.procedure.ProcedureManager;
import java.io.*;
import tceav.gui.*;
import tceav.resources.*;

/**
 *
 * @author nzr4dl
 */
public class TabulateComponent extends TabbedPanel {
    
    private JDialog dialog;
    private JTableAdvanced table;
    private JTableAdvanced tableRowOne;
    private String title;
    private ProcedureManager pm;
    private JFrame parentFrame;
    private RowOneModel rowOneModel;
    
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
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                tableRowOne.repaint();
            }
        });
        
        rowOneModel = data.createRowHeaderModel();
        tableRowOne = new JTableAdvanced(rowOneModel);
        tableRowOne.getTableHeader().setReorderingAllowed(false);
        tableRowOne.getTableHeader().setResizingAllowed(false);
        tableRowOne.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tableRowOne.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                table.repaint();
            }
        });
        
        formatTable();
        
        JPanel panelTable = new JPanel();
        panelTable.setLayout(new BorderLayout());
        panelTable.add(table.getTableHeader(), BorderLayout.NORTH);
        panelTable.add(table, BorderLayout.CENTER);
        
        JScrollPane scrolltable = new JScrollPane(table);
        //scrolltable.setColumnHeaderView(table.getTableHeader());
        scrolltable.setPreferredSize(new Dimension(800,600));
        scrolltable.setBorder(new BevelBorder(BevelBorder.LOWERED));
        JViewport viewport = new JViewport();
        viewport.setView(tableRowOne);
        viewport.setPreferredSize(tableRowOne.getPreferredSize());
        scrolltable.setRowHeaderView(viewport);
        scrolltable.setCorner(JScrollPane.UPPER_LEFT_CORNER, tableRowOne.getTableHeader());
        
        
        this.setLayout(new BorderLayout(GUIutilities.GAP_COMPONENT,GUIutilities.GAP_COMPONENT));
        this.setBorder(new EmptyBorder(GUIutilities.GAP_OUTER_BORDER,GUIutilities.GAP_OUTER_BORDER,GUIutilities.GAP_OUTER_BORDER,GUIutilities.GAP_OUTER_BORDER));
        this.add(scrolltable, BorderLayout.CENTER);
        
    }
    
    public void updateUI() {
        super.updateUI();
        if(table != null && tableRowOne != null)
            formatTable();
    }
    
    private void updateTable() {
        Settings.setPmTblStrictArgument(buttonExactMatch.isSelected());
        table.setModel(new DataModel(pm));
        formatTable();
        textHandler = new JLabel(" "+table.getColumnCount()+" ");
    }
    
    private void formatTable() {
        TableColumn column;
        CellRenderer renderer = new CellRenderer(tableRowOne);
        ColumnHeaderRenderer rendererCol = new ColumnHeaderRenderer();
        RowOneHeaderRenderer rendererRowCol = new RowOneHeaderRenderer();
        RowOneRenderer rendererRow = new RowOneRenderer(table);
        
        for (int i=0; i<table.getColumnCount(); i++) {
            column = table.getColumnModel().getColumn(i);
            column.setHeaderRenderer(rendererCol);
            column.setCellRenderer(renderer);
            packColumn(i);
        }
        for (int i=0; i<tableRowOne.getColumnCount(); i++) {
            column = tableRowOne.getColumnModel().getColumn(i);
            column.setHeaderRenderer(rendererRowCol);
            column.setCellRenderer(rendererRow);
            packRowHeader(i);
        }
    }
    
    private JPanel statusBar;
    private JLabel textHandler;
    
    public JComponent getStatusBar() {
        if(statusBar != null)
            return statusBar;
        
        JLabel textFileLabel = new JLabel(" Path:");
        JLabel textFile = new JLabel(" "+pm.getFile().getParent()+" ");
        textFile.setBorder(new BevelBorder(BevelBorder.LOWERED));
        JPanel panelFile = new JPanel();
        panelFile.setLayout(new BorderLayout());
        panelFile.add(textFileLabel, BorderLayout.WEST);
        panelFile.add(textFile, BorderLayout.CENTER);
        
        JLabel textHandlerLabel = new JLabel("  Columns [Handlers]:");
        textHandler = new JLabel(" "+table.getColumnCount()+" ");
        textHandler.setBorder(new BevelBorder(BevelBorder.LOWERED));
        JPanel panelHandler = new JPanel();
        panelHandler.setLayout(new BorderLayout());
        panelHandler.add(textHandlerLabel, BorderLayout.WEST);
        panelHandler.add(textHandler, BorderLayout.CENTER);
        
        JLabel textTaskLabel = new JLabel("  Rows [Tasks]:");
        JLabel textTask = new JLabel(" "+table.getRowCount()+" ");
        textTask.setBorder(new BevelBorder(BevelBorder.LOWERED));
        JPanel panelTask = new JPanel();
        panelTask.setLayout(new BorderLayout());
        panelTask.add(textTaskLabel, BorderLayout.WEST);
        panelTask.add(textTask, BorderLayout.CENTER);
        
        JLabel textWorkflowLabel = new JLabel("  Workflow Procedures:");
        JLabel textWorkflow = new JLabel(" "+pm.getWorkflowProcesses().size()+" ");
        textWorkflow.setBorder(new BevelBorder(BevelBorder.LOWERED));
        JPanel panelWorkflow = new JPanel();
        panelWorkflow.setLayout(new BorderLayout());
        panelWorkflow.add(textWorkflowLabel, BorderLayout.WEST);
        panelWorkflow.add(textWorkflow, BorderLayout.CENTER);
        
        JPanel panelTaskHandler = new JPanel();
        panelTaskHandler.setLayout(new FlowLayout(FlowLayout.LEFT,GUIutilities.GAP_COMPONENT,0));
        panelTaskHandler.add(panelTask);
        panelTaskHandler.add(panelHandler);
        panelTaskHandler.add(panelWorkflow);
        
        JLabel textDateLabel = new JLabel("  Date:");
        JLabel textDate = new JLabel(" "+pm.getPLMXML().getDate()+" ");
        textDate.setBorder(new BevelBorder(BevelBorder.LOWERED));
        JPanel panelDate = new JPanel();
        panelDate.setLayout(new BorderLayout());
        panelDate.add(textDateLabel, BorderLayout.WEST);
        panelDate.add(textDate, BorderLayout.CENTER);
        
        JLabel textTimeLabel = new JLabel("  Time:");
        JLabel textTime = new JLabel(" "+pm.getPLMXML().getTime()+" ");
        textTime.setBorder(new BevelBorder(BevelBorder.LOWERED));
        JPanel panelTime = new JPanel();
        panelTime.setLayout(new BorderLayout());
        panelTime.add(textTimeLabel, BorderLayout.WEST);
        panelTime.add(textTime, BorderLayout.CENTER);
        
        JPanel panelDateTime = new JPanel();
        panelDateTime.setLayout(new BorderLayout());
        panelDateTime.add(panelDate, BorderLayout.WEST);
        panelDateTime.add(panelTime, BorderLayout.EAST);
        
        
        statusBar = new JPanel();
        statusBar.setLayout(new BorderLayout());
        statusBar.add(panelFile, BorderLayout.WEST);
        statusBar.add(panelTaskHandler, BorderLayout.CENTER);
        statusBar.add(panelDateTime, BorderLayout.EAST);
        
        return statusBar;
    }
    
    private JToolBar toolBar;
    private JCheckBox buttonExactMatch;
    
    public JComponent getToolBar() {
        if(toolBar != null)
            return toolBar;
        
        JButton buttonExport = new JButton("Export");
        buttonExport.setToolTipText("Export to a comma delimited csv file");
        buttonExport.setOpaque(false);
        buttonExport.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                JFileChooser fc = createFileChooser();
                int result = fc.showSaveDialog(parentFrame);
                if(result == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    File sheetFile;
<<<<<<< HEAD
                    int colLimit = 255;
                    int sheetCount = (int)Math.ceil((float)table.getColumnCount() / (float)colLimit);
=======
                    int sheetCount;
>>>>>>> release/3.5
                    String s = file.getName();

                    int colLimit = 256 - rowOneModel.getColumnCount();
                    if(Settings.isPmTblIncludeIds())
                        colLimit = colLimit - 2;
                    
                    if(sheetCount > 1)
                        if(s.toLowerCase().contains(".sheet"))
                            s = s.substring(0, s.toLowerCase().indexOf(".sheet"));
                    
                    for(int i=0; i<sheetCount; i++) {
                        
                        if(s.toLowerCase().endsWith(".csv")) {
                            if(sheetCount > 1)
                                sheetFile = new File(file.getParent(), s.substring(0, s.length()-4)+".sheet"+(i+1)+".csv");
                            else
                                sheetFile = new File(file.getParent(), s.substring(0, s.length()-4)+".csv");
                        } else {
                            if(sheetCount > 1)
                                sheetFile = new File(file.getParent(), s + ".sheet" + (i+1) + ".csv");
                            else
                                sheetFile = new File(file.getParent(), s + ".csv");
                        }
                        
                        if(sheetFile.isDirectory()) {
                            JOptionPane.showMessageDialog(parentFrame, sheetFile.getName()+" is a directory!", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        
                        if (sheetFile.exists()) {
                            if(!sheetFile.canWrite()) {
                                JOptionPane.showMessageDialog(parentFrame, "You do not have write access to "+sheetFile.getName(), "Error", JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                            
                            int option = JOptionPane.showConfirmDialog(parentFrame, sheetFile.getName()+" already exists, overwrite?", "Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                            if(option == JOptionPane.YES_OPTION)
                                sheetFile.delete();
                            else
                                return;
                        }
                        
                        if(i == sheetCount - 1)
                            export(sheetFile, i*colLimit, table.getColumnCount());
                        else
                            export(sheetFile, i*colLimit, (i*colLimit)+colLimit);
                    }
                }
            }
        });
        
        try {
            buttonExport.setIcon(ResourceLoader.getImage(ImageEnum.pmTabulateExport));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error Load Images", JOptionPane.ERROR_MESSAGE);
        }
        
        
        buttonExactMatch = new JCheckBox("Exact Argument Match", Settings.isPmTblStrictArgument());
        buttonExactMatch.setToolTipText("Matches exact order of delimited [;,] handler arguments");
        buttonExactMatch.setOpaque(false);
        buttonExactMatch.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateTable();
            }
        });
        
        toolBar = new JToolBar();
        toolBar.add(buttonExport);
        toolBar.add(buttonExactMatch);
        
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
    
    private void export(File file, int startIndex, int endIndex) {
        RowOneModel rowHeader = (RowOneModel) tableRowOne.getModel();
        DataModel dataModel = (DataModel) table.getModel();
        
        try {
            FileOutputStream fos = new FileOutputStream(file);
            OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
            BufferedWriter bw = new BufferedWriter(osw);
            
            String s = "";
            
            // Header
            if(Settings.isPmTblIncludeIds())
                s = "\"Id\",\"Parent Id\",";
            s = s + rowHeader.getColumnExportName(0);

            // Header
            for(int i=1; i<rowOneModel.getColumnCount(); i++)
                s = s + "," + rowHeader.getColumnExportName(i);
            
            // Header
            for(int i=startIndex; i<endIndex; i++)
                s = s + "," + dataModel.getColumn(i).toExportString();
            
            bw.write(s+"\n");
            
            // Data
            for(int row=0; row<dataModel.getRowCount(); row++) {
                s = "";
                
                if(Settings.isPmTblIncludeIds())
                    s = "\""+rowOneModel.getId(row)+"\",\""+rowOneModel.getParentId(row)+"\",";
                s = s + rowHeader.getExportValueAt(row, 0);
                
                for(int col=1; col<rowOneModel.getColumnCount(); col++)
                    s = s + "," + rowHeader.getExportValueAt(row, col);
                
                for(int col=startIndex; col<endIndex; col++)
                    s = s+","+dataModel.getValueAt(row, col);
                
                bw.write(s+"\n");
            }
            
            bw.close();
        } catch(Exception e) {
            JOptionPane.showMessageDialog(parentFrame, e.getMessage(), "Save Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
<<<<<<< HEAD
    JFileChooser fileChooser;
=======
    private JFileChooser fileChooser;
    private JCheckBox checkMultiSheet;
    private JCheckBox checkIncludeIndents;
    private JCheckBox checkIncludeIds;
>>>>>>> release/3.5
    
    private JFileChooser createFileChooser() {
        if(fileChooser != null)
            return fileChooser;
        
<<<<<<< HEAD
=======
        checkMultiSheet = new JCheckBox("Use Multiple Sheets", Settings.isPmTblMultiSheet());
        checkMultiSheet.setToolTipText("Split file into multiple sheets of 256 columns");
        checkMultiSheet.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Settings.setPmTblMultiSheet(checkMultiSheet.isSelected());
            }
        });
        checkIncludeIndents = new JCheckBox("Include Indentation", Settings.isPmTblIncludeIndents());
        checkIncludeIndents.setToolTipText("Include the indentation to indicate hirarchy");
        checkIncludeIndents.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Settings.setPmTblIncludeIndents(checkIncludeIndents.isSelected());
            }
        });
        checkIncludeIds = new JCheckBox("Include Id", Settings.isPmTblIncludeIds());
        checkIncludeIds.setToolTipText("Include the id of the procedure and it's parent");
        checkIncludeIds.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Settings.setPmTblIncludeIds(checkIncludeIds.isSelected());
            }
        });
        
        JPanel panelFile = new JPanel();
        panelFile.setBorder(new TitledBorder(new EtchedBorder(), "File Options"));
        panelFile.setLayout(new GridLayout(2,1));
        panelFile.add(checkMultiSheet);
        JPanel panelFormat = new JPanel();
        panelFormat.setBorder(new TitledBorder(new EtchedBorder(), "Format Options"));
        panelFormat.setLayout(new GridLayout(2,1));
        panelFormat.add(checkIncludeIndents);
        JPanel panelData = new JPanel();
        panelData.setBorder(new TitledBorder(new EtchedBorder(), "Data Options"));
        panelData.setLayout(new GridLayout(2,1));
        panelData.add(checkIncludeIds);
        
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3,1));
        panel.add(panelFile);
        panel.add(panelFormat);
        panel.add(panelData);
        
>>>>>>> release/3.5
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setCurrentDirectory(pm.getFile());
        fileChooser.addChoosableFileFilter(new CustomFileFilter(
                new String[]{"csv"},
                "Comma Delimited Text File (*.csv)"));
        
        return fileChooser;
    }
    
    
    private void packRowHeader(int colIndex) {
        TableColumn column = tableRowOne.getColumnModel().getColumn(colIndex);
        int width = 0;
        String s;
        int insetVal = 2;
        
        //DefaultTableCellRenderer label = RowOneRenderer.getRenderer();
        JLabel label = new JLabel();
        
        for (int r=0; r<tableRowOne.getRowCount(); r++) {
            s = (String)tableRowOne.getValueAt(r, colIndex);
            label.setText(s);
            if(rowOneModel.isRootNode(r))
                label.setFont(label.getFont().deriveFont(Font.BOLD));
            else
                label.setFont(label.getFont().deriveFont(Font.PLAIN));
            width = Math.max(width, label.getPreferredSize().width);
        }
        
        width += 10;
        
        // Set the width
        column.setPreferredWidth(width);
        column.setMaxWidth(width);
        column.setMinWidth(width);
        column.setWidth(width);
        
        //tableRowOne.setRowHeight(height);
        //table.setRowHeight(height);
    }
    
    private void packColumn(int columnIndex) {
        TableColumn column = table.getColumnModel().getColumn(columnIndex);
        int width = 16; //icon size
        String s;
        
        JLabel label = new JLabel();
        DataModel model = (DataModel)table.getModel();
        s = model.getColumn(columnIndex).toString();
        //if(!s.startsWith(ColumnHeaderEntry.ARGUMENT_PREFIX))
        //    label.setFont(label.getFont().deriveFont(Font.BOLD));
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
    
}
