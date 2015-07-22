/*
 * TabulateComponent.java
 *
 * Created on 9 May 2008, 14:48
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package tceav.gui.procedure.tabulate;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Calendar;
import java.text.DecimalFormat;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.JViewport;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumn;
import tceav.Settings;
import tceav.gui.tools.CustomFileFilter;
import tceav.gui.tools.GUIutilities;
import tceav.gui.tools.RotatedTextIcon;
import tceav.gui.tools.table.JTableAdvanced;
import tceav.manager.procedure.ProcedureManager;
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
import tceav.gui.AdminViewFrame;
import tceav.gui.TabbedPanel;
import tceav.resources.ImageEnum;
import tceav.resources.ResourceLoader;

/**
 *
 * @author nzr4dl
 */
public class TabulateComponent extends TabbedPanel {

    private JDialog dialog;
    private JTableAdvanced table;
    private JTableAdvanced tableFreezeRow;
    private String title;
    private ProcedureManager pm;
    private AdminViewFrame parentFrame;
    private FreezeRowTableData modelFreezeRow;

    /**
     * Creates a new instance of TabulateComponent
     */
    public TabulateComponent(ProcedureManager pm, AdminViewFrame parentFrame) {
        super();
        this.pm = pm;
        this.parentFrame = parentFrame;
        title = pm.getFile().getName() + " Table";

        TableData data = new TableData(pm);
        table = new JTableAdvanced(data);
        modelFreezeRow = data.createRowHeaderModel();
        tableFreezeRow = new JTableAdvanced(modelFreezeRow);


        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setResizingAllowed(false);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                tableFreezeRow.repaint();
            }
        });
        tableFreezeRow = new JTableAdvanced(modelFreezeRow);
        tableFreezeRow.getTableHeader().setReorderingAllowed(false);
        tableFreezeRow.getTableHeader().setResizingAllowed(false);
        tableFreezeRow.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tableFreezeRow.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

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
        scrolltable.setPreferredSize(new Dimension(800, 600));
        scrolltable.setBorder(new BevelBorder(BevelBorder.LOWERED));
        JViewport viewport = new JViewport();
        viewport.setView(tableFreezeRow);
        viewport.setPreferredSize(tableFreezeRow.getPreferredSize());
        scrolltable.setRowHeaderView(viewport);
        scrolltable.setCorner(JScrollPane.UPPER_LEFT_CORNER, tableFreezeRow.getTableHeader());


        this.setLayout(new BorderLayout(GUIutilities.GAP_COMPONENT, GUIutilities.GAP_COMPONENT));
        this.setBorder(new EmptyBorder(GUIutilities.GAP_OUTER_BORDER, GUIutilities.GAP_OUTER_BORDER, GUIutilities.GAP_OUTER_BORDER, GUIutilities.GAP_OUTER_BORDER));
        this.add(scrolltable, BorderLayout.CENTER);

    }

    /*
    class RowSelector implements ListSelectionListener {
    
    JTable primaryTable;
    JTable secondayTable;
    
    public RowSelector(JTable primaryTable, JTable secondayTable) {
    this.primaryTable = primaryTable;
    this.secondayTable = secondayTable;
    }
    
    public void valueChanged(ListSelectionEvent e) {
    if (secondayTable.getColumnSelectionAllowed()) {
    secondayTable.clearSelection();
    secondayTable.setColumnSelectionAllowed(false);
    secondayTable.setRowSelectionAllowed(true);
    }
    if (primaryTable.getColumnSelectionAllowed()) {
    primaryTable.clearSelection();
    primaryTable.setColumnSelectionAllowed(false);
    primaryTable.setRowSelectionAllowed(true);
    }
    }
    }
    
    class HeaderColumnSelector extends MouseAdapter {
    
    JTable primaryTable;
    JTable secondayTable;
    
    public HeaderColumnSelector(JTable primaryTable, JTable secondayTable) {
    this.primaryTable = primaryTable;
    this.secondayTable = secondayTable;
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
    if (!secondayTable.getColumnSelectionAllowed()) {
    secondayTable.clearSelection();
    secondayTable.setColumnSelectionAllowed(true);
    secondayTable.setRowSelectionAllowed(false);
    }
    if (!primaryTable.getColumnSelectionAllowed()) {
    primaryTable.setColumnSelectionAllowed(true);
    primaryTable.setRowSelectionAllowed(false);
    }
    TableColumnModel colModel = table.getColumnModel();
    
    // The index of the column whose header was clicked
    int vColIndex = colModel.getColumnIndexAtX(e.getX());
    
    // Return if not clicked on any column header
    if (vColIndex == -1) {
    return;
    }
    
    // Determine if mouse was clicked between column heads
    Rectangle headerRect = table.getTableHeader().getHeaderRect(vColIndex);
    if (vColIndex == 0) {
    headerRect.width -= 3;    // Hard-coded constant
    
    } else {
    headerRect.grow(-3, 0);   // Hard-coded constant
    
    }
    if (headerRect.contains(e.getX(), e.getY())) {
    // Mouse was clicked on column heads
    // vColIndex is the column head closest to the click
    
    // vLeftColIndex is the column head to the left of the click
    int vLeftColIndex = vColIndex;
    if (e.getX() < headerRect.x) {
    vLeftColIndex--;
    }
    primaryTable.setColumnSelectionInterval(vLeftColIndex, vLeftColIndex);
    }
    }
    }
     */
    @Override
    public void updateUI() {
        super.updateUI();
        if (table != null && tableFreezeRow != null) {
            formatTable();
        }
    }

    private void updateTable() {
        Settings.setPmTblStrictArgument(buttonExactMatch.isSelected());
        table.setModel(new TableData(pm));
        formatTable();
        textHandler.setText(" " + table.getColumnCount() + " ");
    }

    private void formatTable() {
        TableColumn column;
        CellRenderer renderer = new CellRenderer(tableFreezeRow);
        ColumnHeaderRenderer rendererCol = new ColumnHeaderRenderer();
        FreezeRowHeaderRenderer rendererRowCol = new FreezeRowHeaderRenderer();
        FreezeRowRenderer rendererRow = new FreezeRowRenderer(table);

        for (int i = 0; i < table.getColumnCount(); i++) {
            column = table.getColumnModel().getColumn(i);
            column.setHeaderRenderer(rendererCol);
            column.setCellRenderer(renderer);
            packColumn(i);
        }
        for (int i = 0; i < tableFreezeRow.getColumnCount(); i++) {
            column = tableFreezeRow.getColumnModel().getColumn(i);
            column.setHeaderRenderer(rendererRowCol);
            column.setCellRenderer(rendererRow);
            packRowHeader(i);
        }
    }
    private JPanel statusBar;
    private JLabel textHandler;

    public JComponent getStatusBar() {
        if (statusBar != null) {
            return statusBar;
        }
        JLabel textFileLabel = new JLabel(" Path:");
        JLabel textFile = new JLabel(" " + pm.getFile().getParent() + " ");
        textFile.setBorder(new BevelBorder(BevelBorder.LOWERED));
        JPanel panelFile = new JPanel();
        panelFile.setLayout(new BorderLayout());
        panelFile.add(textFileLabel, BorderLayout.WEST);
        panelFile.add(textFile, BorderLayout.CENTER);

        JLabel textHandlerLabel = new JLabel("  Columns [Handlers]:");
        textHandler = new JLabel(" " + table.getColumnCount() + " ");
        textHandler.setBorder(new BevelBorder(BevelBorder.LOWERED));
        JPanel panelHandler = new JPanel();
        panelHandler.setLayout(new BorderLayout());
        panelHandler.add(textHandlerLabel, BorderLayout.WEST);
        panelHandler.add(textHandler, BorderLayout.CENTER);

        JLabel textTaskLabel = new JLabel("  Rows [Tasks]:");
        JLabel textTask = new JLabel(" " + table.getRowCount() + " ");
        textTask.setBorder(new BevelBorder(BevelBorder.LOWERED));
        JPanel panelTask = new JPanel();
        panelTask.setLayout(new BorderLayout());
        panelTask.add(textTaskLabel, BorderLayout.WEST);
        panelTask.add(textTask, BorderLayout.CENTER);

        JLabel textWorkflowLabel = new JLabel("  Workflow Procedures:");
        JLabel textWorkflow = new JLabel(" " + pm.getWorkflowProcesses().size() + " ");
        textWorkflow.setBorder(new BevelBorder(BevelBorder.LOWERED));
        JPanel panelWorkflow = new JPanel();
        panelWorkflow.setLayout(new BorderLayout());
        panelWorkflow.add(textWorkflowLabel, BorderLayout.WEST);
        panelWorkflow.add(textWorkflow, BorderLayout.CENTER);

        JPanel panelTaskHandler = new JPanel();
        panelTaskHandler.setLayout(new FlowLayout(FlowLayout.LEFT, GUIutilities.GAP_COMPONENT, 0));
        panelTaskHandler.add(panelTask);
        panelTaskHandler.add(panelHandler);
        panelTaskHandler.add(panelWorkflow);

        JLabel textDateLabel = new JLabel("  Date:");
        JLabel textDate = new JLabel(" " + pm.getPLMXML().getDate() + " ");
        textDate.setBorder(new BevelBorder(BevelBorder.LOWERED));
        JPanel panelDate = new JPanel();
        panelDate.setLayout(new BorderLayout());
        panelDate.add(textDateLabel, BorderLayout.WEST);
        panelDate.add(textDate, BorderLayout.CENTER);

        JLabel textTimeLabel = new JLabel("  Time:");
        JLabel textTime = new JLabel(" " + pm.getPLMXML().getTime() + " ");
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
        if (toolBar != null) {
            return toolBar;
        }
        JButton buttonExport = new JButton("Export");
        buttonExport.setToolTipText("Export to a comma delimited csv file");
        buttonExport.setOpaque(false);
        buttonExport.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = createFileChooser();
                int result = fc.showSaveDialog(parentFrame);
                if (result == JFileChooser.APPROVE_OPTION) {

                    if (!Settings.isPmTblDatabaseMode()) {
                        exportToSpreadSheet(fc.getSelectedFile());
                    } else {
                        exportToDataBaseTables(fc.getSelectedFile());
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
        if (iconTabulate == null) {
            try {
                iconTabulate = ResourceLoader.getImage(ImageEnum.pmTabulate);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(parentFrame, ex.getMessage(), "Error Load Images", JOptionPane.ERROR_MESSAGE);
            }
        }
        return iconTabulate;
    }

    private boolean accessToFile(File file) {
        if (file.isDirectory()) {
            JOptionPane.showMessageDialog(parentFrame, file.getName() + " is a directory!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (file.exists()) {
            if (!file.canWrite()) {
                JOptionPane.showMessageDialog(parentFrame, "You do not have write access to " + file.getName(), "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            int option = JOptionPane.showConfirmDialog(parentFrame, file.getName() + " already exists, overwrite?", "Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (option == JOptionPane.YES_OPTION) {
                file.delete();
            } else {
                return false;
            }
        }
        return true;
    }
    private String padding = "000000";

    private String StrFormat(String num) {
        return padding.substring(0, padding.length() - num.length()) + num;
    }

    private void attachXMLDataRow(Document dom, Element table, String name, String value) {
        Element rowName = dom.createElement(name);
        table.appendChild(rowName);

        Text rowValue = dom.createTextNode(value);
        rowName.appendChild(rowValue);
    }

    private Element attachSchemaSequence(Document dom, Element schema, String name) {
        return attachSchemaSequence(dom, schema, name, false);
    }

    private Element attachSchemaSequence(Document dom, Element schema, String name, boolean addDateInfo) {
        Element schemaElement = dom.createElement("xsd:element");
        schemaElement.setAttribute("name", name);
        schema.appendChild(schemaElement);

        Element schemaType = dom.createElement("xsd:complexType");
        schemaElement.appendChild(schemaType);

        Element schemaSequence = dom.createElement("xsd:sequence");
        schemaType.appendChild(schemaSequence);

        if (addDateInfo) {
            Element dateInfo = dom.createElement("xsd:attribute");
            dateInfo.setAttribute("name", "generated");
            dateInfo.setAttribute("type", "xsd:dateTime");
            schemaType.appendChild(dateInfo);
        }

        return schemaSequence;
    }

    private void attachSchemaTableRef(Document dom, Element schemaSequence, String tableName) {
        Element schemaTable = dom.createElement("xsd:element");
        schemaTable.setAttribute("ref", tableName);
        schemaTable.setAttribute("minOccurs", "0");
        schemaTable.setAttribute("maxOccurs", "unbounded");
        schemaSequence.appendChild(schemaTable);
    }

    private void attachSchemaTableTextCol(Document dom, Element schemaSequence, String colName) {
        attachSchemaTableCol(dom, schemaSequence, colName, 1);
    }

    private void attachSchemaTableMemoCol(Document dom, Element schemaSequence, String colName) {
        attachSchemaTableCol(dom, schemaSequence, colName, 2);
    }

    private void attachSchemaTableIntCol(Document dom, Element schemaSequence, String colName) {
        attachSchemaTableCol(dom, schemaSequence, colName, 3);
    }

    private void attachSchemaTableCol(Document dom, Element schemaSequence, String colName, int mode) {
        Element schemaCol = dom.createElement("xsd:element");
        schemaCol.setAttribute("name", colName);
        schemaCol.setAttribute("minOccurs", "0");
        switch (mode) {
            case 1:
                schemaCol.setAttribute("od:jetType", "text");
                schemaCol.setAttribute("od:sqlSType", "nvarchar");
                break;
            case 2:
                schemaCol.setAttribute("od:jetType", "memo");
                schemaCol.setAttribute("od:sqlSType", "ntext");
                break;
            case 3:
                schemaCol.setAttribute("od:jetType", "longinteger");
                schemaCol.setAttribute("od:sqlSType", "int");
                schemaCol.setAttribute("type", "xsd:int");
                break;
        }
        schemaSequence.appendChild(schemaCol);

        Element schemaColType = dom.createElement("xsd:simpleType");
        schemaCol.appendChild(schemaColType);

        Element schemaColTypeRestriction = dom.createElement("xsd:restriction");
        schemaColTypeRestriction.setAttribute("base", "xsd:string");
        schemaColType.appendChild(schemaColTypeRestriction);

        Element schemaColTypeRestrictionLength = dom.createElement("xsd:maxLength");
        switch (mode) {
            case 1:
                schemaColTypeRestrictionLength.setAttribute("value", "255");
                break;
            case 2:
                schemaColTypeRestrictionLength.setAttribute("value", "536870910");
                break;
        }
        schemaColTypeRestriction.appendChild(schemaColTypeRestrictionLength);
    }

    private void exportToDataBaseTables(File file) {
        String tableName = file.getName();
        File tableFile;
        if (!tableName.toLowerCase().endsWith(".xml")) {
            tableName += ".xml";
        }
        tableFile = new File(file.getParent(), tableName);
        if (!accessToFile(tableFile)) {
            return;
        }
        FreezeRowTableData rowHeader = (FreezeRowTableData) tableFreezeRow.getModel();
        TableData dataModel = (TableData) table.getModel();

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document dom = builder.newDocument();
            Element rootElement = dom.createElement("root");
            rootElement.setAttribute("xmlns:xsd", "http://www.w3.org/2001/XMLSchema");
            rootElement.setAttribute("xmlns:od", "urn:schemas-microsoft-com:officedata");
            dom.appendChild(rootElement);

            // Build Schema
            Element schema = dom.createElement("xsd:schema");
            rootElement.appendChild(schema);
            Element schemaSequence;
            schemaSequence = attachSchemaSequence(dom, schema, "dataroot", true);
            attachSchemaTableRef(dom, schemaSequence, "Task");
            attachSchemaTableRef(dom, schemaSequence, "Map");
            attachSchemaTableRef(dom, schemaSequence, "ActionHandler");
            attachSchemaTableRef(dom, schemaSequence, "ActionHandlerArgument");
            attachSchemaTableRef(dom, schemaSequence, "RuleHandler");
            attachSchemaTableRef(dom, schemaSequence, "RuleHandlerArgument");

            schemaSequence = attachSchemaSequence(dom, schema, "Task");
            attachSchemaTableTextCol(dom, schemaSequence, "TaskId");
            attachSchemaTableTextCol(dom, schemaSequence, "ParentId");
            attachSchemaTableTextCol(dom, schemaSequence, "RootId");
            attachSchemaTableTextCol(dom, schemaSequence, "SiteId");
            attachSchemaTableTextCol(dom, schemaSequence, "ProcedureName");
            attachSchemaTableTextCol(dom, schemaSequence, "ProcedureType");

            schemaSequence = attachSchemaSequence(dom, schema, "Map");
            attachSchemaTableTextCol(dom, schemaSequence, "MapId");
            attachSchemaTableTextCol(dom, schemaSequence, "SiteId");
            attachSchemaTableTextCol(dom, schemaSequence, "TaskId");
            attachSchemaTableTextCol(dom, schemaSequence, "HandlerType");
            attachSchemaTableTextCol(dom, schemaSequence, "ActionHandlerId");
            attachSchemaTableTextCol(dom, schemaSequence, "RuleHandlerId");
            attachSchemaTableTextCol(dom, schemaSequence, "MapValue");

            schemaSequence = attachSchemaSequence(dom, schema, "ActionHandler");
            attachSchemaTableTextCol(dom, schemaSequence, "ActionHandlerId");
            attachSchemaTableTextCol(dom, schemaSequence, "SiteId");
            attachSchemaTableTextCol(dom, schemaSequence, "ActionHandlerName");
            attachSchemaTableIntCol(dom, schemaSequence, "ActionAllArgumentsHash");
            attachSchemaTableMemoCol(dom, schemaSequence, "ActionAllArguments");

            schemaSequence = attachSchemaSequence(dom, schema, "RuleHandler");
            attachSchemaTableTextCol(dom, schemaSequence,      "RuleHandlerId");
            attachSchemaTableTextCol(dom, schemaSequence,      "SiteId");
            attachSchemaTableTextCol(dom, schemaSequence,      "RuleValue");
            attachSchemaTableIntCol(dom, schemaSequence,       "RuleHandlerIndex");
            attachSchemaTableTextCol(dom, schemaSequence,      "RuleHandlerName");
            attachSchemaTableIntCol(dom, schemaSequence,       "RuleAllArgumentsHash");
            attachSchemaTableMemoCol(dom, schemaSequence,      "RuleAllArguments");

            schemaSequence = attachSchemaSequence(dom, schema, "ActionArgument");
            attachSchemaTableTextCol(dom, schemaSequence, "ActionArgumentId");
            attachSchemaTableTextCol(dom, schemaSequence, "ActionHandlerId");
            attachSchemaTableTextCol(dom, schemaSequence, "SiteId");
            attachSchemaTableIntCol(dom, schemaSequence, "ActionArgumentHash");
            attachSchemaTableMemoCol(dom, schemaSequence, "ActionArgument");

            schemaSequence = attachSchemaSequence(dom, schema, "RuleArgument");
            attachSchemaTableTextCol(dom, schemaSequence, "RuleArgumentId");
            attachSchemaTableTextCol(dom, schemaSequence, "RuleHandlerId");
            attachSchemaTableIntCol(dom, schemaSequence, "RuleHandlerIndex");
            attachSchemaTableTextCol(dom, schemaSequence, "SiteId");
            attachSchemaTableIntCol(dom, schemaSequence, "RuleArgumentHash");
            attachSchemaTableMemoCol(dom, schemaSequence, "RuleArgument");

            // Build Data
            DecimalFormat dateformat = new DecimalFormat("00");
            Calendar date = Calendar.getInstance();
            String dYear = dateformat.format(date.get(Calendar.YEAR));
            String dMonth = dateformat.format(date.get(Calendar.MONTH) + 1);
            String dDate = dateformat.format(date.get(Calendar.DATE));
            String dHour = dateformat.format(date.get(Calendar.HOUR_OF_DAY));
            String dMinute = dateformat.format(date.get(Calendar.MINUTE));
            String dSecond = dateformat.format(date.get(Calendar.SECOND));
            Element dataElement = dom.createElement("dataroot");
            dataElement.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
            dataElement.setAttribute("generated", dYear + "-" + dMonth + "-" + dDate + "T" + dHour + ":" + dMinute + ":" + dSecond);
            rootElement.appendChild(dataElement);

            Element xmlTable;
            DecimalFormat df = new DecimalFormat(padding);

            for (int row = 0; row < dataModel.getRowCount(); row++) {

                xmlTable = dom.createElement("Task");
                dataElement.appendChild(xmlTable);

                attachXMLDataRow(dom, xmlTable, "TaskId", modelFreezeRow.getSiteId() + "_" + StrFormat(modelFreezeRow.getId(row)));

                if (!modelFreezeRow.getParentId(row).equals("")) {
                    attachXMLDataRow(dom, xmlTable, "ParentId", modelFreezeRow.getSiteId() + "_" + StrFormat(modelFreezeRow.getParentId(row)));
                }
                if (!modelFreezeRow.getRootId(row).equals("")) {
                    attachXMLDataRow(dom, xmlTable, "RootId", modelFreezeRow.getSiteId() + "_" + StrFormat(modelFreezeRow.getRootId(row)));
                }
                attachXMLDataRow(dom, xmlTable, "SiteId", modelFreezeRow.getSiteId());

                for (int col = 0; col < modelFreezeRow.getColumnCount(); col++) {
                    attachXMLDataRow(dom, xmlTable, rowHeader.getColumnName(col), rowHeader.getRawValueAt(row, col));
                }
            }


            for (int col = 0; col < dataModel.getColumnCount(); col++) {
                if (!dataModel.getColumn(col).isActionHandler()) {
                    continue;
                }

                xmlTable = dom.createElement("ActionHandler");
                dataElement.appendChild(xmlTable);

                attachXMLDataRow(dom, xmlTable, "ActionHandlerId", modelFreezeRow.getSiteId() + "_" + df.format(col));
                attachXMLDataRow(dom, xmlTable, "SiteId", modelFreezeRow.getSiteId());
                attachXMLDataRow(dom, xmlTable, "ActionHandlerName", dataModel.getColumn(col).getHandler(0).getName());

                if (dataModel.getColumn(col).getHandler(0).getArgumentSize() > 0) {
                    attachXMLDataRow(dom, xmlTable, "ActionAllArgumentsHash", Integer.toString(dataModel.getColumn(col).getHandler(0).getAllArguments().hashCode()));
                    attachXMLDataRow(dom, xmlTable, "ActionAllArguments", dataModel.getColumn(col).getHandler(0).getAllArguments());
                }
            }

            for (int col = 0; col < dataModel.getColumnCount(); col++) {
                if (!dataModel.getColumn(col).isRuleHandler()) {
                    continue;
                }

                for (int j = 0; j < dataModel.getColumn(col).getHandlerSize(); j++) {

                    xmlTable = dom.createElement("RuleHandler");
                    dataElement.appendChild(xmlTable);

                    attachXMLDataRow(dom, xmlTable, "RuleHandlerId", modelFreezeRow.getSiteId() + "_" + df.format(col));
                    attachXMLDataRow(dom, xmlTable, "SiteId", modelFreezeRow.getSiteId());
                    attachXMLDataRow(dom, xmlTable, "RuleValue", dataModel.getColumn(col).getRule());
                    attachXMLDataRow(dom, xmlTable, "RuleHandlerIndex", Integer.toString(j));
                    attachXMLDataRow(dom, xmlTable, "RuleHandlerName", dataModel.getColumn(col).getHandler(j).getName());

                    if (dataModel.getColumn(col).getHandler(j).getArgumentSize() > 0) {
                        attachXMLDataRow(dom, xmlTable, "RuleAllArgumentsHash", Integer.toString(dataModel.getColumn(col).getHandler(j).getAllArguments().hashCode()));
                        attachXMLDataRow(dom, xmlTable, "RuleAllArguments", dataModel.getColumn(col).getHandler(j).getAllArguments());
                    }
                }
            }

            int idCount = 0;
            for (int col = 0; col < dataModel.getColumnCount(); col++) {
                if (!dataModel.getColumn(col).isActionHandler()) {
                    continue;
                }

                for (int i = 0; i < dataModel.getColumn(col).getHandler(0).getArgumentSize(); i++) {
                    xmlTable = dom.createElement("ActionArgument");
                    dataElement.appendChild(xmlTable);
                    idCount++;

                    attachXMLDataRow(dom, xmlTable, "ActionArgumentId", modelFreezeRow.getSiteId() + "_" + df.format(idCount));
                    attachXMLDataRow(dom, xmlTable, "ActionHandlerId", modelFreezeRow.getSiteId() + "_" + df.format(col));
                    attachXMLDataRow(dom, xmlTable, "SiteId", modelFreezeRow.getSiteId());
                    attachXMLDataRow(dom, xmlTable, "ActionArgumentHash", Integer.toString(dataModel.getColumn(col).getHandler(0).getArgument(i).getArgument().hashCode()));
                    attachXMLDataRow(dom, xmlTable, "ActionArgument", dataModel.getColumn(col).getHandler(0).getArgument(i).getArgument());
                }
            }

            idCount = 0;
            for (int col = 0; col < dataModel.getColumnCount(); col++) {
                if (!dataModel.getColumn(col).isRuleHandler()) {
                    continue;
                }

                for (int arg = 0; arg < dataModel.getColumn(col).getHandlerSize(); arg++) {
                    for (int i = 0; i < dataModel.getColumn(col).getHandler(arg).getArgumentSize(); i++) {
                        xmlTable = dom.createElement("RuleArgument");
                        dataElement.appendChild(xmlTable);
                        idCount++;

                        attachXMLDataRow(dom, xmlTable, "RuleArgumentId", modelFreezeRow.getSiteId() + "_" + df.format(idCount));
                        attachXMLDataRow(dom, xmlTable, "RuleHandlerId", modelFreezeRow.getSiteId() + "_" + df.format(col));
                        attachXMLDataRow(dom, xmlTable, "RuleHandlerIndex", Integer.toString(arg));
                        attachXMLDataRow(dom, xmlTable, "SiteId", modelFreezeRow.getSiteId());
                        attachXMLDataRow(dom, xmlTable, "RuleArgumentHash", Integer.toString(dataModel.getColumn(col).getHandler(arg).getArgument(i).getArgument().hashCode()));
                        attachXMLDataRow(dom, xmlTable, "RuleArgument", dataModel.getColumn(col).getHandler(arg).getArgument(i).getArgument());
                    }
                }
            }

            idCount = 0;
            for (int row = 0; row < dataModel.getRowCount(); row++) {
                for (int col = 0; col < dataModel.getColumnCount(); col++) {

                    if (dataModel.getValueAt(row, col).equals("")) {
                        continue;
                    }
                    idCount++;
                    xmlTable = dom.createElement("Map");
                    dataElement.appendChild(xmlTable);

                    attachXMLDataRow(dom, xmlTable, "MapId", modelFreezeRow.getSiteId() + "_" + df.format(idCount));
                    attachXMLDataRow(dom, xmlTable, "SiteId", dataModel.getSiteId());
                    attachXMLDataRow(dom, xmlTable, "TaskId", modelFreezeRow.getSiteId() + "_" + StrFormat(modelFreezeRow.getId(row)));
                    if (dataModel.getColumn(col).isActionHandler()) {
                        attachXMLDataRow(dom, xmlTable, "ActionHandlerId", modelFreezeRow.getSiteId() + "_" + df.format(col));
                        attachXMLDataRow(dom, xmlTable, "RuleHandlerId", "");
                    } else if (dataModel.getColumn(col).isRuleHandler()) {
                        attachXMLDataRow(dom, xmlTable, "ActionHandlerId", "");
                        attachXMLDataRow(dom, xmlTable, "RuleHandlerId", modelFreezeRow.getSiteId() + "_" + df.format(col));
                    }
                    attachXMLDataRow(dom, xmlTable, "HandlerType", dataModel.getColumn(col).getClassification());
                    attachXMLDataRow(dom, xmlTable, "MapValue", (String) dataModel.getValueAt(row, col));
                }
            }

            TransformerFactory tf = TransformerFactory.newInstance();
            tf.setAttribute("indent-number", new Integer(4));
            Transformer tr = tf.newTransformer();
            tr.setOutputProperty(OutputKeys.METHOD, "xml");
            tr.setOutputProperty(OutputKeys.INDENT, "yes");
            tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            tr.setOutputProperty("{http://xml.apache.org/xalan}indent-amount", "4");
            tr.transform(new DOMSource(dom), new StreamResult(new OutputStreamWriter(new FileOutputStream(tableFile), "utf-8")));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(parentFrame, e.getMessage(), "Save Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void exportToSpreadSheet(File file) {
        File sheetFile;
        int sheetCount;
        String s = file.getName();

        int colLimit = 256 - modelFreezeRow.getColumnCount();

        if (Settings.isPmTblMultiSheet()) {
            sheetCount = (int) Math.ceil((float) table.getColumnCount() / (float) colLimit);
        } else {
            sheetCount = 1;
        }
        if (sheetCount > 1) {
            if (s.toLowerCase().contains(".sheet")) {
                s = s.substring(0, s.toLowerCase().indexOf(".sheet"));
            }
        }
        for (int i = 0; i < sheetCount; i++) {

            if (s.toLowerCase().endsWith(".csv")) {
                if (sheetCount > 1) {
                    sheetFile = new File(file.getParent(), s.substring(0, s.length() - 4) + ".sheet" + (i + 1) + ".csv");
                } else {
                    sheetFile = new File(file.getParent(), s.substring(0, s.length() - 4) + ".csv");
                }
            } else {
                if (sheetCount > 1) {
                    sheetFile = new File(file.getParent(), s + ".sheet" + (i + 1) + ".csv");
                } else {
                    sheetFile = new File(file.getParent(), s + ".csv");
                }
            }

            if (!accessToFile(sheetFile)) {
                return;
            }
            if (i == sheetCount - 1) {
                exportToSpreadSheet(sheetFile, i * colLimit, table.getColumnCount());
            } else {
                exportToSpreadSheet(sheetFile, i * colLimit, (i * colLimit) + colLimit);
            }
        }
    }

    private void exportToSpreadSheet(File file, int startIndex, int endIndex) {
        FreezeRowTableData rowHeader = (FreezeRowTableData) tableFreezeRow.getModel();
        TableData dataModel = (TableData) table.getModel();

        try {
            FileOutputStream fos = new FileOutputStream(file);
            OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
            BufferedWriter bw = new BufferedWriter(osw);


            // Header
            String s = rowHeader.getCSVColumnName(0);
            for (int i = 1; i < modelFreezeRow.getColumnCount(); i++) {
                s = s + "," + rowHeader.getCSVColumnName(i);
            }
            // Header
            for (int i = startIndex; i < endIndex; i++) {
                s = s + "," + dataModel.getColumn(i).toStringCSV();
            }
            bw.write(s + "\n");

            // Write Data
            for (int row = 0; row < dataModel.getRowCount(); row++) {

                s = rowHeader.getCSVValueAt(row, 0);
                for (int col = 1; col < modelFreezeRow.getColumnCount(); col++) {
                    s = s + "," + rowHeader.getCSVValueAt(row, col);
                }
                for (int col = startIndex; col < endIndex; col++) {
                    s = s + "," + dataModel.getCSVValueAt(row, col);
                }
                bw.write(s + "\n");
            }

            bw.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(parentFrame, e.getMessage(), "Save Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private JFileChooser fileChooser;
    private JCheckBox checkMultiSheet;
    private JCheckBox checkIncludeIndents;
    private CustomFileFilter filterCSV;
    private CustomFileFilter filterXML;

    private JFileChooser createFileChooser() {
        if (fileChooser != null) {
            return fileChooser;
        }
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

        if (Settings.isPmTblDatabaseMode()) {
            checkMultiSheet.setEnabled(false);
            checkIncludeIndents.setEnabled(false);
        } else {
            checkMultiSheet.setEnabled(true);
            checkIncludeIndents.setEnabled(true);
        }

        JRadioButton radioDatabaseMode = new JRadioButton("Database", Settings.isPmTblDatabaseMode());
        radioDatabaseMode.setToolTipText("Export to XML table files");
        radioDatabaseMode.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Settings.setPmTblDatabaseMode(true);
                checkMultiSheet.setEnabled(false);
                checkIncludeIndents.setEnabled(false);
                if (fileChooser.getFileFilter().equals(filterCSV)) {
                    fileChooser.setFileFilter(filterXML);
                }
            }
        });
        JRadioButton radioSpreadSheetMode = new JRadioButton("Spreadsheet", Settings.isPmTblDatabaseMode());
        radioSpreadSheetMode.setToolTipText("Export to CSV spreadsheet file(s)");
        radioSpreadSheetMode.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Settings.setPmTblDatabaseMode(false);
                checkMultiSheet.setEnabled(true);
                checkIncludeIndents.setEnabled(true);
                if (fileChooser.getFileFilter().equals(filterXML)) {
                    fileChooser.setFileFilter(filterCSV);
                }
            }
        });

        ButtonGroup group = new ButtonGroup();
        group.add(radioDatabaseMode);
        group.add(radioSpreadSheetMode);
        radioSpreadSheetMode.setSelected(!Settings.isPmTblDatabaseMode());
        radioDatabaseMode.setSelected(Settings.isPmTblDatabaseMode());

        JPanel panelSpreadsheetOptions = new JPanel();
        panelSpreadsheetOptions.setBorder(new TitledBorder(new EtchedBorder(), "Spreadsheet Options"));
        panelSpreadsheetOptions.setLayout(new GridLayout(2, 1));
        panelSpreadsheetOptions.add(checkMultiSheet);
        panelSpreadsheetOptions.add(checkIncludeIndents);
        JPanel panelExportMode = new JPanel();
        panelExportMode.setBorder(new TitledBorder(new EtchedBorder(), "Export Mode"));
        panelExportMode.setLayout(new GridLayout(2, 1));
        panelExportMode.add(radioDatabaseMode);
        panelExportMode.add(radioSpreadSheetMode);

        JPanel panelOptions = new JPanel();
        panelOptions.setLayout(new GridLayout(2, 1));
        panelOptions.add(panelExportMode);
        panelOptions.add(panelSpreadsheetOptions);

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
                new String[]{"csv", "xml"},
                "All supported Text Files (*.csv, *.xml)"));
        fileChooser.addChoosableFileFilter(filterCSV);
        fileChooser.addChoosableFileFilter(filterXML);

        if (Settings.isPmTblDatabaseMode()) {
            fileChooser.setFileFilter(filterXML);
        } else {
            fileChooser.setFileFilter(filterCSV);
        }
        return fileChooser;
    }

    private void packRowHeader(int colIndex) {
        TableColumn column = tableFreezeRow.getColumnModel().getColumn(colIndex);
        int width = 0;
        String s;

        //DefaultTableCellRenderer label = RowOneRenderer.getRenderer();
        JLabel label = new JLabel();

        for (int r = 0; r < tableFreezeRow.getRowCount(); r++) {
            s = (String) tableFreezeRow.getValueAt(r, colIndex);
            label.setText(s);
            if (modelFreezeRow.isRootNode(r)) {
                label.setFont(label.getFont().deriveFont(Font.BOLD));
            } else {
                label.setFont(label.getFont().deriveFont(Font.PLAIN));
            }
            width = Math.max(width, label.getPreferredSize().width);
        }

        width += 10;

        // Set the width
        column.setPreferredWidth(width);
        column.setMaxWidth(width);
        column.setMinWidth(width);
        column.setWidth(width);

    //tableFreezeRow.setRowHeight(height);
    //table.setRowHeight(height);
    }

    private void packColumn(int columnIndex) {
        TableColumn column = table.getColumnModel().getColumn(columnIndex);
        int width = 16; //icon size

        String s;

        JLabel label = new JLabel();
        TableData model = (TableData) table.getModel();
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
