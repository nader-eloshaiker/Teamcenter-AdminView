/*
 * CompareInterface.java
 *
 * Created on 3 March 2008, 14:19
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tcav.manager.compare;

import java.awt.Color;

/**
 *
 * @author nzr4dl
 */
public interface CompareInterface {
    
    public static final int EQUAL = 0;
    public static final String EQUAL_LABEL = "Identical";
    public static final Color EQUAL_COLOR = new Color(0,255,0);
    
    public static final int NOT_EQUAL = 1;
    public static final String NOT_EQUAL_LABEL = "Different";
    public static final Color NOT_EQUAL_COLOR = new Color(255,255,0);
    
    public static final int NOT_FOUND = -1;
    public static final String NOT_FOUND_LABEL = "Missing";
    public static final Color NOT_FOUND_COLOR = new Color(255,0,0);
    
    public int getComparison();
    
    public void setComparison(int value);
    
    public int compare(Object o);
    
}
