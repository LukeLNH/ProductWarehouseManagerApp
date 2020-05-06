package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoadingFrame {
    String warehouseName;

    public LoadingFrame() {
        makeLoadFrame();
    }

    private void makeLoadFrame() {
        JFrame loadFrame = new JFrame("Load Warehouse");
        loadFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loadFrame.setSize(400,250);
        loadFrame.setResizable(false);
        loadFrame.setLocationRelativeTo(null);

        JPanel loadPanel = new JPanel();
        loadPanel.add(new JPanel()); //widget
        loadPanel.add(selectLabelPanel());
        loadPanel.add(cbPanel());
        loadPanel.add(loadButtonPanel());
        loadPanel.setLayout(new BoxLayout(loadPanel, BoxLayout.Y_AXIS));

        loadFrame.add(loadPanel);

        loadFrame.setVisible(true);
    }

    private JPanel loadButtonPanel() {
        JPanel loadButtonPanel = new JPanel();
        JButton loadButton = new JButton("Load");
        loadButton.setPreferredSize(new Dimension(200,30));
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new GUI(warehouseName);
            }
        });
        loadButtonPanel.add(loadButton);
        return loadButtonPanel;
    }

    private JPanel cbPanel() {
        JPanel cbPanel = new JPanel();
        String[] names = new String[] {"TechWarehouse", "SportsWarehouse", "ClothesWarehouse"};
        JComboBox cb = new JComboBox(names);
        cb.setPreferredSize(new Dimension(300, 50));
        cb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                warehouseName = (String) cb.getSelectedItem();
            }
        });
        cb.setSelectedIndex(0);
        cbPanel.add(cb);
        return cbPanel;
    }

    private JPanel selectLabelPanel() {
        JPanel labelPanel = new JPanel();
        JLabel label = new JLabel("Select the Warehouse to load:");
        labelPanel.add(label);
        return labelPanel;
    }
}
