/*
 * XMLComponent.java
 *
 * Created on 8 February 2008, 14:00
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tcav.gui.procedure;

import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import tcav.gui.*;
import tcav.manager.procedure.plmxmlpdm.base.IdBase;
/**
 *
 * @author nzr4dl
 */
public class XMLComponent extends JComponent {
    
    /** Creates a new instance of XMLComponent */
    private JTableAdvanced table;
    
    public XMLComponent() {
        super();
        table = new JTableAdvanced(new XMLTableModel());
        table.getTableHeader().setReorderingAllowed(false);        
    }
    
    public JTableAdvanced getTable() {
        return table;
    }
    
    private IdBase procedure;
    
    public void updateTable(IdBase procedure){
        this.procedure = procedure;
        
        if(dialog != null)
            if(dialog.isVisible()) {
                table.setModel(new XMLTableModel(procedure));
                GUIutilities.packColumns(table, 2);
            }
    }
    
    private JDialog dialog;
    
    public void show(JFrame parentFrame) {
        if(dialog != null)
            if(dialog.isVisible())
                return;
        
        JScrollPane scrolltable = new JScrollPane();
        scrolltable.setPreferredSize(new Dimension(400,220));
        scrolltable.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrolltable.setBorder(new BevelBorder(BevelBorder.LOWERED));
        scrolltable.getViewport().add(table);
        
        JButton button = new JButton("Close Window");
        button.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                dialog.setVisible(false);
                dialog.dispose();
                dialog = null;
            }
        });
        JPanel panelLabel = new JPanel();
        panelLabel.setLayout(new FlowLayout(FlowLayout.CENTER, 0,0));
        panelLabel.add(new JLabel("This dialog can remain open as new selections are made"));
        
        JPanel panelButton = new JPanel();
        panelButton.setLayout(new FlowLayout(FlowLayout.CENTER, 0,0));
        panelButton.add(button);
        
        JPanel panelLower = new JPanel();
        panelLower.setLayout(new GridLayout(2,1,GUIutilities.GAP_OUTER_BORDER,GUIutilities.GAP_OUTER_BORDER));
        panelLower.add(panelLabel);
        panelLower.add(panelButton);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(GUIutilities.GAP_COMPONENT,GUIutilities.GAP_COMPONENT));
        panel.setBorder(new EmptyBorder(GUIutilities.GAP_OUTER_BORDER,GUIutilities.GAP_OUTER_BORDER,GUIutilities.GAP_OUTER_BORDER,GUIutilities.GAP_OUTER_BORDER));
        panel.add(scrolltable, BorderLayout.CENTER);
        panel.add(panelLower, BorderLayout.SOUTH);
        
        dialog = new JDialog(parentFrame, "XML Properties", false);
        Container contentPane = dialog.getContentPane();
        contentPane.setLayout(new BorderLayout(GUIutilities.GAP_COMPONENT,GUIutilities.GAP_COMPONENT));
        contentPane.add(panel, BorderLayout.CENTER);
        
        WindowAdapter adapter = new WindowAdapter() {
            public void windowClosed(WindowEvent we) {
                dialog = null;
            }
        };
        
        dialog.setComponentOrientation(table.getComponentOrientation());
        dialog.setResizable(false);
        dialog.pack();
        dialog.setLocationRelativeTo(parentFrame);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.addWindowListener(adapter);
        dialog.setVisible(true);

        if(procedure != null) {
            table.setModel(new XMLTableModel(procedure));
            GUIutilities.packColumns(table, 2);
        }
        

    }
    
}
