package org.projectlauncher.gui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.util.HashMap;

import org.projectlauncher.gui.mod.LauncherModMenu;
import org.projectlauncher.instances.*;

public class LauncherInterfaceMain {

    public void launchInterface() {
        final String[] currentTab = { "Home" };
        LauncherData data = new LauncherData();

        // --- FRAME ---
        JFrame frame = new JFrame("Project Launcher : Beta Dev Test");
        frame.setSize(1000, 600);
        frame.setUndecorated(true);
        float arcWidth = 30f;
        float arcHeight = 30f;
        frame.setShape(new RoundRectangle2D.Double(0, 0, frame.getWidth(), frame.getHeight(), arcWidth, arcHeight));

        frame.setLocationRelativeTo(null);
        frame.getContentPane().setBackground(new Color(23, 23, 23));
        frame.setLayout(null);

        // --- DRAG WINDOW ---
        final Point[] mouse = { null };
        frame.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                mouse[0] = e.getPoint();
            }
        });
        frame.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                Point p = frame.getLocation();
                frame.setLocation(p.x + e.getX() - mouse[0].x, p.y + e.getY() - mouse[0].y);
            }
        });

        // --- WINDOW BUTTONS ---
        JButton minimize = createWindowButton(Color.decode("#171717"), "-");
        minimize.setBounds(925, 18, 30, 30);
        minimize.setToolTipText("Minimize");
        minimize.addActionListener(e -> frame.setState(Frame.ICONIFIED));

        JButton close = createWindowButton(Color.decode("#171717"), "X");
        close.setBounds(960, 18, 30, 30);
        close.setToolTipText("Close");
        close.addActionListener(e -> System.exit(0));

        frame.add(minimize);
        frame.add(close);

        // --- TITLE ---
        JLabel title = new JLabel("Project Launcher Beta Dev Test");
        title.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 26));
        title.setForeground(Color.decode("#efefef"));
        title.setBounds(20, 15, 500, 35);
        frame.add(title);

        // --- SIDEBAR ---
        JPanel sidebar = new JPanel();
        sidebar.setLayout(null);
        sidebar.setBackground(Color.decode("#171717"));
        sidebar.setBounds(0, 60, 180, 540);
        frame.add(sidebar);

        // --- ACCOUNT PANEL ---
        JPanel accountPanel = new JPanel();
        accountPanel.setLayout(null);
        accountPanel.setBackground(Color.decode("#171717"));
        accountPanel.setBounds(5, 460, 220, 90);
        accountPanel.setBorder(null);

        sidebar.add(accountPanel);

        // --- MAIN CONTENT AREA ---
        RoundedPanel contentPanel = new RoundedPanel(null, 30, Color.decode("#111111"));
        contentPanel.setBounds(200, 70, 820, 540);
        frame.add(contentPanel);

        // --- TAB PANELS ---
        HashMap<String, RoundedPanel> tabPanels = new HashMap<>();

        // HOME PANEL
        RoundedPanel homePanel = new RoundedPanel(null, 30, Color.decode("#111111"));
        homePanel.setBounds(0, 0, 820, 540);
        JLabel homeLabel = new JLabel("Welcome to project launcher!");
        homeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        homeLabel.setForeground(Color.WHITE);
        homeLabel.setBounds(20, 20, 400, 30);
        homePanel.add(homeLabel);

        String[] images = {
                "img1.png",
                "img2.png",
                "img3.jpg",
                "img4.jpg"
        };

        ImagePanel imagePanel = new ImagePanel(images);
        imagePanel.setBounds(20, 100, 700, 300);
        imagePanel.setBackground(Color.decode("#171717"));
        imagePanel.setBorder(null);
        homePanel.add(imagePanel);

        JLabel imgText = new JLabel("This is the beta developer test build. Some features may be missing...");
        imgText.setForeground(new Color(120, 120, 120));
        imgText.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        imgText.setBounds(20, 55, 500, 30);
        homePanel.add(imgText);

        RoundedButton play = new RoundedButton("PLAY", 25);
        play.setBounds(220, 420, 300, 50);
        play.setBackground(Color.decode("#171717"));
        play.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 20));
        homePanel.add(play);

        String[] instances = { "Default", "Dev", "Beta" };
        RoundedComboBox<String> instanceDropdown = new RoundedComboBox<>(instances, 25);
        instanceDropdown.setBounds(220, 480, 300, 30);
        instanceDropdown.setBackground(Color.decode("#171717"));
        instanceDropdown.setForeground(Color.WHITE);
        instanceDropdown.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));
        homePanel.add(instanceDropdown);

        tabPanels.put("Home", homePanel);
        contentPanel.add(homePanel);

        // OTHER TAB PANELS
        String[] otherTabs = { "Instances", "Account", "Settings", "Mods" };
        for (String tabName : otherTabs) {
            RoundedPanel panel = new RoundedPanel(null, 30, Color.decode("#111111"));
            panel.setBounds(0, 0, 820, 540);
            JLabel label = new JLabel("This is the " + tabName + " tab");
            label.setForeground(Color.WHITE);
            label.setFont(new Font("Segoe UI", Font.PLAIN, 22));
            label.setBounds(20, 20, 500, 30);
            panel.add(label);
            panel.setVisible(false);
            contentPanel.add(panel);
            tabPanels.put(tabName, panel);
        }

        LauncherInstancesMain instMenu = new LauncherInstancesMain();

        // --- SIDEBAR BUTTONS ---
        int y = 40;
        String[] tabs = { "Home", "Instances", "Account", "Settings", "Beta", "Mods" };
        for (String tab : tabs) {
            JButton btn = new JButton(tab);
            btn.setBounds(5, y, 220, 40);
            btn.setAlignmentX(JButton.LEFT_ALIGNMENT);
            btn.setFont(new Font("Segoe UI", Font.PLAIN, 20));
            btn.setFocusPainted(false);
            btn.setBorderPainted(false);
            btn.setBackground(Color.decode("#171717"));
            btn.setForeground(Color.decode("#efefef"));
            btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            btn.setBorder(new RoundedBorder(15));
            btn.setHorizontalAlignment(SwingConstants.LEFT);
            btn.setMargin(new Insets(0, 15, 0, 0));
            btn.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    btn.setBackground(Color.decode("#151515"));
                }

                public void mouseExited(MouseEvent e) {
                    btn.setBackground(Color.decode("#171717"));
                }
            });
            LauncherModMenu mods = new LauncherModMenu();

            btn.addActionListener(e -> {
                for (RoundedPanel p : tabPanels.values())
                    p.setVisible(false);
                tabPanels.get(tab).setVisible(true);

                currentTab[0] = tab; // store active tab
                if (tabPanels.get("Instances").isVisible()) {
                    instMenu.launchInstancesMenu(frame);
                } else if (tabPanels.get("Mods").isVisible()) {
                    mods.launchModMenu(frame);
                }
            });

            sidebar.add(btn);
            y += 60;
        }
        JLabel user = new JLabel(data.getUsername());
        user.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 16));
        user.setForeground(Color.WHITE);
        user.setBounds(15, 10, 180, 25);
        accountPanel.add(user);

        JLabel status = new JLabel(data.getStatus());
        status.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        status.setForeground(Color.WHITE);
        status.setBounds(15, 40, 180, 20);
        accountPanel.add(status);

        user.setText(data.getUsername());
        status.setText(data.getStatus());

        data.setUsername(user.getText());
        data.setStatus(status.getText());

        instanceDropdown.removeAllItems();

        for (Instance inst : InstanceManager.detectInstances()) {
            instanceDropdown.addItem(inst.name);
        }

        play.addActionListener(e -> {
            String selectedName = (String) instanceDropdown.getSelectedItem();
            new Thread(() -> {
                try {
                    for (Instance inst : InstanceManager.detectInstances()) {
                        if (inst.name.equals(selectedName)) {
                            org.projectlauncher.Main.launchInstance(inst);
                            return;
                        }

                    }
                    System.out.println("Instance not found: " + selectedName);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }).start();
        });

        // SETTINGS PANEL
        RoundedPanel settingsPanel = tabPanels.get("Settings");
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setForeground(Color.WHITE);
        usernameLabel.setBounds(20, 70, 150, 25);
        settingsPanel.add(usernameLabel);

        JTextField usernameField = new JTextField(data.getUsername());
        usernameField.setBounds(150, 70, 200, 25);
        settingsPanel.add(usernameField);

        JButton saveBtn = new JButton("Save Settings");
        saveBtn.setBounds(150, 110, 200, 30);
        saveBtn.setBackground(Color.decode("#171717"));
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setFocusPainted(false);
        saveBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        saveBtn.addActionListener(e -> {
            data.setUsername(usernameField.getText());
            data.save();
            user.setText(data.getUsername());
        });
        settingsPanel.add(saveBtn);

        frame.setVisible(true);
    }

    // --- HELPER METHODS ---
    private JButton createWindowButton(Color color, String text) {
        JButton btn = new JButton();
        btn.setText(text);
        btn.setBackground(color);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(new RoundedBorder(10));
        btn.setForeground(Color.WHITE);
        return btn;
    }

    static class RoundedBorder implements Border {
        private int radius;

        RoundedBorder(int radius) {
            this.radius = radius;
        }

        public Insets getBorderInsets(Component c) {
            return new Insets(radius + 1, radius + 1, radius + 2, radius);
        }

        public boolean isBorderOpaque() {
            return false;
        }

        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            g.setColor(new Color(250, 250, 250));
            g.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
        }
    }
}