/*
 * WorkflowTreeData.java
 *
 * Created on 5 August 2007, 09:23
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package tceav.gui.procedure;

import java.util.ArrayList;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import tceav.manager.procedure.plmxmlpdm.base.IdBase;
import tceav.manager.procedure.plmxmlpdm.type.SiteType;
import tceav.manager.procedure.plmxmlpdm.type.WorkflowTemplateType;

/**
 *
 * @author NZR4DL
 */
public class WorkflowTreeData implements TreeModel {
    
    private ArrayList<WorkflowTemplateType> workflowProcesses;
    private SiteType site;
    
    /** Creates a new instance of RuleTreeModel */
    public WorkflowTreeData(ArrayList<WorkflowTemplateType> workflowProcesses, SiteType site) {
        this.workflowProcesses = workflowProcesses;
        this.site = site;
    }
    
    public Object getRoot() {
        return site;
    }
    
    public Object getChild(Object parent, int index) {
        
        if(parent instanceof SiteType){
            return workflowProcesses.get(index);
            
        } else if(parent instanceof WorkflowTemplateType) {
            return ((WorkflowTemplateType) parent).getSubTemplates()[index];
            
        } else {
            System.out.println("getChild -ITEM- default Class Type should not reach this point");
            return null;
            
        }
    }
    
    public int getChildCount(Object parent) {
        
        if(parent instanceof SiteType){
            return workflowProcesses.size();
            
        } else if(parent instanceof WorkflowTemplateType) {
            WorkflowTemplateType wt = (WorkflowTemplateType) parent;
            return ((WorkflowTemplateType) parent).getSubTemplates().length;
            
        } else {
            System.out.println("getChildCount -ITEM- default should not reach this point");
            return 0;
        }
    }
    
    public boolean isLeaf(Object node) {
        
        if(node instanceof SiteType){
            return (workflowProcesses.size() == 0);
            
        } else if(node instanceof WorkflowTemplateType) {
            WorkflowTemplateType wt = (WorkflowTemplateType) node;
            return (((WorkflowTemplateType) node).getSubTemplates().length == 0);
            
        } else {
            System.out.println("isLeaf -ITEM- default class type should not reach this point");
            return true;
        }
    }
    
    public int getIndexOfChild(Object parent, Object child) {
        IdBase childProcedure = (IdBase) child;
        
        if(parent instanceof SiteType){
            return workflowProcesses.indexOf((WorkflowTemplateType) child);
            
        } else if(parent instanceof WorkflowTemplateType) {
            return ((WorkflowTemplateType) parent).getSubTemplateRefs().indexOf(((WorkflowTemplateType) child).getId());
            
        } else {
            System.out.println("getIndexOfChild -ITEM- default class type should not reach this point");
            return -1;
        }
    }
    
    public void addTreeModelListener(TreeModelListener listener) {
        // not editable
    }
    
    public void removeTreeModelListener(TreeModelListener listener) {
        // not editable
    }
    
    public void valueForPathChanged(TreePath path, Object newValue) {
        // not editable
    }
}


