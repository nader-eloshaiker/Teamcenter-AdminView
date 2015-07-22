/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tceav.gui.procedure;

import tceav.resources.ResourceLoader;
import tceav.resources.ImageEnum;
import java.awt.Point;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Font;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import tceav.manager.procedure.plmxmlpdm.type.WorkflowTemplateType;

/**
 *
 * @author nzr4dl-e
 */
public class ProcessTaskComponent extends JLabel {
    
    private WorkflowTemplateType task;
    private int taskType;
    public static final int SUB_TASK = 0;
    public static final int START_TASK = 1;
    public static final int FINISH_TASK = 2;
    
    public ProcessTaskComponent(WorkflowTemplateType task) {
        this(task, SUB_TASK);
    }
    
    public ProcessTaskComponent(WorkflowTemplateType task, int taskType) {
        super();
        this.task = task;
        this.taskType = taskType;
        String name;
        
        switch (taskType) {
            case START_TASK:
                name = "Start";
                setBackground(new Color(190, 230, 200));
                break;
            case FINISH_TASK:
                name = "Finish";
                setBackground(new Color(230, 175, 175));
                break;
            case SUB_TASK:
            default:
                name = task.getName();
                break;
        }
        
        setText(name);
        setOpaque(true);
        setHorizontalTextPosition(SwingConstants.CENTER);
        setVerticalTextPosition(SwingConstants.TOP);
        setFont(getFont().deriveFont(Font.PLAIN, (float) 11));
        setIcon(TaskProperties.getProcessKeyIcon(task.getIconKey()));
        setBorder(new CompoundBorder(
                new BevelBorder(BevelBorder.RAISED),
                new EmptyBorder(3, 3, 3, 3)));
        
        setLocation(getPreferedLocation());
    }
    
    public Point getPreferedLocation() {
        switch (taskType) {
            case START_TASK:
                return task.getStartLocation();
            case FINISH_TASK:
                return task.getEndLocation();
            case SUB_TASK:
            default:
                return task.getTaskLocation();
        }
    }
    
    public Point getRelativeCenterEdge(JComponent comp) {
        Point src = (Point) getLocation().clone();
        Dimension srcDim = getSize();
        Point dst = comp.getLocation();
        Dimension dstDim = comp.getSize();
        boolean horizontalDominant = (Math.abs(src.x - dst.x) >= Math.abs(src.y - dst.y));
        
        if (horizontalDominant) {
            if (src.x + srcDim.width < dst.x) {
                src.x = src.x + srcDim.width;
                src.y = src.y + (srcDim.height / 2);
            } else if (src.x > dst.x + dstDim.width) {
                src.y = src.y + (srcDim.height / 2);
            } else {
            }
        } else {
            if (src.y + srcDim.height < dst.y) {
                src.x = src.x + (srcDim.width / 2);
                src.y = src.y + srcDim.height;
            } else if (src.y > dst.y + dstDim.height) {
                src.x = src.x + (srcDim.width / 2);
            } else {
            }
        }
        
        return src;
    }
    
    public WorkflowTemplateType getTask() {
        return task;
    }
}
