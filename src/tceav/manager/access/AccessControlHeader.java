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

        add(new AccessControlHeaderItem(AccessControlHeaderEnum.AccessorType));
        add(new AccessControlHeaderItem(AccessControlHeaderEnum.Accessor));
        add(new AccessControlHeaderItem(AccessControlHeaderEnum.Read));
        add(new AccessControlHeaderItem(AccessControlHeaderEnum.Write));
        add(new AccessControlHeaderItem(AccessControlHeaderEnum.Delete));
        add(new AccessControlHeaderItem(AccessControlHeaderEnum.Change));
        add(new AccessControlHeaderItem(AccessControlHeaderEnum.Promote));
        add(new AccessControlHeaderItem(AccessControlHeaderEnum.Demote));
        add(new AccessControlHeaderItem(AccessControlHeaderEnum.Copy));
        add(new AccessControlHeaderItem(AccessControlHeaderEnum.Export));
        add(new AccessControlHeaderItem(AccessControlHeaderEnum.Import));
        add(new AccessControlHeaderItem(AccessControlHeaderEnum.TransferOut));
        add(new AccessControlHeaderItem(AccessControlHeaderEnum.TransferIn));
        add(new AccessControlHeaderItem(AccessControlHeaderEnum.ChangeOwnership));
        add(new AccessControlHeaderItem(AccessControlHeaderEnum.Publish));
        add(new AccessControlHeaderItem(AccessControlHeaderEnum.Subscribe));
        add(new AccessControlHeaderItem(AccessControlHeaderEnum.Accessor));
        add(new AccessControlHeaderItem(AccessControlHeaderEnum.Classification));
        add(new AccessControlHeaderItem(AccessControlHeaderEnum.AssignToProject));
        add(new AccessControlHeaderItem(AccessControlHeaderEnum.RemoveFromProject));
        add(new AccessControlHeaderItem(AccessControlHeaderEnum.Unamanged));
        add(new AccessControlHeaderItem(AccessControlHeaderEnum.IPAdmin));
        add(new AccessControlHeaderItem(AccessControlHeaderEnum.ITARAdmin));
        add(new AccessControlHeaderItem(AccessControlHeaderEnum.CheckInCheckOut));
        add(new AccessControlHeaderItem(AccessControlHeaderEnum.RemoteCICO));
        add(new AccessControlHeaderItem(AccessControlHeaderEnum.AdministerADALicense));
        add(new AccessControlHeaderItem(AccessControlHeaderEnum.Translation));
        add(new AccessControlHeaderItem(AccessControlHeaderEnum.MarkUp));
        add(new AccessControlHeaderItem(AccessControlHeaderEnum.BatchPrint));
        add(new AccessControlHeaderItem(AccessControlHeaderEnum.DigitalSign));
    }

    public AccessControlHeader(String str) {
        super(32);

        add(new AccessControlHeaderItem(AccessControlHeaderEnum.AccessorType));
        add(new AccessControlHeaderItem(AccessControlHeaderEnum.Accessor));

        String[] s = str.split("!");
        for (int i = 0; i < s.length; i++) {
            add(new AccessControlHeaderItem(AccessControlHeaderEnum.fromValue(s[i])));
        }
    }

    public int indexOfAccessControl(AccessControlHeaderEnum item) {
        for (int i = 0; i < size(); i++) {
            if (get(i).getEnum().equals(item)) {
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
