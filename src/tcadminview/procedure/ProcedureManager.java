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
//import tcadminview.plmxmlpdm.*;
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
    
    //private ArrayList<WorkflowTemplateType> worklowTemplateList;
    private WorkflowTemplateList workflowTemplateList;
    private ArrayList<WorkflowHandlerType> workflowHanderList;
    private ArrayList<SiteType> siteList;
    private ArrayList<WorkflowActionType> workflowActionList;
    private ArrayList<WorkflowBusinessRuleHandlerType> workflowBusinessRuleHandlerList;
    private ArrayList<WorkflowBusinessRuleType> workflowBusinessRuleList;
    private ArrayList<WorkflowSignoffProfileType> workflowSignoffProfileList;
    private ArrayList<RoleType> roleList;
    private ArrayList<AccessIntentType> accessIntentList;
    private ArrayList<OrganisationType> organisationList;
    
    private Hashtable<String, Integer>idIndexLookup;
    private Hashtable<String, TagTypeEnum>idClassLookup;
    
    
    /**
     * Creates a new instance of ProcedureManager
     */
    public ProcedureManager(Node rootXMLNode) {
        idIndexLookup = new Hashtable<String, Integer>();
        idClassLookup = new Hashtable<String, TagTypeEnum>();
        
        //worklowTemplateList = new ArrayList<WorkflowTemplateType>();
        workflowTemplateList = new WorkflowTemplateList();
        workflowHanderList = new ArrayList<WorkflowHandlerType>();
        siteList = new ArrayList<SiteType>();
        workflowActionList = new ArrayList<WorkflowActionType>();
        workflowBusinessRuleHandlerList = new ArrayList<WorkflowBusinessRuleHandlerType>();
        workflowBusinessRuleList = new ArrayList<WorkflowBusinessRuleType>();
        workflowSignoffProfileList = new ArrayList<WorkflowSignoffProfileType>();
        roleList = new ArrayList<RoleType>();
        accessIntentList = new ArrayList<AccessIntentType>();
        organisationList = new ArrayList<OrganisationType>();
        
        decodeXML(rootXMLNode);
    }
    
    public int getIdIndex(String id) {
        if(id == null || id.equals(""))
            return -1;
        else {
            if(id.startsWith("#"))
                return idIndexLookup.get(id.substring(1)).intValue();
            else
                return idIndexLookup.get(id).intValue();
        }
    }
    
    public TagTypeEnum getIdClass(String id) {
        if(id == null || id.equals(""))
            return null;
        else {
            if(id.startsWith("#"))
                return idClassLookup.get(id.substring(1));
            else
                return idClassLookup.get(id);
        }
    }
    
    public ArrayList<String> getNamedReferences(List<String> id){
        ArrayList<String> names = new ArrayList<String>();
        for(int i=0; i<id.size(); i++){
            names.add(getNamedReference(id.get(i)));
        }
        return names;
    }
    
    public String getNamedReference(String id){
        switch(getIdClass(id)){
            case WorkflowTemplate:
                return workflowTemplateList.get(getIdIndex(id)).getName();
                
            case WorkflowHandler:
                return workflowHanderList.get(getIdIndex(id)).getName();
                
            case Site:
                return siteList.get(getIdIndex(id)).getName();
                
            case WorkflowAction:
                return workflowActionList.get(getIdIndex(id)).getName();
                
            case WorkflowBusinessRuleHandler:
                return workflowBusinessRuleHandlerList.get(getIdIndex(id)).getName();
                
            case WorkflowBusinessRule:
                return workflowBusinessRuleList.get(getIdIndex(id)).getName();
                
            case WorkflowSignoffProfile:
                return workflowSignoffProfileList.get(getIdIndex(id)).getName();
                
            case Role:
                return roleList.get(getIdIndex(id)).getName();
                
            case AccessIntent:
                return accessIntentList.get(getIdIndex(id)).getName();
                
            case Organisation:
                return organisationList.get(getIdIndex(id)).getName();
                
            default:
                return null;
        }
    }
    
    public ArrayList<AccessIntentType> getAccessIntents() {
        return accessIntentList;
    }
    
    public ArrayList<WorkflowActionType> getWorkflowActions() {
        return workflowActionList;
    }
    
    public ArrayList<WorkflowBusinessRuleType> getWorkflowBusinessRules() {
        return workflowBusinessRuleList;
    }
    
    public ArrayList<WorkflowBusinessRuleHandlerType> getWorkflowBusinessRuleHandlers() {
        return workflowBusinessRuleHandlerList;
    }
    
    public ArrayList<WorkflowHandlerType> getWorkflowHandlers() {
        return workflowHanderList;
    }
    
    public ArrayList<WorkflowSignoffProfileType> getWorkflowSignoffProfiles() {
        return workflowSignoffProfileList;
    }
    
    public /*ArrayList<WorkflowTemplateType>*/WorkflowTemplateList getWorkflowTemplates() {
        return workflowTemplateList;
    }
    
    public ArrayList<OrganisationType> getOrganisations() {
        return organisationList;
    }
    
    public ArrayList<RoleType> getRoles() {
        return roleList;
    }
    
    public ArrayList<SiteType> getSites() {
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
    
    private void setIdLookup(String id, TagTypeEnum tagType, int index) {
        idIndexLookup.put(id,index);
        idClassLookup.put(id, tagType);
    }
    
    private void decodeXML(Node node) {
        Node currentNode = node;
        NodeList list;
        
        try {
            /*
            //<PLMXML>
            list = currentNode.getChildNodes();
            currentNode = list.item(0);
            
            //Workflow List
            list = currentNode.getChildNodes();
             **/
            list = currentNode.getChildNodes();
            
            TagTypeEnum tagType;
            
            for(int i=0; i<list.getLength(); i++) {
                currentNode = list.item(i);
                //System.out.println("Node name: "+currentNode.getNodeName());
                tagType = TagTypeEnum.fromValue(currentNode.getNodeName());
                
                switch(tagType) {
                    case WorkflowTemplate:
                        WorkflowTemplateType wt = new WorkflowTemplateType(currentNode);
                        workflowTemplateList.add(wt);
                        setIdLookup(wt.getId(), TagTypeEnum.WorkflowTemplate, workflowTemplateList.size()-1);
                        break;
                        
                    case WorkflowHandler:
                        WorkflowHandlerType wh = new WorkflowHandlerType(currentNode);
                        workflowHanderList.add(wh);
                        setIdLookup(wh.getId(), TagTypeEnum.WorkflowHandler, workflowHanderList.size()-1);
                        break;
                        
                    case Site:
                        SiteType stype = new SiteType(currentNode);
                        siteList.add(stype);
                        setIdLookup(stype.getId(), TagTypeEnum.Site, siteList.size()-1);
                        break;
                        
                    case WorkflowAction:
                        WorkflowActionType wa = new WorkflowActionType(currentNode);
                        workflowActionList.add(wa);
                        setIdLookup(wa.getId(), TagTypeEnum.WorkflowAction, workflowActionList.size()-1);
                        break;
                        
                    case WorkflowBusinessRuleHandler:
                        WorkflowBusinessRuleHandlerType wbrh = new WorkflowBusinessRuleHandlerType(currentNode);
                        workflowBusinessRuleHandlerList.add(wbrh);
                        setIdLookup(wbrh.getId(), TagTypeEnum.WorkflowBusinessRuleHandler, workflowBusinessRuleHandlerList.size()-1);
                        break;
                        
                    case WorkflowBusinessRule:
                        WorkflowBusinessRuleType wbr = new WorkflowBusinessRuleType(currentNode);
                        workflowBusinessRuleList.add(wbr);
                        setIdLookup(wbr.getId(), TagTypeEnum.WorkflowBusinessRule, workflowBusinessRuleList.size()-1);
                        break;
                        
                    case WorkflowSignoffProfile:
                        WorkflowSignoffProfileType wsp = new WorkflowSignoffProfileType(currentNode);
                        workflowSignoffProfileList.add(wsp);
                        setIdLookup(wsp.getId(), TagTypeEnum.WorkflowSignoffProfile, workflowSignoffProfileList.size()-1);
                        break;
                        
                    case Role:
                        RoleType r = new RoleType(currentNode);
                        roleList.add(r);
                        setIdLookup(r.getId(), TagTypeEnum.Role, roleList.size()-1);
                        break;
                        
                    case AccessIntent:
                        AccessIntentType ai = new AccessIntentType(currentNode);
                        accessIntentList.add(ai);
                        setIdLookup(ai.getId(), TagTypeEnum.AccessIntent, accessIntentList.size()-1);
                        break;
                        
                    case Organisation:
                        OrganisationType o = new OrganisationType(currentNode);
                        organisationList.add(o);
                        setIdLookup(o.getId(), TagTypeEnum.Organisation, organisationList.size()-1);
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
