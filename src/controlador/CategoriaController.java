package controlador;

import dao.CategoriaDAO;
import modelo.Categoria;

import java.util.List;

public class CategoriaController {

    private final CategoriaDAO categoriaDAO;

    public CategoriaController() {
        categoriaDAO = new CategoriaDAO();
    }

    public List<Categoria> listarCategorias() {
        return categoriaDAO.listarTodos();
    }

    public boolean registrarCategoria(String nombre) {

        if (nombre == null || nombre.trim().isEmpty()) {
            return false;
        }

        Categoria categoria = new Categoria(
                0,
                nombre.trim()
        );

        return categoriaDAO.guardar(categoria);
    }

    public boolean actualizarCategoria(int id, String nombre) {

        if (id <= 0 || nombre == null || nombre.trim().isEmpty()) {
            return false;
        }

        Categoria categoria = new Categoria(
                id,
                nombre.trim()
        );

        return categoriaDAO.actualizar(categoria);
    }

    public boolean eliminarCategoria(int id) {

        if (id <= 0) {
            return false;
        }

        return categoriaDAO.eliminar(id);
    }
}