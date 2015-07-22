/*
 * JTableAdvanced.java
 *
 * Created on 17 August 2007, 13:03
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tceav.gui.tools.table;

import javax.swing.JTable;
import javax.swing.table.TableModel;

/**
 *
 * @author nzr4dl
 */
public class JTableAdvanced extends JTable {
    
    /** Creates a new instance of JTableAdvanced */
    public JTableAdvanced() {
        super();
    }
    
    public JTableAdvanced(TableModel model) {
        super(model);
    }
    
    // overriden to make the height of scroll match viewpost height if smaller 
    public boolean getScrollableTracksViewportHeight() { 
        return getPreferredSize().height < getParent().getHeight(); 
    } 
}