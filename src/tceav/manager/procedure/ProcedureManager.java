/*
 * ProcedureManager.java
 *
 * Created on 21 July 2007, 11:22
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tceav.manager.procedure;

import java.util.*;
import java.io.*;
import javax.swing.*;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import tceav.manager.ManagerAdapter;
import tceav.manager.procedure.plmxmlpdm.HeaderType;
import tceav.manager.procedure.plmxmlpdm.TagTypeEnum;
import tceav.manager.procedure.plmxmlpdm.base.*;
import tceav.manager.procedure.plmxmlpdm.classtype.WorkflowTemplateClassificationEnum;
import tceav.manager.procedure.plmxmlpdm.type.*;
import tceav.xml.DOMReader;

/**
 *
 * @author NZR4DL
 */
public class ProcedureManager extends ManagerAdapter {
    
    private ArrayList<WorkflowTemplateType> workflowProcesses;
    private HeaderType header;
    private ArrayList<SiteType> site;
    private PLMXMLType plmxml;
    private File file;
    
    
    private JFrame frame;
    
    /**
     * Creates a new instance of ProcedureManager
     */
    public ProcedureManager(JFrame frame) {
        this.frame = frame;
    }
    
    public File getFile() {
        return file;
    }
    
    public String getManagerType() {
        return PROCEDURE_MANAGER_TYPE;
    }
    
    public void readFile(File file) throws Exception {
        this.file = file;
        FileInputStream fis = new FileInputStream(file);
        DOMReader domUtil;
        try {
            ProgressMonitorInputStream pmi = new ProgressMonitorInputStream(
                    frame,"Reading "+file.getName(),fis);
            domUtil = new DOMReader(pmi);
            
        } catch (Exception exc) {
            throw new Exception("Error reading XML: " + exc);
        }
        mapXML(domUtil.getRootNode());
    }
    
    public boolean isValid() {
        return (getWorkflowProcesses().size() != 0);
    }
    
    public ArrayList<WorkflowTemplateType> getWorkflowProcesses(){
        return workflowProcesses;
    }
    
    public ArrayList<SiteType> getSites() {
        return site;
    }
    
    private int siteIndex = -1;
    
    public SiteType getSite() {
        if(siteIndex == -1) {
            for(int i=0; i<site.size(); i++) {
                if(getPLMXML().getAuthor().indexOf(site.get(i).getSiteId()) > 0) {
                    siteIndex = i;
                    break;
                }
            }
            if(siteIndex == -1)
                siteIndex = 0;
        }

        return site.get(siteIndex);
    }
    
    public HeaderType getHeader() {
        return header;
    }
    
    public PLMXMLType getPLMXML() {
        return plmxml;
    }
    
    private void mapXML(Node parentNode) throws Exception {
        Node currentNode;
        NodeList list = parentNode.getChildNodes();
        int nodeCount = list.getLength();
        int nodeIndex = 0;
        
        if(TagTypeEnum.fromValue(parentNode.getNodeName()) == TagTypeEnum.PLMXML)
            plmxml = new PLMXMLType(parentNode);
        
        TagTypeEnum tagType;
        workflowProcesses = new ArrayList<WorkflowTemplateType>();
        site = new ArrayList<SiteType>();
        
        Hashtable<String,IdBase> tagCache = new Hashtable<String,IdBase>();
        
        try {
            ProgressMonitor progressMonitor = new ProgressMonitor(
                    frame,
                    "Mapping XML Tags to Procedures",
                    "",
                    0,
                    list.getLength()-1);
            
            
            for(int i=0; i<nodeCount; i++) {
                
                if (progressMonitor.isCanceled()) {
                    progressMonitor.close();
                    return;
                }
                
                currentNode = list.item(nodeIndex);
                tagType = TagTypeEnum.fromValue(currentNode.getNodeName());
                
                progressMonitor.setProgress(i);
                progressMonitor.setNote(tagType.value());
                
                switch(tagType) {
                    case Header:
                        header = new HeaderType(currentNode);
                        parentNode.removeChild(currentNode);
                        break;
                        
                    case Site:
                        site.add(new SiteType(currentNode));
                        parentNode.removeChild(currentNode);
                        break;
                        
                    case Text:
                        nodeIndex++;
                        break;
                        
                    case WorkflowTemplate:
                        WorkflowTemplateType wt = new WorkflowTemplateType(currentNode);
                        
                        if(wt.getTemplateClassification() == WorkflowTemplateClassificationEnum.PROCESS)
                            workflowProcesses.add(wt);
                        tagCache.put(wt.getId(),wt);
                        
                        parentNode.removeChild(currentNode);
                        break;
                        
                    case WorkflowAction:
                        WorkflowActionType wa = new WorkflowActionType(currentNode);
                        tagCache.put(wa.getId(), wa);
                        parentNode.removeChild(currentNode);
                        break;
                        
                    case WorkflowHandler:
                        WorkflowHandlerType wh = new WorkflowHandlerType(currentNode);
                        tagCache.put(wh.getId(), wh);
                        parentNode.removeChild(currentNode);
                        break;
                        
                    case WorkflowBusinessRule:
                        WorkflowBusinessRuleType wbr = new WorkflowBusinessRuleType(currentNode);
                        tagCache.put(wbr.getId(), wbr);
                        parentNode.removeChild(currentNode);
                        break;
                        
                    case WorkflowBusinessRuleHandler:
                        WorkflowBusinessRuleHandlerType wbrh = new WorkflowBusinessRuleHandlerType(currentNode);
                        tagCache.put(wbrh.getId(), wbrh);
                        parentNode.removeChild(currentNode);
                        break;
                        
                        
                        
                    case WorkflowSignoffProfile:
                        WorkflowSignoffProfileType wsp = new WorkflowSignoffProfileType(currentNode);
                        tagCache.put(wsp.getId(), wsp);
                        parentNode.removeChild(currentNode);
                        break;
                        
                    case Role:
                        RoleType r = new RoleType(currentNode);
                        tagCache.put(r.getId(), r);
                        parentNode.removeChild(currentNode);
                        break;
                        
                    case AccessIntent:
                        AccessIntentType ai = new AccessIntentType(currentNode);
                        tagCache.put(ai.getId(), ai);
                        parentNode.removeChild(currentNode);
                        break;
                        
                    case Organisation:
                        OrganisationType o = new OrganisationType(currentNode);
                        tagCache.put(o.getId(), o);
                        parentNode.removeChild(currentNode);
                        break;
                        
                    default:
                        nodeIndex++;
                        break;
                }
            }
            
            for(int iP=0; iP<workflowProcesses.size(); iP++) {
                processSubWorkflows(workflowProcesses.get(iP), tagCache);
                processAttributes(workflowProcesses.get(iP), tagCache);
                processDependantTasks(workflowProcesses.get(iP), tagCache);
            }
            
            
        } catch (Exception ex) {
            System.err.println("Map XML Error: "+ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    private void processAttributes(WorkflowTemplateType node, Hashtable<String,IdBase> tagCache) {
        AttributeModel model = new AttributeModel(tagCache);
        model.processNodeAttributes(node);
        
        for(int k=0; k<node.getActions().length; k++)
            model.processNodeAttributes(node.getActions()[k]);
        
        for(int j=0; j<node.getSubTemplates().length; j++)
            model.processNodeAttributes(node.getSubTemplates()[j]);
        
        sort(node, tagCache);
        
    }
    
    private void sort(WorkflowTemplateType wt, Hashtable<String,IdBase> tagCache) {
        ArrayList<String> order = new ArrayList<String>();
        WorkflowTemplateType tmp;
        
        getOrder(wt, wt.getId(), order, tagCache);
        if(order.size() != wt.getSubTemplates().length)
            return;
        
        int counter = order.size();
        
        for(int i=0; i<order.size(); i++) {
            counter--;
            for(int j=0; j<wt.getSubTemplates().length; j++) {
                if(i == j)
                    continue;
                if(order.get(counter).equals(wt.getSubTemplates()[j].getId())) {
                    tmp =  wt.getSubTemplates()[i];
                    wt.getSubTemplates()[i] = wt.getSubTemplates()[j];
                    wt.getSubTemplates()[j] = tmp;
                }
            }
        }
    }
    
    private void getOrder(WorkflowTemplateType wt, String parentId, ArrayList<String> order, Hashtable<String,IdBase> tagCache) {
        UserDataType ud;
        IdBase dataRef;
        WorkflowTemplateType wtRef;
        
        for(int i=0; i<wt.getAttribute().size(); i++) {
            if((wt.getAttribute().get(i).getTagType() == TagTypeEnum.UserData) ||
                    (wt.getAttribute().get(i).getTagType() == TagTypeEnum.Arguments)) {
                
                ud = (UserDataType)wt.getAttribute().get(i);
                
                if(ud.getType().equals("reference")) {
                    for(int j=0; j<ud.getUserValue().size(); j++) {
                        
                        dataRef = tagCache.get(ud.getUserValue().get(i).getDataRef());
                        if(dataRef.getTagType() == TagTypeEnum.WorkflowTemplate) {
                            
                            wtRef = (WorkflowTemplateType)dataRef;
                            if(parentId.equals(wtRef.getParentTaskTemplateRef())) {
                                if(order.indexOf(wtRef.getId()) == -1)
                                    order.add(wtRef.getId());
                                
                                getOrder(wtRef, parentId, order, tagCache);
                            }
                        }
                        
                    }
                }
            }
        }
    }
    
    private void processSubWorkflows(WorkflowTemplateType node, Hashtable<String, IdBase> tagCache) {
        processActions(node, tagCache);
        
        for(int i=0; i<node.getSubTemplateRefs().size(); i++) {
            node.getSubTemplates()[i] = (WorkflowTemplateType)tagCache.get(node.getSubTemplateRefs().get(i));
            node.getSubTemplates()[i].setParentSubTaskTemplate(node);
            node.getSubTemplates()[i].setParentTaskTemplate((WorkflowTemplateType)tagCache.get(node.getSubTemplates()[i].getParentTaskTemplateRef()));
            processSubWorkflows(node.getSubTemplates()[i], tagCache);
        }
    }
    
    private void processDependantTasks(WorkflowTemplateType node, Hashtable<String, IdBase> tagCache) {
        for(int iD=0; iD<node.getDependencyTaskTemplateRefs().size(); iD++) {
            node.getDependantTaskTemplates()[iD] = (WorkflowTemplateType)tagCache.get(node.getDependencyTaskTemplateRefs().get(iD));
            processDependantTasks(node.getDependantTaskTemplates()[iD], tagCache);
        }
    }
    
    private void processActions(WorkflowTemplateType node, Hashtable<String, IdBase> tagCache) {
        for(int iA=0; iA<node.getActionRefs().size(); iA++) {
            node.getActions()[iA] = (WorkflowActionType)tagCache.get(node.getActionRefs().get(iA));
            attachActionHandlers(node.getActions()[iA], tagCache);
            attachBusinessRules(node.getActions()[iA], tagCache);
        }
        
        WorkflowActionType tmp;
        
        for (int i = 0; i < node.getActions().length; i++) {
            for (int j = i+1; j < node.getActions().length; j++) {
                if (node.getActions()[i].getType().getOrder() > node.getActions()[j].getType().getOrder()) {
                    tmp = node.getActions()[i];
                    node.getActions()[i] = node.getActions()[j];
                    node.getActions()[j] = tmp;
                }
            }
        }
    }
    
    private void attachActionHandlers(WorkflowActionType node, Hashtable<String, IdBase> tagCache) {
        for(int iH=0; iH<node.getActionHandlerRefs().size(); iH++) {
            node.getActionHandlers()[iH] = (WorkflowHandlerType)tagCache.get(node.getActionHandlerRefs().get(iH));
        }
    }
    
    private void attachBusinessRules(WorkflowActionType node, Hashtable<String, IdBase> tagCache){
        for(int iR=0; iR<node.getRuleRefs().size(); iR++) {
            node.getRules()[iR] = (WorkflowBusinessRuleType)tagCache.get(node.getRuleRefs().get(iR));
            attachBusinessRuleHandlers(node.getRules()[iR], tagCache);
        }
    }
    
    private void attachBusinessRuleHandlers(WorkflowBusinessRuleType node, Hashtable<String, IdBase> tagCache){
        for(int irh=0; irh<node.getRuleHandlerRefs().size(); irh++) {
            node.getRuleHandlers()[irh] = (WorkflowBusinessRuleHandlerType)tagCache.get(node.getRuleHandlerRefs().get(irh));
        }
    }
    
}
