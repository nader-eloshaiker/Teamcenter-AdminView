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

import tcav.procedure.plmxmlpdm.TagTypeEnum;
import tcav.procedure.plmxmlpdm.base.IdBase;
import javax.swing.tree.*;
import javax.swing.*;
import java.awt.*;
import tcav.procedure.plmxmlpdm.type.AssociatedDataSetType;
import tcav.procedure.plmxmlpdm.type.AssociatedFolderType;
import tcav.procedure.plmxmlpdm.type.AssociatedFormType;
import tcav.procedure.plmxmlpdm.type.OrganisationType;
import tcav.procedure.plmxmlpdm.type.RoleType;
import tcav.procedure.plmxmlpdm.type.SiteType;
import tcav.procedure.plmxmlpdm.type.UserDataType;
import tcav.procedure.plmxmlpdm.type.ValidationResultsType;
import tcav.procedure.plmxmlpdm.type.WorkflowActionType;
import tcav.procedure.plmxmlpdm.type.WorkflowBusinessRuleHandlerType;
import tcav.procedure.plmxmlpdm.type.WorkflowHandlerType;
import tcav.procedure.plmxmlpdm.type.WorkflowTemplateType;
import tcav.procedure.plmxmlpdm.type.element.UserDataElementType;
import tcav.procedure.plmxmlpdm.type.element.ValidationCheckerType;
/**
 *
 * @author nzr4dl
 */
public class TagTreeCellRenderer  implements TreeCellRenderer {
    
    //Workflow Template
    protected static ImageIcon acknowledgeTaskIcon;
    protected static ImageIcon addStatusTaskIcon;
    protected static ImageIcon checkListTaskIcon;
    protected static ImageIcon conditionTaskIcon;
    protected static ImageIcon doTaskIcon;
    protected static ImageIcon impactAnalysisTaskIcon;
    protected static ImageIcon notifyTaskIcon;
    protected static ImageIcon orTaskIcon;
    protected static ImageIcon performSignoffTaskIcon;
    protected static ImageIcon prepareecoTaskIcon;
    protected static ImageIcon processIcon;
    protected static ImageIcon reviewProcess;
    protected static ImageIcon reviewTaskIcon;
    protected static ImageIcon routeTaskIcon;
    protected static ImageIcon selectSignoffTaskIcon;
    protected static ImageIcon syncTaskIcon;
    protected static ImageIcon taskIcon;
    protected static ImageIcon taskPropertiesIcon;
    
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
            acknowledgeTaskIcon = new ImageIcon(ResourceLocator.getWorkflowImage("acknowledgeTask.gif"));
            addStatusTaskIcon = new ImageIcon(ResourceLocator.getWorkflowImage("acknowledgeTask.gif"));
            checkListTaskIcon = new ImageIcon(ResourceLocator.getWorkflowImage("checkListTask.gif"));
            conditionTaskIcon = new ImageIcon(ResourceLocator.getWorkflowImage("conditionTask.gif"));
            doTaskIcon = new ImageIcon(ResourceLocator.getWorkflowImage("doTask.gif"));
            notifyTaskIcon = new ImageIcon(ResourceLocator.getWorkflowImage("notifyTask.gif"));
            orTaskIcon = new ImageIcon(ResourceLocator.getWorkflowImage("orTask.gif"));
            impactAnalysisTaskIcon = new ImageIcon(ResourceLocator.getWorkflowImage("impactAnalysisTask.gif"));
            performSignoffTaskIcon = new ImageIcon(ResourceLocator.getWorkflowImage("performSignoffTask.gif"));
            prepareecoTaskIcon = new ImageIcon(ResourceLocator.getWorkflowImage("prepareecoTask.gif"));
            processIcon = new ImageIcon(ResourceLocator.getWorkflowImage("process.gif"));
            reviewProcess = new ImageIcon(ResourceLocator.getWorkflowImage("reviewProcess.gif"));
            reviewTaskIcon = new ImageIcon(ResourceLocator.getWorkflowImage("reviewTask.gif"));
            routeTaskIcon = new ImageIcon(ResourceLocator.getWorkflowImage("routeTask.gif"));
            selectSignoffTaskIcon = new ImageIcon(ResourceLocator.getWorkflowImage("selectSignoffTask.gif"));
            syncTaskIcon = new ImageIcon(ResourceLocator.getWorkflowImage("syncTask.gif"));
            taskIcon = new ImageIcon(ResourceLocator.getWorkflowImage("task.gif"));
            taskPropertiesIcon = new ImageIcon(ResourceLocator.getWorkflowImage("taskProperties.gif"));
            
            siteIcon = new ImageIcon(ResourceLocator.getProcedureImage("site.gif"));
            workflowHandlerIcon = new ImageIcon(ResourceLocator.getProcedureImage("workflowHandler.gif"));
            workflowActionIcon = new ImageIcon(ResourceLocator.getProcedureImage("workflowAction.gif"));
            businessRuleHandlerIcon = new ImageIcon(ResourceLocator.getProcedureImage("businessRuleHandler.gif"));
            businessRuleIcon = new ImageIcon(ResourceLocator.getProcedureImage("businessRule.gif"));
            workflowIcon = new ImageIcon(ResourceLocator.getProcedureImage("workflow.gif"));

            role = new ImageIcon(ResourceLocator.getProcedureImage("role.gif"));
            argument = new ImageIcon(ResourceLocator.getProcedureImage("argument.gif"));
            associatedDataSet = new ImageIcon(ResourceLocator.getProcedureImage("associatedDataSet.gif"));
            associatedFolder = new ImageIcon(ResourceLocator.getProcedureImage("associatedFolder.gif"));
            associatedForm = new ImageIcon(ResourceLocator.getProcedureImage("associatedForm.gif"));
            userData = new ImageIcon(ResourceLocator.getProcedureImage("userData.gif"));
            userValue= new ImageIcon(ResourceLocator.getProcedureImage("userValue.gif"));
            validationResult = new ImageIcon(ResourceLocator.getProcedureImage("validationResults.gif"));
            validationChecker = new ImageIcon(ResourceLocator.getProcedureImage("validationChecker.gif"));
            signoffProfile = new ImageIcon(ResourceLocator.getProcedureImage("signoffProfile.gif"));
            organisation = new ImageIcon(ResourceLocator.getProcedureImage("organisation.gif"));
            
        } catch (Exception e) {
            System.out.println("Couldn't load images: " + e);
        }
    }
    
    /**
     * Creates a new instance of ProcedureTreeCellRenderer
     */
    public TagTreeCellRenderer() {
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
                String s = wt.getIconKey();
                if(s == null)
                    cell.setIcon(workflowIcon);
                else if(s.equals("acknowledgeTask"))
                    cell.setIcon(acknowledgeTaskIcon);
                else if(s.equals("addStatusTask"))
                    cell.setIcon(addStatusTaskIcon);
                else if(s.equals("checkListTask"))
                    cell.setIcon(checkListTaskIcon);
                else if(s.equals("conditionTask"))
                    cell.setIcon(conditionTaskIcon);
                else if(s.equals("doTask"))
                    cell.setIcon(doTaskIcon);
                else if(s.equals("impactAnalysisTask"))
                    cell.setIcon(impactAnalysisTaskIcon);
                else if(s.equals("notifyTask"))
                    cell.setIcon(notifyTaskIcon);
                else if(s.equals("orTask"))
                    cell.setIcon(orTaskIcon);
                else if(s.equals("performSignoffTask"))
                    cell.setIcon(performSignoffTaskIcon);
                else if(s.equals("prepareecoTask"))
                    cell.setIcon(prepareecoTaskIcon);
                else if(s.equals("process"))
                    cell.setIcon(processIcon);
                else if(s.equals("reviewProcess"))
                    cell.setIcon(reviewProcess);
                else if(s.equals("reviewTask"))
                    cell.setIcon(reviewTaskIcon);
                else if(s.equals("routeTask"))
                    cell.setIcon(routeTaskIcon);
                else if(s.equals("selectSignoffTask"))
                    cell.setIcon(selectSignoffTaskIcon);
                else if(s.equals("syncTask"))
                    cell.setIcon(syncTaskIcon);
                else if(s.equals("task"))
                    cell.setIcon(taskIcon);
                else if(s.equals("taskProperties"))
                    cell.setIcon(taskPropertiesIcon);
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
        
        return cell;
    }
}
