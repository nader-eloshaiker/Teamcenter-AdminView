/*
 * ProcedureTreeCellRenderer.java
 *
 * Created on 6 August 2007, 16:55
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tcav.gui.procedure;

import tcav.manager.procedure.plmxmlpdm.type.AssociatedDataSetType;
import tcav.manager.procedure.plmxmlpdm.type.AssociatedFolderType;
import tcav.manager.procedure.plmxmlpdm.type.AssociatedFormType;
import tcav.manager.procedure.plmxmlpdm.type.OrganisationType;
import tcav.manager.procedure.plmxmlpdm.type.RoleType;
import tcav.manager.procedure.plmxmlpdm.type.SiteType;
import tcav.manager.procedure.plmxmlpdm.type.UserDataType;
import tcav.manager.procedure.plmxmlpdm.type.ValidationResultsType;
import tcav.manager.procedure.plmxmlpdm.type.WorkflowActionType;
import tcav.manager.procedure.plmxmlpdm.type.WorkflowBusinessRuleType;
import tcav.manager.procedure.plmxmlpdm.type.WorkflowBusinessRuleHandlerType;
import tcav.manager.procedure.plmxmlpdm.type.WorkflowHandlerType;
import tcav.manager.procedure.plmxmlpdm.type.WorkflowTemplateType;
import tcav.resources.*;
import tcav.manager.procedure.plmxmlpdm.TagTypeEnum;
import tcav.manager.procedure.plmxmlpdm.base.IdBase;
import javax.swing.tree.*;
import javax.swing.*;
import java.awt.*;
import tcav.manager.procedure.plmxmlpdm.type.element.UserDataElementType;
import tcav.manager.procedure.plmxmlpdm.type.element.ValidationCheckerType;
/**
 *
 * @author nzr4dl
 */
public class AttributelRenderer  implements TreeCellRenderer {
    
    
    //Class Type
    protected static ImageIcon siteIcon;
    protected static ImageIcon workflowHandlerIcon;
    protected static ImageIcon workflowActionIcon;
    protected static ImageIcon businessRuleHandlerIcon;
    protected static ImageIcon businessRuleIcon;
    protected static ImageIcon workflowIcon;

    protected static ImageIcon argument;
    protected static ImageIcon associatedDataSet;
    protected static ImageIcon associatedFolder;
    protected static ImageIcon associatedForm;
    protected static ImageIcon userData;
    protected static ImageIcon userValue;
    protected static ImageIcon organisation;
    protected static ImageIcon validationResult;
    protected static ImageIcon validationChecker;
    protected static ImageIcon signoffProfile;
    protected static ImageIcon role;
    
    static {
        
        
        try {
            siteIcon = ResourceLoader.getImage(ImageEnum.pmSite);
            workflowHandlerIcon = ResourceLoader.getImage(ImageEnum.pmWorkflowHandler);
            workflowActionIcon = ResourceLoader.getImage(ImageEnum.pmWorkflowAction);
            businessRuleHandlerIcon = ResourceLoader.getImage(ImageEnum.pmBusinessRuleHandler);
            businessRuleIcon = ResourceLoader.getImage(ImageEnum.pmBusinessRule);
            workflowIcon = ResourceLoader.getImage(ImageEnum.pmWorkflow);

            role = ResourceLoader.getImage(ImageEnum.pmRole);
            argument = ResourceLoader.getImage(ImageEnum.pmArgument);
            associatedDataSet = ResourceLoader.getImage(ImageEnum.pmAssociatedDataSet);
            associatedFolder = ResourceLoader.getImage(ImageEnum.pmAssociatedFolder);
            associatedForm = ResourceLoader.getImage(ImageEnum.pmAssociatedForm);
            userData = ResourceLoader.getImage(ImageEnum.pmUserData);
            userValue= ResourceLoader.getImage(ImageEnum.pmUserValue);
            validationResult = ResourceLoader.getImage(ImageEnum.pmValidationResults);
            validationChecker = ResourceLoader.getImage(ImageEnum.pmValidationChecker);
            signoffProfile = ResourceLoader.getImage(ImageEnum.pmSignOffProfile);
            organisation = ResourceLoader.getImage(ImageEnum.pmOrganisation);
            
        } catch (Exception e) {
            System.out.println("Couldn't load images: " + e);
        }
    }
    
    /**
     * Creates a new instance of ProcedureTreeCellRenderer
     */
    public AttributelRenderer() {
    }
    
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean isSelected, boolean expanded, boolean leaf, int row, boolean hasFocus){
        
        DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
        DefaultTreeCellRenderer cell = (DefaultTreeCellRenderer)renderer.getTreeCellRendererComponent(tree, value, isSelected, expanded, leaf, row, hasFocus);
        
        if(value == null)
            return cell;

        UserDataType ud;

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
                WorkflowBusinessRuleType wbr = (WorkflowBusinessRuleType)value;
                cell.setText("Quorum Rule: "+wbr.getRuleQuorum());
                cell.setToolTipText("Quorum Rule: "+wbr.getRuleQuorum());
                cell.setIcon(businessRuleIcon);
                break;
                
            case WorkflowAction:
                WorkflowActionType wa = (WorkflowActionType)value;
                cell.setText("Action: "+wa.getType().getName());
                cell.setToolTipText("Action: "+wa.getType().getName());
                cell.setIcon(workflowActionIcon);
                break;

            
            case UserData:
                ud = (UserDataType)value;
                cell.setText(ud.getType());
                cell.setToolTipText(ud.getType());
                cell.setIcon(userData);
                break;
                
            case Arguments:
                ud = (UserDataType)value;
                cell.setText(ud.getType());
                cell.setToolTipText(ud.getType());
                cell.setIcon(argument);
                break;
                
            case AssociatedDataSet:
                AssociatedDataSetType ad = (AssociatedDataSetType)value;
                cell.setText(ad.getRole());
                cell.setToolTipText(ad.getRole());
                cell.setIcon(associatedDataSet);
                break;
                
            case AssociatedFolder:
                AssociatedFolderType af = (AssociatedFolderType)value;
                cell.setText(af.getRole());
                cell.setToolTipText(af.getRole());
                cell.setIcon(associatedFolder);
                break;
                
            case AssociatedForm:
                AssociatedFormType afm = (AssociatedFormType)value;
                cell.setText(afm.getRole());
                cell.setToolTipText(afm.getRole());
                cell.setIcon(associatedForm);
                break;
                
            case ValidationResults:
                ValidationResultsType vr = (ValidationResultsType)value;
                cell.setText(vr.getApplication());
                cell.setToolTipText(vr.getApplication());
                cell.setIcon(validationResult);
                break;
                
                
            case Organisation:
                OrganisationType o = (OrganisationType)value;
                cell.setText(o.getName());
                cell.setToolTipText(o.getName());
                cell.setIcon(organisation);
                break;
                
            case Role:
                RoleType r = (RoleType)value;
                cell.setText(r.getName());
                cell.setToolTipText(r.getName());
                cell.setIcon(role);
                break;
                
            case UserValue:
                UserDataElementType udv = (UserDataElementType)value;
                cell.setText(udv.getTitle()+": "+udv.getValue());
                cell.setToolTipText(udv.getTitle()+": "+udv.getValue());
                cell.setIcon(userValue);
                break;
                
            case Checker:
                ValidationCheckerType vc = (ValidationCheckerType)value;
                cell.setText(vc.getName());
                cell.setToolTipText(vc.getName());
                cell.setIcon(validationResult);
                break;
                
            case WorkflowSignoffProfile:
                cell.setText("Signoff Profile");
                cell.setToolTipText("Signoff Profile");
                cell.setIcon(signoffProfile);
                break;
                
            default:
                cell.setText(value.toString());
                cell.setToolTipText(value.toString());
                cell.setIcon(workflowIcon);
                break;
                
        }
        
        if (!leaf)
            cell.setFont(cell.getFont().deriveFont(Font.BOLD));
        
        return cell;
    }
}
