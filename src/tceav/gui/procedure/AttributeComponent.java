/*
 * AttributeComponent.java
 *
 * Created on 8 February 2008, 13:38
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tceav.gui.procedure;

import java.awt.BorderLayout;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.ToolTipManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.tree.TreeSelectionModel;
import tceav.gui.AdminViewFrame;
import tceav.gui.tools.GUIutilities;
import tceav.gui.tools.tree.JTreeAdvanced;
import tceav.gui.tools.tree.toolbar.TreeCopyProcedureAdaptor;
import tceav.gui.tools.tree.toolbar.TreeToolBar;
import tceav.manager.procedure.plmxmlpdm.base.IdBase;
import tceav.manager.procedure.ProcedureManager;

/**
 *
 * @author nzr4dl
 */
public class AttributeComponent extends JComponent {
    
    private JTreeAdvanced tree;
    private AdminViewFrame parentFrame;
    private TreeToolBar toolbar;
    
    public AttributeComponent(AdminViewFrame parentFrame, ProcedureManager pm) {
        super();
        this.parentFrame = parentFrame;
        
        tree = new JTreeAdvanced(new AttributeTreeData());
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.setLargeModel(true);
        tree.setCellRenderer(new AttributeRenderer());
        if(tree.getRowHeight() < 18)
            tree.setRowHeight(18);
        
        ToolTipManager.sharedInstance().registerComponent(tree);
        
        JScrollPane scrolltree = new JScrollPane();
        scrolltree.setBorder(new BevelBorder(BevelBorder.LOWERED));
        scrolltree.getViewport().add(tree);
        
        
        toolbar = new TreeToolBar(tree, parentFrame, new TreeCopyProcedureAdaptor());
        
        
        this.setLayout(new BorderLayout(GUIutilities.GAP_COMPONENT,GUIutilities.GAP_COMPONENT));
        this.setBorder(new CompoundBorder(
                new TitledBorder(new EtchedBorder(),"Attributes"),
                new EmptyBorder(GUIutilities.GAP_MARGIN,GUIutilities.GAP_MARGIN,GUIutilities.GAP_MARGIN,GUIutilities.GAP_MARGIN)));
        this.add(scrolltree, BorderLayout.CENTER);
        this.add(toolbar, BorderLayout.SOUTH);
        
    }
    
    public JTreeAdvanced getTree() {
        return tree;
    }
    
    public void updateTree(IdBase procedure) {
        tree.setModel(new AttributeTreeData(procedure));
        toolbar.expandTree();
    }
}
