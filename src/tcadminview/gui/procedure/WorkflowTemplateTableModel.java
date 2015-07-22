/*
 * WorkflowTemplateTableModel.java
 *
 * Created on 30 July 2007, 15:28
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tcadminview.gui.procedure;

import javax.swing.table.*;
import java.util.ArrayList;
import tcadminview.procedure.WorkflowTemplateList;
import tcadminview.procedure.ProcedureManager;
import tcadminview.plmxmlpdm.classtype.WorkflowTemplateClassificationEnum;
import tcadminview.plmxmlpdm.type.WorkflowTemplateType;
/**
 *
 * @author nzr4dl
 */
public class WorkflowTemplateTableModel extends AbstractTableModel implements TableModel {
    
    WorkflowTemplateList list;
    //ProcedureManager pm;
    WorkflowTemplateClassificationEnum filterType;
    
    /** Creates a new instance of WorkflowTemplateTableModel */
    public WorkflowTemplateTableModel(WorkflowTemplateList list, WorkflowTemplateClassificationEnum filterType) {
        this.list = list;
        this.filterType = filterType;
    }
    
    public WorkflowTemplateTableModel(WorkflowTemplateList list) {
        this(list, WorkflowTemplateClassificationEnum.TASK);
    }

    public WorkflowTemplateClassificationEnum getFilter() {
        return filterType;
    }
    
    public void setFilter(WorkflowTemplateClassificationEnum filterType) {
        this.filterType = filterType;
        super.fireTableDataChanged();
    }
    
    public Class getColumnClass(int columnIndex) {
        return String.class;
    }
    
    public int getColumnCount() {
        return 5;
    }
    
    public String getColumnName(int columnIndex) {
        switch(columnIndex){
            case 0: return "id";
            case 1: return "iconKey";
            case 2: return "Name";
            case 3: return "Show in Process";
            case 4: return "objectType";
            default: return "";
        }
    }
    
    public int getRowCount() {
        return list.getIndexesForClassification(filterType).size();
    }
    
    public Object getValueAt(int rowIndex, int columnIndex) {
        WorkflowTemplateType wt = list.get(list.getIndexesForClassification(filterType).get(rowIndex));
        
        switch(columnIndex){
            case 0: return wt.getId();
            case 1: return wt.getIconKey();
            case 2: return wt.getName();
            case 3: return wt.isShowInProcessStage();
            case 4: return wt.getObjectType();
            default: return "";
        }
    }
    
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }
    
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        
    }
}
