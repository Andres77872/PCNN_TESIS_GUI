package GUI.COMP;

import javax.swing.*;
import java.awt.*;

public class Algoritmos extends JScrollPane {
    private JCheckBox Jcbx_Algoritmos_Propuesto;
    private JCheckBox Jcbx_Algoritmos_SCM;
    private JCheckBox Jcbx_Algoritmos_PCNN;
    private JCheckBox Jcbx_Algoritmos_HE;
    private JCheckBox Jcbx_Algoritmos_SPCNN;
    private JCheckBox Jcbx_Algoritmos_CLAHE;
    private JCheckBox Jcbx_Algoritmos_ICM;

    public Algoritmos() {
        Jcbx_Algoritmos_Propuesto = new JCheckBox("Algoritmo Propuesto");
        Jcbx_Algoritmos_Propuesto.setSelected(true);
        Jcbx_Algoritmos_Propuesto.setEnabled(false);
        Jcbx_Algoritmos_SCM = new JCheckBox("Algoritmo SCM");
        Jcbx_Algoritmos_PCNN = new JCheckBox("Algoritmo PCNN");
        Jcbx_Algoritmos_HE = new JCheckBox("Algoritmo HE");
        Jcbx_Algoritmos_SPCNN = new JCheckBox("Algoritmo SPCNN");
        Jcbx_Algoritmos_CLAHE = new JCheckBox("Algoritmo CLAHE");
        Jcbx_Algoritmos_ICM = new JCheckBox("Algoritmo ICM");

        JPanel JP = new JPanel(new GridLayout(0, 1, 10, 5));

        JP.add(Jcbx_Algoritmos_Propuesto);
        JP.add(Jcbx_Algoritmos_SCM);
        JP.add(Jcbx_Algoritmos_PCNN);
        JP.add(Jcbx_Algoritmos_HE);
        JP.add(Jcbx_Algoritmos_SPCNN);
        JP.add(Jcbx_Algoritmos_CLAHE);
        JP.add(Jcbx_Algoritmos_ICM);
        setViewportView(JP);
        setBorder(BorderFactory.createTitledBorder("Algoritmos"));
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
