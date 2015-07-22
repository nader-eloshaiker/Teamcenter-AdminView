/*
 * AbstractManager.java
 *
 * Created on 3 March 2008, 10:26
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tcav.manager;

import java.io.File;

/**
 *
 * @author nzr4dl
 */
public abstract class AbstractManager {
    
    /** Creates a new instance of AbstractManager */
    protected File file;
    
    public File getFile() {
        return file;
    }
    
    /* Set file should be done from readFile method */
    abstract public void readFile(File file) throws Exception;
    
    abstract public boolean isValid();
    
    abstract public String getManagerType();
    
}
