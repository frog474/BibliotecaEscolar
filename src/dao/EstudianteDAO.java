package dao;

import modelo.Estudiante;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EstudianteDAO {

    private final Connection connection;

    public EstudianteDAO() {
        connection = DatabaseConnection.getInstance().getConnection();
    }

    // Registrar estudiante
    public boolean guardar(Estudiante estudiante) {

        String sql = """
                INSERT INTO estudiantes (nombre, rut, curso, correo)
                VALUES (?, ?, ?, ?)
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, estudiante.getNombre());
            statement.setString(2, estudiante.getRut());
            statement.setString(3, estudiante.getCurso());
            statement.setString(4, estudiante.getCorreo());

            statement.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Error al guardar estudiante.");
            e.printStackTrace();
            return false;
        }
    }

    // Listar estudiantes
    public List<Estudiante> listarTodos() {

        List<Estudiante> estudiantes = new ArrayList<>();

        String sql = """
                SELECT id, nombre, rut, curso, correo
                FROM estudiantes
                ORDER BY id
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {

                Estudiante estudiante = new Estudiante(
                        result.getInt("id"),
                        result.getString("nombre"),
                        result.getString("rut"),
                        result.getString("curso"),
                        result.getString("correo")
                );

                estudiantes.add(estudiante);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar estudiantes.");
            e.printStackTrace();
        }

        return estudiantes;
    }

    // Modificar estudiante
    public boolean actualizar(Estudiante estudiante) {

        String sql = """
                UPDATE estudiantes
                SET nombre = ?, rut = ?, curso = ?, correo = ?
                WHERE id = ?
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, estudiante.getNombre());
            statement.setString(2, estudiante.getRut());
            statement.setString(3, estudiante.getCurso());
            statement.setString(4, estudiante.getCorreo());
            statement.setInt(5, estudiante.getId());

            statement.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Error al actualizar estudiante.");
            e.printStackTrace();
            return false;
        }
    }

    // Eliminar estudiante
    public boolean eliminar(int id) {

        String sql = "DELETE FROM estudiantes WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            statement.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Error al eliminar estudiante.");
            e.printStackTrace();
            return false;
        }
    }
}