/*
 * WorkflowIconKeyRenderer.java
 *
 * Created on 1 August 2007, 16:39
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tcadminview.gui.procedure;

import tcadminview.ResourceLocator;
import javax.swing.table.*;
import javax.swing.*;
import java.awt.Component;
/**
 *
 * @author nzr4dl
 */
public class WorkflowIconKeyRenderer extends DefaultTableCellRenderer implements TableCellRenderer {
    
    protected static ImageIcon acknowledgeTask;
    protected static ImageIcon addStatusTask;
    protected static ImageIcon checkListTask;
    protected static ImageIcon conditionTask;
    protected static ImageIcon doTask;
    protected static ImageIcon impactAnalysisTask;
    protected static ImageIcon notifyTask;
    protected static ImageIcon orTask;
    protected static ImageIcon performSignoffTask;
    protected static ImageIcon prepareecoTask;
    protected static ImageIcon process;
    protected static ImageIcon reviewProcess;
    protected static ImageIcon reviewTask;
    protected static ImageIcon routeTask;
    protected static ImageIcon selectSignoffTask;
    protected static ImageIcon syncTask;
    protected static ImageIcon task;
    protected static ImageIcon taskProperties;

    static
    {
        try {
            acknowledgeTask = new ImageIcon(ResourceLocator.getWorkflowImage("acknowledgeTask.gif"));
            addStatusTask = new ImageIcon(ResourceLocator.getWorkflowImage("acknowledgeTask.gif"));
            checkListTask = new ImageIcon(ResourceLocator.getWorkflowImage("checkListTask.gif"));
            conditionTask = new ImageIcon(ResourceLocator.getWorkflowImage("conditionTask.gif"));
            doTask = new ImageIcon(ResourceLocator.getWorkflowImage("doTask.gif"));
            notifyTask = new ImageIcon(ResourceLocator.getWorkflowImage("notifyTask.gif"));
            orTask = new ImageIcon(ResourceLocator.getWorkflowImage("orTask.gif"));
            impactAnalysisTask = new ImageIcon(ResourceLocator.getWorkflowImage("impactAnalysisTask.gif"));
            performSignoffTask = new ImageIcon(ResourceLocator.getWorkflowImage("performSignoffTask.gif"));
            prepareecoTask = new ImageIcon(ResourceLocator.getWorkflowImage("prepareecoTask.gif"));
            process = new ImageIcon(ResourceLocator.getWorkflowImage("process.gif"));
            reviewProcess = new ImageIcon(ResourceLocator.getWorkflowImage("reviewProcess.gif"));
            reviewTask = new ImageIcon(ResourceLocator.getWorkflowImage("reviewTask.gif"));
            routeTask = new ImageIcon(ResourceLocator.getWorkflowImage("routeTask.gif"));
            selectSignoffTask = new ImageIcon(ResourceLocator.getWorkflowImage("selectSignoffTask.gif"));
            syncTask = new ImageIcon(ResourceLocator.getWorkflowImage("syncTask.gif"));
            task = new ImageIcon(ResourceLocator.getWorkflowImage("task.gif"));
            taskProperties = new ImageIcon(ResourceLocator.getWorkflowImage("taskProperties.gif"));
        } catch (Exception e) {
            System.out.println("Couldn't load images: " + e);
        }
    }

    /** Creates a new instance of WorkflowIconKeyRenderer */
    public WorkflowIconKeyRenderer() {
    }
    
    /** Creates a new instance of AccessRuleTableHearderRenderer */
    public Component getTableCellRendererComponent(JTable table,
            Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        
        String s = (String)value;
        
        if(s.equals("acknowledgeTask"))
            setIcon(acknowledgeTask);
        else if(s.equals("addStatusTask"))
            setIcon(addStatusTask);
        else if(s.equals("checkListTask"))
            setIcon(checkListTask);
        else if(s.equals("conditionTask"))
            setIcon(conditionTask);
        else if(s.equals("doTask"))
            setIcon(doTask);
        else if(s.equals("impactAnalysisTask"))
            setIcon(impactAnalysisTask);
        else if(s.equals("notifyTask"))
            setIcon(notifyTask);
        else if(s.equals("orTask"))
            setIcon(orTask);
        else if(s.equals("performSignoffTask"))
            setIcon(performSignoffTask);
        else if(s.equals("prepareecoTask"))
            setIcon(prepareecoTask);
        else if(s.equals("process"))
            setIcon(process);
        else if(s.equals("reviewProcess"))
            setIcon(reviewProcess);
        else if(s.equals("reviewTask"))
            setIcon(reviewTask);
        else if(s.equals("routeTask"))
            setIcon(routeTask);
        else if(s.equals("selectSignoffTask"))
            setIcon(selectSignoffTask);
        else if(s.equals("syncTask"))
            setIcon(syncTask);
        else if(s.equals("task"))
            setIcon(task);
        else if(s.equals("taskProperties"))
            setIcon(taskProperties);
         else{
            setIcon(null);
            setText(s);
        }
        setHorizontalAlignment(CENTER);
        setToolTipText(s);
        
        if (isSelected) {
            setForeground(table.getSelectionForeground());
            super.setBackground(table.getSelectionBackground());
        } else {
            setForeground(table.getForeground());
            setBackground(table.getBackground());
        }
        
        return this;
    }
    
}