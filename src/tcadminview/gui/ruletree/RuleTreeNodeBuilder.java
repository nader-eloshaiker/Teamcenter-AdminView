/*
 * RuleTreeNodeBuilder.java
 *
 * Created on 20 June 2007, 11:39
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tcadminview.gui.ruletree;
import tcadminview.ruletree.AccessManagerTree;
import tcadminview.ruletree.AccessManagerItem;
import tcadminview.ruletree.AccessRule;
import javax.swing.JTree;
import javax.swing.tree.*;
import java.util.*;

/**
 *
 * @author NZR4DL
 */
public class RuleTreeNodeBuilder {
    
    /*
     * Depreciated as it is nolonger required. The JTree component is now
     * implemented instead of populated.
     */
    public static DefaultMutableTreeNode getRuleTreeNodes(AccessManagerTree amTree) {
        int currentIndent = 0;
        int newIndent = 0;
        int indentVariance = 0;
        DefaultMutableTreeNode newNode;
        DefaultMutableTreeNode currentNode;
        DefaultMutableTreeNode topNode = new DefaultMutableTreeNode(amTree.elementAt(0));
        currentNode = topNode;
        
        for(int index = 1; index<amTree.size(); index++) {
            newIndent = amTree.elementAt(index).getIndentLevel();
            newNode = new DefaultMutableTreeNode(amTree.elementAt(index));
            
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
    
    public static DefaultMutableTreeNode getRuleTreePathsForRule(AccessManagerTree amTree, AccessRule ar) {
        DefaultMutableTreeNode topNode = new DefaultMutableTreeNode(ar.toString());
        DefaultMutableTreeNode currentNode = topNode;
        DefaultMutableTreeNode newNode;
        
        AccessManagerItem amItem;
        AccessManagerItem amItemTemp;
        int[] paths;
        
        for(int i=0; i<ar.getTreeIndexSize(); i++) {
            amItem = amTree.get(ar.getTreeIndexAt(i));
            paths = amItem.getAncestors();
            newNode = new DefaultMutableTreeNode(amItem.toString());
            topNode.add(newNode);
            for(int k=paths.length-1; k>=0; k--) {
                currentNode = newNode;
                amItemTemp = amTree.get(paths[k]);
                newNode = new DefaultMutableTreeNode(amItemTemp.toString());
                currentNode.add(newNode);
            }
        }
        return topNode;
    }
    
    
}
