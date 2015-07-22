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
public class NamedRuleFilteredTableModel extends NamedRuleTableModel{
    private int[] indexes;
    
    /** Creates a new instance of NamedRuleFilteredTableModel */
    public NamedRuleFilteredTableModel(AccessRuleList accessRuleList) {
        super(accessRuleList);
        reallocateIndexes();
    }

    public int getDataIndex(int modelIndex) {
        return indexes[modelIndex];
    }
    
    public int getModelIndex(int dataIndex) {
        for(int i=0; i<indexes.length; i++)
            if (indexes[i] == dataIndex)
                return i;
        return -1;
    }
    
     private void reallocateIndexes() {
        int rowCount = getRowCount();
        
        // Set up a new array of indexes with the right number of elements
        // for the new data model.
        indexes = new int[rowCount];
        
        // Initialise with the identity mapping.
        for (int row = 0; row < rowCount; row++) {
            indexes[row] = row;
        }
    }

     public Object getValueAt(int aRow, int aColumn) {
        return super.getValueAt(indexes[aRow], aColumn);
    }
    
    public void setValueAt(Object aValue, int aRow, int aColumn) {
        super.setValueAt(aValue, indexes[aRow], aColumn);
    }
    
   
}
