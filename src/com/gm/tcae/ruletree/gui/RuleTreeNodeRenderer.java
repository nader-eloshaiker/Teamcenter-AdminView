/*
 * RuleTreeNodeRenderer.java
 *
 * Created on 26 June 2007, 15:56
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.gm.tcae.ruletree.gui;

/**
 *
 * @author NZR4DL
 */
import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;
import com.gm.tcae.ruletree.acl.AccessManagerItem;

public class RuleTreeNodeRenderer extends DefaultTreeCellRenderer implements TreeCellRenderer{
    
    static protected ImageIcon ruleIcon;
    static protected ImageIcon rootIcon;
    
    
    static
    {
        try {
            ruleIcon = new ImageIcon(RuleTreeNodeRenderer.class.getResource("images/acl/rule.gif"));
            rootIcon = new ImageIcon(RuleTreeNodeRenderer.class.getResource("images/acl/ruletree.gif"));
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
        
        super.selected = isSelected;
        super.hasFocus = hasFocus;
        
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
        
        if(node == null)
            return this;
        
        AccessManagerItem amItem = (AccessManagerItem)node.getUserObject();
        
        setText(amItem.toString());
        setToolTipText(amItem.toString());
        
        if(node.isRoot())
            setIcon(rootIcon);
        else if(leaf) {
            setIcon(ruleIcon);
        }
        
	if(isSelected)
	    setForeground(getTextSelectionColor());
	else
	    setForeground(getTextNonSelectionColor());
	// There needs to be a way to specify disabled icons.
	if (!tree.isEnabled()) {
	    setEnabled(false);
	    if (leaf) {
		setDisabledIcon(ruleIcon);//getLeafIcon());
	    } else if (expanded) {
		setDisabledIcon(getOpenIcon());
	    } else {
		setDisabledIcon(getClosedIcon());
	    }
	}
	else {
	    setEnabled(true);
	    if (leaf) {
		setIcon(ruleIcon);//getLeafIcon());
	    } else if (expanded) {
		setIcon(getOpenIcon());
	    } else {
		setIcon(getClosedIcon());
	    }
	}
        setComponentOrientation(tree.getComponentOrientation());
        
        return this;
    }
}
