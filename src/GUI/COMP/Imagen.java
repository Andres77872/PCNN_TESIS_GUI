package GUI.COMP;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class Imagen extends JLabel {
    public void setLabelPic(File F, int reshape) {
        setIcon(new ImageIcon(
                        new ImageIcon(
                                F.toString())
                                .getImage()
                                .getScaledInstance(reshape, reshape, Image.SCALE_DEFAULT)));
    }

    public void delPic() {
        setIcon(null);
    }
}
