/*
 * AccessControlColumns.java
 *
 * Created on 18 June 2007, 11:26
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package tcav.ruletree;

/**
 *
 * @author NZR4DL
 */
public class AccessControlColumns {
    
    private final String[] ACCESS_CONTROL_NAMES = new String[]{
        "TYPEACCESSOR",
        "IDACCESSOR",
        "READ",
        "WRITE",
        "DELETE",
        "CHANGE",
        "PROMOTE",
        "DEMOTE",
        "COPY",
        "EXPORT",
        "IMPORT",
        "TRANSFER_OUT",
        "TRANSFER_IN",
        "CHANGE_OWNER",
        "PUBLISH",
        "SUBSCRIBE",
        "WRITE_ICOS",
        "ASSIGN_TO_PROJECT",
        "REMOVE_FROM_PROJECT"
    };
    private final String[] ACCESS_CONTROL_DESCRIPTIONS = new String[]{
        "Type Of Accessor",
        "Id Of Accessor",
        "Read",
        "Write",
        "Delete",
        "Change",
        "Promote",
        "Demote",
        "Copy",
        "Export",
        "Import",
        "Transfer Out",
        "Transfer In",
        "Change Ownership",
        "Publish",
        "Subscribe",
        "Write Classification ICO's",
        "Assign to Project",
        "Remove From Project"
    };
    private final String[] ACCESS_CONTROL_IMAGES = new String[]{
        "AccessorType.gif",
        "AccessorId.gif",
        "ReadAccess.gif",
        "WriteAccess.gif",
        "DeleteAccess.gif",
        "ChangeAccess.gif",
        "PromoteAccess.gif",
        "DemoteAccess.gif",
        "CopyAccess.gif",
        "ExportAccess.gif",
        "ImportAccess.gif",
        "TransferOutAccess.gif",
        "TransferInAccess.gif",
        "ChangeOwnerShipAccess.gif",
        "PublishAccess.gif",
        "SubscribeAccess.gif",
        "ClassificationAccess.gif",
        "AssignToProjectAccess.gif",
        "RemoveFromProjectAccess.gif"
    };
    
    private AccessControlColumnsEntry[] column;
    
    /**
     * Creates a new instance of AccessControlColumns
     */
    public AccessControlColumns() {
        column = new AccessControlColumnsEntry[0];
    }
    
    public void setAccessControlColumns(String str) {
        String[] s = str.split("!");
        column = new AccessControlColumnsEntry[s.length+2];
                    
        for (int i=0; i<column.length; i++){
        

            column[i] = new AccessControlColumnsEntry();
            
            if(i == 0)
                column[0].setName("TYPEACCESSOR");
            else if(i == 1)
                column[1].setName("IDACCESSOR");
            else
                column[i].setName(s[i-2]);
            
            column[i].setDescription(ACCESS_CONTROL_DESCRIPTIONS[ACCESS_CONTROL_DESCRIPTIONS.length-1]);
            column[i].setIconName(ACCESS_CONTROL_IMAGES[ACCESS_CONTROL_IMAGES.length-1]);

            //Dynamically build the descriptions to the names
            for (int j=0; j<ACCESS_CONTROL_NAMES.length; j++) {
                if (column[i].getName().equals(ACCESS_CONTROL_NAMES[j])){
                    column[i].setDescription(ACCESS_CONTROL_DESCRIPTIONS[j]);
                    column[i].setIconName(ACCESS_CONTROL_IMAGES[j]);
                    break;
                }
            }
        }
    }
    
    public String getName(int i) {
        return column[i].getName();
    }
    
    public String getDescritpion(int i) {
        return column[i].getDescription();
    }
    
    public AccessControlColumnsEntry getColumn(int index) {
        return column[index];
    }

    public AccessControlColumnsEntry[] getColumns() {
        return column;
    }
    
    public int getSize() {
        return column.length;
    }
    
    public String toString() {
        String s = "";
        for(int i=0; i<column.length; i++)
            s += column[i].getName()+"!";
        return s;
    }
    
     public String generateExportString() {
        String s = "";
        for(int i=2; i<column.length; i++)
            s += column[i].getName()+"!";
        return s;
    }
}
