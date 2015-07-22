/*
 * HandlerTreeData.java
 *
 * Created on 8 October 2007, 12:40
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tceav.gui.procedure;



import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import tceav.manager.procedure.plmxmlpdm.base.IdBase;
import tceav.manager.procedure.plmxmlpdm.base.AttribOwnerBase;
import tceav.manager.procedure.plmxmlpdm.type.SiteType;
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
public class ActionTreeData implements TreeModel {
    
    private IdBase root;
    
    public static final int MODE_DEPENDANT_TASKS = 0;
    public static final int MODE_SUB_WORKFLOWS = 1;
    
    /** Creates a new instance of RuleTreeModel */
    public ActionTreeData(IdBase root) {
        this.root = root;
    }
    
    public ActionTreeData() {
        this.root = null;
    }
    
    public Object getRoot(){
        return root;
    }
    
    public Object getChild(Object parent, int index){
        
        if(parent instanceof WorkflowTemplateType) {
            WorkflowTemplateType wt = (WorkflowTemplateType)parent;
            return wt.getActions()[index];
            
        } else if(parent instanceof WorkflowActionType) {
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
            
        } else if(parent instanceof WorkflowBusinessRuleType) {
            WorkflowBusinessRuleType br = (WorkflowBusinessRuleType)parent;
            return br.getRuleHandlers()[index];
            
        } else if(parent instanceof WorkflowHandlerType || parent instanceof WorkflowBusinessRuleHandlerType) {
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
            
        } else if (parent instanceof UserDataElementType) {
            UserDataElementType uv = (UserDataElementType)parent;
            return uv.getData();
            
        } else {
            return null; //getChild -ITEM- default Class Type should not reach this point
        }
    }
    
    public int getChildCount(Object parent){
        
        if(parent instanceof WorkflowTemplateType) {
            WorkflowTemplateType wt = (WorkflowTemplateType)parent;
            return wt.getActions().length;
            
        } else if(parent instanceof WorkflowActionType) {
            WorkflowActionType wa = (WorkflowActionType)parent;
            return wa.getRules().length + wa.getActionHandlers().length;
            
        } else if(parent instanceof WorkflowBusinessRuleType) {
            WorkflowBusinessRuleType br = (WorkflowBusinessRuleType)parent;
            return br.getRuleHandlers().length;
            
        } else if(parent instanceof WorkflowHandlerType || parent instanceof WorkflowBusinessRuleHandlerType) {
            AttribOwnerBase aob = (AttribOwnerBase)parent;
            int len = 0;
            for(int i=0; i<aob.getAttribute().size(); i++)
                len += ((UserDataType)aob.getAttribute().get(i)).getUserValue().size();
            return len;
            
        } else if (parent instanceof UserDataElementType) {
            UserDataElementType uv = (UserDataElementType)parent;
            if(uv.getDataRef() == null)
                return 0;
            else
                return 1;
            
        } else if(parent instanceof SiteType) {
            return 0;
            
        } else {
            return 0; //getChildCount -ITEM- default should not reach this point
            
        }
    }
    
    public boolean isLeaf(Object node){
        
        return (getChildCount(node) == 0);
    }
    
    public int getIndexOfChild(Object parent, Object child){
        WorkflowActionType wa;
        WorkflowBusinessRuleType br;
        
        if(parent instanceof WorkflowTemplateType) {
            WorkflowTemplateType wt = (WorkflowTemplateType)parent;
            wa = (WorkflowActionType)child;
            return wt.getActionRefs().indexOf(wa.getId());
            
        } else if(parent instanceof WorkflowActionType) {
            wa = (WorkflowActionType)parent;
            if(child instanceof WorkflowBusinessRuleType) {
                br = (WorkflowBusinessRuleType)child;
                return wa.getRuleRefs().indexOf(br.getId());
                
            } else if (child instanceof WorkflowHandlerType) {
                WorkflowHandlerType wh = (WorkflowHandlerType)child;
                return wa.getActionHandlerRefs().indexOf(wh.getId()) + wa.getRuleRefs().size();
                
            } else {
                return -1; //getIndexOfChild WorkflowActionType should not reach this point
                
            }
            
        } else if(parent instanceof WorkflowBusinessRuleType) {
            br = (WorkflowBusinessRuleType)parent;
            WorkflowBusinessRuleHandlerType brh = (WorkflowBusinessRuleHandlerType)child;
            return br.getRuleHandlerRefs().indexOf(br.getId());
            
        } else if(parent instanceof WorkflowHandlerType || parent instanceof WorkflowBusinessRuleHandlerType) {
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
            
        } else if (parent instanceof UserDataElementType) {
            UserDataElementType uv = (UserDataElementType)parent;
            if(uv.getDataRef() == null)
                return -1;
            else
                return 0;
            
        } else {
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


