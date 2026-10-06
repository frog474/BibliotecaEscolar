package vista;

import javax.swing.*;
import controlador.LibroController;
import controlador.CategoriaController;
import modelo.Categoria;
import javax.swing.table.DefaultTableModel;

public class LibrosFrame extends JFrame {

    private JPanel panelPrincipal;
    private JTextField txtTitulo;
    private JTextField txtAutor;
    private JTextField txtIsbn;
    private JTextField txtEditorial;
    private JTextField txtStock;
    private JComboBox cmbCategoria;
    private JButton btnGuardar;
    private JButton btnActualizar;
    private JButton btnEliminar;
    private JTable tablaLibros;

    private final LibroController controller;
    private final CategoriaController categoriaController;
    private DefaultTableModel modeloTabla;

    private int idLibroSeleccionado = -1;

    public LibrosFrame() {
        setTitle("Biblioteca Escolar - Gestión de Libros");
        setContentPane(panelPrincipal);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        controller = new LibroController();
        categoriaController = new CategoriaController();

        cargarCategorias();
        cargarLibros();

        btnGuardar.addActionListener(e -> guardarLibro());

        btnActualizar.addActionListener(e -> actualizarLibro());

        btnEliminar.addActionListener(e -> eliminarLibro());

        tablaLibros.getSelectionModel().addListSelectionListener(
                e -> seleccionarLibro()
        );
    }

    private void cargarCategorias() {
        cmbCategoria.removeAllItems();

        for (Categoria categoria : categoriaController.listarCategorias()) {
            cmbCategoria.addItem(categoria);
        }
    }

    private void guardarLibro() {
        String titulo = txtTitulo.getText().trim();
        String autor = txtAutor.getText().trim();
        String isbn = txtIsbn.getText().trim();
        String editorial = txtEditorial.getText().trim();

        int stock;

        try {
            stock = Integer.parseInt(txtStock.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "El stock debe ser un número entero.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        Categoria categoriaSeleccionada =
                (Categoria) cmbCategoria.getSelectedItem();

        if (categoriaSeleccionada == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Debe seleccionar una categoría.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        int idCategoria = categoriaSeleccionada.getId();

        boolean guardado = controller.registrarLibro(
                titulo,
                autor,
                isbn,
                editorial,
                stock,
                idCategoria
        );

        if (guardado) {
            JOptionPane.showMessageDialog(
                    this,
                    "Libro registrado correctamente."
            );

            cargarLibros();
            limpiarCampos();

        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo registrar el libro.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void actualizarLibro() {

        if (idLibroSeleccionado == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Debe seleccionar un libro de la tabla.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        String titulo = txtTitulo.getText().trim();
        String autor = txtAutor.getText().trim();
        String isbn = txtIsbn.getText().trim();
        String editorial = txtEditorial.getText().trim();

        int stock;

        try {
            stock = Integer.parseInt(txtStock.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "El stock debe ser un número entero.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        Categoria categoriaSeleccionada =
                (Categoria) cmbCategoria.getSelectedItem();

        if (categoriaSeleccionada == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Debe seleccionar una categoría.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        int idCategoria = categoriaSeleccionada.getId();

        boolean actualizado = controller.actualizarLibro(
                idLibroSeleccionado,
                titulo,
                autor,
                isbn,
                editorial,
                stock,
                idCategoria
        );

        if (actualizado) {
            JOptionPane.showMessageDialog(
                    this,
                    "Libro actualizado correctamente."
            );

            cargarLibros();
            limpiarCampos();

        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo actualizar el libro.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void eliminarLibro() {

        if (idLibroSeleccionado == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Debe seleccionar un libro de la tabla.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(
                this,
                "¿Está seguro de que desea eliminar este libro?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }

        boolean eliminado = controller.eliminarLibro(idLibroSeleccionado);

        if (eliminado) {
            JOptionPane.showMessageDialog(
                    this,
                    "Libro eliminado correctamente."
            );

            cargarLibros();
            limpiarCampos();

        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo eliminar el libro.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void seleccionarLibro() {
        int fila = tablaLibros.getSelectedRow();

        if (fila >= 0) {

            idLibroSeleccionado = Integer.parseInt(
                    modeloTabla.getValueAt(fila, 0).toString()
            );

            txtTitulo.setText(
                    modeloTabla.getValueAt(fila, 1).toString()
            );

            txtAutor.setText(
                    modeloTabla.getValueAt(fila, 2).toString()
            );

            txtIsbn.setText(
                    modeloTabla.getValueAt(fila, 3).toString()
            );

            txtEditorial.setText(
                    modeloTabla.getValueAt(fila, 4).toString()
            );

            txtStock.setText(
                    modeloTabla.getValueAt(fila, 5).toString()
            );

            int idCategoria = Integer.parseInt(
                    modeloTabla.getValueAt(fila, 6).toString()
            );

            for (int i = 0; i < cmbCategoria.getItemCount(); i++) {

                Categoria categoria =
                        (Categoria) cmbCategoria.getItemAt(i);

                if (categoria.getId() == idCategoria) {
                    cmbCategoria.setSelectedIndex(i);
                    break;
                }
            }
        }
    }

    private void limpiarCampos() {

        txtTitulo.setText("");
        txtAutor.setText("");
        txtIsbn.setText("");
        txtEditorial.setText("");
        txtStock.setText("");

        if (cmbCategoria.getItemCount() > 0) {
            cmbCategoria.setSelectedIndex(0);
        }

        idLibroSeleccionado = -1;
        tablaLibros.clearSelection();
    }

    private void cargarLibros() {

        modeloTabla = new DefaultTableModel(
                new Object[]{
                        "ID",
                        "Título",
                        "Autor",
                        "ISBN",
                        "Editorial",
                        "Stock",
                        "Categoría"
                },
                0
        );

        tablaLibros.setModel(modeloTabla);

        for (var libro : controller.listarLibros()) {

            modeloTabla.addRow(new Object[]{
                    libro.getId(),
                    libro.getTitulo(),
                    libro.getAutor(),
                    libro.getIsbn(),
                    libro.getEditorial(),
                    libro.getStock(),
                    libro.getIdCategoria()
            });
        }
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            LibrosFrame ventana = new LibrosFrame();
            ventana.setVisible(true);

        });
    }
}