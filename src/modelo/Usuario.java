package modelo;

public class Usuario extends UsuarioBiblioteca {

    private String rut;
    private String contraseña;
    private String rol;

    public Usuario() {
        super(0, "", "");
    }

    public Usuario(int id, String nombre, String rut,
                   String correo, String contraseña, String rol) {

        super(id, nombre, correo);

        this.rut = rut;
        this.contraseña = contraseña;
        this.rol = rol;
    }

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    @Override
    public String obtenerTipoUsuario() {
        return rol != null ? rol.toUpperCase() : "USUARIO";
    }
}