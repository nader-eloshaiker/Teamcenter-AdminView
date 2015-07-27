/*
 * AccessControlHeaderItem.java
 *
 * Created on 10 January 2008, 14:19
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tceav.manager.access;

import tceav.resources.ImageEnum;

/**
 *
 * @author nzr4dl
 */
public class AccessControlHeaderItem {
    
    private final String value;
    private final String desc;
    private final ImageEnum image;
    
    public static final String ACCESSOR_TYPE = "accessor_type";
    public static final String ACCESSOR = "accessor";
    public static final String ADMIN_ADA_LIC = "Administer_ADA_Licenses";
    public static final String ASSIGN_TO_PROJECT = "ASSIGN_TO_PROJECT";
    public static final String BATCH_PRINT = "BATCH_PRINT";
    public static final String CHANGE = "CHANGE";
    public static final String CHANGE_OWNER = "CHANGE_OWNER";
    public static final String CICO = "CICO";
    public static final String WRITE_ICOS = "WRITE_ICOS";
    public static final String COPY = "COPY";
    public static final String DELETE = "DELETE";
    public static final String DEMOTE = "DEMOTE";
    public static final String DIGITAL_SIGN = "DIGITAL_SIGN";
    public static final String EXPORT = "EXPORT";
    public static final String IMPORT = "IMPORT";
    public static final String IP_ADMIN = "IP_ADMIN";
    public static final String ITAR_ADMIN = "ITAR_ADMIN";
    public static final String MARKUP = "MARKUP";
    public static final String PROMOTE = "PROMOTE";
    public static final String PUBLISH = "PUBLISH";
    public static final String READ = "READ";
    public static final String REMOTE_CICO = "REMOTE_CICO";
    public static final String REMOVE_FROM_PROJECT = "REMOVE_FROM_PROJECT";
    public static final String SUBSCRIBE = "SUBSCRIBE";
    public static final String TRANSFER_IN = "TRANSFER_IN";
    public static final String TRANSFER_OUT = "TRANSFER_OUT";
    public static final String TRANSLATION = "TRANSLATION";
    public static final String WRITE = "WRITE";
    public static final String UNMANAGE = "UNMANAGE";
    public static final String ITAR_CLASSIFIER = "ITAR_Classifier";
    public static final String IP_CLASSIFIER = "IP_Classifier";
    public static final String ADD_CONTENT = "ADD_CONTENT";
    public static final String REMOVE_CONTENT = "REMOVE_CONTENT";
    
    /** Creates a new instance of AccessControlHeaderItem */
    public AccessControlHeaderItem(String value) {
        this.value = value;
        
        switch (value) {
            case ACCESSOR_TYPE:
                desc = "Type Of Accessor";
                image = ImageEnum.aclAccessorType;
                break;
                
            case ACCESSOR:
                desc = "Id Of Accessor";
                image = ImageEnum.aclAccessorID;
                break;
                
            case ADMIN_ADA_LIC:
                desc = "Administer ADA Lisence";
                image = ImageEnum.aclAdministerADALicense;
                break;

            case ASSIGN_TO_PROJECT:
                desc = "Assign to Project";
                image = ImageEnum.aclAssignToProject;
                break;

            case BATCH_PRINT:
                desc = "Batch Print";
                image = ImageEnum.aclBatchPrint;
                break;

            case CHANGE:
                desc = "Change";
                image = ImageEnum.aclChange;
                break;

            case CHANGE_OWNER:
                desc = "Change Ownership";
                image = ImageEnum.aclChangeOwnership;
                break;

            case CICO:
                desc = "CheckIn CheckOut";
                image = ImageEnum.aclCheckInCheckOut;
                break;

            case WRITE_ICOS:
                desc = "Write Classification ICO's";
                image = ImageEnum.aclClassification;
                break;

            case COPY:
                desc = "Copy";
                image = ImageEnum.aclCopy;
                break;

            case DELETE:
                desc = "Delete";
                image = ImageEnum.aclDelete;
                break;

            case DEMOTE:
                desc = "Demote";
                image = ImageEnum.aclDemote;
                break;

            case DIGITAL_SIGN:
                desc = "Digital Sign";
                image = ImageEnum.aclDigitalSign;
                break;

            case EXPORT:
                desc = "Export";
                image = ImageEnum.aclExport;
                break;

            case IMPORT:
                desc = "Import";
                image = ImageEnum.aclImport;
                break;

            case IP_ADMIN:
                desc = "IP Admin";
                image = ImageEnum.aclIPAdmin;
                break;

            case ITAR_ADMIN:
                desc = "ITAR Admin";
                image = ImageEnum.aclITARAdmin;
                break;

            case MARKUP:
                desc = "Markup";
                image = ImageEnum.aclMarkUp;
                break;

            case PROMOTE:
                desc = "Promote";
                image = ImageEnum.aclPromote;
                break;

            case PUBLISH:
                desc = "Publish";
                image = ImageEnum.aclPublish;
                break;

            case READ:
                desc = "Read";
                image = ImageEnum.aclRead;
                break;

            case REMOTE_CICO:
                desc = "Remote Checkout";
                image = ImageEnum.aclRemoteCICO;
                break;

            case REMOVE_FROM_PROJECT:
                desc = "Remove From Project";
                image = ImageEnum.aclRemoveFromProject;
                break;

            case SUBSCRIBE:
                desc = "Subscribe";
                image = ImageEnum.aclSubscribe;
                break;

            case TRANSFER_IN:
                desc = "Transfer In";
                image = ImageEnum.aclTransferIn;
                break;

            case TRANSFER_OUT:
                desc = "Transfer Out";
                image = ImageEnum.aclTransferOut;
                break;

            case TRANSLATION:
                desc = "Translation";
                image = ImageEnum.aclTranslation;
                break;

            case WRITE:
                desc = "Write";
                image = ImageEnum.aclWrite;
                break;

            case UNMANAGE:
                desc = "Unmanage";
                image = ImageEnum.aclUnmanage;
                break;
                
            case ITAR_CLASSIFIER:
                desc = "ITAR Classifier";
                image = ImageEnum.aclITARClassifier;
                break;
                
            case IP_CLASSIFIER:
                desc = "IP Classifier";
                image = ImageEnum.aclIPClassifier;
                break;
                
            case ADD_CONTENT:
                desc = "Add Content";
                image = ImageEnum.aclAddContent;
                break;
                
            case REMOVE_CONTENT:
                desc = "Remove Content";
                image = ImageEnum.aclRemoveContent;
                break;
                
            default:
                desc = value;
                image = ImageEnum.aclUndefined;
                break;
        }
        
    }
    
    public String value() {
        return this.value;
    }
    
    public String description() {
        if(desc != null)
            return desc;
        else
            return desc+": No Description";
    }
    
    public ImageEnum image() {
        return image;
    }

    public boolean equals(AccessControlHeaderItem a) {
        return value.equals(a.value());
    }
}
