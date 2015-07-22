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
            ruleTreeReference = new ArrayList<RuleTreeNode>();
        }

        return ruleTreeReference;
    }
    /***************************************************************************
     * Compare Interface
     **************************************************************************/
    private int compare_result = CompareInterface.EQUAL;

    public int getComparison() {
        return compare_result;
    }

    public void setComparison(int compare_result) {
        this.compare_result = compare_result;
    }

    public int compare(Object o) {
        NamedAcl a = (NamedAcl) o;

        if (!getRuleName().equals(a.getRuleName())) {
            return CompareInterface.NOT_FOUND;
        }

        if (!getRuleType().equals(a.getRuleType())) {
            return CompareInterface.NOT_FOUND;
        }

        if ((this.size() == 0) && (a.size() == 0)) {
            return CompareInterface.EQUAL;
        }

        int compare;
        int compareOverall = CompareInterface.EQUAL;
        AccessControl c;

        for (int i = 0; i < this.size(); i++) {
            c = this.get(i);
            compare = CompareInterface.NOT_FOUND;

            for (int k = 0; k < a.size(); k++) {
                compare = c.compare(a.get(k));
                if (compare != CompareInterface.NOT_FOUND) {
                    break;
                }
            }

            c.setComparison(compare);
            if (compare != CompareInterface.EQUAL) {
                compareOverall = CompareInterface.NOT_EQUAL;
            }

        }

        if ((compareOverall == CompareInterface.EQUAL) && (this.size() < a.size())) {
            compareOverall = CompareInterface.NOT_EQUAL;
        }

        return compareOverall;

    }
}
