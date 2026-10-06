package vista;

import controlador.EstudianteController;
import controlador.LibroController;
import controlador.PrestamoController;
import modelo.Estudiante;
import modelo.Libro;
import modelo.Prestamo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.time.LocalDate;
import java.util.List;

public class PrestamosFrame extends JFrame {

    private JPanel panelPrincipal;
    private JComboBox cmbEstudiante;
    private JComboBox cmbLibro;
    private JButton btnPrestar;
    private JButton btnDevolver;
    private JTable tablaPrestamos;
    private JButton btnActualizar;

    private final EstudianteController estudianteController;
    private final LibroController libroController;
    private final PrestamoController prestamoController;

    private DefaultTableModel modeloTabla;

    public PrestamosFrame() {

        setTitle("Biblioteca Escolar - Préstamos y Devoluciones");
        setContentPane(panelPrincipal);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        estudianteController = new EstudianteController();
        libroController = new LibroController();
        prestamoController = new PrestamoController();

        cargarEstudiantes();
        cargarLibros();
        cargarPrestamos();

        btnPrestar.addActionListener(e -> registrarPrestamo());
        btnDevolver.addActionListener(e -> registrarDevolucion());
        btnActualizar.addActionListener(e -> {
            cargarEstudiantes();
            cargarLibros();
            cargarPrestamos();
        });
    }

    private void cargarEstudiantes() {

        cmbEstudiante.removeAllItems();

        List<Estudiante> estudiantes =
                estudianteController.listarEstudiantes();

        for (Estudiante estudiante : estudiantes) {
            cmbEstudiante.addItem(
                    estudiante.getId() + " - " + estudiante.getNombre()
            );
        }
    }

    private void cargarLibros() {

        cmbLibro.removeAllItems();

        List<Libro> libros = libroController.listarLibros();

        for (Libro libro : libros) {

            cmbLibro.addItem(
                    libro.getId()
                            + " - "
                            + libro.getTitulo()
                            + " (Stock: "
                            + libro.getStock()
                            + ")"
            );
        }
    }

    private void cargarPrestamos() {

        modeloTabla = new DefaultTableModel(
                new Object[]{
                        "ID",
                        "Estudiante",
                        "Libro",
                        "Fecha Préstamo",
                        "Fecha Devolución",
                        "Estado"
                },
                0
        );

        tablaPrestamos.setModel(modeloTabla);

        List<Prestamo> prestamos =
                prestamoController.listarPrestamos();

        List<Estudiante> estudiantes =
                estudianteController.listarEstudiantes();

        List<Libro> libros =
                libroController.listarLibros();

        for (Prestamo prestamo : prestamos) {

            String nombreEstudiante =
                    obtenerNombreEstudiante(
                            estudiantes,
                            prestamo.getIdEstudiante()
                    );

            String nombreLibro =
                    obtenerTituloLibro(
                            libros,
                            prestamo.getIdLibro()
                    );

            String estado;

            if (prestamo.isDevuelto()) {
                estado = "DEVUELTO";
            } else if (prestamoController.estaAtrasado(prestamo)) {
                estado = "ATRASADO";
            } else {
                estado = "PRESTADO";
            }

            modeloTabla.addRow(
                    new Object[]{
                            prestamo.getId(),
                            nombreEstudiante,
                            nombreLibro,
                            prestamo.getFechaPrestamo(),
                            prestamo.getFechaDevolucion(),
                            estado
                    }
            );
        }
    }

    private String obtenerNombreEstudiante(
            List<Estudiante> estudiantes,
            int idEstudiante) {

        for (Estudiante estudiante : estudiantes) {

            if (estudiante.getId() == idEstudiante) {
                return estudiante.getNombre();
            }
        }

        return "Desconocido";
    }

    private String obtenerTituloLibro(
            List<Libro> libros,
            int idLibro) {

        for (Libro libro : libros) {

            if (libro.getId() == idLibro) {
                return libro.getTitulo();
            }
        }

        return "Desconocido";
    }

    private void registrarPrestamo() {

        if (cmbEstudiante.getSelectedIndex() == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Debe seleccionar un estudiante.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        if (cmbLibro.getSelectedIndex() == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Debe seleccionar un libro.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        String estudianteSeleccionado =
                cmbEstudiante.getSelectedItem().toString();

        String libroSeleccionado =
                cmbLibro.getSelectedItem().toString();

        int idEstudiante =
                Integer.parseInt(
                        estudianteSeleccionado
                                .split(" - ")[0]
                );

        int idLibro =
                Integer.parseInt(
                        libroSeleccionado
                                .split(" - ")[0]
                );

        btnPrestar.setEnabled(false);

        // Ejecutamos el préstamo en un hilo independiente
        // para no bloquear la interfaz gráfica.
        Thread hiloPrestamo = new Thread(() -> {

            boolean resultado =
                    prestamoController.registrarPrestamo(
                            idEstudiante,
                            idLibro
                    );

            SwingUtilities.invokeLater(() -> {

                btnPrestar.setEnabled(true);

                if (resultado) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Préstamo registrado correctamente.\n"
                                    + "La fecha de devolución es en 7 días."
                    );

                    cargarLibros();
                    cargarPrestamos();

                } else {

                    JOptionPane.showMessageDialog(
                            this,
                            "No se pudo registrar el préstamo.\n"
                                    + "Verifique que haya stock disponible.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            });

        });

        hiloPrestamo.start();
    }

    private void registrarDevolucion() {

        int fila = tablaPrestamos.getSelectedRow();

        if (fila == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Debe seleccionar un préstamo de la tabla.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int idPrestamo =
                Integer.parseInt(
                        modeloTabla
                                .getValueAt(fila, 0)
                                .toString()
                );

        String estado =
                modeloTabla
                        .getValueAt(fila, 5)
                        .toString();

        if (estado.equals("DEVUELTO")) {

            JOptionPane.showMessageDialog(
                    this,
                    "Este préstamo ya fue devuelto.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int confirmacion =
                JOptionPane.showConfirmDialog(
                        this,
                        "¿Registrar la devolución de este préstamo?",
                        "Confirmar devolución",
                        JOptionPane.YES_NO_OPTION
                );

        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }

        btnDevolver.setEnabled(false);

        // La devolución también se ejecuta
        // en un hilo independiente.
        Thread hiloDevolucion = new Thread(() -> {

            boolean resultado =
                    prestamoController.registrarDevolucion(
                            idPrestamo
                    );

            SwingUtilities.invokeLater(() -> {

                btnDevolver.setEnabled(true);

                if (resultado) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Devolución registrada correctamente."
                    );

                    cargarLibros();
                    cargarPrestamos();

                } else {

                    JOptionPane.showMessageDialog(
                            this,
                            "No se pudo registrar la devolución.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            });

        });

        hiloDevolucion.start();
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            PrestamosFrame ventana =
                    new PrestamosFrame();

            ventana.setVisible(true);
        });
    }
}