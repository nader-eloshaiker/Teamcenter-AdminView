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
public class WorkflowTemplateList extends ArrayList<WorkflowTemplateType>{
    
    private Hashtable<WorkflowTemplateClassificationEnum, ArrayList<Integer>> classifications;
    
    /** Creates a new instance of WorkflowTemplateList */
    public WorkflowTemplateList() {
        super();
        classifications = new Hashtable<WorkflowTemplateClassificationEnum, ArrayList<Integer>>();
    }
    
    public ArrayList<Integer> getIndexesForClassification(WorkflowTemplateClassificationEnum classification) {
        return classifications.get(classification);
    }
    
    public int sizeOfClassifications() {
        return classifications.size();
    }
    
    public boolean add(WorkflowTemplateType element){
        
        if(!classifications.containsKey(element.getTemplateClassification()))
            classifications.put(element.getTemplateClassification(), new ArrayList<Integer>());
        
        classifications.get(element.getTemplateClassification()).add(size());
        
        return super.add(element);
    }
    
    public void add(int index, WorkflowTemplateType element){
        
        if(!classifications.containsKey(element.getTemplateClassification()))
            classifications.put(element.getTemplateClassification(), new ArrayList<Integer>());
        
        classifications.get(element.getTemplateClassification()).add(index);
        
        super.add(index, element);
    }
    
}
