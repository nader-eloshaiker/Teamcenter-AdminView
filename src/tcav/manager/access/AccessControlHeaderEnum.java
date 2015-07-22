/*
 * AccessControlHeaderEnum.java
 *
 * Created on 10 January 2008, 12:01
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tcav.manager.access;

import tcav.resources.*;
import tcav.resources.ImageEnum;

/**
 *
 * @author nzr4dl
 */
public enum AccessControlHeaderEnum {
    
    AccessorType("TYPEACCESSOR", "Type Of Accessor", ImageEnum.aclAccessorType),
    AccessorID("IDACCESSOR", "Id Of Accessor", ImageEnum.aclAccessorID),
    AssignToProject("ASSIGN_TO_PROJECT", "Assign to Project", ImageEnum.aclAssignToProject),
    Change("CHANGE", "Change", ImageEnum.aclChange),
    ChangeOwnership("CHANGE_OWNER", "Change Ownership", ImageEnum.aclChangeOwnership),
    CheckInCheckOut("CICO", "CheckIn CheckOut", ImageEnum.aclCheckInCheckOut),
    Classification("WRITE_ICOS", "Write Classification ICO's", ImageEnum.aclClassification),
    Copy("COPY", "Copy", ImageEnum.aclCopy),
    Delete("DELETE", "Delete", ImageEnum.aclDelete),
    Demote("DEMOTE", "Demote", ImageEnum.aclDemote),
    Export("EXPORT", "Export", ImageEnum.aclExport),
    Import("IMPORT", "Import", ImageEnum.aclImport),
    IPAdmin("IP_ADMIN", "IP Admin", ImageEnum.aclIPAdmin),
    ITARAdmin("ITAR_ADMIN", "ITAR Admin", ImageEnum.aclITARAdmin),
    Promote("PROMOTE", "Promote", ImageEnum.aclPromote),
    Publish("PUBLISH", "Publish", ImageEnum.aclPublish),
    Read("READ", "Read", ImageEnum.aclRead),
    RemoteCICO("REMOTE_CICO", "Remote Checkout", ImageEnum.aclRemoteCICO),
    RemoveFromProject("REMOVE_FROM_PROJECT", "Remove From Project", ImageEnum.aclRemoveFromProject),
    Subscribe("SUBSCRIBE", "Subscribe", ImageEnum.aclSubscribe),
    TransferIn("TRANSFER_IN", "Transfer In", ImageEnum.aclTransferIn),
    TransferOut("TRANSFER_OUT", "Transfer Out", ImageEnum.aclTransferOut),
    Write("WRITE", "Write", ImageEnum.aclWrite),
    Unamanged("UNMANAGE", "Unmanaged",ImageEnum.aclUnmanaged),
    Undefined("UNDEFINED", null, ImageEnum.aclUndefined);
    
    private String value;
    private String desc;
    private ImageEnum image;
    
    /**
     * Creates a new instance of AccessControlHeaderEnum
     */
    AccessControlHeaderEnum(String value, String desc, ImageEnum image) {
        this.value = value;
        this.desc = desc;
        this.image = image;
    }
    
    public String value() {
        return value;
    }
    
    private void setValue(String value) {
        this.value = value;
    }
    
    public String description() {
        return desc;
    }
    
    public ImageEnum image() {
        return image;
    }
    
    public static AccessControlHeaderEnum fromValue(String v) {
        for (AccessControlHeaderEnum c: AccessControlHeaderEnum.values())
            if (c.value.equals(v))
                return c;
        
        AccessControlHeaderEnum result = Undefined;
        result.setValue(v);
        return result;
    }
    
}
