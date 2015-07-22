/*
 * ProcedureTreeCellRenderer.java
 *
 * Created on 6 August 2007, 16:55
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tcav.gui.procedure;

import tcav.ResourceLocator;

import tcav.plmxmlpdm.TagTypeEnum;
import tcav.plmxmlpdm.base.IdBase;
import tcav.plmxmlpdm.type.*;
import tcav.procedure.*;
import javax.swing.tree.*;
import javax.swing.*;
import java.awt.*;
/**
 *
 * @author nzr4dl
 */
public class ActionTreeCellRenderer  implements TreeCellRenderer {
    
    //Class Type
    protected static ImageIcon siteIcon;
    protected static ImageIcon workflowHandlerIcon;
    protected static ImageIcon workflowActionIcon;
    protected static ImageIcon businessRuleHandlerIcon;
    protected static ImageIcon businessRuleIcon;
    protected static ImageIcon workflowIcon;
    
    static {
        
        try {
            siteIcon = new ImageIcon(ResourceLocator.getProcedureImage("site.gif"));
            workflowHandlerIcon = new ImageIcon(ResourceLocator.getProcedureImage("workflowHandler.gif"));
            workflowActionIcon = new ImageIcon(ResourceLocator.getProcedureImage("workflowAction.gif"));
            businessRuleHandlerIcon = new ImageIcon(ResourceLocator.getProcedureImage("businessRuleHandler.gif"));
            businessRuleIcon = new ImageIcon(ResourceLocator.getProcedureImage("businessRule.gif"));
            workflowIcon = new ImageIcon(ResourceLocator.getProcedureImage("workflow.gif"));
            
        } catch (Exception e) {
            System.out.println("Couldn't load images: " + e);
        }
    }
    
    /**
     * Creates a new instance of ProcedureTreeCellRenderer
     */
    public ActionTreeCellRenderer() {
    }
    
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean isSelected, boolean expanded, boolean leaf, int row, boolean hasFocus){
        
        DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
        DefaultTreeCellRenderer cell = (DefaultTreeCellRenderer)renderer.getTreeCellRendererComponent(tree, value, isSelected, expanded, leaf, row, hasFocus);
        
        if(value == null)
            return cell;

        switch(((IdBase)value).getTagType()) {
            case WorkflowTemplate:
                WorkflowTemplateType wt = (WorkflowTemplateType)value;
                cell.setText(wt.getName());
                cell.setToolTipText(wt.getName());
               cell.setIcon(workflowIcon);
                break;
                
            case Site:
                SiteType site = (SiteType)value;
                cell.setText(site.getName());
                cell.setToolTipText(site.getName());
                cell.setIcon(siteIcon);
                break;
                
            case WorkflowHandler:
                WorkflowHandlerType wh = (WorkflowHandlerType)value;
                cell.setText(wh.getName());
                cell.setToolTipText(wh.getName());
                cell.setIcon(workflowHandlerIcon);
                break;
                
            case WorkflowBusinessRuleHandler:
                WorkflowBusinessRuleHandlerType brh = (WorkflowBusinessRuleHandlerType)value;
                cell.setText(brh.getName());
                cell.setToolTipText(brh.getName());
                cell.setIcon(businessRuleHandlerIcon);
                break;
                
            case WorkflowBusinessRule:
                cell.setText("Business Rule");
                cell.setToolTipText("Business Rule");
                cell.setIcon(businessRuleIcon);
                break;
                
            case WorkflowAction:
                WorkflowActionType wa = (WorkflowActionType)value;
                cell.setText("Action: Type "+wa.getActionType());
                cell.setToolTipText("Action: Type "+wa.getActionType());
                cell.setIcon(workflowActionIcon);
                break;
                
            default:
                cell.setText(value.toString());
                cell.setToolTipText(value.toString());
                break;
        }
        
        return cell;
    }
}
