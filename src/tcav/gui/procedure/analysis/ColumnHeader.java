/*
 * ColumnHeader.java
 *
 * Created on 9 May 2008, 08:29
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tcav.gui.procedure.analysis;

import java.util.Vector;
import tcav.manager.procedure.plmxmlpdm.type.WorkflowHandlerType;
import tcav.manager.procedure.plmxmlpdm.type.UserDataType;

/**
 *
 * @author nzr4dl
 */
public class ColumnHeader extends Vector<ColumnHeaderEntry> {
    
    /** Creates a new instance of ColumnHeader */
    public ColumnHeader() {
        super();
    }
    
    public boolean contains(WorkflowHandlerType wh) {
        return indexOf(wh, null, 0) >= 0;
    }
    
    public boolean contains(WorkflowHandlerType wh, UserDataType ud) {
        return indexOf(wh, ud, 0) >= 0;
    }
    
    public int indexOf(WorkflowHandlerType wh) {
        return indexOf(wh, null, 0);
    }
    
    public int indexOf(WorkflowHandlerType wh, UserDataType ud) {
        return indexOf(wh, ud, 0);
    }
    
    public synchronized int indexOf(WorkflowHandlerType wh, UserDataType ud, int index) {
        for (int i = index ; i < super.size() ; i++)
            if (get(i).equals(wh, ud))
                return i;
        
        return -1;
    }
    
    public synchronized int lastIndexOf(WorkflowHandlerType wh) {
        return lastIndexOf(wh, null, size()-1);
    }
    
    public synchronized int lastIndexOf(WorkflowHandlerType wh, UserDataType ud) {
        return lastIndexOf(wh, ud, size()-1);
    }
    
    public synchronized int lastIndexOf(WorkflowHandlerType wh, UserDataType ud, int index) {
        if (index >= size())
            throw new IndexOutOfBoundsException(index + " >= "+ size());
        
        for (int i = index; i >= 0; i--)
            if (get(i).equals(wh, ud))
                return i;
        
        return -1;
    }
    
    
    
}
