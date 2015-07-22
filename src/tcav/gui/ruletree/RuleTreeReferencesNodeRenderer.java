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
public class RuleTreeReferencesNodeRenderer extends DefaultTreeCellRenderer implements TreeCellRenderer{
    
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
        
        super.selected = isSelected;
        super.hasFocus = hasFocus;
        
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
        
        if(node == null)
            return this;
        
        String s = (String)node.getUserObject();
        this.setLeafIcon(ruleIcon);
        
        setText(s);
        setToolTipText(s);
        
	if(isSelected)
	    setForeground(getTextSelectionColor());
	else
	    setForeground(getTextNonSelectionColor());
	// There needs to be a way to specify disabled icons.
	if (!tree.isEnabled()) {
	    setEnabled(false);
	    if (leaf) {
		setDisabledIcon(getLeafIcon());
	    } else if (expanded) {
		setDisabledIcon(getOpenIcon());
	    } else {
		setDisabledIcon(getClosedIcon());
	    }
	}
	else {
	    setEnabled(true);
	    if (leaf) {
		setIcon(getLeafIcon());
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
