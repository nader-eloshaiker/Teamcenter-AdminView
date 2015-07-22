/*
 * SearchTreeComponent.java
 *
 * Created on 25 September 2007, 07:27
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tceav.gui.tools.search;

import java.util.ArrayList;
import tceav.gui.tools.tree.JTreeAdvanced;
import javax.swing.tree.TreePath;

/**
 *
 * @author NZR4DL
 */
public abstract class SearchTreeComponent extends SearchAdapter implements SearchTreeComparator{
    
    private ArrayList<TreePath> resultsTreeLocations;
    
    /** Creates a new instance of SearchTreeComponent */
    public SearchTreeComponent() {
        resultsTreeLocations = new ArrayList<TreePath>();
    }
    
    public void addResult(TreePath path) {
        resultsTreeLocations.add(path);
    }
    
    public TreePath getResult() {
        return resultsTreeLocations.get(getResultIndex());
    }
    
    public TreePath getResult(int index) {
        return resultsTreeLocations.get(index);
    }
    
    public int getResultSize() {
        return resultsTreeLocations.size();
    }
    
    public void resetResults() {
        resultsTreeLocations.clear();
        resetResultIndex();
    }
    
    public boolean searchNext(JTreeAdvanced tree) {
        incrementResultIndex();
        if(getResultIndex() >= getResultSize())
            resetResultIndex();
        
        if(getResultSize() > 0) {
            tree.expandPath(getResult());
            tree.setSelectionPath(getResult());
            tree.scrollPathToVisible(getResult());
            return true;
        } else
            return false;
    }
    
    public boolean search(JTreeAdvanced tree, String type, String value) {
        resetResults();
            searchTree(tree, tree.getPathForRow(0), type, value);
        if(getResultSize() > 0) {
            tree.expandPath(getResult());
            tree.scrollPathToVisible(getResult());
            tree.setSelectionPath(getResult());
            return true;
        } else
            return false;
    }
    
    private void searchTree(JTreeAdvanced tree, TreePath parent, String type, String value) {
        // Traverse children
        if(compare(parent, type, value))
            addResult(parent);
        
        int childCount = tree.getModel().getChildCount(parent.getLastPathComponent());
        if(childCount > 0) {
            for (int e=0; e<childCount; e++ ) {
                TreePath path = parent.pathByAddingChild(tree.getModel().getChild(parent.getLastPathComponent(), e));
                searchTree(tree, path, type, value);
            }
        }
    }
}
