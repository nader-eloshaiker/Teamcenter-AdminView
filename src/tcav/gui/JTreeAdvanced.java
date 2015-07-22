/*
 * JTreeAdvanced.java
 *
 * Created on 29 August 2007, 19:34
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tcav.gui;

import javax.swing.JTree;
import javax.swing.tree.TreeModel;

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
    
}
