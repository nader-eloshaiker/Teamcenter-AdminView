/*
 * NamedRuleDataModel.java
 *
 * Created on 11 July 2007, 11:03
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tcav.gui.access;

import tcav.manager.access.AccessRule;
import tcav.manager.access.AccessRuleList;

/**
 *
 * @author nzr4dl
 */
public class NamedRuleDataModel extends NamedRuleDataAbstractModel implements NamedRuleDataFilterInterface {
    
    protected AccessRuleList accessRuleList;
    
    /**
     * Creates a new instance of NamedRuleDataModel
     */
    public NamedRuleDataModel(AccessRuleList accessRuleList) {
        this.accessRuleList = accessRuleList;
    }
    
    public AccessRule getAccessRule(int row) {
        return accessRuleList.get(row);
    }
    
    public int getRowCount() {
        return accessRuleList.size();
    }
    
    public Object getValueAt(int row, int col) {
        switch(col) {
            case TYPE_COLUMN:
                return accessRuleList.get(row).getRuleType();
            case INSTANCES_COLUMN:
                return accessRuleList.get(row).getRuleTreeReferences().size();
            case NAME_COLUMN:
                return accessRuleList.get(row).getRuleName();
            case COMPARE_COLUMN:
                return accessRuleList.get(row).getComparison();
            default:
                return null;
        }
    }
    
    public void applyFilter() {}
}
