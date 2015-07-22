package tcav.gui.access;

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
public class AccessManagerComponent extends TabbedPanel {
    
    private JFrame parentFrame;
    private RuleTreeComponent ruletree;
    private NamedRuleComponent namedACL;
    private AccessRuleComponent accessControl;
    private JSplitPane splitPane;
    
    private AccessManager am;
    
    /**
     * Creates a new instance of AccessManagerComponent
     */
    public AccessManagerComponent(JFrame parent, AccessManager am) {
        super();
        this.parentFrame = parent;
        this.am = am;
        
        accessControl = new AccessRuleComponent(am);
        
        namedACL = new NamedRuleComponent(parentFrame, am);
        namedACL.getTable().getSelectionModel().addListSelectionListener( new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                int i = namedACL.getTable().getSelectedRow();
                if (i > -1) {
                    if(namedACL.getModel().getAccessRule(i).getRuleTreeReferences().size() > 0) {
                        
                        TreePath[] paths = getTreePaths(ruletree.getTree(), namedACL.getModel().getAccessRule(i).getRuleTreeReferences());
                        if(!isTreePathAvailable(paths, ruletree.getTree().getSelectionPaths())) {
                            ruletree.getTree().setSelectionPaths(paths);
                            ruletree.getTree().scrollPathToVisible(paths[0]);
                        }
                        
                    } else {
                        ruletree.getTree().clearSelection();
                    }
                    namedACL.updateReferences(i);
                    accessControl.updateTable(namedACL.getModel().getAccessRule(i));
                    
                } else
                    accessControl.updateTable();
            }
        });
        
        ruletree = new RuleTreeComponent(parentFrame, am);
        ruletree.getTree().addTreeSelectionListener(new TreeSelectionListener() {
            private TreePath oldPath;
            
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
                    int index = namedACL.getModel().indexOfRuleName(treeNode.getAccessRuleName());
                    if(index > -1) {
                        namedACL.getTable().setRowSelectionInterval(index,index);
                        namedACL.getTable().getSelectionModel().setAnchorSelectionIndex(index);
                        namedACL.getTable().scrollRectToVisible(
                                namedACL.getTable().getCellRect(
                                namedACL.getTable().getSelectionModel().getAnchorSelectionIndex(),
                                namedACL.getTable().getColumnModel().getSelectionModel().getAnchorSelectionIndex(),
                                false)
                                );
                    } else
                        accessControl.updateTable(treeNode.getAccessRule());
                } else if(e.isAddedPath(e.getPath()) && treeNode.getAccessRule() == null){
                    namedACL.getTable().clearSelection();
                    accessControl.updateTable();
                }
            }
        });
        
        /* Rules Panel */
        JPanel panelRule =  new JPanel();
        panelRule.setLayout(new BorderLayout(GUIutilities.GAP_MARGIN,GUIutilities.GAP_MARGIN));
        panelRule.add("East", namedACL);
        panelRule.add("Center",ruletree);
        
        splitPane = new JSplitPane(
                JSplitPane.VERTICAL_SPLIT,
                true,
                GUIutilities.createPanelMargined(panelRule),
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
    
    public AbstractManager getManager() {
        return am;
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
    
    private JPanel statusBar;
    
    public JComponent getStatusBar() {
        if(statusBar == null) {
            JLabel textAuthor = new JLabel(" Author: "+am.getMetaData().getUserDetails()+" ");
            textAuthor.setBorder(new BevelBorder(BevelBorder.LOWERED));
            JLabel textDate = new JLabel(" Date: "+am.getMetaData().getTimeDetails()+" ");
            textDate.setBorder(new BevelBorder(BevelBorder.LOWERED));
            JLabel textFile = new JLabel(" Path: "+am.getFile().getParent()+" ");
            textFile.setBorder(new BevelBorder(BevelBorder.LOWERED));
            statusBar = new JPanel();
            statusBar.setLayout(new BorderLayout(1,1));
            statusBar.add("Center", textAuthor);
            statusBar.add("West", textFile);
            statusBar.add("East", textDate);
        }
        return statusBar;
    }
    
    public AccessManager getAccessManager() {
        return am;
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
    
    
}
