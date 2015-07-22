/*
 * TabulateComponent.java
 *
 * Created on 9 May 2008, 14:48
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tceav.gui.procedure.tabulate;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.io.*;
import tceav.Settings;
import tceav.utils.CustomFileFilter;
import tceav.manager.procedure.ProcedureManager;
import tceav.gui.*;
import tceav.resources.*;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
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
    private JCheckBox buttonShowActions;
    
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
                    
                    if(!Settings.isPmTblDatabaseMode())
                        exportToSpreadSheet(fc.getSelectedFile());
                    else
                        exportToDataBaseTables(fc.getSelectedFile());
                    
                }
            }
        });
        
        try {
            buttonExport.setIcon(ResourceLoader.getImage(ImageEnum.pmTabulateExport));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error Load Images", JOptionPane.ERROR_MESSAGE);
        }
        
        
        buttonExactMatch = new JCheckBox("Exact Argument Match", Settings.isPmTblStrictArgument());
        buttonExactMatch.setToolTipText("Matches exact order of delimited [;,] handler arguments and case");
        buttonExactMatch.setOpaque(false);
        buttonExactMatch.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateTable();
            }
        });
        
        buttonShowActions = new JCheckBox("Show Action Type", Settings.isPmTblShowActions());
        buttonShowActions.setToolTipText("Show action type or just a tick for each Workflow");
        buttonShowActions.setOpaque(false);
        buttonShowActions.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Settings.setPmTblShowActions(buttonShowActions.isSelected());
                updateTable();
            }
        });
        
        toolBar = new JToolBar();
        toolBar.add(buttonExport);
        toolBar.addSeparator();
        toolBar.add(buttonExactMatch);
        toolBar.addSeparator();
        toolBar.add(buttonShowActions);
        
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
    
    private boolean accessToFile(File file) {
        if(file.isDirectory()) {
            JOptionPane.showMessageDialog(parentFrame, file.getName()+" is a directory!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        if (file.exists()) {
            if(!file.canWrite()) {
                JOptionPane.showMessageDialog(parentFrame, "You do not have write access to "+file.getName(), "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            
            int option = JOptionPane.showConfirmDialog(parentFrame, file.getName()+" already exists, overwrite?", "Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if(option == JOptionPane.YES_OPTION)
                file.delete();
            else
                return false;
        }
        return true;
    }
    
    private final int TaskTable = 0;
    private final int HandlerTable = 1;
    private final int TaskHandlerMapTable = 2;
    
    private void exportToDataBaseTables(File file) {
        String s = file.getName();
        String tableName = "";
        File tableFile;
        int tableCount = 3;
        
        for(int i=0; i<tableCount; i++) {
            switch(i) {
                case TaskTable:
                    if(s.toLowerCase().endsWith(".xml"))
                        tableName = s.substring(0, s.length()-4)+".TaskTable.xml";
                    else
                        tableName = s + ".TaskTable.xml";
                    break;
                    
                case HandlerTable:
                    if(s.toLowerCase().endsWith(".xml"))
                        tableName = s.substring(0, s.length()-4)+".HandlerTable.xml";
                    else
                        tableName = s + ".HandlerTable.xml";
                    break;
                    
                case TaskHandlerMapTable:
                    if(s.toLowerCase().endsWith(".xml"))
                        tableName = s.substring(0, s.length()-4)+".TaskHandlerMapTable.xml";
                    else
                        tableName = s + ".TaskHandlerMapTable.xml";
                    break;
            }
            
            tableFile = new File(file.getParent(), tableName);
            if(!accessToFile(tableFile))
                return;
            
            exportToDataBaseTables(tableFile, i);
            
        }
        
    }
    
    private String padding = "000000000";
    
    private String StrFormat(String num) {
        return padding.substring(0,padding.length()-num.length()) + num;
    }
    
    private void attachXMLRow(Document dom, Element table, String name, String value) {
        Element rowName = dom.createElement(name);
        table.appendChild(rowName);
        
        Text rowValue = dom.createTextNode(value);
        rowName.appendChild(rowValue);
    }
    
    private void exportToDataBaseTables(File file, int tableMode) {
        RowOneModel rowHeader = (RowOneModel) tableRowOne.getModel();
        DataModel dataModel = (DataModel) table.getModel();
        
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document dom =  builder.newDocument();
            Element rootElement = dom.createElement("dataroot");
            rootElement.setAttribute("xmlns:od", "urn:schemas-microsoft-com:officedata");
            dom.appendChild(rootElement);
            Element table;
            
            DecimalFormat df = new DecimalFormat(padding);
            
            switch(tableMode) {
                case TaskTable:
                    
                    for(int row=0; row<dataModel.getRowCount(); row++) {
                        
                        table = dom.createElement("TaskTable");
                        rootElement.appendChild(table);
                        
                        attachXMLRow(dom, table, "TaskId", StrFormat(rowOneModel.getId(row)) + "_" + rowOneModel.getSiteId());
                        
                        if(!rowOneModel.getParentId(row).equals(""))
                            attachXMLRow(dom, table, "ParentId", StrFormat(rowOneModel.getParentId(row)) + "_" + rowOneModel.getSiteId());
                        
                        if(!rowOneModel.getRootId(row).equals(""))
                            attachXMLRow(dom, table, "RootId", StrFormat(rowOneModel.getRootId(row)) + "_" + rowOneModel.getSiteId());
                        
                        attachXMLRow(dom, table, "SiteId", rowOneModel.getSiteId());
                        
                        for(int col=0; col<rowOneModel.getColumnCount(); col++)
                            attachXMLRow(dom, table, rowHeader.getColumnName(col), rowHeader.getRawValueAt(row, col));
                    }
                    
                    break;
                    
                case HandlerTable:
                    
                    for(int col=0; col<dataModel.getColumnCount(); col++) {
                        table = dom.createElement("HandlerTable");
                        rootElement.appendChild(table);
                        
                        attachXMLRow(dom, table, "HandlerId", df.format(col) + "_" + rowOneModel.getSiteId());
                        attachXMLRow(dom, table, "SiteId", rowOneModel.getSiteId());
                        attachXMLRow(dom, table, "HandlerType", dataModel.getColumn(col).getClassification());
                        
                        if(dataModel.getColumn(col).isRuleClassicifaction())
                            attachXMLRow(dom, table, "RuleValue", dataModel.getColumn(col).getRule());
                        
                        attachXMLRow(dom, table, "HandlerName", dataModel.getColumn(col).getHandler());
                        
                        if(!dataModel.getColumn(col).getArgument().equals(""))
                            attachXMLRow(dom, table, "Argument", dataModel.getColumn(col).getArgument());
                        
                    }
                    
                    break;
                    
                case TaskHandlerMapTable:
                    int idCount = 0;
                    
                    for(int row=0; row<dataModel.getRowCount(); row++) {
                        for(int col=0; col<dataModel.getColumnCount(); col++) {
                            
                            if(dataModel.getValueAt(row, col).equals(""))
                                continue;
                            
                            idCount++;
                            table = dom.createElement("MapTable");
                            rootElement.appendChild(table);
                            
                            attachXMLRow(dom, table, "MapId", df.format(idCount) + "_" + rowOneModel.getSiteId());
                            attachXMLRow(dom, table, "SiteId", dataModel.getSiteId());
                            attachXMLRow(dom, table, "TaskId", StrFormat(rowOneModel.getId(row)) + "_"+rowOneModel.getSiteId());
                            attachXMLRow(dom, table, "HandlerId", df.format(col) + "_" + rowOneModel.getSiteId());
                            attachXMLRow(dom, table, "MapValue", (String)dataModel.getValueAt(row, col));
                        }
                    }
                    
                    break;
            }
            
            //FileOutputStream fos = new FileOutputStream(file);
            
            Transformer tr = TransformerFactory.newInstance().newTransformer();
            tr.setOutputProperty(OutputKeys.METHOD,"xml");
            tr.setOutputProperty(OutputKeys.INDENT, "yes");
            tr.setOutputProperty(OutputKeys.ENCODING,"UTF-8");
            tr.transform( new DOMSource(dom),new StreamResult(new FileOutputStream(file)));
            
        } catch(Exception e) {
            JOptionPane.showMessageDialog(parentFrame, e.getMessage(), "Save Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void exportToSpreadSheet(File file) {
        File sheetFile;
        int sheetCount;
        String s = file.getName();
        
        int colLimit = 256 - rowOneModel.getColumnCount();
        
        if(Settings.isPmTblMultiSheet())
            sheetCount = (int)Math.ceil((float)table.getColumnCount() / (float)colLimit);
        else
            sheetCount = 1;
        
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
            
            if(!accessToFile(sheetFile))
                return;
            
            if(i == sheetCount - 1)
                exportToSpreadSheet(sheetFile, i*colLimit, table.getColumnCount());
            else
                exportToSpreadSheet(sheetFile, i*colLimit, (i*colLimit)+colLimit);
        }
    }
    
    private void exportToSpreadSheet(File file, int startIndex, int endIndex) {
        RowOneModel rowHeader = (RowOneModel) tableRowOne.getModel();
        DataModel dataModel = (DataModel) table.getModel();
        
        try {
            FileOutputStream fos = new FileOutputStream(file);
            OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
            BufferedWriter bw = new BufferedWriter(osw);
            
            
            // Header
            String s = rowHeader.getCSVColumnName(0);
            for(int i=1; i<rowOneModel.getColumnCount(); i++)
                s = s + "," + rowHeader.getCSVColumnName(i);
            
            // Header
            for(int i=startIndex; i<endIndex; i++)
                s = s + "," + dataModel.getColumn(i).toExportString();
            
            bw.write(s+"\n");
            
            // Write Data
            for(int row=0; row<dataModel.getRowCount(); row++) {
                
                s = rowHeader.getCSVValueAt(row, 0);
                for(int col=1; col<rowOneModel.getColumnCount(); col++)
                    s = s + "," + rowHeader.getCSVValueAt(row, col);
                
                for(int col=startIndex; col<endIndex; col++)
                    s = s+","+dataModel.getCSVValueAt(row, col);
                
                bw.write(s+"\n");
            }
            
            bw.close();
        } catch(Exception e) {
            JOptionPane.showMessageDialog(parentFrame, e.getMessage(), "Save Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private JFileChooser fileChooser;
    private JCheckBox checkMultiSheet;
    private JCheckBox checkIncludeIndents;
    private CustomFileFilter filterCSV;
    private CustomFileFilter filterXML;
    
    private JFileChooser createFileChooser() {
        if(fileChooser != null)
            return fileChooser;
        
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
        JRadioButton radioDatabaseMode = new JRadioButton("Database", Settings.isPmTblDatabaseMode());
        radioDatabaseMode.setToolTipText("Export to XML table files");
        radioDatabaseMode.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Settings.setPmTblDatabaseMode(true);
                if(fileChooser.getFileFilter().equals(filterCSV))
                    fileChooser.setFileFilter(filterXML);
            }
        });
        JRadioButton radioSpreadSheetMode = new JRadioButton("Spreadsheet", Settings.isPmTblDatabaseMode());
        radioSpreadSheetMode.setToolTipText("Export to CSV spreadsheet file(s)");
        radioSpreadSheetMode.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Settings.setPmTblDatabaseMode(false);
                if(fileChooser.getFileFilter().equals(filterXML))
                    fileChooser.setFileFilter(filterCSV);
            }
        });
        
        ButtonGroup group = new ButtonGroup();
        group.add(radioDatabaseMode);
        group.add(radioSpreadSheetMode);
        radioSpreadSheetMode.setSelected(!Settings.isPmTblDatabaseMode());
        radioDatabaseMode.setSelected(Settings.isPmTblDatabaseMode());
        
        JPanel panelSpreadsheetOptions = new JPanel();
        panelSpreadsheetOptions.setBorder(new TitledBorder(new EtchedBorder(), "Spreadsheet Options"));
        panelSpreadsheetOptions.setLayout(new GridLayout(2,1));
        panelSpreadsheetOptions.add(checkMultiSheet);
        panelSpreadsheetOptions.add(checkIncludeIndents);
        JPanel panelExportMode = new JPanel();
        panelExportMode.setBorder(new TitledBorder(new EtchedBorder(), "Export Mode"));
        panelExportMode.setLayout(new GridLayout(2,1));
        panelExportMode.add(radioDatabaseMode);
        panelExportMode.add(radioSpreadSheetMode);
        
        JPanel panelOptions = new JPanel();
        panelOptions.setLayout(new GridLayout(2,1));
        panelOptions.add(panelSpreadsheetOptions);
        panelOptions.add(panelExportMode);
        
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(panelOptions, BorderLayout.NORTH);
        
        filterCSV = new CustomFileFilter(
                new String[]{"csv"},
                "Comma Delimited Text File (*.csv)");
        filterXML = new CustomFileFilter(
                new String[]{"xml"},
                "Database Text File (*.xml)");
        
        fileChooser = new JFileChooser();
        fileChooser.setAccessory(panel);
        fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setCurrentDirectory(pm.getFile());
        fileChooser.addChoosableFileFilter(new CustomFileFilter(
                new String[]{"csv","xml"},
                "All supported Text Files (*.csv, *.xml)"));
        fileChooser.addChoosableFileFilter(filterCSV);
        fileChooser.addChoosableFileFilter(filterXML);
        
        if(Settings.isPmTblDatabaseMode())
            fileChooser.setFileFilter(filterXML);
        else
            fileChooser.setFileFilter(filterCSV);
        
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
