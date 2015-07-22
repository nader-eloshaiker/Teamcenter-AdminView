/*
 * NamedRuleDataFilterAbstract.java
 *
 * Created on 18 March 2008, 13:49
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tceav.gui.access;

import javax.swing.table.*;
import tceav.manager.access.NamedAcl;

/**
 *
 * @author nzr4dl
 */
public abstract class NamedRuleDataFilterAdapter extends NamedRuleDataAdapterModel implements NamedRuleDataFilterInterface {
    
    protected int[] indexes;
    protected NamedRuleDataFilterInterface model;
    
    public void setCompareMode(boolean compareMode) {
        super.setCompareMode(compareMode);
        model.setCompareMode(compareMode);
    }
    
    public final Object getValueAt(int aRow, int aColumn) {
        return model.getValueAt(indexes[aRow], aColumn);
    }
    
    public final int getRowCount() {
        return indexes.length;
    }
    
    public final NamedAcl getAccessRule(int row) {
        return model.getAccessRule(indexes[row]);
    }
    
    public final int indexOfRuleName(String name) {
        for(int i=0; i<getRowCount(); i++)
            if(getAccessRule(i).getRuleName().equals(name))
                return i;
        return -1;
    }
    
    public final void reallocateIndexes() {
        int rowCount = model.getRowCount();
        indexes = new int[rowCount];
        
        for (int row = 0; row < rowCount; row++)
            indexes[row] = row;
        
    }
    
    public final void applyFilter(){
        model.applyFilter();
        reallocateIndexes();
        filter();
    }
    
    public final void fireTableDataChanged() {
        model.fireTableDataChanged();
        super.fireTableDataChanged();
    }
    
    protected abstract void filter();
    
    
    
    protected final boolean isStringMatch(String checkString, String pattern) {
        return isStringMatchCaseSensitive(checkString.toLowerCase(), pattern.toLowerCase());
    }
    
    /** Creates a new instance of PatternMatch */
    protected final boolean isStringMatchCaseSensitive(String checkString, String pattern) {
        char patternChar;
        int patternPos = 0;
        char lastPatternChar;
        char thisChar;
        int i, j;
        
        for (i = 0; i < checkString.length(); i++) {
            // if we're at the end of the pattern but not the end
            // of the string, return false
            if (patternPos >= pattern.length())
                return false;
            
            // grab the characters we'll be looking at
            patternChar = pattern.charAt(patternPos);
            thisChar = checkString.charAt(i);
            
            
            switch (patternChar) {
                // check for '*', which is zero or more characters
                case '*' :
                    // if this is the last thing we're matching,
                    // we have a match
                    if (patternPos >= (pattern.length() - 1))
                        return true;
                    
                    // otherwise, do a recursive search
                    for (j = i; j < checkString.length(); j++) {
                        if (isStringMatch(checkString.substring(j), pattern.substring(patternPos + 1)))
                            return true;
                    }
                    
                    // if we never returned from that, there is no match
                    return false;
                    
                    
                    // check for '?', which is a single character
                case '?' :
                    // do nothing, just advance the patternPos at the end
                    break;
                    
                    
                    // check for '[', which indicates a range of characters
                case '[' :
                    // if there's nothing after the bracket, we have
                    // a syntax problem
                    if (patternPos >= (pattern.length() - 1))
                        return false;
                    
                    lastPatternChar = '\u0000';
                    for (j = patternPos + 1; j < pattern.length(); j++) {
                        patternChar = pattern.charAt(j);
                        if (patternChar == ']') {
                            // no match found
                            return false;
                        }  else	if (patternChar == '-')  {
                            // we're matching a range of characters
                            j++;
                            if (j == pattern.length())
                                return false;		// bad syntax
                            
                            patternChar = pattern.charAt(j);
                            if (patternChar == ']') {
                                return false;		// bad syntax
                            }  else  {
                                if ((thisChar >= lastPatternChar) && (thisChar <= patternChar))
                                    break;		// found a match
                            }
                        }  else if (thisChar == patternChar)  {
                            // if we got here, we're doing an exact match
                            break;
                        }
                        
                        lastPatternChar = patternChar;
                    }
                    
                    // if we broke out of the loop, advance to the end bracket
                    patternPos = j;
                    for (j = patternPos; j < pattern.length(); j++) {
                        if (pattern.charAt(j) == ']')
                            break;
                    }
                    patternPos = j;
                    break;
                    
                    
                default :
                    // the default condition is to do an exact character match
                    if (thisChar != patternChar)
                        return false;
                    
            }
            
            // advance the patternPos before we loop again
            patternPos++;
            
        }
        
        // if there's still something in the pattern string, check to
        // see if it's one or more '*' characters. If that's all it is,
        // just advance to the end
        for (j = patternPos; j < pattern.length(); j++) {
            if (pattern.charAt(j) != '*')
                break;
        }
        patternPos = j;
        
        // at the end of all this, if we're at the end of the pattern
        // then we have a good match
        if (patternPos == pattern.length()) {
            return true;
        }  else  {
            return false;
        }
        
    }
    
}
