/*
 * SearchAbstract.java
 *
 * Created on 25 September 2007, 07:09
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tceav.gui.tools.search;

import tceav.utils.PatternMatch;
import java.util.ArrayList;

/**
 *
 * @author NZR4DL
 */
public abstract class SearchAdapter {

    private int resultIndex;

    public int getResultIndex() {
        return resultIndex;
    }
    
    protected void resetResultIndex() {
        resultIndex = 0;
    }
    
    protected void incrementResultIndex() {
        resultIndex++;
    }

    protected boolean isMatched(String s, String pattern) {
        if((pattern != null) && (!pattern.equals(""))){
            if((s != null) && (!s.equals(""))){
                return PatternMatch.isStringMatch(s, pattern);
            } else
                return false;
        } else
            return false;
    }
    
}
