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
public class ProcedureTreeCellRenderer  implements TreeCellRenderer {
    
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
    protected static ImageIcon workflow;
    
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
            workflow = new ImageIcon(ResourceLocator.getProcedureImage("workflow.gif"));
            
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

        
        switch(((IdBase)value).getTagType()) {
            case WorkflowTemplate:
                WorkflowTemplateType wt = (WorkflowTemplateType)value;
                cell.setText(wt.getName());
                cell.setToolTipText(wt.getName());
                String s = wt.getIconKey();
                if(s == null)
                    cell.setIcon(workflow);
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

            default:
                cell.setText(value.toString());
                cell.setToolTipText(value.toString());
                cell.setIcon(workflow);
                break;
        }
        
        return cell;
    }
}
