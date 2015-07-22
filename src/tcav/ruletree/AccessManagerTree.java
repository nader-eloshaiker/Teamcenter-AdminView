/*
 * AccessManagerTree.java
 *
 * Created on 26 June 2007, 16:12
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package tcav.ruletree;

import java.util.Vector;
import tcav.utils.ArrayListSorter;

/**
 *
 * @author NZR4DL
 */
public class AccessManagerTree extends Vector<AccessManagerItem> {
    
    Vector<String> conditionsList;
    
    /** Creates a new instance of AccessManagerTree */
    public AccessManagerTree() {
        super();
        conditionsList = new Vector<String>();
    }
    
    public AccessManagerItem elementAt(int index) {
        return (AccessManagerItem)super.elementAt(index);
    }
    
    public void addElement(AccessManagerItem amItem) {
        super.addElement(amItem);
        
        String s = amItem.getCondition();
        
        if(conditionsList.indexOf(s) == -1) {
            conditionsList.add(s);
            ArrayListSorter.sortStringArray(conditionsList);
        }
    }
    
    public Vector<String> getConditions() {
        return conditionsList;
    }
    
}
