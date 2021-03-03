package GUI.COMP;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.io.File;

public class ArbolFiles extends JPanel implements TreeSelectionListener {

    Imagen Jlbl_img;
    JTree arbol;
    File path;

    public ArbolFiles() {
        Jlbl_img = new Imagen();
        arbol = new JTree();
        JScrollPane JSP = new JScrollPane(arbol);
        arbol.getSelectionModel().addTreeSelectionListener(this);
        setLayout(new GridLayout());
        add(JSP);
        add(Jlbl_img);
    }

    public void setTree(File path) {
        this.path = path;
        DefaultMutableTreeNode carpetaRaiz = new DefaultMutableTreeNode(path.getName());
        DefaultTreeModel model = new DefaultTreeModel(carpetaRaiz);
        arbol.setModel(model);
        for (File file : path.listFiles()) {
            if (file.isFile() && (file.toString().toLowerCase().endsWith(".png") || file.toString().toLowerCase().endsWith(".jpg"))) {
                model.insertNodeInto(new DefaultMutableTreeNode(file.getName()), carpetaRaiz, model.getChildCount(carpetaRaiz));
            }
        }
    }

    public void delTree() {
    }

    @Override
    public void valueChanged(TreeSelectionEvent e) {
        String s = "\\";
        TreePath rutaSeleccionada = e.getPath();
        Object[] nodos = rutaSeleccionada.getPath();
        for (int i = 0; i < nodos.length; i++) {
            Object nodo = nodos[i];
            s += nodo.toString() + "\\";
        }
        s = s.substring(0, s.length() - 1);

        Jlbl_img.setLabelPic(new File(path.getParent() + s), 200);
    }
}
