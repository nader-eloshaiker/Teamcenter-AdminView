//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.0.3-b24-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2007.07.22 at 11:41:15 AM EST 
//


package tcadminview.plmxmlpdm.type;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.NamedNodeMap;
import tcadminview.plmxmlpdm.*;
import tcadminview.plmxmlpdm.base.AttribOwnerBase;
import tcadminview.plmxmlpdm.classtype.WorkflowTemplateClassificationEnum;
import tcadminview.plmxmlpdm.classtype.WorkflowTemplateStageEnum;


/**
 * 
 *       The WorkflowTemplateType represents the type for the workflow templates.  
 *       Typically workflow template is used to initialize workflow processes. A workflow 
 *       template have actions associated with it to represent a list of workflow actions that
 *       will be triggered during a workflow process. The actions objects are WorkflowAction 
 *       objects. A workflow template also might have a parent task template, one or more 
 *       dependency task templates, and one or more child task templates associated with it. 
 *       These are also WorkflowTemplate objects.
 *       
 * 
 * <p>Java class for WorkflowTemplateType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="WorkflowTemplateType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.plmxml.org/Schemas/PLMXMLSchema}AttribOwnerBase">
 *       &lt;sequence>
 *         &lt;element name="TaskDescription" type="{http://www.plmxml.org/Schemas/PLMXMLSchema}UserListDataType"/>
 *         &lt;element name="DependencyTaskActions" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="DependencyTaskTemplates" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *       &lt;attribute name="actions" type="{http://www.plmxml.org/Schemas/PLMXMLSchema}uriReferenceListType" />
 *       &lt;attribute name="dependencyTaskTemplateRefs" type="{http://www.plmxml.org/Schemas/PLMXMLSchema}uriReferenceListType" />
 *       &lt;attribute name="iconKey" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="location" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="objectType" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="parentTaskTemplateRef" type="{http://www.plmxml.org/Schemas/PLMXMLSchema}anyURIType" />
 *       &lt;attribute name="showInProcessStage" use="required" type="{http://www.w3.org/2001/XMLSchema}Boolean" />
 *       &lt;attribute name="signoffQuorum" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *       &lt;attribute name="stage" type="{http://www.plmxml.org/Schemas/PLMXMLSchema}WorkflowTemplateStageEnum" default="available" />
 *       &lt;attribute name="subTemplateRefs" type="{http://www.plmxml.org/Schemas/PLMXMLSchema}uriReferenceListType" />
 *       &lt;attribute name="templateClassification" type="{http://www.plmxml.org/Schemas/PLMXMLSchema}WorkflowTemplateClassificationEnum" default="task" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
//@XmlAccessorType(XmlAccessType.FIELD)
/*
 @XmlType(name = "WorkflowTemplateType", propOrder = {
    "taskDescription",
    "dependencyTaskActions",
    "dependencyTaskTemplates"
})
 */
public class WorkflowTemplateType extends AttribOwnerBase {

    //@XmlElement(name = "TaskDescription", required = true)
    protected UserListDataType taskDescription;
    //@XmlElement(name = "DependencyTaskActions", required = true)
    protected String dependencyTaskActions;
    //@XmlElement(name = "DependencyTaskTemplates", required = true)
    protected String dependencyTaskTemplates;
    
    //@XmlAttribute
    protected final String actionsAttribute = "actions";
    protected List<String> actions;
    
    //@XmlAttribute
    protected final String dependencyTaskTemplateRefsAttribute = "dependencyTaskTemplateRefs";
    protected List<String> dependencyTaskTemplateRefs;
    
    //@XmlAttribute
    protected final String iconKeyAttribute = "iconKey";
    protected String iconKey;
    
    //@XmlAttribute(required = true)
    protected final String locationAttribute = "location";
    protected String location;
    
    //@XmlAttribute(required = true)
    protected final String objectTypeAttribute = "objectType";
    protected String objectType;
    
    //@XmlAttribute
    protected final String parentTaskTemplateRefAttribute = "parentTaskTemplateRef";
    protected String parentTaskTemplateRef;
    
    //@XmlAttribute(required = true)
    protected final String showInProcessStageAttribute = "showInProcessStage";
    protected Boolean showInProcessStage;
    
    //@XmlAttribute
    protected final String signoffQuorumAttribute = "signoffQuorum";
    protected Integer signoffQuorum;
    
    //@XmlAttribute
    protected final String stageAttribute = "stage";
    protected WorkflowTemplateStageEnum stage;
    
    //@XmlAttribute
    protected final String subTemplateRefsAttribute = "subTemplateRefs";
    protected List<String> subTemplateRefs;
    
    //@XmlAttribute
    protected final String templateClassificationAttribute = "templateClassification";
    protected WorkflowTemplateClassificationEnum templateClassification;
    

    public WorkflowTemplateType(Node node) {
        super(node);
        Node currentNode = node;
        NamedNodeMap attrib = currentNode.getAttributes();
        NodeList nodeList = currentNode.getChildNodes();
        
        
        setIconKey(
                TagTools.getStringValue(attrib, iconKeyAttribute));
        setLocation(
                TagTools.getStringValue(attrib, locationAttribute));
        setObjectType(
                TagTools.getStringValue(attrib, objectTypeAttribute));
        setParentTaskTemplateRef(
                TagTools.getStringValue(attrib, parentTaskTemplateRefAttribute));
        setShowInProcessStage(
                TagTools.getBooleanValue(attrib, showInProcessStageAttribute));
        setSignoffQuorum(
                TagTools.getIntegerValue(attrib, signoffQuorumAttribute));
        
        String s;
        
        s = TagTools.getStringValue(attrib, stageAttribute);
        if(s != null)
            setStage(WorkflowTemplateStageEnum.fromValue(s));
        
        s = TagTools.getStringValue(attrib, templateClassificationAttribute);
        if(s != null)
            setTemplateClassification(WorkflowTemplateClassificationEnum.fromValue(s));
        
        TagTools.addToList(attrib, subTemplateRefsAttribute, getSubTemplateRefs());
        
        TagTools.addToList(attrib, actionsAttribute, getActions());
        
        TagTools.addToList(attrib, dependencyTaskTemplateRefsAttribute, getDependencyTaskTemplateRefs());
        
        
        TagTypeEnum tagType;
        for (int i=0; i<nodeList.getLength(); i++) {
            currentNode = nodeList.item(i);
            tagType = TagTypeEnum.fromValue(currentNode.getNodeName());
            
            switch(tagType) {
                case DependencyTaskActions:
                    setDependencyTaskActions(currentNode.getNodeValue());
                    break;
                    
                case DependencyTaskTemplates:
                    setDependencyTaskTemplates(currentNode.getNodeValue());
                    break;
                    
                case TaskDescription:
                    setTaskDescription(new UserListDataType(currentNode));
                    break;
                    
                default:
                    break;
            }
        }
        
    }
    
    /**
     * Gets the value of the taskDescription property.
     * 
     * @return
     *     possible object is
     *     {@link UserListDataType }
     *     
     */
    public UserListDataType getTaskDescription() {
        return taskDescription;
    }

    /**
     * Sets the value of the taskDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link UserListDataType }
     *     
     */
    public void setTaskDescription(UserListDataType value) {
        this.taskDescription = value;
    }

    /**
     * Gets the value of the dependencyTaskActions property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDependencyTaskActions() {
        return dependencyTaskActions;
    }

    /**
     * Sets the value of the dependencyTaskActions property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDependencyTaskActions(String value) {
        this.dependencyTaskActions = value;
    }

    /**
     * Gets the value of the dependencyTaskTemplates property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDependencyTaskTemplates() {
        return dependencyTaskTemplates;
    }

    /**
     * Sets the value of the dependencyTaskTemplates property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDependencyTaskTemplates(String value) {
        this.dependencyTaskTemplates = value;
    }

    /**
     * Gets the value of the actions property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the actions property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getActions().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getActions() {
        if (actions == null) {
            actions = new ArrayList<String>();
        }
        return this.actions;
    }
    
    /**
     * Gets the value of the dependencyTaskTemplateRefs property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the dependencyTaskTemplateRefs property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDependencyTaskTemplateRefs().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getDependencyTaskTemplateRefs() {
        if (dependencyTaskTemplateRefs == null) {
            dependencyTaskTemplateRefs = new ArrayList<String>();
        }
        return this.dependencyTaskTemplateRefs;
    }

    /**
     * Gets the value of the iconKey property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIconKey() {
        return iconKey;
    }

    /**
     * Sets the value of the iconKey property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIconKey(String value) {
        this.iconKey = value;
    }

    /**
     * Gets the value of the location property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets the value of the location property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLocation(String value) {
        this.location = value;
    }

    /**
     * Gets the value of the objectType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getObjectType() {
        return objectType;
    }

    /**
     * Sets the value of the objectType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setObjectType(String value) {
        this.objectType = value;
    }

    /**
     * Gets the value of the parentTaskTemplateRef property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getParentTaskTemplateRef() {
        return parentTaskTemplateRef;
    }

    /**
     * Sets the value of the parentTaskTemplateRef property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setParentTaskTemplateRef(String value) {
        this.parentTaskTemplateRef = value;
    }

    /**
     * Gets the value of the showInProcessStage property.
     * 
     */
    public Boolean isShowInProcessStage() {
        return showInProcessStage;
    }

    /**
     * Sets the value of the showInProcessStage property.
     * 
     */
    public void setShowInProcessStage(Boolean value) {
        this.showInProcessStage = value;
    }

    /**
     * Gets the value of the signoffQuorum property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public Integer getSignoffQuorum() {
        return signoffQuorum;
    }

    /**
     * Sets the value of the signoffQuorum property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setSignoffQuorum(Integer value) {
        this.signoffQuorum = value;
    }

    /**
     * Gets the value of the stage property.
     * 
     * @return
     *     possible object is
     *     {@link WorkflowTemplateStageEnum }
     *     
     */
    public WorkflowTemplateStageEnum getStage() {
        if (stage == null) {
            return WorkflowTemplateStageEnum.AVAILABLE;
        } else {
            return stage;
        }
    }

    /**
     * Sets the value of the stage property.
     * 
     * @param value
     *     allowed object is
     *     {@link WorkflowTemplateStageEnum }
     *     
     */
    public void setStage(WorkflowTemplateStageEnum value) {
        this.stage = value;
    }

    /**
     * Gets the value of the subTemplateRefs property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the subTemplateRefs property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSubTemplateRefs().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getSubTemplateRefs() {
        if (subTemplateRefs == null) {
            subTemplateRefs = new ArrayList<String>();
        }
        return this.subTemplateRefs;
    }

    /**
     * Gets the value of the templateClassification property.
     * 
     * @return
     *     possible object is
     *     {@link WorkflowTemplateClassificationEnum }
     *     
     */
    public WorkflowTemplateClassificationEnum getTemplateClassification() {
        if (templateClassification == null) {
            return WorkflowTemplateClassificationEnum.TASK;
        } else {
            return templateClassification;
        }
    }

    /**
     * Sets the value of the templateClassification property.
     * 
     * @param value
     *     allowed object is
     *     {@link WorkflowTemplateClassificationEnum }
     *     
     */
    public void setTemplateClassification(WorkflowTemplateClassificationEnum value) {
        this.templateClassification = value;
    }

}
