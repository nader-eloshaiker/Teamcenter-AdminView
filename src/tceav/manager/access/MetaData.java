/*
 * MetaData.java
 *
 * Created on 18 June 2007, 14:36
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tceav.manager.access;

/**
 *
 * @author NZR4DL
 */
public class MetaData {
    
    private String userDetails;
    private String dateDetails;
    private String timeDetails;
    private String stringID;
    private boolean legacey;
    
    /**
     * Creates a new instance of MetaData
     */
    public MetaData() {
        legacey = false;
    }
    
    public void loadLegacey(String metaData) {
        String[] temp;
        
        stringID = metaData;
        temp = metaData.split("\n");
        userDetails = temp[0].substring(26);
        temp = temp[1].split(" ");
        dateDetails = temp[0].substring(1);
        timeDetails = temp[1].substring(1);
        
        legacey = true;
    }
    
    public boolean isLegacey() {
        return legacey;
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
    
    @Override
    public String toString() {
        return stringID;
    }
}
