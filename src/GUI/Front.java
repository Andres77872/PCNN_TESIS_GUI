package GUI;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class Front implements Runnable {
    public static String PCNN_PATH = "C:\\Users\\Andres\\Spyder Proyectos\\PCNN\\__temp__";
    public static int reshape = 300;


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
    private JButton Jbtb_LimpiarReferencia;
    private JButton Jbtb_LimpiarEntrada;
    private JButton LOGDEBUGButton;

    private JFileChooser jfc = new JFileChooser();
    File F_imgEntrada, F_imgRef;

    private JFrame log;
    private log lg = new log();

    public Front() {
        jfc.addChoosableFileFilter(
                new FileNameExtensionFilter(
                        "Imgaen", "png", "jpg"));
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
                                            .getScaledInstance(reshape, reshape, Image.SCALE_DEFAULT)));
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
                                            .getScaledInstance(reshape, reshape, Image.SCALE_DEFAULT)));
                }
            }
        });
        mejorarButton.addActionListener(e -> evtMejoraImagenUnitario());
        Jbtb_LimpiarEntrada.addActionListener(e -> {
            Jtxf_ImgPathEntrada.setText("");
            F_imgEntrada = null;
        });
        Jbtb_LimpiarReferencia.addActionListener(e -> {
            Jtxf_ImgPathReferencia.setText("");
            F_imgRef = null;
        });
        LOGDEBUGButton.addActionListener(e -> {
            if (log == null) {
                log = new JFrame("LOG");
                log.setContentPane(lg.root);
                log.setDefaultCloseOperation(log.HIDE_ON_CLOSE);
                log.setSize(800, 600);
                log.setLocationRelativeTo(null);
                log.setVisible(true);
            } else if (!log.isShowing()) {
                log.setVisible(true);
            }
        });
    }

    private void evtMejoraImagenUnitario() {
        if (P == null) {
            Thread stdOutReader;
            stdOutReader = new Thread(this);
            stdOutReader.setDaemon(true);
            stdOutReader.start();
        } else if (!P.isAlive()) {
            stop = false;
            Thread stdOutReader;
            stdOutReader = new Thread(this);
            stdOutReader.setDaemon(true);
            stdOutReader.start();
        } else {
            showErrorMSG("Ya se esta ejecutando un proceso\t->\t" + P.info());
        }

    }

    public void showErrorMSG(String msg) {
        JOptionPane.showMessageDialog(null, msg, "ERROR", JOptionPane.ERROR_MESSAGE);
        lg.setText("ERROR: " + msg);
    }

    private boolean stop = false;

    public synchronized void run() {
        if (F_imgEntrada == null) {
            showErrorMSG("No hay imagen de entrada");
            return;
        }
        if (F_imgRef == null && (Jcbx_Metricas_MSE.isSelected() ||
                Jcbx_Metricas_SSIM.isSelected() ||
                Jcbx_Metricas_PSNR.isSelected() ||
                Jcbx_Metricas_VIF.isSelected())) {
            showErrorMSG("Para calcular metricas es neceraria una imagen de referencia");
            return;
        } else if (F_imgRef != null && !(Jcbx_Metricas_MSE.isSelected() ||
                Jcbx_Metricas_SSIM.isSelected() ||
                Jcbx_Metricas_PSNR.isSelected() ||
                Jcbx_Metricas_VIF.isSelected())) {
            showErrorMSG("Hay imagen de referencia pero no hay metricas seleccionadas");
            return;
        }
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

        try {
            jp(COMANDO.replace(",", " "));
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        while (!stop) {
            if (!P.isAlive()) {
                stop = true;
            }
            try {
                this.wait(100);
            } catch (InterruptedException ie) {
            }
            try {
                if (System.in.available() != 0) {
                    String input = this.readLine(System.in);
                    lg.setText(input);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        JFrame g = new JFrame("Mejora una imagen");
        g.setContentPane(new MejoraImagenUNITARIO(COMANDO).root);
        g.setDefaultCloseOperation(g.DISPOSE_ON_CLOSE);
        g.setSize(800, 600);
        g.setLocationRelativeTo(null);
        g.setVisible(true);
    }

    private synchronized String readLine(InputStream in) throws IOException {
        String input = "";
        do {
            int available = in.available();
            if (available == 0) {
                break;
            }
            byte b[] = new byte[available];
            in.read(b);
            input += new String(b, 0, b.length);
        } while (!input.endsWith("\n") && !input.endsWith("\r\n") && !stop);
        return input.trim();
    }

    Process P;

    private void jp(String cmd) throws IOException {
        resetUnitario();
        //        ProcessBuilder PB = new ProcessBuilder("C:\\Users\\Andres\\Anaconda3\\Scripts\\activate.bat","cd..","python.exe", "C:\\Users\\Andres\\Documents\\Python Programas\\java_python\\py.py");
        try (PrintWriter PW_Script = new PrintWriter(new FileOutputStream("S.bat", false))) {
            PW_Script.println(""
                    + "cd C:\\Users\\Andres\\Spyder Proyectos\\PCNN\n"
                    + "call C:\\Users\\Andres\\Anaconda3\\Scripts\\activate.bat OpenCl\n"
                    + "python \"C:\\Users\\Andres\\Spyder Proyectos\\PCNN\\API.py\" " + cmd + "\n" +
                    "");
        }

        ProcessBuilder PB = new ProcessBuilder("S.bat");
        PB.redirectInput(ProcessBuilder.Redirect.PIPE);
        PB.redirectOutput(ProcessBuilder.Redirect.PIPE);
        PB.redirectError(ProcessBuilder.Redirect.INHERIT);

        P = PB.start();

        System.setIn(P.getInputStream());

        //stdOutPin=P.getInputStream();


        //BufferedReader BR = new BufferedReader(new InputStreamReader(P.getInputStream()));

        //String S;
        //System.out.println(Arrays.toString(BR.lines().toArray()));
        //long timestamp = System.currentTimeMillis();

        //while ((S = BR.readLine()) != null) {
        //    lg.setText(S);
        //}
        //BR.lines().forEach((e)->{
        //    System.out.println(e);
        //});

        //lg.setText("Tiempo de ejecucion en python:\t->\t"+(System.currentTimeMillis() - timestamp)+"ms");

        //System.out.println("Tiempo: " + (System.currentTimeMillis() - timestamp) + ", Numeros: " + (c - 1));

    }

    private void delFile(File f) {
        File[] contents = f.listFiles();
        if (contents != null) {
            for (File file : contents) {
                delFile(file);
            }
        }
        f.delete();
    }

    private void makeFile(File f) {
        f.mkdirs();
        new File(f, "IND").mkdirs();
    }

    private void resetUnitario() {
        File F = new File(PCNN_PATH);
        delFile(F);
        makeFile(F);
    }

}
