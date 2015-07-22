/*
 * ColumnHeaderEntry.java
 *
 * Created on 9 May 2008, 08:29
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tcav.gui.procedure.analysis;

/**
 *
 * @author nzr4dl
 */
public class ColumnHeaderEntry {
    public String NAME;
    public String VALUE;
    public static final String ARGUEMENT = "  ";
    
    /**
     * Creates a new instance of ColumnHeaderEntry
     */
    public ColumnHeaderEntry(String name) {
        this(name, null);
    }
    
    public ColumnHeaderEntry(String name, String value) {
        this.NAME = name;
        this.VALUE = value;
    }
    
    public String toString() {
        if(VALUE == null)
            return NAME;
        
        String entry = VALUE.replaceAll(";","\n"+ARGUEMENT);
        entry = entry.replaceAll(",","\n"+ARGUEMENT);
        
        return ARGUEMENT + entry;
    }
    
}
