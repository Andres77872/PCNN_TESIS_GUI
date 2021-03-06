package GUI.COMP;

import javax.swing.*;
import java.awt.*;

public class Metricas extends JScrollPane {
    private JCheckBox Jcbx_Metricas_MSE;
    private JCheckBox Jcbx_Metricas_PSNR;
    private JCheckBox Jcbx_Metricas_SSIM;
    private JCheckBox Jcbx_Metricas_VIF;

    public Metricas() {
        Jcbx_Metricas_MSE = new JCheckBox("Metrica MSE");
        Jcbx_Metricas_PSNR = new JCheckBox("Metrica PSNR");
        Jcbx_Metricas_SSIM = new JCheckBox("Metrica SSIM");
        Jcbx_Metricas_VIF = new JCheckBox("Metrica VIF");

        JPanel JP = new JPanel(new GridLayout(0, 1, 10, 5));

        JP.add(Jcbx_Metricas_MSE);
        JP.add(Jcbx_Metricas_PSNR);
        JP.add(Jcbx_Metricas_SSIM);
        JP.add(Jcbx_Metricas_VIF);
        setViewportView(JP);
        setBorder(BorderFactory.createTitledBorder("Metricas"));
    }

    public String getCMD() {
        String cmd = "";
        if (Jcbx_Metricas_MSE.isSelected()) {
            cmd += "-mse ";
        }
        if (Jcbx_Metricas_SSIM.isSelected()) {
            cmd += "-ssim ";
        }
        if (Jcbx_Metricas_PSNR.isSelected()) {
            cmd += "-psnr ";
        }
        if (Jcbx_Metricas_VIF.isSelected()) {
            cmd += "-vif ";
        }
        cmd = cmd.trim().replace(" ", ",");

        return cmd;
    }

    public boolean isMetSelec() {
        return Jcbx_Metricas_MSE.isSelected() || Jcbx_Metricas_SSIM.isSelected() || Jcbx_Metricas_PSNR.isSelected() || Jcbx_Metricas_VIF.isSelected();
    }
}
