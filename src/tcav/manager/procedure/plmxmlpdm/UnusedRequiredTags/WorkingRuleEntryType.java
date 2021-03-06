//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.0.3-b24-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2007.07.22 at 11:41:15 AM EST 
//


package tcav.manager.procedure.plmxmlpdm.UnusedRequiredTags;

import org.w3c.dom.Node;

/*
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
*/

/**
 * 
 *       Working Rule Entry - i.e. no Release Status, selecting by user or group.
 *       
 *       Attributes:
 *       
 *       userRef:         reference to User
 *       groupRef:        
 *       
 *       isCurrentUser:   if 'true', then select by the current user
 *       isCurrentGroup:  if 'true', then select by the current group.
 *       
 * 
 * <p>Java class for WorkingRuleEntryType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="WorkingRuleEntryType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.plmxml.org/Schemas/PLMXMLSchema}RuleEntryBase">
 *       &lt;attribute name="groupRef" type="{http://www.plmxml.org/Schemas/PLMXMLSchema}anyURIType" />
 *       &lt;attribute name="isCurrentGroup" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="isCurrentUser" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="userRef" type="{http://www.plmxml.org/Schemas/PLMXMLSchema}anyURIType" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
//@XmlAccessorType(XmlAccessType.FIELD)
//@XmlType(name = "WorkingRuleEntryType")
public class WorkingRuleEntryType extends RuleEntryBase {

    //@XmlAttribute
    protected String groupRef;
    //@XmlAttribute
    protected Boolean isCurrentGroup;
    //@XmlAttribute
    protected Boolean isCurrentUser;
    //@XmlAttribute
    protected String userRef;
    
    
    public WorkingRuleEntryType(Node node) {
        super(node);
    }

    /**
     * Gets the value of the groupRef property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGroupRef() {
        return groupRef;
    }

    /**
     * Sets the value of the groupRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGroupRef(String value) {
        this.groupRef = value;
    }

    /**
     * Gets the value of the isCurrentGroup property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsCurrentGroup() {
        return isCurrentGroup;
    }

    /**
     * Sets the value of the isCurrentGroup property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsCurrentGroup(Boolean value) {
        this.isCurrentGroup = value;
    }

    /**
     * Gets the value of the isCurrentUser property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsCurrentUser() {
        return isCurrentUser;
    }

    /**
     * Sets the value of the isCurrentUser property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsCurrentUser(Boolean value) {
        this.isCurrentUser = value;
    }

    /**
     * Gets the value of the userRef property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUserRef() {
        return userRef;
    }

    /**
     * Sets the value of the userRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUserRef(String value) {
        this.userRef = value;
    }

}
