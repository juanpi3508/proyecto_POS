package GUI;

import DP.Cliente;
import DP.Factura;
import DP.ProxFac;
import DP.Producto;
import MD.FacturaMD;
import util.CargadorProperties;
import util.ValidacionesFactura;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.DocumentEvent;       
import java.time.LocalDateTime;         
import java.time.format.DateTimeFormatter;  
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class VentanaFactura extends JFrame {
    private JLabel lblCodigoGenerado;
    // CardLayout para cambiar entre paneles
    private CardLayout cardLayout;
    private JPanel panelContenedor;
    
    // ComboBox principal
    private JComboBox<String> comboOpciones;
    
    // Constantes de paneles
    private static final String PANEL_VACIO = "VACIO";
    private static final String PANEL_CREAR = "CREAR";
    private static final String PANEL_MODIFICAR = "MODIFICAR";
    private static final String PANEL_ELIMINAR = "ELIMINAR";
    private static final String PANEL_CONSULTAR = "CONSULTAR";
    
    // Componentes del panel CREAR
    private JTextField txtCedulaCrear, txtCantidadCrear;
    private JLabel lblNombreClienteCrear, lblCedulaClienteCrear, lblEmailClienteCrear, lblTelefonoClienteCrear;
    private JLabel lblErrorCedulaCrear, lblErrorCantidadCrear;
    private JComboBox<ItemProducto> cmbProductoCrear;
    private JTable tablaProductosCrear;
    private DefaultTableModel modeloProductosCrear;
    private JTextField txtSubtotalCrear, txtIVACrear, txtTotalCrear;

    // Componentes del panel MODIFICAR
    private JTextField txtCedulaMod;
    private JTable tablaFacturasMod;
    private DefaultTableModel modeloFacturasMod;
    private JTable tablaProductosMod;
    private DefaultTableModel modeloProductosMod;
    private JComboBox<String> cmbTipoMod;
    private JComboBox<ItemProducto> cmbProductoMod;
    private JTextField txtCantidadMod, txtSubtotalMod, txtIVAMod, txtTotalMod;
    private JButton btnGuardarMod;
    private Factura facturaSeleccionada;
    private JLabel lblCodigoMod, lblFechaMod;
    private JLabel lblNombreClienteMod, lblCedulaClienteMod, lblEmailClienteMod, lblTelefonoClienteMod;
    private JPanel panelCabeceraMod;
    
    // Componentes del panel ELIMINAR
    private JTextField txtCedulaElim;
    private JTable tablaFacturasElim;
    private DefaultTableModel modeloFacturasElim;
    private JButton btnEliminar;
    
    // Componentes del panel CONSULTAR
    private JComboBox<String> comboTipoConsulta;
    private JTextField txtCedulaConsulta;
    private JTable tablaResultados;
    private DefaultTableModel modeloTablaResultados;
    private JPanel panelBusqueda;
        
    private static final DateTimeFormatter FMT_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    
    private Cliente clienteSeleccionado;
    private ArrayList<ProxFac> productosFactura;
    
    public VentanaFactura() {
        productosFactura = new ArrayList<>();
        configurarVentana();
        inicializarComponentes();
        configurarLayout();
    }
    
    private void configurarVentana() {
        setTitle("Gestión de Facturas - KoKo Market");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }
    
    private void inicializarComponentes() {
        cardLayout = new CardLayout();
        panelContenedor = new JPanel(cardLayout);
        
        comboOpciones = new JComboBox<>(new String[]{
            "Seleccione una opción...",
            "Crear Factura",
            "Modificar Factura",
            "Eliminar Factura",
            "Consultar Facturas"
        });
        
        comboOpciones.addActionListener(e -> cambiarPanel());
        
        panelContenedor.add(crearPanelVacio(), PANEL_VACIO);
        panelContenedor.add(crearPanelCrear(), PANEL_CREAR);
        panelContenedor.add(crearPanelModificar(), PANEL_MODIFICAR);
        panelContenedor.add(crearPanelEliminar(), PANEL_ELIMINAR);
        panelContenedor.add(crearPanelConsultar(), PANEL_CONSULTAR);
    }
    
    private void configurarLayout() {
        setLayout(new BorderLayout(10, 10));
        
        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        panelSuperior.add(comboOpciones);
        add(panelSuperior, BorderLayout.NORTH);
        
        add(panelContenedor, BorderLayout.CENTER);
        
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        JButton btnVolver = new JButton("Volver");
        btnVolver.addActionListener(e -> volverAlMenu());
        panelInferior.add(btnVolver);
        add(panelInferior, BorderLayout.SOUTH);
    }
    
    // ==================== PANEL VACÍO ====================
    private JPanel crearPanelVacio() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        JLabel lblTitulo = new JLabel("Gestión de Facturas");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 48));
        lblTitulo.setForeground(Color.DARK_GRAY);
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(lblTitulo, gbc);
        
        return panel;
    }

// ========== CREAR CABECERA BONITA ==========
private JPanel crearPanelCabecera() {
    JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
    panelPrincipal.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
        BorderFactory.createEmptyBorder(15, 15, 15, 15)
    ));
    panelPrincipal.setBackground(new Color(245, 245, 245));
    
    // ========== PANEL IZQUIERDO (EMPRESA) ==========
    JPanel panelEmpresa = new JPanel();
    panelEmpresa.setLayout(new BoxLayout(panelEmpresa, BoxLayout.Y_AXIS));
    panelEmpresa.setBackground(new Color(245, 245, 245));
    
    JLabel lblEmpresa = new JLabel("KoKo Market");
    lblEmpresa.setFont(new Font("Arial", Font.BOLD, 18));
    lblEmpresa.setAlignmentX(Component.LEFT_ALIGNMENT);
    
    JLabel lblEmail = new JLabel("ventas@kokomarket.com.ec");
    lblEmail.setFont(new Font("Arial", Font.PLAIN, 12));
    lblEmail.setAlignmentX(Component.LEFT_ALIGNMENT);
    
    JLabel lblTelefono = new JLabel("Teléfono: 022876543");
    lblTelefono.setFont(new Font("Arial", Font.PLAIN, 12));
    lblTelefono.setAlignmentX(Component.LEFT_ALIGNMENT);
    
    panelEmpresa.add(lblEmpresa);
    panelEmpresa.add(Box.createVerticalStrut(5));
    panelEmpresa.add(lblEmail);
    panelEmpresa.add(Box.createVerticalStrut(3));
    panelEmpresa.add(lblTelefono);
    
    // ========== PANEL DERECHO (FECHA Y CÓDIGO) ==========
    JPanel panelInfo = new JPanel();
    panelInfo.setLayout(new BoxLayout(panelInfo, BoxLayout.Y_AXIS));
    panelInfo.setBackground(new Color(245, 245, 245));
    
    LocalDateTime ahora = LocalDateTime.now();
    String fechaHora = ahora.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    
    JLabel lblFecha = new JLabel("Fecha: " + fechaHora);
    lblFecha.setFont(new Font("Arial", Font.PLAIN, 12));
    lblFecha.setAlignmentX(Component.RIGHT_ALIGNMENT);
    
    JLabel lblCodigo = new JLabel("Código: [Autogenerado]");
    lblCodigo.setFont(new Font("Arial", Font.BOLD, 12));
    lblCodigo.setForeground(new Color(0, 102, 204));
    lblCodigo.setAlignmentX(Component.RIGHT_ALIGNMENT);
    
    panelInfo.add(lblFecha);
    panelInfo.add(Box.createVerticalStrut(5));
    panelInfo.add(lblCodigo);
    
    panelPrincipal.add(panelEmpresa, BorderLayout.WEST);
    panelPrincipal.add(panelInfo, BorderLayout.EAST);
    
    return panelPrincipal;
}

private JPanel crearPanelCrear() {
    JPanel panel = new JPanel(new GridBagLayout());
    panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(5, 10, 0, 10);
    gbc.anchor = GridBagConstraints.WEST;
    
    // Inicializar componentes
    txtCedulaCrear = new JTextField();
    txtCedulaCrear.setPreferredSize(new Dimension(200, 25));
    
    lblNombreClienteCrear = new JLabel("");
    lblCedulaClienteCrear = new JLabel("");
    lblEmailClienteCrear = new JLabel("");
    lblTelefonoClienteCrear = new JLabel("");
    
    cmbProductoCrear = new JComboBox<>();
    cmbProductoCrear.setPreferredSize(new Dimension(400, 25));
    
    txtCantidadCrear = new JTextField();
    txtCantidadCrear.setPreferredSize(new Dimension(80, 25));
    
    txtSubtotalCrear = new JTextField("0.00");
    txtSubtotalCrear.setEditable(false);
    txtSubtotalCrear.setPreferredSize(new Dimension(120, 25));
    
    txtIVACrear = new JTextField("0.00");
    txtIVACrear.setEditable(false);
    txtIVACrear.setPreferredSize(new Dimension(120, 25));
    
    txtTotalCrear = new JTextField("0.00");
    txtTotalCrear.setEditable(false);
    txtTotalCrear.setFont(new Font("Arial", Font.BOLD, 12));
    txtTotalCrear.setPreferredSize(new Dimension(120, 25));
    
    // Labels de error
    lblErrorCedulaCrear = crearLabelError();
    lblErrorCantidadCrear = crearLabelError();
    
    // Label código generado
    lblCodigoGenerado = new JLabel("Código: [Generando...]");
    lblCodigoGenerado.setFont(new Font("Arial", Font.BOLD, 12));
    
    // Generar código al abrir panel
    generarYMostrarCodigo();
    
    // Configurar validaciones
    configurarValidacionesCrear();
    
   int fila = 0;

// ==================== TÍTULO PRINCIPAL ====================
gbc.gridx = 0;
gbc.gridy = fila;
gbc.gridwidth = 8;
gbc.anchor = GridBagConstraints.CENTER;
gbc.insets = new Insets(10, 10, 15, 10);
JLabel lblTitulo = new JLabel("Factura");
lblTitulo.setFont(new Font("Arial", Font.BOLD, 42));
panel.add(lblTitulo, gbc);
gbc.gridwidth = 1;
gbc.insets = new Insets(5, 10, 0, 10);

// ==================== FILA CON 3 SECCIONES (MISMO NIVEL) ====================
fila++;

// COLUMNA IZQUIERDA: Código y Fecha
gbc.gridx = 0;
gbc.gridy = fila;
gbc.gridwidth = 2;
gbc.anchor = GridBagConstraints.NORTHWEST;
gbc.insets = new Insets(10, 10, 0, 10);

JPanel panelIzq = new JPanel();
panelIzq.setLayout(new BoxLayout(panelIzq, BoxLayout.Y_AXIS));

lblCodigoGenerado.setFont(new Font("Arial", Font.BOLD, 14));
lblCodigoGenerado.setAlignmentX(Component.LEFT_ALIGNMENT);
panelIzq.add(lblCodigoGenerado);
panelIzq.add(Box.createVerticalStrut(8));

JLabel lblFechaTit = new JLabel("Fecha y hora:");
lblFechaTit.setFont(new Font("Arial", Font.BOLD, 13));
lblFechaTit.setAlignmentX(Component.LEFT_ALIGNMENT);
panelIzq.add(lblFechaTit);

LocalDateTime ahora = LocalDateTime.now();
String fechaHora = ahora.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
JLabel lblFechaVal = new JLabel(fechaHora);
lblFechaVal.setFont(new Font("Arial", Font.PLAIN, 13));
lblFechaVal.setAlignmentX(Component.LEFT_ALIGNMENT);
panelIzq.add(lblFechaVal);

panel.add(panelIzq, gbc);
gbc.gridwidth = 1;

// COLUMNA CENTRO: Información Empresa (CENTRADA COMPLETAMENTE)
gbc.gridx = 0;  // ← DESDE EL INICIO
gbc.gridy = fila;
gbc.gridwidth = 8;  // ← TODO EL ANCHO
gbc.anchor = GridBagConstraints.CENTER;  // ← CENTRADO
gbc.insets = new Insets(10, 10, 0, 10);

JPanel panelEmpresa = new JPanel();
panelEmpresa.setLayout(new BoxLayout(panelEmpresa, BoxLayout.Y_AXIS));

JLabel lblEmpNom = new JLabel("Koko Market");
lblEmpNom.setFont(new Font("Arial", Font.BOLD, 16));
lblEmpNom.setAlignmentX(Component.CENTER_ALIGNMENT);
panelEmpresa.add(lblEmpNom);
panelEmpresa.add(Box.createVerticalStrut(3));

JLabel lblEmpEmail = new JLabel("Email: ventas@kokomarket.com");
lblEmpEmail.setFont(new Font("Arial", Font.PLAIN, 14));
lblEmpEmail.setAlignmentX(Component.CENTER_ALIGNMENT);
panelEmpresa.add(lblEmpEmail);
panelEmpresa.add(Box.createVerticalStrut(3));

JLabel lblEmpTel = new JLabel("Telefono: 022876543");
lblEmpTel.setFont(new Font("Arial", Font.PLAIN, 14));
lblEmpTel.setAlignmentX(Component.CENTER_ALIGNMENT);
panelEmpresa.add(lblEmpTel);

panel.add(panelEmpresa, gbc);
gbc.gridwidth = 1;

// COLUMNA DERECHA: Información Cliente
gbc.gridx = 6;
gbc.gridy = fila; // ← MISMA FILA
gbc.gridwidth = 2;
gbc.anchor = GridBagConstraints.NORTHEAST;
gbc.insets = new Insets(10, 10, 0, 10);

JPanel panelCliente = new JPanel(new GridBagLayout());
GridBagConstraints gbcCli = new GridBagConstraints();
gbcCli.anchor = GridBagConstraints.WEST;
gbcCli.insets = new Insets(2, 5, 2, 5);

int filaC = 0;

// Título
gbcCli.gridx = 0;
gbcCli.gridy = filaC;
gbcCli.gridwidth = 3;
JLabel lblTitCliente = new JLabel("Información del cliente");
lblTitCliente.setFont(new Font("Arial", Font.BOLD, 16));
panelCliente.add(lblTitCliente, gbcCli);
gbcCli.gridwidth = 1;

// Cédula
filaC++;
gbcCli.gridx = 0;
gbcCli.gridy = filaC;
JLabel lblCedRuc = new JLabel("Cédula o Ruc:");
lblCedRuc.setFont(new Font("Arial", Font.BOLD, 12));
panelCliente.add(lblCedRuc, gbcCli);

gbcCli.gridx = 1;
panelCliente.add(txtCedulaCrear, gbcCli);

gbcCli.gridx = 2;
JButton btnLupa = new JButton("🔍");
btnLupa.setPreferredSize(new Dimension(35, 25));
btnLupa.setFont(new Font("Arial", Font.PLAIN, 14));
btnLupa.addActionListener(e -> buscarClienteCrear());
panelCliente.add(btnLupa, gbcCli);

// Error
filaC++;
gbcCli.gridx = 1;
gbcCli.gridy = filaC;
gbcCli.gridwidth = 2;
panelCliente.add(lblErrorCedulaCrear, gbcCli);
gbcCli.gridwidth = 1;

// Nombre
filaC++;
gbcCli.gridx = 0;
gbcCli.gridy = filaC;
JLabel lblNomTit = new JLabel("Nombre:");
lblNomTit.setFont(new Font("Arial", Font.BOLD, 12));
panelCliente.add(lblNomTit, gbcCli);

gbcCli.gridx = 1;
gbcCli.gridwidth = 2;
lblNombreClienteCrear.setFont(new Font("Arial", Font.PLAIN, 12));
panelCliente.add(lblNombreClienteCrear, gbcCli);
gbcCli.gridwidth = 1;

// Cédula datos
filaC++;
gbcCli.gridx = 0;
gbcCli.gridy = filaC;
JLabel lblCedTit = new JLabel("Cédula:");
lblCedTit.setFont(new Font("Arial", Font.BOLD, 12));
panelCliente.add(lblCedTit, gbcCli);

gbcCli.gridx = 1;
gbcCli.gridwidth = 2;
lblCedulaClienteCrear.setFont(new Font("Arial", Font.PLAIN, 12));
panelCliente.add(lblCedulaClienteCrear, gbcCli);
gbcCli.gridwidth = 1;

// Email
filaC++;
gbcCli.gridx = 0;
gbcCli.gridy = filaC;
JLabel lblEmailTit = new JLabel("Email:");
lblEmailTit.setFont(new Font("Arial", Font.BOLD, 12));
panelCliente.add(lblEmailTit, gbcCli);

gbcCli.gridx = 1;
gbcCli.gridwidth = 2;
lblEmailClienteCrear.setFont(new Font("Arial", Font.PLAIN, 12));
panelCliente.add(lblEmailClienteCrear, gbcCli);
gbcCli.gridwidth = 1;

// Teléfono
filaC++;
gbcCli.gridx = 0;
gbcCli.gridy = filaC;
JLabel lblTelTit = new JLabel("Telefono:");
lblTelTit.setFont(new Font("Arial", Font.BOLD, 12));
panelCliente.add(lblTelTit, gbcCli);

gbcCli.gridx = 1;
gbcCli.gridwidth = 2;
lblTelefonoClienteCrear.setFont(new Font("Arial", Font.PLAIN, 12));
panelCliente.add(lblTelefonoClienteCrear, gbcCli);

panel.add(panelCliente, gbc);
gbc.gridwidth = 1;
gbc.anchor = GridBagConstraints.WEST;
gbc.insets = new Insets(5, 10, 0, 10);

// ==================== SEPARADOR ====================
fila++;
gbc.gridx = 0;
gbc.gridy = fila;
gbc.gridwidth = 8;
gbc.insets = new Insets(15, 10, 15, 10);
gbc.fill = GridBagConstraints.HORIZONTAL;
panel.add(new JSeparator(), gbc);
gbc.fill = GridBagConstraints.NONE;
gbc.gridwidth = 1;
gbc.insets = new Insets(5, 10, 0, 10);

        // ==================== TÍTULO PRODUCTOS ====================
    fila++;
    gbc.gridx = 0;
    gbc.gridy = fila;
    gbc.gridwidth = 8;
    JLabel lblTitProd = new JLabel("Productos de la factura");
    lblTitProd.setFont(new Font("Arial", Font.BOLD, 18)); // ← MÁS GRANDE
    panel.add(lblTitProd, gbc);
    gbc.gridwidth = 1;
    
    // ==================== SELECCIÓN PRODUCTO ====================
    fila++;
    gbc.gridx = 0;
    gbc.gridy = fila;
    JLabel lblProd = new JLabel("Producto:");
    lblProd.setFont(new Font("Arial", Font.BOLD, 12));
    panel.add(lblProd, gbc);
    
    gbc.gridx = 1;
    gbc.gridwidth = 4;
    panel.add(cmbProductoCrear, gbc);
    gbc.gridwidth = 1;
    
    gbc.gridx = 5;
    JLabel lblCant = new JLabel("Cantidad:");
    lblCant.setFont(new Font("Arial", Font.BOLD, 12));
    panel.add(lblCant, gbc);
    
    gbc.gridx = 6;
    panel.add(txtCantidadCrear, gbc);
    
    gbc.gridx = 7;
    JButton btnAgregar = new JButton("Agregar");
    btnAgregar.setPreferredSize(new Dimension(90, 25));
    btnAgregar.addActionListener(e -> agregarProducto());
    panel.add(btnAgregar, gbc);
    
    // Error cantidad
    fila++;
    gbc.gridx = 6;
    gbc.gridy = fila;
    gbc.insets = new Insets(0, 10, 0, 10);
    panel.add(lblErrorCantidadCrear, gbc);
    gbc.insets = new Insets(5, 10, 0, 10);
    
    // ==================== TABLA PRODUCTOS ====================
    fila++;
    gbc.gridx = 0;
    gbc.gridy = fila;
    gbc.gridwidth = 8;
    gbc.fill = GridBagConstraints.BOTH;
    gbc.weightx = 1.0;
    gbc.weighty = 1.0;
    gbc.insets = new Insets(10, 10, 10, 10);
    
    String[] cols = {"Código", "Descripción", "Cantidad", "Precio Unitario", "Subtotal"}; // ← CAMBIO AQUÍ
    modeloProductosCrear = new DefaultTableModel(cols, 0) {
        @Override
        public boolean isCellEditable(int row, int column) { return false; }
    };
    tablaProductosCrear = new JTable(modeloProductosCrear);
    JScrollPane scrollTabla = new JScrollPane(tablaProductosCrear);
    scrollTabla.setPreferredSize(new Dimension(800, 180));
    panel.add(scrollTabla, gbc);
    
    // Botón quitar
    fila++;
    gbc.gridy = fila;
    gbc.gridx = 7;
    gbc.gridwidth = 1;
    gbc.anchor = GridBagConstraints.EAST;
    gbc.fill = GridBagConstraints.NONE;
    gbc.weightx = 0;
    gbc.weighty = 0;
    gbc.insets = new Insets(5, 10, 10, 10);
    JButton btnQuitar = new JButton("Quitar");
    btnQuitar.setPreferredSize(new Dimension(90, 25));
    btnQuitar.addActionListener(e -> quitarProducto(tablaProductosCrear, modeloProductosCrear));
    panel.add(btnQuitar, gbc);
    
    // ==================== TOTALES ====================
    fila++;
    gbc.gridx = 6;
    gbc.gridy = fila;
    gbc.anchor = GridBagConstraints.EAST;
    JLabel lblSub = new JLabel("Subtotal:");
    lblSub.setFont(new Font("Arial", Font.BOLD, 12));
    panel.add(lblSub, gbc);
    
    gbc.gridx = 7;
    panel.add(txtSubtotalCrear, gbc);
    
    fila++;
    gbc.gridx = 6;
    gbc.gridy = fila;
    JLabel lblIva = new JLabel("IVA (15%):");
    lblIva.setFont(new Font("Arial", Font.BOLD, 12));
    panel.add(lblIva, gbc);
    
    gbc.gridx = 7;
    panel.add(txtIVACrear, gbc);
    
    fila++;
    gbc.gridx = 6;
    gbc.gridy = fila;
    JLabel lblTot = new JLabel("Total:");
    lblTot.setFont(new Font("Arial", Font.BOLD, 14));
    panel.add(lblTot, gbc);
    
    gbc.gridx = 7;
    panel.add(txtTotalCrear, gbc);
    
    // Botón guardar
    fila++;
    gbc.gridx = 7;
    gbc.gridy = fila;
    gbc.insets = new Insets(15, 10, 10, 10);
    JButton btnGuardar = new JButton("Guardar Factura");
    btnGuardar.setPreferredSize(new Dimension(140, 30));
    btnGuardar.setFont(new Font("Arial", Font.BOLD, 12));
    btnGuardar.addActionListener(e -> crearFactura());
    panel.add(btnGuardar, gbc);
    
    cargarProductosActivos(cmbProductoCrear);
    
    // Scroll
    JPanel panelConScroll = new JPanel(new BorderLayout());
    JScrollPane scrollPane = new JScrollPane(panel);
    scrollPane.getVerticalScrollBar().setUnitIncrement(16);
    scrollPane.setBorder(null);
    panelConScroll.add(scrollPane, BorderLayout.CENTER);
    
    return panelConScroll;
}

    // ==================== MÉTODO AUXILIAR PARA CREAR LABELS DE ERROR ====================
    private JLabel crearLabelError() {
        JLabel lbl = new JLabel(" "); // Espacio en blanco
        lbl.setForeground(Color.RED);
        lbl.setFont(new Font("Arial", Font.PLAIN, 11));
        return lbl;
    }

    // ==================== VALIDACIONES EN TIEMPO REAL ====================
    private void configurarValidacionesCrear() {
        // Validación de Cédula en tiempo real
        txtCedulaCrear.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { validarCedulaCrear(); }
            public void removeUpdate(DocumentEvent e) { validarCedulaCrear(); }
            public void changedUpdate(DocumentEvent e) { validarCedulaCrear(); }
        });

        // Validación de Cantidad en tiempo real
        txtCantidadCrear.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { validarCantidadCrear(); }
            public void removeUpdate(DocumentEvent e) { validarCantidadCrear(); }
            public void changedUpdate(DocumentEvent e) { validarCantidadCrear(); }
        });
    }

    private void validarCedulaCrear() {
        String error = ValidacionesFactura.validarCedRucCliente(txtCedulaCrear.getText());
        mostrarError(lblErrorCedulaCrear, error);
    }

    private void validarCantidadCrear() {
        String error = ValidacionesFactura.validarCantidad(txtCantidadCrear.getText());
        mostrarError(lblErrorCantidadCrear, error);
    }

    private void mostrarError(JLabel label, String mensaje) {
        if (mensaje != null) {
            label.setText(mensaje);
        } else {
            label.setText(" ");
        }
    }

   private void buscarClienteCrear() {
    String cedRuc = txtCedulaCrear.getText().trim();
    
    String error = ValidacionesFactura.validarCedRucCliente(cedRuc);
    if (error != null) {
        mostrarError(lblErrorCedulaCrear, error);
        limpiarDatosCliente();
        return;
    }
    
    Cliente cli = new Cliente();
    clienteSeleccionado = cli.verificarDP(cedRuc);
    
    if (clienteSeleccionado != null) {
        lblNombreClienteCrear.setText(clienteSeleccionado.getNombre());
        lblCedulaClienteCrear.setText(clienteSeleccionado.getCedRuc());
        lblEmailClienteCrear.setText(clienteSeleccionado.getEmail() != null ? clienteSeleccionado.getEmail() : "");
        lblTelefonoClienteCrear.setText(clienteSeleccionado.getTelefono() != null ? clienteSeleccionado.getTelefono() : "");
        mostrarError(lblErrorCedulaCrear, null);
    } else {
        limpiarDatosCliente();
        mostrarError(lblErrorCedulaCrear, CargadorProperties.obtenerMessages("FC_A_009"));
    }
}

    private void limpiarDatosCliente() {
        lblNombreClienteCrear.setText("");
        lblCedulaClienteCrear.setText("");
        lblEmailClienteCrear.setText("");
        lblTelefonoClienteCrear.setText("");
    }

       private JPanel crearPanelModificar() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 0, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Inicializar componentes
        txtCedulaMod = new JTextField();
        txtCedulaMod.setPreferredSize(new Dimension(200, 25));

        cmbProductoMod = new JComboBox<>();
        cmbProductoMod.setPreferredSize(new Dimension(400, 25));

        txtCantidadMod = new JTextField();
        txtCantidadMod.setPreferredSize(new Dimension(80, 25));

        txtSubtotalMod = new JTextField("0.00");
        txtSubtotalMod.setEditable(false);
        txtSubtotalMod.setPreferredSize(new Dimension(120, 25));

        txtIVAMod = new JTextField("0.00");
        txtIVAMod.setEditable(false);
        txtIVAMod.setPreferredSize(new Dimension(120, 25));

        txtTotalMod = new JTextField("0.00");
        txtTotalMod.setEditable(false);
        txtTotalMod.setFont(new Font("Arial", Font.BOLD, 12));
        txtTotalMod.setPreferredSize(new Dimension(120, 25));

        int fila = 0;

        // ==================== TÍTULO ====================
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.gridwidth = 8;
        gbc.anchor = GridBagConstraints.CENTER;
        JLabel lblTitulo = new JLabel("Modificar Factura");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 32));
        panel.add(lblTitulo, gbc);
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;

        // ==================== BÚSQUEDA CLIENTE ====================
        fila++;
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.insets = new Insets(20, 10, 5, 10);
        JLabel lblCed = new JLabel("Cédula Cliente:");
        lblCed.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(lblCed, gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        panel.add(txtCedulaMod, gbc);

        gbc.gridx = 3;
        gbc.gridwidth = 1;
        JButton btnBuscar = new JButton("🔍 Buscar");
        btnBuscar.addActionListener(e -> buscarFacturasCliente(txtCedulaMod, modeloFacturasMod));
        panel.add(btnBuscar, gbc);
        gbc.gridwidth = 1;
        gbc.insets = new Insets(5, 10, 0, 10);

        // ==================== TABLA FACTURAS ====================
        fila++;
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.gridwidth = 8;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 0.3;
        gbc.insets = new Insets(10, 10, 10, 10);

        String[] colsFacturas = {"Código", "Fecha y Hora", "Nombre Cliente"};
        modeloFacturasMod = new DefaultTableModel(colsFacturas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tablaFacturasMod = new JTable(modeloFacturasMod);
        tablaFacturasMod.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                seleccionarFacturaModificar();
            }
        });
        JScrollPane scrollFacturas = new JScrollPane(tablaFacturasMod);
        scrollFacturas.setPreferredSize(new Dimension(900, 120));
        panel.add(scrollFacturas, gbc);

        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(5, 10, 0, 10);

        // ==================== SEPARADOR ====================
        fila++;
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.gridwidth = 8;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(15, 10, 15, 10);
        panel.add(new JSeparator(), gbc);
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(5, 10, 0, 10);

        // ==================== CABECERA FACTURA (OCULTA INICIALMENTE) ====================
        fila++;
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.gridwidth = 8;

        JPanel cabecera = crearCabeceraMod();  // ← AQUÍ SE USA
        cabecera.setVisible(false);
        panel.add(cabecera, gbc);
        gbc.gridwidth = 1;

        // ==================== TÍTULO PRODUCTOS ====================
        fila++;
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.gridwidth = 8;
        JLabel lblTitProd = new JLabel("Productos de la factura");
        lblTitProd.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(lblTitProd, gbc);
        gbc.gridwidth = 1;

        // ==================== SELECCIÓN PRODUCTO ====================
        fila++;
        gbc.gridx = 0;
        gbc.gridy = fila;
        JLabel lblProd = new JLabel("Producto:");
        lblProd.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(lblProd, gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 4;
        panel.add(cmbProductoMod, gbc);
        gbc.gridwidth = 1;

        gbc.gridx = 5;
        JLabel lblCant = new JLabel("Cantidad:");
        lblCant.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(lblCant, gbc);

        gbc.gridx = 6;
        panel.add(txtCantidadMod, gbc);

        gbc.gridx = 7;
        JButton btnAgregar = new JButton("Agregar");
        btnAgregar.setPreferredSize(new Dimension(90, 25));
        btnAgregar.addActionListener(e -> agregarProductoMod());
        panel.add(btnAgregar, gbc);

        // ==================== TABLA PRODUCTOS ====================
        fila++;
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.gridwidth = 8;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 0.7;
        gbc.insets = new Insets(10, 10, 10, 10);

        String[] colsProductos = {"Código", "Descripción", "Cantidad", "Precio Unitario", "Subtotal"};
        modeloProductosMod = new DefaultTableModel(colsProductos, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tablaProductosMod = new JTable(modeloProductosMod);
        JScrollPane scrollProductos = new JScrollPane(tablaProductosMod);
        scrollProductos.setPreferredSize(new Dimension(900, 200));
        panel.add(scrollProductos, gbc);

        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(5, 10, 0, 10);

        // Botón quitar
        fila++;
        gbc.gridx = 7;
        gbc.gridy = fila;
        gbc.anchor = GridBagConstraints.EAST;
        JButton btnQuitar = new JButton("Quitar");
        btnQuitar.setPreferredSize(new Dimension(90, 25));
        btnQuitar.addActionListener(e -> quitarProductoMod());
        panel.add(btnQuitar, gbc);

        // ==================== TOTALES ====================
        fila++;
        gbc.gridx = 6;
        gbc.gridy = fila;
        JLabel lblSub = new JLabel("Subtotal:");
        lblSub.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(lblSub, gbc);

        gbc.gridx = 7;
        panel.add(txtSubtotalMod, gbc);

        fila++;
        gbc.gridx = 6;
        gbc.gridy = fila;
        JLabel lblIva = new JLabel("IVA (15%):");
        lblIva.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(lblIva, gbc);

        gbc.gridx = 7;
        panel.add(txtIVAMod, gbc);

        fila++;
        gbc.gridx = 6;
        gbc.gridy = fila;
        JLabel lblTot = new JLabel("Total:");
        lblTot.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(lblTot, gbc);

        gbc.gridx = 7;
        panel.add(txtTotalMod, gbc);

        // Botón guardar
        fila++;
        gbc.gridx = 7;
        gbc.gridy = fila;
        gbc.insets = new Insets(15, 10, 10, 10);
        btnGuardarMod = new JButton("Guardar Cambios");
        btnGuardarMod.setPreferredSize(new Dimension(140, 30));
        btnGuardarMod.setFont(new Font("Arial", Font.BOLD, 12));
        btnGuardarMod.setEnabled(false);
        btnGuardarMod.addActionListener(e -> modificarFactura());
        panel.add(btnGuardarMod, gbc);

        // Scroll
        JPanel panelConScroll = new JPanel(new BorderLayout());
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(null);
        panelConScroll.add(scrollPane, BorderLayout.CENTER);

        return panelConScroll;
    }
    
    // ==================== PANEL ELIMINAR ====================
    private JPanel crearPanelEliminar() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 0, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        txtCedulaElim = new JTextField();
        txtCedulaElim.setPreferredSize(new Dimension(320, 25));
        
        int fila = 0;
        
        gbc.gridx = 0;
        gbc.gridy = fila;
        panel.add(new JLabel("Cédula Cliente:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridwidth = 3;
        JPanel panelCedula = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        panelCedula.add(txtCedulaElim);
        JLabel lblLupa = new JLabel("🔍");
        lblLupa.setFont(new Font("Arial", Font.PLAIN, 16));
        lblLupa.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lblLupa.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                buscarFacturasCliente(txtCedulaElim, modeloFacturasElim);
            }
        });
        panelCedula.add(lblLupa);
        panel.add(panelCedula, gbc);
        gbc.gridwidth = 1;
        
        fila++;
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.gridwidth = 4;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(10, 10, 10, 10);
        
        String[] cols = {"Código", "Cliente", "Fecha", "Subtotal", "IVA", "Total", "Tipo"};
        modeloFacturasElim = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tablaFacturasElim = new JTable(modeloFacturasElim);
        JScrollPane scroll = new JScrollPane(tablaFacturasElim);
        scroll.setPreferredSize(new Dimension(900, 400));
        panel.add(scroll, gbc);
        
        fila++;
        gbc.gridx = 3;
        gbc.gridy = fila;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.insets = new Insets(15, 10, 10, 10);
        btnEliminar = new JButton("Eliminar");
        btnEliminar.setPreferredSize(new Dimension(100, 28));
        btnEliminar.setEnabled(false);
        btnEliminar.addActionListener(e -> eliminarFactura());
        panel.add(btnEliminar, gbc);
        
        return panel;
    }
    
    // ==================== PANEL CONSULTAR ====================
    private JPanel crearPanelConsultar() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel panelTipo = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        comboTipoConsulta = new JComboBox<>(new String[]{
            "Seleccione tipo de consulta...",
            "Consulta General",
            "Consulta por Cliente"
        });
        comboTipoConsulta.setPreferredSize(new Dimension(250, 30));
        comboTipoConsulta.addActionListener(e -> cambiarTipoConsulta());
        panelTipo.add(comboTipoConsulta);
        panel.add(panelTipo, BorderLayout.NORTH);
        
        JPanel panelCentral = new JPanel(new BorderLayout(5, 5));
        
        panelBusqueda = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        txtCedulaConsulta = new JTextField();
        txtCedulaConsulta.setPreferredSize(new Dimension(250, 25));
        
        panelBusqueda.add(new JLabel("Cédula Cliente:"));
        panelBusqueda.add(txtCedulaConsulta);
        
        JLabel lblLupaBusq = new JLabel("🔍");
        lblLupaBusq.setFont(new Font("Arial", Font.PLAIN, 16));
        lblLupaBusq.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lblLupaBusq.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                consultaPorCliente();
            }
        });
        panelBusqueda.add(lblLupaBusq);
        panelBusqueda.setVisible(false);
        
        panelCentral.add(panelBusqueda, BorderLayout.NORTH);
        
        String[] columnas = {"Código", "Cliente", "Fecha", "Subtotal", "IVA", "Total", "Tipo", "Estado"};
        modeloTablaResultados = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tablaResultados = new JTable(modeloTablaResultados);
        tablaResultados.setRowHeight(25);
        JScrollPane scrollTabla = new JScrollPane(tablaResultados);
        scrollTabla.setVisible(false);
        
        panelCentral.add(scrollTabla, BorderLayout.CENTER);
        panel.add(panelCentral, BorderLayout.CENTER);
        
        return panel;
    }
    
    // ==================== MÉTODOS DE NEGOCIO ====================
    
    private void cambiarPanel() {
        String opcion = (String) comboOpciones.getSelectedItem();
        
        switch (opcion) {
            case "Crear Factura":
                limpiarPanelCrear();
                cardLayout.show(panelContenedor, PANEL_CREAR);
                break;
            case "Modificar Factura":
                limpiarPanelModificar();
                cardLayout.show(panelContenedor, PANEL_MODIFICAR);
                break;
            case "Eliminar Factura":
                limpiarPanelEliminar();
                cardLayout.show(panelContenedor, PANEL_ELIMINAR);
                break;
            case "Consultar Facturas":
                cardLayout.show(panelContenedor, PANEL_CONSULTAR);
                break;
            default:
                cardLayout.show(panelContenedor, PANEL_VACIO);
                break;
        }
    }
    
    private void volverAlMenu() {
        comboOpciones.setSelectedIndex(0);
        cardLayout.show(panelContenedor, PANEL_VACIO);
    }
    
  
    private void agregarProducto() {
        if (clienteSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Debe buscar un cliente primero", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        ItemProducto item = (ItemProducto) cmbProductoCrear.getSelectedItem();
        if (item == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String cantidadStr = txtCantidadCrear.getText().trim();
        if (cantidadStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese la cantidad", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int cantidad;
        try {
            cantidad = Integer.parseInt(cantidadStr);
            if (cantidad <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Cantidad inválida", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Producto prod = item.producto;
        
        ProxFac pxf = new ProxFac();
        pxf.setCodigoProd(prod.getCodigo());
        pxf.setCantidad(cantidad);
        pxf.setPrecioVenta(prod.getPrecioVenta());
        pxf.calcularSubtotal();
        
        productosFactura.add(pxf);
        
        modeloProductosCrear.addRow(new Object[]{
            prod.getCodigo(),
            prod.getDescripcion(),
            cantidad,
            String.format("%.2f", prod.getPrecioVenta()),
            String.format("%.2f", pxf.getSubtotalProducto())
        });
        
        calcularTotales(txtSubtotalCrear, txtIVACrear, txtTotalCrear);
        txtCantidadCrear.setText("");
    }
    
    private void quitarProducto(JTable tabla, DefaultTableModel modelo) {
        int fila = tabla.getSelectedRow();
        
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        productosFactura.remove(fila);
        modelo.removeRow(fila);
        calcularTotales(txtSubtotalCrear, txtIVACrear, txtTotalCrear);
    }
    
    private void calcularTotales(JTextField txtSub, JTextField txtIva, JTextField txtTot) {
        double subtotal = 0.0;
        for (ProxFac pxf : productosFactura) {
            subtotal += pxf.getSubtotalProducto();
        }
        
        double iva = Math.round(subtotal * 0.15 * 100.0) / 100.0;
        double total = subtotal + iva;
        
        txtSub.setText(String.format("%.2f", subtotal));
        txtIva.setText(String.format("%.2f", iva));
        txtTot.setText(String.format("%.2f", total));
    }
    
    private void crearFactura() {
        if (clienteSeleccionado == null) {
            JOptionPane.showMessageDialog(this, CargadorProperties.obtenerMessages("FC_A_009"));
            return;
        }

        if (productosFactura.isEmpty()) {
            JOptionPane.showMessageDialog(this, CargadorProperties.obtenerMessages("FC_A_005"));
            return;
        }

        Factura fac = new Factura();
        fac.setCedRucCliente(clienteSeleccionado.getIdCliente());  // ← Pasar id_cliente (ej: "CLI0001")
        fac.setNombreEmpresa("KoKo Market");
        fac.setEmailEmpresa("ventas@kokomarket.com.ec");
        fac.setTelefonoEmpresa("022876543");
        fac.setFechaHora(LocalDateTime.now());

        // Calcular totales
        double subtotal = 0.0;
        for (ProxFac pxf : productosFactura) {
            subtotal += pxf.getSubtotalProducto();
        }
        double iva = Math.round(subtotal * 0.15 * 100.0) / 100.0;
        double total = subtotal + iva;

        fac.setSubtotal(subtotal);
        fac.setIva(iva);
        fac.setTotal(total);
        fac.setTipo("POS");
        fac.setEstado("APR");
        fac.setProductos(productosFactura);

        FacturaMD facMD = new FacturaMD();
        String codigoGenerado = facMD.insertarFacturaCompleta(fac);

        if (codigoGenerado != null) {
            JOptionPane.showMessageDialog(this,
                CargadorProperties.obtenerMessages("FC_I_001") + "\nCódigo: " + codigoGenerado,
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
            limpiarPanelCrear();
        } else {
            JOptionPane.showMessageDialog(this, CargadorProperties.obtenerMessages("FC_E_002"));
        }
    }

    private void buscarFacturasCliente(JTextField txtCed, DefaultTableModel modelo) {
        String cedula = txtCed.getText().trim();

        if (cedula.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese una cédula", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Cliente cli = new Cliente();
        cli.setCedRuc(cedula);

        FacturaMD facMD = new FacturaMD();
        ArrayList<Factura> lista = facMD.consultarPorParametro(cli);

        modelo.setRowCount(0);

        if (lista == null || lista.isEmpty()) {
            JOptionPane.showMessageDialog(this, CargadorProperties.obtenerMessages("FC_A_002"), "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Buscar el cliente UNA VEZ para obtener su nombre
        Cliente clienteBuscado = cli.verificarDP(cedula);
        String nombreCliente = (clienteBuscado != null) ? clienteBuscado.getNombre() : "N/A";

        for (Factura f : lista) {
            modelo.addRow(new Object[]{
                f.getCodigo(),
                f.getFechaHora() != null ? f.getFechaHora().format(FMT_FECHA) : "",
                nombreCliente  // ← Nombre del cliente (del objeto Cliente)
            });
        }
    }

    private void seleccionarFacturaModificar() {
        int fila = tablaFacturasMod.getSelectedRow();
        if (fila == -1) return;

        String codigo = (String) modeloFacturasMod.getValueAt(fila, 0);

        // 1. Buscar la factura completa
        FacturaMD facMD = new FacturaMD();
        ArrayList<Factura> todas = facMD.consultarTodos();

        for (Factura f : todas) {
            if (f.getCodigo().equals(codigo)) {
                facturaSeleccionada = f;

                // 2. Buscar el cliente para obtener sus datos
                Cliente cli = new Cliente();
                Cliente clienteCompleto = cli.verificarDP(txtCedulaMod.getText().trim());

                // 3. MOSTRAR LA CABECERA
                panelCabeceraMod.setVisible(true);

                // 4. LLENAR DATOS DE LA CABECERA
                lblCodigoMod.setText("Código: " + f.getCodigo());
                lblFechaMod.setText("Fecha y hora: " + f.getFechaHora().format(FMT_FECHA));

                if (clienteCompleto != null) {
                    lblNombreClienteMod.setText(clienteCompleto.getNombre());
                    lblCedulaClienteMod.setText(clienteCompleto.getCedRuc());
                    lblEmailClienteMod.setText(clienteCompleto.getEmail() != null ? clienteCompleto.getEmail() : "");
                    lblTelefonoClienteMod.setText(clienteCompleto.getTelefono() != null ? clienteCompleto.getTelefono() : "");
                }

                // 5. CARGAR PRODUCTOS DE LA FACTURA
                cargarProductosFactura(f.getCodigo());

                // 6. CALCULAR Y MOSTRAR TOTALES
                txtSubtotalMod.setText(String.format("%.2f", f.getSubtotal()));
                txtIVAMod.setText(String.format("%.2f", f.getIva()));
                txtTotalMod.setText(String.format("%.2f", f.getTotal()));

                // 7. HABILITAR CONTROLES
                cmbProductoMod.setEnabled(true);
                txtCantidadMod.setEnabled(true);
                btnGuardarMod.setEnabled(true);
                cargarProductosActivos(cmbProductoMod);

                break;
            }
        }
    }
    
    private void cargarProductosFactura(String codigoFactura) {
        // Limpiar tabla
        modeloProductosMod.setRowCount(0);
        productosFactura.clear();

        // Consultar productos de la factura
        ProxFac pxfConsulta = new ProxFac();
        pxfConsulta.setCodigoFac(codigoFactura);

        ArrayList<ProxFac> productos = pxfConsulta.consultarPorFactura(pxfConsulta);

        if (productos != null && !productos.isEmpty()) {
            for (ProxFac pxf : productos) {
                // Buscar el producto para obtener descripción
                Producto prod = new Producto();
                Producto prodCompleto = prod.verificarPorCodigoDP(pxf.getCodigoProd());  // ← CAMBIO AQUÍ

                String descripcion = (prodCompleto != null) ? prodCompleto.getDescripcion() : "N/A";

                // Agregar a la tabla
                modeloProductosMod.addRow(new Object[]{
                    pxf.getCodigoProd(),
                    descripcion,
                    pxf.getCantidad(),
                    String.format("%.2f", pxf.getPrecioVenta()),
                    String.format("%.2f", pxf.getSubtotalProducto())
                });

                // Agregar al ArrayList
                productosFactura.add(pxf);
            }
        }
    }
    
    private void agregarProductoMod() {
        ItemProducto item = (ItemProducto) cmbProductoMod.getSelectedItem();
        if (item == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String cantidadStr = txtCantidadMod.getText().trim();
        if (cantidadStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese la cantidad", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int cantidad;
        try {
            cantidad = Integer.parseInt(cantidadStr);
            if (cantidad <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Cantidad inválida", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Producto prod = item.producto;

        ProxFac pxf = new ProxFac();
        pxf.setCodigoFac(facturaSeleccionada.getCodigo());
        pxf.setCodigoProd(prod.getCodigo());
        pxf.setCantidad(cantidad);
        pxf.setPrecioVenta(prod.getPrecioVenta());
        pxf.calcularSubtotal();
        pxf.setEstado("APR");

        productosFactura.add(pxf);

        modeloProductosMod.addRow(new Object[]{
            prod.getCodigo(),
            prod.getDescripcion(),
            cantidad,
            String.format("%.2f", prod.getPrecioVenta()),
            String.format("%.2f", pxf.getSubtotalProducto())
        });

        calcularTotales(txtSubtotalMod, txtIVAMod, txtTotalMod);
        txtCantidadMod.setText("");
    }
    
    private void quitarProductoMod() {
        int fila = tablaProductosMod.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        productosFactura.remove(fila);
        modeloProductosMod.removeRow(fila);
        calcularTotales(txtSubtotalMod, txtIVAMod, txtTotalMod);
    }
    
    private void modificarFactura() {
        if (facturaSeleccionada == null) {
            JOptionPane.showMessageDialog(this, "Seleccione una factura", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        facturaSeleccionada.setTipo((String) cmbTipoMod.getSelectedItem());
        
        FacturaMD facMD = new FacturaMD();
        if (facMD.modificar(facturaSeleccionada)) {
            JOptionPane.showMessageDialog(this, CargadorProperties.obtenerMessages("FC_I_002"), "Éxito", JOptionPane.INFORMATION_MESSAGE);
            limpiarPanelModificar();
        } else {
            JOptionPane.showMessageDialog(this, CargadorProperties.obtenerMessages("FC_E_004"), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void eliminarFactura() {
        int fila = tablaFacturasElim.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una factura", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String codigo = (String) modeloFacturasElim.getValueAt(fila, 0);
        
        int conf = JOptionPane.showConfirmDialog(this, "¿Eliminar factura " + codigo + "?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (conf != JOptionPane.YES_OPTION) return;
        
        Factura fac = new Factura();
        fac.setCodigo(codigo);
        
        FacturaMD facMD = new FacturaMD();
        if (facMD.eliminar(fac)) {
            JOptionPane.showMessageDialog(this, CargadorProperties.obtenerMessages("FC_I_003"), "Éxito", JOptionPane.INFORMATION_MESSAGE);
            buscarFacturasCliente(txtCedulaElim, modeloFacturasElim);
        } else {
            JOptionPane.showMessageDialog(this, CargadorProperties.obtenerMessages("FC_E_005"), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void cambiarTipoConsulta() {
        String tipo = (String) comboTipoConsulta.getSelectedItem();
        
        if ("Consulta General".equals(tipo)) {
            panelBusqueda.setVisible(false);
            consultarTodos();
        } else if ("Consulta por Cliente".equals(tipo)) {
            panelBusqueda.setVisible(true);
            modeloTablaResultados.setRowCount(0);
        } else {
            panelBusqueda.setVisible(false);
            modeloTablaResultados.setRowCount(0);
        }
    }
    
    private void consultarTodos() {
        FacturaMD facMD = new FacturaMD();
        ArrayList<Factura> lista = facMD.consultarTodos();
        
        modeloTablaResultados.setRowCount(0);
        
        for (Factura f : lista) {
            modeloTablaResultados.addRow(new Object[]{
                f.getCodigo(),
                f.getCedRucCliente(),
                f.getFechaHora() != null ? f.getFechaHora().format(FMT_FECHA) : "",
                String.format("%.2f", f.getSubtotal()),
                String.format("%.2f", f.getIva()),
                String.format("%.2f", f.getTotal()),
                f.getTipo(),
                f.getEstado()
            });
        }
    }
    
    private void consultaPorCliente() {
        String cedula = txtCedulaConsulta.getText().trim();
        
        if (cedula.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese una cédula", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Cliente cli = new Cliente();
        cli.setCedRuc(cedula);
        
        FacturaMD facMD = new FacturaMD();
        ArrayList<Factura> lista = facMD.consultarPorParametro(cli);
        
        modeloTablaResultados.setRowCount(0);
        
        for (Factura f : lista) {
            modeloTablaResultados.addRow(new Object[]{
                f.getCodigo(),
                f.getCedRucCliente(),
                f.getFechaHora() != null ? f.getFechaHora().format(FMT_FECHA) : "",
                String.format("%.2f", f.getSubtotal()),
                String.format("%.2f", f.getIva()),
                String.format("%.2f", f.getTotal()),
                f.getTipo(),
                f.getEstado()
            });
        }
    }
    
    private void cargarProductosActivos(JComboBox<ItemProducto> combo) {
        combo.removeAllItems();
        
        Producto prod = new Producto();
        ArrayList<Producto> lista = prod.consultarTodos();
        
        if (lista != null) {
            for (Producto p : lista) {
                if ("ACT".equalsIgnoreCase(p.getEstado())) {
                    combo.addItem(new ItemProducto(p));
                }
            }
        }
    }
    
    private JPanel crearCabeceraMod() {
        panelCabeceraMod = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 0, 10);

        int fila = 0;

        // ==================== COLUMNA IZQUIERDA: Código y Fecha ====================
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.insets = new Insets(10, 10, 0, 10);

        JPanel panelIzq = new JPanel();
        panelIzq.setLayout(new BoxLayout(panelIzq, BoxLayout.Y_AXIS));

        lblCodigoMod = new JLabel("Código: ");
        lblCodigoMod.setFont(new Font("Arial", Font.BOLD, 14));
        lblCodigoMod.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelIzq.add(lblCodigoMod);
        panelIzq.add(Box.createVerticalStrut(8));

        JLabel lblFechaTit = new JLabel("Fecha y hora:");
        lblFechaTit.setFont(new Font("Arial", Font.BOLD, 13));
        lblFechaTit.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelIzq.add(lblFechaTit);

        lblFechaMod = new JLabel("");
        lblFechaMod.setFont(new Font("Arial", Font.PLAIN, 13));
        lblFechaMod.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelIzq.add(lblFechaMod);

        panelCabeceraMod.add(panelIzq, gbc);
        gbc.gridwidth = 1;

        // ==================== COLUMNA CENTRO: Información Empresa (CENTRADA) ====================
        gbc.gridx = 0;  // ← DESDE EL INICIO
        gbc.gridy = fila;
        gbc.gridwidth = 8;  // ← TODO EL ANCHO
        gbc.anchor = GridBagConstraints.CENTER;  // ← CENTRADO
        gbc.insets = new Insets(10, 10, 0, 10);

        JPanel panelEmpresa = new JPanel();
        panelEmpresa.setLayout(new BoxLayout(panelEmpresa, BoxLayout.Y_AXIS));

        JLabel lblEmpNom = new JLabel("Koko Market");
        lblEmpNom.setFont(new Font("Arial", Font.BOLD, 16));
        lblEmpNom.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelEmpresa.add(lblEmpNom);
        panelEmpresa.add(Box.createVerticalStrut(3));

        JLabel lblEmpEmail = new JLabel("Email: ventas@kokomarket.com");
        lblEmpEmail.setFont(new Font("Arial", Font.PLAIN, 14));
        lblEmpEmail.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelEmpresa.add(lblEmpEmail);
        panelEmpresa.add(Box.createVerticalStrut(3));

        JLabel lblEmpTel = new JLabel("Telefono: 022876543");
        lblEmpTel.setFont(new Font("Arial", Font.PLAIN, 14));
        lblEmpTel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelEmpresa.add(lblEmpTel);

        panelCabeceraMod.add(panelEmpresa, gbc);
        gbc.gridwidth = 1;

        // ==================== COLUMNA DERECHA: Información Cliente ====================
        gbc.gridx = 6;
        gbc.gridy = fila; // ← MISMA FILA
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        gbc.insets = new Insets(10, 10, 0, 10);

        JPanel panelCliente = new JPanel(new GridBagLayout());
        GridBagConstraints gbcCli = new GridBagConstraints();
        gbcCli.anchor = GridBagConstraints.WEST;
        gbcCli.insets = new Insets(2, 5, 2, 5);

        int filaC = 0;

        // Título
        gbcCli.gridx = 0;
        gbcCli.gridy = filaC;
        gbcCli.gridwidth = 2;
        JLabel lblTitCliente = new JLabel("Información del cliente");
        lblTitCliente.setFont(new Font("Arial", Font.BOLD, 16));
        panelCliente.add(lblTitCliente, gbcCli);
        gbcCli.gridwidth = 1;

        // Nombre
        filaC++;
        gbcCli.gridx = 0;
        gbcCli.gridy = filaC;
        JLabel lblNomTit = new JLabel("Nombre:");
        lblNomTit.setFont(new Font("Arial", Font.BOLD, 12));
        panelCliente.add(lblNomTit, gbcCli);

        gbcCli.gridx = 1;
        lblNombreClienteMod = new JLabel("");
        lblNombreClienteMod.setFont(new Font("Arial", Font.PLAIN, 12));
        panelCliente.add(lblNombreClienteMod, gbcCli);

        // Cédula
        filaC++;
        gbcCli.gridx = 0;
        gbcCli.gridy = filaC;
        JLabel lblCedTit = new JLabel("Cédula:");
        lblCedTit.setFont(new Font("Arial", Font.BOLD, 12));
        panelCliente.add(lblCedTit, gbcCli);

        gbcCli.gridx = 1;
        lblCedulaClienteMod = new JLabel("");
        lblCedulaClienteMod.setFont(new Font("Arial", Font.PLAIN, 12));
        panelCliente.add(lblCedulaClienteMod, gbcCli);

        // Email
        filaC++;
        gbcCli.gridx = 0;
        gbcCli.gridy = filaC;
        JLabel lblEmailTit = new JLabel("Email:");
        lblEmailTit.setFont(new Font("Arial", Font.BOLD, 12));
        panelCliente.add(lblEmailTit, gbcCli);

        gbcCli.gridx = 1;
        lblEmailClienteMod = new JLabel("");
        lblEmailClienteMod.setFont(new Font("Arial", Font.PLAIN, 12));
        panelCliente.add(lblEmailClienteMod, gbcCli);

        // Teléfono
        filaC++;
        gbcCli.gridx = 0;
        gbcCli.gridy = filaC;
        JLabel lblTelTit = new JLabel("Telefono:");
        lblTelTit.setFont(new Font("Arial", Font.BOLD, 12));
        panelCliente.add(lblTelTit, gbcCli);

        gbcCli.gridx = 1;
        lblTelefonoClienteMod = new JLabel("");
        lblTelefonoClienteMod.setFont(new Font("Arial", Font.PLAIN, 12));
        panelCliente.add(lblTelefonoClienteMod, gbcCli);

        panelCabeceraMod.add(panelCliente, gbc);

        return panelCabeceraMod;
    }
    
private void limpiarPanelCrear() {
    // Limpiar campos de entrada
    txtCedulaCrear.setText("");
    txtCantidadCrear.setText("");
    
    // Limpiar datos del cliente
    lblNombreClienteCrear.setText("");
    lblCedulaClienteCrear.setText("");
    lblEmailClienteCrear.setText("");
    lblTelefonoClienteCrear.setText("");
    
    // Limpiar errores
    lblErrorCedulaCrear.setText(" ");
    lblErrorCantidadCrear.setText(" ");
    
    // Limpiar tabla
    modeloProductosCrear.setRowCount(0);
    
    // Resetear totales
    txtSubtotalCrear.setText("0.00");
    txtIVACrear.setText("0.00");
    txtTotalCrear.setText("0.00");
    
    // Limpiar variables
    clienteSeleccionado = null;
    if (productosFactura != null) {
        productosFactura.clear();
    }
}
    
    private void limpiarPanelModificar() {
    txtCedulaMod.setText("");
    modeloFacturasMod.setRowCount(0);
    modeloProductosMod.setRowCount(0);
    
    // Ocultar cabecera
    if (panelCabeceraMod != null) {
        panelCabeceraMod.setVisible(false);
    }
    
    // Limpiar labels de cabecera
    if (lblCodigoMod != null) lblCodigoMod.setText("Código: ");
    if (lblFechaMod != null) lblFechaMod.setText("Fecha y hora: ");
    if (lblNombreClienteMod != null) lblNombreClienteMod.setText("");
    if (lblCedulaClienteMod != null) lblCedulaClienteMod.setText("");
    if (lblEmailClienteMod != null) lblEmailClienteMod.setText("");
    if (lblTelefonoClienteMod != null) lblTelefonoClienteMod.setText("");
    
    txtSubtotalMod.setText("0.00");
    txtIVAMod.setText("0.00");
    txtTotalMod.setText("0.00");
    
    // ← QUITÉ: cmbTipoMod.setSelectedIndex(0);
    // ← QUITÉ: cmbTipoMod.setEnabled(false);
    
    cmbProductoMod.setEnabled(false);
    txtCantidadMod.setEnabled(false);
    btnGuardarMod.setEnabled(false);
    
    facturaSeleccionada = null;
    productosFactura.clear();
}
    
    private void limpiarPanelEliminar() {
        txtCedulaElim.setText("");
        modeloFacturasElim.setRowCount(0);
        btnEliminar.setEnabled(false);
    }
    
    private static class ItemProducto {
        Producto producto;
        
        ItemProducto(Producto p) {
            this.producto = p;
        }
        
        @Override
        public String toString() {
            return producto.getCodigo() + " - " + producto.getDescripcion();
        }
    }
    
    private void generarYMostrarCodigo() {
        FacturaMD facMD = new FacturaMD();
        String codigo = facMD.generarCodigoFactura();

        if (codigo != null) {
            lblCodigoGenerado.setText("Código: " + codigo);
        } else {
            lblCodigoGenerado.setText("Código: [Error]");
        }
    }
}