/*
 * WorkflowActionTypeEnum.java
 *
 * Created on 2 May 2008, 11:39
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tceav.manager.procedure.plmxmlpdm.classtype;

/**
 * Custom Enumerator
 *
 * @author nzr4dl
 */
public enum WorkflowActionTypeEnum {
    
    ASSIGN("1", "Assign", 1),
    START("2", "Start", 2),
    PERFORM("100", "Perform", 3),
    COMPLETE("4", "Complete", 4),
    SKIP("5", "Skip", 5),
    SUSPEND("6", "Suspend", 6),
    RESUME("7", "Resume", 7),
    UNDO("8", "Undo", 8),
    ABORT("9", "Abort", 9),
    UNDEFINED("0", "Undefined", 0);
    
    private String value;
    private String name;
    private int order;
    
    /** Creates a new instance of WorkflowActionTypeEnum */
    WorkflowActionTypeEnum(String value, String name, int order) {
        this.value = value;
        this.name = name;
        this.order = order;
    }

    public int getOrder() {
        return order;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
        this.name = value;
        
        try{
            this.order = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            this.order = 0;
        }
    }
    
    public static WorkflowActionTypeEnum fromValue(String v) {
        for (WorkflowActionTypeEnum c: WorkflowActionTypeEnum.values())
            if (c.value.equals(v))
                return c;
        
        WorkflowActionTypeEnum result = UNDEFINED;
        result.setValue(v);
        return result;
    }
    
}
