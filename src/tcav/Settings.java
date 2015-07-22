/*
 * Settings.java
 *
 * Created on 30 August 2007, 10:52
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tcav;

import java.awt.GraphicsEnvironment;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.File;
import java.util.Properties;

/**
 *
 * @author nzr4dl
 */
public class Settings {
    /* properties application*/
    private static int frameSizeX;
    private static final String PROPERTY_FRAMESIZEX = "tcav.frameSizeX";
    private static final int PROPERTY_FRAMESIZEX_DEFAULT = -1;
    private static int frameSizeY;
    private static final String PROPERTY_FRAMESIZEY = "tcav.frameSizeY";
    private static final int PROPERTY_FRAMESIZEY_DEFAULT = -1;
    private static int frameLocationX;
    private static final String PROPERTY_FRAMELOCATIONX = "tcav.frameLocationX";
    private static final int PROPERTY_FRAMELOCATIONX_DEFAULT = 0;
    private static int frameLocationY;
    private static final String PROPERTY_FRAMELOCATIONY = "tcav.frameLocationY";
    private static final int PROPERTY_FRAMELOCATIONY_DEFAULT = 0;
    private static String loadPath;
    private static final String PROPERTY_LOADPATH = "tcav.loadPath";
    private static final String PROPERTY_LOADPATH_DEFAULT = "";
    private static boolean saveSettingsOnExit;
    private static final String PROPERTY_SAVESETTINGSONEXIT = "tcav.saveSettingsOnExit";
    private static final boolean PROPERTY_SAVESETTINGSONEXIT_DEFAULT = true;
    
    /* properties access manager*/
    private static int amSplitLocation;
    private static final String PROPERTY_AM_SPLITLOCATION = "tcav.am.splitLocation";
    private static final int PROPERTY_AM_SPLITLOCATION_DEFAULT = -1;
    private static int[] amACLSort;
    private static final String PROPERTY_AM_ACLSORT = "tcav.am.aclSort";
    private static final String PROPERTY_AM_ACLSORT_DEFAULT = "0,2";
    private static boolean amACLSortAscending;
    private static final String PROPERTY_AM_ACLSORTASCENDING = "tcav.am.aclSortAscending";
    private static final boolean PROPERTY_AM_ACLSORTASCENDING_DEFAULT = true;
    private static int amACLTab;
    private static final String PROPERTY_AM_ACLTAB = "tcav.am.aclTab";
    private static final int PROPERTY_AM_ACLTAB_DEFAULT = 0;
    
    /* properties process manager */
    private static int pmProcedureMode;
    private static final String PROPERTY_PM_PROCEDUREMODE = "tcav.pm.procedureMode";
    private static int PROPERTY_PM_PROCEDUREMODE_DEFAULT = -1;
    private static int pmSplitLocation1;
    private static final String PROPERTY_PM_SPLITLOCATION1 = "tcav.pm.splitLocation1";
    private static int PROPERTY_PM_SPLITLOCATION1_DEFAULT = -1;
    private static int pmSplitLocation2;
    private static final String PROPERTY_PM_SPLITLOCATION2 = "tcav.pm.splitLocation2";
    private static int PROPERTY_PM_SPLITLOCATION2_DEFAULT = -1;
    private static boolean pmExpandedView;
    private static final String PROPERTY_PM_EXPANDEDVIEW = "tcav.pm.expandedView";
    private static final boolean PROPERTY_PM_EXPANDEDVIEW_DEFAULT = true;
    
    private static Properties property = new Properties();
    
    /** Creates a new instance of Settings */
    public Settings() {
        
    }
    
    public static void load() throws Exception {
        File file = new File("TcAdminView.cfg");
        if(file.exists()) {
            FileInputStream fis =  new FileInputStream(file);
            property.load(fis);
            fis.close();
        }
        
        setLoadPath(
                getPropertyAsString(
                PROPERTY_LOADPATH,
                PROPERTY_LOADPATH_DEFAULT));
        setSaveSettingsOnExit(
                getPropertyAsBoolean(
                PROPERTY_SAVESETTINGSONEXIT,
                PROPERTY_SAVESETTINGSONEXIT_DEFAULT
                ));
        setFrameSizeX(
                getPropertyAsInt(
                PROPERTY_FRAMESIZEX,
                PROPERTY_FRAMESIZEX_DEFAULT));
        setFrameSizeY(
                getPropertyAsInt(
                PROPERTY_FRAMESIZEY,
                PROPERTY_FRAMESIZEY_DEFAULT));
        setFrameLocationX(
                getPropertyAsInt(
                PROPERTY_FRAMELOCATIONX,
                PROPERTY_FRAMELOCATIONX_DEFAULT));
        setFrameLocationY(
                getPropertyAsInt(
                PROPERTY_FRAMELOCATIONY,
                PROPERTY_FRAMELOCATIONY_DEFAULT));
        
        
        
        
        setAMSplitLocation(
                getPropertyAsInt(
                PROPERTY_AM_SPLITLOCATION,
                PROPERTY_AM_SPLITLOCATION_DEFAULT));
        setAMACLSortAscending(
                getPropertyAsBoolean(
                PROPERTY_AM_ACLSORTASCENDING,
                PROPERTY_AM_ACLSORTASCENDING_DEFAULT));
        setAMACLTab(
                getPropertyAsInt(
                PROPERTY_AM_ACLTAB,
                PROPERTY_AM_ACLTAB_DEFAULT));
        setAMACLSort(
                getPropertyAsIntArray(
                PROPERTY_AM_ACLSORT,
                PROPERTY_AM_ACLSORT_DEFAULT));
        
        setPMProcedureMode(
                getPropertyAsInt(
                PROPERTY_PM_PROCEDUREMODE,
                PROPERTY_PM_PROCEDUREMODE_DEFAULT));
        setPMSplitLocation1(
                getPropertyAsInt(
                PROPERTY_PM_SPLITLOCATION1,
                PROPERTY_PM_SPLITLOCATION1_DEFAULT));
        setPMSplitLocation2(
                getPropertyAsInt(
                PROPERTY_PM_SPLITLOCATION2,
                PROPERTY_PM_SPLITLOCATION2_DEFAULT));
        setPMExpandedView(
                getPropertyAsBoolean(
                PROPERTY_PM_EXPANDEDVIEW,
                PROPERTY_PM_EXPANDEDVIEW_DEFAULT));
        
    }
    
    public static void store() throws Exception  {
        File file = new File("TcAdminView.cfg");
        FileOutputStream fos = new FileOutputStream(file);
        property.setProperty(
                PROPERTY_LOADPATH,
                getLoadPath());
        property.setProperty(
                PROPERTY_SAVESETTINGSONEXIT,
                Boolean.toString(getSaveSettingsOnExit()));
        property.setProperty(
                PROPERTY_FRAMESIZEX,
                Integer.toString(getFrameSizeX()));
        property.setProperty(
                PROPERTY_FRAMESIZEY,
                Integer.toString(getFrameSizeY()));
        property.setProperty(
                PROPERTY_FRAMELOCATIONX,
                Integer.toString(getFrameLocationX()));
        property.setProperty(
                PROPERTY_FRAMELOCATIONY,
                Integer.toString(getFrameLocationY()));
        
        
        property.setProperty(
                PROPERTY_AM_SPLITLOCATION,
                Integer.toString(getAMSplitLocation()));
        property.setProperty(
                PROPERTY_AM_ACLSORTASCENDING,
                Boolean.toString(getAMACLSortAscending()));
        property.setProperty(
                PROPERTY_AM_ACLTAB,
                Integer.toString(getAMACLTab()));
        property.setProperty(
                PROPERTY_AM_ACLSORT,
                convertIntArrayToString(getAMACLSort()));
        
        property.setProperty(
                PROPERTY_PM_PROCEDUREMODE,
                Integer.toString(getPMProcedureMode()));
        property.setProperty(
                PROPERTY_PM_SPLITLOCATION1,
                Integer.toString(getPMSplitLocation1()));
        property.setProperty(
                PROPERTY_PM_SPLITLOCATION2,
                Integer.toString(getPMSplitLocation2()));
        property.setProperty(
                PROPERTY_PM_EXPANDEDVIEW,
                Boolean.toString(getPMExpandedView()));
        
        property.store(fos, ResourceLocator.getVersion());
        fos.close();
    }
    
    public static String getLoadPath() {
        return loadPath;
    }
    
    public static boolean getSaveSettingsOnExit() {
        return saveSettingsOnExit;
    }
    
    public static void setSaveSettingsOnExit(boolean saveSettingsOnExit){
        Settings.saveSettingsOnExit = saveSettingsOnExit;
    }

    public static int getFrameSizeX() {
        return frameSizeX;
    }
    
    public static void setFrameSizeX(int frameSizeX) {
        int width = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().width;
        if( (frameSizeX == -1) || (frameSizeX > width) )
            Settings.frameSizeX = width;
        else
            Settings.frameSizeX = frameSizeX;
    }
    
    public static int getFrameSizeY() {
        return frameSizeY;
    }
    
    public static void setFrameSizeY(int frameSizeY) {
        int height = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().height;
        if( (frameSizeY == -1) || (frameSizeY > height) )
            Settings.frameSizeY = height;
        else
            Settings.frameSizeY = frameSizeY;
    }
    
    public static int getFrameLocationX() {
        return frameLocationX;
    }
    
    public static void setFrameLocationX(int frameLocationX) {
        Settings.frameLocationX = frameLocationX;
    }
    
    public static int getFrameLocationY() {
        return frameLocationY;
    }
    
    public static void setFrameLocationY(int frameLocationY) {
        Settings.frameLocationY = frameLocationY;
    }
    
    public static void setLoadPath(String loadPath) {
        Settings.loadPath = loadPath;
    }
    
    public static int getAMSplitLocation(){
        return amSplitLocation;
    }
    
    public static void setAMSplitLocation(int amSplitLocation) {
        if(amSplitLocation == -1)
            Settings.amSplitLocation = (int)(frameSizeY*0.58);
        else
            Settings.amSplitLocation = amSplitLocation;
    }
    
    public static int[] getAMACLSort() {
        return amACLSort;
    }
    
    public static void setAMACLSort(int[] amACLSort) {
        Settings.amACLSort = amACLSort;
    }
    
    public static boolean getAMACLSortAscending() {
        return amACLSortAscending;
    }
    
    public static void setAMACLSortAscending(boolean amACLSortAscending) {
        Settings.amACLSortAscending = amACLSortAscending;
    }
    
    public static int getAMACLTab() {
        return amACLTab;
    }
    
    public static void setAMACLTab(int amACLTab) {
        Settings.amACLTab = amACLTab;
    }
    
    public static int getPMProcedureMode() {
        return pmProcedureMode;
    }
    
    public static void setPMProcedureMode(int pmProcedureMode) {
        if(pmProcedureMode == -1)
            Settings.pmProcedureMode = 0;
        else
            Settings.pmProcedureMode = pmProcedureMode;
    }
    
    public static int getPMSplitLocation1(){
        return pmSplitLocation1;
    }
    
    public static void setPMSplitLocation1(int pmSplitLocation1) {
        if(pmSplitLocation1 == -1)
            Settings.pmSplitLocation1 = (int)(frameSizeX*0.5);
        else
            Settings.pmSplitLocation1 = pmSplitLocation1;
    }
    
    public static int getPMSplitLocation2(){
        return pmSplitLocation2;
    }
    
    public static void setPMSplitLocation2(int pmSplitLocation2) {
        if(pmSplitLocation2 == -1)
            Settings.pmSplitLocation2 = (int)(frameSizeY-380);
        else
            Settings.pmSplitLocation2 = pmSplitLocation2;
    }
    
    public static boolean getPMExpandedView() {
        return pmExpandedView;
    }
    
    public static void setPMExpandedView(boolean pmExpandedView) {
        Settings.pmExpandedView = pmExpandedView;
    }
    
    private static int getPropertyAsInt(String name, int defaultValue) throws NumberFormatException {
        int intValue = defaultValue;
        String stringValue = property.getProperty(name);
        
        if (stringValue != null)
            intValue = Integer.parseInt(stringValue);
        
        return intValue;
    }
    
    private static int[] getPropertyAsIntArray(String name, String defaultValue) throws NumberFormatException {
        String[] defaultValueArrayString = defaultValue.split(",");
        int[] intArrayValue = new int[defaultValueArrayString.length];
        String stringValue = property.getProperty(name);
        
        intArrayValue = convertStringArrayToIntArray(defaultValueArrayString);
        
        if ((stringValue != null) && (!stringValue.equals(""))){
            intArrayValue = convertStringArrayToIntArray(stringValue.split(","));
        }
        
        return intArrayValue;
    }
    
    private static int[] convertStringArrayToIntArray(String[] s) throws NumberFormatException {
        int[] intArray = new int[s.length];
        
        for(int i=0; i<intArray.length; i++)
            intArray[i] = Integer.parseInt(s[i]);
        
        return intArray;
    }
    
    private static String convertIntArrayToString(int[] i) {
        String s = "";
        
        for(int k=0; k < i.length; k++){
            if(k != 0)
                s += ",";
            s += Integer.toString(i[k]);
        }
        
        return s;
    }
    
    
    private static boolean getPropertyAsBoolean(String name, boolean defaultValue) {
        boolean booleanValue = defaultValue;
        String  stringValue  = property.getProperty(name);
        
        if (stringValue != null)
            booleanValue = Boolean.valueOf(stringValue).booleanValue();
        
        return booleanValue;
    }
    
    private static String getPropertyAsString(String name, String defaultValue) {
        String  stringValue  = property.getProperty(name);
        
        if (stringValue == null)
            stringValue = defaultValue;
        
        return stringValue;
    }
}
