/*
 * XMLTableData.java
 *
 * Created on 31 July 2007, 20:25
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tceav.gui.procedure;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import tceav.manager.procedure.plmxmlpdm.base.IdBase;
import tceav.manager.procedure.plmxmlpdm.type.AssociatedDataSetType;
import tceav.manager.procedure.plmxmlpdm.type.AssociatedFolderType;
import tceav.manager.procedure.plmxmlpdm.type.AssociatedFormType;
import tceav.manager.procedure.plmxmlpdm.type.OrganisationType;
import tceav.manager.procedure.plmxmlpdm.type.RoleType;
import tceav.manager.procedure.plmxmlpdm.type.UserDataType;
import tceav.manager.procedure.plmxmlpdm.type.UserListDataType;
import tceav.manager.procedure.plmxmlpdm.type.ValidationResultsType;
import tceav.manager.procedure.plmxmlpdm.type.WorkflowActionType;
import tceav.manager.procedure.plmxmlpdm.type.WorkflowBusinessRuleHandlerType;
import tceav.manager.procedure.plmxmlpdm.type.WorkflowBusinessRuleType;
import tceav.manager.procedure.plmxmlpdm.type.WorkflowHandlerType;
import tceav.manager.procedure.plmxmlpdm.type.WorkflowSignoffProfileType;
import tceav.manager.procedure.plmxmlpdm.type.WorkflowTemplateType;
import tceav.manager.procedure.plmxmlpdm.type.element.UserDataElementType;

/**
 *
 * @author NZR4DL
 */
public class PropertiesTableData extends AbstractTableModel implements TableModel {
    private IdBase data;
    //private ProcedureManager pm;
    
    /**
     * Creates a new instance of XMLTableData
     */
    public PropertiesTableData(IdBase data) {
        this.data = data;
    }
    
    public PropertiesTableData() {
        this(null);
    }
    
    @Override
    public Class getColumnClass(int columnIndex) {
        return String.class;
    }
    
    public int getColumnCount() {
        return 2;
    }
    
    @Override
    public String getColumnName(int columnIndex) {
        switch(columnIndex){
            case 0:
                return "Name";
                
            case 1:
                return "Value";
                
            default:
                return "";
        }
    }
    
    public int getRowCount() {
        if(data == null)
            return 0;
        
        if(data instanceof WorkflowTemplateType)
            return 14;
        
        else if(data instanceof WorkflowActionType)
            return 6;
        
        else if(data instanceof WorkflowHandlerType)
            return 4;
        
        else if(data instanceof WorkflowBusinessRuleType)
            return 6;
        
        else if(data instanceof WorkflowBusinessRuleHandlerType)
            return 6;
        
        else if(data instanceof WorkflowSignoffProfileType)
            return 9;
        
        else if(data instanceof OrganisationType)
            return 4;
        
        else if(data instanceof RoleType)
            return 4;
        
        else if(data instanceof UserDataType) //UserData & Argument
            return 5;
        
        else if(data instanceof UserDataElementType) //UserValue
            return 15;
        
        else if(data instanceof AssociatedDataSetType)
            return 6;
        
        else if(data instanceof AssociatedFolderType)
            return 6;
        
        else if(data instanceof AssociatedFormType)
            return 6;
        
        else if(data instanceof ValidationResultsType)
            return 5;
        
        else
            return 0;
        
    }
    
    public Object getValueAt(int rowIndex, int columnIndex) {
        if(data == null)
            return null;
        
        if(data instanceof WorkflowTemplateType)
            return getWorkflowTemplateValueAt(rowIndex, columnIndex);
        
        else if(data instanceof WorkflowActionType)
            return getWorkflowActionValueAt(rowIndex, columnIndex);
        
        else if(data instanceof WorkflowHandlerType)
            return getWorkflowHandlerValueAt(rowIndex, columnIndex);
        
        else if(data instanceof WorkflowBusinessRuleType)
            return getWorkflowBusinessRuleValueAt(rowIndex, columnIndex);
        
        else if(data instanceof WorkflowBusinessRuleHandlerType)
            return getWorkflowBusinessRuleHandlerValueAt(rowIndex, columnIndex);
        
        else if(data instanceof WorkflowSignoffProfileType)
            return getWorkflowSignoffProfileValueAt(rowIndex, columnIndex);
        
        else if(data instanceof OrganisationType)
            return getOrganisationValueAt(rowIndex, columnIndex);
        
        else if(data instanceof RoleType)
            return getRoleValueAt(rowIndex, columnIndex);
        
        else if(data instanceof UserDataType)
            return getUserDataValueAt(rowIndex, columnIndex);
        
        else if(data instanceof UserDataElementType)
            return getUserValueAt(rowIndex, columnIndex);
        
        else if(data instanceof AssociatedDataSetType)
            return getAssociatedDataSetValueAt(rowIndex, columnIndex);
        
        else if(data instanceof AssociatedFolderType)
            return getAssociatedFolderValueAt(rowIndex, columnIndex);
        
        else if(data instanceof AssociatedFormType)
            return getAssociatedFormValueAt(rowIndex, columnIndex);
        
        else if(data instanceof ValidationResultsType)
            return getValidationResultsValueAt(rowIndex, columnIndex);
        
        else
            return null;
        
    }
    
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }
    
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        
    }
    
    private Object getUserDataValueAt(int rowIndex, int columnIndex) {
        UserDataType ud = (UserDataType)data;
        switch(rowIndex){
            case 0:
                if(columnIndex == 0)
                    return "Id";
                else
                    return ud.getId();
            case 1:
                if(columnIndex == 0)
                    return "Name";
                else
                    return ud.getName();
            case 2:
                if(columnIndex == 0)
                    return "Description";
                else
                    return ud.getDescription();
            case 3:
                if(columnIndex == 0)
                    return "Procedure Type";
                else
                    return data.getClass().getSimpleName();
            case 4:
                if(columnIndex == 0)
                    return "Type";
                else
                    return ud.getType();
            default:
                return null;
        }
    }
    
    private Object getUserValueAt(int rowIndex, int columnIndex) {
        UserDataElementType uv = (UserDataElementType)data;
        switch(rowIndex){
            case 0:
                if(columnIndex == 0)
                    return "Title";
                else
                    return uv.getTitle();
            case 1:
                if(columnIndex == 0)
                    return "Procedure Type";
                else
                    return data.getClass().getSimpleName();
            case 2:
                if(columnIndex == 0)
                    return "Value";
                else
                    return uv.getValue();
            case 3:
                if(columnIndex == 0)
                    return "Type";
                else
                    return uv.getType();
            case 4:
                if(columnIndex == 0)
                    return "Data Reference";
                else
                    return uv.getDataRef();
            case 5:
                if(columnIndex == 0)
                    return "Editable";
                else
                    return uv.isEditable();
            case 6:
                if(columnIndex == 0)
                    return "Format";
                else
                    return uv.getFormat();
            case 7:
                if(columnIndex == 0)
                    return "Max Exclusive";
                else
                    return uv.getMaxExclusive();
            case 8:
                if(columnIndex == 0)
                    return "Max Inclusive";
                else
                    return uv.getMaxInclusive();
            case 9:
                if(columnIndex == 0)
                    return "Max Length";
                else
                    return uv.getMaxLength();
            case 10:
                if(columnIndex == 0)
                    return "Min Exclusive";
                else
                    return uv.getMinExclusive();
            case 11:
                if(columnIndex == 0)
                    return "Min Inclusive";
                else
                    return uv.getMinInclusive();
            case 12:
                if(columnIndex == 0)
                    return "Min Length";
                else
                    return uv.getMinLength();
            case 13:
                if(columnIndex == 0)
                    return "Step Value";
                else
                    return uv.getStepValue();
            case 14:
                if(columnIndex == 0)
                    return "User List";
                else {
                    if(uv.getUserList() != null)
                        return uv.getUserList().getType()+" Need more coding";
                    else
                        return null;
                }
            default:
                return null;
        }
    }
    
    private Object getWorkflowTemplateValueAt(int rowIndex, int columnIndex) {
        WorkflowTemplateType wt = (WorkflowTemplateType)data;
        switch(rowIndex){
            case 0:
                if(columnIndex == 0)
                    return "Id";
                else
                    return wt.getId();
            case 1:
                if(columnIndex == 0)
                    return "Name";
                else
                    return wt.getName();
            case 2:
                if(columnIndex == 0)
                    return "Description";
                else
                    return wt.getDescription();
            case 3:
                if(columnIndex == 0)
                    return "Procedure Type";
                else
                    return data.getClass().getSimpleName();
            case 4:
                if(columnIndex == 0)
                    return "Sign Off Quorum";
                else
                    return wt.getSignoffQuorum();
            case 5:
                if(columnIndex == 0)
                    return "Classification";
                else
                    return wt.getTemplateClassification().value();
            case 6:
                if(columnIndex == 0)
                    return "Show in Process Stage";
                else
                    return wt.isShowInProcessStage();
            case 7:
                if(columnIndex == 0)
                    return "Parent Task";
                else
                    return wt.getParentTaskTemplateRef();
            case 8:
                if(columnIndex == 0)
                    return "Stage";
                else
                    return wt.getStage().value();
            case 9:
                if(columnIndex == 0)
                    return "Object Type";
                else
                    return wt.getObjectType();
            case 10:
                if(columnIndex == 0)
                    return "Key";
                else
                    return wt.getIconKey();
            case 11:
                if(columnIndex == 0)
                    return "Task Description";
                else {
                    UserListDataType uld = wt.getTaskDescription();
                    if(uld != null)
                        return uld.getItem().get(0).getValue();
                    else
                        return null;
                }
            case 12:
                if(columnIndex == 0)
                    return "Dependency Tasks";
                else
                    return wt.getDependencyTaskTemplateRefs().toString();
            case 13:
                if(columnIndex == 0)
                    return "Sub Tasks";
                else
                    return wt.getSubTemplateRefs().toString();
            default:
                return null;
        }
    }
    
    private Object getWorkflowActionValueAt(int rowIndex, int columnIndex) {
        WorkflowActionType wa = (WorkflowActionType)data;
        switch(rowIndex){
            case 0:
                if(columnIndex == 0)
                    return "Id";
                else
                    return wa.getId();
            case 1:
                if(columnIndex == 0)
                    return "Name";
                else
                    return wa.getName();
            case 2:
                if(columnIndex == 0)
                    return "Description";
                else
                    return wa.getDescription();
            case 3:
                if(columnIndex == 0)
                    return "Procedure Type";
                else
                    return data.getClass().getSimpleName();
            case 4:
                if(columnIndex == 0)
                    return "Action Type";
                else
                    return wa.getActionType();
            case 5:
                if(columnIndex == 0)
                    return "Parent Process";
                else
                    wa.getParentRef();
            default:
                return null;
        }
    }
    
    private Object getWorkflowHandlerValueAt(int rowIndex, int columnIndex) {
        WorkflowHandlerType wh = (WorkflowHandlerType)data;
        switch(rowIndex){
            case 0:
                if(columnIndex == 0)
                    return "Id";
                else
                    return wh.getId();
            case 1:
                if(columnIndex == 0)
                    return "Name";
                else
                    return wh.getName();
            case 2:
                if(columnIndex == 0)
                    return "Description";
                else
                    return wh.getDescription();
            case 3:
                if(columnIndex == 0)
                    return "Procedure Type";
                else
                    return data.getClass().getSimpleName();
            default:
                return null;
        }
    }
    
    private Object getWorkflowBusinessRuleValueAt(int rowIndex, int columnIndex) {
        WorkflowBusinessRuleType wbr = (WorkflowBusinessRuleType)data;
        switch(rowIndex){
            case 0:
                if(columnIndex == 0)
                    return "Id";
                else
                    return wbr.getId();
            case 1:
                if(columnIndex == 0)
                    return "Name";
                else
                    return wbr.getName();
            case 2:
                if(columnIndex == 0)
                    return "Description";
                else
                    return wbr.getDescription();
            case 3:
                if(columnIndex == 0)
                    return "Procedure Type";
                else
                    return data.getClass().getSimpleName();
            case 4:
                if(columnIndex == 0)
                    return "Sign Off Quorum";
                else
                    return wbr.getRuleQuorum();
            case 5:
                if(columnIndex == 0)
                    return "Parent Action";
                else
                    return wbr.getParentRef();
            default:
                return null;
        }
    }
    
    private Object getWorkflowBusinessRuleHandlerValueAt(int rowIndex, int columnIndex) {
        WorkflowBusinessRuleHandlerType wbrh = (WorkflowBusinessRuleHandlerType)data;
        switch(rowIndex){
            case 0:
                if(columnIndex == 0)
                    return "Id";
                else
                    return wbrh.getId();
            case 1:
                if(columnIndex == 0)
                    return "Name";
                else
                    return wbrh.getName();
            case 2:
                if(columnIndex == 0)
                    return "Description";
                else
                    return wbrh.getDescription();
            case 3:
                if(columnIndex == 0)
                    return "Procedure Type";
                else
                    return data.getClass().getSimpleName();
            case 4:
                if(columnIndex == 0)
                    return "Negated";
                else
                    return wbrh.isNegated();
            case 5:
                if(columnIndex == 0)
                    return "Overide";
                else
                    return wbrh.isOverride();
            default:
                return null;
        }
    }
    
    private Object getWorkflowSignoffProfileValueAt(int rowIndex, int columnIndex) {
        WorkflowSignoffProfileType wsp = (WorkflowSignoffProfileType)data;
        switch(rowIndex){
            case 0:
                if(columnIndex == 0)
                    return "Id";
                else
                    return wsp.getId();
            case 1:
                if(columnIndex == 0)
                    return "Name";
                else
                    return wsp.getName();
            case 2:
                if(columnIndex == 0)
                    return "Description";
                else
                    return wsp.getDescription();
            case 3:
                if(columnIndex == 0)
                    return "Procedure Type";
                else
                    return data.getClass().getSimpleName();
            case 4:
                if(columnIndex == 0)
                    return "Sign Off Quorum";
                else
                    return wsp.getSignoffQuorum();
            case 5:
                if(columnIndex == 0)
                    return "Number of Sign Offs";
                else
                    return wsp.getNumberOfSignoffs();
            case 6:
                if(columnIndex == 0)
                    return "Allow Sub Groups";
                else
                    return wsp.isAllowSubgroups();
            case 7:
                if(columnIndex == 0)
                    return "Role Reference";
                else
                    return wsp.getRoleRef()+" "+wsp.getRole().getName();
            case 8:
                if(columnIndex == 0)
                    return "Group Reference";
                else if(wsp.getGroupRef() != null)
                    return wsp.getGroupRef()+" "+wsp.getGroup().getName();
            default:
                return null;
        }
    }
    
    private Object getOrganisationValueAt(int rowIndex, int columnIndex){
        OrganisationType o = (OrganisationType)data;
        switch(rowIndex){
            case 0:
                if(columnIndex == 0)
                    return "Id";
                else
                    return o.getId();
            case 1:
                if(columnIndex == 0)
                    return "Name";
                else
                    return o.getName();
            case 2:
                if(columnIndex == 0)
                    return "Description";
                else
                    return o.getDescription();
            case 3:
                if(columnIndex == 0)
                    return "Procedure Type";
                else
                    return data.getClass().getSimpleName();
            default:
                return null;
        }
    }
    
    private Object getRoleValueAt(int rowIndex, int columnIndex) {
        RoleType r = (RoleType)data;
        switch(rowIndex){
            case 0:
                if(columnIndex == 0)
                    return "Id";
                else
                    return r.getId();
            case 1:
                if(columnIndex == 0)
                    return "Name";
                else
                    return r.getName();
            case 2:
                if(columnIndex == 0)
                    return "Description";
                else
                    return r.getDescription();
            case 3:
                if(columnIndex == 0)
                    return "Procedure Type";
                else
                    return data.getClass().getSimpleName();
            default:
                return null;
        }
    }
    
    private Object getAssociatedDataSetValueAt(int rowIndex, int columnIndex) {
        AssociatedDataSetType ad = (AssociatedDataSetType)data;
        switch(rowIndex){
            case 0:
                if(columnIndex == 0)
                    return "Id";
                else
                    return ad.getId();
            case 1:
                if(columnIndex == 0)
                    return "Name";
                else
                    return ad.getName();
            case 2:
                if(columnIndex == 0)
                    return "Description";
                else
                    return ad.getDescription();
            case 3:
                if(columnIndex == 0)
                    return "Procedure Type";
                else
                    return data.getClass().getSimpleName();
            case 4:
                if(columnIndex == 0)
                    return "Role";
                else
                    return ad.getRole();
            case 5:
                if(columnIndex == 0)
                    return "DataSet Reference";
                else
                    return ad.getDataSetRef();
            default:
                return null;
        }
    }
    
    private Object getAssociatedFolderValueAt(int rowIndex, int columnIndex) {
        AssociatedFolderType af = (AssociatedFolderType)data;
        switch(rowIndex){
            case 0:
                if(columnIndex == 0)
                    return "Id";
                else
                    return af.getId();
            case 1:
                if(columnIndex == 0)
                    return "Name";
                else
                    return af.getName();
            case 2:
                if(columnIndex == 0)
                    return "Description";
                else
                    return af.getDescription();
            case 3:
                if(columnIndex == 0)
                    return "Procedure Type";
                else
                    return data.getClass().getSimpleName();
            case 4:
                if(columnIndex == 0)
                    return "Role";
                else
                    return af.getRole();
            case 5:
                if(columnIndex == 0)
                    return "Folder Reference";
                else
                    return af.getFolderRef();
            default:
                return null;
        }
    }
    
    private Object getAssociatedFormValueAt(int rowIndex, int columnIndex) {
        AssociatedFormType af = (AssociatedFormType)data;
        switch(rowIndex){
            case 0:
                if(columnIndex == 0)
                    return "Id";
                else
                    return af.getId();
            case 1:
                if(columnIndex == 0)
                    return "Name";
                else
                    return af.getName();
            case 2:
                if(columnIndex == 0)
                    return "Description";
                else
                    return af.getDescription();
            case 3:
                if(columnIndex == 0)
                    return "Procedure Type";
                else
                    return data.getClass().getSimpleName();
            case 4:
                if(columnIndex == 0)
                    return "Role";
                else
                    return af.getRole();
            case 5:
                if(columnIndex == 0)
                    return "Folder Reference";
                else
                    return af.getFormRef();
            default:
                return null;
        }
    }
    
    private Object getValidationResultsValueAt(int rowIndex, int columnIndex) {
        ValidationResultsType vr = (ValidationResultsType)data;
        switch(rowIndex){
            case 0:
                if(columnIndex == 0)
                    return "Id";
                else
                    return vr.getId();
            case 1:
                if(columnIndex == 0)
                    return "Name";
                else
                    return vr.getName();
            case 2:
                if(columnIndex == 0)
                    return "Description";
                else
                    return vr.getDescription();
            case 3:
                if(columnIndex == 0)
                    return "Application";
                else
                    return vr.getApplication();
            case 4:
                if(columnIndex == 0)
                    return "Procedure Type";
                else
                    return data.getClass().getSimpleName();
            default:
                return null;
        }
    }
    
}
