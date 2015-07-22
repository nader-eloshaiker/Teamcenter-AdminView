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
import org.w3c.dom.Node;
import tceav.utils.ArrayListSorter;

/**
 *
 * @author NZR4DL
 */
public class NamedAclList extends ArrayList<NamedAcl> {

    private ArrayList<String> aclTypes;
    private Hashtable<String, Integer> aclTypeSize;
    private ArrayList<String> accessorTypes;
    private AccessControlHeader acHeader;

    /* Creates a new instance of AccessRuleList */
    public NamedAclList() {
        super();
        aclTypes = new ArrayList<String>();
        aclTypeSize = new Hashtable<String, Integer>();
        accessorTypes = new ArrayList<String>();
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

    public NamedAcl get(String s) {
        if (s.equals("")) {
            return null;
        }
        /* Used to support ruletree entry with trailing spaces */
        return get(indexOf(s.trim()));
    }

    /***************************************************************************
     * Text File Methods
     **************************************************************************/
    public void createAccessControlColumns(String s) {
        acHeader = new AccessControlHeader(s);
    }

    @Override
    public boolean add(NamedAcl namedACL) {
        String s = namedACL.getRuleType();

        if (aclTypes.indexOf(s) == -1) {
            aclTypes.add(s);
            aclTypeSize.put(s, 1);
            ArrayListSorter.sortStringArray(aclTypes);
        } else {
            int i = aclTypeSize.get(s) + 1;
            aclTypeSize.put(s, i);
        }

        return super.add(namedACL);
    }

    public NamedAcl addNewACL(String ruleName) {
        NamedAcl namedACL = new NamedAcl(ruleName, acHeader);

        if (add(namedACL)) {
            return namedACL;
        } else {
            return null;
        }
    }

    public boolean addNewAccessControl(String str) {
        AccessControl ac = new AccessControl(str, acHeader);

        if (accessorTypes.indexOf(ac.getTypeOfAccessor()) == -1) {
            accessorTypes.add(ac.getTypeOfAccessor());
            ArrayListSorter.sortStringArray(accessorTypes);
        }

        return get(size() - 1).add(ac);//accessRule.add(ac);
    }

    /***************************************************************************
     * XML Methods
     **************************************************************************/
    public void createAccessControlColumns() {
        acHeader = new AccessControlHeader();
    }

    public NamedAcl addNewACL(Node namedAclNode) {
        if (acHeader == null) {
            createAccessControlColumns();
        }

        NamedAcl namedACL = new NamedAcl(namedAclNode, acHeader);
        AccessControl ac;

        for (int i = 0; i < namedACL.size(); i++) {
            ac = namedACL.get(i);
            if (accessorTypes.indexOf(ac.getTypeOfAccessor()) == -1) {
                accessorTypes.add(ac.getTypeOfAccessor());
                ArrayListSorter.sortStringArray(accessorTypes);
            }
        }

        if (add(namedACL)) {
            return namedACL;
        } else {
            return null;
        }
    }

    /***************************************************************************
     * Helper Methods
     ***************************************************************************/
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
