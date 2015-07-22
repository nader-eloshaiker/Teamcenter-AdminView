/*
 * MetaData.java
 *
 * Created on 18 June 2007, 14:36
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tcav.manager.access;

/**
 *
 * @author NZR4DL
 */
public class MetaData {
    
    private String userDetails;
    private String dateDetails;
    private String timeDetails;
    private String stringID;
    
    /**
     * Creates a new instance of MetaData
     */
    public MetaData(String metaData) {
        String[] temp;
        
        stringID = metaData;
        temp = metaData.split("\n");
        userDetails = temp[0].substring(26);
        temp = temp[1].split(" ");
        dateDetails = temp[0].substring(1);
        timeDetails = temp[1].substring(1);
    }
    
    public String getUserDetails() {
        return userDetails;
    }
    
    public String getTime() {
        return timeDetails;
    }
    
    public String getDate() {
        return dateDetails;
    }
    
    public String toString() {
        return stringID;
    }
}
