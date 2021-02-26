import GUI.Front;

import javax.swing.*;

public class main {
        public static void main(String[] args) {
            //26/02/2021
            try {
                UIManager.setLookAndFeel(
                        UIManager.getSystemLookAndFeelClassName());
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
                e.printStackTrace();
            }

            new Thread(() -> {
                JFrame g = new JFrame("pcnn 0.0.3");
                g.setContentPane(new Front().root);
                g.setDefaultCloseOperation(g.EXIT_ON_CLOSE);
                g.setSize(800,600);
                g.setLocationRelativeTo(null);
                g.setVisible(true);
            }).start();


        }

}
