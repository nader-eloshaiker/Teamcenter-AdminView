/*
 * ColumnHeaderEntry.java
 *
 * Created on 9 May 2008, 08:29
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package tceav.gui.procedure.tabulate;

import tceav.manager.procedure.plmxmlpdm.ProcedureTagTypeEnum;
import tceav.manager.procedure.plmxmlpdm.type.WorkflowBusinessRuleHandlerType;
import tceav.manager.procedure.plmxmlpdm.type.WorkflowBusinessRuleType;
import tceav.manager.procedure.plmxmlpdm.type.UserDataType;
import tceav.manager.procedure.plmxmlpdm.type.WorkflowHandlerType;

/**
 *
 * @author nzr4dl
 */
public class ColumnHeaderRule extends ColumnHeaderAdapter {

    private int quorum;

    /**
     * Creates a new instance of ColumnHeaderEntry
     */
    public ColumnHeaderRule(WorkflowBusinessRuleType wbr) {
        quorum = wbr.getRuleQuorum();
        WorkflowBusinessRuleHandlerType[] wbrh = wbr.getRuleHandlers();
        UserDataType ud;
        for (int j = 0; j < wbrh.length; j++) {
            addHandler(wbrh[j]);
            for (int k = 0; k < wbrh[j].getAttribute().size(); k++) {
                ud = (UserDataType) wbrh[j].getAttribute().get(k);
                if (ud.getUserDataType() == ProcedureTagTypeEnum.Arguments) {
                    for (int i = 0; i < ud.getUserValue().size(); i++) {
                        getHandler(j).addArgument(ud.getUserValue().get(i).getValue());
                    }
                    break;
                }
            }
        }
    }

    public String getClassification() {
        return "Rule";
    }

    @Override
    public String getRule() {
        return Integer.toString(quorum);
    }

    public boolean isRuleHandler() {
        return true;
    }

    public boolean isActionHandler() {
        return false;
    }

    public boolean equals(WorkflowHandlerType wh) {
        return false;
    }

    public boolean equals(WorkflowBusinessRuleType wbr) {
        ColumnHeaderRule c = new ColumnHeaderRule(wbr);

        if (!getRule().equals(c.getRule())) {
            return false;
        }
        
        if (getHandlerSize() != c.getHandlerSize()) {
            return false;
        }

        for (int j = 0; j < c.getHandlerSize(); j++) {
            if (getHandler(j).getName().compareTo(c.getHandler(j).getName()) != 0) {
                return false;
            }

            if (getHandler(j).getArgumentSize() != c.getHandler(j).getArgumentSize()) {
                return false;
            }

            for (int i = 0; i < c.getHandler(j).getArgumentSize(); i++) {
                if (!getHandler(j).hasArgument(c.getHandler(j).getArgument(i).getArgument())) {
                    return false;
                }
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

        toString = toStringRule();
        for (int i = 0; i < getHandlerSize(); i++) {
            toString += "\n" + getHandler(i).toString();
        }

        return toString;
    }
    private String cmpString;

    public String toStringCompare() {
        if (cmpString != null) {
            return cmpString;
        }

        for (int i = 0; i < getHandlerSize(); i++) {
            cmpString += getHandler(i).toString();
        }
        cmpString += toStringRule();

        return cmpString;
    }

    public String toStringRule() {
        return "Quorum Rule: " + quorum;
    }
}
