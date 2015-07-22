/*
 * SearchTreeComparator.java
 *
 * Created on 25 September 2007, 07:34
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tceav.gui.tools.search;

import tceav.gui.tools.tree.JTreeAdvanced;
import javax.swing.tree.TreePath;
/**
 *
 * @author NZR4DL
 */
public interface SearchTreeComparator {
    
    /*
     * Both condition and value cannot be empty, either one or the other.
     */
    public boolean compare(TreePath path, String type, String value);
    
}
