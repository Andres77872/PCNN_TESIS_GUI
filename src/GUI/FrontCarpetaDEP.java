package GUI;

import GUI.COMP.*;

import javax.swing.*;
import java.awt.*;
import java.io.File;

import static MAIN.main.*;


public class FrontCarpetaDEP extends JFrame {
    private JPanel root;
    private JButton Jbtn_Mejorar;
    private JButton mejoraUnitarioButton;
    private JButton pruebasButton;
    private JButton LOGDEBUGButton;
    private JPanel JP_Abrir;
    private JPanel JP_Opciones;
    private JScrollPane JSP_Metricas;
    private JScrollPane JSP_Algoritmos;
    private JTabbedPane tabbedPane1;
    private JPanel JP_CarpEntrada;
    private JPanel JP_CarpReferencia;

    File F_carEntrada, F_carReferencia;
    Metricas JP_MET;
    Algoritmos JP_ALG;


    ArbolFiles Jtree_Entrada, Jtree_Referencia;

    public FrontCarpetaDEP() {
        Jtree_Entrada = new ArbolFiles();
        Jtree_Referencia = new ArbolFiles();


        JPanel OP = new Abrir(false) {
            @Override
            public void evtFileEntrada(File F) {
                F_carEntrada = F;
                Jtree_Entrada.setTreeFiles(F);

            }

            @Override
            public void evtFileReferencia(File F) {
                F_carReferencia = F;
                Jtree_Referencia.setTreeFiles(F);
            }

            @Override
            public void evtLimpiarEntrada() {
                F_carEntrada = null;
                Jtree_Entrada.delTree();
            }

            @Override
            public void evtLimpiarReferencia() {
                F_carReferencia = null;
                Jtree_Referencia.delTree();
            }
        };

        JP_CarpEntrada.setLayout(new GridLayout());
        JP_CarpReferencia.setLayout(new GridLayout());
        JP_CarpEntrada.add(Jtree_Entrada);
        JP_CarpReferencia.add(Jtree_Referencia);

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

        mejoraUnitarioButton.addActionListener(e -> {
            JFrame g = new FrontUnitarioDEP();
            g.setVisible(true);
            dispose();
        });

        setTitle("PCNN " + VER);
        setContentPane(root);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

    }

    private void evtMejoraImagenUnitario() {

        if (F_carEntrada == null) {
            lg.showErrorMSG("No hay imagen de entrada");
            return;
        }
        if (F_carReferencia == null && JP_MET.isMetSelec()) {
            lg.showErrorMSG("Para calcular metricas es neceraria una imagen de referencia");
            return;
        } else if (F_carReferencia != null && !JP_MET.isMetSelec()) {
            lg.showErrorMSG("Hay imagen de referencia pero no hay metricas seleccionadas");
            return;
        }
        String COMANDO = ((F_carEntrada != null) ? "-img \"" + F_carEntrada.toString() + "\"" : "") + "," +
                ((F_carReferencia != null) ? "-ref \"" + F_carReferencia.toString() + "\"" : "") + "," +
                JP_MET.getCMD() + "," +
                JP_ALG.getCMD();

        if (!py.run(COMANDO, py.CARPETA, F_carEntrada)) {
            lg.showErrorMSG("Ya se esta ejecutando un proceso\t->\t" + py.getProcessInfo());
        }

    }
}
