package GUI;

import GUI.COMP.Imagen;
import GUI.COMP.Tabla;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import static MAIN.main.PCNN_PATH;
import static MAIN.main.reshape;
import static UTIL.FileUtil.*;

public class MejoraImagenUNITARIO extends JFrame {
    public JPanel root;
    private JTabbedPane Jtbp_Algoritmos;
    private JTabbedPane Jtbp_Evaluacion;

    private Map<String, Map<String, Map<String, Double>>> METRICAS;
    private Map<String, Map<String, Map<String, double[]>>> HISTOGRAMAS;
    private Map<String, DefaultCategoryDataset> dataset = new HashMap<>();

    private File fo;

    public MejoraImagenUNITARIO(String Comando, File fo) {
        File MET_t = new File(PCNN_PATH + "\\Metricas.json");
        if (MET_t.exists()) {
            METRICAS = loadMetricas(MET_t);
        }
        HISTOGRAMAS = loadHistogramas(new File(PCNN_PATH + "\\Histogramas.json"));
        this.fo = fo;

        for (String cmd : Comando.split(",")) {
            if (cmd.equals("-propuesto")) {
                Jtbp_Algoritmos.addTab("PROPUESTO", getNewAlgoritmoPane("\\" + fo.getName() + "\\PROPUESTO.png", "PROPUESTO"));
            }
            if (cmd.equals("-pcnn")) {
                Jtbp_Algoritmos.addTab("PCNN", getNewAlgoritmoPane("\\" + fo.getName() + "\\PCNN.png", "PCNN"));
            }
            if (cmd.equals("-spcnn")) {
                Jtbp_Algoritmos.addTab("SPCNN", getNewAlgoritmoPane("\\" + fo.getName() + "\\SPCNN.png", "SPCNN"));
            }
            if (cmd.equals("-scm")) {
                Jtbp_Algoritmos.addTab("SCM", getNewAlgoritmoPane("\\" + fo.getName() + "\\SCM.png", "SCM"));
            }
            if (cmd.equals("-icm")) {
                Jtbp_Algoritmos.addTab("ICM", getNewAlgoritmoPane("\\" + fo.getName() + "\\ICM.png", "ICM"));
            }
            if (cmd.equals("-he")) {
                Jtbp_Algoritmos.addTab("HE", getNewAlgoritmoPane("\\" + fo.getName() + "\\HE.png", "HE"));
            }
            if (cmd.equals("-clahe")) {
                Jtbp_Algoritmos.addTab("CLAHE", getNewAlgoritmoPane("\\" + fo.getName() + "\\CLAHE.png", "CLAHE"));
            }
        }

        if (METRICAS != null) {
            Jtbp_Evaluacion.addTab("Tabla", new Tabla(METRICAS, fo.getName()));
        }

        setTitle("Mejora una imagen");
        setContentPane(root);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

    }


    private JSplitPane getNewAlgoritmoPane(String ipath, String alg) {
        JSplitPane jsp = new JSplitPane();

        Imagen img = new Imagen();
        img.setLabelPic(new File(PCNN_PATH + ipath), (int) (reshape * 0.75));
        jsp.setRightComponent(img);

        JPanel JP_met = new JPanel();
        JP_met.setLayout(new BoxLayout(JP_met, BoxLayout.Y_AXIS));


        JScrollPane JS = new JScrollPane();
        JS.getViewport().add(getInfoTab(alg));
        JP_met.add(JS);

        JPanel HISTbtn = new JPanel();
        HISTbtn.setLayout(new BoxLayout(HISTbtn, BoxLayout.X_AXIS));
        String btn[] = new String[]{"r", "g", "b"};

        for (String b : btn) {
            JButton bt = new JButton(b);
            bt.addActionListener(e -> {
                String tempAlg = Jtbp_Algoritmos.getTitleAt(Jtbp_Algoritmos.getSelectedIndex());
                dataset.get(tempAlg).clear();
                updateDataset(((JButton) e.getSource()).getText(), b, tempAlg);
            });
            HISTbtn.add(bt);
        }
        JButton global = new JButton("Global");
        global.addActionListener(e -> {
            String tempAlg = Jtbp_Algoritmos.getTitleAt(Jtbp_Algoritmos.getSelectedIndex());
            dataset.get(tempAlg).clear();
            double r[] = HISTOGRAMAS.get(fo.getName()).get(Jtbp_Algoritmos.getTitleAt(Jtbp_Algoritmos.getSelectedIndex())).get("r");
            double g[] = HISTOGRAMAS.get(fo.getName()).get(Jtbp_Algoritmos.getTitleAt(Jtbp_Algoritmos.getSelectedIndex())).get("g");
            double b[] = HISTOGRAMAS.get(fo.getName()).get(Jtbp_Algoritmos.getTitleAt(Jtbp_Algoritmos.getSelectedIndex())).get("b");
            for (int i = 0; i < r.length; i++) {
                dataset.get(Jtbp_Algoritmos.getTitleAt(Jtbp_Algoritmos.getSelectedIndex())).addValue(r[i] + g[i] + b[i], "rgb", (Integer) i);
            }
        });
        JButton rgb = new JButton("rgb");
        rgb.addActionListener(e -> {
            String tempAlg = Jtbp_Algoritmos.getTitleAt(Jtbp_Algoritmos.getSelectedIndex());
            dataset.get(tempAlg).clear();
            updateDataset("r", "r", tempAlg);
            updateDataset("b", "b", tempAlg);
            updateDataset("g", "g", tempAlg);
        });


        HISTbtn.add(rgb);
        HISTbtn.add(global);
        JP_met.add(HISTbtn);


        DefaultCategoryDataset localdataset = new DefaultCategoryDataset();
        double H[] = HISTOGRAMAS.get(fo.getName()).get(alg).get("r");
        for (int i = 0; i < H.length; i++) {
            localdataset.addValue(H[i], "r", (Integer) i);
        }
        dataset.put(alg, localdataset);

        JP_met.add(new ChartPanel(getChart("Histograma", "", "Freciencia", dataset.get(alg), "AREA")));

        jsp.setLeftComponent(JP_met);
        jsp.setDividerLocation(350);
        return jsp;
    }

    public void updateDataset(String key, String id, String alg) {
        double H[] = HISTOGRAMAS.get(fo.getName()).get(alg).get(key);
        for (int i = 0; i < H.length; i++) {
            dataset.get(alg).addValue(H[i], id, (Integer) i);
        }
    }

    private JTable getInfoTab(String lbl) {
        DefaultTableModel model = new DefaultTableModel();
        JTable jt = new JTable(model);
        model.addColumn("Metrica");
        model.addColumn("Valor");
        if (METRICAS != null) {
            Map<String, Double> mt = METRICAS.get(fo.getName()).get(lbl);
            for (String k : mt.keySet()) {
                model.addRow(new Object[]{k, mt.get(k)});
            }
        } else {
            model.addRow(new Object[]{"", ""});
        }
        return jt;
    }
}
