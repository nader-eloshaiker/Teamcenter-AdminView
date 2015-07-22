/*
 * AttributeTreeModel.java
 *
 * Created on 9 August 2007, 22:03
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tcav.gui.procedure;

import tcav.plmxmlpdm.*;
import tcav.plmxmlpdm.type.*;
import tcav.plmxmlpdm.type.element.*;
import tcav.plmxmlpdm.base.*;
import tcav.procedure.ProcedureManager;
import javax.swing.JTree;
import javax.swing.tree.*;
import javax.swing.event.*;
import java.util.*;
/**
 *
 * @author NZR4DL
 */
public class AttributeTreeModel implements TreeModel {
    
    private ProcedureManager pm;
    private NodeReference rootNode;
    
    /** Creates a new instance of AttributeTreeModel */
    public AttributeTreeModel(NodeReference rootNode, ProcedureManager pm) {
        this.pm = pm;
        this.rootNode = rootNode;
    }
    
    public AttributeTreeModel() {
        this(null, null);
    }
    
    public Object getRoot(){
        if(rootNode == null)
            return null;
        else
            return rootNode;
    }
    
    public Object getChild(Object parent, int index){
        NodeReference nrParent = (NodeReference)parent;
        
        NodeReference nr;
        AttribOwnerBase aob;
        UserDataType ud;
        WorkflowTemplateType wt;
        
        if(nrParent.getEntryType() == NodeReference.ENTRY_COLLECTOR) {
            
            switch(nrParent.getEntryType()) {
                case NodeReference.PROCEDURE_DEPENDANT_TASKS:
                /*
                wt = pm.getWorkflowTemplates().get(pm.getIdIndex(nrParent.getId()));
                wt = pm.getWorkflowTemplates().get(pm.getIdIndex(wt.getDependencyTaskTemplateRefs().get(index)));
                return new NodeReference(
                        wt.getId(),
                        wt.getName(),
                        TagTypeEnum.WorkflowTemplate);
                 */
                case NodeReference.PROCEDURE_SUB_WORKFLOW:
                case NodeReference.PROCEDURE_WORKFLOW_ACTION:
                case NodeReference.PROCEDURE_WORKFLOW_HANDLER:
                case NodeReference.PROCEDURE_WORKFLOW_BUSINESS_RULE:
                case NodeReference.PROCEDURE_WORKFLOW_BUSINESS_RULE_HANDLER:
                default:
                    return null;
            }
        } else if(nrParent.getEntryType() == NodeReference.ENTRY_ITEM) {
            
            switch(nrParent.getClassType()){
                case WorkflowHandler:
                case WorkflowBusinessRuleHandler:
                case Organisation:
                case WorkflowTemplate:
                    aob = pm.getAttribOwnerBase(nrParent.getId());
                    nr = new NodeReference(
                            aob.getAttribute().get(index).getId(),
                            aob.getAttributeName(index),
                            NodeReference.ENTRY_ITEM,
                            NodeReference.PROCEDURE_ATTRIBUTE,
                            aob.getAttributeClass(index));
                    nr.setParentId(nrParent.getId());
                    return nr;
            /*
            case WorkflowHandler:
                WorkflowHandlerType wh = pm.getWorkflowHandlers().get(pm.getIdIndex(nrParent.getId()));
                nr = new NodeReference(
                        wh.getAttribute().get(index).getId(),
                        wh.getAttributeName(index),
                        wh.getAttributeClass(index));
                nr.setParentId(wh.getId());
                //nr.setParentClassType(TagTypeEnum.WorkflowHandler);
                return nr;
             
            case WorkflowBusinessRuleHandler:
                WorkflowBusinessRuleHandlerType wbrh = pm.getWorkflowBusinessRuleHandlers().get(pm.getIdIndex(nrParent.getId()));
                nr = new NodeReference(
                        wbrh.getAttribute().get(index).getId(),
                        wbrh.getAttributeName(index),
                        wbrh.getAttributeClass(index));
                nr.setParentId(wbrh.getId());
                //nr.setParentClassType(TagTypeEnum.WorkflowBusinessRuleHandler);
                return nr;
             
             case Organisation:
                OrganisationType o = pm.getOrganisations().get(pm.getIdIndex(nrParent.getId()));
                nr = new NodeReference(
                        o.getAttribute().get(index).getId(),
                        o.getAttributeName(index),
                        o.getAttributeClass(index));
                nr.setParentId(o.getId());
                return nr;
             */
                    
                case Arguments:
                case UserData:
                    aob = pm.getAttribOwnerBase(nrParent.getParentId());
                    ud = (UserDataType)aob.getAttribute().get(aob.getAttributeIdIndex(nrParent.getId()));
                    nr = new NodeReference(
                            ud.getId(),
                            ud.getUserValue().get(index).getTitle()+": "+ud.getUserValue().get(index).getValue(),
                            NodeReference.ENTRY_ITEM,
                            NodeReference.PROCEDURE_ATTRIBUTE,
                            TagTypeEnum.UserValue);
                    nr.setParentId(aob.getId());
                    nr.setIndex(index);
                    return nr;
                    
                case UserValue:
                    aob = pm.getAttribOwnerBase(nrParent.getParentId());
                    ud = (UserDataType)aob.getAttribute().get(aob.getAttributeIdIndex(nrParent.getId()));
                    UserDataElementType uv = ud.getUserValue().get(nrParent.getIndex());
                    return new NodeReference(
                            uv.getDataRef(),
                            pm.getAttribOwnerBase(uv.getDataRef()).getName(),
                            NodeReference.ENTRY_ITEM,
                            NodeReference.PROCEDURE_ATTRIBUTE,
                            pm.getIdClass(uv.getDataRef()));
                    
                case AssociatedDataSet:
                    aob = pm.getAttribOwnerBase(nrParent.getParentId());
                    AssociatedDataSetType ad = (AssociatedDataSetType)aob.getAttribute().get(aob.getAttributeIdIndex(nrParent.getId()));
                    if(pm.getIdClass(ad.getDataSetRef()) == TagTypeEnum.WorkflowSignoffProfile){
                        WorkflowSignoffProfileType wsp =
                                pm.getWorkflowSignoffProfiles().get(pm.getIdIndex(ad.getDataSetRef()));
                        return new NodeReference(
                                wsp.getId(),
                                "Signoff Profile",
                                NodeReference.ENTRY_ITEM,
                                NodeReference.PROCEDURE_ATTRIBUTE,
                                TagTypeEnum.WorkflowSignoffProfile);
                        
                    } else
                        return null;
                    
                case AssociatedFolder:
                    aob = pm.getAttribOwnerBase(nrParent.getParentId());
                    AssociatedFolderType af = (AssociatedFolderType)aob.getAttribute().get(aob.getAttributeIdIndex(nrParent.getId()));
                    if(pm.getIdClass(af.getFolderRef()) == TagTypeEnum.WorkflowSignoffProfile){
                        WorkflowSignoffProfileType wsp =
                                pm.getWorkflowSignoffProfiles().get(pm.getIdIndex(af.getFolderRef()));
                        return new NodeReference(
                                wsp.getId(),
                                "Signoff Profile",
                                NodeReference.ENTRY_ITEM,
                                NodeReference.PROCEDURE_ATTRIBUTE,
                                TagTypeEnum.WorkflowSignoffProfile);
                    } else
                        return null;
                    
                case AssociatedForm:
                    aob = pm.getAttribOwnerBase(nrParent.getParentId());
                    AssociatedFormType afm = (AssociatedFormType)aob.getAttribute().get(aob.getAttributeIdIndex(nrParent.getId()));
                    if(pm.getIdClass(afm.getFormRef()) == TagTypeEnum.WorkflowSignoffProfile){
                        WorkflowSignoffProfileType wsp =
                                pm.getWorkflowSignoffProfiles().get(pm.getIdIndex(afm.getFormRef()));
                        return new NodeReference(
                                wsp.getId(),
                                "Signoff Profile",
                                NodeReference.ENTRY_ITEM,
                                NodeReference.PROCEDURE_ATTRIBUTE,
                                TagTypeEnum.WorkflowSignoffProfile);
                    } else
                        return null;
                    
                case ValidationResults:
                    aob = pm.getAttribOwnerBase(nrParent.getParentId());
                    ValidationResultsType vr = (ValidationResultsType)aob.getAttribute().get(aob.getAttributeIdIndex(nrParent.getId()));
                    nr = new NodeReference(
                            vr.getId(),
                            vr.getApplicationRef().get(index).getLabel(),
                            NodeReference.ENTRY_ITEM,
                            NodeReference.PROCEDURE_ATTRIBUTE,
                            TagTypeEnum.Checker);
                    nr.setParentId(aob.getId());
                    nr.setIndex(index);
                    return nr;
                    
                case WorkflowSignoffProfile:
                    WorkflowSignoffProfileType wsp =
                            pm.getWorkflowSignoffProfiles().get(pm.getIdIndex(nrParent.getId()));
                    if(index == 0){
                        if(wsp.getRoleRef() != null) {
                            RoleType tr = pm.getRoles().get(pm.getIdIndex(wsp.getRoleRef()));
                            return new NodeReference(
                                    tr.getId(),
                                    tr.getName(),
                                    NodeReference.ENTRY_ITEM,
                                    NodeReference.PROCEDURE_ATTRIBUTE,
                                    TagTypeEnum.Role);
                        } else if(wsp.getGroupRef() != null) {
                            OrganisationType o = pm.getOrganisations().get(pm.getIdIndex(wsp.getGroupRef()));
                            return new NodeReference(
                                    o.getId(),
                                    o.getName(),
                                    NodeReference.ENTRY_ITEM,
                                    NodeReference.PROCEDURE_ATTRIBUTE,
                                    TagTypeEnum.Organisation);
                        }
                    } else if(index == 1) {
                        if(wsp.getGroupRef() != null) {
                            OrganisationType o = pm.getOrganisations().get(pm.getIdIndex(wsp.getGroupRef()));
                            return new NodeReference(
                                    o.getId(),
                                    o.getName(),
                                    NodeReference.ENTRY_ITEM,
                                    NodeReference.PROCEDURE_ATTRIBUTE,
                                    TagTypeEnum.Organisation);
                        }
                    }
                    
                default:
                    return null;
            }
        } else
            System.out.println("getChild should not reach this point");
        return null;
    }
    
    
    public int getChildCount(Object parent){
        NodeReference nrParent = (NodeReference)parent;
        AttribOwnerBase aob;
        WorkflowTemplateType wt;
        
        if(nrParent.getEntryType() == NodeReference.ENTRY_COLLECTOR) {
            
            switch(nrParent.getEntryType()) {
                case NodeReference.PROCEDURE_DEPENDANT_TASKS:
                /*
                wt = pm.getWorkflowTemplates().get(pm.getIdIndex(nrParent.getId()));
                return wt.getDependencyTaskTemplateRefs().size();
                 */
                case NodeReference.PROCEDURE_SUB_WORKFLOW:
                case NodeReference.PROCEDURE_WORKFLOW_ACTION:
                case NodeReference.PROCEDURE_WORKFLOW_HANDLER:
                case NodeReference.PROCEDURE_WORKFLOW_BUSINESS_RULE:
                case NodeReference.PROCEDURE_WORKFLOW_BUSINESS_RULE_HANDLER:
                default:
                    return 0;
            }
        } else if(nrParent.getEntryType() == NodeReference.ENTRY_ITEM) {
            
            switch(nrParent.getClassType()) {
                case WorkflowHandler:
                case WorkflowBusinessRuleHandler:
                case Organisation:
                case WorkflowTemplate:
                    aob = pm.getAttribOwnerBase(nrParent.getId());
                    return aob.getAttribute().size();
                    
                case Arguments:
                case UserData:
                    aob = pm.getAttribOwnerBase(nrParent.getParentId());
                    UserDataType ud = (UserDataType)aob.getAttribute().get(aob.getAttributeIdIndex(nrParent.getId()));
                    return ud.getUserValue().size();
                    
                case UserValue:
                    aob = pm.getAttribOwnerBase(nrParent.getParentId());
                    ud = (UserDataType)aob.getAttribute().get(aob.getAttributeIdIndex(nrParent.getId()));
                    if((ud.getUserValue().get(nrParent.getIndex()).getDataRef() == null) ||
                            (ud.getUserValue().get(nrParent.getIndex()).getDataRef().equals("")))
                        return 0;
                    else
                        return 1;
                    
                case AssociatedDataSet:
                case AssociatedFolder:
                case AssociatedForm:
                    return 1;
                    
                case ValidationResults:
                    aob = pm.getAttribOwnerBase(nrParent.getParentId());
                    ValidationResultsType vr = (ValidationResultsType)aob.getAttribute().get(aob.getAttributeIdIndex(nrParent.getId()));
                    return vr.getChecker().size();
                    
                case WorkflowSignoffProfile:
                    WorkflowSignoffProfileType wsp =
                            pm.getWorkflowSignoffProfiles().get(pm.getIdIndex(nrParent.getId()));
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
        } else
            System.out.println("getChildCount should not reach this point");
        return 0;
    }
    
    public boolean isLeaf(Object node){
        NodeReference nr = (NodeReference)node;
        AttribOwnerBase aob;
        WorkflowTemplateType wt;
        
        if(nr.getEntryType() == NodeReference.ENTRY_COLLECTOR) {
            switch(nr.getEntryType()) {
                case NodeReference.PROCEDURE_DEPENDANT_TASKS:
                /*
                wt = pm.getWorkflowTemplates().get(pm.getIdIndex(nr.getId()));
                return (wt.getDependencyTaskTemplateRefs().size() == 0);
                 */
                case NodeReference.PROCEDURE_SUB_WORKFLOW:
                case NodeReference.PROCEDURE_WORKFLOW_ACTION:
                case NodeReference.PROCEDURE_WORKFLOW_HANDLER:
                case NodeReference.PROCEDURE_WORKFLOW_BUSINESS_RULE:
                case NodeReference.PROCEDURE_WORKFLOW_BUSINESS_RULE_HANDLER:
                    return true;
            }
        } else if(nr.getEntryType() == NodeReference.ENTRY_ITEM) {
            
            switch(nr.getClassType()){
                case WorkflowHandler:
                case WorkflowBusinessRuleHandler:
                case Organisation:
                case WorkflowTemplate:
                    aob = pm.getAttribOwnerBase(nr.getId());
                    return (aob.getAttribute().size() == 0);
                    
                case Arguments:
                case UserData:
                    aob = pm.getAttribOwnerBase(nr.getParentId());
                    UserDataType ud = (UserDataType)aob.getAttribute().get(aob.getAttributeIdIndex(nr.getId()));
                    return (ud.getUserValue().size()==0);
                    
                case UserValue:
                    aob = pm.getAttribOwnerBase(nr.getParentId());
                    ud = (UserDataType)aob.getAttribute().get(aob.getAttributeIdIndex(nr.getId()));
                    if((ud.getUserValue().get(nr.getIndex()).getDataRef() == null) ||
                            (ud.getUserValue().get(nr.getIndex()).getDataRef().equals("")))
                        return true;
                    else
                        return false;
                    
                case AssociatedDataSet:
                case AssociatedFolder:
                case AssociatedForm:
                    return false;
                    
                case ValidationResults:
                    aob = pm.getAttribOwnerBase(nr.getParentId());
                    ValidationResultsType vr = (ValidationResultsType)aob.getAttribute().get(aob.getAttributeIdIndex(nr.getId()));
                    return (vr.getChecker().size()==0);
                    
                case WorkflowSignoffProfile:
                    WorkflowSignoffProfileType wsp =
                            pm.getWorkflowSignoffProfiles().get(pm.getIdIndex(nr.getId()));
                    return ((wsp.getRoleRef() == null) && (wsp.getGroupRef() == null));
                    
                case Role:
                default:
                    return true;
            }
        } else
            System.out.println("isLeaf should not reach this point");
        return true;
    }
    
    public int getIndexOfChild(Object parent, Object child){
        NodeReference nrParent = (NodeReference)parent;
        NodeReference nrChild = (NodeReference)child;
        AttribOwnerBase aob;
        WorkflowTemplateType wt;
        
        if(nrParent.getEntryType() == NodeReference.ENTRY_COLLECTOR) {
            switch(nrParent.getEntryType()) {
                case NodeReference.PROCEDURE_DEPENDANT_TASKS:
                /*
                wt = pm.getWorkflowTemplates().get(pm.getIdIndex(nrParent.getId()));
                return wt.getDependencyTaskTemplateRefs().indexOf("#"+nrChild.getId());
                 */
                case NodeReference.PROCEDURE_SUB_WORKFLOW:
                case NodeReference.PROCEDURE_WORKFLOW_ACTION:
                case NodeReference.PROCEDURE_WORKFLOW_HANDLER:
                case NodeReference.PROCEDURE_WORKFLOW_BUSINESS_RULE:
                case NodeReference.PROCEDURE_WORKFLOW_BUSINESS_RULE_HANDLER:
                default:
                    return 0;
            }
        } else if(nrParent.getEntryType() == NodeReference.ENTRY_ITEM) {
            
            switch(nrParent.getClassType()){
                case WorkflowHandler:
                case WorkflowBusinessRuleHandler:
                case Organisation:
                case WorkflowTemplate:
                    aob = pm.getAttribOwnerBase(nrParent.getId());
                    return aob.getAttributeIdIndex(nrChild.getId());
                    
                case UserData:
                case Arguments:
                case ValidationResults:
                    return nrChild.getIndex();
                    
                case WorkflowSignoffProfile:
                    WorkflowSignoffProfileType wsp =
                            pm.getWorkflowSignoffProfiles().get(pm.getIdIndex(nrParent.getId()));
                    if( (wsp.getRoleRef() == null) && (wsp.getGroupRef() != null))
                        return 0;
                    else if( (wsp.getRoleRef() != null) && (wsp.getGroupRef() == null))
                        return 0;
                    else if( (wsp.getRoleRef() != null) && (wsp.getGroupRef() != null)){
                        if(nrChild.getClassType() == TagTypeEnum.Role)
                            return 0;
                        else if(nrChild.getClassType() == TagTypeEnum.Organisation)
                            return 1;
                    }
                case AssociatedDataSet:
                case AssociatedFolder:
                case AssociatedForm:
                case UserValue:
                default:
                    return 0;
            }
        } else
            System.out.println("getIndexOfChild should not reach this point");
        return 0;
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
