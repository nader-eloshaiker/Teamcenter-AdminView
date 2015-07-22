/*
 * TreeNodeBuilder.java
 *
 * Created on 20 June 2007, 11:39
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.gm.tcae.ruletree.gui;
import com.gm.tcae.ruletree.acl.*;
import javax.swing.JTree;
import javax.swing.tree.*;
import java.util.*;

/**
 *
 * @author NZR4DL
 */
public class TreeNodeBuilder {
    
    public static DefaultMutableTreeNode getTreeNodes(AccessManagerTree amTree) {
        int currentIndent = 0;
        int newIndent = 0;
        int indentVariance = 0;
        int ruleTreeListIndex;
        DefaultMutableTreeNode newNode;
        DefaultMutableTreeNode currentNode;
        DefaultMutableTreeNode topNode = new DefaultMutableTreeNode(amTree.elementAt(0));
        currentNode = topNode;
        
        for(int index = 1; index<amTree.size(); index++) {
            newIndent = amTree.elementAt(index).getIndentLevel();
            newNode = new DefaultMutableTreeNode(amTree.elementAt(index));
            ruleTreeListIndex = amTree.elementAt(index).getAccessRuleListIndex();
            
            if (newIndent >  currentIndent) {
                currentNode.add(newNode);
                
            } else if (newIndent <  currentIndent) {
                indentVariance = currentIndent - newIndent + 1;
                
                for (int j=0; j<indentVariance; j++) 
                    currentNode = (DefaultMutableTreeNode)currentNode.getParent();
                
                currentNode.add(newNode);
                
            } else if (newIndent ==  currentIndent) {
                ((DefaultMutableTreeNode)currentNode.getParent()).add(newNode);
            }
            
            currentNode = newNode;
            currentIndent = newIndent;
        }
        return topNode;
    }
}
