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
import tceav.manager.access.RuleTreeNode;


/**
 *
 * @author NZR4DL
 */
public class RuleTreeModel implements TreeModel{
    
    private RuleTreeNode amTree;
    
    /** Creates a new instance of RuleTreeModel */
    public RuleTreeModel(RuleTreeNode amTree) {
        this.amTree = amTree;
    }
    
 
    public Object getRoot(){ 
        if(amTree.isValid())
            return amTree; 
        else
            return null;
    } 
 
    public Object getChild(Object parent, int index){ 
        return ((RuleTreeNode)parent).getChild(index);
    } 
 
    public int getChildCount(Object parent){ 
        return ((RuleTreeNode)parent).getChildCount();
    } 
 
    public boolean isLeaf(Object node){ 
        return (((RuleTreeNode)node).getChildCount() == 0);
    } 
 
    public int getIndexOfChild(Object parent, Object child){ 
        if(parent == null || child == null)
            return -1;
        
        RuleTreeNode parentNode = (RuleTreeNode)parent; 
        RuleTreeNode childNode = (RuleTreeNode)child; 
        
        for(int i=0; i<=parentNode.getChildCount(); i++)
            if(parentNode.getChild(i).equals(childNode))
                return i;
        
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


