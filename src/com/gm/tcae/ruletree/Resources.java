/*
 * Resources.java
 *
 * Created on 13 July 2007, 11:56
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.gm.tcae.ruletree;

import java.net.URL;
import java.io.IOException;
/**
 *
 * @author nzr4dl
 */
public class Resources {
    
    /**
     * Creates a new instance of Resources
     */
    public Resources() {
    }
    
    public static URL getChangeLog() throws IOException {
        return Resources.class.getResource("Resources/ChangeLog.html");
    }
    
}
