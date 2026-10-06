package vista;

import controlador.CategoriaController;
import modelo.Categoria;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class CategoriasFrame extends JFrame {

    private JPanel panelPrincipal;
    private JTextField txtNombre;
    private JButton btnGuardar;
    private JButton btnActualizar;
    private JButton btnEliminar;
    private JTable tablaCategorias;

    private final CategoriaController categoriaController;
    private DefaultTableModel modeloTabla;

    public CategoriasFrame() {

        setTitle("Biblioteca Escolar - Categorías");
        setContentPane(panelPrincipal);
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        categoriaController = new CategoriaController();

        configurarTabla();
        cargarCategorias();

        btnGuardar.addActionListener(e -> guardarCategoria());
        btnActualizar.addActionListener(e -> actualizarCategoria());
        btnEliminar.addActionListener(e -> eliminarCategoria());

        tablaCategorias.getSelectionModel().addListSelectionListener(
                e -> cargarCategoriaSeleccionada()
        );
    }

    private void configurarTabla() {

        modeloTabla = new DefaultTableModel(
                new Object[]{
                        "ID",
                        "Nombre"
                },
                0
        );

        tablaCategorias.setModel(modeloTabla);
    }

    private void cargarCategorias() {

        modeloTabla.setRowCount(0);

        List<Categoria> categorias =
                categoriaController.listarCategorias();

        for (Categoria categoria : categorias) {

            modeloTabla.addRow(
                    new Object[]{
                            categoria.getId(),
                            categoria.getNombre()
                    }
            );
        }
    }

    private void cargarCategoriaSeleccionada() {

        int fila = tablaCategorias.getSelectedRow();

        if (fila == -1) {
            return;
        }

        String nombre =
                modeloTabla.getValueAt(fila, 1).toString();

        txtNombre.setText(nombre);
    }

    private void guardarCategoria() {

        String nombre = txtNombre.getText().trim();

        if (nombre.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Debe ingresar un nombre para la categoría.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        boolean resultado =
                categoriaController.registrarCategoria(nombre);

        if (resultado) {

            JOptionPane.showMessageDialog(
                    this,
                    "Categoría registrada correctamente.",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE
            );

            txtNombre.setText("");
            cargarCategorias();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo registrar la categoría.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void actualizarCategoria() {

        int fila = tablaCategorias.getSelectedRow();

        if (fila == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione una categoría de la tabla.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int id =
                Integer.parseInt(
                        modeloTabla.getValueAt(fila, 0).toString()
                );

        String nombre = txtNombre.getText().trim();

        if (nombre.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Debe ingresar un nombre para la categoría.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        boolean resultado =
                categoriaController.actualizarCategoria(
                        id,
                        nombre
                );

        if (resultado) {

            JOptionPane.showMessageDialog(
                    this,
                    "Categoría actualizada correctamente.",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE
            );

            txtNombre.setText("");
            cargarCategorias();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo actualizar la categoría.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void eliminarCategoria() {

        int fila = tablaCategorias.getSelectedRow();

        if (fila == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione una categoría de la tabla.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int id =
                Integer.parseInt(
                        modeloTabla.getValueAt(fila, 0).toString()
                );

        String nombre =
                modeloTabla.getValueAt(fila, 1).toString();

        int confirmacion =
                JOptionPane.showConfirmDialog(
                        this,
                        "¿Está seguro de eliminar la categoría \""
                                + nombre + "\"?",
                        "Confirmar eliminación",
                        JOptionPane.YES_NO_OPTION
                );

        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }

        boolean resultado =
                categoriaController.eliminarCategoria(id);

        if (resultado) {

            JOptionPane.showMessageDialog(
                    this,
                    "Categoría eliminada correctamente.",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE
            );

            txtNombre.setText("");
            cargarCategorias();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo eliminar la categoría.\n"
                            + "Puede que tenga libros asociados.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            CategoriasFrame ventana =
                    new CategoriasFrame();

            ventana.setVisible(true);
        });
    }
}