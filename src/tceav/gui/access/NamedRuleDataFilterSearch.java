/*
 * NamedRuleDataFilterSearch.java
 *
 * Created on 18 March 2008, 11:02
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tceav.gui.access;

import tceav.manager.access.NamedAcl;
import java.util.*;

/**
 *
 * @author nzr4dl
 */
public class NamedRuleDataFilterSearch extends NamedRuleDataFilterAdapter implements NamedRuleDataFilterInterface {
    
    private String strPattern;
    private ArrayList<Integer> filteringColumns = new ArrayList<Integer>();
    private ArrayList<String> filteringPatterns = new ArrayList<String>();
    
    /**
     * Creates a new instance of NamedRuleDataFilterSearch
     */
    public NamedRuleDataFilterSearch(NamedRuleDataFilterInterface model) {
        this.model = model;
        reallocateIndexes();
    }
    
    public void setFilter(int column, String value) {
        if((value == null) || (value.equals("")))
            return;
        
        filteringColumns.clear();
        filteringColumns.add(new Integer(column));
        filteringPatterns.clear();
        filteringPatterns.add(value);
    }
    
    public void setFilter(int[] columns, String[] patterns) {
        filteringColumns.clear();
        filteringPatterns.clear();
        
        int length;
        if (columns.length < getColumnCount())
            length = columns.length;
        else
            length = getColumnCount();
        
        for(int i=0; i<length; i++) {
            if((patterns[i] != null) &&(!patterns[i].equals(""))) {
                filteringColumns.add(new Integer(columns[i]));
                filteringPatterns.add(patterns[i]);
            }
        }
    }
    
    public String getFilterPattern(int column) {
        //if (filteringColumns.size() == 0)
        //    return null;
        int i = filteringColumns.indexOf(new Integer(column));
        if(i == -1)
            return null;
        else
            return filteringPatterns.get(i);
    }
    
    public String[] getFilterPatterns() {
        String[] patterns = new String[filteringPatterns.size()];
        for(int i=0; i<patterns.length; i++)
            patterns[i] = filteringPatterns.get(i);
        return patterns;
    }
    
    public int[] getFilterColumns() {
        int[] columns = new int[filteringColumns.size()];
        for(int i=0; i<columns.length; i++)
            columns[i] = filteringColumns.get(i).intValue();
        return columns;
    }
    
    public void resetFilter(){
        filteringColumns.clear();
        filteringPatterns.clear();
        reallocateIndexes();
    }
    
    protected void filter() {
        indexes = searchFilter(indexes);
    }
    
    private int[] searchFilter(int[] list){
        Vector<Integer> newList = new Vector<Integer>();
        int[] newIndexes;
        
        for(int i=0; i<list.length; i++)
            if(compare(list[i]))
                newList.addElement(list[i]);
        
        newIndexes = new int[newList.size()];
        for(int j=0; j<newIndexes.length; j++)
            newIndexes[j] = newList.elementAt(j).intValue();
        
        return newIndexes;
    }
    
    private boolean compare(int row) {
        boolean result;
        for (int level = 0; level < filteringColumns.size(); level++) {
            Integer column = filteringColumns.get(level);
            result = compare(row, filteringPatterns.get(level), column.intValue());
            if (!result) {
                return result;
            }
        }
        return true;
    }
    
    private boolean compare(int row, String strPattern, int column) {
        // Check for nulls.
        Object o1 = model.getValueAt(row, column);
        // If both values are null, return 0.
        if (strPattern == null && o1 == null) {
            return true;
        } else if (o1 == null) { // Define null less than everything.
            return false;
        } else if (strPattern == null) {
            return false;
        }
        
        String s;
        int i1;
        Integer i2;
        switch(column) {
            case TYPE_COLUMN:
                s = model.getAccessRule(row).getRuleType();
                return isStringMatch(s,strPattern);
                
            case INSTANCES_COLUMN:
                i1 = model.getAccessRule(row).getRuleTreeReferences().size();
                i2 = new Integer(strPattern);
                
                if (i1 == i2.intValue())
                    return true;
                else
                    return false;
                
            case NAME_COLUMN:
                s = model.getAccessRule(row).getRuleName();
                return isStringMatch(s,strPattern);
                
            default:
                s = model.getValueAt(row, column).toString();
                return isStringMatch(s,strPattern);
        }
    }
    
}
