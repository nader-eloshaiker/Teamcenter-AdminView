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
import tcadminview.procedure.ProcedureManager;
import org.xml.sax.InputSource;
import javax.swing.border.*;
import javax.swing.*;
import java.awt.*;
/**
 *
 * @author nzr4dl
 */
public class ProcedureComponent extends JPanel{
    
    protected JTree treeWorkFlow;
    protected XMLTreeModel treeModelWorkFlow;
    protected ProcedureManager pm;
    
    /**
     * Creates a new instance of ProcedureComponent
     */
    public ProcedureComponent(InputSource is) {
        try {
            treeModelWorkFlow = new XMLTreeModel(is);
            pm = new ProcedureManager(DOMUtil.getNode());
        } catch (Exception exc) {
            System.err.println("Error reading XML: " + exc);
        }
        
        treeWorkFlow = new JTree(treeModelWorkFlow);
        treeWorkFlow.setCellRenderer(new XMLTreeCellRenderer());
        
        JScrollPane treeScroll = new JScrollPane();
        treeScroll.setPreferredSize(new Dimension(550,340));
        treeScroll.setBorder(new BevelBorder(BevelBorder.LOWERED));
        treeScroll.getViewport().add(treeWorkFlow);

        JPanel panelDebug = new JPanel();
        panelDebug.setLayout(new GridLayout(1,1,GUITools.GAP_COMPONENT,GUITools.GAP_COMPONENT));
        panelDebug.setBorder(new TitledBorder(new EtchedBorder(),"PLMXML Debug Window"));
        panelDebug.add(GUITools.createPanelMargined(treeScroll));
        

        WorkflowTemplateTableModel wtModel = new WorkflowTemplateTableModel(pm.getWorkflowTemplates());
        JTable listWorkflowTemplates = new JTable(wtModel);
        listWorkflowTemplates.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
        listWorkflowTemplates.doLayout();
        
        
        
        JScrollPane scrollWorkflowTemplates = new JScrollPane();
        scrollWorkflowTemplates.setPreferredSize(new Dimension(600,500));
        scrollWorkflowTemplates.setBorder(new BevelBorder(BevelBorder.LOWERED));
        scrollWorkflowTemplates.getViewport().add(listWorkflowTemplates);
        
        JPanel panelSummary = new JPanel();
        panelSummary.setLayout(new GridLayout(10,1,GUITools.GAP_COMPONENT,GUITools.GAP_COMPONENT));
        panelSummary.add(new JLabel("AccessIntent                 : "+pm.getAccessIntents().size()));
        panelSummary.add(new JLabel("WorkflowActions              : "+pm.getWorkflowActions().size()));
        panelSummary.add(new JLabel("WorkflowBusinessRules        : "+pm.getWorkflowBusinessRules().size()));
        panelSummary.add(new JLabel("WorkflowBusinessRuleHandlers : "+pm.getWorkflowBusinessRuleHandlers().size()));
        panelSummary.add(new JLabel("WorkflowHandlers             : "+pm.getWorkflowHandlers().size()));
        panelSummary.add(new JLabel("WorkflowSignoffProfiles      : "+pm.getWorkflowSignoffProfiles().size()));
        panelSummary.add(new JLabel("WorkflowTemplates            : "+pm.getWorkflowTemplates().size()));
        panelSummary.add(new JLabel("Organisations                : "+pm.getOrganisations().size()));
        panelSummary.add(new JLabel("Roles                        : "+pm.getRoles().size()));
        panelSummary.add(new JLabel("Sites                        : "+pm.getSites().size()));
        
        JPanel panelWorkflowTeamplates = new JPanel();
        panelWorkflowTeamplates.setLayout(new BorderLayout(GUITools.GAP_COMPONENT,GUITools.GAP_COMPONENT));
        panelWorkflowTeamplates.setBorder(new TitledBorder(new EtchedBorder(),"PLMXML WorkFlow"));
        panelWorkflowTeamplates.add("Center", GUITools.createPanelMargined(scrollWorkflowTemplates));
        
        
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(GUITools.GAP_COMPONENT,GUITools.GAP_COMPONENT));
        //panel.setBorder(new TitledBorder(new EtchedBorder(),"PLMXML WorkFlow"));
        panel.add("Center",panelWorkflowTeamplates);
        panel.add("South",GUITools.createPanelMargined(panelSummary));
        
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelDebug, panel);
        splitPane.setDividerLocation(0.1);
        
        this.setLayout(new BorderLayout(GUITools.GAP_MARGIN,GUITools.GAP_MARGIN));
        this.setBorder(new EmptyBorder(GUITools.GAP_MARGIN,GUITools.GAP_MARGIN,GUITools.GAP_MARGIN,GUITools.GAP_MARGIN));
        this.add("Center", splitPane);
    }
    
}
