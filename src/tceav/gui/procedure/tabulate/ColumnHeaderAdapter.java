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
import tceav.manager.procedure.plmxmlpdm.type.UserDataType;
import tceav.Settings;
import java.util.ArrayList;

/**
 *
 * @author nzr4dl
 */
public abstract class ColumnHeaderAdapter implements ColumnHeaderModel {

    private ArrayList<Handler> handlers = new ArrayList<Handler>();;
    public static final String ARGUMENT_PREFIX = "   ";
    public static final String ARGUMENT_PAD = "   ";

    public int compare(ColumnHeaderAdapter c) {
        if (isRuleHandler() && c.isActionHandler()) {
            return 1;
        } else if (isActionHandler() && c.isRuleHandler()) {
            return -1;
        }
        
        int result = toStringCompare().compareToIgnoreCase(c.toStringCompare());
        
        if (result > 0) {
            return 1;
        } else if (result < 0) {
            return -1;
        } else {
            return 0;
        }
    }

    public String getRule() {
        return null;
    }

    public Handler getHandler(int index) {
        return handlers.get(index);
    }
    
    public int getHandlerSize() {
        return handlers.size();
    }
    
    public boolean addHandler(WorkflowBusinessRuleHandlerType wbrh) {
        return handlers.add(new Handler(wbrh.getName()));
    }

    public boolean addHandler(WorkflowHandlerType wh) {
        return handlers.add(new Handler(wh.getName()));
    }

    public String toStringCSV() {
        String s;
        String str = toString();
        s = str.replaceAll("\"", "\"\"");
        s = "\"" + s + "\"";

        return s;
    }

    public class Handler {

        private String handler;
        private ArrayList<Argument> arguments;

        public Handler(String handler) {
            this.handler = handler;
            arguments = new ArrayList<Argument>();
        }

        public String getName() {
            return handler;
        }

        public boolean addArgument(String str) {
            return arguments.add(new Argument(str));
        }

        public Argument getArgument(int index) {
            return arguments.get(index);
        }

        public int getArgumentSize() {
            return arguments.size();
        }
        
        public boolean hasArgument(UserDataType ud) {

            if (getArgumentSize() != ud.getUserValue().size()) {
                return false;
            }
            for (int i = 0; i < ud.getUserValue().size(); i++) {
                if (!hasArgument(ud.getUserValue().get(i).getValue())) {
                    return false;
                }
            }

            return true;
        }
        
        public boolean hasArgument(String elem) {
            return (indexOfArgument(elem) > -1);
        }

        public int indexOfArgument(String elem) {
            if (elem == null) {
                for (int i = 0; i < getArgumentSize(); i++) {
                    if (getArgument(i) == null) {
                        return i;
                    }
                }
            } else {
                for (int i = 0; i < getArgumentSize(); i++) {
                    if (getArgument(i).equals(elem)) {
                        return i;
                    }
                }
            }
            return -1;
        }

        public boolean hasArguments() {
            return (arguments.size() != 0);
        }

        public String getAllArguments() {
            String str = "";
            for (int i = 0; i < getArgumentSize(); i++) {
                str += getArgument(i).getArgument();
            }
            return str;
        }
        
        @Override
        public String toString() {
            if (arguments.size() > 0) {
                return getName() + "\n" + toStringArguments();
            } else {
                return getName();
            }
        }

        public String toStringArguments() {
            String indent = ARGUMENT_PREFIX;
            String pad = ARGUMENT_PAD + indent;
            String s = "";

            for (int i = 0; i < getArgumentSize(); i++) {
                if ((getArgument(i).getValues().length == 0) || (getArgument(i).getValues().length == 1)) {
                    s += indent + getArgument(i).getArgument();
                } else {
                    s += indent + getArgument(i).getField() + "=\n" + pad + getArgument(i).getValues()[0];
                    for (int j = 1; j < getArgument(i).getValues().length; j++) {
                        s += "\n" + pad + getArgument(i).getValues()[j];
                    }
                }
                if (i < getArgumentSize() - 1) {
                    s += "\n";
                }
            }

            return s;
        }
    }

    public class Argument {

        private String field;
        private String[] values = new String[0];
        private String argumentStr;
        private String[] delimeters = new String[]{";", ","};

        public Argument(String argumentStr) {
            this.argumentStr = argumentStr;

            if (argumentStr.indexOf('=') > -1) {
                field = argumentStr.substring(0, argumentStr.indexOf('='));
                String s = argumentStr.substring(argumentStr.indexOf('=') + 1);
                values = new String[]{s};

                for (int i = 0; i < delimeters.length; i++) {
                    if (s.indexOf(delimeters[i]) > -1) {
                        values = s.split(delimeters[i]);
                        break;
                    }
                }

            } else {
                field = argumentStr;
            }
        }

        public String getField() {
            return field;
        }

        public String[] getValues() {
            return values;
        }

        public String getArgument() {
            return argumentStr;
        }

        @Override
        public String toString() {
            if ((values.length == 0) || (values.length == 1)) {
                return argumentStr;
            }
            String pad = "   ";
            String s = field + "=\n" + pad + values[0];

            for (int i = 1; i < values.length; i++) {
                s += "\n" + pad + values[i];
            }
            return s;

        }

        public boolean equals(String argumentCmp) {
            if (Settings.isPmTblStrictArgument()) {
                return (argumentStr.equals(argumentCmp));
            }
            
            String fieldCmp;
            String[] valuesCmp = new String[0];

            if (argumentCmp.indexOf('=') > -1) {
                fieldCmp = argumentCmp.substring(0, argumentCmp.indexOf('='));
                String s = argumentCmp.substring(argumentCmp.indexOf('=') + 1);
                valuesCmp = new String[]{s};

                for (int i = 0; i < delimeters.length; i++) {
                    if (s.indexOf(delimeters[i]) > -1) {
                        valuesCmp = s.split(delimeters[i]);
                        break;
                    }
                }

            } else {
                fieldCmp = argumentCmp;
            }

            if (field.compareToIgnoreCase(fieldCmp) != 0) {
                return false;
            }
            if ((values.length == 0) && (valuesCmp.length == 0)) {
                return true;
            }
            if (values.length != valuesCmp.length) {
                return false;
            }
            for (int i = 0; i < values.length; i++) {
                if (indexOfArray(values[i], valuesCmp) == -1) {
                    return false;
                }
            }
            return true;
        }

        private int indexOfArray(String s, String[] array) {
            for (int i = 0; i < array.length; i++) {
                if (s.compareToIgnoreCase(array[i]) == 0) {
                    return i;
                }
            }
            return -1;
        }
    }
}
