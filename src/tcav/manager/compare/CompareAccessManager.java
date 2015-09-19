/*
 * CompareAccessManager.java
 *
 * Created on 3 March 2008, 13:51
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tcav.manager.compare;

import tcav.manager.ManagerAdapter;
import java.io.File;
import java.util.ArrayList;
import tcav.manager.access.AccessManager;
import tcav.manager.access.NamedAcl;
import tcav.manager.access.NamedAclList;
import tcav.manager.access.RuleTreeNode;

/**
 *
 * @author nzr4dl
 */
public class CompareAccessManager extends ManagerAdapter {
    public static final int TREE_TYPE = 0;
    public static final int ACL_TYPE = 1;
    
    private final AccessManager[] amList;
    private final CompareResult[] compareRuletreeResult;
    private final CompareResult[] compareNamedACLResult;
    
    public CompareAccessManager(AccessManager[] amList) {
        this.amList = amList;
        compareRuletreeResult = new CompareResult[]{new CompareResult(),new CompareResult()};
        compareNamedACLResult = new CompareResult[]{new CompareResult(),new CompareResult()};
        
        compare(amList[0].getAccessRuleList(), amList[1].getAccessRuleList(), compareNamedACLResult[0]);
        compare(amList[1].getAccessRuleList(), amList[0].getAccessRuleList(), compareNamedACLResult[1]);
        
        compare(amList[0].getAccessTreeList(), amList[1].getAccessTreeList());
        compare(amList[1].getAccessTreeList(), amList[0].getAccessTreeList(), compareRuletreeResult[1]);
        compare(amList[0].getAccessTreeList(), amList[1].getAccessTreeList(), compareRuletreeResult[0]);
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
    
    @Override
    public File getFile() {
        return amList[0].getFile();//+" <> "+amList[1].getFile();
    }
    
    @Override
    public void readFile(File file) throws Exception {
        
    }
    
    @Override
    public boolean isValid() {
        /*
        if (amList[0].isValid() && amList[1].isValid())
            return (amList[0].getAccessRuleList().getAccessControlColumns().size() == amList[1].getAccessRuleList().getAccessControlColumns().size());
        else
            return false;
         */
        return true;
    }
    
    @Override
    public String getManagerType() {
        return ACCESS_COMPARE_MANAGER_TYPE;
    }

    private void compare(ArrayList<RuleTreeNode> am1, ArrayList<RuleTreeNode> am2) {
        compare(am1, am2, null);
    }
    
    private void compare(ArrayList<RuleTreeNode> am1, ArrayList<RuleTreeNode> am2, CompareResult results) {
        int result = CompareInterface.NOT_FOUND;
     
        for (RuleTreeNode node1 : am1) {
            for (RuleTreeNode node2 : am2) {
                result = node1.compare(node2);
                if(result != CompareInterface.NOT_FOUND) {
                    break;
                }
            }
            node1.setComparison(result);
            if(results != null)
                results.increment(result);
        }
    }

    private void compare(NamedAclList am1, NamedAclList am2) {
        compare(am1, am2, null);
    }
    
    private void compare(NamedAclList am1, NamedAclList am2, CompareResult results) {
        int result = CompareInterface.NOT_FOUND;
        
        for (NamedAcl node1 : am1) {
            for (NamedAcl node2 : am2) {
                result = node1.compare(node2);
                if(result != CompareInterface.NOT_FOUND)
                    break;
            }
            node1.setComparison(result);
            if(results != null)
                results.increment(result);
        }
    }
    
}