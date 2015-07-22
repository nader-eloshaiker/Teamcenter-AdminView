/*
 * AttributeTreeModel.java
 *
 * Created on 9 August 2007, 22:03
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tceav.gui.procedure;

import javax.swing.JTree;
import javax.swing.tree.*;
import javax.swing.event.*;
import java.util.*;
import tceav.manager.procedure.plmxmlpdm.TagTypeEnum;
import tceav.manager.procedure.plmxmlpdm.base.AttribOwnerBase;
import tceav.manager.procedure.plmxmlpdm.base.IdBase;
import tceav.manager.procedure.plmxmlpdm.type.AssociatedDataSetType;
import tceav.manager.procedure.plmxmlpdm.type.AssociatedFolderType;
import tceav.manager.procedure.plmxmlpdm.type.AssociatedFormType;
import tceav.manager.procedure.plmxmlpdm.type.OrganisationType;
import tceav.manager.procedure.plmxmlpdm.type.UserDataType;
import tceav.manager.procedure.plmxmlpdm.type.ValidationResultsType;
import tceav.manager.procedure.plmxmlpdm.type.WorkflowActionType;
import tceav.manager.procedure.plmxmlpdm.type.WorkflowSignoffProfileType;
import tceav.manager.procedure.plmxmlpdm.type.WorkflowTemplateType;
import tceav.manager.procedure.plmxmlpdm.type.element.UserDataElementType;
import tceav.manager.procedure.plmxmlpdm.type.element.ValidationCheckerType;
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
                
            case WorkflowAction:
                WorkflowActionType wa = (WorkflowActionType)nrParent;
                return wa.getActionHandlers()[index];
                
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
                return ad.getDataSet();
                
            case AssociatedFolder:
                AssociatedFolderType af = (AssociatedFolderType)nrParent;
                af.getFolder();
                
            case AssociatedForm:
                AssociatedFormType afm = (AssociatedFormType)nrParent;
                afm.getForm();
                
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
                
            case WorkflowAction:
                WorkflowActionType wa = (WorkflowActionType)parent;
                return wa.getActionHandlers().length;
                
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
                AssociatedDataSetType ad = (AssociatedDataSetType)nrParent;
                if(ad.getDataSet() == null)
                    return 0;
                else
                    return 1;
                
            case AssociatedFolder:
                AssociatedFolderType af = (AssociatedFolderType)nrParent;
                if(af.getFolder() == null)
                    return 0;
                else
                    return 1;
                
            case AssociatedForm:
                AssociatedFormType afm = (AssociatedFormType)nrParent;
                if(afm.getForm() == null)
                    return 0;
                else
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
                
            case WorkflowAction:
                WorkflowActionType wa = (WorkflowActionType)nr;
                return (wa.getActionHandlers().length == 0);
                
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
                AssociatedDataSetType ad = (AssociatedDataSetType)nr;
                if(ad.getDataSet() == null)
                    return true;
                else
                    return false;
                
            case AssociatedFolder:
                AssociatedFolderType af = (AssociatedFolderType)nr;
                if(af.getFolder() == null)
                    return true;
                else
                    return false;
                
            case AssociatedForm:
                AssociatedFormType afm = (AssociatedFormType)nr;
                if(afm.getForm() == null)
                    return true;
                else
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
                
            case WorkflowAction:
                WorkflowActionType wa = (WorkflowActionType)nrParent;
                for(int i=0; i<wa.getActionHandlers().length; i++)
                    if(wa.getActionHandlers()[i].getId().equals(nrChild.getId()))
                        return i;
                return -1;
                
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
                } else
                    return -1;
                
            case UserValue:
                UserDataElementType uv = (UserDataElementType)nrParent;
                if(uv.getDataRef() == null)
                    return -1;
                else
                    return 0;
                
            case AssociatedDataSet:
                AssociatedDataSetType ad = (AssociatedDataSetType)nrParent;
                if(ad.getDataSet() == null)
                    return -1;
                else
                    return 0;
                
            case AssociatedFolder:
                AssociatedFolderType af = (AssociatedFolderType)nrParent;
                if(af.getFolder() == null)
                    return -1;
                else
                    return 0;
                
            case AssociatedForm:
                AssociatedFormType afm = (AssociatedFormType)nrParent;
                if(afm.getForm() == null)
                    return -1;
                else
                    return 0;

            default:
                return -1;
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
