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
    private ArrayList <WorkflowTemplateType>workflowList;
    
    /**
     * Creates a new instance of DataModel
     */
    public DataModel(ProcedureManager pm) {
        header = new ColumnHeader();
        header.add(new ColumnHeaderEntry("Procedure Name"));
        workflowList = new ArrayList<WorkflowTemplateType>();
        
        for (int i=0; i<pm.getWorkflowProcesses().size(); i++) {
            scanWorkflowTemplate(pm.getWorkflowProcesses().get(i));
        }
    }
    
    private void scanWorkflowTemplate(WorkflowTemplateType wt) {
        workflowList.add(wt);
        WorkflowHandlerType[] wh;
        int index;
        
        for(int i=0; i<wt.getActions().length; i++) {
            wh = wt.getActions()[i].getActionHandlers();
            for(int j=0; j<wh.length; j++) {
                if(!header.contains(wh[j].getName()))
                    header.add(new ColumnHeaderEntry(wh[j].getName()));
                
                for(int k=0; k<wh[j].getAttribute().size(); k++) {
                    if(wh[j].getAttribute().get(k).getTagType() == TagTypeEnum.Arguments) {
                        UserDataType ud = (UserDataType)wh[j].getAttribute().get(k);
                        for(int l=0; l<ud.getUserValue().size(); l++) {
                            if(!header.contains(wh[j].getName(), ud.getUserValue().get(l).getValue())) {
                                index = header.lastIndexOf(wh[j].getName());
                                header.insertElementAt(new ColumnHeaderEntry(wh[j].getName(), ud.getUserValue().get(l).getValue()), index+1);
                            }
                        }
                    }
                }
            }
        }
        
        for(int k=0; k<wt.getSubTemplates().length; k++)
            scanWorkflowTemplate(wt.getSubTemplates()[k]);
    }
    
    public String getIndent(int rowIndex) {
        WorkflowTemplateType wt = workflowList.get(rowIndex).getParentSubTaskTemplate();
        int indent = 0;
        
        while(wt != null) {
            wt = wt.getParentSubTaskTemplate();
            indent++;
        }
        
        String s = "";
        for (int i=0; i<indent; i++)
            s = s + "    ";
        
        return s;
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
        
        if(columnIndex == 0)
            return getIndent(rowIndex) + wt.getName();
        
        ColumnHeaderEntry entry = getColumn(columnIndex);
        for(int i=0; i<wt.getActions().length; i++) {
            wh = wt.getActions()[i].getActionHandlers();
            for(int j=0; j<wh.length; j++) {
                if(entry.NAME.equals(wh[j].getName())) {
                    if(entry.VALUE == null) {
                        return "y";
                    } else {
                        for(int k=0; k<wh[j].getAttribute().size(); k++) {
                            if(wh[j].getAttribute().get(k).getTagType() == TagTypeEnum.Arguments) {
                                UserDataType ud = (UserDataType)wh[j].getAttribute().get(k);
                                for(int l=0; l<ud.getUserValue().size(); l++) {
                                    if(entry.NAME.equals(wh[j].getName()) && entry.VALUE.equals(ud.getUserValue().get(l).getValue())) {
                                        return "y";
                                    }
                                }
                            }
                        }
                        
                    }
                }
            }
        }
        
        return "";
        
    }
    
    /**********************
     * Unused methods
     **********************/
    
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }
    
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        
    }
    
    public void removeTableModelListener(TableModelListener l) {
        
    }
    
    public void addTableModelListener(TableModelListener l) {
        
    }
    
}
