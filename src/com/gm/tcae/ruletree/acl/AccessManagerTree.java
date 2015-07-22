/*
 * AccessManagerTree.java
 *
 * Created on 26 June 2007, 16:12
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
public class AccessManagerTree extends Vector<AccessManagerItem> {
    
    /** Creates a new instance of AccessManagerTree */
    public AccessManagerTree() {
        super();
    }
    
    public AccessManagerItem elementAt(int index) {
        return (AccessManagerItem)super.elementAt(index);
    }

}
