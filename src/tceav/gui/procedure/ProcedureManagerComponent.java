/*
 * ProcedureManagerComponent.java
 *
 * Created on 20 July 2007, 11:46
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package tceav.gui.procedure;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.border.BevelBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import tceav.gui.tools.GUIutilities;
import tceav.manager.procedure.ProcedureManager;
import tceav.gui.procedure.tabulate.TabulateComponent;
import tceav.manager.procedure.plmxmlpdm.base.IdBase;
import tceav.Settings;
import tceav.gui.AdminViewFrame;
import tceav.gui.TabbedPanel;
import tceav.resources.ImageEnum;
import tceav.resources.ResourceLoader;

/**
 *
 * @author nzr4dl
 */
public class ProcedureManagerComponent extends TabbedPanel {

    private AdminViewFrame parentFrame;
    private ProcedureManager pm;
    private JSplitPane splitPane1;
    private ActionComponent actionComponent;
    private AttributeComponent attributeComponent;
    private PropertiesComponent xmlComponent;
    private WorkflowComponent workflowComponent;
    private ProcessViewComponent processViewer;
    private DescriptionComponent descriptionComponent;

    /**
     * Creates a new instance of ProcedureManagerComponent
     */
    public ProcedureManagerComponent(AdminViewFrame parentFrame, ProcedureManager pm) {
        this.pm = pm;
        this.parentFrame = parentFrame;

        descriptionComponent = new DescriptionComponent();
        processViewer = new ProcessViewComponent();

        workflowComponent = new WorkflowComponent(parentFrame, pm);
        workflowComponent.getTree().addTreeSelectionListener(new TreeSelectionListener() {

            public void valueChanged(TreeSelectionEvent tse) {
                if (tse.isAddedPath(tse.getPath())) {
                    IdBase procedure = (IdBase) tse.getPath().getLastPathComponent();
                    actionComponent.updateTree(procedure);
                    attributeComponent.updateTree(procedure);
                    xmlComponent.updateTable(procedure);
                    processViewer.setWorkflowTemplate(procedure);
                    descriptionComponent.setDescription(procedure);
                    for (int i = 0; i < processViewer.getSubComponentSize(); i++) {
                        if (i == 0 || i == processViewer.getSubComponentSize() - 1) {
                            if (processViewer.getTask().getParentTaskTemplateRef() != null) {
                                processViewer.getSubComponent(i).addMouseListener(new ProcessTaskMouseAction(workflowComponent.getTree(), tse.getPath()));
                            }
                        } else {
                            processViewer.getSubComponent(i).addMouseListener(new ProcessTaskMouseAction(workflowComponent.getTree(), tse.getPath(), processViewer.getSubComponent(i)));
                        }
                    }
                }
            }
        });


        actionComponent = new ActionComponent(parentFrame, pm);
        actionComponent.getTree().addTreeSelectionListener(new TreeSelectionListener() {

            public void valueChanged(TreeSelectionEvent e) {
                if (e.isAddedPath(e.getPath())) {
                    IdBase procedure = (IdBase) e.getPath().getLastPathComponent();
                    xmlComponent.updateTable(procedure);
                }
            }
        });

        attributeComponent = new AttributeComponent(parentFrame, pm);
        attributeComponent.getTree().addTreeSelectionListener(new TreeSelectionListener() {

            public void valueChanged(TreeSelectionEvent e) {
                if (e.isAddedPath(e.getPath())) {
                    IdBase procedure = (IdBase) e.getPath().getLastPathComponent();
                    xmlComponent.updateTable(procedure);
                }
            }
        });

        xmlComponent = new PropertiesComponent();

        workflowComponent.setPreferredSize(new Dimension(310,310));
        attributeComponent.setPreferredSize(new Dimension(310,310));

        JPanel panelRight = new JPanel();
        panelRight.setLayout(new BorderLayout());
        panelRight.add(GUIutilities.createPanelMargined(attributeComponent), BorderLayout.WEST);
        panelRight.add(GUIutilities.createPanelMargined(actionComponent), BorderLayout.CENTER);

        JPanel panelLeft = new JPanel();
        panelLeft.setLayout(new BorderLayout());
        panelLeft.add(GUIutilities.createPanelMargined(workflowComponent), BorderLayout.CENTER);
        panelLeft.add(GUIutilities.createPanelMargined(descriptionComponent), BorderLayout.SOUTH);

        splitPane1 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, panelRight, GUIutilities.createPanelMargined(processViewer));
        splitPane1.setResizeWeight(1);
        splitPane1.setBorder(null);
        splitPane1.setDividerLocation(Settings.getPmSplitLocation());
        splitPane1.setOneTouchExpandable(true);
        ((BasicSplitPaneUI) splitPane1.getUI()).getDivider().addComponentListener(new ComponentAdapter() {

            @Override
            public void componentMoved(ComponentEvent e) {
                Settings.setPmSplitLocation(splitPane1.getDividerLocation());
            }
        });

        this.setLayout(new BorderLayout());
        this.add(panelLeft, BorderLayout.WEST);
        this.add(splitPane1, BorderLayout.CENTER);

    }

    @Override
    public void updateUI() {
        super.updateUI();
        if (attributeComponent != null && workflowComponent != null) {
            attributeComponent.setPreferredSize(workflowComponent.getPreferredSize());
        }
    }

    public String getTitle() {
        return pm.getFile().getName();
    }
    private ImageIcon iconProcedure;

    public ImageIcon getIcon() {
        if (iconProcedure == null) {
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

        if (toolBar != null) {
            return toolBar;
        }
        JButton buttonXML = new JButton("Task Properties");
        buttonXML.setToolTipText("View Task propeties");
        buttonXML.setOpaque(false);
        buttonXML.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                xmlComponent.show(parentFrame);
            }
        });

        JButton buttonTabulate = new JButton("Tabulate Procedures");
        buttonTabulate.setToolTipText("Show procedures/handlers in a spreadsheet view");
        buttonTabulate.setOpaque(false);
        buttonTabulate.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
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
        if (statusBar != null) {
            return statusBar;
        }
        JLabel textFileLabel = new JLabel(" Path:");
        JLabel textFile = new JLabel(" " + pm.getFile().getParent() + " ");
        textFile.setBorder(new BevelBorder(BevelBorder.LOWERED));
        JPanel panelFile = new JPanel();
        panelFile.setLayout(new BorderLayout());
        panelFile.add(textFileLabel, BorderLayout.WEST);
        panelFile.add(textFile, BorderLayout.CENTER);

        JLabel textSchemaLabel = new JLabel("  Schema:");
        JLabel textSchema = new JLabel(" " + pm.getPLMXML().getSchemaVersion() + " ");
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
        JLabel textAuthor = new JLabel(" " + pm.getPLMXML().getAuthor() + " ");
        textAuthor.setBorder(new BevelBorder(BevelBorder.LOWERED));
        JPanel panelAuthor = new JPanel();
        panelAuthor.setLayout(new BorderLayout());
        panelAuthor.add(textAuthorLabel, BorderLayout.WEST);
        panelAuthor.add(textAuthor, BorderLayout.CENTER);

        JLabel textDateLabel = new JLabel("  Date:");
        JLabel textDate = new JLabel(" " + pm.getPLMXML().getDate() + " ");
        textDate.setBorder(new BevelBorder(BevelBorder.LOWERED));
        JPanel panelDate = new JPanel();
        panelDate.setLayout(new BorderLayout());
        panelDate.add(textDateLabel, BorderLayout.WEST);
        panelDate.add(textDate, BorderLayout.CENTER);

        JLabel textTimeLabel = new JLabel("  Time:");
        JLabel textTime = new JLabel(" " + pm.getPLMXML().getTime() + " ");
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
