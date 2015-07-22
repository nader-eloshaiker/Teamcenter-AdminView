/*
 * CompareResult.java
 *
 * Created on 11 March 2008, 17:12
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tceav.manager.compare;

/**
 *
 * @author nzr4dl
 */
public class CompareResult {
    private int equal = 0;
    private int notequal = 0;
    private int notfound = 0;
    
    public CompareResult() {
    }
    
    public void increment(int type) {
        switch(type) {
            case CompareInterface.EQUAL: equal++; break;
            case CompareInterface.NOT_EQUAL: notequal++; break;
            case CompareInterface.NOT_FOUND: notfound++; break;
            default: break;
        }
    }
    
    public int getResult(int type) {
        switch(type) {
            case CompareInterface.EQUAL: return equal;
            case CompareInterface.NOT_EQUAL: return notequal;
            case CompareInterface.NOT_FOUND: return notfound;
            default: return 0;
        }
    }
}

