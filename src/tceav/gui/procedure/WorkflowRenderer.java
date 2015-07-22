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
import tceav.manager.procedure.plmxmlpdm.type.UserDataType;
import tceav.manager.procedure.plmxmlpdm.type.WorkflowTemplateType;
import tceav.manager.procedure.plmxmlpdm.base.IdBase;
import javax.swing.tree.*;
import javax.swing.*;
import java.awt.*;
import tceav.resources.*;

/**
 *
 * @author nzr4dl
 */
public class WorkflowRenderer  implements TreeCellRenderer {
    
    //Workflow Template
    protected static ImageIcon acknowledgeTaskIcon;
    protected static ImageIcon addStatusTaskIcon;
    protected static ImageIcon checklistTaskIcon;
    protected static ImageIcon conditionTaskIcon;
    protected static ImageIcon doTaskIcon;
    protected static ImageIcon impactAnalysisTaskIcon;
    protected static ImageIcon notifyTaskIcon;
    protected static ImageIcon orTaskIcon;
    protected static ImageIcon performSignoffTaskIcon;
    protected static ImageIcon prepareECOTaskIcon;
    protected static ImageIcon processIcon;
    protected static ImageIcon reviewProcess;
    protected static ImageIcon reviewTaskIcon;
    protected static ImageIcon routeTaskIcon;
    protected static ImageIcon selectSignoffTaskIcon;
    protected static ImageIcon syncTaskIcon;
    protected static ImageIcon taskIcon;
    protected static ImageIcon taskPropertiesIcon;
    protected static ImageIcon workflowIcon;
    
    //Class Type
    protected static ImageIcon siteIcon;

    static {
        
        
        try {
            acknowledgeTaskIcon = ResourceLoader.getImage(ImageEnum.workflowAcknowledge);
            addStatusTaskIcon = ResourceLoader.getImage(ImageEnum.workflowAddStatus);
            checklistTaskIcon = ResourceLoader.getImage(ImageEnum.workflowChecklist);
            conditionTaskIcon = ResourceLoader.getImage(ImageEnum.workflowCondition);
            doTaskIcon = ResourceLoader.getImage(ImageEnum.workflowDo);
            impactAnalysisTaskIcon = ResourceLoader.getImage(ImageEnum.workflowImpactAnalysis);
            notifyTaskIcon = ResourceLoader.getImage(ImageEnum.workflowNotify);
            orTaskIcon = ResourceLoader.getImage(ImageEnum.workflowOr);
            performSignoffTaskIcon = ResourceLoader.getImage(ImageEnum.workflowPerformSignOff);
            prepareECOTaskIcon = ResourceLoader.getImage(ImageEnum.workflowPrePareECO);
            processIcon = ResourceLoader.getImage(ImageEnum.workflowProcess);
            reviewProcess = ResourceLoader.getImage(ImageEnum.workflowReviewProcess);
            reviewTaskIcon = ResourceLoader.getImage(ImageEnum.workflowReview);
            routeTaskIcon = ResourceLoader.getImage(ImageEnum.workflowRoute);
            selectSignoffTaskIcon = ResourceLoader.getImage(ImageEnum.workflowSelectSignOff);
            syncTaskIcon = ResourceLoader.getImage(ImageEnum.workflowSync);
            taskIcon = ResourceLoader.getImage(ImageEnum.workflowTask);
            taskPropertiesIcon = ResourceLoader.getImage(ImageEnum.workflowTaskDependancies);
            
            siteIcon = ResourceLoader.getImage(ImageEnum.pmSite);
            workflowIcon = ResourceLoader.getImage(ImageEnum.pmWorkflow);

            
        } catch (Exception e) {
            System.out.println("Couldn't load images: " + e);
        }
    }
    
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

        UserDataType ud;

        switch(((IdBase)value).getTagType()) {
            case WorkflowTemplate:
                WorkflowTemplateType wt = (WorkflowTemplateType)value;
                cell.setText(wt.getName());
                cell.setToolTipText(wt.getName());
                String s = wt.getIconKey();
                if(s == null)
                    cell.setIcon(workflowIcon);
                else if(s.equalsIgnoreCase("acknowledgeTask"))
                    cell.setIcon(acknowledgeTaskIcon);
                else if(s.equalsIgnoreCase("addStatusTask"))
                    cell.setIcon(addStatusTaskIcon);
                else if(s.equalsIgnoreCase("checklistTask"))
                    cell.setIcon(checklistTaskIcon);
                else if(s.equalsIgnoreCase("conditionTask"))
                    cell.setIcon(conditionTaskIcon);
                else if(s.equalsIgnoreCase("doTask"))
                    cell.setIcon(doTaskIcon);
                else if(s.equalsIgnoreCase("impactAnalysisTask"))
                    cell.setIcon(impactAnalysisTaskIcon);
                else if(s.equalsIgnoreCase("notifyTask"))
                    cell.setIcon(notifyTaskIcon);
                else if(s.equalsIgnoreCase("orTask"))
                    cell.setIcon(orTaskIcon);
                else if(s.equalsIgnoreCase("performSignoffTask"))
                    cell.setIcon(performSignoffTaskIcon);
                else if(s.equalsIgnoreCase("prepareecoTask"))
                    cell.setIcon(prepareECOTaskIcon);
                else if(s.equalsIgnoreCase("process"))
                    cell.setIcon(processIcon);
                else if(s.equalsIgnoreCase("reviewProcess"))
                    cell.setIcon(reviewProcess);
                else if(s.equalsIgnoreCase("reviewTask"))
                    cell.setIcon(reviewTaskIcon);
                else if(s.equalsIgnoreCase("routeTask"))
                    cell.setIcon(routeTaskIcon);
                else if(s.equalsIgnoreCase("selectSignoffTask"))
                    cell.setIcon(selectSignoffTaskIcon);
                else if(s.equalsIgnoreCase("syncTask"))
                    cell.setIcon(syncTaskIcon);
                else if(s.equalsIgnoreCase("task"))
                    cell.setIcon(taskIcon);
                else if(s.equalsIgnoreCase("taskProperties"))
                    cell.setIcon(taskPropertiesIcon);
                else
                    cell.setIcon(workflowIcon);
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
                cell.setIcon(workflowIcon);
                break;
                
        }
        
        if (!leaf)
            cell.setFont(cell.getFont().deriveFont(Font.BOLD));
        
        return cell;
    }
}
