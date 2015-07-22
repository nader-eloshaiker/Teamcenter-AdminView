/*
 * ProcedureNodeBuilder.java
 *
 * Created on 4 August 2007, 09:52
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tcadminview.gui.procedure;

import tcadminview.procedure.*;
import tcadminview.plmxmlpdm.type.*;
import tcadminview.plmxmlpdm.base.*;
import tcadminview.plmxmlpdm.classtype.*;
import javax.swing.JTree;
import javax.swing.tree.*;
import java.util.*;

/**
 *
 * @author NZR4DL
 */
public class ProcedureNodeBuilder {
    
    /** Creates a new instance of ProcedureNodeBuilder */
    public static DefaultMutableTreeNode buildProcessNodes(ProcedureManager pm) {
        DefaultMutableTreeNode topNode = new DefaultMutableTreeNode(
                new WorkflowTreeItem(
                     pm.getSites().get(0).getId(),pm.getSites().get(0).getName()));
        ArrayList<Integer> WorkflowProcessIds = pm.getWorkflowTemplates().getIndexesForClassification(WorkflowTemplateClassificationEnum.PROCESS);
        List<String> taskWorkflows;
        
        //Build process Nodes from root
        for(int i=0; i<WorkflowProcessIds.size(); i++){
            WorkflowTemplateType wt = pm.getWorkflowTemplates().get(WorkflowProcessIds.get(i));
            WorkflowTreeItem wti = new WorkflowTreeItem(wt.getId(), wt.getName(), wt.getIconKey());
            DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(wti);
            topNode.add(newNode);
            buildWorkflows(pm, wt, newNode);
        }
        
        return topNode;
    }
    
    private static void buildWorkflows(ProcedureManager pm, WorkflowTemplateType wt, DefaultMutableTreeNode parentNode){
        List<String> taskWorkflows = wt.getDependencyTaskTemplateRefs();
        List<String> subWorkflows = wt.getSubTemplateRefs();
        List<String> actionWorkflows = wt.getActions();
        DefaultMutableTreeNode dependTaskNode = new DefaultMutableTreeNode(
                new WorkflowTreeItem(
                    "Dependant Workflow Tasks", WorkflowTreeItem.DEPENDANT_TASKS));
        DefaultMutableTreeNode subWorkflowNode = new DefaultMutableTreeNode(
                new WorkflowTreeItem(
                    "Sub Workflows", WorkflowTreeItem.SUB_WORKFLOW));
        DefaultMutableTreeNode actionWorkflowNode = new DefaultMutableTreeNode(
                new WorkflowTreeItem(
                    "Workflow Actions",WorkflowTreeItem.WORKFLOW_ACTION));
        int maxLength = 0;
        int taskLength = 0;
        int subLength = 0;
        int actionLength = 0;
        
        if(taskWorkflows.size() != 0){
            taskLength = taskWorkflows.size();
            parentNode.add(dependTaskNode);
        }
        if(subWorkflows.size() != 0) {
            subLength = subWorkflows.size();
            parentNode.add(subWorkflowNode);
        }
        if(actionWorkflows.size() != 0) {
            actionLength = actionWorkflows.size();
            parentNode.add(actionWorkflowNode);
        }
        
        if( taskLength > maxLength)
            maxLength = taskLength;
        if(subLength > maxLength)
            maxLength = subLength;
        if(actionLength > maxLength)
            maxLength = actionLength;
        
        //Build Nodes
        for(int k=0; k<maxLength; k++) {
            
            if(k<taskLength) {
                WorkflowTemplateType wtTemp = pm.getWorkflowTemplates().get(pm.getIdIndex(taskWorkflows.get(k)));
                WorkflowTreeItem wti = new WorkflowTreeItem(wtTemp.getId(), wtTemp.getName(), wtTemp.getIconKey());
                DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(wti);
                dependTaskNode.add(newNode);
                buildWorkflows(pm, wtTemp, newNode);
            }
            if(k<subLength){
                WorkflowTemplateType wtTemp = pm.getWorkflowTemplates().get(pm.getIdIndex(subWorkflows.get(k)));
                WorkflowTreeItem wti = new WorkflowTreeItem(wtTemp.getId(), wtTemp.getName(), wtTemp.getIconKey());
                DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(wti);
                subWorkflowNode.add(newNode);
                buildWorkflows(pm, wtTemp, newNode);
            }
            if(k<actionLength){
                WorkflowActionType waTemp = pm.getWorkflowActions().get(pm.getIdIndex(actionWorkflows.get(k)));
                WorkflowTreeItem wti = new WorkflowTreeItem(waTemp.getId(), "Action "+k, waTemp.getActionType());
                DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(wti);
                actionWorkflowNode.add(newNode);
                buildActionHandlers(pm, waTemp, newNode);
            }
        }
    }
    
    private static void buildActionHandlers(ProcedureManager pm, WorkflowActionType wa, DefaultMutableTreeNode parentNode) {
        List<String> actionHandlers = wa.getActionHandlerRefs();
        List<String> rules = wa.getRuleRefs();
        DefaultMutableTreeNode actionHandlerNode = new DefaultMutableTreeNode(
                new WorkflowTreeItem(
                    "Workflow Handler",WorkflowTreeItem.WORKFLOW_HANDLER));
        DefaultMutableTreeNode businessRulesNode = new DefaultMutableTreeNode(
                new WorkflowTreeItem(
                    "Workflow Business Rules",WorkflowTreeItem.WORKFLOW_BUSINESS_RULE));
        int actionLength = 0;
        int rulesLength = 0;
        int maxLength = 0;
        
        if(actionHandlers.size() != 0){
            actionLength = actionHandlers.size();
            parentNode.add(actionHandlerNode);
        }
        if(rules.size() != 0){
            rulesLength = rules.size();
            parentNode.add(businessRulesNode);
        }
        
        if( actionLength > maxLength)
            maxLength = actionLength;
        if(rulesLength > maxLength)
            maxLength = rulesLength;
        
        //Build Nodes
        for(int k=0; k<maxLength; k++) {
            
            if(k < rulesLength){
                WorkflowBusinessRuleType wbrTemp = pm.getWorkflowBusinessRules().get(pm.getIdIndex(rules.get(k)));
                WorkflowTreeItem wti = new WorkflowTreeItem(wbrTemp.getId(), "Rule "+k);
                DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(wti);
                businessRulesNode.add(newNode);
                buildBusinessRuleHandler(pm, wbrTemp, newNode);
            }
            
            if(k < actionLength) {
                WorkflowHandlerType wh = pm.getWorkflowHandlers().get(pm.getIdIndex(actionHandlers.get(k)));
                WorkflowTreeItem wti = new WorkflowTreeItem(wh.getId(), wh.getName());
                DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(wti);
                actionHandlerNode.add(newNode);
            }
            
        }
    }
    
    private static void buildBusinessRuleHandler(ProcedureManager pm, WorkflowBusinessRuleType wbr, DefaultMutableTreeNode parentNode) {
        List<String> businessRuleHandlers = wbr.getRuleHandlerRefs();
        DefaultMutableTreeNode businessRuleHandlerNode = new DefaultMutableTreeNode(
                new WorkflowTreeItem(
                    "Business Rule Handler",WorkflowTreeItem.WORKFLOW_BUSINESS_RULE_HANDLER));
        int ruleHandlerLength = businessRuleHandlers.size();
        
        if(businessRuleHandlers.size() != 0){
            ruleHandlerLength = businessRuleHandlers.size();
            parentNode.add(businessRuleHandlerNode);
        }
        
        for(int k=0; k<ruleHandlerLength; k++) {
            WorkflowBusinessRuleHandlerType wbrh = pm.getWorkflowBusinessRuleHandlers().get(pm.getIdIndex(businessRuleHandlers.get(k)));
            WorkflowTreeItem wti = new WorkflowTreeItem(wbrh.getId(), wbrh.getName());
            DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(wti);
            businessRuleHandlerNode.add(newNode);
        }
    }
    
}
