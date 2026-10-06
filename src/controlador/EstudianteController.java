package controlador;

import dao.EstudianteDAO;
import modelo.Estudiante;

import java.util.List;

public class EstudianteController {

    private final EstudianteDAO estudianteDAO;

    public EstudianteController() {
        estudianteDAO = new EstudianteDAO();
    }

    // Registrar estudiante
    public boolean registrarEstudiante(String nombre, String rut,
                                       String curso, String correo) {

        if (nombre == null || nombre.trim().isEmpty()) {
            return false;
        }

        if (rut == null || rut.trim().isEmpty()) {
            return false;
        }

        if (curso == null || curso.trim().isEmpty()) {
            return false;
        }

        if (correo == null || correo.trim().isEmpty()) {
            return false;
        }

        Estudiante estudiante = new Estudiante(
                0,
                nombre.trim(),
                rut.trim(),
                curso.trim(),
                correo.trim()
        );

        return estudianteDAO.guardar(estudiante);
    }

    // Obtener todos los estudiantes
    public List<Estudiante> listarEstudiantes() {
        return estudianteDAO.listarTodos();
    }

    // Actualizar estudiante
    public boolean actualizarEstudiante(int id, String nombre, String rut,
                                        String curso, String correo) {

        if (id <= 0) {
            return false;
        }

        if (nombre == null || nombre.trim().isEmpty()) {
            return false;
        }

        if (rut == null || rut.trim().isEmpty()) {
            return false;
        }

        if (curso == null || curso.trim().isEmpty()) {
            return false;
        }

        if (correo == null || correo.trim().isEmpty()) {
            return false;
        }

        Estudiante estudiante = new Estudiante(
                id,
                nombre.trim(),
                rut.trim(),
                curso.trim(),
                correo.trim()
        );

        return estudianteDAO.actualizar(estudiante);
    }

    // Eliminar estudiante
    public boolean eliminarEstudiante(int id) {

        if (id <= 0) {
            return false;
        }

        return estudianteDAO.eliminar(id);
    }
}