/*
 * ActionComponent.java
 *
 * Created on 8 February 2008, 13:17
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
public class ActionComponent extends JComponent {
    
    private JTreeAdvanced tree;
    private AdminViewFrame parentFrame;
    private TreeToolBar toolbar;
    
    
    public ActionComponent(AdminViewFrame parentFrame, ProcedureManager pm) {
        super();
        this.parentFrame = parentFrame;
        
        // Action Tree
        tree = new JTreeAdvanced(new ActionTreeData());
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.setCellRenderer(new ActionRenderer());

        ToolTipManager.sharedInstance().registerComponent(tree);

        JScrollPane scrolltree = new JScrollPane();
        scrolltree.setBorder(new BevelBorder(BevelBorder.LOWERED));
        scrolltree.getViewport().add(tree);
        
        toolbar = new TreeToolBar(tree, parentFrame, new TreeCopyProcedureAdaptor());
        
        setBorder(new CompoundBorder(
                new TitledBorder(new EtchedBorder(),"Handlers"),
                new EmptyBorder(GUIutilities.GAP_MARGIN,GUIutilities.GAP_MARGIN,GUIutilities.GAP_MARGIN,GUIutilities.GAP_MARGIN)));
        setLayout(new BorderLayout(GUIutilities.GAP_COMPONENT,GUIutilities.GAP_COMPONENT));
        add(toolbar, BorderLayout.SOUTH);
        add(scrolltree, BorderLayout.CENTER);
    }
    
    public JTreeAdvanced getTree() {
        return tree;
    }
    
    public void updateTree(IdBase procedure) {
        tree.setModel(new ActionTreeData(procedure));
        toolbar.expandTree();
    }
}
