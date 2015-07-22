package tceav.gui.access;
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
import tceav.manager.access.RuleTreeNode;
import tceav.manager.compare.CompareInterface;
import tceav.resources.*;

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
    
    private boolean compareMode;
    
    public RuleTreeNodeRenderer() {
        this(false);
    }
    
    public RuleTreeNodeRenderer(boolean compareMode) {
        this.compareMode = compareMode;
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
        
        if(!compareMode)
            return cell;
        
        
        int result = amItem.getComparison();
        switch(result) {
            case CompareInterface.NOT_EQUAL:
                if (isSelected) {
                    cell.setForeground(CompareInterface.NOT_EQUAL_COLOR);
                    //cell.setBackgroundSelectionColor(CompareInterface.NOT_EQUAL_COLOR_BACKGROUND);
                } else {
                    cell.setBackgroundNonSelectionColor(CompareInterface.NOT_EQUAL_COLOR);
                    //cell.setForeground(tree.getForeground());
                }
                break;
            case CompareInterface.NOT_FOUND:
                if (isSelected) {
                    cell.setForeground(CompareInterface.NOT_FOUND_COLOR);
                    //cell.setBackgroundSelectionColor(CompareInterface.NOT_FOUND_COLOR_BACKGROUND);
                } else {
                    cell.setBackgroundNonSelectionColor(CompareInterface.NOT_FOUND_COLOR);
                    //cell.setForeground(tree.getForeground());
                }
                break;
            case CompareInterface.EQUAL:
            default:
                break;
        }

        
        return cell;
    }
}
