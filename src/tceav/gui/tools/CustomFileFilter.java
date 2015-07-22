package tceav.gui.tools;
/*
 * CustomFileFilter.java
 *
 * Created on 6 September 2007, 16:28
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

import java.io.File;

public class CustomFileFilter extends javax.swing.filechooser.FileFilter {
    
    private String[] extension;
    private String description;
    
    public CustomFileFilter(String[] extension, String description) {
        this.extension = extension;
        this.description = description;
    }
    
    public boolean accept(File f) {
        //if it is a directory -- we want to show it so return true.
        if (f.isDirectory())
            return true;
        
        //get the extension of the file
        String testExtension = getExtension(f);
        
        for(int i=0; i<extension.length; i++) {
            if (extension[i].equals(testExtension))
                return true;
        }
        
        return false;
    }
    
    public String getDescription() {
        return description;
    }

    private String getExtension(File f) {
        String s = f.getName();
        int i = s.lastIndexOf('.');
        if (i > 0 &&  i < s.length() - 1)
            return s.substring(i+1).toLowerCase();
        return "";
    }
}