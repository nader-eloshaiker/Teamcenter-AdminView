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
import tceav.manager.procedure.plmxmlpdm.type.WorkflowBusinessRuleType;

/**
 *
 * @author nzr4dl
 */
public class ColumnHeader extends ArrayList<ColumnHeaderAdapter> {
    
    /** Creates a new instance of ColumnHeader */
    public ColumnHeader() {
        super();
    }
    
    public void sort() {
        ColumnHeaderAdapter c;
        
        for (int i = 0; i < size(); i++) {
            for (int j = i+1; j < size(); j++) {
                if (get(i).compare(get(j)) == 1) {
                    c = get(i);
                    set(i, get(j));
                    set(j, c);
                }
            }
        }
    }
    
    public boolean contains(WorkflowBusinessRuleType wbr) {
        return indexOf(wbr, 0) >= 0;
    }
    
    public boolean contains(WorkflowHandlerType wh) {
        return indexOf(wh, 0) >= 0;
    }
    
    public int indexOf(WorkflowBusinessRuleType wbr) {
        return indexOf(wbr, 0);
    }
    
    public int indexOf(WorkflowHandlerType wh) {
        return indexOf(wh, 0);
    }
    
    
    public int indexOf(WorkflowBusinessRuleType wbr, int index) {
        for (int i = index ; i < super.size() ; i++)
            if (get(i).equals(wbr))
                return i;
        
        return -1;
    }
    
    public int indexOf(WorkflowHandlerType wh, int index) {
        for (int i = index ; i < super.size() ; i++)
            if (get(i).equals(wh))
                return i;
        
        return -1;
    }
    
    /*************************
     * Last Index Of
     *************************/
    
    public int lastIndexOf(WorkflowHandlerType wh) {
        return lastIndexOf(wh, size()-1);
    }
    
    public int lastIndexOf(WorkflowBusinessRuleType wbr) {
        return lastIndexOf(wbr, size()-1);
    }
    
    public int lastIndexOf(WorkflowHandlerType wh, int index) {
        if (index >= size())
            throw new IndexOutOfBoundsException(index + " >= "+ size());
        
        for (int i = index; i >= 0; i--)
            if (get(i).equals(wh))
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
    
}
