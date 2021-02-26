package GUI;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;

public class log {
    public JPanel root;
    public JTextPane LOGTextPane;
    private JTextField textField1;

    public void setText(String text) {
        StyledDocument doc = LOGTextPane.getStyledDocument();

        SimpleAttributeSet keyWord = new SimpleAttributeSet();
        StyleConstants.setForeground(keyWord, switch (text.split(":")[0]) {
            case "ERROR":
                yield Color.RED;
            case "Pymsg":
                yield Color.MAGENTA;
            default:
                yield Color.BLACK;
        });

        try {
            doc.insertString(doc.getLength(), text + "\n", keyWord);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }
}
