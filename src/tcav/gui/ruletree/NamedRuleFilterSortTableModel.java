/*
 * NamedRuleFilterSortTableModel.java
 *
 * Created on 12 July 2007, 13:24
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tcav.gui.ruletree;

import tcav.ruletree.AccessRuleList;
import java.util.*;
import javax.swing.table.*;

/**
 *
 * @author NZR4DL
 */
public class NamedRuleFilterSortTableModel extends NamedRuleTableModel {
    private int[] indexes;
    private Vector<Integer> sortingColumns = new Vector<Integer>();
    private boolean ascending = true;
    private String strPattern;
    private Vector<Integer> filteringColumns = new Vector<Integer>();
    private Vector<String> filteringPatterns = new Vector<String>();
    
    public static final String[] SORT_COLUMN_SELECTION = new String[]{"Type","Instance Count","ACL Name","None"};
    private final int SORT_NONE_VALUE = 3;
    
    /**
     * Creates a new instance of NamedRuleFilterSortTableModel
     */
    public NamedRuleFilterSortTableModel(AccessRuleList accessRuleList) {
        super(accessRuleList);
        reallocateIndexes();
    }
    
    public boolean isAscending() {
        return ascending;
    }
    
    public void setAscending(boolean state) {
        ascending = state;
    }
    
    // The mapping only affects the contents of the data rows.
    // Pass all requests to these rows through the mapping array: "indexes".
    public Object getValueAt(int aRow, int aColumn) {
        return super.getValueAt(indexes[aRow], aColumn);
    }
    
    public void setValueAt(Object aValue, int aRow, int aColumn) {
        
    }
    
    public int getRowCount() {
        return indexes.length;
    }
    
    public void setFilter(int column, String value) {
        if((value == null) || (value.equals("")))
            return;
        
        filteringColumns.removeAllElements();
        filteringColumns.addElement(new Integer(column));
        filteringPatterns.removeAllElements();
        filteringPatterns.addElement(value);
        
        reallocateIndexes();
        if(sortingColumns.size() > 0)
            sort();
        filter();
    }
    
    public void setFilter(int[] columns, String[] patterns) {
        filteringColumns.removeAllElements();
        filteringPatterns.removeAllElements();
        
        int length;
        if (columns.length < TOTAL_COLUMNS)
            length = columns.length;
        else
            length = TOTAL_COLUMNS;
        
        for(int i=0; i<length; i++) {
            if((patterns[i] != null) &&(!patterns[i].equals(""))) {
                filteringColumns.addElement(new Integer(columns[i]));
                filteringPatterns.addElement(patterns[i]);
            }
        }
        
        reallocateIndexes();
        if(sortingColumns.size() > 0)
            sort();
        filter();
    }
    
    public String getFilterPattern(int column) {
        //if (filteringColumns.size() == 0)
        //    return null;
        int i = filteringColumns.indexOf(new Integer(column));
        if(i == -1)
            return null;
        else
            return filteringPatterns.elementAt(i);
    }
    
    public String[] getFilterPatterns() {
        String[] patterns = new String[filteringPatterns.size()];
        for(int i=0; i<patterns.length; i++)
            patterns[i] = filteringPatterns.elementAt(i);
        return patterns;
    }
    
    public int[] getFilterColumns() {
        int[] columns = new int[filteringColumns.size()];
        for(int i=0; i<columns.length; i++)
            columns[i] = filteringColumns.elementAt(i).intValue();
        return columns;
    }

    public void resetFilter(){
        filteringColumns.removeAllElements();
        filteringPatterns.removeAllElements();
        reallocateIndexes();
        sort();
    }

    public void setSort(int column, boolean ascending) {
        this.ascending = ascending;
        sortingColumns.removeAllElements();
        sortingColumns.addElement(new Integer(column));
        
        reallocateIndexes();
        sort();
        if(filteringColumns.size() > 0)
            filter();
    }
    
    public void setSort(int[] columns, boolean ascending) {
        this.ascending = ascending;
        sortingColumns.removeAllElements();
        
        int length;
        if (columns.length < TOTAL_COLUMNS)
            length = columns.length;
        else
            length = TOTAL_COLUMNS;
        
        for(int i=0; i<length; i++)
            if(columns[i] != SORT_NONE_VALUE)
                sortingColumns.addElement(new Integer(columns[i]));
        
        reallocateIndexes();
        sort();
        if(filteringColumns.size() > 0)
            filter();
    }
    
    public int getSort(int column) {
        if ((column >= sortingColumns.size()) || (column >= TOTAL_COLUMNS))
            return SORT_NONE_VALUE;
        
        return sortingColumns.elementAt(column).intValue();
    }
    
    public int[] getSort() {
        int[] columns = new int[TOTAL_COLUMNS];
        for(int i=0; i<columns.length; i++)
            columns[i] = getSort(i);
        return columns;
    }
    
    public int getDataIndex(int modelIndex) {
        if(modelIndex >= indexes.length)
            return -1;
        else
            return indexes[modelIndex];
    }
    
    public int getModelIndex(int dataIndex) {
        for(int i=0; i<indexes.length; i++)
            if (indexes[i] == dataIndex)
                return i;
        return -1;
    }
    
    /*
     public String getRuleType(int row) {
        return super.getRuleType(row);
    }
    
    public int getInstanceCount(int row) {
        return super.getInstanceCount(row);
    }
    
    public String getRuleName(int row) {
        return super.getRuleName(row);
    }
    */
    private void reallocateIndexes() {
        int rowCount = super.getRowCount();
        indexes = new int[rowCount];

        for (int row = 0; row < rowCount; row++) {
            indexes[row] = row;
        }
    }
    
    private void sort() {
        // n2sort();
        // qsort(0, indexes.length-1);
        shuttlesort((int[])indexes.clone(), indexes, 0, indexes.length);
    }
    
    private void filter(){
        indexes = filter(indexes);
    }
    
    private int[] filter(int[] list){
        Vector<Integer> newList = new Vector<Integer>();
        int[] newIndexes;
        
        for(int i=0; i<list.length; i++)
            if(compareFilter(list[i]))
                newList.addElement(list[i]);
        
        newIndexes = new int[newList.size()];
        for(int j=0; j<newIndexes.length; j++)
            newIndexes[j] = newList.elementAt(j).intValue();
        
        return newIndexes;
    }
    
    private int compare(int row1, int row2) {
        for (int level = 0; level < sortingColumns.size(); level++) {
            Integer column = sortingColumns.elementAt(level);
            int result = compareRowsByColumn(row1, row2, column.intValue());
            if (result != 0) {
                return ascending ? result : -result;
            }
        }
        return 0;
    }
    
    private int compareRowsByColumn(int row1, int row2, int column) {
        // Check for nulls.
        Object o1 = super.getValueAt(row1, column);
        Object o2 = super.getValueAt(row2, column);
        // If both values are null, return 0.
        if (o1 == null && o2 == null) {
            return 0;
        } else if (o1 == null) { // Define null less than everything.
            return -1;
        } else if (o2 == null) {
            return 1;
        }
        
    /*
     * We copy all returned values from the getValue call in case
     * an optimised model is reusing one object to return many
     * values. The Number subclasses in the JDK are immutable and
     * so will not be used in this way but other subclasses of
     * Number might want to do this to save space and avoid
     * unnecessary heap allocation.
     */
        String s1;
        String s2;
        int result;
        int i1;
        int i2;
        switch(column) {
            case TYPE_COLUMN:
                s1 = getRuleType(row1);
                s2 = getRuleType(row2);
                result = s1.compareToIgnoreCase(s2);
                if (result < 0) {
                    return -1;
                } else if (result > 0) {
                    return 1;
                } else {
                    return 0;
                }
                
            case INSTANCES_COLUMN:
                i1 = getInstanceCount(row1);
                i2 = getInstanceCount(row2);
                
                if (i1 < i2) {
                    return -1;
                } else if (i1 > i2) {
                    return 1;
                } else {
                    return 0;
                }
                
            case NAME_COLUMN:
                s1 = getRuleName(row1);
                s2 = getRuleName(row2);
                result = s1.compareToIgnoreCase(s2);
                
                if (result < 0) {
                    return -1;
                } else if (result > 0) {
                    return 1;
                } else {
                    return 0;
                }
            default:
                s1 = super.getValueAt(row1, column).toString();
                s2 = super.getValueAt(row2, column).toString();
                result = s1.compareToIgnoreCase(s2);
                
                if (result < 0) {
                    return -1;
                } else if (result > 0) {
                    return 1;
                } else {
                    return 0;
                }
        }
    }
    
    private void n2sort() {
        for (int i = 0; i < getRowCount(); i++) {
            for (int j = i+1; j < getRowCount(); j++) {
                if (compare(indexes[i], indexes[j]) == -1) {
                    swap(i, j);
                }
            }
        }
    }
    
    // This is a home-grown implementation which we have not had time
    // to research - it may perform poorly in some circumstances. It
    // requires twice the space of an in-place algorithm and makes
    // NlogN assigments shuttling the values between the two
    // arrays. The number of compares appears to vary between N-1 and
    // NlogN depending on the initial order but the main reason for
    // using it here is that, unlike qsort, it is stable.
    private void shuttlesort(int from[], int to[], int low, int high) {
        if (high - low < 2) {
            return;
        }
        int middle = (low + high)/2;
        shuttlesort(to, from, low, middle);
        shuttlesort(to, from, middle, high);
        
        int p = low;
        int q = middle;
        
    /* This is an optional short-cut; at each recursive call,
    check to see if the elements in this subset are already
    ordered. If so, no further comparisons are needed; the
    sub-array can just be copied. The array must be copied rather
    than assigned otherwise sister calls in the recursion might
    get out of sinc. When the number of elements is three they
    are partitioned so that the first set, [low, mid), has one
    element and and the second, [mid, high), has two. We skip the
    optimisation when the number of elements is three or less as
    the first compare in the normal merge will produce the same
    sequence of steps. This optimisation seems to be worthwhile
    for partially ordered lists but some analysis is needed to
    find out how the performance drops to Nlog(N) as the initial
    order diminishes - it may drop very quickly. */
        
        if (high - low >= 4 && compare(from[middle-1], from[middle]) <= 0) {
            for (int i = low; i < high; i++) {
                to[i] = from[i];
            }
            return;
        }
        
        // A normal merge.
        
        for (int i = low; i < high; i++) {
            if (q >= high || (p < middle && compare(from[p], from[q]) <= 0)) {
                to[i] = from[p++];
            } else {
                to[i] = from[q++];
            }
        }
    }
    
    private void swap(int i, int j) {
        int tmp = indexes[i];
        indexes[i] = indexes[j];
        indexes[j] = tmp;
    }
    
    private boolean compareFilter(int row) {
        boolean result;
        for (int level = 0; level < filteringColumns.size(); level++) {
            Integer column = filteringColumns.elementAt(level);
            result = compareFilterRowsByColumn(row, filteringPatterns.elementAt(level), column.intValue());
            if (!result) {
                return result;
            }
        }
        return true;
    }
    
    private boolean compareFilterRowsByColumn(int row, String strPattern, int column) {
        // Check for nulls.
        Object o1 = super.getValueAt(row, column);
        // If both values are null, return 0.
        if (strPattern == null && o1 == null) {
            return true;
        } else if (o1 == null) { // Define null less than everything.
            return false;
        } else if (strPattern == null) {
            return false;
        }
        
        String s;
        int i1;
        Integer i2;
        switch(column) {
            case TYPE_COLUMN:
                s = getRuleType(row);
                return isStringMatch(s,strPattern);
                
            case INSTANCES_COLUMN:
                i1 = getInstanceCount(row);
                i2 = new Integer(strPattern);
                
                if (i1 == i2.intValue())
                    return true;
                else
                    return false;
                
            case NAME_COLUMN:
                s = getRuleName(row);
                return isStringMatch(s,strPattern);
                
            default:
                s = super.getValueAt(row, column).toString();
                return isStringMatch(s,strPattern);
        }
    }
    
    private boolean isStringMatch(String checkString, String pattern) {
        return isStringMatchCaseSensitive(checkString.toLowerCase(), pattern.toLowerCase());
    }
    
    /** Creates a new instance of PatternMatch */
    private boolean isStringMatchCaseSensitive(String checkString, String pattern) {
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