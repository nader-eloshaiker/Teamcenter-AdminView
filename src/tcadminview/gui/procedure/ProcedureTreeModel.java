/*
 * ProcedureTreeModel.java
 *
 * Created on 5 August 2007, 09:23
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tcadminview.gui.procedure;

import java.util.ArrayList;
import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.event.*;
import tcadminview.procedure.ProcedureManager;
import tcadminview.plmxmlpdm.*;
import tcadminview.plmxmlpdm.classtype.*;
import tcadminview.plmxmlpdm.base.*;
import tcadminview.plmxmlpdm.type.*;
/**
 *
 * @author NZR4DL
 */
public class ProcedureTreeModel implements TreeModel {
    
    private ProcedureManager pm;
    private ArrayList<Integer> rootWorkflows;
    
    /** Creates a new instance of RuleTreeModel */
    public ProcedureTreeModel(ProcedureManager pm) {
        this.pm = pm;
        if(pm.getWorkflowTemplates().getIndexesForClassification(
                WorkflowTemplateClassificationEnum.PROCESS).size() != 0)
            rootWorkflows = pm.getWorkflowTemplates().getIndexesForClassification(
                    WorkflowTemplateClassificationEnum.PROCESS);
        else {
            rootWorkflows = new ArrayList<Integer>();
            for(int i=0; i<pm.getHeader().getTraverseRootRefs().size(); i++)
                rootWorkflows.add(pm.getIdIndex(pm.getHeader().getTraverseRootRefs().get(i)));
        }
    }
    
    
    public Object getRoot(){
        if(pm.getWorkflowTemplates().size() == 0)
            return null;
        else
            return
                new NodeReference(
                    pm.getSites().get(0).getId(),
                    pm.getSites().get(0).getName()+" ("+pm.getSites().get(0).getSiteId()+")",
                    TagTypeEnum.Site);
    }
    
    public Object getChild(Object parent, int index){
        NodeReference nrParent = (NodeReference)parent;
        String childId;
        NodeReference nr;
        WorkflowTemplateType wt;
        WorkflowActionType wa;
        WorkflowBusinessRuleType wbr;
        WorkflowBusinessRuleHandlerType wbrh;
        
        switch(nrParent.getItemType()){
            case NodeReference.DEPENDANT_TASKS:
                wt = pm.getWorkflowTemplates().get(pm.getIdIndex(nrParent.getId()));
                childId = wt.getDependencyTaskTemplateRefs().get(index);
                wt = pm.getWorkflowTemplates().get(pm.getIdIndex(childId));
                return new NodeReference(
                        wt.getId(), wt.getName(), wt.getIconKey(), TagTypeEnum.WorkflowTemplate);
                
            case NodeReference.SUB_WORKFLOW:
                wt = pm.getWorkflowTemplates().get(pm.getIdIndex(nrParent.getId()));
                childId = wt.getSubTemplateRefs().get(index);
                wt = pm.getWorkflowTemplates().get(pm.getIdIndex(childId));
                return new NodeReference(
                        wt.getId(), wt.getName(), wt.getIconKey(), TagTypeEnum.WorkflowTemplate);
                
            case NodeReference.WORKFLOW_ACTION:
                wt = pm.getWorkflowTemplates().get(pm.getIdIndex(nrParent.getId()));
                childId = wt.getActions().get(index);
                wa = pm.getWorkflowActions().get(pm.getIdIndex(childId));
                return new NodeReference(
                        wa.getId(), "Action "+index, wa.getActionType(), TagTypeEnum.WorkflowAction);
                
            case NodeReference.WORKFLOW_HANDLER:
                wa = pm.getWorkflowActions().get(pm.getIdIndex(nrParent.getId()));
                childId = wa.getActionHandlerRefs().get(index);
                WorkflowHandlerType wh = pm.getWorkflowHandlers().get(pm.getIdIndex(childId));
                return new NodeReference(
                        wh.getId(), wh.getName(), TagTypeEnum.WorkflowHandler);
                
            case NodeReference.WORKFLOW_BUSINESS_RULE:
                wa = pm.getWorkflowActions().get(pm.getIdIndex(nrParent.getId()));
                childId = wa.getRuleRefs().get(index);
                wbr = pm.getWorkflowBusinessRules().get(pm.getIdIndex(childId));
                return new NodeReference(
                        wbr.getId(), "Rule "+index, TagTypeEnum.WorkflowBusinessRule);
                
            case NodeReference.WORKFLOW_BUSINESS_RULE_HANDLER:
                wbr = pm.getWorkflowBusinessRules().get(pm.getIdIndex(nrParent.getId()));
                childId = wbr.getRuleHandlerRefs().get(index);
                wbrh = pm.getWorkflowBusinessRuleHandlers().get(pm.getIdIndex(childId));
                return new NodeReference(
                        wbrh.getId(), wbrh.getName(), TagTypeEnum.WorkflowBusinessRuleHandler);
                
            case NodeReference.ITEM:
                switch(nrParent.getClassType()){
                    case Site:
                        wt = pm.getWorkflowTemplates().get(rootWorkflows.get(index));
                        return new NodeReference(
                                wt.getId(),
                                wt.getName(),
                                wt.getIconKey(),
                                TagTypeEnum.WorkflowTemplate);
                        
                    case WorkflowTemplate:
                        wt = pm.getWorkflowTemplates().get(pm.getIdIndex(nrParent.getId()));
                        switch(index){
                            case 0:
                                if(wt.getDependencyTaskTemplateRefs().size() != 0)
                                    return new NodeReference(
                                            wt.getId(), "Dependant Workflow Tasks", NodeReference.DEPENDANT_TASKS, nrParent.getClassType());
                                else if(wt.getSubTemplateRefs().size() != 0)
                                    return new NodeReference(
                                            wt.getId(), "Sub Workflows", NodeReference.SUB_WORKFLOW, nrParent.getClassType());
                                else if(wt.getActions().size() != 0)
                                    return new NodeReference(
                                            wt.getId(), "Workflow Actions", NodeReference.WORKFLOW_ACTION, nrParent.getClassType());
                                
                            case 1:
                                if(wt.getDependencyTaskTemplateRefs().size() != 0) {
                                    if(wt.getSubTemplateRefs().size() != 0)
                                        return new NodeReference(
                                                wt.getId(), "Sub Workflows", NodeReference.SUB_WORKFLOW, nrParent.getClassType());
                                    else
                                        return new NodeReference(
                                                wt.getId(), "Workflow Actions", NodeReference.WORKFLOW_ACTION, nrParent.getClassType());
                                } else {
                                    return new NodeReference(
                                            wt.getId(), "Workflow Actions", NodeReference.WORKFLOW_ACTION, nrParent.getClassType());
                                }
                                
                            case 2:
                                return new NodeReference(
                                        wt.getId(), "Workflow Actions", NodeReference.WORKFLOW_ACTION, nrParent.getClassType());
                                
                            default:
                                System.out.println("getChild WorkflowTemplate should not reach this point");
                                return new Object();
                        }
                        
                    case WorkflowAction:
                        wa = pm.getWorkflowActions().get(pm.getIdIndex(nrParent.getId()));
                        switch(index){
                            case 0:
                                if(wa.getActionHandlerRefs().size() != 0)
                                    return new NodeReference(
                                            wa.getId(), "Handlers", NodeReference.WORKFLOW_HANDLER, nrParent.getClassType());
                                else if(wa.getRuleRefs().size() != 0)
                                    return new NodeReference(
                                            wa.getId(), "Business Rules", NodeReference.WORKFLOW_BUSINESS_RULE, nrParent.getClassType());
                                
                            case 1:
                                if(wa.getRuleRefs().size() != 0)
                                    return new NodeReference(
                                            wa.getId(), "Business Rules", NodeReference.WORKFLOW_BUSINESS_RULE, nrParent.getClassType());
                                
                            default:
                                System.out.println("getChild WorkflowAction should not reach this point");
                                return new Object();
                        }
                        
                    case WorkflowBusinessRule:
                        wbr = pm.getWorkflowBusinessRules().get(pm.getIdIndex(nrParent.getId()));
                        if(wbr.getRuleHandlerRefs().size() != 0)
                            return new NodeReference(
                                    wbr.getId(), "Business Rule Handlers", NodeReference.WORKFLOW_BUSINESS_RULE_HANDLER, nrParent.getClassType());
                        
                    default:
                        System.out.println("getChild default Class Type should not reach this point");
                        return new Object();
                }
            default:
                System.out.println("getChild default Node Type should not reach this point");
                return new Object();
                
        }
    }
    
    public int getChildCount(Object parent){
        NodeReference nrParent = (NodeReference)parent;
        int counter = 0;
        WorkflowActionType wa;
        WorkflowBusinessRuleType wbr;
        WorkflowTemplateType wt;
        
        switch(nrParent.getItemType()){
            case NodeReference.DEPENDANT_TASKS:
                wt = pm.getWorkflowTemplates().get(pm.getIdIndex(nrParent.getId()));
                return wt.getDependencyTaskTemplateRefs().size();
                
            case NodeReference.SUB_WORKFLOW:
                wt = pm.getWorkflowTemplates().get(pm.getIdIndex(nrParent.getId()));
                return wt.getSubTemplateRefs().size();
                
            case NodeReference.WORKFLOW_ACTION:
                wt = pm.getWorkflowTemplates().get(pm.getIdIndex(nrParent.getId()));
                return wt.getActions().size();
                
            case NodeReference.WORKFLOW_HANDLER:
                wa = pm.getWorkflowActions().get(pm.getIdIndex(nrParent.getId()));
                return wa.getActionHandlerRefs().size();
                
            case NodeReference.WORKFLOW_BUSINESS_RULE:
                wbr = pm.getWorkflowBusinessRules().get(pm.getIdIndex(nrParent.getId()));
                return wbr.getRuleHandlerRefs().size();
                
            case NodeReference.WORKFLOW_BUSINESS_RULE_HANDLER:
                wbr = pm.getWorkflowBusinessRules().get(pm.getIdIndex(nrParent.getId()));
                return wbr.getRuleHandlerRefs().size();
                
            case NodeReference.ITEM:
                switch(nrParent.getClassType()){
                    case Site:
                        return rootWorkflows.size();
                        
                    case WorkflowTemplate:
                        wt = pm.getWorkflowTemplates().get(pm.getIdIndex(nrParent.getId()));
                        counter = 0;
                        if(wt.getDependencyTaskTemplateRefs().size() != 0)
                            counter++;
                        if(wt.getActions().size() != 0)
                            counter++;
                        if(wt.getSubTemplateRefs().size() != 0)
                            counter++;
                        return counter;
                        
                    case WorkflowAction:
                        wa = pm.getWorkflowActions().get(pm.getIdIndex(nrParent.getId()));
                        counter = 0;
                        if(wa.getActionHandlerRefs().size() !=0)
                            counter++;
                        if(wa.getRuleRefs().size() != 0)
                            counter++;
                        return counter;
                        
                    case WorkflowBusinessRule:
                        wbr = pm.getWorkflowBusinessRules().get(pm.getIdIndex(nrParent.getId()));
                        if(wbr.getRuleHandlerRefs().size() != 0)
                            return 1;
                        else
                            return 0;
                        
                        
                    case WorkflowBusinessRuleHandler:
                    case WorkflowHandler:
                        return 0;
                        
                    default:
                        System.out.println("getChildCount default should not reach this point");
                        return 0;
                }
                
            default:
                System.out.println("getChildCount Collector should not reach this point");
                return 0;
        }
        
    }
    
    public boolean isLeaf(Object node){
        NodeReference nr = (NodeReference)node;
        AttribOwnerBase aob;
        int counter = 0;
        
        switch(nr.getItemType()){
            case NodeReference.DEPENDANT_TASKS:
            case NodeReference.SUB_WORKFLOW:
            case NodeReference.WORKFLOW_ACTION:
            case NodeReference.WORKFLOW_HANDLER:
            case NodeReference.WORKFLOW_BUSINESS_RULE:
            case NodeReference.WORKFLOW_BUSINESS_RULE_HANDLER:
                return false;
                
            case NodeReference.ITEM:
                switch(nr.getClassType()){
                    case Site:
                        return (rootWorkflows.size() == 0);
                        
                    case WorkflowTemplate:
                        WorkflowTemplateType wt =
                                pm.getWorkflowTemplates().get(pm.getIdIndex(nr.getId()));
                        counter = 0;
                        if(wt.getDependencyTaskTemplateRefs().size() != 0)
                            counter++;
                        if(wt.getActions().size() != 0)
                            counter++;
                        if(wt.getSubTemplateRefs().size() != 0)
                            counter++;
                        return (counter == 0);
                        
                    case WorkflowBusinessRule:
                        WorkflowBusinessRuleType wbr =
                                pm.getWorkflowBusinessRules().get(pm.getIdIndex(nr.getId()));
                        return (wbr.getRuleHandlerRefs().size()==0);
                        
                    case WorkflowAction:
                        WorkflowActionType wa =
                                pm.getWorkflowActions().get(pm.getIdIndex(nr.getId()));
                        counter = 0;
                        if(wa.getActionHandlerRefs().size() !=0)
                            counter++;
                        if(wa.getRuleRefs().size() != 0)
                            counter++;
                        return (counter == 0);
                        
                    case WorkflowBusinessRuleHandler:
                    case WorkflowHandler:
                        return true;
                        
                    default:
                        System.out.println("isLeaf default class type should not reach this point");
                        return true;
                }
                
            default:
                System.out.println("isLeaf default item type should not reach this point");
                return true;
        }
    }
    
    public int getIndexOfChild(Object parent, Object child){
        NodeReference nrParent = (NodeReference)parent;
        NodeReference nrChild = (NodeReference)child;
        
        WorkflowTemplateType wt;
        WorkflowActionType wa;
        WorkflowBusinessRuleType wbr;
        
        switch(nrParent.getItemType()){
            case NodeReference.DEPENDANT_TASKS:
                wt = pm.getWorkflowTemplates().get(pm.getIdIndex(nrParent.getId()));
                return wt.getDependencyTaskTemplateRefs().indexOf('#'+nrChild.getId());
                
            case NodeReference.SUB_WORKFLOW:
                wt = pm.getWorkflowTemplates().get(pm.getIdIndex(nrParent.getId()));
                return wt.getSubTemplateRefs().indexOf('#'+nrChild.getId());
                
            case NodeReference.WORKFLOW_ACTION:
                wt = pm.getWorkflowTemplates().get(pm.getIdIndex(nrParent.getId()));
                return wt.getActions().indexOf('#'+nrChild.getId());
                
            case NodeReference.WORKFLOW_HANDLER:
                wa = pm.getWorkflowActions().get(pm.getIdIndex(nrParent.getId()));
                return wa.getActionHandlerRefs().indexOf('#'+nrChild.getId());
                
            case NodeReference.WORKFLOW_BUSINESS_RULE:
                wa = pm.getWorkflowActions().get(pm.getIdIndex(nrParent.getId()));
                return wa.getRuleRefs().indexOf('#'+nrChild.getId());
                
            case NodeReference.WORKFLOW_BUSINESS_RULE_HANDLER:
                wbr = pm.getWorkflowBusinessRules().get(pm.getIdIndex(nrParent.getId()));
                return wbr.getRuleHandlerRefs().indexOf('#'+nrChild.getId());
                
            case NodeReference.ITEM:
                switch(nrParent.getClassType()){
                    case Site:
                        return rootWorkflows.indexOf(pm.getIdIndex(nrChild.getId()));
                        
                    case WorkflowTemplate:
                        wt = pm.getWorkflowTemplates().get(pm.getIdIndex(nrParent.getId()));
                        switch(nrChild.getItemType()){
                            case NodeReference.DEPENDANT_TASKS:
                                return 0;
                                
                            case NodeReference.SUB_WORKFLOW:
                                if(wt.getDependencyTaskTemplateRefs().size() != 0)
                                    return 1;
                                else
                                    return 0;
                                
                            case NodeReference.WORKFLOW_ACTION:
                                if( (wt.getDependencyTaskTemplateRefs().size() != 0) &&
                                        (wt.getSubTemplateRefs().size() != 0))
                                    return 2;
                                else if( (wt.getDependencyTaskTemplateRefs().size() != 0) &&
                                        (wt.getSubTemplateRefs().size() == 0))
                                    return 1;
                                else if( (wt.getDependencyTaskTemplateRefs().size() == 0) &&
                                        (wt.getSubTemplateRefs().size() != 0))
                                    return 1;
                                else if( (wt.getDependencyTaskTemplateRefs().size() == 0) &&
                                        (wt.getSubTemplateRefs().size() == 0))
                                    return 0;
                                
                            default:
                                System.out.println("getIndexOfChild WorkflowTemplate should not reach this point");
                                return 0;
                        }
                        
                    case WorkflowAction:
                        wa = pm.getWorkflowActions().get(pm.getIdIndex(nrParent.getId()));
                        switch(nrChild.getItemType()){
                            case NodeReference.WORKFLOW_HANDLER:
                                return 0;
                                
                            case NodeReference.WORKFLOW_BUSINESS_RULE:
                                if(wa.getActionHandlerRefs().size() != 0)
                                    return 1;
                                else
                                    return 0;
                                
                            default:
                                System.out.println("getIndexOfChild WorkflowAction should not reach this point");
                                return 0;
                        }
                        
                    case WorkflowBusinessRule:
                        return 0;
                        
                    default:
                        System.out.println("getIndexOfChild default class type should not reach this point");
                        return 0;
                }
            default:
                System.out.println("getIndexOfChild item type should not reach this point");
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


