/*
 * ResourceStrings.java
 *
 * Created on 13 July 2007, 11:56
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tcav.resources;

import java.net.URL;
import java.io.IOException;
/**
 *
 * @author nzr4dl
 */
public class ResourceStrings {
    
    /**
     * Creates a new instance of ResourceStrings
     */
    public ResourceStrings() {
    }
    
    public static URL getChangeLog() throws IOException {
        return ResourceStrings.class.getResource("ChangeLog.html");
    }
    
    public static URL getLicense() throws IOException {
        return ResourceStrings.class.getResource("gpl-3.html");
    }

    public static URL getPLMXMLPDMSchema() throws IOException {
        return ResourceStrings.class.getResource("schema/PLMXMLPDMSchema.xsd");
    }
    
    public static String getVersion() {
        return "7.0";
    }
    
    public static String getBuild() {
        return "202";
    }
    
    public static String getReleaseDate() {
        return "22-Jul-15";
    }
    
    public static String getApplicationName() {
        return "Teamcenter Admin View";
    }
    
    public static String getApplicationNameShort() {
        return "TcAV";
    }
    
    public static String getDeveloperName() {
        return "Nader Eloshaiker";
    }
    
    public static String getDeveloperEmail() {
        return "nader.eloshaiker@gmail.com";
    }

    public static String getWebsite() {
        return "http://sourceforge.net/projects/tceav/";
    }
    
    public static String getProjectSupporters() {
        return "Anja Bartsch\n"+ 
               "Nikolaos Boidoglou\n"+ 
               "Doug Schilling\n"+
               "Kevin Coykendal\n"+
               "Annemie Witters";
    }
    
}
