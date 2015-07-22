/*
 * ActionTreeModel.java
 *
 * Created on 8 October 2007, 12:40
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tcav.gui.procedure;



import java.util.ArrayList;
import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.event.*;
import tcav.procedure.ProcedureManager;
import tcav.plmxmlpdm.*;
import tcav.plmxmlpdm.type.*;
import tcav.plmxmlpdm.classtype.*;
import tcav.plmxmlpdm.base.*;


/**
 *
 * @author NZR4DL
 */
public class ActionTreeModel implements TreeModel {
    
    private ProcedureManager pm;
    private WorkflowTemplateType root;
    private ArrayList<Integer> rootActions;
    
    public static final int MODE_DEPENDANT_TASKS = 0;
    public static final int MODE_SUB_WORKFLOWS = 1;
    
    /** Creates a new instance of RuleTreeModel */
    public ActionTreeModel(NodeReference nr, ProcedureManager pm) {
        this.pm = pm;
        this.root = pm.getWorkflowTemplates().get(pm.getIdIndex(nr.getId()));
        rootActions = new ArrayList<Integer>();
        for(int i=0; i<root.getActions().size(); i++)
            rootActions.add(pm.getIdIndex(root.getActions().get(i)));
    }
    
    public ActionTreeModel(WorkflowTemplateType root, ProcedureManager pm) {
        this.pm = pm;
        this.root = root;
        rootActions = new ArrayList<Integer>();
        for(int i=0; i<root.getActions().size(); i++)
            rootActions.add(pm.getIdIndex(root.getActions().get(i)));
    }

    public ActionTreeModel() {
        rootActions = new ArrayList<Integer>();
    }

    public Object getRoot(){
        if(rootActions.size() == 0)
            return null;
        else
            return new NodeReference(
                    root.getId(),
                    root.getName(),
                    NodeReference.PROCEDURE_WORKFLOW_PROCESS,
                    TagTypeEnum.WorkflowTemplate,
                    root.getIconKey());
    }
    
    public Object getChild(Object parent, int index){
        NodeReference nrParent = (NodeReference)parent;
        String childId;
        NodeReference nr;
        WorkflowActionType wa;
        WorkflowBusinessRuleType wbr;
        WorkflowBusinessRuleHandlerType wbrh;
        
        switch(nrParent.getClassType()){
            case WorkflowTemplate:
                if(root.getActions().size() != 0) {
                    
                    childId = root.getActions().get(index);
                    wa = pm.getWorkflowActions().get(pm.getIdIndex(childId));
                    return new NodeReference(
                            wa.getId(),
                            "Action: Type "+wa.getActionType(),
                            NodeReference.PROCEDURE_WORKFLOW_ACTION,
                            TagTypeEnum.WorkflowAction,
                            wa.getActionType());
                    
                } else
                    return null;
                
            case WorkflowAction:
                wa = pm.getWorkflowActions().get(pm.getIdIndex(nrParent.getId()));
                
                int handlerSize = wa.getActionHandlerRefs().size();
                
                if((wa.getActionHandlerRefs().size() != 0) && (index < handlerSize)) {
                    
                    childId = wa.getActionHandlerRefs().get(index);
                    WorkflowHandlerType wh = pm.getWorkflowHandlers().get(pm.getIdIndex(childId));
                    return new NodeReference(
                            wh.getId(),
                            wh.getName(),
                            NodeReference.PROCEDURE_WORKFLOW_HANDLER,
                            TagTypeEnum.WorkflowHandler);
                    
                } else if((wa.getRuleRefs().size() != 0) && (index >= handlerSize)) {
                    
                    childId = wa.getRuleRefs().get(index - handlerSize);
                    wbr = pm.getWorkflowBusinessRules().get(pm.getIdIndex(childId));
                    return new NodeReference(
                            wbr.getId(),
                            "Business Rule ",
                            NodeReference.PROCEDURE_WORKFLOW_BUSINESS_RULE,
                            TagTypeEnum.WorkflowBusinessRule);
                    
                } else
                    System.out.println("getChild WorkflowAction should not reach this point");
                
                return null;
                
            case WorkflowBusinessRule:
                wbr = pm.getWorkflowBusinessRules().get(pm.getIdIndex(nrParent.getId()));
                childId = wbr.getRuleHandlerRefs().get(index);
                wbrh = pm.getWorkflowBusinessRuleHandlers().get(pm.getIdIndex(childId));
                return new NodeReference(
                        wbrh.getId(),
                        wbrh.getName(),
                        NodeReference.PROCEDURE_WORKFLOW_BUSINESS_RULE_HANDLER,
                        TagTypeEnum.WorkflowBusinessRuleHandler);
                
            default:
                System.out.println("getChild -ITEM- default Class Type should not reach this point");
                return null;
        }
    }
    
    public int getChildCount(Object parent){
        NodeReference nrParent = (NodeReference)parent;
        int counter = 0;
        WorkflowActionType wa;
        WorkflowBusinessRuleType wbr;
        WorkflowTemplateType wt;
        
        switch(nrParent.getClassType()){
            case WorkflowTemplate:
                return root.getActions().size();
                
            case WorkflowAction:
                wa = pm.getWorkflowActions().get(pm.getIdIndex(nrParent.getId()));
                counter = 0;
                counter += wa.getActionHandlerRefs().size();
                counter += wa.getRuleRefs().size();
                return counter;
                
            case WorkflowBusinessRule:
                wbr = pm.getWorkflowBusinessRules().get(pm.getIdIndex(nrParent.getId()));
                return wbr.getRuleHandlerRefs().size();
                
                
            case WorkflowBusinessRuleHandler:
            case WorkflowHandler:
                return 0;
                
            default:
                System.out.println("getChildCount -ITEM- default should not reach this point");
                return 0;
        }
    }
    
    public boolean isLeaf(Object node){
        NodeReference nr = (NodeReference)node;
        AttribOwnerBase aob;
        int counter = 0;
        
        switch(nr.getClassType()){
            case WorkflowTemplate:
                return (root.getActions().size() == 0);
                
            case WorkflowAction:
                WorkflowActionType wa =
                        pm.getWorkflowActions().get(pm.getIdIndex(nr.getId()));
                counter = 0;
                counter += wa.getActionHandlerRefs().size();
                counter += wa.getRuleRefs().size();
                return (counter == 0);
                
            case WorkflowBusinessRule:
                WorkflowBusinessRuleType wbr =
                        pm.getWorkflowBusinessRules().get(pm.getIdIndex(nr.getId()));
                return (wbr.getRuleHandlerRefs().size()==0);
                
            case WorkflowBusinessRuleHandler:
            case WorkflowHandler:
                return true;
                
            default:
                System.out.println("isLeaf -ITEM- default class type should not reach this point");
                return true;
        }
    }
    
    public int getIndexOfChild(Object parent, Object child){
        NodeReference nrParent = (NodeReference)parent;
        NodeReference nrChild = (NodeReference)child;
        
        WorkflowTemplateType wt;
        WorkflowActionType wa;
        WorkflowBusinessRuleType wbr;
        
        switch(nrParent.getClassType()){
            case WorkflowTemplate:
                        return root.getActions().indexOf('#'+nrChild.getId());
                
            case WorkflowAction:
                wa = pm.getWorkflowActions().get(pm.getIdIndex(nrParent.getId()));
                switch(nrChild.getProcedureType()){
                    case NodeReference.PROCEDURE_WORKFLOW_HANDLER:
                        wa = pm.getWorkflowActions().get(pm.getIdIndex(nrParent.getId()));
                        return wa.getActionHandlerRefs().indexOf('#'+nrChild.getId());
                        
                    case NodeReference.PROCEDURE_WORKFLOW_BUSINESS_RULE:
                        wa = pm.getWorkflowActions().get(pm.getIdIndex(nrParent.getId()));
                        return wa.getRuleRefs().indexOf('#'+nrChild.getId())+wa.getActionHandlerRefs().size();
                        
                    default:
                        System.out.println("getIndexOfChild WorkflowAction should not reach this point");
                        return 0;
                }
                
            case WorkflowBusinessRule:
                wbr = pm.getWorkflowBusinessRules().get(pm.getIdIndex(nrParent.getId()));
                return wbr.getRuleHandlerRefs().indexOf('#'+nrChild.getId());
                
            default:
                System.out.println("getIndexOfChild -ITEM- default class type should not reach this point");
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


