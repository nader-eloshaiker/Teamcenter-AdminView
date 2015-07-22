/*
 * SearchTableComparator.java
 *
 * Created on 25 September 2007, 09:12
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tceav.gui.tools.search;

/**
 *
 * @author NZR4DL
 */
public interface SearchTableComparator {
    
    public boolean compare(int index, String type, String value);
    
}
