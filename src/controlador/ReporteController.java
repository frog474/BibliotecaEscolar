package controlador;

import dao.ReporteDAO;

import java.util.List;

public class ReporteController {

    private final ReporteDAO reporteDAO;

    public ReporteController() {
        reporteDAO = new ReporteDAO();
    }

    public List<Object[]> obtenerLibrosMasPrestados() {
        return reporteDAO.librosMasPrestados();
    }

    public List<Object[]> obtenerHistorialEstudiante(int idEstudiante) {

        if (idEstudiante <= 0) {
            return List.of();
        }

        return reporteDAO.historialEstudiante(idEstudiante);
    }

    public List<Object[]> obtenerPrestamosActuales() {
        return reporteDAO.prestamosActuales();
    }
}