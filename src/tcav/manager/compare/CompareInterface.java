/*
 * CompareInterface.java
 *
 * Created on 3 March 2008, 14:19
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tcav.manager.compare;

/**
 *
 * @author nzr4dl
 */
public interface CompareInterface {
    
    public static final int EQUAL = 0;
    public static final int NOT_EQUAL = 1;
    public static final int NOT_FOUND = -1;
    
    public int getComparison();
    
    public void setComparison(int value);
    
    public int compare(Object o);
    
}
