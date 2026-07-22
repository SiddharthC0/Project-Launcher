package org.projectlauncher.gui.loader;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class LoadingFrame {

    JFrame window = new JFrame("PL Loading");
    JPanel loaderIn = new JPanel();

    JPanel loader = new JPanel() {
        int pos = 0;
        int dotSize = 10;

        {
            new javax.swing.Timer(12, e -> {
                pos += 10;
                if (pos > 500)
                    pos = 0;
                repaint();
            }).start();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            g.setColor(Color.decode("#000000"));
            g.fillRect(0, 0, 500, 12);

            g.setColor(Color.WHITE);
            g.fillRect(pos, 1, 50, 10);
        }
    };

    public void launchLoader() {

        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(500, 260);
        window.setLocationRelativeTo(null);
        window.setUndecorated(true);
        window.setBackground(Color.decode("#000000"));

        JLabel title = new JLabel("Project");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Arial", Font.BOLD, 70));
        title.setLayout(null);
        title.setBounds(120, 40, 500, 100);

        JLabel title2 = new JLabel("Launcher");
        title2.setForeground(Color.WHITE);
        title2.setFont(new Font("Arial", Font.BOLD, 70));
        title2.setLayout(null);
        title2.setBounds(90, 130, 500, 100);

        JPanel bg = new JPanel(null);
        bg.setBackground(Color.decode("#000000"));

        loader.setBounds(0, 250, 500, 12);
        loader.setOpaque(false);

        bg.add(title2);
        bg.add(title);
        bg.add(loader);

        window.add(bg);
        window.setVisible(true);
    }

    public void destroy() {
        window.setVisible(false);
    }
}