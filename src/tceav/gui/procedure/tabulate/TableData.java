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
import tceav.Settings;
import tceav.manager.procedure.ProcedureManager;
import tceav.manager.procedure.plmxmlpdm.type.UserDataType;
import tceav.manager.procedure.plmxmlpdm.type.WorkflowActionType;
import tceav.manager.procedure.plmxmlpdm.type.WorkflowBusinessRuleHandlerType;
import tceav.manager.procedure.plmxmlpdm.type.WorkflowBusinessRuleType;
import tceav.manager.procedure.plmxmlpdm.type.WorkflowHandlerType;
import tceav.manager.procedure.plmxmlpdm.type.WorkflowTemplateType;

/**
 *
 * @author nzr4dl
 */
public class TableData extends WorkflowAbstractTable {

    private ColumnHeader header;

    /**
     * Creates a new instance of DataModel
     */
    public TableData(ProcedureManager pm) {
        header = new ColumnHeader();
        rowList = new ArrayList<WorkflowTemplateType>();
        siteId = pm.getSite().getName();

        for (int i = 0; i < pm.getWorkflowProcesses().size(); i++) {
            scanWorkflowTemplate(pm.getWorkflowProcesses().get(i));
        }
        header.sort();
    }

    private void scanWorkflowTemplate(WorkflowTemplateType wt) {
        rowList.add(wt);
        WorkflowHandlerType[] wh;
        WorkflowBusinessRuleType[] wbr;
        WorkflowBusinessRuleHandlerType[] wbrh;
        UserDataType ud;

        for (int i = 0; i < wt.getActions().length; i++) {
            wh = wt.getActions()[i].getActionHandlers();
            for (int j = 0; j < wh.length; j++) {
                if (!header.contains(wh[j])) {
                    header.add(new ColumnHeaderAction(wh[j]));
                }
            }

            wbr = wt.getActions()[i].getRules();
            for (int j = 0; j < wbr.length; j++) {
                wbrh = wbr[j].getRuleHandlers();
                for (int k = 0; k < wbrh.length; k++) {
                    if (!header.contains(wbr[j])) {
                        header.add(new ColumnHeaderRule(wbr[j]));
                    }
                }
            }
        }


        for (int k = 0; k < wt.getSubTemplates().length; k++) {
            scanWorkflowTemplate(wt.getSubTemplates()[k]);
        }
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

    public ColumnHeaderAdapter getColumn(int index) {
        return header.get(index);
    }

    public String getColumnName(int columnIndex) {
        return header.get(columnIndex).toString();
    }

    public int getRowCount() {
        return rowList.size();
    }

    public String getCSVValueAt(int rowIndex, int columnIndex) {
        return "\"" + ((String) getValueAt(rowIndex, columnIndex)).replace("\"", "\"\"") + "\"";
    }

    private String getValue(WorkflowActionType wa) {
        if (Settings.isPmTblShowActions()) {
            return wa.getType().getName();
        } else {
            return "y";
        }
    }

    public Object getValueAt(int rowIndex, int columnIndex) {

        WorkflowTemplateType wt = rowList.get(rowIndex);
        ColumnHeaderAdapter  entry = getColumn(columnIndex);
        WorkflowHandlerType[] wh;
        WorkflowBusinessRuleType[] wbr;

        for (int i = 0; i < wt.getActions().length; i++) {
            if (entry.isActionHandler()) {
                wh = wt.getActions()[i].getActionHandlers();
                for (int j = 0; j < wh.length; j++) {
                    if (entry.equals(wh[j])) {
                        return getValue(wt.getActions()[i]);
                    }
                }
            } else if (entry.isRuleHandler()) {
                wbr = wt.getActions()[i].getRules();
                for (int j = 0; j < wbr.length; j++) {
                    if (entry.equals(wbr[j])) {
                        return getValue(wt.getActions()[i]);
                    }

                }
            }
        }

        return "";

    }

    public FreezeRowTableData createRowHeaderModel() {

        return new FreezeRowTableData(rowList, siteId);
    }
}
