package GUI;

import util.CargadorProperties;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MenuPrincipal extends JFrame {

    private static final Color COLOR_PRIMARIO = new Color(255, 173, 51);
    private static final Color COLOR_SECUNDARIO = new Color(102, 151, 74);
    private static final Color COLOR_ACENTO = new Color(204, 20, 0);
    private static final Color COLOR_FONDO = new Color(255, 247, 227);
    private static final Color COLOR_TEXTO = new Color(76, 87, 169);
    private static final Color COLOR_BLANCO = Color.WHITE;

    private static final Font FUENTE_TITULO = new Font("Poppins", Font.BOLD, 34);
    private static final Font FUENTE_BOTON = new Font("Poppins", Font.BOLD, 16);

    public MenuPrincipal() {
        configurarVentana();
        construirUI();
    }

    private void configurarVentana() {
        setTitle("KoKo Market | Menú Principal");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        try {
            setIconImage(new ImageIcon(
                    getClass().getResource("/resources/img/logo-removebg.png")
            ).getImage());
        } catch (Exception ignored) {}

        getContentPane().setBackground(COLOR_FONDO);
        setLayout(new BorderLayout());
    }

    private void construirUI() {

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(COLOR_PRIMARIO);
        header.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel lblTitulo = new JLabel("Menú Principal", SwingConstants.CENTER);
        lblTitulo.setFont(FUENTE_TITULO);
        lblTitulo.setForeground(COLOR_TEXTO);

        header.add(lblTitulo, BorderLayout.CENTER);
        add(header, BorderLayout.NORTH);

        JPanel contenido = new JPanel(new GridLayout(2, 3, 30, 30));
        contenido.setBorder(new EmptyBorder(40, 60, 40, 60));
        contenido.setBackground(COLOR_FONDO);

        contenido.add(crearBotonModulo("Clientes", "/resources/img/clientes.png", COLOR_SECUNDARIO,
                () -> abrirVentana(new VentanaCliente())));

        contenido.add(crearBotonModulo("Proveedores", "/resources/img/proveedores.png", COLOR_SECUNDARIO,
                () -> abrirVentana(new VentanaProveedor())));

        contenido.add(crearBotonModulo("Productos", "/resources/img/productos.png", COLOR_SECUNDARIO,
                () -> abrirVentana(new VentanaProducto())));

        contenido.add(crearBotonModulo("Compras", "/resources/img/compras.png", COLOR_TEXTO,
                () -> abrirVentana(new VentanaCompra())));

        contenido.add(crearBotonModulo("Facturación", "/resources/img/facturas.png", COLOR_TEXTO,
                () -> abrirVentana(new VentanaFactura())));

        contenido.add(crearBotonModulo("Salir", "/resources/img/salir.png", COLOR_ACENTO, this::salirSistema));

        add(contenido, BorderLayout.CENTER);

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        footer.setBackground(COLOR_PRIMARIO);

        JLabel lblFooter = new JLabel("© KoKo Market – Sistema de Gestión Comercial");
        lblFooter.setFont(new Font("Poppins", Font.PLAIN, 12));
        lblFooter.setForeground(COLOR_TEXTO);

        footer.add(lblFooter);
        add(footer, BorderLayout.SOUTH);
    }

    private JPanel crearBotonModulo(String texto, String iconoPath, Color color, Runnable accion) {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(color);
        panel.setBorder(new LineBorder(color.darker(), 1, true));
        panel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panel.setPreferredSize(new Dimension(240, 140));

        JLabel lblIcono = new JLabel();
        lblIcono.setAlignmentX(Component.CENTER_ALIGNMENT);

        try {
            ImageIcon icon = new ImageIcon(getClass().getResource(iconoPath));
            Image img = icon.getImage().getScaledInstance(48, 48, Image.SCALE_SMOOTH);
            lblIcono.setIcon(new ImageIcon(img));
        } catch (Exception ignored) {}

        JLabel lblTexto = new JLabel(texto);
        lblTexto.setFont(FUENTE_BOTON);
        lblTexto.setForeground(COLOR_BLANCO);
        lblTexto.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel centro = new JPanel();
        centro.setOpaque(false);
        centro.setLayout(new BoxLayout(centro, BoxLayout.Y_AXIS));

        centro.add(Box.createVerticalGlue());
        centro.add(lblIcono);
        centro.add(Box.createRigidArea(new Dimension(0, 8)));
        centro.add(lblTexto);
        centro.add(Box.createVerticalGlue());

        panel.add(centro, BorderLayout.CENTER);

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                accion.run();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                panel.setBackground(color.brighter());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                panel.setBackground(color);
            }
        });

        return panel;
    }

    private void abrirVentana(JFrame ventana) {
        dispose();
        ventana.setVisible(true);
    }

    private void salirSistema() {
        int opcion = JOptionPane.showConfirmDialog(
                this,
                CargadorProperties.obtenerMessages("MP_C_001"),
                "Confirmación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (opcion == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MenuPrincipal().setVisible(true));
    }
}
