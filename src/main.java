import GUI.Front;

import javax.swing.*;

public class main {
        public static void main(String[] args) {
            //25/02/2021
            JFrame g = new JFrame("pcnn 0.0.2");
            g.setContentPane(new Front().root);
            g.setDefaultCloseOperation(g.EXIT_ON_CLOSE);
            g.setSize(800,600);
            g.setLocationRelativeTo(null);
            g.setVisible(true);
        }

}
