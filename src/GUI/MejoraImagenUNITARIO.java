package GUI;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MejoraImagenUNITARIO {
    public JPanel root;
    private JTabbedPane Jtbp_Algoritmos;
    private JTabbedPane Jtbp_Evaluacion;

    private Map<String, Map<String, Double>> METRICAS = new HashMap<>();
    private Map<String, Map<String, double[]>> HISTOGRAMAS = new HashMap<>();
    private Map<String, DefaultCategoryDataset> dataset = new HashMap<>();

    public MejoraImagenUNITARIO(String Comando) {

        try {
            BufferedReader BR = new BufferedReader(new FileReader(new File(Front.PCNN_PATH + "\\IND\\metricas.csv")));

            BR.lines().forEach((ln) -> {
                String met_alg[] = ln.trim().split(",");
                Map<String, Double> alg = new HashMap<>();

                for (int i = 1; i < met_alg.length; i++) {
                    String mt[] = met_alg[i].split(":");
                    alg.put(mt[0], Double.parseDouble(mt[1]));
                }
                METRICAS.put(met_alg[0], alg);
            });
            BR.close();
        } catch (FileNotFoundException e) {
            METRICAS = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            BufferedReader BR = new BufferedReader(new FileReader(new File(Front.PCNN_PATH + "\\IND\\Histogramas.csv")));
            BR.lines().forEach((ln) -> {
                String canal[] = ln.trim().split(":");
                Map<String, double[]> hist = new HashMap<>();

                for (int i = 1; i < canal.length; i++) {
                    String h[] = canal[i].split(",");
                    double histarr[] = new double[h.length - 1];

                    for (int j = 1; j < h.length; j++) {
                        histarr[j - 1] = Double.parseDouble(h[j]);
                    }

                    hist.put(h[0], histarr);
                }
                HISTOGRAMAS.put(canal[0], hist);
            });
            BR.close();
        } catch (FileNotFoundException e) {
            HISTOGRAMAS = null;
        } catch (IOException e) {
            e.printStackTrace();
        }

        String cmd[] = Comando.split(",");

        if (cmd[6].equals("1")) {
            Jtbp_Algoritmos.addTab("Propuesto", getNewAlgoritmoPane("\\IND\\o.png", "Propuesto"));
        }
        if (cmd[7].equals("1")) {
            Jtbp_Algoritmos.addTab("PCNN", getNewAlgoritmoPane("\\IND\\PCNN.png", "PCNN"));
        }
        if (cmd[8].equals("1")) {
            Jtbp_Algoritmos.addTab("SPCNN", getNewAlgoritmoPane("\\IND\\SPCNN.png", "SPCNN"));
        }
        if (cmd[9].equals("1")) {
            Jtbp_Algoritmos.addTab("SCM", getNewAlgoritmoPane("\\IND\\SCM.png", "SCM"));
        }
        if (cmd[10].equals("1")) {
            Jtbp_Algoritmos.addTab("ICM", getNewAlgoritmoPane("\\IND\\ICM.png", "ICM"));
        }
        if (cmd[11].equals("1")) {
            Jtbp_Algoritmos.addTab("HE", getNewAlgoritmoPane("\\IND\\HE.png", "HE"));
        }
        if (cmd[12].equals("1")) {
            Jtbp_Algoritmos.addTab("CLAHE", getNewAlgoritmoPane("\\IND\\CLAHE.png", "CLAHE"));
        }

        if (METRICAS != null) {
            Jtbp_Evaluacion.addTab("Tabla", getNewTablaPane());
        }

    }

    private JScrollPane getNewTablaPane() {
        JScrollPane JS = new JScrollPane();

        DefaultTableModel model = new DefaultTableModel();
        JTable JT = new JTable(model);
        JS.getViewport().add(JT);
        Set<String> keySet = METRICAS.keySet();
        model.addColumn("Metrica");
        for (String alg : keySet) {
            model.addColumn(alg);
        }
        model.addColumn("BTN");
        model.setRowCount(METRICAS.get(model.getColumnName(1)).size() + 1);
        int i = 1;
        for (String alg : keySet) {
            int j = 0;
            for (String met : METRICAS.get(alg).keySet()) {
                model.setValueAt(met, j, 0);
                model.setValueAt(met, j, METRICAS.size() + 1);
                model.setValueAt(alg, METRICAS.get(alg).size(), i);
                model.setValueAt(METRICAS.get(alg).get(met), j, i);
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
                    case "Propuesto":
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
        for (String alg : METRICAS.keySet()) {
            localdataset.addValue(METRICAS.get(alg).get(Metrica), Metrica, alg);
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
                                Front.PCNN_PATH + ipath)
                                .getImage()
                                .getScaledInstance(Front.reshape, Front.reshape, Image.SCALE_DEFAULT)));
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
            double r[] = HISTOGRAMAS.get(Jtbp_Algoritmos.getTitleAt(Jtbp_Algoritmos.getSelectedIndex())).get("r");
            double g[] = HISTOGRAMAS.get(Jtbp_Algoritmos.getTitleAt(Jtbp_Algoritmos.getSelectedIndex())).get("g");
            double b[] = HISTOGRAMAS.get(Jtbp_Algoritmos.getTitleAt(Jtbp_Algoritmos.getSelectedIndex())).get("b");
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
        double H[] = HISTOGRAMAS.get(alg).get("r");
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
        double H[] = HISTOGRAMAS.get(alg).get(key);
        for (int i = 0; i < H.length; i++) {
            dataset.get(alg).addValue(H[i], id, (Integer) i);
        }
    }

    private String getInfoMet(String lbl) {
        String Cadena = "Informacion de la imagen\n";
        Map<String, Double> mt = METRICAS.get(lbl);
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
            Map<String, Double> mt = METRICAS.get(lbl);
            for (String k : mt.keySet()) {
                model.addRow(new Object[]{k, mt.get(k)});
            }
        } else {
            model.addRow(new Object[]{"", ""});
        }
        return jt;
    }
}
