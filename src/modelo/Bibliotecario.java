package modelo;

public class Bibliotecario extends UsuarioBiblioteca
        implements GestionPrestamos {

    public Bibliotecario(int id, String nombre, String correo) {
        super(id, nombre, correo);
    }

    @Override
    public String obtenerTipoUsuario() {
        return "BIBLIOTECARIO";
    }

    @Override
    public void realizarPrestamo() {
        System.out.println(
                "El bibliotecario puede registrar préstamos."
        );
    }

    @Override
    public void realizarDevolucion() {
        System.out.println(
                "El bibliotecario puede registrar devoluciones."
        );
    }
}