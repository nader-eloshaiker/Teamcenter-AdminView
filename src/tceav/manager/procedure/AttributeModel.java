/*
 * AttributeModel.java
 *
 * Created on 9 August 2007, 22:03
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tceav.manager.procedure;

import java.util.Hashtable;
import tceav.manager.procedure.plmxmlpdm.base.AttribOwnerBase;
import tceav.manager.procedure.plmxmlpdm.base.IdBase;
import tceav.manager.procedure.plmxmlpdm.type.AssociatedDataSetType;
import tceav.manager.procedure.plmxmlpdm.type.AssociatedFolderType;
import tceav.manager.procedure.plmxmlpdm.type.AssociatedFormType;
import tceav.manager.procedure.plmxmlpdm.type.OrganisationType;
import tceav.manager.procedure.plmxmlpdm.type.RoleType;
import tceav.manager.procedure.plmxmlpdm.type.UserDataType;
import tceav.manager.procedure.plmxmlpdm.type.ValidationResultsType;
import tceav.manager.procedure.plmxmlpdm.type.WorkflowActionType;
import tceav.manager.procedure.plmxmlpdm.type.WorkflowBusinessRuleHandlerType;
import tceav.manager.procedure.plmxmlpdm.type.WorkflowHandlerType;
import tceav.manager.procedure.plmxmlpdm.type.WorkflowSignoffProfileType;
import tceav.manager.procedure.plmxmlpdm.type.WorkflowTemplateType;
import tceav.manager.procedure.plmxmlpdm.type.element.UserDataElementType;


/**
 *
 * @author NZR4DL
 */
public class AttributeModel {
    Hashtable<String,IdBase> tagCache;
    
    /**
     * Creates a new instance of AttributeModel
     */
    public AttributeModel(Hashtable<String,IdBase> tagCache) {
        this.tagCache = tagCache;
    }
    
    public void processWorkflowTemplateAttributes(WorkflowTemplateType wf) {
        
    }
    
    public void processNodeAttributes(IdBase node) {
        /*
         * Workaround BUG for infinite looping
         * 
         */
        
        if(node instanceof UserDataElementType) { 
            UserDataElementType ude = (UserDataElementType)node;
            if (ude.getValue().equals(UserDataElementType.parentDependencyTaskRef)){
                return;
            }
        }
                 
        attachAttribute(node);
        
        if(!isLeaf(node)) {
            for(int i=0; i<getChildCount(node); i++)
                processNodeAttributes(getChild(node, i));
        }
        
    }
    
    private void attachAttribute(IdBase parent) {
        
        if(parent instanceof UserDataElementType) {
            UserDataElementType uv = (UserDataElementType)parent;

            if(uv.getDataRef() != null && uv.getData() == null)
                uv.setData(tagCache.get(uv.getDataRef()));
            
        } else if(parent instanceof AssociatedDataSetType) {
            AssociatedDataSetType ad = (AssociatedDataSetType)parent;
            if(ad.getDataSetRef() != null && ad.getDataSet() == null)
                ad.setDataSet(tagCache.get(ad.getDataSetRef()));
            
        } else if(parent instanceof AssociatedFolderType) {
            AssociatedFolderType af = (AssociatedFolderType)parent;
            if(af.getFolderRef() != null && af.getFolder() == null)
                af.setFolder(tagCache.get(af.getFolderRef()));
            
        } else if(parent instanceof AssociatedFormType) {
            AssociatedFormType afm = (AssociatedFormType)parent;
            if(afm.getFormRef() != null && afm.getForm() == null)
                afm.setForm(tagCache.get(afm.getFormRef()));
            
        } else if(parent instanceof WorkflowSignoffProfileType) {
            WorkflowSignoffProfileType wsp = (WorkflowSignoffProfileType)parent;
            if(wsp.getRoleRef() != null && wsp.getRole() == null)
                wsp.setRole((RoleType)tagCache.get(wsp.getRoleRef()));
            if(wsp.getGroupRef() != null && wsp.getGroup() == null)
                wsp.setGroup((OrganisationType)tagCache.get(wsp.getGroupRef()));
        }
    }
    
    private IdBase getChild(IdBase parent, int index){
        AttribOwnerBase aob;
        UserDataType ud;
        //WorkflowTemplateType wt;
        
        if(parent instanceof WorkflowHandlerType || 
                parent instanceof WorkflowBusinessRuleHandlerType || 
                parent instanceof WorkflowTemplateType) {
            aob = (AttribOwnerBase)parent;
            if(index >= aob.getAttribute().size())
                return null;
            else
                return aob.getAttribute().get(index);
            
        } else if(parent instanceof WorkflowActionType) {
            WorkflowActionType wa = (WorkflowActionType)parent;
            return wa.getActionHandlers()[index];
            
        } else if(parent instanceof OrganisationType) {
            OrganisationType o = (OrganisationType)parent;
            return o.getAttribute().get(index);
            
        } else if(parent instanceof UserDataType) {
            ud = (UserDataType)parent;
            if(index >= ud.getUserValue().size())
                return null;
            else
                return ud.getUserValue().get(index);
            
        } else if(parent instanceof UserDataElementType) {
            UserDataElementType uv = (UserDataElementType)parent;
            return uv.getData();
            
        } else if(parent instanceof AssociatedDataSetType) {
            AssociatedDataSetType ad = (AssociatedDataSetType)parent;
            return ad.getDataSet();
            
        } else if(parent instanceof AssociatedFolderType) {
            AssociatedFolderType af = (AssociatedFolderType)parent;
            return af.getFolder();
            
        } else if(parent instanceof AssociatedFormType) {
            AssociatedFormType afm = (AssociatedFormType)parent;
            return afm.getForm();
            
        } else if(parent instanceof ValidationResultsType) {
            ValidationResultsType vr = (ValidationResultsType)parent;
            return vr.getChecker().get(index);
            
        } else if(parent instanceof WorkflowSignoffProfileType) {
            WorkflowSignoffProfileType wsp = (WorkflowSignoffProfileType)parent;
            if(index == 0){
                if(wsp.getRoleRef() != null)
                    return wsp.getRole();
                else if(wsp.getGroupRef() != null)
                    return wsp.getGroup();
            } else if(index == 1) {
                if(wsp.getGroupRef() != null)
                    return wsp.getGroup();
            }
            return null;
            
        } else {
            return null;
            
        }
    }
    
    
    private int getChildCount(IdBase parent){
        AttribOwnerBase aob;
        //WorkflowTemplateType wt;
        
        if(parent instanceof WorkflowHandlerType || 
                parent instanceof WorkflowBusinessRuleHandlerType || 
                parent instanceof WorkflowTemplateType) {
            aob = (AttribOwnerBase)parent;
            if(aob.getAttribute() != null)
                return aob.getAttribute().size();
            else
                return 0;
            
        } else if(parent instanceof WorkflowActionType) {
            WorkflowActionType wa = (WorkflowActionType)parent;
            if(wa.getActionHandlers() != null)
                return wa.getActionHandlers().length;
            else 
                return 0;
            
        } else if(parent instanceof OrganisationType) {
            aob = (AttribOwnerBase)parent;
            if(aob.getAttribute() != null)
                return aob.getAttribute().size();
            else
                return 0;
            
        } else if(parent instanceof UserDataType) {
            UserDataType ud = (UserDataType)parent;
            if(ud.getUserValue() != null)
                return ud.getUserValue().size();
            else
                return 0;
            
        } else if(parent instanceof UserDataElementType) {
            UserDataElementType uv = (UserDataElementType)parent;
            if(uv.getDataRef() != null)
                return 1;
            else
                return 0;
            
        } else if(parent instanceof AssociatedDataSetType || parent instanceof AssociatedFolderType || parent instanceof AssociatedFormType) {
            return 1;
            
        } else if(parent instanceof ValidationResultsType) {
            ValidationResultsType vr = (ValidationResultsType)parent;
            if(vr.getChecker() != null)
                return vr.getChecker().size();
            else
                return 0;
            
        } else if(parent instanceof WorkflowSignoffProfileType) {
            WorkflowSignoffProfileType wsp = (WorkflowSignoffProfileType)parent;
            if( (wsp.getRoleRef() == null) && (wsp.getGroupRef() == null))
                return 0;
            else if( (wsp.getRoleRef() == null) && (wsp.getGroupRef() != null))
                return 1;
            else if( (wsp.getRoleRef() != null) && (wsp.getGroupRef() == null))
                return 1;
            else if( (wsp.getRoleRef() != null) && (wsp.getGroupRef() != null))
                return 2;
            
        }
        
        return 0;
    }
    
    private boolean isLeaf(IdBase node){
        AttribOwnerBase aob;
        //WorkflowTemplateType wt;
        
        if(node instanceof WorkflowHandlerType || node instanceof WorkflowBusinessRuleHandlerType || node instanceof WorkflowTemplateType) {
            aob = (AttribOwnerBase)node;
            if(aob.getAttribute() != null)
                return (aob.getAttribute().isEmpty());
            else
                return true;
            
        } else if(node instanceof WorkflowActionType) {
            WorkflowActionType wa = (WorkflowActionType)node;
            if(wa.getActionHandlers() != null)
                return (wa.getActionHandlers().length == 0);
            else
                return true;
            
        } else if(node instanceof OrganisationType) {
            aob = (AttribOwnerBase)node;
            if(aob.getAttribute() != null)
                return (aob.getAttribute().isEmpty());
            else
                return true;
            
        } else if(node instanceof UserDataType) {
            UserDataType ud = (UserDataType)node;
            if(ud.getUserValue() != null)
                return (ud.getUserValue().isEmpty());
            else
                return true;
            
        } else if(node instanceof UserDataElementType) {
            UserDataElementType uv = (UserDataElementType)node;
            if(uv.getDataRef() != null)
                return false;
            else
                return true;
            
        } else if(node instanceof AssociatedDataSetType || node instanceof AssociatedFolderType || node instanceof AssociatedFormType) {
            return false;
            
        } else if(node instanceof ValidationResultsType) {
            ValidationResultsType vr = (ValidationResultsType)node;
            if(vr.getChecker() != null)
                return (vr.getChecker().isEmpty());
            else
                return true;
            
        } else if(node instanceof WorkflowSignoffProfileType) {
            WorkflowSignoffProfileType wsp = (WorkflowSignoffProfileType)node;
            return ((wsp.getRoleRef() == null) && (wsp.getGroupRef() == null));
            
        }
        
        return true;
    }
}