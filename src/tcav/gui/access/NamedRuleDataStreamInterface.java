/*
 * NamedRuleDataStreamInterface.java
 *
 * Created on 18 March 2008, 12:02
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tcav.gui.access;

import javax.swing.table.*;
import tcav.manager.access.AccessRule;

/**
 *
 * @author nzr4dl
 */
public interface NamedRuleDataStreamInterface extends TableModel {
    
    public static final int TYPE_COLUMN = 0;
    public static final int INSTANCES_COLUMN = 1;
    public static final int NAME_COLUMN = 2;
    public static final int TOTAL_COLUMNS = 3;

    
    public void apply();
    
    public AccessRule getAccessRule(int row);

}
