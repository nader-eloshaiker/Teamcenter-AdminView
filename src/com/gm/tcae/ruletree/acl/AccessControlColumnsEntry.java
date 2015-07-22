/*
 * AccessControlColumnsEntry.java
 *
 * Created on 27 June 2007, 14:56
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.gm.tcae.ruletree.acl;

/**
 *
 * @author nzr4dl
 */
public class AccessControlColumnsEntry {
    
    private String name;
    private String description;
    private String icon;
    
    /** Creates a new instance of AccessControlColumnsEntry */
    public AccessControlColumnsEntry() {
    }
    
    public String toString(){
        return description;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        if (description == null)
            return name+": No description found";
        else
            return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getIconName() {
        if(icon == null)
            return "DefaultError.gif";
        else
            return icon;
    }
    
    public void setIconName(String icon) {
        this.icon = icon;
    }
}
