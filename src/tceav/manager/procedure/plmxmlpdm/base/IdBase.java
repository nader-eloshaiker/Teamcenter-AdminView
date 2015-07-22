
package tceav.manager.procedure.plmxmlpdm.base;

import tceav.xml.TagTools;
import tceav.manager.procedure.plmxmlpdm.ProcedureTagTypeEnum;
import org.w3c.dom.Node;
import org.w3c.dom.NamedNodeMap;

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

    public IdBase() {
    }

    public String getId() {
        return id;
    }

    public void setId(String value) {
        this.id = value;
    }
    
}
