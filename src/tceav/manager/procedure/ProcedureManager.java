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
import tceav.gui.AdminViewFrame;
import tceav.manager.ManagerAdapter;
import tceav.manager.procedure.plmxmlpdm.ProcedureHeaderType;
import tceav.manager.procedure.plmxmlpdm.ProcedureTagTypeEnum;
import tceav.manager.procedure.plmxmlpdm.base.IdBase;
import tceav.manager.procedure.plmxmlpdm.classtype.WorkflowTemplateClassificationEnum;
import tceav.manager.procedure.plmxmlpdm.type.AccessIntentType;
import tceav.manager.procedure.plmxmlpdm.type.OrganisationType;
import tceav.manager.procedure.plmxmlpdm.type.PLMXMLType;
import tceav.manager.procedure.plmxmlpdm.type.RoleType;
import tceav.manager.procedure.plmxmlpdm.type.SiteType;
import tceav.manager.procedure.plmxmlpdm.type.WorkflowActionType;
import tceav.manager.procedure.plmxmlpdm.type.WorkflowBusinessRuleHandlerType;
import tceav.manager.procedure.plmxmlpdm.type.WorkflowBusinessRuleType;
import tceav.manager.procedure.plmxmlpdm.type.WorkflowHandlerType;
import tceav.manager.procedure.plmxmlpdm.type.WorkflowSignoffProfileType;
import tceav.manager.procedure.plmxmlpdm.type.WorkflowTemplateType;
import tceav.xml.DOMReader;

/**
 *
 * @author NZR4DL
 */
public class ProcedureManager extends ManagerAdapter {
    
    private ArrayList<WorkflowTemplateType> workflowProcesses;
    private ProcedureHeaderType header;
    private ArrayList<SiteType> site;
    private PLMXMLType plmxml;
    private File file;
    private AdminViewFrame parentFrame;
    
    /**
     * Creates a new instance of ProcedureManager
     */
    public ProcedureManager(AdminViewFrame parentFrame) {
        this.parentFrame = parentFrame;
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
                    parentFrame, "Reading " + file.getName(), fis);
            domUtil = new DOMReader(pmi);
            
        } catch (Exception exc) {
            throw new Exception("Error reading XML: " + exc);
        }
        mapXML(domUtil.getRootNode());
    }
    
    public boolean isValid() {
        return (!getWorkflowProcesses().isEmpty());
    }
    
    public ArrayList<WorkflowTemplateType> getWorkflowProcesses() {
        return workflowProcesses;
    }
    
    public ArrayList<SiteType> getSites() {
        return site;
    }
    private int siteIndex = -1;
    
    public SiteType getSite() {
        if (siteIndex == -1) {
            for (int i = 0; i < site.size(); i++) {
                if (getPLMXML().getAuthor().indexOf(site.get(i).getSiteId()) > 0) {
                    siteIndex = i;
                    break;
                }
            }
            if (siteIndex == -1) {
                siteIndex = 0;
            }
        }
        
        return site.get(siteIndex);
    }
    
    public ProcedureHeaderType getHeader() {
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
        
        if (ProcedureTagTypeEnum.fromValue(parentNode.getNodeName()) == ProcedureTagTypeEnum.PLMXML) {
            plmxml = new PLMXMLType(parentNode);
        }
        ProcedureTagTypeEnum tagType;
        workflowProcesses = new ArrayList<WorkflowTemplateType>();
        site = new ArrayList<SiteType>();
        
        Hashtable<String, IdBase> tagCache = new Hashtable<String, IdBase>();
        
        try {
            ProgressMonitor progressMonitor = new ProgressMonitor(
                    parentFrame,
                    "Mapping XML Tags to Procedures",
                    "",
                    0,
                    list.getLength() - 1);
            
            
            for (int i = 0; i < nodeCount; i++) {
                
                if (progressMonitor.isCanceled()) {
                    progressMonitor.close();
                    return;
                }
                
                currentNode = list.item(nodeIndex);
                tagType = ProcedureTagTypeEnum.fromValue(currentNode.getNodeName());
                
                progressMonitor.setProgress(i);
                progressMonitor.setNote(tagType.value());
                
                switch (tagType) {
                    case Header:
                        header = new ProcedureHeaderType(currentNode);
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
                        
                        if (wt.getTemplateClassification() == WorkflowTemplateClassificationEnum.PROCESS) {
                            workflowProcesses.add(wt);
                        }
                        tagCache.put(wt.getId(), wt);
                        
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
            
            for (int k = 0; k < workflowProcesses.size(); k++) {
                processSubWorkflows(workflowProcesses.get(k), tagCache);
                sortSubWorkflows(workflowProcesses.get(k));
                processAttributes(workflowProcesses.get(k), tagCache);
            }
            
            
        } catch (Exception ex) {
            System.err.println("Map XML Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    private void processSubWorkflows(WorkflowTemplateType node, Hashtable<String, IdBase> tagCache) {
        processActions(node, tagCache);
        
        for (int i = 0; i < node.getSubTemplateRefs().size(); i++) {
            node.getSubTemplates()[i] = (WorkflowTemplateType) tagCache.get(node.getSubTemplateRefs().get(i));
            node.getSubTemplates()[i].setParentTaskTemplate(node);
            processSubWorkflows(node.getSubTemplates()[i], tagCache);
        }
    }
    
    private void processActions(WorkflowTemplateType node, Hashtable<String, IdBase> tagCache) {
        for (int iA = 0; iA < node.getActionRefs().size(); iA++) {
            node.getActions()[iA] = (WorkflowActionType) tagCache.get(node.getActionRefs().get(iA));
            attachActionHandlers(node.getActions()[iA], tagCache);
            attachBusinessRules(node.getActions()[iA], tagCache);
        }
        
        WorkflowActionType tmp;
        
        for (int i = 0; i < node.getActions().length; i++) {
            for (int j = i + 1; j < node.getActions().length; j++) {
                if (node.getActions()[i].getType().getOrder() > node.getActions()[j].getType().getOrder()) {
                    tmp = node.getActions()[i];
                    node.getActions()[i] = node.getActions()[j];
                    node.getActions()[j] = tmp;
                }
            }
        }
    }
    
    private void processAttributes(WorkflowTemplateType node, Hashtable<String, IdBase> tagCache) {
        AttributeModel model = new AttributeModel(tagCache);
        model.processNodeAttributes(node);
        
        for (int k = 0; k < node.getActions().length; k++) {
            model.processNodeAttributes(node.getActions()[k]);
        }

        
        for (int j = 0; j < node.getSubTemplates().length; j++) {
            model.processNodeAttributes(node.getSubTemplates()[j]);
        }
    }
    
    private void attachActionHandlers(WorkflowActionType node, Hashtable<String, IdBase> tagCache) {
        for (int iH = 0; iH < node.getActionHandlerRefs().size(); iH++) {
            node.getActionHandlers()[iH] = (WorkflowHandlerType) tagCache.get(node.getActionHandlerRefs().get(iH));
        }
    }
    
    private void attachBusinessRules(WorkflowActionType node, Hashtable<String, IdBase> tagCache) {
        for (int iR = 0; iR < node.getRuleRefs().size(); iR++) {
            node.getRules()[iR] = (WorkflowBusinessRuleType) tagCache.get(node.getRuleRefs().get(iR));
            attachBusinessRuleHandlers(node.getRules()[iR], tagCache);
        }
    }
    
    private void attachBusinessRuleHandlers(WorkflowBusinessRuleType node, Hashtable<String, IdBase> tagCache) {
        for (int irh = 0; irh < node.getRuleHandlerRefs().size(); irh++) {
            node.getRuleHandlers()[irh] = (WorkflowBusinessRuleHandlerType) tagCache.get(node.getRuleHandlerRefs().get(irh));
        }
    }

    private void sortSubWorkflows(WorkflowTemplateType node) {
        for (int j = 0; j < node.getSubTemplates().length; j++) {
            sortSubWorkflows(node.getSubTemplates()[j]);
        }
        sort(node);
    }
    
    private void sort(WorkflowTemplateType wt) {
        if(wt.getSubTemplates().length == 0)
            return;
        
        WorkflowTemplateType[] subTasks = wt.getSubTemplates();
        List<String> subTasksRefs = wt.getSubTemplateRefs();
        List<String> dependantRefs = wt.getDependencyTaskTemplateRefs();
        ArrayList<WorkflowTemplateType> sortedTasks = new ArrayList<WorkflowTemplateType>();
        
        /* Create a snap shot of the sub tasks */
        for (int i = 0; i < subTasks.length; i++) {
            sortedTasks.add(subTasks[i]);
        }
        
        /* Locate final tasks and place them at the end */
        int matchCount = 0;
        for (int i = 0; i < dependantRefs.size(); i++) {
            for (int j = 0; j < sortedTasks.size(); j++) {
                if (dependantRefs.get(i).equals(sortedTasks.get(j).getId())) {
                    swap(j, sortedTasks.size() - 1 - matchCount, sortedTasks);
                    matchCount++;
                    break;
                }
            }
        }
        
        /* Printing for debug */
        //System.out.println("---- " + wt.getName() + " ----");
        //print(sortedTasks);
        
        /* Back to front sort dependancies */
        int index;
        for (int j = sortedTasks.size() - 1; j >= 0; j--) {
            dependantRefs = sortedTasks.get(j).getDependencyTaskTemplateRefs();
            index = indexOfChild(dependantRefs, sortedTasks);
            
            if (index == -1) {
                if (j == 0) {
                    continue;
                } else {
                    swap(j, 0, sortedTasks);
                    continue;
                }
            }
            
            if (index > j) {
                move(j, index + 1, sortedTasks);
                j = index;
            } else if (index < j) {
                swap(index, j - 1, sortedTasks);
            }
            
            /* Printing for debug */
            //print(sortedTasks);
        }
        /* Printing for debug */
        //System.out.println();
        
        /* Apply sort to refs */
        for (int j = 0; j < sortedTasks.size(); j++) {
            subTasks[j] = sortedTasks.get(j);
            subTasksRefs.set(j, sortedTasks.get(j).getId());
        }
        
    }
    
    private void swap(int index1, int index2, List array) {
        Object tmp = array.get(index1);
        array.set(index1, array.get(index2));
        array.set(index2, tmp);
    }
    
    private void move(int src, int dst, List array) {
        Object tmp = array.remove(src);
        if (dst > src) {
            array.add(dst - 1, tmp);
        } else {
            array.add(dst, tmp);
        }
    }
    
    private int indexOfChild(List<String> ids, ArrayList<WorkflowTemplateType> array) {
        for (int j = 0; j < ids.size(); j++) {
            if (ids.get(j) == null) {
                return -1;
            }
            for (int i = 0; i < array.size(); i++) {
                if (ids.get(j).equals(array.get(i).getId())) {
                    return i;
                }
            }
        }
        return -1;
    }
    
    private void print(ArrayList<WorkflowTemplateType> array) {
        for (int i = 0; i < array.size(); i++) {
            System.out.print(array.get(i).getName() + " ; ");
        }
        System.out.println();
    }
    
}
