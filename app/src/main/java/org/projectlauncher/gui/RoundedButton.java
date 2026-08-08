package org.projectlauncher.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class RoundedButton extends JButton {

    private int cornerRadius;

    public RoundedButton(String text, int radius) {

        super(text);

        this.cornerRadius = radius;

        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setOpaque(false);
        setDoubleBuffered(true);

        setForeground(Color.WHITE);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                repaint();
            }
        });
    }


    @Override
    protected void paintComponent(Graphics g) {

        Graphics2D g2 = (Graphics2D) g.create();

        // Clear previous translucent frames
        g2.setComposite(AlphaComposite.Clear);
        g2.fillRect(0, 0, getWidth(), getHeight());

        g2.setComposite(AlphaComposite.SrcOver);


        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        g2.setRenderingHint(
                RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY
        );


        Color bg = getBackground();

        Color drawColor = bg;


        // Hover effect without changing alpha
        if (getModel().isPressed()) {

            drawColor = new Color(
                    Math.max(bg.getRed() - 30, 0),
                    Math.max(bg.getGreen() - 30, 0),
                    Math.max(bg.getBlue() - 30, 0),
                    bg.getAlpha()
            );

        } else if (getModel().isRollover()) {

            drawColor = new Color(
                    Math.min(bg.getRed() + 30, 255),
                    Math.min(bg.getGreen() + 30, 255),
                    Math.min(bg.getBlue() + 30, 255),
                    bg.getAlpha()
            );
        }


        g2.setColor(drawColor);

        g2.fillRoundRect(
                0,
                0,
                getWidth(),
                getHeight(),
                cornerRadius,
                cornerRadius
        );


        // Draw text
        g2.setFont(getFont());
        g2.setColor(getForeground());

        FontMetrics fm = g2.getFontMetrics();

        int textWidth = fm.stringWidth(getText());
        int textHeight = fm.getAscent();

        int x = (getWidth() - textWidth) / 2;
        int y = (getHeight() + textHeight) / 2 - 2;

        g2.drawString(
                getText(),
                x,
                y
        );


        g2.dispose();
    }


    @Override
    public Dimension getPreferredSize() {

        Dimension size = super.getPreferredSize();

        size.width += cornerRadius;
        size.height += cornerRadius;

        return size;
    }
}