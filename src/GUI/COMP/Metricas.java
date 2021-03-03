package GUI.COMP;

import javax.swing.*;
import java.awt.*;

public class Metricas extends JPanel {
    private JCheckBox Jcbx_Metricas_MSE;
    private JCheckBox Jcbx_Metricas_PSNR;
    private JCheckBox Jcbx_Metricas_SSIM;
    private JCheckBox Jcbx_Metricas_VIF;

    public Metricas() {
        Jcbx_Metricas_MSE = new JCheckBox("MSE");
        Jcbx_Metricas_PSNR = new JCheckBox("PSNR");
        Jcbx_Metricas_SSIM = new JCheckBox("SSIM");
        Jcbx_Metricas_VIF = new JCheckBox("VIF");
        setLayout(new GridLayout(0, 2, 10, 10));
        add(Jcbx_Metricas_MSE);
        add(Jcbx_Metricas_PSNR);
        add(Jcbx_Metricas_SSIM);
        add(Jcbx_Metricas_VIF);
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
