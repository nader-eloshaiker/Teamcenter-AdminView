/*
 * ReferencesTableModel.java
 *
 * Created on 31 July 2007, 20:25
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tcadminview.gui.procedure;

import javax.swing.table.*;
import java.util.List;
import tcadminview.procedure.ProcedureManager;
import tcadminview.plmxmlpdm.classtype.WorkflowTemplateClassificationEnum;
/**
 *
 * @author NZR4DL
 */
public class ReferencesTableModel extends AbstractTableModel implements TableModel {
    private List<String> list;
    private ProcedureManager pm;
    
    /** Creates a new instance of ReferencesTableModel */
    public ReferencesTableModel(List<String> list, ProcedureManager pm) {
        this.list = list;
        this.pm = pm;
    }
    
    public ReferencesTableModel(ProcedureManager pm) {
        this(null, pm);
    }
    
    public void setArrayList(List<String> list) {
        this.list = list;
    }
    
    public Class getColumnClass(int columnIndex) {
        return String.class;
    }
    
    public int getColumnCount() {
        return 3;
    }
    
    public String getColumnName(int columnIndex) {
        switch(columnIndex){
            case 0: return "Name";
            case 1: return "Type";
            case 2: return "Id";
            default: return "";
        }
    }
    
    public int getRowCount() {
        if(list == null)
            return 0;
        else
            return list.size();
    }
    
    public Object getValueAt(int rowIndex, int columnIndex) {
        if(list == null)
            return null;
        
        switch(columnIndex){
            case 0: return pm.getNamedReference(list.get(rowIndex));
            case 1: return pm.getIdClass(list.get(rowIndex)).value();
            case 2: return list.get(rowIndex);
            default: return "";
        }
    }
    
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }
    
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        
    }
}
