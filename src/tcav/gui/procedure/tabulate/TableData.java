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

    private final ColumnHeader header;

    /**
     * Creates a new instance of DataModel
     * @param pm
     */
    public TableData(ProcedureManager pm) {
        header = new ColumnHeader();
        rowList = new ArrayList<>();
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

        for (WorkflowActionType action : wt.getActions()) {
            wh = action.getActionHandlers();
            for (WorkflowHandlerType handler : wh) {
                if (!header.contains(handler)) {
                    header.add(new ColumnHeaderAction(handler));
                }
            }
            wbr = action.getRules();
            for (WorkflowBusinessRuleType rule : wbr) {
                wbrh = rule.getRuleHandlers();
                for (int k = 0; k < wbrh.length; k++) {
                    if (!header.contains(rule)) {
                        header.add(new ColumnHeaderRule(rule));
                    }
                }
            }
        }


        for (WorkflowTemplateType subTemplate : wt.getSubTemplates()) {
            scanWorkflowTemplate(subTemplate);
        }
    }

    @Override
    public Class getColumnClass(int columnIndex) {
        return String.class;
    }

    @Override
    public int getColumnCount() {
        return header.size();
    }

    public ColumnHeader getColumns() {
        return header;
    }

    public ColumnHeaderAdapter getColumn(int index) {
        return header.get(index);
    }

    @Override
    public String getColumnName(int columnIndex) {
        return header.get(columnIndex).toString();
    }

    @Override
    public int getRowCount() {
        return rowList.size();
    }

    @Override
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

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {

        WorkflowTemplateType wt = rowList.get(rowIndex);
        ColumnHeaderAdapter  entry = getColumn(columnIndex);
        WorkflowHandlerType[] wh;
        WorkflowBusinessRuleType[] wbr;

        for (WorkflowActionType action : wt.getActions()) {
            if (entry.isActionHandler()) {
                wh = action.getActionHandlers();
                for (WorkflowHandlerType handler : wh) {
                    if (entry.equals(handler)) {
                        return getValue(action);
                    }
                }
            } else if (entry.isRuleHandler()) {
                wbr = action.getRules();
                for (WorkflowBusinessRuleType rule : wbr) {
                    if (entry.equals(rule)) {
                        return getValue(action);
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
