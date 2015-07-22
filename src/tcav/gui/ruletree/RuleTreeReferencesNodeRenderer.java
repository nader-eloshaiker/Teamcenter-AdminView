/*
 * RuleTreeReferencesNodeRenderer.java
 *
 * Created on 18 July 2007, 16:23
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tcav.gui.ruletree;

import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;
import tcav.ruletree.AccessManagerItem;
import tcav.ResourceLocator;

/**
 *
 * @author nzr4dl
 */
public class RuleTreeReferencesNodeRenderer implements TreeCellRenderer{
    
    static protected ImageIcon ruleIcon;
    
    static
    {
        try {
            ruleIcon = new ImageIcon(ResourceLocator.getRultreeImage("Rule.gif"));
        } catch (Exception e) {
            System.out.println("Couldn't load images: " + e);
        }
    }
    
    /**
     * This is messaged from JTree whenever it needs to get the size
     * of the component or it wants to draw it.
     * This attempts to set the font based on value, which will be
     * a TreeNode.
     */
    public Component getTreeCellRendererComponent(JTree tree, Object value,
            boolean isSelected, boolean expanded,
            boolean leaf, int row,
            boolean hasFocus) {
        
        DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
        DefaultTreeCellRenderer cell = (DefaultTreeCellRenderer)renderer.getTreeCellRendererComponent(tree, value, isSelected, expanded, leaf, row, hasFocus);
        
        if(value == null)
            return cell;
        
        
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
        
        String s = (String)node.getUserObject();
        
        cell.setText(s);
        cell.setToolTipText(s);
        
        if (leaf) {
            cell.setIcon(ruleIcon);
        }
        
        return cell;
    }
}
