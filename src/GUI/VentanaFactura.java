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
    private static final String PANEL_VACIO = "VACIO";
    private static final String PANEL_CREAR = "CREAR";
    private static final String PANEL_MODIFICAR = "MODIFICAR";
    private static final String PANEL_ELIMINAR = "ELIMINAR";
    private static final String PANEL_CONSULTAR = "CONSULTAR";
    private static final DateTimeFormatter FMT_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    
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
    private JTable tablaResultados;
    private DefaultTableModel modeloTablaResultados;
    private JPanel panelBusqueda;
    
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
    
    //Inicializa CardLayout y crea todos los paneles
    private void inicializarComponentes() {
        cardLayout = new CardLayout();
        panelContenedor = new JPanel(cardLayout);
        
        String[] opciones = CargadorProperties.obtenerComponentes("CMB_OPERACION").split(",");
        comboOpciones = new JComboBox<>(opciones);
        comboOpciones.addActionListener(e -> cambiarPanel());
        
        panelContenedor.add(crearPanelVacio(), PANEL_VACIO);
        panelContenedor.add(crearPanelCrear(), PANEL_CREAR);
        panelContenedor.add(crearPanelModificar(), PANEL_MODIFICAR);
        panelContenedor.add(crearPanelEliminar(), PANEL_ELIMINAR);
        panelContenedor.add(crearPanelConsultar(), PANEL_CONSULTAR);
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
    
    //**CREACION DE PANELES**
    //Crea panel vacío con título centrado
    private JPanel crearPanelVacio() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        JLabel lblTitulo = new JLabel(CargadorProperties.obtenerComponentes("TITULO_PANEL_VACIO"));
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 48));
        lblTitulo.setForeground(Color.DARK_GRAY);
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(lblTitulo, gbc);
        
        return panel;
    }
    
    //Crea panel para ingresar nueva factura
    private JPanel crearPanelCrear() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 0, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
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
        
        txtSubtotalCrear = new JTextField(CargadorProperties.obtenerComponentes("TOTAL_INICIAL"));
        txtSubtotalCrear.setEditable(false);
        txtSubtotalCrear.setPreferredSize(new Dimension(120, 25));
        
        txtIVACrear = new JTextField(CargadorProperties.obtenerComponentes("TOTAL_INICIAL"));
        txtIVACrear.setEditable(false);
        txtIVACrear.setPreferredSize(new Dimension(120, 25));
        
        txtTotalCrear = new JTextField(CargadorProperties.obtenerComponentes("TOTAL_INICIAL"));
        txtTotalCrear.setEditable(false);
        txtTotalCrear.setFont(new Font("Arial", Font.BOLD, 12));
        txtTotalCrear.setPreferredSize(new Dimension(120, 25));
        
        lblErrorCedulaCrear = crearLabelError();
        lblErrorCantidadCrear = crearLabelError();
        
        lblCodigoGenerado = new JLabel(CargadorProperties.obtenerComponentes("LBL_CODIGO") + " " + CargadorProperties.obtenerComponentes("CODIGO_GENERANDO"));
        lblCodigoGenerado.setFont(new Font("Arial", Font.BOLD, 12));
        
        generarYMostrarCodigo();
        configurarValidacionesCrear();
        
        int fila = 0;
                
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.gridwidth = 8;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 10, 15, 10);
        JLabel lblTitulo = new JLabel(CargadorProperties.obtenerComponentes("TITULO_CREAR_FACTURA"));
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 42));
        panel.add(lblTitulo, gbc);
        gbc.gridwidth = 1;
        gbc.insets = new Insets(5, 10, 0, 10);
        
        fila++;

        // PANEL IZQUIERDO (Código y Fecha)
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

        JLabel lblFechaTit = new JLabel(CargadorProperties.obtenerComponentes("LBL_FECHA_HORA"));
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

        // PANEL EMPRESA (Centro)
        gbc.gridx = 6;
        gbc.gridy = fila;
        gbc.gridwidth = 4;
        gbc.anchor = GridBagConstraints.NORTHWEST;

        JPanel panelEmpresa = new JPanel();
        panelEmpresa.setLayout(new BoxLayout(panelEmpresa, BoxLayout.Y_AXIS));

        JLabel lblEmpNom = new JLabel(CargadorProperties.obtenerComponentes("EMPRESA_NOMBRE"));
        lblEmpNom.setFont(new Font("Arial", Font.BOLD, 16));
        lblEmpNom.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelEmpresa.add(lblEmpNom);
        panelEmpresa.add(Box.createVerticalStrut(3));

        JLabel lblEmpEmail = new JLabel(CargadorProperties.obtenerComponentes("EMPRESA_EMAIL"));
        lblEmpEmail.setFont(new Font("Arial", Font.PLAIN, 14));
        lblEmpEmail.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelEmpresa.add(lblEmpEmail);
        panelEmpresa.add(Box.createVerticalStrut(3));

        JLabel lblEmpTel = new JLabel(CargadorProperties.obtenerComponentes("EMPRESA_TELEFONO"));
        lblEmpTel.setFont(new Font("Arial", Font.PLAIN, 14));
        lblEmpTel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelEmpresa.add(lblEmpTel);

        panel.add(panelEmpresa, gbc);

        // PANEL CLIENTE (Derecha)
        gbc.gridx = 6;
        gbc.gridy = fila;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.NORTHEAST;

        JPanel panelCliente = new JPanel(new GridBagLayout());
        GridBagConstraints gbcCli = new GridBagConstraints();
        gbcCli.anchor = GridBagConstraints.WEST;
        gbcCli.insets = new Insets(2, 5, 2, 5);

       int filaC = 0;
        
        gbcCli.gridx = 0;
        gbcCli.gridy = filaC;
        gbcCli.gridwidth = 3;
        JLabel lblTitCliente = new JLabel(CargadorProperties.obtenerComponentes("LBL_INFO_CLIENTE"));
        lblTitCliente.setFont(new Font("Arial", Font.BOLD, 16));
        panelCliente.add(lblTitCliente, gbcCli);
        gbcCli.gridwidth = 1;
        
        filaC++;
        gbcCli.gridx = 0;
        gbcCli.gridy = filaC;
        JLabel lblCedRuc = new JLabel(CargadorProperties.obtenerComponentes("LBL_CEDULA_RUC"));
        lblCedRuc.setFont(new Font("Arial", Font.BOLD, 12));
        panelCliente.add(lblCedRuc, gbcCli);
        
        gbcCli.gridx = 1;
        panelCliente.add(txtCedulaCrear, gbcCli);
        
        //AQUI
        gbcCli.gridx = 2;
        JLabel btnLupa = new JLabel(CargadorProperties.obtenerComponentes("BTN_LUPA"));
        btnLupa.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        btnLupa.setPreferredSize(new Dimension(35, 25));
        btnLupa.setHorizontalAlignment(SwingConstants.CENTER); // Centra el texto
        btnLupa.setOpaque(true); // Permite que se vea el fondo
        btnLupa.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                buscarClienteCrear();
            }
        });
        panelCliente.add(btnLupa, gbcCli);
        
        
        filaC++;
        gbcCli.gridx = 1;
        gbcCli.gridy = filaC;
        gbcCli.gridwidth = 2;
        panelCliente.add(lblErrorCedulaCrear, gbcCli);
        gbcCli.gridwidth = 1;
        
        filaC++;
        gbcCli.gridx = 0;
        gbcCli.gridy = filaC;
        JLabel lblNomTit = new JLabel(CargadorProperties.obtenerComponentes("LBL_NOMBRE"));
        lblNomTit.setFont(new Font("Arial", Font.BOLD, 12));
        panelCliente.add(lblNomTit, gbcCli);
        
        gbcCli.gridx = 1;
        gbcCli.gridwidth = 2;
        lblNombreClienteCrear.setFont(new Font("Arial", Font.PLAIN, 12));
        panelCliente.add(lblNombreClienteCrear, gbcCli);
        gbcCli.gridwidth = 1;
        
        filaC++;
        gbcCli.gridx = 0;
        gbcCli.gridy = filaC;
        JLabel lblCedTit = new JLabel(CargadorProperties.obtenerComponentes("FC_UI_019"));
        lblCedTit.setFont(new Font("Arial", Font.BOLD, 12));
        panelCliente.add(lblCedTit, gbcCli);
        
        gbcCli.gridx = 1;
        gbcCli.gridwidth = 2;
        lblCedulaClienteCrear.setFont(new Font("Arial", Font.PLAIN, 12));
        panelCliente.add(lblCedulaClienteCrear, gbcCli);
        gbcCli.gridwidth = 1;
        
        filaC++;
        gbcCli.gridx = 0;
        gbcCli.gridy = filaC;
        JLabel lblEmailTit = new JLabel(CargadorProperties.obtenerComponentes("LBL_EMAIL"));
        lblEmailTit.setFont(new Font("Arial", Font.BOLD, 12));
        panelCliente.add(lblEmailTit, gbcCli);
        
        gbcCli.gridx = 1;
        gbcCli.gridwidth = 2;
        lblEmailClienteCrear.setFont(new Font("Arial", Font.PLAIN, 12));
        panelCliente.add(lblEmailClienteCrear, gbcCli);
        gbcCli.gridwidth = 1;
        
        filaC++;
        gbcCli.gridx = 0;
        gbcCli.gridy = filaC;
        JLabel lblTelTit = new JLabel(CargadorProperties.obtenerComponentes("LBL_TELEFONO"));
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
        
        fila++;
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.gridwidth = 8;
        JLabel lblTitProd = new JLabel(CargadorProperties.obtenerComponentes("TITULO_PRODUCTOS_FACTURA"));
        lblTitProd.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(lblTitProd, gbc);
        gbc.gridwidth = 1;
        
        fila++;
        gbc.gridx = 0;
        gbc.gridy = fila;
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
        btnAgregar.addActionListener(e -> agregarProducto());
        panel.add(btnAgregar, gbc);
        
        fila++;
        gbc.gridx = 6;
        gbc.gridy = fila;
        gbc.insets = new Insets(0, 10, 0, 10);
        panel.add(lblErrorCantidadCrear, gbc);
        gbc.insets = new Insets(5, 10, 0, 10);
        
        fila++;
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.gridwidth = 8;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(10, 10, 10, 10);
        
        String[] cols = CargadorProperties.obtenerComponentes("TABLA_PRODUCTOS_COLS").split(",");
        modeloProductosCrear = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2;  // Solo columna Cantidad editable
            }

            @Override
            public Class<?> getColumnClass(int column) {
                if (column == 2) return Integer.class;
                return String.class;
            }
        };
        tablaProductosCrear = new JTable(modeloProductosCrear);
        modeloProductosCrear.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if (e.getType() == TableModelEvent.UPDATE && e.getColumn() == 2) {
                    validarYActualizarCantidad(modeloProductosCrear, txtSubtotalCrear, txtIVACrear, txtTotalCrear, e.getFirstRow());
                }
            }
        });
        JScrollPane scrollTabla = new JScrollPane(tablaProductosCrear);
        scrollTabla.setPreferredSize(new Dimension(800, 180));
        panel.add(scrollTabla, gbc);
        
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
        btnQuitar.addActionListener(e -> quitarProducto(tablaProductosCrear, modeloProductosCrear));
        panel.add(btnQuitar, gbc);
        
        fila++;
        gbc.gridx = 6;
        gbc.gridy = fila;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel lblSub = new JLabel(CargadorProperties.obtenerComponentes("LBL_SUBTOTAL"));
        lblSub.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(lblSub, gbc);
        
        gbc.gridx = 7;
        panel.add(txtSubtotalCrear, gbc);
        
        fila++;
        gbc.gridx = 6;
        gbc.gridy = fila;
        JLabel lblIva = new JLabel(CargadorProperties.obtenerComponentes("LBL_IVA"));
        lblIva.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(lblIva, gbc);
        
        gbc.gridx = 7;
        panel.add(txtIVACrear, gbc);
        
        fila++;
        gbc.gridx = 6;
        gbc.gridy = fila;
        JLabel lblTot = new JLabel(CargadorProperties.obtenerComponentes("LBL_TOTAL"));
        lblTot.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(lblTot, gbc);
        
        gbc.gridx = 7;
        panel.add(txtTotalCrear, gbc);
        
        fila++;
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
    
    //Crea cabecera de factura para panel modificar
    private JPanel crearCabeceraMod() {
        panelCabeceraMod = new JPanel(new BorderLayout(20, 10));
        panelCabeceraMod.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JPanel panelIzq = new JPanel();
        panelIzq.setLayout(new BoxLayout(panelIzq, BoxLayout.Y_AXIS));
        
        lblCodigoMod = new JLabel(CargadorProperties.obtenerComponentes("LBL_CODIGO") + " ");
        lblCodigoMod.setFont(new Font("Arial", Font.BOLD, 14));
        lblCodigoMod.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelIzq.add(lblCodigoMod);
        panelIzq.add(Box.createVerticalStrut(8));
        
        JLabel lblFechaTit = new JLabel(CargadorProperties.obtenerComponentes("LBL_FECHA_HORA"));
        lblFechaTit.setFont(new Font("Arial", Font.BOLD, 13));
        lblFechaTit.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelIzq.add(lblFechaTit);
        
        lblFechaMod = new JLabel("");
        lblFechaMod.setFont(new Font("Arial", Font.PLAIN, 13));
        lblFechaMod.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelIzq.add(lblFechaMod);
        
        JPanel panelEmpresa = new JPanel();
        panelEmpresa.setLayout(new BoxLayout(panelEmpresa, BoxLayout.Y_AXIS));
        
        JLabel lblEmpNom = new JLabel(CargadorProperties.obtenerComponentes("EMPRESA_NOMBRE"));
        lblEmpNom.setFont(new Font("Arial", Font.BOLD, 16));
        lblEmpNom.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelEmpresa.add(lblEmpNom);
        panelEmpresa.add(Box.createVerticalStrut(3));
        
        JLabel lblEmpEmail = new JLabel(CargadorProperties.obtenerComponentes("EMPRESA_EMAIL"));
        lblEmpEmail.setFont(new Font("Arial", Font.PLAIN, 14));
        lblEmpEmail.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelEmpresa.add(lblEmpEmail);
        panelEmpresa.add(Box.createVerticalStrut(3));
        
        JLabel lblEmpTel = new JLabel(CargadorProperties.obtenerComponentes("EMPRESA_TELEFONO"));
        lblEmpTel.setFont(new Font("Arial", Font.PLAIN, 14));
        lblEmpTel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelEmpresa.add(lblEmpTel);
        
        JPanel panelCliente = new JPanel();
        panelCliente.setLayout(new BoxLayout(panelCliente, BoxLayout.Y_AXIS));
        
        JLabel lblTitCliente = new JLabel(CargadorProperties.obtenerComponentes("LBL_INFO_CLIENTE"));
        lblTitCliente.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitCliente.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelCliente.add(lblTitCliente);
        panelCliente.add(Box.createVerticalStrut(5));
        
        JPanel pNom = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        JLabel lblNomTit = new JLabel(CargadorProperties.obtenerComponentes("LBL_NOMBRE"));
        lblNomTit.setFont(new Font("Arial", Font.BOLD, 12));
        pNom.add(lblNomTit);
        lblNombreClienteMod = new JLabel("");
        lblNombreClienteMod.setFont(new Font("Arial", Font.PLAIN, 12));
        pNom.add(lblNombreClienteMod);
        panelCliente.add(pNom);
        
        JPanel pCed = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        JLabel lblCedTit = new JLabel(CargadorProperties.obtenerComponentes("FC_UI_019"));
        lblCedTit.setFont(new Font("Arial", Font.BOLD, 12));
        pCed.add(lblCedTit);
        lblCedulaClienteMod = new JLabel("");
        lblCedulaClienteMod.setFont(new Font("Arial", Font.PLAIN, 12));
        pCed.add(lblCedulaClienteMod);
        panelCliente.add(pCed);
        
        JPanel pEmail = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        JLabel lblEmailTit = new JLabel(CargadorProperties.obtenerComponentes("LBL_EMAIL"));
        lblEmailTit.setFont(new Font("Arial", Font.BOLD, 12));
        pEmail.add(lblEmailTit);
        lblEmailClienteMod = new JLabel("");
        lblEmailClienteMod.setFont(new Font("Arial", Font.PLAIN, 12));
        pEmail.add(lblEmailClienteMod);
        panelCliente.add(pEmail);
        
        JPanel pTel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        JLabel lblTelTit = new JLabel(CargadorProperties.obtenerComponentes("LBL_TELEFONO"));
        lblTelTit.setFont(new Font("Arial", Font.BOLD, 12));
        pTel.add(lblTelTit);
        lblTelefonoClienteMod = new JLabel("");
        lblTelefonoClienteMod.setFont(new Font("Arial", Font.PLAIN, 12));
        pTel.add(lblTelefonoClienteMod);
        panelCliente.add(pTel);
        
        panelCabeceraMod.add(panelIzq, BorderLayout.WEST);
        panelCabeceraMod.add(panelEmpresa, BorderLayout.CENTER);
        panelCabeceraMod.add(panelCliente, BorderLayout.EAST);
        
        return panelCabeceraMod;
    }
    
    //Crea panel para modificar facturas existentes
    private JPanel crearPanelModificar() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 0, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        txtCedulaMod = new JTextField();
        txtCedulaMod.setPreferredSize(new Dimension(200, 25));
        
        cmbProductoMod = new JComboBox<>();
        cmbProductoMod.setPreferredSize(new Dimension(400, 25));
        
        txtCantidadMod = new JTextField();
        txtCantidadMod.setPreferredSize(new Dimension(80, 25));
        lblErrorCantidadMod = crearLabelError();
        
        txtSubtotalMod = new JTextField(CargadorProperties.obtenerComponentes("TOTAL_INICIAL"));
        txtSubtotalMod.setEditable(false);
        txtSubtotalMod.setPreferredSize(new Dimension(120, 25));
        
        txtIVAMod = new JTextField(CargadorProperties.obtenerComponentes("TOTAL_INICIAL"));
        txtIVAMod.setEditable(false);
        txtIVAMod.setPreferredSize(new Dimension(120, 25));
        
        txtTotalMod = new JTextField(CargadorProperties.obtenerComponentes("TOTAL_INICIAL"));
        txtTotalMod.setEditable(false);
        txtTotalMod.setFont(new Font("Arial", Font.BOLD, 12));
        txtTotalMod.setPreferredSize(new Dimension(120, 25));
        
        int fila = 0;
        
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.gridwidth = 8;
        gbc.anchor = GridBagConstraints.CENTER;
        JLabel lblTitulo = new JLabel(CargadorProperties.obtenerComponentes("TITULO_MODIFICAR_FACTURA"));
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 32));
        panel.add(lblTitulo, gbc);
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        
        fila++;
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.insets = new Insets(20, 10, 5, 10);
        JLabel lblCed = new JLabel(CargadorProperties.obtenerComponentes("LBL_CEDULA"));
        lblCed.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(lblCed, gbc);
        
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 5, 10);  
        panel.add(txtCedulaMod, gbc);
        
        gbc.gridx = 3;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(20, 5, 5, 10);
        JLabel lblLupa = new JLabel(CargadorProperties.obtenerComponentes("BTN_LUPA"));
        lblLupa.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        lblLupa.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lblLupa.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                buscarFacturasCliente(txtCedulaMod, modeloFacturasMod);
            }
        });
        panel.add(lblLupa, gbc);
        gbc.gridwidth = 1;
        gbc.insets = new Insets(5, 10, 0, 10);
        
        fila++;
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.gridwidth = 8;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 0.3;
        gbc.insets = new Insets(10, 10, 10, 10);
        
        String[] colsFacturas = CargadorProperties.obtenerComponentes("TABLA_FACTURAS_MOD_COLS").split(",");
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
        
        fila++;
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.gridwidth = 8;
        
        JPanel cabecera = crearCabeceraMod();
        cabecera.setVisible(false);

        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.add(cabecera);

        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;

        panel.add(wrapper, gbc);
        gbc.gridwidth = 1;

        fila++;
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.gridwidth = 8;
        gbc.anchor = GridBagConstraints.WEST; 
        JLabel lblTitProd = new JLabel(CargadorProperties.obtenerComponentes("TITULO_PRODUCTOS_FACTURA"));
        lblTitProd.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(lblTitProd, gbc);
        gbc.gridwidth = 1;
        
        fila++;
        gbc.gridx = 0;
        gbc.gridy = fila;
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
        btnAgregar.addActionListener(e -> agregarProductoMod());
        panel.add(btnAgregar, gbc);
        
        fila++;
        gbc.gridx = 6;
        gbc.gridy = fila;
        gbc.insets = new Insets(0, 10, 0, 10);
        panel.add(lblErrorCantidadMod, gbc);
        gbc.insets = new Insets(5, 10, 0, 10);

        fila++;
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.gridwidth = 8;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 0.7;
        gbc.insets = new Insets(10, 10, 10, 10);
        
        String[] colsProductos = CargadorProperties.obtenerComponentes("TABLA_PRODUCTOS_COLS").split(",");
        modeloProductosMod = new DefaultTableModel(colsProductos, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2;
            }
            
            @Override
            public Class<?> getColumnClass(int column) {
                if (column == 2) return Integer.class;
                return String.class;
            }
        };
        
        tablaProductosMod = new JTable(modeloProductosMod);
        
        modeloProductosMod.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if (e.getType() == TableModelEvent.UPDATE && e.getColumn() == 2) {
                    validarYActualizarCantidad(modeloProductosMod, txtSubtotalMod, txtIVAMod, txtTotalMod, e.getFirstRow());
                }
            }
        });
        
        JScrollPane scrollProductos = new JScrollPane(tablaProductosMod);
        scrollProductos.setPreferredSize(new Dimension(900, 200));
        panel.add(scrollProductos, gbc);
        
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(5, 10, 0, 10);
        
        fila++;
        gbc.gridx = 7;
        gbc.gridy = fila;
        gbc.anchor = GridBagConstraints.EAST;
        JButton btnQuitar = new JButton(CargadorProperties.obtenerComponentes("FC_UI_028"));
        btnQuitar.setPreferredSize(new Dimension(90, 25));
        btnQuitar.addActionListener(e -> quitarProductoMod());
        panel.add(btnQuitar, gbc);
        
        fila++;
        gbc.gridx = 6;
        gbc.gridy = fila;
        JLabel lblSub = new JLabel(CargadorProperties.obtenerComponentes("LBL_SUBTOTAL"));
        lblSub.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(lblSub, gbc);
        
        gbc.gridx = 7;
        panel.add(txtSubtotalMod, gbc);
        
        fila++;
        gbc.gridx = 6;
        gbc.gridy = fila;
        JLabel lblIva = new JLabel(CargadorProperties.obtenerComponentes("LBL_IVA"));
        lblIva.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(lblIva, gbc);
        
        gbc.gridx = 7;
        panel.add(txtIVAMod, gbc);
        
        fila++;
        gbc.gridx = 6;
        gbc.gridy = fila;
        JLabel lblTot = new JLabel(CargadorProperties.obtenerComponentes("LBL_TOTAL"));
        lblTot.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(lblTot, gbc);
        
        gbc.gridx = 7;
        panel.add(txtTotalMod, gbc);
        
        fila++;
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
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 0, 10);
        gbc.anchor = GridBagConstraints.WEST;

        txtCedulaElim = new JTextField();
        txtCedulaElim.setPreferredSize(new Dimension(200, 25));

        JLabel lblErrorCedulaElim = crearLabelError();

        // Configurar validación en tiempo real
        txtCedulaElim.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { validarCedulaElim(lblErrorCedulaElim); }
            public void removeUpdate(DocumentEvent e) { validarCedulaElim(lblErrorCedulaElim); }
            public void changedUpdate(DocumentEvent e) { validarCedulaElim(lblErrorCedulaElim); }
        });

        int fila = 0;

        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.gridwidth = 4;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 10, 15, 10);
        JLabel lblTitulo = new JLabel("Eliminar Factura");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 32));
        panel.add(lblTitulo, gbc);
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 10, 0, 10);

        fila++;
        gbc.gridx = 0;
        gbc.gridy = fila;
        JLabel lblCed = new JLabel(CargadorProperties.obtenerComponentes("LBL_CEDULA"));
        lblCed.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(lblCed, gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        panel.add(txtCedulaElim, gbc);

        
        //AQUI
        gbc.gridx = 3;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(20, 5, 5, 10);
        JLabel lblLupa = new JLabel(CargadorProperties.obtenerComponentes("BTN_LUPA"));
        lblLupa.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        lblLupa.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lblLupa.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                buscarFacturasClienteEliminar();
            }
        });
        panel.add(lblLupa, gbc);
                
        fila++;
        gbc.gridx = 1;
        gbc.gridy = fila;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 10, 5, 10);
        panel.add(lblErrorCedulaElim, gbc);
        gbc.gridwidth = 1;
        gbc.insets = new Insets(5, 10, 0, 10);

        fila++;
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.gridwidth = 4;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(10, 10, 10, 10);

        String[] cols = CargadorProperties.obtenerComponentes("TABLA_FACTURAS_COLS").split(",");
        modeloFacturasElim = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tablaFacturasElim = new JTable(modeloFacturasElim);
        tablaFacturasElim.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                btnEliminar.setEnabled(tablaFacturasElim.getSelectedRow() != -1);
            }
        });
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
        btnEliminar = new JButton(CargadorProperties.obtenerComponentes("BTN_ELIMINAR"));
        btnEliminar.setPreferredSize(new Dimension(100, 28));
        btnEliminar.setEnabled(false);
        btnEliminar.addActionListener(e -> eliminarFactura());
        panel.add(btnEliminar, gbc);

        return panel;
    }

    //Valida cédula en panel eliminar
    private void validarCedulaElim(JLabel lblError) {
        String error = ValidacionesFactura.validarCedRucCliente(txtCedulaElim.getText());
        mostrarError(lblError, error);
    }
    
   //Crea panel para consultar facturas
    private JPanel crearPanelConsultar() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel panelTipo = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        String[] opcionesTipo = CargadorProperties.obtenerComponentes("CMB_TIPO_CONSULTA").split(",");
        comboTipoConsulta = new JComboBox<>(opcionesTipo);
        comboTipoConsulta.setPreferredSize(new Dimension(250, 30));
        comboTipoConsulta.addActionListener(e -> cambiarTipoConsulta());
        panelTipo.add(comboTipoConsulta);
        panel.add(panelTipo, BorderLayout.NORTH);

        JPanel panelCentral = new JPanel(new BorderLayout(5, 5));

        panelBusqueda = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        txtCedulaConsulta = new JTextField();
        txtCedulaConsulta.setPreferredSize(new Dimension(250, 25));

        panelBusqueda.add(new JLabel(CargadorProperties.obtenerComponentes("LBL_CEDULA")));
        panelBusqueda.add(txtCedulaConsulta);

        JLabel lblBuscarConsulta = new JLabel(CargadorProperties.obtenerComponentes("BTN_LUPA"));
        lblBuscarConsulta.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        lblBuscarConsulta.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lblBuscarConsulta.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                consultaPorCliente();
            }
        });
        panelBusqueda.add(lblBuscarConsulta);

        panelBusqueda.setVisible(false);
        panelCentral.add(panelBusqueda, BorderLayout.NORTH);


        panelCentral.add(panelBusqueda, BorderLayout.NORTH);

        String[] columnas = CargadorProperties.obtenerComponentes("TABLA_FACTURAS_COLS").split(",");
        modeloTablaResultados = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tablaResultados = new JTable(modeloTablaResultados);
        tablaResultados.setRowHeight(25);
        JScrollPane scrollTabla = new JScrollPane(tablaResultados);

        panelCentral.add(scrollTabla, BorderLayout.CENTER);
        panel.add(panelCentral, BorderLayout.CENTER);

        return panel;
    }

    //**METODOS AUXILIARES UI**
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
    
    //**VALIDACIONES EN TIEMPO REAL**
    //Configura validaciones en tiempo real para panel crear
    private void configurarValidacionesCrear() {
        txtCedulaCrear.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { validarCedulaCrear(); }
            public void removeUpdate(DocumentEvent e) { validarCedulaCrear(); }
            public void changedUpdate(DocumentEvent e) { validarCedulaCrear(); }
        });
        
        txtCantidadCrear.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { validarCantidadCrear(); }
            public void removeUpdate(DocumentEvent e) { validarCantidadCrear(); }
            public void changedUpdate(DocumentEvent e) { validarCantidadCrear(); }
        });
    }
    
    //Valida cédula del cliente en tiempo real
    private void validarCedulaCrear() {
        String error = ValidacionesFactura.validarCedRucCliente(txtCedulaCrear.getText());
        mostrarError(lblErrorCedulaCrear, error);
    }
    
    //Valida cantidad de producto en tiempo real
    private void validarCantidadCrear() {
        String texto = txtCantidadCrear.getText();
        if (texto.isEmpty()) {
            mostrarError(lblErrorCantidadCrear, null);
            return;
        }
        String error = ValidacionesFactura.validarCantidad(texto);
        mostrarError(lblErrorCantidadCrear, error);
    }
    
    //Configura validaciones en tiempo real para panel modificar
    private void configurarValidacionesMod() {
        txtCantidadMod.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { validarCantidadMod(); }
            public void removeUpdate(DocumentEvent e) { validarCantidadMod(); }
            public void changedUpdate(DocumentEvent e) { validarCantidadMod(); }
        });
    }

    //Valida cantidad de producto en modificar en tiempo real
    private void validarCantidadMod() {
        String texto = txtCantidadMod.getText();
        if (texto.isEmpty()) {
            mostrarError(lblErrorCantidadMod, null);
            return;
        }
        String error = ValidacionesFactura.validarCantidad(texto);
        mostrarError(lblErrorCantidadMod, error);
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
    
    //Limpia datos del cliente mostrados
    private void limpiarDatosCliente() {
        lblNombreClienteCrear.setText("");
        lblCedulaClienteCrear.setText("");
        lblEmailClienteCrear.setText("");
        lblTelefonoClienteCrear.setText("");
    }
    
    //Agrega producto a la tabla de productos
    private void agregarProducto() {
        if (clienteSeleccionado == null) {
            JOptionPane.showMessageDialog(this, 
                CargadorProperties.obtenerMessages("FC_A_012"), 
                CargadorProperties.obtenerMessages("FC_C_005"), 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        ItemProducto item = (ItemProducto) cmbProductoCrear.getSelectedItem();
        if (item == null) {
            JOptionPane.showMessageDialog(this, 
                CargadorProperties.obtenerMessages("FC_A_013"), 
                CargadorProperties.obtenerMessages("FC_C_005"), 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        String cantidadStr = txtCantidadCrear.getText().trim();
        if (cantidadStr.isEmpty()) {
            mostrarError(lblErrorCantidadCrear, CargadorProperties.obtenerMessages("FC_A_014"));
            return;
        }

        int cantidad;
        try {
            cantidad = Integer.parseInt(cantidadStr);
            if (cantidad <= 0) throw new NumberFormatException();
            mostrarError(lblErrorCantidadCrear, null);
        } catch (NumberFormatException e) {
            mostrarError(lblErrorCantidadCrear, CargadorProperties.obtenerMessages("FC_A_020"));
            return;
        }

        Producto prod = item.producto;

        // Verificar si el producto YA existe en la tabla
        boolean productoExiste = false;
        for (int i = 0; i < productosFactura.size(); i++) {
            ProxFac pxfExistente = productosFactura.get(i);

            if (pxfExistente.getCodigoProd().equals(prod.getCodigo())) {
                productoExiste = true;

                int cantidadAnterior = pxfExistente.getCantidad();
                int cantidadNueva = cantidadAnterior + cantidad;

                pxfExistente.setCantidad(cantidadNueva);
                pxfExistente.calcularSubtotal();

                modeloProductosCrear.setValueAt(cantidadNueva, i, 2);
                modeloProductosCrear.setValueAt(String.format("%.2f", pxfExistente.getSubtotalProducto()), i, 4);

                JOptionPane.showMessageDialog(this, 
                    CargadorProperties.obtenerMessages("FC_A_021") + cantidadAnterior + " + " + cantidad + " = " + cantidadNueva, 
                    CargadorProperties.obtenerComponentes("TITULO_PRODUCTO_DUPLICADO"), 
                    JOptionPane.INFORMATION_MESSAGE);

                break;
            }
        }

        if (!productoExiste) {
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
        }

        calcularTotales(txtSubtotalCrear, txtIVACrear, txtTotalCrear);
        txtCantidadCrear.setText("");
        mostrarError(lblErrorCantidadCrear, null);
    }

    //Quita producto seleccionado de la tabla
    private void quitarProducto(JTable tabla, DefaultTableModel modelo) {
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
        calcularTotales(txtSubtotalCrear, txtIVACrear, txtTotalCrear);
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
                CargadorProperties.obtenerMessages("FC_I_001") + CargadorProperties.obtenerMessages("FC_I_004") + " " + codigoGenerado,
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
    //Busca facturas asociadas a un cliente
    private void buscarFacturasCliente(JTextField txtCed, DefaultTableModel modelo) {
        String cedula = txtCed.getText().trim();
        
        if (cedula.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                CargadorProperties.obtenerMessages("FC_A_019"), 
                CargadorProperties.obtenerMessages("FC_C_005"), 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Cliente cli = new Cliente();
        cli.setCedRuc(cedula);
        
        Factura fac = new Factura();
        ArrayList<Factura> lista = fac.consultarPorParametro(cli);
        
        modelo.setRowCount(0);
        
        if (lista == null || lista.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                CargadorProperties.obtenerMessages("FC_A_002"), 
                CargadorProperties.obtenerMessages("FC_C_006"), 
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        Cliente clienteBuscado = cli.verificarDP(cedula);
        String nombreCliente = (clienteBuscado != null) ? clienteBuscado.getNombre() : "N/A";
        
        for (Factura f : lista) {
            modelo.addRow(new Object[]{
                f.getCodigo(),
                f.getFechaHora() != null ? f.getFechaHora().format(FMT_FECHA) : "",
                nombreCliente
            });
        }
    }
    
    //Carga factura seleccionada para modificar
    private void seleccionarFacturaModificar() {
        int fila = tablaFacturasMod.getSelectedRow();
        if (fila == -1) return;

        String codigo = (String) modeloFacturasMod.getValueAt(fila, 0);

        Factura facParam = new Factura();
        facParam.setCodigo(codigo);

        Factura fac = new Factura();
        facturaSeleccionada = fac.consultarPorParametro(facParam);

        if (facturaSeleccionada != null) {
            Cliente cli = new Cliente();
            Cliente clienteCompleto = cli.verificarDP(txtCedulaMod.getText().trim());

            panelCabeceraMod.setVisible(true);  // ← MUESTRA LA CABECERA

            lblCodigoMod.setText(CargadorProperties.obtenerComponentes("LBL_CODIGO") + " " + facturaSeleccionada.getCodigo());
            lblFechaMod.setText(facturaSeleccionada.getFechaHora().format(FMT_FECHA));

            if (clienteCompleto != null) {
                lblNombreClienteMod.setText(clienteCompleto.getNombre());
                lblCedulaClienteMod.setText(clienteCompleto.getCedRuc());
                lblEmailClienteMod.setText(clienteCompleto.getEmail() != null ? clienteCompleto.getEmail() : "");
                lblTelefonoClienteMod.setText(clienteCompleto.getTelefono() != null ? clienteCompleto.getTelefono() : "");
            }

            cargarProductosFactura(facturaSeleccionada.getCodigo());  // ← CARGA PRODUCTOS

            txtSubtotalMod.setText(String.format("%.2f", facturaSeleccionada.getSubtotal()));
            txtIVAMod.setText(String.format("%.2f", facturaSeleccionada.getIva()));
            txtTotalMod.setText(String.format("%.2f", facturaSeleccionada.getTotal()));

            cmbProductoMod.setEnabled(true);
            txtCantidadMod.setEnabled(true);
            btnGuardarMod.setEnabled(true);
            cargarProductosActivos(cmbProductoMod);
        }
    }
    
    //Busca facturas para ELIMINAR (con todas las columnas)
    private void buscarFacturasClienteEliminar() {
        String cedula = txtCedulaElim.getText().trim();

        if (cedula.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                CargadorProperties.obtenerMessages("FC_A_019"), 
                CargadorProperties.obtenerMessages("FC_C_005"), 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        Cliente cli = new Cliente();
        cli.setCedRuc(cedula);

        Factura fac = new Factura();
        ArrayList<Factura> lista = fac.consultarPorParametro(cli);

        modeloFacturasElim.setRowCount(0);

        if (lista == null || lista.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                CargadorProperties.obtenerMessages("FC_A_002"), 
                CargadorProperties.obtenerMessages("FC_C_006"), 
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        Cliente clienteBuscado = cli.verificarDP(cedula);
        String nombreCliente = (clienteBuscado != null) ? clienteBuscado.getNombre() : "N/A";

        for (Factura f : lista) {
            modeloFacturasElim.addRow(new Object[]{
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
    
    //Carga productos de una factura específica
    private void cargarProductosFactura(String codigoFactura) {
        modeloProductosMod.setRowCount(0);
        productosFactura.clear();
        
        ProxFac pxfConsulta = new ProxFac();
        pxfConsulta.setCodigoFac(codigoFactura);
        
        ArrayList<ProxFac> productos = pxfConsulta.consultarPorFactura(pxfConsulta);
        
        if (productos != null && !productos.isEmpty()) {
            for (ProxFac pxf : productos) {
                Producto prod = new Producto();
                Producto prodCompleto = prod.verificarPorCodigoDP(pxf.getCodigoProd());
                
                String descripcion = (prodCompleto != null) ? prodCompleto.getDescripcion() : "N/A";
                
                modeloProductosMod.addRow(new Object[]{
                    pxf.getCodigoProd(),
                    descripcion,
                    pxf.getCantidad(),
                    String.format("%.2f", pxf.getPrecioVenta()),
                    String.format("%.2f", pxf.getSubtotalProducto())
                });
                
                productosFactura.add(pxf);
            }
        }
    }
    
    //Agrega producto a factura en modificación
    private void agregarProductoMod() {
        ItemProducto item = (ItemProducto) cmbProductoMod.getSelectedItem();
        if (item == null) {
            JOptionPane.showMessageDialog(this, 
                CargadorProperties.obtenerMessages("FC_A_013"), 
                CargadorProperties.obtenerMessages("FC_C_005"), 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        String cantidadStr = txtCantidadMod.getText().trim();
        if (cantidadStr.isEmpty()) {
            mostrarError(lblErrorCantidadMod, CargadorProperties.obtenerMessages("FC_A_014"));
            return;
        }

        int cantidad;
        try {
            cantidad = Integer.parseInt(cantidadStr);
            if (cantidad <= 0) throw new NumberFormatException();
            mostrarError(lblErrorCantidadMod, null);
        } catch (NumberFormatException e) {
            mostrarError(lblErrorCantidadMod, CargadorProperties.obtenerMessages("FC_A_020"));
            return;
        }

        Producto prod = item.producto;

        // Verificar si el producto YA existe en la tabla
        boolean productoExiste = false;
        for (int i = 0; i < productosFactura.size(); i++) {
            ProxFac pxfExistente = productosFactura.get(i);

            if (pxfExistente.getCodigoProd().equals(prod.getCodigo())) {
                productoExiste = true;

                int cantidadAnterior = pxfExistente.getCantidad();
                int cantidadNueva = cantidadAnterior + cantidad;

                pxfExistente.setCantidad(cantidadNueva);
                pxfExistente.calcularSubtotal();

                modeloProductosMod.setValueAt(cantidadNueva, i, 2);
                modeloProductosMod.setValueAt(String.format("%.2f", pxfExistente.getSubtotalProducto()), i, 4);

                JOptionPane.showMessageDialog(this, 
                    CargadorProperties.obtenerMessages("FC_A_021") + cantidadAnterior + " + " + cantidad + " = " + cantidadNueva, 
                    CargadorProperties.obtenerComponentes("TITULO_PRODUCTO_DUPLICADO"), 
                    JOptionPane.INFORMATION_MESSAGE);

                break;
            }
        }

        if (!productoExiste) {
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
        }

        calcularTotales(txtSubtotalMod, txtIVAMod, txtTotalMod);
        txtCantidadMod.setText("");
        mostrarError(lblErrorCantidadMod, null);
    }

    //Quita producto de factura en modificación
    private void quitarProductoMod() {
        int fila = tablaProductosMod.getSelectedRow();
        
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, 
                CargadorProperties.obtenerMessages("FC_A_013"), 
                CargadorProperties.obtenerMessages("FC_C_005"), 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        productosFactura.remove(fila);
        modeloProductosMod.removeRow(fila);
        calcularTotales(txtSubtotalMod, txtIVAMod, txtTotalMod);
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
            return;
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
        
        int conf = JOptionPane.showConfirmDialog(this, 
            CargadorProperties.obtenerMessages("FC_C_001") + codigo + "?", 
            CargadorProperties.obtenerMessages("FC_C_002"), 
            JOptionPane.YES_NO_OPTION);
        if (conf != JOptionPane.YES_OPTION) return;
        
        Factura fac = new Factura();
        fac.setCodigo(codigo);
        
        if (fac.eliminar()) {
            JOptionPane.showMessageDialog(this, 
                CargadorProperties.obtenerMessages("FC_I_003"), 
                CargadorProperties.obtenerMessages("FC_C_003"), 
                JOptionPane.INFORMATION_MESSAGE);
            buscarFacturasCliente(txtCedulaElim, modeloFacturasElim);
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

        if (tipo.equals(opciones[1])) {
            panelBusqueda.setVisible(false);
            consultarTodos();
        } else if (tipo.equals(opciones[2])) {
            panelBusqueda.setVisible(true);
            modeloTablaResultados.setRowCount(0);
        } else {
            panelBusqueda.setVisible(false);
            modeloTablaResultados.setRowCount(0);
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
    }
 
    
    //Consulta facturas de un cliente específico (APR y ANU)
    private void consultaPorCliente() {
        String cedula = txtCedulaConsulta.getText().trim();

        if (cedula.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                CargadorProperties.obtenerMessages("FC_A_019"), 
                CargadorProperties.obtenerMessages("FC_C_005"), 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        Cliente cli = new Cliente();
        cli.setCedRuc(cedula);

        Factura fac = new Factura();
        ArrayList<Factura> lista = fac.consultarTodasPorParametro(cli);  // ← CAMBIO AQUÍ

        modeloTablaResultados.setRowCount(0);

        if (lista == null || lista.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                CargadorProperties.obtenerMessages("FC_A_002"), 
                CargadorProperties.obtenerMessages("FC_C_006"), 
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        Cliente clienteBuscado = cli.verificarDP(cedula);
        String nombreCliente = (clienteBuscado != null) ? clienteBuscado.getNombre() : "N/A";

        for (Factura f : lista) {
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
    }
    
    //**METODOS AUXILIARES DATOS**
    //Carga productos activos en el combobox
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
        
        generarYMostrarCodigo();
    }
    
    //Limpia panel modificar y resetea variables
    private void limpiarPanelModificar() {
        txtCedulaMod.setText("");
        modeloFacturasMod.setRowCount(0);
        modeloProductosMod.setRowCount(0);
        
        if (panelCabeceraMod != null) {
            panelCabeceraMod.setVisible(false);
        }
        
        if (lblCodigoMod != null) lblCodigoMod.setText(CargadorProperties.obtenerComponentes("LBL_CODIGO") + " ");
        if (lblFechaMod != null) lblFechaMod.setText(CargadorProperties.obtenerComponentes("LBL_FECHA_HORA") + " ");
        if (lblNombreClienteMod != null) lblNombreClienteMod.setText("");
        if (lblCedulaClienteMod != null) lblCedulaClienteMod.setText("");
        if (lblEmailClienteMod != null) lblEmailClienteMod.setText("");
        if (lblTelefonoClienteMod != null) lblTelefonoClienteMod.setText("");
        
        txtSubtotalMod.setText(CargadorProperties.obtenerComponentes("TOTAL_INICIAL"));
        txtIVAMod.setText(CargadorProperties.obtenerComponentes("TOTAL_INICIAL"));
        txtTotalMod.setText(CargadorProperties.obtenerComponentes("TOTAL_INICIAL"));
        
        cmbProductoMod.setEnabled(false);
        txtCantidadMod.setEnabled(false);
        btnGuardarMod.setEnabled(false);
        
        facturaSeleccionada = null;
        productosFactura.clear();
        
        if (lblErrorCantidadMod != null) lblErrorCantidadMod.setText(" ");
    }
    
    //Limpia panel consultar
    private void limpiarPanelConsultar() {
        comboTipoConsulta.setSelectedIndex(0);
        txtCedulaConsulta.setText("");
        modeloTablaResultados.setRowCount(0);
        panelBusqueda.setVisible(false);
    }

    //Limpia panel eliminar
    private void limpiarPanelEliminar() {
        txtCedulaElim.setText("");
        modeloFacturasElim.setRowCount(0);
        btnEliminar.setEnabled(false);
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
