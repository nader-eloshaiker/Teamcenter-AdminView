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
import tcav.manager.access.RuleTreeNode;
import tcav.resources.*;

public class RuleTreeNodeRenderer implements TreeCellRenderer {
    
    static protected ImageIcon ruleIcon;
    static protected ImageIcon ruleBranchIcon;
    
    static
    {
        try {
            ruleIcon = ResourceLoader.getImage(ImageEnum.amRule);
            ruleBranchIcon = ResourceLoader.getImage(ImageEnum.amRuleBranch);
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
        
        //DefaultTreeCellRenderer cell =  (DefaultTreeCellRenderer)super.getTreeCellRendererComponent(tree, value, isSelected, expanded, leaf, row, hasFocus);
        DefaultTreeCellRenderer cell = (DefaultTreeCellRenderer)new DefaultTreeCellRenderer().getTreeCellRendererComponent(tree, value, isSelected, expanded, leaf, row, hasFocus);
        
        if(value == null)
            return cell;
        
        RuleTreeNode amItem = (RuleTreeNode)value;
        
        cell.setText(amItem.toString());
        cell.setToolTipText(amItem.toString());
        
        if(amItem.getParamters() != null)
            for(int i=0; i<amItem.getParamters().length; i++)
                if(!amItem.getParamters()[i].equals("Collapsed"))
                    cell.setText(cell.getText()+" ["+amItem.getParamters()[i]+"]");
        
        if (leaf)
            cell.setIcon(ruleIcon);
        else {
            cell.setIcon(ruleBranchIcon);
            cell.setFont(cell.getFont().deriveFont(Font.BOLD));
        }
        
        //cell.setBackgroundNonSelectionColor(new Color(255,0,0));
        //cell.setBorderSelectionColor(new Color(255,0,0));
        
        
        return cell;
    }
}
