/*
 * RuleTreeModel.java
 *
 * Created on 28 July 2007, 08:46
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tcav.gui.ruletree;

import java.util.ArrayList;
import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.event.*;
import tcav.ruletree.RuleTreeItem;


/**
 *
 * @author NZR4DL
 */
public class RuleTreeModel implements TreeModel{
    
    private ArrayList<RuleTreeItem> amTree;
    
    /** Creates a new instance of RuleTreeModel */
    public RuleTreeModel(ArrayList<RuleTreeItem> amTree) {
        this.amTree = amTree;
    }
    
 
    public Object getRoot(){ 
        if(amTree.size() == 0)
            return null;
        else
            return amTree.get(0); 
    } 
 
    public Object getChild(Object parent, int index){ 
        RuleTreeItem node = (RuleTreeItem)parent; 
        int indexCounter = -1;
        int parentIndex = amTree.indexOf(node);
        
        for(int i=parentIndex; i<amTree.size(); i++) {
            if(isChild(node,amTree.get(i)))
                indexCounter++;
            
            if(indexCounter == index){
                //System.out.println("node: "+amTree.get(i).toString()+"Index: "+index);
                return amTree.get(i);
            }
        }
        
        throw new RuntimeException("RuleTree TreeModel: this should never happen!");
    } 
 
    public int getChildCount(Object parent){ 
        RuleTreeItem node = (RuleTreeItem)parent; 
        int counter = 0;
        int parentIndex = amTree.indexOf(node);
        
        for(int i=parentIndex; i<amTree.size(); i++) {
            if(isChild(node,amTree.get(i)))
                counter++;
        }
        //System.out.println("node: "+node.toString()+" Count: "+counter);
        return counter;
    } 
 
    public boolean isLeaf(Object node){ 
        RuleTreeItem leafNode = (RuleTreeItem)node; 
        int leafIndex = amTree.indexOf(leafNode);
        if(leafIndex+1 >= amTree.size())
            return true;
        else if (leafNode.getAncestorsSize() >= amTree.get(leafIndex+1).getAncestorsSize()) {
            //System.out.println("leafNode = "+leafNode.toString()+
            //        "\nleafNode.getAncestorsSize() = "+leafNode.getAncestorsSize()+
            //        "\namTree.get(leafIndex+1).getAncestorsSize() = "+amTree.get(leafIndex+1).getAncestorsSize()
            //        );
            return true;
        }
        else
            return false;
        
    } 
 
    public int getIndexOfChild(Object parent, Object child){ 
        if(parent == null || child == null)
            return -1;
        
        RuleTreeItem node = (RuleTreeItem)parent; 
        RuleTreeItem childNode = (RuleTreeItem)child; 
        int indexCounter = -1;
        int parentIndex = amTree.indexOf(node);
        int childIndex = amTree.indexOf(childNode);
        
        for(int i=parentIndex; i<=childIndex; i++)
            if(isChild(node,amTree.get(i)))
                indexCounter++;
        //System.out.println("Parent: "+node.toString()+
        //        "\nChild: "+childNode.toString()+
        //        "\nIndex: "+indexCounter);
        return indexCounter;
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
    
    private boolean isChild(RuleTreeItem parentItem, RuleTreeItem currentItem) {
        int[] parentAncestors = parentItem.getAncestors();
        int[] currentAncestors = currentItem.getAncestors();
        int parentIndex = amTree.indexOf(parentItem);
        
        if(currentAncestors.length != parentAncestors.length + 1)
            return false;
        
        for(int i=0; i<parentAncestors.length; i++)
            if(parentAncestors[i] != currentAncestors[i])
                return false;
        
        if(currentAncestors[currentAncestors.length-1] != parentIndex)
            return false;
        else
            return true;
    }
    
    private boolean isParent(RuleTreeItem currentItem, RuleTreeItem parentItem) {
        int[] parentAncestors = parentItem.getAncestors();
        int[] currentAncestors = currentItem.getAncestors();
        int parentIndex = amTree.indexOf(parentItem);
        
        if(currentAncestors.length -1 != parentAncestors.length)
            return false;
        
        for(int i=0; i<parentAncestors.length; i++)
            if(parentAncestors[i] != currentAncestors[i])
                return false;
        
        return true;
    }
}


