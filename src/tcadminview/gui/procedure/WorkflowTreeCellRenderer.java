/*
 * WorkflowTreeCellRenderer.java
 *
 * Created on 6 August 2007, 16:55
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tcadminview.gui.procedure;

import tcadminview.ResourceLocator;

import tcadminview.plmxmlpdm.TagTypeEnum;
import javax.swing.tree.*;
import javax.swing.*;
import java.awt.*;
/**
 *
 * @author nzr4dl
 */
public class WorkflowTreeCellRenderer  extends DefaultTreeCellRenderer implements TreeCellRenderer {
    
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
    protected static ImageIcon site;
    protected static ImageIcon workflowHandler;
    protected static ImageIcon workflowAction;
    protected static ImageIcon businessRuleHandler;
    protected static ImageIcon businessRule;
    protected static ImageIcon collector;
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
            workflow = new ImageIcon(ResourceLocator.getProcedureImage("workflow.gif"));
            
        } catch (Exception e) {
            System.out.println("Couldn't load images: " + e);
        }
    }
    
    /** Creates a new instance of WorkflowTreeCellRenderer */
    public WorkflowTreeCellRenderer() {
    }
    
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean isSelected, boolean expanded, boolean leaf, int row, boolean hasFocus){
        
        super.selected = isSelected;
        super.hasFocus = hasFocus;
        Icon ClosedIcon = super.closedIcon;
        Icon OpenIcon = super.openIcon;
        Icon LeafIcon = super.leafIcon;
        
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
        WorkflowTreeItem wti = (WorkflowTreeItem)node.getUserObject();
        
        switch(wti.getItemType()){
            case WorkflowTreeItem.ITEM:
                setText(wti.getName());
                break;
            case WorkflowTreeItem.DEPENDANT_TASKS:
            case WorkflowTreeItem.SUB_WORKFLOW:
            case WorkflowTreeItem.WORKFLOW_ACTION:
            case WorkflowTreeItem.WORKFLOW_BUSINESS_RULE:
            case WorkflowTreeItem.WORKFLOW_BUSINESS_RULE_HANDLER:
            case WorkflowTreeItem.WORKFLOW_HANDLER:
                setText('@'+wti.getName());
                break;
            default:
                setText(wti.getName());
        }
        
        if(!isSelected){
            switch(wti.getItemType()){
                case WorkflowTreeItem.ITEM:
                    setForeground(this.textNonSelectionColor);
                    break;
                case WorkflowTreeItem.DEPENDANT_TASKS:
                case WorkflowTreeItem.SUB_WORKFLOW:
                case WorkflowTreeItem.WORKFLOW_ACTION:
                case WorkflowTreeItem.WORKFLOW_BUSINESS_RULE:
                case WorkflowTreeItem.WORKFLOW_BUSINESS_RULE_HANDLER:
                case WorkflowTreeItem.WORKFLOW_HANDLER:
                    setForeground(COLOR_COLLECTOR);
                    break;
                default:
                    setForeground(this.textNonSelectionColor);
                    break;
            }
        }
        
        //TagTypeEnum classType = TagTypeEnum.UNDEFINED;
        if(wti.getClassType() == null) {
            setIcon(collector);
        } else {
            TagTypeEnum classType = wti.getClassType();
            switch(classType) {
                case WorkflowTemplate:
                    String s = wti.getIconKey();
                    if(s == null)
                        setIcon(workflow);
                    else if(s.equals("acknowledgeTask"))
                        setIcon(acknowledgeTask);
                    else if(s.equals("addStatusTask"))
                        setIcon(addStatusTask);
                    else if(s.equals("checkListTask"))
                        setIcon(checkListTask);
                    else if(s.equals("conditionTask"))
                        setIcon(conditionTask);
                    else if(s.equals("doTask"))
                        setIcon(doTask);
                    else if(s.equals("impactAnalysisTask"))
                        setIcon(impactAnalysisTask);
                    else if(s.equals("notifyTask"))
                        setIcon(notifyTask);
                    else if(s.equals("orTask"))
                        setIcon(orTask);
                    else if(s.equals("performSignoffTask"))
                        setIcon(performSignoffTask);
                    else if(s.equals("prepareecoTask"))
                        setIcon(prepareecoTask);
                    else if(s.equals("process"))
                        setIcon(process);
                    else if(s.equals("reviewProcess"))
                        setIcon(reviewProcess);
                    else if(s.equals("reviewTask"))
                        setIcon(reviewTask);
                    else if(s.equals("routeTask"))
                        setIcon(routeTask);
                    else if(s.equals("selectSignoffTask"))
                        setIcon(selectSignoffTask);
                    else if(s.equals("syncTask"))
                        setIcon(syncTask);
                    else if(s.equals("task"))
                        setIcon(task);
                    else if(s.equals("taskProperties"))
                        setIcon(taskProperties);
                    break;
                    
                case Site:
                    setIcon(site);
                    break;
                    
                case WorkflowHandler:
                    setIcon(workflowHandler);
                    break;
                    
                case WorkflowBusinessRuleHandler:
                    setIcon(businessRuleHandler);
                    break;
                    
                case WorkflowBusinessRule:
                    setIcon(businessRule);
                    break;
                    
                case WorkflowAction:
                    setIcon(workflowAction);
                    break;
                    
                default:
                    setIcon(workflow);
                    break;
            }
        }
        
        return this;
    }
}
