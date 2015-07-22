/*
 * CellRenderer.java
 *
 * Created on 11 May 2008, 13:28
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tceav.gui.procedure.tabulate;

import javax.swing.table.*;
import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import tceav.Settings;
import tceav.gui.TableShadedRenderer;
import tceav.manager.procedure.plmxmlpdm.classtype.WorkflowActionTypeEnum;
import tceav.resources.*;

/**
 *
 * @author NZR4DL
 */
public class CellRenderer extends SyncedRenderer implements TableCellRenderer {
    
    private static ImageIcon yesIcon;
    private static HashMap<String, SquareIcon> actionMap;
    
    static
    {
        try {
            yesIcon = ResourceLoader.getImage(ImageEnum.amYes);
        } catch (Exception e) {
            System.out.println("Couldn't load images: " + e);
        }
        
        WorkflowActionTypeEnum[] actionTypes = WorkflowActionTypeEnum.values();
        actionMap = new HashMap<String, SquareIcon>();
        for(WorkflowActionTypeEnum wa : actionTypes) {
            actionMap.put(wa.getName(), new SquareIcon(wa.getColor()));
        }
    }
    
    public CellRenderer(JTable oppositeTable) {
        super(oppositeTable);
    }
    
    
    public Component getTableCellRendererComponent(JTable table,
            Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        
        String s = (String)value;
        
        
        setHorizontalAlignment(SwingConstants.CENTER);
        setVerticalAlignment(SwingConstants.CENTER);
        
        if(s == null || s.equals("")) {
            setIcon(null);
            setToolTipText(null);
            setValue(null);
        } else {
            if(Settings.isPmTblShowActions()) {
                setValue(null);
                setToolTipText(s);
                setIcon(actionMap.get(s));
            } else /*if(s.equals("y"))*/ {
                setValue(null);
                setToolTipText(null);
                setIcon(yesIcon);
            }
        }
        
        return this;
    }
    
}
