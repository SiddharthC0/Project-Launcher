package org.projectlauncher.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

import org.projectlauncher.instances.InstanceManager;
import org.projectlauncher.instances.Instance;

public class LauncherInstancesMain {

    public void launchInstancesMenu(JFrame parent) {

        JDialog dialog = new JDialog(parent, "Instances", true); // MODAL
        dialog.setSize(600, 700);
        dialog.setUndecorated(true);

        float arc = 30f;
        dialog.setShape(new RoundRectangle2D.Double(0, 0, 600, 700, arc, arc));

        dialog.setLocationRelativeTo(parent);
        dialog.getContentPane().setBackground(Color.decode("#000000"));
        dialog.setLayout(null);

        JLabel title = new JLabel("Instances");
        title.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 24));
        title.setForeground(Color.WHITE);
        title.setBounds(20, 20, 300, 40);
        dialog.add(title);

        JButton close = new JButton("X");
        close.setBounds(550, 15, 50, 50);
        close.setForeground(Color.WHITE);
        close.setFont(new Font("Arial", Font.BOLD, 16));
        close.setBackground(null);
        close.setFocusable(false);
        close.setBorder(null);
        close.addActionListener(e -> dialog.dispose());
        dialog.add(close);

        JButton newI = new JButton("+");
        newI.setBounds(490, 15, 50, 50);
        newI.setForeground(Color.WHITE);
        newI.setFont(new Font("Arial", Font.BOLD, 16));
        newI.setBackground(null);
        newI.setFocusable(false);
        newI.setBorder(null);
        newI.addActionListener(e -> {
            NInstancePanel newInst = new NInstancePanel();
            newInst.launchNInstancePanel(parent);
        });
        dialog.add(newI);

        InstanceListPanel listPanel = new InstanceListPanel();

        JScrollPane scroll = new JScrollPane(listPanel);
        scroll.setBounds(10, 60, 580, 680);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(14);

        dialog.add(scroll);

        for (Instance inst : InstanceManager.detectInstances()) {

            listPanel.addInstance(inst);
        }

        dialog.setVisible(true);
    }
}