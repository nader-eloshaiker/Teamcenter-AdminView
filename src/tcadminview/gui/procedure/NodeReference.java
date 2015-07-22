/*
 * NodeReference.java
 *
 * Created on 5 August 2007, 15:02
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tcadminview.gui.procedure;

import tcadminview.procedure.*;
import tcadminview.plmxmlpdm.TagTypeEnum;
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
    private int itemType;
    private TagTypeEnum classType;
    private TagTypeEnum parentClassType;
    private String iconKey;
    
    public static final int ITEM = 0;
    public static final int DEPENDANT_TASKS = 1;
    public static final int SUB_WORKFLOW = 2;
    public static final int WORKFLOW_ACTION = 3;
    public static final int WORKFLOW_HANDLER = 4;
    public static final int WORKFLOW_BUSINESS_RULE = 5;
    public static final int WORKFLOW_BUSINESS_RULE_HANDLER = 6;
    
    
    /**
     * Creates a new instance of NodeReference
     */
    public NodeReference(String id, String name, int itemType, String iconKey, TagTypeEnum classType) {
        if(id.charAt(0)== '#')
            this.id = id.substring(1);
        else
            this.id = id;
        this.name = name;
        this.itemType = itemType;
        this.iconKey = iconKey;
        this.classType = classType;
    }
    
    /** Worflow Collector Collector Entry */
    public NodeReference(String id, String name, int itemType, TagTypeEnum classType) {
        this(id, name, itemType, null, classType);
    }
    
    /** Tree item representing a Tag Entry used for Workflow Templates */
    public NodeReference(String id, String name, String iconKey, TagTypeEnum classType) {
        this(id, name, ITEM, iconKey, classType);
    }

    /** Tree item representing a Tag Entry */
    public NodeReference(String id, String name, TagTypeEnum classType) {
        this(id, name, ITEM, null, classType);
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
    
    public int getItemType() {
        return itemType;
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
