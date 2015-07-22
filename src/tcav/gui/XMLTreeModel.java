/*
 * XMLTreeModel.java
 *
 * Created on 20 July 2007, 10:48
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tcav.gui;

import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.event.*;
import java.io.*;
import javax.xml.*;
import org.xml.sax.*;
import org.w3c.dom.*;
import tcav.xml.DOMUtil;

/**
 *
 * @author nzr4dl
 */
public class XMLTreeModel implements TreeModel{ 
    private Node node; 
    
    public XMLTreeModel(Node node){ 
        this.node = node; 
    } 
 
    public Object getRoot(){ 
        return node; 
    } 
 
    public Object getChild(Object parent, int index){ 
        Node node = (Node)parent; 
        NamedNodeMap attrs = node.getAttributes(); 
        int attrCount = attrs!=null ? attrs.getLength() : 0; 
        if(index<attrCount) 
            return attrs.item(index); 
        NodeList children = node.getChildNodes(); 
        return children.item(index - attrCount); 
    } 
 
    public int getChildCount(Object parent){ 
        Node node = (Node)parent; 
        NamedNodeMap attrs = node.getAttributes(); 
        int attrCount = attrs!=null ? attrs.getLength() : 0; 
        NodeList children = node.getChildNodes(); 
        int childCount = children!=null ? children.getLength() : 0; 
        return attrCount + childCount; 
    } 
 
    public boolean isLeaf(Object node){ 
        return getChildCount(node)==0; 
    } 
 
    public int getIndexOfChild(Object parent, Object child){ 
        Node node = (Node)parent; 
        Node childNode = (Node)child; 
 
        NamedNodeMap attrs = node.getAttributes(); 
        int attrCount = attrs!=null ? attrs.getLength() : 0; 
        if(childNode.getNodeType()==Node.ATTRIBUTE_NODE){ 
            for(int i=0; i<attrCount; i++){ 
                if(attrs.item(i)==child) 
                    return i; 
            } 
        }else{ 
            NodeList children = node.getChildNodes(); 
            int childCount = children!=null ? children.getLength() : 0; 
            for(int i=0; i<childCount; i++){ 
                if(children.item(i)==child) 
                    return attrCount + i; 
            } 
        } 
        throw new RuntimeException("this should never happen!"); 
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


