/*
 * TableShadedRenderer.java
 *
 * Created on 17 April 2008, 11:52
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tceav.gui.procedure.tabulate;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
/**
 *
 * @author nzr4dl
 */
public class TableShadedWorkflowRenderer extends DefaultTableCellRenderer implements TableCellRenderer {
    
    private final Color acknowledgeTaskColor = new Color(205, 210, 235);
    private final Color addStatusTaskColor = new Color(200, 200, 200);
    private final Color checklistTaskColor = new Color(245, 200, 245);
    private final Color conditionTaskColor = new Color(205, 230, 235);
    private final Color doTaskColor = new Color(235, 235, 205);
    private final Color impactAnalysisTaskColor = new Color(230, 215, 170);
    private final Color notifyTaskColor = new Color(255, 235, 115);
    private final Color orTaskColor = new Color(215, 195, 240);
    private final Color performSignoffTaskColor = new Color(235, 205, 205);
    private final Color prepareECOTaskColor = new Color(160, 190, 255);
    private final Color reviewTaskColor = new Color(245, 245, 200);
    private final Color routeTaskColor = new Color(190, 230, 200);
    private final Color selectSignoffTaskColor = new Color(225, 235, 205);
    private final Color syncTaskColor = new Color(230, 175, 175);
    private final Color taskColor = new Color(255, 255, 255);
    
    
    /** Creates a new instance of TableShadedRenderer */
    public TableShadedWorkflowRenderer() {
        super();
    }
    
    private void setBackgroundShading(JTable table, int row, boolean isSelected) {
        Color normalColor = table.getBackground();
        if(isSelected) {
            setBackground(table.getSelectionBackground());
        } else {
            WorkflowAbstractTable wti = (WorkflowAbstractTable)table.getModel();
            String s = wti.getWorkflowType(row);
            if(s == null)
                setBackground(normalColor);
            else if(s.equalsIgnoreCase("AcknowledgeTask"))
                setBackground(acknowledgeTaskColor);
            else if(s.equalsIgnoreCase("AddStatusTask"))
                setBackground(addStatusTaskColor);
            else if(s.equalsIgnoreCase("ChecklistTask"))
                setBackground(checklistTaskColor);
            else if(s.equalsIgnoreCase("ConditionTask"))
                setBackground(conditionTaskColor);
            else if(s.equalsIgnoreCase("DoTask"))
                setBackground(doTaskColor);
            else if(s.equalsIgnoreCase("ImpactAnalysisTask"))
                setBackground(impactAnalysisTaskColor);
            else if(s.equalsIgnoreCase("NotifyTask"))
                setBackground(notifyTaskColor);
            else if(s.equalsIgnoreCase("OrTask"))
                setBackground(orTaskColor);
            else if(s.equalsIgnoreCase("PerformSignoffTask"))
                setBackground(performSignoffTaskColor);
            else if(s.equalsIgnoreCase("PrepareECOTask"))
                setBackground(prepareECOTaskColor);
            else if(s.equalsIgnoreCase("ReviewTask"))
                setBackground(reviewTaskColor);
            else if(s.equalsIgnoreCase("RouteTask"))
                setBackground(routeTaskColor);
            else if(s.equalsIgnoreCase("SelectSignoffTask"))
                setBackground(selectSignoffTaskColor);
            else if(s.equalsIgnoreCase("SyncTask"))
                setBackground(syncTaskColor);
            else if(s.equalsIgnoreCase("Task"))
                setBackground(taskColor);
            else
                setBackground(normalColor);
            
        }
        
    }
    
    public Component getTableCellRendererComponent(JTable table,
            Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        
        setBackgroundShading(table, row, isSelected);
        
        return this;
    }
}
