/*
 * ResourceStrings.java
 *
 * Created on 13 July 2007, 11:56
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tceav.resources;

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
        return "6.2";
    }
    
    public static String getBuild() {
        return "199";
    }
    
    public static String getReleaseDate() {
        return "21-Jul-12";
    }
    
    public static String getApplicationName() {
        return "Teamcenter Engineering Admin View";
    }
    
    public static String getApplicationNameShort() {
        return "TcEAV";
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
               "Doug Schilling\n"+
               "Kevin Coykendal\n"+
               "Annemie Witters";
    }
    
}
