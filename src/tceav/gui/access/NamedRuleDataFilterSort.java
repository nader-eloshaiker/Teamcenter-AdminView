/*
 * NamedRuleDataFilterSort.java
 *
 * Created on 12 July 2007, 13:24
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tceav.gui.access;

import tceav.manager.access.NamedAcl;
import tceav.manager.compare.CompareInterface;
import java.util.*;

/**
 *
 * @author NZR4DL
 */
public class NamedRuleDataFilterSort extends NamedRuleDataFilterAdapter implements NamedRuleDataFilterInterface {
    private ArrayList<Integer> sortingColumns = new ArrayList<Integer>();
    private boolean ascending = true;
    
    private static final String[] SORT_COLUMN_SELECTION = new String[]{"Type","Instance Count","ACL Name","None"};
    private static final String[] SORT_COLUMN_SELECTION_COMPAREMODE = new String[]{"Type","Instance Count","ACL Name","Comparison","None"};
    
    /**
     * Creates a new instance of NamedRuleDataFilterSort
     */
    public NamedRuleDataFilterSort(NamedRuleDataFilterInterface model) {
        this.model = model;
        reallocateIndexes();
    }
    
    public boolean isAscending() {
        return ascending;
    }
    
    public void setAscending(boolean state) {
        ascending = state;
    }
    
    public String[] getSortColumns() {
        if(!isCompare())
            return SORT_COLUMN_SELECTION;
        else
            return SORT_COLUMN_SELECTION_COMPAREMODE;
    }
    
    public int getSortNoneValue() {
        return getSortColumns().length - 1;
        
    }
    
    
    public void setSort(int column, boolean ascending) {
        this.ascending = ascending;
        sortingColumns.clear();
        sortingColumns.add(new Integer(column));
    }
    
    public void setSort(int[] columns, boolean ascending) {
        this.ascending = ascending;
        sortingColumns.clear();
        
        int length;
        if (columns.length < getColumnCount())
            length = columns.length;
        else
            length = getColumnCount();
        
        for(int i=0; i<length; i++)
            if(columns[i] != getSortNoneValue())
                sortingColumns.add(new Integer(columns[i]));
    }
    
    public int getSort(int column) {
        if ((column >= sortingColumns.size()) || (column >= getColumnCount()))
            return getSortNoneValue();
        
        return sortingColumns.get(column).intValue();
    }
    
    public int[] getSort() {
        int[] columns = new int[getColumnCount()];
        for(int i=0; i<columns.length; i++)
            columns[i] = getSort(i);
        return columns;
    }
    
    protected void filter() {
        if(sortingColumns.size() > 0)
            shuttlesort((int[])indexes.clone(), indexes, 0, indexes.length);
    }
    
    private int compare(int row1, int row2) {
        for (int level = 0; level < sortingColumns.size(); level++) {
            Integer column = sortingColumns.get(level);
            int result = compareRowsByColumn(row1, row2, column.intValue());
            if (result != 0) {
                return ascending ? result : -result;
            }
        }
        return 0;
    }
    
    private int compareRowsByColumn(int row1, int row2, int column) {
        // Check for nulls.
        Object o1 = model.getValueAt(row1, column);
        Object o2 = model.getValueAt(row2, column);
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
        if(!isCompare()) {
            switch(column) {
                case TYPE_COLUMN:
                    return compareStr(
                            model.getAccessRule(row1).getRuleType(),
                            model.getAccessRule(row2).getRuleType());
                    
                case INSTANCES_COLUMN:
                    return compareInt(
                            model.getAccessRule(row1).getRuleTreeReferences().size(),
                            model.getAccessRule(row2).getRuleTreeReferences().size());
                    
                case NAME_COLUMN:
                    return compareStr(
                            model.getAccessRule(row1).getRuleName(),
                            model.getAccessRule(row2).getRuleName());
                    
                default:
                    return compareStr(
                            model.getValueAt(row1, column).toString(),
                            model.getValueAt(row2, column).toString());
                    
            }
        } else {
            switch(column) {
                case TYPE_COLUMN:
                    return compareStr(
                            model.getAccessRule(row1).getRuleType(),
                            model.getAccessRule(row2).getRuleType());
                    
                case COMPARE_COLUMN:
                    return compareInt(
                            model.getAccessRule(row1).getComparison(),
                            model.getAccessRule(row2).getComparison());
                    
                case INSTANCES_COLUMN:
                    return compareInt(
                            model.getAccessRule(row1).getRuleTreeReferences().size(),
                            model.getAccessRule(row2).getRuleTreeReferences().size());
                    
                case NAME_COLUMN:
                    return compareStr(
                            model.getAccessRule(row1).getRuleName(),
                            model.getAccessRule(row2).getRuleName());
                    
                default:
                    return compareStr(
                            model.getValueAt(row1, column).toString(),
                            model.getValueAt(row2, column).toString());
            }
        }
    }
    
    private int compareStr(String s1, String s2) {
        int result = s1.compareToIgnoreCase(s2);
        
        if (result < 0)
            return -1;
        else if (result > 0)
            return 1;
        else
            return 0;
    }
    
    private int compareInt(int i1, int i2) {
        if (i1 < i2)
            return -1;
        else if (i1 > i2)
            return 1;
        else
            return 0;
    }
    
    /*
    private void n2sort() {
        for (int i = 0; i < getRowCount(); i++) {
            for (int j = i+1; j < getRowCount(); j++) {
                if (compare(indexes[i], indexes[j]) == -1) {
                    swap(i, j);
                }
            }
        }
    }
     */
    
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
    
}