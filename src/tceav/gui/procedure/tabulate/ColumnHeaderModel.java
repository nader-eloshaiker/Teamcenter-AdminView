/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tceav.gui.procedure.tabulate;

import tceav.manager.procedure.plmxmlpdm.type.WorkflowHandlerType;
import tceav.manager.procedure.plmxmlpdm.type.WorkflowBusinessRuleType;

/**
 *
 * @author nzr4dl-e
 */
public interface ColumnHeaderModel {
    
    public boolean isRuleHandler();

    public boolean isActionHandler();

    public String getClassification();
    
    public boolean equals(WorkflowHandlerType wh);
    
    public boolean equals(WorkflowBusinessRuleType wbr);
    
    @Override
    public String toString();
    
    public String toStringRule();
    
    public String toStringCompare();

}
