package GUI;

import GUI.COMP.ArbolFiles;
import GUI.COMP.Tabla;

import javax.swing.*;
import java.io.File;
import java.util.Map;

import static MAIN.main.PCNN_PATH;
import static UTIL.FileUtil.loadHistogramas;
import static UTIL.FileUtil.loadMetricas;

public class MejoraImagenCARPETA extends JFrame {
    private JPanel root;
    private JTabbedPane tabbedPane1;
    private ArbolFiles JT;

    public MejoraImagenCARPETA(String comando, File fo) {
        JT = new ArbolFiles();
        JT.setTreeDirs(comando, new File(PCNN_PATH));
        tabbedPane1.addTab("VIEW", JT);

        File h = new File(PCNN_PATH + "\\Histogramas.json"), m = new File(PCNN_PATH + "\\Metricas.json");

        Map<String, Map<String, Map<String, double[]>>> HISTOGRAMAS = loadHistogramas(h);
        Map<String, Map<String, Map<String, Double>>> METRICAS = null;
        if (m.exists()) {
            METRICAS = loadMetricas(m);
        }
        tabbedPane1.addTab("Tabla", new Tabla(METRICAS));

        setTitle("Mejora por carpeta");
        setContentPane(root);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
    }
}
