package GUI;

import DP.Proveedor;
import DP.Compra;
import DP.ProxOc;
import DP.Producto;
import util.CargadorProperties;
import util.ValidacionesCompra;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

public class VentanaCompra extends JFrame {

    // Paleta de colores
    private static final Color COLOR_PRIMARIO = new Color(255, 173, 51); // #FFAD33 - Naranja
    private static final Color COLOR_SECUNDARIO = new Color(102, 151, 74); // #66974A - Verde
    private static final Color COLOR_ACENTO = new Color(204, 20, 0); // #CC1400 - Rojo
    private static final Color COLOR_TEXTO = new Color(76, 87, 169); // #4C57A9 - Azul tinta
    private static final Color COLOR_FONDO_CENTRAL = new Color(255, 247, 227); // #fff7e3 - Fondo panel central
    private static final Color COLOR_TEXTO_SECUNDARIO = new Color(153, 153, 153); // #999
    private static final Color COLOR_BORDE = new Color(221, 221, 221); // #ddd
    private static final Color COLOR_ENFASIS = new Color(255, 164, 28); // #ffa41c - Naranja énfasis
    private static final Color COLOR_BLANCO = Color.WHITE;
    private static final Color COLOR_TEXTO_CAMPO = Color.BLACK;

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
    // private JTextField txtRucCrear; // REEMPLAZADO POR COMBO
    private JComboBox<ItemProveedor> cmbProveedorCrear;
    private JTextField txtCantidadCrear;
    private JLabel lblNombreProveedorCrear, lblRucProveedorCrear, lblEmailProveedorCrear, lblTelefonoProveedorCrear;
    private JLabel lblErrorRucCrear, lblErrorCantidadCrear;
    private JComboBox<ItemProducto> cmbProductoCrear;
    private JTable tablaProductosCrear;
    private DefaultTableModel modeloProductosCrear;
    private JTextField txtSubtotalCrear, txtIVACrear, txtTotalCrear;
    private String codigoCompraActual = null;
    private boolean compraYaInsertada = false;

    // **VARIABLES PANEL MODIFICAR**
    private JComboBox<ItemProveedor> cmbProveedorModSelector;
    private JTable tablaComprasMod;
    private JLabel lblErrorRucMod;
    private DefaultTableModel modeloComprasMod;
    private JTable tablaProductosMod;
    private DefaultTableModel modeloProductosMod;
    private JComboBox<ItemProducto> cmbProductoMod;
    private JTextField txtCantidadMod, txtSubtotalMod, txtIVAMod, txtTotalMod;
    private JButton btnGuardarMod;
    private JButton btnGuardarCrear;
    private JButton btnGuardarModificar;
    private JButton btnQuitarCrear;
    private JButton btnAprobarCrear;
    private JButton btnQuitarMod;
    private Compra compraSeleccionada;
    private JLabel lblCodigoMod, lblFechaMod;
    private JLabel lblNombreProveedorMod, lblRucProveedorMod, lblEmailProveedorMod, lblTelefonoProveedorMod,
            lblErrorCantidadMod;
    private JPanel panelCabeceraMod;

    // **VARIABLES PANEL ELIMINAR**
    private JComboBox<ItemProveedor> cmbProveedorElimSelector;
    private JTable tablaComprasElim;
    private DefaultTableModel modeloComprasElim;
    private JButton btnEliminar;

    // CONSULTAR
    private JComboBox<String> comboTipoConsulta;
    private JPanel panelBusqueda;
    private JComboBox<ItemProveedor> cmbProveedorConsulta;
    private JTable tablaResultados;
    private DefaultTableModel modeloTablaResultados;

    // DETALLE
    private JPanel panelDetalleConsulta;
    private JLabel lblCodigoConsulta, lblFechaConsulta;
    private JLabel lblNombreProveedorConsulta, lblRucProveedorConsulta;
    private JLabel lblEmailProveedorConsulta, lblTelefonoProveedorConsulta;
    private JTable tablaProductosConsulta;
    private DefaultTableModel modeloProductosConsulta;
    private JTextField txtSubtotalConsulta, txtIVAConsulta, txtTotalConsulta;


    // Variables de paginación
    private JPanel panelPaginacion;
    private JButton btnPrimero, btnAnterior, btnSiguiente, btnUltimo;
    private ArrayList<Compra> comprasPaginadasTotal = new ArrayList<>();
    private int paginaActual = 0;
    private final int FILAS_POR_PAGINA = 18;

    // **VARIABLES DATOS**
    private Proveedor proveedorSeleccionado;
    private ArrayList<ProxOc> productosCompra;

    // **CONSTRUCTOR**
    public VentanaCompra() {
        // Cargar fuentes
        FUENTE_TITULO = cargarFuente("/resources/fonts/Poppins-Bold.ttf", Font.BOLD, 32f);
        FUENTE_SUBTITULO = cargarFuente("/resources/fonts/Poppins-Bold.ttf", Font.BOLD, 24f);
        FUENTE_BASE = cargarFuente("/resources/fonts/Poppins-Regular.ttf", Font.PLAIN, 14f);
        FUENTE_LABEL = cargarFuente("/resources/fonts/Poppins-Regular.ttf", Font.PLAIN, 13f);
        FUENTE_BOTON = cargarFuente("/resources/fonts/Poppins-Bold.ttf", Font.BOLD, 13f);

        productosCompra = new ArrayList<>();
        configurarVentana();
        inicializarComponentes();
        configurarLayout();
    }

    private void configurarVentana() {
        setTitle(CargadorProperties.obtenerComponentes("CP_TITULO"));
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
        panelInferior.setBackground(COLOR_PRIMARIO);
        JButton btnVolver = new JButton(CargadorProperties.obtenerComponentes("CP_UI_001"));
        estilizarBotonSecundario(btnVolver);
        btnVolver.addActionListener(e -> {
            new MenuPrincipal().setVisible(true);
            dispose();
        });
        panelInferior.add(btnVolver);
        add(panelInferior, BorderLayout.SOUTH);
    }

    private JPanel crearPanelVacio() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(COLOR_FONDO_CENTRAL);
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel lblTitulo = new JLabel(CargadorProperties.obtenerComponentes("CP_TITULO_PANEL_VACIO"));
        lblTitulo.setFont(new Font("Poppins", Font.BOLD, 48));
        lblTitulo.setForeground(COLOR_PRIMARIO);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(lblTitulo, gbc);

        return panel;
    }

    // Panel Crear
    private JPanel crearPanelCrear() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(COLOR_FONDO_CENTRAL);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 0, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Inicializar componentes
        // Inicializar componentes
        // txtRucCrear = new JTextField(); -- REEMPLAZADO
        // estilizarCampoTexto(txtRucCrear);
        // txtRucCrear.setPreferredSize(new Dimension(200, 25));

        cmbProveedorCrear = new JComboBox<>();
        estilizarComboBox(cmbProveedorCrear);
        cmbProveedorCrear.setPreferredSize(new Dimension(300, 30));
        DefaultListCellRenderer renderer = new DefaultListCellRenderer();
        renderer.setHorizontalAlignment(DefaultListCellRenderer.CENTER);
        cmbProveedorCrear.setRenderer(renderer);

        // Cargar Proveedores
        cargarProveedores(cmbProveedorCrear);

        // Listener selección proveedor
        cmbProveedorCrear.addActionListener(e -> {
            ItemProveedor item = (ItemProveedor) cmbProveedorCrear.getSelectedItem();
            if (item != null) {
                // Corrected assignment
                // Usare ItemProveedor.proveedor
                proveedorSeleccionado = item.proveedor;

                lblNombreProveedorCrear.setText(proveedorSeleccionado.getNombre());
                lblRucProveedorCrear.setText(proveedorSeleccionado.getCedRuc());
                lblEmailProveedorCrear.setText(proveedorSeleccionado.getEmail());
                lblTelefonoProveedorCrear.setText(proveedorSeleccionado.getCelular());

                cmbProductoCrear.setEnabled(true);
                lblErrorRucCrear.setText("");
            } else {
                proveedorSeleccionado = null;
                lblNombreProveedorCrear.setText("");
                lblRucProveedorCrear.setText("");
                lblEmailProveedorCrear.setText("");
                lblTelefonoProveedorCrear.setText("");
                cmbProductoCrear.setEnabled(false);
            }
        });

        lblNombreProveedorCrear = new JLabel("");
        estilizarLabel(lblNombreProveedorCrear);
        lblRucProveedorCrear = new JLabel("");
        estilizarLabel(lblRucProveedorCrear);
        lblEmailProveedorCrear = new JLabel("");
        estilizarLabel(lblEmailProveedorCrear);
        lblTelefonoProveedorCrear = new JLabel("");
        estilizarLabel(lblTelefonoProveedorCrear);

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

        lblErrorRucCrear = crearLabelError();
        lblErrorCantidadCrear = crearLabelError();

        lblCodigoGenerado = new JLabel();
        configurarLabelConPrefijoBold(lblCodigoGenerado,
                CargadorProperties.obtenerComponentes("CP_LBL_CODIGO"),
                CargadorProperties.obtenerComponentes("CP_CODIGO_GENERANDO"));

        generarYMostrarCodigo();
        configurarValidacionesCrear();

        int fila = 0;

        // Cabecera
        gbc.gridx = 0;
        gbc.gridy = fila++;
        gbc.gridwidth = 8;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 0, 10);
        panel.add(crearCabeceraCompra(
                lblCodigoGenerado, null,
                lblNombreProveedorCrear, lblRucProveedorCrear, lblEmailProveedorCrear, lblTelefonoProveedorCrear,
                true, cmbProveedorCrear, lblErrorRucCrear, null // No action needed for combo
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
        JLabel lblTitProd = new JLabel(CargadorProperties.obtenerComponentes("CP_TITULO_PRODUCTOS_COMPRA"));
        lblTitProd.setFont(FUENTE_TITULO.deriveFont(18f));
        lblTitProd.setForeground(COLOR_TEXTO);
        panel.add(lblTitProd, gbc);
        gbc.gridwidth = 1;

        // Fila: Producto | Cantidad | Agregar
        gbc.gridy = fila++;

        gbc.gridx = 0;
        JLabel lblProd = new JLabel(CargadorProperties.obtenerComponentes("CP_LBL_PRODUCTO"));
        estilizarLabel(lblProd);
        panel.add(lblProd, gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 4;
        panel.add(cmbProductoCrear, gbc);
        gbc.gridwidth = 1;

        gbc.gridx = 5;
        JLabel lblCant = new JLabel(CargadorProperties.obtenerComponentes("CP_LBL_CANTIDAD"));
        estilizarLabel(lblCant);
        panel.add(lblCant, gbc);

        gbc.gridx = 6;
        panel.add(txtCantidadCrear, gbc);

        gbc.gridx = 7;
        gbc.insets = new Insets(5, 3, 0, 10);
        JButton btnAgregar = new JButton(CargadorProperties.obtenerComponentes("CP_UI_003"));
        btnAgregar.setPreferredSize(new Dimension(120, 25));
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
        gbc.insets = new Insets(5, 10, 0, 10);

        gbc.gridx = 6;
        gbc.gridy = fila++;
        gbc.insets = new Insets(0, 10, 0, 10);
        panel.add(lblErrorCantidadCrear, gbc);
        gbc.insets = new Insets(5, 10, 0, 10);

        // Tabla productos
        modeloProductosCrear = crearModeloTablaProductos(true);
        tablaProductosCrear = new JTable(modeloProductosCrear);

        // Sincronizar tabla con lista
        productosCompra = new ArrayList<>();

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

        btnQuitarCrear = new JButton(CargadorProperties.obtenerComponentes("CP_UI_004"));
        estilizarBotonEliminar(btnQuitarCrear);
        btnQuitarCrear.setPreferredSize(new Dimension(90, 25));
        btnQuitarCrear.setEnabled(false);
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

        // Botones Finales
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.gridwidth = 8;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panelBotones.setOpaque(false);

        btnGuardarCrear = new JButton(CargadorProperties.obtenerComponentes("CP_UI_006"));
        estilizarBotonSecundario(btnGuardarCrear);
        btnGuardarCrear.setPreferredSize(new Dimension(100, 30));
        btnGuardarCrear.setEnabled(false);
        btnGuardarCrear.addActionListener(e -> {
            mostrarMensaje(CargadorProperties.obtenerMessages("CP_I_005"), "Info", JOptionPane.INFORMATION_MESSAGE);
            crearCompra("ABI"); // Guardar como Abierta
            btnGuardarCrear.setEnabled(false);
        });
        panelBotones.add(btnGuardarCrear);

        btnAprobarCrear = new JButton(CargadorProperties.obtenerComponentes("CP_UI_005"));
        estilizarBotonPrimario(btnAprobarCrear);
        btnAprobarCrear.setPreferredSize(new Dimension(100, 30));
        btnAprobarCrear.setEnabled(false);
        btnAprobarCrear.addActionListener(e -> crearCompra("APR")); // Guardar como Aprobada
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

    private JPanel crearPanelEliminar() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(COLOR_FONDO_CENTRAL);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 0, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // 1. Selector de Proveedor
        cmbProveedorElimSelector = new JComboBox<>();
        estilizarComboBox(cmbProveedorElimSelector);
        cmbProveedorElimSelector.setPreferredSize(new Dimension(300, 30));
        DefaultListCellRenderer renderer = new DefaultListCellRenderer();
        renderer.setHorizontalAlignment(DefaultListCellRenderer.CENTER);
        cmbProveedorElimSelector.setRenderer(renderer);
        cargarProveedores(cmbProveedorElimSelector);

        int fila = 0;

        // Fila 0: Etiqueta y Combo Proveedor
        gbc.gridx = 0; gbc.gridy = fila;
        JLabel lblProv = new JLabel(CargadorProperties.obtenerComponentes("CP_LBL_PROVEEDOR"));
        estilizarLabel(lblProv);
        panel.add(lblProv, gbc);

        gbc.gridx = 1; gbc.gridwidth = 3;
        panel.add(cmbProveedorElimSelector, gbc);
        gbc.gridwidth = 1;

        // Fila 1: Tabla de Compras
        fila++;
        gbc.gridx = 0; gbc.gridy = fila;
        gbc.gridwidth = 8;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JLabel lblTabla = new JLabel("Seleccione una compra para eliminar:");
        estilizarLabel(lblTabla);
        panel.add(lblTabla, gbc);

        fila++;
        gbc.gridy = fila;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0; gbc.weighty = 0.8;

        modeloComprasElim = crearModeloTablaCompras("eliminar"); // Usa "eliminar" o "completa" según config
        tablaComprasElim = new JTable(modeloComprasElim);
        estilizarTabla(tablaComprasElim);

        JScrollPane scroll = new JScrollPane(tablaComprasElim);
        scroll.setPreferredSize(new Dimension(900, 300));
        panel.add(scroll, gbc);

        // Listener para cargar compras al cambiar proveedor
        cmbProveedorElimSelector.addActionListener(e -> {
            ItemProveedor item = (ItemProveedor) cmbProveedorElimSelector.getSelectedItem();
            if (item != null && item.proveedor != null) {
                cargarComprasEliminables(item.proveedor);
            } else {
                modeloComprasElim.setRowCount(0);
            }
        });

        // Listener tabla para habilitar boton eliminar
        tablaComprasElim.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                btnEliminar.setEnabled(tablaComprasElim.getSelectedRow() != -1);
            }
        });

        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0; gbc.weighty = 0;
        gbc.gridwidth = 1;

        // Botón Eliminar alineado a la derecha
        fila++;
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.gridwidth = 8;                 // ocupar todo el ancho
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(15, 10, 10, 10);


        JPanel panelBotonEliminar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        panelBotonEliminar.setOpaque(false);

        btnEliminar = new JButton(CargadorProperties.obtenerComponentes("BTN_ELIMINAR"));
        estilizarBotonEliminar(btnEliminar);
        btnEliminar.setPreferredSize(new Dimension(140, 30));
        btnEliminar.setEnabled(false);
        btnEliminar.addActionListener(e -> eliminarCompraSeleccionada());

        panelBotonEliminar.add(btnEliminar);
        panel.add(panelBotonEliminar, gbc);

        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;


        return panel;
    }

    private void cargarComprasEliminables(Proveedor p) {
        Compra c = new Compra();
        // Consultar todas las compras del proveedor
        ArrayList<Compra> lista = c.consultarTodasPorParametro(p);
        modeloComprasElim.setRowCount(0);

        if (lista != null) {
            for (Compra cp : lista) {
                // Aqui podriamos filtrar solo ABIERTAS si se requiere, pero VentanaFactura elimina cualquiera (sujeto a BD)
                modeloComprasElim.addRow(new Object[]{
                    cp.getCodigo(),
                    p.getNombre(), // Nombre proveedor en columna
                    cp.getFechaHora().format(FMT_FECHA),
                    String.format("%.2f", cp.getSubtotal()),
                    String.format("%.2f", cp.getIva()),
                    String.format("%.2f", cp.getTotal()),
                    cp.getEstado()
                });
            }
        }
    }

    private void eliminarCompraSeleccionada() {
        int fila = tablaComprasElim.getSelectedRow();
        if (fila == -1) return;

        String codigo = (String) modeloComprasElim.getValueAt(fila, 0);

        int conf = confirmarAccion(
                CargadorProperties.obtenerMessages("CP_C_001") + " " + codigo + "?", // Confirmar eliminar compra X
                CargadorProperties.obtenerMessages("CP_C_002"));

        if (conf != JOptionPane.YES_OPTION) return;

        Compra c = new Compra();
        c.setCodigo(codigo);

        if (c.eliminar()) {
            mostrarMensaje(CargadorProperties.obtenerMessages("CP_I_003"), "Exito", JOptionPane.INFORMATION_MESSAGE);
            // Recargar tabla
            ItemProveedor item = (ItemProveedor) cmbProveedorElimSelector.getSelectedItem();
            if (item != null) {
                cargarComprasEliminables(item.proveedor);
            }
            btnEliminar.setEnabled(false);
        } else {
             mostrarMensaje(CargadorProperties.obtenerMessages("CP_E_005"), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarPanelEliminar() {
        if(cmbProveedorElimSelector != null) cmbProveedorElimSelector.setSelectedIndex(-1);
        if(modeloComprasElim != null) modeloComprasElim.setRowCount(0);
        if(btnEliminar != null) btnEliminar.setEnabled(false);
    }

    private JPanel crearPanelModificar() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(COLOR_FONDO_CENTRAL);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 0, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // --- SECCIÓN 1: SELECCIÓN DE PROVEEDOR Y COMPRA ---

        // 1. Selector de Proveedor
        cmbProveedorModSelector = new JComboBox<>();
        estilizarComboBox(cmbProveedorModSelector);
        cmbProveedorModSelector.setPreferredSize(new Dimension(300, 30));
        DefaultListCellRenderer renderer = new DefaultListCellRenderer();
        renderer.setHorizontalAlignment(DefaultListCellRenderer.CENTER);
        cmbProveedorModSelector.setRenderer(renderer);
        cargarProveedores(cmbProveedorModSelector);

        // Labels informativos del proveedor (para la selección)
        lblNombreProveedorMod = new JLabel(""); estilizarLabel(lblNombreProveedorMod);
        lblRucProveedorMod = new JLabel(""); estilizarLabel(lblRucProveedorMod);
        lblEmailProveedorMod = new JLabel(""); estilizarLabel(lblEmailProveedorMod);
        lblTelefonoProveedorMod = new JLabel(""); estilizarLabel(lblTelefonoProveedorMod);

        // Inicializar componentes de Detalle (Header Compra)
        lblCodigoMod = new JLabel("");
        lblFechaMod = new JLabel("");

        // Inicializar campos de edición
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

        // Fila 0: Etiqueta y Combo Proveedor
        gbc.gridx = 0; gbc.gridy = fila;
        JLabel lblProv = new JLabel(CargadorProperties.obtenerComponentes("CP_LBL_PROVEEDOR"));
        estilizarLabel(lblProv);
        panel.add(lblProv, gbc);

        gbc.gridx = 1; gbc.gridwidth = 3;
        panel.add(cmbProveedorModSelector, gbc);
        gbc.gridwidth = 1;

        // Listener del Combo Proveedor
        cmbProveedorModSelector.addActionListener(e -> {
        ItemProveedor item = (ItemProveedor) cmbProveedorModSelector.getSelectedItem();

        if (item != null && item.proveedor != null) {

            Proveedor p = item.proveedor;

            // CARGAR LABELS
            lblNombreProveedorMod.setText(p.getNombre());
            lblRucProveedorMod.setText(p.getCedRuc());
            lblEmailProveedorMod.setText(p.getEmail());
            lblTelefonoProveedorMod.setText(p.getCelular());

            cargarComprasModificables(p);
            limpiarAreaEdicionMod();

            lblCodigoMod.setText("-");
            lblFechaMod.setText("-");
            panelCabeceraMod.setVisible(true);

        } else {
            limpiarDatosProveedorMod();
            modeloComprasMod.setRowCount(0);
            panelCabeceraMod.setVisible(false);
        }
    });


        // Fila 1: Tabla de Compras Disponibles
        fila++;
        gbc.gridx = 0; gbc.gridy = fila;
        gbc.gridwidth = 8;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JLabel lblTabla = new JLabel("Seleccione una compra abierta para modificar:");
        estilizarLabel(lblTabla);
        panel.add(lblTabla, gbc);

        fila++;
        gbc.gridy = fila;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0; gbc.weighty = 0.2;

        modeloComprasMod = crearModeloTablaCompras("modificar");
        tablaComprasMod = new JTable(modeloComprasMod);
        estilizarTabla(tablaComprasMod);

        JScrollPane scrollCompras = new JScrollPane(tablaComprasMod);
        scrollCompras.setPreferredSize(new Dimension(0, 120));
        panel.add(scrollCompras, gbc);

        // SEPARADOR DEBAJO DE LA TABLA
        fila++;
        gbc.gridy = fila;
        gbc.gridx = 0;
        gbc.gridwidth = 8;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        panel.add(new JSeparator(), gbc);

        // Listener Tabla Compras
        tablaComprasMod.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int filaSel = tablaComprasMod.getSelectedRow();
                if (filaSel >= 0) {
                    String codigo = (String) modeloComprasMod.getValueAt(filaSel, 0);
                    cargarDetalleCompraParaModificar(codigo);
                }
            }
        });

        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0; gbc.weighty = 0;
        gbc.gridwidth = 1;

        // Separador
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(5, 10, 0, 10);

        // --- SECCIÓN 2: DETALLE DE LA COMPRA (Igual que Factura) ---

        // Cabecera de la Compra (Proveedor, Empresa, Info Compra)
        // Usamos el mismo panelCabecera que definimos antes
        fila++;
        gbc.gridx = 0;
        gbc.gridy = fila++;
        gbc.gridwidth = 8;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Reutilizamos crearCabeceraCompra pero pasando los labels de Modificar
        panelCabeceraMod = crearCabeceraCompra(
                lblCodigoMod, lblFechaMod,
                lblNombreProveedorMod, lblRucProveedorMod, lblEmailProveedorMod, lblTelefonoProveedorMod,
                false, null, null, null // No editable (búsqueda ya hecha arriba)
        );
        panelCabeceraMod.setVisible(false);
        panel.add(panelCabeceraMod, gbc);

        gbc.fill = GridBagConstraints.NONE;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(5, 10, 0, 10);

        // Título Productos
        gbc.gridx = 0;
        gbc.gridy = fila++;
        gbc.gridwidth = 8;
        gbc.anchor = GridBagConstraints.WEST;
        JLabel lblTitProd = new JLabel(CargadorProperties.obtenerComponentes("CP_TITULO_PRODUCTOS_COMPRA"));
        lblTitProd.setFont(FUENTE_TITULO.deriveFont(18f));
        lblTitProd.setForeground(COLOR_TEXTO);
        panel.add(lblTitProd, gbc);
        gbc.gridwidth = 1;

        // Campos de Ingreso Producto
        gbc.gridy = fila++;

        gbc.gridx = 0;
        JLabel lblProd = new JLabel(CargadorProperties.obtenerComponentes("CP_LBL_PRODUCTO"));
        estilizarLabel(lblProd);
        panel.add(lblProd, gbc);

        gbc.gridx = 1; gbc.gridwidth = 4;
        panel.add(cmbProductoMod, gbc);
        gbc.gridwidth = 1;

        gbc.gridx = 5;
        JLabel lblCant = new JLabel(CargadorProperties.obtenerComponentes("CP_LBL_CANTIDAD"));
        estilizarLabel(lblCant);
        panel.add(lblCant, gbc);

        gbc.gridx = 6;
        panel.add(txtCantidadMod, gbc);

        gbc.gridx = 7;
        JButton btnAgregar = new JButton(CargadorProperties.obtenerComponentes("CP_UI_003"));
        btnAgregar.setPreferredSize(new Dimension(120, 25));
        estilizarBotonPrimario(btnAgregar);
        btnAgregar.addActionListener(e -> agregarProducto(
                cmbProductoMod, txtCantidadMod, lblErrorCantidadMod,
                modeloProductosMod, txtSubtotalMod, txtIVAMod, txtTotalMod));
        panel.add(btnAgregar, gbc);

        // Error Cantidad
        gbc.gridx = 6; gbc.gridy = fila++;
        panel.add(lblErrorCantidadMod, gbc);

        // Tabla Productos Detalle
        gbc.gridx = 0; gbc.gridy = fila++;
        gbc.gridwidth = 8;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0; gbc.weighty = 0.7;

        modeloProductosMod = crearModeloTablaProductos(true);
        tablaProductosMod = new JTable(modeloProductosMod);
        configurarTablaProductos(modeloProductosMod, tablaProductosMod, true, txtSubtotalMod, txtIVAMod, txtTotalMod);

        JScrollPane scrollProd = new JScrollPane(tablaProductosMod);
        scrollProd.setPreferredSize(new Dimension(0, 200));
        panel.add(scrollProd, gbc);

        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0; gbc.weighty = 0;
        gbc.gridwidth = 1;

        // Botón Quitar
        gbc.gridx = 7; gbc.gridy = fila++;
        gbc.anchor = GridBagConstraints.EAST;
        btnQuitarMod = new JButton(CargadorProperties.obtenerComponentes("CP_UI_004"));
        estilizarBotonEliminar(btnQuitarMod);
        btnQuitarMod.setPreferredSize(new Dimension(90, 25));
        btnQuitarMod.setEnabled(false);
        btnQuitarMod.addActionListener(e -> quitarProducto(
                tablaProductosMod, modeloProductosMod, txtSubtotalMod, txtIVAMod, txtTotalMod));
        panel.add(btnQuitarMod, gbc);

        // Totales
        agregarSeccionTotales(panel, gbc, fila, txtSubtotalMod, txtIVAMod, txtTotalMod);
        fila += 3;

        // Botones Guardar y Aprobar
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.gridwidth = 8;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JPanel panelBotonesMod = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        panelBotonesMod.setOpaque(false);

        btnGuardarModificar = new JButton(CargadorProperties.obtenerComponentes("CP_UI_006"));
        estilizarBotonSecundario(btnGuardarModificar);
        btnGuardarModificar.setPreferredSize(new Dimension(100, 30));
        btnGuardarModificar.setEnabled(false);
        btnGuardarModificar.addActionListener(e -> {
            mostrarMensaje(CargadorProperties.obtenerMessages("CP_I_006"), "Info", JOptionPane.INFORMATION_MESSAGE);
            modificarCompra("ABI");
        });
        panelBotonesMod.add(btnGuardarModificar);

        btnGuardarMod = new JButton(CargadorProperties.obtenerComponentes("CP_UI_005"));
        estilizarBotonPrimario(btnGuardarMod);
        btnGuardarMod.setPreferredSize(new Dimension(100, 30));
        btnGuardarMod.setEnabled(false);
        btnGuardarMod.addActionListener(e -> modificarCompra("APR"));
        panelBotonesMod.add(btnGuardarMod);

        panel.add(panelBotonesMod, gbc);

        configurarValidacionesMod();
        cargarProductosActivos(cmbProductoMod);

        JPanel panelConScroll = new JPanel(new BorderLayout());
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(null);
        panelConScroll.add(scrollPane, BorderLayout.CENTER);

        return panelConScroll;
    }

    private void limpiarDatosProveedorMod() {
        lblNombreProveedorMod.setText("");
        lblRucProveedorMod.setText("");
        lblEmailProveedorMod.setText("");
        lblTelefonoProveedorMod.setText("");
    }

    private void limpiarAreaEdicionMod() {
        modeloProductosMod.setRowCount(0);
        txtSubtotalMod.setText("0.00");
        txtIVAMod.setText("0.00");
        txtTotalMod.setText("0.00");
        lblCodigoMod.setText("");
        lblFechaMod.setText("");
        btnGuardarMod.setEnabled(false);
        btnGuardarModificar.setEnabled(false);
        compraSeleccionada = null;
    }

    private void cargarComprasModificables(Proveedor prov) {
        modeloComprasMod.setRowCount(0);
        Compra bo = new Compra();
        // Consultar todas las del proveedor
        ArrayList<Compra> lista = bo.consultarTodasPorParametro(prov);

        for (Compra c : lista) {
            // FILTRO: Solo ABIERTAS (ABI)
            if ("ABI".equals(c.getEstado())) {
                Object[] fila = {
                        c.getCodigo(),
                        c.getFechaHora().format(FMT_FECHA),
                        String.format("%.2f", c.getTotal())
                };
                modeloComprasMod.addRow(fila);
            }
        }
    }

    private void configurarValidacionesMod() {
        txtCantidadMod.getDocument().addDocumentListener(
                crearValidadorTiempoReal(txtCantidadMod, lblErrorCantidadMod, "cantidad"));
    }

    private void cargarDetalleCompraParaModificar(String codigo) {
        Compra cBusqueda = new Compra();
        cBusqueda.setCodigo(codigo);
        compraSeleccionada = cBusqueda.consultarPorParametro(cBusqueda); // Trae cabecera

        if (compraSeleccionada != null) {
            lblCodigoMod.setText(compraSeleccionada.getCodigo());
            lblFechaMod.setText(compraSeleccionada.getFechaHora().format(FMT_FECHA));

            // Limpiar inputs
            if (cmbProductoMod.getItemCount() > 0) cmbProductoMod.setSelectedIndex(0);
            txtCantidadMod.setText("");
            lblErrorCantidadMod.setText("");

            // Cargar items
            ProxOc pxo = new ProxOc();
            pxo.setCodigoCompra(codigo);
            // Usar metodo del BO ProxOc que llama a MD
            ArrayList<ProxOc> items = pxo.consultarPorCompra(pxo);
            compraSeleccionada.setProductos(items);

            // Llenar tabla GUI
            modeloProductosMod.setRowCount(0);
            for (ProxOc it : items) {
                // Necesitamos nombre prod, buscamos en combo o DB?
                // En la tabla guardamos el objeto ItemProducto o Strings?
                // crearModeloTablaProductos usa: ID, Nombre, Cant, Precio, Subtotal
                // Pero ProxOc solo tiene codigoProducto.
                // Debemos buscar el nombre.
                String nomProd = obtenerNombreProducto(it.getCodigoProducto()); // Helper

                Object[] row = {
                        it.getCodigoProducto(),
                        nomProd,
                        it.getCantidad(),
                        String.format("%.2f", it.getPrecioCompra()),
                        String.format("%.2f", it.getSubtotalProducto())
                };
                modeloProductosMod.addRow(row);
            }

            actualizarTotales(modeloProductosMod, txtSubtotalMod, txtIVAMod, txtTotalMod);

            panelCabeceraMod.setVisible(true);
            btnGuardarMod.setEnabled(true);
            btnGuardarModificar.setEnabled(true);
        }
    }

    // Helper simple para buscar nombre producto en el combo ya cargado
    private String obtenerNombreProducto(String idProd) {
        for (int i=0; i<cmbProductoMod.getItemCount(); i++) {
            ItemProducto ip = cmbProductoMod.getItemAt(i);
            if (ip.producto.getCodigo().equals(idProd)) {
                return ip.producto.getDescripcion();
            }
        }
        return "Producto " + idProd;
    }

    private void modificarCompra(String estado) {
        if (compraSeleccionada == null) return;

        // 1. Actualizar objeto compraSeleccionada con datos de la tabla
        ArrayList<ProxOc> nuevosItems = new ArrayList<>();
        for (int i=0; i<modeloProductosMod.getRowCount(); i++) {
            String idProd = (String) modeloProductosMod.getValueAt(i, 0);
            int cant = Integer.parseInt(modeloProductosMod.getValueAt(i, 2).toString());
            double precio = Double.parseDouble(modeloProductosMod.getValueAt(i, 3).toString().replace(",", "."));

            ProxOc item = new ProxOc();
            item.setCodigoCompra(compraSeleccionada.getCodigo());
            item.setCodigoProducto(idProd);
            item.setCantidad(cant);
            item.setPrecioCompra(precio); // Asumimos precio no cambia o se toma del combo
            item.calcularSubtotal();
            nuevosItems.add(item);
        }
        compraSeleccionada.setProductos(nuevosItems);
        compraSeleccionada.setEstado(estado);

        // 2. Llamar BO
        if (compraSeleccionada.modificar()) { // Este metodo actualiza cabecera e items
            if ("APR".equals(estado)) {
                compraSeleccionada.aprobar(); // Si hay logica extra de aprobacion
            }
            mostrarMensaje("Compra modificada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

            // Recargar
            ItemProveedor item = (ItemProveedor) cmbProveedorModSelector.getSelectedItem();
            if(item != null) cargarComprasModificables(item.proveedor);

            limpiarAreaEdicionMod();
            panelCabeceraMod.setVisible(false);
        } else {
            mostrarMensaje("Error al modificar compra.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel crearPanelPaginacion() {
        return new JPanel();
    }

    private JButton crearBotonPaginacion(String iconName) {
        JButton btn = new JButton();
        btn.setPreferredSize(new Dimension(50, 35));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBackground(COLOR_TEXTO);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder());
        btn.setContentAreaFilled(false);

        // Try load icon
        try {
            java.net.URL url = getClass().getResource("/resources/img/" + iconName);
            if (url != null) {
                ImageIcon icon = new ImageIcon(url);
                Image img = icon.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH);
                btn.setIcon(new ImageIcon(img));
            } else {
                btn.setText(iconName.replace(".png","").substring(0,1).toUpperCase()); // Fallback text
            }
        } catch (Exception e) {
            btn.setText("?");
        }

        btn.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                JButton button = (JButton) c;
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (!button.isEnabled()) {
                    g2.setColor(new Color(200, 200, 200));
                } else if (button.getModel().isRollover()) {
                    g2.setColor(button.getBackground().brighter());
                } else {
                    g2.setColor(button.getBackground());
                }
                g2.fillRoundRect(0, 0, button.getWidth(), button.getHeight(), 10, 10);
                g2.dispose();
                super.paint(g, c);
            }
        });
        return btn;
    }

    private void actualizarTablaPaginada() {
        // Stubbed
    }

    // **MÉTODOS CONSULTAR**
    
    // Cambia el tipo de consulta (General vs Por Proveedor)
    private void cambiarTipoConsulta() {
        String tipo = (String) comboTipoConsulta.getSelectedItem();
        
        modeloTablaResultados.setRowCount(0); // Limpiar tabla
        
        if ("Consulta General".equals(tipo)) {
            panelBusqueda.setVisible(false);
            // Cargar todas las compras (General)
            buscarYCargarCompras(null, modeloTablaResultados, true); 
        } else {
            panelBusqueda.setVisible(true);
            // Cargar proveedores si está vacío
            if (cmbProveedorConsulta.getItemCount() == 0) {
                 cargarProveedores(cmbProveedorConsulta);
            }
        }
        
        // Revalidar layout
        if (panelContenedor != null) {
            panelContenedor.revalidate();
            panelContenedor.repaint();
        }
    }

    private JPanel crearPanelConsultar() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(COLOR_FONDO_CENTRAL);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel superior: Tipo de consulta
        JPanel panelTipo = new JPanel(new GridBagLayout()); 
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
        
        panel.add(panelTipo, BorderLayout.NORTH);
        
        // Panel Central
        JPanel panelCentral = new JPanel(new GridBagLayout());
        panelCentral.setBackground(COLOR_FONDO_CENTRAL);
        GridBagConstraints gbcC = new GridBagConstraints();
        gbcC.fill = GridBagConstraints.HORIZONTAL;
        gbcC.weightx = 1.0;
        gbcC.insets = new Insets(0, 0, 5, 0);

        // Panel de Búsqueda (Por Proveedor) - Inicialmente oculto
        panelBusqueda = new JPanel(new GridBagLayout());
        panelBusqueda.setBackground(COLOR_FONDO_CENTRAL);
        GridBagConstraints gbcBusq = new GridBagConstraints();
        gbcBusq.insets = new Insets(5, 5, 0, 5);

        JLabel lblProv = new JLabel(CargadorProperties.obtenerComponentes("CP_LBL_PROVEEDOR"));
        estilizarLabel(lblProv);
        
        cmbProveedorConsulta = new JComboBox<>();
        estilizarComboBox(cmbProveedorConsulta);
        cmbProveedorConsulta.setPreferredSize(new Dimension(300, 30));
        
        // Listener para buscar al seleccionar proveedor
        cmbProveedorConsulta.addActionListener(e -> {
            ItemProveedor item = (ItemProveedor) cmbProveedorConsulta.getSelectedItem();
            if (item != null) {
                buscarYCargarCompras(item.proveedor, modeloTablaResultados, true);
            } else {
                modeloTablaResultados.setRowCount(0);
            }
        });

        gbcBusq.gridx = 0; gbcBusq.gridy = 0;
        gbcBusq.anchor = GridBagConstraints.EAST;
        panelBusqueda.add(lblProv, gbcBusq);
        
        gbcBusq.gridx = 1; 
        gbcBusq.anchor = GridBagConstraints.WEST;
        panelBusqueda.add(cmbProveedorConsulta, gbcBusq);

        panelBusqueda.setVisible(false); // Oculto por defecto (modo General)
        
        gbcC.gridy = 0;
        gbcC.weighty = 0.0;
        panelCentral.add(panelBusqueda, gbcC);

        // Tabla de Compras
        modeloTablaResultados = crearModeloTablaCompras("consultar");
        tablaResultados = new JTable(modeloTablaResultados);
        estilizarTabla(tablaResultados);
        
        JScrollPane scrollTablaResultados = new JScrollPane(tablaResultados);
        scrollTablaResultados.setBorder(BorderFactory.createEmptyBorder());
        scrollTablaResultados.getViewport().setBackground(COLOR_FONDO_CENTRAL);
        scrollTablaResultados.setPreferredSize(new Dimension(900, 450));
        
        gbcC.gridy = 1;
        gbcC.weighty = 0.3;
        gbcC.fill = GridBagConstraints.BOTH;
        panelCentral.add(scrollTablaResultados, gbcC);
        
        // Listener Tabla
        tablaResultados.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                 if (e.getClickCount() == 2) { // Doble click para ver detalle
                    int row = tablaResultados.getSelectedRow();
                    if (row >= 0) {
                        String codigo = modeloTablaResultados.getValueAt(row, 0).toString();
                        mostrarDetalleCompra(codigo);
                    }
                 }
            }
        });

        // Panel Detalle (Inicialmente oculto)
        panelDetalleConsulta = crearPanelDetalleConsulta();
        panelDetalleConsulta.setVisible(false);
        gbcC.gridy = 3; 
        gbcC.weighty = 0.7;
        gbcC.fill = GridBagConstraints.BOTH;
        panelCentral.add(panelDetalleConsulta, gbcC);
        
        panel.add(panelCentral, BorderLayout.CENTER);
        
        JPanel panelConScroll = new JPanel(new BorderLayout());
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(null);
        panelConScroll.add(scrollPane, BorderLayout.CENTER);

        return panelConScroll;
    }
    
    // Método unificado para buscar y cargar compras
    private void buscarYCargarCompras(Proveedor proveedorFiltro, DefaultTableModel modelo, boolean incluirTodosEstados) {
       modelo.setRowCount(0);
       Compra c = new Compra();
       ArrayList<Compra> lista;
       
       if (proveedorFiltro != null) {
           // Consulta especifica por proveedor
           lista = c.consultarTodasPorParametro(proveedorFiltro);
        } else {
            // Consulta General
            lista = c.consultarTodos(); 
        }
       
       if (lista != null) {
           Proveedor pAux = new Proveedor();
           for (Compra cp : lista) {
               String nombreProv = "N/A";
               if (cp.getCodigoProveedor() != null) {
                   Proveedor pEnc = pAux.verificarPorIdDP(cp.getCodigoProveedor());
                   if (pEnc != null) nombreProv = pEnc.getNombre();
               }

                modelo.addRow(new Object[]{
                        cp.getCodigo(),
                        nombreProv,
                        cp.getFechaHora().format(FMT_FECHA),
                        String.format("%.2f", cp.getSubtotal()),
                        String.format("%.2f", cp.getIva()),
                        String.format("%.2f", cp.getTotal()),
                        cp.getEstado()
                });
           }
       }
       
       if (modelo.getRowCount() == 0) {
           // Mensaje opcional o vacio
       }
    }
    
    private JPanel crearPanelDetalleConsulta() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(COLOR_FONDO_CENTRAL);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 0, 10);
        
        // Botón Cerrar Detalle
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 8;
        gbc.anchor = GridBagConstraints.EAST;
        JButton btnCerrar = new JButton("Cerrar Detalle"); // key?
        estilizarBotonSecundario(btnCerrar);
        btnCerrar.addActionListener(e -> panelDetalleConsulta.setVisible(false));
        panel.add(btnCerrar, gbc);
        
        gbc.gridwidth = 1; gbc.anchor = GridBagConstraints.CENTER;
        
        // Cabecera info
        lblCodigoConsulta = new JLabel();
        lblFechaConsulta = new JLabel();
        lblNombreProveedorConsulta = new JLabel();
        lblRucProveedorConsulta = new JLabel();
        lblEmailProveedorConsulta = new JLabel();
        lblTelefonoProveedorConsulta = new JLabel();
        
        gbc.gridy++;
        gbc.gridx = 0; gbc.gridwidth = 8;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(crearCabeceraCompra(
            lblCodigoConsulta, lblFechaConsulta,
            lblNombreProveedorConsulta, lblRucProveedorConsulta,
            lblEmailProveedorConsulta, lblTelefonoProveedorConsulta,
            false, null, null, null
        ), gbc);
        
        // Tabla Prod
        gbc.gridy++;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0; gbc.weighty = 1.0;
        
        modeloProductosConsulta = crearModeloTablaProductos(false);
        tablaProductosConsulta = new JTable(modeloProductosConsulta);
        estilizarTabla(tablaProductosConsulta);
        
        JScrollPane sc = new JScrollPane(tablaProductosConsulta);
        sc.setPreferredSize(new Dimension(900, 150));
        panel.add(sc, gbc);
        
        // Totales
        txtSubtotalConsulta = crearCampoTotal(false);
        txtIVAConsulta = crearCampoTotal(false);
        txtTotalConsulta = crearCampoTotal(true);
        
        gbc.weighty = 0; gbc.fill = GridBagConstraints.NONE;
        gbc.gridy++;
        agregarSeccionTotales(panel, gbc, gbc.gridy, txtSubtotalConsulta, txtIVAConsulta, txtTotalConsulta);
        
        return panel;
    }


    private void mostrarDetalleCompra(String codigo) {
        Compra c = new Compra();
        c.setCodigo(codigo);
        Compra compra = c.consultarPorParametro(c);

        if (compra == null) return;

        Proveedor p = new Proveedor().verificarPorIdDP(compra.getCodigoProveedor());

        lblCodigoConsulta.setText(compra.getCodigo());
        lblFechaConsulta.setText(compra.getFechaHora().format(FMT_FECHA));

        if (p != null) {
            lblNombreProveedorConsulta.setText(p.getNombre());
            lblRucProveedorConsulta.setText(p.getCedRuc());
            lblEmailProveedorConsulta.setText(p.getEmail());
            lblTelefonoProveedorConsulta.setText(p.getCelular());
        }

        modeloProductosConsulta.setRowCount(0);

        ProxOc po = new ProxOc();
        po.setCodigoCompra(codigo);
        ArrayList<ProxOc> items = po.consultarPorCompra(po);

        for (ProxOc it : items) {
            String nombre = obtenerNombreProducto(it.getCodigoProducto());
            modeloProductosConsulta.addRow(new Object[]{
                    it.getCodigoProducto(),
                    nombre,
                    it.getCantidad(),
                    it.getPrecioCompra(),
                    it.getSubtotalProducto()
            });
        }

        actualizarTotales(modeloProductosConsulta, txtSubtotalConsulta, txtIVAConsulta, txtTotalConsulta);
        panelDetalleConsulta.setVisible(true);
    }

    // Métodos Auxiliares
    private void cambiarPanel() {
        // ... Logica CardLayout
        String opcion = (String) comboOpciones.getSelectedItem();
        if (opcion == null) return;

        switch (opcion) {
            case "Ingresar":
                lblTituloSuperior.setText(CargadorProperties.obtenerComponentes("CP_TITULO_CREAR_COMPRA"));
                cardLayout.show(panelContenedor, PANEL_CREAR);
                generarYMostrarCodigo();
                break;
            case "Modificar":
                lblTituloSuperior.setText(CargadorProperties.obtenerComponentes("CP_TITULO_MODIFICAR_COMPRA"));
                cardLayout.show(panelContenedor, PANEL_MODIFICAR);
                break;
            case "Eliminar":
                lblTituloSuperior.setText(CargadorProperties.obtenerComponentes("CP_TITULO_ELIMINAR_COMPRA"));
                limpiarPanelEliminar();
                cardLayout.show(panelContenedor, PANEL_ELIMINAR);
                break;
            case "Consultar":
                lblTituloSuperior.setText(CargadorProperties.obtenerComponentes("CP_TITULO_CONSULTAR_COMPRA"));
                cardLayout.show(panelContenedor, PANEL_CONSULTAR);
                break;
            default:
                lblTituloSuperior.setText("");
                cardLayout.show(panelContenedor, PANEL_VACIO);
                break;
        }
    }

    private void generarYMostrarCodigo() {
        if (!compraYaInsertada) {
            SwingWorker<String, Void> worker = new SwingWorker<>() {
                @Override
                protected String doInBackground() {
                    return Compra.generarCodigo();
                }
                @Override
                protected void done() {
                    try {
                        codigoCompraActual = get();
                        lblCodigoGenerado.setText(CargadorProperties.obtenerComponentes("CP_LBL_CODIGO") + " " + codigoCompraActual);
                    } catch (Exception e) {
                        lblCodigoGenerado.setText(CargadorProperties.obtenerComponentes("CP_CODIGO_ERROR"));
                    }
                }
            };
            worker.execute();
        }
    }

    private void buscarProveedorCrear() {
        /*
        String ruc = txtRucCrear.getText().trim();
        String error = ValidacionesCompra.validarRucProveedor(ruc);
        if (error != null) {
            lblErrorRucCrear.setText(error);;
            return;
        }

        Proveedor p = new Proveedor();
        p = p.verificarDP(ruc);
        if (p != null) {
            proveedorSeleccionado = p;
            // Solo mostramos el valor, el label "Nombre:" ya está en la estructura del panel header
            lblNombreProveedorCrear.setText(p.getNombre());
            lblRucProveedorCrear.setText(p.getCedRuc());
            lblEmailProveedorCrear.setText(p.getEmail());
            lblTelefonoProveedorCrear.setText(p.getCelular());
            cmbProductoCrear.setEnabled(true);
            lblErrorRucCrear.setText("");
        }
        */
    }

    private void agregarProducto(JComboBox<ItemProducto> cmb, JTextField txtCant, JLabel lblErr,
                                 DefaultTableModel modelo, JTextField txtSub, JTextField txtImp, JTextField txtTot) {
        ItemProducto item = (ItemProducto) cmb.getSelectedItem();
        if (item == null) {
            mostrarMensaje(CargadorProperties.obtenerMessages("CP_A_010"), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String errCant = ValidacionesCompra.validarCantidad(txtCant.getText());
        if (errCant != null) {
            lblErr.setText(errCant);
            return;
        }

        lblErr.setText("");
        int cantidad = Integer.parseInt(txtCant.getText());
        Producto prod = item.producto;

        // Verificar duplicados en tabla
        for (int i = 0; i < modelo.getRowCount(); i++) {
            if (modelo.getValueAt(i, 0).equals(prod.getCodigo())) {
                mostrarMensaje(CargadorProperties.obtenerMessages("CP_A_016") + " " + cantidad, "Info", JOptionPane.INFORMATION_MESSAGE);
                int cantExistente = (int) modelo.getValueAt(i, 2);
                modelo.setValueAt(cantExistente + cantidad, i, 2);
                double nuevoSub = (cantExistente + cantidad) * prod.getPrecioCompra();
                modelo.setValueAt(nuevoSub, i, 4);
                actualizarTotales(modelo, txtSub, txtImp, txtTot);
                return;
            }
        }


        double subtotal = cantidad * prod.getPrecioCompra();

        // Agregar a la lista lógica
        ProxOc nuevoProxOc = new ProxOc();
        nuevoProxOc.setCodigoProducto(prod.getCodigo());
        nuevoProxOc.setCantidad(cantidad);
        nuevoProxOc.setPrecioCompra(prod.getPrecioCompra());
        nuevoProxOc.setSubtotalProducto(subtotal);
        nuevoProxOc.setEstado("ABI"); // Estado inicial

        productosCompra.add(nuevoProxOc);

        // Agregar a la tabla visual
        modelo.addRow(new Object[]{prod.getCodigo(), prod.getDescripcion(), cantidad, prod.getPrecioCompra(), subtotal});
        actualizarTotales(modelo, txtSub, txtImp, txtTot);

        btnGuardarCrear.setEnabled(true);
        btnAprobarCrear.setEnabled(true);
        if(btnQuitarCrear != null) btnQuitarCrear.setEnabled(true);
        // Si estamos en modificar, habilitar sus botones
        if(btnQuitarMod != null && cmb == cmbProductoMod) {
            btnQuitarMod.setEnabled(true);
            if(compraSeleccionada != null) {
                // Si es modificar, agregar a la lista de la compra seleccionada tambien
                ProxOc po = new ProxOc();
                po.setCodigoCompra(compraSeleccionada.getCodigo());
                po.setCodigoProducto(prod.getCodigo());
                po.setCantidad(cantidad);
                po.setPrecioCompra(prod.getPrecioCompra());
                po.setSubtotalProducto(subtotal);
                compraSeleccionada.getProductos().add(po);
            }
        }
    }

    private void quitarProducto(JTable tabla, DefaultTableModel modelo, JTextField txtSub, JTextField txtImp, JTextField txtTot) {
        int row = tabla.getSelectedRow();
        if (row >= 0) {
            modelo.removeRow(row);
            // Remover de la lista
            if (tabla == tablaProductosCrear && row < productosCompra.size()) {
                productosCompra.remove(row);
            } else if (tabla == tablaProductosMod && compraSeleccionada != null && row < compraSeleccionada.getProductos().size()) {
                compraSeleccionada.getProductos().remove(row);
            }
            actualizarTotales(modelo, txtSub, txtImp, txtTot);
        }
    }

    private void actualizarTotales(DefaultTableModel modelo, JTextField txtSub, JTextField txtImp, JTextField txtTot) {
        double subtotal = 0;
        for (int i = 0; i < modelo.getRowCount(); i++) {
            try {
                Object val = modelo.getValueAt(i, 4); // Columna Subtotal
                double valDouble;
                if (val instanceof Number) {
                    valDouble = ((Number) val).doubleValue();
                } else {
                    valDouble = Double.parseDouble(val.toString().replace(",", "."));
                }
                subtotal += valDouble;
            } catch (Exception e) {
                // Ignore parsing errors
            }
        }
        txtSub.setText(String.format("%.2f", subtotal));
        double iva = subtotal * 0.15; // Asumimos 15%
        txtImp.setText(String.format("%.2f", iva));
        txtTot.setText(String.format("%.2f", subtotal + iva));
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
                    if (e.getType() == TableModelEvent.UPDATE && e.getColumn() == 2) {
                        validarYActualizarCantidad(modelo, txtSub, txtIva, txtTotal, e.getFirstRow());
                    }
                }
            });
        }
    }

    // Valida y actualiza cantidad editada en tabla (con POPUP)
    private void validarYActualizarCantidad(DefaultTableModel modelo, JTextField txtSub, JTextField txtIva,
                                            JTextField txtTot, int fila) {
        try {
            Object valor = modelo.getValueAt(fila, 2);
            int cantidad = Integer.parseInt(valor.toString());

            if (cantidad <= 0) {
                mostrarMensaje(
                        CargadorProperties.obtenerMessages("CP_A_012"), // Mensaje positivo
                        CargadorProperties.obtenerMessages("CP_C_004"),
                        JOptionPane.ERROR_MESSAGE);
                // Restaurar valor anterior
                int cantAnterior = 1;
                if(modelo == modeloProductosCrear && fila < productosCompra.size()) {
                    cantAnterior = productosCompra.get(fila).getCantidad();
                } else if (modelo == modeloProductosMod && compraSeleccionada != null && fila < compraSeleccionada.getProductos().size()) {
                    cantAnterior = compraSeleccionada.getProductos().get(fila).getCantidad();
                }
                modelo.setValueAt(cantAnterior, fila, 2);
                // Asegurarse de que el listener no vuelva a dispararse o manejar reentrancia si fuera necesario
                return;
            }

            // Actualizar objeto lógico y recalcular subtotal
            ProxOc pxf = null;
            if (modelo == modeloProductosCrear && fila < productosCompra.size()) {
                pxf = productosCompra.get(fila);
            } else if (modelo == modeloProductosMod && compraSeleccionada != null && fila < compraSeleccionada.getProductos().size()) {
                pxf = compraSeleccionada.getProductos().get(fila);
            }

            if (pxf != null) {
                pxf.setCantidad(cantidad);
                pxf.calcularSubtotal(); // Recalcula subtotal basado en cantidad * precio
                // Actualizar subtotal en tabla
                // Deshabilitar listener temporalmente para evitar ciclo infinito?
                // setValueAt disparara UPDATE evento en columna 4.
                // Nuestro listener solo escucha columna 2. Asi que esta SAFE.
                modelo.setValueAt(pxf.getSubtotalProducto(), fila, 4);
            }

            actualizarTotales(modelo, txtSub, txtIva, txtTot);

        } catch (NumberFormatException ex) {
            // Restaurar si falla
            int cantAnterior = 1;
            if (modelo == modeloProductosCrear && fila < productosCompra.size()) {
                cantAnterior = productosCompra.get(fila).getCantidad();
            } else if (modelo == modeloProductosMod && compraSeleccionada != null && fila < compraSeleccionada.getProductos().size()) {
                cantAnterior = compraSeleccionada.getProductos().get(fila).getCantidad();
            }

            mostrarMensaje(
                    CargadorProperties.obtenerMessages("CP_A_008"), // Solo enteros
                    CargadorProperties.obtenerMessages("CP_C_004"),
                    JOptionPane.ERROR_MESSAGE);
            modelo.setValueAt(cantAnterior, fila, 2);
        }
    }

    private void mostrarError(JLabel label, String mensaje) {
        if (mensaje != null) {
            label.setText(mensaje);
        } else {
            label.setText(" ");
        }
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
                // Si el campo esta vacio
                if (texto.isEmpty()) {
                    mostrarError(lblError, null);
                    return;
                }

                String error = null;
                switch (tipo) {
                    case "cedula": // Validacion Ruc
                        error = ValidacionesCompra.validarRucInput(texto);
                        break;
                    case "cantidad":
                        // Validacion Cantidad
                        error = ValidacionesCompra.validarCantidad(texto);
                        break;
                }
                mostrarError(lblError, error);
            }
        };
    }

    private void crearCompra(String estado) {
        if (proveedorSeleccionado == null) {
            mostrarMensaje(CargadorProperties.obtenerMessages("CP_A_004"), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (modeloProductosCrear.getRowCount() == 0) {
            mostrarMensaje(CargadorProperties.obtenerMessages("CP_A_005"), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Compra compra = new Compra();
        compra.setCodigo(codigoCompraActual);
        compra.setCodigoProveedor(proveedorSeleccionado.getIdProveedor());
        compra.setFechaHora(LocalDateTime.now());
        
        // SIEMPRE insertar como ABIERTA primero
        compra.setEstado("ABI"); 

        // Productos
        // Usamos la lista productosCompra que ya está sincronizada
        compra.setProductos(new ArrayList<>(productosCompra));

        String res = compra.insertar();
        
        if (res != null) {
            // Si el usuario pidió APROBAR, intentamos aprobar ahora
            if ("APR".equals(estado)) {
                if (compra.aprobar()) {
                     mostrarMensaje(CargadorProperties.obtenerMessages("CP_I_001") + " y Aprobada.", "Exito", JOptionPane.INFORMATION_MESSAGE);
                } else {
                     mostrarMensaje("Compra guardada como ABIERTA, pero falló la aprobación.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                }
            } else {
                // Solo guardar (ABI)
                mostrarMensaje(CargadorProperties.obtenerMessages("CP_I_001"), "Exito", JOptionPane.INFORMATION_MESSAGE);
            }
            
            limpiarPanelCrear();
            generarYMostrarCodigo();
        } else {
            mostrarMensaje(CargadorProperties.obtenerMessages("CP_E_002"), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }



    // -- Métodos de configuración visual -- (Copiados de VentanaFactura y adaptados)
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
    private void estilizarLabel(JLabel lbl) {
        lbl.setFont(FUENTE_LABEL);
        lbl.setForeground(COLOR_TEXTO);
    }
    private void estilizarBotonPrimario(JButton btn) {
        btn.setFont(FUENTE_BOTON);
        btn.setBackground(COLOR_SECUNDARIO);
        btn.setForeground(COLOR_BLANCO);
    }
    private void estilizarBotonSecundario(JButton btn) {
        btn.setFont(FUENTE_BOTON);
        btn.setBackground(COLOR_ENFASIS);
        btn.setForeground(COLOR_BLANCO);
    }
    private void estilizarBotonEliminar(JButton btn) {
        btn.setFont(FUENTE_BOTON);
        btn.setBackground(COLOR_ACENTO);
        btn.setForeground(COLOR_BLANCO);
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

    private JLabel crearLabelError() {
        JLabel lbl = new JLabel("");
        lbl.setForeground(COLOR_ACENTO);
        lbl.setFont(FUENTE_LABEL.deriveFont(11f));
        return lbl;
    }

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

    private void configurarLabelConPrefijoBold(JLabel label, String prefix, String content) {
        label.setText("<html><b>" + prefix + "</b> " + content + "</html>");
        label.setFont(FUENTE_TITULO.deriveFont(20f));
        label.setForeground(COLOR_TEXTO);
    }

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

    private DefaultTableModel crearModeloTablaProductos(boolean editable) {
        String[] cols = CargadorProperties.obtenerComponentes("CP_TABLA_PRODUCTOS_COLS").split(",");
        return new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // For simplcity in this view
            }
        };
    }

    private DefaultTableModel crearModeloTablaCompras(String tipo) {
        String key = tipo.equals("modificar") ? "CP_TABLA_COMPRAS_MOD_COLS" : "CP_TABLA_COMPRAS_COLS";
        String[] cols = CargadorProperties.obtenerComponentes(key).split(",");
        return new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
    }

    private void cargarProductosActivos(JComboBox<ItemProducto> combo) {
        combo.removeAllItems();
        Producto p = new Producto();
        ArrayList<Producto> lista = p.consultarTodos();
        for (Producto prod : lista) {
            if ("ACT".equals(prod.getEstado())) {
                combo.addItem(new ItemProducto(prod));
            }
        }
    }

    private JButton crearBotonLupa(Runnable action) {
        JButton btn = new JButton(CargadorProperties.obtenerComponentes("BTN_LUPA")); // ? char
        btn.setPreferredSize(new Dimension(50, 25));
        estilizarBotonSecundario(btn);
        btn.addActionListener(e -> action.run());
        return btn;
    }

    // Cabecera Compra (Proveedor info)
    private JPanel crearCabeceraCompra(JLabel lblCod, JLabel lblFech, JLabel lblNom, JLabel lblRuc,
                                       JLabel lblEm, JLabel lblTel, boolean isEditable, JComponent inputComponent, JLabel lblErr, Runnable searchAction) {

        JPanel panelCabecera = new JPanel(new BorderLayout(20, 10));
        panelCabecera.setBackground(COLOR_FONDO_CENTRAL); // Mismo fondo que Factura
        panelCabecera.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // PANEL IZQUIERDO: Proveedor
        JPanel panelProveedor = new JPanel();
        panelProveedor.setOpaque(false);
        panelProveedor.setLayout(new BoxLayout(panelProveedor, BoxLayout.Y_AXIS));

        JLabel lblTitProv = new JLabel(CargadorProperties.obtenerComponentes("CP_LBL_INFO_PROVEEDOR"));
        if (lblTitProv.getText() == null || lblTitProv.getText().startsWith("!")) lblTitProv.setText("Información del Proveedor");

        lblTitProv.setFont(FUENTE_TITULO.deriveFont(16f));
        lblTitProv.setForeground(COLOR_TEXTO);
        lblTitProv.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelProveedor.add(lblTitProv);
        panelProveedor.add(Box.createVerticalStrut(5));

        // Si es editable (Panel Crear) tiene busqueda
        if (isEditable && inputComponent != null) {
            JPanel pBusqueda = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
            pBusqueda.setOpaque(false);
            JLabel lblRucTit = new JLabel(CargadorProperties.obtenerComponentes("CP_LBL_PROVEEDOR")); // Generic Label
            if(lblRucTit.getText().startsWith("!")) lblRucTit.setText("Proveedor:");

            estilizarLabel(lblRucTit);
            pBusqueda.add(lblRucTit);
            pBusqueda.add(inputComponent);

            if (searchAction != null) {
                pBusqueda.add(crearBotonLupa(searchAction));
            }
            panelProveedor.add(pBusqueda);

            if (lblErr != null) {
                lblErr.setAlignmentX(Component.CENTER_ALIGNMENT);
                panelProveedor.add(lblErr);
            }
            panelProveedor.add(Box.createVerticalStrut(3));
        }

        // Info Proveedor
        agregarCampoInfoProveedorCabecera(panelProveedor, "FC_UI_008", lblNom);
        agregarCampoInfoProveedorCabecera(panelProveedor, "CP_LBL_RUC_PROVEEDOR", lblRuc);
        agregarCampoInfoProveedorCabecera(panelProveedor, "FC_UI_009", lblEm);
        agregarCampoInfoProveedorCabecera(panelProveedor, "FC_UI_010", lblTel);

        // PANEL CENTRO: Empresa (Igual que Factura)
        JPanel panelEmpresa = new JPanel();
        panelEmpresa.setOpaque(false);
        panelEmpresa.setLayout(new BoxLayout(panelEmpresa, BoxLayout.Y_AXIS));
        // Borde derecho para separar visualmente
        panelEmpresa.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 50));

        JLabel lblTitEmpresa = new JLabel(CargadorProperties.obtenerComponentes("LBL_INFO_EMPRESA"));
        lblTitEmpresa.setFont(FUENTE_TITULO.deriveFont(16f));
        lblTitEmpresa.setForeground(COLOR_TEXTO);
        lblTitEmpresa.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelEmpresa.add(lblTitEmpresa);
        panelEmpresa.add(Box.createVerticalStrut(10));

        // Info Empresa
        agregarCampoInfoEmpresa(panelEmpresa, "FC_UI_008", "EMPRESA_NOMBRE");
        agregarCampoInfoEmpresa(panelEmpresa, "FC_UI_009", "EMPRESA_EMAIL");
        agregarCampoInfoEmpresa(panelEmpresa, "FC_UI_010", "EMPRESA_TELEFONO");
        panelEmpresa.add(Box.createVerticalGlue());

        // PANEL DERECHO: Compra Info
        JPanel panelCompra = new JPanel();
        panelCompra.setOpaque(false);
        panelCompra.setLayout(new BoxLayout(panelCompra, BoxLayout.Y_AXIS));

        JLabel lblTitCompra = new JLabel(CargadorProperties.obtenerComponentes("CP_LBL_INFO_COMPRA"));
        if (lblTitCompra.getText() == null || lblTitCompra.getText().startsWith("!")) lblTitCompra.setText("Información de la Compra");

        lblTitCompra.setFont(FUENTE_TITULO.deriveFont(16f));
        lblTitCompra.setForeground(COLOR_TEXTO);
        lblTitCompra.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelCompra.add(lblTitCompra);
        panelCompra.add(Box.createVerticalStrut(10));

        if (lblCod != null) {
            lblCod.setFont(FUENTE_LABEL.deriveFont(Font.BOLD));
            lblCod.setForeground(COLOR_TEXTO);
            lblCod.setAlignmentX(Component.CENTER_ALIGNMENT);
            panelCompra.add(lblCod);
            panelCompra.add(Box.createVerticalStrut(8));
        }

        JPanel pFecha = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        pFecha.setOpaque(false);
        JLabel lblFechaTit = new JLabel(CargadorProperties.obtenerComponentes("LBL_FECHA_HORA"));
        lblFechaTit.setFont(FUENTE_LABEL.deriveFont(Font.BOLD));
        lblFechaTit.setForeground(COLOR_TEXTO);
        pFecha.add(lblFechaTit);

        if (lblFech != null) {
            estilizarLabel(lblFech);
            pFecha.add(lblFech);
        } else {
            LocalDateTime ahora = LocalDateTime.now();
            String fechaHora = ahora.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
            JLabel lblFechaActual = new JLabel(fechaHora);
            estilizarLabel(lblFechaActual);
            pFecha.add(lblFechaActual);
        }
        pFecha.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelCompra.add(pFecha);
        panelCompra.add(Box.createVerticalGlue());

        // Ensamblar
        panelCabecera.add(panelProveedor, BorderLayout.WEST);
        panelCabecera.add(panelEmpresa, BorderLayout.CENTER);
        panelCabecera.add(panelCompra, BorderLayout.EAST);

        return panelCabecera;
    }

    private void agregarCampoInfoProveedorCabecera(JPanel panel, String keyLabel, JLabel lblValor) {
        JPanel pCampo = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        pCampo.setOpaque(false);
        String txtLabel = CargadorProperties.obtenerComponentes(keyLabel);
        if (txtLabel.startsWith("!")) txtLabel = keyLabel.contains("RUC") ? "RUC:" : keyLabel; // Fallback simple

        JLabel lblTit = new JLabel(txtLabel);
        lblTit.setFont(FUENTE_LABEL.deriveFont(Font.BOLD));
        lblTit.setForeground(COLOR_TEXTO);
        pCampo.add(lblTit);

        if (lblValor != null) {
            // Limpiar texto previo si tenia prefijos manuales
            pCampo.add(lblValor);
        }
        pCampo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(pCampo);
        panel.add(Box.createVerticalStrut(5));
    }

    private void agregarCampoInfoEmpresa(JPanel panel, String keyLabel, String keyValor) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        p.setOpaque(false);
        JLabel lblTit = new JLabel(CargadorProperties.obtenerComponentes(keyLabel));
        lblTit.setFont(FUENTE_LABEL.deriveFont(Font.BOLD));
        lblTit.setForeground(COLOR_TEXTO);
        p.add(lblTit);

        JLabel lblVal = new JLabel(CargadorProperties.obtenerComponentes(keyValor));
        estilizarLabel(lblVal);
        p.add(lblVal);

        p.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(p);
        panel.add(Box.createVerticalStrut(8));
    }

    // Auxiliar para seleccion en Modificar
    private void seleccionarYMostrarCompra(JTable tabla, DefaultTableModel modComp, DefaultTableModel modProd,
                                           JPanel pHeader, JLabel lCod, JLabel lFec, JLabel lNom, JLabel lRuc, JLabel lEm, JLabel lTel,
                                           JTextField tSub, JTextField tIva, JTextField tTot, boolean editable,
                                           JComboBox<ItemProducto> cmb, JTextField tCant, JButton btnSave) {
        // Stubbed
    }


    private void limpiarPanelCrear() {
        if(cmbProveedorCrear != null) cmbProveedorCrear.setSelectedIndex(-1);
        // txtRucCrear.setText(""); -- REMOVED
        lblNombreProveedorCrear.setText("");
        lblRucProveedorCrear.setText("");
        lblEmailProveedorCrear.setText("");
        lblTelefonoProveedorCrear.setText("");
        modeloProductosCrear.setRowCount(0);
        txtSubtotalCrear.setText("0.00");
        txtIVACrear.setText("0.00");
        txtTotalCrear.setText("0.00");
        cmbProductoCrear.setEnabled(false);
        compraYaInsertada = false;
        productosCompra.clear(); // Limpiar lista lógica
        generarYMostrarCodigo();
    }

    private void limpiarPanelModificar() {
        // Stubbed
    }

    // **Validaciones UI**
    private void configurarValidacionesCrear() {
        // txtRucCrear validation removed
        /* txtRucCrear.getDocument().addDocumentListener(
                crearValidadorTiempoReal(txtRucCrear, lblErrorRucCrear, "cedula")); */
        txtCantidadCrear.getDocument().addDocumentListener(
                crearValidadorTiempoReal(txtCantidadCrear, lblErrorCantidadCrear, "cantidad"));
    }

    private Font cargarFuente(String path, int style, float size) {
        try {
            Font font = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream(path)).deriveFont(style, size);
            return font;
        } catch (Exception e) {
            return new Font("Arial", style, (int)size);
        }
    }

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

    // Clase interna item producto
    private static class ItemProducto {
        Producto producto;
        ItemProducto(Producto p) { this.producto = p; }
        @Override public String toString() { return producto.getCodigo() + " - " + producto.getDescripcion(); }
    }

    // Clase interna item proveedor
    private static class ItemProveedor {
        Proveedor proveedor;
        ItemProveedor(Proveedor p) { this.proveedor = p; }
        @Override public String toString() { return proveedor.getCedRuc() + " - " + proveedor.getNombre(); }
    }

    private void cargarProveedores(JComboBox<ItemProveedor> combo) {
        combo.removeAllItems();
        Proveedor p = new Proveedor();
        ArrayList<Proveedor> lista = p.consultarTodos();
        if(lista != null) {
            for (Proveedor prov : lista) {
                // if ("ACT".equals(prov.getEstado())) {
                combo.addItem(new ItemProveedor(prov));
                // }
            }
        }
        combo.setSelectedIndex(-1);
    }

    private void volverAlMenu() {
        dispose();
    }
}
