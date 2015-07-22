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
import tceav.Settings;
import tceav.manager.procedure.plmxmlpdm.type.WorkflowTemplateType;

/**
 *
 * @author NZR4DL
 */
public class FreezeRowTableData extends WorkflowAbstractTable {

    public FreezeRowTableData(ArrayList<WorkflowTemplateType> rowList, String siteId) {
        this.rowList = rowList;
        this.siteId = siteId;
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
    
    public String getRootId(int rowIndex) {
        if(rowList.get(rowIndex).isRootTaskTemplate())
            return "";
        
        String s = rowList.get(rowIndex).getRootTaskTemplate().getId();
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
            return "ProcedureName";
        else
            return "ProcedureType";
    }
    
    public String getCSVColumnName(int columnIndex) {
        //return "\"Procedure Name\"";
        return "\"" + getColumnName(columnIndex) + "\"";
    }
    
    public int getRowCount() {
        return rowList.size();
    }
    
    public String getRawValueAt(int rowIndex, int columnIndex) {
        if(columnIndex == 0)
            return rowList.get(rowIndex).getName();
        else
            return getWorkflowType(rowIndex);
    }
    
    public Object getValueAt(int rowIndex, int columnIndex) {
        return getIndentString(rowIndex) + getRawValueAt(rowIndex, columnIndex);
    }
    
    public String getCSVValueAt(int rowIndex, int columnIndex) {
        if(Settings.isPmTblIncludeIndents())
            return "\"" + getIndentString(rowIndex) + getRawValueAt(rowIndex, columnIndex).replace("\"", "\"\"") + "\"";
        else
            return "\"" + getRawValueAt(rowIndex, columnIndex).replace("\"", "\"\"") + "\"";
    }
    
    private String getIndentString(int rowIndex) {
        
        int indent = getIndentLevel(rowIndex);
        
        String s = "";
        for (int i=0; i<indent; i++)
            s = s + ColumnHeaderAdapter.ARGUMENT_PREFIX;
        
        return s;
    }
}
