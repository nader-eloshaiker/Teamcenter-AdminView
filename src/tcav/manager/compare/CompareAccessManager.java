/*
 * CompareAccessManager.java
 *
 * Created on 3 March 2008, 13:51
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tcav.manager.compare;

import tcav.manager.access.*;
import tcav.manager.AbstractManager;
import tcav.manager.compare.*;
import java.io.File;

/**
 *
 * @author nzr4dl
 */
public class CompareAccessManager extends AbstractManager {
    public static final int TREE_TYPE = 0;
    public static final int ACL_TYPE = 1;
    
    private AccessManager[] amList;
    private CompareResult[] compareRuletreeResult;
    private CompareResult[] compareNamedACLResult;
    
    public CompareAccessManager(AccessManager[] amList) {
        this.amList = amList;
        compareRuletreeResult = new CompareResult[]{new CompareResult(),new CompareResult()};
        compareNamedACLResult = new CompareResult[]{new CompareResult(),new CompareResult()};
        
        compareNamedACL(amList[0], amList[1], compareNamedACLResult[0]);
        compareNamedACL(amList[1], amList[0], compareNamedACLResult[1]);
    }
    
    public AccessManager[] getAccessManagers() {
        return amList;
    }
    
    public CompareResult getCompareResult(int type, int index) {
        if(type == ACL_TYPE) {
            if(index < compareNamedACLResult.length)
                return compareNamedACLResult[index];
        } else if(type == TREE_TYPE) {
            if(index < compareRuletreeResult.length)
                return compareRuletreeResult[index];
        }
        return null;
    }
    
    public String getId(){
        return amList[0].getId()+" <> "+amList[1].getId();
    }
    
    public String getName() {
        return amList[0].getName()+" <> "+amList[1].getName();
    }
    
    public void readFile(File file) throws Exception {
        
    }
    
    public boolean isValid() {
        if (amList[0].isValid() && amList[1].isValid())
            return (amList[0].getAccessControlColumns().size() == amList[1].getAccessControlColumns().size());
        else
            return false;
    }
    
    public String getManagerType() {
        return super.ACCESS_COMPARE_MANAGER_TYPE;
    }
    
    private void compareNamedACL(AccessManager amOne, AccessManager amTwo, CompareResult results) {
        int compareResult = CompareInterface.NOT_FOUND;
        AccessRule ar;
        
        for(int j=0; j<amOne.getAccessRuleList().size(); j++){
            ar = amOne.getAccessRuleList().get(j);
            for(int l=0; l<amTwo.getAccessRuleList().size(); l++) {
                compareResult = ar.compare(amTwo.getAccessRuleList().get(l));
                if(compareResult != CompareInterface.NOT_FOUND)
                    break;
            }
            ar.setComparison(compareResult);
            results.increment(compareResult);
            //System.out.println(amOne.getFile().getName()+" "+compareResult);
        }
    }
    
}