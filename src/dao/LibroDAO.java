package dao;

import modelo.Libro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LibroDAO {

    private final Connection connection;

    public LibroDAO() {
        connection = DatabaseConnection.getInstance().getConnection();
    }

    // Registrar libro
    public boolean guardar(Libro libro) {

        String sql = """
                INSERT INTO libros (titulo, autor, isbn, editorial, stock, id_categoria)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, libro.getTitulo());
            statement.setString(2, libro.getAutor());
            statement.setString(3, libro.getIsbn());
            statement.setString(4, libro.getEditorial());
            statement.setInt(5, libro.getStock());
            statement.setInt(6, libro.getIdCategoria());

            statement.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Error al guardar libro.");
            e.printStackTrace();
            return false;
        }
    }

    // Listar libros
    public List<Libro> listarTodos() {

        List<Libro> libros = new ArrayList<>();

        String sql = """
                SELECT id, titulo, autor, isbn, editorial, stock, id_categoria
                FROM libros
                ORDER BY id
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {

                Libro libro = new Libro(
                        result.getInt("id"),
                        result.getString("titulo"),
                        result.getString("autor"),
                        result.getString("isbn"),
                        result.getString("editorial"),
                        result.getInt("stock"),
                        result.getInt("id_categoria")
                );

                libros.add(libro);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar libros.");
            e.printStackTrace();
        }

        return libros;
    }

    // Modificar libro
    public boolean actualizar(Libro libro) {

        String sql = """
                UPDATE libros
                SET titulo = ?, autor = ?, isbn = ?, editorial = ?,
                    stock = ?, id_categoria = ?
                WHERE id = ?
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, libro.getTitulo());
            statement.setString(2, libro.getAutor());
            statement.setString(3, libro.getIsbn());
            statement.setString(4, libro.getEditorial());
            statement.setInt(5, libro.getStock());
            statement.setInt(6, libro.getIdCategoria());
            statement.setInt(7, libro.getId());

            statement.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Error al actualizar libro.");
            e.printStackTrace();
            return false;
        }
    }

    // Eliminar libro
    public boolean eliminar(int id) {

        String sql = "DELETE FROM libros WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            statement.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Error al eliminar libro.");
            e.printStackTrace();
            return false;
        }
    }

    // Buscar libro por ID
    public Libro buscarPorId(int id) {

        String sql = """
                SELECT id, titulo, autor, isbn, editorial, stock, id_categoria
                FROM libros
                WHERE id = ?
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet result = statement.executeQuery()) {

                if (result.next()) {

                    return new Libro(
                            result.getInt("id"),
                            result.getString("titulo"),
                            result.getString("autor"),
                            result.getString("isbn"),
                            result.getString("editorial"),
                            result.getInt("stock"),
                            result.getInt("id_categoria")
                    );
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar libro.");
            e.printStackTrace();
        }

        return null;
    }
}