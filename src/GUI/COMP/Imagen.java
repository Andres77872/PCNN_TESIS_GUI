package GUI.COMP;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class Imagen extends JPanel {

    public void setLabelPic(File F, int reshape) {
        delPic();
        setLayout(new GridLayout());
        JLabel jl = new JLabel();
        jl.setIcon(getPic(reshape, F));
        add(jl);
        doLayout();
        repaint();
    }

    public void setLabelPic(File f) {
        int size = getWidth() / 3;
        File dir[] = f.listFiles();
        delPic();
        setLayout(new GridLayout(3, 3, 10, 10));
        int i = 0;
        for (File d : dir) {
            if (i == 9) {
                break;
            }
            if (d.isFile()) {
                JLabel jl = new JLabel();
                jl.setIcon(getPic(size, d));
                add(jl);
                i++;
            }

        }
        doLayout();
        repaint();
    }

    public void delPic() {
        removeAll();
        doLayout();
        repaint();
    }

    private ImageIcon getPic(int size, File f) {

        ImageIcon img = new ImageIcon(f.toString());

        double max = size / (double) Integer.max(img.getIconWidth(), img.getIconHeight());

        double x = img.getIconWidth() * max, y = img.getIconHeight() * max;

        return new ImageIcon(img.getImage()
                .getScaledInstance((int) x, (int) y, Image.SCALE_DEFAULT));
    }
}
