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
import tcadminview.procedure.WorkflowTemplateList;
import tcadminview.plmxmlpdm.classtype.WorkflowTemplateClassificationEnum;
import tcadminview.plmxmlpdm.type.WorkflowTemplateType;
/**
 *
 * @author nzr4dl
 */
public class WorkflowTemplateTableModel extends AbstractTableModel implements TableModel {
    
    WorkflowTemplateList list;
    WorkflowTemplateClassificationEnum filterType;
    
    /** Creates a new instance of WorkflowTemplateTableModel */
    public WorkflowTemplateTableModel(WorkflowTemplateList list) {
        this.list = list;
        filterType = WorkflowTemplateClassificationEnum.PROCESS;
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
            case 0: return "Name";
            case 1: return "Dependant Tasks";
            case 2: return "Sub Workflow";
            case 3: return "key";
            case 4: return "actions";
            default: return "";
        }
    }
    
    public int getRowCount() {
        return list.getIndexesForClassification(filterType).size();
    }
    
    public Object getValueAt(int rowIndex, int columnIndex) {
        WorkflowTemplateType wt = list.get(list.getIndexesForClassification(filterType).get(rowIndex));
        
        switch(columnIndex){
            case 0: return wt.getName();
            case 1: return wt.getDependencyTaskTemplateRefs();
            case 2: return wt.getSubTemplateRefs();
            case 3: return wt.getIconKey();
            case 4: return wt.getActions();
            default: return "";
        }
    }
    
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }
    
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        
    }
}
