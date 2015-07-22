/*
 * TagTools.java
 *
 * Created on 25 July 2007, 14:21
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tcadminview.plmxmlpdm;

import java.util.List;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 *
 * @author nzr4dl
 */
public class TagTools {
    
    public static void addToList(NamedNodeMap nodeMap, String attribute, List<String> list)
    {
        Node node = nodeMap.getNamedItem(attribute);
        if(node == null)
            return;
        
        String listValues = node.getNodeValue();
        if(listValues == null)
            return;
        
        String[] s = listValues.split(" ");
        for(int i=0; i<s.length; i++)
            list.add(s[i]);
    }
    
    public static String getStringValue(NamedNodeMap nodeMap, String attribute) {
        Node node = nodeMap.getNamedItem(attribute);
        if(node != null)
            return node.getNodeValue();
        else
            return null;
    }
    
    public static Double getDoubleValue(NamedNodeMap nodeMap, String attribute) {
        Node node = nodeMap.getNamedItem(attribute);
        if(node == null)
            return null;
        
        String s = node.getNodeValue();
        if((s.equals("")) || (s ==null))
            return null;
        else
            return Double.parseDouble(s);
    }
    
    public static Boolean getBooleanValue(NamedNodeMap nodeMap, String attribute) {
        Node node = nodeMap.getNamedItem(attribute);
        if(node == null)
            return null;
        
        String s = node.getNodeValue();
        if((s.equals("")) || (s ==null))
            return null;
        else
            return Boolean.parseBoolean(s);
    }

    public static Integer getIntegerValue(NamedNodeMap nodeMap, String attribute) {
        Node node = nodeMap.getNamedItem(attribute);
        if(node == null)
            return null;
        
        String s = node.getNodeValue();
        if((s.equals("")) || (s == null))
            return null;
        else
            return Integer.parseInt(s);
    }
}
