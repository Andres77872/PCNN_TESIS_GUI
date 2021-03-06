package MAIN;

import GUI.FrontU;
import GUI.log;
import UTIL.j2p;

import javax.swing.*;

public class main {
    public static String PCNN_PATH = "C:\\Users\\Andres\\Spyder Proyectos\\PCNN\\__temp__";
    public static int reshape = 450;
    public static String VER = "0.0.6";

    public static final j2p py = new j2p() {
        @Override
        public void evtLogListener(String text) {
            lg.setText(text);
        }
    };
    public static final log lg = new log();

    public static void main(String[] args) {
        //02/03/2021

        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        JFrame g = new FrontU();
        g.setVisible(true);
    }

}
