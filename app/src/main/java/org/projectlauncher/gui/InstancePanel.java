package org.projectlauncher.gui;

import javax.swing.JPanel;
import java.awt.*;
import org.projectlauncher.utils.*;

public class InstancePanel extends JPanel {
    ThemeDetector.Theme theme = ThemeDetector.getTheme();

    Color backgroundColor1 = theme.backgroundColor1;
    Color backgroundColor2 = theme.backgroundColor2;
    Color textColor1 = theme.textColor1;
    Color textColor2 = theme.textColor2;
    Color buttonColor = theme.buttonColor;
    public InstancePanel(int yPos, String instanceName, String instanceLocation, String instanceType,
            String instanceState) {

        setLayout(null);
        setBounds(10, yPos, 580, 65);

        RoundedPanel panel = new RoundedPanel(null, 30);
        panel.setBounds(0, 0, 580, 65);
        add(panel);
    }
}
