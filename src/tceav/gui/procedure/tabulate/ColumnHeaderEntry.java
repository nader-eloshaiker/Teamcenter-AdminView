/*
 * ColumnHeaderEntry.java
 *
 * Created on 9 May 2008, 08:29
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tceav.gui.procedure.tabulate;

import tceav.manager.procedure.plmxmlpdm.type.WorkflowHandlerType;
import tceav.manager.procedure.plmxmlpdm.type.WorkflowBusinessRuleHandlerType;
import tceav.manager.procedure.plmxmlpdm.type.WorkflowBusinessRuleType;
import tceav.manager.procedure.plmxmlpdm.type.UserDataType;
import tceav.Settings;
import java.util.ArrayList;

/**
 *
 * @author nzr4dl
 */
public class ColumnHeaderEntry {
    private String quorum;
    private String handler;
    private ArrayList<String> arguments;
    
    private int type;
    public static final int WORKFLOW_HANDLER_TYPE = 0;
    public static final int WORKFLOW_HANDLER_ARGUMENT_TYPE = 1;
    public static final int BUSINESS_RULE_TYPE = 2;
    public static final int BUSINESS_RULE_HANDLER_TYPE = 3;
    public static final int BUSINESS_RULE_HANDLER_ARGUMENT_TYPE = 4;
    public static final String ARGUMENT_PREFIX = "   ";
    
    private int classification;
    public static final int WORKFLOW_HANDLER_CLASSIFICATION = 0;
    public static final int BUSINESS_RULE_CLASSIFICATION = 1;
    
    /**
     * Creates a new instance of ColumnHeaderEntry
     */
    public ColumnHeaderEntry(WorkflowHandlerType wh) {
        this.classification = WORKFLOW_HANDLER_CLASSIFICATION;
        this.type = WORKFLOW_HANDLER_TYPE;
        this.handler = wh.getName();
        arguments = new ArrayList<String>();
    }
    
    public ColumnHeaderEntry(WorkflowHandlerType wh, UserDataType ud) {
        this.classification = WORKFLOW_HANDLER_CLASSIFICATION;
        this.type = WORKFLOW_HANDLER_ARGUMENT_TYPE;
        this.handler = wh.getName();
        arguments = new ArrayList<String>();
        for(int i=0; i<ud.getUserValue().size(); i++)
            arguments.add(ud.getUserValue().get(i).getValue());
    }
    
    public ColumnHeaderEntry(WorkflowBusinessRuleType wbr) {
        this.classification = BUSINESS_RULE_CLASSIFICATION;
        this.type = BUSINESS_RULE_TYPE;
        this.quorum = wbr.getRuleQuorum().toString();
        this.handler = "";
        arguments = new ArrayList<String>();
    }
    
    public ColumnHeaderEntry(WorkflowBusinessRuleType wbr, WorkflowBusinessRuleHandlerType wbrh) {
        this.classification = BUSINESS_RULE_CLASSIFICATION;
        this.type = BUSINESS_RULE_HANDLER_TYPE;
        this.quorum = wbr.getRuleQuorum().toString();
        this.handler = wbrh.getName();
        arguments = new ArrayList<String>();
    }
    
    public ColumnHeaderEntry(WorkflowBusinessRuleType wbr, WorkflowBusinessRuleHandlerType wbrh, UserDataType ud) {
        this.classification = BUSINESS_RULE_CLASSIFICATION;
        this.type = BUSINESS_RULE_HANDLER_ARGUMENT_TYPE;
        this.quorum = wbr.getRuleQuorum().toString();
        this.handler = wbrh.getName();
        arguments = new ArrayList<String>();
        for(int i=0; i<ud.getUserValue().size(); i++)
            arguments.add(ud.getUserValue().get(i).getValue());
    }
    
    public boolean isBusinessRule() {
        return (type == BUSINESS_RULE_TYPE);
    }
    
    public boolean isHandler() {
        return (type == BUSINESS_RULE_HANDLER_TYPE || type == WORKFLOW_HANDLER_TYPE);
    }
    
    public boolean isArgument() {
        return (type == BUSINESS_RULE_HANDLER_ARGUMENT_TYPE || type == WORKFLOW_HANDLER_ARGUMENT_TYPE);
    }
    
    public boolean equals(WorkflowBusinessRuleType wbr) {
        if(classification != BUSINESS_RULE_CLASSIFICATION)
            return false;
        
        return (quorum.equals(wbr.getRuleQuorum().toString()));
    }
    
    public boolean equals(WorkflowBusinessRuleType wbr, WorkflowBusinessRuleHandlerType wbrh) {
        if(classification != BUSINESS_RULE_CLASSIFICATION)
            return false;
        
        if(!equals(wbr))
            return false;
        
        return (handler.equals(wbrh.getName()));
    }
    
    public boolean equals(WorkflowBusinessRuleType wbr, WorkflowBusinessRuleHandlerType wbrh, UserDataType ud) {
        if(classification != BUSINESS_RULE_CLASSIFICATION)
            return false;
        
        if(!equals(wbr))
            return false;
        
        if(wbrh == null)
            return isBusinessRule();
        else if(!equals(wbr, wbrh))
            return false;
        
        if(ud == null)
            return isHandler();
        
        return equals(ud);
    }
    
    public boolean equals(WorkflowHandlerType wh) {
        if(classification != WORKFLOW_HANDLER_CLASSIFICATION)
            return false;
        
        return handler.equals(wh.getName());
    }
    
    public boolean equals(WorkflowHandlerType wh, UserDataType ud) {
        if(classification != WORKFLOW_HANDLER_CLASSIFICATION)
            return false;
        
        if(!equals(wh))
            return false;
        
        if(ud == null)
            return isHandler();
        else
            return equals(ud);
    }
    
    private String toExportString;
    
    public String toExportString() {
        if(toExportString == null)
            toExportString = toString(true);
        
        return toExportString;
    }
    
    private String toString;
    
    public String toString() {
        if(toString == null)
            toString = toString(false);
        
        return toString;
    }
    
    private String toString(boolean export) {
        switch(type) {
            case WORKFLOW_HANDLER_TYPE:
            case BUSINESS_RULE_HANDLER_TYPE:
                return toStringHandler(export);
                
            case WORKFLOW_HANDLER_ARGUMENT_TYPE:
            case BUSINESS_RULE_HANDLER_ARGUMENT_TYPE:
                return toStringArgument(export);
                
            case BUSINESS_RULE_TYPE:
                return toStringBusinessRule(export);
                
            default:
                return "";
        }
    }
    
    private String toStringBusinessRule(boolean export)  {
        if(export)
            return "\"Quorum Rule " + quorum + "\"";
        else
            return "Quorum Rule "+quorum;
    }
    
    private String toStringHandler(boolean export) {
        String indent;
        
        if(type == BUSINESS_RULE_HANDLER_TYPE)
            indent = ARGUMENT_PREFIX;
        else
            indent = "";
        
        if(export)
            return "\"" + indent + handler + "\"";
        else
            return indent + handler;
    }
    
    private String toStringArgument(boolean export) {
        String argument = "";
        String tmp;
        String pad;
        String indent;
        
        if(type == BUSINESS_RULE_HANDLER_ARGUMENT_TYPE)
            indent = ARGUMENT_PREFIX + ARGUMENT_PREFIX;
        else
            indent = ARGUMENT_PREFIX;
        
        for(int i=0; i<arguments.size(); i++){
            tmp = arguments.get(i);
            pad = createPadString(tmp.indexOf("=")+1);
            tmp = converDelimiters(tmp, indent, pad);
            if(!export)
                tmp = hyphenateString(tmp, indent, pad);
            
            argument += indent + tmp;
            
            if(export)
                if(!argument.endsWith("\n"))
                    argument += "\n";
        }
        
        if(export) {
            argument = argument.replaceAll("\"", "\"\"");
            return "\"" + argument + "\"";
        } else
            return argument;
    }
    
    private String createPadString(int index) {
        String pad = "";
        for(int k=0; k<index; k++)
            pad = pad+" ";
        return pad;
    }
    
    private String converDelimiters(String s, String indent, String pad) {
        String tmp = s;
        tmp = tmp.replaceAll(";","\n" + indent + pad);
        tmp = tmp.replaceAll(",","\n" + indent + pad);
        
        return tmp;
    }
    
    private String hyphenateString(String s, String indent, String pad) {
        int stringLimit = 50;
        int division;
        String[] tmpArr = s.split("\n");
        ArrayList<String> tmpList = new ArrayList<String>();
        
        for(int j=0; j<tmpArr.length; j++) {
            if(tmpArr[j].length() > stringLimit) {
                division = tmpArr[j].length() / stringLimit;
                for(int k=0; k<=division; k++) {
                    if(k == division)
                        tmpList.add(indent + pad + tmpArr[j].substring(stringLimit*k));
                    else if (k == 0)
                        tmpList.add(tmpArr[j].substring(stringLimit*k,stringLimit*(k+1))+"...");
                    else
                        tmpList.add(indent + pad + tmpArr[j].substring(stringLimit*k,stringLimit*(k+1))+"...");
                }
            } else {
                tmpList.add(tmpArr[j]);
            }
        }
        
        String tmp = "";
        for(int j=0; j<tmpList.size(); j++) {
            if(!tmpList.get(j).endsWith("\n"))
                tmp += tmpList.get(j) + "\n";
            else
                tmp += tmpList.get(j);
            
        }
        
        return tmp;
    }
    
    private String[] splitDelimiter(String s) {
        if(s.indexOf(';') > -1)
            return s.split(";");
        else if(s.indexOf(',') > -1)
            return s.split(",");
        
        return null;
    }
    
    private boolean equals(UserDataType ud) {
        
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
    }
    
    private boolean matchArgument(String str) {
        
        if(arguments.indexOf(str) > -1)
            return true;
        
        if(str.indexOf("=") == -1)
            return false;
        
        String[] value;
        String name;
        
        value = str.split("=");
        if(value.length < 2)
            return false;
        
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
                if(indexOfArray(value[j], argValue) == -1)  {
                    found = false;
                    break;
                }
            }
            
            if(found)
                break;
        }
        
        return found;
        
    }
    
    private int indexOfArray(String s, String[] array) {
        for(int i=0; i<array.length; i++)
            if(s.equals(array[i]))
                return i;
        
        return -1;
    }
    
}
