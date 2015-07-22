/*
 * WorkflowTableInterface.java
 *
 * Created on 1 September 2008, 10:21
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tceav.gui.procedure.tabulate;

import java.util.ArrayList;
import javax.swing.table.TableModel;
import javax.swing.event.TableModelListener;
import tceav.manager.procedure.plmxmlpdm.type.WorkflowTemplateType;
/**
 *
 * @author nzr4dl-e
 */
public abstract class WorkflowAbstractTable implements TableModel {
    
    protected ArrayList<WorkflowTemplateType> rowList;
    protected String siteId;
    
    public boolean isRootNode(int rowIndex) {
        if(rowList.get(rowIndex).getParentTaskTemplate() == null)
            return true;
        else
            return false;
    }
    
    
    public String getSiteId() {
        return siteId;
    }
    
    public String getWorkflowType(int rowIndex) {
        String s = rowList.get(rowIndex).getObjectType();
        
        if(s.startsWith("EPM"))
            s = s.substring(3);
        
        if(s.startsWith("ECM"))
            s = s.substring(3);
        
        if(s.endsWith("Template"))
            s = s.substring(0, s.length() - 8);
        
        return s;
    }
    
    public abstract String getCSVValueAt(int rowIndex, int columnIndex);
    
    public int getIndentLevel(int rowIndex) {
        WorkflowTemplateType tmp = rowList.get(rowIndex).getParentTaskTemplate();
        int indent = 0;
        
        while(tmp != null) {
            tmp = tmp.getParentTaskTemplate();
            indent++;
        }
        
        return indent;
    }
    
    public boolean isCellEditable(int rowIndex, int columnIndex) { return false; }
    
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) { }
    
    public void removeTableModelListener(TableModelListener l) { }
    
    public void addTableModelListener(TableModelListener l) { }
    
}
