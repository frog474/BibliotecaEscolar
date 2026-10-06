package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReporteDAO {

    private final Connection connection;

    public ReporteDAO() {
        connection = DatabaseConnection.getInstance().getConnection();
    }

    // Reporte 1: libros más prestados
    public List<Object[]> librosMasPrestados() {

        List<Object[]> resultados = new ArrayList<>();

        String sql = """
                SELECT l.titulo, l.autor, COUNT(p.id) AS cantidad_prestamos
                FROM prestamos p
                INNER JOIN libros l ON p.id_libro = l.id
                GROUP BY l.id, l.titulo, l.autor
                ORDER BY cantidad_prestamos DESC
                """;

        try (PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {

                resultados.add(new Object[]{
                        result.getString("titulo"),
                        result.getString("autor"),
                        result.getInt("cantidad_prestamos")
                });
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultados;
    }

    // Reporte 2: historial de un estudiante
    public List<Object[]> historialEstudiante(int idEstudiante) {

        List<Object[]> resultados = new ArrayList<>();

        String sql = """
                SELECT
                    e.nombre AS estudiante,
                    l.titulo AS libro,
                    p.fecha_prestamo,
                    p.fecha_devolucion,
                    p.devuelto
                FROM prestamos p
                INNER JOIN estudiantes e
                    ON p.id_estudiante = e.id
                INNER JOIN libros l
                    ON p.id_libro = l.id
                WHERE e.id = ?
                ORDER BY p.fecha_prestamo DESC
                """;

        try (PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, idEstudiante);

            try (ResultSet result = statement.executeQuery()) {

                while (result.next()) {

                    String estado = result.getBoolean("devuelto")
                            ? "DEVUELTO"
                            : "PENDIENTE";

                    resultados.add(new Object[]{
                            result.getString("estudiante"),
                            result.getString("libro"),
                            result.getDate("fecha_prestamo"),
                            result.getDate("fecha_devolucion"),
                            estado
                    });
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultados;
    }

    // Reporte 3: préstamos actualmente activos
    public List<Object[]> prestamosActuales() {

        List<Object[]> resultados = new ArrayList<>();

        String sql = """
                SELECT
                    p.id,
                    e.nombre AS estudiante,
                    l.titulo AS libro,
                    p.fecha_prestamo,
                    p.fecha_devolucion
                FROM prestamos p
                INNER JOIN estudiantes e
                    ON p.id_estudiante = e.id
                INNER JOIN libros l
                    ON p.id_libro = l.id
                WHERE p.devuelto = FALSE
                ORDER BY p.fecha_devolucion
                """;

        try (PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {

                resultados.add(new Object[]{
                        result.getInt("id"),
                        result.getString("estudiante"),
                        result.getString("libro"),
                        result.getDate("fecha_prestamo"),
                        result.getDate("fecha_devolucion")
                });
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultados;
    }
}