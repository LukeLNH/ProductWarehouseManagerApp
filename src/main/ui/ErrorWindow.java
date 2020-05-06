package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ErrorWindow {

    //EFFECTS: constructs a new JFrame containing an error message
    public ErrorWindow(String message) {
        JFrame errorWindow = new JFrame("Error");
        errorWindow.setSize(new Dimension(250,100));
        errorWindow.setLocationRelativeTo(null);
        JPanel errorPanel = new JPanel();
        errorPanel.setLayout(new BoxLayout(errorPanel, BoxLayout.Y_AXIS));

        JPanel errorMessagePanel = new JPanel();
        errorMessagePanel.add(new JLabel(message));

        JPanel errorButtonPanel = new JPanel();
        JButton okButton = new JButton("OK");
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                errorWindow.dispose();
            }
        });
        errorButtonPanel.add(okButton);
        errorPanel.add(errorMessagePanel);
        errorPanel.add(errorButtonPanel);
        errorWindow.add(errorPanel);

        errorWindow.setVisible(true);
    }
}
