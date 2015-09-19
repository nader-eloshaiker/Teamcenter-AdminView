/*
 * ColumnHeaderEntry.java
 *
 * Created on 9 May 2008, 08:29
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package tcav.gui.procedure.tabulate;

import tcav.manager.procedure.plmxmlpdm.type.WorkflowHandlerType;
import tcav.manager.procedure.plmxmlpdm.type.UserDataType;
import tcav.manager.procedure.plmxmlpdm.type.WorkflowBusinessRuleType;
import tcav.manager.procedure.plmxmlpdm.ProcedureTagTypeEnum;

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

    @Override
    public String getClassification() {
        return "Action";
    }

    @Override
    public boolean isRuleHandler() {
        return false;
    }

    @Override
    public boolean isActionHandler() {
        return true;
    }

    @Override
    public boolean equals(WorkflowBusinessRuleType wbr) {
        return false;
    }

    @Override
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

    @Override
    public String toStringCompare() {
        return toString();
    }

    @Override
    public String toStringRule() {
        return null;
    }
}
