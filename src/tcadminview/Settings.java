/*
 * Settings.java
 *
 * Created on 30 August 2007, 10:52
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tcadminview;

import java.awt.Toolkit;
import java.awt.*;
/**
 *
 * @author nzr4dl
 */
public class Settings {
    
    private static String loadPath = "";
    private static int frameSizeX = 1024;
    private static int frameSizeY = 740;
    
    private static int amSplitLocation = 420;
    private static int[] amNamedACLSort = new int[]{0,2};
    private static boolean amNamedACLSortAscending = true;
    private static int amNamedACLTab = 0;
    
    private static int pmSplitLocation1 = 500;
    private static int pmSplitLocation2 = 370;
    private static boolean pmExpandedView = true;

    /** static */
    static {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        frameSizeY = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().height;
        frameSizeX = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().width;
        amSplitLocation = (int)(frameSizeY*0.58);
        pmSplitLocation1 = (int)(frameSizeX*0.5);
        pmSplitLocation2 = (int)(frameSizeY-380);
    }
    /** Creates a new instance of Settings */
    public Settings() {
    }
    
    public static String getLoadPath(){
        return loadPath;
    }
    
    public static int getFrameSizeX() {
        return frameSizeX;
    }
    
    public static void setFrameSizeX(int frameSizeX) {
        Settings.frameSizeX = frameSizeX;
    }
    
    public static int getFrameSizeY() {
        return frameSizeY;
    }
    
    public static void setFrameSizeY(int frameSizeY) {
        Settings.frameSizeY = frameSizeY;
    }
    
    public static void setLoadPath(String loadPath) {
        Settings.loadPath = loadPath;
    }
    
    public static int getAMSplitLocation(){
        return amSplitLocation;
    }
    
    public static void setAMSplitLocation(int amSplitLocation) {
        Settings.amSplitLocation = amSplitLocation;
    }
    
    public static int[] getAMNamedACLSort() {
        return amNamedACLSort;
    }
    
    public static void setAMNamedACLSort(int[] amNamedACLSort) {
        Settings.amNamedACLSort = amNamedACLSort;
    }
    
    public static boolean getAMNamedACLSortAscending() {
        return amNamedACLSortAscending;
    }
    
    public static void setAMNamedACLSortAscending(boolean amNamedACLSortAscending) {
        Settings.amNamedACLSort = amNamedACLSort;
    }
    
    public static int getAMNamedACLTab() {
        return amNamedACLTab;
    }
    
    public static void setAMNamedACLTab(int amNamedACLTab) {
        Settings.amNamedACLTab = amNamedACLTab;
    }

    public static int getPMSplitLocation1(){
        return pmSplitLocation1;
    }
    
    public static void setPMSplitLocation1(int amSplitLocation1) {
        Settings.pmSplitLocation1 = pmSplitLocation1;
    }
    
    public static int getPMSplitLocation2(){
        return pmSplitLocation2;
    }
    
    public static void setPMSplitLocation2(int amSplitLocation2) {
        Settings.pmSplitLocation2 = pmSplitLocation2;
    }
    
    public static boolean getPMExpandedView() {
        return pmExpandedView;
    }

    public static void setPMExpandedView(boolean pmExpandedView) {
        Settings.pmExpandedView = pmExpandedView;
    }
}
