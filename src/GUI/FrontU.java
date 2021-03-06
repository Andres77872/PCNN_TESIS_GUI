package GUI;

import javax.swing.*;

import GUI.COMP.*;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import static MAIN.main.*;
import static MAIN.main.py;

public class FrontU extends JFrame {
    private JTabbedPane Jtbp_muestra;

    private JButton Jbtn_Mejorar;
    private JButton mejoraConjuntoDeImagenesButton;
    private JButton LOGDEBUGButton;
    private JPanel JP_BTN;

    private File F_imgEntrada, F_imgRef;
    private Metricas JP_MET;
    private Algoritmos JP_ALG;
    private Abrir OP;


    private Imagen Jlbl_imgEntrada, Jlbl_imgReferencia;

    public FrontU() {
        setLayout(new BorderLayout());

        Jlbl_imgEntrada = new Imagen();
        Jlbl_imgReferencia = new Imagen();
        Jtbp_muestra = new JTabbedPane();

        OP = new Abrir(true) {
            @Override
            public void evtFileEntrada(File F) {
                F_imgEntrada = F;
                Jlbl_imgEntrada.setLabelPic(F, reshape);
                Jtbp_muestra.repaint();
            }

            @Override
            public void evtFileReferencia(File F) {
                F_imgRef = F;
                Jlbl_imgReferencia.setLabelPic(F, reshape);
                Jtbp_muestra.repaint();
            }

            @Override
            public void evtLimpiarEntrada() {
                F_imgEntrada = null;
                Jlbl_imgEntrada.delPic();
                Jtbp_muestra.repaint();
            }

            @Override
            public void evtLimpiarReferencia() {
                F_imgRef = null;
                Jlbl_imgReferencia.delPic();
                Jtbp_muestra.repaint();
            }
        };

        Jtbp_muestra.add("Entrada", Jlbl_imgEntrada);
        Jtbp_muestra.add("Referencia", Jlbl_imgReferencia);

        JPanel JP_OPT = new JPanel(new GridLayout(0,1));
        JP_MET = new Metricas();
        JP_ALG = new Algoritmos();
        JP_OPT.add(JP_MET);
        JP_OPT.add(JP_ALG);

        JPanel JP_M = new JPanel(new BorderLayout());
        JP_M.add(Jtbp_muestra,BorderLayout.CENTER);
        JP_M.add(JP_OPT,BorderLayout.EAST);

        Jbtn_Mejorar = new JButton("Mejorar");
        Jbtn_Mejorar.addActionListener(e -> evtMejoraImagenUnitario());

        LOGDEBUGButton = new JButton("LOG (DEBUG)");
        LOGDEBUGButton.addActionListener(e -> {
            lg.setVisible(true);
        });

        mejoraConjuntoDeImagenesButton = new JButton("Mejora Conjunto de imagenes");
        mejoraConjuntoDeImagenesButton.addActionListener(e -> {
            JFrame g = new FrontC();
            g.setVisible(true);
            dispose();
        });

        JP_BTN = new JPanel(new GridLayout());
        JP_BTN.add(Jbtn_Mejorar);
        JP_BTN.add(mejoraConjuntoDeImagenesButton);
        JP_BTN.add(new JButton("Pruebas"));

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                super.windowClosed(e);
                py.kill();
            }
        });

        JPanel ST=new JPanel(new BorderLayout());
        ST.add(LOGDEBUGButton,BorderLayout.NORTH);
        ST.add(OP, BorderLayout.PAGE_END);
        add(ST, BorderLayout.PAGE_START);
        add(JP_M, BorderLayout.CENTER);
        add(JP_BTN, BorderLayout.PAGE_END);

        setTitle("PCNN " + VER);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
    }

    private void evtMejoraImagenUnitario() {
        if (F_imgEntrada == null) {
            lg.showErrorMSG("No hay imagen de entrada");
            return;
        }
        if (F_imgRef == null && JP_MET.isMetSelec()) {
            lg.showErrorMSG("Para calcular metricas es neceraria una imagen de referencia");
            return;
        } else if (F_imgRef != null && !JP_MET.isMetSelec()) {
            lg.showErrorMSG("Hay imagen de referencia pero no hay metricas seleccionadas");
            return;
        }
        String COMANDO = OP.getCMD() + "," +
                JP_MET.getCMD() + "," +
                JP_ALG.getCMD();

        if (!py.run(COMANDO, py.UNITARIO, F_imgEntrada)) {
            lg.showErrorMSG("Ya se esta ejecutando un proceso\t->\t" + py.getProcessInfo());
        }
    }
}
