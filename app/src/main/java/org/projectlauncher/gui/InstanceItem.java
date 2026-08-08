package org.projectlauncher.gui;

import javax.swing.*;
import java.awt.*;
import org.projectlauncher.instances.Instance;
import org.projectlauncher.utils.ThemeDetector;

public class InstanceItem extends RoundedPanel {
    static ThemeDetector.Theme theme = ThemeDetector.getTheme();

    static Color backgroundColor1 = theme.backgroundColor1;
    static Color backgroundColor2 = theme.backgroundColor2;
    static Color textColor1 = theme.textColor1;
    static Color textColor2 = theme.textColor2;
    static Color buttonColor = theme.buttonColor;
    public InstanceItem(Instance instance) {
        super(null, 30); // layout, radius, color

        setLayout(null);
        setBounds(0, 0, 560, 65);

        JLabel nameLabel = new JLabel(instance.name);
        nameLabel.setBounds(15, 10, 300, 20);
        nameLabel.setForeground(textColor2);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JLabel infoLabel = new JLabel(instance.type + " • " + instance.version);
        infoLabel.setBounds(15, 35, 300, 18);
        infoLabel.setForeground(textColor1);
        infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        Window parent = SwingUtilities.getWindowAncestor(this);
        ModifyInstanceMenu mim = new ModifyInstanceMenu();
        RoundedButton modifyBtn = new RoundedButton("Modify", 15);
        modifyBtn.setBounds(445, 15, 100, 35);
        modifyBtn.setBackground(buttonColor);
        modifyBtn.setForeground(Color.WHITE);
        modifyBtn.addActionListener(e -> {
            mim.launchModifyInstanceMenu(parent, instance);
            System.out.println("YO THEY ACCESED IT!");
        });

        add(modifyBtn);
        add(nameLabel);
        add(infoLabel);
    }
}