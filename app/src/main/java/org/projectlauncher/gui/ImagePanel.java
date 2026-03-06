package org.projectlauncher.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class ImagePanel extends JPanel {
    private Image image;
    private double zoom = 2.0; // 1.0 = fit mode
    private int cornerRadius = 30; // radius of rounded corners

    public ImagePanel(String imagePath) {
        this.image = new ImageIcon(imagePath).getImage();
        setLayout(null); // keep absolute positioning
        setOpaque(false); // important for smooth rounded edges
    }

    /** Set zoom factor (1.0 = fit, 2.0 = 200%, 0.5 = 50%) */
    public void setZoom(double zoom) {
        if (zoom <= 0) return;
        this.zoom = zoom;
        repaint();
    }

    /** Set corner radius of the panel */
    public void setCornerRadius(int radius) {
        this.cornerRadius = radius;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (image != null) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int panelWidth = getWidth();
            int panelHeight = getHeight();

            // Clip to rounded rectangle
            RoundRectangle2D rounded = new RoundRectangle2D.Float(0, 0, panelWidth, panelHeight, cornerRadius, cornerRadius);
            g2.setClip(rounded);

            int imgWidth = image.getWidth(null);
            int imgHeight = image.getHeight(null);

            // Compute scale while keeping aspect ratio, then apply zoom
            double scale = Math.min((double) panelWidth / imgWidth, (double) panelHeight / imgHeight) * zoom;

            int drawWidth = (int) (imgWidth * scale);
            int drawHeight = (int) (imgHeight * scale);

            // Center the image
            int x = (panelWidth - drawWidth) / 2;
            int y = (panelHeight - drawHeight) / 2;

            g2.drawImage(image, x, y, drawWidth, drawHeight, this);
            g2.dispose();
        }
    }
}