/*
 * SearchTableComponent.java
 *
 * Created on 25 September 2007, 09:14
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tceav.gui.tools.search;

import java.util.ArrayList;
import tceav.gui.tools.table.JTableAdvanced;
import tceav.manager.access.NamedAcl;
/**
 *
 * @author NZR4DL
 */
public abstract class SearchTableComponent extends SearchAdapter implements SearchTableComparator {
    private ArrayList<Integer> resultsRowLocations;
    
    /** Creates a new instance of SearchTableComponent */
    public SearchTableComponent() {
        resultsRowLocations = new ArrayList<Integer>();
    }
    
    public void addResult(int row) {
        resultsRowLocations.add(row);
    }
    
    public int getResult() {
        return resultsRowLocations.get(getResultIndex());
    }
    
    public int getResult(int index) {
        return resultsRowLocations.get(index);
    }
    
    public int getResultSize() {
        return resultsRowLocations.size();
    }
    
    public void resetResults() {
        resultsRowLocations.clear();
        resetResultIndex();
    }

    public boolean searchNext(JTableAdvanced table) {
        incrementResultIndex();
        if(getResultIndex() >= getResultSize())
            resetResultIndex();
        
        if(getResultSize() > 0) {
            table.setRowSelectionInterval(getResult(),getResult());
            table.getSelectionModel().setAnchorSelectionIndex(getResult());
            table.scrollRectToVisible(
                    table.getCellRect(
                    table.getSelectionModel().getAnchorSelectionIndex(),
                    table.getColumnModel().getSelectionModel().getAnchorSelectionIndex(),
                    false));
            return true;
        } else
            return false;
    }

    public boolean search(JTableAdvanced table, String type, String value) {
        resetResults();
        searchTable(table, type, value);
        
        if(getResultSize() > 0) {
            table.setRowSelectionInterval(getResult(),getResult());
            table.getSelectionModel().setAnchorSelectionIndex(getResult());
            table.scrollRectToVisible(
                    table.getCellRect(
                    table.getSelectionModel().getAnchorSelectionIndex(),
                    table.getColumnModel().getSelectionModel().getAnchorSelectionIndex(),
                    false));
            return true;
        } else
            return false;
    }

    private void searchTable(JTableAdvanced table, String type, String value) {
        Boolean matched;
        NamedAcl ar;
        int index;
        
        for(int i=0; i<table.getRowCount(); i++) {
            if(compare(i,type, value))
                addResult(i);
            
        }
    }
}
