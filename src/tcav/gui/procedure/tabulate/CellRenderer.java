/*
 * CellRenderer.java
 *
 * Created on 11 May 2008, 13:28
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package tcav.gui.procedure.tabulate;

import java.awt.Component;
import java.util.HashMap;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellRenderer;
import tcav.Settings;
import tcav.manager.procedure.plmxmlpdm.classtype.WorkflowActionTypeEnum;
import tcav.resources.ImageEnum;
import tcav.resources.ResourceLoader;

/**
 *
 * @author NZR4DL
 */
public class CellRenderer extends SyncedRenderer implements TableCellRenderer {

    private static ImageIcon yesIcon;
    private static final HashMap<String, SquareIcon> actionMap;
    

    static {
        try {
            yesIcon = ResourceLoader.getImage(ImageEnum.amYes);
        } catch (Exception e) {
            System.out.println("Couldn't load images: " + e);
        }

        WorkflowActionTypeEnum[] actionTypes = WorkflowActionTypeEnum.values();
        actionMap = new HashMap<>();
        for (WorkflowActionTypeEnum wa : actionTypes) {
            actionMap.put(wa.getName(), new SquareIcon(wa.getColor()));
        }
    }

    public CellRenderer(JTable oppositeTable) {
        super(oppositeTable);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table,
            Object value, boolean isSelected, boolean hasFocus, int row, int column) {

        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        String s = (String) value;


        setHorizontalAlignment(SwingConstants.CENTER);
        setVerticalAlignment(SwingConstants.CENTER);

        if (s == null || s.equals("")) {
            setIcon(null);
            setToolTipText(null);
            setValue(null);
        } else {
            if (Settings.isPmTblShowActions()) {
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
