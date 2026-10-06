package controlador;

import dao.DatabaseConnection;
import dao.LibroDAO;
import dao.PrestamoDAO;
import modelo.Libro;
import modelo.Prestamo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class PrestamoController {

    private final PrestamoDAO prestamoDAO;
    private final LibroDAO libroDAO;

    // Mecanismo sincronizado para evitar operaciones simultáneas
    // sobre el stock y los préstamos.
    private final Object lock = new Object();

    public PrestamoController() {
        prestamoDAO = new PrestamoDAO();
        libroDAO = new LibroDAO();
    }

    public boolean registrarPrestamo(int idEstudiante, int idLibro) {

        if (idEstudiante <= 0 || idLibro <= 0) {
            return false;
        }

        synchronized (lock) {

            Connection connection = DatabaseConnection
                    .getInstance()
                    .getConnection();

            try {
                connection.setAutoCommit(false);

                // Bloqueamos el registro del libro mientras
                // comprobamos y modificamos el stock.
                String sqlStock = """
                        SELECT stock
                        FROM libros
                        WHERE id = ?
                        FOR UPDATE
                        """;

                int stock;

                try (PreparedStatement statement =
                             connection.prepareStatement(sqlStock)) {

                    statement.setInt(1, idLibro);

                    try (ResultSet result = statement.executeQuery()) {

                        if (!result.next()) {
                            connection.rollback();
                            return false;
                        }

                        stock = result.getInt("stock");
                    }
                }

                // No hay ejemplares disponibles.
                if (stock <= 0) {
                    connection.rollback();
                    return false;
                }

                LocalDate fechaPrestamo = LocalDate.now();
                LocalDate fechaDevolucion = fechaPrestamo.plusDays(7);

                // Descontar un ejemplar del stock.
                String sqlActualizarStock = """
                        UPDATE libros
                        SET stock = stock - 1
                        WHERE id = ?
                        """;

                try (PreparedStatement statement =
                             connection.prepareStatement(sqlActualizarStock)) {

                    statement.setInt(1, idLibro);
                    statement.executeUpdate();
                }

                // Registrar el préstamo.
                String sqlPrestamo = """
                        INSERT INTO prestamos
                        (id_estudiante, id_libro, fecha_prestamo,
                         fecha_devolucion, devuelto)
                        VALUES (?, ?, ?, ?, ?)
                        """;

                try (PreparedStatement statement =
                             connection.prepareStatement(sqlPrestamo)) {

                    statement.setInt(1, idEstudiante);
                    statement.setInt(2, idLibro);
                    statement.setDate(
                            3,
                            java.sql.Date.valueOf(fechaPrestamo)
                    );
                    statement.setDate(
                            4,
                            java.sql.Date.valueOf(fechaDevolucion)
                    );
                    statement.setBoolean(5, false);

                    statement.executeUpdate();
                }

                // Confirmamos ambas operaciones juntas.
                connection.commit();

                return true;

            } catch (SQLException e) {

                try {
                    connection.rollback();
                } catch (SQLException rollbackException) {
                    rollbackException.printStackTrace();
                }

                e.printStackTrace();
                return false;

            } finally {

                try {
                    connection.setAutoCommit(true);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public List<Prestamo> listarPrestamos() {
        return prestamoDAO.listarTodos();
    }

    public List<Prestamo> listarPrestamosActivos() {
        return prestamoDAO.listarActivos();
    }

    public List<Prestamo> listarHistorialEstudiante(int idEstudiante) {

        if (idEstudiante <= 0) {
            return List.of();
        }

        return prestamoDAO.listarPorEstudiante(idEstudiante);
    }

    public boolean registrarDevolucion(int idPrestamo) {

        if (idPrestamo <= 0) {
            return false;
        }

        synchronized (lock) {

            Connection connection = DatabaseConnection
                    .getInstance()
                    .getConnection();

            try {
                connection.setAutoCommit(false);

                // Buscamos el préstamo y bloqueamos su registro.
                String sqlPrestamo = """
                        SELECT id_libro, devuelto
                        FROM prestamos
                        WHERE id = ?
                        FOR UPDATE
                        """;

                int idLibro;
                boolean devuelto;

                try (PreparedStatement statement =
                             connection.prepareStatement(sqlPrestamo)) {

                    statement.setInt(1, idPrestamo);

                    try (ResultSet result = statement.executeQuery()) {

                        if (!result.next()) {
                            connection.rollback();
                            return false;
                        }

                        idLibro = result.getInt("id_libro");
                        devuelto = result.getBoolean("devuelto");
                    }
                }

                // El préstamo ya fue devuelto.
                if (devuelto) {
                    connection.rollback();
                    return false;
                }

                // Marcar préstamo como devuelto.
                String sqlDevolver = """
                        UPDATE prestamos
                        SET devuelto = TRUE
                        WHERE id = ?
                        """;

                try (PreparedStatement statement =
                             connection.prepareStatement(sqlDevolver)) {

                    statement.setInt(1, idPrestamo);
                    statement.executeUpdate();
                }

                // Devolver el ejemplar al stock.
                String sqlStock = """
                        UPDATE libros
                        SET stock = stock + 1
                        WHERE id = ?
                        """;

                try (PreparedStatement statement =
                             connection.prepareStatement(sqlStock)) {

                    statement.setInt(1, idLibro);
                    statement.executeUpdate();
                }

                // Confirmamos las dos operaciones juntas.
                connection.commit();

                return true;

            } catch (SQLException e) {

                try {
                    connection.rollback();
                } catch (SQLException rollbackException) {
                    rollbackException.printStackTrace();
                }

                e.printStackTrace();
                return false;

            } finally {

                try {
                    connection.setAutoCommit(true);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public boolean estaAtrasado(Prestamo prestamo) {

        if (prestamo == null || prestamo.isDevuelto()) {
            return false;
        }

        return LocalDate.now()
                .isAfter(prestamo.getFechaDevolucion());
    }
}