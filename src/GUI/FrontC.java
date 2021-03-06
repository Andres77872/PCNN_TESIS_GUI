package GUI;

import javax.swing.*;

import GUI.COMP.*;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import static MAIN.main.*;


public class FrontC extends JFrame{
    private JButton Jbtn_Mejorar;
    private JButton mejoraUnitarioButton;
    private JButton LOGDEBUGButton;
    private JPanel JP_BTN;

    private File F_carEntrada, F_carReferencia;
    private Metricas JP_MET;
    private Algoritmos JP_ALG;
    private Abrir OP;


    private ArbolFiles Jtree_Entrada, Jtree_Referencia;

    public FrontC() {
        setLayout(new BorderLayout());

        Jtree_Entrada = new ArbolFiles();
        Jtree_Referencia = new ArbolFiles();
        JTabbedPane Jtbp_muestra = new JTabbedPane();

        OP = new Abrir(false) {

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

        Jtbp_muestra.add("Entrada", Jtree_Entrada);
        Jtbp_muestra.add("Referecnia", Jtree_Referencia);

        JPanel JP_OPT = new JPanel(new GridLayout(0, 1));
        JP_MET = new Metricas();
        JP_ALG = new Algoritmos();
        JP_OPT.add(JP_MET);
        JP_OPT.add(JP_ALG);

        JPanel JP_M = new JPanel(new BorderLayout());
        JP_M.add(Jtbp_muestra, BorderLayout.CENTER);
        JP_M.add(JP_OPT, BorderLayout.EAST);

        Jbtn_Mejorar = new JButton("Mejorar");
        Jbtn_Mejorar.addActionListener(e -> evtMejoraImagenUnitario());

        LOGDEBUGButton = new JButton("LOG (DEBUG)");
        LOGDEBUGButton.addActionListener(e -> {
            lg.setVisible(true);
        });

        mejoraUnitarioButton = new JButton("Mejora unitario de imagen");
        mejoraUnitarioButton.addActionListener(e -> {
            JFrame g = new FrontU();
            g.setVisible(true);
            dispose();
        });

        JP_BTN = new JPanel(new GridLayout());
        JP_BTN.add(Jbtn_Mejorar);
        JP_BTN.add(mejoraUnitarioButton);
        JP_BTN.add(new JButton("Pruebas"));

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                super.windowClosed(e);
                py.kill();
            }
        });

        JPanel ST = new JPanel(new BorderLayout());
        ST.add(LOGDEBUGButton, BorderLayout.NORTH);
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
        String COMANDO = OP.getCMD() + "," +
                JP_MET.getCMD() + "," +
                JP_ALG.getCMD();

        if (!py.run(COMANDO, py.CARPETA, F_carEntrada)) {
            lg.showErrorMSG("Ya se esta ejecutando un proceso\t->\t" + py.getProcessInfo());
        }

    }
}


