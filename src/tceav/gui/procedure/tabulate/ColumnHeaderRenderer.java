/*
 * ColumnHeaderRenderer.java
 *
 * Created on 10 May 2008, 08:38
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package tceav.gui.procedure.tabulate;

import java.util.ArrayList;
import java.awt.Component;
import java.awt.Font;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import tceav.gui.tools.GUIutilities;
import tceav.gui.tools.RotatedTextIcon;
import javax.swing.SwingConstants;

/**
 *
 * @author NZR4DL
 */
public class ColumnHeaderRenderer implements TableCellRenderer {

    private DefaultTableCellRenderer cell;

    public DefaultTableCellRenderer getCell() {
        return cell;
    }

    public Component getTableCellRendererComponent(JTable table,
            Object value, boolean isSelected, boolean hasFocus, int row, int column) {

        TableCellRenderer temp = table.getTableHeader().getDefaultRenderer();
        cell = (DefaultTableCellRenderer) temp.getTableCellRendererComponent(table, null, isSelected, hasFocus, row, column);
        ColumnHeaderAdapter che = ((TableData) table.getModel()).getColumns().get(column);

        cell.setText(null);
        cell.setBorder(new CompoundBorder(
                cell.getBorder(),
                new EmptyBorder(GUIutilities.GAP_INSETS_HEADER)));

        cell.setVerticalAlignment(SwingConstants.BOTTOM);
        cell.setHorizontalAlignment(SwingConstants.CENTER);

        Font font = cell.getFont();
        RotatedTextIcon icon;

        ArrayList<Font> fonts = new ArrayList<Font>();
        ArrayList<String> str = new ArrayList<String>();

        if (che.isRuleHandler()) {
            fonts.add(font.deriveFont(Font.ITALIC));
            str.add(che.toStringRule());
        }

        for (int i = 0; i < che.getHandlerSize(); i++) {
            fonts.add(font.deriveFont(Font.BOLD));
            str.add(che.getHandler(i).getName());
            if (che.getHandler(i).hasArguments()) {
                fonts.add(font.deriveFont(Font.PLAIN));
                str.add(che.getHandler(i).toStringArguments());
            }
        }

        icon = new RotatedTextIcon(RotatedTextIcon.LEFT, fonts, str);

        cell.setIcon(icon);

        return cell;
    }
}
