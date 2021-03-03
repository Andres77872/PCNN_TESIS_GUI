package GUI;

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

import static MAIN.main.PCNN_PATH;
import static MAIN.main.reshape;
import static UTIL.FileUtil.loadHistogramas;
import static UTIL.FileUtil.loadMetricas;

public class MejoraImagenUNITARIO extends JFrame {
    public JPanel root;
    private JTabbedPane Jtbp_Algoritmos;
    private JTabbedPane Jtbp_Evaluacion;

    private Map<String, Map<String, Map<String, Double>>> METRICAS = new HashMap<>();
    private Map<String, Map<String, Map<String, double[]>>> HISTOGRAMAS = new HashMap<>();
    private Map<String, DefaultCategoryDataset> dataset = new HashMap<>();

    private File fo;
    
    public MejoraImagenUNITARIO(String Comando, File fo) {
        METRICAS = loadMetricas(new File(PCNN_PATH + "\\Metricas.json"));
        HISTOGRAMAS = loadHistogramas(new File(PCNN_PATH + "\\Histogramas.json"));
        this.fo=fo;

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
            Jtbp_Evaluacion.addTab("Tabla", getNewTablaPane());
        }

        setTitle("Mejora una imagen");
        setContentPane(root);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

    }

    private JScrollPane getNewTablaPane() {
        JScrollPane JS = new JScrollPane();

        DefaultTableModel model = new DefaultTableModel();
        JTable JT = new JTable(model);
        JS.getViewport().add(JT);
        Set<String> keySet = METRICAS.get(fo.getName()).keySet();
        model.addColumn("Metrica");
        for (String alg : keySet) {
            model.addColumn(alg);
        }
        model.addColumn("BTN");
        model.setRowCount(METRICAS.get(fo.getName()).get(model.getColumnName(1)).size() + 1);
        int i = 1;
        for (String alg : keySet) {
            int j = 0;
            for (String met : METRICAS.get(fo.getName()).get(alg).keySet()) {
                model.setValueAt(met, j, 0);
                model.setValueAt(met, j, METRICAS.get(fo.getName()).size() + 1);
                model.setValueAt(alg, METRICAS.get(fo.getName()).get(alg).size(), i);
                model.setValueAt(METRICAS.get(fo.getName()).get(alg).get(met), j, i);
                j++;
            }
            i++;
        }
        JT.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int y = JT.getSelectedColumn();
                int x = JT.getSelectedRow();

                String val = JT.getValueAt(x, y).toString();

                switch (val) {
                    case "psnr":
                    case "ssim":
                    case "vifp":
                    case "mse":
                        showGR_METRICAS(val);
                    case "HE":
                    case "CLAHE":
                    case "PCNN":
                    case "SPCNN":
                    case "ICM":
                    case "SCM":
                    case "PROPUESTO":
                        return;
                    default:
                        return;
                }
            }
        });
        return JS;
    }

    private void showGR_METRICAS(String Metrica) {
        DefaultCategoryDataset localdataset = new DefaultCategoryDataset();
        for (String alg : METRICAS.get(fo.getName()).keySet()) {
            localdataset.addValue(METRICAS.get(fo.getName()).get(alg).get(Metrica), Metrica, alg);
        }
        class GR extends JFrame {
            DefaultCategoryDataset dataset;
            String Metrica;

            public GR(DefaultCategoryDataset dataset, String Metrica) {
                this.dataset = dataset;
                this.Metrica = Metrica;
                JComboBox JCB = new JComboBox();
                JCB.addItem("BAR");
                JCB.addItem("AREA");
                JCB.addItem("LINE");
                JPanel JP = new JPanel();
                JCB.addItemListener(e -> {
                    if (e.getStateChange() == 1) {
                        JP.remove(2);
                        JP.add(new ChartPanel(getChart("Histograma de " + Metrica, Metrica, "Valor de la metrica", localdataset, e.getItem().toString())));
                        setVisible(true);
                    }
                });

                JP.setLayout(new BoxLayout(JP, BoxLayout.Y_AXIS));
                JP.add(new JLabel("Seleccionar tipo de grafica"));
                JP.add(JCB);
                JP.add(new ChartPanel(getChart("Histograma de " + Metrica, Metrica, "Valor de la metrica", localdataset, "BAR")));
                add(JP);
                setTitle("Metric " + Metrica);
                setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                setSize(800, 600);
                setLocationRelativeTo(null);
            }
        }
        new GR(localdataset, Metrica).setVisible(true);
    }

    public JFreeChart getChart(String Title, String ValX, String ValY, DefaultCategoryDataset Dataset, String Type) {
        return switch (Type) {
            case "BAR" -> ChartFactory.createBarChart(Title, ValX, ValY, Dataset,
                    PlotOrientation.VERTICAL, true, true, false);
            case "AREA" -> ChartFactory.createAreaChart(Title, ValX, ValY, Dataset);
            case "LINE" -> ChartFactory.createLineChart(Title, ValX, ValY, Dataset,
                    PlotOrientation.VERTICAL, true, true, false);
            default -> null;
        };
    }

    private JSplitPane getNewAlgoritmoPane(String ipath, String alg) {
        JSplitPane jsp = new JSplitPane();
        jsp.setRightComponent(new JLabel() {
            @Override
            public void setIcon(Icon icon) {
                super.setIcon(new ImageIcon(
                        new ImageIcon(
                                PCNN_PATH + ipath)
                                .getImage()
                                .getScaledInstance(reshape, reshape, Image.SCALE_DEFAULT)));
            }
        });

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

    private String getInfoMet(String lbl) {
        String Cadena = "Informacion de la imagen\n";
        Map<String, Double> mt = METRICAS.get(fo.getName()).get(lbl);
        for (String k : mt.keySet()) {
            Cadena += k + "\t" + mt.get(k) + "\n";
        }

        return Cadena;
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
