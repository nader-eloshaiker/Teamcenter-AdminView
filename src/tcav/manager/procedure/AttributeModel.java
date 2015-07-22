/*
 * AttributeModel.java
 *
 * Created on 9 August 2007, 22:03
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tcav.manager.procedure;

import java.util.*;
import tcav.manager.procedure.plmxmlpdm.type.element.*;
import tcav.manager.procedure.plmxmlpdm.type.*;
import tcav.manager.procedure.plmxmlpdm.TagTypeEnum;
import tcav.manager.procedure.plmxmlpdm.base.*;

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
    
    public void processNodeAttributes(IdBase node) {
        attachAttribute(node);
        if(!isLeaf(node))
            for(int i=0; i<getChildCount(node); i++)
                processNodeAttributes(getChild(node, i));
        
    }
    
    private void attachAttribute(IdBase parent) {
        switch(parent.getTagType()){
            case UserValue:
                UserDataElementType uv = (UserDataElementType)parent;
                if(uv.getDataRef() != null && uv.getData() == null)
                    uv.setData(tagCache.get(uv.getDataRef()));
                break;
                
            case AssociatedDataSet:
                AssociatedDataSetType ad = (AssociatedDataSetType)parent;
                if(ad.getDataSetRef() != null && ad.getDataSet() == null)
                    ad.setDataSet(tagCache.get(ad.getDataSetRef()));
                break;
                
            case AssociatedFolder:
                AssociatedFolderType af = (AssociatedFolderType)parent;
                if(af.getFolderRef() != null && af.getFolder() == null)
                    af.setFolder(tagCache.get(af.getFolderRef()));
                break;
                
            case AssociatedForm:
                AssociatedFormType afm = (AssociatedFormType)parent;
                if(afm.getFormRef() != null && afm.getForm() == null)
                    afm.setForm(tagCache.get(afm.getFormRef()));
                break;
                
            case WorkflowSignoffProfile:
                WorkflowSignoffProfileType wsp = (WorkflowSignoffProfileType)parent;
                if(wsp.getRoleRef() != null && wsp.getRole() == null)
                    wsp.setRole((RoleType)tagCache.get(wsp.getRoleRef()));
                if(wsp.getGroupRef() != null && wsp.getGroup() == null)
                    wsp.setGroup((OrganisationType)tagCache.get(wsp.getGroupRef()));
                break;
        }
    }
    
    private IdBase getChild(IdBase parent, int index){
        AttribOwnerBase aob;
        UserDataType ud;
        WorkflowTemplateType wt;
        
        switch(parent.getTagType()){
            case WorkflowHandler:
            case WorkflowBusinessRuleHandler:
            case WorkflowTemplate:
                aob = (AttribOwnerBase)parent;
                return aob.getAttribute().get(index);
                
            case WorkflowAction:
                WorkflowActionType wa = (WorkflowActionType)parent;
                return wa.getActionHandlers()[index];
                
            case Organisation:
                OrganisationType o = (OrganisationType)parent;
                return o.getAttribute().get(index);
                
            case Arguments:
            case UserData:
                ud = (UserDataType)parent;
                return ud.getUserValue().get(index);
                
            case UserValue:
                UserDataElementType uv = (UserDataElementType)parent;
                return uv.getData();
                
            case AssociatedDataSet:
                AssociatedDataSetType ad = (AssociatedDataSetType)parent;
                /*
                 if(ad.getDataSet().getTagType() == TagTypeEnum.WorkflowSignoffProfile)
                    return (WorkflowSignoffProfileType)ad.getDataSet();
                else
                    return null;
                 */
                return ad.getDataSet();
                
            case AssociatedFolder:
                AssociatedFolderType af = (AssociatedFolderType)parent;
                /*
                if(af.getFolder().getTagType() == TagTypeEnum.WorkflowSignoffProfile)
                    return (WorkflowSignoffProfileType)af.getFolder();
                else
                    return null;
                 */
                return af.getFolder();
                
            case AssociatedForm:
                AssociatedFormType afm = (AssociatedFormType)parent;
                /*
                if(afm.getForm().getTagType() == TagTypeEnum.WorkflowSignoffProfile)
                    return (WorkflowSignoffProfileType)afm.getForm();
                else
                    return null;
                 */
                return afm.getForm();
                
            case ValidationResults:
                ValidationResultsType vr = (ValidationResultsType)parent;
                return vr.getChecker().get(index);
                
            case WorkflowSignoffProfile:
                WorkflowSignoffProfileType wsp = (WorkflowSignoffProfileType)parent;
                if(index == 0){
                    if(wsp.getRoleRef() != null)
                        return wsp.getRole();
                    else if(wsp.getGroupRef() != null)
                        return wsp.getGroup();
                } else if(index == 1)
                    if(wsp.getGroupRef() != null)
                        return wsp.getGroup();
                
            default:
                return null;
        }
    }
    
    
    private int getChildCount(IdBase parent){
        AttribOwnerBase aob;
        WorkflowTemplateType wt;
        
        switch(parent.getTagType()) {
            case WorkflowHandler:
            case WorkflowBusinessRuleHandler:
            case WorkflowTemplate:
                aob = (AttribOwnerBase)parent;
                return aob.getAttribute().size();
                
            case WorkflowAction:
                WorkflowActionType wa = (WorkflowActionType)parent;
                return wa.getActionHandlers().length;

            case Organisation:
                aob = (AttribOwnerBase)parent;
                return aob.getAttribute().size();
                
            case Arguments:
            case UserData:
                UserDataType ud = (UserDataType)parent;
                return ud.getUserValue().size();
                
            case UserValue:
                UserDataElementType uv = (UserDataElementType)parent;
                if(uv.getDataRef() == null)
                    return 0;
                else
                    return 1;
                
            case AssociatedDataSet:
            case AssociatedFolder:
            case AssociatedForm:
                return 1;
                
            case ValidationResults:
                ValidationResultsType vr = (ValidationResultsType)parent;
                return vr.getChecker().size();
                
            case WorkflowSignoffProfile:
                WorkflowSignoffProfileType wsp = (WorkflowSignoffProfileType)parent;
                if( (wsp.getRoleRef() == null) && (wsp.getGroupRef() == null))
                    return 0;
                else if( (wsp.getRoleRef() == null) && (wsp.getGroupRef() != null))
                    return 1;
                else if( (wsp.getRoleRef() != null) && (wsp.getGroupRef() == null))
                    return 1;
                else if( (wsp.getRoleRef() != null) && (wsp.getGroupRef() != null))
                    return 2;
                
            case Role:
            default:
                return 0;
        }
    }
    
    private boolean isLeaf(IdBase node){
        AttribOwnerBase aob;
        WorkflowTemplateType wt;
        
        switch(node.getTagType()){
            case WorkflowHandler:
            case WorkflowBusinessRuleHandler:
            case WorkflowTemplate:
                aob = (AttribOwnerBase)node;
                return (aob.getAttribute().size() == 0);
                
            case WorkflowAction:
                WorkflowActionType wa = (WorkflowActionType)node;
                return (wa.getActionHandlers().length == 0);

            case Organisation:
                aob = (AttribOwnerBase)node;
                return (aob.getAttribute().size() == 0);
                
            case Arguments:
            case UserData:
                UserDataType ud = (UserDataType)node;
                return (ud.getUserValue().size()==0);
                
            case UserValue:
                UserDataElementType uv = (UserDataElementType)node;
                if(uv.getDataRef() == null)
                    return true;
                else
                    return false;
                
            case AssociatedDataSet:
            case AssociatedFolder:
            case AssociatedForm:
                return false;
                
            case ValidationResults:
                ValidationResultsType vr = (ValidationResultsType)node;
                return (vr.getChecker().size()==0);
                
            case WorkflowSignoffProfile:
                WorkflowSignoffProfileType wsp = (WorkflowSignoffProfileType)node;
                return ((wsp.getRoleRef() == null) && (wsp.getGroupRef() == null));
                
            case Role:
            default:
                return true;
        }
    }
    
}
