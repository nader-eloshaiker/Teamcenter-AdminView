/*
 * AccessManagerTree.java
 *
 * Created on 26 June 2007, 16:12
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package tcadminview.ruletree;

import java.util.Vector;
import java.util.ArrayList;

/**
 *
 * @author NZR4DL
 */
public class AccessManagerTree extends Vector<AccessManagerItem> {
    
    ArrayList<String> conditionsList = new ArrayList<String>();
    
    /** Creates a new instance of AccessManagerTree */
    public AccessManagerTree() {
        super();
    }
    
    public AccessManagerItem elementAt(int index) {
        return (AccessManagerItem)super.elementAt(index);
    }
    
    public void addElement(AccessManagerItem amItem) {
        super.addElement(amItem);
        
        String s = amItem.getCondition();
        
        if(conditionsList.indexOf(s) == -1)
            conditionsList.add(s);
    }
    
    public String getCondition(int index) {
        return conditionsList.get(index);
    }
    
    public int getConditionSize() {
        return conditionsList.size();
    }
    
}
