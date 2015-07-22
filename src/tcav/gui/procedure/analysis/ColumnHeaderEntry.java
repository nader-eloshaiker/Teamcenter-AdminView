/*
 * ColumnHeaderEntry.java
 *
 * Created on 9 May 2008, 08:29
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tcav.gui.procedure.analysis;

import tcav.manager.procedure.plmxmlpdm.type.WorkflowHandlerType;
import tcav.manager.procedure.plmxmlpdm.type.UserDataType;
import java.util.ArrayList;

/**
 *
 * @author nzr4dl
 */
public class ColumnHeaderEntry {
    private String handler;
    private ArrayList<String> arguments;
    public static final String ARGUMENT_PREFIX = "  ";
    
    /**
     * Creates a new instance of ColumnHeaderEntry
     */
    public ColumnHeaderEntry(String handler) {
        this.handler = handler;
        arguments = new ArrayList<String>();
    }
    
    public ColumnHeaderEntry(WorkflowHandlerType wh) {
        this.handler = wh.getName();
        arguments = new ArrayList<String>();
    }
    
    public ColumnHeaderEntry(WorkflowHandlerType wh, UserDataType ud) {
        this.handler = wh.getName();
        arguments = new ArrayList<String>();
        for(int i=0; i<ud.getUserValue().size(); i++)
            arguments.add(ud.getUserValue().get(i).getValue());
    }
    
    public boolean isArgumentsEmpty() {
        return (arguments.size() == 0);
    }
    
    public boolean equals(WorkflowHandlerType wh, UserDataType ud) {
        if(!handler.equals(wh.getName()))
            return false;
        
        if(ud != null) {
            if(arguments.size() != ud.getUserValue().size())
                return false;
            
            for(int i=0; i<ud.getUserValue().size(); i++)
                if(arguments.indexOf(ud.getUserValue().get(i).getValue()) == -1)
                    return false;
            
            return true;
            
        } else {
            if(arguments.size() != 0)
                return false;
            
            return true;
            
        }
    }
    
    public boolean equals(WorkflowHandlerType wh) {
        return handler.equals(wh.getName());
    }
    
    public String toString() {
        if(arguments.size() == 0)
            return handler;
        
        String argument = "";
        String tmp;
        String pad;
        int index;
        
        for(int i=0; i<arguments.size(); i++){
            tmp = arguments.get(i);
            index = tmp.indexOf("=");
            
            pad = "";
            for(int k=0; k<index; k++)
                pad = pad+" ";
            
            tmp = tmp.replaceAll(";","\n" + ARGUMENT_PREFIX + pad);
            tmp = tmp.replaceAll(",","\n" + ARGUMENT_PREFIX + pad);
            
            argument += ARGUMENT_PREFIX + tmp +"\n";
        }
        
        return argument;
    }
    
}
