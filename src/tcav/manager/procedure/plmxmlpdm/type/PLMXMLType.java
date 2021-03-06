//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.0.3-b24-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2007.07.22 at 11:41:15 AM EST 
//


package tcav.manager.procedure.plmxmlpdm.type;

import tcav.xml.TagTools;
import tcav.manager.procedure.plmxmlpdm.base.IdBase;
import org.w3c.dom.Node;
import org.w3c.dom.NamedNodeMap;
/*
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
*/

/**
 * 
 *       This defines the type of the outermost element in a PLMXML file.
 *       
 *       Elements:
 *       
 *       Header:			Document header
 *       AttribOwner:		Any elements derived from AttribOwner are allowed under
 *                         this element, except as described below.
 *       
 * 
 * <p>Java class for PLMXMLType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PLMXMLType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.plmxml.org/Schemas/PLMXMLSchema}DocumentBase">
 *       &lt;choice maxOccurs="unbounded" minOccurs="0">
 *         &lt;element name="Header" type="{http://www.plmxml.org/Schemas/PLMXMLSchema}DocumentHeaderType"/>
 *         &lt;element ref="{http://www.plmxml.org/Schemas/PLMXMLSchema}AttribOwner"/>
 *       &lt;/choice>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * @XmlAccessorType(XmlAccessType.FIELD)
 * @XmlType(name = "PLMXMLType", propOrder = {
 * "headerOrAttribOwner"
 * })
 * 
 */
public final class PLMXMLType extends IdBase
{

    //@XmlAttribute
    protected final String schemaVersionAttribute = "schemaVersion";
    protected String schemaVersion;
    
    //@XmlAttribute
    protected final String dateAttribute = "date";
    protected String date;
    
    //@XmlAttribute
    protected final String timeAttribute = "time";
    protected String time;
    
    //@XmlAttribute
    protected final String authorAttribute = "author";
    protected String author;
    
    public PLMXMLType(Node node) {
        //super(node);
        Node currentNode = node;
        NamedNodeMap attrib = currentNode.getAttributes();
        
        setSchemaVersion(TagTools.getStringValue(attrib, schemaVersionAttribute));
        setDate(TagTools.getStringValue(attrib, dateAttribute));
        setTime(TagTools.getStringValue(attrib, timeAttribute));
        setAuthor(TagTools.getStringValue(attrib, authorAttribute));
    }

    public String getSchemaVersion() {
        return schemaVersion;
    }

    public void setSchemaVersion(String value) {
        this.schemaVersion = value;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String value) {
        this.date = value;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String value) {
        this.time = value;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String value) {
        this.author = value;
    }

    /***************
     * Customisation
     ***************/
}
