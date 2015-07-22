/*
 * MetaData.java
 *
 * Created on 18 June 2007, 14:36
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tcadminview.ruletree;

/**
 *
 * @author NZR4DL
 */
public class MetaData {
    
    private String userDetails;
    private String timeDetails;
    private String stringID;
    
    /**
     * Creates a new instance of MetaData
     */
    public MetaData() {

    }

    public void setMetaData(String metaData) {
        String[] temp;
        
        stringID = metaData;
        temp = metaData.split("\n");
        userDetails = temp[0].substring(26);
        timeDetails = temp[1].substring(1);
    }
    
    public String getUserDetails() {
        return userDetails;
    }
    
    public String getTimeDetails() {
        return timeDetails;
    }
    
    public String toString() {
        return stringID;
    }
}
