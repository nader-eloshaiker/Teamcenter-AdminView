/*
 * ArrayListSorter.java
 *
 * Created on 5 September 2007, 19:43
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tceav.utils;

import java.util.AbstractList;

/**
 *
 * @author NZR4DL
 */
public class ArrayListSorter {
    
    public static void sortStringArray(AbstractList<String> list) {
        for (int i = 0; i < list.size(); i++) {
            for (int j = i+1; j < list.size(); j++) {
                if (compare(list.get(i), list.get(j), true) < 0) {
                    String tmp = list.get(i);
                    list.set(i,list.get(j)); 
                    list.set(j,tmp);
                }
            }
        }
    }
    
    private static int compare(String s1, String s2, boolean ascending) {
        int result = 0;
        
        // If both values are null, return 0.
        if (s1 == null && s2 == null) {
            result = 0;
        } else if (s1 == null) { // Define null less than everything.
            result = -1;
        } else if (s2 == null) {
            result = 1;
        }
        
        result = s2.compareToIgnoreCase(s1);
        
        if (result != 0) {
            return ascending ? result : -result;
        }
        
        return result;
    }
    
}
