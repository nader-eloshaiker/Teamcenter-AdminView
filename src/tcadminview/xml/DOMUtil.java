/*
 * DOMUtil.java
 *
 * Created on 20 July 2007, 11:10
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tcadminview.xml;

import tcadminview.procedure.ProcedureManager;
import java.util.List;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.SAXParser;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.ErrorHandler;       
import org.xml.sax.SAXParseException;  
import org.xml.sax.XMLReader;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.sax.SAXSource;
/**
 *
 * @author nzr4dl
 */
public class DOMUtil{
    
    private static DOMResult result;
    
    public static Node createNodes(InputSource is) throws Exception{
        
        SAXParserFactory saxFactory = SAXParserFactory.newInstance(); 
        SAXParser parser = saxFactory.newSAXParser(); 
        XMLReader reader = new XMLTrimFilter(parser.getXMLReader()); 
 
        TransformerFactory factory = TransformerFactory.newInstance(); 
        Transformer transformer = factory.newTransformer(); 
        transformer.setOutputProperty(OutputKeys.INDENT, "no"); 
        result = new DOMResult(); 
        transformer.transform(new SAXSource(reader, is), result); 
        return result.getNode(); 
        
    }
    
    public static Node getNode() {
        return result.getNode();
    }
    
}