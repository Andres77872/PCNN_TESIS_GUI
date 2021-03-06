package GUI.COMP;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;

public class Abrir extends JPanel {
    private JButton abrirImagenButton;
    private JTextField Jtxf_ImgPathEntrada;
    private JButton imagenDeReferenciaButton;
    private JTextField Jtxf_ImgPathReferencia;
    private JButton Jbtb_LimpiarReferencia;
    private JButton Jbtb_LimpiarEntrada;

    private JFileChooser jfc = new JFileChooser();
    File F_imgEntrada, F_imgRef;

    public Abrir(boolean FileOnly) {
        abrirImagenButton = new JButton("Abrir E");
        Jtxf_ImgPathEntrada = new JTextField();
        imagenDeReferenciaButton = new JButton("Abrir R");
        Jtxf_ImgPathReferencia = new JTextField();
        Jbtb_LimpiarReferencia = new JButton("Limpiar");
        Jbtb_LimpiarEntrada = new JButton("Limpiar");

        jfc.addChoosableFileFilter(
                new FileNameExtensionFilter(
                        "Imgaen", "png", "jpg"));

        jfc.setFileSelectionMode((FileOnly) ?
                JFileChooser.FILES_ONLY : JFileChooser.DIRECTORIES_ONLY);

        abrirImagenButton.addActionListener(e -> {
            int r = jfc.showOpenDialog(null);
            if (r == JFileChooser.APPROVE_OPTION) {
                F_imgEntrada = jfc.getSelectedFile();
                Jtxf_ImgPathEntrada.setText(F_imgEntrada.toString());
                evtFileEntrada(F_imgEntrada);
            }
        });
        imagenDeReferenciaButton.addActionListener(e -> {
            int r = jfc.showOpenDialog(null);
            if (r == JFileChooser.APPROVE_OPTION) {
                F_imgRef = jfc.getSelectedFile();
                Jtxf_ImgPathReferencia.setText(F_imgRef.toString());
                evtFileReferencia(F_imgRef);
            }
        });

        Jbtb_LimpiarEntrada.addActionListener(e -> {
            Jtxf_ImgPathEntrada.setText("");
            F_imgEntrada = null;
            evtLimpiarEntrada();
        });
        Jbtb_LimpiarReferencia.addActionListener(e -> {
            Jtxf_ImgPathReferencia.setText("");
            F_imgRef = null;
            evtLimpiarReferencia();
        });

        setLayout(new GridLayout(2, 1, 10, 10));

        JPanel jp1 = new JPanel(new BorderLayout());
        jp1.setLayout(new BoxLayout(jp1, BoxLayout.X_AXIS));
        jp1.add(abrirImagenButton);
        jp1.add(new JLabel("   "));
        jp1.add(Jtxf_ImgPathEntrada);
        jp1.add(new JLabel("   "));
        jp1.add(Jbtb_LimpiarEntrada);

        JPanel jp2 = new JPanel();
        jp2.setLayout(new BoxLayout(jp2, BoxLayout.X_AXIS));
        jp2.add(imagenDeReferenciaButton);
        jp2.add(new JLabel("   "));
        jp2.add(Jtxf_ImgPathReferencia);
        jp2.add(new JLabel("   "));
        jp2.add(Jbtb_LimpiarReferencia);

        add(jp1);
        add(jp2);
    }

    public String getCMD() {
        String cmd = ((F_imgEntrada != null) ? "-img \"" + F_imgEntrada.toString() + "\"" : "") + "," +
                ((F_imgRef != null) ? "-ref \"" + F_imgRef.toString() + "\"" : "");
        return cmd;
    }

    public void evtFileEntrada(File F) {
    }

    public void evtFileReferencia(File F) {
    }

    public void evtLimpiarEntrada() {
    }

    public void evtLimpiarReferencia() {
    }


}
