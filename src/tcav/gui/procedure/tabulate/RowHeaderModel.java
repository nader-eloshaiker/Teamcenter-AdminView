/*
 * RowHeaderModel.java
 *
 * Created on 14 May 2008, 23:08
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tcav.gui.procedure.tabulate;

import java.util.ArrayList;
import javax.swing.table.TableModel;
import javax.swing.event.TableModelListener;
import tcav.manager.procedure.ProcedureManager;
import tcav.manager.procedure.plmxmlpdm.type.*;
import tcav.manager.procedure.plmxmlpdm.TagTypeEnum;

/**
 *
 * @author NZR4DL
 */
public class RowHeaderModel implements TableModel {
    private String[] rowHeader;
    
    public RowHeaderModel(ArrayList<WorkflowTemplateType> rowList) {
        rowHeader = new String[rowList.size()];
        for(int i=0; i<rowList.size(); i++)
            rowHeader[i] = getIndent(rowList.get(i)) + rowList.get(i).getName();
    }
    
    public Class getColumnClass(int columnIndex) { return String.class; }
    
    public int getColumnCount() { return 1; }
    
    public String getColumnName(int columnIndex) { return "Procedure Name"; }
    
    public String getColumnExportName(int columnIndex) { return "\"Procedure Name\""; }
    
    public int getRowCount() { return rowHeader.length; }
    
    public Object getValueAt(int rowIndex, int columnIndex) { return rowHeader[rowIndex]; }
    
    public String getExportValueAt(int rowIndex, int columnIndex) { return "\"" + rowHeader[rowIndex].replace("\"", "\"\"") + "\""; }
    
    public boolean isCellEditable(int rowIndex, int columnIndex) { return false; }
    
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) { }
    
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
            s = s + "    ";
        
        return s;
    }
}
