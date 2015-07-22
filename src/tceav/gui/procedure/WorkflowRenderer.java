/*
 * ProcedureTreeCellRenderer.java
 *
 * Created on 6 August 2007, 16:55
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tceav.gui.procedure;

import java.awt.Component;
import java.awt.Font;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;

/**
 *
 * @author nzr4dl
 */
public class WorkflowRenderer  implements TreeCellRenderer {
    
    /**
     * Creates a new instance of ProcedureTreeCellRenderer
     */
    public WorkflowRenderer() {
    }
    
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean isSelected, boolean expanded, boolean leaf, int row, boolean hasFocus){
        
        DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
        DefaultTreeCellRenderer cell = (DefaultTreeCellRenderer)renderer.getTreeCellRendererComponent(tree, value, isSelected, expanded, leaf, row, hasFocus);
        
        if(value == null)
            return cell;

        TaskProperties element = TaskProperties.getProcessElement(value, cell.getFont());
        cell.setText(element.getName());
        cell.setToolTipText(element.getToolTip());
        cell.setIcon(element.getIcon());
        /*
        if(element.hasFont())
            cell.setFont(element.getFont());
        */
        if (!leaf)
            cell.setFont(cell.getFont().deriveFont(Font.BOLD));
        
        return cell;
    }
}
