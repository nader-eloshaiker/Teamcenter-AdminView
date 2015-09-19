/*
 * AbstractManager.java
 *
 * Created on 3 March 2008, 10:26
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tceav.manager;

import java.io.File;

/**
 *
 * @author nzr4dl
 */
public abstract class ManagerAdapter {
    
    public static final String EMPTY_MANAGER_TYPE = "EMPTY";
    public static final String ACCESS_MANAGER_TYPE = "ACCESS";
    public static final String ACCESS_COMPARE_MANAGER_TYPE = "ACCESS_COMPARE";
    public static final String PROCEDURE_MANAGER_TYPE = "PROCEDURE";
    
    abstract public void readFile(File file) throws Exception;
    
    abstract public boolean isValid();
    
    abstract public String getManagerType();
    
    abstract public File getFile();
    
}
