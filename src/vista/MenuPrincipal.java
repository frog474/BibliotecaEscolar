package vista;

import javax.swing.*;
import modelo.Usuario;

public class MenuPrincipal extends JFrame {

    private JPanel panelPrincipal;
    private JLabel lblUsuario;
    private JButton btnLibros;
    private JButton btnEstudiantes;
    private JButton btnPrestamos;
    private JButton btnReportes;
    private JButton btnCategorias;
    private JButton btnCerrarSesion;

    public MenuPrincipal(Usuario usuario) {

        setTitle("Biblioteca Escolar - Menú Principal");
        setContentPane(panelPrincipal);
        setSize(600, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        lblUsuario.setText(
                "Usuario: " + usuario.getNombre()
                        + " | Rol: " + usuario.getRol()
        );

        configurarPermisos(usuario);

        btnLibros.addActionListener(e -> {

            LibrosFrame ventanaLibros =
                    new LibrosFrame();

            ventanaLibros.setVisible(true);
        });

        btnEstudiantes.addActionListener(e -> {

            EstudiantesFrame ventanaEstudiantes =
                    new EstudiantesFrame();

            ventanaEstudiantes.setVisible(true);
        });

        btnCategorias.addActionListener(e -> {

            CategoriasFrame ventanaCategorias =
                    new CategoriasFrame();

            ventanaCategorias.setVisible(true);
        });

        btnPrestamos.addActionListener(e -> {

            PrestamosFrame ventanaPrestamos =
                    new PrestamosFrame();

            ventanaPrestamos.setVisible(true);
        });

        btnReportes.addActionListener(e -> {

            ReportesFrame ventanaReportes =
                    new ReportesFrame();

            ventanaReportes.setVisible(true);
        });

        btnCerrarSesion.addActionListener(
                e -> cerrarSesion()
        );
    }

    private void configurarPermisos(Usuario usuario) {

        boolean esBibliotecario =
                usuario.getRol()
                        .equalsIgnoreCase("bibliotecario");

        boolean esEstudiante =
                usuario.getRol()
                        .equalsIgnoreCase("estudiante");

        btnLibros.setEnabled(esBibliotecario);

        btnEstudiantes.setEnabled(esBibliotecario);

        btnCategorias.setEnabled(esBibliotecario);

        btnPrestamos.setEnabled(
                esBibliotecario || esEstudiante
        );

        btnReportes.setEnabled(
                esBibliotecario || esEstudiante
        );
    }

    private void cerrarSesion() {

        int confirmacion =
                JOptionPane.showConfirmDialog(
                        this,
                        "¿Está seguro de que desea cerrar sesión?",
                        "Cerrar sesión",
                        JOptionPane.YES_NO_OPTION
                );

        if (confirmacion ==
                JOptionPane.YES_OPTION) {

            LoginFrame login =
                    new LoginFrame();

            login.setVisible(true);

            dispose();
        }
    }
}