/*
 * WorkflowTreeItem.java
 *
 * Created on 5 August 2007, 15:02
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tcadminview.gui.procedure;

import tcadminview.procedure.*;
import tcadminview.plmxmlpdm.*;
import tcadminview.plmxmlpdm.type.*;
import tcadminview.plmxmlpdm.base.*;
import tcadminview.plmxmlpdm.classtype.*;
import javax.swing.JTree;
import javax.swing.tree.*;
import java.util.*;
/**
 *
 * @author NZR4DL
 */
public class WorkflowTreeItem {
    
    private String id;
    private String name;
    private int type;
    private String iconKey;
    
    public static final int ITEM = 0;
    public static final int DEPENDANT_TASKS = 1;
    public static final int SUB_WORKFLOW = 2;
    public static final int WORKFLOW_ACTION = 3;
    public static final int WORKFLOW_HANDLER = 4;
    public static final int WORKFLOW_BUSINESS_RULE = 5;
    public static final int WORKFLOW_BUSINESS_RULE_HANDLER = 6;
    
    
    /** Creates a new instance of WorkflowTreeItem */
    public WorkflowTreeItem(String id, String name, int type, String iconKey) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.iconKey = iconKey;
    }
    
    /** Creates a new instance of WorkflowTreeItem */
    public WorkflowTreeItem(String name, int type) {
        this(null, name, type, null);
    }
    
    /** Creates a new instance of WorkflowTreeItem */
    public WorkflowTreeItem(String id, String name) {
        this(id, name, 0, null);
    }
    
    /** Creates a new instance of WorkflowTreeItem */
    public WorkflowTreeItem(String id, String name, String iconKey) {
        this(id, name, 0, iconKey);
    }
    
    public String getId(){
        return id;
    }
    
    public String getName(){
        return name;
    }
    
    public int getType() {
        return type;
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
