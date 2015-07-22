/*
 * ProcedureManager.java
 *
 * Created on 21 July 2007, 11:22
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tcav.procedure;

import java.util.*;
import java.io.*;
import javax.swing.*;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.NamedNodeMap;

import tcav.plmxmlpdm.HeaderType;
import tcav.plmxmlpdm.TagTypeEnum;
import tcav.plmxmlpdm.base.AttribOwnerBase;
import tcav.plmxmlpdm.type.WorkflowHandlerType;
import tcav.plmxmlpdm.type.WorkflowTemplateType;
import tcav.plmxmlpdm.type.SiteType;
import tcav.plmxmlpdm.type.WorkflowActionType;
import tcav.plmxmlpdm.type.WorkflowBusinessRuleHandlerType;
import tcav.plmxmlpdm.type.WorkflowBusinessRuleType;
import tcav.plmxmlpdm.type.WorkflowSignoffProfileType;
import tcav.plmxmlpdm.type.RoleType;
import tcav.plmxmlpdm.type.AccessIntentType;
import tcav.plmxmlpdm.type.OrganisationType;
import tcav.xml.DOMUtil;

/**
 *
 * @author NZR4DL
 */
public class ProcedureManager {
    
    //private ArrayList<WorkflowTemplateType> worklowTemplateList;
    private HeaderType header;
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
    
    private Hashtable<String, Integer> idIndexLookup;
    private Hashtable<String, TagTypeEnum> idClassLookup;
    
    private JFrame frame;
    private DOMUtil domUtil;
    
    public final static String[] WORKFLOW_TYPES_NAMES = {
            "Action",
            "Business Rule",
            "Business Handler",
            "Handler",
            "Template"
    };
    
    public final static TagTypeEnum[] WORKFLOW_TYPES = {
        TagTypeEnum.WorkflowAction,
        TagTypeEnum.WorkflowBusinessRule,
        TagTypeEnum.WorkflowBusinessRuleHandler,
        TagTypeEnum.WorkflowHandler,
        TagTypeEnum.WorkflowTemplate
    };
    
    
    /**
     * Creates a new instance of ProcedureManager
     */
    public ProcedureManager(JFrame frame) {
        this.frame = frame;
        
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
        
    }
    
    public void readFile(File file) throws Exception {
        FileInputStream fis = new FileInputStream(file);
        try {
            ProgressMonitorInputStream pmi = new ProgressMonitorInputStream(
                    frame,"Reading "+file.getName(),fis);
            domUtil = new DOMUtil(pmi);
            
        } catch (Exception exc) {
            throw new Exception("Error reading XML: " + exc);
        }
        decodeXML(domUtil.getRootNode());
    }
    
    
    private String toStringId(String id){
        if(id.charAt(0)== '#')
            return id.substring(1);
        else
            return id;
    }
    
    public int getIdIndex(String id) {
        if(id == null || id.equals(""))
            return -1;
        else
            return idIndexLookup.get(toStringId(id)).intValue();
        
    }
    
    public TagTypeEnum getIdClass(String id) {
        if(id == null || id.equals(""))
            return null;
        else
            return idClassLookup.get(toStringId(id));
        
    }
    
    public AttribOwnerBase getAttribOwnerBase(String id){
        if(id == null || id.equals(""))
            return null;
        switch(getIdClass(id)){
            case WorkflowTemplate:
                return workflowTemplateList.get(getIdIndex(id));
                
            case WorkflowHandler:
                return workflowHanderList.get(getIdIndex(id));
                
            case Site:
                return siteList.get(getIdIndex(id));
                
            case WorkflowAction:
                return workflowActionList.get(getIdIndex(id));
                
            case WorkflowBusinessRuleHandler:
                return workflowBusinessRuleHandlerList.get(getIdIndex(id));
                
            case WorkflowBusinessRule:
                return workflowBusinessRuleList.get(getIdIndex(id));
                
            case WorkflowSignoffProfile:
                return workflowSignoffProfileList.get(getIdIndex(id));
                
            case Role:
                return roleList.get(getIdIndex(id));
                
            case AccessIntent:
                return accessIntentList.get(getIdIndex(id));
                
            case Organisation:
                return organisationList.get(getIdIndex(id));
                
            default:
                return null;
        }
        
    }
    
    public HeaderType getHeader() {
        return header;
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
        idIndexLookup.put(id, index);
        idClassLookup.put(id, tagType);
    }
    
    private void decodeXML(Node node) {
        Node currentNode = node;
        NodeList list;
        
        try {
            list = currentNode.getChildNodes();
            
            ProgressMonitor progressMonitor = new ProgressMonitor(
                    frame,
                    "Decoding XML Tags",
                    "",
                    0,
                    list.getLength()-1);
            
            if (progressMonitor.isCanceled()) {
                progressMonitor.close();
                return;
            }
            
            TagTypeEnum tagType;
            
            for(int i=0; i<list.getLength(); i++) {
                currentNode = list.item(i);
                tagType = TagTypeEnum.fromValue(currentNode.getNodeName());
                
                progressMonitor.setProgress(i);
                progressMonitor.setNote(tagType.value());
                
                switch(tagType) {
                    case Header:
                        header = new HeaderType(currentNode);
                        setIdLookup(header.getId(),TagTypeEnum.Header,0);
                        break;
                        
                    case Text:
                        break;
                        
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
