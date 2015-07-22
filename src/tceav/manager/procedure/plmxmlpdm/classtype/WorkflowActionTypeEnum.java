/*
 * WorkflowActionTypeEnum.java
 *
 * Created on 2 May 2008, 11:39
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tceav.manager.procedure.plmxmlpdm.classtype;

import java.awt.Color;

/**
 * Custom Enumerator
 *
 * @author nzr4dl
 */
public enum WorkflowActionTypeEnum {
    
    ASSIGN("1", "Assign", 1, new int[]{121,157,201}),       // Light Blue
    START("2", "Start", 2, new int[]{113,137,63}),          // Dark Green
    PERFORM("100", "Perform", 3, new int[]{56,93,138}),     // Dark Blue
    COMPLETE("4", "Complete", 4, new int[]{196,24,24}),     // Dark Red
    SKIP("5", "Skip", 5, new int[]{246,134,42}),            // Dark Orange
    SUSPEND("6", "Suspend", 6, new int[]{255,37,37}),       // Light Red
    RESUME("7", "Resume", 7, new int[]{189,207,160}),       // Light Ggreen
    UNDO("8", "Undo", 8, new int[]{92,71,118}),             // Dark Purple
    ABORT("9", "Abort", 9, new int[]{177,165,194}),         // Light Purble
    UNDEFINED("0", "Undefined", 0, new int[]{100,100,100}); // Gray
    
    private String value;
    private String name;
    private int order;
    private int[] color;
    
    /** Creates a new instance of WorkflowActionTypeEnum */
    WorkflowActionTypeEnum(String value, String name, int order, int[] color) {
        this.value = value;
        this.name = name;
        this.order = order;
        this.color = color;
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
    
    public int[] getColor() {
        return color;
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
