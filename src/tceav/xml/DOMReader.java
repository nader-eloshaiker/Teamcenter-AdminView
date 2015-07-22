/*
 * DOMReader.java
 *
 * Created on 20 July 2007, 11:10
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tceav.xml;

import java.io.File;//InputStream;
import java.io.InputStream;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
/**
 *
 * @author nzr4dl
 */
public class DOMReader {
    
    private Node rootNode;
    
    //public DOMReader(InputStream is) throws Exception {
    public DOMReader(File file) throws Exception {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setIgnoringElementContentWhitespace(true);
        factory.setCoalescing(true);

        DocumentBuilder builder = factory.newDocumentBuilder();
        Document d =  builder.parse(file);
        rootNode = d.getDocumentElement();
    }
    
    public DOMReader(InputStream is) throws Exception {
        // Validation
        //SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        //Schema schema = schemaFactory.newSchema(ResourceStrings.getPLMXMLPDMSchema());
        
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