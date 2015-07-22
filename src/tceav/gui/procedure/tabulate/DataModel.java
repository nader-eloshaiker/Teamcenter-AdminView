/*
 * DataModel.java
 *
 * Created on 9 May 2008, 08:28
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tceav.gui.procedure.tabulate;

import java.util.ArrayList;
import javax.swing.table.TableModel;
import javax.swing.event.TableModelListener;
import tceav.manager.procedure.ProcedureManager;
import tceav.manager.procedure.plmxmlpdm.TagTypeEnum;
import tceav.manager.procedure.plmxmlpdm.type.*;
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
        WorkflowBusinessRuleType[] wbr;
        WorkflowBusinessRuleHandlerType[] wbrh;
        UserDataType ud;
        int index;
        
        for(int i=0; i<wt.getActions().length; i++) {
            wh = wt.getActions()[i].getActionHandlers();
            for(int j=0; j<wh.length; j++) {
                if(!header.contains(wh[j])) {
                    index = header.indexOfBusinessRule();
                    if(index > -1)
                        header.add(index, new ColumnHeaderEntry(wh[j]));
                    else
                        header.add(new ColumnHeaderEntry(wh[j]));
                }
                for(int k=0; k<wh[j].getAttribute().size(); k++) {
                    if(wh[j].getAttribute().get(k).getTagType() == TagTypeEnum.Arguments) {
                        ud = (UserDataType)wh[j].getAttribute().get(k);
                        if(!header.contains(wh[j], ud)) {
                            index = header.lastIndexOf(wh[j]);
                            header.add(index+1, new ColumnHeaderEntry(wh[j], ud));
                        }
                    }
                }
            }
            
            wbr = wt.getActions()[i].getRules();
            for(int j=0; j<wbr.length; j++) {
                if(!header.contains(wbr[j]))
                    header.add(new ColumnHeaderEntry(wbr[j]));
                
                wbrh = wbr[j].getRuleHandlers();
                for(int k=0; k<wbrh.length; k++) {
                    if(!header.contains(wbr[j], wbrh[k])){
                        index = header.lastIndexOf(wbr[j]);
                        header.add(index+1, new ColumnHeaderEntry(wbr[j], wbrh[k]));
                    }
                        
                    for(int l=0; l<wbrh[k].getAttribute().size(); l++) {
                        if(wbrh[k].getAttribute().get(l).getTagType() == TagTypeEnum.Arguments) {
                            ud = (UserDataType)wbrh[k].getAttribute().get(l);
                            if(!header.contains(wbr[j], wbrh[k], ud)) {
                                index = header.lastIndexOf(wbr[j], wbrh[k]);
                                header.add(index+1, new ColumnHeaderEntry(wbr[j], wbrh[k], ud));
                            }
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
    
    public String getExportValueAt(int rowIndex, int columnIndex) {
        return "\"" + ((String)getValueAt(rowIndex, columnIndex)).replace("\"", "\"\"") + "\"";
    }
    
    public Object getValueAt(int rowIndex, int columnIndex) {
        
        WorkflowTemplateType wt = workflowList.get(rowIndex);
        WorkflowHandlerType[] wh;
        WorkflowBusinessRuleType[] wbr;
        WorkflowBusinessRuleHandlerType[] wbrh;
        UserDataType ud;
        
        ColumnHeaderEntry entry = getColumn(columnIndex);
        
        for(int i=0; i<wt.getActions().length; i++) {
            wh = wt.getActions()[i].getActionHandlers();
            
            for(int j=0; j<wh.length; j++) {
                if(entry.equals(wh[j])) {
                    
                    //if(entry.isArgumentsEmpty())
                    if(entry.isHandler())
                        return "y";
                    
                    for(int k=0; k<wh[j].getAttribute().size(); k++) {
                        if(wh[j].getAttribute().get(k).getTagType() == TagTypeEnum.Arguments) {
                            ud = (UserDataType)wh[j].getAttribute().get(k);
                            if(entry.equals(wh[j], ud))
                                return "y";
                        }
                    }
                    
                }
            }
            
            wbr = wt.getActions()[i].getRules();
            for(int j=0; j<wbr.length; j++) {
                if(entry.equals(wbr[j])){
                    
                    //if(entry.isHandlerEmpty() && entry.isArgumentsEmpty())
                    if(entry.isBusinessRule())
                        return "y";
                    
                    wbrh = wbr[j].getRuleHandlers();
                    
                    for(int k=0; k<wbrh.length; k++) {
                        if(entry.equals(wbr[j], wbrh[k])) {
                            
                            //if(entry.isArgumentsEmpty())
                            if(entry.isHandler())
                                return "y";
                            
                            for(int l=0; l<wbrh[k].getAttribute().size(); l++) {
                                if(wbrh[k].getAttribute().get(l).getTagType() == TagTypeEnum.Arguments) {
                                    ud = (UserDataType)wbrh[k].getAttribute().get(l);
                                    if(entry.equals(wbr[j], wbrh[k], ud))
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
        
        return new RowOneModel(workflowList);
    }
    
    
    public boolean isCellEditable(int rowIndex, int columnIndex) { return false; }
    
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) { }
    
    public void removeTableModelListener(TableModelListener l) { }
    
    public void addTableModelListener(TableModelListener l) { }
    
}
