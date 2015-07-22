/*
 * AccessRuleComponent.java
 *
 * Created on 25 January 2008, 13:19
 *
 * To change table template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tceav.gui.access;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.border.*;
import tceav.gui.tools.GUIutilities;
import tceav.gui.tools.table.JTableAdvanced;
import tceav.manager.access.AccessControlHeader;
import tceav.manager.access.AccessManager;
import tceav.manager.access.AccessRule;
import tceav.gui.*;
import tceav.resources.*;

/**
 *
 * @author NZR4DL
 */
public class AccessRuleComponent extends JPanel {
    
    private JTableAdvanced table;
    private AccessRuleTableModel tableModel;
    private AccessControlHeader header;
    private boolean compareMode;
    
    /**
     * Creates a new instance of AccessRuleComponent
     */
    public AccessRuleComponent(AccessManager am) {
        this(am, false);
    }
    
    public AccessRuleComponent(AccessManager am, boolean compareMode) {
        super();
        this.header = am.getAccessControlColumns();
        this.compareMode = compareMode;

        table = new JTableAdvanced();
        tableModel = new AccessRuleTableModel(header,new AccessRule());
        table.setModel(tableModel);
        table.setRowSelectionAllowed(true);
        table.getTableHeader().setReorderingAllowed(false);
        if(table.getRowHeight() < 18)
            table.setRowHeight(18);
        TableColumn column;
        for (int i=0; i<table.getColumnCount(); i++){
            column = table.getColumnModel().getColumn(i);
            column.setHeaderValue(tableModel.getColumn(i));
            column.setHeaderRenderer(new AccessRuleTableHearderRenderer());
            column.setCellRenderer(new AccessRuleTableCellRenderer(compareMode));
            if(i == 0 || i == 1) {
                column.setResizable(true);
                column.setPreferredWidth(80);
            } else {
                column.setResizable(false);
                column.setPreferredWidth(28);
                column.setMaxWidth(28);
                column.setMinWidth(28);
                column.setWidth(28);
            }
        }
        
        JScrollPane accessRuleComponentScroll = new JScrollPane();
        accessRuleComponentScroll.setPreferredSize(new Dimension(980,150));
        accessRuleComponentScroll.getViewport().add(table);
        accessRuleComponentScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        accessRuleComponentScroll.setBorder(new BevelBorder(BevelBorder.LOWERED));

        this.setLayout(new GridLayout(1,1));
        this.setBorder(new TitledBorder(new EtchedBorder(),"Access Control"));
        this.add(GUIutilities.createPanelMargined(accessRuleComponentScroll));
    }
    
    public JTableAdvanced getTable() {
        return table;
    }
    
    private AccessRule emptyAccessRule;
    
    public void updateTable() {
        if(emptyAccessRule == null)
            emptyAccessRule = new AccessRule();
        
        updateTable(emptyAccessRule);
    }
    
    public void updateTable(AccessRule ar) {
        tableModel = new AccessRuleTableModel(header,ar);
        table.setModel(tableModel);
        
        TableColumn column;
        for (int i=0; i<table.getColumnCount(); i++){
            column = table.getColumnModel().getColumn(i);
            column.setHeaderValue(tableModel.getColumn(i));
            column.setHeaderRenderer(new AccessRuleTableHearderRenderer());
            column.setCellRenderer(new AccessRuleTableCellRenderer(compareMode));
            if(i == 0 || i == 1) {
                column.setResizable(true);
                column.setPreferredWidth(80);
            } else {
                column.setResizable(false);
                column.setPreferredWidth(28);
                column.setMaxWidth(28);
                column.setMinWidth(28);
                column.setWidth(28);
            }
        }
    }

}
