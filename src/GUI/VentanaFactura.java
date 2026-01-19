package GUI;

import DP.Cliente;
import DP.Factura;
import DP.ProxFac;
import DP.Producto;
import util.CargadorProperties;
import util.ValidacionesFactura;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
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

    // Paleta de colores (IGUAL A VENTANA CLIENTE)
    private static final Color COLOR_PRIMARIO = new Color(255, 173, 51); // #FFAD33 - Naranja
    private static final Color COLOR_SECUNDARIO = new Color(102, 151, 74); // #66974A - Verde
    private static final Color COLOR_ACENTO = new Color(204, 20, 0); // #CC1400 - Rojo
    private static final Color COLOR_TEXTO = new Color(76, 87, 169); // #4C57A9 - Azul tinta
    private static final Color COLOR_FONDO_CENTRAL = new Color(255, 247, 227); // #fff7e3 - Fondo panel central
    private static final Color COLOR_TEXTO_SECUNDARIO = new Color(153, 153, 153); // #999
    private static final Color COLOR_BORDE = new Color(221, 221, 221); // #ddd
    private static final Color COLOR_ENFASIS = new Color(255, 164, 28); // #ffa41c - Naranja énfasis
    private static final Color COLOR_BLANCO = Color.WHITE;
    private static final Color COLOR_TEXTO_CAMPO = Color.BLACK; // Negro para texto en campos

    // Fuente principal - Poppins
    private static Font FUENTE_TITULO;
    private static Font FUENTE_SUBTITULO;
    private static Font FUENTE_BASE;
    private static Font FUENTE_LABEL;
    private static Font FUENTE_BOTON;

    private JLabel lblTituloSuperior;
    private JLabel lblLogo;

    // **CONSTANTES**
    private static final String PANEL_VACIO = CargadorProperties.obtenerComponentes("PANEL_VACIO");
    private static final String PANEL_CREAR = CargadorProperties.obtenerComponentes("PANEL_CREAR");
    private static final String PANEL_MODIFICAR = CargadorProperties.obtenerComponentes("PANEL_MODIFICAR");
    private static final String PANEL_ELIMINAR = CargadorProperties.obtenerComponentes("PANEL_ELIMINAR");
    private static final String PANEL_CONSULTAR = CargadorProperties.obtenerComponentes("PANEL_CONSULTAR");
    private static final DateTimeFormatter FMT_FECHA = DateTimeFormatter
            .ofPattern(CargadorProperties.obtenerComponentes("FORMATO_FECHA"));

    // **VARIABLES CARDLAYOUT**
    private CardLayout cardLayout;
    private JPanel panelContenedor;
    private JComboBox<String> comboOpciones;

    // **VARIABLES PANEL CREAR**
    private JLabel lblCodigoGenerado;
    private JTextField txtCedulaCrear, txtCantidadCrear;
    private JLabel lblNombreClienteCrear, lblCedulaClienteCrear, lblEmailClienteCrear, lblTelefonoClienteCrear;
    private JLabel lblErrorCedulaCrear, lblErrorCantidadCrear;
    private JComboBox<ItemProducto> cmbProductoCrear;
    private JTable tablaProductosCrear;
    private DefaultTableModel modeloProductosCrear;
    private JTextField txtSubtotalCrear, txtIVACrear, txtTotalCrear;
    private String codigoFacturaActual = null; // Para dar seguimiento a la creacion
    private boolean facturaYaInsertada = false; // Sirve para saber si ya se inserto la factura

    // **VARIABLES PANEL MODIFICAR**
    private JTextField txtCedulaMod;
    private JTable tablaFacturasMod;
    private JLabel lblErrorCedulaMod;
    private DefaultTableModel modeloFacturasMod;
    private JTable tablaProductosMod;
    private DefaultTableModel modeloProductosMod;
    private JComboBox<ItemProducto> cmbProductoMod;
    private JTextField txtCantidadMod, txtSubtotalMod, txtIVAMod, txtTotalMod;
    private JButton btnGuardarMod;
    private JButton btnGuardarCrear; // Referencia para habilitar/deshabilitar
    private JButton btnGuardarModificar; // Referencia para habilitar/deshabilitar
    private JButton btnQuitarCrear; // Promoted to field
    private JButton btnAprobarCrear; // Promoted to field
    private JButton btnQuitarMod; // Promoted to field for Modify Panel
    private Factura facturaSeleccionada;
    private JLabel lblCodigoMod, lblFechaMod;
    private JLabel lblNombreClienteMod, lblCedulaClienteMod, lblEmailClienteMod, lblTelefonoClienteMod,
            lblErrorCantidadMod;
    private JPanel panelCabeceraMod;

    // **VARIABLES PANEL ELIMINAR**
    private JTextField txtCedulaElim;
    private JTable tablaFacturasElim;
    private DefaultTableModel modeloFacturasElim;
    private JButton btnEliminar;

    // **VARIABLES PANEL CONSULTAR**
    private JComboBox<String> comboTipoConsulta;
    private JTextField txtCedulaConsulta;
    private JLabel lblErrorCedulaConsulta;
    private JTable tablaResultados;
    private DefaultTableModel modeloTablaResultados;
    private JPanel panelBusqueda;
    private JScrollPane scrollTablaResultados;

    // NUEVAS para consulta con detalle
    private JPanel panelDetalleConsulta;
    private JLabel lblCodigoConsulta, lblFechaConsulta;
    private JLabel lblNombreClienteConsulta, lblCedulaClienteConsulta;
    private JLabel lblEmailClienteConsulta, lblTelefonoClienteConsulta;
    private JTable tablaProductosConsulta;
    private DefaultTableModel modeloProductosConsulta;
    private JTextField txtSubtotalConsulta, txtIVAConsulta, txtTotalConsulta;

    // Variables de paginación
    private JPanel panelPaginacion;
    private JButton btnPrimero, btnAnterior, btnSiguiente, btnUltimo;
    private ArrayList<Factura> facturasPaginadasTotal = new ArrayList<>();
    private int paginaActual = 0;
    private final int FILAS_POR_PAGINA = 18;

    // **VARIABLES DATOS**
    private Cliente clienteSeleccionado;
    private ArrayList<ProxFac> productosFactura;

    // **CONSTRUCTOR**
    public VentanaFactura() {
        // Cargar fuentes
        FUENTE_TITULO = cargarFuente("/resources/fonts/Poppins-Bold.ttf", Font.BOLD, 32f);
        FUENTE_SUBTITULO = cargarFuente("/resources/fonts/Poppins-Bold.ttf", Font.BOLD, 24f);
        FUENTE_BASE = cargarFuente("/resources/fonts/Poppins-Regular.ttf", Font.PLAIN, 14f);
        FUENTE_LABEL = cargarFuente("/resources/fonts/Poppins-Regular.ttf", Font.PLAIN, 13f);
        FUENTE_BOTON = cargarFuente("/resources/fonts/Poppins-Bold.ttf", Font.BOLD, 13f);

        productosFactura = new ArrayList<>();
        configurarVentana();
        inicializarComponentes();
        configurarLayout();
    }

    // **CONFIGURACION INICIAL**
    // Configura propiedades básicas de la ventana
    private void configurarVentana() {
        setTitle(CargadorProperties.obtenerComponentes("TITULO"));
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        try {
            setIconImage(new ImageIcon(getClass().getResource("/resources/img/logo-removebg.png")).getImage());
        } catch (Exception e) {
            System.err.println("No se pudo cargar el favicon");
        }
        getContentPane().setBackground(COLOR_BLANCO);
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

    // Configura el layout principal de la ventana
    private void configurarLayout() {
        setLayout(new BorderLayout(10, 10));

        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        panelSuperior.setBackground(COLOR_PRIMARIO);

        JPanel panelFilaCombo = new JPanel(new BorderLayout());
        panelFilaCombo.setBackground(COLOR_PRIMARIO);

        JPanel panelIzquierda = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        panelIzquierda.setBackground(COLOR_PRIMARIO);
        try {
            ImageIcon iconoOriginal = new ImageIcon(getClass().getResource("/resources/img/logo.jpg"));
            Image imagenEscalada = iconoOriginal.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
            lblLogo = new JLabel(new ImageIcon(imagenEscalada));
            panelIzquierda.add(lblLogo);
        } catch (Exception e) {
            System.err.println("No se pudo cargar el logo");
        }
        panelFilaCombo.add(panelIzquierda, BorderLayout.WEST);

        JPanel panelDerecha = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        panelDerecha.setBackground(COLOR_PRIMARIO);
        estilizarComboBox(comboOpciones);
        comboOpciones.setPreferredSize(new Dimension(260, 35));
        panelDerecha.add(comboOpciones);
        panelFilaCombo.add(panelDerecha, BorderLayout.EAST);

        JPanel panelFilaTitulo = new JPanel(new BorderLayout());
        panelFilaTitulo.setBackground(COLOR_PRIMARIO);
        lblTituloSuperior = new JLabel("", SwingConstants.CENTER);
        lblTituloSuperior.setFont(FUENTE_TITULO);
        lblTituloSuperior.setForeground(COLOR_TEXTO);
        panelFilaTitulo.add(lblTituloSuperior, BorderLayout.CENTER);

        panelSuperior.add(panelFilaCombo, BorderLayout.NORTH);
        panelSuperior.add(panelFilaTitulo, BorderLayout.CENTER);

        add(panelSuperior, BorderLayout.NORTH);
        add(panelContenedor, BorderLayout.CENTER);

        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        panelInferior.setBackground(COLOR_PRIMARIO); // Naranja

        JButton btnVolver = new JButton(CargadorProperties.obtenerComponentes("FC_UI_001"));
        estilizarBotonSecundario(btnVolver);
        btnVolver.addActionListener(e -> volverAlMenu());
        panelInferior.add(btnVolver);
        add(panelInferior, BorderLayout.SOUTH);
    }

    // Panel vacío
    private JPanel crearPanelVacio() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(COLOR_FONDO_CENTRAL);
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel lblTitulo = new JLabel(CargadorProperties.obtenerComponentes("TITULO_PANEL_VACIO"));
        lblTitulo.setFont(new Font("Poppins", Font.BOLD, 48));
        lblTitulo.setForeground(COLOR_PRIMARIO);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(lblTitulo, gbc);

        return panel;
    }

    // **CREACIÓN DE PANELES**
    // Panel Ingresar
    private JPanel crearPanelCrear() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(COLOR_FONDO_CENTRAL);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 0, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Inicializar componentes
        txtCedulaCrear = new JTextField();
        estilizarCampoTexto(txtCedulaCrear);
        txtCedulaCrear.setPreferredSize(new Dimension(200, 25));

        lblNombreClienteCrear = new JLabel("");
        lblNombreClienteCrear = new JLabel("");
        estilizarLabel(lblNombreClienteCrear);
        lblCedulaClienteCrear = new JLabel("");
        estilizarLabel(lblCedulaClienteCrear);
        lblEmailClienteCrear = new JLabel("");
        estilizarLabel(lblEmailClienteCrear);
        lblTelefonoClienteCrear = new JLabel("");
        estilizarLabel(lblTelefonoClienteCrear);

        cmbProductoCrear = new JComboBox<>();
        estilizarComboBox(cmbProductoCrear);
        cmbProductoCrear.setPreferredSize(new Dimension(400, 25));
        cmbProductoCrear.setEnabled(false);

        txtCantidadCrear = new JTextField();
        estilizarCampoTexto(txtCantidadCrear);
        txtCantidadCrear.setPreferredSize(new Dimension(120, 25));

        txtSubtotalCrear = crearCampoTotal(false);
        txtIVACrear = crearCampoTotal(false);
        txtTotalCrear = crearCampoTotal(true);

        lblErrorCedulaCrear = crearLabelError();
        lblErrorCantidadCrear = crearLabelError();

        lblCodigoGenerado = new JLabel();
        configurarLabelConPrefijoBold(lblCodigoGenerado,
                CargadorProperties.obtenerComponentes("LBL_CODIGO"),
                CargadorProperties.obtenerComponentes("CODIGO_GENERANDO"));

        generarYMostrarCodigo();
        configurarValidacionesCrear();

        int fila = 0;

        // Título
        // Título eliminado (se muestra en cabecera superior)
        // gbc.gridx = 0;
        // gbc.gridy = fila++;
        // gbc.gridwidth = 8;
        // gbc.anchor = GridBagConstraints.CENTER;
        // gbc.insets = new Insets(10, 10, 15, 10);
        // panel.add(crearTituloCentrado("TITULO_CREAR_FACTURA"), gbc);
        // gbc.gridwidth = 1;
        // gbc.anchor = GridBagConstraints.WEST;
        // gbc.insets = new Insets(5, 10, 0, 10);

        // Cabecera completa (Información Factura + Empresa + Cliente)
        gbc.gridx = 0;
        gbc.gridy = fila++;
        gbc.gridwidth = 8;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 0, 10);
        panel.add(crearCabeceraFactura(
                lblCodigoGenerado, null, // lblCodigo a la IZQUIERDA, lblFecha (null = genera fecha automática)
                lblNombreClienteCrear, lblCedulaClienteCrear, lblEmailClienteCrear, lblTelefonoClienteCrear,
                true, txtCedulaCrear, lblErrorCedulaCrear, () -> buscarClienteCrear() // Cliente a la DERECHA con
                                                                                      // búsqueda
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
        lblTitProd.setFont(FUENTE_TITULO.deriveFont(18f));
        lblTitProd.setForeground(COLOR_TEXTO);
        panel.add(lblTitProd, gbc);
        gbc.gridwidth = 1;

        // Fila: Producto | Cantidad | Agregar
        gbc.gridy = fila++;

        gbc.gridx = 0;
        JLabel lblProd = new JLabel(CargadorProperties.obtenerComponentes("LBL_PRODUCTO"));
        estilizarLabel(lblProd);
        panel.add(lblProd, gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 4;
        panel.add(cmbProductoCrear, gbc);
        gbc.gridwidth = 1;

        gbc.gridx = 5;
        JLabel lblCant = new JLabel(CargadorProperties.obtenerComponentes("LBL_CANTIDAD"));
        estilizarLabel(lblCant);
        panel.add(lblCant, gbc);

        gbc.gridx = 6;
        panel.add(txtCantidadCrear, gbc);

        gbc.gridx = 7;
        gbc.insets = new Insets(5, 3, 0, 10); // Reducir margen izquierdo para acercar el botón
        JButton btnAgregar = new JButton(CargadorProperties.obtenerComponentes("FC_UI_003"));
        btnAgregar.setPreferredSize(new Dimension(90, 25));
        estilizarBotonPrimario(btnAgregar);
        btnAgregar.addActionListener(e -> agregarProducto(
                cmbProductoCrear,
                txtCantidadCrear,
                lblErrorCantidadCrear,
                modeloProductosCrear,
                txtSubtotalCrear,
                txtIVACrear,
                txtTotalCrear));
        panel.add(btnAgregar, gbc);
        gbc.insets = new Insets(5, 10, 0, 10); // Restaurar insets

        // Error cantidad
        gbc.gridx = 6;
        gbc.gridy = fila++;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(-5, 10, 0, 10);
        panel.add(lblErrorCantidadCrear, gbc);
        gbc.insets = new Insets(5, 10, 0, 10); // Restaurar insets

        // Tabla productos (EDITABLE)
        modeloProductosCrear = crearModeloTablaProductos(true); // ← USA MÉTODO AUXILIAR
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

        btnQuitarCrear = new JButton(CargadorProperties.obtenerComponentes("FC_UI_004"));
        estilizarBotonEliminar(btnQuitarCrear);
        btnQuitarCrear.setPreferredSize(new Dimension(90, 25));
        btnQuitarCrear.setEnabled(false); // Disabled initially
        btnQuitarCrear.addActionListener(e -> quitarProducto(
                tablaProductosCrear,
                modeloProductosCrear,
                txtSubtotalCrear,
                txtIVACrear,
                txtTotalCrear));
        panel.add(btnQuitarCrear, gbc);

        // Totales
        fila++;
        agregarSeccionTotales(panel, gbc, fila, txtSubtotalCrear, txtIVACrear, txtTotalCrear);
        fila += 3;

        // Botones (Guardar y Aprobar) en Panel dedicado alineado a la derecha
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.gridwidth = 8;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panelBotones.setOpaque(false); // Fondo transparente

        btnGuardarCrear = new JButton(CargadorProperties.obtenerComponentes("FC_UI_006")); // "Guardar"
        estilizarBotonSecundario(btnGuardarCrear);
        btnGuardarCrear.setPreferredSize(new Dimension(100, 30));
        // btnGuardarCrear.setFont(new Font("Arial", Font.BOLD, 12)); // Ya no necesario
        btnGuardarCrear.setEnabled(false); // Deshabilitado al inicio
        btnGuardarCrear.addActionListener(e -> {
            personalizarPopup();
            JOptionPane.showMessageDialog(this, CargadorProperties.obtenerMessages("FC_I_005"));
            restaurarEstilosPopup();
            limpiarPanelCrear();
            btnGuardarCrear.setEnabled(false);
        });
        panelBotones.add(btnGuardarCrear);

        btnAprobarCrear = new JButton(CargadorProperties.obtenerComponentes("FC_UI_005")); // "Aprobar"
        estilizarBotonPrimario(btnAprobarCrear);
        btnAprobarCrear.setPreferredSize(new Dimension(100, 30));
        // btnAprobarCrear.setFont(new Font("Arial", Font.BOLD, 12)); // Ya no necesario
        btnAprobarCrear.setEnabled(false); // Disabled initially
        btnAprobarCrear.addActionListener(e -> crearFactura());
        panelBotones.add(btnAprobarCrear);

        panel.add(panelBotones, gbc);

        cargarProductosActivos(cmbProductoCrear);

        JPanel panelConScroll = new JPanel(new BorderLayout());
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(null);
        panelConScroll.add(scrollPane, BorderLayout.CENTER);

        return panelConScroll;
    }

    // Crea panel para modificar facturas existentes
    private JPanel crearPanelModificar() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(COLOR_FONDO_CENTRAL);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 0, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Inicializar componentes MODIFICAR
        txtCedulaMod = new JTextField();
        estilizarCampoTexto(txtCedulaMod);
        txtCedulaMod.setPreferredSize(new Dimension(200, 25));

        lblCodigoMod = new JLabel("");
        lblFechaMod = new JLabel("");
        lblNombreClienteMod = new JLabel("");
        lblCedulaClienteMod = new JLabel("");
        lblEmailClienteMod = new JLabel("");
        lblTelefonoClienteMod = new JLabel("");

        cmbProductoMod = new JComboBox<>();
        estilizarComboBox(cmbProductoMod);
        cmbProductoMod.setPreferredSize(new Dimension(400, 25));

        txtCantidadMod = new JTextField();
        estilizarCampoTexto(txtCantidadMod);
        txtCantidadMod.setPreferredSize(new Dimension(120, 25));
        lblErrorCantidadMod = crearLabelError();

        txtSubtotalMod = crearCampoTotal(false);
        txtIVAMod = crearCampoTotal(false);
        txtTotalMod = crearCampoTotal(true);

        int fila = 0;

        // Título
        // Título eliminado
        // gbc.gridx = 0;
        // gbc.gridy = fila++;
        // gbc.gridwidth = 8;
        // gbc.anchor = GridBagConstraints.CENTER;
        // gbc.insets = new Insets(10, 10, 15, 10);
        // panel.add(crearTituloCentrado("TITULO_MODIFICAR_FACTURA"), gbc);
        // gbc.gridwidth = 1;
        // gbc.anchor = GridBagConstraints.WEST;
        // gbc.insets = new Insets(5, 10, 0, 10);

        // Buscar cliente por cédula
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.insets = new Insets(20, 10, 5, 10);
        JLabel lblCed = new JLabel(CargadorProperties.obtenerComponentes("LBL_CEDULA"));
        estilizarLabel(lblCed);
        panel.add(lblCed, gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        panel.add(txtCedulaMod, gbc);

        gbc.gridx = 3;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(20, 5, 5, 10);
        panel.add(crearBotonLupa(() -> ejecutarBusquedaConValidacion(txtCedulaMod, modeloFacturasMod, false)), gbc);
        gbc.insets = new Insets(5, 10, 0, 10);

        fila++;
        gbc.gridx = 1;
        gbc.gridy = fila;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(-5, 10, 5, 10);
        lblErrorCedulaMod = crearLabelError();
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

        modeloFacturasMod = crearModeloTablaFacturas("modificar"); // ← USA MÉTODO AUXILIAR
        tablaFacturasMod = new JTable(modeloFacturasMod);
        estilizarTabla(tablaFacturasMod);
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
                        true, // ← EDITABLE
                        cmbProductoMod,
                        txtCantidadMod,
                        btnGuardarMod);
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
                false, null, null, null // sin búsqueda
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
        lblTitProd.setFont(FUENTE_TITULO.deriveFont(18f));
        lblTitProd.setForeground(COLOR_TEXTO);
        panel.add(lblTitProd, gbc);
        gbc.gridwidth = 1;

        // Fila: Producto | Cantidad | Agregar
        gbc.gridy = fila++;

        gbc.gridx = 0;
        JLabel lblProd = new JLabel(CargadorProperties.obtenerComponentes("LBL_PRODUCTO"));
        estilizarLabel(lblProd);
        panel.add(lblProd, gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 4;
        panel.add(cmbProductoMod, gbc);
        gbc.gridwidth = 1;

        gbc.gridx = 5;
        JLabel lblCant = new JLabel(CargadorProperties.obtenerComponentes("LBL_CANTIDAD"));
        estilizarLabel(lblCant);
        panel.add(lblCant, gbc);

        gbc.gridx = 6;
        panel.add(txtCantidadMod, gbc);

        gbc.gridx = 7;
        gbc.insets = new Insets(5, 3, 0, 10); // Reducir margen izquierdo para acercar el botón
        JButton btnAgregar = new JButton(CargadorProperties.obtenerComponentes("FC_UI_003"));
        btnAgregar.setPreferredSize(new Dimension(90, 25));
        estilizarBotonPrimario(btnAgregar);
        btnAgregar.addActionListener(e -> agregarProducto(
                cmbProductoMod, txtCantidadMod, lblErrorCantidadMod,
                modeloProductosMod, txtSubtotalMod, txtIVAMod, txtTotalMod));
        panel.add(btnAgregar, gbc);
        gbc.insets = new Insets(5, 10, 0, 10); // Restaurar insets

        // Error cantidad
        gbc.gridx = 6;
        gbc.gridy = fila++;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(-5, 10, 0, 10);
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

        modeloProductosMod = crearModeloTablaProductos(true); // ← USA MÉTODO AUXILIAR
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
        btnQuitarMod = new JButton(CargadorProperties.obtenerComponentes("FC_UI_004"));
        estilizarBotonEliminar(btnQuitarMod);
        btnQuitarMod.setPreferredSize(new Dimension(90, 25));
        btnQuitarMod.setEnabled(false); // Disabled initially
        btnQuitarMod.addActionListener(e -> quitarProducto(
                tablaProductosMod, modeloProductosMod,
                txtSubtotalMod, txtIVAMod, txtTotalMod));
        panel.add(btnQuitarMod, gbc);

        // Totales
        agregarSeccionTotales(panel, gbc, fila, txtSubtotalMod, txtIVAMod, txtTotalMod);
        fila += 3;

        // Botones (Guardar y Aprobar) en Panel dedicado alineado a la derecha
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.gridwidth = 8;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JPanel panelBotonesMod = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panelBotonesMod.setOpaque(false); // Fondo transparente

        btnGuardarModificar = new JButton(CargadorProperties.obtenerComponentes("FC_UI_006")); // "Guardar"
        estilizarBotonSecundario(btnGuardarModificar);
        btnGuardarModificar.setPreferredSize(new Dimension(100, 30));
        // btnGuardarModificar.setFont(new Font("Arial", Font.BOLD, 12));
        btnGuardarModificar.setEnabled(false); // Deshabilitado al inicio
        btnGuardarModificar.addActionListener(e -> {
            personalizarPopup();
            JOptionPane.showMessageDialog(this, CargadorProperties.obtenerMessages("FC_I_006"));
            restaurarEstilosPopup();
            limpiarPanelModificar();
            btnGuardarModificar.setEnabled(false);
        });
        panelBotonesMod.add(btnGuardarModificar);

        btnGuardarMod = new JButton(CargadorProperties.obtenerComponentes("FC_UI_005")); // "Aprobar" - REUTILIZADO
        estilizarBotonPrimario(btnGuardarMod);
        btnGuardarMod.setPreferredSize(new Dimension(100, 30));
        // btnGuardarMod.setFont(new Font("Arial", Font.BOLD, 12));
        btnGuardarMod.setEnabled(false);
        btnGuardarMod.addActionListener(e -> modificarFactura());
        panelBotonesMod.add(btnGuardarMod);

        panel.add(panelBotonesMod, gbc);

        configurarValidacionesMod();

        JPanel panelConScroll = new JPanel(new BorderLayout());
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(null);
        panelConScroll.add(scrollPane, BorderLayout.CENTER);

        return panelConScroll;
    }

    // Crea panel para eliminar facturas
    private JPanel crearPanelEliminar() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(COLOR_FONDO_CENTRAL);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 0, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Inicializar componentes
        txtCedulaElim = new JTextField();
        estilizarCampoTexto(txtCedulaElim);
        txtCedulaElim.setPreferredSize(new Dimension(200, 25));

        JLabel lblErrorCedulaElim = crearLabelError();

        // Configurar validación en tiempo real usando método auxiliar
        txtCedulaElim.getDocument().addDocumentListener(
                crearValidadorTiempoReal(txtCedulaElim, lblErrorCedulaElim, "cedula"));

        int fila = 0;

        // Título
        // Título eliminado
        // gbc.gridx = 0;
        // gbc.gridy = fila++;
        // gbc.gridwidth = 4;
        // gbc.anchor = GridBagConstraints.CENTER;
        // gbc.insets = new Insets(10, 10, 15, 10);
        // panel.add(crearTituloCentrado("TITULO_ELIMINAR_FACTURA"), gbc);
        // gbc.gridwidth = 1;
        // gbc.anchor = GridBagConstraints.WEST;
        // gbc.insets = new Insets(5, 10, 0, 10);

        // Fila: Cédula + Lupa
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.insets = new Insets(20, 10, 5, 10);
        JLabel lblCed = new JLabel(CargadorProperties.obtenerComponentes("LBL_CEDULA"));
        estilizarLabel(lblCed);
        panel.add(lblCed, gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        panel.add(txtCedulaElim, gbc);

        gbc.gridx = 3;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(20, 5, 5, 10);
        panel.add(crearBotonLupa(() -> ejecutarBusquedaConValidacion(txtCedulaElim, modeloFacturasElim, false)), gbc);

        // Error cédula
        fila++;
        gbc.gridx = 1;
        gbc.gridy = fila;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(-5, 10, 5, 10);
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

        modeloFacturasElim = crearModeloTablaFacturas("completa"); // ← USA MÉTODO AUXILIAR
        tablaFacturasElim = new JTable(modeloFacturasElim);
        estilizarTabla(tablaFacturasElim);
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
        estilizarBotonEliminar(btnEliminar);
        // btnEliminar.setFont(new Font("Arial", Font.BOLD, 12));
        btnEliminar.setEnabled(false); // Deshabilitado hasta seleccionar factura
        btnEliminar.addActionListener(e -> eliminarFactura());
        panel.add(btnEliminar, gbc);

        return panel;
    }

    // Método para crear el panel de paginación
    private JPanel crearPanelPaginacion() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        panel.setBackground(COLOR_FONDO_CENTRAL);

        btnPrimero = crearBotonPaginacion("primero.png");
        btnAnterior = crearBotonPaginacion("anterior.png");
        btnSiguiente = crearBotonPaginacion("siguiente.png");
        btnUltimo = crearBotonPaginacion("ultimo.png");

        btnPrimero.addActionListener(e -> {
            paginaActual = 0;
            actualizarTablaPaginada();
        });
        btnAnterior.addActionListener(e -> {
            if (paginaActual > 0) {
                paginaActual--;
                actualizarTablaPaginada();
            }
        });
        btnSiguiente.addActionListener(e -> {
            if (facturasPaginadasTotal != null
                    && (paginaActual + 1) * FILAS_POR_PAGINA < facturasPaginadasTotal.size()) {
                paginaActual++;
                actualizarTablaPaginada();
            }
        });
        btnUltimo.addActionListener(e -> {
            if (facturasPaginadasTotal != null && !facturasPaginadasTotal.isEmpty()) {
                paginaActual = (int) Math.ceil((double) facturasPaginadasTotal.size() / FILAS_POR_PAGINA) - 1;
                if (paginaActual < 0)
                    paginaActual = 0;
                actualizarTablaPaginada();
            }
        });

        panel.add(btnPrimero);
        panel.add(btnAnterior);
        panel.add(btnSiguiente);
        panel.add(btnUltimo);

        return panel;
    }

    private JButton crearBotonPaginacion(String iconName) {
        JButton btn = new JButton();
        btn.setPreferredSize(new Dimension(50, 35)); // Tamaño uniforme y rectangular/redondeado
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBackground(COLOR_TEXTO); // Fondo Azul solicitado
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder());
        btn.setContentAreaFilled(false); // Importante para bordes redondeados personalizados

        // Cargar icono con escalado
        try {
            java.net.URL url = getClass().getResource("/resources/img/" + iconName);
            if (url != null) {
                ImageIcon icon = new ImageIcon(url);
                // Escalar icono un poco más pequeño para que quepa en el botón
                Image img = icon.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
                btn.setIcon(new ImageIcon(img));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // UI Personalizada para fondo redondeado y hover
        btn.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                JButton button = (JButton) c;
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Colores
                if (!button.isEnabled()) {
                    g2.setColor(new Color(200, 200, 200)); // Gris si deshabilitado
                } else if (button.getModel().isRollover()) {
                    g2.setColor(button.getBackground().brighter()); // Más claro al pasar mouse
                } else {
                    g2.setColor(button.getBackground()); // Color normal
                }

                // Dibujar fondo redondeado
                g2.fillRoundRect(0, 0, button.getWidth(), button.getHeight(), 10, 10);

                g2.dispose();

                // Pintar icono centrado (super.paint llamará a paintIcon)
                super.paint(g, c);
            }
        });

        return btn;
    }

    // Actualiza la tabla con los datos de la página actual
    private void actualizarTablaPaginada() {
        modeloTablaResultados.setRowCount(0);

        if (facturasPaginadasTotal == null || facturasPaginadasTotal.isEmpty()) {
            if (btnPrimero != null) {
                btnPrimero.setEnabled(false);
                btnAnterior.setEnabled(false);
                btnSiguiente.setEnabled(false);
                btnUltimo.setEnabled(false);
            }
            return;
        }

        // Determinar filas por página según el tipo de consulta
        int filasPorPagina = 18; // Default (General)
        // Si el panel de búsqueda por cédula es visible, es Específica -> 16 filas
        if (panelBusqueda != null && panelBusqueda.isVisible()) {
            filasPorPagina = 16;
        }

        int totalRegistros = facturasPaginadasTotal.size();
        int totalPaginas = (int) Math.ceil((double) totalRegistros / filasPorPagina);

        if (paginaActual >= totalPaginas)
            paginaActual = totalPaginas - 1;
        if (paginaActual < 0)
            paginaActual = 0;

        int inicio = paginaActual * filasPorPagina;
        int fin = Math.min(inicio + filasPorPagina, totalRegistros);

        for (int i = inicio; i < fin; i++) {
            Factura f = facturasPaginadasTotal.get(i);

            // Buscar nombre cliente para mostrar si no está cargado
            String nombreCliente = "N/A";
            Cliente cli = new Cliente();
            Cliente c = cli.verificarPorIdDP(f.getCodigoCliente());
            if (c != null)
                nombreCliente = c.getNombre();

            modeloTablaResultados.addRow(new Object[] {
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

        // Actualizar estado botones
        if (btnPrimero != null) {
            btnPrimero.setEnabled(paginaActual > 0);
            btnAnterior.setEnabled(paginaActual > 0);
            btnSiguiente.setEnabled(paginaActual < totalPaginas - 1);
            btnUltimo.setEnabled(paginaActual < totalPaginas - 1);
        }
    }

    private JPanel crearPanelConsultar() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(COLOR_FONDO_CENTRAL);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel superior: Tipo de consulta (Sin título)
        // Panel superior: Tipo de consulta (Sin título)
        JPanel panelTipo = new JPanel(new GridBagLayout()); // Cambiado a GridBagLayout para alinear verticalmente
        panelTipo.setBackground(COLOR_FONDO_CENTRAL);
        GridBagConstraints gbcTipo = new GridBagConstraints();
        gbcTipo.insets = new Insets(5, 0, 5, 0);

        String[] opcionesTipo = CargadorProperties.obtenerComponentes("CMB_TIPO_CONSULTA").split(",");
        comboTipoConsulta = new JComboBox<>(opcionesTipo);
        estilizarComboBox(comboTipoConsulta);
        comboTipoConsulta.setPreferredSize(new Dimension(250, 30));
        comboTipoConsulta.addActionListener(e -> cambiarTipoConsulta());

        gbcTipo.gridx = 0;
        gbcTipo.gridy = 0;
        panelTipo.add(comboTipoConsulta, gbcTipo);

        // Inicializar panel paginación (se agregará al CENTRO, debajo de la tabla)
        panelPaginacion = crearPanelPaginacion();
        panelPaginacion.setVisible(false); // Oculto inicialmente

        panel.add(panelTipo, BorderLayout.NORTH);

        // Panel central: búsqueda + tabla facturas + detalle (USANDO GridBagLayout para
        // control total)
        JPanel panelCentral = new JPanel(new GridBagLayout());
        panelCentral.setBackground(COLOR_FONDO_CENTRAL);
        GridBagConstraints gbcC = new GridBagConstraints();
        gbcC.fill = GridBagConstraints.HORIZONTAL;
        gbcC.weightx = 1.0;
        gbcC.insets = new Insets(0, 0, 5, 0);

        // Panel de búsqueda (inicialmente oculto)
        panelBusqueda = new JPanel(new GridBagLayout());
        panelBusqueda.setBackground(COLOR_FONDO_CENTRAL);
        GridBagConstraints gbcBusq = new GridBagConstraints();
        gbcBusq.insets = new Insets(5, 5, 0, 5);

        txtCedulaConsulta = new JTextField();
        estilizarCampoTexto(txtCedulaConsulta);
        txtCedulaConsulta.setPreferredSize(new Dimension(250, 25));

        lblErrorCedulaConsulta = crearLabelError();

        JLabel lblCed = new JLabel(CargadorProperties.obtenerComponentes("LBL_CEDULA"));
        estilizarLabel(lblCed);

        // Fila 1: Label + Campo + Lupa
        gbcBusq.gridx = 0;
        gbcBusq.gridy = 0;
        gbcBusq.anchor = GridBagConstraints.EAST;
        panelBusqueda.add(lblCed, gbcBusq);

        gbcBusq.gridx = 1;
        gbcBusq.anchor = GridBagConstraints.WEST;
        panelBusqueda.add(txtCedulaConsulta, gbcBusq);

        gbcBusq.gridx = 2;
        panelBusqueda.add(
                crearBotonLupa(() -> ejecutarBusquedaConValidacion(txtCedulaConsulta, modeloTablaResultados, true)),
                gbcBusq);

        // Fila 2: Error (justo debajo del campo)
        gbcBusq.gridx = 1;
        gbcBusq.gridy = 1;
        gbcBusq.insets = new Insets(-5, 5, 0, 5); // Margen inferior eliminado
        gbcBusq.anchor = GridBagConstraints.WEST;
        panelBusqueda.add(lblErrorCedulaConsulta, gbcBusq);

        panelBusqueda.setVisible(false);

        gbcC.gridy = 0;
        gbcC.weighty = 0.0;
        panelCentral.add(panelBusqueda, gbcC);

        // Tabla de facturas (8 columnas - completa)
        modeloTablaResultados = crearModeloTablaFacturas("completa");
        tablaResultados = new JTable(modeloTablaResultados);
        estilizarTabla(tablaResultados);
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
                        false, // NO editable
                        null, null, null);
            }
        });

        scrollTablaResultados = new JScrollPane(tablaResultados);
        scrollTablaResultados.setBorder(BorderFactory.createEmptyBorder()); // Eliminar borde visual
        scrollTablaResultados.getViewport().setBackground(COLOR_FONDO_CENTRAL); // Fondo integrado
        scrollTablaResultados.setPreferredSize(new Dimension(900, 500)); // Aumentar un poco altura para 20 filas
        gbcC.gridy = 1;
        gbcC.weighty = 0.3; // Darle peso para que se mantenga grande
        gbcC.fill = GridBagConstraints.BOTH;
        panelCentral.add(scrollTablaResultados, gbcC);

        // Panel de Paginación (Debajo de la tabla)
        gbcC.gridy = 2;
        gbcC.weighty = 0.0;
        gbcC.fill = GridBagConstraints.NONE; // No estirar
        gbcC.anchor = GridBagConstraints.NORTH; // Pegar arriba
        gbcC.insets = new Insets(0, 0, 10, 0); // Reducir espacio superior
        panelCentral.add(panelPaginacion, gbcC);

        // Panel de detalle (inicialmente oculto)
        panelDetalleConsulta = crearPanelDetalleConsulta();
        panelDetalleConsulta.setVisible(false);
        gbcC.gridy = 3;
        gbcC.weighty = 0.7; // El detalle también toma espacio si es necesario
        gbcC.fill = GridBagConstraints.BOTH; // Restaurar fill
        panelCentral.add(panelDetalleConsulta, gbcC);

        panel.add(panelCentral, BorderLayout.CENTER);

        configurarValidacionesConsultar();

        // Envolver todo en un scroll general para que nada se achique
        JPanel panelConScroll = new JPanel(new BorderLayout());
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(null);
        panelConScroll.add(scrollPane, BorderLayout.CENTER);

        return panelConScroll;
    }

    // Crear panel de detalle para consulta
    private JPanel crearPanelDetalleConsulta() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(COLOR_FONDO_CENTRAL);
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

        // Botón Cerrar Detalle (en la esquina superior derecha)
        gbc.gridx = 0;
        gbc.gridy = fila++;
        gbc.gridwidth = 8;
        gbc.anchor = GridBagConstraints.EAST;
        JButton btnCerrarDetalle = new JButton(CargadorProperties.obtenerComponentes("FC_UI_007"));
        estilizarBotonSecundario(btnCerrarDetalle);
        btnCerrarDetalle.addActionListener(e -> {
            panelDetalleConsulta.setVisible(false);
            if (scrollTablaResultados != null) {
                scrollTablaResultados.setPreferredSize(new Dimension(900, 450));
                scrollTablaResultados.revalidate();
                scrollTablaResultados.repaint();
            }
            if (panelPaginacion != null) {
                panelPaginacion.setVisible(true);
            }
        });
        panel.add(btnCerrarDetalle, gbc);

        gbc.gridwidth = 1; // Restaurar
        gbc.anchor = GridBagConstraints.CENTER; // Restaurar anchor

        // Cabecera de la factura seleccionada
        gbc.gridx = 0;
        gbc.gridy = fila++;
        gbc.gridwidth = 8;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(crearCabeceraFactura(
                lblCodigoConsulta, lblFechaConsulta,
                lblNombreClienteConsulta, lblCedulaClienteConsulta,
                lblEmailClienteConsulta, lblTelefonoClienteConsulta,
                false, null, null, null // sin búsqueda
        ), gbc);
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;

        // Título productos
        gbc.gridx = 0;
        gbc.gridy = fila++;
        gbc.gridwidth = 8;
        JLabel lblTitProd = new JLabel(CargadorProperties.obtenerComponentes("TITULO_PRODUCTOS_FACTURA"));
        lblTitProd.setFont(FUENTE_TITULO.deriveFont(18f));
        lblTitProd.setForeground(COLOR_TEXTO);
        panel.add(lblTitProd, gbc);

        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.CENTER; // Restaurar anchor

        // Tabla productos (NO EDITABLE)
        gbc.gridx = 0;
        gbc.gridy = fila++;
        gbc.gridwidth = 8;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(10, 10, 10, 10);

        modeloProductosConsulta = crearModeloTablaProductos(false); // ← USA EL MÉTODO AUXILIAR
        tablaProductosConsulta = new JTable(modeloProductosConsulta);
        configurarTablaProductos(modeloProductosConsulta, tablaProductosConsulta, false,
                null, null, null); // sin totales automáticos

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

    // **Configuración de tablas**
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

    private void ejecutarBusquedaConValidacion(JTextField campo, DefaultTableModel modelo, boolean busquedaGeneral) {
        String texto = campo.getText().trim();
        String error = ValidacionesFactura.validarCedulaInput(texto);

        if (error != null) {
            mostrarMensaje(error, CargadorProperties.obtenerMessages("FC_C_006"), JOptionPane.ERROR_MESSAGE);
            return;
        }
        buscarYCargarFacturas(campo, modelo, busquedaGeneral);
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
        Cliente clienteBuscado = cli.verificarDP(cedula);

        if (clienteBuscado == null) {
            personalizarPopup();
            JOptionPane.showMessageDialog(this,
                    CargadorProperties.obtenerMessages("FC_A_004"),
                    CargadorProperties.obtenerMessages("FC_C_006"),
                    JOptionPane.INFORMATION_MESSAGE);
            restaurarEstilosPopup();
            modelo.setRowCount(0);
            if (modelo == modeloTablaResultados) {
                facturasPaginadasTotal.clear();
                actualizarTablaPaginada();
            }
            return;
        }

        // Validar estado del cliente (INA)
        String errorEstado = ValidacionesFactura.validarEstadoCliente(clienteBuscado);
        if (errorEstado != null) {
            personalizarPopup();
            JOptionPane.showMessageDialog(this,
                    errorEstado,
                    CargadorProperties.obtenerMessages("FC_C_006"),
                    JOptionPane.INFORMATION_MESSAGE);
            restaurarEstilosPopup();
            modelo.setRowCount(0);
            // Si es la tabla de consulta, limpiar paginación
            if (modelo == modeloTablaResultados) {
                facturasPaginadasTotal.clear();
                actualizarTablaPaginada();
            }
            return;
        }

        // Buscar facturas (APR o todas)
        Factura fac = new Factura();
        ArrayList<Factura> lista = incluirAnuladas
                ? fac.consultarTodasPorParametro(clienteBuscado) // APR + ANU
                : fac.consultarPorParametro(clienteBuscado); // solo APR

        // Limpiar tabla
        modelo.setRowCount(0);

        // Validar resultados
        if (lista == null || lista.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    CargadorProperties.obtenerMessages("FC_A_002"),
                    CargadorProperties.obtenerMessages("FC_C_006"),
                    JOptionPane.INFORMATION_MESSAGE);
            // Limpiar paginación si no hay resultados y es la tabla de consulta
            if (modelo == modeloTablaResultados) {
                facturasPaginadasTotal.clear();
                actualizarTablaPaginada();
            }
            return;
        }

        // LOGICA PAGINACIÓN PARA CONSULTA
        if (modelo == modeloTablaResultados) {
            facturasPaginadasTotal = lista;
            paginaActual = 0;
            actualizarTablaPaginada();
            return; // Salir, ya que actualizarTablaPaginada se encarga de llenar el modelo
        }

        // Obtener nombre del cliente
        String nombreCliente = (clienteBuscado != null) ? clienteBuscado.getNombre() : "N/A";

        // Cargar en tabla según número de columnas (LÓGICA ORIGINAL PARA
        // MODIFICAR/ELIMINAR)
        int numColumnas = modelo.getColumnCount();

        for (Factura f : lista) {
            if (numColumnas == 3) {
                // Tabla MODIFICAR (3 columnas)
                modelo.addRow(new Object[] {
                        f.getCodigo(),
                        f.getFechaHora() != null ? f.getFechaHora().format(FMT_FECHA) : "",
                        nombreCliente
                });
            } else {
                // Tabla ELIMINAR (8 columnas)
                modelo.addRow(new Object[] {
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
                return editable && column == 3; // Solo columna Cantidad si es editable
            }

            @Override
            public Class<?> getColumnClass(int column) {
                if (column == 3)
                    return String.class;
                return String.class;
            }
        };
    }

    // Configura la tabla con listener para recalcular totales si es editable
    private void configurarTablaProductos(DefaultTableModel modelo, JTable tabla,
            boolean editable,
            JTextField txtSub, JTextField txtIva, JTextField txtTotal) {
        estilizarTabla(tabla);

        // Si es editable, agregar listener para recalcular totales
        if (editable && txtSub != null && txtIva != null && txtTotal != null) {
            modelo.addTableModelListener(new TableModelListener() {
                @Override
                public void tableChanged(TableModelEvent e) {
                    if (e.getType() == TableModelEvent.UPDATE && e.getColumn() == 3) {
                        validarYActualizarCantidad(modelo, txtSub, txtIva, txtTotal, e.getFirstRow());
                    }
                }
            });
        }
    }

    // **METODOS AUXILIARES UI**
    // Titulo de ventana centrado (ELIMINADO)
    // private JLabel crearTituloCentrado(String textoKey) {
    // JLabel lblTitulo = new
    // JLabel(CargadorProperties.obtenerComponentes(textoKey));
    // lblTitulo.setFont(FUENTE_TITULO);
    // lblTitulo.setForeground(COLOR_PRIMARIO);
    // return lblTitulo;
    // }

    private JPanel crearCabeceraFactura(JLabel lblCodigo, JLabel lblFecha,
            JLabel lblNombreCliente, JLabel lblCedulaCliente,
            JLabel lblEmailCliente, JLabel lblTelefonoCliente,
            boolean mostrarBusqueda, JTextField txtCedulaBusqueda,
            JLabel lblErrorCedula, Runnable accionBusqueda) {
        JPanel panelCabecera = new JPanel(new BorderLayout(20, 10));
        panelCabecera.setBackground(COLOR_FONDO_CENTRAL);
        panelCabecera.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // PANEL IZQUIERDO: Cliente
        JPanel panelCliente = new JPanel();
        panelCliente.setOpaque(false);
        panelCliente.setLayout(new BoxLayout(panelCliente, BoxLayout.Y_AXIS));

        JLabel lblTitCliente = new JLabel(CargadorProperties.obtenerComponentes("LBL_INFO_CLIENTE"));
        lblTitCliente.setFont(FUENTE_TITULO.deriveFont(16f));
        lblTitCliente.setForeground(COLOR_TEXTO);
        lblTitCliente.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelCliente.add(lblTitCliente);
        panelCliente.add(Box.createVerticalStrut(5));

        // Si tiene búsqueda (panel crear)
        if (mostrarBusqueda && txtCedulaBusqueda != null && accionBusqueda != null) {
            JPanel pBusquedaContainer = new JPanel(new GridBagLayout());
            pBusquedaContainer.setOpaque(false);
            GridBagConstraints gbcB = new GridBagConstraints();
            gbcB.insets = new Insets(0, 5, 0, 5);
            gbcB.fill = GridBagConstraints.NONE;

            // Fila 0: Label, Field, Lupa
            gbcB.gridy = 0;

            gbcB.gridx = 0;
            JLabel lblCedRucTit = new JLabel(CargadorProperties.obtenerComponentes("LBL_CEDULA_RUC"));
            estilizarLabel(lblCedRucTit);
            pBusquedaContainer.add(lblCedRucTit, gbcB);

            gbcB.gridx = 1;
            pBusquedaContainer.add(txtCedulaBusqueda, gbcB);

            gbcB.gridx = 2;
            pBusquedaContainer.add(crearBotonLupa(accionBusqueda), gbcB);

            // Fila 1: Mensaje de error (justo debajo del campo)
            if (lblErrorCedula != null) {
                gbcB.gridy = 1;
                gbcB.gridx = 1;
                gbcB.anchor = GridBagConstraints.WEST;
                gbcB.insets = new Insets(-2, 5, 0, 5); // Un pequeño ajuste hacia arriba para pegarlo al campo
                pBusquedaContainer.add(lblErrorCedula, gbcB);
            }

            pBusquedaContainer.setAlignmentX(Component.CENTER_ALIGNMENT);
            panelCliente.add(pBusquedaContainer);
            panelCliente.add(Box.createVerticalStrut(3));
        }

        // Información del cliente
        agregarCampoInfoClienteCabecera(panelCliente, "FC_UI_008", lblNombreCliente);
        agregarCampoInfoClienteCabecera(panelCliente, "FC_UI_002", lblCedulaCliente);
        agregarCampoInfoClienteCabecera(panelCliente, "FC_UI_009", lblEmailCliente);
        agregarCampoInfoClienteCabecera(panelCliente, "FC_UI_010", lblTelefonoCliente);

        // PANEL CENTRO: Empresa
        JPanel panelEmpresa = new JPanel();
        panelEmpresa.setOpaque(false);
        panelEmpresa.setLayout(new BoxLayout(panelEmpresa, BoxLayout.Y_AXIS));
        panelEmpresa.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 50));

        // Título "Información de la Empresa"
        JLabel lblTitEmpresa = new JLabel(CargadorProperties.obtenerComponentes("LBL_INFO_EMPRESA"));
        lblTitEmpresa.setFont(FUENTE_TITULO.deriveFont(16f));
        lblTitEmpresa.setForeground(COLOR_TEXTO);
        lblTitEmpresa.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelEmpresa.add(lblTitEmpresa);
        panelEmpresa.add(Box.createVerticalStrut(10));

        // Empresa info: Nombre, Email, Telefono con labels bold
        JPanel pNombre = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
        pNombre.setOpaque(false);
        JLabel lblNombreTit = new JLabel(CargadorProperties.obtenerComponentes("FC_UI_008"));
        lblNombreTit.setFont(FUENTE_LABEL.deriveFont(Font.BOLD));
        lblNombreTit.setForeground(COLOR_TEXTO);
        pNombre.add(lblNombreTit);
        JLabel lblNombreVal = new JLabel(CargadorProperties.obtenerComponentes("EMPRESA_NOMBRE"));
        estilizarLabel(lblNombreVal);
        pNombre.add(lblNombreVal);
        pNombre.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelEmpresa.add(pNombre);
        panelEmpresa.add(Box.createVerticalStrut(8));

        JPanel pEmail = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        pEmail.setOpaque(false);
        JLabel lblEmailTit = new JLabel(CargadorProperties.obtenerComponentes("FC_UI_009"));
        lblEmailTit.setFont(FUENTE_LABEL.deriveFont(Font.BOLD));
        lblEmailTit.setForeground(COLOR_TEXTO);
        pEmail.add(lblEmailTit);
        JLabel lblEmailVal = new JLabel(CargadorProperties.obtenerComponentes("EMPRESA_EMAIL"));
        estilizarLabel(lblEmailVal);
        pEmail.add(lblEmailVal);
        pEmail.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelEmpresa.add(pEmail);
        panelEmpresa.add(Box.createVerticalStrut(8));

        JPanel pTel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        pTel.setOpaque(false);
        JLabel lblTelTit = new JLabel(CargadorProperties.obtenerComponentes("FC_UI_010"));
        lblTelTit.setFont(FUENTE_LABEL.deriveFont(Font.BOLD));
        lblTelTit.setForeground(COLOR_TEXTO);
        pTel.add(lblTelTit);
        JLabel lblTelVal = new JLabel(CargadorProperties.obtenerComponentes("EMPRESA_TELEFONO"));
        estilizarLabel(lblTelVal);
        pTel.add(lblTelVal);
        pTel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelEmpresa.add(pTel);
        panelEmpresa.add(Box.createVerticalGlue());

        // PANEL DERECHO: Código y Fecha
        JPanel panelFactura = new JPanel();
        panelFactura.setOpaque(false);
        panelFactura.setLayout(new BoxLayout(panelFactura, BoxLayout.Y_AXIS));

        // Título "Información de la Factura"
        JLabel lblTitFactura = new JLabel(CargadorProperties.obtenerComponentes("LBL_INFO_FACTURA"));
        lblTitFactura.setFont(FUENTE_TITULO.deriveFont(16f));
        lblTitFactura.setForeground(COLOR_TEXTO);
        lblTitFactura.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelFactura.add(lblTitFactura);
        panelFactura.add(Box.createVerticalStrut(10));

        if (lblCodigo != null) {
            // "Código:" debe estar en bold
            lblCodigo.setFont(FUENTE_LABEL.deriveFont(Font.BOLD));
            lblCodigo.setForeground(COLOR_TEXTO);
            lblCodigo.setAlignmentX(Component.CENTER_ALIGNMENT);
            panelFactura.add(lblCodigo);
            panelFactura.add(Box.createVerticalStrut(8));
        }

        // Removed - fecha handling is now in the agregarcampo

        // Agregar "Fecha y hora:" como label y el valor por separado
        JPanel pFecha = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        pFecha.setOpaque(false);
        JLabel lblFechaTit = new JLabel(CargadorProperties.obtenerComponentes("LBL_FECHA_HORA"));
        lblFechaTit.setFont(FUENTE_LABEL.deriveFont(Font.BOLD));
        lblFechaTit.setForeground(COLOR_TEXTO);
        pFecha.add(lblFechaTit);

        if (lblFecha != null) {
            estilizarLabel(lblFecha);
            pFecha.add(lblFecha);
        } else {
            // Si no hay label de fecha (para panel crear), mostrar fecha actual
            LocalDateTime ahora = LocalDateTime.now();
            String fechaHora = ahora.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
            JLabel lblFechaActual = new JLabel(fechaHora);
            estilizarLabel(lblFechaActual);
            pFecha.add(lblFechaActual);
        }
        pFecha.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelFactura.add(pFecha);
        panelFactura.add(Box.createVerticalGlue());

        // Ensamblar cabecera: Cliente IZQUIERDA, Empresa CENTRO, Factura DERECHA
        panelCabecera.add(panelCliente, BorderLayout.WEST);
        panelCabecera.add(panelEmpresa, BorderLayout.CENTER);
        panelCabecera.add(panelFactura, BorderLayout.EAST);

        return panelCabecera;
    }

    // Agregar campos de info cliente en cabecera
    private void agregarCampoInfoClienteCabecera(JPanel panel, String keyLabel, JLabel lblValor) {
        JPanel pCampo = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        pCampo.setOpaque(false);
        JLabel lblTit = new JLabel(CargadorProperties.obtenerComponentes(keyLabel));
        lblTit.setFont(FUENTE_LABEL.deriveFont(Font.BOLD));
        lblTit.setForeground(COLOR_TEXTO);
        pCampo.add(lblTit);

        if (lblValor != null) {
            estilizarLabel(lblValor);
            pCampo.add(lblValor);
        }

        panel.add(pCampo);
    }

    // Boton buscar
    private JLabel crearBotonLupa(Runnable accion) {
        JLabel lupa = new JLabel(CargadorProperties.obtenerComponentes("BTN_LUPA"));
        lupa.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        lupa.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lupa.setPreferredSize(new Dimension(35, 25));
        lupa.setHorizontalAlignment(SwingConstants.CENTER);
        lupa.setOpaque(false); // Fondo transparente
        lupa.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                accion.run();
            }
        });
        return lupa;
    }

    // Estilizar Label estándar
    private void estilizarLabel(JLabel lbl) {
        lbl.setFont(FUENTE_LABEL);
        lbl.setForeground(COLOR_TEXTO);
    }

    // Crear label con prefijo en bold y valor en regular (usando HTML)
    private void configurarLabelConPrefijoBold(JLabel lbl, String prefijo, String valor) {
        String html = "<html><b>" + prefijo + "</b> " + valor + "</html>";
        lbl.setText(html);
        lbl.setFont(FUENTE_LABEL);
        lbl.setForeground(COLOR_TEXTO);
    }

    // Seccion de Totales (REFACTORIZADO en su propio panel para evitar saltos)
    private void agregarSeccionTotales(JPanel panel, GridBagConstraints gbc,
            int filaInicio, JTextField txtSub,
            JTextField txtIva, JTextField txtTotal) {

        gbc.gridx = 0;
        gbc.gridy = filaInicio;
        gbc.gridwidth = 8;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;

        JPanel panelTotales = new JPanel(new GridBagLayout());
        panelTotales.setOpaque(false);
        GridBagConstraints gbcT = new GridBagConstraints();
        gbcT.insets = new Insets(2, 5, 2, 0); // Spacing reducido
        gbcT.anchor = GridBagConstraints.EAST;

        // Subtotal
        gbcT.gridx = 0;
        gbcT.gridy = 0;
        JLabel lblSubtotal = new JLabel(CargadorProperties.obtenerComponentes("LBL_SUBTOTAL"));
        estilizarLabel(lblSubtotal);
        panelTotales.add(lblSubtotal, gbcT);
        gbcT.gridx = 1;
        panelTotales.add(txtSub, gbcT);

        // IVA
        gbcT.gridx = 0;
        gbcT.gridy = 1;
        JLabel lblIva = new JLabel(CargadorProperties.obtenerComponentes("LBL_IVA"));
        estilizarLabel(lblIva);
        panelTotales.add(lblIva, gbcT);
        gbcT.gridx = 1;
        panelTotales.add(txtIva, gbcT);

        // Total
        gbcT.gridx = 0;
        gbcT.gridy = 2;
        JLabel lblTot = new JLabel(CargadorProperties.obtenerComponentes("LBL_TOTAL"));
        lblTot.setFont(FUENTE_TITULO.deriveFont(14f));
        lblTot.setForeground(COLOR_TEXTO);
        panelTotales.add(lblTot, gbcT);
        gbcT.gridx = 1;
        panelTotales.add(txtTotal, gbcT);

        panel.add(panelTotales, gbc);
        gbc.gridwidth = 1; // Restaurar
    }

    // Muestra el total inicial
    private JTextField crearCampoTotal(boolean esBold) {
        JTextField txt = new JTextField(CargadorProperties.obtenerComponentes("TOTAL_INICIAL"));
        txt.setEditable(false);
        txt.setPreferredSize(new Dimension(120, 25));
        estilizarCampoTexto(txt);
        if (esBold) {
            txt.setFont(FUENTE_BOTON); // Usar fuente bold
        }
        return txt;
    }

    // Crea label para mostrar mensajes de error
    private JLabel crearLabelError() {
        JLabel lbl = new JLabel(" ");
        lbl.setForeground(COLOR_ACENTO);
        lbl.setFont(new Font("Poppins", Font.PLAIN, 11));
        lbl.setPreferredSize(new Dimension(200, 30)); // Altura fija para evitar que los botones se muevan
        lbl.setVerticalAlignment(SwingConstants.TOP);
        return lbl;
    }

    // Cambia entre paneles según la opción del combo
    private void cambiarPanel() {
        String opcion = (String) comboOpciones.getSelectedItem();
        String[] opciones = CargadorProperties.obtenerComponentes("CMB_OPERACION").split(",");

        if (opcion.equals(opciones[1])) {
            lblTituloSuperior.setText(CargadorProperties.obtenerComponentes("TITULO_CREAR_FACTURA"));
            limpiarPanelCrear();
            cardLayout.show(panelContenedor, PANEL_CREAR);
        } else if (opcion.equals(opciones[2])) {
            lblTituloSuperior.setText(CargadorProperties.obtenerComponentes("TITULO_MODIFICAR_FACTURA"));
            limpiarPanelModificar();
            cardLayout.show(panelContenedor, PANEL_MODIFICAR);
        } else if (opcion.equals(opciones[3])) {
            lblTituloSuperior.setText(CargadorProperties.obtenerComponentes("TITULO_ELIMINAR_FACTURA"));
            limpiarPanelEliminar();
            cardLayout.show(panelContenedor, PANEL_ELIMINAR);
        } else if (opcion.equals(opciones[4])) {
            lblTituloSuperior.setText(CargadorProperties.obtenerComponentes("TITULO_CONSULTAR_FACTURA"));
            cardLayout.show(panelContenedor, PANEL_CONSULTAR);
            limpiarPanelConsultar();
        } else {
            lblTituloSuperior.setText("");
            cardLayout.show(panelContenedor, PANEL_VACIO);
        }
    }

    // Vuelve al panel vacío
    private void volverAlMenu() {
        this.dispose();
        new MenuPrincipal().setVisible(true);
    }

    // **Validaciones en tiempo real**
    // Validacion de crear
    private void configurarValidacionesCrear() {
        // Cédula
        txtCedulaCrear.getDocument().addDocumentListener(
                crearValidadorTiempoReal(txtCedulaCrear, lblErrorCedulaCrear, "cedula"));

        // Cantidad
        txtCantidadCrear.getDocument().addDocumentListener(
                crearValidadorTiempoReal(txtCantidadCrear, lblErrorCantidadCrear, "cantidad"));
    }

    private void configurarValidacionesMod() {
        txtCedulaMod.getDocument().addDocumentListener(
                crearValidadorTiempoReal(txtCedulaMod, lblErrorCedulaMod, "cedula"));
        txtCantidadMod.getDocument().addDocumentListener(
                crearValidadorTiempoReal(txtCantidadMod, lblErrorCantidadMod, "cantidad"));
    }

    private void configurarValidacionesConsultar() {
        txtCedulaConsulta.getDocument().addDocumentListener(
                crearValidadorTiempoReal(txtCedulaConsulta, lblErrorCedulaConsulta, "cedula"));
    }

    private DocumentListener crearValidadorTiempoReal(JTextField campo,
            JLabel lblError,
            String tipo) {
        return new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                validar();
            }

            public void removeUpdate(DocumentEvent e) {
                validar();
            }

            public void changedUpdate(DocumentEvent e) {
                validar();
            }

            private void validar() {
                String texto = campo.getText().trim();

                if (texto.isEmpty()) {
                    mostrarError(lblError, null);
                    return;
                }

                String error = null;
                switch (tipo) {
                    case "cedula":
                        // Validación en tiempo real más permisiva
                        error = ValidacionesFactura.validarCedulaInput(texto);
                        break;
                    case "cantidad":
                        error = ValidacionesFactura.validarCantidad(texto);
                        break;
                }
                mostrarError(lblError, error);
            }
        };
    }

    // Valida y actualiza cantidad editada en tabla (con POPUP)
    private void validarYActualizarCantidad(DefaultTableModel modelo, JTextField txtSub, JTextField txtIva,
            JTextField txtTot, int fila) {
        try {
            Object valor = modelo.getValueAt(fila, 3);
            int cantidad = Integer.parseInt(valor.toString());

            if (cantidad <= 0) {
                mostrarMensaje(
                        CargadorProperties.obtenerMessages("FC_A_023"),
                        CargadorProperties.obtenerMessages("FC_C_004"),
                        JOptionPane.ERROR_MESSAGE);
                modelo.setValueAt(productosFactura.get(fila).getCantidad(), fila, 3);
                return;
            }

            ProxFac pxf = productosFactura.get(fila);
            Producto prod = new Producto();
            Producto productoCompleto = prod.verificarPorCodigoDP(pxf.getCodigoProd());

            if (productoCompleto != null) {
                // Validar stock (considerando que si estamos editando, la cantidad actual YA
                // está
                // reservada en memoria, pero NO restada del stock si es consulta fresca)
                // Simplificación: Validar si la NUEVA cantidad excede el stock disponible
                // real.
                // Stock disponible en BD = Saldo Final.
                // Si la factura ya existe (Modify), los items ya restaron stock?
                // Depende de la lógica de negocio. Asumiremos validación simple: nueva cantidad
                // vs saldo final.

                String errorStock = ValidacionesFactura.validarStock(cantidad, productoCompleto.getSaldoFin());
                if (errorStock != null) {
                    personalizarPopup(); // Asegurar estilo
                    JOptionPane.showMessageDialog(this,
                            errorStock,
                            CargadorProperties.obtenerMessages("FC_C_004"),
                            JOptionPane.ERROR_MESSAGE);
                    restaurarEstilosPopup();

                    modelo.setValueAt(productosFactura.get(fila).getCantidad(), fila, 3);
                    return;
                }
            }
            pxf.setCantidad(cantidad);
            if (facturaYaInsertada && codigoFacturaActual != null) {
                ProxFac pxfUpdate = new ProxFac();
                pxfUpdate.setCodigoFac(codigoFacturaActual);
                pxfUpdate.setCodigoProd(pxf.getCodigoProd());
                pxfUpdate.setCantidad(pxf.getCantidad());
                pxfUpdate.setPrecioVenta(pxf.getPrecioVenta());
                pxfUpdate.calcularSubtotal();
                pxfUpdate.setEstado("ABI");

                pxfUpdate.modificar(); // tu SQL ya pone estado_pxf='ABI'
                actualizarTotalesEnBD();
            }
            pxf.calcularSubtotal();

            modelo.setValueAt(String.format("%.2f", pxf.getSubtotalProducto()), fila, 5);

            calcularTotales(txtSub, txtIva, txtTot);

        } catch (NumberFormatException ex) {
            String valStr = modelo.getValueAt(fila, 3).toString().trim();

            // Si son solo dígitos pero falla parseInt, es overflow -> Revertir
            // silenciosamente
            if (valStr.matches("\\d+")) {
                modelo.setValueAt(productosFactura.get(fila).getCantidad(), fila, 3);
                return;
            }

            // Determinar tipo de error para mostrar mensaje adecuado
            String mensajeError = CargadorProperties.obtenerMessages("FC_A_011"); // Por defecto: Solo enteros
            if (valStr.startsWith("-")) {
                mensajeError = CargadorProperties.obtenerMessages("FC_A_023"); // Negativos
            }

            mostrarMensaje(
                    mensajeError,
                    CargadorProperties.obtenerMessages("FC_C_004"),
                    JOptionPane.ERROR_MESSAGE);
            modelo.setValueAt(productosFactura.get(fila).getCantidad(), fila, 3);
        }
    }

    // Muestra u oculta mensaje de error en label
    private void mostrarError(JLabel label, String mensaje) {
        if (mensaje != null) {
            label.setText(mensaje);
        } else {
            label.setText(" ");
        }
    }

    // Verifica stock real en BD antes de aprobar la factura
    private boolean verificarStockAntesDeAprobar() {

        if (productosFactura == null || productosFactura.isEmpty()) {
            mostrarMensaje(
                    CargadorProperties.obtenerMessages("FC_A_028"),
                    CargadorProperties.obtenerMessages("FC_C_004"),
                    JOptionPane.WARNING_MESSAGE);
            return false;
        }

        for (ProxFac pxf : productosFactura) {
            Producto prod = new Producto();
            Producto productoBD = prod.verificarPorCodigoDP(pxf.getCodigoProd());

            if (productoBD == null) {
                mostrarMensaje(
                        String.format(CargadorProperties.obtenerMessages("FC_A_029"), pxf.getCodigoProd()),
                        CargadorProperties.obtenerMessages("FC_C_004"),
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }

            int stockDisponible = productoBD.getSaldoFin();
            int cantidadSolicitada = pxf.getCantidad();

            String errorStock = ValidacionesFactura.validarStock(cantidadSolicitada, stockDisponible);
            if (errorStock != null) {
                String descripcion = (productoBD.getDescripcion() != null) ? productoBD.getDescripcion() : "";
                mostrarMensaje(
                        String.format(CargadorProperties.obtenerMessages("FC_A_030"), productoBD.getCodigo(),
                                descripcion, errorStock),
                        CargadorProperties.obtenerMessages("FC_C_004"),
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }

        return true;
    }

    // **METODOS NEGOCIO CREAR**
    // Busca cliente por cédula y muestra sus datos
    private void buscarClienteCrear() {
        String cedRuc = txtCedulaCrear.getText().trim();

        String error = ValidacionesFactura.validarCedRucCliente(cedRuc);
        if (error != null) {
            mostrarError(lblErrorCedulaCrear, error);
            limpiarDatosCliente(lblNombreClienteCrear, lblCedulaClienteCrear, lblEmailClienteCrear,
                    lblTelefonoClienteCrear);
            return;
        }

        Cliente cli = new Cliente();
        clienteSeleccionado = cli.verificarDP(cedRuc);

        if (clienteSeleccionado != null) {
            String errorEstado = ValidacionesFactura.validarEstadoCliente(clienteSeleccionado);
            if (errorEstado != null) {
                limpiarDatosCliente(lblNombreClienteCrear, lblCedulaClienteCrear, lblEmailClienteCrear,
                        lblTelefonoClienteCrear);
                mostrarError(lblErrorCedulaCrear, errorEstado);

                personalizarPopup();
                JOptionPane.showMessageDialog(this,
                        errorEstado,
                        CargadorProperties.obtenerMessages("FC_C_005"),
                        JOptionPane.WARNING_MESSAGE);
                restaurarEstilosPopup();

                cmbProductoCrear.setEnabled(false);
                txtCantidadCrear.setEnabled(false);
                clienteSeleccionado = null;
                return;
            }
            lblNombreClienteCrear.setText(clienteSeleccionado.getNombre());
            lblCedulaClienteCrear.setText(clienteSeleccionado.getCedRuc());
            lblEmailClienteCrear.setText(clienteSeleccionado.getEmail() != null ? clienteSeleccionado.getEmail() : "");
            lblTelefonoClienteCrear
                    .setText(clienteSeleccionado.getTelefono() != null ? clienteSeleccionado.getTelefono() : "");
            mostrarError(lblErrorCedulaCrear, null);

            cmbProductoCrear.setEnabled(true);
            txtCantidadCrear.setEnabled(true);
        } else {
            limpiarDatosCliente(lblNombreClienteCrear, lblCedulaClienteCrear, lblEmailClienteCrear,
                    lblTelefonoClienteCrear);
            mostrarError(lblErrorCedulaCrear, CargadorProperties.obtenerMessages("FC_A_004"));

            cmbProductoCrear.setEnabled(false);
            txtCantidadCrear.setEnabled(false);
        }
    }

    // Limpia datos del cliente mostrados
    private void limpiarDatosCliente(JLabel lblNombre, JLabel lblCedula,
            JLabel lblEmail, JLabel lblTelefono) {
        lblNombre.setText("");
        lblCedula.setText("");
        lblEmail.setText("");
        lblTelefono.setText("");
    }

    // Agrega producto a la tabla de productos
    private void agregarProducto(JComboBox<ItemProducto> combo,
            JTextField txtCantidad,
            JLabel lblError,
            DefaultTableModel modelo,
            JTextField txtSub,
            JTextField txtIva,
            JTextField txtTotal) {

        // Validar cliente (solo en crear)
        if (clienteSeleccionado == null && combo == cmbProductoCrear) {
            mostrarMensaje(
                    CargadorProperties.obtenerMessages("FC_A_012"),
                    CargadorProperties.obtenerMessages("FC_C_005"),
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Validar producto seleccionado
        ItemProducto item = (ItemProducto) combo.getSelectedItem();
        if (item == null) {
            mostrarMensaje(
                    CargadorProperties.obtenerMessages("FC_A_013"),
                    CargadorProperties.obtenerMessages("FC_C_005"),
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Validar cantidad usando lógica centralizada
        String cantidadStr = txtCantidad.getText().trim();
        String errorCantidad = ValidacionesFactura.validarCantidad(cantidadStr);

        if (errorCantidad != null) {
            mostrarError(lblError, errorCantidad);
            return;
        }

        int cantidad;
        try {
            cantidad = Integer.parseInt(cantidadStr);
            if (cantidad <= 0) {
                // Esto no debería pasar si validarCantidad funcionó, pero por seguridad
                mostrarError(lblError, CargadorProperties.obtenerMessages("FC_A_023"));
                return;
            }
            mostrarError(lblError, null);
        } catch (NumberFormatException e) {
            // Número muy grande (overflow) u otro error de formato raro -> No mostrar
            // mensaje y no agregar
            return;
        }

        Producto prod = item.producto; // Solo para obtener el código

        Producto prodActualizado = new Producto();
        Producto productoCompleto = prodActualizado.verificarPorCodigoDP(prod.getCodigo());

        if (productoCompleto == null) {
            mostrarMensaje(
                    CargadorProperties.obtenerMessages("PD_E_002"),
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
            String errorStock = ValidacionesFactura.validarStockAcumulado(cantidad, cantidadExistente, stockDisponible);
            lblError.setForeground(COLOR_ACENTO);
            mostrarError(lblError, errorStock);
            return;
        }

        if (filaExistente != -1) {
            // Producto existe: SUMAR cantidad
            ProxFac pxfExistente = productosFactura.get(filaExistente);
            int cantidadAnterior = pxfExistente.getCantidad();
            int cantidadNueva = cantidadAnterior + cantidad;

            pxfExistente.setCantidad(cantidadNueva);
            pxfExistente.calcularSubtotal();

            if (facturaYaInsertada && codigoFacturaActual != null) {
                ProxFac pxfActualizar = new ProxFac();
                pxfActualizar.setCodigoFac(codigoFacturaActual);
                pxfActualizar.setCodigoProd(pxfExistente.getCodigoProd());
                pxfActualizar.setCantidad(cantidadNueva);
                pxfActualizar.setPrecioVenta(pxfExistente.getPrecioVenta());
                pxfActualizar.calcularSubtotal();
                pxfActualizar.setEstado("ABI");

                if (!pxfActualizar.modificar()) {
                    mostrarMensaje(
                            CargadorProperties.obtenerMessages("FC_E_002"),
                            CargadorProperties.obtenerMessages("FC_C_004"),
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            modelo.setValueAt(cantidadNueva, filaExistente, 3);
            modelo.setValueAt(String.format("%.2f", pxfExistente.getSubtotalProducto()), filaExistente, 5);

            mostrarMensaje(
                    CargadorProperties.obtenerMessages("FC_A_021") +
                            cantidadAnterior + " + " + cantidad + " = " + cantidadNueva,
                    CargadorProperties.obtenerComponentes("TITULO_PRODUCTO_DUPLICADO"),
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            // Producto nuevo: AGREGAR
            ProxFac pxf = new ProxFac();
            pxf.setCodigoProd(prod.getCodigo());
            pxf.setCantidad(cantidad);
            pxf.setPrecioVenta(prod.getPrecioVenta());
            pxf.calcularSubtotal();
            pxf.setEstado("ABI");

            // **SI ES EL PRIMER PRODUCTO: INSERTAR CABECERA Y DETALLE**
            if (!facturaYaInsertada) {
                // Crear objeto Factura con la cabecera
                Factura facturaNueva = new Factura();
                facturaNueva.setCodigoCliente(clienteSeleccionado.getIdCliente());
                facturaNueva.setFechaHora(LocalDateTime.now());
                facturaNueva.setSubtotal(pxf.getSubtotalProducto());
                facturaNueva.setIva(pxf.getSubtotalProducto() * 0.15);
                facturaNueva.setTotal(pxf.getSubtotalProducto() * 1.15);
                facturaNueva.setTipo("POS");
                facturaNueva.setEstado("ABI");

                // Agregar el primer producto
                facturaNueva.agregarProducto(pxf);

                // Insertar en BD
                String codigoGenerado = facturaNueva.insertar();

                if (codigoGenerado == null) {
                    mostrarMensaje(
                            CargadorProperties.obtenerMessages("FC_E_002"),
                            CargadorProperties.obtenerMessages("FC_C_004"),
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Guardar el código generado
                codigoFacturaActual = codigoGenerado;
                facturaYaInsertada = true;

                // Actualizar label con código real
                lblCodigoGenerado
                        .setText(CargadorProperties.obtenerComponentes("LBL_CODIGO") + " " + codigoFacturaActual);

                // Configurar el producto con el código de factura
                pxf.setCodigoFac(codigoFacturaActual);

            } else {
                pxf.setCodigoFac(codigoFacturaActual);
                pxf.setEstado("ABI");

                if (!pxf.insertar()) {
                    mostrarMensaje(
                            CargadorProperties.obtenerMessages("FC_E_002"),
                            CargadorProperties.obtenerMessages("FC_C_004"),
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            productosFactura.add(pxf);
            actualizarTotalesEnBD();

            modelo.addRow(new Object[] {
                    prod.getCodigo(),
                    prod.getDescripcion(),
                    productoCompleto.getNombreUmVenta(),
                    cantidad,
                    String.format("%.2f", prod.getPrecioVenta()),
                    String.format("%.2f", pxf.getSubtotalProducto())
            });
        }

        calcularTotales(txtSub, txtIva, txtTotal);
        txtCantidad.setText("");
        mostrarError(lblError, null);

        // Habilitar botones de Guardar (Borrador) según corresponda
        if (btnGuardarCrear != null && combo == cmbProductoCrear)
            btnGuardarCrear.setEnabled(true);
        if (btnGuardarModificar != null && combo == cmbProductoMod)
            btnGuardarModificar.setEnabled(true);

        // Habilitar botones Quitar/Aprobar en CREAR si hay productos
        if (btnQuitarCrear != null && !productosFactura.isEmpty() && combo == cmbProductoCrear) {
            btnQuitarCrear.setEnabled(true);
            btnAprobarCrear.setEnabled(true);
        }
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
            mostrarMensaje(
                    CargadorProperties.obtenerMessages("FC_A_013"),
                    CargadorProperties.obtenerMessages("FC_C_005"),
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String idProducto = modelo.getValueAt(fila, 0).toString();

        // Eliminar Físico (DELETE)
        if (facturaYaInsertada && codigoFacturaActual != null) {
            ProxFac pxf = new ProxFac();
            pxf.setCodigoFac(codigoFacturaActual);
            pxf.setCodigoProd(idProducto);
            // No seteamos estado a ANU porque ahora es DELETE físico

            if (!pxf.eliminar()) {
                mostrarMensaje(
                        CargadorProperties.obtenerMessages("FC_E_002"),
                        CargadorProperties.obtenerMessages("FC_C_004"),
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

        }
        productosFactura.remove(fila);
        modelo.removeRow(fila);

        calcularTotales(txtSub, txtIva, txtTotal);
        actualizarTotalesEnBD();

        // Habilitar botones de Guardar (Borrador)
        if (btnGuardarCrear != null && tabla == tablaProductosCrear)
            btnGuardarCrear.setEnabled(true);
        if (btnGuardarModificar != null && tabla == tablaProductosMod)
            btnGuardarModificar.setEnabled(true);

        // Deshabilitar botones Quitar/Aprobar en CREAR si no hay productos
        if (btnQuitarCrear != null && productosFactura.isEmpty() && tabla == tablaProductosCrear) {
            btnQuitarCrear.setEnabled(false);
            btnAprobarCrear.setEnabled(false);
        }
    }

    // Calcula subtotal IVA y total de la factura
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

    // Crea y guarda nueva factura en la base de datos
    private void crearFactura() {
        // Validar que la factura ya esté insertada en BD
        if (!facturaYaInsertada || codigoFacturaActual == null) {
            mostrarMensaje(
                    CargadorProperties.obtenerMessages("FC_A_027"),
                    CargadorProperties.obtenerMessages("FC_C_004"),
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!verificarStockAntesDeAprobar()) {
            return;
        }
        // SOLO APROBAR (la factura ya está en BD con estado ABI)
        Factura facAprobar = new Factura();
        facAprobar.setCodigo(codigoFacturaActual);

        if (facAprobar.aprobar()) { // ← Llama al método aprobar que retorna boolean
            mostrarMensaje(
                    CargadorProperties.obtenerMessages("FC_I_001") +
                            CargadorProperties.obtenerMessages("FC_I_004") + " " + codigoFacturaActual,
                    CargadorProperties.obtenerMessages("FC_C_003"),
                    JOptionPane.INFORMATION_MESSAGE);
            limpiarPanelCrear(); // ← Cambié el nombre para que sea consistente
        } else {
            mostrarMensaje(
                    CargadorProperties.obtenerMessages("FC_E_002"),
                    CargadorProperties.obtenerMessages("FC_C_004"),
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // **METODOS NEGOCIO MODIFICAR**
    // Busca facturas asociadas a un cliente -> Se ocupa en consulta especifica
    // tambien
    private void cargarProductosDeFactura(String codigoFactura,
            DefaultTableModel modelo,
            boolean agregarALista,
            boolean soloABI) {
        modelo.setRowCount(0);

        if (agregarALista) {
            productosFactura.clear(); // Solo para modificar
        }

        ProxFac pxfConsulta = new ProxFac();
        pxfConsulta.setCodigoFac(codigoFactura);

        ArrayList<ProxFac> productos;

        // ✅ MODIFICAR -> solo ABI
        if (soloABI) {
            productos = pxfConsulta.consultarPorFacturaABI(pxfConsulta);
        }
        // ✅ CONSULTAR -> completo
        else {
            productos = pxfConsulta.consultarPorFactura(pxfConsulta);
        }

        if (productos != null && !productos.isEmpty()) {
            for (ProxFac pxf : productos) {
                Producto prod = new Producto();
                Producto prodCompleto = prod.verificarPorCodigoDP(pxf.getCodigoProd());
                String descripcion = (prodCompleto != null) ? prodCompleto.getDescripcion() : "N/A";

                modelo.addRow(new Object[] {
                        pxf.getCodigoProd(),
                        descripcion,
                        (prodCompleto != null) ? prodCompleto.getNombreUmVenta() : "N/A",
                        pxf.getCantidad(),
                        String.format("%.2f", pxf.getPrecioVenta()),
                        String.format("%.2f", pxf.getSubtotalProducto())
                });

                if (agregarALista) {
                    productosFactura.add(pxf);
                }
            }
        }
    }

    // Carga factura seleccionada para modificar
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
        if (fila == -1)
            return;

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
                // Si es el panel de detalle de consulta, reducir la tabla
                if (panelCabecera == panelDetalleConsulta && scrollTablaResultados != null) {
                    scrollTablaResultados.setPreferredSize(new Dimension(900, 150));
                    scrollTablaResultados.revalidate();
                    scrollTablaResultados.repaint();

                    if (panelPaginacion != null) {
                        panelPaginacion.setVisible(false);
                    }
                }
            }

            // Llenar datos de cabecera
            lblCodigo
                    .setText(CargadorProperties.obtenerComponentes("LBL_CODIGO") + " " + facturaConsultada.getCodigo());
            lblFecha.setText(facturaConsultada.getFechaHora().format(FMT_FECHA));

            if (clienteCompleto != null) {
                lblNombre.setText(clienteCompleto.getNombre());
                lblCedula.setText(clienteCompleto.getCedRuc());
                lblEmail.setText(clienteCompleto.getEmail() != null ? clienteCompleto.getEmail() : "");
                lblTelefono.setText(clienteCompleto.getTelefono() != null ? clienteCompleto.getTelefono() : "");
            }

            boolean soloABI = esEditable; // ✅ si es MODIFICAR -> true (ABI). Si es CONSULTAR -> false (completo)
            cargarProductosDeFactura(facturaConsultada.getCodigo(), modeloProductos, esEditable, soloABI);
            // Mostrar totales
            txtSubtotal.setText(String.format("%.2f", facturaConsultada.getSubtotal()));
            txtIva.setText(String.format("%.2f", facturaConsultada.getIva()));
            txtTotal.setText(String.format("%.2f", facturaConsultada.getTotal()));

            if (esEditable) {
                if (combo != null)
                    combo.setEnabled(true);
                if (txtCantidad != null)
                    txtCantidad.setEnabled(true);
                if (btnGuardar != null)
                    btnGuardar.setEnabled(true);
                if (btnQuitarMod != null)
                    btnQuitarMod.setEnabled(true); // Enable Quitar when loaded
                if (combo != null)
                    cargarProductosActivos(combo);
            } else {
                if (combo != null)
                    combo.setEnabled(false);
                if (txtCantidad != null)
                    txtCantidad.setEnabled(false);
                if (btnGuardar != null)
                    btnGuardar.setEnabled(false);
            }
            if (esEditable) {
                facturaSeleccionada = facturaConsultada;
                codigoFacturaActual = facturaConsultada.getCodigo();
                facturaYaInsertada = true;
            }
        }
    }

    // Guarda cambios de factura modificadaa
    private void modificarFactura() {
        if (facturaSeleccionada == null) {
            mostrarMensaje(
                    CargadorProperties.obtenerMessages("FC_A_016"),
                    CargadorProperties.obtenerMessages("FC_C_005"),
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (productosFactura.isEmpty()) {
            mostrarMensaje(
                    CargadorProperties.obtenerMessages("FC_A_017"),
                    CargadorProperties.obtenerMessages("FC_C_005"),
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!verificarStockAntesDeAprobar()) {
            return;
        }

        Factura facAprobar = new Factura();
        facAprobar.setCodigo(facturaSeleccionada.getCodigo());

        if (facAprobar.aprobar()) {
            mostrarMensaje(
                    CargadorProperties.obtenerMessages("FC_I_002"),
                    CargadorProperties.obtenerMessages("FC_C_003"),
                    JOptionPane.INFORMATION_MESSAGE);
            limpiarPanelModificar();
        } else {
            mostrarMensaje(
                    CargadorProperties.obtenerMessages("FC_E_002"),
                    CargadorProperties.obtenerMessages("FC_C_004"),
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarTotalesEnBD() {
        if (!facturaYaInsertada || codigoFacturaActual == null)
            return;

        double subtotal = 0;
        for (ProxFac pxf : productosFactura)
            subtotal += pxf.getSubtotalProducto();

        Factura facActualizar = new Factura();
        facActualizar.setCodigo(codigoFacturaActual);
        facActualizar.setSubtotal(subtotal);
        facActualizar.setIva(subtotal * 0.15);
        facActualizar.setTotal(subtotal * 1.15);

        facActualizar.modificarTotalesSoloCabecera();
    }

    // **METODOS NEGOCIO ELIMINAR**
    // Elimina factura seleccionada
    private void eliminarFactura() {
        int fila = tablaFacturasElim.getSelectedRow();
        if (fila == -1) {
            mostrarMensaje(
                    CargadorProperties.obtenerMessages("FC_A_016"),
                    CargadorProperties.obtenerMessages("FC_C_005"),
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String codigo = (String) modeloFacturasElim.getValueAt(fila, 0);

        // Confirmación
        int conf = confirmarAccion(
                CargadorProperties.obtenerMessages("FC_C_001") + codigo + "?",
                CargadorProperties.obtenerMessages("FC_C_002"));
        if (conf != JOptionPane.YES_OPTION)
            return;

        // Eliminar
        Factura fac = new Factura();
        fac.setCodigo(codigo);

        if (fac.eliminar()) {
            mostrarMensaje(
                    CargadorProperties.obtenerMessages("FC_I_003"),
                    CargadorProperties.obtenerMessages("FC_C_003"),
                    JOptionPane.INFORMATION_MESSAGE);
            // Recargar tabla
            buscarYCargarFacturas(txtCedulaElim, modeloFacturasElim, false);
        } else {
            mostrarMensaje(
                    CargadorProperties.obtenerMessages("FC_E_005"),
                    CargadorProperties.obtenerMessages("FC_C_004"),
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // **METODOS NEGOCIO CONSULTAR**
    // Cambia tipo de consulta y muestra panel correspondiente
    // Cambia tipo de consulta y muestra panel correspondiente
    private void cambiarTipoConsulta() {
        String tipo = (String) comboTipoConsulta.getSelectedItem();
        String[] opciones = CargadorProperties.obtenerComponentes("CMB_TIPO_CONSULTA").split(",");

        // Limpiar antes de cambiar
        limpiarPanelConsultar();

        if (tipo.equals(opciones[1])) {
            // Consulta General (todas las facturas APR)
            panelBusqueda.setVisible(false);
            // Mostrar scroll en general
            if (scrollTablaResultados != null) {
                scrollTablaResultados.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
                scrollTablaResultados.setPreferredSize(new Dimension(900, 480)); // Altura para 18 filas
            }

            consultarTodos();
        } else if (tipo.equals(opciones[2])) {
            // Consulta Por Cliente (buscar por cédula)
            panelBusqueda.setVisible(true);
            // Ocultar scroll en específica
            // Ocultar scroll en específica
            if (scrollTablaResultados != null) {
                scrollTablaResultados.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
                scrollTablaResultados.setPreferredSize(new Dimension(900, 425)); // Altura exacta para 16 filas (16*25 +
                                                                                 // header)
            }

            modeloTablaResultados.setRowCount(0);
            facturasPaginadasTotal.clear();
            actualizarTablaPaginada();
            panelDetalleConsulta.setVisible(false);
        } else {
            // "Seleccione..." (opción por defecto)
            panelBusqueda.setVisible(false);
            panelPaginacion.setVisible(false); // Ocultar si no hay selección
            modeloTablaResultados.setRowCount(0);
            facturasPaginadasTotal.clear();
            actualizarTablaPaginada();
            panelDetalleConsulta.setVisible(false);
        }

        // Mostrar paginación si hay un tipo válido seleccionado
        if (tipo.equals(opciones[1]) || tipo.equals(opciones[2])) {
            panelPaginacion.setVisible(true);
        }
    }

    // Consulta todas las facturas activas
    private void consultarTodos() {
        Factura fac = new Factura();
        ArrayList<Factura> lista = fac.consultarTodos();

        modeloTablaResultados.setRowCount(0);

        // Usar lógica de paginación
        facturasPaginadasTotal = (lista != null) ? lista : new ArrayList<>();
        paginaActual = 0;
        actualizarTablaPaginada();
    }

    // **METODOS AUXILIARES DATOS**
    // Carga productos activos en el combobox
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

    // Genera y muestra código de factura
    private void generarYMostrarCodigo() {
        String codigo = Factura.generarCodigo();

        if (codigo != null) {
            lblCodigoGenerado.setText(CargadorProperties.obtenerComponentes("LBL_CODIGO") + " " + codigo);
        } else {
            lblCodigoGenerado.setText(CargadorProperties.obtenerComponentes("LBL_CODIGO") + " "
                    + CargadorProperties.obtenerComponentes("CODIGO_ERROR"));
        }
    }

    // **METODOS LIMPIEZA**
    // Limpia panel crear y resetea variables
    private void limpiarPanelCrear() {
        codigoFacturaActual = null;
        facturaYaInsertada = false;
        clienteSeleccionado = null;
        productosFactura.clear();
        txtCedulaCrear.setText("");
        txtCantidadCrear.setText("");

        lblNombreClienteCrear.setText("");
        txtCedulaCrear.setEnabled(true);
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

        if (cmbProductoCrear != null)
            cmbProductoCrear.setEnabled(false);
        if (txtCantidadCrear != null)
            txtCantidadCrear.setEnabled(false);

        if (btnQuitarCrear != null)
            btnQuitarCrear.setEnabled(false);
        if (btnAprobarCrear != null)
            btnAprobarCrear.setEnabled(false);
        if (btnGuardarCrear != null)
            btnGuardarCrear.setEnabled(false);

        lblCodigoGenerado.setText("Código: Pendiente...");
    }

    // Limpia panel modificar y resetea variables
    private void limpiarPanelModificar() {
        if (txtCedulaMod != null)
            txtCedulaMod.setText("");
        if (lblErrorCedulaMod != null)
            lblErrorCedulaMod.setText(" ");
        if (modeloFacturasMod != null)
            modeloFacturasMod.setRowCount(0);
        if (modeloProductosMod != null)
            modeloProductosMod.setRowCount(0);

        if (panelCabeceraMod != null) {
            panelCabeceraMod.setVisible(false);
        }

        if (lblCodigoMod != null)
            lblCodigoMod.setText("");
        if (lblFechaMod != null)
            lblFechaMod.setText("");
        if (lblNombreClienteMod != null)
            lblNombreClienteMod.setText("");
        if (lblCedulaClienteMod != null)
            lblCedulaClienteMod.setText("");
        if (lblEmailClienteMod != null)
            lblEmailClienteMod.setText("");
        if (lblTelefonoClienteMod != null)
            lblTelefonoClienteMod.setText("");

        if (txtSubtotalMod != null)
            txtSubtotalMod.setText(CargadorProperties.obtenerComponentes("TOTAL_INICIAL"));
        if (txtIVAMod != null)
            txtIVAMod.setText(CargadorProperties.obtenerComponentes("TOTAL_INICIAL"));
        if (txtTotalMod != null)
            txtTotalMod.setText(CargadorProperties.obtenerComponentes("TOTAL_INICIAL"));

        if (cmbProductoMod != null)
            cmbProductoMod.setEnabled(false);
        if (txtCantidadMod != null)
            txtCantidadMod.setEnabled(false);
        if (btnGuardarMod != null)
            btnGuardarMod.setEnabled(false);
        if (btnQuitarMod != null)
            btnQuitarMod.setEnabled(false); // Disable Quitar on clean

        facturaSeleccionada = null;
        if (productosFactura != null)
            productosFactura.clear();

        if (lblErrorCantidadMod != null)
            lblErrorCantidadMod.setText(" ");
    }

    // Limpia panel eliminar
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

    // Limpia panel consultar
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
        if (lblCodigoConsulta != null)
            lblCodigoConsulta.setText("");
        if (lblFechaConsulta != null)
            lblFechaConsulta.setText("");
        if (lblNombreClienteConsulta != null)
            lblNombreClienteConsulta.setText("");
        if (lblCedulaClienteConsulta != null)
            lblCedulaClienteConsulta.setText("");
        if (lblEmailClienteConsulta != null)
            lblEmailClienteConsulta.setText("");
        if (lblTelefonoClienteConsulta != null)
            lblTelefonoClienteConsulta.setText("");

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

    // **METODOS DE ESTILO Y UTILIDADES**

    private Font cargarFuente(String ruta, int estilo, float tamaño) {
        try {
            Font fuente = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream(ruta));
            return fuente.deriveFont(estilo, tamaño);
        } catch (Exception e) {
            System.err.println("No se pudo cargar la fuente Poppins desde: " + ruta);
            return new Font("SansSerif", estilo, (int) tamaño);
        }
    }

    private void estilizarBotonPrimario(JButton btn) {
        btn.setFont(FUENTE_BOTON);
        btn.setBackground(COLOR_SECUNDARIO);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(COLOR_SECUNDARIO, 0, true),
                new EmptyBorder(8, 16, 8, 16)));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Configurar el UI para forzar el color del texto
        btn.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                JButton button = (JButton) c;
                Graphics2D g2 = (Graphics2D) g.create();

                // Dibujar el fondo - más oscuro si está deshabilitado
                if (!button.isEnabled()) {
                    g2.setColor(new Color(60, 90, 44)); // Verde oscuro
                } else {
                    g2.setColor(button.getBackground());
                }
                g2.fillRoundRect(0, 0, button.getWidth(), button.getHeight(), 0, 0);

                // Dibujar el texto en BLANCO
                g2.setColor(COLOR_BLANCO);
                g2.setFont(button.getFont());
                FontMetrics fm = g2.getFontMetrics();
                int x = (button.getWidth() - fm.stringWidth(button.getText())) / 2;
                int y = (button.getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(button.getText(), x, y);

                g2.dispose();
            }
        });

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                if (btn.isEnabled()) {
                    btn.setBackground(new Color(82, 121, 54));
                }
            }

            public void mouseExited(MouseEvent evt) {
                if (btn.isEnabled()) {
                    btn.setBackground(COLOR_SECUNDARIO);
                }
            }
        });
    }

    private void estilizarBotonSecundario(JButton btn) {
        btn.setFont(FUENTE_BOTON);
        btn.setBackground(COLOR_TEXTO_SECUNDARIO);
        btn.setForeground(COLOR_BLANCO);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(COLOR_TEXTO_SECUNDARIO, 0, true),
                new EmptyBorder(8, 16, 8, 16)));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // UI para manejar estado deshabilitado
        btn.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                JButton button = (JButton) c;
                Graphics2D g2 = (Graphics2D) g.create();

                // Fondo más oscuro si está deshabilitado
                if (!button.isEnabled()) {
                    g2.setColor(new Color(100, 100, 100)); // Gris oscuro
                } else {
                    g2.setColor(button.getBackground());
                }
                g2.fillRoundRect(0, 0, button.getWidth(), button.getHeight(), 0, 0);

                // Texto blanco
                g2.setColor(COLOR_BLANCO);
                g2.setFont(button.getFont());
                FontMetrics fm = g2.getFontMetrics();
                int x = (button.getWidth() - fm.stringWidth(button.getText())) / 2;
                int y = (button.getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(button.getText(), x, y);

                g2.dispose();
            }
        });

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                if (btn.isEnabled()) {
                    btn.setBackground(new Color(120, 120, 120));
                }
            }

            public void mouseExited(MouseEvent evt) {
                if (btn.isEnabled()) {
                    btn.setBackground(COLOR_TEXTO_SECUNDARIO);
                }
            }
        });
    }

    private void estilizarBotonEliminar(JButton btn) {
        btn.setFont(FUENTE_BOTON);
        btn.setBackground(COLOR_ACENTO);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(COLOR_ACENTO, 0, true),
                new EmptyBorder(8, 16, 8, 16)));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                JButton button = (JButton) c;
                Graphics2D g2 = (Graphics2D) g.create();

                // Fondo más oscuro si está deshabilitado
                if (!button.isEnabled()) {
                    g2.setColor(new Color(140, 10, 0)); // Rojo oscuro
                } else {
                    g2.setColor(button.getBackground());
                }
                g2.fillRoundRect(0, 0, button.getWidth(), button.getHeight(), 0, 0);

                g2.setColor(COLOR_BLANCO);
                g2.setFont(button.getFont());
                FontMetrics fm = g2.getFontMetrics();
                int x = (button.getWidth() - fm.stringWidth(button.getText())) / 2;
                int y = (button.getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(button.getText(), x, y);
                g2.dispose();
            }
        });

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                if (btn.isEnabled()) {
                    btn.setBackground(new Color(170, 15, 0));
                }
            }

            public void mouseExited(MouseEvent evt) {
                if (btn.isEnabled()) {
                    btn.setBackground(COLOR_ACENTO);
                }
            }
        });
    }

    private void estilizarCampoTexto(JTextField campo) {
        campo.setFont(FUENTE_BASE);
        campo.setForeground(COLOR_TEXTO_CAMPO);
        campo.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(COLOR_BORDE, 1, true),
                new EmptyBorder(4, 8, 4, 8)));
    }

    private void estilizarComboBox(JComboBox<?> combo) {
        combo.setFont(FUENTE_BASE);
        combo.setForeground(COLOR_TEXTO);
        combo.setBackground(COLOR_BLANCO);
        combo.setBorder(new LineBorder(COLOR_BORDE, 1, true));
    }

    // Métodos auxiliares para popups estilizados
    private void mostrarMensaje(String mensaje, String titulo, int tipo) {
        personalizarPopup();
        JOptionPane.showMessageDialog(this, mensaje, titulo, tipo);
        restaurarEstilosPopup();
    }

    private int confirmarAccion(String mensaje, String titulo) {
        personalizarPopup();
        int opcion = JOptionPane.showConfirmDialog(this, mensaje, titulo, JOptionPane.YES_NO_OPTION);
        restaurarEstilosPopup();
        return opcion;
    }

    private void personalizarPopup() {
        UIManager.put("OptionPane.messageFont", FUENTE_BASE);
        UIManager.put("OptionPane.buttonFont", FUENTE_BOTON);
        UIManager.put("OptionPane.background", COLOR_FONDO_CENTRAL);
        UIManager.put("Panel.background", COLOR_FONDO_CENTRAL);
        UIManager.put("Button.background", COLOR_SECUNDARIO);
        UIManager.put("Button.foreground", COLOR_BLANCO);
        UIManager.put("Button.select", new Color(82, 121, 54));
        UIManager.put("Button.focus", new Color(82, 121, 54));
        UIManager.put("OptionPane.messageForeground", COLOR_TEXTO_CAMPO);
    }

    private void restaurarEstilosPopup() {
        UIManager.put("OptionPane.messageFont", null);
        UIManager.put("OptionPane.buttonFont", null);
        UIManager.put("OptionPane.background", null);
        UIManager.put("Panel.background", null);
        UIManager.put("Button.background", null);
        UIManager.put("Button.foreground", null);
        UIManager.put("Button.select", null);
        UIManager.put("Button.focus", null);
        UIManager.put("OptionPane.messageForeground", null);
    }

    private void estilizarTabla(JTable tabla) {
        tabla.setFont(FUENTE_BASE);
        tabla.setForeground(COLOR_TEXTO_CAMPO);
        tabla.setRowHeight(25);
        tabla.getTableHeader().setFont(new Font("Poppins", Font.BOLD, 13));
        tabla.getTableHeader().setBackground(COLOR_ENFASIS);
        tabla.getTableHeader().setForeground(COLOR_TEXTO);
        tabla.getTableHeader().setOpaque(true);
    }

    // **CLASE INTERNA**
    // Clase wrapper para mostrar productos en ComboBox
    private static class ItemProducto {
        Producto producto;

        ItemProducto(Producto p) {
            this.producto = p;
        }

        @Override
        public String toString() {
            String um = (producto.getNombreUmVenta() != null) ? " (" + producto.getNombreUmVenta() + ")" : "";
            return producto.getCodigo() + " - " + producto.getDescripcion() + um;
        }
    }
}
