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
import javax.swing.tree.*;
import javax.swing.*;
import java.awt.*;
/**
 *
 * @author nzr4dl
 */
public class WorkflowTreeCellRenderer  extends DefaultTreeCellRenderer implements TreeCellRenderer {
    
    protected static Color COLOR_DEPENDANT_TASKS;
    protected static Color COLOR_SUB_WORKFLOW;
    protected static Color COLOR_WORKFLOW_ACTION;
    protected static Color COLOR_WORKFLOW_BUSINESS_RULE;
    protected static Color COLOR_WORKFLOW_BUSINESS_RULE_HANDLER;
    protected static Color COLOR_WORKFLOW_HANDLER;
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
    
    static {
        COLOR_DEPENDANT_TASKS                 = new Color(0, 0, 128);
        COLOR_SUB_WORKFLOW                    = new Color(0, 128, 0);
        COLOR_WORKFLOW_ACTION                 = new Color(128, 0, 0);
        COLOR_WORKFLOW_BUSINESS_RULE          = new Color(128, 128, 0);
        COLOR_WORKFLOW_BUSINESS_RULE_HANDLER  = new Color(0, 128, 128);
        COLOR_WORKFLOW_HANDLER                = new Color(128, 128, 128);
        
        
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
        
        switch(wti.getType()){
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
            switch(wti.getType()){
                case WorkflowTreeItem.ITEM:
                    setForeground(this.textNonSelectionColor);
                    break;
                case WorkflowTreeItem.DEPENDANT_TASKS:
                    setForeground(COLOR_DEPENDANT_TASKS);
                    break;
                case WorkflowTreeItem.SUB_WORKFLOW:
                    setForeground(COLOR_SUB_WORKFLOW);
                    break;
                case WorkflowTreeItem.WORKFLOW_ACTION:
                    setForeground(COLOR_WORKFLOW_ACTION);
                    break;
                case WorkflowTreeItem.WORKFLOW_BUSINESS_RULE:
                    setForeground(COLOR_WORKFLOW_BUSINESS_RULE);
                    break;
                case WorkflowTreeItem.WORKFLOW_BUSINESS_RULE_HANDLER:
                    setForeground(COLOR_WORKFLOW_BUSINESS_RULE_HANDLER);
                    break;
                case WorkflowTreeItem.WORKFLOW_HANDLER:
                    setForeground(COLOR_WORKFLOW_HANDLER);
                    break;
                default:
                    setForeground(this.textNonSelectionColor);
                    break;
            }
        }
        
        String s = wti.getIconKey();
        /*
        if(s == null){
            setLeafIcon(UIManager.getIcon("Tree.leafIcon"));
            setClosedIcon(UIManager.getIcon("Tree.closedIcon"));
            setOpenIcon(UIManager.getIcon("Tree.openIcon"));
        }else if(s.equals("acknowledgeTask")){
            setLeafIcon(acknowledgeTask);
            setOpenIcon(acknowledgeTask);
            setClosedIcon(acknowledgeTask);
        }else if(s.equals("addStatusTask")){
            setLeafIcon(addStatusTask);
            setOpenIcon(addStatusTask);
            setClosedIcon(addStatusTask);
        }else if(s.equals("checkListTask")){
            setLeafIcon(checkListTask);
            setOpenIcon(checkListTask);
            setClosedIcon(checkListTask);
        }else if(s.equals("conditionTask")){
            setLeafIcon(conditionTask);
            setOpenIcon(conditionTask);
            setClosedIcon(conditionTask);
        }else if(s.equals("doTask")){
            setLeafIcon(doTask);
            setOpenIcon(doTask);
            setClosedIcon(doTask);
        }else if(s.equals("impactAnalysisTask")){
            setLeafIcon(impactAnalysisTask);
            setOpenIcon(impactAnalysisTask);
            setClosedIcon(impactAnalysisTask);
        }else if(s.equals("notifyTask")){
            setLeafIcon(notifyTask);
            setOpenIcon(notifyTask);
            setClosedIcon(notifyTask);
        }else if(s.equals("orTask")){
            setLeafIcon(orTask);
            setOpenIcon(orTask);
            setClosedIcon(orTask);
        }else if(s.equals("performSignoffTask")){
            setLeafIcon(performSignoffTask);
            setOpenIcon(performSignoffTask);
            setClosedIcon(performSignoffTask);
        }else if(s.equals("prepareecoTask")){
            setLeafIcon(prepareecoTask);
            setOpenIcon(prepareecoTask);
            setClosedIcon(prepareecoTask);
        }else if(s.equals("process")){
            setLeafIcon(process);
            setOpenIcon(process);
            setClosedIcon(process);
        }else if(s.equals("reviewProcess")){
            setLeafIcon(reviewProcess);
            setOpenIcon(reviewProcess);
            setClosedIcon(reviewProcess);
        }else if(s.equals("reviewTask")){
            setLeafIcon(reviewTask);
            setOpenIcon(reviewTask);
            setClosedIcon(reviewTask);
        }else if(s.equals("routeTask")){
            setLeafIcon(routeTask);
            setOpenIcon(routeTask);
            setClosedIcon(routeTask);
        }else if(s.equals("selectSignoffTask")){
            setLeafIcon(selectSignoffTask);
            setOpenIcon(selectSignoffTask);
            setClosedIcon(selectSignoffTask);
        }else if(s.equals("syncTask")){
            setLeafIcon(syncTask);
            setOpenIcon(syncTask);
            setClosedIcon(syncTask);
        }else if(s.equals("task")){
            setLeafIcon(task);
            setOpenIcon(task);
            setClosedIcon(task);
        }else if(s.equals("taskProperties")){
            setLeafIcon(taskProperties);
            setOpenIcon(taskProperties);
            setClosedIcon(taskProperties);
        }
        */
        if(s == null){
            setIcon(null);
        }else if(s.equals("acknowledgeTask")){
            setIcon(acknowledgeTask);
        }else if(s.equals("addStatusTask")){
            setIcon(addStatusTask);
        }else if(s.equals("checkListTask")){
            setIcon(checkListTask);
        }else if(s.equals("conditionTask")){
            setIcon(conditionTask);
        }else if(s.equals("doTask")){
            setIcon(doTask);
        }else if(s.equals("impactAnalysisTask")){
            setIcon(impactAnalysisTask);
        }else if(s.equals("notifyTask")){
            setIcon(notifyTask);
        }else if(s.equals("orTask")){
            setIcon(orTask);
        }else if(s.equals("performSignoffTask")){
            setIcon(performSignoffTask);
        }else if(s.equals("prepareecoTask")){
            setIcon(prepareecoTask);
        }else if(s.equals("process")){
            setIcon(process);
        }else if(s.equals("reviewProcess")){
            setIcon(reviewProcess);
        }else if(s.equals("reviewTask")){
            setIcon(reviewTask);
        }else if(s.equals("routeTask")){
            setIcon(routeTask);
        }else if(s.equals("selectSignoffTask")){
            setIcon(selectSignoffTask);
        }else if(s.equals("syncTask")){
            setIcon(syncTask);
        }else if(s.equals("task")){
            setIcon(task);
        }else if(s.equals("taskProperties")){
            setIcon(taskProperties);
        }
        
        return this;
    }
}
