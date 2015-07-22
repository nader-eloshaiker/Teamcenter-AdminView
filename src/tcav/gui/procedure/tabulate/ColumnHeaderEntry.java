/*
 * ColumnHeaderEntry.java
 *
 * Created on 9 May 2008, 08:29
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tcav.gui.procedure.tabulate;

import tcav.manager.procedure.plmxmlpdm.type.WorkflowHandlerType;
import tcav.manager.procedure.plmxmlpdm.type.UserDataType;
import tcav.Settings;
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
            
            for(int i=0; i<arguments.size(); i++) {
                if(!Settings.isPmTblStrictArgument()) {
                    if(!matchArgument(ud.getUserValue().get(i).getValue()))
                        return false;
                } else {
                    if(arguments.indexOf(ud.getUserValue().get(i).getValue()) == -1)
                        return false;
                }
            }
            
            return true;
            
        } else {
            if(arguments.size() != 0)
                return false;
            
            return true;
            
        }
    }
    
    private boolean matchArgument(String str) {
        
        if(arguments.indexOf(str) > -1)
            return true;
        
        if(str.indexOf('=') == -1)
            return false;
        
        String[] value;
        String name;
        
        value = str.split("=");
        name = value[0];
        
        value = splitDelimiter(value[1]);
        if(value == null)
            return false;
        
        String[] argValue;
        boolean found = false;
        
        for(int i=0; i<arguments.size(); i++) {
            if(arguments.get(i).indexOf('=') == -1) {
                found = false;
                continue;
            }
            
            argValue = arguments.get(i).split("=");
            
            if(!name.equals(argValue[0])) {
                found = false;
                continue;
            }
            
            argValue = splitDelimiter(argValue[1]);
            if(argValue == null) {
                found = false;
                continue;
            }
            
            if(value.length != argValue.length) {
                found = false;
                continue;
            }
            
            found = true;
            
            for(int j=0; j<value.length; j++) {
                if(indexOf(value[j], argValue) == -1)  {
                    found = false;
                    break;
                }
            }
            
            if(found)
                break;
        }
        
        return found;
        
    }
    
    private String[] splitDelimiter(String s) {
        if(s.indexOf(';') > -1)
            return s.split(";");
        else if(s.indexOf(',') > -1)
            return s.split(",");
        
        return null;
    }
    
    private int indexOf(String s, String[] array) {
        for(int i=0; i<array.length; i++)
            if(s.equals(array[i]))
                return i;
        
        return -1;
    }
    
    public boolean equals(WorkflowHandlerType wh) {
        return handler.equals(wh.getName());
    }
    
    public String exportString() {
        if(arguments.size() == 0)
            return "\"" + handler + "\"";
        
        String argument = "";
        String tmp;
        String pad;
        int index;
        
        for(int i=0; i<arguments.size(); i++){
            tmp = arguments.get(i);
            index = tmp.indexOf("=")+1;
            
            pad = "";
            for(int k=0; k<index; k++)
                pad = pad+" ";
            
            tmp = tmp.replaceAll(";","\n" + ARGUMENT_PREFIX + pad);
            tmp = tmp.replaceAll(",","\n" + ARGUMENT_PREFIX + pad);
            
            argument += ARGUMENT_PREFIX + tmp;
            
            if(!argument.endsWith("\n"))
                argument += "\n";
        }
        
        argument = argument.replaceAll("\"", "\"\"");
        return "\"" + argument + "\"";
    }
    
    public String toString() {
        if(arguments.size() == 0)
            return handler;
        
        String argument = "";
        String tmp;
        String[] tmpArr;
        ArrayList<String> tmpList;
        String pad;
        int stringLimit = 50;
        int division;
        int index;
        
        for(int i=0; i<arguments.size(); i++){
            tmp = arguments.get(i);
            index = tmp.indexOf("=");
            
            pad = "";
            for(int k=0; k<index; k++)
                pad = pad+" ";
            
            tmp = tmp.replaceAll(";","\n" + ARGUMENT_PREFIX + pad);
            tmp = tmp.replaceAll(",","\n" + ARGUMENT_PREFIX + pad);
            
            tmpArr = tmp.split("\n");
            tmpList = new ArrayList<String>();
            
            for(int j=0; j<tmpArr.length; j++) {
                if(tmpArr[j].length() > stringLimit) {
                    division = tmpArr[j].length() / stringLimit;
                    for(int k=0; k<=division; k++) {
                        if(k == division)
                            tmpList.add(ARGUMENT_PREFIX + pad + tmpArr[j].substring(stringLimit*k));
                        else if (k == 0)
                            tmpList.add(tmpArr[j].substring(stringLimit*k,stringLimit*(k+1))+"...");
                        else
                            tmpList.add(ARGUMENT_PREFIX + pad + tmpArr[j].substring(stringLimit*k,stringLimit*(k+1))+"...");
                    }
                } else {
                    tmpList.add(tmpArr[j]);
                }
            }
            tmp = "";
            for(int j=0; j<tmpList.size(); j++) {
                if(!tmpList.get(j).endsWith("\n"))
                    tmp += tmpList.get(j) + "\n";
                else
                    tmp += tmpList.get(j);
                
            }
            argument += ARGUMENT_PREFIX + tmp;
        }
        
        return argument;
    }
    
}
