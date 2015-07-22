/*
 * HeaderType.java
 *
 * Created on 14 August 2007, 16:04
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tcadminview.plmxmlpdm;

import java.util.*;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.NamedNodeMap;
import tcadminview.plmxmlpdm.*;
import tcadminview.plmxmlpdm.*;
import tcadminview.plmxmlpdm.base.AttribOwnerBase;

/**
 *
 * @author nzr4dl
 */
public class HeaderType extends AttribOwnerBase{

    //@XmlAttribute
    protected final String traverseRootRefsAttribute = "traverseRootRefs";
    protected List<String> traverseRootRefs;
    
    //@XmlAttribute
    protected final String transferContextAttribute = "transferContext";
    protected String transferContext;
    
    /**
     * Creates a new instance of HeaderType
     */
    public HeaderType(Node node) {
        super(node);
        Node currentNode = node;
        NamedNodeMap attrib = currentNode.getAttributes();
        NodeList nodeList = currentNode.getChildNodes();
        
        setTransferContext(
                TagTools.getStringValue(attrib, transferContextAttribute));
        
        TagTools.addToList(attrib, traverseRootRefsAttribute, getTraverseRootRefs());
    }
    
    public List<String> getTraverseRootRefs() {
        if (traverseRootRefs == null) {
            traverseRootRefs = new ArrayList<String>();
        }
        return this.traverseRootRefs;
    }
    
    public String getTransferContext() {
        return transferContext;
    }
    
    public void setTransferContext(String transferContext) {
        this.transferContext = transferContext;
    }
    
}
