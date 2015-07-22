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
import tceav.Settings;
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
        
        for (int i=0; i<pm.getWorkflowProcesses().size(); i++)
            scanWorkflowTemplate(pm.getWorkflowProcesses().get(i));
        
        header.sort();
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
                if(wh[j].getAttribute().size() == 0) {
                    
                    if(!header.contains(wh[j]))
                        header.add(new ColumnHeaderEntry(wh[j]));
                    
                } else {
                    for(int k=0; k<wh[j].getAttribute().size(); k++) {
                        ud = (UserDataType)wh[j].getAttribute().get(k);
                        
                        if(ud.getTagType() == TagTypeEnum.Arguments)
                            if(!header.contains(wh[j], ud))
                                header.add(new ColumnHeaderEntry(wh[j], ud));
                        
                    }
                }
            }
            
            wbr = wt.getActions()[i].getRules();
            for(int j=0; j<wbr.length; j++) {
                if(wbr[j].getRuleHandlers().length == 0){
                    
                    if(!header.contains(wbr[j]))
                        header.add(new ColumnHeaderEntry(wbr[j]));
                    
                } else {
                    wbrh = wbr[j].getRuleHandlers();
                    for(int k=0; k<wbrh.length; k++) {
                        
                        if(wbrh[k].getAttribute().size() == 0) {
                            if(!header.contains(wbr[j], wbrh[k]))
                                header.add(new ColumnHeaderEntry(wbr[j], wbrh[k]));
                            
                        } else {
                            for(int l=0; l<wbrh[k].getAttribute().size(); l++) {
                                ud = (UserDataType)wbrh[k].getAttribute().get(l);
                                
                                if(ud.getTagType() == TagTypeEnum.Arguments)
                                    if(!header.contains(wbr[j], wbrh[k], ud))
                                        header.add(new ColumnHeaderEntry(wbr[j], wbrh[k], ud));
                                
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
    
    private String getValue(WorkflowActionType wa) {
        if(Settings.isPmTblShowActions())
            return wa.getType().getName();
        else
            return "y";
    }
    
    public Object getValueAt(int rowIndex, int columnIndex) {
        
        WorkflowTemplateType wt = workflowList.get(rowIndex);
        WorkflowHandlerType[] wh;
        WorkflowBusinessRuleType[] wbr;
        WorkflowBusinessRuleHandlerType[] wbrh;
        UserDataType ud;
        
        ColumnHeaderEntry entry = getColumn(columnIndex);
        
        
        for(int i=0; i<wt.getActions().length; i++) {
            if(entry.isActionClassification()) {
                
                wh = wt.getActions()[i].getActionHandlers();
                for(int j=0; j<wh.length; j++) {
                    if(entry.matches(wh[j])) {
                        
                        if(entry.equals(wh[j]))
                            return getValue(wt.getActions()[i]);
                        
                        for(int k=0; k<wh[j].getAttribute().size(); k++) {
                            ud = (UserDataType)wh[j].getAttribute().get(k);
                            
                            if(ud.getTagType() == TagTypeEnum.Arguments)
                                if(entry.equals(wh[j], ud))
                                    return getValue(wt.getActions()[i]);
                            
                        }
                        
                        
                    }
                }
                
            } else if(entry.isRuleClassicifaction()) {
                
                wbr = wt.getActions()[i].getRules();
                for(int j=0; j<wbr.length; j++) {
                    if(entry.matches(wbr[j])){
                        
                        if(entry.equals(wbr[j]))
                            return getValue(wt.getActions()[i]);
                        
                        wbrh = wbr[j].getRuleHandlers();
                        
                        for(int k=0; k<wbrh.length; k++) {
                            if(entry.matches(wbr[j], wbrh[k])) {
                                
                                if(entry.equals(wbr[j], wbrh[k]))
                                    return getValue(wt.getActions()[i]);
                                
                                for(int l=0; l<wbrh[k].getAttribute().size(); l++) {
                                    ud = (UserDataType)wbrh[k].getAttribute().get(l);
                                    
                                    if(ud.getTagType() == TagTypeEnum.Arguments)
                                        if(entry.equals(wbr[j], wbrh[k], ud))
                                            return getValue(wt.getActions()[i]);
                                    
                                }
                                
                            }
                        }
                        
                    }
                }
                
            }
            
        }
        
        return "";
        
    }
    
    public RowOneModel createRowHeaderModel() {
        
        return new RowOneModel(workflowList);
    }
    
    
    public boolean isCellEditable(int rowIndex, int columnIndex) { return false; }
    
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) { }
    
    public void removeTableModelListener(TableModelListener l) { }
    
    public void addTableModelListener(TableModelListener l) { }
    
}
