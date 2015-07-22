/*
 * TreeEntryCopyRuleTree.java
 *
 * Created on 10 November 2008, 14:25
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tceav.gui.tools.tree.toolbar;

import javax.swing.tree.TreePath;
import tceav.gui.*;
import tceav.manager.access.RuleTreeNode;

/**
 *
 * @author nzr4dl-e
 */
public class TreeCopyRuleTreeAdaptor implements TreeCopyModel {
    
    /** Creates a new instance of TreeEntryCopyProcedure */
    public TreeCopyRuleTreeAdaptor() {
    }
    
    public String getEntryAsString(TreePath path) {
        return ((RuleTreeNode)path.getLastPathComponent()).toString();
    };
    
}
