/*
 * WorkflowTemplateList.java
 *
 * Created on 30 July 2007, 14:38
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tcadminview.procedure;

import java.util.*;
import tcadminview.plmxmlpdm.type.WorkflowTemplateType;
import tcadminview.plmxmlpdm.classtype.WorkflowTemplateClassificationEnum;

/**
 *
 * @author nzr4dl
 */
public class WorkflowTemplateList extends Vector<WorkflowTemplateType>{
    
    Hashtable<WorkflowTemplateClassificationEnum, Vector<Integer>> classifications;
    
    /** Creates a new instance of WorkflowTemplateList */
    public WorkflowTemplateList() {
        super();
        classifications = new Hashtable<WorkflowTemplateClassificationEnum, Vector<Integer>>();
    }
    
    public Vector<Integer> getIndexesForClassification(WorkflowTemplateClassificationEnum classification) {
        return classifications.get(classification);
    }
    
    public boolean add(WorkflowTemplateType element){
        
        if(!classifications.containsKey(element.getTemplateClassification()))
            classifications.put(element.getTemplateClassification(), new Vector<Integer>());
        
        classifications.get(element.getTemplateClassification()).add(super.size());
        
        return super.add(element);
    }
    
    public void add(int index, WorkflowTemplateType element){
        
        if(!classifications.containsKey(element.getTemplateClassification()))
            classifications.put(element.getTemplateClassification(), new Vector<Integer>());
        
        classifications.get(element.getTemplateClassification()).add(index);
        
        super.add(index, element);
    }
    
    public void addElement(WorkflowTemplateType element) {
        add(element);
    }

    public void insertElementAt(WorkflowTemplateType element, int index) {
        add(index, element);
    }
}
