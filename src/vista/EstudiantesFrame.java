package vista;

import controlador.EstudianteController;
import modelo.Estudiante;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class EstudiantesFrame extends JFrame {

    private JPanel panelPrincipal;
    private JTextField txtNombre;
    private JTextField txtRut;
    private JTextField txtCurso;
    private JTextField txtCorreo;
    private JButton btnGuardar;
    private JButton btnActualizar;
    private JButton btnEliminar;
    private JTable tablaEstudiantes;

    private final EstudianteController controller;
    private DefaultTableModel modeloTabla;

    private int idEstudianteSeleccionado = -1;

    public EstudiantesFrame() {
        setTitle("Biblioteca Escolar - Gestión de Estudiantes");
        setContentPane(panelPrincipal);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        controller = new EstudianteController();

        cargarEstudiantes();

        btnGuardar.addActionListener(e -> guardarEstudiante());

        btnActualizar.addActionListener(e -> actualizarEstudiante());

        btnEliminar.addActionListener(e -> eliminarEstudiante());

        tablaEstudiantes.getSelectionModel().addListSelectionListener(
                e -> seleccionarEstudiante()
        );
    }

    private void cargarEstudiantes() {

        modeloTabla = new DefaultTableModel(
                new Object[]{
                        "ID",
                        "Nombre",
                        "RUT",
                        "Curso",
                        "Correo"
                },
                0
        );

        tablaEstudiantes.setModel(modeloTabla);

        for (Estudiante estudiante : controller.listarEstudiantes()) {

            modeloTabla.addRow(new Object[]{
                    estudiante.getId(),
                    estudiante.getNombre(),
                    estudiante.getRut(),
                    estudiante.getCurso(),
                    estudiante.getCorreo()
            });
        }
    }

    private void guardarEstudiante() {

        String nombre = txtNombre.getText().trim();
        String rut = txtRut.getText().trim();
        String curso = txtCurso.getText().trim();
        String correo = txtCorreo.getText().trim();

        boolean guardado = controller.registrarEstudiante(
                nombre,
                rut,
                curso,
                correo
        );

        if (guardado) {

            JOptionPane.showMessageDialog(
                    this,
                    "Estudiante registrado correctamente."
            );

            cargarEstudiantes();
            limpiarCampos();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo registrar el estudiante.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void seleccionarEstudiante() {

        int fila = tablaEstudiantes.getSelectedRow();

        if (fila >= 0) {

            idEstudianteSeleccionado = Integer.parseInt(
                    modeloTabla.getValueAt(fila, 0).toString()
            );

            txtNombre.setText(
                    modeloTabla.getValueAt(fila, 1).toString()
            );

            txtRut.setText(
                    modeloTabla.getValueAt(fila, 2).toString()
            );

            txtCurso.setText(
                    modeloTabla.getValueAt(fila, 3).toString()
            );

            txtCorreo.setText(
                    modeloTabla.getValueAt(fila, 4).toString()
            );
        }
    }

    private void actualizarEstudiante() {

        if (idEstudianteSeleccionado == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Debe seleccionar un estudiante de la tabla.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        String nombre = txtNombre.getText().trim();
        String rut = txtRut.getText().trim();
        String curso = txtCurso.getText().trim();
        String correo = txtCorreo.getText().trim();

        boolean actualizado = controller.actualizarEstudiante(
                idEstudianteSeleccionado,
                nombre,
                rut,
                curso,
                correo
        );

        if (actualizado) {

            JOptionPane.showMessageDialog(
                    this,
                    "Estudiante actualizado correctamente."
            );

            cargarEstudiantes();
            limpiarCampos();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo actualizar el estudiante.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void eliminarEstudiante() {

        if (idEstudianteSeleccionado == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Debe seleccionar un estudiante de la tabla.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(
                this,
                "¿Está seguro de que desea eliminar este estudiante?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }

        boolean eliminado = controller.eliminarEstudiante(
                idEstudianteSeleccionado
        );

        if (eliminado) {

            JOptionPane.showMessageDialog(
                    this,
                    "Estudiante eliminado correctamente."
            );

            cargarEstudiantes();
            limpiarCampos();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo eliminar el estudiante.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void limpiarCampos() {

        txtNombre.setText("");
        txtRut.setText("");
        txtCurso.setText("");
        txtCorreo.setText("");

        idEstudianteSeleccionado = -1;

        tablaEstudiantes.clearSelection();
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            EstudiantesFrame ventana = new EstudiantesFrame();
            ventana.setVisible(true);

        });
    }
}