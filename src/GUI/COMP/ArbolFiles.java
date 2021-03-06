package GUI.COMP;

import GUI.MejoraImagenUNITARIO;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import static UTIL.FileUtil.evalFiles;

public class ArbolFiles extends JSplitPane implements TreeSelectionListener {

    Imagen Jlbl_img;
    JTree arbol;
    File path;
    String CMD = "";

    public ArbolFiles() {
        Jlbl_img = new Imagen();
        arbol = new JTree();
        arbol.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getClickCount() == 2 && evalFiles(getFileSelected())) {
                    new MejoraImagenUNITARIO(CMD, getFileSelected()).setVisible(true);
                }
            }
        });
        arbol.setModel(null);
        JScrollPane JSP = new JScrollPane(arbol);
        arbol.getSelectionModel().addTreeSelectionListener(this);
        setLeftComponent(JSP);
        setRightComponent(Jlbl_img);
        setDividerLocation(180);
    }

    public void setTreeFiles(File path) {
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

    public void setTreeDirs(String CMD, File path) {
        this.path = path;
        this.CMD = CMD;
        DefaultMutableTreeNode carpetaRaiz = new DefaultMutableTreeNode(path.getName());
        DefaultTreeModel model = new DefaultTreeModel(carpetaRaiz);
        arbol.setModel(model);
        for (File file : path.listFiles()) {
            if (file.isDirectory()) {
                model.insertNodeInto(new DefaultMutableTreeNode(file.getName()), carpetaRaiz, model.getChildCount(carpetaRaiz));
            }
        }
    }

    public void delTree() {
        arbol.setModel(null);
        Jlbl_img.delPic();
    }

    @Override
    public void valueChanged(TreeSelectionEvent e) {
        if (path != null && e.isAddedPath()) {
            File f = getFileSelected();
            if (f.isFile()) {
                Jlbl_img.setLabelPic(f, 200);
            } else {
                Jlbl_img.setLabelPic(f);
            }
        } else {
            Jlbl_img.delPic();
        }
    }

    private File getFileSelected() {
        String s = "\\";
        TreePath rutaSeleccionada = arbol.getSelectionPath();
        Object[] nodos = rutaSeleccionada.getPath();
        for (int i = 0; i < nodos.length; i++) {
            Object nodo = nodos[i];
            s += nodo.toString() + "\\";
        }
        s = s.substring(0, s.length() - 1);
        File f = new File(path.getParent() + s);
        return f;
    }
}
