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
    
    private AccessControlHeaderEnum entry;
    
    /** Creates a new instance of AccessControlHeaderItem */
    public AccessControlHeaderItem(AccessControlHeaderEnum item) {
        entry = item;
    }
    
    public String value() {
        return entry.value();
    }
    
    public String description() {
        if(entry.description() != null)
            return entry.description();
        else
            return entry.value()+": No Description";
    }
    
    public ImageEnum image() {
        return entry.image();
    }

    public AccessControlHeaderEnum getEnum() {
        return entry;
    }
    public boolean equals(AccessControlHeaderItem a) {
        return entry.equals(a.getEnum());
    }
}
