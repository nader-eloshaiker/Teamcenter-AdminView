/*
 * NamedRuleFilteredTableModel.java
 *
 * Created on 15 July 2007, 10:27
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.gm.tcae.ruletree.gui;

import java.util.*;
import javax.swing.table.*;
import com.gm.tcae.ruletree.acl.*;
/**
 *
 * @author NZR4DL
 */
public class NamedRuleFilteredTableModel extends NamedRuleSortedTableModel{
    private int[] indexes;
    private String strPattern;
    private Vector<Integer> filteringColumns = new Vector<Integer>();
    private Vector<String> filteringPatterns = new Vector<String>();
    
    /** Creates a new instance of NamedRuleFilteredTableModel */
    public NamedRuleFilteredTableModel(AccessRuleList accessRuleList) {
        super(accessRuleList);
        reallocateIndexes();
    }
    
    // The mapping only affects the contents of the data rows.
    // Pass all requests to these rows through the mapping array: "filteredIndexes".
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
    
    public void setSort(int column, boolean ascending) {
        super.setSort(column, ascending);
        filter();
    }
    
    public void setSort(int[] columns, boolean ascending) {
        super.setSort(columns, ascending);
        filter();
    }
    
    public int getDataIndex(int modelIndex) {
        if(modelIndex >= indexes.length)
            return -1;
        int tempIndex = indexes[modelIndex];
        return super.getDataIndex(tempIndex);
    }
    
    public int getModelIndex(int dataIndex) {
        int tempIndex = super.getModelIndex(dataIndex);
        if (tempIndex == -1)
            return tempIndex;
        
        for(int i=0; i<indexes.length; i++)
            if (indexes[i] == tempIndex)
                return i;
        return -1;
    }
    
    public void resetFilter(){
        filteringColumns.removeAllElements();
        filteringPatterns.removeAllElements();
        reallocateIndexes();
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
    
    private void filter(){
        reallocateIndexes();
        indexes = filter(indexes);
    }
    
    private int[] filter(int[] list){
        Vector<Integer> newList = new Vector<Integer>();
        int[] newIndexes;
        
        for(int i=0; i<list.length; i++)
            if(compare(list[i]))
                newList.addElement(list[i]);
        
        newIndexes = new int[newList.size()];
        for(int j=0; j<newIndexes.length; j++)
            newIndexes[j] = newList.elementAt(j).intValue();
        
        return newIndexes;
    }
    
    private boolean compare(int row) {
        boolean result;
        for (int level = 0; level < filteringColumns.size(); level++) {
            Integer column = filteringColumns.elementAt(level);
            result = compareRowsByColumn(row, filteringPatterns.elementAt(level), column.intValue());
            if (!result) {
                return result;
            }
        }
        return true;
    }
    
    private boolean compareRowsByColumn(int row, String strPattern, int column) {
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
                s = getRuleType(super.getDataIndex(row));
                return isStringMatch(s,strPattern);
                
            case INSTANCES_COLUMN:
                i1 = getInstanceCount(super.getDataIndex(row));
                i2 = new Integer(strPattern);
                
                if (i1 == i2.intValue())
                    return true;
                else
                    return false;
                
            case NAME_COLUMN:
                s = getRuleName(super.getDataIndex(row));
                return isStringMatch(s,strPattern);
                
            default:
                s = super.getValueAt(row, column).toString();
                return isStringMatch(s,strPattern);
        }
    }
    
    private boolean isStringMatch(String checkString, String pattern) {
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
