/*
 * AccessRuleList.java
 *
 * Created on 20 June 2007, 10:22
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tcav.ruletree;
import java.util.*;
import tcav.utils.ArrayListSorter;

/**
 *
 * @author NZR4DL
 */
public class AccessRuleList extends ArrayList<AccessRule>{
    
    private ArrayList<String> aclTypes;
    private Hashtable<String, Integer> aclTypeSize;
    private ArrayList<String> accessorTypes;
    
    /** Creates a new instance of AccessRuleList */
    public AccessRuleList() {
        super();
        aclTypes = new ArrayList<String>();
        aclTypeSize = new Hashtable<String,Integer>();
        accessorTypes = new ArrayList<String>();
    }
    
    public boolean contains(String s) {
        return indexOf(s, 0) >= 0;
    }
    
    public int indexOf(String s) {
        return indexOf(s,0);
    }
    
    public int indexOf(String str, int index) {
        if (str == null) {
            for (int i=index; i<size(); i++)
                if (get(i)==null)
                    return i;
        } else {
            for (int i=index; i<size(); i++)
                if (str.equals(get(i).getRuleName()))
                    return i;
        }
        return -1;
    }
    
    public AccessRule get(String s) {
        return get(indexOf(s));
    }
    
    public boolean add(AccessRule ar){
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
        
        return super.add(ar);
    }
    
    public ArrayList<String> getACLTypes() {
        return aclTypes;
    }
    
    public int getACLTypeSize(String key) {
        return aclTypeSize.get(key);
    }
    
    public ArrayList<String> getAccessorTypes() {
        return accessorTypes;
    }
    
}
