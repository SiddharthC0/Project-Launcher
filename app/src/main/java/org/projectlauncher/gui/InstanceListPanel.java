package org.projectlauncher.gui;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import org.projectlauncher.instances.*;
import org.projectlauncher.utils.*;

public class InstanceListPanel extends JPanel {
    static ThemeDetector.Theme theme = ThemeDetector.getTheme();

    static Color backgroundColor1 = theme.backgroundColor1;
    static Color backgroundColor2 = theme.backgroundColor2;
    Color textColor1 = theme.textColor1;
    Color textColor2 = theme.textColor2;
    Color buttonColor = theme.buttonColor;
    int currentY = 10;

    public InstanceListPanel() {
        setLayout(null);
        setBackground(backgroundColor1);
    }

    @Override
    public void addNotify() {
        super.addNotify();

        JScrollPane scroll = (JScrollPane) SwingUtilities.getAncestorOfClass(JScrollPane.class, this);

        if (scroll != null) {
            JScrollBar bar = scroll.getVerticalScrollBar();
            bar.setUI(new ModernScrollBarUI());
            bar.setPreferredSize(new Dimension(8, Integer.MAX_VALUE));
            bar.setUnitIncrement(10);
            bar.setOpaque(false);
        }
    }

    public void addInstance(Instance instance) {

        InstanceItem item = new InstanceItem(instance);
        item.setBounds(10, currentY, 560, 65);

        add(item);

        currentY += 75;

        setPreferredSize(new Dimension(580, currentY));
        revalidate();
        repaint();
    }

    static class ModernScrollBarUI extends BasicScrollBarUI {

        @Override
        protected void configureScrollBarColors() {
            thumbColor = new Color(70, 70, 70);
            trackColor = new Color(0, 0, 0);
        }

        @Override
        protected JButton createDecreaseButton(int orientation) {
            return createInvisibleButton();
        }

        @Override
        protected JButton createIncreaseButton(int orientation) {
            return createInvisibleButton();
        }

        private JButton createInvisibleButton() {
            JButton btn = new JButton();
            btn.setPreferredSize(new Dimension(0, 0));
            btn.setMinimumSize(new Dimension(0, 0));
            btn.setMaximumSize(new Dimension(0, 0));
            return btn;
        }

        @Override
        protected void paintThumb(Graphics g, JComponent c, Rectangle r) {

            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(new Color(80, 80, 80));
            g2.fillRoundRect(r.x + 2, r.y, r.width - 4, r.height, 10, 10);

            g2.dispose();
        }

        @Override
        protected void paintTrack(Graphics g, JComponent c, Rectangle r) {
            g.setColor(backgroundColor1);
            g.fillRect(r.x, r.y, r.width, r.height);
        }
    }
}