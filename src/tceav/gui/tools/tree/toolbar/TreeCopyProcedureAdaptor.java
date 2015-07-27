/*
 * TreeEntryCopyProcedure.java
 *
 * Created on 10 November 2008, 13:57
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tceav.gui.tools.tree.toolbar;

import javax.swing.tree.TreePath;
import tceav.gui.procedure.TaskProperties;


/**
 *
 * @author nzr4dl-e
 */
public class TreeCopyProcedureAdaptor implements TreeCopyModel {
    
    /** Creates a new instance of TreeEntryCopyProcedure */
    public TreeCopyProcedureAdaptor() {
    }
    
    @Override
    public String getEntryAsString(TreePath path) {
        return TaskProperties.getElementName(path.getLastPathComponent());
    }
}
