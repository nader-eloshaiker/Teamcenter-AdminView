/*
 * CompareTabChooser.java
 *
 * Created on 4 March 2008, 09:09
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tceav.gui.compare;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.io.File;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import tceav.gui.tools.CustomFileFilter;
import tceav.gui.tools.GUIutilities;
import tceav.manager.ManagerAdapter;
import tceav.Settings;
import tceav.gui.*;
import tceav.resources.*;

/**
 *
 * @author nzr4dl
 */
public class CompareTabChooser extends JPanel {
    
    private File[] files;
    private JTextField list1;
    private JTextField list2;
    private JRadioButton ruletreeButton;
    private JRadioButton procedureButton;
    private String SelectionMode;
    private AdminViewFrame parentFrame;
    
    /** Creates a new instance of CompareTabChooser */
    public CompareTabChooser(AdminViewFrame parent) {
        super();
        this.parentFrame = parent;
        
        files = new File[2];
        SelectionMode = Settings.getCompareMode();
        
        ruletreeButton = new JRadioButton(
                "RuleTree",
                (SelectionMode.equals(ManagerAdapter.ACCESS_MANAGER_TYPE)));
        ruletreeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(ruletreeButton.isSelected()) {
                    SelectionMode = ManagerAdapter.ACCESS_MANAGER_TYPE;
                    Settings.setCompareMode(SelectionMode);
                }
            }
        });
        procedureButton = new JRadioButton(
                "Procedure",
                (SelectionMode.equals(ManagerAdapter.PROCEDURE_MANAGER_TYPE)));
        procedureButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(procedureButton.isSelected()) {
                    SelectionMode = ManagerAdapter.PROCEDURE_MANAGER_TYPE;
                    Settings.setCompareMode(SelectionMode);
                }
            }
        });
        procedureButton.setEnabled(false);
        
        ButtonGroup group = new ButtonGroup();
        group.add(ruletreeButton);
        group.add(procedureButton);
        
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(2,1,GUIutilities.GAP_COMPONENT,GUIutilities.GAP_COMPONENT));
        buttonPanel.setBorder(new CompoundBorder(
                new TitledBorder(new EtchedBorder(),"Compare Mode"),
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
        
        list1 = new JTextField();
        list1.setColumns(20);
        list1.setEditable(false);
        JLabel label1 = new JLabel("1st File:");
        label1.setHorizontalAlignment(JLabel.RIGHT);
        JButton button1 = new JButton("Choose File");
        button1.addActionListener( new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = createFileChooser();
                int result = fc.showOpenDialog(parentFrame);
                if(result == JFileChooser.APPROVE_OPTION) {
                    files[0] = fc.getSelectedFile();
                    list1.setText(files[0].getName());
                }
            }
        });
        JPanel panel1 = new JPanel();
        panel1.setLayout(new BorderLayout(GUIutilities.GAP_COMPONENT,GUIutilities.GAP_COMPONENT));
        panel1.add(BorderLayout.CENTER, list1);
        panel1.add(BorderLayout.WEST, label1);
        panel1.add(BorderLayout.EAST, button1);
        
        list2 = new JTextField();
        list2.setColumns(20);
        list2.setEditable(false);
        JLabel label2 = new JLabel("2nd File:");
        label2.setHorizontalAlignment(JLabel.RIGHT);
        label1.setPreferredSize(label2.getPreferredSize());
        JButton button2 = new JButton("Choose File");
        button2.addActionListener( new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = createFileChooser();
                int result = fc.showOpenDialog(parentFrame);
                if(result == JFileChooser.APPROVE_OPTION) {
                    files[1] = fc.getSelectedFile();
                    list2.setText(files[1].getName());
                }
            }
        });
        JPanel panel2 = new JPanel();
        panel2.setLayout(new BorderLayout(GUIutilities.GAP_COMPONENT,GUIutilities.GAP_COMPONENT));
        panel2.add(BorderLayout.CENTER, list2);
        panel2.add(BorderLayout.WEST, label2);
        panel2.add(BorderLayout.EAST, button2);
        
        JPanel managerPanel = new JPanel();
        managerPanel.setLayout(new GridLayout(2,1,GUIutilities.GAP_COMPONENT,GUIutilities.GAP_COMPONENT));
        managerPanel.setBorder(new CompoundBorder(
                new TitledBorder(new EtchedBorder(),"File Selection"),
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
    
    public File[] getSelectedFiles() {
        return files;
    }
    
    private JFileChooser createFileChooser() {
        String path = "";
        JFileChooser fc = new JFileChooser();
        
        if(ruletreeButton.isSelected()) {
            path = Settings.getAmLoadPath();
            fc.setCurrentDirectory(new File(path));
            fc.addChoosableFileFilter(new CustomFileFilter(
                    new String[]{"txt",""},"Text File (*.txt; *.)"));
        } else if(procedureButton.isSelected()) {
            path = Settings.getPmLoadPath();
            fc.setCurrentDirectory(new File(path));
            fc.addChoosableFileFilter(new CustomFileFilter(
                        new String[]{"xml","plmxml"},"XML File (*.xml; *.plmxml)"));
        }
        
        return fc;
    }
    
}
