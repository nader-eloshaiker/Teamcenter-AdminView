package tcadminview.gui.ruletree;
/*
 * RuleTreeNodeRenderer.java
 *
 * Created on 26 June 2007, 15:56
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

/**
 *
 * @author NZR4DL
 */
import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;
import tcadminview.ruletree.AccessManagerItem;
import tcadminview.ResourceLocator;

public class RuleTreeNodeRenderer extends DefaultTreeCellRenderer implements TreeCellRenderer{
    
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
        /*
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
        
        if(node == null)
            return this;
        
        if(node.getUserObject() == null)
            return this;
        
        setLeafIcon(ruleIcon);
        
        AccessManagerItem amItem = (AccessManagerItem)node.getUserObject();
        */
        if(value == null)
            return this;
        
        AccessManagerItem amItem = (AccessManagerItem)value;
        
        setText(amItem.toString());
        setToolTipText(amItem.toString());
        setLeafIcon(ruleIcon);
        
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
