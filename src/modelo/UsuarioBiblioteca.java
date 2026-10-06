package modelo;

public abstract class UsuarioBiblioteca {

    private int id;
    private String nombre;
    private String correo;

    public UsuarioBiblioteca(int id, String nombre, String correo) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public abstract String obtenerTipoUsuario();

    public void mostrarInformacion() {
        System.out.println(
                "Usuario: " + nombre
                        + " | Correo: " + correo
                        + " | Tipo: " + obtenerTipoUsuario()
        );
    }
}