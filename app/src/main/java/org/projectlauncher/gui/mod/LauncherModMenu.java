package org.projectlauncher.gui.mod;

import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

import javax.swing.plaf.basic.BasicScrollBarUI;

import org.projectlauncher.gui.RoundedPanel;

import java.awt.geom.RoundRectangle2D;
import java.awt.*;

public class LauncherModMenu {

    public void launchModMenu(JFrame parent) {

        JDialog dialog = new JDialog(parent, "Mods", true); // MODAL
        dialog.setSize(600, 700);
        dialog.setUndecorated(true);

        float arc = 30f;
        dialog.setShape(new RoundRectangle2D.Double(0, 0, 600, 700, arc, arc));

        dialog.setLocationRelativeTo(parent);
        dialog.getContentPane().setBackground(new Color(23, 23, 23));
        dialog.setLayout(null);

        // ---------- Title ----------
        JLabel title = new JLabel("Mods");
        title.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 24));
        title.setForeground(Color.WHITE);
        title.setBounds(20, 20, 300, 40);
        dialog.add(title);

        // ---------- Close button ----------
        JButton close = new JButton("X");
        close.setBounds(550, 15, 50, 50);
        close.setForeground(Color.WHITE);
        close.setFont(new Font("Arial", Font.BOLD, 16));
        close.setBackground(null);
        close.setFocusable(false);
        close.setBorder(null);
        close.addActionListener(e -> dialog.dispose());
        dialog.add(close);

        // ---------- Mods panel + scroll ----------
        JPanel modsPanel = new JPanel();
        modsPanel.setLayout(new BoxLayout(modsPanel, BoxLayout.Y_AXIS));
        modsPanel.setBackground(new Color(23, 23, 23));

        JScrollPane scrollPane = new JScrollPane(modsPanel);
        scrollPane.setBounds(20, 80, 560, 580);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(null);
        dialog.add(scrollPane);

        if (scrollPane != null) {
            JScrollBar bar = scrollPane.getVerticalScrollBar();
            bar.setUI(new ModernScrollBarUI());
            bar.setPreferredSize(new Dimension(8, Integer.MAX_VALUE));
            bar.setUnitIncrement(10);
            bar.setOpaque(false);
        }

        // ---------- Read mods ----------
        List<Mod> modList = ModsReader.readMods();

        for (Mod mod : modList) {
            JPanel modEntry = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
            modEntry.setBackground(Color.decode("#000000"));
            modEntry.setMaximumSize(new Dimension(540, 60));
            JLabel iconLabel = new JLabel(mod.icon != null ? mod.icon : new ImageIcon("default_icon.png"));
            JLabel nameLabel = new JLabel(mod.name);
            nameLabel.setForeground(Color.WHITE);
            nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));

            modEntry.add(iconLabel);
            modEntry.add(nameLabel);

            // Tooltip with version + description
            String tooltip = "";
            if (!mod.version.isEmpty())
                tooltip += "Version: " + mod.version + "\n";
            if (!mod.description.isEmpty())
                tooltip += mod.description;
            if (!tooltip.isEmpty())
                modEntry.setToolTipText("<html>" + tooltip.replaceAll("\n", "<br>") + "</html>");

            modsPanel.add(modEntry);
        }

        dialog.setVisible(true);
    }

    static class ModernScrollBarUI extends BasicScrollBarUI {

        @Override
        protected void configureScrollBarColors() {
            thumbColor = new Color(70, 70, 70);
            trackColor = new Color(23, 23, 23);
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
            g.setColor(new Color(23, 23, 23));
            g.fillRect(r.x, r.y, r.width, r.height);
        }
    }
}