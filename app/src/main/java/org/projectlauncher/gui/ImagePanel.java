package org.projectlauncher.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class ImagePanel extends JPanel {

    private Image[] images;
    private int currentIndex = 0;
    private int nextIndex = 1;

    private float alpha = 0f; // transition progress
    private Timer timer;

    private double zoom = 2.0;
    private int cornerRadius = 30;

    public ImagePanel(String[] imagePaths) {

        images = new Image[imagePaths.length];

        for (int i = 0; i < imagePaths.length; i++) {
            images[i] = new ImageIcon(imagePaths[i]).getImage();
        }

        setOpaque(false);

        startSlideshow();
    }

    private void startSlideshow() {

        timer = new Timer(66, e -> { // ~60 FPS

            alpha += 0.02f;

            if (alpha >= 1f) {
                alpha = 0f;

                currentIndex = nextIndex;
                nextIndex = (nextIndex + 1) % images.length;
            }

            repaint();

        });

        timer.start();
    }

    public void setZoom(double zoom) {
        if (zoom <= 0) return;
        this.zoom = zoom;
        repaint();
    }

    public void setCornerRadius(int radius) {
        this.cornerRadius = radius;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        if (images.length == 0) return;

        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int panelWidth = getWidth();
        int panelHeight = getHeight();

        RoundRectangle2D rounded = new RoundRectangle2D.Float(0, 0, panelWidth, panelHeight, cornerRadius, cornerRadius);
        g2.setClip(rounded);

        drawImage(g2, images[currentIndex], 1f - alpha);
        drawImage(g2, images[nextIndex], alpha);

        g2.dispose();
    }

    private void drawImage(Graphics2D g2, Image image, float alpha) {

        int panelWidth = getWidth();
        int panelHeight = getHeight();

        int imgWidth = image.getWidth(null);
        int imgHeight = image.getHeight(null);

        double scale = Math.min((double) panelWidth / imgWidth, (double) panelHeight / imgHeight) * zoom;

        int drawWidth = (int) (imgWidth * scale);
        int drawHeight = (int) (imgHeight * scale);

        int x = (panelWidth - drawWidth) / 2;
        int y = (panelHeight - drawHeight) / 2;

        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g2.drawImage(image, x, y, drawWidth, drawHeight, this);
    }
}