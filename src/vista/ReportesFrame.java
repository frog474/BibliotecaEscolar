package vista;

import controlador.EstudianteController;
import controlador.ReporteController;
import modelo.Estudiante;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class ReportesFrame extends JFrame {

    private JPanel panelPrincipal;
    private JButton btnLibrosMasPrestados;
    private JButton btnHistorial;
    private JButton btnPrestamosActuales;
    private JTable tablaReportes;

    private final ReporteController reporteController;
    private final EstudianteController estudianteController;

    private DefaultTableModel modeloTabla;

    public ReportesFrame() {

        setTitle("Biblioteca Escolar - Reportes");
        setContentPane(panelPrincipal);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        reporteController = new ReporteController();
        estudianteController = new EstudianteController();

        btnLibrosMasPrestados.addActionListener(
                e -> mostrarLibrosMasPrestados()
        );

        btnHistorial.addActionListener(
                e -> mostrarHistorialEstudiante()
        );

        btnPrestamosActuales.addActionListener(
                e -> mostrarPrestamosActuales()
        );
    }

    private void mostrarLibrosMasPrestados() {

        List<Object[]> resultados =
                reporteController.obtenerLibrosMasPrestados();

        modeloTabla = new DefaultTableModel(
                new Object[]{
                        "Libro",
                        "Autor",
                        "Cantidad de préstamos"
                },
                0
        );

        tablaReportes.setModel(modeloTabla);

        for (Object[] fila : resultados) {
            modeloTabla.addRow(fila);
        }
    }

    private void mostrarHistorialEstudiante() {

        List<Estudiante> estudiantes =
                estudianteController.listarEstudiantes();

        if (estudiantes.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "No hay estudiantes registrados.",
                    "Aviso",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        String[] opciones =
                new String[estudiantes.size()];

        for (int i = 0; i < estudiantes.size(); i++) {

            Estudiante estudiante = estudiantes.get(i);

            opciones[i] =
                    estudiante.getId()
                            + " - "
                            + estudiante.getNombre();
        }

        String seleccion =
                (String) JOptionPane.showInputDialog(
                        this,
                        "Seleccione un estudiante:",
                        "Historial de estudiante",
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        opciones,
                        opciones[0]
                );

        if (seleccion == null) {
            return;
        }

        int idEstudiante =
                Integer.parseInt(
                        seleccion.split(" - ")[0]
                );

        List<Object[]> resultados =
                reporteController.obtenerHistorialEstudiante(
                        idEstudiante
                );

        modeloTabla = new DefaultTableModel(
                new Object[]{
                        "Estudiante",
                        "Libro",
                        "Fecha préstamo",
                        "Fecha devolución",
                        "Estado"
                },
                0
        );

        tablaReportes.setModel(modeloTabla);

        for (Object[] fila : resultados) {
            modeloTabla.addRow(fila);
        }

        if (resultados.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "El estudiante seleccionado no tiene préstamos registrados.",
                    "Información",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    private void mostrarPrestamosActuales() {

        List<Object[]> resultados =
                reporteController.obtenerPrestamosActuales();

        modeloTabla = new DefaultTableModel(
                new Object[]{
                        "ID",
                        "Estudiante",
                        "Libro",
                        "Fecha préstamo",
                        "Fecha devolución"
                },
                0
        );

        tablaReportes.setModel(modeloTabla);

        for (Object[] fila : resultados) {
            modeloTabla.addRow(fila);
        }

        if (resultados.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "No hay préstamos actualmente activos.",
                    "Información",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            ReportesFrame ventana =
                    new ReportesFrame();

            ventana.setVisible(true);
        });
    }
}