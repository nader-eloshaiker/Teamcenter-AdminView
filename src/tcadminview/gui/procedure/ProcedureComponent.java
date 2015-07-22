/*
 * ProcedureComponent.java
 *
 * Created on 20 July 2007, 11:46
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tcadminview.gui.procedure;

import tcadminview.gui.*;
import tcadminview.xml.DOMUtil;
import tcadminview.utils.GUITools;
import tcadminview.procedure.*;
import tcadminview.plmxmlpdm.classtype.WorkflowTemplateClassificationEnum;
import tcadminview.plmxmlpdm.type.WorkflowTemplateType;
import org.xml.sax.InputSource;
import javax.swing.border.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.util.*;
/**
 *
 * @author nzr4dl
 */
public class ProcedureComponent extends JPanel{
    
    protected JTable tableDepTaskTemplateRef;
    protected JTable tableWorkflowTemplatesProcess;
    protected JTable tableWorkflowTemplatesSub;
    protected JTable tableActionsRef;
    protected JTable tableSubTemplateRef;
    protected JTree treeWorkflowProcess;
    
    protected ProcedureManager pm;
    
    protected JTree treeXML;
    protected XMLTreeModel treeModelXML;
    
    private DOMUtil domUtil;
    
    /**
     * Creates a new instance of ProcedureComponent
     */
    public ProcedureComponent(InputSource is) {
        try {
            domUtil = new DOMUtil(is);
            
        } catch (Exception exc) {
            System.err.println("Error reading XML: " + exc);
        }
        
        pm = new ProcedureManager(domUtil.getRootNode());
        /*
        treeModelXML = new XMLTreeModel(domUtil.getRootNode());
        treeXML = new JTree(treeModelXML);
        treeXML.setCellRenderer(new XMLTreeCellRenderer());
        JScrollPane scrollTreeXML = new JScrollPane();
        scrollTreeXML.setPreferredSize(new Dimension(550,340));
        scrollTreeXML.setBorder(new BevelBorder(BevelBorder.LOWERED));
        scrollTreeXML.getViewport().add(treeXML);
        JPanel panelDebug = new JPanel();
        panelDebug.setLayout(new GridLayout(1,1,GUITools.GAP_COMPONENT,GUITools.GAP_COMPONENT));
        panelDebug.setBorder(new TitledBorder(new EtchedBorder(),"PLMXML Debug Window"));
        panelDebug.add(GUITools.createPanelMargined(scrollTreeXML));
        */
        
        // Workflow Process Tree
        treeWorkflowProcess = new JTree(ProcedureNodeBuilder.buildProcessNodes(pm));
        treeWorkflowProcess.setCellRenderer(new WorkflowTreeCellRenderer());
        JScrollPane scrollTreeWorkflowProcess = new JScrollPane();
        //scrollTreeWorkflowProcess.setPreferredSize(new Dimension(600,500));
        scrollTreeWorkflowProcess.setBorder(new BevelBorder(BevelBorder.LOWERED));
        scrollTreeWorkflowProcess.getViewport().add(treeWorkflowProcess);
        JPanel panelTreeWorkflowProcess = new JPanel();
        panelTreeWorkflowProcess.setLayout(new BorderLayout(GUITools.GAP_COMPONENT,GUITools.GAP_COMPONENT));
        panelTreeWorkflowProcess.setBorder(new TitledBorder(new EtchedBorder(),"Workflow Templates: PROCESS"));
        panelTreeWorkflowProcess.add(GUITools.createPanelMargined(scrollTreeWorkflowProcess));

        
        TableColumn column;
        
        // Process Workflows
        WorkflowTemplateTableModel wtProcessModel = new WorkflowTemplateTableModel(pm.getWorkflowTemplates(), WorkflowTemplateClassificationEnum.PROCESS);
        tableWorkflowTemplatesProcess = new JTable(wtProcessModel);
        tableWorkflowTemplatesProcess.setRowSelectionAllowed(true);
        tableWorkflowTemplatesProcess.setSelectionMode(0);
        tableWorkflowTemplatesProcess.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
        tableWorkflowTemplatesProcess.doLayout();
        tableWorkflowTemplatesProcess.setRowHeight(20);
        tableWorkflowTemplatesProcess.getSelectionModel().addListSelectionListener( new ListSelectionListener() {
           public void valueChanged(ListSelectionEvent e) {
               WorkflowTemplateList wtl = pm.getWorkflowTemplates();
               WorkflowTemplateClassificationEnum filterType = ((WorkflowTemplateTableModel)tableWorkflowTemplatesProcess.getModel()).getFilter();
               WorkflowTemplateType wt = wtl.get(wtl.getIndexesForClassification(filterType).get(tableWorkflowTemplatesProcess.getSelectedRow()));
               List<String> listRefs;
               listRefs = wt.getDependencyTaskTemplateRefs();
               tableDepTaskTemplateRef.setModel(new ReferencesTableModel(listRefs,pm));
               listRefs = wt.getActions();
               tableActionsRef.setModel(new ReferencesTableModel(listRefs,pm));
               listRefs = wt.getSubTemplateRefs();
               tableSubTemplateRef.setModel(new ReferencesTableModel(listRefs,pm));
           } 
        });
        for (int i=0; i<tableWorkflowTemplatesProcess.getColumnCount(); i++){
            column = tableWorkflowTemplatesProcess.getColumnModel().getColumn(i);
            if(column.getHeaderValue().equals("id")) {
                column.setResizable(false);
                column.setPreferredWidth(50);
                column.setMaxWidth(50);
                column.setMinWidth(50);
                column.setWidth(50);
            } else if(column.getHeaderValue().equals("iconKey")) {
                column.setCellRenderer(new WorkflowIconKeyRenderer());
                column.setResizable(false);
                column.setPreferredWidth(30);
                column.setMaxWidth(30);
                column.setMinWidth(30);
                column.setWidth(30);
            }
        }
        JScrollPane scrollWorkflowTemplatesProcess = new JScrollPane();
        //scrollWorkflowTemplatesProcess.setPreferredSize(new Dimension(600,500));
        scrollWorkflowTemplatesProcess.setBorder(new BevelBorder(BevelBorder.LOWERED));
        scrollWorkflowTemplatesProcess.getViewport().add(tableWorkflowTemplatesProcess);
        JPanel panelWorkflowTemplatesProcess = new JPanel();
        panelWorkflowTemplatesProcess.setLayout(new BorderLayout(GUITools.GAP_COMPONENT,GUITools.GAP_COMPONENT));
        panelWorkflowTemplatesProcess.setBorder(new TitledBorder(new EtchedBorder(),"Workflow Templates: PROCESS"));
        panelWorkflowTemplatesProcess.add(GUITools.createPanelMargined(scrollWorkflowTemplatesProcess));

        //Sub Workflows
        WorkflowTemplateTableModel wtSubModel = new WorkflowTemplateTableModel(pm.getWorkflowTemplates());
        tableWorkflowTemplatesSub = new JTable(wtSubModel);
        tableWorkflowTemplatesSub.setRowSelectionAllowed(true);
        tableWorkflowTemplatesSub.setSelectionMode(0);
        tableWorkflowTemplatesSub.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
        tableWorkflowTemplatesSub.doLayout();
        tableWorkflowTemplatesSub.setRowHeight(20);
        tableWorkflowTemplatesSub.getSelectionModel().addListSelectionListener( new ListSelectionListener() {
           public void valueChanged(ListSelectionEvent e) {
               WorkflowTemplateList wtl = pm.getWorkflowTemplates();
               WorkflowTemplateClassificationEnum filterType = ((WorkflowTemplateTableModel)tableWorkflowTemplatesSub.getModel()).getFilter();
               WorkflowTemplateType wt = wtl.get(wtl.getIndexesForClassification(filterType).get(tableWorkflowTemplatesSub.getSelectedRow()));
               List<String> listRefs;
               listRefs = wt.getDependencyTaskTemplateRefs();
               tableDepTaskTemplateRef.setModel(new ReferencesTableModel(listRefs,pm));
               listRefs = wt.getActions();
               tableActionsRef.setModel(new ReferencesTableModel(listRefs,pm));
               listRefs = wt.getSubTemplateRefs();
               tableSubTemplateRef.setModel(new ReferencesTableModel(listRefs,pm));
           } 
        });
        for (int i=0; i<tableWorkflowTemplatesSub.getColumnCount(); i++){
            column = tableWorkflowTemplatesSub.getColumnModel().getColumn(i);
            if(column.getHeaderValue().equals("id")) {
                column.setResizable(false);
                column.setPreferredWidth(50);
                column.setMaxWidth(50);
                column.setMinWidth(50);
                column.setWidth(50);
            } else if(column.getHeaderValue().equals("iconKey")) {
                column.setCellRenderer(new WorkflowIconKeyRenderer());
                column.setResizable(false);
                column.setPreferredWidth(30);
                column.setMaxWidth(30);
                column.setMinWidth(30);
                column.setWidth(30);
            }
        }
        JScrollPane scrollWorkflowTemplatesSub = new JScrollPane();
        //scrollWorkflowTemplatesSub.setPreferredSize(new Dimension(600,500));
        scrollWorkflowTemplatesSub.setBorder(new BevelBorder(BevelBorder.LOWERED));
        scrollWorkflowTemplatesSub.getViewport().add(tableWorkflowTemplatesSub);
        JPanel panelWorkflowTemplatesSub = new JPanel();
        panelWorkflowTemplatesSub.setLayout(new BorderLayout(GUITools.GAP_COMPONENT,GUITools.GAP_COMPONENT));
        panelWorkflowTemplatesSub.setBorder(new TitledBorder(new EtchedBorder(),"Sub Workflow Templates"));
        panelWorkflowTemplatesSub.add(GUITools.createPanelMargined(scrollWorkflowTemplatesSub));

        
        JPanel panelWorkflowTamplates = new JPanel();
        /*
        panelWorkflowTamplates.setLayout(new GridLayout(2,1,GUITools.GAP_COMPONENT,GUITools.GAP_COMPONENT));
        panelWorkflowTamplates.add(panelWorkflowTemplatesProcess);
        panelWorkflowTamplates.add(panelWorkflowTemplatesSub);
        */
        panelWorkflowTamplates.setLayout(new GridLayout(1,1,GUITools.GAP_COMPONENT,GUITools.GAP_COMPONENT));
        panelWorkflowTamplates.add(panelTreeWorkflowProcess);
        
        
        tableDepTaskTemplateRef = new JTable();
        tableDepTaskTemplateRef.setModel(new ReferencesTableModel(pm));
        JScrollPane scrollDepTaskTemplateRef = new JScrollPane();
        scrollDepTaskTemplateRef.setBorder(new BevelBorder(BevelBorder.LOWERED));
        scrollDepTaskTemplateRef.getViewport().add(tableDepTaskTemplateRef);
        JPanel panelDepTaskTemplateRef = new JPanel();
        panelDepTaskTemplateRef.setLayout(new BorderLayout(GUITools.GAP_COMPONENT,GUITools.GAP_COMPONENT));
        panelDepTaskTemplateRef.setBorder(new TitledBorder(new EtchedBorder(),"Dependancy Task Templates"));
        panelDepTaskTemplateRef.add(GUITools.createPanelMargined(scrollDepTaskTemplateRef));

        tableSubTemplateRef = new JTable();
        tableSubTemplateRef.setModel(new ReferencesTableModel(pm));
        JScrollPane scrollSubTemplateRef = new JScrollPane();
        scrollSubTemplateRef.setBorder(new BevelBorder(BevelBorder.LOWERED));
        scrollSubTemplateRef.getViewport().add(tableSubTemplateRef);
        JPanel panelSubTemplateRef = new JPanel();
        panelSubTemplateRef.setLayout(new BorderLayout(GUITools.GAP_COMPONENT,GUITools.GAP_COMPONENT));
        panelSubTemplateRef.setBorder(new TitledBorder(new EtchedBorder(),"Sub Templates"));
        panelSubTemplateRef.add(GUITools.createPanelMargined(scrollSubTemplateRef));

        tableActionsRef = new JTable();
        tableActionsRef.setModel(new ReferencesTableModel(pm));
        JScrollPane scrollActionsRef = new JScrollPane();
        scrollActionsRef.setBorder(new BevelBorder(BevelBorder.LOWERED));
        scrollActionsRef.getViewport().add(tableActionsRef);
        JPanel panelActionsRef = new JPanel();
        panelActionsRef.setLayout(new BorderLayout(GUITools.GAP_COMPONENT,GUITools.GAP_COMPONENT));
        panelActionsRef.setBorder(new TitledBorder(new EtchedBorder(),"Actions"));
        panelActionsRef.add(GUITools.createPanelMargined(scrollActionsRef));
        
        JPanel processRefsPanel = new JPanel();
        processRefsPanel.setLayout(new GridLayout(3,1,GUITools.GAP_COMPONENT,GUITools.GAP_COMPONENT));
        processRefsPanel.add(panelDepTaskTemplateRef);
        processRefsPanel.add(panelActionsRef);
        processRefsPanel.add(panelSubTemplateRef);
        
        JPanel processPanel = new JPanel();
        processPanel.setLayout(new BorderLayout(GUITools.GAP_COMPONENT,GUITools.GAP_COMPONENT));
        processPanel.add("Center", panelWorkflowTamplates);
        processPanel.add("East", processRefsPanel);
        
        
        JPanel panelSummary = new JPanel();
        panelSummary.setLayout(new GridLayout(2,5,GUITools.GAP_COMPONENT,GUITools.GAP_COMPONENT));
        panelSummary.add(new JLabel("AccessIntent: "+pm.getAccessIntents().size()));
        panelSummary.add(new JLabel("WorkflowActions: "+pm.getWorkflowActions().size()));
        panelSummary.add(new JLabel("WorkflowBusinessRules: "+pm.getWorkflowBusinessRules().size()));
        panelSummary.add(new JLabel("WorkflowBusinessRuleHandlers: "+pm.getWorkflowBusinessRuleHandlers().size()));
        panelSummary.add(new JLabel("WorkflowHandlers: "+pm.getWorkflowHandlers().size()));
        panelSummary.add(new JLabel("WorkflowSignoffProfiles: "+pm.getWorkflowSignoffProfiles().size()));
        panelSummary.add(new JLabel("WorkflowTemplates: "+pm.getWorkflowTemplates().size()));
        panelSummary.add(new JLabel("Organisations: "+pm.getOrganisations().size()));
        panelSummary.add(new JLabel("Roles: "+pm.getRoles().size()));
        panelSummary.add(new JLabel("Sites: "+pm.getSites().size()));
        
        JPanel panelWorkflowTeamplates = new JPanel();
        panelWorkflowTeamplates.setLayout(new BorderLayout(GUITools.GAP_COMPONENT,GUITools.GAP_COMPONENT));
        //panelWorkflowTeamplates.setBorder(new TitledBorder(new EtchedBorder(),"PLMXML WorkFlow"));
        panelWorkflowTeamplates.add("Center", processPanel);
        
        
        this.setLayout(new BorderLayout(GUITools.GAP_COMPONENT,GUITools.GAP_COMPONENT));
        this.add("Center",panelWorkflowTeamplates);
        this.add("South",GUITools.createPanelMargined(panelSummary));
        
    }
    
}
