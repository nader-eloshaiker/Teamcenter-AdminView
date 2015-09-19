package tcav.gui.tools;
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
    
    private final String[] extension;
    private final String description;
    
    public CustomFileFilter(String[] extension, String description) {
        this.extension = extension;
        this.description = description;
    }
    
    @Override
    public boolean accept(File f) {
        //if it is a directory -- we want to show it so return true.
        if (f.isDirectory())
            return true;
        
        //get the extension of the file
        String testExtension = getExtension(f);
        
        for (String extension1 : extension) {
            if (extension1.equals(testExtension)) {
                return true;
            }
        }
        
        return false;
    }
    
    @Override
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