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
    private static boolean saveSettingsOnExit;
    private static final String PROPERTY_SAVESETTINGSONEXIT = "tcav.saveSettingsOnExit";
    private static final boolean PROPERTY_SAVESETTINGSONEXIT_DEFAULT = true;
    private static String userInterface;
    private static final String PROPERTY_USERINTERFACE = "tcav.userInterface";
    private static final String PROPERTY_USERINTERFACE_DEFAULT = "";
    
    /* properties access manager*/
    private static String amLoadPath;
    private static final String PROPERTY_AMLOADPATH = "tcav.am.LoadPath";
    private static final String PROPERTY_AMLOADPATH_DEFAULT = "";
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
    private static String pmLoadPath;
    private static final String PROPERTY_PMLOADPATH = "tcav.pm.loadPath";
    private static final String PROPERTY_PMLOADPATH_DEFAULT = "";
    private static int pmProcedureMode;
    private static final String PROPERTY_PM_PROCEDUREMODE = "tcav.pm.procedureMode";
    private static int PROPERTY_PM_PROCEDUREMODE_DEFAULT = -1;
    private static int pmSplitLocation;
    private static final String PROPERTY_PM_SPLITLOCATION = "tcav.pm.SplitLocation";
    private static int PROPERTY_PM_SPLITLOCATION_DEFAULT = -1;
    private static boolean pmWokflowExpandedView;
    private static final String PROPERTY_PM_WORKFLOWEXPANDEDVIEW = "tcav.pm.workflowExpandedView";
    private static final boolean PROPERTY_PM_WORKFLOWEXPANDEDVIEW_DEFAULT = true;
    private static boolean pmActionExpandedView;
    private static final String PROPERTY_PM_ACTIONEXPANDEDVIEW = "tcav.pm.actionExpandedView";
    private static final boolean PROPERTY_PM_ACTIONEXPANDEDVIEW_DEFAULT = true;
    
    private static Properties property = new Properties();
    
    /** Creates a new instance of Settings */
    public Settings() {
        
    }
    
    public static void load() throws Exception {
        File path = new File(System.getenv("USERPROFILE"),".TcAV");
        if(!path.exists())
            path.mkdir();
        File file = new File(path,"TcAdminView.cfg");
        if(file.exists()) {
            FileInputStream fis =  new FileInputStream(file);
            property.load(fis);
            fis.close();
        }
        
        setAMLoadPath(
                getPropertyAsString(
                PROPERTY_AMLOADPATH,
                PROPERTY_AMLOADPATH_DEFAULT));
        setUserInterface(
                getPropertyAsString(
                PROPERTY_USERINTERFACE,
                PROPERTY_USERINTERFACE_DEFAULT));
        setSaveSettingsOnExit(
                getPropertyAsBoolean(
                PROPERTY_SAVESETTINGSONEXIT,
                PROPERTY_SAVESETTINGSONEXIT_DEFAULT));
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
        
        
        setPMLoadPath(
                getPropertyAsString(
                PROPERTY_PMLOADPATH,
                PROPERTY_PMLOADPATH_DEFAULT));
        setPMProcedureMode(
                getPropertyAsInt(
                PROPERTY_PM_PROCEDUREMODE,
                PROPERTY_PM_PROCEDUREMODE_DEFAULT));
        setPMSplitLocation(
                getPropertyAsInt(
                PROPERTY_PM_SPLITLOCATION,
                PROPERTY_PM_SPLITLOCATION_DEFAULT));
        setPMWorkflowExpandedView(
                getPropertyAsBoolean(
                PROPERTY_PM_WORKFLOWEXPANDEDVIEW,
                PROPERTY_PM_WORKFLOWEXPANDEDVIEW_DEFAULT));
        setPMActionExpandedView(
                getPropertyAsBoolean(
                PROPERTY_PM_ACTIONEXPANDEDVIEW,
                PROPERTY_PM_ACTIONEXPANDEDVIEW_DEFAULT));
        
    }
    
    public static void store() throws Exception  {
        property.setProperty(
                PROPERTY_USERINTERFACE,
                getUserInterface());
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
                PROPERTY_AMLOADPATH,
                getAMLoadPath());
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
                PROPERTY_PMLOADPATH,
                getPMLoadPath());
        property.setProperty(
                PROPERTY_PM_PROCEDUREMODE,
                Integer.toString(getPMProcedureMode()));
        property.setProperty(
                PROPERTY_PM_SPLITLOCATION,
                Integer.toString(getPMSplitLocation()));
        property.setProperty(
                PROPERTY_PM_WORKFLOWEXPANDEDVIEW,
                Boolean.toString(getPMWorkflowExpandedView()));
        property.setProperty(
                PROPERTY_PM_ACTIONEXPANDEDVIEW,
                Boolean.toString(getPMActionExpandedView()));
        
        File path = new File(System.getenv("USERPROFILE"),".TcAV");
        if(!path.exists())
            path.mkdir();
        File file = new File(path,"TcAdminView.cfg");
        
        if(file.exists())
            if(!file.canWrite())
                return;
        
        if(path.canWrite()) {
            FileOutputStream fos = new FileOutputStream(file);
            property.store(fos, ResourceLocator.getVersion());
            fos.close();
        }
    }
    
    public static String getAMLoadPath() {
        return amLoadPath;
    }
    
    public static void setAMLoadPath(String amLoadPath) {
        Settings.amLoadPath = amLoadPath;
    }
    
    public static String getPMLoadPath() {
        return pmLoadPath;
    }
    
    public static void setPMLoadPath(String pmLoadPath) {
        Settings.pmLoadPath = pmLoadPath;
    }
    
    public static String getUserInterface() {
        return userInterface;
    }
    
    public static void setUserInterface(String userInterface) {
        Settings.userInterface = userInterface;
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
    
    public static int getPMSplitLocation(){
        return pmSplitLocation;
    }
    
    public static void setPMSplitLocation(int pmSplitLocation) {
        if(pmSplitLocation == -1)
            Settings.pmSplitLocation = (int)(frameSizeX*0.5);
        else
            Settings.pmSplitLocation = pmSplitLocation;
    }
    
    public static boolean getPMWorkflowExpandedView() {
        return pmWokflowExpandedView;
    }
    
    public static void setPMWorkflowExpandedView(boolean pmWokflowExpandedView) {
        Settings.pmWokflowExpandedView = pmWokflowExpandedView;
    }
    
    public static boolean getPMActionExpandedView() {
        return pmActionExpandedView;
    }
    
    public static void setPMActionExpandedView(boolean pmActionExpandedView) {
        Settings.pmActionExpandedView = pmActionExpandedView;
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
