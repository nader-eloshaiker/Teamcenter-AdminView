/*
 * ProcessTreeData.java
 *
 * Created on 9 August 2007, 22:03
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package tceav.gui.procedure;

import javax.swing.tree.*;
import javax.swing.event.*;
import tceav.manager.procedure.plmxmlpdm.TagTypeEnum;
import tceav.manager.procedure.plmxmlpdm.base.IdBase;
import tceav.manager.procedure.plmxmlpdm.base.AttribOwnerBase;
import tceav.manager.procedure.plmxmlpdm.type.AssociatedDataSetType;
import tceav.manager.procedure.plmxmlpdm.type.AssociatedFolderType;
import tceav.manager.procedure.plmxmlpdm.type.AssociatedFormType;
import tceav.manager.procedure.plmxmlpdm.type.OrganisationType;
import tceav.manager.procedure.plmxmlpdm.type.UserDataType;
import tceav.manager.procedure.plmxmlpdm.type.ValidationResultsType;
import tceav.manager.procedure.plmxmlpdm.type.WorkflowSignoffProfileType;
import tceav.manager.procedure.plmxmlpdm.type.WorkflowTemplateType;
import tceav.manager.procedure.plmxmlpdm.type.element.UserDataElementType;
import tceav.manager.procedure.plmxmlpdm.type.element.ValidationCheckerType;

/**
 *
 * @author NZR4DL
 */
public class ProcessTreeData implements TreeModel {

    private IdBase root;

    /**
     * Creates a new instance of ProcessTreeData
     */
    public ProcessTreeData(IdBase root) {
        this.root = root;
    }

    public ProcessTreeData() {
        this(null);
    }

    public Object getRoot() {
        return root;
    }

    public Object getChild(Object parent, int index) {

        int counter;
        UserDataType ud;

        switch (((IdBase) parent).getTagType()) {
            case WorkflowTemplate:
                WorkflowTemplateType wt = (WorkflowTemplateType) parent;
                counter = 0;

                for (int i = 0; i < wt.getAttribute().size(); i++) {
                    switch (wt.getAttribute().get(i).getTagType()) {
                        case UserData:
                            ud = (UserDataType) wt.getAttribute().get(i);
                            for (int k = 0; k < ud.getUserValue().size(); k++) {
                                if (counter == index) {
                                    return ud.getUserValue().get(k).getData();
                                } else {
                                    counter++;
                                }
                            }
                            break;
                        case AssociatedDataSet:
                            if (counter == index) {
                                return ((AssociatedDataSetType) wt.getAttribute().get(i)).getDataSet();
                            } else {
                                counter++;
                            }
                            break;
                        case AssociatedFolder:
                            if (counter == index) {
                                return ((AssociatedFolderType) wt.getAttribute().get(i)).getFolder();
                            } else {
                                counter++;
                            }
                            break;
                        case AssociatedForm:
                            if (counter == index) {
                                return ((AssociatedFormType) wt.getAttribute().get(i)).getForm();
                            } else {
                                counter++;
                            }
                            break;
                        default:
                            if (counter == index) {
                                return wt.getAttribute().get(i);
                            } else {
                                counter++;
                            }
                            break;
                    }
                }
                return null;

            case Organisation:
                OrganisationType o = (OrganisationType) parent;
                counter = 0;

                for (int i = 0; i < o.getAttribute().size(); i++) {
                    if (o.getAttribute().get(i).getTagType() == TagTypeEnum.UserData) {
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

            case UserValue:
                UserDataElementType uv = (UserDataElementType) parent;
                return uv.getData();

            case AssociatedDataSet:
                AssociatedDataSetType ad = (AssociatedDataSetType) parent;
                return ad.getDataSet();

            case AssociatedFolder:
                AssociatedFolderType af = (AssociatedFolderType) parent;
                af.getFolder();

            case AssociatedForm:
                AssociatedFormType afm = (AssociatedFormType) parent;
                afm.getForm();

            case ValidationResults:
                ValidationResultsType vr = (ValidationResultsType) parent;
                return vr.getChecker().get(index);

            case WorkflowSignoffProfile:
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
            default:
                return null;
        }
    }

    public int getChildCount(Object parent) {

        switch (((IdBase) parent).getTagType()) {
            case WorkflowTemplate:
            case Organisation:
                AttribOwnerBase aob = (AttribOwnerBase) parent;

                int len = 0;
                for (int i = 0; i < aob.getAttribute().size(); i++) {
                    if (aob.getAttribute().get(i).getTagType() == TagTypeEnum.UserData) {
                        len += ((UserDataType) aob.getAttribute().get(i)).getUserValue().size();
                    } else {
                        len++;
                    }
                }
                return len;

            case UserValue:
                UserDataElementType uv = (UserDataElementType) parent;
                if (uv.getDataRef() == null) {
                    return 0;
                } else {
                    return 1;
                }
            case AssociatedDataSet:
                AssociatedDataSetType ad = (AssociatedDataSetType) parent;
                if (ad.getDataSet() == null) {
                    return 0;
                } else {
                    return 1;
                }
            case AssociatedFolder:
                AssociatedFolderType af = (AssociatedFolderType) parent;
                if (af.getFolder() == null) {
                    return 0;
                } else {
                    return 1;
                }
            case AssociatedForm:
                AssociatedFormType afm = (AssociatedFormType) parent;
                if (afm.getForm() == null) {
                    return 0;
                } else {
                    return 1;
                }
            case ValidationResults:
                ValidationResultsType vr = (ValidationResultsType) parent;
                return vr.getChecker().size();

            case WorkflowSignoffProfile:
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
            case Role:
            default:
                return 0;
        }
    }

    public boolean isLeaf(Object node) {

        return (getChildCount(node) == 0);
    }

    public int getIndexOfChild(Object parent, Object child) {
        int index;
        IdBase childBase;

        switch (((IdBase) parent).getTagType()) {
            case Organisation:
                OrganisationType o = (OrganisationType) parent;
                index = 0;
                childBase = (IdBase) child;

                for (int i = 0; i < o.getAttribute().size(); i++) {
                    if (o.getAttribute().get(i).getTagType() == TagTypeEnum.UserData) {
                        UserDataType ud = (UserDataType) o.getAttribute().get(i);
                        if (childBase.getTagType() == TagTypeEnum.UserValue) {

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

            case WorkflowTemplate:
                WorkflowTemplateType wt = (WorkflowTemplateType) parent;
                index = 0;
                childBase = (IdBase) child;

                for (int i = 0; i < wt.getAttribute().size(); i++) {
                        switch (wt.getAttribute().get(i).getTagType()) {
                            case UserData:
                                UserDataType ud = (UserDataType) wt.getAttribute().get(i);
                                for (int j = 0; j < ud.getUserValue().size(); j++) {
                                    if (ud.getUserValue().get(j).getDataRef().equals(childBase.getId())) {
                                        return index;
                                    } else {
                                        index++;
                                    }
                                }
                                break;
                            case AssociatedDataSet:
                                if (((AssociatedDataSetType) wt.getAttribute().get(i)).getDataSet().getId().equals(childBase.getId())) {
                                    return index;
                                } else {
                                    index++;
                                }
                                break;
                            case AssociatedFolder:
                                if (((AssociatedFolderType) wt.getAttribute().get(i)).getFolder().getId().equals(childBase.getId())) {
                                    return index;
                                } else {
                                    index++;
                                }
                                break;
                            case AssociatedForm:
                                if (((AssociatedFormType) wt.getAttribute().get(i)).getForm().getId().equals(childBase.getId())) {
                                    return index;
                                } else {
                                    index++;
                                }
                                break;
                            default:
                                if (wt.getAttributeRefs().get(i).equals(childBase.getId())) {
                                    return index;
                                } else {
                                    index++;
                                }
                                break;
                        }
                }
                return -1;

            case UserValue:
                UserDataElementType uv = (UserDataElementType) parent;
                if (uv.getDataRef() == null) {
                    return -1;
                } else {
                    return 0;
                }
            case ValidationResults:
                ValidationResultsType vr = (ValidationResultsType) parent;
                return vr.getChecker().indexOf((ValidationCheckerType) child);

            case WorkflowSignoffProfile:
                WorkflowSignoffProfileType wsp = (WorkflowSignoffProfileType) parent;
                if ((wsp.getRoleRef() == null) && (wsp.getGroupRef() != null)) {
                    return 0;
                } else if ((wsp.getRoleRef() != null) && (wsp.getGroupRef() == null)) {
                    return 0;
                } else if ((wsp.getRoleRef() != null) && (wsp.getGroupRef() != null)) {
                    if (((IdBase) child).getTagType() == TagTypeEnum.Role) {
                        return 0;
                    } else if (((IdBase) child).getTagType() == TagTypeEnum.Organisation) {
                        return 1;
                    }
                } else {
                    return -1;
                }
            case AssociatedDataSet:
                AssociatedDataSetType ad = (AssociatedDataSetType) parent;
                if (ad.getDataSet() == null) {
                    return -1;
                } else {
                    return 0;
                }
            case AssociatedFolder:
                AssociatedFolderType af = (AssociatedFolderType) parent;
                if (af.getFolder() == null) {
                    return -1;
                } else {
                    return 0;
                }
            case AssociatedForm:
                AssociatedFormType afm = (AssociatedFormType) parent;
                if (afm.getForm() == null) {
                    return -1;
                } else {
                    return 0;
                }
            default:
                return -1;
        }
    }

    public void addTreeModelListener(TreeModelListener listener) {
        // not editable
    }

    public void removeTreeModelListener(TreeModelListener listener) {
        // not editable
    }

    public void valueForPathChanged(TreePath path, Object newValue) {
        // not editable
    }
}
