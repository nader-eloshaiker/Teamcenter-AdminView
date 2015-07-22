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
import tcav.procedure.*;
import tcav.plmxmlpdm.*;
import tcav.plmxmlpdm.type.*;
import tcav.plmxmlpdm.classtype.*;
import tcav.plmxmlpdm.base.*;


/**
 *
 * @author NZR4DL
 */
public class ActionTreeModel implements TreeModel {
    
    private IdBase root;
    
    public static final int MODE_DEPENDANT_TASKS = 0;
    public static final int MODE_SUB_WORKFLOWS = 1;
    
    /** Creates a new instance of RuleTreeModel */
    public ActionTreeModel(IdBase root) {
        this.root = root;
    }
    
    public ActionTreeModel() {
        this.root = null;
    }

    public Object getRoot(){
        return root;
    }
    
    public Object getChild(Object parent, int index){
        
        switch(((IdBase)parent).getTagType()){
            case WorkflowTemplate:
                WorkflowTemplateType wt = (WorkflowTemplateType)parent;
                return wt.getActions()[index];
                
            case WorkflowAction:
                WorkflowActionType wa = (WorkflowActionType)parent;
                int handlerSize = wa.getActionHandlers().length;
                
                if((handlerSize != 0) && (index < handlerSize))
                    return wa.getActionHandlers()[index];
                else if((wa.getRules().length != 0) && (index >= handlerSize))
                    return wa.getRules()[index - handlerSize];
                else
                    System.out.println("getChild WorkflowActionType should not reach this point");
                
                return null;
                
            case WorkflowBusinessRule:
                WorkflowBusinessRuleType br = (WorkflowBusinessRuleType)parent;
                return br.getRuleHandlers()[index];
                
            default:
                System.out.println("getChild -ITEM- default Class Type should not reach this point");
                return null;
        }
    }
    
    public int getChildCount(Object parent){
        
        switch(((IdBase)parent).getTagType()){
            case WorkflowTemplate:
                WorkflowTemplateType wt = (WorkflowTemplateType)parent;
                return wt.getActions().length;
                
            case WorkflowAction:
                WorkflowActionType wa = (WorkflowActionType)parent;
                return wa.getActionHandlers().length + wa.getRules().length;
                
            case WorkflowBusinessRule:
                WorkflowBusinessRuleType br = (WorkflowBusinessRuleType)parent;
                return br.getRuleHandlers().length;
                
                
            case WorkflowBusinessRuleHandler:
            case WorkflowHandler:
            case Site:
                return 0;
                
            default:
                System.out.println("getChildCount -ITEM- default should not reach this point");
                return 0;
        }
    }
    
    public boolean isLeaf(Object node){
        
        switch(((IdBase)node).getTagType()){
            case WorkflowTemplate:
                WorkflowTemplateType wt = (WorkflowTemplateType)node;
                return (wt.getActions().length == 0);
                
            case WorkflowAction:
                WorkflowActionType wa = (WorkflowActionType)node;
                return (wa.getActionHandlers().length + wa.getRules().length == 0);
                
            case WorkflowBusinessRule:
                WorkflowBusinessRuleType br = (WorkflowBusinessRuleType)node;
                return (br.getRuleHandlers().length == 0);
                
            case WorkflowBusinessRuleHandler:
            case WorkflowHandler:
            case Site:
                return true;
                
            default:
                System.out.println("isLeaf -ITEM- default class type should not reach this point");
                return true;
        }
    }
    
    public int getIndexOfChild(Object parent, Object child){
        WorkflowActionType wa;
        WorkflowBusinessRuleType br;
        
        switch(((IdBase)parent).getTagType()){
            case WorkflowTemplate:
                WorkflowTemplateType wt = (WorkflowTemplateType)parent;
                wa = (WorkflowActionType)child;
                return wt.getActionRefs().indexOf(wa.getId());
                
            case WorkflowAction:
                wa = (WorkflowActionType)parent;
                switch(((IdBase)child).getTagType()){
                    case WorkflowHandler:
                        WorkflowHandlerType wh = (WorkflowHandlerType)child;
                        return wa.getActionHandlerRefs().indexOf(wh.getId());
                        
                    case WorkflowBusinessRule:
                        br = (WorkflowBusinessRuleType)child;
                        return wa.getRuleRefs().indexOf(br.getId());
                        
                    default:
                        System.out.println("getIndexOfChild WorkflowActionType should not reach this point");
                        return -1;
                }
                
            case WorkflowBusinessRule:
                br = (WorkflowBusinessRuleType)parent;
                WorkflowBusinessRuleHandlerType brh = (WorkflowBusinessRuleHandlerType)child;
                return br.getRuleHandlerRefs().indexOf(br.getId());
                
            default:
                System.out.println("getIndexOfChild -ITEM- default class type should not reach this point");
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


