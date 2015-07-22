/*
 * JTreeAdvanced.java
 *
 * Created on 29 August 2007, 19:34
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tceav.gui.tools.tree;

import java.util.ArrayList;
import javax.swing.JTree;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeModelEvent;

/**
 *
 * @author nader
 */
public class JTreeAdvanced extends JTree{
    
    /** Creates a new instance of JTreeAdvanced */
    public JTreeAdvanced(TreeModel model) {
        super(model);
    }
    
    public JTreeAdvanced(Object[] model){
        super(model);
    }
    
    /*
     * This is used to get around the caching bug for collapse and expanding all
     * branches.
     */
    public void clearToggledPaths(){
        super.clearToggledPaths();
    }
    
    public TreeModelListener getTreeModelListener() {
        return treeModelListener;
    }
    
    public void reloadModel() {
        getTreeModelListener().treeStructureChanged(new TreeModelEvent(
                getModel(), 
                new Object[] {getModel().getRoot()},
                null, 
                null));
    }
    
}
