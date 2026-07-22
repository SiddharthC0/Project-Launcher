package org.projectlauncher.gui;

import javax.swing.*;
import java.awt.*;
import org.projectlauncher.instances.Instance;

public class InstanceItem extends RoundedPanel {

    public InstanceItem(Instance instance) {

        super(null, 30, Color.decode("#000000")); // layout, radius, color

        setLayout(null);
        setBounds(0, 0, 560, 65);

        JLabel nameLabel = new JLabel(instance.name);
        nameLabel.setBounds(15, 10, 300, 20);
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JLabel infoLabel = new JLabel(instance.type + " • " + instance.version);
        infoLabel.setBounds(15, 35, 300, 18);
        infoLabel.setForeground(Color.GRAY);
        infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        Window parent = SwingUtilities.getWindowAncestor(this);
        ModifyInstanceMenu mim = new ModifyInstanceMenu();
        RoundedButton modifyBtn = new RoundedButton("Modify", 15);
        modifyBtn.setBounds(445, 15, 100, 35);
        modifyBtn.setBackground(Color.decode("#111111"));
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