/*
 * RuleTreeModel.java
 *
 * Created on 28 July 2007, 08:46
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tceav.gui.access;

import java.util.ArrayList;
import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.event.*;
import tceav.manager.access.NamedAcl;
import tceav.manager.access.RuleTreeNode;


/**
 *
 * @author NZR4DL
 */
public class RuleTreeReferencesModel implements TreeModel{
    
    private NamedAcl root;
    
    /** Creates a new instance of RuleTreeModel */
    public RuleTreeReferencesModel(NamedAcl root) {
        this.root = root;
    }
    
    
    public Object getRoot(){
        return root;
    }
    
    public Object getChild(Object parent, int index){
        if(parent.equals(root))
            return root.getRuleTreeReferences().get(index);
        else
            return ((RuleTreeNode)parent).getParent();
    }
    
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
    
    public boolean isLeaf(Object node){
        if(node.equals(root))
            return (root.getRuleTreeReferences().size() == 0);
        else
            return (((RuleTreeNode)node).getParent() == null);
    }
    
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
    
    public void addTreeModelListener(TreeModelListener listener){
        // not editable
    }
    
    public void removeTreeModelListener(TreeModelListener listener){
        // not editable
    }
    
    public void valueForPathChanged(TreePath path, Object newValue){
        // not editable
    }
    
}


