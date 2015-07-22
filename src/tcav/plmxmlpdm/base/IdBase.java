
package tcav.plmxmlpdm.base;

import tcav.plmxmlpdm.TagTools;
import org.w3c.dom.Node;
import org.w3c.dom.NamedNodeMap;

/*
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
*/

/**
 * <p>Java class for IdBase complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="IdBase">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attGroup ref="{http://www.plmxml.org/Schemas/PLMXMLSchema}idGroup"/>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
//@XmlAccessorType(XmlAccessType.FIELD)
//@XmlType(name = "IdBase")
public abstract class IdBase {

    //@XmlAttribute
    //@XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    //@XmlID
    protected final String idAttribute = "id";
    protected String id;
    
    public IdBase(Node node) {
        
        Node currentNode = node;
        NamedNodeMap attrib = currentNode.getAttributes();
        
        setId(TagTools.getStringValue(attrib, idAttribute));
        
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

}
