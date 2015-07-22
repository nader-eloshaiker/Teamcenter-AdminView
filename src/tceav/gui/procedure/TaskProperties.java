/*
 * WorkflowElement.java
 *
 * Created on 10 November 2008, 15:32
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tceav.gui.procedure;

import java.awt.Font;
import javax.swing.ImageIcon;
import tceav.manager.procedure.plmxmlpdm.base.DescriptionBase;
import tceav.manager.procedure.plmxmlpdm.type.AssociatedDataSetType;
import tceav.manager.procedure.plmxmlpdm.type.AssociatedFolderType;
import tceav.manager.procedure.plmxmlpdm.type.AssociatedFormType;
import tceav.manager.procedure.plmxmlpdm.type.OrganisationType;
import tceav.manager.procedure.plmxmlpdm.type.RoleType;
import tceav.manager.procedure.plmxmlpdm.type.SiteType;
import tceav.manager.procedure.plmxmlpdm.type.UserDataType;
import tceav.manager.procedure.plmxmlpdm.type.ValidationResultsType;
import tceav.manager.procedure.plmxmlpdm.type.WorkflowActionType;
import tceav.manager.procedure.plmxmlpdm.type.WorkflowBusinessRuleHandlerType;
import tceav.manager.procedure.plmxmlpdm.type.WorkflowBusinessRuleType;
import tceav.manager.procedure.plmxmlpdm.type.WorkflowHandlerType;
import tceav.manager.procedure.plmxmlpdm.type.WorkflowSignoffProfileType;
import tceav.manager.procedure.plmxmlpdm.type.WorkflowTemplateType;
import tceav.manager.procedure.plmxmlpdm.type.element.UserDataElementType;
import tceav.manager.procedure.plmxmlpdm.type.element.ValidationCheckerType;
import tceav.resources.ImageEnum;
import tceav.resources.ResourceLoader;

/**
 *
 * @author nzr4dl-e
 */
public class TaskProperties {
    
    private ImageIcon icon;
    private String name;
    private String toolTip;
    private Font font;
    
    /** Creates a new instance of WorkflowElement */
    public TaskProperties(String name, String toolTip, ImageIcon icon) {
        this(name, toolTip, icon, null);
    }
    
    public TaskProperties(String name, String toolTip, ImageIcon icon, Font font) {
        this.icon = icon;
        this.name = name;
        this.toolTip = toolTip;
        this.font = font;
    }
    
    public boolean hasFont() {
        return (font != null);
    }
    
    public String getName() {
        return name;
    }
    
    public String getToolTip() {
        return toolTip;
    }
    
    public ImageIcon getIcon() {
        return icon;
    }
    
    public Font getFont() {
        return font;
    }
    
    //Action
    private static ImageIcon siteIcon;
    private static ImageIcon workflowHandlerIcon;
    private static ImageIcon workflowActionIcon;
    private static ImageIcon businessRuleHandlerIcon;
    private static ImageIcon businessRuleIcon;
    
    //Process
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
    
    //Attributes
    protected static ImageIcon associatedDataSet;
    protected static ImageIcon associatedFolder;
    protected static ImageIcon associatedForm;
    protected static ImageIcon argument;
    protected static ImageIcon userData;
    protected static ImageIcon organisation;
    protected static ImageIcon validationResult;
    protected static ImageIcon validationChecker;
    protected static ImageIcon signoffProfile;
    protected static ImageIcon role;
    
    
    //Common
    protected static ImageIcon workflowIcon;
    protected static ImageIcon userValue;
    
    
    
    static {
        
        
        try {
            siteIcon = ResourceLoader.getImage(ImageEnum.pmSite);
            workflowHandlerIcon = ResourceLoader.getImage(ImageEnum.pmWorkflowHandler);
            workflowActionIcon = ResourceLoader.getImage(ImageEnum.pmWorkflowAction);
            businessRuleHandlerIcon = ResourceLoader.getImage(ImageEnum.pmBusinessRuleHandler);
            businessRuleIcon = ResourceLoader.getImage(ImageEnum.pmBusinessRule);
            
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
            
            role = ResourceLoader.getImage(ImageEnum.pmRole);
            associatedDataSet = ResourceLoader.getImage(ImageEnum.pmAssociatedDataSet);
            associatedFolder = ResourceLoader.getImage(ImageEnum.pmAssociatedFolder);
            associatedForm = ResourceLoader.getImage(ImageEnum.pmAssociatedForm);
            argument = ResourceLoader.getImage(ImageEnum.pmArgument);
            userData = ResourceLoader.getImage(ImageEnum.pmUserData);
            validationResult = ResourceLoader.getImage(ImageEnum.pmValidationResults);
            validationChecker = ResourceLoader.getImage(ImageEnum.pmValidationChecker);
            signoffProfile = ResourceLoader.getImage(ImageEnum.pmSignOffProfile);
            organisation = ResourceLoader.getImage(ImageEnum.pmOrganisation);
            
            userValue= ResourceLoader.getImage(ImageEnum.pmUserValue);
            workflowIcon = ResourceLoader.getImage(ImageEnum.pmWorkflow);
        } catch (Exception e) {
            System.out.println("Couldn't load images: " + e);
        }
    }
    
    public static String getElementName(Object element) {
        if(element instanceof WorkflowActionType) {
            return ((WorkflowActionType)element).getType().getName();
            
        } else if(element instanceof WorkflowBusinessRuleType) {
            return "Quorum Rule: "+((WorkflowBusinessRuleType)element).getRuleQuorum();
            
        } else if(element instanceof UserDataElementType) {
            UserDataElementType ud = (UserDataElementType)element;
            if(ud.getTitle().equals("handler_argument"))
                return ud.getValue();
            else
                return ud.getTitle()+": "+ud.getValue();
            
        } else if(element instanceof UserDataType) {
            return ((UserDataType)element).getType();
            
        } else if(element instanceof AssociatedDataSetType) {
            return ((AssociatedDataSetType)element).getRole();
            
        } else if(element instanceof AssociatedFolderType) {
            return ((AssociatedDataSetType)element).getRole();
            
        } else if(element instanceof AssociatedFormType) {
            return ((AssociatedDataSetType)element).getRole();
            
        } else if(element instanceof ValidationResultsType) {
            return ((ValidationResultsType)element).getApplication();
            
        } else if(element instanceof WorkflowSignoffProfileType) {
            return "Profile";
            
        } else if(element instanceof DescriptionBase) {
            return ((DescriptionBase)element).getName();
            
        } else {
            return element.toString();
            
        }
    }
    
    public static TaskProperties getActionElement(Object element) {
        return getActionElement(element, null);
    }
    
    public static TaskProperties getActionElement(Object element, Font font) {
        ImageIcon icon;
        Font newFont = null;
        
        if(element instanceof SiteType) {
            icon = siteIcon;
        } else if(element instanceof WorkflowTemplateType) {
            icon = workflowIcon;
        } else if(element instanceof WorkflowHandlerType) {
            icon = workflowHandlerIcon;
            if(font != null)
                newFont = font.deriveFont(Font.BOLD);
        } else if(element instanceof WorkflowBusinessRuleHandlerType) {
            icon = businessRuleHandlerIcon;
            if(font != null)
                newFont = font.deriveFont(Font.BOLD);
        } else if(element instanceof WorkflowBusinessRuleType) {
            icon = businessRuleIcon;
        } else if(element instanceof WorkflowActionType) {
            icon = workflowActionIcon;
        } else if(element instanceof UserDataElementType) {
            icon = userValue;
        } else {
            icon = workflowIcon;
        }
        
        String name = getElementName(element);
        
        if(newFont == null)
            return new TaskProperties(name, name, icon);
        else
            return new TaskProperties(name, name, icon, newFont);
    }
    
    public static ImageIcon getProcessKeyIcon(String s){
        if(s == null)
            return workflowIcon;
        else if(s.equalsIgnoreCase("acknowledgeTask"))
            return acknowledgeTaskIcon;
        else if(s.equalsIgnoreCase("addStatusTask"))
            return  addStatusTaskIcon;
        else if(s.equalsIgnoreCase("checklistTask"))
            return  checklistTaskIcon;
        else if(s.equalsIgnoreCase("conditionTask"))
            return  conditionTaskIcon;
        else if(s.equalsIgnoreCase("doTask"))
            return  doTaskIcon;
        else if(s.equalsIgnoreCase("impactAnalysisTask"))
            return  impactAnalysisTaskIcon;
        else if(s.equalsIgnoreCase("notifyTask"))
            return  notifyTaskIcon;
        else if(s.equalsIgnoreCase("orTask"))
            return  orTaskIcon;
        else if(s.equalsIgnoreCase("performSignoffTask"))
            return  performSignoffTaskIcon;
        else if(s.equalsIgnoreCase("prepareecoTask"))
            return  prepareECOTaskIcon;
        else if(s.equalsIgnoreCase("process"))
            return  processIcon;
        else if(s.equalsIgnoreCase("reviewProcess"))
            return  reviewProcess;
        else if(s.equalsIgnoreCase("reviewTask"))
            return  reviewTaskIcon;
        else if(s.equalsIgnoreCase("routeTask"))
            return  routeTaskIcon;
        else if(s.equalsIgnoreCase("selectSignoffTask"))
            return  selectSignoffTaskIcon;
        else if(s.equalsIgnoreCase("syncTask"))
            return  syncTaskIcon;
        else if(s.equalsIgnoreCase("task"))
            return  taskIcon;
        else if(s.equalsIgnoreCase("taskProperties"))
            return  taskPropertiesIcon;
        else
            return  workflowIcon;
    }
    
    public static TaskProperties getProcessElement(Object element) {
        return getProcessElement(element, null);
    }
    
    public static TaskProperties getProcessElement(Object element, Font font) {
        ImageIcon icon;
        
        if(element instanceof WorkflowTemplateType) {
            String s = ((WorkflowTemplateType)element).getIconKey();
            if(s == null)
                icon = workflowIcon;
            else if(s.equalsIgnoreCase("acknowledgeTask"))
                icon = acknowledgeTaskIcon;
            else if(s.equalsIgnoreCase("addStatusTask"))
                icon =  addStatusTaskIcon;
            else if(s.equalsIgnoreCase("checklistTask"))
                icon =  checklistTaskIcon;
            else if(s.equalsIgnoreCase("conditionTask"))
                icon =  conditionTaskIcon;
            else if(s.equalsIgnoreCase("doTask"))
                icon =  doTaskIcon;
            else if(s.equalsIgnoreCase("impactAnalysisTask"))
                icon =  impactAnalysisTaskIcon;
            else if(s.equalsIgnoreCase("notifyTask"))
                icon =  notifyTaskIcon;
            else if(s.equalsIgnoreCase("orTask"))
                icon =  orTaskIcon;
            else if(s.equalsIgnoreCase("performSignoffTask"))
                icon =  performSignoffTaskIcon;
            else if(s.equalsIgnoreCase("prepareecoTask"))
                icon =  prepareECOTaskIcon;
            else if(s.equalsIgnoreCase("process"))
                icon =  processIcon;
            else if(s.equalsIgnoreCase("reviewProcess"))
                icon =  reviewProcess;
            else if(s.equalsIgnoreCase("reviewTask"))
                icon =  reviewTaskIcon;
            else if(s.equalsIgnoreCase("routeTask"))
                icon =  routeTaskIcon;
            else if(s.equalsIgnoreCase("selectSignoffTask"))
                icon =  selectSignoffTaskIcon;
            else if(s.equalsIgnoreCase("syncTask"))
                icon =  syncTaskIcon;
            else if(s.equalsIgnoreCase("task"))
                icon =  taskIcon;
            else if(s.equalsIgnoreCase("taskProperties"))
                icon =  taskPropertiesIcon;
            else
                icon =  workflowIcon;
            
        } else if(element instanceof SiteType) {
            icon = siteIcon;
        } else {
            icon =  workflowIcon;
        }
        
        String name = getElementName(element);
        
        return new TaskProperties(name, name, icon);
    }
    
    public static TaskProperties getAttributeElement(Object element, Font font) {
        
        ImageIcon icon;
        Font newFont = null;
        
        if(element instanceof WorkflowTemplateType) {
            if(font != null)
                newFont = font.deriveFont(Font.BOLD);
            
            String s = ((WorkflowTemplateType)element).getIconKey();
            if(s == null)
                icon = workflowIcon;
            else if(s.equalsIgnoreCase("acknowledgeTask"))
                icon = acknowledgeTaskIcon;
            else if(s.equalsIgnoreCase("addStatusTask"))
                icon =  addStatusTaskIcon;
            else if(s.equalsIgnoreCase("checklistTask"))
                icon =  checklistTaskIcon;
            else if(s.equalsIgnoreCase("conditionTask"))
                icon =  conditionTaskIcon;
            else if(s.equalsIgnoreCase("doTask"))
                icon =  doTaskIcon;
            else if(s.equalsIgnoreCase("impactAnalysisTask"))
                icon =  impactAnalysisTaskIcon;
            else if(s.equalsIgnoreCase("notifyTask"))
                icon =  notifyTaskIcon;
            else if(s.equalsIgnoreCase("orTask"))
                icon =  orTaskIcon;
            else if(s.equalsIgnoreCase("performSignoffTask"))
                icon =  performSignoffTaskIcon;
            else if(s.equalsIgnoreCase("prepareecoTask"))
                icon =  prepareECOTaskIcon;
            else if(s.equalsIgnoreCase("process"))
                icon =  processIcon;
            else if(s.equalsIgnoreCase("reviewProcess"))
                icon =  reviewProcess;
            else if(s.equalsIgnoreCase("reviewTask"))
                icon =  reviewTaskIcon;
            else if(s.equalsIgnoreCase("routeTask"))
                icon =  routeTaskIcon;
            else if(s.equalsIgnoreCase("selectSignoffTask"))
                icon =  selectSignoffTaskIcon;
            else if(s.equalsIgnoreCase("syncTask"))
                icon =  syncTaskIcon;
            else if(s.equalsIgnoreCase("task"))
                icon =  taskIcon;
            else if(s.equalsIgnoreCase("taskProperties"))
                icon =  taskPropertiesIcon;
            else
                icon =  workflowIcon;
            
        } else if(element instanceof SiteType) {
            icon = siteIcon;
        } else if( element instanceof UserDataType) {
            icon = userData;
        } else if(element instanceof UserDataElementType) {
            icon = userValue;
        } else if(element instanceof AssociatedDataSetType) {
            icon = associatedDataSet;
        } else if(element instanceof AssociatedFolderType) {
            icon = associatedFolder;
        } else if(element instanceof AssociatedFormType) {
            icon = associatedForm;
        } else if(element instanceof ValidationResultsType) {
            icon = validationResult;
        } else if(element instanceof OrganisationType) {
            icon = organisation;
        } else if(element instanceof RoleType) {
            icon = role;
        } else if(element instanceof ValidationCheckerType) {
            icon = validationResult;
        } else if(element instanceof WorkflowSignoffProfileType) {
            icon = signoffProfile;
        } else {
            icon = workflowIcon;
        }
        
        String name = getElementName(element);
        
        if(newFont == null)
            return new TaskProperties(name, name, icon);
        else
            return new TaskProperties(name, name, icon, newFont);
        
    }
    
}
