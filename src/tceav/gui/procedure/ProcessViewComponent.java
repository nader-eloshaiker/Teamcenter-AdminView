/*
 * To change this template, choose Tools | Templates
 * and open the rootTask in the editor.
 */
package tceav.gui.procedure;

import tceav.manager.procedure.plmxmlpdm.base.IdBase;
import tceav.manager.procedure.plmxmlpdm.ProcedureTagTypeEnum;
import tceav.gui.tools.GUIutilities;
import java.awt.BorderLayout;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import tceav.manager.procedure.plmxmlpdm.type.WorkflowTemplateType;

/**
 *
 * @author nzr4dl-e
 */
public class ProcessViewComponent extends JComponent {

    private WorkflowTemplateType wt;
    private ProcessViewRenderer renderer;
    private JScrollPane scrolltree;

    public ProcessViewComponent() {
        super();
        renderer = new ProcessViewRenderer();
        scrolltree = new JScrollPane();
        scrolltree.setBorder(new BevelBorder(BevelBorder.LOWERED));
        scrolltree.getViewport().add(renderer);

        this.setLayout(new BorderLayout(GUIutilities.GAP_COMPONENT, GUIutilities.GAP_COMPONENT));
        this.setBorder(new CompoundBorder(
                new TitledBorder(new EtchedBorder(), "Process View"),
                new EmptyBorder(GUIutilities.GAP_MARGIN, GUIutilities.GAP_MARGIN, GUIutilities.GAP_MARGIN, GUIutilities.GAP_MARGIN)));
        this.add(scrolltree, BorderLayout.CENTER);
    }

    public void setWorkflowTemplate(IdBase procedure) {
        scrolltree.getViewport().remove(renderer);
        if (procedure instanceof WorkflowTemplateType) {
            wt = (WorkflowTemplateType) procedure;
            renderer = new ProcessViewRenderer(wt);
        } else {
            wt = null;
            renderer = new ProcessViewRenderer();
        }
        scrolltree.getViewport().add(renderer);
    }
    
    public ProcessTaskComponent getSubComponent(int index) {
        return renderer.getSubComponents()[index];
    }
    
    public int getSubComponentSize() {
        return renderer.getSubComponents().length;
    }
    
    public WorkflowTemplateType getTask() {
        return wt;
    }
}
