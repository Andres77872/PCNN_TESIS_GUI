import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class main {
    private JPanel root;
    private JPanel JP_Abrir;
    private JButton abrirImagenButton;
    private JTextField Jtxf_ImgPathEntrada;
    private JButton imagenDeReferenciaButton;
    private JTextField Jtxf_ImgPathReferencia;
    private JTabbedPane tabbedPane1;
    private JCheckBox MSECheckBox;
    private JCheckBox PSNRCheckBox;
    private JCheckBox SSIMCheckBox;
    private JCheckBox VIFCheckBox;
    private JCheckBox propuestoCheckBox;
    private JCheckBox SCMCheckBox;
    private JCheckBox PCNNCheckBox;
    private JCheckBox EHCheckBox;
    private JCheckBox SPCNNCheckBox;
    private JCheckBox CLAHECheckBox;
    private JCheckBox ICMCheckBox;
    private JCheckBox _OTRO_CheckBox;
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

    public main() {
        jfc.addChoosableFileFilter(
                new FileNameExtensionFilter(
                        "Imagen", "png"));
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

            }
        });
    }


    public static void main(String[] args) {
        JFrame g = new JFrame("pcnn 0.0.1");
        g.setContentPane(new main().root);
        g.setDefaultCloseOperation(g.EXIT_ON_CLOSE);
        g.pack();
        g.setLocationRelativeTo(null);
        g.setVisible(true);
    }
}
