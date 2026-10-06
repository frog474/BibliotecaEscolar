package controlador;

import dao.LibroDAO;
import modelo.Libro;

import java.util.List;

public class LibroController {

    private final LibroDAO libroDAO;

    public LibroController() {
        libroDAO = new LibroDAO();
    }

    // Registrar libro
    public boolean registrarLibro(String titulo, String autor,
                                  String isbn, String editorial,
                                  int stock, int idCategoria) {

        if (titulo == null || titulo.trim().isEmpty()) {
            return false;
        }

        if (autor == null || autor.trim().isEmpty()) {
            return false;
        }

        if (isbn == null || isbn.trim().isEmpty()) {
            return false;
        }

        if (editorial == null || editorial.trim().isEmpty()) {
            return false;
        }

        if (stock < 0) {
            return false;
        }

        if (idCategoria <= 0) {
            return false;
        }

        Libro libro = new Libro(
                0,
                titulo.trim(),
                autor.trim(),
                isbn.trim(),
                editorial.trim(),
                stock,
                idCategoria
        );

        return libroDAO.guardar(libro);
    }

    // Obtener todos los libros
    public List<Libro> listarLibros() {
        return libroDAO.listarTodos();
    }

    // Buscar libro por ID
    public Libro buscarLibro(int id) {

        if (id <= 0) {
            return null;
        }

        return libroDAO.buscarPorId(id);
    }

    // Actualizar libro
    public boolean actualizarLibro(int id, String titulo,
                                   String autor, String isbn,
                                   String editorial, int stock,
                                   int idCategoria) {

        if (id <= 0) {
            return false;
        }

        if (titulo == null || titulo.trim().isEmpty()) {
            return false;
        }

        if (autor == null || autor.trim().isEmpty()) {
            return false;
        }

        if (isbn == null || isbn.trim().isEmpty()) {
            return false;
        }

        if (editorial == null || editorial.trim().isEmpty()) {
            return false;
        }

        if (stock < 0) {
            return false;
        }

        if (idCategoria <= 0) {
            return false;
        }

        Libro libro = new Libro(
                id,
                titulo.trim(),
                autor.trim(),
                isbn.trim(),
                editorial.trim(),
                stock,
                idCategoria
        );

        return libroDAO.actualizar(libro);
    }

    // Eliminar libro
    public boolean eliminarLibro(int id) {

        if (id <= 0) {
            return false;
        }

        return libroDAO.eliminar(id);
    }
}