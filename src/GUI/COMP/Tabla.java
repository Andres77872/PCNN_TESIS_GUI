package GUI.COMP;

import org.jfree.chart.ChartPanel;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import static UTIL.FileUtil.getChart;

public class Tabla extends JScrollPane {
    private JTable JT;
    private String[] alg;

    public Tabla(Map<String, Map<String, Map<String, Double>>> METRICAS) {
        String key = METRICAS.keySet().toArray()[0].toString();
        JT = INI(METRICAS, key);
        DefaultTableModel model = (DefaultTableModel) JT.getModel();
        for (String k : METRICAS.keySet()) {
            model = getTable(model, METRICAS, k, METRICAS.size());
        }
        setViewportView(JT);

    }

    public Tabla(Map<String, Map<String, Map<String, Double>>> METRICAS, String key) {
        JT = INI(METRICAS, key);

        JT.setModel(getTable((DefaultTableModel) JT.getModel(), METRICAS, key, 1));

        setViewportView(JT);


    }

    private DefaultTableModel getTable(DefaultTableModel model, Map<String, Map<String, Map<String, Double>>> METRICAS, String key, double count) {
        Set<String> keySet = METRICAS.get(key).keySet();
        int i = 1;
        for (String alg : keySet) {
            int j = 0;
            for (String met : METRICAS.get(key).get(alg).keySet()) {
                model.setValueAt(met, j, 0);
                model.setValueAt(met, j, METRICAS.get(key).size() + 1);
                model.setValueAt(alg, METRICAS.get(key).get(alg).size(), i);
                Object v = model.getValueAt(j, i);
                model.setValueAt(Double.parseDouble(v == null ? "0" : v.toString()) + METRICAS.get(key).get(alg).get(met) / count, j, i);
                j++;
            }
            i++;
        }
        return model;
    }

    private void evtMetSel(String val) {
        DefaultTableModel model = (DefaultTableModel) JT.getModel();
        for (var a : model.getDataVector()) {
            if (a.firstElement() != null && a.firstElement().toString().equals(val)) {
                showGR_METRICAS(a.toArray());
            }
        }
    }

    private JTable INI(Map<String, Map<String, Map<String, Double>>> METRICAS, String key) {
        DefaultTableModel model = new DefaultTableModel();
        JTable JT = new JTable(model);
        Set<String> keySet = METRICAS.get(key).keySet();
        model.addColumn("Metrica");
        this.alg = new String[keySet.size()];
        int i = 0;
        for (String alg : keySet) {
            this.alg[i] = alg;
            model.addColumn(alg);
            i++;
        }
        model.addColumn("BTN");
        model.setRowCount(METRICAS.get(key).get(model.getColumnName(1)).size() + 1);

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
                        evtMetSel(val);
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
        return JT;
    }

    private void showGR_METRICAS(Object[] Metrica) {
        DefaultCategoryDataset localdataset = new DefaultCategoryDataset();
        int i = 1;
        for (String alg : alg) {
            localdataset.addValue(Double.parseDouble(Metrica[i].toString()), Metrica[0].toString(), alg);
            i++;
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
        new GR(localdataset, Metrica[0].toString()).setVisible(true);
    }
}
