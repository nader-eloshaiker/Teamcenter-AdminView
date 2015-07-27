/*
 * NamedRuleDataFilterCompare.java
 *
 * Created on 18 March 2008, 15:59
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tceav.gui.access;

import tceav.manager.compare.CompareInterface;
import java.util.*;

/**
 *
 * @author nzr4dl
 */
public class NamedRuleDataFilterCompare extends NamedRuleDataFilterAdapter implements NamedRuleDataFilterInterface {
    
    private boolean filterEqual;
    private boolean filterNotEqual;
    private boolean filterNotFound;
    
    /**
     * Creates a new instance of NamedRuleDataFilterCompare
     */
    public NamedRuleDataFilterCompare(NamedRuleDataFilterInterface model) {
        this.model = model;
        reallocateIndexes();
        
        filterEqual = true;
        filterNotEqual = true;
        filterNotFound = true;
    }
    
    public void setFilterEqual(boolean b) {
        filterEqual = b;
    }
    
    public boolean isFilterEqual() {
        return filterEqual;
    }
    
    public void setFilterNotEqual(boolean b) {
        filterNotEqual = b;
    }
    
    public boolean isFilterNotEqual() {
        return filterNotEqual;
    }
    
    public void setFilterNotFound(boolean b) {
        filterNotFound = b;
    }
    
    public boolean isFilterNotFound() {
        return filterNotFound;
    }
    
    @Override
    protected void filter(){
        if(!isFilterNotFound() || !isFilterNotEqual() || !isFilterEqual())
            indexes = compareFilter(indexes);
    }
    
    private int[] compareFilter(int[] list){
        ArrayList<Integer> newList;
        int[] newIndexes;
        
        newList = new ArrayList<>();
        
        for(int i=0; i<list.length; i++)
            if(compare(list[i]))
                newList.add(list[i]);
        
        newIndexes = new int[newList.size()];
        for(int j=0; j<newIndexes.length; j++)
            newIndexes[j] = newList.get(j);
        
        return newIndexes;
    }
    
    private boolean compare(int row) {
        int result = model.getAccessRule(row).getComparison();

        switch(result){
            case CompareInterface.EQUAL:
                return isFilterEqual();
                
            case CompareInterface.NOT_EQUAL:
                return isFilterNotEqual();
                
            case CompareInterface.NOT_FOUND:
                return isFilterNotFound();

            default:
                return false;
        }
    }
}
