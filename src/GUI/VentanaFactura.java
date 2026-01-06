package GUI;

import GUI.MenuPrincipal;
import DP.Cliente;
import DP.Factura;
import DP.ProxFac;
import DP.Producto;
import util.CargadorProperties;
import util.ValidacionesFactura;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.DocumentEvent;       
import java.time.LocalDateTime;         
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

public class VentanaFactura extends JFrame {
    
    //**CONSTANTES**
    private static final String PANEL_VACIO = CargadorProperties.obtenerComponentes("PANEL_VACIO");
    private static final String PANEL_CREAR = CargadorProperties.obtenerComponentes("PANEL_CREAR");
    private static final String PANEL_MODIFICAR = CargadorProperties.obtenerComponentes("PANEL_MODIFICAR");
    private static final String PANEL_ELIMINAR = CargadorProperties.obtenerComponentes("PANEL_ELIMINAR");
    private static final String PANEL_CONSULTAR = CargadorProperties.obtenerComponentes("PANEL_CONSULTAR");
    private static final DateTimeFormatter FMT_FECHA = DateTimeFormatter.ofPattern( CargadorProperties.obtenerComponentes("FORMATO_FECHA"));
    
    //**VARIABLES CARDLAYOUT**
    private CardLayout cardLayout;
    private JPanel panelContenedor;
    private JComboBox<String> comboOpciones;
    
    //**VARIABLES PANEL CREAR**
    private JLabel lblCodigoGenerado;
    private JTextField txtCedulaCrear, txtCantidadCrear;
    private JLabel lblNombreClienteCrear, lblCedulaClienteCrear, lblEmailClienteCrear, lblTelefonoClienteCrear;
    private JLabel lblErrorCedulaCrear, lblErrorCantidadCrear;
    private JComboBox<ItemProducto> cmbProductoCrear;
    private JTable tablaProductosCrear;
    private DefaultTableModel modeloProductosCrear;
    private JTextField txtSubtotalCrear, txtIVACrear, txtTotalCrear;
    
    //**VARIABLES PANEL MODIFICAR**
    private JTextField txtCedulaMod;
    private JTable tablaFacturasMod;
    private JLabel lblErrorCedulaMod; 
    private DefaultTableModel modeloFacturasMod;
    private JTable tablaProductosMod;
    private DefaultTableModel modeloProductosMod;
    private JComboBox<ItemProducto> cmbProductoMod;
    private JTextField txtCantidadMod, txtSubtotalMod, txtIVAMod, txtTotalMod;
    private JButton btnGuardarMod;
    private Factura facturaSeleccionada;
    private JLabel lblCodigoMod, lblFechaMod;
    private JLabel lblNombreClienteMod, lblCedulaClienteMod, lblEmailClienteMod, lblTelefonoClienteMod, lblErrorCantidadMod;
    private JPanel panelCabeceraMod;
    
    //**VARIABLES PANEL ELIMINAR**
    private JTextField txtCedulaElim;
    private JTable tablaFacturasElim;
    private DefaultTableModel modeloFacturasElim;
    private JButton btnEliminar;
    
    //**VARIABLES PANEL CONSULTAR**
    private JComboBox<String> comboTipoConsulta;
    private JTextField txtCedulaConsulta;
    private JLabel lblErrorCedulaConsulta; 
    private JTable tablaResultados;
    private DefaultTableModel modeloTablaResultados;
    private JPanel panelBusqueda;

    // NUEVAS para consulta con detalle
    private JPanel panelDetalleConsulta;
    private JLabel lblCodigoConsulta, lblFechaConsulta;
    private JLabel lblNombreClienteConsulta, lblCedulaClienteConsulta;
    private JLabel lblEmailClienteConsulta, lblTelefonoClienteConsulta;
    private JTable tablaProductosConsulta;
    private DefaultTableModel modeloProductosConsulta;
    private JTextField txtSubtotalConsulta, txtIVAConsulta, txtTotalConsulta;

    //**VARIABLES DATOS**
    private Cliente clienteSeleccionado;
    private ArrayList<ProxFac> productosFactura;
    
    //**CONSTRUCTOR**
    public VentanaFactura() {
        productosFactura = new ArrayList<>();
        configurarVentana();
        inicializarComponentes();
        configurarLayout();
    }
    
    //**CONFIGURACION INICIAL**
    //Configura propiedades básicas de la ventana
    private void configurarVentana() {
        setTitle(CargadorProperties.obtenerComponentes("TITULO"));
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }
    
    private void inicializarComponentes() {
        cardLayout = new CardLayout();
        panelContenedor = new JPanel(cardLayout);

        String[] opciones = CargadorProperties.obtenerComponentes("CMB_OPERACION").split(",");
        comboOpciones = new JComboBox<>(opciones);

        panelContenedor.add(crearPanelVacio(), PANEL_VACIO);
        panelContenedor.add(crearPanelCrear(), PANEL_CREAR);
        panelContenedor.add(crearPanelModificar(), PANEL_MODIFICAR);
        panelContenedor.add(crearPanelEliminar(), PANEL_ELIMINAR);
        panelContenedor.add(crearPanelConsultar(), PANEL_CONSULTAR);

        comboOpciones.addActionListener(e -> cambiarPanel());
    }
    
    //Configura el layout principal de la ventana
    private void configurarLayout() {
        setLayout(new BorderLayout(10, 10));
        
        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        panelSuperior.add(comboOpciones);
        add(panelSuperior, BorderLayout.NORTH);
        
        add(panelContenedor, BorderLayout.CENTER);
        
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        JButton btnVolver = new JButton(CargadorProperties.obtenerComponentes("FC_UI_007"));
        btnVolver.addActionListener(e -> volverAlMenu());
        panelInferior.add(btnVolver);
        add(panelInferior, BorderLayout.SOUTH);
    }    
    
    //Panel vacío
    private JPanel crearPanelVacio() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        JLabel lblTitulo = crearTituloCentrado("TITULO_PANEL_VACIO");
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(lblTitulo, gbc);
        
        return panel;
    }
    
    //**CREACIÓN DE PANELES**    
    //Panel Ingresar
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
        cmbProductoCrear.setEnabled(false); 

        txtCantidadCrear = new JTextField();
        txtCantidadCrear.setPreferredSize(new Dimension(80, 25));

        txtSubtotalCrear = crearCampoTotal(false);
        txtIVACrear = crearCampoTotal(false);
        txtTotalCrear = crearCampoTotal(true);

        lblErrorCedulaCrear = crearLabelError();
        lblErrorCantidadCrear = crearLabelError();

        lblCodigoGenerado = new JLabel(CargadorProperties.obtenerComponentes("LBL_CODIGO") + " " + 
                                       CargadorProperties.obtenerComponentes("CODIGO_GENERANDO"));
        lblCodigoGenerado.setFont(new Font("Arial", Font.BOLD, 12));

        generarYMostrarCodigo();
        configurarValidacionesCrear();

        int fila = 0;

        // Título
        gbc.gridx = 0;
        gbc.gridy = fila++;
        gbc.gridwidth = 8;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 10, 15, 10);
        panel.add(crearTituloCentrado("TITULO_CREAR_FACTURA"), gbc);
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 10, 0, 10);

        // Cabecera completa (Código/Fecha + Empresa + Cliente)
        gbc.gridx = 0;
        gbc.gridy = fila++;
        gbc.gridwidth = 8;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 0, 10);
        panel.add(crearCabeceraFactura(
            lblCodigoGenerado, null,  // lblCodigo, lblFecha (null = genera fecha automática)
            lblNombreClienteCrear, lblCedulaClienteCrear, lblEmailClienteCrear, lblTelefonoClienteCrear,
            true, txtCedulaCrear, lblErrorCedulaCrear, () -> buscarClienteCrear()  // con búsqueda
        ), gbc);
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(5, 10, 0, 10);

        // Separador
        gbc.gridx = 0;
        gbc.gridy = fila++;
        gbc.gridwidth = 8;
        gbc.insets = new Insets(15, 10, 15, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(new JSeparator(), gbc);
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(5, 10, 0, 10);

        // Título productos
        gbc.gridx = 0;
        gbc.gridy = fila++;
        gbc.gridwidth = 8;
        gbc.anchor = GridBagConstraints.WEST;
        JLabel lblTitProd = new JLabel(CargadorProperties.obtenerComponentes("TITULO_PRODUCTOS_FACTURA"));
        lblTitProd.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(lblTitProd, gbc);
        gbc.gridwidth = 1;

        // Fila: Producto | Cantidad | Agregar
        gbc.gridy = fila++;

        gbc.gridx = 0;
        JLabel lblProd = new JLabel(CargadorProperties.obtenerComponentes("LBL_PRODUCTO"));
        lblProd.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(lblProd, gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 4;
        panel.add(cmbProductoCrear, gbc);
        gbc.gridwidth = 1;

        gbc.gridx = 5;
        JLabel lblCant = new JLabel(CargadorProperties.obtenerComponentes("LBL_CANTIDAD"));
        lblCant.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(lblCant, gbc);

        gbc.gridx = 6;
        panel.add(txtCantidadCrear, gbc);

        gbc.gridx = 7;
        JButton btnAgregar = new JButton(CargadorProperties.obtenerComponentes("FC_UI_027"));
        btnAgregar.setPreferredSize(new Dimension(90, 25));
        btnAgregar.addActionListener(e -> agregarProducto(
            cmbProductoCrear,
            txtCantidadCrear,
            lblErrorCantidadCrear,
            modeloProductosCrear,
            txtSubtotalCrear,
            txtIVACrear,
            txtTotalCrear
        ));
        panel.add(btnAgregar, gbc);

        // Error cantidad
        gbc.gridx = 6;
        gbc.gridy = fila++;
        gbc.insets = new Insets(0, 10, 0, 10);
        panel.add(lblErrorCantidadCrear, gbc);
        gbc.insets = new Insets(5, 10, 0, 10);

        // Tabla productos (EDITABLE)
        modeloProductosCrear = crearModeloTablaProductos(true);  // ← USA MÉTODO AUXILIAR
        tablaProductosCrear = new JTable(modeloProductosCrear);
        configurarTablaProductos(modeloProductosCrear, tablaProductosCrear, true, 
                        txtSubtotalCrear, txtIVACrear, txtTotalCrear);

        gbc.gridx = 0;
        gbc.gridy = fila++;
        gbc.gridwidth = 8;
        gbc.fill = GridBagConstraints.BOTH;  
        gbc.weightx = 1.0; 
        gbc.weighty = 1.0; 
        gbc.insets = new Insets(10, 10, 10, 10);

        JScrollPane scrollTabla = new JScrollPane(tablaProductosCrear);
        scrollTabla.setPreferredSize(new Dimension(0, 180)); 
        panel.add(scrollTabla, gbc);

        // Resetear para siguientes componentes
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(5, 10, 0, 10);

        // Botón Quitar
        fila++;
        gbc.gridy = fila;
        gbc.gridx = 7;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.insets = new Insets(5, 10, 10, 10);

        JButton btnQuitar = new JButton(CargadorProperties.obtenerComponentes("FC_UI_028"));
        btnQuitar.setPreferredSize(new Dimension(90, 25));
        btnQuitar.addActionListener(e -> quitarProducto(
            tablaProductosCrear,
            modeloProductosCrear,
            txtSubtotalCrear,
            txtIVACrear,
            txtTotalCrear
        ));
        panel.add(btnQuitar, gbc);

        // Totales
        fila++;
        agregarSeccionTotales(panel, gbc, fila, txtSubtotalCrear, txtIVACrear, txtTotalCrear);
        fila += 3;

        // Botón Guardar
        gbc.gridx = 7;
        gbc.gridy = fila;
        gbc.insets = new Insets(15, 10, 10, 10);
        JButton btnGuardar = new JButton(CargadorProperties.obtenerComponentes("FC_UI_037"));
        btnGuardar.setPreferredSize(new Dimension(140, 30));
        btnGuardar.setFont(new Font("Arial", Font.BOLD, 12));
        btnGuardar.addActionListener(e -> crearFactura());
        panel.add(btnGuardar, gbc);

        cargarProductosActivos(cmbProductoCrear);

        JPanel panelConScroll = new JPanel(new BorderLayout());
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(null);
        panelConScroll.add(scrollPane, BorderLayout.CENTER);

        return panelConScroll;
    }
    //Crea panel para modificar facturas existentes
    private JPanel crearPanelModificar() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 0, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Inicializar componentes MODIFICAR
        txtCedulaMod = new JTextField();
        txtCedulaMod.setPreferredSize(new Dimension(200, 25));

        lblCodigoMod = new JLabel("");
        lblFechaMod = new JLabel("");
        lblNombreClienteMod = new JLabel("");
        lblCedulaClienteMod = new JLabel("");
        lblEmailClienteMod = new JLabel("");
        lblTelefonoClienteMod = new JLabel("");

        cmbProductoMod = new JComboBox<>();
        cmbProductoMod.setPreferredSize(new Dimension(400, 25));

        txtCantidadMod = new JTextField();
        txtCantidadMod.setPreferredSize(new Dimension(80, 25));
        lblErrorCantidadMod = crearLabelError();

        txtSubtotalMod = crearCampoTotal(false);
        txtIVAMod = crearCampoTotal(false);
        txtTotalMod = crearCampoTotal(true);

        int fila = 0;

        // Título
        gbc.gridx = 0;
        gbc.gridy = fila++;
        gbc.gridwidth = 8;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 10, 15, 10);
        panel.add(crearTituloCentrado("TITULO_MODIFICAR_FACTURA"), gbc);
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 10, 0, 10);

        // Buscar cliente por cédula
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.insets = new Insets(20, 10, 5, 10);
        JLabel lblCed = new JLabel(CargadorProperties.obtenerComponentes("LBL_CEDULA"));
        lblCed.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(lblCed, gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        panel.add(txtCedulaMod, gbc);

        gbc.gridx = 3;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(20, 5, 5, 10);
        panel.add(crearBotonLupa(() -> buscarYCargarFacturas(txtCedulaMod, modeloFacturasMod, false)), gbc);
        gbc.insets = new Insets(5, 10, 0, 10);
        
        fila++;
        gbc.gridx = 1;
        gbc.gridy = fila;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 10, 5, 10);
        lblErrorCedulaMod = crearLabelError();  // ← INICIALIZAR
        panel.add(lblErrorCedulaMod, gbc);
        gbc.gridwidth = 1;
        gbc.insets = new Insets(5, 10, 0, 10);
        
        // Tabla de facturas del cliente (3 columnas)
        fila++;
        gbc.gridx = 0;
        gbc.gridy = fila++;
        gbc.gridwidth = 8;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 0.3;
        gbc.insets = new Insets(10, 10, 10, 10);

        modeloFacturasMod = crearModeloTablaFacturas("modificar");  // ← USA MÉTODO AUXILIAR
        tablaFacturasMod = new JTable(modeloFacturasMod);
        tablaFacturasMod.setRowHeight(25);

        // Listener para seleccionar factura
        tablaFacturasMod.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                seleccionarYMostrarFactura(
                    tablaFacturasMod,
                    modeloFacturasMod,
                    modeloProductosMod,
                    panelCabeceraMod,
                    lblCodigoMod,
                    lblFechaMod,
                    lblNombreClienteMod,
                    lblCedulaClienteMod,
                    lblEmailClienteMod,
                    lblTelefonoClienteMod,
                    txtSubtotalMod,
                    txtIVAMod,
                    txtTotalMod,
                    true,  // ← EDITABLE
                    cmbProductoMod,
                    txtCantidadMod,
                    btnGuardarMod
                );
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

        // Separador
        gbc.gridx = 0;
        gbc.gridy = fila++;
        gbc.gridwidth = 8;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(15, 10, 15, 10);
        panel.add(new JSeparator(), gbc);
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(5, 10, 0, 10);

        // Cabecera de la factura seleccionada (oculta inicialmente)
        gbc.gridx = 0;
        gbc.gridy = fila++;
        gbc.gridwidth = 8;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        panelCabeceraMod = crearCabeceraFactura(
            lblCodigoMod, lblFechaMod,
            lblNombreClienteMod, lblCedulaClienteMod, lblEmailClienteMod, lblTelefonoClienteMod,
            false, null, null, null  // sin búsqueda
        );
        panelCabeceraMod.setVisible(false);

        panel.add(panelCabeceraMod, gbc); 

        gbc.fill = GridBagConstraints.NONE;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(5, 10, 0, 10);

        // Título productos
        gbc.gridx = 0;
        gbc.gridy = fila++;
        gbc.gridwidth = 8;
        gbc.anchor = GridBagConstraints.WEST;
        JLabel lblTitProd = new JLabel(CargadorProperties.obtenerComponentes("TITULO_PRODUCTOS_FACTURA"));
        lblTitProd.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(lblTitProd, gbc);
        gbc.gridwidth = 1;

        // Fila: Producto | Cantidad | Agregar
        gbc.gridy = fila++;

        gbc.gridx = 0;
        JLabel lblProd = new JLabel(CargadorProperties.obtenerComponentes("LBL_PRODUCTO"));
        lblProd.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(lblProd, gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 4;
        panel.add(cmbProductoMod, gbc);
        gbc.gridwidth = 1;

        gbc.gridx = 5;
        JLabel lblCant = new JLabel(CargadorProperties.obtenerComponentes("LBL_CANTIDAD"));
        lblCant.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(lblCant, gbc);

        gbc.gridx = 6;
        panel.add(txtCantidadMod, gbc);

        gbc.gridx = 7;
        JButton btnAgregar = new JButton(CargadorProperties.obtenerComponentes("FC_UI_027"));
        btnAgregar.setPreferredSize(new Dimension(90, 25));
        btnAgregar.addActionListener(e -> agregarProducto(
            cmbProductoMod, txtCantidadMod, lblErrorCantidadMod,
            modeloProductosMod, txtSubtotalMod, txtIVAMod, txtTotalMod
        ));
        panel.add(btnAgregar, gbc);

        // Error cantidad
        gbc.gridx = 6;
        gbc.gridy = fila++;
        gbc.insets = new Insets(0, 10, 0, 10);
        panel.add(lblErrorCantidadMod, gbc);
        gbc.insets = new Insets(5, 10, 0, 10);

        // Tabla productos (EDITABLE)
        gbc.gridx = 0;
        gbc.gridy = fila++;
        gbc.gridwidth = 8;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 0.7;
        gbc.insets = new Insets(10, 10, 10, 10);

        modeloProductosMod = crearModeloTablaProductos(true);  // ← USA MÉTODO AUXILIAR
        tablaProductosMod = new JTable(modeloProductosMod);
        configurarTablaProductos(modeloProductosMod, tablaProductosMod, true, 
                                txtSubtotalMod, txtIVAMod, txtTotalMod);

        JScrollPane scrollProductos = new JScrollPane(tablaProductosMod);
        scrollProductos.setPreferredSize(new Dimension(900, 200));
        panel.add(scrollProductos, gbc);

        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(5, 10, 0, 10);

        // Botón Quitar
        gbc.gridx = 7;
        gbc.gridy = fila++;
        gbc.anchor = GridBagConstraints.EAST;
        JButton btnQuitar = new JButton(CargadorProperties.obtenerComponentes("FC_UI_028"));
        btnQuitar.setPreferredSize(new Dimension(90, 25));
        btnQuitar.addActionListener(e -> quitarProducto(
            tablaProductosMod, modeloProductosMod,
            txtSubtotalMod, txtIVAMod, txtTotalMod
        ));
        panel.add(btnQuitar, gbc);

        // Totales
        agregarSeccionTotales(panel, gbc, fila, txtSubtotalMod, txtIVAMod, txtTotalMod);
        fila += 3;

        // Botón Guardar
        gbc.gridx = 7;
        gbc.gridy = fila;
        gbc.insets = new Insets(15, 10, 10, 10);
        btnGuardarMod = new JButton(CargadorProperties.obtenerComponentes("FC_UI_038"));
        btnGuardarMod.setPreferredSize(new Dimension(140, 30));
        btnGuardarMod.setFont(new Font("Arial", Font.BOLD, 12));
        btnGuardarMod.setEnabled(false);
        btnGuardarMod.addActionListener(e -> modificarFactura());
        panel.add(btnGuardarMod, gbc);

        configurarValidacionesMod();

        JPanel panelConScroll = new JPanel(new BorderLayout());
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(null);
        panelConScroll.add(scrollPane, BorderLayout.CENTER);

        return panelConScroll;
    }

    //Crea panel para eliminar facturas
    private JPanel crearPanelEliminar() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 0, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Inicializar componentes
        txtCedulaElim = new JTextField();
        txtCedulaElim.setPreferredSize(new Dimension(200, 25));

        JLabel lblErrorCedulaElim = crearLabelError();

        // Configurar validación en tiempo real usando método auxiliar
        txtCedulaElim.getDocument().addDocumentListener(
            crearValidadorTiempoReal(txtCedulaElim, lblErrorCedulaElim, "cedula")
        );

        int fila = 0;

        // Título
        gbc.gridx = 0;
        gbc.gridy = fila++;
        gbc.gridwidth = 4;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 10, 15, 10);
        panel.add(crearTituloCentrado("TITULO_ELIMINAR_FACTURA"), gbc);
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 10, 0, 10);

        // Fila: Cédula + Lupa
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.insets = new Insets(20, 10, 5, 10);
        JLabel lblCed = new JLabel(CargadorProperties.obtenerComponentes("LBL_CEDULA"));
        lblCed.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(lblCed, gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        panel.add(txtCedulaElim, gbc);

        gbc.gridx = 3;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(20, 5, 5, 10);
        panel.add(crearBotonLupa(() -> buscarYCargarFacturas(txtCedulaElim, modeloFacturasElim, false)), gbc);

        // Error cédula
        fila++;
        gbc.gridx = 1;
        gbc.gridy = fila;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 10, 5, 10);
        panel.add(lblErrorCedulaElim, gbc);
        gbc.gridwidth = 1;
        gbc.insets = new Insets(5, 10, 0, 10);

        // Tabla de facturas (8 columnas - completa)
        fila++;
        gbc.gridx = 0;
        gbc.gridy = fila++;
        gbc.gridwidth = 4;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(10, 10, 10, 10);

        modeloFacturasElim = crearModeloTablaFacturas("completa");  // ← USA MÉTODO AUXILIAR
        tablaFacturasElim = new JTable(modeloFacturasElim);
        tablaFacturasElim.setRowHeight(25);

        // Listener para habilitar botón eliminar
        tablaFacturasElim.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                btnEliminar.setEnabled(tablaFacturasElim.getSelectedRow() != -1);
            }
        });

        JScrollPane scroll = new JScrollPane(tablaFacturasElim);
        scroll.setPreferredSize(new Dimension(900, 400));
        panel.add(scroll, gbc);

        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.gridwidth = 1;

        // Botón Eliminar
        gbc.gridx = 3;
        gbc.gridy = fila;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(15, 10, 10, 10);

        btnEliminar = new JButton(CargadorProperties.obtenerComponentes("BTN_ELIMINAR"));
        btnEliminar.setPreferredSize(new Dimension(140, 30));
        btnEliminar.setFont(new Font("Arial", Font.BOLD, 12));
        btnEliminar.setEnabled(false);  // Deshabilitado hasta seleccionar factura
        btnEliminar.addActionListener(e -> eliminarFactura());
        panel.add(btnEliminar, gbc);
                
        

        return panel;
    }
    
    private JPanel crearPanelConsultar() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel superior: Título + tipo de consulta
        JPanel panelSuperior = new JPanel(new BorderLayout(10, 10));

        // Título centrado
        JPanel panelTitulo = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelTitulo.add(crearTituloCentrado("TITULO_CONSULTAR_FACTURA"));
        panelSuperior.add(panelTitulo, BorderLayout.NORTH);

        // ComboBox de tipo de consulta
        JPanel panelTipo = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        String[] opcionesTipo = CargadorProperties.obtenerComponentes("CMB_TIPO_CONSULTA").split(",");
        comboTipoConsulta = new JComboBox<>(opcionesTipo);
        comboTipoConsulta.setPreferredSize(new Dimension(250, 30));
        comboTipoConsulta.addActionListener(e -> cambiarTipoConsulta());
        panelTipo.add(comboTipoConsulta);
        panelSuperior.add(panelTipo, BorderLayout.CENTER);

        panel.add(panelSuperior, BorderLayout.NORTH);

        // Panel central: búsqueda + tabla facturas + detalle
        JPanel panelCentral = new JPanel(new BorderLayout(5, 5));

        // Panel de búsqueda (inicialmente oculto) - CON GridBagLayout para error debajo
        panelBusqueda = new JPanel(new GridBagLayout());
        GridBagConstraints gbcBusq = new GridBagConstraints();
        gbcBusq.insets = new Insets(5, 5, 0, 5);

        txtCedulaConsulta = new JTextField();
        txtCedulaConsulta.setPreferredSize(new Dimension(250, 25));

        lblErrorCedulaConsulta = crearLabelError();

        JLabel lblCed = new JLabel(CargadorProperties.obtenerComponentes("LBL_CEDULA"));
        lblCed.setFont(new Font("Arial", Font.BOLD, 12));

        // Fila 1: Label + Campo + Lupa
        gbcBusq.gridx = 0;
        gbcBusq.gridy = 0;
        gbcBusq.anchor = GridBagConstraints.EAST;
        panelBusqueda.add(lblCed, gbcBusq);

        gbcBusq.gridx = 1;
        gbcBusq.anchor = GridBagConstraints.WEST;
        panelBusqueda.add(txtCedulaConsulta, gbcBusq);

        gbcBusq.gridx = 2;
        panelBusqueda.add(crearBotonLupa(() -> buscarYCargarFacturas(txtCedulaConsulta, modeloTablaResultados, true)), gbcBusq);

        // Fila 2: Error (centrado debajo del campo)
        gbcBusq.gridx = 1;
        gbcBusq.gridy = 1;
        gbcBusq.insets = new Insets(0, 5, 5, 5);
        gbcBusq.anchor = GridBagConstraints.WEST;
        panelBusqueda.add(lblErrorCedulaConsulta, gbcBusq);

        panelBusqueda.setVisible(false);
        

        panelCentral.add(panelBusqueda, BorderLayout.NORTH);

        // Tabla de facturas (8 columnas - completa)
        modeloTablaResultados = crearModeloTablaFacturas("completa");  // ← USA EL MÉTODO AUXILIAR
        tablaResultados = new JTable(modeloTablaResultados);
        tablaResultados.setRowHeight(25);

        // LISTENER para seleccionar factura y mostrar detalle
        tablaResultados.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                seleccionarYMostrarFactura(
                    tablaResultados,
                    modeloTablaResultados,
                    modeloProductosConsulta,
                    panelDetalleConsulta,
                    lblCodigoConsulta,
                    lblFechaConsulta,
                    lblNombreClienteConsulta,
                    lblCedulaClienteConsulta,
                    lblEmailClienteConsulta,
                    lblTelefonoClienteConsulta,
                    txtSubtotalConsulta,
                    txtIVAConsulta,
                    txtTotalConsulta,
                    false,  // NO editable
                    null, null, null
                );
            }
        });

        JScrollPane scrollTabla = new JScrollPane(tablaResultados);
        scrollTabla.setPreferredSize(new Dimension(900, 200));
        panelCentral.add(scrollTabla, BorderLayout.CENTER);

        // Panel de detalle (inicialmente oculto)
        panelDetalleConsulta = crearPanelDetalleConsulta();
        panelDetalleConsulta.setVisible(false);
        panelCentral.add(panelDetalleConsulta, BorderLayout.SOUTH);

        panel.add(panelCentral, BorderLayout.CENTER);
        
        configurarValidacionesConsultar(); 

        return panel;
    }

    //Crear panel de detalle para consulta
    private JPanel crearPanelDetalleConsulta() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 0, 10);

        // Inicializar labels
        lblCodigoConsulta = new JLabel("");
        lblFechaConsulta = new JLabel("");
        lblNombreClienteConsulta = new JLabel("");
        lblCedulaClienteConsulta = new JLabel("");
        lblEmailClienteConsulta = new JLabel("");
        lblTelefonoClienteConsulta = new JLabel("");

        txtSubtotalConsulta = crearCampoTotal(false);
        txtIVAConsulta = crearCampoTotal(false);
        txtTotalConsulta = crearCampoTotal(true);

        int fila = 0;

        // Separador
        gbc.gridx = 0; gbc.gridy = fila++;
        gbc.gridwidth = 8;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        panel.add(new JSeparator(), gbc);
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(5, 10, 0, 10);

        // Cabecera de la factura seleccionada
        gbc.gridx = 0; gbc.gridy = fila++;
        gbc.gridwidth = 8;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(crearCabeceraFactura(
            lblCodigoConsulta, lblFechaConsulta,
            lblNombreClienteConsulta, lblCedulaClienteConsulta, 
            lblEmailClienteConsulta, lblTelefonoClienteConsulta,
            false, null, null, null  // sin búsqueda
        ), gbc);
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;

        // Título productos
        gbc.gridx = 0; gbc.gridy = fila++;
        gbc.gridwidth = 8;
        JLabel lblTitProd = new JLabel(CargadorProperties.obtenerComponentes("TITULO_PRODUCTOS_FACTURA"));
        lblTitProd.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(lblTitProd, gbc);
        gbc.gridwidth = 1;

        // Tabla productos (NO EDITABLE)
        gbc.gridx = 0; gbc.gridy = fila++;
        gbc.gridwidth = 8;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(10, 10, 10, 10);

        modeloProductosConsulta = crearModeloTablaProductos(false);  // ← USA EL MÉTODO AUXILIAR
        tablaProductosConsulta = new JTable(modeloProductosConsulta);
        configurarTablaProductos(modeloProductosConsulta, tablaProductosConsulta, false, 
                                null, null, null);  // sin totales automáticos

        JScrollPane scrollProductos = new JScrollPane(tablaProductosConsulta);
        scrollProductos.setPreferredSize(new Dimension(900, 150));
        panel.add(scrollProductos, gbc);

        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(5, 10, 0, 10);

        // Totales
        fila++;
        agregarSeccionTotales(panel, gbc, fila, txtSubtotalConsulta, txtIVAConsulta, txtTotalConsulta);

        return panel;
    }

    //**Configuración de tablas**
    private DefaultTableModel crearModeloTablaFacturas(String tipo) {
        String[] columnas;

        if ("modificar".equals(tipo)) {
            // 3 columnas para MODIFICAR
            columnas = CargadorProperties.obtenerComponentes("TABLA_FACTURAS_MOD_COLS").split(",");
        } else {
            // 8 columnas para ELIMINAR y CONSULTAR
            columnas = CargadorProperties.obtenerComponentes("TABLA_FACTURAS_COLS").split(",");
        }

        return new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { 
                return false; 
            }
        };
    }
    
    private void buscarYCargarFacturas(JTextField txtCedula, 
                                       DefaultTableModel modelo, 
                                       boolean incluirAnuladas) {
        String cedula = txtCedula.getText().trim();
        
        // Validación
        if (cedula.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                CargadorProperties.obtenerMessages("FC_A_019"), 
                CargadorProperties.obtenerMessages("FC_C_005"), 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Buscar cliente
        Cliente cli = new Cliente();
        cli.setCedRuc(cedula);
        
        // Buscar facturas (APR o todas)
        Factura fac = new Factura();
        ArrayList<Factura> lista = incluirAnuladas 
            ? fac.consultarTodasPorParametro(cli)  // APR + ANU
            : fac.consultarPorParametro(cli);       // solo APR
        
        // Limpiar tabla
        modelo.setRowCount(0);
        
        // Validar resultados
        if (lista == null || lista.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                CargadorProperties.obtenerMessages("FC_A_002"), 
                CargadorProperties.obtenerMessages("FC_C_006"), 
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        // Obtener nombre del cliente
        Cliente clienteBuscado = cli.verificarDP(cedula);
        String nombreCliente = (clienteBuscado != null) ? clienteBuscado.getNombre() : "N/A";
        
        // Cargar en tabla según número de columnas
        int numColumnas = modelo.getColumnCount();
        
        for (Factura f : lista) {
            if (numColumnas == 3) {
                // Tabla MODIFICAR (3 columnas)
                modelo.addRow(new Object[]{
                    f.getCodigo(),
                    f.getFechaHora() != null ? f.getFechaHora().format(FMT_FECHA) : "",
                    nombreCliente
                });
            } else {
                // Tabla ELIMINAR/CONSULTAR (8 columnas)
                modelo.addRow(new Object[]{
                    f.getCodigo(),
                    nombreCliente,
                    f.getFechaHora() != null ? f.getFechaHora().format(FMT_FECHA) : "",
                    String.format("%.2f", f.getSubtotal()),
                    String.format("%.2f", f.getIva()),
                    String.format("%.2f", f.getTotal()),
                    f.getTipo(),
                    f.getEstado()
                });
            }
        }
    }
        
    // Crea y configura el modelo de la tabla de productos
    private DefaultTableModel crearModeloTablaProductos(boolean editable) {
        String[] cols = CargadorProperties.obtenerComponentes("TABLA_PRODUCTOS_COLS").split(",");

        return new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return editable && column == 2;  // Solo columna Cantidad si es editable
            }

            @Override
            public Class<?> getColumnClass(int column) {
                if (column == 2) return Integer.class;
                return String.class;
            }
        };
    }

    // Configura la tabla con listener para recalcular totales si es editable
    private void configurarTablaProductos(DefaultTableModel modelo, JTable tabla, 
                                          boolean editable, 
                                          JTextField txtSub, JTextField txtIva, JTextField txtTotal) {
        tabla.setRowHeight(25);

        // Si es editable, agregar listener para recalcular totales
        if (editable && txtSub != null && txtIva != null && txtTotal != null) {
            modelo.addTableModelListener(new TableModelListener() {
                @Override
                public void tableChanged(TableModelEvent e) {
                    if (e.getType() == TableModelEvent.UPDATE && e.getColumn() == 2) {
                        validarYActualizarCantidad(modelo, txtSub, txtIva, txtTotal, e.getFirstRow());
                    }
                }
            });
        }
    }
    
    //**METODOS AUXILIARES UI**
    //Titulo de ventana centrado
    private JLabel crearTituloCentrado(String textoKey) {
        JLabel lblTitulo = new JLabel(CargadorProperties.obtenerComponentes(textoKey));
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 42));
        lblTitulo.setForeground(Color.DARK_GRAY);
        return lblTitulo;
    }
    
    //**Creación de cabeceras**
    private JPanel crearCabeceraFactura(JLabel lblCodigo, JLabel lblFecha,
                                        JLabel lblNombreCliente, JLabel lblCedulaCliente,
                                        JLabel lblEmailCliente, JLabel lblTelefonoCliente,
                                        boolean mostrarBusqueda, JTextField txtCedulaBusqueda,
                                        JLabel lblErrorCedula, Runnable accionBusqueda) {
        JPanel panelCabecera = new JPanel(new BorderLayout(20, 10));
        panelCabecera.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        //PANEL IZQUIERDO: Código y Fecha
        JPanel panelIzq = new JPanel();
        panelIzq.setLayout(new BoxLayout(panelIzq, BoxLayout.Y_AXIS));

        if (lblCodigo != null) {
            lblCodigo.setFont(new Font("Arial", Font.BOLD, 14));
            lblCodigo.setAlignmentX(Component.CENTER_ALIGNMENT);
            panelIzq.add(lblCodigo);
            panelIzq.add(Box.createVerticalStrut(8));
        }

        JLabel lblFechaTit = new JLabel(CargadorProperties.obtenerComponentes("LBL_FECHA_HORA"));
        lblFechaTit.setFont(new Font("Arial", Font.BOLD, 13));
        lblFechaTit.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelIzq.add(lblFechaTit);

        if (lblFecha != null) {
            lblFecha.setFont(new Font("Arial", Font.PLAIN, 13));
            lblFecha.setAlignmentX(Component.CENTER_ALIGNMENT);
            panelIzq.add(lblFecha);
        } else {
            //Si no hay label de fecha (para panel crear), mostrar fecha actual
            LocalDateTime ahora = LocalDateTime.now();
            String fechaHora = ahora.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
            JLabel lblFechaActual = new JLabel(fechaHora);
            lblFechaActual.setFont(new Font("Arial", Font.PLAIN, 13));
            lblFechaActual.setAlignmentX(Component.CENTER_ALIGNMENT);
            panelIzq.add(lblFechaActual);
        }

        //PANEL CENTRO: Empresa
        JPanel panelEmpresa = new JPanel();
        panelEmpresa.setLayout(new BoxLayout(panelEmpresa, BoxLayout.Y_AXIS));

        JLabel lblNom = new JLabel(CargadorProperties.obtenerComponentes("EMPRESA_NOMBRE"));
        lblNom.setFont(new Font("Arial", Font.BOLD, 16));
        lblNom.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelEmpresa.add(lblNom);

        JLabel lblEmail = new JLabel(CargadorProperties.obtenerComponentes("EMPRESA_EMAIL"));
        lblEmail.setFont(new Font("Arial", Font.PLAIN, 14));
        lblEmail.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelEmpresa.add(lblEmail);

        JLabel lblTel = new JLabel(CargadorProperties.obtenerComponentes("EMPRESA_TELEFONO"));
        lblTel.setFont(new Font("Arial", Font.PLAIN, 14));
        lblTel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelEmpresa.add(lblTel);

        // PANEL DERECHO: Cliente
        JPanel panelCliente = new JPanel();
        panelCliente.setLayout(new BoxLayout(panelCliente, BoxLayout.Y_AXIS));

        JLabel lblTitCliente = new JLabel(CargadorProperties.obtenerComponentes("LBL_INFO_CLIENTE"));
        lblTitCliente.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitCliente.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelCliente.add(lblTitCliente);
        panelCliente.add(Box.createVerticalStrut(5));

        // Si tiene búsqueda (panel crear)
        if (mostrarBusqueda && txtCedulaBusqueda != null && accionBusqueda != null) {
            JPanel pCedBusqueda = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
            JLabel lblCedRucTit = new JLabel(CargadorProperties.obtenerComponentes("LBL_CEDULA_RUC"));
            lblCedRucTit.setFont(new Font("Arial", Font.BOLD, 12));
            pCedBusqueda.add(lblCedRucTit);
            pCedBusqueda.add(txtCedulaBusqueda);
            pCedBusqueda.add(crearBotonLupa(accionBusqueda));
            panelCliente.add(pCedBusqueda);

            if (lblErrorCedula != null) {
                lblErrorCedula.setAlignmentX(Component.CENTER_ALIGNMENT);
                panelCliente.add(lblErrorCedula);
            }

            panelCliente.add(Box.createVerticalStrut(3));
        }

        // Información del cliente
        agregarCampoInfoClienteCabecera(panelCliente, "LBL_NOMBRE", lblNombreCliente);
        agregarCampoInfoClienteCabecera(panelCliente, "FC_UI_019", lblCedulaCliente);
        agregarCampoInfoClienteCabecera(panelCliente, "LBL_EMAIL", lblEmailCliente);
        agregarCampoInfoClienteCabecera(panelCliente, "LBL_TELEFONO", lblTelefonoCliente);

        // Ensamblar cabecera
        panelCabecera.add(panelIzq, BorderLayout.WEST);
        panelCabecera.add(panelEmpresa, BorderLayout.CENTER);
        panelCabecera.add(panelCliente, BorderLayout.EAST);

        return panelCabecera;
    }

    //Agregar campos de info cliente en cabecera
    private void agregarCampoInfoClienteCabecera(JPanel panel, String keyLabel, JLabel lblValor) {
        JPanel pCampo = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        JLabel lblTit = new JLabel(CargadorProperties.obtenerComponentes(keyLabel));
        lblTit.setFont(new Font("Arial", Font.BOLD, 12));
        pCampo.add(lblTit);

        if (lblValor != null) {
            lblValor.setFont(new Font("Arial", Font.PLAIN, 12));
            pCampo.add(lblValor);
        }

        panel.add(pCampo);
    }
    
    //Boton buscar
    private JLabel crearBotonLupa(Runnable accion) {
        JLabel lupa = new JLabel(CargadorProperties.obtenerComponentes("BTN_LUPA"));
        lupa.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        lupa.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lupa.setPreferredSize(new Dimension(35, 25));
        lupa.setHorizontalAlignment(SwingConstants.CENTER);
        lupa.setOpaque(true);
        lupa.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                accion.run();
            }
        });
        return lupa;
    }
    
    //Seccion de Totales
    private void agregarSeccionTotales(JPanel panel, GridBagConstraints gbc, 
                                       int filaInicio, JTextField txtSub, 
                                       JTextField txtIva, JTextField txtTotal) {
        int fila = filaInicio;

        //Subtotal
        gbc.gridx = 6; gbc.gridy = fila;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel(CargadorProperties.obtenerComponentes("LBL_SUBTOTAL")), gbc);
        gbc.gridx = 7;
        panel.add(txtSub, gbc);

        //IVA
        fila++;
        gbc.gridx = 6; gbc.gridy = fila;
        panel.add(new JLabel(CargadorProperties.obtenerComponentes("LBL_IVA")), gbc);
        gbc.gridx = 7;
        panel.add(txtIva, gbc);

        //Total
        fila++;
        gbc.gridx = 6; gbc.gridy = fila;
        JLabel lblTot = new JLabel(CargadorProperties.obtenerComponentes("LBL_TOTAL"));
        lblTot.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(lblTot, gbc);
        gbc.gridx = 7;
        panel.add(txtTotal, gbc);
    }

    //Muestra el total inicial
    private JTextField crearCampoTotal(boolean esBold) {
        JTextField txt = new JTextField(CargadorProperties.obtenerComponentes("TOTAL_INICIAL"));
        txt.setEditable(false);
        txt.setPreferredSize(new Dimension(120, 25));
        if (esBold) {
            txt.setFont(new Font("Arial", Font.BOLD, 12));
        }
        return txt;
    }
        
    //Crea label para mostrar mensajes de error
    private JLabel crearLabelError() {
        JLabel lbl = new JLabel(" ");
        lbl.setForeground(Color.RED);
        lbl.setFont(new Font("Arial", Font.PLAIN, 11));
        return lbl;
    }
    
    //Cambia entre paneles según la opción del combo
    private void cambiarPanel() {
        String opcion = (String) comboOpciones.getSelectedItem();
        String[] opciones = CargadorProperties.obtenerComponentes("CMB_OPERACION").split(",");
        
        if (opcion.equals(opciones[1])) {
            limpiarPanelCrear();
            cardLayout.show(panelContenedor, PANEL_CREAR);
        } else if (opcion.equals(opciones[2])) {
            limpiarPanelModificar();
            cardLayout.show(panelContenedor, PANEL_MODIFICAR);
        } else if (opcion.equals(opciones[3])) {
            limpiarPanelEliminar();
            cardLayout.show(panelContenedor, PANEL_ELIMINAR);
        } else if (opcion.equals(opciones[4])) {
            cardLayout.show(panelContenedor, PANEL_CONSULTAR);
            limpiarPanelConsultar();
        } else {
            cardLayout.show(panelContenedor, PANEL_VACIO);
        }
    }
    
    //Vuelve al panel vacío
    private void volverAlMenu() {
        this.dispose();             
        new MenuPrincipal().setVisible(true);
    }
    
    //**Validaciones en tiempo real**
    //Validacion de crear
    private void configurarValidacionesCrear() {
        // Cédula
        txtCedulaCrear.getDocument().addDocumentListener(
            crearValidadorTiempoReal(txtCedulaCrear, lblErrorCedulaCrear, "cedula")
        );
        
        // Cantidad
        txtCantidadCrear.getDocument().addDocumentListener(
            crearValidadorTiempoReal(txtCantidadCrear, lblErrorCantidadCrear, "cantidad")
        );
    }
    
    private void configurarValidacionesMod() {
        txtCedulaMod.getDocument().addDocumentListener(
            crearValidadorTiempoReal(txtCedulaMod, lblErrorCedulaMod, "cedula")
        );
        txtCantidadMod.getDocument().addDocumentListener(
            crearValidadorTiempoReal(txtCantidadMod, lblErrorCantidadMod, "cantidad")
        );
    }
    
    private void configurarValidacionesConsultar() {
        txtCedulaConsulta.getDocument().addDocumentListener(
            crearValidadorTiempoReal(txtCedulaConsulta, lblErrorCedulaConsulta, "cedula")
        );
    }
        
   private DocumentListener crearValidadorTiempoReal(JTextField campo, 
                                                    JLabel lblError, 
                                                    String tipo) {
        return new DocumentListener() {
          public void insertUpdate(DocumentEvent e) { validar(); }
          public void removeUpdate(DocumentEvent e) { validar(); }
          public void changedUpdate(DocumentEvent e) { validar(); }

          private void validar() {
              String texto = campo.getText().trim();

              if (texto.isEmpty()) {
                  mostrarError(lblError, null);
                  return;
              }

              String error = null;
              switch(tipo) {
                  case "cedula":
                      // Validación en tiempo real más permisiva
                      error = validarCedulaTiempoReal(texto);
                      break;
                  case "cantidad":
                      error = ValidacionesFactura.validarCantidad(texto);
                      break;
              }
              mostrarError(lblError, error);
          }
        };
        }

        // NUEVO MÉTODO: Validación permisiva para cédula mientras se escribe
        private String validarCedulaTiempoReal(String texto) {
        // 1. Solo números
        if (!texto.matches("\\d*")) {
          return CargadorProperties.obtenerMessages("FC_A_011"); // "Solo números"
        }

        // 2. Máximo 13 caracteres (mientras escribe)
        if (texto.length() > 13) {
          return CargadorProperties.obtenerMessages("CL_A_005"); // "Máximo 13 dígitos"
        }

        // 3. Si ya escribió 10 o 13 dígitos, validar que sea válido
        if (texto.length() == 10 || texto.length() == 13) {
          return ValidacionesFactura.validarCedRucCliente(texto);
        }

        // 4. Si está escribiendo (menos de 10 o entre 10-13), NO mostrar error
        return null;
        }
        
    //Valida y actualiza cantidad editada en tabla (con POPUP)
    private void validarYActualizarCantidad(DefaultTableModel modelo, JTextField txtSub, JTextField txtIva, JTextField txtTot, int fila) {
        try {
            Object valor = modelo.getValueAt(fila, 2);
            int cantidad = Integer.parseInt(valor.toString());

            if (cantidad <= 0) {
                JOptionPane.showMessageDialog(this, 
                    CargadorProperties.obtenerMessages("FC_A_018"), 
                    CargadorProperties.obtenerMessages("FC_C_004"), 
                    JOptionPane.ERROR_MESSAGE);
                modelo.setValueAt(productosFactura.get(fila).getCantidad(), fila, 2);
                return;
            }
            
            ProxFac pxf = productosFactura.get(fila);
            Producto prod = new Producto();
            Producto productoCompleto = prod.verificarPorCodigoDP(pxf.getCodigoProd());

            if (productoCompleto != null && cantidad > productoCompleto.getSaldoFin()) {
                JOptionPane.showMessageDialog(this,
                    CargadorProperties.obtenerComponentes("FC_A_022") + productoCompleto.getSaldoFin(),
                    CargadorProperties.obtenerMessages("FC_C_004"),
                    JOptionPane.ERROR_MESSAGE);
                modelo.setValueAt(productosFactura.get(fila).getCantidad(), fila, 2);
                return;
            }
            pxf.setCantidad(cantidad);
            pxf.calcularSubtotal();

            modelo.setValueAt(String.format("%.2f", pxf.getSubtotalProducto()), fila, 4);

            calcularTotales(txtSub, txtIva, txtTot);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, 
                CargadorProperties.obtenerMessages("FC_A_020"), 
                CargadorProperties.obtenerMessages("FC_C_004"), 
                JOptionPane.ERROR_MESSAGE);
            modelo.setValueAt(productosFactura.get(fila).getCantidad(), fila, 2);
        }
    }

    //Muestra u oculta mensaje de error en label
    private void mostrarError(JLabel label, String mensaje) {
        if (mensaje != null) {
            label.setText(mensaje);
        } else {
            label.setText(" ");
        }
    }
    
    //**METODOS NEGOCIO CREAR**
    //Busca cliente por cédula y muestra sus datos
    private void buscarClienteCrear() {
        String cedRuc = txtCedulaCrear.getText().trim();
        
        String error = ValidacionesFactura.validarCedRucCliente(cedRuc);
        if (error != null) {
            mostrarError(lblErrorCedulaCrear, error);
            limpiarDatosCliente(lblNombreClienteCrear, lblCedulaClienteCrear, lblEmailClienteCrear, lblTelefonoClienteCrear);
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
            
            cmbProductoCrear.setEnabled(true);
            txtCantidadCrear.setEnabled(true);
        } else {
            limpiarDatosCliente(lblNombreClienteCrear, lblCedulaClienteCrear, lblEmailClienteCrear, lblTelefonoClienteCrear);
            mostrarError(lblErrorCedulaCrear, CargadorProperties.obtenerMessages("FC_A_009"));
            
            cmbProductoCrear.setEnabled(false);
            txtCantidadCrear.setEnabled(false);
        }
    }
       
    //Limpia datos del cliente mostrados
    private void limpiarDatosCliente(JLabel lblNombre, JLabel lblCedula, 
                                     JLabel lblEmail, JLabel lblTelefono) {
        lblNombre.setText("");
        lblCedula.setText("");
        lblEmail.setText("");
        lblTelefono.setText("");
    }
    
    //Agrega producto a la tabla de productos
    private void agregarProducto(JComboBox<ItemProducto> combo,
                                         JTextField txtCantidad,
                                         JLabel lblError,
                                         DefaultTableModel modelo,
                                         JTextField txtSub,
                                         JTextField txtIva,
                                         JTextField txtTotal) {
        
        // Validar cliente (solo en crear)
        if (clienteSeleccionado == null && combo == cmbProductoCrear) {
            JOptionPane.showMessageDialog(this, 
                CargadorProperties.obtenerMessages("FC_A_012"), 
                CargadorProperties.obtenerMessages("FC_C_005"), 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Validar producto seleccionado
        ItemProducto item = (ItemProducto) combo.getSelectedItem();
        if (item == null) {
            JOptionPane.showMessageDialog(this, 
                CargadorProperties.obtenerMessages("FC_A_013"), 
                CargadorProperties.obtenerMessages("FC_C_005"), 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Validar cantidad
        String cantidadStr = txtCantidad.getText().trim();
        if (cantidadStr.isEmpty()) {
            mostrarError(lblError, CargadorProperties.obtenerMessages("FC_A_014"));
            return;
        }
        
        
        int cantidad;
        try {
            cantidad = Integer.parseInt(cantidadStr);
            if (cantidad <= 0) throw new NumberFormatException();
            mostrarError(lblError, null);
        } catch (NumberFormatException e) {
            mostrarError(lblError, CargadorProperties.obtenerMessages("FC_A_020"));
            return;
        }
        
        Producto prod = item.producto;  // Solo para obtener el código

        Producto prodActualizado = new Producto();
        Producto productoCompleto = prodActualizado.verificarPorCodigoDP(prod.getCodigo());

        if (productoCompleto == null) {
            JOptionPane.showMessageDialog(this, 
                CargadorProperties.obtenerMessages("FC_A_022"), 
                CargadorProperties.obtenerMessages("FC_C_004"), 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        int stockDisponible = productoCompleto.getSaldoFin();

        int cantidadExistente = 0;

        // Si el producto ya está en la lista, sumar su cantidad
        int filaExistente = buscarProductoEnLista(prod.getCodigo());
        if (filaExistente != -1) {
            cantidadExistente = productosFactura.get(filaExistente).getCantidad();
        }

        int cantidadTotal = cantidadExistente + cantidad;

       if (cantidadTotal > stockDisponible) {
            String mensajeError = CargadorProperties.obtenerComponentes("FC_A_022");
            if (mensajeError == null || mensajeError.trim().isEmpty()) {
                mensajeError = "Stock insuficiente. Disponible: ";
            }
            mensajeError += stockDisponible;

            if (cantidadExistente > 0) {
                mensajeError += " (Ya tienes " + cantidadExistente + " en la factura)";
            }

            mostrarError(lblError, mensajeError);
            return;
        }     
        
        if (filaExistente != -1) {
            // Producto existe: SUMAR cantidad
            ProxFac pxfExistente = productosFactura.get(filaExistente);
            int cantidadAnterior = pxfExistente.getCantidad();
            int cantidadNueva = cantidadAnterior + cantidad;
            
            pxfExistente.setCantidad(cantidadNueva);
            pxfExistente.calcularSubtotal();
            
            modelo.setValueAt(cantidadNueva, filaExistente, 2);
            modelo.setValueAt(String.format("%.2f", pxfExistente.getSubtotalProducto()), filaExistente, 4);
            
            JOptionPane.showMessageDialog(this, 
                CargadorProperties.obtenerMessages("FC_A_021") + 
                cantidadAnterior + " + " + cantidad + " = " + cantidadNueva, 
                CargadorProperties.obtenerComponentes("TITULO_PRODUCTO_DUPLICADO"), 
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            // Producto nuevo: AGREGAR
            ProxFac pxf = new ProxFac();
            if (facturaSeleccionada != null) {
                pxf.setCodigoFac(facturaSeleccionada.getCodigo());
            }
            pxf.setCodigoProd(prod.getCodigo());
            pxf.setCantidad(cantidad);
            pxf.setPrecioVenta(prod.getPrecioVenta());
            pxf.calcularSubtotal();
            pxf.setEstado("APR");
            
            productosFactura.add(pxf);
            
            modelo.addRow(new Object[]{
                prod.getCodigo(),
                prod.getDescripcion(),
                cantidad,
                String.format("%.2f", prod.getPrecioVenta()),
                String.format("%.2f", pxf.getSubtotalProducto())
            });
        }
        
        calcularTotales(txtSub, txtIva, txtTotal);
        txtCantidad.setText("");
        mostrarError(lblError, null);
    }
    
    private int buscarProductoEnLista(String codigoProducto) {
        for (int i = 0; i < productosFactura.size(); i++) {
            if (productosFactura.get(i).getCodigoProd().equals(codigoProducto)) {
                return i;
            }
        }
        return -1;
    }
    
       
    private void quitarProducto(JTable tabla, 
                                        DefaultTableModel modelo,
                                        JTextField txtSub,
                                        JTextField txtIva,
                                        JTextField txtTotal) {
        int fila = tabla.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this, 
                CargadorProperties.obtenerMessages("FC_A_013"), 
                CargadorProperties.obtenerMessages("FC_C_005"), 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        productosFactura.remove(fila);
        modelo.removeRow(fila);
        calcularTotales(txtSub, txtIva, txtTotal);
    }
    
    //Calcula subtotal IVA y total de la factura
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
    
    //Crea y guarda nueva factura en la base de datos
    private void crearFactura() {
        if (clienteSeleccionado == null) {
            JOptionPane.showMessageDialog(this, 
                CargadorProperties.obtenerMessages("FC_A_009"),
                CargadorProperties.obtenerMessages("FC_C_005"), 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (productosFactura.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                CargadorProperties.obtenerMessages("FC_A_005"),
                CargadorProperties.obtenerMessages("FC_C_005"), 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        Factura fac = new Factura();
        fac.setCodigoCliente(clienteSeleccionado.getIdCliente());
        fac.setFechaHora(LocalDateTime.now());
        fac.setProductos(productosFactura);

        String codigoGenerado = fac.insertar();

        if (codigoGenerado != null) {
            JOptionPane.showMessageDialog(this,
                CargadorProperties.obtenerMessages("FC_I_001") + 
                CargadorProperties.obtenerMessages("FC_I_004") + " " + codigoGenerado,
                CargadorProperties.obtenerMessages("FC_C_003"), 
                JOptionPane.INFORMATION_MESSAGE);
            limpiarPanelCrear();
        } else {
            JOptionPane.showMessageDialog(this, 
                CargadorProperties.obtenerMessages("FC_E_002"),
                CargadorProperties.obtenerMessages("FC_C_004"), 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    //**METODOS NEGOCIO MODIFICAR**
    //Busca facturas asociadas a un cliente -> Se ocupa en consulta especifica tambien
    private void cargarProductosDeFactura(String codigoFactura, 
                                        DefaultTableModel modelo,
                                        boolean agregarALista) {
        modelo.setRowCount(0);

        if (agregarALista) {
          productosFactura.clear();  // Solo para modificar
        }

        ProxFac pxfConsulta = new ProxFac();
        pxfConsulta.setCodigoFac(codigoFactura);

        ArrayList<ProxFac> productos = pxfConsulta.consultarPorFactura(pxfConsulta);

        if (productos != null && !productos.isEmpty()) {
          for (ProxFac pxf : productos) {
              Producto prod = new Producto();
              Producto prodCompleto = prod.verificarPorCodigoDP(pxf.getCodigoProd());

              String descripcion = (prodCompleto != null) ? prodCompleto.getDescripcion() : "N/A";

              modelo.addRow(new Object[]{
                  pxf.getCodigoProd(),
                  descripcion,
                  pxf.getCantidad(),
                  String.format("%.2f", pxf.getPrecioVenta()),
                  String.format("%.2f", pxf.getSubtotalProducto())
              });

              // Solo agregar a productosFactura si es para MODIFICAR
              if (agregarALista) {
                  productosFactura.add(pxf);
              }
          }
        }
     }
    
    //Carga factura seleccionada para modificar
    private void seleccionarYMostrarFactura(JTable tabla,
                                            DefaultTableModel modeloTabla,
                                            DefaultTableModel modeloProductos,
                                            JPanel panelCabecera,
                                            JLabel lblCodigo,
                                            JLabel lblFecha,
                                            JLabel lblNombre,
                                            JLabel lblCedula,
                                            JLabel lblEmail,
                                            JLabel lblTelefono,
                                            JTextField txtSubtotal,
                                            JTextField txtIva,
                                            JTextField txtTotal,
                                            boolean esEditable,
                                            JComboBox<ItemProducto> combo,
                                            JTextField txtCantidad,
                                            JButton btnGuardar) {
        int fila = tabla.getSelectedRow();
        if (fila == -1) return;

        String codigo = (String) modeloTabla.getValueAt(fila, 0);

        // Consultar factura
        Factura facParam = new Factura();
        facParam.setCodigo(codigo);

        Factura fac = new Factura();
        Factura facturaConsultada = fac.consultarPorCodigoDetalle(facParam);

        if (facturaConsultada != null) {
            // Guardar referencia si es editable
            if (esEditable) {
                facturaSeleccionada = facturaConsultada;
            }

            // Obtener info del cliente
            Cliente cli = new Cliente();
            Cliente clienteCompleto = cli.verificarPorIdDP(facturaConsultada.getCodigoCliente());

            // Mostrar cabecera
            if (panelCabecera != null) {
                panelCabecera.setVisible(true);
            }

            // Llenar datos de cabecera
            lblCodigo.setText(CargadorProperties.obtenerComponentes("LBL_CODIGO") + " " + facturaConsultada.getCodigo());
            lblFecha.setText(facturaConsultada.getFechaHora().format(FMT_FECHA));

            if (clienteCompleto != null) {
                lblNombre.setText(clienteCompleto.getNombre());
                lblCedula.setText(clienteCompleto.getCedRuc());
                lblEmail.setText(clienteCompleto.getEmail() != null ? clienteCompleto.getEmail() : "");
                lblTelefono.setText(clienteCompleto.getTelefono() != null ? clienteCompleto.getTelefono() : "");
            }

            // Cargar productos
            cargarProductosDeFactura(facturaConsultada.getCodigo(), modeloProductos, esEditable);

            // Mostrar totales
            txtSubtotal.setText(String.format("%.2f", facturaConsultada.getSubtotal()));
            txtIva.setText(String.format("%.2f", facturaConsultada.getIva()));
            txtTotal.setText(String.format("%.2f", facturaConsultada.getTotal()));

            // Si es EDITABLE, habilitar controles
            if (esEditable && combo != null && txtCantidad != null && btnGuardar != null) {
                combo.setEnabled(true);
                txtCantidad.setEnabled(true);
                btnGuardar.setEnabled(true);
                cargarProductosActivos(combo);
            }
        }
    }

    //Guarda cambios de factura modificadaa
    private void modificarFactura() {
        if (facturaSeleccionada == null) {
            JOptionPane.showMessageDialog(this, 
                CargadorProperties.obtenerMessages("FC_A_016"), 
                CargadorProperties.obtenerMessages("FC_C_005"), 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (productosFactura.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                CargadorProperties.obtenerMessages("FC_A_017"), 
                CargadorProperties.obtenerMessages("FC_C_005"), 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        facturaSeleccionada.setProductos(productosFactura);

        if (facturaSeleccionada.modificar()) {
            JOptionPane.showMessageDialog(this, 
                CargadorProperties.obtenerMessages("FC_I_002"), 
                CargadorProperties.obtenerMessages("FC_C_003"), 
                JOptionPane.INFORMATION_MESSAGE);
            limpiarPanelModificar();
        } else {
            JOptionPane.showMessageDialog(this, 
                CargadorProperties.obtenerMessages("FC_E_004"), 
                CargadorProperties.obtenerMessages("FC_C_004"), 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    //**METODOS NEGOCIO ELIMINAR**
    //Elimina factura seleccionada
    private void eliminarFactura() {
        int fila = tablaFacturasElim.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, 
                CargadorProperties.obtenerMessages("FC_A_016"), 
                CargadorProperties.obtenerMessages("FC_C_005"), 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        String codigo = (String) modeloFacturasElim.getValueAt(fila, 0);

        // Confirmación
        int conf = JOptionPane.showConfirmDialog(this, 
            CargadorProperties.obtenerMessages("FC_C_001") + codigo + "?", 
            CargadorProperties.obtenerMessages("FC_C_002"), 
            JOptionPane.YES_NO_OPTION);
        if (conf != JOptionPane.YES_OPTION) return;

        // Eliminar
        Factura fac = new Factura();
        fac.setCodigo(codigo);

        if (fac.eliminar()) {
            JOptionPane.showMessageDialog(this, 
                CargadorProperties.obtenerMessages("FC_I_003"), 
                CargadorProperties.obtenerMessages("FC_C_003"), 
                JOptionPane.INFORMATION_MESSAGE);
            // Recargar tabla
            buscarYCargarFacturas(txtCedulaElim, modeloFacturasElim, false);
        } else {
            JOptionPane.showMessageDialog(this, 
                CargadorProperties.obtenerMessages("FC_E_005"), 
                CargadorProperties.obtenerMessages("FC_C_004"), 
                JOptionPane.ERROR_MESSAGE);
        }
    }  
    
    //**METODOS NEGOCIO CONSULTAR**
    //Cambia tipo de consulta y muestra panel correspondiente
    private void cambiarTipoConsulta() {
        String tipo = (String) comboTipoConsulta.getSelectedItem();
        String[] opciones = CargadorProperties.obtenerComponentes("CMB_TIPO_CONSULTA").split(",");

        // Limpiar antes de cambiar
        limpiarPanelConsultar();

        if (tipo.equals(opciones[1])) {
            // Consulta General (todas las facturas APR)
            panelBusqueda.setVisible(false);
            consultarTodos();
        } else if (tipo.equals(opciones[2])) {
            // Consulta Por Cliente (buscar por cédula)
            panelBusqueda.setVisible(true);
            panelBusqueda.setVisible(true);
            modeloTablaResultados.setRowCount(0);
            panelDetalleConsulta.setVisible(false);
        } else {
            // "Seleccione..." (opción por defecto)
            panelBusqueda.setVisible(false);
            modeloTablaResultados.setRowCount(0);
            panelDetalleConsulta.setVisible(false);
        }
    }

    //Consulta todas las facturas activas
    private void consultarTodos() {
        Factura fac = new Factura();
        ArrayList<Factura> lista = fac.consultarTodos();

        modeloTablaResultados.setRowCount(0);

        if (lista == null || lista.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                CargadorProperties.obtenerMessages("FC_A_001"), 
                CargadorProperties.obtenerMessages("FC_C_006"), 
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        for (Factura f : lista) {
            // Obtener nombre del cliente
            Cliente cli = new Cliente();
            Cliente clienteCompleto = cli.verificarPorIdDP(f.getCodigoCliente());
            String nombreCliente = (clienteCompleto != null) ? clienteCompleto.getNombre() : "N/A";

            modeloTablaResultados.addRow(new Object[]{
                f.getCodigo(),
                nombreCliente,
                f.getFechaHora() != null ? f.getFechaHora().format(FMT_FECHA) : "",
                String.format("%.2f", f.getSubtotal()),
                String.format("%.2f", f.getIva()),
                String.format("%.2f", f.getTotal()),
                f.getTipo(),
                f.getEstado()
            });
        }

        // Ocultar detalle al mostrar lista general
        panelDetalleConsulta.setVisible(false);
    }
     
    //**METODOS AUXILIARES DATOS**
    //Carga productos activos en el combobox
    private void cargarProductosActivos(JComboBox<ItemProducto> combo) {
        combo.removeAllItems();
        
        Producto prod = new Producto();
        ArrayList<Producto> lista = prod.consultarTodos();
        
        if (lista != null) {
            for (Producto p : lista) {
                if ("ACT".equalsIgnoreCase(p.getEstado()) && p.getSaldoFin() > 0) {
                    combo.addItem(new ItemProducto(p));
                }
            }
        }
    }
    
    //Genera y muestra código de factura
    private void generarYMostrarCodigo() {
        String codigo = Factura.generarCodigo();
        
        if (codigo != null) {
            lblCodigoGenerado.setText(CargadorProperties.obtenerComponentes("LBL_CODIGO") + " " + codigo);
        } else {
            lblCodigoGenerado.setText(CargadorProperties.obtenerComponentes("LBL_CODIGO") + " " + CargadorProperties.obtenerComponentes("CODIGO_ERROR"));
        }
    }
    
    //**METODOS LIMPIEZA**
    //Limpia panel crear y resetea variables
    private void limpiarPanelCrear() {
        txtCedulaCrear.setText("");
        txtCantidadCrear.setText("");
        
        lblNombreClienteCrear.setText("");
        lblCedulaClienteCrear.setText("");
        lblEmailClienteCrear.setText("");
        lblTelefonoClienteCrear.setText("");
        
        lblErrorCedulaCrear.setText(" ");
        lblErrorCantidadCrear.setText(" ");
        
        modeloProductosCrear.setRowCount(0);
        
        txtSubtotalCrear.setText(CargadorProperties.obtenerComponentes("TOTAL_INICIAL"));
        txtIVACrear.setText(CargadorProperties.obtenerComponentes("TOTAL_INICIAL"));
        txtTotalCrear.setText(CargadorProperties.obtenerComponentes("TOTAL_INICIAL"));
        
        clienteSeleccionado = null;
        if (productosFactura != null) {
            productosFactura.clear();
        }

        if (cmbProductoCrear != null) cmbProductoCrear.setEnabled(false);
        if (txtCantidadCrear != null) txtCantidadCrear.setEnabled(false);
        
        generarYMostrarCodigo();
    }
    
    //Limpia panel modificar y resetea variables
    private void limpiarPanelModificar() {
        if (txtCedulaMod != null) txtCedulaMod.setText("");
        if (lblErrorCedulaMod != null) lblErrorCedulaMod.setText(" ");
        if (modeloFacturasMod != null) modeloFacturasMod.setRowCount(0);
        if (modeloProductosMod != null) modeloProductosMod.setRowCount(0);

        if (panelCabeceraMod != null) {
            panelCabeceraMod.setVisible(false);
        }

        if (lblCodigoMod != null) lblCodigoMod.setText("");
        if (lblFechaMod != null) lblFechaMod.setText("");
        if (lblNombreClienteMod != null) lblNombreClienteMod.setText("");
        if (lblCedulaClienteMod != null) lblCedulaClienteMod.setText("");
        if (lblEmailClienteMod != null) lblEmailClienteMod.setText("");
        if (lblTelefonoClienteMod != null) lblTelefonoClienteMod.setText("");

        if (txtSubtotalMod != null) txtSubtotalMod.setText(CargadorProperties.obtenerComponentes("TOTAL_INICIAL"));
        if (txtIVAMod != null) txtIVAMod.setText(CargadorProperties.obtenerComponentes("TOTAL_INICIAL"));
        if (txtTotalMod != null) txtTotalMod.setText(CargadorProperties.obtenerComponentes("TOTAL_INICIAL"));

        if (cmbProductoMod != null) cmbProductoMod.setEnabled(false);
        if (txtCantidadMod != null) txtCantidadMod.setEnabled(false);
        if (btnGuardarMod != null) btnGuardarMod.setEnabled(false);

        facturaSeleccionada = null;
        if (productosFactura != null) productosFactura.clear();

        if (lblErrorCantidadMod != null) lblErrorCantidadMod.setText(" ");
    }

    //Limpia panel eliminar
    private void limpiarPanelEliminar() {
        if (txtCedulaElim != null) {
            txtCedulaElim.setText("");
        }
        if (modeloFacturasElim != null) {
            modeloFacturasElim.setRowCount(0);
        }
        if (btnEliminar != null) {
            btnEliminar.setEnabled(false);
        }
    }

    //Limpia panel consultar
    private void limpiarPanelConsultar() {
        // Resetear combo
        if (comboTipoConsulta != null) {
            comboTipoConsulta.setSelectedIndex(0);
        }

        // Limpiar campo de búsqueda
        if (txtCedulaConsulta != null) {
            txtCedulaConsulta.setText("");
        }
        
        
        if (lblErrorCedulaConsulta != null) {
            lblErrorCedulaConsulta.setText(" ");
        }

        // Limpiar tabla de facturas
        if (modeloTablaResultados != null) {
            modeloTablaResultados.setRowCount(0);
        }

        // Limpiar tabla de productos
        if (modeloProductosConsulta != null) {
            modeloProductosConsulta.setRowCount(0);
        }

        // Limpiar labels de cabecera
        if (lblCodigoConsulta != null) lblCodigoConsulta.setText("");
        if (lblFechaConsulta != null) lblFechaConsulta.setText("");
        if (lblNombreClienteConsulta != null) lblNombreClienteConsulta.setText("");
        if (lblCedulaClienteConsulta != null) lblCedulaClienteConsulta.setText("");
        if (lblEmailClienteConsulta != null) lblEmailClienteConsulta.setText("");
        if (lblTelefonoClienteConsulta != null) lblTelefonoClienteConsulta.setText("");

        // Limpiar totales
        if (txtSubtotalConsulta != null) {
            txtSubtotalConsulta.setText(CargadorProperties.obtenerComponentes("TOTAL_INICIAL"));
        }
        if (txtIVAConsulta != null) {
            txtIVAConsulta.setText(CargadorProperties.obtenerComponentes("TOTAL_INICIAL"));
        }
        if (txtTotalConsulta != null) {
            txtTotalConsulta.setText(CargadorProperties.obtenerComponentes("TOTAL_INICIAL"));
        }

        // Ocultar paneles
        if (panelBusqueda != null) {
            panelBusqueda.setVisible(false);
        }
        if (panelDetalleConsulta != null) {
            panelDetalleConsulta.setVisible(false);
        }
    }

    //**CLASE INTERNA**
    //Clase wrapper para mostrar productos en ComboBox
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
}
