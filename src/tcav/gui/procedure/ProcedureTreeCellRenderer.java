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
import javax.swing.tree.*;
import javax.swing.*;
import java.awt.*;
/**
 *
 * @author nzr4dl
 */
public class ProcedureTreeCellRenderer  implements TreeCellRenderer {
    
    //Item Type
    protected static Color COLOR_COLLECTOR;
    
    //Workflow Template
    protected static ImageIcon acknowledgeTask;
    protected static ImageIcon addStatusTask;
    protected static ImageIcon checkListTask;
    protected static ImageIcon conditionTask;
    protected static ImageIcon doTask;
    protected static ImageIcon impactAnalysisTask;
    protected static ImageIcon notifyTask;
    protected static ImageIcon orTask;
    protected static ImageIcon performSignoffTask;
    protected static ImageIcon prepareecoTask;
    protected static ImageIcon process;
    protected static ImageIcon reviewProcess;
    protected static ImageIcon reviewTask;
    protected static ImageIcon routeTask;
    protected static ImageIcon selectSignoffTask;
    protected static ImageIcon syncTask;
    protected static ImageIcon task;
    protected static ImageIcon taskProperties;
    
    //Class Type
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
    protected static ImageIcon site;
    protected static ImageIcon workflowHandler;
    protected static ImageIcon workflowAction;
    protected static ImageIcon businessRuleHandler;
    protected static ImageIcon businessRule;
    protected static ImageIcon collector;
    protected static ImageIcon role;
    protected static ImageIcon workflow;
    
    static {
        COLOR_COLLECTOR = new Color(128, 128, 128);
        
        
        try {
            acknowledgeTask = new ImageIcon(ResourceLocator.getWorkflowImage("acknowledgeTask.gif"));
            addStatusTask = new ImageIcon(ResourceLocator.getWorkflowImage("acknowledgeTask.gif"));
            checkListTask = new ImageIcon(ResourceLocator.getWorkflowImage("checkListTask.gif"));
            conditionTask = new ImageIcon(ResourceLocator.getWorkflowImage("conditionTask.gif"));
            doTask = new ImageIcon(ResourceLocator.getWorkflowImage("doTask.gif"));
            notifyTask = new ImageIcon(ResourceLocator.getWorkflowImage("notifyTask.gif"));
            orTask = new ImageIcon(ResourceLocator.getWorkflowImage("orTask.gif"));
            impactAnalysisTask = new ImageIcon(ResourceLocator.getWorkflowImage("impactAnalysisTask.gif"));
            performSignoffTask = new ImageIcon(ResourceLocator.getWorkflowImage("performSignoffTask.gif"));
            prepareecoTask = new ImageIcon(ResourceLocator.getWorkflowImage("prepareecoTask.gif"));
            process = new ImageIcon(ResourceLocator.getWorkflowImage("process.gif"));
            reviewProcess = new ImageIcon(ResourceLocator.getWorkflowImage("reviewProcess.gif"));
            reviewTask = new ImageIcon(ResourceLocator.getWorkflowImage("reviewTask.gif"));
            routeTask = new ImageIcon(ResourceLocator.getWorkflowImage("routeTask.gif"));
            selectSignoffTask = new ImageIcon(ResourceLocator.getWorkflowImage("selectSignoffTask.gif"));
            syncTask = new ImageIcon(ResourceLocator.getWorkflowImage("syncTask.gif"));
            task = new ImageIcon(ResourceLocator.getWorkflowImage("task.gif"));
            taskProperties = new ImageIcon(ResourceLocator.getWorkflowImage("taskProperties.gif"));
            
            site = new ImageIcon(ResourceLocator.getProcedureImage("site.gif"));
            workflowHandler = new ImageIcon(ResourceLocator.getProcedureImage("workflowHandler.gif"));
            workflowAction = new ImageIcon(ResourceLocator.getProcedureImage("workflowAction.gif"));
            businessRuleHandler = new ImageIcon(ResourceLocator.getProcedureImage("businessRuleHandler.gif"));
            businessRule = new ImageIcon(ResourceLocator.getProcedureImage("businessRule.gif"));
            collector = new ImageIcon(ResourceLocator.getProcedureImage("collector.gif"));
            role = new ImageIcon(ResourceLocator.getProcedureImage("role.gif"));
            workflow = new ImageIcon(ResourceLocator.getProcedureImage("workflow.gif"));
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
    public ProcedureTreeCellRenderer() {
    }
    
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean isSelected, boolean expanded, boolean leaf, int row, boolean hasFocus){
        
        DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
        DefaultTreeCellRenderer cell = (DefaultTreeCellRenderer)renderer.getTreeCellRendererComponent(tree, value, isSelected, expanded, leaf, row, hasFocus);
        
        if(value == null)
            return cell;

        NodeReference nr = (NodeReference)value;

        cell.setText(nr.getName());
        cell.setToolTipText(nr.getName());
        
        switch(nr.getClassType()) {
            case WorkflowTemplate:
                String s = nr.getIconKey();
                if(s == null)
                    cell.setIcon(workflow);
                else if(s.equals("acknowledgeTask"))
                    cell.setIcon(acknowledgeTask);
                else if(s.equals("addStatusTask"))
                    cell.setIcon(addStatusTask);
                else if(s.equals("checkListTask"))
                    cell.setIcon(checkListTask);
                else if(s.equals("conditionTask"))
                    cell.setIcon(conditionTask);
                else if(s.equals("doTask"))
                    cell.setIcon(doTask);
                else if(s.equals("impactAnalysisTask"))
                    cell.setIcon(impactAnalysisTask);
                else if(s.equals("notifyTask"))
                    cell.setIcon(notifyTask);
                else if(s.equals("orTask"))
                    cell.setIcon(orTask);
                else if(s.equals("performSignoffTask"))
                    cell.setIcon(performSignoffTask);
                else if(s.equals("prepareecoTask"))
                    cell.setIcon(prepareecoTask);
                else if(s.equals("process"))
                    cell.setIcon(process);
                else if(s.equals("reviewProcess"))
                    cell.setIcon(reviewProcess);
                else if(s.equals("reviewTask"))
                    cell.setIcon(reviewTask);
                else if(s.equals("routeTask"))
                    cell.setIcon(routeTask);
                else if(s.equals("selectSignoffTask"))
                    cell.setIcon(selectSignoffTask);
                else if(s.equals("syncTask"))
                    cell.setIcon(syncTask);
                else if(s.equals("task"))
                    cell.setIcon(task);
                else if(s.equals("taskProperties"))
                    cell.setIcon(taskProperties);
                break;
                
            case Arguments:
                cell.setIcon(argument);
                break;
                
            case AssociatedDataSet:
                cell.setIcon(associatedDataSet);
                break;
                
            case AssociatedFolder:
                cell.setIcon(associatedFolder);
                break;
                
            case AssociatedForm:
                cell.setIcon(associatedForm);
                break;
                
            case Organisation:
                cell.setIcon(organisation);
                break;
                
            case Site:
                cell.setIcon(site);
                break;
                
            case Role:
                cell.setIcon(role);
                break;
                
            case UserData:
                cell.setIcon(userData);
                break;
                
            case UserValue:
                cell.setIcon(userValue);
                break;
                
            case ValidationResults:
                cell.setIcon(validationResult);
                break;
                
            case Checker:
                cell.setIcon(validationResult);
                break;
                
            case WorkflowSignoffProfile:
                cell.setIcon(signoffProfile);
                break;
                
            case WorkflowHandler:
                cell.setIcon(workflowHandler);
                break;
                
            case WorkflowBusinessRuleHandler:
                cell.setIcon(businessRuleHandler);
                break;
                
            case WorkflowBusinessRule:
                cell.setIcon(businessRule);
                break;
                
            case WorkflowAction:
                cell.setIcon(workflowAction);
                break;
                
            default:
                cell.setIcon(workflow);
                break;
        }
        
        return cell;
    }
}
