/*
 * NodeReference.java
 *
 * Created on 5 August 2007, 15:02
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tcav.gui.procedure;

import tcav.plmxmlpdm.TagTypeEnum;
import javax.swing.JTree;
import javax.swing.tree.*;
import java.util.*;
/**
 *
 * @author NZR4DL
 */
public class NodeReference {
    
    private String id;
    private String parentId;
    private int index;
    private String name;
    private int entryType;
    private int procedureType;
    private TagTypeEnum classType;
    private TagTypeEnum parentClassType;
    private String iconKey;
    
    /* Entry Type */
    public static final int ENTRY_ITEM = 0;
    public static final int ENTRY_COLLECTOR = 1;
    
    /* Procedure Tree Type */
    public static final int PROCEDURE_SITE = 0;
    public static final int PROCEDURE_DEPENDANT_TASKS = 1;
    public static final int PROCEDURE_SUB_WORKFLOW = 2;
    public static final int PROCEDURE_WORKFLOW_ACTION = 3;
    public static final int PROCEDURE_WORKFLOW_HANDLER = 4;
    public static final int PROCEDURE_WORKFLOW_BUSINESS_RULE = 5;
    public static final int PROCEDURE_WORKFLOW_BUSINESS_RULE_HANDLER = 6;
    public static final int PROCEDURE_WORKFLOW_PROCESS = 7;
    public static final int PROCEDURE_ATTRIBUTE = 7;
    
    
    /**
     * Creates a new instance of NodeReference
     */
    public NodeReference(String id, String name, int entryType, int procedureType, TagTypeEnum classType, String iconKey) {
        if(id.charAt(0)== '#')
            this.id = id.substring(1);
        else
            this.id = id;
        this.name = name;
        this.entryType = entryType;
        this.procedureType = procedureType;
        this.iconKey = iconKey;
        this.classType = classType;
    }
    
    public NodeReference(String id, String name, int entryType, int procedureType, TagTypeEnum classType) {
        this(id, name, entryType, procedureType, classType, null);
    }
    
    public String getId(){
        return id;
    }
    
    /*
     * Used for Attributes which have no globally accessible id.
     * AttributeOwnerBase Id is placed here to gain access to the Attribute Id
     */
    public String getParentId() {
        return parentId;
    }
    
    public TagTypeEnum getParentClassType() {
        return parentClassType;
    }
    
    public int getIndex(){
        return index;
    }
    
    /*
     * Used for elements which have no Id
     */
    public void setIndex(int index) {
        this.index = index;
    }
    
    public void setParentId(String parentId){
        if(parentId.charAt(0)== '#')
            this.parentId = parentId.substring(1);
        else
            this.parentId = parentId;
    }
    
    public void setParentClassType(TagTypeEnum parentClassType) {
        this.parentClassType = parentClassType;
    }
    
    public String getName(){
        return name;
    }
    
    public int getEntryType() {
        return entryType;
    }
    
    public int getProcedureType() {
        return procedureType;
    }
    
    public TagTypeEnum getClassType() {
        return classType;
    }
    
    public String getIconKey() {
        return iconKey;
    }
    
    public String toString(){
        if(name != null)
            return name;
        else
            return id;
    }
    
}
