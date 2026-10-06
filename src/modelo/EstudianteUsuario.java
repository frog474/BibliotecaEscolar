package modelo;

public class EstudianteUsuario extends UsuarioBiblioteca
        implements GestionPrestamos {

    public EstudianteUsuario(int id, String nombre, String correo) {
        super(id, nombre, correo);
    }

    @Override
    public String obtenerTipoUsuario() {
        return "ESTUDIANTE";
    }

    @Override
    public void realizarPrestamo() {
        System.out.println(
                "El estudiante puede solicitar préstamos."
        );
    }

    @Override
    public void realizarDevolucion() {
        System.out.println(
                "El estudiante puede realizar devoluciones."
        );
    }
}