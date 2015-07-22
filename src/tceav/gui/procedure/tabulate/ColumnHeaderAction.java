/*
 * ColumnHeaderEntry.java
 *
 * Created on 9 May 2008, 08:29
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package tceav.gui.procedure.tabulate;

import tceav.manager.procedure.plmxmlpdm.type.WorkflowHandlerType;
import tceav.manager.procedure.plmxmlpdm.type.UserDataType;
import tceav.manager.procedure.plmxmlpdm.type.WorkflowBusinessRuleType;
import tceav.manager.procedure.plmxmlpdm.ProcedureTagTypeEnum;

/**
 *
 * @author nzr4dl
 */
public class ColumnHeaderAction extends ColumnHeaderAdapter {

    /**
     * Creates a new instance of ColumnHeaderEntry
     */
    public ColumnHeaderAction(WorkflowHandlerType wh) {
        addHandler(wh);
        UserDataType ud;
        for (int k = 0; k < wh.getAttribute().size(); k++) {
            ud = (UserDataType) wh.getAttribute().get(k);
            if (ud.getUserDataType() == ProcedureTagTypeEnum.Arguments) {
                for (int i = 0; i < ud.getUserValue().size(); i++) {
                    getHandler(0).addArgument(ud.getUserValue().get(i).getValue());
                }
                break;
            }
        }
    }

    public String getClassification() {
        return "Action";
    }

    public boolean isRuleHandler() {
        return false;
    }

    public boolean isActionHandler() {
        return true;
    }

    public boolean equals(WorkflowBusinessRuleType wbr) {
        return false;
    }

    public boolean equals(WorkflowHandlerType wh) {
        ColumnHeaderAction c = new ColumnHeaderAction(wh);

        if (!getHandler(0).getName().equals(c.getHandler(0).getName())) {
            return false;
        }

        if (getHandler(0).getArgumentSize() != c.getHandler(0).getArgumentSize()) {
            return false;
        }

        for (int i = 0; i < c.getHandler(0).getArgumentSize(); i++) {
            if (!getHandler(0).hasArgument(c.getHandler(0).getArgument(i).getArgument())) {
                return false;
            }
        }
        
        return true;
    }
    private String toString;

    @Override
    public String toString() {
        if (toString != null) {
            return toString;
        }
        toString = getHandler(0).toString();
        return toString;
    }

    public String toStringCompare() {
        return toString();
    }

    public String toStringRule() {
        return null;
    }
}
