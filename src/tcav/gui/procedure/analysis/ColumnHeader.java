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

/**
 *
 * @author nzr4dl
 */
public class ColumnHeader extends Vector<ColumnHeaderEntry> {
    
    /** Creates a new instance of ColumnHeader */
    public ColumnHeader() {
        super();
    }
    
    public boolean contains(String name) {
        return indexOf(name, null, 0) >= 0;
    }
    
    public boolean contains(String name, String value) {
        return indexOf(name, value, 0) >= 0;
    }
    
    public int indexOf(String name) {
        return indexOf(name, null, 0);
    }
    
    public int indexOf(String name, String value) {
        return indexOf(name, value, 0);
    }
    
    public synchronized int indexOf(String name, String value, int index) {
        if (value == null) {
            for (int i = index ; i < super.size() ; i++)
                if (name.equals(get(i).NAME) && get(i).VALUE==null)
                    return i;
        } else {
            for (int i = index ; i < super.size() ; i++){
                if (name.equals(get(i).NAME) && value.equals(get(i).VALUE)) 
                    return i;
                
            }
        }
        return -1;
    }
    
    public synchronized int lastIndexOf(String name) {
        return lastIndexOf(name, null, size()-1);
    }
    
    public synchronized int lastIndexOf(String name, String value) {
        return lastIndexOf(name, value, size()-1);
    }
    
    public synchronized int lastIndexOf(String name, String value, int index) {
        if (index >= size())
            throw new IndexOutOfBoundsException(index + " >= "+ size());
        
        if (value == null) {
            for (int i = index; i >= 0; i--)
                if (name.equals(get(i).NAME))
                    return i;
        } else {
            for (int i = index; i >= 0; i--)
                if (name.equals(get(i).NAME) && value.equals(get(i).VALUE)) 
                    return i;
        }
        return -1;
    }
    
    
    
}
