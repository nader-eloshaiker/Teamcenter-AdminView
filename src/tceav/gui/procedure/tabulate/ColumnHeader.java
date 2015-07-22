/*
 * ColumnHeader.java
 *
 * Created on 9 May 2008, 08:29
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tceav.gui.procedure.tabulate;

import java.util.ArrayList;
import tceav.manager.procedure.plmxmlpdm.type.WorkflowHandlerType;
import tceav.manager.procedure.plmxmlpdm.type.WorkflowBusinessRuleHandlerType;
import tceav.manager.procedure.plmxmlpdm.type.WorkflowBusinessRuleType;
import tceav.manager.procedure.plmxmlpdm.type.UserDataType;

/**
 *
 * @author nzr4dl
 */
public class ColumnHeader extends ArrayList<ColumnHeaderEntry> {
    
    /** Creates a new instance of ColumnHeader */
    public ColumnHeader() {
        super();
    }
    
    public boolean containsBusinessRule() {
        return indexOfBusinessRule(0) >=0;
    }
    
    public int indexOfBusinessRule() {
        return indexOfBusinessRule(0);
    }
    
    public int indexOfBusinessRule(int index) {
        for (int i = index ; i < super.size() ; i++)
            if (get(i).isBusinessRule())
                return i;
        
        return -1;
    }
    
    public int lastIndexOfBusinessRule() {
        return lastIndexOfBusinessRule(size()-1);
    }
    
    public int lastIndexOfBusinessRule(int index) {
        if (index >= size())
            throw new IndexOutOfBoundsException(index + " >= "+ size());
        
        for (int i = index; i >= 0; i--)
            if (get(i).isBusinessRule())
                return i;
        
        return -1;
    }
    
    public boolean contains(WorkflowBusinessRuleType wbr) {
        return indexOf(wbr, null, null, 0) >= 0;
    }
    
    public boolean contains(WorkflowBusinessRuleType wbr, WorkflowBusinessRuleHandlerType wbrh) {
        return indexOf(wbr, wbrh, null, 0) >= 0;
    }
    
    public boolean contains(WorkflowBusinessRuleType wbr, WorkflowBusinessRuleHandlerType wbrh, UserDataType ud) {
        return indexOf(wbr, wbrh, ud, 0) >= 0;
    }
    
    public boolean contains(WorkflowHandlerType wh) {
        return indexOf(wh, null, 0) >= 0;
    }
    
    public boolean contains(WorkflowHandlerType wh, UserDataType ud) {
        return indexOf(wh, ud, 0) >= 0;
    }
    
    public int indexOf(WorkflowBusinessRuleType wbr) {
        return indexOf(wbr, null, null, 0);
    }
    
    public int indexOf(WorkflowBusinessRuleType wbr, WorkflowBusinessRuleHandlerType wbrh) {
        return indexOf(wbr, wbrh, null, 0);
    }
    
    public int indexOf(WorkflowBusinessRuleType wbr, WorkflowBusinessRuleHandlerType wbrh, UserDataType ud) {
        return indexOf(wbr, wbrh, ud, 0);
    }
    
    public int indexOf(WorkflowHandlerType wh) {
        return indexOf(wh, null, 0);
    }
    
    public int indexOf(WorkflowHandlerType wh, UserDataType ud) {
        return indexOf(wh, ud, 0);
    }
    
    public int indexOf(WorkflowBusinessRuleType wbr, WorkflowBusinessRuleHandlerType wbrh, UserDataType ud, int index) {
        for (int i = index ; i < super.size() ; i++)
            if (get(i).equals(wbr, wbrh, ud))
                return i;
        
        return -1;
    }
    
    public int indexOf(WorkflowHandlerType wh, UserDataType ud, int index) {
        for (int i = index ; i < super.size() ; i++)
            if (get(i).equals(wh, ud))
                return i;
        
        return -1;
    }
    
    public int lastIndexOf(WorkflowHandlerType wh) {
        return lastIndexOf(wh, null, size()-1);
    }
    
    public int lastIndexOf(WorkflowHandlerType wh, UserDataType ud) {
        return lastIndexOf(wh, ud, size()-1);
    }
    
    public int lastIndexOf(WorkflowBusinessRuleType wbr) {
        return lastIndexOf(wbr, null, null, size()-1);
    }
    
    public int lastIndexOf(WorkflowBusinessRuleType wbr, WorkflowBusinessRuleHandlerType wbrh) {
        return lastIndexOf(wbr, wbrh, null, size()-1);
    }
    
    public int lastIndexOf(WorkflowBusinessRuleType wbr, WorkflowBusinessRuleHandlerType wbrh, UserDataType ud) {
        return lastIndexOf(wbr, wbrh, ud, size()-1);
    }
    
    public int lastIndexOf(WorkflowHandlerType wh, UserDataType ud, int index) {
        if (index >= size())
            throw new IndexOutOfBoundsException(index + " >= "+ size());
        
        for (int i = index; i >= 0; i--)
            if (get(i).equals(wh, ud))
                return i;
        
        return -1;
    }
    
    public int lastIndexOf(WorkflowBusinessRuleType wbr, WorkflowBusinessRuleHandlerType wbrh, UserDataType ud, int index) {
        if (index >= size())
            throw new IndexOutOfBoundsException(index + " >= "+ size());
        
        for (int i = index; i >= 0; i--)
            if (get(i).equals(wbr, wbrh, ud))
                return i;
        
        return -1;
    }
    
}
