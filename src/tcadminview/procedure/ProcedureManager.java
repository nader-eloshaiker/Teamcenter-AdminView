/*
 * ProcedureManager.java
 *
 * Created on 21 July 2007, 11:22
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tcadminview.procedure;

import java.util.*;
import javax.xml.transform.dom.DOMResult;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.NamedNodeMap;
import org.xml.sax.InputSource;
import tcadminview.plmxmlpdm.*;
import tcadminview.plmxmlpdm.TagTypeEnum;
import tcadminview.plmxmlpdm.type.WorkflowHandlerType;
import tcadminview.plmxmlpdm.type.WorkflowTemplateType;
import tcadminview.plmxmlpdm.type.SiteType;
import tcadminview.plmxmlpdm.type.WorkflowActionType;
import tcadminview.plmxmlpdm.type.WorkflowBusinessRuleHandlerType;
import tcadminview.plmxmlpdm.type.WorkflowBusinessRuleType;
import tcadminview.plmxmlpdm.type.WorkflowSignoffProfileType;
import tcadminview.plmxmlpdm.type.RoleType;
import tcadminview.plmxmlpdm.type.AccessIntentType;
import tcadminview.plmxmlpdm.type.OrganisationType;
import tcadminview.xml.DOMUtil;

/**
 *
 * @author NZR4DL
 */
public class ProcedureManager {
    
    //private Vector<WorkflowTemplateType> worklowTemplateList;
    private WorkflowTemplateList workflowTemplateList;
    private Vector<WorkflowHandlerType> workflowHanderList;
    private Vector<SiteType> siteList;
    private Vector<WorkflowActionType> workflowActionList;
    private Vector<WorkflowBusinessRuleHandlerType> workflowBusinessRuleHandlerList;
    private Vector<WorkflowBusinessRuleType> workflowBusinessRuleList;
    private Vector<WorkflowSignoffProfileType> workflowSignoffProfileList;
    private Vector<RoleType> roleList;
    private Vector<AccessIntentType> accessIntentList;
    private Vector<OrganisationType> organisationList;
    
    
    
    public ProcedureManager(InputSource is) throws Exception {
        this(DOMUtil.createNodes(is));
    }
    
    /**
     * Creates a new instance of ProcedureManager
     */
    public ProcedureManager(Node rootXMLNode) throws Exception {
        //this.rootXMLNode = rootXMLNode;
        
        //worklowTemplateList = new Vector<WorkflowTemplateType>();
        workflowTemplateList = new WorkflowTemplateList();
        workflowHanderList = new Vector<WorkflowHandlerType>();
        siteList = new Vector<SiteType>();
        workflowActionList = new Vector<WorkflowActionType>();
        workflowBusinessRuleHandlerList = new Vector<WorkflowBusinessRuleHandlerType>();
        workflowBusinessRuleList = new Vector<WorkflowBusinessRuleType>();
        workflowSignoffProfileList = new Vector<WorkflowSignoffProfileType>();
        roleList = new Vector<RoleType>();
        accessIntentList = new Vector<AccessIntentType>();
        organisationList = new Vector<OrganisationType>();
        
        decodeXML(rootXMLNode);
    }
    
    public Vector<AccessIntentType> getAccessIntents() {
        return accessIntentList;
    }
    
    public Vector<WorkflowActionType> getWorkflowActions() {
        return workflowActionList;
    }
    
    public Vector<WorkflowBusinessRuleType> getWorkflowBusinessRules() {
        return workflowBusinessRuleList;
    }
    
    public Vector<WorkflowBusinessRuleHandlerType> getWorkflowBusinessRuleHandlers() {
        return workflowBusinessRuleHandlerList;
    }
    
    public Vector<WorkflowHandlerType> getWorkflowHandlers() {
        return workflowHanderList;
    }
    
    public Vector<WorkflowSignoffProfileType> getWorkflowSignoffProfiles() {
        return workflowSignoffProfileList;
    }
    
    public /*Vector<WorkflowTemplateType>*/WorkflowTemplateList getWorkflowTemplates() {
        return workflowTemplateList;
    }
    
    public Vector<OrganisationType> getOrganisations() {
        return organisationList;
    }

    public Vector<RoleType> getRoles() {
        return roleList;
    }
    
    public Vector<SiteType> getSites() {
        return siteList;
    }
    
    private void traverse(Node node) {
        NodeList list = node.getChildNodes();
        int length = list.getLength();
        for (int i=0; i<length; i++) {
            Node n = list.item(i);
            traverse(n);
        }
    }
    
    private void decodeXML(Node node) {
        Node currentNode = node;
        NodeList list;
        
        try {
        
        //<PLMXML>
        list = currentNode.getChildNodes();
        currentNode = list.item(0);
        
        //Workflow List
        list = currentNode.getChildNodes();
       
        TagTypeEnum tagType;
        
        for(int i=0; i<list.getLength(); i++) {
            currentNode = list.item(i);
            tagType = TagTypeEnum.fromValue(currentNode.getNodeName());
            
            switch(tagType) {
                case WorkflowTemplate:
                    workflowTemplateList.add(new WorkflowTemplateType(currentNode));
                    break;
                    
                case WorkflowHandler:
                    workflowHanderList.add(new WorkflowHandlerType(currentNode));
                    break;
                    
                case Site:
                    siteList.add(new SiteType(currentNode));
                    break;
                    
                case WorkflowAction:
                    workflowActionList.add(new WorkflowActionType(currentNode));
                    break;
                    
                case WorkflowBusinessRuleHandler:
                    workflowBusinessRuleHandlerList.add(new WorkflowBusinessRuleHandlerType(currentNode));
                    break;
                    
                case WorkflowBusinessRule:
                    workflowBusinessRuleList.add(new WorkflowBusinessRuleType(currentNode));
                    break;
                    
                case WorkflowSignoffProfile:
                    workflowSignoffProfileList.add(new WorkflowSignoffProfileType(currentNode));
                    break;
                    
                case Role:
                    roleList.add(new RoleType(currentNode));
                    break;
                    
                case AccessIntent:
                    accessIntentList.add(new AccessIntentType(currentNode));
                    break;
                    
                case Organisation:
                    organisationList.add(new OrganisationType(currentNode));
                    break;
                    
                default:
                    System.out.println("ProcedureManager: "+currentNode.getNodeName());
                    break;
            }
        }
        } catch (Exception ex) {
            System.err.println("XML Decode Error: "+ex.getMessage());
            ex.printStackTrace();
        }
        
    }
    
    
}
