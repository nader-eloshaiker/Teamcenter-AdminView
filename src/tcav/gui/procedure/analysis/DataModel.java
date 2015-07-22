/*
 * DataModel.java
 *
 * Created on 9 May 2008, 08:28
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tcav.gui.procedure.analysis;

import java.util.ArrayList;
import javax.swing.table.TableModel;
import javax.swing.event.TableModelListener;
import tcav.manager.procedure.ProcedureManager;
import tcav.manager.procedure.plmxmlpdm.type.*;
import tcav.manager.procedure.plmxmlpdm.TagTypeEnum;
/**
 *
 * @author nzr4dl
 */
public class DataModel implements TableModel {
    
    private ColumnHeader header;
    private ArrayList<WorkflowTemplateType> workflowList;
    
    /**
     * Creates a new instance of DataModel
     */
    public DataModel(ProcedureManager pm) {
        header = new ColumnHeader();
        workflowList = new ArrayList<WorkflowTemplateType>();
        
        for (int i=0; i<pm.getWorkflowProcesses().size(); i++) {
            scanWorkflowTemplate(pm.getWorkflowProcesses().get(i));
        }
    }
    
    private void scanWorkflowTemplate(WorkflowTemplateType wt) {
        workflowList.add(wt);
        WorkflowHandlerType[] wh;
        UserDataType ud;
        int index;
        
        for(int i=0; i<wt.getActions().length; i++) {
            wh = wt.getActions()[i].getActionHandlers();
            for(int j=0; j<wh.length; j++) {
                if(!header.contains(wh[j]))
                    header.add(new ColumnHeaderEntry(wh[j]));
                
                for(int k=0; k<wh[j].getAttribute().size(); k++) {
                    if(wh[j].getAttribute().get(k).getTagType() == TagTypeEnum.Arguments) {
                        ud = (UserDataType)wh[j].getAttribute().get(k);
                        if(!header.contains(wh[j], ud)) {
                            index = header.lastIndexOf(wh[j]);
                            header.insertElementAt(new ColumnHeaderEntry(wh[j], ud), index+1);
                        }
                    }
                }
            }
        }
        
        for(int k=0; k<wt.getSubTemplates().length; k++)
            scanWorkflowTemplate(wt.getSubTemplates()[k]);
    }
    
    public Class getColumnClass(int columnIndex) {
        return String.class;
    }
    
    public int getColumnCount() {
        return header.size();
    }
    
    public ColumnHeader getColumns() {
        return header;
    }
    
    public ColumnHeaderEntry getColumn(int index) {
        return header.get(index);
    }
    
    public String getColumnName(int columnIndex) {
        return header.get(columnIndex).toString();
    }
    
    public int getRowCount() {
        return workflowList.size();
    }
    
    public Object getValueAt(int rowIndex, int columnIndex) {
        
        WorkflowTemplateType wt = workflowList.get(rowIndex);
        WorkflowHandlerType[] wh;
        UserDataType ud;
        
        ColumnHeaderEntry entry = getColumn(columnIndex);
        
        for(int i=0; i<wt.getActions().length; i++) {
            wh = wt.getActions()[i].getActionHandlers();
            for(int j=0; j<wh.length; j++) {
                if(entry.equals(wh[j])) {
                    if(entry.isArgumentsEmpty()) {
                        return "y";
                    } else if (wh[j].getAttribute().size() > 0) {
                        for(int k=0; k<wh[j].getAttribute().size(); k++) {
                            if(wh[j].getAttribute().get(k).getTagType() == TagTypeEnum.Arguments) {
                                ud = (UserDataType)wh[j].getAttribute().get(k);
                                if(entry.equals(wh[j], ud)) {
                                    return "y";
                                }
                            }
                        }
                    }
                }
            }
        }
        
        return "";
        
    }
    
    public TableModel createRowHeaderModel() {
        
        return new RowHeaderModel(workflowList);
    }
    
    private class RowHeaderModel implements TableModel {
        private String[] rowHeader;
        
        public RowHeaderModel(ArrayList<WorkflowTemplateType> rowList) {
            rowHeader = new String[rowList.size()];
            for(int i=0; i<rowList.size(); i++)
                rowHeader[i] = getIndent(rowList.get(i)) + rowList.get(i).getName();
        }
        
        public Class getColumnClass(int columnIndex) { return String.class; }
        
        public int getColumnCount() { return 1; }
        
        public String getColumnName(int columnIndex) { return "Procedure Name"; }
        
        public int getRowCount() { return rowHeader.length; }
        
        public Object getValueAt(int rowIndex, int columnIndex) { return rowHeader[rowIndex]; }
        
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
    
    public boolean isCellEditable(int rowIndex, int columnIndex) { return false; }
    
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) { }
    
    public void removeTableModelListener(TableModelListener l) { }
    
    public void addTableModelListener(TableModelListener l) { }
    
}
