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
    
    private static final int OPCION_SELECCIONE = 0;
    private static final int OPCION_INGRESAR = 1;
    private static final int OPCION_MODIFICAR = 2;
    private static final int OPCION_ELIMINAR = 3;
    private static final int OPCION_CONSULTAR = 4;
    
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
    private boolean validandoCedulaIna = false;
    
    public VentanaCliente() {
        configurarVentana();
        inicializarComponentes();
        configurarLayout();
    }
    
    private void configurarVentana() {
        setTitle(CargadorProperties.obtenerComponentes("ventana.clientes.titulo"));
        setExtendedState(JFrame.MAXIMIZED_BOTH); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }
    
    private void inicializarComponentes() {
        cardLayout = new CardLayout();
        panelContenedor = new JPanel(cardLayout);
        
        comboOpciones = new JComboBox<>(new String[]{
            CargadorProperties.obtenerComponentes("combo.opciones.seleccione"),
            CargadorProperties.obtenerComponentes("combo.opciones.ingresar"),
            CargadorProperties.obtenerComponentes("combo.opciones.modificar"),
            CargadorProperties.obtenerComponentes("combo.opciones.eliminar"),
            CargadorProperties.obtenerComponentes("combo.opciones.consultar")
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

        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JPanel panelFilaCombo = new JPanel(new BorderLayout());
        JPanel panelDerecha = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        comboOpciones.setPreferredSize(new Dimension(260, 30));
        panelDerecha.add(comboOpciones);
        panelFilaCombo.add(panelDerecha, BorderLayout.EAST);

        JPanel panelFilaTitulo = new JPanel(new BorderLayout());
        lblTituloSuperior = new JLabel("", SwingConstants.CENTER);
        lblTituloSuperior.setFont(new Font("Arial", Font.BOLD, 42));
        lblTituloSuperior.setForeground(Color.DARK_GRAY);
        panelFilaTitulo.add(lblTituloSuperior, BorderLayout.CENTER);

        panelSuperior.add(panelFilaCombo, BorderLayout.NORTH);
        panelSuperior.add(panelFilaTitulo, BorderLayout.CENTER);

        add(panelSuperior, BorderLayout.NORTH);
        add(panelContenedor, BorderLayout.CENTER);

        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        JButton btnVolver = new JButton(CargadorProperties.obtenerComponentes("boton.volver"));
        btnVolver.addActionListener(e -> volverAlMenu());
        panelInferior.add(btnVolver);
        add(panelInferior, BorderLayout.SOUTH);
    }
    
    private JPanel crearPanelVacio() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        JLabel lblTitulo = new JLabel(CargadorProperties.obtenerComponentes("ventana.clientes.titulo.vacio"));
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
        
        //Cargar ciudades y aplicar validaciones
        cargarCiudades(comboCiudadIng);
        configurarValidacionesIngresar();
        
        int fila = 0;
        
        //COLUMNA IZQUIERDA
        
        //Cedula o RUC
        gbc.gridx = 0;
        gbc.gridy = fila;
        panel.add(new JLabel(CargadorProperties.obtenerComponentes("label.cedula.ruc")), gbc);
        
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
        panel.add(new JLabel(CargadorProperties.obtenerComponentes("label.nombre")), gbc);
        
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
        panel.add(new JLabel(CargadorProperties.obtenerComponentes("label.telefono")), gbc);
        
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
        panel.add(new JLabel(CargadorProperties.obtenerComponentes("label.email")), gbc);
        
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
        panel.add(new JLabel(CargadorProperties.obtenerComponentes("label.ciudad")), gbc);
        
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
        panel.add(new JLabel(CargadorProperties.obtenerComponentes("label.direccion")), gbc);
        
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
        JButton btnGuardar = new JButton(CargadorProperties.obtenerComponentes("boton.guardar"));
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
        
        //Coniguracion de campos
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
        
        //Cargar ciudades y aplicar validaciones 
        cargarCiudades(comboCiudadMod);
        configurarValidacionesModificar();
        
        //Deshabilitar campos inicialmente
        txtNombreMod.setEnabled(false);
        txtTelefonoMod.setEnabled(false);
        txtEmailMod.setEnabled(false);
        comboCiudadMod.setEnabled(false);
        txtDireccionMod.setEnabled(false);
        
        //Configurar búsqueda por cédula
        txtCedulaMod.addActionListener(e -> buscarClienteModificar());
        
        //COLUMNA IZQUIERDA
        int fila = 0;
        
        //Cedula o RUC
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.gridwidth = 1;
        panel.add(new JLabel(CargadorProperties.obtenerComponentes("label.cedula.ruc")), gbc);
        
        gbc.gridx = 1;
        gbc.gridwidth = 3;
        JPanel panelCedula = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panelCedula.setPreferredSize(new Dimension(380, 30));
        panelCedula.add(txtCedulaMod);
        JLabel lblLupa = new JLabel(CargadorProperties.obtenerComponentes("emoji.lupa"));
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
        
        fila++;
        
        //Nombre
        gbc.gridx = 0;
        gbc.gridy = fila;
        panel.add(new JLabel(CargadorProperties.obtenerComponentes("label.nombre")), gbc);
        
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
        panel.add(new JLabel(CargadorProperties.obtenerComponentes("label.telefono")), gbc);
        
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
        panel.add(new JLabel(CargadorProperties.obtenerComponentes("label.email")), gbc);
        
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
        panel.add(new JLabel(CargadorProperties.obtenerComponentes("label.ciudad")), gbc);
        
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
        panel.add(new JLabel(CargadorProperties.obtenerComponentes("label.direccion")), gbc);
        
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
        btnGuardarMod = new JButton(CargadorProperties.obtenerComponentes("boton.guardar"));
        btnGuardarMod.setPreferredSize(new Dimension(100, 25));
        btnGuardarMod.setEnabled(false);
        btnGuardarMod.addActionListener(e -> modificarCliente());
        panel.add(btnGuardarMod, gbc);
        
        return panel;
    }
     
    private JPanel crearPanelEliminar() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 15, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        //Configurar Campos
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
        
        //COLUMNA IZQUIERDA
        //Cédula 
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.gridwidth = 1;
        panel.add(new JLabel(CargadorProperties.obtenerComponentes("label.cedula.ruc")), gbc);
        
        gbc.gridx = 1;
        gbc.gridwidth = 3;
        JPanel panelCedula = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panelCedula.setPreferredSize(new Dimension(380, 32));
        panelCedula.add(txtCedulaElim);
        JLabel lblLupa = new JLabel(CargadorProperties.obtenerComponentes("emoji.lupa"));
        lblLupa.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
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
        
        fila++;
        
        //Nombre
        gbc.gridx = 0;
        gbc.gridy = fila;
        panel.add(new JLabel(CargadorProperties.obtenerComponentes("label.nombre")), gbc);
        
        gbc.gridx = 1;
        panel.add(txtNombreElim, gbc);
        
        fila++;
        
        //Telefono
        gbc.gridx = 0;
        gbc.gridy = fila;
        panel.add(new JLabel(CargadorProperties.obtenerComponentes("label.telefono")), gbc);
        
        gbc.gridx = 1;
        panel.add(txtTelefonoElim, gbc);
        
        fila++;
        
        //email
        gbc.gridx = 0;
        gbc.gridy = fila;
        panel.add(new JLabel(CargadorProperties.obtenerComponentes("label.email")), gbc);
        
        gbc.gridx = 1;
        panel.add(txtEmailElim, gbc);
        
        //COLUMNA DERECHA
        fila = 1; 
        
        //Ciudad
        gbc.gridx = 2;
        gbc.gridy = fila;
        gbc.insets = new Insets(5, 10, 0, 10);
        panel.add(new JLabel(CargadorProperties.obtenerComponentes("label.ciudad")), gbc);
        
        gbc.gridx = 3;
        panel.add(comboCiudadElim, gbc);
        
        fila++;
        
        //Direccion
        gbc.gridx = 2;
        gbc.gridy = fila;
        panel.add(new JLabel(CargadorProperties.obtenerComponentes("label.direccion")), gbc);
        
        gbc.gridx = 3;
        panel.add(txtDireccionElim, gbc);
        
        //Botón Eliminar
        fila++;
        gbc.gridx = 3;
        gbc.gridy = fila;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(0, 10, 0, 10);
        btnEliminar = new JButton(CargadorProperties.obtenerComponentes("boton.eliminar"));
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
            CargadorProperties.obtenerComponentes("combo.consulta.seleccione"),
            CargadorProperties.obtenerComponentes("combo.consulta.general"),
            CargadorProperties.obtenerComponentes("combo.consulta.parametro")
        });
        comboTipoConsulta.setPreferredSize(new Dimension(250, 30));
        comboTipoConsulta.addActionListener(e -> cambiarTipoConsulta());
        panelTipo.add(comboTipoConsulta);
        panel.add(panelTipo, BorderLayout.NORTH);
        
        JPanel panelCentral = new JPanel(new BorderLayout(5, 5));
        
        //Configurar elementos de busqueda 
        panelBusqueda = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        txtBusqueda = new JTextField(20);
        txtBusqueda.setPreferredSize(new Dimension(250, 25));
        txtBusqueda.setToolTipText(CargadorProperties.obtenerComponentes("tooltip.busqueda"));
        comboParametroBusqueda = new JComboBox<>(new String[]{
            CargadorProperties.obtenerComponentes("combo.param.nombre"),
            CargadorProperties.obtenerComponentes("combo.param.cedula.ruc"),
            CargadorProperties.obtenerComponentes("combo.param.telefono"),     
            CargadorProperties.obtenerComponentes("combo.param.email"),
            CargadorProperties.obtenerComponentes("combo.param.ciudad.desc"),
            CargadorProperties.obtenerComponentes("combo.param.direccion")
        });
        comboParametroBusqueda.setPreferredSize(new Dimension(150, 25));
        
        panelBusqueda.add(new JLabel(CargadorProperties.obtenerComponentes("label.buscar")));
        panelBusqueda.add(txtBusqueda);
        panelBusqueda.add(comboParametroBusqueda);
        JLabel lblLupaBusq = new JLabel(CargadorProperties.obtenerComponentes("emoji.lupa"));
        panelBusqueda.add(lblLupaBusq);
        panelBusqueda.setVisible(false);
        
        panelCentral.add(panelBusqueda, BorderLayout.NORTH);
        
        //Configuracion tabla 
        String[] columnas = {
            CargadorProperties.obtenerComponentes("tabla.col.cedula.ruc"),
            CargadorProperties.obtenerComponentes("label.nombre"),
            CargadorProperties.obtenerComponentes("label.telefono"),
            CargadorProperties.obtenerComponentes("label.email"),
            CargadorProperties.obtenerComponentes("label.ciudad"),
            CargadorProperties.obtenerComponentes("label.direccion")
        };
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
    
    //Errores en rojo
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
    
    //Validaciones para el ingreso 
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
    
    //Validaciones en tiempo real 
    private void validarCedulaIng() {
        if (validandoCedulaIna) {
            return;
        }

        String cedula = txtCedulaIng.getText().trim();
        if (cedula.length() != 10 && cedula.length() != 13) {
            String error = ValidacionesCliente.validarCedula(cedula, false);
            mostrarError(lblErrorCedulaIng, error);
            return;
        }

        String error = ValidacionesCliente.validarCedula(cedula, false);

        if ("CLIENTE_INACTIVO".equals(error)) {
            mostrarError(lblErrorCedulaIng, CargadorProperties.obtenerMessages("CL_A_016"));

            validandoCedulaIna = true;

            SwingUtilities.invokeLater(() -> {
                int opcion = JOptionPane.showConfirmDialog(this,
                    CargadorProperties.obtenerMessages("CL_A_016"),
                    CargadorProperties.obtenerMessages("FC_A_008"),
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

                if (opcion == JOptionPane.YES_OPTION) {
                    cargarClienteInactivoParaReactivar(cedula);
                } else {
                    txtCedulaIng.setText("");
                    lblErrorCedulaIng.setText(" ");
                    validandoCedulaIna = false; 
                }
            });
        } else {
            mostrarError(lblErrorCedulaIng, error);
        }
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
    
    //Validaciones para la modificacion 
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
    
    //Validaciones en tiempo real
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

    //Carga de ComboBox
    private void cargarCiudades(JComboBox<ItemCombo> combo) {
        combo.removeAllItems();
        combo.addItem(new ItemCombo("", CargadorProperties.obtenerComponentes("combo.ciudad.seleccione")));
        
        Cliente cli = new Cliente();
        ArrayList<ItemCombo> ciudades = cli.obtenerCiudadesDP();
        
        for (ItemCombo ciudad : ciudades) {
            combo.addItem(ciudad);
        }
    }
    
    //Metodo para cargar el panel segun la elección
    private void cambiarPanel() {
        int seleccion = comboOpciones.getSelectedIndex();

        switch (seleccion) {
            case OPCION_INGRESAR:
                lblTituloSuperior.setText(CargadorProperties.obtenerComponentes("ventana.clientes.titulo.ingresar"));
                limpiarCamposIngresar();
                cardLayout.show(panelContenedor, PANEL_INGRESAR);
                break;
            case OPCION_MODIFICAR:
                lblTituloSuperior.setText(CargadorProperties.obtenerComponentes("ventana.clientes.titulo.modificar"));
                limpiarCamposModificar();
                cardLayout.show(panelContenedor, PANEL_MODIFICAR);
                break;
            case OPCION_ELIMINAR:
                lblTituloSuperior.setText(CargadorProperties.obtenerComponentes("ventana.clientes.titulo.eliminar"));
                limpiarCamposEliminar();
                cardLayout.show(panelContenedor, PANEL_ELIMINAR);
                break;
            case OPCION_CONSULTAR:
                lblTituloSuperior.setText(CargadorProperties.obtenerComponentes("ventana.clientes.titulo.consultar"));
                cardLayout.show(panelContenedor, PANEL_CONSULTAR);
                break;
            default:
                lblTituloSuperior.setText(CargadorProperties.obtenerComponentes(""));
                cardLayout.show(panelContenedor, PANEL_VACIO);
                break;
        }
    }
    
    private void cambiarTipoConsulta() {
        String tipo = (String) comboTipoConsulta.getSelectedItem();
        
        if (CargadorProperties.obtenerComponentes("combo.consulta.general").equals(tipo)) {
            panelBusqueda.setVisible(false);
            scrollTabla.setVisible(true);
            consultarGeneral();
        } else if (CargadorProperties.obtenerComponentes("combo.consulta.parametro").equals(tipo)) {
            panelBusqueda.setVisible(true);
            scrollTabla.setVisible(true);
            modeloTabla.setRowCount(0);
        } else {
            panelBusqueda.setVisible(false);
            scrollTabla.setVisible(false);
        }
    }
    
    //Vuelve al panel vacío
    private void volverAlMenu() {
        this.dispose();             
        new MenuPrincipal().setVisible(true);
    }
    
    //Caso especial de carga para reactivaion de un cliente
    private void cargarClienteInactivoParaReactivar(String cedula) {
        Cliente cli = new Cliente();
        Cliente clienteExistente = cli.verificarDP(cedula);

        if (clienteExistente != null) {
            txtCedulaIng.setText(clienteExistente.getCedRuc());
            txtNombreIng.setText(clienteExistente.getNombre());
            txtTelefonoIng.setText(clienteExistente.getTelefono());
            txtEmailIng.setText(clienteExistente.getEmail());
            txtDireccionIng.setText(clienteExistente.getDireccion());

            for (int i = 0; i < comboCiudadIng.getItemCount(); i++) {
                ItemCombo item = comboCiudadIng.getItemAt(i);
                if (item.getId().equals(clienteExistente.getIdCiudad())) {
                    comboCiudadIng.setSelectedIndex(i);
                    break;
                }
            }

            txtCedulaIng.setEnabled(false);

            lblErrorCedulaIng.setText(" ");
            lblErrorNombreIng.setText(" ");
            lblErrorTelefonoIng.setText(" ");
            lblErrorEmailIng.setText(" ");
            lblErrorCiudadIng.setText(" ");
            lblErrorDireccionIng.setText(" ");

            validandoCedulaIna = false;

            JOptionPane.showMessageDialog(this,
                CargadorProperties.obtenerMessages("CL_I_006"),
                CargadorProperties.obtenerMessages("FC_C_006"),
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    //Metodos CRUD 
    private void guardarCliente() {
        ItemCombo ciudadSel = (ItemCombo) comboCiudadIng.getSelectedItem();
        String idCiudad = ciudadSel != null ? ciudadSel.getId() : "";

        String errorCedula = ValidacionesCliente.validarCedula(txtCedulaIng.getText(), false);
        String errorNombre = ValidacionesCliente.validarNombre(txtNombreIng.getText());
        String errorTelefono = ValidacionesCliente.validarTelefono(txtTelefonoIng.getText());
        String errorEmail = ValidacionesCliente.validarEmail(txtEmailIng.getText());
        String errorCiudad = ValidacionesCliente.validarCiudad(idCiudad);
        String errorDireccion = ValidacionesCliente.validarDireccion(txtDireccionIng.getText());

        mostrarError(lblErrorCedulaIng, "CLIENTE_INACTIVO".equals(errorCedula) ? null : errorCedula);
        mostrarError(lblErrorNombreIng, errorNombre);
        mostrarError(lblErrorTelefonoIng, errorTelefono);
        mostrarError(lblErrorEmailIng, errorEmail);
        mostrarError(lblErrorCiudadIng, errorCiudad);
        mostrarError(lblErrorDireccionIng, errorDireccion);

        boolean hayErrores = (errorCedula != null && !"CLIENTE_INACTIVO".equals(errorCedula)) || 
                             errorNombre != null || 
                             errorTelefono != null || 
                             errorEmail != null || 
                             errorCiudad != null || 
                             errorDireccion != null;

        if (hayErrores) {
            JOptionPane.showMessageDialog(this,
                CargadorProperties.obtenerMessages("CL_A_015"),
                CargadorProperties.obtenerMessages("FC_C_005"),
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

        boolean exitoso;
        if (!txtCedulaIng.isEnabled()) {
            exitoso = cli.reactivarDP();
        } else {
            exitoso = cli.grabarDP();
        }

        if (exitoso) {
            String mensaje = !txtCedulaIng.isEnabled() ? 
                CargadorProperties.obtenerMessages("CL_I_007") : 
                CargadorProperties.obtenerMessages("CL_I_001");  

            JOptionPane.showMessageDialog(this,
                mensaje,
                CargadorProperties.obtenerMessages("FC_C_003"),
                JOptionPane.INFORMATION_MESSAGE);
            limpiarCamposIngresar();
        } else {
            JOptionPane.showMessageDialog(this,
                CargadorProperties.obtenerMessages("CL_E_002"),
                CargadorProperties.obtenerMessages("FC_C_004"),
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void buscarClienteModificar() {
        String cedula = txtCedulaMod.getText().trim();

        if (cedula.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                CargadorProperties.obtenerMessages("CL_A_013"),
                CargadorProperties.obtenerMessages("FC_C_005"),
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        Cliente cli = new Cliente();
        Cliente encontrado = cli.verificarDP(cedula);

        if (encontrado != null) {
            if ("INA".equals(encontrado.getEstado())) {
                JOptionPane.showMessageDialog(this,
                    CargadorProperties.obtenerMessages("CL_A_017"),
                    CargadorProperties.obtenerMessages("FC_C_006"),
                    JOptionPane.INFORMATION_MESSAGE);
                return;
            }

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
                CargadorProperties.obtenerMessages("CL_A_014"),
                CargadorProperties.obtenerMessages("FC_C_006"),
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
   
    private void modificarCliente() {
        ItemCombo ciudadSel = (ItemCombo) comboCiudadMod.getSelectedItem();
        String idCiudad = ciudadSel != null ? ciudadSel.getId() : "";

        String errorCedula = ValidacionesCliente.validarCedula(txtCedulaMod.getText(), true);
        String errorNombre = ValidacionesCliente.validarNombre(txtNombreMod.getText());
        String errorTelefono = ValidacionesCliente.validarTelefono(txtTelefonoMod.getText());
        String errorEmail = ValidacionesCliente.validarEmail(txtEmailMod.getText());
        String errorCiudad = ValidacionesCliente.validarCiudad(idCiudad);
        String errorDireccion = ValidacionesCliente.validarDireccion(txtDireccionMod.getText());

        mostrarError(lblErrorCedulaMod, errorCedula);
        mostrarError(lblErrorNombreMod, errorNombre);
        mostrarError(lblErrorTelefonoMod, errorTelefono);
        mostrarError(lblErrorEmailMod, errorEmail);
        mostrarError(lblErrorCiudadMod, errorCiudad);
        mostrarError(lblErrorDireccionMod, errorDireccion);

        boolean hayErrores = errorCedula != null || errorNombre != null || 
                             errorTelefono != null || errorEmail != null || 
                             errorCiudad != null || errorDireccion != null;

        if (hayErrores) {
            JOptionPane.showMessageDialog(this,
                CargadorProperties.obtenerMessages("CL_A_015"),
                CargadorProperties.obtenerMessages("FC_C_005"),
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
                CargadorProperties.obtenerMessages("FC_C_003"),
                JOptionPane.INFORMATION_MESSAGE);
            limpiarCamposModificar();
        } else {
            JOptionPane.showMessageDialog(this,
                CargadorProperties.obtenerMessages("CL_E_004"),
                CargadorProperties.obtenerMessages("FC_C_004"),
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void buscarClienteEliminar() {
        String cedula = txtCedulaElim.getText().trim();

        if (cedula.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                CargadorProperties.obtenerMessages("CL_A_013"),
                CargadorProperties.obtenerMessages("FC_C_005"),
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        Cliente cli = new Cliente();
        Cliente encontrado = cli.verificarDP(cedula);

        if (encontrado != null) {
            if ("INA".equals(encontrado.getEstado())) {
                JOptionPane.showMessageDialog(this,
                    CargadorProperties.obtenerMessages("CL_A_017"),
                    CargadorProperties.obtenerMessages("FC_C_006"),
                    JOptionPane.INFORMATION_MESSAGE);
                return;
            }

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
                CargadorProperties.obtenerMessages("CL_A_014"),
                CargadorProperties.obtenerMessages("FC_C_006"),
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void eliminarCliente() {
        int confirm = JOptionPane.showConfirmDialog(this,
            CargadorProperties.obtenerMessages("CL_C_001") + txtNombreElim.getText() + CargadorProperties.obtenerMessages("CL_C_002"),
            CargadorProperties.obtenerMessages("FC_A_008"),
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            Cliente cli = new Cliente();
            cli.setCedRuc(txtCedulaElim.getText().trim());
            
            if (cli.eliminarDP()) {
                JOptionPane.showMessageDialog(this,
                    CargadorProperties.obtenerMessages("CL_I_003"),
                    CargadorProperties.obtenerMessages("FC_C_003"),
                    JOptionPane.INFORMATION_MESSAGE);
                limpiarCamposEliminar();
            } else {
                JOptionPane.showMessageDialog(this,
                    CargadorProperties.obtenerMessages("CL_E_005"),
                    CargadorProperties.obtenerMessages("FC_C_004"),
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
        
    private void consultarGeneral() {
        Cliente cli = new Cliente();
        ArrayList<Cliente> clientes = cli.consultarTodos();

        modeloTabla.setRowCount(0);

        if (clientes.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                CargadorProperties.obtenerMessages("CL_I_004"),
                CargadorProperties.obtenerMessages("FC_C_006"),
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }

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
    
    //Busqueda en tiempo real para la consulta por parámetro 
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
        
        if (parametro.equals(CargadorProperties.obtenerComponentes("combo.param.nombre"))) {
            resultados = cli.buscarPorNombreDP(texto);
        } else if (parametro.equals(CargadorProperties.obtenerComponentes("combo.param.cedula.ruc"))) {
            resultados = cli.buscarPorCedulaDP(texto);
        } else if (parametro.equals(CargadorProperties.obtenerComponentes("combo.param.telefono"))) {  
            resultados = cli.buscarPorTelefonoDP(texto);
        } else if (parametro.equals(CargadorProperties.obtenerComponentes("combo.param.email"))) {    
            resultados = cli.buscarPorEmailDP(texto);
        } else if (parametro.equals(CargadorProperties.obtenerComponentes("combo.param.ciudad.desc"))) {
            resultados = cli.buscarPorCiudadDP(texto);
        } else if (parametro.equals(CargadorProperties.obtenerComponentes("combo.param.direccion"))) {
            resultados = cli.buscarPorDireccionDP(texto);
        }
        
        modeloTabla.setRowCount(0);
        
        if (resultados.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                CargadorProperties.obtenerMessages("CL_I_005"),
                CargadorProperties.obtenerMessages("FC_C_006"),
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
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
    
    //Métods para limpiar campos 
    private void limpiarCamposIngresar() {
        validandoCedulaIna = false;
        String cedulaActual = txtCedulaIng.getText().trim();
        boolean esReactivacion = false;

        if (!cedulaActual.isEmpty()) {
            Cliente cli = new Cliente();
            Cliente existe = cli.verificarDP(cedulaActual);
            if (existe != null && "ACT".equals(existe.getEstado())) {
                esReactivacion = true;
            }
        }

        txtCedulaIng.setText("");
        txtCedulaIng.setEnabled(true); 
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
}   
