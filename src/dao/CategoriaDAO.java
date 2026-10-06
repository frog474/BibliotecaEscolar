package dao;

import modelo.Categoria;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAO {

    private final Connection connection;

    public CategoriaDAO() {
        connection = DatabaseConnection.getInstance().getConnection();
    }

    // Registrar categoría
    public boolean guardar(Categoria categoria) {

        String sql = """
                INSERT INTO categorias (nombre)
                VALUES (?)
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, categoria.getNombre());

            statement.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Error al guardar categoría.");
            e.printStackTrace();
            return false;
        }
    }

    // Listar categorías
    public List<Categoria> listarTodos() {

        List<Categoria> categorias = new ArrayList<>();

        String sql = """
                SELECT id, nombre
                FROM categorias
                ORDER BY id
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {

                Categoria categoria = new Categoria(
                        result.getInt("id"),
                        result.getString("nombre")
                );

                categorias.add(categoria);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar categorías.");
            e.printStackTrace();
        }

        return categorias;
    }

    // Modificar categoría
    public boolean actualizar(Categoria categoria) {

        String sql = """
                UPDATE categorias
                SET nombre = ?
                WHERE id = ?
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, categoria.getNombre());
            statement.setInt(2, categoria.getId());

            statement.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Error al actualizar categoría.");
            e.printStackTrace();
            return false;
        }
    }

    // Eliminar categoría
    public boolean eliminar(int id) {

        String sql = "DELETE FROM categorias WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            statement.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Error al eliminar categoría.");
            e.printStackTrace();
            return false;
        }
    }
}