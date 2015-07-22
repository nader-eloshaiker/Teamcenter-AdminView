/*
 * ProcedureTreeCellRenderer.java
 *
 * Created on 6 August 2007, 16:55
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tcav.gui.procedure;

import tcav.resources.*;
import tcav.procedure.plmxmlpdm.TagTypeEnum;
import tcav.procedure.plmxmlpdm.base.IdBase;
import javax.swing.tree.*;
import javax.swing.*;
import java.awt.*;
import tcav.procedure.plmxmlpdm.type.*;
import tcav.procedure.plmxmlpdm.type.element.UserDataElementType;
import tcav.procedure.plmxmlpdm.type.element.ValidationCheckerType;
/**
 *
 * @author nzr4dl
 */
public class ProcessRenderer  implements TreeCellRenderer {
    
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
    protected static ImageIcon workflowIcon;
    
    //Class Type
    protected static ImageIcon siteIcon;

    static {
        
        
        try {
            acknowledgeTaskIcon = ResourceLoader.getImage(ImageEnum.workflowAcknowledge);
            addStatusTaskIcon = ResourceLoader.getImage(ImageEnum.workflowAddStatus);
            checkListTaskIcon = ResourceLoader.getImage(ImageEnum.workflowCheckList);
            conditionTaskIcon = ResourceLoader.getImage(ImageEnum.workflowCondition);
            doTaskIcon = ResourceLoader.getImage(ImageEnum.workflowDo);
            impactAnalysisTaskIcon = ResourceLoader.getImage(ImageEnum.workflowImpactAnalysis);
            notifyTaskIcon = ResourceLoader.getImage(ImageEnum.workflowNotify);
            orTaskIcon = ResourceLoader.getImage(ImageEnum.workflowOr);
            performSignoffTaskIcon = ResourceLoader.getImage(ImageEnum.workflowPerformSignOff);
            prepareecoTaskIcon = ResourceLoader.getImage(ImageEnum.workflowPrePareeco);
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
    public ProcessRenderer() {
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
                
            default:
                cell.setText(value.toString());
                cell.setToolTipText(value.toString());
                cell.setIcon(workflowIcon);
                break;
                
        }
        
        return cell;
    }
}
