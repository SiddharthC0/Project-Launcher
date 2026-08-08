package org.projectlauncher.gui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.util.HashMap;
import java.io.*;
import org.projectlauncher.gui.TransparentFrameImplementation;
import org.projectlauncher.gui.mod.LauncherModMenu;
import org.projectlauncher.instances.*;
import org.projectlauncher.utils.*;

import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.Native;
import javax.swing.plaf.basic.BasicScrollBarUI;

public class LauncherInterfaceMain {

    JProgressBar loader = new JProgressBar(0, 100);
    public void launchInterface() {

        ThemeDetector.Theme theme = ThemeDetector.getTheme();

        Color backgroundColor1 = theme.backgroundColor1;
        Color backgroundColor2 = theme.backgroundColor2;
        Color textColor1 = theme.textColor1;
        Color textColor2 = theme.textColor2;
        Color buttonColor = theme.buttonColor;
        Color actualColor = theme.actualColor;
        loader.setStringPainted(true);
        loader.setForeground(Color.WHITE);
        loader.setBackground(backgroundColor1);
        final String[] currentTab = { "Home" };
        LauncherData data = LauncherData.load();
        JFrame frame = new JFrame("Project Launcher");
        frame.setSize(1200, 600);
        frame.setUndecorated(true);
        float arcWidth = 0f;
        float arcHeight = 0f;

        Image icon = new ImageIcon(
                LauncherInterfaceMain.class.getResource("/pl_logo.png")
        ).getImage();

        frame.setIconImage(icon);
        TransparentFrameImplementation.makeTransparent(frame);
        // frame.setShape(new RoundRectangle2D.Double(0, 0, frame.getWidth(), frame.getHeight(), arcWidth, arcHeight));

        frame.setLocationRelativeTo(null);
        frame.getContentPane().setBackground(backgroundColor2);
        frame.setLayout(null);
        frame.getRootPane().setOpaque(false);
        frame.getContentPane().setBackground(backgroundColor2);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowActivated(WindowEvent e) {
                System.out.println("WINDOW ACTIVE");
            }

            @Override
            public void windowDeactivated(WindowEvent e) {
                System.out.println("WINDOW LOST FOCUS");
            }
        });


        JPanel titleBar = new JPanel(null);
        titleBar.setBounds(0, 0, 1200, 60);
        titleBar.setBackground(actualColor);

        frame.add(titleBar);
        final Point[] mouse = {null};

        titleBar.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mouse[0] = e.getPoint();
            }
        });

        titleBar.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                Point p = frame.getLocation();

                frame.setLocation(
                        p.x + e.getX() - mouse[0].x,
                        p.y + e.getY() - mouse[0].y
                );
            }
        });


        JButton minimize = createWindowButton(backgroundColor2, "-");
        minimize.setToolTipText("Minimize");
        minimize.addActionListener(e -> frame.setState(Frame.ICONIFIED));

        JButton close = createWindowButton(backgroundColor2, "X");
        close.setToolTipText("Close");
        close.addActionListener(e -> System.exit(0));

        minimize.setBounds(1100, 18, 30, 30);
        close.setBounds(1140, 18, 30, 30);

        titleBar.add(minimize);
        titleBar.add(close);

        JLabel title = new JLabel("PROJECT LAUNCHER");
        title.setFont(FontLoader.loadFont(getClass().getResource("/fonts/poppins/main.ttf"),28));

        title.setForeground(textColor1);
        title.setBounds(20, 15, 500, 35);
        titleBar.add(title);
        JPanel sidebar = new JPanel();
        sidebar.setLayout(null);
        sidebar.setBackground(backgroundColor2);
        sidebar.setBounds(0, 60, 180, 540);
        sidebar.setLayout(null);
        frame.add(sidebar);


        JPanel instancesBar = new JPanel();
        instancesBar.setLayout(null);
        instancesBar.setBackground(backgroundColor2);

        JScrollPane instanceScroll = new JScrollPane(instancesBar);

        instanceScroll.setBounds(990, 70, 200, 520);
        instanceScroll.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        instanceScroll.setBorder(null);
        instanceScroll.setViewportBorder(null);

        instanceScroll.getViewport().setBackground(backgroundColor2);

        instanceScroll.setOpaque(false);
        instanceScroll.getViewport().setOpaque(false);
        instanceScroll.getVerticalScrollBar().setUI(new BasicScrollBarUI() {

            @Override
            protected void configureScrollBarColors() {
                thumbColor = buttonColor;
                trackColor = backgroundColor2;
            }

            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createZeroButton();
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return createZeroButton();
            }

            private JButton createZeroButton() {
                JButton b = new JButton();
                b.setPreferredSize(new Dimension(0, 0));
                b.setMinimumSize(new Dimension(0, 0));
                b.setMaximumSize(new Dimension(0, 0));
                return b;
            }

            @Override
            protected void paintTrack(Graphics g, JComponent c, Rectangle r) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(backgroundColor2);
                g2.fillRect(r.x, r.y, r.width, r.height);
                g2.dispose();
            }

            @Override
            protected void paintThumb(Graphics g, JComponent c, Rectangle r) {
                if (r.isEmpty() || !scrollbar.isEnabled())
                    return;

                Graphics2D g2 = (Graphics2D) g.create();

                g2.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(buttonColor);

                g2.fillRoundRect(
                        r.x + 3,
                        r.y + 3,
                        r.width - 6,
                        r.height - 6,
                        10,
                        10);

                g2.dispose();
            }
        });
        frame.add(instanceScroll);

        JPanel accountPanel = new JPanel();
        accountPanel.setLayout(null);
        accountPanel.setBackground(backgroundColor2);
        accountPanel.setBounds(5, 460, 220, 90);
        accountPanel.setBorder(null);

        sidebar.add(accountPanel);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(null);
        contentPanel.setBackground(backgroundColor1);
        contentPanel.setBounds(200, 70, 775, 520);
        contentPanel.setOpaque(false);
        frame.add(contentPanel);

        HashMap<String, RoundedPanel> tabPanels = new HashMap<>();

        RoundedPanel homePanel = new RoundedPanel(null, 0);
        homePanel.setOpaque(false);
        homePanel.setBounds(0, 0, 760, 540);
        homePanel.setBackground(backgroundColor2);
        JLabel homeLabel = new JLabel("Welcome to PL-Nebula-1.4.0");
        homeLabel.setFont(FontLoader.loadFont(getClass().getResource("/fonts/poppins/main.ttf"),22));
        homeLabel.setForeground(textColor1);
        homeLabel.setBounds(20, 20, 800, 30);
        homePanel.add(homeLabel);

        String[] images = {
                "img1.png",
                "img2.png",
                "img3.jpg",
                "img4.jpg"
        };

        RoundedPanel consoleContainer = new RoundedPanel(
                new BorderLayout(),
                0
        );

        consoleContainer.setOpaque(false);

        JTextPane consoleP = new JTextPane();
        consoleP.setEditable(false);
        consoleP.setOpaque(false);
        consoleP.setBackground(new Color(0,0,0,0));
        consoleP.setForeground(textColor1);
        consoleP.setFont(FontLoader.loadFont(getClass().getResource("/fonts/progo/main.otf"),12));
        consoleP.setMargin(new Insets(15, 15, 15, 15));
        consoleP.setBorder(null);
        consoleP.putClientProperty("JTextPane.border", null);
        JScrollPane scroll = new JScrollPane(consoleP);

        scroll.setBorder(null);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBackground(new Color(0,0,0,0));

        consoleContainer.add(scroll, BorderLayout.CENTER);
        consoleContainer.setBackground(new Color(0,0,0,0));
        consoleContainer.setBounds(20, 100, 735, 320);

        ConsoleOutputStream output = new ConsoleOutputStream(consoleP);
        PrintStream ps = new PrintStream(output, true);

        System.setOut(ps);
        System.setErr(ps);

        homePanel.add(consoleContainer);

        JLabel imgText = new JLabel("Introducing, Color! Check settings.");
        imgText.setForeground(textColor1);
        imgText.setFont(FontLoader.loadFont(getClass().getResource("/fonts/progo/main.ttf"),16)); //FontLoader.loadFont(getClass().getResource("/fonts/poppins/main.otf"),28)
        imgText.setBounds(20, 55, 340, 30);
        homePanel.add(imgText);

        JPanel loaderIn = new JPanel();
        loaderIn.setBackground(backgroundColor1);
        loaderIn.setLayout(null);
        loaderIn.setBounds(300, 200, 400, 150);

        JLabel loaderT = new JLabel("INSTANCE_LAUNCHING_STATUS_ERR");
        loaderT.setLayout(null);
        loaderT.setBounds(20, 10, 400, 60);
        loaderT.setForeground(textColor2);
        loaderT.setFont(new Font("Arial", Font.BOLD, 44));

        JLabel loaderS = new JLabel("INSTANCE_NAME_NULL_ERR");
        loaderS.setLayout(null);
        loaderS.setBounds(20, 70, 400, 60);
        loaderS.setForeground(textColor2);
        loaderS.setFont(new Font("Arial", Font.BOLD, 24));

        JLabel loaderS2 = new JLabel("INSTANCe_LAUNCH_STATUS_DETAIL_ERR");
        loaderS2.setLayout(null);
        loaderS2.setBounds(20, 95, 300, 60);
        loaderS2.setForeground(textColor2);
        loaderS2.setFont(new Font("Arial", Font.BOLD, 14));

        loader.setBounds(20, 140, 360, 20);


        ImagePanel imagePanel = new ImagePanel(images);
        imagePanel.setOpaque(true);
        imagePanel.setBounds(
                20,
                100,
                735,
                320
        );

        imagePanel.setZoom(1.5);
        imagePanel.setCornerRadius(0);


        loaderIn.add(loaderS);
        loaderIn.add(loaderS2);
        loaderIn.add(loaderT);
        LaunchProgress.bind(
            loader,
            loaderS2
        );

        frame.getLayeredPane().remove(loaderIn);

        RoundedButton play = new RoundedButton("PLAY", 0);
        play.setBounds(20, 450, 300, 50);
        play.setBackground(buttonColor);
        play.setFont(FontLoader.loadFont(getClass().getResource("/fonts/progo/main.otf"),20));
        play.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 100), 1));
        homePanel.setLayout(null);
        homePanel.add(play);
        homePanel.setBackground(backgroundColor2);
        JLabel instancesTitle = new JLabel("Instances");
        instancesTitle.setBounds(15, 15, 120, 25);
        instancesTitle.setFont(FontLoader.loadFont(getClass().getResource("/fonts/progo/main.otf"),18));
        instancesTitle.setForeground(textColor1);

        instancesBar.add(instancesTitle);
        final Instance[] selectedInstance = { null };


        tabPanels.put("Home", homePanel);
        contentPanel.add(homePanel);

        String[] otherTabs = { "Instances", "Account", "Settings", "Mods" };
        for (String tabName : otherTabs) {
            RoundedPanel panel = new RoundedPanel(null, 0);
            panel.setBounds(0, 0, 820, 540);
            panel.setVisible(false);
            panel.setLayout(null);
            panel.setBackground(backgroundColor2);
            contentPanel.add(panel);
            tabPanels.put(tabName, panel);
        }

        LauncherInstancesMain instMenu = new LauncherInstancesMain();
        Font progo20 = FontLoader.loadFont(
                getClass().getResource("/fonts/progo/main.otf"),
                20
        );

        Font poppins20 = FontLoader.loadFont(
                getClass().getResource("/fonts/poppins/main.ttf"),
                20
        );
        int y = 40;
        String[] tabs = { "Home", "Mods", "Account", "Settings"};
        for (String tab : tabs) {
            JButton btn = new JButton(tab);
            btn.setBounds(5, y, 200, 40);
            btn.setAlignmentX(JButton.LEFT_ALIGNMENT);
            btn.setFocusPainted(false);
            btn.setBorderPainted(false);
            btn.setBackground(buttonColor);
            btn.setForeground(textColor1);
            btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            btn.setHorizontalAlignment(SwingConstants.LEFT);
            btn.setMargin(new Insets(0, 15, 0, 0));
            btn.setFont(progo20);

            LauncherModMenu mods = new LauncherModMenu();

            btn.addActionListener(e -> {
                for (RoundedPanel p : tabPanels.values())
                    p.setVisible(false);
                tabPanels.get(tab).setVisible(true);

                currentTab[0] = tab;
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
        user.setFont(FontLoader.loadFont(getClass().getResource("/fonts/progo/main.otf"),16));
        user.setForeground(textColor2);
        user.setBounds(15, 10, 180, 25);
        accountPanel.add(user);

        JLabel status = new JLabel(data.getStatus());
        status.setFont(FontLoader.loadFont(getClass().getResource("/fonts/progo/main.otf"),14));
        status.setForeground(textColor2);
        status.setBounds(15, 40, 180, 20);
        accountPanel.add(status);

        user.setText(data.getUsername());
        status.setText(data.getStatus());

        data.setUsername(user.getText());
        data.setStatus(status.getText());

        int iy = 55;

        for (Instance inst : InstanceManager.detectInstances()) {

            JButton button = new RoundedButton(inst.name, 0);

            button.setBounds(10, iy, 150, 35);
            button.setHorizontalAlignment(SwingConstants.LEFT);

            button.setBackground(buttonColor);
            button.setForeground(textColor1);
            button.setFont(FontLoader.loadFont(getClass().getResource("/fonts/progo/main.otf"),12));

            button.setFocusPainted(false);

            button.addActionListener(e -> {
                selectedInstance[0] = inst;

                // highlight selected button


                button.setBackground(buttonColor);
            });

            instancesBar.add(button);

            if (selectedInstance[0] == null) {
                selectedInstance[0] = inst;
                button.setBackground(buttonColor);
            }

            iy += 45;
            instancesBar.setPreferredSize(new Dimension(140, iy + 20));
            instancesBar.revalidate();
        }

        PrintStream console = System.out;

        PrintStream dual = new PrintStream(new OutputStream() {
            StringBuilder buffer = new StringBuilder();

            @Override
            public void write(int b) throws IOException {
                console.write(b);

                if (b == '\n') {
                    String line = buffer.toString();
                    buffer.setLength(0);

                    SwingUtilities.invokeLater(() -> {
                        loaderS2.setText(line);
                    });

                } else {
                    buffer.append((char) b);
                }
            }
        });

        System.setOut(dual);
        System.setErr(dual);

        play.addActionListener(e -> {
            imagePanel.setVisible(false);
            Instance inst = selectedInstance[0];

            if (inst == null)
                return;

            new Thread(() -> {
                try {

                    org.projectlauncher.Main.launchInstance(inst);

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }).start();
        });

        RoundedPanel settingsPanel = tabPanels.get("Settings");
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setForeground(textColor2);
        usernameLabel.setBounds(20, 70, 150, 25);
        settingsPanel.add(usernameLabel);

        JTextField usernameField = new JTextField(data.getUsername());
        usernameField.setBounds(150, 70, 200, 25);
        settingsPanel.add(usernameField);

        JLabel themeLabel = new JLabel("Theme:");
        themeLabel.setForeground(textColor2);
        themeLabel.setBounds(20, 110, 150, 25);
        settingsPanel.add(themeLabel);

        String[] themes = {
                "System",
                "Dark",
                "Light",
                "Electric Yellow",
                "Inferno Orange",
                "Nebula",
                "Neon Green",
                "Hyper Pink",
                "Crimson Red",
                "UV"
        };

        JComboBox<String> themeBox = new JComboBox<>(themes);
        themeBox.setBounds(150, 110, 220, 28);
        themeBox.setSelectedItem(data.getTheme());
        settingsPanel.add(themeBox);



        JButton saveBtn = new JButton("Save Settings");
        saveBtn.setBounds(150, 160, 200, 35);
        saveBtn.setBackground(backgroundColor2);
        saveBtn.setForeground(textColor2);
        saveBtn.setFocusPainted(false);
        saveBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        saveBtn.addActionListener(e -> {
            data.setUsername(usernameField.getText());
            data.setTheme((String) themeBox.getSelectedItem());

            data.save();

            user.setText(data.getUsername());

            int option = JOptionPane.showConfirmDialog(
                    frame,
                    "Theme saved!\nRestart the launcher to apply changes.",
                    "Restart Required",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.INFORMATION_MESSAGE
            );

            if (option == JOptionPane.OK_OPTION) {
                frame.dispose();

                // restart launcher
                try {
                    String java = System.getProperty("java.home")
                            + File.separator + "bin"
                            + File.separator + "java";

                    String classpath = System.getProperty("java.class.path");

                    new ProcessBuilder(
                            java,
                            "-cp",
                            classpath,
                            "org.projectlauncher.Main"
                    ).start();

                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                System.exit(0);
            }
        });
        settingsPanel.add(saveBtn);

        homePanel.add(imagePanel);
        frame.setVisible(true);

        System.out.println(frame.isFocusableWindow());
        System.out.println(frame.getBackground());
        Color c = frame.getBackground();

        System.out.println(
                "RGBA: " +
                        c.getRed() + "," +
                        c.getGreen() + "," +
                        c.getBlue() + "," +
                        c.getAlpha()
        );
    }

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