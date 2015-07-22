/*
 * RuleTreeSelectionListener.java
 *
 * Created on 4 April 2008, 13:57
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tceav.gui.access;

import javax.swing.table.*;
import javax.swing.tree.*;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import tceav.manager.access.RuleTreeNode;

/**
 *
 * @author nzr4dl
 */
public class RuleTreeSelectionListener implements TreeSelectionListener {
    private TreePath oldPath;
    private int index;
    private RuleTreeComponent[] ruleTreeList;
    private NamedRuleComponent namedRule;
    private AccessRuleComponent accessRule;
    
    
    public RuleTreeSelectionListener(int index, NamedRuleComponent[] namedRuleList, RuleTreeComponent[] ruleTreeList, AccessRuleComponent accessRule) {
        this(namedRuleList[index], accessRule);
        this.index = index;
        this.ruleTreeList = ruleTreeList;
    }
    
    public RuleTreeSelectionListener(int index, NamedRuleComponent[] namedRuleList, RuleTreeComponent[] ruleTreeList, AccessRuleComponent accessRule, boolean sync) {
        this(namedRuleList[index], accessRule);
        this.index = index;
        this.ruleTreeList = ruleTreeList;
    }
    
    public RuleTreeSelectionListener(NamedRuleComponent namedRule, AccessRuleComponent accessRule) {
        this.namedRule = namedRule;
        this.accessRule = accessRule;
        this.sync = true;
    }
    
    public RuleTreeSelectionListener(NamedRuleComponent namedRule, AccessRuleComponent accessRule, boolean sync) {
        this.namedRule = namedRule;
        this.accessRule = accessRule;
        this.sync = sync;
    }
    
    private boolean sync;
    
    public void setSync(boolean sync) {
        this.sync = sync;
    }
    
    public void valueChanged(TreeSelectionEvent e) {
        TreePath newPath = e.getPath();
        
        if(oldPath != null)
            if(e.isAddedPath(e.getPath()) && newPath.equals(oldPath))
                return;
        
        RuleTreeNode treeNode = (RuleTreeNode)e.getPath().getLastPathComponent();
        
        if(e.isAddedPath(e.getPath())){
            oldPath = newPath;
            
            if(treeNode.getAccessRule() != null){
                if(sync) {
                    int selIndex = namedRule.getModel().indexOfRuleName(treeNode.getAccessRuleName());
                    if(selIndex > -1) {
                        namedRule.getTable().setRowSelectionInterval(selIndex,selIndex);
                        namedRule.getTable().getSelectionModel().setAnchorSelectionIndex(selIndex);
                        namedRule.getTable().scrollRectToVisible(
                                namedRule.getTable().getCellRect(
                                namedRule.getTable().getSelectionModel().getAnchorSelectionIndex(),
                                namedRule.getTable().getColumnModel().getSelectionModel().getAnchorSelectionIndex(),
                                false)
                                );
                    } else
                        accessRule.updateTable(treeNode.getAccessRule());
                } else {
                    if(namedRule.getTable().getSelectedRowCount() != 0)
                         namedRule.getTable().clearSelection();
                    accessRule.updateTable(treeNode.getAccessRule());
                }
                
            } else {
                 if(sync)
                     if(namedRule.getTable().getSelectedRowCount() != 0)
                         namedRule.getTable().clearSelection();
                accessRule.updateTable();
            }
            
            if(ruleTreeList != null)
                for(int k=0; k<ruleTreeList.length; k++)
                    if(k != index)
                        if(ruleTreeList[k].getTree().getSelectionCount() != 0)
                            ruleTreeList[k].getTree().clearSelection();
            
        } else {
            oldPath = null;
            //namedRule.getTable().clearSelection();
            //accessRule.updateTable();
        }
    }
    
}

