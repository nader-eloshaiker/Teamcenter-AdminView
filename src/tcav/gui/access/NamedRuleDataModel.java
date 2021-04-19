/*
 * NamedRuleDataModel.java
 *
 * Created on 11 July 2007, 11:03
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tcav.gui.access;

import tcav.manager.access.NamedAcl;
import tcav.manager.access.NamedAclList;

/**
 *
 * @author nzr4dl
 */
public class NamedRuleDataModel extends NamedRuleDataAdapterModel implements NamedRuleDataFilterInterface {
    
    protected NamedAclList accessRuleList;
    
    /**
     * Creates a new instance of NamedRuleDataModel
     */
    public NamedRuleDataModel(NamedAclList accessRuleList) {
        this.accessRuleList = accessRuleList;
    }
    
    @Override
    public NamedAcl getAccessRule(int row) {
        return accessRuleList.get(row);
    }
    
    @Override
    public int getRowCount() {
        return accessRuleList.size();
    }
    
    @Override
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
