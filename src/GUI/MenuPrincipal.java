package GUI;
import MD.ConexionBD;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuPrincipal extends JFrame {
    private JLabel lblTitulo;
    private JButton btnClientes;
    private JButton btnProductos;
    private JButton btnFacturas;
    
    public MenuPrincipal() {
        configurarVentana();
        cargarComponentes();
        configurarLayout();
        configurarEventos();
    }
    
    public void configurarVentana() {
        setTitle("KoKo Market - Sistema de Gestión");
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Pantalla completa
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }
    
    private void cargarComponentes() {
        lblTitulo = new JLabel("KoKo Market");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 32));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        
        btnClientes = new JButton("Clientes");
        btnClientes.setFont(new Font("Arial", Font.PLAIN, 16));
        btnClientes.setPreferredSize(new Dimension(250, 60));
        
        btnProductos = new JButton("Productos");
        btnProductos.setFont(new Font("Arial", Font.PLAIN, 16));
        btnProductos.setPreferredSize(new Dimension(250, 60));
        
        btnFacturas = new JButton("Facturas");
        btnFacturas.setFont(new Font("Arial", Font.PLAIN, 16));
        btnFacturas.setPreferredSize(new Dimension(250, 60));
    }
    
    private void configurarLayout() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        // Título
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 30, 0); // Espacio abajo del título
        add(lblTitulo, gbc);
        
        // Botón Clientes
        gbc.gridy = 1;
        gbc.insets = new Insets(10, 0, 10, 0); // Espacio entre botones
        add(btnClientes, gbc);
        
        // Botón Productos
        gbc.gridy = 2;
        add(btnProductos, gbc);
        
        // Botón Facturas
        gbc.gridy = 3;
        gbc.insets = new Insets(10, 0, 0, 0);
        add(btnFacturas, gbc);
    }
    
    private void configurarEventos() {
        // Evento para botón Clientes
        btnClientes.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                VentanaCliente();
            }
        });
        
        // Evento para botón Productos
        btnProductos.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                VentanaProducto();
            }
        });
        
        // Evento para botón Facturas
        btnFacturas.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                VentanaFactura();
            }
        });
    }
    
    private void VentanaCliente() {
        VentanaCliente ventanaCliente = new VentanaCliente();
        ventanaCliente.setVisible(true);
        this.dispose();
    }
    
    private void VentanaProducto() {
        VentanaProducto ventanaProducto = new VentanaProducto();
        ventanaProducto.setVisible(true);
        this.dispose();
    }
    
    private void VentanaFactura() {
        VentanaFactura ventanaFactura = new VentanaFactura();
        ventanaFactura.setVisible(true);
        this.dispose();
    }
    
    // MÉTODO MAIN
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                MenuPrincipal menu = new MenuPrincipal();
                menu.setVisible(true);
            }
        });
    }
}
