package controlador;

import dao.UsuarioDAO;
import modelo.Bibliotecario;
import modelo.EstudianteUsuario;
import modelo.Usuario;
import modelo.UsuarioBiblioteca;

public class LoginController {

    private final UsuarioDAO usuarioDAO;

    public LoginController() {
        usuarioDAO = new UsuarioDAO();
    }

    public Usuario iniciarSesion(String correo, String contraseña) {

        if (correo == null || correo.trim().isEmpty()) {
            return null;
        }

        if (contraseña == null || contraseña.trim().isEmpty()) {
            return null;
        }

        Usuario usuario =
                usuarioDAO.login(correo.trim(), contraseña);

        if (usuario == null) {
            return null;
        }

        UsuarioBiblioteca usuarioEspecializado;

        if (usuario.getRol().equalsIgnoreCase("bibliotecario")) {

            usuarioEspecializado =
                    new Bibliotecario(
                            usuario.getId(),
                            usuario.getNombre(),
                            usuario.getCorreo()
                    );

        } else {

            usuarioEspecializado =
                    new EstudianteUsuario(
                            usuario.getId(),
                            usuario.getNombre(),
                            usuario.getCorreo()
                    );
        }

        System.out.println(
                "Tipo de usuario: "
                        + usuarioEspecializado.obtenerTipoUsuario()
        );

        return usuario;
    }
}