/*
 * ProcedureTreeCellRenderer.java
 *
 * Created on 6 August 2007, 16:55
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tceav.gui.procedure;

import tceav.manager.procedure.plmxmlpdm.type.SiteType;
import tceav.manager.procedure.plmxmlpdm.type.WorkflowActionType;
import tceav.manager.procedure.plmxmlpdm.type.WorkflowBusinessRuleType;
import tceav.manager.procedure.plmxmlpdm.type.WorkflowBusinessRuleHandlerType;
import tceav.manager.procedure.plmxmlpdm.type.WorkflowHandlerType;
import tceav.manager.procedure.plmxmlpdm.type.WorkflowTemplateType;
import tceav.manager.procedure.plmxmlpdm.base.IdBase;
import javax.swing.tree.*;
import javax.swing.*;
import java.awt.*;
import tceav.manager.procedure.plmxmlpdm.type.element.UserDataElementType;
import tceav.resources.*;

/**
 *
 * @author nzr4dl
 */
public class HandlerRenderer  implements TreeCellRenderer {
    
    //Class Type
    protected static ImageIcon siteIcon;
    protected static ImageIcon workflowHandlerIcon;
    protected static ImageIcon workflowActionIcon;
    protected static ImageIcon businessRuleHandlerIcon;
    protected static ImageIcon businessRuleIcon;
    protected static ImageIcon workflowIcon;
    protected static ImageIcon userValue;

    static {
        
        
        try {
            siteIcon = ResourceLoader.getImage(ImageEnum.pmSite);
            workflowHandlerIcon = ResourceLoader.getImage(ImageEnum.pmWorkflowHandler);
            workflowActionIcon = ResourceLoader.getImage(ImageEnum.pmWorkflowAction);
            businessRuleHandlerIcon = ResourceLoader.getImage(ImageEnum.pmBusinessRuleHandler);
            businessRuleIcon = ResourceLoader.getImage(ImageEnum.pmBusinessRule);
            workflowIcon = ResourceLoader.getImage(ImageEnum.pmWorkflow);
            userValue= ResourceLoader.getImage(ImageEnum.pmUserValue);
            
        } catch (Exception e) {
            System.out.println("Couldn't load images: " + e);
        }
    }
    
    /**
     * Creates a new instance of ProcedureTreeCellRenderer
     */
    public HandlerRenderer() {
    }
    
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean isSelected, boolean expanded, boolean leaf, int row, boolean hasFocus){
        
        DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
        DefaultTreeCellRenderer cell = (DefaultTreeCellRenderer)renderer.getTreeCellRendererComponent(tree, value, isSelected, expanded, leaf, row, hasFocus);
        
        if(value == null)
            return cell;

        switch(((IdBase)value).getTagType()) {
            case Site:
                SiteType site = (SiteType)value;
                cell.setText(site.getName());
                cell.setToolTipText(site.getName());
                cell.setIcon(siteIcon);
                break;
                
            case WorkflowTemplate:
                WorkflowTemplateType wt = (WorkflowTemplateType)value;
                cell.setText(wt.getName());
                cell.setToolTipText(wt.getName());
                cell.setIcon(workflowIcon);
                break;
                
            case WorkflowHandler:
                WorkflowHandlerType wh = (WorkflowHandlerType)value;
                cell.setText(wh.getName());
                cell.setToolTipText(wh.getName());
                cell.setIcon(workflowHandlerIcon);
                cell.setFont(cell.getFont().deriveFont(Font.BOLD));
                break;
                
            case WorkflowBusinessRuleHandler:
                WorkflowBusinessRuleHandlerType brh = (WorkflowBusinessRuleHandlerType)value;
                cell.setText(brh.getName());
                cell.setToolTipText(brh.getName());
                cell.setIcon(businessRuleHandlerIcon);
                cell.setFont(cell.getFont().deriveFont(Font.BOLD));
                break;
                
            case WorkflowBusinessRule:
                WorkflowBusinessRuleType wbr = (WorkflowBusinessRuleType)value;
                cell.setText("Quorum Rule: "+wbr.getRuleQuorum());
                cell.setToolTipText("Quorum Rule: "+wbr.getRuleQuorum());
                cell.setIcon(businessRuleIcon);
                break;
                
            case WorkflowAction:
                WorkflowActionType wa = (WorkflowActionType)value;
                cell.setText(wa.getType().getName());
                cell.setToolTipText(wa.getType().getName());
                cell.setIcon(workflowActionIcon);
                //cell.setFont(cell.getFont().deriveFont(Font.BOLD));
                break;
            
            case UserValue:
                UserDataElementType udv = (UserDataElementType)value;
                cell.setText(udv.getValue());
                cell.setToolTipText(udv.getValue());
                cell.setIcon(userValue);
                break;
                
            default:
                cell.setText(value.toString());
                cell.setToolTipText(value.toString());
                cell.setIcon(workflowIcon);
                break;
        }
        /*
        if (!leaf)
            cell.setFont(cell.getFont().deriveFont(Font.BOLD));
        */
        return cell;
    }
}
