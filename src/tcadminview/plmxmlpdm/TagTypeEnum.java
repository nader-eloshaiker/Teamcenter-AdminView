/*
 * TagTypeEnum.java
 *
 * Created on 24 July 2007, 14:08
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tcadminview.plmxmlpdm;

/**
 *
 * @author NZR4DL
 */
public enum TagTypeEnum {
    Document("#document"),
    Text("#text"),
    
    PLMXML("PLMXML"),
    Header("Header"),
    
    AccessIntent("AccessIntent"),
    ApplicationRef("ApplicationRef"),
    Arguments("Arguments"),
    AssociatedDataSet("AssociatedDataSet"),
    AssociatedFolder("AssociatedFolder"),
    AssociatedForm("AssociatedForm"),
    Checker("Checker"),
    DependencyTaskActions("DependencyTaskActions"),
    DependencyTaskTemplates("DependencyTaskTemplates"),
    Description("Description"),
    Item("Item"),
    Organisation("Organisation"),
    Role("Role"),
    Site("Site"),
    TaskDescription("TaskDescription"),
    UserData("UserData"),
    UserValue("UserValue"),
    ValidationResults("ValidationResults"),
    WorkflowAction("WorkflowAction"),
    WorkflowBusinessRule("WorkflowBusinessRule"),
    WorkflowBusinessRuleHandler("WorkflowBusinessRuleHandler"),
    WorkflowHandler("WorkflowHandler"),
    WorkflowSignoffProfile("WorkflowSignoffProfile"),
    WorkflowTemplate("WorkflowTemplate"),
    UNDEFINED("Undefined");

    private final String value;

    TagTypeEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }
    
    public static TagTypeEnum fromValue(String v) {
        for (TagTypeEnum c: TagTypeEnum.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }

        return UNDEFINED;
        //throw new IllegalArgumentException(v.toString());
    }

}
