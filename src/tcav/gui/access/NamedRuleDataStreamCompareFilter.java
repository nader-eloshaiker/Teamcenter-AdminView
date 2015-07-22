/*
 * NamedRuleDataStreamCompareFilter.java
 *
 * Created on 18 March 2008, 15:59
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tcav.gui.access;

import tcav.manager.access.AccessRule;
import tcav.manager.compare.CompareInterface;
import java.util.*;

/**
 *
 * @author nzr4dl
 */
public class NamedRuleDataStreamCompareFilter extends NamedRuleDataStreamAbstract implements NamedRuleDataStreamInterface {
    
    private boolean filterEqual;
    private boolean filterNotEqual;
    private boolean filterNotFound;
    
    /** Creates a new instance of NamedRuleDataStreamCompareFilter */
    public NamedRuleDataStreamCompareFilter(NamedRuleDataStreamInterface model) {
        this.model = model;
        reallocateIndexes();
        
        filterEqual = false;
        filterNotEqual = false;
        filterNotFound = false;
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
    
    public void apply(){
        reallocateIndexes();
        if(isFilterNotFound() || isFilterNotEqual() || isFilterEqual())
            indexes = filter(indexes);
    }
    
    private int[] filter(int[] list){
        Vector<Integer> newList = new Vector<Integer>();
        int[] newIndexes;
        
        for(int i=0; i<list.length; i++)
            if(compareFilter(list[i]))
                newList.addElement(list[i]);
        
        newIndexes = new int[newList.size()];
        for(int j=0; j<newIndexes.length; j++)
            newIndexes[j] = newList.elementAt(j).intValue();
        
        return newIndexes;
    }
    
    private boolean compareFilter(int row) {
        int result = model.getAccessRule(row).getComparison();

        switch(result){
            case CompareInterface.EQUAL:
                if(isFilterEqual())
                    return true;
                else
                    return false;
                
            case CompareInterface.NOT_EQUAL:
                if(isFilterNotEqual())
                    return true;
                else
                    return false;
                
            case CompareInterface.NOT_FOUND:
                if(isFilterNotFound())
                    return true;
                else
                    return false;

            default:
                return false;
        }
    }
}
