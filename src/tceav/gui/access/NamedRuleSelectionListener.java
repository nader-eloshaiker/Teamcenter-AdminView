/*
 * NamedRuleSelectionListener.java
 *
 * Created on 4 April 2008, 13:30
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tceav.gui.access;

import javax.swing.table.*;
import javax.swing.tree.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import java.util.ArrayList;
import tceav.manager.access.RuleTreeNode;
import tceav.gui.tools.tree.JTreeAdvanced;


/**
 *
 * @author nzr4dl
 */
public class NamedRuleSelectionListener implements ListSelectionListener {
    private int index;
    private NamedRuleComponent[] namedRuleList;
    private NamedRuleComponent namedRule;
    private RuleTreeComponent ruleTree;
    private AccessRuleComponent accessRule;
    
    
    public NamedRuleSelectionListener(int index, NamedRuleComponent[] namedRuleList, RuleTreeComponent[] ruleTreeList, AccessRuleComponent accessRule) {
        this(namedRuleList[index], ruleTreeList[index], accessRule);
        this.index = index;
        this.namedRuleList = namedRuleList;
    }
    
    public NamedRuleSelectionListener(int index, NamedRuleComponent[] namedRuleList, RuleTreeComponent[] ruleTreeList, AccessRuleComponent accessRule, boolean sync) {
        this(namedRuleList[index], ruleTreeList[index], accessRule, sync);
        this.index = index;
        this.namedRuleList = namedRuleList;
    }
    
    public NamedRuleSelectionListener(NamedRuleComponent namedRule, RuleTreeComponent ruleTree, AccessRuleComponent accessRule) {
        this.namedRule = namedRule;
        this.ruleTree = ruleTree;
        this.accessRule = accessRule;
        this.sync = true;
    }
    
    public NamedRuleSelectionListener(NamedRuleComponent namedRule, RuleTreeComponent ruleTree, AccessRuleComponent accessRule, boolean sync) {
        this.namedRule = namedRule;
        this.ruleTree = ruleTree;
        this.accessRule = accessRule;
        this.sync = sync;
    }

    private int indexOfTreePath(TreePath path, TreePath[] paths) {
        if((path == null) || (paths == null))
            return -1;
        
        for(int i=0; i<paths.length; i++)
            if(path.equals(paths[i]))
                return i;
        
        return -1;
    }
    
    private boolean isTreePathAvailable(TreePath[] src, TreePath[] dst) {
        for(int i=0; i<src.length; i++)
            if(indexOfTreePath(src[i], dst) > -1)
                return true;
        
        return false;
    }
    
    private TreePath[] getTreePaths(JTreeAdvanced tree, ArrayList<RuleTreeNode> components) {
        ArrayList<TreePath> paths = new ArrayList<TreePath>();
        
        searchTree(tree, tree.getPathForRow(0), paths, components);
        
        return paths.toArray(new TreePath[components.size()]);
    }
    
    private void searchTree(JTreeAdvanced tree, TreePath currentPath, ArrayList<TreePath> paths, ArrayList<RuleTreeNode> components) {
        if(components.indexOf((RuleTreeNode)currentPath.getLastPathComponent()) > -1)
            paths.add(currentPath);
        
        int childCount = tree.getModel().getChildCount(currentPath.getLastPathComponent());
        if(childCount > 0) {
            for (int e=0; e<childCount; e++ ) {
                TreePath newPath = currentPath.pathByAddingChild(tree.getModel().getChild(currentPath.getLastPathComponent(), e));
                searchTree(tree, newPath, paths, components);
            }
        }
    }
    
    private boolean sync;
    
    public void setSync(boolean sync) {
        this.sync = sync;
    }
    
    public void valueChanged(ListSelectionEvent e) {
        int i = namedRule.getTable().getSelectedRow();
        if (i > -1) {
            if(sync) {
                if(namedRule.getModel().getAccessRule(i).getRuleTreeReferences().size() > 0) {
                    
                    TreePath[] paths = getTreePaths(ruleTree.getTree(), namedRule.getModel().getAccessRule(i).getRuleTreeReferences());
                    if(!isTreePathAvailable(paths, ruleTree.getTree().getSelectionPaths())) {
                        ruleTree.getTree().setSelectionPaths(paths);
                        ruleTree.getTree().scrollPathToVisible(paths[0]);
                    }
                    
                } else {
                    if(ruleTree.getTree().getSelectionCount() > 0)
                        ruleTree.getTree().removeSelectionPaths(ruleTree.getTree().getSelectionPaths());
                }
            } else {
                if(ruleTree.getTree().getSelectionCount() > 0)
                        ruleTree.getTree().removeSelectionPaths(ruleTree.getTree().getSelectionPaths());
            }
            namedRule.updateReferences(i);
            accessRule.updateTable(namedRule.getModel().getAccessRule(i));
            
            if(namedRuleList != null) {
                for(int k=0; k<namedRuleList.length; k++) {
                    if(k != index){
                        if(namedRuleList[k].getTable().getSelectedRowCount() != 0) {
                            namedRuleList[k].getTable().clearSelection();
                        }
                    }
                }
            }
            
        } else
            accessRule.updateTable();
    }
}

