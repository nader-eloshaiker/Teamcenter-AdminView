/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tceav.gui.procedure;

import java.awt.Color;
import javax.swing.JTree;
import javax.swing.tree.TreePath;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JLabel;

/**
 *
 * @author nzr4dl-e
 */
public class ProcessTaskMouseAction implements MouseListener {
    
    private TreePath path;
    private JTree tree;
    private ProcessTaskComponent task;
    private Color background;
    private Color foreground;
    
    
    public ProcessTaskMouseAction(JTree tree, TreePath path, ProcessTaskComponent task) {
        this.path = path;
        this.task = task;
        this.tree = tree;
    }
    
    public ProcessTaskMouseAction(JTree tree, TreePath path) {
        this(tree, path, null);
    }
    
    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
            if (task == null) {
                tree.setSelectionPath(path.getParentPath());
            } else {
                tree.setSelectionPath(path.pathByAddingChild(task.getTask()));
            }
        }
    }
    
    public void mouseEntered(MouseEvent e) {
        JLabel label = (JLabel)e.getComponent();
        foreground = label.getForeground();
        if(!label.getForeground().equals(Color.RED))
            label.setForeground(Color.RED);
        else
            label.setForeground(Color.BLUE);
    }
    
    public void mouseExited(MouseEvent e) {
        JLabel label = (JLabel)e.getComponent();
        if(foreground == null) {
            JLabel tmp = new JLabel();
            label.setForeground(tmp.getForeground());
        } else {
            label.setForeground(foreground);
            foreground = null;
        }
    }
    
    public void mousePressed(MouseEvent e) {
        JLabel label = (JLabel)e.getComponent();
        background = label.getBackground();
        if(!label.getBackground().equals(Color.BLACK))
            label.setBackground(label.getBackground().darker());
        else
            label.setBackground(label.getBackground().brighter());
    }
    
    public void mouseReleased(MouseEvent e) {
        JLabel label = (JLabel)e.getComponent();
        if(background == null) {
            JLabel tmp = new JLabel();
            label.setBackground(tmp.getBackground());
        } else {
            label.setBackground(background);
            background = null;
        }
    }
    
}
