/*
 * Settings.java
 *
 * Created on 30 August 2007, 10:52
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tceav;

import java.awt.GraphicsEnvironment;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.File;
import java.util.Properties;
import tceav.resources.ResourceStrings;
import tceav.manager.ManagerAdapter;
import tceav.gui.access.NamedRuleComponent;
import tceav.gui.compare.CompareAccessManagerComponent;

/**
 *
 * @author nzr4dl
 */
public class Settings {
    /* properties application*/
    private static int frameSizeX;
    private static final String PROPERTY_FRAMESIZEX = "tceav.frameSizeX";
    private static final int PROPERTY_FRAMESIZEX_DEFAULT = -1;
    private static int frameSizeY;
    private static final String PROPERTY_FRAMESIZEY = "tceav.frameSizeY";
    private static final int PROPERTY_FRAMESIZEY_DEFAULT = -1;
    private static int frameLocationX;
    private static final String PROPERTY_FRAMELOCATIONX = "tceav.frameLocationX";
    private static final int PROPERTY_FRAMELOCATIONX_DEFAULT = 0;
    private static int frameLocationY;
    private static final String PROPERTY_FRAMELOCATIONY = "tceav.frameLocationY";
    private static final int PROPERTY_FRAMELOCATIONY_DEFAULT = 0;
    private static boolean saveSettingsOnExit;
    private static final String PROPERTY_SAVESETTINGSONEXIT = "tceav.saveSettingsOnExit";
    private static final boolean PROPERTY_SAVESETTINGSONEXIT_DEFAULT = true;
    private static String userInterface;
    private static final String PROPERTY_USERINTERFACE = "tceav.userInterface";
    private static final String PROPERTY_USERINTERFACE_DEFAULT = "";
    private static String version;
    private static final String PROPERTY_VERSION = "tceav.version";
    private static final String PROPERTY_VERSION_DEFAULT = "1.0";
    
    /* properties access manager*/
    private static String amLoadPath;
    private static final String PROPERTY_AMLOADPATH = "tceav.am.LoadPath";
    private static final String PROPERTY_AMLOADPATH_DEFAULT = "";
    private static int amSplitLocation;
    private static final String PROPERTY_AM_SPLITLOCATION = "tceav.am.splitLocation";
    private static final int PROPERTY_AM_SPLITLOCATION_DEFAULT = -1;
    private static int[] amACLSort;
    private static final String PROPERTY_AM_ACLSORT = "tceav.am.aclSort";
    private static final String PROPERTY_AM_ACLSORT_DEFAULT = "0,2";
    private static boolean amACLSortAscending;
    private static final String PROPERTY_AM_ACLSORTASCENDING = "tceav.am.aclSortAscending";
    private static final boolean PROPERTY_AM_ACLSORTASCENDING_DEFAULT = true;
    private static int amACLTab;
    private static final String PROPERTY_AM_ACLTAB = "tceav.am.aclTab";
    private static final int PROPERTY_AM_ACLTAB_DEFAULT = 0;
    private static boolean amSyncSelection;
    private static final String PROPERTY_AM_SYNCSELECTION = "tceav.am.syncSelection";
    private static final boolean PROPERTY_AM_SYNCSELECTION_DEFAULT = true;
    
    /* properties process manager */
    private static String pmLoadPath;
    private static final String PROPERTY_PMLOADPATH = "tceav.pm.loadPath";
    private static final String PROPERTY_PMLOADPATH_DEFAULT = "";
    private static int pmSplitLocation;
    private static final String PROPERTY_PM_SPLITLOCATION = "tceav.pm.SplitLocation";
    private static int PROPERTY_PM_SPLITLOCATION_DEFAULT = -1;
    
    
    private static boolean pmTblStrictArgument;
    private static final String PROPERTY_PM_TBL_STRICTARUMENT = "tceav.pm.tbl.strictArgument";
    private static final boolean PROPERTY_PM_TBL_STRICTARUMENT_DEFAULT = false;
    private static boolean pmTblMultiSheet;
    private static final String PROPERTY_PM_TBL_MULTISHEET = "tceav.pm.tbl.multiSheet";
    private static final boolean PROPERTY_PM_TBL_MULTISHEET_DEFAULT = true;
    private static boolean pmTblShowActions;
    private static final String PROPERTY_PM_TBL_SHOWACTIONS = "tceav.pm.tbl.showActions";
    private static final boolean PROPERTY_PM_TBL_SHOWACTIONS_DEFAULT = true;
    private static boolean pmTblIncludeIndents;
    private static final String PROPERTY_PM_TBL_INCLUDEINDENTS = "tceav.pm.tbl.includeIndents";
    private static final boolean PROPERTY_PM_TBL_INCLUDEINDENTS_DEFAULT = true;
    private static boolean pmTblDatabaseMode;
    private static final String PROPERTY_PM_TBL_DATABASEMODE = "tceav.pm.tbl.database";
    private static final boolean PROPERTY_PM_TBL_DATABASEMODE_DEFAULT = true;
    
    
    private static String compareMode;
    private static final String PROPERTY_COMPAREMODE = "tceav.compareMode";
    private static final String PROPERTY_COMPAREMODE_DEFAULT = ManagerAdapter.ACCESS_MANAGER_TYPE;
    
    
    /* properties access manager comparison */
    private static boolean amCmpSyncSelection;
    private static final String PROPERTY_AM_CMP_SYNCSELECTION = "tceav.am.cmp.syncSelection";
    private static final boolean PROPERTY_AM_CMP_SYNCSELECTION_DEFAULT = true;
    private static int amCmpACLTab;
    private static final String PROPERTY_AM_CMP_ACLTAB = "tceav.am.cmp.aclTab";
    private static final int PROPERTY_AM_CMP_ACLTAB_DEFAULT = 6;
    private static String amCmpDisplayMode;
    private static final String PROPERTY_AM_CMP_DISPLAYMODE = "tceav.am.cmp.displayMode";
    private static final String PROPERTY_AM_CMP_DISPLAYMODE_DEFAULT = CompareAccessManagerComponent.MODE_TREE;
    private static int amCmpFilterEqual;
    private static final String PROPERTY_AM_CMP_FILTEREQUAL = "tceav.am.cmp.filterEqual";
    private static final int PROPERTY_AM_CMP_FILTEREQUAL_DEFAULT = NamedRuleComponent.COMPARE_HIDE_INDEX;
    private static int amCmpFilterNotEqual;
    private static final String PROPERTY_AM_CMP_FILTERNOTEQUAL = "tceav.am.cmp.filterNotEqual";
    private static final int PROPERTY_AM_CMP_FILTERNOTEQUAL_DEFAULT = NamedRuleComponent.COMPARE_SHOW_INDEX;
    private static int amCmpFilterNotFound;
    private static final String PROPERTY_AM_CMP_FILTERNOTFOUND = "tceav.am.cmp.filterNotFound";
    private static final int PROPERTY_AM_CMP_FILTERNOTFOUND_DEFAULT = NamedRuleComponent.COMPARE_SHOW_INDEX;
    private static int[] amCmpACLSort;
    private static final String PROPERTY_AM_CMP_ACLSORT = "tceav.am.cmp.aclSort";
    private static final String PROPERTY_AM_CMP_ACLSORT_DEFAULT = "3,0,2";
    private static boolean amCmpACLSortAscending;
    private static final String PROPERTY_AM_CMP_ACLSORTASCENDING = "tceav.am.cmp.aclSortAscending";
    private static final boolean PROPERTY_AM_CMP_ACLSORTASCENDING_DEFAULT = true;
    
    
    private static Properties property = new Properties();
    
    /** Creates a new instance of Settings */
    public Settings() {
        
    }
    
    public static void load() throws Exception {
        File path = new File(System.getenv("USERPROFILE"),".TcEAV");
        if(!path.exists())
            path.mkdir();
        File file = new File(path,"TcEAdminView.cfg");
        if(file.exists()) {
            FileInputStream fis =  new FileInputStream(file);
            property.load(fis);
            fis.close();
        }
        
        setVersion(
                getPropertyAsString(
                PROPERTY_VERSION,
                PROPERTY_VERSION_DEFAULT));
        
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
        
        
        setCompareMode(
                getPropertyAsString(
                PROPERTY_COMPAREMODE,
                PROPERTY_COMPAREMODE_DEFAULT));
        
        
        setAmCmpSyncSelection(
                getPropertyAsBoolean(
                PROPERTY_AM_CMP_SYNCSELECTION,
                PROPERTY_AM_CMP_SYNCSELECTION_DEFAULT));
        setAmCmpRuleTab(
                getPropertyAsInt(
                PROPERTY_AM_CMP_ACLTAB,
                PROPERTY_AM_CMP_ACLTAB_DEFAULT));
        setAmCmpFilterEqual(
                getPropertyAsInt(
                PROPERTY_AM_CMP_FILTEREQUAL,
                PROPERTY_AM_CMP_FILTEREQUAL_DEFAULT));
        setAmCmpFilterNotEqual(
                getPropertyAsInt(
                PROPERTY_AM_CMP_FILTERNOTEQUAL,
                PROPERTY_AM_CMP_FILTERNOTEQUAL_DEFAULT));
        setAmCmpFilterNotFound(
                getPropertyAsInt(
                PROPERTY_AM_CMP_FILTERNOTFOUND,
                PROPERTY_AM_CMP_FILTERNOTFOUND_DEFAULT));
        setAmCmpDisplayMode(
                getPropertyAsString(
                PROPERTY_AM_CMP_DISPLAYMODE,
                PROPERTY_AM_CMP_DISPLAYMODE_DEFAULT));
        setAmCmpRuleSort(
                getPropertyAsIntArray(
                PROPERTY_AM_CMP_ACLSORT,
                PROPERTY_AM_CMP_ACLSORT_DEFAULT));
        setAmCmpRuleSortAscending(
                getPropertyAsBoolean(
                PROPERTY_AM_CMP_ACLSORTASCENDING,
                PROPERTY_AM_CMP_ACLSORTASCENDING_DEFAULT));
        
        
        setAmLoadPath(
                getPropertyAsString(
                PROPERTY_AMLOADPATH,
                PROPERTY_AMLOADPATH_DEFAULT));
        setAmSplitLocation(
                getPropertyAsInt(
                PROPERTY_AM_SPLITLOCATION,
                PROPERTY_AM_SPLITLOCATION_DEFAULT));
        setAmRuleSortAscending(
                getPropertyAsBoolean(
                PROPERTY_AM_ACLSORTASCENDING,
                PROPERTY_AM_ACLSORTASCENDING_DEFAULT));
        setAmRuleTab(
                getPropertyAsInt(
                PROPERTY_AM_ACLTAB,
                PROPERTY_AM_ACLTAB_DEFAULT));
        setAmRuleSort(
                getPropertyAsIntArray(
                PROPERTY_AM_ACLSORT,
                PROPERTY_AM_ACLSORT_DEFAULT));
        setAmSyncSelection(
                getPropertyAsBoolean(
                PROPERTY_AM_SYNCSELECTION,
                PROPERTY_AM_SYNCSELECTION_DEFAULT));
        
        
        setPmLoadPath(
                getPropertyAsString(
                PROPERTY_PMLOADPATH,
                PROPERTY_PMLOADPATH_DEFAULT));
        setPmSplitLocation(
                getPropertyAsInt(
                PROPERTY_PM_SPLITLOCATION,
                PROPERTY_PM_SPLITLOCATION_DEFAULT));
        
        
        setPmTblStrictArgument(
                getPropertyAsBoolean(
                PROPERTY_PM_TBL_STRICTARUMENT,
                PROPERTY_PM_TBL_STRICTARUMENT_DEFAULT));
        setPmTblMultiSheet(
                getPropertyAsBoolean(
                PROPERTY_PM_TBL_MULTISHEET,
                PROPERTY_PM_TBL_MULTISHEET_DEFAULT));
        setPmTblShowActions(
                getPropertyAsBoolean(
                PROPERTY_PM_TBL_SHOWACTIONS,
                PROPERTY_PM_TBL_SHOWACTIONS_DEFAULT));
        setPmTblIncludeIndents(
                getPropertyAsBoolean(
                PROPERTY_PM_TBL_INCLUDEINDENTS,
                PROPERTY_PM_TBL_INCLUDEINDENTS_DEFAULT));
        setPmTblDatabaseMode(
                getPropertyAsBoolean(
                PROPERTY_PM_TBL_DATABASEMODE,
                PROPERTY_PM_TBL_DATABASEMODE_DEFAULT));
        
    }
    
    public static void store() throws Exception  {
        property.setProperty(
                PROPERTY_VERSION,
                ResourceStrings.getVersion());
        property.setProperty(
                PROPERTY_USERINTERFACE,
                getUserInterface());
        property.setProperty(
                PROPERTY_SAVESETTINGSONEXIT,
                Boolean.toString(isSaveSettingsOnExit()));
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
                PROPERTY_AM_CMP_SYNCSELECTION,
                Boolean.toString(isAmCmpSyncSelection()));
        property.setProperty(
                PROPERTY_AM_CMP_ACLTAB,
                Integer.toString(getAmCmpRuleTab()));
        property.setProperty(
                PROPERTY_COMPAREMODE,
                getCompareMode());
        property.setProperty(
                PROPERTY_AM_CMP_FILTEREQUAL,
                Integer.toString(getAmCmpFilterEqual()));
        property.setProperty(
                PROPERTY_AM_CMP_FILTERNOTEQUAL,
                Integer.toString(getAmCmpFilterNotEqual()));
        property.setProperty(
                PROPERTY_AM_CMP_FILTERNOTFOUND,
                Integer.toString(getAmCmpFilterNotFound()));
        property.setProperty(
                PROPERTY_AM_CMP_DISPLAYMODE,
                getAmCmpDisplayMode());
        property.setProperty(
                PROPERTY_AM_CMP_ACLSORT,
                convertIntArrayToString(getAmCmpRuleSort()));
        property.setProperty(
                PROPERTY_AM_CMP_ACLSORTASCENDING,
                Boolean.toString(isAmCmpRuleSortAscending()));
        
        
        property.setProperty(
                PROPERTY_AMLOADPATH,
                getAmLoadPath());
        property.setProperty(
                PROPERTY_AM_SPLITLOCATION,
                Integer.toString(getAmSplitLocation()));
        property.setProperty(
                PROPERTY_AM_ACLSORTASCENDING,
                Boolean.toString(isAmRuleSortAscending()));
        property.setProperty(
                PROPERTY_AM_ACLTAB,
                Integer.toString(getAmRuleTab()));
        property.setProperty(
                PROPERTY_AM_ACLSORT,
                convertIntArrayToString(getAmRuleSort()));
        property.setProperty(
                PROPERTY_AM_SYNCSELECTION,
                Boolean.toString(isAmSyncSelection()));
        
        
        property.setProperty(
                PROPERTY_PMLOADPATH,
                getPmLoadPath());
        property.setProperty(
                PROPERTY_PM_SPLITLOCATION,
                Integer.toString(getPmSplitLocation()));
        
        
        property.setProperty(
                PROPERTY_PM_TBL_STRICTARUMENT,
                Boolean.toString(isPmTblStrictArgument()));
        property.setProperty(
                PROPERTY_PM_TBL_MULTISHEET,
                Boolean.toString(isPmTblMultiSheet()));
        property.setProperty(
                PROPERTY_PM_TBL_SHOWACTIONS,
                Boolean.toString(isPmTblShowActions()));
        property.setProperty(
                PROPERTY_PM_TBL_INCLUDEINDENTS,
                Boolean.toString(isPmTblIncludeIndents()));
        property.setProperty(
                PROPERTY_PM_TBL_DATABASEMODE,
                Boolean.toString(isPmTblDatabaseMode()));
        
        
        File path = new File(System.getenv("USERPROFILE"),".TcEAV");
        if(!path.exists())
            path.mkdir();
        File file = new File(path,"TcEAdminView.cfg");
        
        if(file.exists())
            if(!file.canWrite())
                return;
        
        if(path.canWrite()) {
            FileOutputStream fos = new FileOutputStream(file);
            property.store(fos, ResourceStrings.getVersion());
            fos.close();
        }
    }
    
    public static void setVersion(String version) {
        Settings.version = version;
    }
    
    public static int compareVersion() {
        return version.compareToIgnoreCase(ResourceStrings.getVersion());
    }
    
    public static String getCompareMode() {
        return compareMode;
    }
    
    public static void setCompareMode(String compareMode) {
        Settings.compareMode = compareMode;
    }
    
    public static int getAmCmpRuleTab() {
        return amCmpACLTab;
    }
    
    public static void setAmCmpRuleTab(int amCmpACLTab) {
        Settings.amCmpACLTab = amCmpACLTab;
    }
    
    public static void setAmCmpSyncSelection(boolean amCmpSyncSelection) {
        Settings.amCmpSyncSelection = amCmpSyncSelection;
    }
    
    public static boolean isAmCmpSyncSelection() {
        return amCmpSyncSelection;
    }
    
    public static String getAmCmpDisplayMode() {
        return amCmpDisplayMode;
    }
    
    public static void setAmCmpDisplayMode(String amCmpDisplayMode) {
        Settings.amCmpDisplayMode = amCmpDisplayMode;
    }
    
    public static int getAmCmpFilterEqual() {
        return  amCmpFilterEqual;
    }
    
    public static void setAmCmpFilterEqual(int amCmpFilterEqual) {
        Settings.amCmpFilterEqual = amCmpFilterEqual;
    }
    
    public static int getAmCmpFilterNotEqual() {
        return  amCmpFilterNotEqual;
    }
    
    public static void setAmCmpFilterNotEqual(int amCmpFilterNotEqual) {
        Settings.amCmpFilterNotEqual = amCmpFilterNotEqual;
    }
    
    public static int getAmCmpFilterNotFound() {
        return  amCmpFilterNotFound;
    }
    
    public static void setAmCmpFilterNotFound(int amCmpFilterNotFound) {
        Settings.amCmpFilterNotFound = amCmpFilterNotFound;
    }
    
    public static int[] getAmCmpRuleSort() {
        return amCmpACLSort;
    }
    
    public static void setAmCmpRuleSort(int[] amCmpACLSort) {
        Settings.amCmpACLSort = amCmpACLSort;
    }
    
    public static boolean isAmCmpRuleSortAscending() {
        return amCmpACLSortAscending;
    }
    
    public static void setAmCmpRuleSortAscending(boolean amCmpACLSortAscending) {
        Settings.amCmpACLSortAscending = amCmpACLSortAscending;
    }
    
    
    
    public static String getAmLoadPath() {
        return amLoadPath;
    }
    
    public static void setAmLoadPath(String amLoadPath) {
        Settings.amLoadPath = amLoadPath;
    }
    
    public static String getPmLoadPath() {
        return pmLoadPath;
    }
    
    public static void setPmLoadPath(String pmLoadPath) {
        Settings.pmLoadPath = pmLoadPath;
    }
    
    public static String getUserInterface() {
        return userInterface;
    }
    
    public static void setUserInterface(String userInterface) {
        Settings.userInterface = userInterface;
    }
    
    public static boolean isSaveSettingsOnExit() {
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
    
    public static int getAmSplitLocation(){
        return amSplitLocation;
    }
    
    public static void setAmSplitLocation(int amSplitLocation) {
        if(compareVersion() != 0)
            Settings.amSplitLocation = (int)(frameSizeY*0.58); 
        else if(amSplitLocation == -1)
            Settings.amSplitLocation = (int)(frameSizeY*0.58);
        else
            Settings.amSplitLocation = amSplitLocation;
    }
    
    public static int[] getAmRuleSort() {
        return amACLSort;
    }
    
    public static void setAmRuleSort(int[] amACLSort) {
        Settings.amACLSort = amACLSort;
    }
    
    public static boolean isAmRuleSortAscending() {
        return amACLSortAscending;
    }
    
    public static void setAmRuleSortAscending(boolean amACLSortAscending) {
        Settings.amACLSortAscending = amACLSortAscending;
    }
    
    public static int getAmRuleTab() {
        return amACLTab;
    }
    
    public static void setAmRuleTab(int amACLTab) {
        Settings.amACLTab = amACLTab;
    }
    
    public static void setAmSyncSelection(boolean amSyncSelection) {
        Settings.amSyncSelection = amSyncSelection;
    }
    
    public static boolean isAmSyncSelection() {
        return amSyncSelection;
    }
    
    public static int getPmSplitLocation(){
        return pmSplitLocation;
    }
    
    public static void setPmSplitLocation(int pmSplitLocation) {
        if(compareVersion() != 0)
            Settings.pmSplitLocation = (int)(frameSizeY*0.5);
        else if(pmSplitLocation == -1)
            Settings.pmSplitLocation = (int)(frameSizeY*0.5);
        else
            Settings.pmSplitLocation = pmSplitLocation;
    }
    
    
    
    
    public static boolean isPmTblDatabaseMode() {
        return pmTblDatabaseMode;
    }
    
    public static void setPmTblDatabaseMode(boolean pmTblDatabaseMode) {
        Settings.pmTblDatabaseMode = pmTblDatabaseMode;
    }
    
    public static boolean isPmTblIncludeIndents() {
        return pmTblIncludeIndents;
    }
    
    public static void setPmTblIncludeIndents(boolean pmTblIncludeIndents) {
        Settings.pmTblIncludeIndents = pmTblIncludeIndents;
    }
    
    public static boolean isPmTblMultiSheet() {
        return pmTblMultiSheet;
    }
    
    public static void setPmTblMultiSheet(boolean pmTblMultiSheet) {
        Settings.pmTblMultiSheet = pmTblMultiSheet;
    }
    
    public static boolean isPmTblShowActions() {
        return pmTblShowActions;
    }
    
    public static void setPmTblShowActions(boolean pmTblShowActions) {
        Settings.pmTblShowActions = pmTblShowActions;
    }
    
    public static boolean isPmTblStrictArgument() {
        return pmTblStrictArgument;
    }
    
    public static void setPmTblStrictArgument(boolean pmTblStrictArgument) {
        Settings.pmTblStrictArgument = pmTblStrictArgument;
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
