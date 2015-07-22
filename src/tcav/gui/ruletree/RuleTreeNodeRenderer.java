package tcav.gui.ruletree;
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
import tcav.ruletree.RuleTreeItem;
import tcav.ResourceLocator;

public class RuleTreeNodeRenderer implements TreeCellRenderer {
    
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
        
        RuleTreeItem amItem = (RuleTreeItem)value;
        
        cell.setText(amItem.toString());
        cell.setToolTipText(amItem.toString());
        if(amItem.getParamters() != null)
            for(int i=0; i<amItem.getParamters().length; i++)
                if(amItem.getParamters()[i].equals("Protected")){
                    cell.setBackgroundNonSelectionColor(new Color(255,0,0));
                    cell.setBorderSelectionColor(new Color(255,0,0));
                }
        if (leaf) {
            cell.setIcon(ruleIcon);
        }
        return cell;
    }
}
