package GUI;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Scanner;

public class Front {
    public static String PCNN_PATH="C:\\Users\\Andres\\Spyder Proyectos\\PCNN\\__temp__";


    public JPanel root;
    private JPanel JP_Abrir;
    private JButton abrirImagenButton;
    private JTextField Jtxf_ImgPathEntrada;
    private JButton imagenDeReferenciaButton;
    private JTextField Jtxf_ImgPathReferencia;
    private JTabbedPane tabbedPane1;
    private JCheckBox Jcbx_Metricas_MSE;
    private JCheckBox Jcbx_Metricas_PSNR;
    private JCheckBox Jcbx_Metricas_SSIM;
    private JCheckBox Jcbx_Metricas_VIF;
    private JCheckBox Jcbx_Algoritmos_Propuesto;
    private JCheckBox Jcbx_Algoritmos_SCM;
    private JCheckBox Jcbx_Algoritmos_PCNN;
    private JCheckBox Jcbx_Algoritmos_EH;
    private JCheckBox Jcbx_Algoritmos_SPCNN;
    private JCheckBox Jcbx_Algoritmos_CLAHE;
    private JCheckBox Jcbx_Algoritmos_ICM;
    private JCheckBox _Jcbx_Algoritmos_OTRO;
    private JButton mejorarButton;
    private JButton mejoraConjuntoDeImagenesButton;
    private JButton pruebasButton;
    private JPanel JP_Metricas;
    private JScrollPane JSP_Metricas;
    private JScrollPane JSP_Algoritmos;
    private JPanel JP_Algoritmos;
    private JPanel JP_ImgEntrada;
    private JPanel JP_ImgReferencia;
    private JLabel Jlbl_Entrada;
    private JLabel Jlbl_Referencia;

    private JFileChooser jfc = new JFileChooser();
    File F_imgEntrada, F_imgRef;

    public Front() {
        jfc.addChoosableFileFilter(
                new FileNameExtensionFilter(
                        "Imagen", "png", "jpg"));
        abrirImagenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int r = jfc.showOpenDialog(null);
                if (r == JFileChooser.APPROVE_OPTION) {
                    F_imgEntrada = jfc.getSelectedFile();
                    Jtxf_ImgPathEntrada.setText(F_imgEntrada.toString());
                    Jlbl_Entrada.setIcon(
                            new ImageIcon(
                                    new ImageIcon(
                                            F_imgEntrada.toString())
                                            .getImage()
                                            .getScaledInstance(200, 200, Image.SCALE_DEFAULT)));
                }
            }
        });
        imagenDeReferenciaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int r = jfc.showOpenDialog(null);
                if (r == JFileChooser.APPROVE_OPTION) {
                    F_imgRef = jfc.getSelectedFile();
                    Jtxf_ImgPathReferencia.setText(F_imgRef.toString());
                    Jlbl_Referencia.setIcon(
                            new ImageIcon(
                                    new ImageIcon(
                                            F_imgRef.toString())
                                            .getImage()
                                            .getScaledInstance(200, 200, Image.SCALE_DEFAULT)));
                }
            }
        });
        mejorarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String COMANDO = ((F_imgEntrada != null) ? F_imgEntrada.toString() : "path") + "," +
                        ((F_imgRef != null) ? F_imgRef.toString() : "path") + "," +
                        ((Jcbx_Metricas_MSE.isSelected()) ? "1" : "0") + "," +
                        ((Jcbx_Metricas_SSIM.isSelected()) ? "1" : "0") + "," +
                        ((Jcbx_Metricas_PSNR.isSelected()) ? "1" : "0") + "," +
                        ((Jcbx_Metricas_VIF.isSelected()) ? "1" : "0") + "," +
                        ((Jcbx_Algoritmos_Propuesto.isSelected()) ? "1" : "0") + "," +
                        ((Jcbx_Algoritmos_PCNN.isSelected()) ? "1" : "0") + "," +
                        ((Jcbx_Algoritmos_SPCNN.isSelected()) ? "1" : "0") + "," +
                        ((Jcbx_Algoritmos_SCM.isSelected()) ? "1" : "0") + "," +
                        ((Jcbx_Algoritmos_ICM.isSelected()) ? "1" : "0") + "," +
                        ((Jcbx_Algoritmos_EH.isSelected()) ? "1" : "0") + "," +
                        ((Jcbx_Algoritmos_CLAHE.isSelected()) ? "1" : "0");
                System.out.println(COMANDO);

                try {
                    jp(COMANDO.replace(","," "));
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }

                JFrame g = new JFrame("Mejora una imagen");
                g.setContentPane(new MejoraImagenUNITARIO(COMANDO).root);
                g.setDefaultCloseOperation(g.DISPOSE_ON_CLOSE);
                g.setSize(800,600);
                g.setLocationRelativeTo(null);
                g.setVisible(true);

            }
        });
    }

    private int c = 1;

    public void jp(String cmd) throws IOException {
        Scanner lee = new Scanner(System.in);
        //        ProcessBuilder PB = new ProcessBuilder("C:\\Users\\Andres\\Anaconda3\\Scripts\\activate.bat","cd..","python.exe", "C:\\Users\\Andres\\Documents\\Python Programas\\java_python\\py.py");
        try (PrintWriter PW_Script = new PrintWriter(new FileOutputStream("S.bat", false))) {
            PW_Script.println(""
                    + "cd C:\\Users\\Andres\\Spyder Proyectos\\PCNN\n"
                    + "call C:\\Users\\Andres\\Anaconda3\\Scripts\\activate.bat OpenCl\n"
                    + "python \"C:\\Users\\Andres\\Spyder Proyectos\\PCNN\\API.py\" " + cmd + "\n"+
                    "");
        }

        ProcessBuilder PB = new ProcessBuilder("S.bat");
        PB.redirectInput(ProcessBuilder.Redirect.PIPE);
        PB.redirectOutput(ProcessBuilder.Redirect.PIPE);
        PB.redirectError(ProcessBuilder.Redirect.PIPE);
        Process P = PB.start();
        PrintWriter PW = new PrintWriter(P.getOutputStream());
        BufferedReader BR = new BufferedReader(new InputStreamReader(P.getInputStream()));

        String S;
        //System.out.println(Arrays.toString(BR.lines().toArray()));
        long timestamp = System.currentTimeMillis();

        while ((S = BR.readLine()) != null) {
            System.out.println(S);
        }
        //BR.lines().forEach((e)->{
        //    System.out.println(e);
        //});

        System.out.println("Tiempo: " + (System.currentTimeMillis() - timestamp) + ", Numeros: " + (c - 1));

    }
}
