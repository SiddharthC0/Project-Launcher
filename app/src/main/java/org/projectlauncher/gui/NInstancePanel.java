package org.projectlauncher.gui;

import java.awt.geom.RoundRectangle2D;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import org.projectlauncher.instances.*;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class NInstancePanel {

    private List<String> versionsCache = new ArrayList<>();

    public void launchNInstancePanel(JFrame parent) {

        String wTitle = "New instance";

        JDialog dialog = new JDialog(parent, wTitle, Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setSize(800, 500);
        dialog.setUndecorated(true);

        float arc = 30f;
        dialog.setShape(new RoundRectangle2D.Double(0, 0, 800, 500, arc, arc));

        dialog.setLocationRelativeTo(parent);
        dialog.getContentPane().setBackground(Color.decode("#000000"));
        dialog.setLayout(null);

        JLabel title = new JLabel(wTitle);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 24));
        title.setBounds(20, 20, 500, 50);
        dialog.add(title);

        // ---------------- Instance Name ----------------

        JTextField instanceName = new JTextField();
        instanceName.setBounds(50, 80, 300, 30);
        dialog.add(instanceName);

        // ---------------- Version Search ----------------

        JTextField search = new JTextField();
        search.setBounds(400, 80, 300, 30);
        dialog.add(search);

        // ---------------- Instance types ----------------

        String[] instanceTypes = {
                "Vanilla",
                "Fabric",
                "Forge",
                "OptiFine"
        };

        RoundedComboBox<String> instanceTypeDropdown = new RoundedComboBox<>(instanceTypes, 25);
        instanceTypeDropdown.setBounds(50, 120, 300, 30);
        instanceTypeDropdown.setBackground(Color.decode("#111111"));
        instanceTypeDropdown.setForeground(Color.WHITE);
        dialog.add(instanceTypeDropdown);

        // ---------------- Version Dropdown ----------------

        RoundedComboBox<String> instanceVersionDropdown = new RoundedComboBox<>(new String[] {}, 25);
        instanceVersionDropdown.setBounds(400, 120, 300, 30);
        instanceVersionDropdown.setBackground(Color.decode("#111111"));
        instanceVersionDropdown.setForeground(Color.WHITE);
        dialog.add(instanceVersionDropdown);

        // ---------------- Version Filters ----------------

        JCheckBox showSnapshots = new JCheckBox("Snapshots");
        JCheckBox showOld = new JCheckBox("Old");

        showSnapshots.setBounds(400, 160, 120, 25);
        showOld.setBounds(520, 160, 120, 25);

        showSnapshots.setForeground(Color.WHITE);
        showOld.setForeground(Color.WHITE);

        showSnapshots.setBackground(Color.BLACK);
        showOld.setBackground(Color.BLACK);

        dialog.add(showSnapshots);
        dialog.add(showOld);

        // ---------------- Loader Animation ----------------

        JPanel loader = new JPanel() {

            int pos = 0;

            {
                new javax.swing.Timer(12, e -> {
                    pos += 10;
                    if (pos > 500)
                        pos = 0;
                    repaint();
                }).start();
            }

            protected void paintComponent(Graphics g) {

                super.paintComponent(g);

                g.setColor(Color.decode("#111111"));
                g.fillRect(0, 0, 500, 12);

                g.setColor(Color.WHITE);
                g.fillRect(pos, 1, 50, 10);
            }

        };

        // ---------------- Loader UI ----------------

        JPanel loaderIn = new JPanel();
        loaderIn.setBackground(Color.BLACK);
        loaderIn.setLayout(null);
        loaderIn.setBounds(300, 200, 400, 150);

        JLabel loaderT = new JLabel("STATUS");
        loaderT.setBounds(20, 10, 400, 60);
        loaderT.setForeground(Color.WHITE);
        loaderT.setFont(new Font("Arial", Font.BOLD, 44));

        JLabel loaderS = new JLabel("VERSION");
        loaderS.setBounds(20, 70, 400, 60);
        loaderS.setForeground(Color.WHITE);
        loaderS.setFont(new Font("Arial", Font.BOLD, 24));

        JLabel loaderS2 = new JLabel("DETAILS");
        loaderS2.setBounds(20, 95, 300, 60);
        loaderS2.setForeground(Color.WHITE);
        loaderS2.setFont(new Font("Arial", Font.BOLD, 14));

        loader.setBounds(0, 140, 400, 10);
        loader.setOpaque(false);

        loaderIn.add(loaderT);
        loaderIn.add(loaderS);
        loaderIn.add(loaderS2);
        loaderIn.add(loader);

        dialog.getLayeredPane().remove(loaderIn);

        // ---------------- Console redirect ----------------

        PrintStream console = System.out;

        PrintStream dual = new PrintStream(new OutputStream() {

            StringBuilder buffer = new StringBuilder();

            public void write(int b) throws IOException {

                console.write(b);

                if (b == '\n') {

                    String line = buffer.toString();
                    buffer.setLength(0);

                    SwingUtilities.invokeLater(() -> loaderS2.setText(line));

                } else {
                    buffer.append((char) b);
                }
            }
        });

        System.setOut(dual);
        System.setErr(dual);

        // ---------------- Version refresh ----------------

        Runnable refreshVersions = () -> {

            instanceVersionDropdown.removeAllItems();
            versionsCache.clear();

            String type = (String) instanceTypeDropdown.getSelectedItem();

            List<String> versions = new ArrayList<>();

            if (type.equals("Vanilla"))
                versions = VersionFetcher.getVanillaVersions();

            if (type.equals("Fabric"))
                versions = VersionFetcher.getFabricVersions();

            for (String v : versions) {

                boolean snapshot = v.contains("w") || v.contains("snapshot");
                boolean old = v.contains("alpha") || v.contains("beta");

                if (!showSnapshots.isSelected() && snapshot)
                    continue;

                if (!showOld.isSelected() && old)
                    continue;

                versionsCache.add(v);
                instanceVersionDropdown.addItem(v);
            }

        };

        instanceTypeDropdown.addActionListener(e -> refreshVersions.run());
        showSnapshots.addActionListener(e -> refreshVersions.run());
        showOld.addActionListener(e -> refreshVersions.run());

        // ---------------- Version Search ----------------

        search.getDocument().addDocumentListener(new DocumentListener() {

            public void insertUpdate(DocumentEvent e) {
                filter();
            }

            public void removeUpdate(DocumentEvent e) {
                filter();
            }

            public void changedUpdate(DocumentEvent e) {
                filter();
            }

            private void filter() {

                String q = search.getText().toLowerCase();

                instanceVersionDropdown.removeAllItems();

                for (String v : versionsCache)
                    if (v.toLowerCase().contains(q))
                        instanceVersionDropdown.addItem(v);
            }

        });

        // ---------------- Download Button ----------------

        RoundedButton download = new RoundedButton("Download", 25);

        download.setBounds(300, 220, 200, 40);
        download.setForeground(Color.WHITE);
        download.setBackground(Color.decode("#111111"));

        dialog.add(download);

        download.addActionListener(e -> {

            String type = (String) instanceTypeDropdown.getSelectedItem();
            String version = (String) instanceVersionDropdown.getSelectedItem();

            if (version == null) {

                JOptionPane.showMessageDialog(dialog, "Select a version first.");
                return;
            }

            System.out.println("Downloading " + type + " " + version);

            if (type.equals("Vanilla")) {

                new Thread(() -> {

                    try {

                        SwingUtilities.invokeLater(() -> {

                            dialog.getLayeredPane().add(loaderIn, JLayeredPane.POPUP_LAYER);
                            dialog.getLayeredPane().revalidate();
                            dialog.getLayeredPane().repaint();

                            loaderT.setText("Downloading");
                            loaderS.setText(version);

                        });

                        VersionFetcher.downloadVanilla(version);

                        SwingUtilities.invokeLater(() -> loaderT.setText("Finished"));

                    } catch (Exception ex) {

                        ex.printStackTrace();
                    }

                }).start();
            }

            if (type.equals("Fabric")) {

                dialog.getLayeredPane().add(loaderIn, JLayeredPane.POPUP_LAYER);
            }

        });

        // ---------------- Close Button ----------------

        JButton close = new JButton("X");

        close.setBounds(730, 15, 50, 50);
        close.setForeground(Color.WHITE);
        close.setFont(new Font("Arial", Font.BOLD, 16));
        close.setBackground(null);
        close.setFocusable(false);
        close.setBorder(null);

        close.addActionListener(e -> dialog.dispose());

        dialog.add(close);

        // ---------------- Initial load ----------------

        instanceTypeDropdown.setSelectedIndex(0);
        refreshVersions.run();

        dialog.setVisible(true);
    }
}