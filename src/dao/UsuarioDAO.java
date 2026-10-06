package dao;

import modelo.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO {

    private final Connection connection;

    public UsuarioDAO() {
        connection = DatabaseConnection.getInstance().getConnection();
    }

    public Usuario login(String correo, String contraseña) {

        String sql = """
                SELECT id, nombre, rut, correo, contraseña, rol
                FROM usuarios
                WHERE correo = ? AND contraseña = ?
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, correo);
            statement.setString(2, contraseña);

            try (ResultSet result = statement.executeQuery()) {

                if (result.next()) {

                    return new Usuario(
                            result.getInt("id"),
                            result.getString("nombre"),
                            result.getString("rut"),
                            result.getString("correo"),
                            result.getString("contraseña"),
                            result.getString("rol")
                    );
                }

            }

        } catch (SQLException e) {
            System.out.println("Error al realizar el inicio de sesión.");
            e.printStackTrace();
        }

        return null;
    }
}