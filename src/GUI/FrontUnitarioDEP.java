package GUI;

import GUI.COMP.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;

import static MAIN.main.*;

public class FrontUnitarioDEP extends JFrame {


    public JPanel root;
    private JTabbedPane Jtbp_muestra;

    private JButton Jbtn_Mejorar;
    private JButton mejoraConjuntoDeImagenesButton;
    private JButton pruebasButton;
    private JScrollPane JSP_Metricas;
    private JScrollPane JSP_Algoritmos;
    private JPanel JP_ImgEntrada;
    private JPanel JP_ImgReferencia;
    private JButton LOGDEBUGButton;
    private JPanel JP_Abrir;
    private JPanel JP_Opciones;
    private JPanel JP_CONT;
    private JPanel JP_BTN;

    File F_imgEntrada, F_imgRef;
    Metricas JP_MET;
    Algoritmos JP_ALG;


    Imagen Jlbl_imgEntrada, Jlbl_imgReferencia;

    public FrontUnitarioDEP() {
        root.setLayout(new BorderLayout());
        root.removeAll();
        Jlbl_imgEntrada = new Imagen();
        Jlbl_imgReferencia = new Imagen();

        JPanel OP = new Abrir(true) {
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

        JP_ImgEntrada.setLayout(new GridLayout());
        JP_ImgReferencia.setLayout(new GridLayout());
        JP_ImgEntrada.add(Jlbl_imgEntrada);
        JP_ImgReferencia.add(Jlbl_imgReferencia);

        JP_MET = new Metricas();
        JP_ALG = new Algoritmos();
        JP_Abrir.setLayout(new GridLayout());
        JP_Abrir.add(OP);

        JSP_Metricas.setViewportView(JP_MET);
        JSP_Algoritmos.setViewportView(JP_ALG);

        Jbtn_Mejorar.addActionListener(e -> evtMejoraImagenUnitario());

        LOGDEBUGButton.addActionListener(e -> {
            lg.setVisible(true);
        });
        mejoraConjuntoDeImagenesButton.addActionListener(e -> {
            JFrame g = new FrontCarpetaDEP();
            g.setVisible(true);
            dispose();
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                super.windowClosed(e);
                py.kill();
            }
        });

        root.add(JP_Abrir,BorderLayout.PAGE_START);
        root.add(JP_CONT,BorderLayout.CENTER);
        root.add(JP_BTN,BorderLayout.PAGE_END);

        setTitle("PCNN " + VER);
        setContentPane(root);
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
        String COMANDO = ((F_imgEntrada != null) ? "-img \"" + F_imgEntrada.toString() + "\"" : "") + "," +
                ((F_imgRef != null) ? "-ref \"" + F_imgRef.toString() + "\"" : "") + "," +
                JP_MET.getCMD() + "," +
                JP_ALG.getCMD();

        if (!py.run(COMANDO, py.UNITARIO, F_imgEntrada)) {
            lg.showErrorMSG("Ya se esta ejecutando un proceso\t->\t" + py.getProcessInfo());
        }
    }

}
