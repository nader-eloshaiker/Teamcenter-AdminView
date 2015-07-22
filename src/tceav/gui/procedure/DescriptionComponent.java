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
import javax.swing.JTextArea;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import tceav.gui.tools.GUIutilities;
import tceav.manager.procedure.plmxmlpdm.base.DescriptionBase;
import tceav.manager.procedure.plmxmlpdm.base.IdBase;
import tceav.manager.procedure.plmxmlpdm.type.WorkflowTemplateType;

/**
 *
 * @author nzr4dl
 */
public class DescriptionComponent extends JComponent {

    JTextArea textComp;

    public DescriptionComponent() {
        super();

        // Action Tree
        textComp = new JTextArea(8, 20);
        textComp.setLineWrap(true);
        textComp.setWrapStyleWord(true);
        textComp.setEditable(false);

        JScrollPane scrollText = new JScrollPane();
        scrollText.setBorder(new BevelBorder(BevelBorder.LOWERED));
        scrollText.getViewport().add(textComp);

        setBorder(new CompoundBorder(
                new TitledBorder(new EtchedBorder(), "Description"),
                new EmptyBorder(GUIutilities.GAP_MARGIN, GUIutilities.GAP_MARGIN, GUIutilities.GAP_MARGIN, GUIutilities.GAP_MARGIN)));
        setLayout(new BorderLayout(GUIutilities.GAP_COMPONENT, GUIutilities.GAP_COMPONENT));
        add(scrollText, BorderLayout.CENTER);
    }

    public void setDescription(IdBase description) {
        if (description instanceof WorkflowTemplateType) {
            WorkflowTemplateType wf = (WorkflowTemplateType) description;
            if (wf.getTaskDescription() != null) {
                textComp.setText(wf.getTaskDescription().getItem().get(0).getValue());
            } else {
                textComp.setText("");
            }
        } else if (description instanceof DescriptionBase) {
            textComp.setText(((DescriptionBase) description).getDescription());
        } else {
            textComp.setText("");
        }
    }
}
