/*
 * AccessRuleList.java
 *
 * Created on 20 June 2007, 10:22
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tcav.ruletree;
import java.util.Vector;
import java.util.Hashtable;
import java.util.ArrayList;
import tcav.utils.ArrayListSorter;

/**
 *
 * @author NZR4DL
 */
public class AccessRuleList extends Vector<AccessRule>{
    
    private Vector<String> aclTypes;
    private Hashtable<String, Integer> aclTypeSize;
    private Vector<String> accessorTypes;
    
    /** Creates a new instance of AccessRuleList */
    public AccessRuleList() {
        super();
        aclTypes = new Vector<String>();
        aclTypeSize = new Hashtable<String,Integer>();
        accessorTypes = new Vector<String>();
    }
    
    public boolean contains(String s) {
        return indexOf(s, 0) >= 0;
    }
    
    public synchronized int indexOf(String str) {
        return indexOf(str,0);
    }
    
    public synchronized int indexOf(String str, int index) {
        if (str == null) {
            for (int i = index ; i < elementCount ; i++)
                if (elementData[i]==null)
                    return i;
        } else {
            for (int i = index ; i < elementCount ; i++)
                if (str.equals( ((AccessRule)elementData[i]).getRuleName() ))
                    return i;
        }
        return -1;
    }
    
    public AccessRule elementAt(int index) {
        return (AccessRule)super.elementAt(index);
    }
    
    public void addElement(AccessRule ar){
        super.addElement(ar);
        String s = ar.getRuleType();
        
        if(aclTypes.indexOf(s) == -1){
            aclTypes.add(s);
            aclTypeSize.put(s,1);
            ArrayListSorter.sortStringArray(aclTypes);
        } else {
            int i = aclTypeSize.get(s)+1;
            aclTypeSize.put(s,i);
        }
        
        for(int i=0; i<ar.size(); i++) {
            String accessor = ar.get(i).getTypeOfAccessor();
            if(accessorTypes.indexOf(accessor) == -1) {
                accessorTypes.add(accessor);
                ArrayListSorter.sortStringArray(accessorTypes);
            }
        }
    }
    
    public Vector<String> getACLTypes() {
        return aclTypes;
    }
    
    public int getACLTypeSize(String key) {
        return aclTypeSize.get(key);
    }
    
    public Vector<String> getAccessorTypes() {
        return accessorTypes;
    }
    
}
