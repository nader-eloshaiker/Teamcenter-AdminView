/*
 * RuleTreeNode.java
 *
 * Created on 19 June 2007, 11:47
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tcav.manager.access;

import java.util.*;
import tcav.manager.compare.CompareInterface;

/**
 *
 * @author NZR4DL
 */
public class RuleTreeNode extends Object implements CompareInterface {
    
    private String condition;
    private String value;
    private String accessRuleName;
    private AccessRule accessRule;
    private int indentLevel = 0;
    private String[] parameters;
    
    private ArrayList<RuleTreeNode> children;
    private RuleTreeNode parent;
    
    /**
     * Creates a new instance of RuleTreeNode
     */
    public RuleTreeNode() {
        this(null);
    }
    
    public RuleTreeNode(String s) {
        children = new ArrayList<RuleTreeNode>();
        if(s == null)
            return;
        
        String[] s1 = s.split("    ");
        String[] s2 = s1[s1.length-1].split("->");
        
        indentLevel = s1.length-1;
        
        condition = s2[0].substring(0,s2[0].indexOf("("));
        value = s2[0].substring(s2[0].indexOf("(")+2,s2[0].indexOf(")")-1);
        
        if(s2.length > 1){
            if ( s2[1].indexOf(',') > 0 ) {
                String[] s3 = s2[1].split(" , ");
                parameters = new String[s3.length-1];
                for(int i=1; i<s3.length; i++)
                    parameters[i-1] = s3[i];
                accessRuleName = s3[0];
            } else
                accessRuleName = s2[1];
        }
    }
    
    public boolean isValid() {
        if(getCondition() == null)
            return false;
        else
            return true;
    }
    
    public String[] getParamters() {
        return parameters;
    }
    
    public void setAccessRule(AccessRule accessRule) {
        this.accessRule = accessRule;
        accessRule.getRuleTreeReferences().add(this);
    }
    
    public AccessRule getAccessRule() {
        return accessRule;
    }
    
    public String getCondition(){
        return condition;
    }
    
    public String getValue() {
        return value;
    }
    
    public String getAccessRuleName() {
        return accessRuleName;
    }
    
    public int getIndentLevel() {
        return indentLevel;
    }
    
    public String toString() {
        String s = condition+"( "+value+" )";
        
        if (accessRuleName != null)
            s += " -> "+accessRuleName;
        
        return s;
    }
    
    public String generateExportString() {
        String s = "";
        
        for(int l=0; l<indentLevel; l++)
            s += "    ";
        
        s += toString();
        
        if (parameters != null)
            for(int k=0; k<parameters.length; k++)
                s += " , "+parameters[k];
        
        return s;
    }
    
    
    public RuleTreeNode getChild(int index) {
        return children.get(index);
    }
    
    public int getChildCount() {
        return children.size();
    }
    
    public void addChild(RuleTreeNode node) {
        if(node == null)
            return;
        
        children.add(node);
        node.setParent(this);
    }
    
    public RuleTreeNode getParent() {
        return parent;
    }
    
    private void setParent(RuleTreeNode parent) {
        this.parent = parent;
    }
    
    public RuleTreeNode getRoot() {
        if(isRoot())
            return this;
        
        RuleTreeNode root = parent;
        while(root != null)
            root = root.getParent();
        
        return root;
    }
    
    public ArrayList<RuleTreeNode> getPath() {
        return getPath(false);
    }
    
    public ArrayList<RuleTreeNode> getPath(boolean reverseOrder) {
        ArrayList<RuleTreeNode> path = new ArrayList<RuleTreeNode>();
        RuleTreeNode root = parent;
        
        while(root != null) {
            path.add(root);
            root = root.getParent();
        }
        
        if(!reverseOrder) {
            ArrayList<RuleTreeNode> pathReversed = new ArrayList<RuleTreeNode>();
            
            for(int i=path.size()-1; i>=0; i--)
                pathReversed.add(path.get(i));
            
            path = pathReversed;
        }
        
        return path;
    }
    
    public boolean isLeaf() {
        return (children.size() == 0);
    }
    
    public boolean isRoot() {
        return (parent == null);
    }
    
    
    /********************
     * Comapare interface
     ********************/
    
    private int compare_result = CompareInterface.EQUAL;
    
    public int getComparison() {
        return compare_result;
    }
    
    public void setComparison(int compare_result) {
        this.compare_result = compare_result;
    }
    
    public int compare(Object o) {
        RuleTreeNode node = (RuleTreeNode)o;
        
        if(equalString(getCondition(), node.getCondition()))
            if(equalString(getValue(), node.getValue())) {
            
                if(getAccessRuleName() == null && node.getAccessRuleName() == null) {
                    if(equalPaths(getPath(), node.getPath()))
                        return CompareInterface.EQUAL;
                } else if(getAccessRuleName() != null && node.getAccessRuleName() != null) {
                    if(getAccessRuleName().equals(node.getAccessRuleName()))
                        if(equalPaths(getPath(), node.getPath()))
                            return getAccessRule().getComparison();
                }
            }
        
        return CompareInterface.NOT_FOUND;
    }
    
    private boolean equalString(String s1, String s2) {
        if (s1 == null && s2 == null)
            return true;
        else if(s1 == null || s2 == null)
            return false;
        else
            return s1.equals(s2);
    }
    
    private boolean equalPaths(ArrayList<RuleTreeNode> path1, ArrayList<RuleTreeNode> path2){
        if (path1.size() != path2.size())
            return false;
        
        for(int i=0; i<path1.size(); i++)
            if(!path1.get(i).getCondition().equals(path2.get(i).getCondition()) || !path1.get(i).getValue().equals(path2.get(i).getValue()))
                return false;
        
        return true;
    }
    
    
    /*
    public Enumeration<RuleTreeNode> nodes() {
        return new Enumerator<RuleTreeNode>(this);
    }
    
    
    class Entry {
        private RuleTreeNode node;
        private Entry[] children;
        private int childIndex;
        
        Entry(RuleTreeNode node) {
            this.node = node;
            childIndex = 0;
            
            children = new Entry[node.getChildCount()];
            for(int i=0; i<node.getChildCount(); i++)
                children[i] = new Entry(node.getChild(i));
        }
        
        public Entry getChild() {
            if(childIndex < children.length)
                return children[childIndex];
            else
                return null;
        }
        
        public Entry getNextChild() {
            childIndex++;
            return getChild();
        }
        
        public boolean hasMoreChildren() {
            if(childIndex+1 < children.length)
                return true;
            else
                return false;
        }
        
        public RuleTreeNode getNode() {
            return node;
        }
    }
    
    private class Enumerator<T> implements Enumeration<T> {
        private Stack<Entry> que;
        private boolean initial = true;
        
        Enumerator(RuleTreeNode node) {
            Entry entry = new Entry(node);
            que = new Stack<Entry>();
            que.push(entry);
        }
        
        public boolean hasMoreElements() {
            if(initial) {
                if(que.size() > 0)
                    return true;
                else
                    return false;
            }
            int index = que.search(que.peek());
            for(int i=index; i>=0; i--) {
                if(que.get(i).hasMoreChildren())
                    return true;
            }
            return false;
        }
        
        public T nextElement() {
            Entry entry;
            
            if(initial) {
                initial = false;
                if(que.size() > 0)
                    return (T)que.peek().getNode();
                else
                    throw new NoSuchElementException("RuleTree Enumerator");
            }
            while (!que.empty()) {
                if(que.peek().hasMoreChildren()) {
                    entry = que.peek().getNextChild();
                    que.push(entry);
                    return (T)entry.getNode();
                } else
                    que.pop();
            }
            throw new NoSuchElementException("RuleTree Enumerator");
        }
        
    }
*/    
}
