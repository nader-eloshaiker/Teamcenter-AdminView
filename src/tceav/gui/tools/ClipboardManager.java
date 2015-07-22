/*
 * ClipboardManager.java
 *
 * Created on 7 November 2008, 14:45
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tceav.gui.tools;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;

/**
 *
 * @author nzr4dl-e
 */
public class ClipboardManager implements ClipboardOwner {
    
    /** Creates a new instance of ClipboardManager */
    public ClipboardManager() {
        
    }
    
    public void copyToClipboard(String s) {
        SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            try {
                sm.checkSystemClipboardAccess();
            } catch (Exception ex) {ex.printStackTrace();}
        }
        StringSelection st = new StringSelection(s);
        Clipboard cp = Toolkit.getDefaultToolkit().getSystemClipboard();
        cp.setContents(st, this);
    }
    
    public void lostOwnership(Clipboard clip, Transferable tr) { 
       System.out.println("Lost Clipboard Ownership?!?");
    }
    
}
