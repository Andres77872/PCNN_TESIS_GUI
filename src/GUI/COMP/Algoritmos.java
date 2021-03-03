package GUI.COMP;

import javax.swing.*;
import java.awt.*;

public class Algoritmos extends JPanel {
    private JCheckBox Jcbx_Algoritmos_Propuesto;
    private JCheckBox Jcbx_Algoritmos_SCM;
    private JCheckBox Jcbx_Algoritmos_PCNN;
    private JCheckBox Jcbx_Algoritmos_HE;
    private JCheckBox Jcbx_Algoritmos_SPCNN;
    private JCheckBox Jcbx_Algoritmos_CLAHE;
    private JCheckBox Jcbx_Algoritmos_ICM;

    public Algoritmos() {
        Jcbx_Algoritmos_Propuesto = new JCheckBox("Propuesto");
        Jcbx_Algoritmos_Propuesto.setSelected(true);
        Jcbx_Algoritmos_Propuesto.setEnabled(false);
        Jcbx_Algoritmos_SCM = new JCheckBox("SCM");
        Jcbx_Algoritmos_PCNN = new JCheckBox("PCNN");
        Jcbx_Algoritmos_HE = new JCheckBox("HE");
        Jcbx_Algoritmos_SPCNN = new JCheckBox("SPCNN");
        Jcbx_Algoritmos_CLAHE = new JCheckBox("CLAHE");
        Jcbx_Algoritmos_ICM = new JCheckBox("ICM");

        setLayout(new GridLayout(0, 2, 10, 10));

        add(Jcbx_Algoritmos_Propuesto);
        add(Jcbx_Algoritmos_SCM);
        add(Jcbx_Algoritmos_PCNN);
        add(Jcbx_Algoritmos_HE);
        add(Jcbx_Algoritmos_SPCNN);
        add(Jcbx_Algoritmos_CLAHE);
        add(Jcbx_Algoritmos_ICM);

    }

    public String getCMD() {
        String cmd = "";

        if (Jcbx_Algoritmos_Propuesto.isSelected()) {
            cmd += "-propuesto ";
        }
        if (Jcbx_Algoritmos_PCNN.isSelected()) {
            cmd += "-pcnn ";
        }
        if (Jcbx_Algoritmos_SPCNN.isSelected()) {
            cmd += "-spcnn ";
        }
        if (Jcbx_Algoritmos_SCM.isSelected()) {
            cmd += "-scm ";
        }
        if (Jcbx_Algoritmos_ICM.isSelected()) {
            cmd += "-icm ";
        }
        if (Jcbx_Algoritmos_HE.isSelected()) {
            cmd += "-he ";
        }
        if (Jcbx_Algoritmos_CLAHE.isSelected()) {
            cmd += "-clahe ";
        }
        cmd = cmd.trim().replace(" ", ",");
        return cmd;
    }
}
