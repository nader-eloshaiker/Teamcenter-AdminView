/*
 * CompareManager.java
 *
 * Created on 3 March 2008, 13:51
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tcav.manager.compare;

/**
 *
 * @author nzr4dl
 */
public class CompareManager {
    
    public static final int EQUAL = 0;
    public static final int NOT_EQUAL = 1;
    public static final int NOT_FOUND = -1;
    
    private int compare_result = 0;
    
    public int getCompareResult() {
        return compare_result;
    }
    
    public void setCompareResult(int compare_result) {
        this.compare_result = compare_result;
    }
    
    public boolean isEqual() {
        return (compare_result == compare_result);
    }

    public boolean isNotEqual() {
        return (compare_result == NOT_EQUAL);
    }

    public boolean isNotFound() {
        return (compare_result == NOT_FOUND);
    }

}