/*
 * AccessRuleList.java
 *
 * Created on 20 June 2007, 10:22
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package tceav.manager.access;

import java.util.ArrayList;
import java.util.Hashtable;
import tceav.utils.ArrayListSorter;

/**
 *
 * @author NZR4DL
 */
public class AccessRuleList extends ArrayList<AccessRule> {

    private ArrayList<String> aclTypes;
    private Hashtable<String, Integer> aclTypeSize;
    private ArrayList<String> accessorTypes;
    private AccessControlHeader acHeader;

    /** Creates a new instance of AccessRuleList */
    public AccessRuleList() {
        super();
        aclTypes = new ArrayList<String>();
        aclTypeSize = new Hashtable<String, Integer>();
        accessorTypes = new ArrayList<String>();
    }

    public void createAccessControlColumns(String s) {
        acHeader = new AccessControlHeader(s);
    }

    public AccessControlHeader getAccessControlColumns() {
        return acHeader;
    }

    public boolean contains(String s) {
        return indexOf(s, 0) >= 0;
    }

    public int indexOf(String s) {
        return indexOf(s, 0);
    }

    public int indexOf(String str, int index) {
        if (str == null) {
            for (int i = index; i < size(); i++) {
                if (get(i) == null) {
                    return i;
                }
            }
        } else {
            for (int i = index; i < size(); i++) {
                if (str.equals(get(i).getRuleName())) {
                    return i;
                }
            }
        }
        return -1;
    }

    public AccessRule get(String s) {
        /* Used to support ruletree entry with trailing spaces */
        return get(indexOf(s.trim()));
    }

    @Override
    public boolean add(AccessRule ar) {
        ar.setColumns(acHeader.getColumns());
        String s = ar.getRuleType();

        if (aclTypes.indexOf(s) == -1) {
            aclTypes.add(s);
            aclTypeSize.put(s, 1);
            ArrayListSorter.sortStringArray(aclTypes);
        } else {
            int i = aclTypeSize.get(s) + 1;
            aclTypeSize.put(s, i);
        }

        return super.add(ar);
    }
    
    AccessRule accessRule;

    public boolean addNewRule(String ruleName) {
        accessRule = new AccessRule(ruleName);
        return add(accessRule);
    }

    public boolean addNewAccessControl(String accessControl) {
        AccessControl ac = new AccessControl(accessControl);
        
        if (accessorTypes.indexOf(ac.getTypeOfAccessor()) == -1) {
            accessorTypes.add(ac.getTypeOfAccessor());
            ArrayListSorter.sortStringArray(accessorTypes);
        }

        return accessRule.add(ac);
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
