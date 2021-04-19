/*
 * RuleTreeModel.java
 *
 * Created on 28 July 2007, 08:46
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tcav.gui.access;

import javax.swing.tree.*;
import javax.swing.event.*;
import tcav.manager.access.NamedAcl;
import tcav.manager.access.RuleTreeNode;


/**
 *
 * @author NZR4DL
 */
public class RuleTreeReferencesModel implements TreeModel{
    
    private final NamedAcl root;
    
    /** Creates a new instance of RuleTreeModel */
    public RuleTreeReferencesModel(NamedAcl root) {
        this.root = root;
    }
    
    
    @Override
    public Object getRoot(){
        return root;
    }
    
    @Override
    public Object getChild(Object parent, int index){
        if(parent.equals(root))
            return root.getRuleTreeReferences().get(index);
        else
            return ((RuleTreeNode)parent).getParent();
    }
    
    @Override
    public int getChildCount(Object parent){
        if(parent.equals(root))
            return root.getRuleTreeReferences().size();
        else {
            if(((RuleTreeNode)parent).getParent() != null)
                return 1;
            else
                return 0;
        }
    }
    
    @Override
    public boolean isLeaf(Object node){
        if(node.equals(root))
            return (root.getRuleTreeReferences().isEmpty());
        else
            return (((RuleTreeNode)node).getParent() == null);
    }
    
    @Override
    public int getIndexOfChild(Object parent, Object child){
        if(parent == null || child == null)
            return -1;
        
        RuleTreeNode childNode = (RuleTreeNode)child;
        
        if(parent.equals(root)) {
            for(int k=0; k<root.getRuleTreeReferences().size(); k++)
                if(root.getRuleTreeReferences().get(k).equals(childNode))
                    return k;
        } else
            return 0;
        
        return -1;
    }
    
    @Override
    public void addTreeModelListener(TreeModelListener listener){
        // not editable
    }
    
    @Override
    public void removeTreeModelListener(TreeModelListener listener){
        // not editable
    }
    
    @Override
    public void valueForPathChanged(TreePath path, Object newValue){
        // not editable
    }
    
}


