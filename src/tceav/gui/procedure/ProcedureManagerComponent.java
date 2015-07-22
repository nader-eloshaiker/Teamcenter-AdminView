/*
 * ProcedureManagerComponent.java
 *
 * Created on 20 July 2007, 11:46
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tceav.gui.procedure;

import tceav.manager.procedure.ProcedureManager;
import tceav.manager.AbstractManager;
import tceav.gui.procedure.tabulate.TabulateComponent;
import tceav.manager.procedure.plmxmlpdm.base.IdBase;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import javax.swing.tree.*;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import javax.swing.event.*;
import java.awt.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.io.File;
import tceav.Settings;
import tceav.gui.*;
import tceav.resources.*;

/**
 *
 * @author nzr4dl
 */
public class ProcedureManagerComponent extends TabbedPanel {
    
    private AdminViewFrame parentFrame;
    private ProcedureManager pm;
    private JSplitPane splitPane1;
    private ProcedureTreeModel modelProcedure;
    private ActionComponent actionComponent;
    private AttributeComponent attributeComponent;
    private XMLComponent xmlComponent;
    private ProcessComponent processComponent;
    
    
    /**
     * Creates a new instance of ProcedureManagerComponent
     */
    public ProcedureManagerComponent(AdminViewFrame parentFrame, ProcedureManager pm) {
        this.pm = pm;
        this.parentFrame = parentFrame;
        
        processComponent = new ProcessComponent(parentFrame, pm);
        processComponent.getTree().addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent e) {
                if(e.isAddedPath(e.getPath())) {
                    IdBase procedure = (IdBase)e.getPath().getLastPathComponent();
                    actionComponent.updateTree(procedure);
                    attributeComponent.updateTree(procedure);
                    xmlComponent.updateTable(procedure);
                }
            }
        });
        
        actionComponent = new ActionComponent(parentFrame, pm);
        actionComponent.getTree().addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent e) {
                if(e.isAddedPath(e.getPath())) {
                    IdBase procedure = (IdBase)e.getPath().getLastPathComponent();
                    processComponent.getTree().clearSelection();
                    attributeComponent.updateTree(procedure);
                    xmlComponent.updateTable(procedure);
                }
            }
        });
        
        attributeComponent = new AttributeComponent(parentFrame, pm);
        attributeComponent.getTree().addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent e) {
                if(e.isAddedPath(e.getPath())) {
                    IdBase procedure = (IdBase)e.getPath().getLastPathComponent();
                    processComponent.getTree().clearSelection();
                    actionComponent.getTree().clearSelection();
                    xmlComponent.updateTable(procedure);
                }
            }
        });
        
        xmlComponent = new XMLComponent();
        
        
        
        JPanel panelLeft = new JPanel();
        panelLeft.setLayout(new GridLayout(2,1,GUIutilities.GAP_COMPONENT,GUIutilities.GAP_COMPONENT));
        panelLeft.add(GUIutilities.createPanelMargined(processComponent));
        panelLeft.add(GUIutilities.createPanelMargined(actionComponent));
        
        JPanel panelRight = new JPanel();
        panelRight.setLayout(new GridLayout(1,1,GUIutilities.GAP_COMPONENT,GUIutilities.GAP_COMPONENT));
        panelRight.add("Center",GUIutilities.createPanelMargined(attributeComponent));
        //panelRight.add("South", GUIutilities.createPanelMargined(xmlComponent));
        
        splitPane1 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, panelLeft, panelRight);
        splitPane1.setResizeWeight(1.0);
        splitPane1.setBorder(null);
        splitPane1.setDividerLocation(Settings.getPmSplitLocation());
        splitPane1.setOneTouchExpandable(true);
        ((BasicSplitPaneUI)splitPane1.getUI()).getDivider().addComponentListener(new ComponentAdapter(){
            public void componentMoved(ComponentEvent e){
                Settings.setPmSplitLocation(splitPane1.getDividerLocation());
            }
        });
        
        this.setLayout(new BorderLayout());
        this.add("Center",splitPane1);
        
    }
    
    public String getTitle() {
        return pm.getFile().getName();
    }
    
    private ImageIcon iconProcedure;
    
    public ImageIcon getIcon() {
        if(iconProcedure == null) {
            try {
                iconProcedure = ResourceLoader.getImage(ImageEnum.pmWorkflow);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error Load Images", JOptionPane.ERROR_MESSAGE);
            }
        }
        return iconProcedure;
    }
    
    private JToolBar toolBar;
    
    public JToolBar getToolBar() {
        
        if(toolBar != null)
            return toolBar;
        
        JButton buttonXML = new JButton("XML Properties");
        buttonXML.setOpaque(false);
        buttonXML.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                xmlComponent.show(parentFrame);
            }
        });
        
        JButton buttonTabulate = new JButton("Tabulate Procedures");
        buttonTabulate.setOpaque(false);
        buttonTabulate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                TabulateComponent component = new TabulateComponent(pm, parentFrame);
                parentFrame.addTabbedPane(component);
            }
        });
        
        try {
            buttonTabulate.setIcon(ResourceLoader.getImage(ImageEnum.pmTabulate));
            buttonXML.setIcon(ResourceLoader.getImage(ImageEnum.pmXML));
        } catch (Exception ex) {
                JOptionPane.showMessageDialog(parentFrame, ex.getMessage(), "Error Load Images", JOptionPane.ERROR_MESSAGE);
            }
        
        toolBar = new JToolBar();
        toolBar.add(buttonXML);
        toolBar.add(buttonTabulate);
        
        return toolBar;
    }
    
    private JPanel statusBar;
    
    public JComponent getStatusBar() {
        if(statusBar != null)
            return statusBar;
        
        JLabel textFileLabel = new JLabel(" Path:");
        JLabel textFile = new JLabel(" "+pm.getFile().getParent()+" ");
        textFile.setBorder(new BevelBorder(BevelBorder.LOWERED));
        JPanel panelFile = new JPanel();
        panelFile.setLayout(new BorderLayout());
        panelFile.add(textFileLabel, BorderLayout.WEST);
        panelFile.add(textFile, BorderLayout.CENTER);
        
        JLabel textSchemaLabel = new JLabel("  Schema:");
        JLabel textSchema = new JLabel(" "+pm.getPLMXML().getSchemaVersion()+" ");
        textSchema.setBorder(new BevelBorder(BevelBorder.LOWERED));
        JPanel panelSchema = new JPanel();
        panelSchema.setLayout(new BorderLayout());
        panelSchema.add(textSchemaLabel, BorderLayout.WEST);
        panelSchema.add(textSchema, BorderLayout.CENTER);
        
        JPanel panelPathSchema = new JPanel();
        panelPathSchema.setLayout(new BorderLayout());
        panelPathSchema.add(panelFile, BorderLayout.WEST);
        panelPathSchema.add(panelSchema, BorderLayout.EAST);
        
        JLabel textAuthorLabel = new JLabel("  Author:");
        JLabel textAuthor = new JLabel(" "+pm.getPLMXML().getAuthor()+" ");
        textAuthor.setBorder(new BevelBorder(BevelBorder.LOWERED));
        JPanel panelAuthor = new JPanel();
        panelAuthor.setLayout(new BorderLayout());
        panelAuthor.add(textAuthorLabel, BorderLayout.WEST);
        panelAuthor.add(textAuthor, BorderLayout.CENTER);
        
        JLabel textDateLabel = new JLabel("  Date:");
        JLabel textDate = new JLabel(" "+pm.getPLMXML().getDate()+" ");
        textDate.setBorder(new BevelBorder(BevelBorder.LOWERED));
        JPanel panelDate = new JPanel();
        panelDate.setLayout(new BorderLayout());
        panelDate.add(textDateLabel, BorderLayout.WEST);
        panelDate.add(textDate, BorderLayout.CENTER);
        
        JLabel textTimeLabel = new JLabel("  Time:");
        JLabel textTime = new JLabel(" "+pm.getPLMXML().getTime()+" ");
        textTime.setBorder(new BevelBorder(BevelBorder.LOWERED));
        JPanel panelTime = new JPanel();
        panelTime.setLayout(new BorderLayout());
        panelTime.add(textTimeLabel, BorderLayout.WEST);
        panelTime.add(textTime, BorderLayout.CENTER);
        
        JPanel panelDateTime = new JPanel();
        panelDateTime.setLayout(new BorderLayout());
        panelDateTime.add(panelDate, BorderLayout.WEST);
        panelDateTime.add(panelTime, BorderLayout.EAST);
        
        
        statusBar = new JPanel();
        statusBar.setLayout(new BorderLayout());
        statusBar.add(panelPathSchema, BorderLayout.WEST);
        statusBar.add(panelAuthor, BorderLayout.CENTER);
        statusBar.add(panelDateTime, BorderLayout.EAST);
        
        return statusBar;
    }
    
    
}
