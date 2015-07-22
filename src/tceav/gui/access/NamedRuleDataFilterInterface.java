/*
 * NamedRuleDataFilterInterface.java
 *
 * Created on 18 March 2008, 12:02
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tceav.gui.access;

import javax.swing.table.*;
import tceav.manager.access.NamedAcl;

/**
 *
 * @author nzr4dl
 */
public interface NamedRuleDataFilterInterface extends TableModel {
    
    public static final int TYPE_COLUMN = 0;
    public static final int INSTANCES_COLUMN = 1;
    public static final int NAME_COLUMN = 2;
    public static final int COMPARE_COLUMN = 3;

    
    public void applyFilter();
    
    public NamedAcl getAccessRule(int row);
    
    public void fireTableDataChanged();
    
    public void setCompareMode(boolean compareMode);
   
}
