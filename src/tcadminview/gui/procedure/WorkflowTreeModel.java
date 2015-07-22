/*
 * WorkflowTreeModel.java
 *
 * Created on 5 August 2007, 09:23
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tcadminview.gui.procedure;
import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.event.*;
import tcadminview.procedure.ProcedureManager;
/**
 *
 * @author NZR4DL
 */
public class WorkflowTreeModel implements TreeModel {
    
    private ProcedureManager pm;
    
    /** Creates a new instance of RuleTreeModel */
    public WorkflowTreeModel(ProcedureManager pm) {
        this.pm = pm;
    }
    
    
    public Object getRoot(){
        if(pm.getWorkflowTemplates().size() == 0)
            return null;
        else
            return pm.getSites();
    }
    
    public Object getChild(Object parent, int index){
        return new Object();
    }
    
    
    public int getChildCount(Object parent){
        return 0;
    }
    
    public boolean isLeaf(Object node){
        return false;
    }
    
    public int getIndexOfChild(Object parent, Object child){
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


