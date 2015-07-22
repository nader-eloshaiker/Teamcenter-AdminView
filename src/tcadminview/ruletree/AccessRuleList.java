/*
 * AccessRuleList.java
 *
 * Created on 20 June 2007, 10:22
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tcadminview.ruletree;
import java.util.Vector;
import java.util.ArrayList;

/**
 *
 * @author NZR4DL
 */
public class AccessRuleList extends Vector<AccessRule>{
    
    private int acRuleTreeSize = 0;
    private int acWorkFlowSize = 0;
    private ArrayList<String> acTypes;
    
    /** Creates a new instance of AccessRuleList */
    public AccessRuleList() {
        super();
         acTypes = new ArrayList<String>();
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
        String s = ar.getRuleType();
        
        if(acTypes.indexOf(s) == -1)
            acTypes.add(s);
        
        if(ar.getRuleType().equals("WORKFLOW"))
            acWorkFlowSize++;
        else if(ar.getRuleType().equals("RULETREE"))
            acRuleTreeSize++;
    }
    
    public int getTreeTypeSize() {
        return acRuleTreeSize;
    }
    
    public int getWorkFlowTypeSize() {
        return acWorkFlowSize;
    }
    
    public String getType(int index) {
        return acTypes.get(index);
    }
    
    public int getTypeSize() {
        return acTypes.size();
    }
    
}
