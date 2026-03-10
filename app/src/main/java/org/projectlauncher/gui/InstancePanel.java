package org.projectlauncher.gui;

import javax.swing.JPanel;
import java.awt.*;

public class InstancePanel extends JPanel {

    public InstancePanel(int yPos, String instanceName, String instanceLocation, String instanceType, String instanceState) {

        setLayout(null);
        setBounds(10, yPos, 580, 65);

        RoundedPanel panel = new RoundedPanel(null, 30, Color.decode("#111111"));
        panel.setBounds(0, 0, 580, 65);
        add(panel);
    }
}
