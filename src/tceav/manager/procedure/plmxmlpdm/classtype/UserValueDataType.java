//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.0.3-b24-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2007.07.22 at 11:41:15 AM EST 
//


package tceav.manager.procedure.plmxmlpdm.classtype;
/*
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
*/

/**
 * <p>Java class for UserValueDataType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="UserValueDataType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}NMTOKEN">
 *     &lt;enumeration value="int"/>
 *     &lt;enumeration value="ints"/>
 *     &lt;enumeration value="real"/>
 *     &lt;enumeration value="reals"/>
 *     &lt;enumeration value="Boolean"/>
 *     &lt;enumeration value="booleans"/>
 *     &lt;enumeration value="string"/>
 *     &lt;enumeration value="reference"/>
 *     &lt;enumeration value="enum"/>
 *     &lt;enumeration value="list"/>
 *     &lt;enumeration value="dateTime"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 */
//@XmlEnum
public enum UserValueDataType {

    //@XmlEnumValue("int")
    INT("int"),
    //@XmlEnumValue("ints")
    INTS("ints"),
    //@XmlEnumValue("real")
    REAL("real"),
    //@XmlEnumValue("reals")
    REALS("reals"),
    //@XmlEnumValue("Boolean")
    BOOLEAN("boolean"),
    //@XmlEnumValue("booleans")
    BOOLEANS("booleans"),
    //@XmlEnumValue("string")
    STRING("string"),
    //@XmlEnumValue("reference")
    REFERENCE("reference"),
    //@XmlEnumValue("enum")
    ENUM("enum"),
    //@XmlEnumValue("list")
    LIST("list"),
    //@XmlEnumValue("dateTime")
    DATE_TIME("dateTime");
    private final String value;

    UserValueDataType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static UserValueDataType fromValue(String v) {
        for (UserValueDataType c: UserValueDataType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v.toString());
    }

}
