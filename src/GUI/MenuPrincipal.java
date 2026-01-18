package GUI;
import MD.ConexionBD;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuPrincipal extends JFrame {
    private JLabel lblTitulo;
    private JButton btnClientes;
    private JButton btnProveedores;
    private JButton btnProductos;
    private JButton btnFacturas;
    private JButton btnCompras;
    
    public MenuPrincipal() {
        configurarVentana();
        cargarComponentes();
        configurarLayout();
        configurarEventos();
    }
    
    public void configurarVentana() {
        setTitle("KoKo Market - Sistema de Gestión");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
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
        
        btnProveedores = new JButton("Proveedores");
        btnProveedores.setFont(new Font("Arial", Font.PLAIN, 16));
        btnProveedores.setPreferredSize(new Dimension(250, 60));
        
        btnProductos = new JButton("Productos");
        btnProductos.setFont(new Font("Arial", Font.PLAIN, 16));
        btnProductos.setPreferredSize(new Dimension(250, 60));
        
        btnFacturas = new JButton("Facturas");
        btnFacturas.setFont(new Font("Arial", Font.PLAIN, 16));
        btnFacturas.setPreferredSize(new Dimension(250, 60));
        
        btnCompras = new JButton("Compras");
        btnCompras.setFont(new Font("Arial", Font.PLAIN, 16));
        btnCompras.setPreferredSize(new Dimension(250, 60));
    }
    
    private void configurarLayout() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 30, 0);
        add(lblTitulo, gbc);
        
        gbc.gridy = 1;
        gbc.insets = new Insets(10, 0, 10, 0);
        add(btnClientes, gbc);
        
        gbc.gridy = 2;
        add(btnProveedores, gbc);
        
        gbc.gridy = 3;
        add(btnProductos, gbc);
        
        gbc.gridy = 4;
        gbc.insets = new Insets(10, 0, 0, 0);
        add(btnFacturas, gbc);
        
        gbc.gridy = 5;
        gbc.insets = new Insets(10, 0, 0, 0);
        add(btnCompras, gbc);
    }
    
    private void configurarEventos() {
        btnClientes.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                VentanaCliente();
            }
        });
        
        btnProveedores.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                VentanaProveedor();
            }
        });
        
        btnProductos.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                VentanaProducto();
            }
        });
        
        btnFacturas.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                VentanaFactura();
            }
        });
        
        btnCompras.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                VentanaCompra();
            }
        });
    }
    
    private void VentanaCliente() {
        VentanaCliente ventanaCliente = new VentanaCliente();
        ventanaCliente.setVisible(true);
        this.dispose();
    }
    
    private void VentanaProveedor() {
        VentanaProveedor ventanaProveedor = new VentanaProveedor();
        ventanaProveedor.setVisible(true);
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
    
    private void VentanaCompra() {
        VentanaCompra ventanaCompra = new VentanaCompra();
        ventanaCompra.setVisible(true);
        this.dispose();
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                MenuPrincipal menu = new MenuPrincipal();
                menu.setVisible(true);
            }
        });
    }
}
