package org.projectlauncher.gui;

import javax.swing.*;
import java.awt.*;

import org.projectlauncher.gui.platform.Windows11Glass;

public class GlassTest {

    public static void main(String[] args) {

        JFrame frame = new JFrame("Glass Test");

        frame.setUndecorated(true);
        frame.setSize(800, 500);
        frame.setLocationRelativeTo(null);

        // IMPORTANT
        frame.setBackground(new Color(0, 0, 0, 0));

        frame.getRootPane().setOpaque(false);

        JPanel panel = new JPanel() {

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                Graphics2D g2 = (Graphics2D) g.create();

                g2.setColor(new Color(0, 0, 0, 90));
                g2.fillRect(
                        0,
                        0,
                        getWidth(),
                        getHeight()
                );

                g2.dispose();
            }
        };


        panel.setOpaque(false);

        frame.setContentPane(panel);


        frame.setVisible(true);


        // Apply AFTER HWND exists
        Windows11Glass.apply(frame);
    }
}