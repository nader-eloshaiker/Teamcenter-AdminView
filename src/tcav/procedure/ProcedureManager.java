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
import tcav.procedure.plmxmlpdm.HeaderType;
import tcav.procedure.plmxmlpdm.TagTypeEnum;
import tcav.procedure.plmxmlpdm.base.AttribOwnerBase;
import tcav.procedure.plmxmlpdm.base.IdBase;
import tcav.procedure.plmxmlpdm.classtype.WorkflowTemplateClassificationEnum;
import tcav.procedure.plmxmlpdm.type.AccessIntentType;
import tcav.procedure.plmxmlpdm.type.AssociatedDataSetType;
import tcav.procedure.plmxmlpdm.type.AssociatedFolderType;
import tcav.procedure.plmxmlpdm.type.AssociatedFormType;
import tcav.procedure.plmxmlpdm.type.OrganisationType;
import tcav.procedure.plmxmlpdm.type.RoleType;
import tcav.procedure.plmxmlpdm.type.SiteType;
import tcav.procedure.plmxmlpdm.type.UserDataType;
import tcav.procedure.plmxmlpdm.type.WorkflowActionType;
import tcav.procedure.plmxmlpdm.type.WorkflowBusinessRuleHandlerType;
import tcav.procedure.plmxmlpdm.type.WorkflowBusinessRuleType;
import tcav.procedure.plmxmlpdm.type.WorkflowHandlerType;
import tcav.procedure.plmxmlpdm.type.WorkflowSignoffProfileType;
import tcav.procedure.plmxmlpdm.type.WorkflowTemplateType;
import tcav.xml.DOMUtil;

import tcav.procedure.plmxmlpdm.base.AttributeBase;

/**
 *
 * @author NZR4DL
 */
public class ProcedureManager {
    
    private ArrayList<WorkflowTemplateType> workflowProcesses;
    private HeaderType header;
    private SiteType site;
    
    private JFrame frame;
    
    /**
     * Creates a new instance of ProcedureManager
     */
    public ProcedureManager(JFrame frame) {
        this.frame = frame;
    }
    
    public void readFile(File file) throws Exception {
        FileInputStream fis = new FileInputStream(file);
        DOMUtil domUtil;
        try {
            ProgressMonitorInputStream pmi = new ProgressMonitorInputStream(
                    frame,"Reading "+file.getName(),fis);
            domUtil = new DOMUtil(pmi);
            
        } catch (Exception exc) {
            throw new Exception("Error reading XML: " + exc);
        }
        mapXML(domUtil.getRootNode());
    }
    
    public ArrayList<WorkflowTemplateType> getWorkflowProcesses(){
        return workflowProcesses;
    }
    
    public SiteType getSite() {
        return site;
    }
    
    public HeaderType getHeader() {
        return header;
    }
    
    private void mapXML(Node parentNode) throws Exception {
        Node currentNode;
        NodeList list = parentNode.getChildNodes();
        int nodeCount = list.getLength();
        int nodeIndex = 0;
        
        TagTypeEnum tagType;
        workflowProcesses = new ArrayList<WorkflowTemplateType>();
        
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
                        site = new SiteType(currentNode);
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
                processDependantTasks(workflowProcesses.get(iP), tagCache);
                processAttributes(workflowProcesses.get(iP), tagCache);
            }
            
            
        } catch (Exception ex) {
            System.err.println("Map XML Error: "+ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    private void processAttributes(WorkflowTemplateType node, Hashtable<String,IdBase> tagCache) {
        AttributeModel model = new AttributeModel(tagCache);
        model.processNodeAttributes(node);
        
        for(int k=0; k<node.getActionRefs().size(); k++)
            model.processNodeAttributes(node.getActions()[k]);
        
        for(int j=0; j<node.getSubTemplateRefs().size(); j++) {
            model.processNodeAttributes(node.getSubTemplates()[j]);
        }
    }
    
    private void processSubWorkflows(WorkflowTemplateType node, Hashtable<String, IdBase> tagCache) {
        processActions(node, tagCache);
        
        for(int i=0; i<node.getSubTemplateRefs().size(); i++) {
            node.getSubTemplates()[i] = (WorkflowTemplateType)tagCache.get(node.getSubTemplateRefs().get(i));
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
