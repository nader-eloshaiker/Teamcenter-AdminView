/*
 * TreeEntryCopyRuleTree.java
 *
 * Created on 10 November 2008, 14:25
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tcav.gui.tools.tree.toolbar;

import javax.swing.tree.TreePath;
import tcav.manager.access.RuleTreeNode;

/**
 *
 * @author nzr4dl-e
 */
public class TreeCopyRuleTreeAdaptor implements TreeCopyModel {
    
    /** Creates a new instance of TreeEntryCopyProcedure */
    public TreeCopyRuleTreeAdaptor() {
    }
    
    @Override
    public String getEntryAsString(TreePath path) {
        return ((RuleTreeNode)path.getLastPathComponent()).toString();
    };
    
}
