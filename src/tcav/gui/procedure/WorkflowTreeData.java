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
    
    private final ArrayList<WorkflowTemplateType> workflowProcesses;
    private final SiteType site;
    
    /** Creates a new instance of RuleTreeModel
     * @param workflowProcesses
     * @param site */
    public WorkflowTreeData(ArrayList<WorkflowTemplateType> workflowProcesses, SiteType site) {
        this.workflowProcesses = workflowProcesses;
        this.site = site;
    }
    
    @Override
    public Object getRoot() {
        return site;
    }
    
    @Override
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
    
    @Override
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
    
    @Override
    public boolean isLeaf(Object node) {
        
        if(node instanceof SiteType){
            return (workflowProcesses.isEmpty());
            
        } else if(node instanceof WorkflowTemplateType) {
            WorkflowTemplateType wt = (WorkflowTemplateType) node;
            return (((WorkflowTemplateType) node).getSubTemplates().length == 0);
            
        } else {
            System.out.println("isLeaf -ITEM- default class type should not reach this point");
            return true;
        }
    }
    
    @Override
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
    
    @Override
    public void addTreeModelListener(TreeModelListener listener) {
        // not editable
    }
    
    @Override
    public void removeTreeModelListener(TreeModelListener listener) {
        // not editable
    }
    
    @Override
    public void valueForPathChanged(TreePath path, Object newValue) {
        // not editable
    }
}


