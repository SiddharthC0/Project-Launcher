package org.projectlauncher.gui;

import java.awt.geom.RoundRectangle2D;
import org.projectlauncher.instances.*;

import javax.swing.*;
import java.awt.*;

public class ModifyInstanceMenu {
    public void launchModifyInstanceMenu(Window parent, Instance instance) {
        String wTitle = "Modify Instance : " + instance.name;
        JDialog dialog = new JDialog(parent, wTitle, Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setSize(800, 500);
        dialog.setUndecorated(true);

        float arc = 30f;
        dialog.setShape(new RoundRectangle2D.Double(0, 0, 800, 500, arc, arc));

        dialog.setLocationRelativeTo(parent);
        dialog.getContentPane().setBackground(Color.decode("#000000"));
        dialog.setLayout(null);

        JLabel title = new JLabel(wTitle);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 24));
        title.setLayout(null);
        title.setBounds(20, 20, 500, 50);
        dialog.add(title);

        JButton close = new JButton("X");
        close.setBounds(730, 15, 50, 50);
        close.setForeground(Color.WHITE);
        close.setFont(new Font("Arial", Font.BOLD, 16));
        close.setBackground(null);
        close.setFocusable(false);
        close.setBorder(null);
        close.addActionListener(e -> dialog.dispose());
        dialog.add(close);
        dialog.setVisible(true);
    }
}
