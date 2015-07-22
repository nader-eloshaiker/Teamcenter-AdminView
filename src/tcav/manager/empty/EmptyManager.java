/*
 * EmptyManager.java
 *
 * Created on 3 March 2008, 11:09
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tcav.manager.empty;

import tcav.manager.AbstractManager;
import java.io.File;

/**
 *
 * @author nzr4dl
 */
public class EmptyManager extends AbstractManager {
    
    public static final String MANAGER_TYPE = "EMPTY";
    
    /** Creates a new instance of EmptyManager */
    public void readFile(File file) throws Exception {
    }
    
    public boolean isValid() { 
        return true; 
    }
    
    public String getManagerType() {
        return super.EMPTY_MANAGER_TYPE;
    }

    public String getId() {
        return "empty";
    }
    
    public String getName() {
        return "empty";
    }
}
