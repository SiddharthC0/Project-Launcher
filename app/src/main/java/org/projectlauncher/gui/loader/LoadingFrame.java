package org.projectlauncher.gui.loader;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import org.projectlauncher.gui.TransparentFrameImplementation;
import org.projectlauncher.utils.FontLoader;
import org.projectlauncher.utils.ThemeDetector;


public class LoadingFrame {

    private final ThemeDetector.Theme theme = ThemeDetector.getTheme();

    public Color backgroundColor1 = theme.backgroundColor1;
    public Color backgroundColor2 = theme.backgroundColor2;
    public Color textColor1 = theme.textColor1;
    public Color textColor2 = theme.textColor2;
    public Color buttonColor = theme.buttonColor;
    public Color actualColor = theme.actualColor;


    JFrame window = new JFrame("PL Loading");
    JPanel loader = new JPanel() {

        int pos = 0;

        {
            new javax.swing.Timer(16, e -> {
                pos += 10;

                if (pos > 500)
                    pos = 0;

                repaint();

            }).start();
        }


        @Override
        protected void paintComponent(Graphics g) {

            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );


            g2.setColor(actualColor);

            g2.fillRoundRect(
                    0,
                    0,
                    getWidth(),
                    getHeight(),
                    20,
                    20
            );


            g2.setColor(textColor1);

            g2.fillRoundRect(
                    pos,
                    5,
                    70,
                    20,
                    10,
                    10
            );


            g2.dispose();
        }
    };


    public void launchLoader() {


        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        window.setSize(700, 300);

        window.setLocationRelativeTo(null);

        window.setUndecorated(true);
        TransparentFrameImplementation.makeTransparent(window);


        // Transparent JFrame
        window.setBackground(
                new Color(0, 0, 0, 0)
        );


        window.setShape(
                new RoundRectangle2D.Double(
                        0,
                        0,
                        700,
                        300,
                        30,
                        30
                )
        );


        JPanel bg = new JPanel(null) {

            @Override
            protected void paintComponent(Graphics g) {

                Graphics2D g2 = (Graphics2D) g.create();

                g2.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON
                );


                g2.setColor(backgroundColor2);

                g2.fillRoundRect(
                        0,
                        0,
                        getWidth(),
                        getHeight(),
                        30,
                        30
                );


                g2.dispose();
            }
        };


        bg.setOpaque(false);


        JLabel title = new JLabel("Project");

        title.setForeground(textColor2);

        title.setFont(
                FontLoader.loadFont(
                        getClass().getResource("/fonts/poppins/main.ttf"),
                        70
                )
        );

        title.setBounds(
                240,
                40,
                500,
                100
        );


        JLabel title2 = new JLabel("Launcher");

        title2.setForeground(textColor2);

        title2.setFont(
                FontLoader.loadFont(
                        getClass().getResource("/fonts/poppins/main.ttf"),
                        70
                )
        );

        title2.setBounds(
                240,
                110,
                500,
                100
        );


        JLabel versionEtc = new JLabel("Nebula v3.5.0");

        versionEtc.setForeground(textColor1);

        versionEtc.setFont(
                FontLoader.loadFont(
                        getClass().getResource("/fonts/poppins/main.ttf"),
                        20
                )
        );

        versionEtc.setBounds(
                240,
                185,
                250,
                40
        );


        JLabel detailsEtc = new JLabel("Released");

        detailsEtc.setForeground(textColor1);

        detailsEtc.setFont(
                FontLoader.loadFont(
                        getClass().getResource("/fonts/poppins/main.ttf"),
                        20
                )
        );

        detailsEtc.setBounds(
                240,
                215,
                250,
                40
        );


        loader.setBounds(
                40,
                260,
                620,
                30
        );

        loader.setOpaque(false);


        Image logo = new ImageIcon(
                getClass().getResource("/pl_logo.png")
        ).getImage();


        JLabel logoLabel = new JLabel() {

            @Override
            protected void paintComponent(Graphics g) {

                super.paintComponent(g);

                Graphics2D g2 = (Graphics2D) g.create();

                g2.setRenderingHint(
                        RenderingHints.KEY_INTERPOLATION,
                        RenderingHints.VALUE_INTERPOLATION_BILINEAR
                );

                g2.drawImage(
                        logo,
                        0,
                        0,
                        getWidth(),
                        getHeight(),
                        this
                );

                g2.dispose();
            }
        };


        logoLabel.setBounds(
                20,
                20,
                200,
                200
        );


        bg.add(logoLabel);
        bg.add(title);
        bg.add(title2);
        bg.add(versionEtc);
        bg.add(detailsEtc);
        bg.add(loader);


        window.setContentPane(bg);

        window.setVisible(true);
    }


    public void destroy() {

        window.dispose();

    }
}