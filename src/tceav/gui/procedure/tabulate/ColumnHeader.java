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
    
    public boolean containsRuleHandler() {
        return indexOfRuleClassification(0) >=0;
    }
    
    public int indexOfRuleClassification() {
        return indexOfRuleClassification(0);
    }
    
    public int indexOfRuleClassification(int index) {
        for (int i = index ; i < super.size() ; i++)
            if (get(i).isRuleClassicifaction())
                return i;
        
        return -1;
    }
    
    public int lastIndexOfRuleClassification() {
        return lastIndexOfRuleClassification(size()-1);
    }
    
    public int lastIndexOfRuleClassification(int index) {
        if (index >= size())
            throw new IndexOutOfBoundsException(index + " >= "+ size());
        
        for (int i = index; i >= 0; i--)
            if (get(i).isRuleClassicifaction())
                return i;
        
        return -1;
    }
    
    public boolean contains(WorkflowBusinessRuleType wbr) {
        return indexOf(wbr, 0) >= 0;
    }
    
    public boolean contains(WorkflowBusinessRuleType wbr, WorkflowBusinessRuleHandlerType wbrh) {
        return indexOf(wbr, wbrh, 0) >= 0;
    }
    
    public boolean contains(WorkflowBusinessRuleType wbr, WorkflowBusinessRuleHandlerType wbrh, UserDataType ud) {
        return indexOf(wbr, wbrh, ud, 0) >= 0;
    }
    
    public boolean contains(WorkflowHandlerType wh) {
        return indexOf(wh, 0) >= 0;
    }
    
    public boolean contains(WorkflowHandlerType wh, UserDataType ud) {
        return indexOf(wh, ud, 0) >= 0;
    }
    
    public int indexOf(WorkflowBusinessRuleType wbr) {
        return indexOf(wbr, 0);
    }
    
    public int indexOf(WorkflowBusinessRuleType wbr, WorkflowBusinessRuleHandlerType wbrh) {
        return indexOf(wbr, wbrh, 0);
    }
    
    public int indexOf(WorkflowBusinessRuleType wbr, WorkflowBusinessRuleHandlerType wbrh, UserDataType ud) {
        return indexOf(wbr, wbrh, ud, 0);
    }
    
    public int indexOf(WorkflowHandlerType wh) {
        return indexOf(wh, 0);
    }
    
    public int indexOf(WorkflowHandlerType wh, UserDataType ud) {
        return indexOf(wh, ud, 0);
    }
    
    public int indexOf(WorkflowBusinessRuleType wbr, int index) {
        for (int i = index ; i < super.size() ; i++)
            if (get(i).equals(wbr))
                return i;
        
        return -1;
    }
    
    public int indexOf(WorkflowBusinessRuleType wbr, WorkflowBusinessRuleHandlerType wbrh, int index) {
        for (int i = index ; i < super.size() ; i++)
            if (get(i).equals(wbr, wbrh))
                return i;
        
        return -1;
    }
    
    public int indexOf(WorkflowBusinessRuleType wbr, WorkflowBusinessRuleHandlerType wbrh, UserDataType ud, int index) {
        for (int i = index ; i < super.size() ; i++)
            if (get(i).equals(wbr, wbrh, ud))
                return i;
        
        return -1;
    }
    
    public int indexOf(WorkflowHandlerType wh, int index) {
        for (int i = index ; i < super.size() ; i++)
            if (get(i).equals(wh))
                return i;
        
        return -1;
    }
    
    public int indexOf(WorkflowHandlerType wh, UserDataType ud, int index) {
        for (int i = index ; i < super.size() ; i++)
            if (get(i).equals(wh, ud))
                return i;
        
        return -1;
    }
    
    
    /********************************
     * Matches
     ********************************/
    
    public int indexOfMatches(WorkflowBusinessRuleType wbr, int index) {
        for (int i = index ; i < super.size() ; i++)
            if (get(i).matches(wbr))
                return i;
        
        return -1;
    }
    
    public int indexOfMatches(WorkflowBusinessRuleType wbr, WorkflowBusinessRuleHandlerType wbrh, int index) {
        for (int i = index ; i < super.size() ; i++)
            if (get(i).matches(wbr, wbrh))
                return i;
        
        return -1;
    }
    
    public int indexOfMatches(WorkflowBusinessRuleType wbr, WorkflowBusinessRuleHandlerType wbrh, UserDataType ud, int index) {
        for (int i = index ; i < super.size() ; i++)
            if (get(i).matches(wbr, wbrh, ud))
                return i;
        
        return -1;
    }
    
    public int indexOfMatches(WorkflowHandlerType wh, int index) {
        for (int i = index ; i < super.size() ; i++)
            if (get(i).matches(wh))
                return i;
        
        return -1;
    }
    
    public int indexOfMatches(WorkflowHandlerType wh, UserDataType ud, int index) {
        for (int i = index ; i < super.size() ; i++)
            if (get(i).matches(wh, ud))
                return i;
        
        return -1;
    }
    
    
    /*************************
     * Last Index Of
     *************************/
    
    public int lastIndexOf(WorkflowHandlerType wh) {
        return lastIndexOf(wh, size()-1);
    }
    
    public int lastIndexOf(WorkflowHandlerType wh, UserDataType ud) {
        return lastIndexOf(wh, ud, size()-1);
    }
    
    public int lastIndexOf(WorkflowBusinessRuleType wbr) {
        return lastIndexOf(wbr, size()-1);
    }
    
    public int lastIndexOf(WorkflowBusinessRuleType wbr, WorkflowBusinessRuleHandlerType wbrh) {
        return lastIndexOf(wbr, wbrh, size()-1);
    }
    
    public int lastIndexOf(WorkflowBusinessRuleType wbr, WorkflowBusinessRuleHandlerType wbrh, UserDataType ud) {
        return lastIndexOf(wbr, wbrh, ud, size()-1);
    }
    
    public int lastIndexOf(WorkflowHandlerType wh, int index) {
        if (index >= size())
            throw new IndexOutOfBoundsException(index + " >= "+ size());
        
        for (int i = index; i >= 0; i--)
            if (get(i).equals(wh))
                return i;
        
        return -1;
    }
    
    public int lastIndexOf(WorkflowHandlerType wh, UserDataType ud, int index) {
        if (index >= size())
            throw new IndexOutOfBoundsException(index + " >= "+ size());
        
        for (int i = index; i >= 0; i--)
            if (get(i).equals(wh, ud))
                return i;
        
        return -1;
    }
    
    public int lastIndexOf(WorkflowBusinessRuleType wbr, int index) {
        if (index >= size())
            throw new IndexOutOfBoundsException(index + " >= "+ size());
        
        for (int i = index; i >= 0; i--)
            if (get(i).equals(wbr))
                return i;
        
        return -1;
    }
    
    public int lastIndexOf(WorkflowBusinessRuleType wbr, WorkflowBusinessRuleHandlerType wbrh, int index) {
        if (index >= size())
            throw new IndexOutOfBoundsException(index + " >= "+ size());
        
        for (int i = index; i >= 0; i--)
            if (get(i).equals(wbr, wbrh))
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

    
    /*************************
     * Last Index Of Matches
     *************************/

    public int lastIndexOfMatches(WorkflowHandlerType wh) {
        return lastIndexOfMatches(wh, size()-1);
    }
    
    public int lastIndexOfMatches(WorkflowHandlerType wh, UserDataType ud) {
        return lastIndexOfMatches(wh, ud, size()-1);
    }
    
    public int lastIndexOfMatches(WorkflowBusinessRuleType wbr) {
        return lastIndexOfMatches(wbr, size()-1);
    }
    
    public int lastIndexOfMatches(WorkflowBusinessRuleType wbr, WorkflowBusinessRuleHandlerType wbrh) {
        return lastIndexOfMatches(wbr, wbrh, size()-1);
    }
    
    public int lastIndexOfMatches(WorkflowBusinessRuleType wbr, WorkflowBusinessRuleHandlerType wbrh, UserDataType ud) {
        return lastIndexOfMatches(wbr, wbrh, ud, size()-1);
    }
    
    public int lastIndexOfMatches(WorkflowHandlerType wh, int index) {
        if (index >= size())
            throw new IndexOutOfBoundsException(index + " >= "+ size());
        
        for (int i = index; i >= 0; i--)
            if (get(i).matches(wh))
                return i;
        
        return -1;
    }
    
    public int lastIndexOfMatches(WorkflowHandlerType wh, UserDataType ud, int index) {
        if (index >= size())
            throw new IndexOutOfBoundsException(index + " >= "+ size());
        
        for (int i = index; i >= 0; i--)
            if (get(i).matches(wh, ud))
                return i;
        
        return -1;
    }
    
    public int lastIndexOfMatches(WorkflowBusinessRuleType wbr, int index) {
        if (index >= size())
            throw new IndexOutOfBoundsException(index + " >= "+ size());
        
        for (int i = index; i >= 0; i--)
            if (get(i).matches(wbr))
                return i;
        
        return -1;
    }
    
    public int lastIndexOfMatches(WorkflowBusinessRuleType wbr, WorkflowBusinessRuleHandlerType wbrh, int index) {
        if (index >= size())
            throw new IndexOutOfBoundsException(index + " >= "+ size());
        
        for (int i = index; i >= 0; i--)
            if (get(i).matches(wbr, wbrh))
                return i;
        
        return -1;
    }
    
    public int lastIndexOfMatches(WorkflowBusinessRuleType wbr, WorkflowBusinessRuleHandlerType wbrh, UserDataType ud, int index) {
        if (index >= size())
            throw new IndexOutOfBoundsException(index + " >= "+ size());
        
        for (int i = index; i >= 0; i--)
            if (get(i).matches(wbr, wbrh, ud))
                return i;
        
        return -1;
    }
    
}
