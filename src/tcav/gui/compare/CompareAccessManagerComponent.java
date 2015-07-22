/*
 * CompareAccessManagerComponent.java
 *
 * Created on 7 March 2008, 19:51
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tcav.gui.compare;

import tcav.gui.access.AccessRuleComponent;
import tcav.gui.access.NamedRuleComponent;
import tcav.gui.access.RuleTreeComponent;
import tcav.manager.compare.CompareAccessManager;

import javax.naming.ldap.StartTlsRequest;
import tcav.gui.*;
import tcav.manager.access.AccessManager;
import tcav.manager.access.RuleTreeNode;
import tcav.manager.AbstractManager;
import tcav.utils.PatternMatch;
import tcav.resources.*;
import tcav.Settings;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import javax.swing.table.*;
import javax.swing.tree.*;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import java.io.File;
/**
 *
 * @author NZR4DL
 */
public class CompareAccessManagerComponent extends TabbedPanel {
    
    private JFrame parentFrame;
    private RuleTreeComponent[] ruletree;
    private NamedRuleComponent[] namedACL;
    private AccessRuleComponent accessControl;
    private JSplitPane splitPane;
    private JPanel pane;

    private final int compareCount = 2;
    private final String MODE_ACL = "Named ACL";
    private final String MODE_TREE = "Rule Tree";
    
    private CompareAccessManager cam;
    
    /** Creates a new instance of CompareAccessManagerComponent */
    public CompareAccessManagerComponent(JFrame parent, CompareAccessManager cam) {
        super();
        this.parentFrame = parent;
        this.cam = cam;
        
        ruletree = new RuleTreeComponent[compareCount];
        namedACL = new NamedRuleComponent[compareCount];
        
        accessControl = new AccessRuleComponent(cam.getAccessManagers()[0]);
        
        JRadioButton ruletreeButton = new JRadioButton("Rule Tree Mode");
        ruletreeButton.setOpaque(false);
        ruletreeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ((CardLayout)pane.getLayout()).show(pane,MODE_TREE);
            }
        });
        JRadioButton namedAclButton = new JRadioButton("Named ACL Mode");
        namedAclButton.setOpaque(false);
        namedAclButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ((CardLayout)pane.getLayout()).show(pane,MODE_ACL);
            }
        });
        
        ButtonGroup group = new ButtonGroup();
        group.add(ruletreeButton);
        group.add(namedAclButton);
        namedAclButton.setSelected(true);
        
        JPanel panelMode = new JPanel();
        panelMode.setLayout(new FlowLayout(FlowLayout.CENTER, GUIutilities.GAP_COMPONENT,0));
        panelMode.add(ruletreeButton);
        panelMode.add(namedAclButton);
        
        
        pane = new JPanel();
        pane.setLayout(new CardLayout());
        pane.add(buildRuletreeComponent(), MODE_TREE);
        pane.add(buildNamedAclComponent(), MODE_ACL);
        ((CardLayout)pane.getLayout()).show(pane,MODE_ACL);
        
        /* Rules Panel */
        JPanel panel =  new JPanel();
        panel.setLayout(new BorderLayout(0,0));//GUIutilities.GAP_MARGIN,GUIutilities.GAP_MARGIN));
        panel.add(BorderLayout.CENTER, pane);
        panel.add(BorderLayout.SOUTH, panelMode);
        
        splitPane = new JSplitPane(
                JSplitPane.VERTICAL_SPLIT,
                true,
                GUIutilities.createPanelMargined(panel),
                GUIutilities.createPanelMargined(accessControl));
        splitPane.setDividerLocation(Settings.getAMSplitLocation());
        splitPane.setResizeWeight(1.0);
        splitPane.setOneTouchExpandable(true);
        splitPane.setBorder(null);
        ((BasicSplitPaneUI)splitPane.getUI()).getDivider().addComponentListener(new ComponentAdapter(){
            public void componentMoved(ComponentEvent e){
                Settings.setAMSplitLocation(splitPane.getDividerLocation());
            }
        });
        
        /* And show it. */
        this.setLayout(new BorderLayout());//GUIutilities.GAP_MARGIN,GUIutilities.GAP_MARGIN));
        this.add("Center",splitPane);
        
    }
    
    public JPanel buildNamedAclComponent() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1,compareCount,GUIutilities.GAP_MARGIN,GUIutilities.GAP_MARGIN));
        
        
        for(int index=0; index<compareCount; index++) {
            namedACL[index] = new NamedRuleComponent(parentFrame, cam.getAccessManagers()[index], cam.getAccessManagers()[index].getName());
            namedACL[index].getTable().getSelectionModel().addListSelectionListener(new NamedAclSelectionListener(index));
            namedACL[index].attachCompareTab(cam.getCompareResult(CompareAccessManager.ACL_TYPE,index));
            panel.add(namedACL[index]);
        }

        return panel;
    }
    
    public JPanel buildRuletreeComponent() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1,compareCount,GUIutilities.GAP_MARGIN,GUIutilities.GAP_MARGIN));
        
        for(int index=0; index<compareCount; index++) {
            ruletree[index] = new RuleTreeComponent(parentFrame, cam.getAccessManagers()[index], cam.getAccessManagers()[index].getName());
            ruletree[index].getTree().addTreeSelectionListener(new RuletreeSelectionListener(index));
            panel.add(ruletree[index]);
        }

        return panel;
    }
    
    private JPanel statusBar;
    
    public JComponent getStatusBar() {
        if(statusBar == null) {
            JLabel textAuthor = new JLabel(" Author: "+cam.getAccessManagers()[0].getMetaData().getUserDetails()+" ");
            textAuthor.setBorder(new BevelBorder(BevelBorder.LOWERED));
            JLabel textDate = new JLabel(" Date: "+cam.getAccessManagers()[0].getMetaData().getTimeDetails()+" ");
            textDate.setBorder(new BevelBorder(BevelBorder.LOWERED));
            JLabel textFile = new JLabel(" Path: "+cam.getAccessManagers()[0].getFile().getParent()+" ");
            textFile.setBorder(new BevelBorder(BevelBorder.LOWERED));
            statusBar = new JPanel();
            statusBar.setLayout(new BorderLayout(1,1));
            statusBar.add("Center", textAuthor);
            statusBar.add("West", textFile);
            statusBar.add("East", textDate);
        }
        return statusBar;
    }
    
    public int indexOfTreePath(TreePath path, TreePath[] paths) {
        if((path == null) || (paths == null))
            return -1;
        
        for(int i=0; i<paths.length; i++)
            if(path.equals(paths[i]))
                return i;
        
        return -1;
    }
    
    public boolean isTreePathAvailable(TreePath[] src, TreePath[] dst) {
        for(int i=0; i<src.length; i++)
            if(indexOfTreePath(src[i], dst) > -1)
                return true;
        
        return false;
    }
    
    private TreePath[] getTreePaths(JTreeAdvanced tree, ArrayList<RuleTreeNode> components) {
        ArrayList<TreePath> paths = new ArrayList<TreePath>();
        
        searchTree(tree, tree.getPathForRow(0), paths, components);
        
        return paths.toArray(new TreePath[components.size()]);
    }
    
    private void searchTree(JTreeAdvanced tree, TreePath currentPath, ArrayList<TreePath> paths, ArrayList<RuleTreeNode> components) {
        if(components.indexOf((RuleTreeNode)currentPath.getLastPathComponent()) > -1)
            paths.add(currentPath);
        
        int childCount = tree.getModel().getChildCount(currentPath.getLastPathComponent());
        if(childCount > 0) {
            for (int e=0; e<childCount; e++ ) {
                TreePath newPath = currentPath.pathByAddingChild(tree.getModel().getChild(currentPath.getLastPathComponent(), e));
                searchTree(tree, newPath, paths, components);
            }
        }
    }
    
    
    
    public AbstractManager getManager() {
        return cam;
    }
    
    private ImageIcon iconRuleTree;
    
    public ImageIcon getIcon() {
        if(iconRuleTree == null){
            try {
                iconRuleTree = ResourceLoader.getImage(ImageEnum.amRuletree);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error Load Images", JOptionPane.ERROR_MESSAGE);
            }
        }
        return iconRuleTree;
    }
    
    class NamedAclSelectionListener implements ListSelectionListener {
        private int index;
        
        public NamedAclSelectionListener(int index) {
            this.index = index;
        }
        
        public void valueChanged(ListSelectionEvent e) {
            int i = namedACL[index].getTable().getSelectedRow();
            if (i > -1) {
                if(namedACL[index].getModel().getAccessRule(i).getRuleTreeReferences().size() > 0) {
                    
                    TreePath[] paths = getTreePaths(ruletree[index].getTree(), namedACL[index].getModel().getAccessRule(i).getRuleTreeReferences());
                    if(!isTreePathAvailable(paths, ruletree[index].getTree().getSelectionPaths())) {
                        ruletree[index].getTree().setSelectionPaths(paths);
                        ruletree[index].getTree().scrollPathToVisible(paths[0]);
                    }
                    
                } else {
                    ruletree[index].getTree().clearSelection();
                }
                namedACL[index].updateReferences(i);
                accessControl.updateTable(namedACL[index].getModel().getAccessRule(i));
                
                for(int k=0; k<namedACL.length; k++)
                    if(k != index)
                        if(namedACL[k].getTable().getSelectedRowCount() != 0)
                            namedACL[k].getTable().clearSelection();
                
            } else
                accessControl.updateTable();
        }
    }
    
    class RuletreeSelectionListener implements TreeSelectionListener {
        private TreePath oldPath;
        private int index;
        
        public RuletreeSelectionListener(int index) {
            this.index = index;
        }
        
        public void valueChanged(TreeSelectionEvent e) {
            TreePath newPath = e.getPath();
            
            if(oldPath != null){
                if(e.isAddedPath(e.getPath()) && newPath.equals(oldPath))
                    return;
            }
            
            if(e.isAddedPath(e.getPath()))
                oldPath = newPath;
            else
                oldPath = null;
            
            RuleTreeNode treeNode = (RuleTreeNode)e.getPath().getLastPathComponent();
            
            if(e.isAddedPath(e.getPath()) && treeNode.getAccessRule() != null){
                int selIndex = namedACL[index].getModel().indexOfRuleName(treeNode.getAccessRuleName());
                if(selIndex > -1) {
                    namedACL[index].getTable().setRowSelectionInterval(selIndex,selIndex);
                    namedACL[index].getTable().getSelectionModel().setAnchorSelectionIndex(selIndex);
                    namedACL[index].getTable().scrollRectToVisible(
                            namedACL[index].getTable().getCellRect(
                            namedACL[index].getTable().getSelectionModel().getAnchorSelectionIndex(),
                            namedACL[index].getTable().getColumnModel().getSelectionModel().getAnchorSelectionIndex(),
                            false)
                            );
                } else
                    accessControl.updateTable(treeNode.getAccessRule());
            } else if(e.isAddedPath(e.getPath()) && treeNode.getAccessRule() == null){
                namedACL[index].getTable().clearSelection();
                accessControl.updateTable();
            }
        }
    }
}
