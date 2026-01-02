package GUI;

import DP.Cliente;
import util.CargadorProperties;
import util.ValidacionesCliente;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.Timer;

public class VentanaCliente extends JFrame {
    
    private CardLayout cardLayout;
    private JPanel panelContenedor;
    
    private JComboBox<String> comboOpciones;
    private JLabel lblTituloSuperior;
    
    private static final String PANEL_VACIO = "VACIO";
    private static final String PANEL_INGRESAR = "INGRESAR";
    private static final String PANEL_MODIFICAR = "MODIFICAR";
    private static final String PANEL_ELIMINAR = "ELIMINAR";
    private static final String PANEL_CONSULTAR = "CONSULTAR";
    
    private JTextField txtCedulaIng, txtNombreIng, txtTelefonoIng, txtEmailIng, txtDireccionIng;
    private JComboBox<ItemCombo> comboCiudadIng;
    private JLabel lblErrorCedulaIng, lblErrorNombreIng, lblErrorTelefonoIng;
    private JLabel lblErrorEmailIng, lblErrorCiudadIng, lblErrorDireccionIng;
    
    private JTextField txtCedulaMod, txtNombreMod, txtTelefonoMod, txtEmailMod, txtDireccionMod;
    private JComboBox<ItemCombo> comboCiudadMod;
    private JLabel lblErrorCedulaMod, lblErrorNombreMod, lblErrorTelefonoMod;
    private JLabel lblErrorEmailMod, lblErrorCiudadMod, lblErrorDireccionMod;
    private JButton btnGuardarMod;
    
    private JTextField txtCedulaElim, txtNombreElim, txtTelefonoElim, txtEmailElim, txtDireccionElim;
    private JComboBox<ItemCombo> comboCiudadElim;
    private JButton btnEliminar;
    
    private JComboBox<String> comboTipoConsulta;
    private JComboBox<String> comboParametroBusqueda;
    private JTextField txtBusqueda;
    private JTable tablaResultados;
    private DefaultTableModel modeloTabla;
    private JScrollPane scrollTabla;
    private JPanel panelBusqueda;
    
    private Timer timerBusqueda;
    
    public VentanaCliente() {
        configurarVentana();
        inicializarComponentes();
        configurarLayout();
    }
    
    private void configurarVentana() {
        setTitle("Gestión de Clientes");
        setExtendedState(JFrame.MAXIMIZED_BOTH); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }
    
    private void inicializarComponentes() {
        cardLayout = new CardLayout();
        panelContenedor = new JPanel(cardLayout);
        
        comboOpciones = new JComboBox<>(new String[]{
            "Seleccione una opción...",
            "Ingresar",
            "Modificar",
            "Eliminar",
            "Consultar"
        });
        
        comboOpciones.addActionListener(e -> cambiarPanel());
        
        panelContenedor.add(crearPanelVacio(), PANEL_VACIO);
        panelContenedor.add(crearPanelIngresar(), PANEL_INGRESAR);
        panelContenedor.add(crearPanelModificar(), PANEL_MODIFICAR);
        panelContenedor.add(crearPanelEliminar(), PANEL_ELIMINAR);
        panelContenedor.add(crearPanelConsultar(), PANEL_CONSULTAR);
    }
    
    private void configurarLayout() {
        setLayout(new BorderLayout(10, 10));

        JPanel panelSuperior = new JPanel(new BorderLayout(15, 0));
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        lblTituloSuperior = new JLabel("Gestión de Clientes");
        lblTituloSuperior.setFont(new Font("Arial", Font.BOLD, 22));
        lblTituloSuperior.setForeground(Color.DARK_GRAY);
        JPanel panelDerecha = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        comboOpciones.setPreferredSize(new Dimension(260, 30));
        panelDerecha.add(comboOpciones);
        panelSuperior.add(lblTituloSuperior, BorderLayout.WEST);
        panelSuperior.add(panelDerecha, BorderLayout.EAST);
        add(panelSuperior, BorderLayout.NORTH);
        
        add(panelContenedor, BorderLayout.CENTER);
        
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        JButton btnVolver = new JButton("Volver");
        btnVolver.addActionListener(e -> volverAlMenu());
        panelInferior.add(btnVolver);
        add(panelInferior, BorderLayout.SOUTH);
    }
    
    private JPanel crearPanelVacio() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        JLabel lblTitulo = new JLabel("Gestión de Clientes");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 48));
        lblTitulo.setForeground(Color.DARK_GRAY);
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(lblTitulo, gbc);
        
        return panel;
    }
    
    private JPanel crearPanelIngresar() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 0, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        //Configuración de campos
        txtCedulaIng = new JTextField();
        txtCedulaIng.setPreferredSize(new Dimension(350, 28));
        txtCedulaIng.setMinimumSize(new Dimension(350, 28));
        txtCedulaIng.setMaximumSize(new Dimension(350, 28));
        
        txtNombreIng = new JTextField();
        txtNombreIng.setPreferredSize(new Dimension(350, 28));
        txtNombreIng.setMinimumSize(new Dimension(350, 28));
        txtNombreIng.setMaximumSize(new Dimension(350, 28));
        
        txtTelefonoIng = new JTextField();
        txtTelefonoIng.setPreferredSize(new Dimension(350, 28));
        txtTelefonoIng.setMinimumSize(new Dimension(350, 28));
        txtTelefonoIng.setMaximumSize(new Dimension(350, 28));
        
        txtEmailIng = new JTextField();
        txtEmailIng.setPreferredSize(new Dimension(350, 28));
        txtEmailIng.setMinimumSize(new Dimension(350, 28));
        txtEmailIng.setMaximumSize(new Dimension(350, 28));
        
        comboCiudadIng = new JComboBox<>();
        comboCiudadIng.setPreferredSize(new Dimension(350, 28));
        comboCiudadIng.setMinimumSize(new Dimension(350, 28));
        comboCiudadIng.setMaximumSize(new Dimension(350, 28));
        
        txtDireccionIng = new JTextField();
        txtDireccionIng.setPreferredSize(new Dimension(350, 28));
        txtDireccionIng.setMinimumSize(new Dimension(350, 28));
        txtDireccionIng.setMaximumSize(new Dimension(350, 28));
        
        //Labels de error 
        lblErrorCedulaIng = crearLabelError();
        lblErrorCedulaIng.setPreferredSize(new Dimension(350, 20));
        
        lblErrorNombreIng = crearLabelError();
        lblErrorNombreIng.setPreferredSize(new Dimension(350, 20));
        
        lblErrorTelefonoIng = crearLabelError();
        lblErrorTelefonoIng.setPreferredSize(new Dimension(350, 20));
        
        lblErrorEmailIng = crearLabelError();
        lblErrorEmailIng.setPreferredSize(new Dimension(350, 20));
        
        lblErrorCiudadIng = crearLabelError();
        lblErrorCiudadIng.setPreferredSize(new Dimension(350, 20));
        
        lblErrorDireccionIng = crearLabelError();
        lblErrorDireccionIng.setPreferredSize(new Dimension(350, 20));
        
        //Cargar ciudades
        cargarCiudades(comboCiudadIng);
        
        configurarValidacionesIngresar();
        
        int fila = 0;
        
        //COLUMNA IZQUIERDA
        
        //Cedula o RUC
        gbc.gridx = 0;
        gbc.gridy = fila;
        panel.add(new JLabel("Cédula o RUC:"), gbc);
        
        gbc.gridx = 1;
        panel.add(txtCedulaIng, gbc);
        
        fila++;
        gbc.gridx = 1;
        gbc.gridy = fila;
        gbc.insets = new Insets(0, 10, 0, 10);
        panel.add(lblErrorCedulaIng, gbc);
        gbc.insets = new Insets(5, 10, 0, 10);
        
        fila++;
        
        //Nombre
        gbc.gridx = 0;
        gbc.gridy = fila;
        panel.add(new JLabel("Nombre:"), gbc);
        
        gbc.gridx = 1;
        panel.add(txtNombreIng, gbc);
        
        fila++;
        gbc.gridx = 1;
        gbc.gridy = fila;
        gbc.insets = new Insets(0, 10, 0, 10);
        panel.add(lblErrorNombreIng, gbc);
        gbc.insets = new Insets(5, 10, 0, 10);
        
        fila++;
        
        //Telefono
        gbc.gridx = 0;
        gbc.gridy = fila;
        panel.add(new JLabel("Teléfono:"), gbc);
        
        gbc.gridx = 1;
        panel.add(txtTelefonoIng, gbc);
        
        fila++;
        gbc.gridx = 1;
        gbc.gridy = fila;
        gbc.insets = new Insets(0, 10, 0, 10);
        panel.add(lblErrorTelefonoIng, gbc);
        gbc.insets = new Insets(5, 10, 0, 10);
        
        fila++;
        
        //Email
        gbc.gridx = 0;
        gbc.gridy = fila;
        panel.add(new JLabel("Email:"), gbc);
        
        gbc.gridx = 1;
        panel.add(txtEmailIng, gbc);
        
        fila++;
        gbc.gridx = 1;
        gbc.gridy = fila;
        gbc.insets = new Insets(0, 10, 0, 10);
        panel.add(lblErrorEmailIng, gbc);
        gbc.insets = new Insets(5, 10, 0, 10);
        
        //COLUMNA DERECHA
        
        fila = 0;
        
        //Ciudad
        gbc.gridx = 2;
        gbc.gridy = fila;
        gbc.insets = new Insets(5, 10, 0, 10); 
        panel.add(new JLabel("Ciudad:"), gbc);
        
        gbc.gridx = 3;
        panel.add(comboCiudadIng, gbc);
        
        fila++;
        gbc.gridx = 3;
        gbc.gridy = fila;
        gbc.insets = new Insets(0, 10, 0, 10);
        panel.add(lblErrorCiudadIng, gbc);
        gbc.insets = new Insets(5, 10, 0, 10);
        
        fila++;
        
        //Direccion
        gbc.gridx = 2;
        gbc.gridy = fila;
        panel.add(new JLabel("Dirección:"), gbc);
        
        gbc.gridx = 3;
        panel.add(txtDireccionIng, gbc);
        
        fila++;
        gbc.gridx = 3;
        gbc.gridy = fila;
        gbc.insets = new Insets(0, 10, 0, 10);
        panel.add(lblErrorDireccionIng, gbc);
        
        //Botón Guardar
        fila++;
        gbc.gridx = 3;
        gbc.gridy = fila;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(0, 10, 0, 10);
        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.setPreferredSize(new Dimension(100, 25));
        btnGuardar.addActionListener(e -> guardarCliente());
        panel.add(btnGuardar, gbc);
        
        return panel;
    }
    
    private JPanel crearPanelModificar() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 0, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        txtCedulaMod = new JTextField();
        txtCedulaMod.setPreferredSize(new Dimension(320, 28));
        txtCedulaMod.setMinimumSize(new Dimension(320, 28));
        txtCedulaMod.setMaximumSize(new Dimension(320, 28));
        
        txtNombreMod = new JTextField();
        txtNombreMod.setPreferredSize(new Dimension(350, 28));
        txtNombreMod.setMinimumSize(new Dimension(350, 28));
        txtNombreMod.setMaximumSize(new Dimension(350, 28));
        
        txtTelefonoMod = new JTextField();
        txtTelefonoMod.setPreferredSize(new Dimension(350, 28));
        txtTelefonoMod.setMinimumSize(new Dimension(350, 28));
        txtTelefonoMod.setMaximumSize(new Dimension(350, 28));
        
        txtEmailMod = new JTextField();
        txtEmailMod.setPreferredSize(new Dimension(350, 28));
        txtEmailMod.setMinimumSize(new Dimension(350, 28));
        txtEmailMod.setMaximumSize(new Dimension(350, 28));
        
        comboCiudadMod = new JComboBox<>();
        comboCiudadMod.setPreferredSize(new Dimension(350, 28));
        comboCiudadMod.setMinimumSize(new Dimension(350, 28));
        comboCiudadMod.setMaximumSize(new Dimension(350, 28));
        
        txtDireccionMod = new JTextField();
        txtDireccionMod.setPreferredSize(new Dimension(350, 28));
        txtDireccionMod.setMinimumSize(new Dimension(350, 28));
        txtDireccionMod.setMaximumSize(new Dimension(350, 28));
        
        //Labels de error 
        lblErrorCedulaMod = crearLabelError();
        lblErrorCedulaMod.setPreferredSize(new Dimension(350, 20));
        
        lblErrorNombreMod = crearLabelError();
        lblErrorNombreMod.setPreferredSize(new Dimension(350, 20));
        
        lblErrorTelefonoMod = crearLabelError();
        lblErrorTelefonoMod.setPreferredSize(new Dimension(350, 20));
        
        lblErrorEmailMod = crearLabelError();
        lblErrorEmailMod.setPreferredSize(new Dimension(350, 20));
        
        lblErrorCiudadMod = crearLabelError();
        lblErrorCiudadMod.setPreferredSize(new Dimension(350, 20));
        
        lblErrorDireccionMod = crearLabelError();
        lblErrorDireccionMod.setPreferredSize(new Dimension(350, 20));
        
        //Cargar ciudades
        cargarCiudades(comboCiudadMod);
        
        //Validaciones automaticas 
        configurarValidacionesModificar();
        
        //Deshabilitar campos inicialmente
        txtNombreMod.setEnabled(false);
        txtTelefonoMod.setEnabled(false);
        txtEmailMod.setEnabled(false);
        comboCiudadMod.setEnabled(false);
        txtDireccionMod.setEnabled(false);
        
        //Configurar búsqueda por cédula
        txtCedulaMod.addActionListener(e -> buscarClienteModificar());
        
        
        int fila = 0;
        
        //Cedula o RUC
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.gridwidth = 1;
        panel.add(new JLabel("Cédula o RUC:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridwidth = 3;
        JPanel panelCedula = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panelCedula.add(txtCedulaMod);
        JLabel lblLupa = new JLabel("🔍");
        lblLupa.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        lblLupa.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lblLupa.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                buscarClienteModificar();
            }
        });
        panelCedula.add(lblLupa);
        panel.add(panelCedula, gbc);
        
        fila++;
        gbc.gridx = 1;
        gbc.gridy = fila;
        gbc.gridwidth = 3;
        gbc.insets = new Insets(0, 10, 0, 10);
        panel.add(lblErrorCedulaMod, gbc);
        gbc.insets = new Insets(5, 10, 0, 10);
        gbc.gridwidth = 1;
        
        //COLUMNA IZQUIERDA
        fila++;
        
        //Nombre
        gbc.gridx = 0;
        gbc.gridy = fila;
        panel.add(new JLabel("Nombre:"), gbc);
        
        gbc.gridx = 1;
        panel.add(txtNombreMod, gbc);
        
        fila++;
        gbc.gridx = 1;
        gbc.gridy = fila;
        gbc.insets = new Insets(0, 10, 0, 10);
        panel.add(lblErrorNombreMod, gbc);
        gbc.insets = new Insets(5, 10, 0, 10);
        
        fila++;
        
        //Telefono
        gbc.gridx = 0;
        gbc.gridy = fila;
        panel.add(new JLabel("Teléfono:"), gbc);
        
        gbc.gridx = 1;
        panel.add(txtTelefonoMod, gbc);
        
        fila++;
        gbc.gridx = 1;
        gbc.gridy = fila;
        gbc.insets = new Insets(0, 10, 0, 10);
        panel.add(lblErrorTelefonoMod, gbc);
        gbc.insets = new Insets(5, 10, 0, 10);
        
        fila++;
        
        //Email
        gbc.gridx = 0;
        gbc.gridy = fila;
        panel.add(new JLabel("Email:"), gbc);
        
        gbc.gridx = 1;
        panel.add(txtEmailMod, gbc);
        
        fila++;
        gbc.gridx = 1;
        gbc.gridy = fila;
        gbc.insets = new Insets(0, 10, 0, 10);
        panel.add(lblErrorEmailMod, gbc);
        gbc.insets = new Insets(5, 10, 0, 10);
        
        //COLUMNA DERECHA
        //Ciudad
        fila = 2; 
        
        gbc.gridx = 2;
        gbc.gridy = fila;
        gbc.insets = new Insets(5, 10, 0, 10);
        panel.add(new JLabel("Ciudad:"), gbc);
        
        gbc.gridx = 3;
        panel.add(comboCiudadMod, gbc);
        
        fila++;
        gbc.gridx = 3;
        gbc.gridy = fila;
        gbc.insets = new Insets(0, 10, 0, 10);
        panel.add(lblErrorCiudadMod, gbc);
        gbc.insets = new Insets(5, 10, 0, 10);
        
        fila++;
        
        //Direccion
        gbc.gridx = 2;
        gbc.gridy = fila;
        panel.add(new JLabel("Dirección:"), gbc);
        
        gbc.gridx = 3;
        panel.add(txtDireccionMod, gbc);
        
        fila++;
        gbc.gridx = 3;
        gbc.gridy = fila;
        gbc.insets = new Insets(0, 10, 0, 10);
        panel.add(lblErrorDireccionMod, gbc);
        
        //Botón Guardar
        fila++;
        gbc.gridx = 3;
        gbc.gridy = fila;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(0, 10, 10, 10);
        btnGuardarMod = new JButton("Guardar");
        btnGuardarMod.setPreferredSize(new Dimension(100, 25));
        btnGuardarMod.setEnabled(false);
        btnGuardarMod.addActionListener(e -> modificarCliente());
        panel.add(btnGuardarMod, gbc);
        
        return panel;
    }
     
    private JPanel crearPanelEliminar() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        txtCedulaElim = new JTextField();
        txtCedulaElim.setPreferredSize(new Dimension(320, 28));
        txtCedulaElim.setMinimumSize(new Dimension(320, 28));
        txtCedulaElim.setMaximumSize(new Dimension(320, 28));
        
        txtNombreElim = new JTextField();
        txtNombreElim.setPreferredSize(new Dimension(350, 28));
        txtNombreElim.setMinimumSize(new Dimension(350, 28));
        txtNombreElim.setMaximumSize(new Dimension(350, 28));
        
        txtTelefonoElim = new JTextField();
        txtTelefonoElim.setPreferredSize(new Dimension(350, 28));
        txtTelefonoElim.setMinimumSize(new Dimension(350, 28));
        txtTelefonoElim.setMaximumSize(new Dimension(350, 28));
        
        txtEmailElim = new JTextField();
        txtEmailElim.setPreferredSize(new Dimension(350, 28));
        txtEmailElim.setMinimumSize(new Dimension(350, 28));
        txtEmailElim.setMaximumSize(new Dimension(350, 28));
        
        comboCiudadElim = new JComboBox<>();
        comboCiudadElim.setPreferredSize(new Dimension(350, 28));
        comboCiudadElim.setMinimumSize(new Dimension(350, 28));
        comboCiudadElim.setMaximumSize(new Dimension(350, 28));
        
        txtDireccionElim = new JTextField();
        txtDireccionElim.setPreferredSize(new Dimension(350, 28));
        txtDireccionElim.setMinimumSize(new Dimension(350, 28));
        txtDireccionElim.setMaximumSize(new Dimension(350, 28));
        
        //Cargar ciudades
        cargarCiudades(comboCiudadElim);
        
        //Deshabilitar todos los campos 
        txtNombreElim.setEnabled(false);
        txtTelefonoElim.setEnabled(false);
        txtEmailElim.setEnabled(false);
        comboCiudadElim.setEnabled(false);
        txtDireccionElim.setEnabled(false);
        
        //Configurar búsqueda por cédula
        txtCedulaElim.addActionListener(e -> buscarClienteEliminar());
        
        int fila = 0;
        
        // Cédula con botón de búsqueda
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.gridwidth = 1;
        panel.add(new JLabel("Cédula o RUC:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridwidth = 3;
        JPanel panelCedula = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panelCedula.add(txtCedulaElim);
        JLabel lblLupa = new JLabel("🔍");
        lblLupa.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        lblLupa.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lblLupa.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                buscarClienteEliminar();
            }
        });
        panelCedula.add(lblLupa);
        panel.add(panelCedula, gbc);
        gbc.gridwidth = 1;
        
        //COLUMNA IZQUIERDA
        fila++;
        
        //Nombre
        gbc.gridx = 0;
        gbc.gridy = fila;
        panel.add(new JLabel("Nombre:"), gbc);
        
        gbc.gridx = 1;
        panel.add(txtNombreElim, gbc);
        
        fila++;
        
        //Telefono
        gbc.gridx = 0;
        gbc.gridy = fila;
        panel.add(new JLabel("Teléfono:"), gbc);
        
        gbc.gridx = 1;
        panel.add(txtTelefonoElim, gbc);
        
        fila++;
        
        //email
        gbc.gridx = 0;
        gbc.gridy = fila;
        panel.add(new JLabel("Email:"), gbc);
        
        gbc.gridx = 1;
        panel.add(txtEmailElim, gbc);
        
        // COLUMNA DERECHA
        fila = 1; 
        
        //Ciudad
        gbc.gridx = 2;
        gbc.gridy = fila;
        gbc.insets = new Insets(5, 10, 0, 10);
        panel.add(new JLabel("Ciudad:"), gbc);
        
        gbc.gridx = 3;
        panel.add(comboCiudadElim, gbc);
        
        fila++;
        
        //Direccion
        gbc.gridx = 2;
        gbc.gridy = fila;
        panel.add(new JLabel("Dirección:"), gbc);
        
        gbc.gridx = 3;
        panel.add(txtDireccionElim, gbc);
        
        //Botón Eliminar
        fila++;
        gbc.gridx = 3;
        gbc.gridy = fila;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(0, 10, 0, 10);
        btnEliminar = new JButton("Eliminar");
        btnEliminar.setPreferredSize(new Dimension(100, 25));
        btnEliminar.setEnabled(false);
        btnEliminar.addActionListener(e -> eliminarCliente());
        panel.add(btnEliminar, gbc);
        
        return panel;
    }
    
    
    private JPanel crearPanelConsultar() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel panelTipo = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        comboTipoConsulta = new JComboBox<>(new String[]{
            "Seleccione tipo de consulta...",
            "Consulta General",
            "Consulta por Parámetro"
        });
        comboTipoConsulta.setPreferredSize(new Dimension(250, 30));
        comboTipoConsulta.addActionListener(e -> cambiarTipoConsulta());
        panelTipo.add(comboTipoConsulta);
        panel.add(panelTipo, BorderLayout.NORTH);
        
        JPanel panelCentral = new JPanel(new BorderLayout(5, 5));
        
        panelBusqueda = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        txtBusqueda = new JTextField(20);
        txtBusqueda.setPreferredSize(new Dimension(250, 25));
        txtBusqueda.setToolTipText("Ingrese texto para buscar...");
        comboParametroBusqueda = new JComboBox<>(new String[]{
            "Nombre",
            "Cédula/RUC",
            "Teléfono",
            "Email",
            "Ciudad",
            "Dirección"
        });
        comboParametroBusqueda.setPreferredSize(new Dimension(150, 25));
        
        panelBusqueda.add(new JLabel("Buscar:"));
        panelBusqueda.add(txtBusqueda);
        panelBusqueda.add(comboParametroBusqueda);
        JLabel lblLupaBusq = new JLabel("🔍");
        panelBusqueda.add(lblLupaBusq);
        panelBusqueda.setVisible(false);
        
        panelCentral.add(panelBusqueda, BorderLayout.NORTH);
        
        String[] columnas = {"Cédula/RUC", "Nombre", "Teléfono", "Email", "Ciudad", "Dirección"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaResultados = new JTable(modeloTabla);
        tablaResultados.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaResultados.setRowHeight(25);
        scrollTabla = new JScrollPane(tablaResultados);
        scrollTabla.setVisible(false);
        
        panelCentral.add(scrollTabla, BorderLayout.CENTER);
        
        panel.add(panelCentral, BorderLayout.CENTER);
        
        configurarBusquedaTiempoReal();
        
        return panel;
    }
    
    
    private JLabel crearLabelError() {
        JLabel lbl = new JLabel(" "); 
        lbl.setForeground(Color.RED);
        lbl.setFont(new Font("Arial", Font.PLAIN, 11));
        lbl.setPreferredSize(new Dimension(350, 20));
        lbl.setMinimumSize(new Dimension(350, 20));
        lbl.setMaximumSize(new Dimension(350, 20));
        
        lbl.setVerticalAlignment(SwingConstants.BOTTOM);
        return lbl;
    }
    
    private void agregarCampo(JPanel panel, GridBagConstraints gbc, String label, 
                              JTextField campo, JLabel lblError, int fila) {
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel(label), gbc);
        
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(campo, gbc);
        
        gbc.gridy = fila + 1;
        panel.add(lblError, gbc);
    }
    
    private void agregarCombo(JPanel panel, GridBagConstraints gbc, String label, 
                             JComboBox<ItemCombo> combo, JLabel lblError, int fila) {
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel(label), gbc);
        
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(combo, gbc);
        
        gbc.gridy = fila + 1;
        panel.add(lblError, gbc);
    }
    
    private void agregarCampoGrande(JPanel panel, GridBagConstraints gbc, String label, 
                                   JTextField campo, JLabel lblError, int fila) {
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel(label), gbc);
        
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridwidth = 2;
        campo.setPreferredSize(new Dimension(300, 60));
        panel.add(campo, gbc);
        
        gbc.gridy = fila + 1;
        gbc.gridwidth = 1;
        panel.add(lblError, gbc);
    }
    
    private void agregarCampoSoloLectura(JPanel panel, GridBagConstraints gbc, String label, 
                                        JTextField campo, int fila) {
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel(label), gbc);
        
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(campo, gbc);
    }
    
    private void agregarComboSoloLectura(JPanel panel, GridBagConstraints gbc, String label, 
                                        JComboBox<ItemCombo> combo, int fila) {
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel(label), gbc);
        
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(combo, gbc);
    }
    
    private void agregarCampoGrandeSoloLectura(JPanel panel, GridBagConstraints gbc, String label, 
                                              JTextField campo, int fila) {
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel(label), gbc);
        
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridwidth = 2;
        campo.setPreferredSize(new Dimension(300, 60));
        panel.add(campo, gbc);
        gbc.gridwidth = 1;
    }
    
    // ==================== VALIDACIONES EN TIEMPO REAL ====================
    
    private void configurarValidacionesIngresar() {
        txtCedulaIng.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { validarCedulaIng(); }
            public void removeUpdate(DocumentEvent e) { validarCedulaIng(); }
            public void changedUpdate(DocumentEvent e) { validarCedulaIng(); }
        });
        
        txtNombreIng.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { validarNombreIng(); }
            public void removeUpdate(DocumentEvent e) { validarNombreIng(); }
            public void changedUpdate(DocumentEvent e) { validarNombreIng(); }
        });
        
        txtTelefonoIng.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { validarTelefonoIng(); }
            public void removeUpdate(DocumentEvent e) { validarTelefonoIng(); }
            public void changedUpdate(DocumentEvent e) { validarTelefonoIng(); }
        });
        
        txtEmailIng.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { validarEmailIng(); }
            public void removeUpdate(DocumentEvent e) { validarEmailIng(); }
            public void changedUpdate(DocumentEvent e) { validarEmailIng(); }
        });
        
        comboCiudadIng.addActionListener(e -> validarCiudadIng());
        
        txtDireccionIng.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { validarDireccionIng(); }
            public void removeUpdate(DocumentEvent e) { validarDireccionIng(); }
            public void changedUpdate(DocumentEvent e) { validarDireccionIng(); }
        });
    }
    
    private void validarCedulaIng() {
        String error = ValidacionesCliente.validarCedula(txtCedulaIng.getText(), false);
        mostrarError(lblErrorCedulaIng, error);
    }
    
    private void validarNombreIng() {
        String error = ValidacionesCliente.validarNombre(txtNombreIng.getText());
        mostrarError(lblErrorNombreIng, error);
    }
    
    private void validarTelefonoIng() {
        String error = ValidacionesCliente.validarTelefono(txtTelefonoIng.getText());
        mostrarError(lblErrorTelefonoIng, error);
    }
    
    private void validarEmailIng() {
        String error = ValidacionesCliente.validarEmail(txtEmailIng.getText());
        mostrarError(lblErrorEmailIng, error);
    }
    
    private void validarCiudadIng() {
        ItemCombo selected = (ItemCombo) comboCiudadIng.getSelectedItem();
        String error = ValidacionesCliente.validarCiudad(selected != null ? selected.getId() : "");
        mostrarError(lblErrorCiudadIng, error);
    }
    
    private void validarDireccionIng() {
        String error = ValidacionesCliente.validarDireccion(txtDireccionIng.getText());
        mostrarError(lblErrorDireccionIng, error);
    }
    
    private void mostrarError(JLabel label, String mensaje) {
        if (mensaje != null) {
            label.setText(mensaje);
        } else {
            label.setText(" "); 
        }
    }
    
    private void configurarValidacionesModificar() {

        txtCedulaMod.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { validarCedulaMod(); }
            public void removeUpdate(DocumentEvent e) { validarCedulaMod(); }
            public void changedUpdate(DocumentEvent e) { validarCedulaMod(); }
        });

        txtNombreMod.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { validarNombreMod(); }
            public void removeUpdate(DocumentEvent e) { validarNombreMod(); }
            public void changedUpdate(DocumentEvent e) { validarNombreMod(); }
        });

        txtTelefonoMod.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { validarTelefonoMod(); }
            public void removeUpdate(DocumentEvent e) { validarTelefonoMod(); }
            public void changedUpdate(DocumentEvent e) { validarTelefonoMod(); }
        });

        txtEmailMod.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { validarEmailMod(); }
            public void removeUpdate(DocumentEvent e) { validarEmailMod(); }
            public void changedUpdate(DocumentEvent e) { validarEmailMod(); }
        });

        comboCiudadMod.addActionListener(e -> validarCiudadMod());

        txtDireccionMod.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { validarDireccionMod(); }
            public void removeUpdate(DocumentEvent e) { validarDireccionMod(); }
            public void changedUpdate(DocumentEvent e) { validarDireccionMod(); }
        });
    }
    
    private void validarCedulaMod() {
        String error = ValidacionesCliente.validarCedula(txtCedulaMod.getText(), true);
        mostrarError(lblErrorCedulaMod, error);
    }

    private void validarNombreMod() {
        if (txtCedulaMod.getText().trim().isEmpty()) {
            lblErrorNombreMod.setText(" ");
            return;
        }
        String error = ValidacionesCliente.validarNombre(txtNombreMod.getText());
        mostrarError(lblErrorNombreMod, error);
    }

    private void validarTelefonoMod() {
        if (txtCedulaMod.getText().trim().isEmpty()) {
            lblErrorTelefonoMod.setText(" ");
            return;
        }
        String error = ValidacionesCliente.validarTelefono(txtTelefonoMod.getText());
        mostrarError(lblErrorTelefonoMod, error);
    }

    private void validarEmailMod() {
        if (txtCedulaMod.getText().trim().isEmpty()) {
            lblErrorEmailMod.setText(" ");
            return;
        }
        String error = ValidacionesCliente.validarEmail(txtEmailMod.getText());
        mostrarError(lblErrorEmailMod, error);
    }

    private void validarCiudadMod() {
        if (txtCedulaMod.getText().trim().isEmpty()) {
            lblErrorCiudadMod.setText(" ");
            return;
        }
        ItemCombo selected = (ItemCombo) comboCiudadMod.getSelectedItem();
        String error = ValidacionesCliente.validarCiudad(selected != null ? selected.getId() : "");
        mostrarError(lblErrorCiudadMod, error);
    }

    private void validarDireccionMod() {
        if (txtCedulaMod.getText().trim().isEmpty()) {
            lblErrorDireccionMod.setText(" ");
            return;
        }
    String error = ValidacionesCliente.validarDireccion(txtDireccionMod.getText());
    mostrarError(lblErrorDireccionMod, error);
}

    
    private void cargarCiudades(JComboBox<ItemCombo> combo) {
        combo.removeAllItems();
        combo.addItem(new ItemCombo("", "Seleccione una ciudad..."));
        
        Cliente cli = new Cliente();
        ArrayList<ItemCombo> ciudades = cli.obtenerCiudadesDP();
        
        for (ItemCombo ciudad : ciudades) {
            combo.addItem(ciudad);
        }
    }
    
    private void cambiarPanel() {
        String opcion = (String) comboOpciones.getSelectedItem();

        actualizarTituloSuperior(opcion); 

        switch (opcion) {
            case "Ingresar":
                limpiarCamposIngresar();
                cardLayout.show(panelContenedor, PANEL_INGRESAR);
                break;
            case "Modificar":
                limpiarCamposModificar();
                cardLayout.show(panelContenedor, PANEL_MODIFICAR);
                break;
            case "Eliminar":
                limpiarCamposEliminar();
                cardLayout.show(panelContenedor, PANEL_ELIMINAR);
                break;
            case "Consultar":
                cardLayout.show(panelContenedor, PANEL_CONSULTAR);
                break;
            default:
                cardLayout.show(panelContenedor, PANEL_VACIO);
                break;
        }
    }
    
    private void actualizarTituloSuperior(String opcion) {
        String titulo;

        switch (opcion) {
            case "Ingresar":
                titulo = "Ingresar Cliente";
                break;
            case "Modificar":
                titulo = "Modificar Cliente";
                break;
            case "Eliminar":
                titulo = "Eliminar Cliente";
                break;
            case "Consultar":
                titulo = "Consultar Clientes";
                break;
            default:
                titulo = "Gestión de Clientes";
                break;
        }

        lblTituloSuperior.setText(titulo);
    }
        
    //Vuelve al panel vacío
    private void volverAlMenu() {
        this.dispose();             
        new MenuPrincipal().setVisible(true);
    }
    
    private void guardarCliente() {
        ItemCombo ciudadSel = (ItemCombo) comboCiudadIng.getSelectedItem();
        String idCiudad = ciudadSel != null ? ciudadSel.getId() : "";
        
        boolean valido = ValidacionesCliente.validarTodo(
            txtCedulaIng.getText(),
            txtNombreIng.getText(),
            txtTelefonoIng.getText(),
            txtEmailIng.getText(),
            idCiudad,
            txtDireccionIng.getText(),
            false
        );
        
        if (!valido) {
            JOptionPane.showMessageDialog(this,
                "Por favor corrija los errores antes de guardar",
                "Validación",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Cliente cli = new Cliente();
        cli.setCedRuc(txtCedulaIng.getText().trim());
        cli.setNombre(txtNombreIng.getText().trim());
        cli.setTelefono(txtTelefonoIng.getText().trim());
        cli.setEmail(txtEmailIng.getText().trim());
        cli.setIdCiudad(idCiudad);
        cli.setDireccion(txtDireccionIng.getText().trim());
        
        if (cli.grabarDP()) {
            JOptionPane.showMessageDialog(this,
                CargadorProperties.obtenerMessages("CL_I_001"),
                "Éxito",
                JOptionPane.INFORMATION_MESSAGE);
            limpiarCamposIngresar();
        } else {
            JOptionPane.showMessageDialog(this,
                CargadorProperties.obtenerMessages("CL_E_002"),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void buscarClienteModificar() {
        String cedula = txtCedulaMod.getText().trim();
        
        if (cedula.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Ingrese una cédula para buscar",
                "Advertencia",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Cliente cli = new Cliente();
        Cliente encontrado = cli.verificarDP(cedula);
        
        if (encontrado != null) {
            txtNombreMod.setText(encontrado.getNombre());
            txtTelefonoMod.setText(encontrado.getTelefono());
            txtEmailMod.setText(encontrado.getEmail());
            txtDireccionMod.setText(encontrado.getDireccion());
            
            for (int i = 0; i < comboCiudadMod.getItemCount(); i++) {
                ItemCombo item = comboCiudadMod.getItemAt(i);
                if (item.getId().equals(encontrado.getIdCiudad())) {
                    comboCiudadMod.setSelectedIndex(i);
                    break;
                }
            }
            
            txtNombreMod.setEnabled(true);
            txtTelefonoMod.setEnabled(true);
            txtEmailMod.setEnabled(true);
            comboCiudadMod.setEnabled(true);
            txtDireccionMod.setEnabled(true);
            
            txtCedulaMod.setEnabled(false);
            
            btnGuardarMod.setEnabled(true); 
            
            lblErrorCedulaMod.setText(" "); 
        } else {
            JOptionPane.showMessageDialog(this,
                "Cliente no encontrado",
                "Búsqueda",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void modificarCliente() {
        ItemCombo ciudadSel = (ItemCombo) comboCiudadMod.getSelectedItem();
        String idCiudad = ciudadSel != null ? ciudadSel.getId() : "";
        
        boolean valido = ValidacionesCliente.validarTodo(
            txtCedulaMod.getText(),
            txtNombreMod.getText(),
            txtTelefonoMod.getText(),
            txtEmailMod.getText(),
            idCiudad,
            txtDireccionMod.getText(),
            true
        );
        
        if (!valido) {
            JOptionPane.showMessageDialog(this,
                "Por favor corrija los errores antes de guardar",
                "Validación",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Cliente cli = new Cliente();
        cli.setCedRuc(txtCedulaMod.getText().trim());
        cli.setNombre(txtNombreMod.getText().trim());
        cli.setTelefono(txtTelefonoMod.getText().trim());
        cli.setEmail(txtEmailMod.getText().trim());
        cli.setIdCiudad(idCiudad);
        cli.setDireccion(txtDireccionMod.getText().trim());
        
        if (cli.grabarDP()) {
            JOptionPane.showMessageDialog(this,
                CargadorProperties.obtenerMessages("CL_I_002"),
                "Éxito",
                JOptionPane.INFORMATION_MESSAGE);
            limpiarCamposModificar();
        } else {
            JOptionPane.showMessageDialog(this,
                CargadorProperties.obtenerMessages("CL_E_004"),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void buscarClienteEliminar() {
        String cedula = txtCedulaElim.getText().trim();
        
        if (cedula.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Ingrese una cédula para buscar",
                "Advertencia",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Cliente cli = new Cliente();
        Cliente encontrado = cli.verificarDP(cedula);
        
        if (encontrado != null) {
            txtNombreElim.setText(encontrado.getNombre());
            txtTelefonoElim.setText(encontrado.getTelefono());
            txtEmailElim.setText(encontrado.getEmail());
            txtDireccionElim.setText(encontrado.getDireccion());
            
            for (int i = 0; i < comboCiudadElim.getItemCount(); i++) {
                ItemCombo item = comboCiudadElim.getItemAt(i);
                if (item.getDescripcion().equals(encontrado.getCiudad())) {
                    comboCiudadElim.setSelectedIndex(i);
                    break;
                }
            }
            
            btnEliminar.setEnabled(true);
        } else {
            JOptionPane.showMessageDialog(this,
                "Cliente no encontrado",
                "Búsqueda",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void eliminarCliente() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "¿Está seguro de eliminar al cliente " + txtNombreElim.getText() + "?",
            "Confirmar eliminación",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            Cliente cli = new Cliente();
            cli.setCedRuc(txtCedulaElim.getText().trim());
            
            if (cli.eliminarDP()) {
                JOptionPane.showMessageDialog(this,
                    CargadorProperties.obtenerMessages("CL_I_003"),
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
                limpiarCamposEliminar();
            } else {
                JOptionPane.showMessageDialog(this,
                    CargadorProperties.obtenerMessages("CL_E_005"),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void cambiarTipoConsulta() {
        String tipo = (String) comboTipoConsulta.getSelectedItem();
        
        if ("Consulta General".equals(tipo)) {
            panelBusqueda.setVisible(false);
            scrollTabla.setVisible(true);
            consultarGeneral();
        } else if ("Consulta por Parámetro".equals(tipo)) {
            panelBusqueda.setVisible(true);
            scrollTabla.setVisible(true);
            modeloTabla.setRowCount(0);
        } else {
            panelBusqueda.setVisible(false);
            scrollTabla.setVisible(false);
        }
    }
    
    private void consultarGeneral() {
        Cliente cli = new Cliente();
        ArrayList<Cliente> clientes = cli.consultarTodos();
        
        modeloTabla.setRowCount(0);
        
        for (Cliente c : clientes) {
            modeloTabla.addRow(new Object[]{
                c.getCedRuc(),
                c.getNombre(),
                c.getTelefono(),
                c.getEmail(),
                c.getCiudad(),
                c.getDireccion()
            });
        }
    }
    
    private void configurarBusquedaTiempoReal() {
        timerBusqueda = new Timer(300, e -> realizarBusqueda());
        timerBusqueda.setRepeats(false);
        
        txtBusqueda.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { reiniciarTimer(); }
            public void removeUpdate(DocumentEvent e) { reiniciarTimer(); }
            public void changedUpdate(DocumentEvent e) { reiniciarTimer(); }
        });
        
        comboParametroBusqueda.addActionListener(e -> realizarBusqueda());
    }
    
    private void reiniciarTimer() {
        timerBusqueda.restart();
    }
    
    private void realizarBusqueda() {
        String texto = txtBusqueda.getText().trim();
        
        if (texto.isEmpty()) {
            modeloTabla.setRowCount(0);
            return;
        }
        
        String parametro = (String) comboParametroBusqueda.getSelectedItem();
        Cliente cli = new Cliente();
        ArrayList<Cliente> resultados = new ArrayList<>();
        
        switch (parametro) {
            case "Nombre":
                resultados = cli.buscarPorNombreDP(texto);
                break;
            case "Cédula/RUC":
                resultados = cli.buscarPorCedulaDP(texto);
                break;
            case "Teléfono":
                resultados = cli.buscarPorTelefonoDP(texto);
                break;
            case "Email":
                resultados = cli.buscarPorEmailDP(texto);
                break;
            case "Ciudad":
                resultados = cli.buscarPorCiudadDP(texto);
                break;
            case "Dirección":
                resultados = cli.buscarPorDireccionDP(texto);
                break;
        }
        
        modeloTabla.setRowCount(0);
        
        for (Cliente c : resultados) {
            modeloTabla.addRow(new Object[]{
                c.getCedRuc(),
                c.getNombre(),
                c.getTelefono(),
                c.getEmail(),
                c.getCiudad(),
                c.getDireccion()
            });
        }
    }
   
    
    private void limpiarCamposIngresar() {
        txtCedulaIng.setText("");
        txtNombreIng.setText("");
        txtTelefonoIng.setText("");
        txtEmailIng.setText("");
        comboCiudadIng.setSelectedIndex(0);
        txtDireccionIng.setText("");
        
        lblErrorCedulaIng.setText(" ");
        lblErrorNombreIng.setText(" ");
        lblErrorTelefonoIng.setText(" ");
        lblErrorEmailIng.setText(" ");
        lblErrorCiudadIng.setText(" ");
        lblErrorDireccionIng.setText(" ");
    }
    
    private void limpiarCamposModificar() {
        txtCedulaMod.setText("");
        txtNombreMod.setText("");
        txtTelefonoMod.setText("");
        txtEmailMod.setText("");
        comboCiudadMod.setSelectedIndex(0);
        txtDireccionMod.setText("");
        
        txtCedulaMod.setEnabled(true);
        
        txtNombreMod.setEnabled(false);
        txtTelefonoMod.setEnabled(false);
        txtEmailMod.setEnabled(false);
        comboCiudadMod.setEnabled(false);
        txtDireccionMod.setEnabled(false);
        
        btnGuardarMod.setEnabled(false);
        lblErrorCedulaMod.setText(" ");
    }
    
    private void limpiarCamposEliminar() {
        txtCedulaElim.setText("");
        txtNombreElim.setText("");
        txtTelefonoElim.setText("");
        txtEmailElim.setText("");
        comboCiudadElim.setSelectedIndex(0);
        txtDireccionElim.setText("");
        
        btnEliminar.setEnabled(false); 
    }
    
    
    
    
    /*
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            VentanaCliente ventana = new VentanaCliente();
            ventana.setVisible(true);
        });
    }
    */
}
