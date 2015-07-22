/*
 * HandlerTreeData.java
 *
 * Created on 8 October 2007, 12:40
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tceav.gui.procedure;



import java.util.ArrayList;
import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.event.*;
import tceav.manager.procedure.plmxmlpdm.base.IdBase;
import tceav.manager.procedure.plmxmlpdm.base.AttribOwnerBase;
import tceav.manager.procedure.plmxmlpdm.type.WorkflowActionType;
import tceav.manager.procedure.plmxmlpdm.type.WorkflowBusinessRuleHandlerType;
import tceav.manager.procedure.plmxmlpdm.type.WorkflowBusinessRuleType;
import tceav.manager.procedure.plmxmlpdm.type.WorkflowHandlerType;
import tceav.manager.procedure.plmxmlpdm.type.WorkflowTemplateType;
import tceav.manager.procedure.plmxmlpdm.type.UserDataType;
import tceav.manager.procedure.plmxmlpdm.type.element.UserDataElementType;


/**
 *
 * @author NZR4DL
 */
public class HandlerTreeData implements TreeModel {
    
    private IdBase root;
    
    public static final int MODE_DEPENDANT_TASKS = 0;
    public static final int MODE_SUB_WORKFLOWS = 1;
    
    /** Creates a new instance of RuleTreeModel */
    public HandlerTreeData(IdBase root) {
        this.root = root;
    }
    
    public HandlerTreeData() {
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
                int actionSize = wa.getActionHandlers().length;
                int ruleSize = wa.getRules().length;
                /*
                if((actionSize != 0) && (index < actionSize))
                    return wa.getActionHandlers()[index];
                else if((ruleSize != 0) && (index >= actionSize))
                    return wa.getRules()[index - actionSize];
                else
                    return null;//getChild WorkflowActionType should not reach this point
                */
                if((ruleSize != 0) && (index < ruleSize))
                    return wa.getRules()[index];
                else if((actionSize != 0) && (index >= ruleSize))
                    return wa.getActionHandlers()[index - ruleSize];
                else
                    return null;//getChild WorkflowActionType should not reach this point
                
            case WorkflowBusinessRule:
                WorkflowBusinessRuleType br = (WorkflowBusinessRuleType)parent;
                return br.getRuleHandlers()[index];
                
            case WorkflowHandler:
            case WorkflowBusinessRuleHandler:
                AttribOwnerBase aob = (AttribOwnerBase)parent;
                int counter = 0;
                UserDataType ud;
                
                for(int i=0; i<aob.getAttribute().size(); i++) {
                    ud =  (UserDataType)aob.getAttribute().get(i);
                    for(int k=0; k<ud.getUserValue().size(); k++) {
                        if(counter == index)
                            return ud.getUserValue().get(k);
                        else
                            counter++;
                    }
                }
                return null;

            case UserValue:
                UserDataElementType uv = (UserDataElementType)parent;
                return uv.getData();
                
            default:
                return null; //getChild -ITEM- default Class Type should not reach this point
        }
    }
    
    public int getChildCount(Object parent){
        
        switch(((IdBase)parent).getTagType()){
            case WorkflowTemplate:
                WorkflowTemplateType wt = (WorkflowTemplateType)parent;
                return wt.getActions().length;
                
            case WorkflowAction:
                WorkflowActionType wa = (WorkflowActionType)parent;
                return wa.getRules().length + wa.getActionHandlers().length;
                
            case WorkflowBusinessRule:
                WorkflowBusinessRuleType br = (WorkflowBusinessRuleType)parent;
                return br.getRuleHandlers().length;
                
            case WorkflowHandler:
            case WorkflowBusinessRuleHandler:
                AttribOwnerBase aob = (AttribOwnerBase)parent;
                int len = 0;
                for(int i=0; i<aob.getAttribute().size(); i++)
                    len += ((UserDataType)aob.getAttribute().get(i)).getUserValue().size();
                return len;

            case UserValue:
                UserDataElementType uv = (UserDataElementType)parent;
                if(uv.getDataRef() == null)
                    return 0;
                else
                    return 1;
                
            case Site:
                return 0;
                
            default:
                return 0; //getChildCount -ITEM- default should not reach this point
                
        }
    }
    
    public boolean isLeaf(Object node){
        
        return (getChildCount(node) == 0);
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
                    case WorkflowBusinessRule:
                        br = (WorkflowBusinessRuleType)child;
                        return wa.getRuleRefs().indexOf(br.getId());
                        
                    case WorkflowHandler:
                        WorkflowHandlerType wh = (WorkflowHandlerType)child;
                        return wa.getActionHandlerRefs().indexOf(wh.getId()) + wa.getRuleRefs().size();
                        
                    default:
                        return -1; //getIndexOfChild WorkflowActionType should not reach this point
                        
                }
                
            case WorkflowBusinessRule:
                br = (WorkflowBusinessRuleType)parent;
                WorkflowBusinessRuleHandlerType brh = (WorkflowBusinessRuleHandlerType)child;
                return br.getRuleHandlerRefs().indexOf(br.getId());
                
            case WorkflowHandler:
            case WorkflowBusinessRuleHandler:
                AttribOwnerBase aob = (AttribOwnerBase)parent;
                int index = 0;
                int result;
                UserDataType ud;
                UserDataElementType userValue = (UserDataElementType)child;
                
                for(int i=0; i<aob.getAttribute().size(); i++) {
                    ud =  (UserDataType)aob.getAttribute().get(i);
                    result = ud.getUserValue().indexOf(userValue);
                    if ( result > -1) {
                        index += result;
                        return index;
                    } else
                        index += ud.getUserValue().size()-1;
                }
                return -1;

            case UserValue:
                UserDataElementType uv = (UserDataElementType)parent;
                if(uv.getDataRef() == null)
                    return -1;
                else
                    return 0;
                
            default:
                return -1; //getIndexOfChild -ITEM- default class type should not reach this point
                
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


