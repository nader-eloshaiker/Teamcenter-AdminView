/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tceav.gui.procedure;

import tceav.gui.tools.GUIutilities;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.JTree;
import tceav.manager.procedure.plmxmlpdm.type.SiteType;
import tceav.manager.procedure.plmxmlpdm.type.WorkflowTemplateType;

/**
 *
 * @author nzr4dl-e
 */
public class ProcessViewRenderer extends JPanel {
    
    private ProcessTaskComponent[] subTasks;
    
    public ProcessViewRenderer() {
        this((SiteType)null);
    }
    
    public ProcessViewRenderer(SiteType rootTask) {
        subTasks = new ProcessTaskComponent[0];
    }
    
    public ProcessViewRenderer(WorkflowTemplateType rootTask) {
        super();
        this.setLayout(new CoordinateLayout(GUIutilities.GAP_COMPONENT, GUIutilities.GAP_COMPONENT));
        
        JTree tree = new JTree();
        setBackground(tree.getBackground());
        setOpaque(true);
        tree = null;
        
        if (rootTask == null) {
            subTasks = new ProcessTaskComponent[0];
            return;
        }
        
        subTasks = new ProcessTaskComponent[rootTask.getSubTemplateRefs().size() + 2];
        for (int i = 0; i < subTasks.length; i++) {
            if (i == 0) {
                subTasks[i] = new ProcessTaskComponent(rootTask, ProcessTaskComponent.START_TASK);
            } else if (i == subTasks.length - 1) {
                subTasks[i] = new ProcessTaskComponent(rootTask, ProcessTaskComponent.FINISH_TASK);
            } else {
                subTasks[i] = new ProcessTaskComponent(rootTask.getSubTemplates()[i - 1]);
            }
            this.add(subTasks[i]);
        }
    }
    
    public ProcessTaskComponent[] getSubComponents() {
        return subTasks;
    }
    
    @Override
    public void updateUI() {
        super.updateUI();
        JTree tree = new JTree();
        setBackground(tree.getBackground());
        tree = null;
    }
    
    public void drawArrow(Point src, Point dst, Graphics2D g2) {
        int[] xpoints = {0, -12, -12};
        int[] ypoints = {0, 5, -5};
        Shape arrow = new Polygon(xpoints, ypoints, 3);
        double hori = dst.x - src.x;
        double vert = dst.y - src.y;
        double theta = Math.PI * 0.5 - Math.atan2(hori, vert);
        AffineTransform at = AffineTransform.getTranslateInstance(dst.x, dst.y);
        at.rotate(theta);
        g2.fill(at.createTransformedShape(arrow));
        g2.drawLine(src.x, src.y, dst.x, dst.y);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (subTasks == null) {
            return;
        }
        
        List<String> dependants;
        Graphics2D g2d = (Graphics2D) g;
        Point srcPoint;
        Point dstPoint;
        boolean found = false;
        
        for (int dst = 1; dst < subTasks.length; dst++) {
            dependants = subTasks[dst].getTask().getDependencyTaskTemplateRefs();
            for (int src = 1; src < subTasks.length - 1; src++) {
                if (dependants.indexOf(subTasks[src].getTask().getId()) > -1) {
                    found = true;
                    srcPoint = subTasks[src].getRelativeCenterEdge(subTasks[dst]);
                    dstPoint = subTasks[dst].getRelativeCenterEdge(subTasks[src]);
                    drawArrow(srcPoint, dstPoint, g2d);
                }
            }
            if (!found) {
                srcPoint = subTasks[0].getRelativeCenterEdge(subTasks[dst]);
                dstPoint = subTasks[dst].getRelativeCenterEdge(subTasks[0]);
                drawArrow(srcPoint, dstPoint, g2d);
            } else {
                found = false;
            }
        }
    }
}
