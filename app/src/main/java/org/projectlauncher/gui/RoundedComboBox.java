package org.projectlauncher.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;
import java.awt.*;

public class RoundedComboBox<E> extends JComboBox<E> {
    private int cornerRadius;

    public RoundedComboBox(E[] items, int radius) {
        super(items);
        this.cornerRadius = radius;

        setOpaque(false);
        setFocusable(false);
        setBorder(null);
        setBackground(Color.decode("#111111"));
        setForeground(Color.WHITE);
        setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));

        // Custom renderer: dark background, white text, padding
        setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                JLabel lbl = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                lbl.setOpaque(true);
                lbl.setBackground(Color.decode("#111111"));
                lbl.setForeground(Color.WHITE);
                lbl.setBorder(new EmptyBorder(5, 10, 5, 10));
                return lbl;
            }
        });

        // Custom UI to round arrow button & remove ugly borders
        setUI(new BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                JButton button = new JButton("\u25BC");
                button.setBorder(null);
                button.setContentAreaFilled(false);
                button.setFocusPainted(false);
                button.setForeground(Color.WHITE);
                return button;
            }

            @Override
            public void configureArrowButton() {
                super.configureArrowButton();
                arrowButton.setBorder(null);
            }

            @Override
            protected void installListeners() {
                super.installListeners();
                comboBox.setFocusable(false);
            }

            @Override
            public void paintCurrentValueBackground(Graphics g, Rectangle bounds, boolean hasFocus) {
                // Draw fully rounded background
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(comboBox.getBackground());
                g2.fillRoundRect(0, 0, comboBox.getWidth(), comboBox.getHeight(), cornerRadius, cornerRadius);
                g2.dispose();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
        super.paintComponent(g2);
        g2.dispose();
    }
}