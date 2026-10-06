package vista;

import javax.swing.*;
import controlador.LoginController;
import modelo.Usuario;

public class LoginFrame extends JFrame {

    private JTextField txtCorreo;
    private JPasswordField txtContraseña;
    private JButton btnIngresar;
    private JPanel panelPrincipal;

    public LoginFrame() {

        setTitle("Biblioteca Escolar");
        setContentPane(panelPrincipal);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        btnIngresar.addActionListener(e -> iniciarSesion());
    }

    private void iniciarSesion() {

        String correo =
                txtCorreo.getText().trim();

        String contraseña =
                new String(txtContraseña.getPassword());

        LoginController controller =
                new LoginController();

        Usuario usuario =
                controller.iniciarSesion(
                        correo,
                        contraseña
                );

        if (usuario != null) {

            MenuPrincipal menu =
                    new MenuPrincipal(usuario);

            menu.setVisible(true);

            dispose();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Correo o contraseña incorrectos.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}