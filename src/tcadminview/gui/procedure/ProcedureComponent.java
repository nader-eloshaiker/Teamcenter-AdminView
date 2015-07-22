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
import tcadminview.plmxmlpdm.classtype.*;
import tcadminview.plmxmlpdm.type.*;
import tcadminview.plmxmlpdm.TagTypeEnum;
import org.xml.sax.InputSource;
import javax.swing.border.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.tree.*;
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
        treeWorkflowProcess.setRowHeight(20);
        treeWorkflowProcess.addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent e) {
                TreePath path = e.getPath();
                DefaultMutableTreeNode  nodes = (DefaultMutableTreeNode)path.getLastPathComponent();
                WorkflowTreeItem wti = (WorkflowTreeItem)nodes.getUserObject();
                
                if(wti.getClassType() == null)
                    return;
                
                TagTypeEnum classType = wti.getClassType();
                switch(classType) {
                    case WorkflowTemplate:
                        WorkflowTemplateType wt = pm.getWorkflowTemplates().get(pm.getIdIndex(wti.getId()));
                        labelId.setText(wt.getId());
                        labelSignOffQuorum.setText(wt.getSignoffQuorum().toString());
                        labelClassification.setText(wt.getTemplateClassification().value());
                        labelShowProcessStage.setText(wt.isShowInProcessStage().toString());
                        labelObjectType.setText(wt.getObjectType());
                        labelParentTask.setText(wt.getParentTaskTemplateRef());
                        labelStage.setText(wt.getStage().value());
                        break;
                }
            }
        });
        JScrollPane scrollTreeWorkflowProcess = new JScrollPane();
        //scrollTreeWorkflowProcess.setPreferredSize(new Dimension(600,500));
        scrollTreeWorkflowProcess.setBorder(new BevelBorder(BevelBorder.LOWERED));
        scrollTreeWorkflowProcess.getViewport().add(treeWorkflowProcess);
        JPanel panelTreeWorkflowProcess = new JPanel();
        panelTreeWorkflowProcess.setLayout(new BorderLayout(GUITools.GAP_COMPONENT,GUITools.GAP_COMPONENT));
        panelTreeWorkflowProcess.setBorder(new TitledBorder(new EtchedBorder(),"Workflow Templates: PROCESS"));
        panelTreeWorkflowProcess.add(GUITools.createPanelMargined(scrollTreeWorkflowProcess));
        
        
        
        
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1,2,GUITools.GAP_COMPONENT,GUITools.GAP_COMPONENT));
        panel.add(panelTreeWorkflowProcess);
        panel.add(createWorkflowProcessPanel());
        
        
        
        this.setLayout(new BorderLayout(GUITools.GAP_COMPONENT,GUITools.GAP_COMPONENT));
        this.add("Center",GUITools.createPanelMargined(panel));
        
    }
    
    protected JLabel labelId = new JLabel("#");
    protected JLabel labelSignOffQuorum = new JLabel("#");
    protected JLabel labelClassification = new JLabel("#");
    protected JLabel labelShowProcessStage = new JLabel("#");
    protected JLabel labelObjectType = new JLabel("#");
    protected JLabel labelParentTask = new JLabel("#");
    protected JLabel labelStage = new JLabel("#");
    
    private JPanel createWorkflowProcessPanel() {
        
        JPanel panelInnerDetails = new JPanel();
        panelInnerDetails.setLayout(new GridLayout(7,2,GUITools.GAP_COMPONENT,GUITools.GAP_COMPONENT));
        panelInnerDetails.add(new JLabel("Id:"));
        panelInnerDetails.add(labelId);
        panelInnerDetails.add(new JLabel("Sign off Quorum:"));
        panelInnerDetails.add(labelSignOffQuorum);
        panelInnerDetails.add(new JLabel("Classification:"));
        panelInnerDetails.add(labelClassification);
        panelInnerDetails.add(new JLabel("Show in Process Stage:"));
        panelInnerDetails.add(labelShowProcessStage);
        panelInnerDetails.add(new JLabel("Object Type:"));
        panelInnerDetails.add(labelObjectType);
        panelInnerDetails.add(new JLabel("Parent Task:"));
        panelInnerDetails.add(labelParentTask);
        panelInnerDetails.add(new JLabel("Stage:"));
        panelInnerDetails.add(labelStage);
        JPanel panelDetails = new JPanel();
        panelDetails.setBorder(new TitledBorder(new EtchedBorder(),"Workflow Details"));
        panelDetails.setLayout(new BorderLayout(GUITools.GAP_COMPONENT,GUITools.GAP_COMPONENT));
        panelDetails.add("Center",GUITools.createPanelMargined(panelInnerDetails));
        
        JPanel panelUserData = new JPanel();
        panelUserData.setLayout(new BorderLayout(GUITools.GAP_COMPONENT,GUITools.GAP_COMPONENT));
        panelUserData.setBorder(new TitledBorder(new EtchedBorder(),"User Data"));
        //panelUserData.add("Center",GUITools.createPanelMargined(new JTable()));
        
        JPanel panelTaskDescription = new JPanel();
        panelTaskDescription.setLayout(new BorderLayout(GUITools.GAP_COMPONENT,GUITools.GAP_COMPONENT));
        panelTaskDescription.setBorder(new TitledBorder(new EtchedBorder(),"Task Description"));
        //panelTaskDescription.add("Center",GUITools.createPanelMargined(new JTable()));
        
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(GUITools.GAP_COMPONENT,GUITools.GAP_COMPONENT));
        panel.add("North",panelDetails);
        panel.add("Center",panelUserData);
        panel.add("South",panelTaskDescription);
        
        return panel;
    }
    
}
