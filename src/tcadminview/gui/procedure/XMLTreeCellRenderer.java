/*
 * XMLTreeCellRenderer.java
 *
 * Created on 20 July 2007, 11:06
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tcadminview.gui.procedure;

import javax.swing.tree.*;
import javax.swing.*;
import java.awt.*;
import org.w3c.dom.*;

/**
 *
 * @author nzr4dl
 */
public class XMLTreeCellRenderer extends DefaultTreeCellRenderer{ 
    Color elementColor = new Color(0, 0, 128); 
    Color attributeColor = new Color(0, 128, 0); 
 
    public XMLTreeCellRenderer(){ 
        //setOpenIcon(null); 
        //setClosedIcon(null); 
        //setLeafIcon(null); 
    } 
 
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus){ 
        Node node = (Node)value; 
        switch(node.getNodeType()){ 
            case Node.ELEMENT_NODE: 
                value = '<'+node.getNodeName()+'>'; 
                break; 
            case Node.ATTRIBUTE_NODE: 
                value = '@'+node.getNodeName(); 
                break; 
            case Node.TEXT_NODE: 
                value = node.getNodeValue(); 
                break; 
            case Node.COMMENT_NODE: 
                value = "<!--"+node.getNodeValue()+"-->"; 
                break; 
            case Node.DOCUMENT_TYPE_NODE: 
                DocumentType dtype = (DocumentType)node; 
                value = "<"+"!DOCTYPE " + dtype.getName() + '>'; 
                break; 
            default: 
                value = node.getNodeName(); 
        } 
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus); 
        if(!selected){ 
            switch(node.getNodeType()){ 
                case Node.ELEMENT_NODE: 
                    setForeground(elementColor); 
                    break; 
                case Node.ATTRIBUTE_NODE: 
                    setForeground(attributeColor); 
                    break; 
            } 
        } 
        return this; 
    } 
} 

