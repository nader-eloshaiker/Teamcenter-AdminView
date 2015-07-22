/*
 * CompareTabChooser.java
 *
 * Created on 4 March 2008, 09:09
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tcav.gui.compare;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import tcav.gui.*;
import tcav.resources.*;
import tcav.manager.AbstractManager;
import tcav.Settings;

/**
 *
 * @author nzr4dl
 */
public class CompareTabChooser extends JPanel {
    
    private JList list1;
    private JList list2;
    private JRadioButton ruletreeButton;
    private JRadioButton procedureButton;
    private JTabbedPane pane;
    private String SelectionMode;
    private ManagerModel listModel;
    
    /** Creates a new instance of CompareTabChooser */
    public CompareTabChooser(JTabbedPane pane) {
        super();
        this.pane = pane;
        
        SelectionMode = Settings.getCompareMode();
        
        ruletreeButton = new JRadioButton(
                "RuleTree", 
                (SelectionMode.equals(AbstractManager.ACCESS_MANAGER_TYPE)));
        ruletreeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(ruletreeButton.isSelected()) {
                    SelectionMode = AbstractManager.ACCESS_MANAGER_TYPE;
                    Settings.setCompareMode(SelectionMode);
                    listModel = new ManagerModel();
                    list1.setModel(listModel);
                    list2.setModel(listModel);
                }
            }
        });
        procedureButton = new JRadioButton(
                "Procedure", 
                (SelectionMode.equals(AbstractManager.PROCEDURE_MANAGER_TYPE)));
        procedureButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(procedureButton.isSelected()) {
                    SelectionMode = AbstractManager.PROCEDURE_MANAGER_TYPE;
                    Settings.setCompareMode(SelectionMode);
                    listModel = new ManagerModel();
                    list1.setModel(listModel);
                    list2.setModel(listModel);
                }
            }
        });
        
        ButtonGroup group = new ButtonGroup();
        group.add(ruletreeButton);
        group.add(procedureButton);
        
        initialiseMode();

        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2,1,GUIutilities.GAP_COMPONENT,GUIutilities.GAP_COMPONENT));
        buttonPanel.setBorder(new CompoundBorder(
                new TitledBorder(new EtchedBorder(),"Mode"),
                new EmptyBorder(
                GUIutilities.GAP_MARGIN,
                GUIutilities.GAP_MARGIN,
                GUIutilities.GAP_MARGIN,
                GUIutilities.GAP_MARGIN)
                ));
        buttonPanel.add(ruletreeButton);
        buttonPanel.add(procedureButton);
        
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BorderLayout());
        controlPanel.add(BorderLayout.NORTH, buttonPanel);
        
        listModel = new ManagerModel();
        
        list1 = new JList(listModel);
        list1.setVisibleRowCount(10);
        list1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list1.setCellRenderer(new ManagerCellRenderer());
        JScrollPane listScr1 = new JScrollPane();
        listScr1.getViewport().add(list1);
        listScr1.setBorder(new BevelBorder(BevelBorder.LOWERED));
        TitledBorder tb1 = new TitledBorder(new EmptyBorder(0,0,0,0), "1st File:");
        tb1.setTitlePosition(TitledBorder.BOTTOM);
        JPanel panel1 = new JPanel();
        panel1.setLayout(new BorderLayout());
        panel1.setBorder(tb1);
        panel1.add(BorderLayout.CENTER, listScr1);
        
        list2 = new JList(listModel);
        list2.setVisibleRowCount(10);
        list2.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list2.setCellRenderer(new ManagerCellRenderer());
        JScrollPane listScr2 = new JScrollPane();
        listScr2.getViewport().add(list2);
        listScr2.setBorder(new BevelBorder(BevelBorder.LOWERED));
        TitledBorder tb2 = new TitledBorder(new EmptyBorder(0,0,0,0), "2nd File:");
        tb2.setTitlePosition(TitledBorder.BOTTOM);
        JPanel panel2 = new JPanel();
        panel2.setLayout(new BorderLayout());
        panel2.setBorder(tb2);
        panel2.add(BorderLayout.CENTER, listScr2);
        
        JPanel managerPanel = new JPanel();
        managerPanel.setLayout(new GridLayout(1,1,GUIutilities.GAP_COMPONENT,GUIutilities.GAP_COMPONENT));
        managerPanel.setBorder(new CompoundBorder(
                new TitledBorder(new EtchedBorder(),"Manager Selection"),
                new EmptyBorder(
                GUIutilities.GAP_MARGIN,
                GUIutilities.GAP_MARGIN,
                GUIutilities.GAP_MARGIN,
                GUIutilities.GAP_MARGIN)
                ));
        managerPanel.add(panel1);
        managerPanel.add(panel2);
        
        
        this.setLayout(new BorderLayout(GUIutilities.GAP_COMPONENT,GUIutilities.GAP_COMPONENT));
        this.add(BorderLayout.CENTER, managerPanel);
        this.add(BorderLayout.EAST, controlPanel);
        
    }
    
    public String getSelectionMode() {
        return SelectionMode;
    }
    
    public TabbedPanel[] getSelectionIndexes() {
        return new TabbedPanel[]{(TabbedPanel)listModel.getElementAt(list1.getSelectedIndex()),
        (TabbedPanel)listModel.getElementAt(list2.getSelectedIndex())};
    }
    
    /* Method not used currently */
    private void initialiseMode() {
        int countRuleTree = 0;
        int countProcedure = 0;
        
        for(int i=0; i<pane.getTabCount(); i++) {
            String type = ((TabbedPanel)pane.getComponentAt(i)).getManager().getManagerType();
            if(type.equals(AbstractManager.ACCESS_MANAGER_TYPE))
                countRuleTree++;
            else if(type.equals(AbstractManager.PROCEDURE_MANAGER_TYPE))
                countProcedure++;
        }
        
        if(countProcedure == 0){
            procedureButton.setEnabled(false);
            SelectionMode = AbstractManager.ACCESS_MANAGER_TYPE;
            //Settings.setCompareMode(SelectionMode);
            ruletreeButton.setSelected(true);
        } 
        
        if(countRuleTree == 0){
            ruletreeButton.setEnabled(false);
            SelectionMode = AbstractManager.PROCEDURE_MANAGER_TYPE;
            //Settings.setCompareMode(SelectionMode);
            procedureButton.setSelected(true);
        }
    }
    
    class ManagerModel implements ListModel {
        private int counter = 0;
        private ArrayList<Integer> indexes;
        
        public ManagerModel() {
            indexes = new ArrayList<Integer>();
            for(int i=0; i<pane.getTabCount(); i++) {
                String type = ((TabbedPanel)pane.getComponentAt(i)).getManager().getManagerType();
                if(ruletreeButton.isSelected()) {
                    if(type.equals(AbstractManager.ACCESS_MANAGER_TYPE)){
                        counter++;
                        indexes.add(i);
                    }
                } else if(procedureButton.isSelected()) {
                    if(type.equals(AbstractManager.PROCEDURE_MANAGER_TYPE)) {
                        counter++;
                        indexes.add(i);
                    }
                }
            }
        }
        
        public int getSize() {
            return counter;
        }
        
        public Object getElementAt(int index) {
            return ((TabbedPanel)pane.getComponentAt(indexes.get(index)));
        }
        
        public void addListDataListener(ListDataListener l)  { }
        
        public void removeListDataListener(ListDataListener l) { }
    }
    
    class ManagerCellRenderer extends JLabel implements ListCellRenderer {
        
        public Component getListCellRendererComponent(
                JList list,
                Object value,            // value to display
                int index,               // cell index
                boolean isSelected,      // is the cell selected
                boolean cellHasFocus)    // the list and the cell have the focus
        {
            //DefaultListCellRenderer cell = (DefaultListCellRenderer)new DefaultListCellRenderer().getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            
            TabbedPanel tab = (TabbedPanel)value;
            setText(tab.getManager().getName());
            setToolTipText(tab.getManager().getId());
            
            if (isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }
            setEnabled(list.isEnabled());
            setFont(list.getFont());
            setOpaque(true);
            return this;
        }
    }
}
