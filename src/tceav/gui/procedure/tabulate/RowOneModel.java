/*
 * RowOneModel.java
 *
 * Created on 14 May 2008, 23:08
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tceav.gui.procedure.tabulate;

import java.util.ArrayList;
import javax.swing.table.TableModel;
import javax.swing.event.TableModelListener;
import tceav.Settings;
import tceav.manager.procedure.ProcedureManager;
import tceav.manager.procedure.plmxmlpdm.TagTypeEnum;
import tceav.manager.procedure.plmxmlpdm.type.*;
/**
 *
 * @author NZR4DL
 */
public class RowOneModel implements TableModel {
    //private String[] rowHeader;
    private ArrayList<WorkflowTemplateType> rowList;
    
    public RowOneModel(ArrayList<WorkflowTemplateType> rowList) {
        this.rowList = rowList;
        //rowHeader = new String[rowList.size()];
        //for(int i=0; i<rowList.size(); i++)
        //    rowHeader[i] = getIndent(rowList.get(i));
    }
    
    public boolean isRootNode(int rowIndex) {
        if(rowList.get(rowIndex).getParentTaskTemplate() == null)
            return true;
        else
            return false;
    }
    
    public String getId(int rowIndex) {
        String s =  rowList.get(rowIndex).getId();
        if(s == null)
            return "";
        else
            return s.substring(2);
    }
    
    public String getParentId(int rowIndex) {
        String s = rowList.get(rowIndex).getParentTaskTemplateRef();
        if(s == null)
            return "";
        else
            return s.substring(2);
    }
    
    public Class getColumnClass(int columnIndex) {
        return String.class;
    }
    
    public int getColumnCount() {
        /*return 1*/
        return 2;
    }
    
    public String getColumnName(int columnIndex) {
        //return "Procedure Name";
        if(columnIndex == 0)
            return "Procedure Name";
        else
            return "Procedure Type";
    }
    
    public String getColumnExportName(int columnIndex) {
        //return "\"Procedure Name\"";
        return "\"" + getColumnName(columnIndex) + "\"";
    }
    
    public int getRowCount() {
        return rowList.size();
    }
    
    private String getStringValueAt(int rowIndex, int columnIndex) {
        if(columnIndex == 0)
            return rowList.get(rowIndex).getName();
        else {
            String s = rowList.get(rowIndex).getObjectType();
            
            if(s.startsWith("EPM"))
                s = s.substring(3);
            
            if(s.endsWith("Template"))
                s = s.substring(0, s.length() - 8);
            
            return s;
        }
    }
    
    public Object getValueAt(int rowIndex, int columnIndex) {
        //return rowHeader[rowIndex];
        return getIndent(rowList.get(rowIndex)) + getStringValueAt(rowIndex, columnIndex);
    }
    
    public String getExportValueAt(int rowIndex, int columnIndex) {
        if(Settings.isPmTblIncludeIndents())
            return "\"" + getIndent(rowList.get(rowIndex)) + getStringValueAt(rowIndex, columnIndex).replace("\"", "\"\"") + "\"";
        else
            return "\"" + getStringValueAt(rowIndex, columnIndex).replace("\"", "\"\"") + "\"";
    }
    
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }
    
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
    }
    
    public void removeTableModelListener(TableModelListener l) { }
    
    public void addTableModelListener(TableModelListener l) { }
    
    private String getIndent(WorkflowTemplateType wt) {
        WorkflowTemplateType tmp = wt.getParentSubTaskTemplate();
        int indent = 0;
        
        while(tmp != null) {
            tmp = tmp.getParentSubTaskTemplate();
            indent++;
        }
        
        String s = "";
        for (int i=0; i<indent; i++)
            s = s + ColumnHeaderEntry.ARGUMENT_PREFIX;
        
        return s;
    }
}
