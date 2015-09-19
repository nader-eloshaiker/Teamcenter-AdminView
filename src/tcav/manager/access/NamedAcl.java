/*
 * AccessRule.java
 *
 * Created on 18 June 2007, 11:11
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package tceav.manager.access;

import java.util.ArrayList;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import tceav.manager.compare.CompareInterface;

/**
 *
 * @author NZR4DL
 */
public class NamedAcl extends ArrayList<AccessControl> implements CompareInterface {

    private String ruleName;
    private String ruleType;
    private ArrayList<RuleTreeNode> ruleTreeReference;
    private AccessControlHeader columns;

    /**
     * Creates a new instance of AccessRule
     */
    public NamedAcl() {
    }

    /***************************************************************************
     * Handling xml format
     **************************************************************************/
    
    public NamedAcl(Node namedAclNode, AccessControlHeader c) {
        Node currentNode;
        columns = c;
        NodeList nodeList = namedAclNode.getChildNodes();

        NamedNodeMap attrib = namedAclNode.getAttributes();
        ruleType = attrib.getNamedItem("type").getNodeValue();


        for (int index = 0; index < nodeList.getLength(); index++) {
            currentNode = nodeList.item(index);

            switch (AccessTagTypeEnum.fromValue(currentNode.getNodeName())) {
                case ACLName:
                    ruleName = currentNode.getTextContent();
                    break;
                    
                case ACEEntry:
                    AccessControl ac = new AccessControl(currentNode, columns);
                    add(ac);
                    break;
                    
                default:
                    break;
            }
        }
    }

    /***************************************************************************
     * Handling text format
     **************************************************************************/
    public NamedAcl(String accessRule, AccessControlHeader c) {
        columns = c;
        String[] temp = accessRule.split("!");
        ruleName = temp[0];
        ruleType = temp[1];
    }
    
    public String getRuleName() {
        return ruleName;
    }

    public String getRuleType() {
        return ruleType;
    }

    @Override
    public String toString() {
        return ruleName;
    }

    public String generateExportString() {
        return ruleName + "!" + ruleType;
    }

    public ArrayList<RuleTreeNode> getRuleTreeReferences() {
        if (ruleTreeReference == null) {
            ruleTreeReference = new ArrayList<>();
        }

        return ruleTreeReference;
    }
    /***************************************************************************
     * Compare Interface
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
        NamedAcl acl = (NamedAcl) o;

        if (!getRuleName().equals(acl.getRuleName())) {
            return CompareInterface.NOT_FOUND;
        }

        if (!getRuleType().equals(acl.getRuleType())) {
            return CompareInterface.NOT_FOUND;
        }

        if ((this.size() == 0) && (acl.size() == 0)) {
            return CompareInterface.EQUAL;
        }

        int compare;
        int compareOverall = CompareInterface.EQUAL;

        for (AccessControl c : this) {
            compare = CompareInterface.NOT_FOUND;
            for (AccessControl ac : acl) {
                compare = c.compare(ac);
                if (compare != CompareInterface.NOT_FOUND) {
                    break;
                }
            }
            c.setComparison(compare);
            if (compare != CompareInterface.EQUAL) {
                compareOverall = CompareInterface.NOT_EQUAL;
            }
        }

        if ((compareOverall == CompareInterface.EQUAL) && (this.size() < acl.size())) {
            compareOverall = CompareInterface.NOT_EQUAL;
        }

        return compareOverall;

    }
}
