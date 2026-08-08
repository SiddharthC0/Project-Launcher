package org.projectlauncher.utils;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;
import java.io.OutputStream;

public class ConsoleOutputStream extends OutputStream {

    private final JTextPane pane;
    private final StyledDocument document;

    public ConsoleOutputStream(JTextPane pane) {
        this.pane = pane;
        this.document = pane.getStyledDocument();
    }

    @Override
    public void write(int b) {

        SwingUtilities.invokeLater(() -> {
            try {

                document.insertString(
                        document.getLength(),
                        String.valueOf((char) b),
                        null);

                pane.setCaretPosition(document.getLength());

            } catch (BadLocationException ignored) {}

        });

    }

}