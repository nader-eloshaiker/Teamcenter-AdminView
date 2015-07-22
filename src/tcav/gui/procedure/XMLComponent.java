/*
 * XMLComponent.java
 *
 * Created on 8 February 2008, 14:00
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tcav.gui.procedure;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import tcav.gui.*;
import tcav.procedure.plmxmlpdm.base.IdBase;
/**
 *
 * @author nzr4dl
 */
public class XMLComponent extends JComponent {
    
    /** Creates a new instance of XMLComponent */
    private JTableAdvanced table;
    
    public XMLComponent() {
        super();
        
        table = new JTableAdvanced(new XMLTableModel());
        table.getTableHeader().setReorderingAllowed(false);
        JScrollPane scrolltable = new JScrollPane();
        scrolltable.setPreferredSize(new Dimension(200,220));
        scrolltable.setBorder(new BevelBorder(BevelBorder.LOWERED));
        scrolltable.getViewport().add(table);
        
        this.setLayout(new BorderLayout(GUIutilities.GAP_COMPONENT,GUIutilities.GAP_COMPONENT));
        this.setBorder(new CompoundBorder(
                new TitledBorder(new EtchedBorder(),"Details"),
                new EmptyBorder(GUIutilities.GAP_MARGIN,GUIutilities.GAP_MARGIN,GUIutilities.GAP_MARGIN,GUIutilities.GAP_MARGIN)));
        this.add("Center",scrolltable);
        
    }
    
    public JTableAdvanced getTable() {
        return table;
    }
    
    public void updateTable(IdBase procedure){
        table.setModel(new XMLTableModel(procedure));
        GUIutilities.packColumns(table, 2);
    }
    
}
