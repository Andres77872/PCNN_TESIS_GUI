package GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MejoraImagenUNITARIO {
    public JPanel root;
    private JTabbedPane Jtbp_Algoritmos;
    private JTabbedPane tabbedPane2;

    private Map<String, Map<String, Double>> METRICAS = new HashMap<>();

    public MejoraImagenUNITARIO(String Comando) {

        try {
            BufferedReader BR=new BufferedReader(new FileReader(new File(Front.PCNN_PATH+"\\IND\\metricas.csv")));

            BR.lines().forEach((ln)->{
                String met_alg[]=ln.trim().split(",");
                Map<String, Double> alg = new HashMap<>();

                for (int i = 1; i < met_alg.length; i++) {
                    String mt[]=met_alg[i].split(":");
                    alg.put(mt[0],Double.parseDouble(mt[1]));
                }

                METRICAS.put(met_alg[0],alg);
            });

            BR.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String cmd[] = Comando.split(",");

        if (cmd[6].equals("1")) {
            Jtbp_Algoritmos.addTab("Propuesto",getNewPane("\\IND\\o.png","Propuesto"));
        }
        if (cmd[7].equals("1")) {
            Jtbp_Algoritmos.addTab("PCNN",getNewPane("\\IND\\PCNN.png","PCNN"));
        }
        if (cmd[8].equals("1")) {
            Jtbp_Algoritmos.addTab("SPCNN",getNewPane("\\IND\\SPCNN.png","SPCNN"));
        }
        if (cmd[9].equals("1")) {
            Jtbp_Algoritmos.addTab("SCM",getNewPane("\\IND\\SCM.png","SCM"));
        }
        if (cmd[10].equals("1")) {
            Jtbp_Algoritmos.addTab("ICM",getNewPane("\\IND\\ICM.png","ICM"));
        }
        if (cmd[11].equals("1")) {
            Jtbp_Algoritmos.addTab("HE",getNewPane("\\IND\\HE.png","HE"));
        }
        if (cmd[12].equals("1")) {
            Jtbp_Algoritmos.addTab("CLAHE",getNewPane("\\IND\\CLAHE.png","CLAHE"));
        }
    }

    private JSplitPane getNewPane(String ipath,String alg){
        return new JSplitPane(){
            @Override
            public void setRightComponent(Component comp) {
                super.setRightComponent(new JLabel(){
                    @Override
                    public void setIcon(Icon icon) {
                        super.setIcon(new ImageIcon(
                                new ImageIcon(
                                        Front.PCNN_PATH + ipath)
                                        .getImage()
                                        .getScaledInstance(200, 200, Image.SCALE_DEFAULT)));
                    }
                });
            }

            @Override
            public void setLeftComponent(Component comp) {
                JScrollPane SP = new JScrollPane();
                SP.getViewport().add(getInfoTab(alg));
                super.setLeftComponent(SP);
            }

            @Override
            public void setDividerLocation(int location) {
                super.setDividerLocation(200);
            }
        };
    }
    private String getInfoMet(String lbl){
        String Cadena="Informacion de la imagen\n";
        Map<String,Double> mt=METRICAS.get(lbl);
        for (String k : mt.keySet()) {
            Cadena += k + "\t" + mt.get(k)+"\n";
        }

        return Cadena;
    }
    private JTable getInfoTab(String lbl){
        DefaultTableModel model = new DefaultTableModel();
        JTable jt=new JTable(model);
        Map<String,Double> mt=METRICAS.get(lbl);
        model.addColumn("Metrica");
        model.addColumn("Valor");
        for (String k : mt.keySet()) {
            model.addRow(new Object[]{k,mt.get(k)});
        }

        return jt;
    }
}
