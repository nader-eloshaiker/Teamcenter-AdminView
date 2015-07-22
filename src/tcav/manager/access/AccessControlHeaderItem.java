/*
 * AccessControlHeaderItem.java
 *
 * Created on 10 January 2008, 14:19
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tcav.manager.access;

import tcav.resources.ImageEnum;

/**
 *
 * @author nzr4dl
 */
public class AccessControlHeaderItem {
    
    private String value;
    private String desc;
    private ImageEnum image;
    
    /** Creates a new instance of AccessControlHeaderItem */
    public AccessControlHeaderItem(AccessControlHeaderEnum item) {
        value = item.value();
        desc = item.description();
        image = item.image();
    }
    
    public String value() {
        return value;
    }
    
    public String description() {
        if(desc != null)
            return desc;
        else
            return value+": No Description";
    }
    
    public ImageEnum image() {
        return image;
    }
}
