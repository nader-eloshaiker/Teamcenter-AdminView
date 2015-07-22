/*
 * RuleTreeModel.java
 *
 * Created on 28 July 2007, 08:46
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tcadminview.gui.ruletree;

import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.event.*;
import java.io.*;
import javax.xml.*;
import org.xml.sax.*;
import org.w3c.dom.*;
import tcadminview.xml.DOMUtil;
import tcadminview.ruletree.AccessManagerTree;
import tcadminview.ruletree.AccessManagerItem;


/**
 *
 * @author NZR4DL
 */
public class RuleTreeModel implements TreeModel{
    
    private AccessManagerTree amTree;
    
    /** Creates a new instance of RuleTreeModel */
    public RuleTreeModel(AccessManagerTree amTree) {
        this.amTree = amTree;
    }
    
 
    public Object getRoot(){ 
        if(amTree.size() == 0)
            return null;
        else
            return amTree.get(0); 
    } 
 
    public Object getChild(Object parent, int index){ 
        AccessManagerItem node = (AccessManagerItem)parent; 
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
        AccessManagerItem node = (AccessManagerItem)parent; 
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
        AccessManagerItem leafNode = (AccessManagerItem)node; 
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
        
        AccessManagerItem node = (AccessManagerItem)parent; 
        AccessManagerItem childNode = (AccessManagerItem)child; 
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
    
    private boolean isChild(AccessManagerItem parentItem, AccessManagerItem currentItem) {
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
    
    private boolean isParent(AccessManagerItem currentItem, AccessManagerItem parentItem) {
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


