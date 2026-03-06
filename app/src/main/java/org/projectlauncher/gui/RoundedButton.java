package org.projectlauncher.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class RoundedButton extends JButton {
    private int cornerRadius;

    public RoundedButton(String text, int radius) {
        super(text);
        this.cornerRadius = radius;
        setContentAreaFilled(false); // prevent default fill
        setFocusPainted(false);
        setBorderPainted(false);
        setOpaque(false); // IMPORTANT for transparency
        setForeground(Color.WHITE);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Optional hover effect
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) { repaint(); }
            @Override
            public void mouseExited(MouseEvent e) { repaint(); }
            @Override
            public void mousePressed(MouseEvent e) { repaint(); }
            @Override
            public void mouseReleased(MouseEvent e) { repaint(); }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        // Smooth corners
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Choose color based on state
        if (getModel().isPressed()) {
            g2.setColor(getBackground().darker());
        } else if (getModel().isRollover()) {
            g2.setColor(getBackground().brighter());
        } else {
            g2.setColor(getBackground());
        }

        // Draw the rounded rectangle
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);

        // Draw text centered
        FontMetrics fm = g2.getFontMetrics();
        int stringWidth = fm.stringWidth(getText());
        int stringHeight = fm.getAscent();
        int x = (getWidth() - stringWidth) / 2;
        int y = (getHeight() + stringHeight) / 2 - 2;
        g2.setColor(getForeground());
        g2.setFont(getFont());
        g2.drawString(getText(), x, y);

        g2.dispose();
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension size = super.getPreferredSize();
        size.width += cornerRadius;  // add some extra space for radius
        size.height += cornerRadius;
        return size;
    }
}