/*
 * AccessControlHeader.java
 *
 * Created on 18 June 2007, 11:26
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package tceav.manager.access;

import java.util.ArrayList;

/**
 *
 * @author NZR4DL
 */
public class AccessControlHeader extends ArrayList<AccessControlHeaderItem> {

    /**
     * Creates a new instance of AccessControlHeader
     */
    public AccessControlHeader() {
        super(32);

        add(new AccessControlHeaderItem(AccessControlHeaderItem.ACCESSOR_TYPE));
        add(new AccessControlHeaderItem(AccessControlHeaderItem.ACCESSOR));
        add(new AccessControlHeaderItem(AccessControlHeaderItem.READ));
        add(new AccessControlHeaderItem(AccessControlHeaderItem.WRITE));
        add(new AccessControlHeaderItem(AccessControlHeaderItem.DELETE));
        add(new AccessControlHeaderItem(AccessControlHeaderItem.CHANGE));
        add(new AccessControlHeaderItem(AccessControlHeaderItem.PROMOTE));
        add(new AccessControlHeaderItem(AccessControlHeaderItem.DEMOTE));
        add(new AccessControlHeaderItem(AccessControlHeaderItem.COPY));
        add(new AccessControlHeaderItem(AccessControlHeaderItem.EXPORT));
        add(new AccessControlHeaderItem(AccessControlHeaderItem.IMPORT));
        add(new AccessControlHeaderItem(AccessControlHeaderItem.TRANSFER_OUT));
        add(new AccessControlHeaderItem(AccessControlHeaderItem.TRANSFER_IN));
        add(new AccessControlHeaderItem(AccessControlHeaderItem.CHANGE_OWNER));
        add(new AccessControlHeaderItem(AccessControlHeaderItem.PUBLISH));
        add(new AccessControlHeaderItem(AccessControlHeaderItem.SUBSCRIBE));
        add(new AccessControlHeaderItem(AccessControlHeaderItem.WRITE_ICOS));
        add(new AccessControlHeaderItem(AccessControlHeaderItem.ASSIGN_TO_PROJECT));
        add(new AccessControlHeaderItem(AccessControlHeaderItem.REMOVE_FROM_PROJECT));
        add(new AccessControlHeaderItem(AccessControlHeaderItem.UNMANAGE));
        add(new AccessControlHeaderItem(AccessControlHeaderItem.IP_ADMIN));
        add(new AccessControlHeaderItem(AccessControlHeaderItem.ITAR_ADMIN));
        add(new AccessControlHeaderItem(AccessControlHeaderItem.CICO));
        add(new AccessControlHeaderItem(AccessControlHeaderItem.REMOTE_CICO));
        add(new AccessControlHeaderItem(AccessControlHeaderItem.ADMIN_ADA_LIC));
        add(new AccessControlHeaderItem(AccessControlHeaderItem.TRANSLATION));
        add(new AccessControlHeaderItem(AccessControlHeaderItem.MARKUP));
        add(new AccessControlHeaderItem(AccessControlHeaderItem.BATCH_PRINT));
        add(new AccessControlHeaderItem(AccessControlHeaderItem.DIGITAL_SIGN));
        add(new AccessControlHeaderItem(AccessControlHeaderItem.ITAR_CLASSIFIER));
        add(new AccessControlHeaderItem(AccessControlHeaderItem.IP_CLASSIFIER));
        add(new AccessControlHeaderItem(AccessControlHeaderItem.ADD_CONTENT));
        add(new AccessControlHeaderItem(AccessControlHeaderItem.REMOVE_CONTENT));
    }

    public AccessControlHeader(String str) {
        super(32);

        add(new AccessControlHeaderItem(AccessControlHeaderItem.ACCESSOR_TYPE));
        add(new AccessControlHeaderItem(AccessControlHeaderItem.ACCESSOR));

        AccessControlHeaderItem acHeader;
        
        String[] s = str.split("!");
        for (String item : s) {
            acHeader = new AccessControlHeaderItem(item);
            add(acHeader);
        }
    }

    public int indexOfAccessControl(String item) {
        for (int i = 0; i < size(); i++) {
            if (get(i).value().equals(item)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public String toString() {
        String s = "";
        for (int i = 0; i < size(); i++) {
            s += get(i).value() + "!";
        }
        return s;
    }

    public String generateExportString() {
        String s = "";
        for (int i = 2; i < size(); i++) {
            s += get(i).value() + "!";
        }
        return s;
    }
}
