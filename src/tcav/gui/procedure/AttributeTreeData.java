/*
 * ProcessTreeData.java
 *
 * Created on 9 August 2007, 22:03
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package tcav.gui.procedure;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import tcav.manager.procedure.plmxmlpdm.base.IdBase;
import tcav.manager.procedure.plmxmlpdm.base.AttribOwnerBase;
import tcav.manager.procedure.plmxmlpdm.base.AttributeBase;
import tcav.manager.procedure.plmxmlpdm.type.AssociatedDataSetType;
import tcav.manager.procedure.plmxmlpdm.type.AssociatedFolderType;
import tcav.manager.procedure.plmxmlpdm.type.AssociatedFormType;
import tcav.manager.procedure.plmxmlpdm.type.OrganisationType;
import tcav.manager.procedure.plmxmlpdm.type.RoleType;
import tcav.manager.procedure.plmxmlpdm.type.UserDataType;
import tcav.manager.procedure.plmxmlpdm.type.ValidationResultsType;
import tcav.manager.procedure.plmxmlpdm.type.WorkflowSignoffProfileType;
import tcav.manager.procedure.plmxmlpdm.type.WorkflowTemplateType;
import tcav.manager.procedure.plmxmlpdm.type.element.UserDataElementType;
import tcav.manager.procedure.plmxmlpdm.type.element.ValidationCheckerType;

/**
 *
 * @author NZR4DL
 */
public class AttributeTreeData implements TreeModel {
    
    private IdBase root;
    
    /**
     * Creates a new instance of ProcessTreeData
     */
    public AttributeTreeData(IdBase root) {
        this.root = root;
    }
    
    public AttributeTreeData() {
        this(null);
    }
    
    @Override
    public Object getRoot() {
        return root;
    }
    
    @Override
    public Object getChild(Object parent, int index) {
        
        int counter;
        UserDataType ud;
        
        if(parent instanceof WorkflowTemplateType || parent instanceof OrganisationType) {
            AttribOwnerBase aob = (AttribOwnerBase) parent;
            IdBase attribute;
            counter = 0;
            
            for (int i = 0; i < aob.getAttribute().size(); i++) {
                attribute = aob.getAttribute().get(i);
                
                if(attribute instanceof UserDataType) {
                    ud = (UserDataType) aob.getAttribute().get(i);
                    
                    for (UserDataElementType dataElement : ud.getUserValue()) {
                        
                        if(dataElement.getValue().equals(UserDataElementType.parentDependencyTaskRef))
                            continue;
                        
                        if (counter == index) {
                            if(dataElement.getData() != null) {
                                return dataElement.getData();
                            } else {
                                return dataElement;
                            }
                        } else {
                            counter++;
                        }
                    }
                    
                } else if(attribute instanceof AssociatedDataSetType) {
                    
                    if (counter == index) {
                        return ((AssociatedDataSetType) attribute).getDataSet();
                    } else {
                        counter++;
                    }
                    
                } else if(attribute instanceof AssociatedFolderType) {
                    
                    if (counter == index) {
                        return ((AssociatedFolderType) attribute).getFolder();
                    } else {
                        counter++;
                    }
                    
                } else if(attribute instanceof AssociatedFormType) {
                    
                    if (counter == index) {
                        return ((AssociatedFormType) attribute).getForm();
                    } else {
                        counter++;
                    }
                    
                } else {
                    
                    if (counter == index) {
                        return attribute;
                    } else {
                        counter++;
                    }
                    
                }
            }
            return null;
        /*    
        } else if(parent instanceof OrganisationType) {
            OrganisationType o = (OrganisationType) parent;
            counter = 0;
            
            for (int i = 0; i < o.getAttribute().size(); i++) {
                if (o.getAttribute().get(i) instanceof UserDataType) {
                    ud = (UserDataType) o.getAttribute().get(i);
                    for (int k = 0; k < ud.getUserValue().size(); k++) {
                        if (counter == index) {
                            return ud.getUserValue().get(k);
                        } else {
                            counter++;
                        }
                    }
                } else {
                    if (counter == index) {
                        return o.getAttribute().get(i);
                    } else {
                        counter++;
                    }
                }
            }
            return null;
        */    
        } else if(parent instanceof UserDataElementType) {
            UserDataElementType uv = (UserDataElementType) parent;
            return uv.getData();
            
        } else if(parent instanceof AssociatedDataSetType) {
            AssociatedDataSetType ad = (AssociatedDataSetType) parent;
            return ad.getDataSet();
            
        } else if(parent instanceof AssociatedFolderType) {
            AssociatedFolderType af = (AssociatedFolderType) parent;
            return af.getFolder();
            
        } else if(parent instanceof AssociatedFormType) {
            AssociatedFormType afm = (AssociatedFormType) parent;
            return afm.getForm();
            
        } else if(parent instanceof ValidationResultsType) {
            ValidationResultsType vr = (ValidationResultsType) parent;
            return vr.getChecker().get(index);
            
        } else if(parent instanceof WorkflowSignoffProfileType) {
            WorkflowSignoffProfileType wsp = (WorkflowSignoffProfileType) parent;
            if (index == 0) {
                if (wsp.getRoleRef() != null) {
                    return wsp.getRole();
                } else if (wsp.getGroupRef() != null) {
                    return wsp.getGroup();
                }
            } else if (index == 1) {
                if (wsp.getGroupRef() != null) {
                    return wsp.getGroup();
                }
            }
            return null;
            
        } else
            return null;
        
    }
    
    @Override
    public int getChildCount(Object parent) {
        
        if(parent instanceof WorkflowTemplateType || parent instanceof OrganisationType) {
            AttribOwnerBase aob = (AttribOwnerBase) parent;
            
            int len = 0;
            for (AttributeBase attrib : aob.getAttribute()) {

                if (attrib instanceof UserDataType) {
                    UserDataType ud = (UserDataType) attrib;
                
                    for (UserDataElementType ude : ud.getUserValue()) {
                        if(!ude.getValue().equals(UserDataElementType.parentDependencyTaskRef)) {
                            len++;
                        }
                    }

                } else {
                    len++;
                }
                
            }
            return len;
            
        } else if(parent instanceof UserDataElementType) {
            UserDataElementType ude = (UserDataElementType) parent;
            if (ude.getDataRef() == null) {
                return 0;
            } else {
                return 1;
            }
            
        } else if(parent instanceof AssociatedDataSetType) {
            AssociatedDataSetType ad = (AssociatedDataSetType) parent;
            if (ad.getDataSet() == null) {
                return 0;
            } else {
                return 1;
            }
            
        } else if(parent instanceof AssociatedFolderType) {
            AssociatedFolderType af = (AssociatedFolderType) parent;
            if (af.getFolder() == null) {
                return 0;
            } else {
                return 1;
            }
            
        } else if(parent instanceof AssociatedFormType) {
            AssociatedFormType afm = (AssociatedFormType) parent;
            if (afm.getForm() == null) {
                return 0;
            } else {
                return 1;
            }
            
        } else if(parent instanceof ValidationResultsType) {
            ValidationResultsType vr = (ValidationResultsType) parent;
            return vr.getChecker().size();
            
            
        } else if(parent instanceof WorkflowSignoffProfileType) {
            WorkflowSignoffProfileType wsp = (WorkflowSignoffProfileType) parent;
            if ((wsp.getRoleRef() == null) && (wsp.getGroupRef() == null)) {
                return 0;
            } else if ((wsp.getRoleRef() == null) && (wsp.getGroupRef() != null)) {
                return 1;
            } else if ((wsp.getRoleRef() != null) && (wsp.getGroupRef() == null)) {
                return 1;
            } else if ((wsp.getRoleRef() != null) && (wsp.getGroupRef() != null)) {
                return 2;
            }
        }
        
        return 0;
    }
    
    @Override
    public boolean isLeaf(Object node) {
        
        return (getChildCount(node) == 0);
    }
    
    @Override
    public int getIndexOfChild(Object parent, Object child) {
        int index;
        IdBase childBase;
        
        if(parent instanceof WorkflowTemplateType || parent instanceof OrganisationType) {
            AttribOwnerBase aob = (AttribOwnerBase) parent;
            IdBase attribute;
            index = 0;
            childBase = (IdBase) child;
            
            for (int i = 0; i < aob.getAttribute().size(); i++) {
                attribute = aob.getAttribute().get(i);
                
                if(attribute instanceof UserDataType) {
                    UserDataType ud = (UserDataType) aob.getAttribute().get(i);
                    
                    for (UserDataElementType ude : ud.getUserValue()) {
                        
                        if(ude.getValue().equals(UserDataElementType.parentDependencyTaskRef))
                            continue;
                        
                        if(ude.getDataRef() != null) {
                            
                            if (ude.getDataRef().equals(childBase.getId())) {
                                return index;
                            } else {
                                index++;
                            }
                            
                        } else { 
                            
                            if (ude.equals(childBase)) {
                                return index;
                            } else {
                                index++;
                            }
                        }
                    }
                    
                } else if(attribute instanceof AssociatedDataSetType) {
                    if (((AssociatedDataSetType) attribute).getDataSet().getId().equals(childBase.getId())) {
                        return index;
                    } else {
                        index++;
                    }
                    
                } else if(attribute instanceof AssociatedFolderType) {
                    if (((AssociatedFolderType) attribute).getFolder().getId().equals(childBase.getId())) {
                        return index;
                    } else {
                        index++;
                    }
                    
                } else if(attribute instanceof AssociatedFormType) {
                    if (((AssociatedFormType) attribute).getForm().getId().equals(childBase.getId())) {
                        return index;
                    } else {
                        index++;
                    }
                    
                } else {
                    if (attribute.equals(childBase.getId())) {
                        return index;
                    } else {
                        index++;
                    }
                    
                }
            }
            return -1;
        /*    
        } else if(parent instanceof OrganisationType) {
            OrganisationType o = (OrganisationType) parent;
            index = 0;
            childBase = (IdBase) child;
            
            for (int i = 0; i < o.getAttribute().size(); i++) {
                if (o.getAttribute().get(i) instanceof UserDataType) {
                    UserDataType ud = (UserDataType) o.getAttribute().get(i);
                    
                    if (childBase instanceof UserDataElementType) {
                        int result = ud.getUserValue().indexOf((UserDataElementType) child);
                        if (result > -1) {
                            index += result;
                            return index;
                        } else {
                            index += ud.getUserValue().size();
                        }
                    } else {
                        index += ud.getUserValue().size();
                    }
                } else {
                    if (o.getAttributeRefs().get(i).equals(childBase.getId())) {
                        return index;
                    } else {
                        index++;
                    }
                }
            }
            return -1;
        */    
        } else if(parent instanceof UserDataElementType) {
            UserDataElementType uv = (UserDataElementType) parent;
            if (uv.getDataRef() == null) {
                return -1;
            } else {
                return 0;
            }
        } else if(parent instanceof ValidationResultsType) {
            ValidationResultsType vr = (ValidationResultsType) parent;
            return vr.getChecker().indexOf((ValidationCheckerType) child);
            
        } else if(parent instanceof WorkflowSignoffProfileType) {
            WorkflowSignoffProfileType wsp = (WorkflowSignoffProfileType) parent;
            if ((wsp.getRoleRef() == null) && (wsp.getGroupRef() != null)) {
                return 0;
            } else if ((wsp.getRoleRef() != null) && (wsp.getGroupRef() == null)) {
                return 0;
            } else if ((wsp.getRoleRef() != null) && (wsp.getGroupRef() != null)) {
                if(child instanceof RoleType) {
                    return 0;
                } else if (child instanceof OrganisationType) {
                    return 1;
                }
            } else {
                return -1;
            }
            
        } else if(parent instanceof AssociatedDataSetType) {
            AssociatedDataSetType ad = (AssociatedDataSetType) parent;
            if (ad.getDataSet() == null) {
                return -1;
            } else {
                return 0;
            }
        } else if(parent instanceof AssociatedFolderType) {
            AssociatedFolderType af = (AssociatedFolderType) parent;
            if (af.getFolder() == null) {
                return -1;
            } else {
                return 0;
            }
        } else if(parent instanceof AssociatedFormType) {
            AssociatedFormType afm = (AssociatedFormType) parent;
            if (afm.getForm() == null) {
                return -1;
            } else {
                return 0;
            }
        }
        return -1;
        
    }
    
    @Override
    public void addTreeModelListener(TreeModelListener listener) {
        // not editable
    }
    
    @Override
    public void removeTreeModelListener(TreeModelListener listener) {
        // not editable
    }
    
    @Override
    public void valueForPathChanged(TreePath path, Object newValue) {
        // not editable
    }
}
