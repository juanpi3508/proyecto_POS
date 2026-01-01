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
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }
    
    private void cargarComponentes() {
        lblTitulo = new JLabel("KoKo Market");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setHorizontalAlignment(0);
        
        btnClientes = new JButton("Clientes");
        btnClientes.setFont(new Font("Arial", Font.PLAIN, 14));
        
        btnProductos = new JButton("Productos");
        btnProductos.setFont(new Font("Arial", Font.PLAIN, 14));
        
        btnFacturas = new JButton("Facturas");
        btnFacturas.setFont(new Font("Arial", Font.PLAIN, 14));
    }
    
    private void configurarLayout() {
        setLayout(null);
        
        lblTitulo.setBounds(50, 30, 300, 30);
        add(lblTitulo);
        
        btnClientes.setBounds(50, 90, 300, 40);
        add(btnClientes);
        
        btnProductos.setBounds(50, 150, 300, 40);
        add(btnProductos);
        
        btnFacturas.setBounds(50, 210, 300, 40);
        add(btnFacturas);
    }
    
    private void configurarEventos() {       
        btnFacturas.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                VentanaFactura();
            }
        });
    }
    
    private void VentanaFactura() {
        VentanaFactura ventanaFactura = new VentanaFactura();
        ventanaFactura.setVisible(true);
        this.dispose();
    }
    
    // MÉTODO MAIN - AGREGA ESTO
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                MenuPrincipal menu = new MenuPrincipal();
                menu.setVisible(true);
            }
        });
    }
}