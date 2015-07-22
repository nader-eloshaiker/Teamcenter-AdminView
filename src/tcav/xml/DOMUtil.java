/*
 * DOMUtil.java
 *
 * Created on 20 July 2007, 11:10
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tcav.xml;

import tcav.procedure.ProcedureManager;
import tcav.ResourceLocator;
import java.util.List;
import java.io.InputStream;
import javax.xml.XMLConstants;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Schema;
import javax.xml.validation.Validator;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
/**
 *
 * @author nzr4dl
 */
public class DOMUtil {
    
    private Node rootNode;
    
    public DOMUtil(InputStream is) throws Exception {
        // Validation
        //SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        //Schema schema = schemaFactory.newSchema(ResourceLocator.getPLMXMLPDMSchema());
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setIgnoringElementContentWhitespace(true);
        factory.setCoalescing(true);
        // Validation
        //factory.setNamespaceAware(true);
        //factory.setValidating(true);
        //factory.setSchema(schema);
        //Validator handler = schema.newValidator();
        
        DocumentBuilder builder = factory.newDocumentBuilder();
        // Validation
        //builder.setErrorHandler(handler.getErrorHandler()); 
        Document d =  builder.parse(is);
        rootNode = d.getDocumentElement();
    }
    
    public Node getRootNode() {
        return rootNode;
    }
    
}