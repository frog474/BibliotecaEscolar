package dao;

import modelo.Prestamo;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PrestamoDAO {

    private final Connection connection;

    public PrestamoDAO() {
        connection = DatabaseConnection.getInstance().getConnection();
    }

    // Registrar préstamo
    public boolean guardar(Prestamo prestamo) {

        String sql = """
                INSERT INTO prestamos
                (id_estudiante, id_libro, fecha_prestamo, fecha_devolucion, devuelto)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, prestamo.getIdEstudiante());
            statement.setInt(2, prestamo.getIdLibro());
            statement.setDate(3, Date.valueOf(prestamo.getFechaPrestamo()));
            statement.setDate(4, Date.valueOf(prestamo.getFechaDevolucion()));
            statement.setBoolean(5, prestamo.isDevuelto());

            statement.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Error al guardar préstamo.");
            e.printStackTrace();
            return false;
        }
    }

    // Listar todos los préstamos
    public List<Prestamo> listarTodos() {

        List<Prestamo> prestamos = new ArrayList<>();

        String sql = """
                SELECT id, id_estudiante, id_libro,
                       fecha_prestamo, fecha_devolucion, devuelto
                FROM prestamos
                ORDER BY id
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {

                LocalDate fechaPrestamo =
                        result.getDate("fecha_prestamo").toLocalDate();

                LocalDate fechaDevolucion =
                        result.getDate("fecha_devolucion").toLocalDate();

                Prestamo prestamo = new Prestamo(
                        result.getInt("id"),
                        result.getInt("id_estudiante"),
                        result.getInt("id_libro"),
                        fechaPrestamo,
                        fechaDevolucion,
                        result.getBoolean("devuelto")
                );

                prestamos.add(prestamo);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar préstamos.");
            e.printStackTrace();
        }

        return prestamos;
    }

    // Registrar devolución
    public boolean devolver(int idPrestamo) {

        String sql = """
                UPDATE prestamos
                SET devuelto = TRUE
                WHERE id = ?
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, idPrestamo);

            int filas = statement.executeUpdate();

            return filas > 0;

        } catch (SQLException e) {
            System.out.println("Error al registrar devolución.");
            e.printStackTrace();
            return false;
        }
    }

    // Buscar préstamo por ID
    public Prestamo buscarPorId(int id) {

        String sql = """
                SELECT id, id_estudiante, id_libro,
                       fecha_prestamo, fecha_devolucion, devuelto
                FROM prestamos
                WHERE id = ?
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet result = statement.executeQuery()) {

                if (result.next()) {

                    return new Prestamo(
                            result.getInt("id"),
                            result.getInt("id_estudiante"),
                            result.getInt("id_libro"),
                            result.getDate("fecha_prestamo").toLocalDate(),
                            result.getDate("fecha_devolucion").toLocalDate(),
                            result.getBoolean("devuelto")
                    );
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar préstamo.");
            e.printStackTrace();
        }

        return null;
    }

    // Listar préstamos activos
    public List<Prestamo> listarActivos() {

        List<Prestamo> prestamos = new ArrayList<>();

        String sql = """
                SELECT id, id_estudiante, id_libro,
                       fecha_prestamo, fecha_devolucion, devuelto
                FROM prestamos
                WHERE devuelto = FALSE
                ORDER BY fecha_devolucion
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {

                Prestamo prestamo = new Prestamo(
                        result.getInt("id"),
                        result.getInt("id_estudiante"),
                        result.getInt("id_libro"),
                        result.getDate("fecha_prestamo").toLocalDate(),
                        result.getDate("fecha_devolucion").toLocalDate(),
                        result.getBoolean("devuelto")
                );

                prestamos.add(prestamo);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar préstamos activos.");
            e.printStackTrace();
        }

        return prestamos;
    }

    // Historial de préstamos de un estudiante
    public List<Prestamo> listarPorEstudiante(int idEstudiante) {

        List<Prestamo> prestamos = new ArrayList<>();

        String sql = """
                SELECT id, id_estudiante, id_libro,
                       fecha_prestamo, fecha_devolucion, devuelto
                FROM prestamos
                WHERE id_estudiante = ?
                ORDER BY fecha_prestamo DESC
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, idEstudiante);

            try (ResultSet result = statement.executeQuery()) {

                while (result.next()) {

                    Prestamo prestamo = new Prestamo(
                            result.getInt("id"),
                            result.getInt("id_estudiante"),
                            result.getInt("id_libro"),
                            result.getDate("fecha_prestamo").toLocalDate(),
                            result.getDate("fecha_devolucion").toLocalDate(),
                            result.getBoolean("devuelto")
                    );

                    prestamos.add(prestamo);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener historial del estudiante.");
            e.printStackTrace();
        }

        return prestamos;
    }
}