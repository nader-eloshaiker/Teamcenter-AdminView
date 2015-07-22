/*
 * AccessRuleList.java
 *
 * Created on 20 June 2007, 10:22
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.gm.tcae.ruletree.acl;
import java.util.Vector;

/**
 *
 * @author NZR4DL
 */
public class AccessRuleList extends Vector<AccessRule>{
    
    private int ruleTreeSize = 0;
    private int workflowSize = 0;
    
    /** Creates a new instance of AccessRuleList */
    public AccessRuleList() {
        super();
    }
    
    
    
    public boolean contains(String s) {
	return indexOf(s, 0) >= 0;
    }

    public synchronized int indexOf(String str) {
        return indexOf(str,0);
    }
    
    public synchronized int indexOf(String str, int index) {
	if (str == null) {
	    for (int i = index ; i < elementCount ; i++)
		if (elementData[i]==null)
		    return i;
	} else {
	    for (int i = index ; i < elementCount ; i++)
		if (str.equals( ((AccessRule)elementData[i]).getRuleName() ))
		    return i;
	}
	return -1;
    }

    public AccessRule elementAt(int index) {
        return (AccessRule)super.elementAt(index);
    }
    
    public void addElement(AccessRule ar){
        super.addElement(ar);
        if(ar.getRuleType().equals("WORKFLOW"))
            workflowSize++;
        else if(ar.getRuleType().equals("RULETREE"))
            ruleTreeSize++;
    }
    
    public int getTreeTypeSize() {
        return ruleTreeSize;
    }
    
    public int getWorkFlowTypeSize() {
        return workflowSize;
    }
    
}
