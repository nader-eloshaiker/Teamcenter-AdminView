/*
 * AttributeTreeModel.java
 *
 * Created on 9 August 2007, 22:03
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tcav.gui.procedure;

import tcav.procedure.*;
import javax.swing.JTree;
import javax.swing.tree.*;
import javax.swing.event.*;
import java.util.*;
import tcav.procedure.plmxmlpdm.TagTypeEnum;
import tcav.procedure.plmxmlpdm.base.AttribOwnerBase;
import tcav.procedure.plmxmlpdm.base.IdBase;
import tcav.procedure.plmxmlpdm.type.AssociatedDataSetType;
import tcav.procedure.plmxmlpdm.type.AssociatedFolderType;
import tcav.procedure.plmxmlpdm.type.AssociatedFormType;
import tcav.procedure.plmxmlpdm.type.OrganisationType;
import tcav.procedure.plmxmlpdm.type.UserDataType;
import tcav.procedure.plmxmlpdm.type.ValidationResultsType;
import tcav.procedure.plmxmlpdm.type.WorkflowSignoffProfileType;
import tcav.procedure.plmxmlpdm.type.WorkflowTemplateType;
import tcav.procedure.plmxmlpdm.type.element.UserDataElementType;
import tcav.procedure.plmxmlpdm.type.element.ValidationCheckerType;
/**
 *
 * @author NZR4DL
 */
public class AttributeTreeModel implements TreeModel {
    
    private IdBase root;
    
    /** Creates a new instance of AttributeTreeModel */
    public AttributeTreeModel(IdBase root) {
        this.root = root;
    }
    
    public AttributeTreeModel() {
        this(null);
    }
    
    public Object getRoot(){
        return root;
    }
    
    public Object getChild(Object parent, int index){
        IdBase nrParent = (IdBase)parent;
        
        IdBase nr;
        AttribOwnerBase aob;
        UserDataType ud;
        WorkflowTemplateType wt;
        
        switch(nrParent.getTagType()){
            case WorkflowHandler:
            case WorkflowBusinessRuleHandler:
            case WorkflowTemplate:
                aob = (AttribOwnerBase)nrParent;
                return aob.getAttribute().get(index);
                
            case Organisation:
                OrganisationType o = (OrganisationType)nrParent;
                return o.getAttribute().get(index);
                
            case Arguments:
            case UserData:
                ud = (UserDataType)nrParent;
                return ud.getUserValue().get(index);
                
            case UserValue:
                UserDataElementType uv = (UserDataElementType)nrParent;
                return uv.getData();
                
            case AssociatedDataSet:
                AssociatedDataSetType ad = (AssociatedDataSetType)nrParent;
                if(ad.getDataSet().getTagType() == TagTypeEnum.WorkflowSignoffProfile)
                    return (WorkflowSignoffProfileType)ad.getDataSet();
                else
                    return null;
                
            case AssociatedFolder:
                AssociatedFolderType af = (AssociatedFolderType)nrParent;
                if(af.getFolder().getTagType() == TagTypeEnum.WorkflowSignoffProfile)
                    return (WorkflowSignoffProfileType)af.getFolder();
                else
                    return null;
                
            case AssociatedForm:
                AssociatedFormType afm = (AssociatedFormType)nrParent;
                if(afm.getForm().getTagType() == TagTypeEnum.WorkflowSignoffProfile)
                    return (WorkflowSignoffProfileType)afm.getForm();
                else
                    return null;
                
            case ValidationResults:
                ValidationResultsType vr = (ValidationResultsType)nrParent;
                return vr.getChecker().get(index);
                
            case WorkflowSignoffProfile:
                WorkflowSignoffProfileType wsp = (WorkflowSignoffProfileType)nrParent;
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
    
    
    public int getChildCount(Object parent){
        IdBase nrParent = (IdBase)parent;
        AttribOwnerBase aob;
        WorkflowTemplateType wt;
        
        switch(nrParent.getTagType()) {
            case WorkflowHandler:
            case WorkflowBusinessRuleHandler:
            case WorkflowTemplate:
                aob = (AttribOwnerBase)nrParent;
                return aob.getAttribute().size();
                
            case Organisation:
                aob = (AttribOwnerBase)nrParent;
                return aob.getAttribute().size();
                
            case Arguments:
            case UserData:
                UserDataType ud = (UserDataType)nrParent;
                return ud.getUserValue().size();
                
            case UserValue:
                UserDataElementType uv = (UserDataElementType)nrParent;
                if(uv.getDataRef() == null)
                    return 0;
                else
                    return 1;
                
            case AssociatedDataSet:
            case AssociatedFolder:
            case AssociatedForm:
                return 1;
                
            case ValidationResults:
                ValidationResultsType vr = (ValidationResultsType)nrParent;
                return vr.getChecker().size();
                
            case WorkflowSignoffProfile:
                WorkflowSignoffProfileType wsp = (WorkflowSignoffProfileType)nrParent;
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
    
    public boolean isLeaf(Object node){
        IdBase nr = (IdBase)node;
        AttribOwnerBase aob;
        WorkflowTemplateType wt;
        
        switch(nr.getTagType()){
            case WorkflowHandler:
            case WorkflowBusinessRuleHandler:
            case WorkflowTemplate:
                aob = (AttribOwnerBase)nr;
                return (aob.getAttribute().size() == 0);

            case Organisation:
                aob = (AttribOwnerBase)nr;
                return (aob.getAttribute().size() == 0);
                
            case Arguments:
            case UserData:
                UserDataType ud = (UserDataType)nr;
                return (ud.getUserValue().size()==0);
                
            case UserValue:
                UserDataElementType uv = (UserDataElementType)nr;
                if(uv.getDataRef() == null)
                    return true;
                else
                    return false;
                
            case AssociatedDataSet:
            case AssociatedFolder:
            case AssociatedForm:
                return false;
                
            case ValidationResults:
                ValidationResultsType vr = (ValidationResultsType)nr;
                return (vr.getChecker().size()==0);
                
            case WorkflowSignoffProfile:
                WorkflowSignoffProfileType wsp = (WorkflowSignoffProfileType)nr;
                return ((wsp.getRoleRef() == null) && (wsp.getGroupRef() == null));
                
            case Role:
            default:
                return true;
        }
    }
    
    public int getIndexOfChild(Object parent, Object child){
        IdBase nrParent = (IdBase)parent;
        IdBase nrChild = (IdBase)child;
        AttribOwnerBase aob;
        WorkflowTemplateType wt;
        
        switch(nrParent.getTagType()){
            case WorkflowHandler:
            case WorkflowBusinessRuleHandler:
            case Organisation:
            case WorkflowTemplate:
                aob = (AttribOwnerBase)nrParent;
                return aob.getAttributeRefs().indexOf(nrChild.getId());
                
            case UserData:
            case Arguments:
                UserDataType ud = (UserDataType)nrParent;
                return ud.getUserValue().indexOf((UserDataElementType)nrChild);
                
            case ValidationResults:
                ValidationResultsType vr = (ValidationResultsType)nrParent;
                return vr.getChecker().indexOf((ValidationCheckerType)nrChild);
                
            case WorkflowSignoffProfile:
                WorkflowSignoffProfileType wsp = (WorkflowSignoffProfileType)nrParent;
                if( (wsp.getRoleRef() == null) && (wsp.getGroupRef() != null))
                    return 0;
                else if( (wsp.getRoleRef() != null) && (wsp.getGroupRef() == null))
                    return 0;
                else if( (wsp.getRoleRef() != null) && (wsp.getGroupRef() != null)){
                    if(nrChild.getTagType() == TagTypeEnum.Role)
                        return 0;
                    else if(nrChild.getTagType() == TagTypeEnum.Organisation)
                        return 1;
                }
            case AssociatedDataSet:
            case AssociatedFolder:
            case AssociatedForm:
            case UserValue:
            default:
                return 0;
        }
    }
    
    public void addTreeModelListener(TreeModelListener listener){
        // not editable
    }
    
    public void removeTreeModelListener(TreeModelListener listener){
        // not editable
    }
    
    public void valueForPathChanged(TreePath path, Object newValue){
        // not editable
    }
    
}
