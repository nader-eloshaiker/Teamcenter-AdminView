//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.0.3-b24-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2007.07.22 at 11:41:15 AM EST 
//


package tcav.manager.procedure.plmxmlpdm.UnusedRequiredTags;
/*
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
*/

/**
 * <p>Java class for TaskStateType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="TaskStateType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="unassigned"/>
 *     &lt;enumeration value="pending"/>
 *     &lt;enumeration value="started"/>
 *     &lt;enumeration value="completed"/>
 *     &lt;enumeration value="skipped"/>
 *     &lt;enumeration value="aborted"/>
 *     &lt;enumeration value="suspended"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
//@XmlEnum
public enum TaskStateType {

    //@XmlEnumValue("unassigned")
    UNASSIGNED("unassigned"),
    //@XmlEnumValue("pending")
    PENDING("pending"),
    //@XmlEnumValue("started")
    STARTED("started"),
    //@XmlEnumValue("completed")
    COMPLETED("completed"),
    //@XmlEnumValue("skipped")
    SKIPPED("skipped"),
    //@XmlEnumValue("aborted")
    ABORTED("aborted"),
    //@XmlEnumValue("suspended")
    SUSPENDED("suspended");
    private final String value;

    TaskStateType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static TaskStateType fromValue(String v) {
        for (TaskStateType c: TaskStateType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
