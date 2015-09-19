/*
 * TagTools.java
 *
 * Created on 25 July 2007, 14:21
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tcav.xml;

import java.util.List;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 *
 * @author nzr4dl
 */
public class TagTools {
    
    public static void addToList(NamedNodeMap nodeMap, String attribute, List<String> list) {
        Node node = nodeMap.getNamedItem(attribute);
        if(node == null)
            return;
        
        String listValues = node.getNodeValue();
        if(listValues == null)
            return;
        
        String[] s = listValues.split(" ");
        for (String item : s) {
            if (item.indexOf('#') == 0) {
                list.add(item.substring(1));
            } else {
                list.add(item);
            }
        }
    }
    
    public static String getStringValue(NamedNodeMap nodeMap, String attribute) {
        Node node = nodeMap.getNamedItem(attribute);
        if(node != null) {
            String s = node.getNodeValue();
            if(s != null)
                if(s.indexOf('#') == 0)
                    s = s.substring(1);
            return s;
        }
        return null;
    }
    
    public static Double getDoubleValue(NamedNodeMap nodeMap, String attribute) {
        Node node = nodeMap.getNamedItem(attribute);
        if(node == null)
            return null;
        
        String s = node.getNodeValue();
        if(s ==null || s.equals(""))
            return null;
        else
            return Double.parseDouble(s);
    }
    
    public static Boolean getBooleanValue(NamedNodeMap nodeMap, String attribute) {
        Node node = nodeMap.getNamedItem(attribute);
        if(node == null)
            return null;
        
        String s = node.getNodeValue();
        if(s ==null || s.equals(""))
            return null;
        else
            return Boolean.parseBoolean(s);
    }
    
    public static Integer getIntegerValue(NamedNodeMap nodeMap, String attribute) {
        Node node = nodeMap.getNamedItem(attribute);
        if(node == null)
            return null;
        
        String s = node.getNodeValue();
        if(s ==null || s.equals(""))
            return null;
        else
            return Integer.parseInt(s);
    }
}
