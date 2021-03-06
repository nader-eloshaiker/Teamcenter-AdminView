/*
 * AccessControl.java
 *
 * Created on 18 June 2007, 11:12
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package tcav.manager.access;

import java.util.HashMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import tcav.manager.compare.CompareInterface;

/**
 *
 * @author NZR4DL
 */
public class AccessControl implements CompareInterface {

    private HashMap<String, String> access;
    AccessControlHeader columns;

    /***************************************************************************
     * Handling xml format
     **************************************************************************/
    public AccessControl(Node accessControlNode, AccessControlHeader c) {
        Node currentNode;
        String s;
        columns = c;
        //AccessControlHeaderItem acTag;
        access = new HashMap<>();
        NodeList nodeList = accessControlNode.getChildNodes();
        NodeList subNodeList;

        for (int index = 0; index < nodeList.getLength(); index++) {
            currentNode = nodeList.item(index);

            switch (AccessTagTypeEnum.fromValue(currentNode.getNodeName())) {
                case AccessorType:
                    s = currentNode.getTextContent();
                    access.put(AccessControlHeaderItem.ACCESSOR_TYPE, s);
                    if (columns.indexOfAccessControl(AccessControlHeaderItem.ACCESSOR_TYPE) == -1) {
                        columns.add(new AccessControlHeaderItem(AccessControlHeaderItem.ACCESSOR_TYPE));
                    }
                    break;

                case Accessor:
                    s = currentNode.getTextContent();
                    access.put(AccessControlHeaderItem.ACCESSOR, s);
                    if (columns.indexOfAccessControl(AccessControlHeaderItem.ACCESSOR) == -1) {
                        columns.add(new AccessControlHeaderItem(AccessControlHeaderItem.ACCESSOR));
                    }
                    break;

                case Grant:
                    subNodeList = currentNode.getChildNodes();

                    for (int subIndex = 0; subIndex < subNodeList.getLength(); subIndex++) {
                        currentNode = subNodeList.item(subIndex);

                        if (AccessTagTypeEnum.fromValue(currentNode.getNodeName()) == AccessTagTypeEnum.ACEEntryColumn) {
                            s = currentNode.getTextContent();
                            access.put(s, "Y");
                            if (columns.indexOfAccessControl(s) == -1) {
                                columns.add(new AccessControlHeaderItem(s));
                            }
                        }
                    }

                    break;

                case Revoke:
                    subNodeList = currentNode.getChildNodes();

                    for (int subIndex = 0; subIndex < subNodeList.getLength(); subIndex++) {
                        currentNode = subNodeList.item(subIndex);

                        if (AccessTagTypeEnum.fromValue(currentNode.getNodeName()) == AccessTagTypeEnum.ACEEntryColumn) {
                            s = currentNode.getTextContent();
                            access.put(s, "N");
                            if (columns.indexOfAccessControl(s) == -1) {
                                columns.add(new AccessControlHeaderItem(s));
                            }
                        }
                    }

                    break;

                default:
                    break;
            }
        }
    }

    /***************************************************************************
     * Handling text format
     **************************************************************************/

    /* Creates a new instance of AclRuleElement */
    public AccessControl(String ruleEntry, AccessControlHeader c) {
        columns = c;
        AccessControlHeaderItem item;
        String[] strArray = ruleEntry.split("!");
        access = new HashMap<>();

        for (int i = 0; i < c.size(); i++) {
            item = c.get(i);
            access.put(item.value(), strArray[i]);
        }
    }

    private int indexOfColumn(AccessControlHeaderItem a) {
        for (int i = 0; i < columns.size(); i++) {
            if (a.equals(columns.get(i))) {
                return i;
            }
        }
        return -1;
    }

    /* Gets the control access
     * @param index location of the control
     * @return state "Y" = True, "N" = False, " " = Not set
     */
    public String getAccessForColumn(AccessControlHeaderItem a) {
        String s = access.get(a.value());
        
        if (s == null)
            return " ";
        else
            return access.get(a.value());
    }

    public String getTypeOfAccessor() {
        return access.get(AccessControlHeaderItem.ACCESSOR_TYPE);
    }

    public String getIdOfAccessor() {
        return access.get(AccessControlHeaderItem.ACCESSOR);
    }

    public String getAccessControlAtIndex(int index) {
        return getAccessForColumn(columns.get(index));
    }

    @Override
    public String toString() {
        String s = "";
        for (int i = 0; i < access.size(); i++) {
            s += getAccessControlAtIndex(i) + "!";
        }
        return s;
    }

    public String generateExportString() {
        return toString();
    }
    /***************************************************************************
     * Comapare interface
     **************************************************************************/
    private int compare_result = CompareInterface.EQUAL;

    @Override
    public int getComparison() {
        return compare_result;
    }

    @Override
    public void setComparison(int compare_result) {
        this.compare_result = compare_result;
    }

    @Override
    public int compare(Object o) {
        AccessControl c = (AccessControl) o;
        String value;

        if (getTypeOfAccessor().equals(c.getTypeOfAccessor()) && getIdOfAccessor().equals(c.getIdOfAccessor())) {

            for (int i = 2; i < access.size(); i++) {
                value = c.getAccessControlAtIndex(i);
                if (value != null) {
                    if (!getAccessControlAtIndex(i).equals(value)) {
                        return CompareInterface.NOT_EQUAL;
                    }
                }
            }

            return CompareInterface.EQUAL;

        } else {
            return CompareInterface.NOT_FOUND;
        }
    }
}
