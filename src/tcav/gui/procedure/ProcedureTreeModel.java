/*
 * ProcedureTreeModel.java
 *
 * Created on 5 August 2007, 09:23
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
public class ProcedureTreeModel implements TreeModel {
    
    private ArrayList<WorkflowTemplateType> workflowProcesses;
    private SiteType site;
    private int procedureMode;
    
    public static final int MODE_DEPENDANT_TASKS = 0;
    public static final int MODE_SUB_WORKFLOWS = 1;
    
    /** Creates a new instance of RuleTreeModel */
    public ProcedureTreeModel(ArrayList<WorkflowTemplateType> workflowProcesses, SiteType site, int procedureMode) {
        this.workflowProcesses = workflowProcesses;
        this.site = site;
        this.procedureMode = procedureMode;
    }
    
    public Object getRoot(){
        return site;
    }
    
    public Object getChild(Object parent, int index){
        
        switch(((IdBase)parent).getTagType()){
            case Site:
                return workflowProcesses.get(index);
                
            case WorkflowTemplate:
                WorkflowTemplateType wt = (WorkflowTemplateType)parent;
                if(procedureMode == MODE_DEPENDANT_TASKS)
                    return wt.getDependantTaskTemplates()[index];
                else if(procedureMode == MODE_SUB_WORKFLOWS)
                    return wt.getSubTemplates()[index];
                else
                    return null;
                
            default:
                System.out.println("getChild -ITEM- default Class Type should not reach this point");
                return null;
        }
    }
    
    public int getChildCount(Object parent){
        
        switch(((IdBase)parent).getTagType()){
            case Site:
                return workflowProcesses.size();
                
            case WorkflowTemplate:
                WorkflowTemplateType wt = (WorkflowTemplateType)parent;
                
                if(procedureMode == MODE_DEPENDANT_TASKS)
                    return wt.getDependantTaskTemplates().length;
                else if(procedureMode == MODE_SUB_WORKFLOWS)
                    return wt.getSubTemplates().length;
                else
                    return 0;
                
            default:
                System.out.println("getChildCount -ITEM- default should not reach this point");
                return 0;
        }
    }
    
    public boolean isLeaf(Object node){
        
        switch(((IdBase)node).getTagType()){
            case Site:
                return (workflowProcesses.size() == 0);
                
            case WorkflowTemplate:
                WorkflowTemplateType wt =(WorkflowTemplateType)node;
                
                if(procedureMode == MODE_DEPENDANT_TASKS)
                    return (wt.getDependantTaskTemplates().length == 0);
                else if(procedureMode == MODE_SUB_WORKFLOWS)
                    return (wt.getSubTemplates().length == 0);
                else
                    return true;
                
            default:
                System.out.println("isLeaf -ITEM- default class type should not reach this point");
                return true;
        }
    }
    
    public int getIndexOfChild(Object parent, Object child){
        IdBase childProcedure = (IdBase)child;
        
        switch(((IdBase)parent).getTagType()){
            case Site:
                return workflowProcesses.indexOf((WorkflowTemplateType)child);
                
            case WorkflowTemplate:
                WorkflowTemplateType wtParent = (WorkflowTemplateType)parent;
                if(procedureMode == MODE_DEPENDANT_TASKS)
                    return wtParent.getDependencyTaskTemplateRefs().indexOf(((WorkflowTemplateType)child).getId());
                else if(procedureMode == MODE_SUB_WORKFLOWS)
                    return wtParent.getSubTemplateRefs().indexOf(((WorkflowTemplateType)child).getId());
                else
                    return -1;
                
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


