package GUI;

import DP.Producto;
import util.CargadorProperties;
import util.ValidacionesProducto;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.Timer;

public class VentanaProducto extends JFrame {
    
    private CardLayout cardLayout;
    private JPanel panelContenedor;
    
    private JComboBox<String> comboOpciones;
    private JLabel lblTituloSuperior;
    
    private static final String PANEL_VACIO = "VACIO";
    private static final String PANEL_INGRESAR = "INGRESAR";
    private static final String PANEL_MODIFICAR = "MODIFICAR";
    private static final String PANEL_ELIMINAR = "ELIMINAR";
    private static final String PANEL_CONSULTAR = "CONSULTAR";
    
    private JComboBox<ItemCombo> comboCategoriaIng, comboUmCompraIng, comboUmVentaIng;
    private JTextField txtCodigoIng, txtDescripcionIng, txtPrecioCompraIng, txtPrecioVentaIng;
    private JLabel lblImagenPreviewIng, lblErrorCategoriaIng, lblErrorCodigoIng;
    private JLabel lblErrorDescripcionIng, lblErrorUmCompraIng, lblErrorPrecioCompraIng;
    private JLabel lblErrorUmVentaIng, lblErrorPrecioVentaIng, lblErrorImagenIng;
    private JButton btnSeleccionarImagenIng;
    private String rutaImagenSeleccionadaIng = "";
    
    private JTextField txtCodigoMod, txtDescripcionMod, txtPrecioCompraMod, txtPrecioVentaMod;
    private JComboBox<ItemCombo> comboUmCompraMod, comboUmVentaMod;
    private JLabel lblImagenPreviewMod;
    private JLabel lblErrorCodigoMod, lblErrorDescripcionMod, lblErrorUmCompraMod;
    private JLabel lblErrorPrecioCompraMod, lblErrorUmVentaMod, lblErrorPrecioVentaMod;
    private JButton btnGuardarMod, btnSeleccionarImagenMod;
    private String rutaImagenSeleccionadaMod = "";
    private String codigoProductoActual = "";
    
    private JTextField txtCodigoElim, txtDescripcionElim, txtCategoriaElim;
    private JTextField txtUmCompraElim, txtPrecioCompraElim, txtUmVentaElim;
    private JTextField txtPrecioVentaElim, txtSaldoIniElim, txtSaldoFinElim;
    private JLabel lblImagenPreviewElim;
    private JButton btnEliminar;
    
    private JComboBox<String> comboTipoConsulta;
    private JComboBox<ItemCombo> comboParametroBusqueda;
    private JTextField txtBusqueda;
    private JTable tablaResultados;
    private DefaultTableModel modeloTabla;
    private JScrollPane scrollTabla;
    private JPanel panelBusqueda;
    private Timer timerBusqueda;
    
    public VentanaProducto() {
        configurarVentana();
        inicializarComponentes();
        configurarLayout();
    }
    
    private void configurarVentana() {
        setTitle("Gestión de Productos");
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
        lblTituloSuperior = new JLabel("Gestión de Productos");
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
        
        JLabel lblTitulo = new JLabel("Gestión de Productos");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 48));
        lblTitulo.setForeground(Color.DARK_GRAY);
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(lblTitulo, gbc);
        
        return panel;
    }
    
    private JPanel crearPanelIngresar() {
        //Wrapper centrado
        JPanel panelWrapper = new JPanel(new GridBagLayout());
        GridBagConstraints gw = new GridBagConstraints();
        gw.gridx = 0;
        gw.gridy = 0;
        gw.insets = new Insets(0, 0, 0, 0);
        gw.anchor = GridBagConstraints.NORTH;

        //Panel con 2 columnas
        JPanel panelPrincipal = new JPanel(new GridBagLayout());
        GridBagConstraints gp = new GridBagConstraints();
        gp.insets = new Insets(0, 0, 0, 0);
        gp.anchor = GridBagConstraints.NORTHWEST;

        //Panel Izquierdo para los campos
        JPanel panelCampos = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 0, 10);
        gbc.anchor = GridBagConstraints.WEST;

        //Componentes
        comboCategoriaIng = new JComboBox<>();
        comboCategoriaIng.setPreferredSize(new Dimension(350, 28));

        txtCodigoIng = new JTextField();
        txtCodigoIng.setPreferredSize(new Dimension(350, 28));
        txtCodigoIng.setEnabled(false);

        txtDescripcionIng = new JTextField();
        txtDescripcionIng.setPreferredSize(new Dimension(350, 28));

        comboUmCompraIng = new JComboBox<>();
        comboUmCompraIng.setPreferredSize(new Dimension(350, 28));

        txtPrecioCompraIng = new JTextField();
        txtPrecioCompraIng.setPreferredSize(new Dimension(350, 28));

        comboUmVentaIng = new JComboBox<>();
        comboUmVentaIng.setPreferredSize(new Dimension(350, 28));

        txtPrecioVentaIng = new JTextField();
        txtPrecioVentaIng.setPreferredSize(new Dimension(350, 28));

        lblImagenPreviewIng = new JLabel("Sin imagen", SwingConstants.CENTER);
        lblImagenPreviewIng.setPreferredSize(new Dimension(200, 234));
        lblImagenPreviewIng.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        btnSeleccionarImagenIng = new JButton("Seleccionar Imagen");
        btnSeleccionarImagenIng.addActionListener(e -> seleccionarImagenIngresar());

        //Labels error
        lblErrorCategoriaIng = crearLabelError();
        lblErrorCategoriaIng.setPreferredSize(new Dimension(350, 20));

        lblErrorCodigoIng = crearLabelError();
        lblErrorCodigoIng.setPreferredSize(new Dimension(350, 20));

        lblErrorDescripcionIng = crearLabelError();
        lblErrorDescripcionIng.setPreferredSize(new Dimension(350, 20));

        lblErrorUmCompraIng = crearLabelError();
        lblErrorUmCompraIng.setPreferredSize(new Dimension(350, 20));

        lblErrorPrecioCompraIng = crearLabelError();
        lblErrorPrecioCompraIng.setPreferredSize(new Dimension(365, 20));

        lblErrorUmVentaIng = crearLabelError();
        lblErrorUmVentaIng.setPreferredSize(new Dimension(350, 20));

        lblErrorPrecioVentaIng = crearLabelError();
        lblErrorPrecioVentaIng.setPreferredSize(new Dimension(365, 20));

        lblErrorImagenIng = crearLabelError();
        lblErrorImagenIng.setPreferredSize(new Dimension(200, 20));

        //Metodos de carga y validación
        cargarCategorias(comboCategoriaIng);
        cargarUnidadesMedida(comboUmCompraIng);
        cargarUnidadesMedida(comboUmVentaIng);

        configurarValidacionesIngresar();
        comboCategoriaIng.addActionListener(e -> generarCodigoAutomatico());

        //Columna Izquierda
        int fila = 0;

        //Categoría
        gbc.gridx = 0; gbc.gridy = fila;
        panelCampos.add(new JLabel("Categoría:"), gbc);

        gbc.gridx = 1;
        panelCampos.add(comboCategoriaIng, gbc);

        fila++;
        gbc.gridx = 1; gbc.gridy = fila;
        gbc.insets = new Insets(0, 10, 0, 10);
        panelCampos.add(lblErrorCategoriaIng, gbc);
        gbc.insets = new Insets(5, 10, 0, 10);

        //Codigo
        fila++;
        gbc.gridx = 0; gbc.gridy = fila;
        panelCampos.add(new JLabel("Código:"), gbc);

        gbc.gridx = 1;
        panelCampos.add(txtCodigoIng, gbc);

        fila++;
        gbc.gridx = 1; gbc.gridy = fila;
        gbc.insets = new Insets(0, 10, 0, 10);
        panelCampos.add(lblErrorCodigoIng, gbc);
        gbc.insets = new Insets(5, 10, 0, 10);

        //Descripcion
        fila++;
        gbc.gridx = 0; gbc.gridy = fila;
        panelCampos.add(new JLabel("Descripción:"), gbc);

        gbc.gridx = 1;
        panelCampos.add(txtDescripcionIng, gbc);

        fila++;
        gbc.gridx = 1; gbc.gridy = fila;
        gbc.insets = new Insets(0, 10, 0, 10);
        panelCampos.add(lblErrorDescripcionIng, gbc);
        gbc.insets = new Insets(5, 10, 0, 10);

        //Unidad compra
        fila++;
        gbc.gridx = 0; gbc.gridy = fila;
        panelCampos.add(new JLabel("Unidad de Compra:"), gbc);

        gbc.gridx = 1;
        panelCampos.add(comboUmCompraIng, gbc);

        fila++;
        gbc.gridx = 1; gbc.gridy = fila;
        gbc.insets = new Insets(0, 10, 0, 10);
        panelCampos.add(lblErrorUmCompraIng, gbc);
        gbc.insets = new Insets(5, 10, 0, 10);

        //Precio compra
        fila++;
        gbc.gridx = 0; gbc.gridy = fila;
        panelCampos.add(new JLabel("Precio de Compra:"), gbc);

        gbc.gridx = 1;
        panelCampos.add(txtPrecioCompraIng, gbc);

        fila++;
        gbc.gridx = 1; gbc.gridy = fila;
        gbc.insets = new Insets(0, 10, 0, 10);
        panelCampos.add(lblErrorPrecioCompraIng, gbc);
        gbc.insets = new Insets(5, 10, 0, 10);

        //Unidad venta
        fila++;
        gbc.gridx = 0; gbc.gridy = fila;
        panelCampos.add(new JLabel("Unidad de Venta:"), gbc);

        gbc.gridx = 1;
        panelCampos.add(comboUmVentaIng, gbc);

        fila++;
        gbc.gridx = 1; gbc.gridy = fila;
        gbc.insets = new Insets(0, 10, 0, 10);
        panelCampos.add(lblErrorUmVentaIng, gbc);
        gbc.insets = new Insets(5, 10, 0, 10);

        //Precio venta
        fila++;
        gbc.gridx = 0; gbc.gridy = fila;
        panelCampos.add(new JLabel("Precio de Venta:"), gbc);

        gbc.gridx = 1;
        panelCampos.add(txtPrecioVentaIng, gbc);

        fila++;
        gbc.gridx = 1; gbc.gridy = fila;
        gbc.insets = new Insets(0, 10, 0, 10);
        panelCampos.add(lblErrorPrecioVentaIng, gbc);
        gbc.insets = new Insets(5, 10, 0, 10);

        //Columna Derecha
        JPanel panelImagen = new JPanel(new GridBagLayout());
        GridBagConstraints gi = new GridBagConstraints();
        gi.insets = new Insets(5, 10, 0, 10);
        gi.anchor = GridBagConstraints.WEST;

        //Imagen
        gi.gridx = 0; gi.gridy = 0;
        panelImagen.add(new JLabel("Imagen:"), gi);

        gi.gridx = 1; gi.gridy = 0;
        panelImagen.add(btnSeleccionarImagenIng, gi);

        gi.gridx = 1; gi.gridy = 1;
        gi.insets = new Insets(0, 10, 0, 10);
        panelImagen.add(lblErrorImagenIng, gi);
        gi.insets = new Insets(10, 10, 0, 10);

        gi.gridx = 1; gi.gridy = 2;
        panelImagen.add(lblImagenPreviewIng, gi);

        //Guardar
        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.addActionListener(e -> guardarProducto());

        gi.gridx = 1; gi.gridy = 3;
        gi.anchor = GridBagConstraints.EAST;
        gi.insets = new Insets(30, 10, 0, 10);
        panelImagen.add(btnGuardar, gi);

        //Combinar columnas
        gp.gridx = 0; gp.gridy = 0;
        gp.weightx = 1;
        panelPrincipal.add(panelCampos, gp);

        gp.gridx = 1; gp.gridy = 0;
        gp.weightx = 0;
        gp.insets = new Insets(0, 40, 0, 0); 
        panelPrincipal.add(panelImagen, gp);

        //Centrar con el wrapper
        panelWrapper.add(panelPrincipal, gw);

        return panelWrapper;
    }

    private JPanel crearPanelModificar() {
        //Wrapper centrado
        JPanel panelWrapper = new JPanel(new GridBagLayout());
        GridBagConstraints gw = new GridBagConstraints();
        gw.gridx = 0;
        gw.gridy = 0;
        gw.insets = new Insets(0, 0, 0, 0);
        gw.anchor = GridBagConstraints.NORTH;

        //Panel con 2 columnas
        JPanel panelPrincipal = new JPanel(new GridBagLayout());
        GridBagConstraints gp = new GridBagConstraints();
        gp.insets = new Insets(0, 0, 0, 0);
        gp.anchor = GridBagConstraints.NORTHWEST;

        //Panel Izquierdo para los campos
        JPanel panelCampos = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 0, 10);
        gbc.anchor = GridBagConstraints.WEST;

        //Componentes
        txtCodigoMod = new JTextField();
        txtCodigoMod.setPreferredSize(new Dimension(350, 28));

        txtDescripcionMod = new JTextField();
        txtDescripcionMod.setPreferredSize(new Dimension(350, 28));
        txtDescripcionMod.setEnabled(false);

        comboUmCompraMod = new JComboBox<>();
        comboUmCompraMod.setPreferredSize(new Dimension(350, 28));
        comboUmCompraMod.setEnabled(false);

        txtPrecioCompraMod = new JTextField();
        txtPrecioCompraMod.setPreferredSize(new Dimension(350, 28));
        txtPrecioCompraMod.setEnabled(false);

        comboUmVentaMod = new JComboBox<>();
        comboUmVentaMod.setPreferredSize(new Dimension(350, 28));
        comboUmVentaMod.setEnabled(false);

        txtPrecioVentaMod = new JTextField();
        txtPrecioVentaMod.setPreferredSize(new Dimension(350, 28));
        txtPrecioVentaMod.setEnabled(false);

        lblImagenPreviewMod = new JLabel("Sin imagen", SwingConstants.CENTER);
        lblImagenPreviewMod.setPreferredSize(new Dimension(200, 245));
        lblImagenPreviewMod.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        btnSeleccionarImagenMod = new JButton("Cambiar Imagen");
        btnSeleccionarImagenMod.setEnabled(false);
        btnSeleccionarImagenMod.addActionListener(e -> seleccionarImagenModificar());

        //Labels error
        lblErrorCodigoMod = crearLabelError();
        lblErrorCodigoMod.setPreferredSize(new Dimension(350, 20));

        lblErrorDescripcionMod = crearLabelError();
        lblErrorDescripcionMod.setPreferredSize(new Dimension(350, 20));

        lblErrorUmCompraMod = crearLabelError();
        lblErrorUmCompraMod.setPreferredSize(new Dimension(350, 20));

        lblErrorPrecioCompraMod = crearLabelError();
        lblErrorPrecioCompraMod.setPreferredSize(new Dimension(365, 20));

        lblErrorUmVentaMod = crearLabelError();
        lblErrorUmVentaMod.setPreferredSize(new Dimension(350, 20));

        lblErrorPrecioVentaMod = crearLabelError();
        lblErrorPrecioVentaMod.setPreferredSize(new Dimension(365, 20));

        //Metodos de carga y validación
        cargarUnidadesMedida(comboUmCompraMod);
        cargarUnidadesMedida(comboUmVentaMod);
        configurarValidacionesModificar();

        //Columna Izquierda
        int fila = 0;

        //Código con botón Buscar
        gbc.gridx = 0; gbc.gridy = fila;
        panelCampos.add(new JLabel("Código:"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 3;
        JPanel panelCodigo = new JPanel(new BorderLayout(5, 0));
        panelCodigo.add(txtCodigoMod, BorderLayout.CENTER);

        JLabel lblLupa = new JLabel("🔍");
        lblLupa.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        lblLupa.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lblLupa.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
        lblLupa.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                buscarProductoModificar();
            }
        });

        panelCodigo.add(lblLupa, BorderLayout.EAST);
        panelCampos.add(panelCodigo, gbc);
        gbc.gridwidth = 1;

        fila++;
        gbc.gridx = 1; gbc.gridy = fila;
        gbc.insets = new Insets(0, 10, 0, 10);
        panelCampos.add(lblErrorCodigoMod, gbc);
        gbc.insets = new Insets(5, 10, 0, 10);

        //Descripción
        fila++;
        gbc.gridx = 0; gbc.gridy = fila;
        panelCampos.add(new JLabel("Descripción:"), gbc);

        gbc.gridx = 1;
        panelCampos.add(txtDescripcionMod, gbc);

        fila++;
        gbc.gridx = 1; gbc.gridy = fila;
        gbc.insets = new Insets(0, 10, 0, 10);
        panelCampos.add(lblErrorDescripcionMod, gbc);
        gbc.insets = new Insets(5, 10, 0, 10);

        //Unidad compra
        fila++;
        gbc.gridx = 0; gbc.gridy = fila;
        panelCampos.add(new JLabel("Unidad de Compra:"), gbc);

        gbc.gridx = 1;
        panelCampos.add(comboUmCompraMod, gbc);

        fila++;
        gbc.gridx = 1; gbc.gridy = fila;
        gbc.insets = new Insets(0, 10, 0, 10);
        panelCampos.add(lblErrorUmCompraMod, gbc);
        gbc.insets = new Insets(5, 10, 0, 10);

        //Precio compra
        fila++;
        gbc.gridx = 0; gbc.gridy = fila;
        panelCampos.add(new JLabel("Precio de Compra:"), gbc);

        gbc.gridx = 1;
        panelCampos.add(txtPrecioCompraMod, gbc);

        fila++;
        gbc.gridx = 1; gbc.gridy = fila;
        gbc.insets = new Insets(0, 10, 0, 10);
        panelCampos.add(lblErrorPrecioCompraMod, gbc);
        gbc.insets = new Insets(5, 10, 0, 10);

        //Unidad venta
        fila++;
        gbc.gridx = 0; gbc.gridy = fila;
        panelCampos.add(new JLabel("Unidad de Venta:"), gbc);

        gbc.gridx = 1;
        panelCampos.add(comboUmVentaMod, gbc);

        fila++;
        gbc.gridx = 1; gbc.gridy = fila;
        gbc.insets = new Insets(0, 10, 0, 10);
        panelCampos.add(lblErrorUmVentaMod, gbc);
        gbc.insets = new Insets(5, 10, 0, 10);

        //Precio venta
        fila++;
        gbc.gridx = 0; gbc.gridy = fila;
        panelCampos.add(new JLabel("Precio de Venta:"), gbc);

        gbc.gridx = 1;
        panelCampos.add(txtPrecioVentaMod, gbc);

        fila++;
        gbc.gridx = 1; gbc.gridy = fila;
        gbc.insets = new Insets(0, 10, 0, 10);
        panelCampos.add(lblErrorPrecioVentaMod, gbc);
        gbc.insets = new Insets(5, 10, 0, 10);

        //Columna Derecha
        JPanel panelImagen = new JPanel(new GridBagLayout());
        GridBagConstraints gi = new GridBagConstraints();
        gi.insets = new Insets(5, 10, 0, 10);
        gi.anchor = GridBagConstraints.WEST;

        //Imagen
        gi.gridx = 0; gi.gridy = 0;
        panelImagen.add(new JLabel("Imagen:"), gi);

        gi.gridx = 1; gi.gridy = 0;
        panelImagen.add(btnSeleccionarImagenMod, gi);

        gi.gridx = 1; gi.gridy = 1;
        gi.insets = new Insets(10, 10, 0, 10);
        panelImagen.add(lblImagenPreviewMod, gi);

        //Guardar
        btnGuardarMod = new JButton("Guardar");
        btnGuardarMod.setEnabled(false);
        btnGuardarMod.addActionListener(e -> modificarProducto());

        gi.gridx = 1; gi.gridy = 2;
        gi.anchor = GridBagConstraints.EAST;
        gi.insets = new Insets(30, 10, 0, 10);
        panelImagen.add(btnGuardarMod, gi);

        //Combinar columnas
        gp.gridx = 0; gp.gridy = 0;
        gp.weightx = 1;
        panelPrincipal.add(panelCampos, gp);

        gp.gridx = 1; gp.gridy = 0;
        gp.weightx = 0;
        gp.insets = new Insets(0, 40, 0, 0);
        panelPrincipal.add(panelImagen, gp);

        //Centrar con el wrapper
        panelWrapper.add(panelPrincipal, gw);

        return panelWrapper;
    }
    
    private JPanel crearPanelEliminar() {
        //Wrapper centrado
        JPanel panelWrapper = new JPanel(new GridBagLayout());
        GridBagConstraints gw = new GridBagConstraints();
        gw.gridx = 0;
        gw.gridy = 0;
        gw.insets = new Insets(0, 0, 0, 0);
        gw.anchor = GridBagConstraints.NORTH;

        //Panel con 2 columnas
        JPanel panelPrincipal = new JPanel(new GridBagLayout());
        GridBagConstraints gp = new GridBagConstraints();
        gp.insets = new Insets(0, 0, 0, 0);
        gp.anchor = GridBagConstraints.NORTHWEST;

        //Panel Izquierdo para los campos
        JPanel panelCampos = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        //Componentes
        txtCodigoElim = new JTextField();
        txtCodigoElim.setPreferredSize(new Dimension(350, 28));

        txtDescripcionElim = new JTextField();
        txtDescripcionElim.setPreferredSize(new Dimension(350, 28));
        txtDescripcionElim.setEnabled(false);

        txtCategoriaElim = new JTextField();
        txtCategoriaElim.setPreferredSize(new Dimension(350, 28));
        txtCategoriaElim.setEnabled(false);

        txtUmCompraElim = new JTextField();
        txtUmCompraElim.setPreferredSize(new Dimension(350, 28));
        txtUmCompraElim.setEnabled(false);

        txtPrecioCompraElim = new JTextField();
        txtPrecioCompraElim.setPreferredSize(new Dimension(350, 28));
        txtPrecioCompraElim.setEnabled(false);

        txtUmVentaElim = new JTextField();
        txtUmVentaElim.setPreferredSize(new Dimension(350, 28));
        txtUmVentaElim.setEnabled(false);

        txtPrecioVentaElim = new JTextField();
        txtPrecioVentaElim.setPreferredSize(new Dimension(350, 28));
        txtPrecioVentaElim.setEnabled(false);

        txtSaldoIniElim = new JTextField();
        txtSaldoIniElim.setPreferredSize(new Dimension(350, 28));
        txtSaldoIniElim.setEnabled(false);

        txtSaldoFinElim = new JTextField();
        txtSaldoFinElim.setPreferredSize(new Dimension(350, 28));
        txtSaldoFinElim.setEnabled(false);

        lblImagenPreviewElim = new JLabel("Sin imagen", SwingConstants.CENTER);
        lblImagenPreviewElim.setPreferredSize(new Dimension(230, 290));
        lblImagenPreviewElim.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        // Columna Izquierda
        int fila = 0;

        // Código con lupa
        gbc.gridx = 0; gbc.gridy = fila;
        gbc.gridwidth = 1;
        panelCampos.add(new JLabel("Código:"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 3;
        JPanel panelCodigo = new JPanel(new BorderLayout(5, 0));
        panelCodigo.add(txtCodigoElim, BorderLayout.CENTER);

        JLabel lblLupa = new JLabel("🔍");
        lblLupa.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        lblLupa.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lblLupa.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
        lblLupa.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                buscarProductoEliminar();
            }
        });

        panelCodigo.add(lblLupa, BorderLayout.EAST);
        panelCampos.add(panelCodigo, gbc);
        gbc.gridwidth = 1;

        // Descripción
        fila++;
        gbc.gridx = 0; gbc.gridy = fila;
        panelCampos.add(new JLabel("Descripción:"), gbc);

        gbc.gridx = 1;
        panelCampos.add(txtDescripcionElim, gbc);

        // Categoría
        fila++;
        gbc.gridx = 0; gbc.gridy = fila;
        panelCampos.add(new JLabel("Categoría:"), gbc);

        gbc.gridx = 1;
        panelCampos.add(txtCategoriaElim, gbc);

        // Unidad compra
        fila++;
        gbc.gridx = 0; gbc.gridy = fila;
        panelCampos.add(new JLabel("Unidad de Compra:"), gbc);

        gbc.gridx = 1;
        panelCampos.add(txtUmCompraElim, gbc);

        // Precio compra
        fila++;
        gbc.gridx = 0; gbc.gridy = fila;
        panelCampos.add(new JLabel("Precio de Compra:"), gbc);

        gbc.gridx = 1;
        panelCampos.add(txtPrecioCompraElim, gbc);

        // Unidad venta
        fila++;
        gbc.gridx = 0; gbc.gridy = fila;
        panelCampos.add(new JLabel("Unidad de Venta:"), gbc);

        gbc.gridx = 1;
        panelCampos.add(txtUmVentaElim, gbc);

        // Precio venta
        fila++;
        gbc.gridx = 0; gbc.gridy = fila;
        panelCampos.add(new JLabel("Precio de Venta:"), gbc);

        gbc.gridx = 1;
        panelCampos.add(txtPrecioVentaElim, gbc);

        // Saldo Inicial
        fila++;
        gbc.gridx = 0; gbc.gridy = fila;
        panelCampos.add(new JLabel("Saldo Inicial:"), gbc);

        gbc.gridx = 1;
        panelCampos.add(txtSaldoIniElim, gbc);

        // Saldo Final
        fila++;
        gbc.gridx = 0; gbc.gridy = fila;
        panelCampos.add(new JLabel("Saldo Final:"), gbc);

        gbc.gridx = 1;
        panelCampos.add(txtSaldoFinElim, gbc);

        // Columna Derecha
        JPanel panelImagen = new JPanel(new GridBagLayout());
        GridBagConstraints gi = new GridBagConstraints();
        gi.insets = new Insets(5, 10, 0, 10);
        gi.anchor = GridBagConstraints.WEST;

        // Imagen
        gi.gridx = 0; gi.gridy = 0;
        panelImagen.add(new JLabel("Imagen:"), gi);

        gi.gridx = 0; gi.gridy = 1;
        gi.insets = new Insets(10, 10, 0, 10);
        panelImagen.add(lblImagenPreviewElim, gi);

        // Eliminar
        btnEliminar = new JButton("Eliminar");
        btnEliminar.setEnabled(false);
        btnEliminar.addActionListener(e -> eliminarProducto());

        gi.gridx = 0; gi.gridy = 2;
        gi.anchor = GridBagConstraints.EAST;
        gi.insets = new Insets(30, 10, 0, 10);
        panelImagen.add(btnEliminar, gi);

        // Combinar columnas
        gp.gridx = 0; gp.gridy = 0;
        gp.weightx = 1;
        panelPrincipal.add(panelCampos, gp);

        gp.gridx = 1; gp.gridy = 0;
        gp.weightx = 0;
        gp.insets = new Insets(0, 40, 0, 0);
        panelPrincipal.add(panelImagen, gp);

        // Centrar con el wrapper
        panelWrapper.add(panelPrincipal, gw);

        return panelWrapper;
    }
    
    private JPanel crearPanelConsultar() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel superior con combo tipo de consulta CENTRADO
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

        // Panel central (contiene búsqueda y tabla)
        JPanel panelCentral = new JPanel(new BorderLayout(5, 5));

        // Panel de búsqueda CENTRADO
        panelBusqueda = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        txtBusqueda = new JTextField(20);
        txtBusqueda.setPreferredSize(new Dimension(250, 25));
        txtBusqueda.setToolTipText("Ingrese texto para buscar...");

        comboParametroBusqueda = new JComboBox<>();
        comboParametroBusqueda.setPreferredSize(new Dimension(200, 25));

        panelBusqueda.add(new JLabel("Buscar:"));
        panelBusqueda.add(txtBusqueda);
        panelBusqueda.add(comboParametroBusqueda);
        panelBusqueda.setVisible(false);

        panelCentral.add(panelBusqueda, BorderLayout.NORTH);

        // Tabla de resultados
        String[] columnas = {
            "Código", 
            "Descripción", 
            "Categoría", 
            "Unidad Compra", 
            "Precio Compra", 
            "Unidad Venta", 
            "Precio Venta", 
            "Saldo Inicial",
            "Saldo Final"
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

        tablaResultados.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    int fila = tablaResultados.getSelectedRow();
                    if (fila != -1) {
                        String codigo = (String) tablaResultados.getValueAt(fila, 0);
                        mostrarPopupImagen(codigo);
                    }
                }
            }
        });

        scrollTabla = new JScrollPane(tablaResultados);
        scrollTabla.setVisible(false);

        panelCentral.add(scrollTabla, BorderLayout.CENTER);

        panel.add(panelCentral, BorderLayout.CENTER);

        configurarBusquedaTiempoReal();
        cargarParametrosBusqueda();

        return panel;
    }
    
    private void cambiarPanel() {
        String seleccion = (String) comboOpciones.getSelectedItem();
        
        switch (seleccion) {
            case "Ingresar":
                cardLayout.show(panelContenedor, PANEL_INGRESAR);
                lblTituloSuperior.setText("Ingresar Producto");
                limpiarCamposIngresar();
                break;
            case "Modificar":
                cardLayout.show(panelContenedor, PANEL_MODIFICAR);
                lblTituloSuperior.setText("Modificar Producto");
                limpiarCamposModificar();
                break;
            case "Eliminar":
                cardLayout.show(panelContenedor, PANEL_ELIMINAR);
                lblTituloSuperior.setText("Eliminar Producto");
                limpiarCamposEliminar();
                break;
            case "Consultar":
                cardLayout.show(panelContenedor, PANEL_CONSULTAR);
                lblTituloSuperior.setText("Consultar Productos");
                comboTipoConsulta.setSelectedIndex(0);
                break;
            default:
                cardLayout.show(panelContenedor, PANEL_VACIO);
                lblTituloSuperior.setText("Gestión de Productos");
                break;
        }
    }
     
    //Vuelve al panel vacío
    private void volverAlMenu() {
        this.dispose();             
        new MenuPrincipal().setVisible(true);
    }
    
    private JLabel crearLabelError() {
        JLabel label = new JLabel(" ");
        label.setForeground(Color.RED);
        label.setFont(new Font("Arial", Font.PLAIN, 11));
        return label;
    }
    
    private void cargarCategorias(JComboBox<ItemCombo> combo) {
        Producto pro = new Producto();
        ArrayList<ItemCombo> categorias = pro.obtenerCategoriasDP();
        
        combo.removeAllItems();
        combo.addItem(new ItemCombo("", "Seleccione..."));
        
        for (ItemCombo cat : categorias) {
            combo.addItem(cat);
        }
    }
    
    private void cargarUnidadesMedida(JComboBox<ItemCombo> combo) {
        Producto pro = new Producto();
        ArrayList<ItemCombo> unidades = pro.obtenerUnidadesMedidaDP();
        
        combo.removeAllItems();
        combo.addItem(new ItemCombo("", "Seleccione..."));
        
        for (ItemCombo um : unidades) {
            combo.addItem(um);
        }
    }
    
    private void cargarParametrosBusqueda() {
        comboParametroBusqueda.removeAllItems();
        comboParametroBusqueda.addItem(new ItemCombo("codigo", "Código"));
        comboParametroBusqueda.addItem(new ItemCombo("descripcion", "Descripción"));
        comboParametroBusqueda.addItem(new ItemCombo("categoria", "Categoría"));
        comboParametroBusqueda.addItem(new ItemCombo("um_compra", "UM Compra"));
        comboParametroBusqueda.addItem(new ItemCombo("um_venta", "UM Venta"));
    }
    
    private void generarCodigoAutomatico() {
        ItemCombo categoriaSeleccionada = (ItemCombo) comboCategoriaIng.getSelectedItem();
        
        if (categoriaSeleccionada != null && !categoriaSeleccionada.getId().isEmpty()) {
            Producto pro = new Producto();
            pro.setIdCategoria(categoriaSeleccionada.getId());
            String codigoGenerado = pro.generarCodigoDP();
            txtCodigoIng.setText(codigoGenerado);
        } else {
            txtCodigoIng.setText("");
        }
    }
    
    private void configurarValidacionesIngresar() {
        comboCategoriaIng.addActionListener(e -> validarCategoriaIngresar());
        
        txtDescripcionIng.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { validarDescripcionIngresar(); }
            public void removeUpdate(DocumentEvent e) { validarDescripcionIngresar(); }
            public void changedUpdate(DocumentEvent e) { validarDescripcionIngresar(); }
        });
        
        comboUmCompraIng.addActionListener(e -> validarUmCompraIngresar());
        
        txtPrecioCompraIng.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { validarPrecioCompraIngresar(); }
            public void removeUpdate(DocumentEvent e) { validarPrecioCompraIngresar(); }
            public void changedUpdate(DocumentEvent e) { validarPrecioCompraIngresar(); }
        });
        
        comboUmVentaIng.addActionListener(e -> validarUmVentaIngresar());
        
        txtPrecioVentaIng.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { validarPrecioVentaIngresar(); }
            public void removeUpdate(DocumentEvent e) { validarPrecioVentaIngresar(); }
            public void changedUpdate(DocumentEvent e) { validarPrecioVentaIngresar(); }
        });
    }
    
    private void validarCategoriaIngresar() {
        ItemCombo categoria = (ItemCombo) comboCategoriaIng.getSelectedItem();
        String error = ValidacionesProducto.validarCategoria(categoria != null ? categoria.getId() : "");
        
        if (error != null) {
            lblErrorCategoriaIng.setText(error);
        } else {
            lblErrorCategoriaIng.setText(" ");
        }
    }
    
    private void validarDescripcionIngresar() {
        String error = ValidacionesProducto.validarDescripcion(txtDescripcionIng.getText(), false);
        
        if (error != null) {
            lblErrorDescripcionIng.setText(error);
        } else {
            lblErrorDescripcionIng.setText(" ");
        }
    }
    
    private void validarUmCompraIngresar() {
        ItemCombo um = (ItemCombo) comboUmCompraIng.getSelectedItem();
        String error = ValidacionesProducto.validarUnidadMedida(um != null ? um.getId() : "", "compra");
        
        if (error != null) {
            lblErrorUmCompraIng.setText(error);
        } else {
            lblErrorUmCompraIng.setText(" ");
        }
    }
    
    private void validarPrecioCompraIngresar() {
        String error = ValidacionesProducto.validarPrecioCompra(txtPrecioCompraIng.getText());
        
        if (error != null) {
            lblErrorPrecioCompraIng.setText(error);
        } else {
            lblErrorPrecioCompraIng.setText(" ");
        }
    }
    
    private void validarUmVentaIngresar() {
        ItemCombo um = (ItemCombo) comboUmVentaIng.getSelectedItem();
        String error = ValidacionesProducto.validarUnidadMedida(um != null ? um.getId() : "", "venta");
        
        if (error != null) {
            lblErrorUmVentaIng.setText(error);
        } else {
            lblErrorUmVentaIng.setText(" ");
        }
    }
    
    private void validarPrecioVentaIngresar() {
        String error = ValidacionesProducto.validarPrecioVenta(
            txtPrecioVentaIng.getText(), 
            txtPrecioCompraIng.getText()
        );
        
        if (error != null) {
            lblErrorPrecioVentaIng.setText(error);
        } else {
            lblErrorPrecioVentaIng.setText(" ");
        }
    }
    
    private void configurarValidacionesModificar() {
        txtDescripcionMod.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { validarDescripcionModificar(); }
            public void removeUpdate(DocumentEvent e) { validarDescripcionModificar(); }
            public void changedUpdate(DocumentEvent e) { validarDescripcionModificar(); }
        });
        
        comboUmCompraMod.addActionListener(e -> validarUmCompraModificar());
        
        txtPrecioCompraMod.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { validarPrecioCompraModificar(); }
            public void removeUpdate(DocumentEvent e) { validarPrecioCompraModificar(); }
            public void changedUpdate(DocumentEvent e) { validarPrecioCompraModificar(); }
        });
        
        comboUmVentaMod.addActionListener(e -> validarUmVentaModificar());
        
        txtPrecioVentaMod.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { validarPrecioVentaModificar(); }
            public void removeUpdate(DocumentEvent e) { validarPrecioVentaModificar(); }
            public void changedUpdate(DocumentEvent e) { validarPrecioVentaModificar(); }
        });
    }
    
    private void validarDescripcionModificar() {
        String error = ValidacionesProducto.validarDescripcion(txtDescripcionMod.getText(), true);
        
        if (error != null) {
            lblErrorDescripcionMod.setText(error);
        } else {
            lblErrorDescripcionMod.setText(" ");
        }
    }
    
    private void validarUmCompraModificar() {
        ItemCombo um = (ItemCombo) comboUmCompraMod.getSelectedItem();
        String error = ValidacionesProducto.validarUnidadMedida(um != null ? um.getId() : "", "compra");
        
        if (error != null) {
            lblErrorUmCompraMod.setText(error);
        } else {
            lblErrorUmCompraMod.setText(" ");
        }
    }
    
    private void validarPrecioCompraModificar() {
        String error = ValidacionesProducto.validarPrecioCompra(txtPrecioCompraMod.getText());
        
        if (error != null) {
            lblErrorPrecioCompraMod.setText(error);
        } else {
            lblErrorPrecioCompraMod.setText(" ");
        }
    }
    
    private void validarUmVentaModificar() {
        ItemCombo um = (ItemCombo) comboUmVentaMod.getSelectedItem();
        String error = ValidacionesProducto.validarUnidadMedida(um != null ? um.getId() : "", "venta");
        
        if (error != null) {
            lblErrorUmVentaMod.setText(error);
        } else {
            lblErrorUmVentaMod.setText(" ");
        }
    }
    
    private void validarPrecioVentaModificar() {
        String error = ValidacionesProducto.validarPrecioVenta(
            txtPrecioVentaMod.getText(), 
            txtPrecioCompraMod.getText()
        );
        
        if (error != null) {
            lblErrorPrecioVentaMod.setText(error);
        } else {
            lblErrorPrecioVentaMod.setText(" ");
        }
    }
    
    private void seleccionarImagenIngresar() {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
            "Imágenes (JPG, PNG, GIF)", "jpg", "jpeg", "png", "gif"
        );
        fileChooser.setFileFilter(filter);
        
        int resultado = fileChooser.showOpenDialog(this);
        
        if (resultado == JFileChooser.APPROVE_OPTION) {
            File archivoSeleccionado = fileChooser.getSelectedFile();
            rutaImagenSeleccionadaIng = archivoSeleccionado.getAbsolutePath();
            
            mostrarPreviewImagen(rutaImagenSeleccionadaIng, lblImagenPreviewIng);
            lblErrorImagenIng.setText(" ");
        }
    }
    
    private void seleccionarImagenModificar() {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
            "Imágenes (JPG, PNG, GIF)", "jpg", "jpeg", "png", "gif"
        );
        fileChooser.setFileFilter(filter);
        
        int resultado = fileChooser.showOpenDialog(this);
        
        if (resultado == JFileChooser.APPROVE_OPTION) {
            File archivoSeleccionado = fileChooser.getSelectedFile();
            rutaImagenSeleccionadaMod = archivoSeleccionado.getAbsolutePath();
            
            mostrarPreviewImagen(rutaImagenSeleccionadaMod, lblImagenPreviewMod);
        }
    }
    
    private void mostrarPreviewImagen(String rutaImagen, JLabel lblPreview) {
        try {
            BufferedImage img = ImageIO.read(new File(rutaImagen));
            Image imagenEscalada = img.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
            lblPreview.setIcon(new ImageIcon(imagenEscalada));
            lblPreview.setText("");
        } catch (IOException e) {
            lblPreview.setText("Error al cargar");
            e.printStackTrace();
        }
    }
    
    private void cargarImagenDesdeRuta(String rutaRelativa, JLabel lblPreview) {
        try {
            String base = CargadorProperties.obtenerConfigProducto("img.base");
            String rutaCompleta = base + "/" + rutaRelativa;

            File archivoImagen = new File(rutaCompleta);

            if (archivoImagen.exists()) {
                BufferedImage img = ImageIO.read(archivoImagen);

                int w = Integer.parseInt(CargadorProperties.obtenerConfigProducto("img.preview.w"));
                int h = Integer.parseInt(CargadorProperties.obtenerConfigProducto("img.preview.h"));

                Image imagenEscalada = img.getScaledInstance(w, h, Image.SCALE_SMOOTH);
                lblPreview.setIcon(new ImageIcon(imagenEscalada));
                lblPreview.setText("");
            } else {
                lblPreview.setIcon(null);
                lblPreview.setText("Sin imagen");
            }
        } catch (IOException e) {
            lblPreview.setIcon(null);
            lblPreview.setText("Error al cargar");
            e.printStackTrace();
        }
    }

    
    private String copiarImagenAProyecto(String rutaOrigen) {
        try {
            File archivoOrigen = new File(rutaOrigen);
            String nombreArchivo = archivoOrigen.getName();
            
            String rutaCarpetaDestino = CargadorProperties.obtenerConfigProducto("img.dir.productos.full");
            File carpetaDestino = new File(rutaCarpetaDestino);
            if (!carpetaDestino.exists()) {
                carpetaDestino.mkdirs();
            }
            
            File archivoDestino = new File(carpetaDestino, nombreArchivo);
            
            Files.copy(archivoOrigen.toPath(), archivoDestino.toPath(), StandardCopyOption.REPLACE_EXISTING);
            
            String dirRelativo = CargadorProperties.obtenerConfigProducto("img.dir.productos");
            return dirRelativo + "/" + nombreArchivo;
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Error al copiar la imagen",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
    
    private void guardarProducto() {
        ItemCombo categoria = (ItemCombo) comboCategoriaIng.getSelectedItem();
        ItemCombo umCompra = (ItemCombo) comboUmCompraIng.getSelectedItem();
        ItemCombo umVenta = (ItemCombo) comboUmVentaIng.getSelectedItem();
        
        String idCategoria = categoria != null ? categoria.getId() : "";
        String codigo = txtCodigoIng.getText().trim();
        String descripcion = txtDescripcionIng.getText().trim();
        String idUmCompra = umCompra != null ? umCompra.getId() : "";
        String precioCompra = txtPrecioCompraIng.getText().trim();
        String idUmVenta = umVenta != null ? umVenta.getId() : "";
        String precioVenta = txtPrecioVentaIng.getText().trim();
        
        if (rutaImagenSeleccionadaIng.isEmpty()) {
            lblErrorImagenIng.setText("Debe seleccionar una imagen");
            return;
        }
        
        if (!ValidacionesProducto.validarTodoInsertar(codigo, descripcion, idCategoria, 
                                                       idUmCompra, precioCompra, 
                                                       idUmVenta, precioVenta, 
                                                       rutaImagenSeleccionadaIng)) {
            JOptionPane.showMessageDialog(this,
                "Por favor corrija los errores antes de guardar",
                "Validación",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String rutaImagenGuardada = copiarImagenAProyecto(rutaImagenSeleccionadaIng);
        
        if (rutaImagenGuardada == null) {
            return;
        }
        
        Producto pro = new Producto();
        pro.setCodigo(codigo);
        pro.setDescripcion(descripcion);
        pro.setIdCategoria(idCategoria);
        pro.setUmCompra(idUmCompra);
        pro.setPrecioCompra(Double.parseDouble(precioCompra));
        pro.setUmVenta(idUmVenta);
        pro.setPrecioVenta(Double.parseDouble(precioVenta));
        pro.setImagen(rutaImagenGuardada);
        
        if (pro.grabarDP()) {
            JOptionPane.showMessageDialog(this,
                CargadorProperties.obtenerMessages("PD_I_001"),
                "Éxito",
                JOptionPane.INFORMATION_MESSAGE);
            limpiarCamposIngresar();
        } else {
            JOptionPane.showMessageDialog(this,
                CargadorProperties.obtenerMessages("PD_E_001"),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void buscarProductoModificar() {
        String codigo = txtCodigoMod.getText().trim();
        
        if (codigo.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Ingrese un código para buscar",
                "Advertencia",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Producto pro = new Producto();
        Producto encontrado = pro.verificarPorCodigoDP(codigo);
        
        if (encontrado != null) {
            codigoProductoActual = encontrado.getCodigo();
            
            txtDescripcionMod.setText(encontrado.getDescripcion());
            txtPrecioCompraMod.setText(String.valueOf(encontrado.getPrecioCompra()));
            txtPrecioVentaMod.setText(String.valueOf(encontrado.getPrecioVenta()));
            
            for (int i = 0; i < comboUmCompraMod.getItemCount(); i++) {
                ItemCombo item = comboUmCompraMod.getItemAt(i);
                if (item.getId().equals(encontrado.getUmCompra())) {
                    comboUmCompraMod.setSelectedIndex(i);
                    break;
                }
            }
            
            for (int i = 0; i < comboUmVentaMod.getItemCount(); i++) {
                ItemCombo item = comboUmVentaMod.getItemAt(i);
                if (item.getId().equals(encontrado.getUmVenta())) {
                    comboUmVentaMod.setSelectedIndex(i);
                    break;
                }
            }
            
            cargarImagenDesdeRuta(encontrado.getImagen(), lblImagenPreviewMod);
            rutaImagenSeleccionadaMod = "";
            
            txtCodigoMod.setEnabled(false); 
            
            txtDescripcionMod.setEnabled(true);
            comboUmCompraMod.setEnabled(true);
            txtPrecioCompraMod.setEnabled(true);
            comboUmVentaMod.setEnabled(true);
            txtPrecioVentaMod.setEnabled(true);
            btnSeleccionarImagenMod.setEnabled(true);
            btnGuardarMod.setEnabled(true);
        } else {
            JOptionPane.showMessageDialog(this,
                "Producto no encontrado",
                "Búsqueda",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void modificarProducto() {
        ItemCombo umCompra = (ItemCombo) comboUmCompraMod.getSelectedItem();
        ItemCombo umVenta = (ItemCombo) comboUmVentaMod.getSelectedItem();
        
        String descripcion = txtDescripcionMod.getText().trim();
        String idUmCompra = umCompra != null ? umCompra.getId() : "";
        String precioCompra = txtPrecioCompraMod.getText().trim();
        String idUmVenta = umVenta != null ? umVenta.getId() : "";
        String precioVenta = txtPrecioVentaMod.getText().trim();
        
        if (!ValidacionesProducto.validarTodoModificar(descripcion, idUmCompra, 
                                                        precioCompra, idUmVenta, 
                                                        precioVenta)) {
            JOptionPane.showMessageDialog(this,
                "Por favor corrija los errores antes de guardar",
                "Validación",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Producto pro = new Producto();
        pro.setCodigo(codigoProductoActual);
        pro.setDescripcion(descripcion);
        pro.setUmCompra(idUmCompra);
        pro.setPrecioCompra(Double.parseDouble(precioCompra));
        pro.setUmVenta(idUmVenta);
        pro.setPrecioVenta(Double.parseDouble(precioVenta));
        
        String rutaImagen;
        if (!rutaImagenSeleccionadaMod.isEmpty()) {
            rutaImagen = copiarImagenAProyecto(rutaImagenSeleccionadaMod);
            if (rutaImagen == null) {
                return;
            }
        } else {
            Producto actual = pro.verificarPorCodigoDP(codigoProductoActual);
            rutaImagen = actual.getImagen();
        }
        
        pro.setImagen(rutaImagen);
        
        if (pro.grabarDP()) {
            JOptionPane.showMessageDialog(this,
                CargadorProperties.obtenerMessages("PD_I_002"),
                "Éxito",
                JOptionPane.INFORMATION_MESSAGE);
            limpiarCamposModificar();
        } else {
            JOptionPane.showMessageDialog(this,
                CargadorProperties.obtenerMessages("PD_E_003"),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void buscarProductoEliminar() {
        String codigo = txtCodigoElim.getText().trim();
        
        if (codigo.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Ingrese un código para buscar",
                "Advertencia",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Producto pro = new Producto();
        Producto encontrado = pro.verificarPorCodigoDP(codigo);
        
        if (encontrado != null) {
            txtDescripcionElim.setText(encontrado.getDescripcion());
            txtCategoriaElim.setText(encontrado.getCategoria());
            txtUmCompraElim.setText(encontrado.getNombreUmCompra());
            txtPrecioCompraElim.setText(String.valueOf(encontrado.getPrecioCompra()));
            txtUmVentaElim.setText(encontrado.getNombreUmVenta());
            txtPrecioVentaElim.setText(String.valueOf(encontrado.getPrecioVenta()));
            txtSaldoIniElim.setText(String.valueOf(encontrado.getSaldoIni()));
            txtSaldoFinElim.setText(String.valueOf(encontrado.getSaldoFin()));
            
            cargarImagenDesdeRuta(encontrado.getImagen(), lblImagenPreviewElim);
            
            btnEliminar.setEnabled(true);
        } else {
            JOptionPane.showMessageDialog(this,
                "Producto no encontrado",
                "Búsqueda",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void eliminarProducto() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "¿Está seguro de eliminar el producto " + txtDescripcionElim.getText() + "?",
            "Confirmar eliminación",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            Producto pro = new Producto();
            pro.setCodigo(txtCodigoElim.getText().trim());
            
            if (pro.eliminarDP()) {
                JOptionPane.showMessageDialog(this,
                    CargadorProperties.obtenerMessages("PD_I_003"),
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
                limpiarCamposEliminar();
            } else {
                JOptionPane.showMessageDialog(this,
                    CargadorProperties.obtenerMessages("PD_E_004"),
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
        Producto pro = new Producto();
        ArrayList<Producto> productos = pro.consultarTodos();
        
        modeloTabla.setRowCount(0);
        
        for (Producto p : productos) {
            modeloTabla.addRow(new Object[]{
                p.getCodigo(),
                p.getDescripcion(),
                p.getCategoria(),
                p.getNombreUmCompra(),
                p.getPrecioCompra(),
                p.getNombreUmVenta(),
                p.getPrecioVenta(),
                p.getSaldoIni(),
                p.getSaldoFin()
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

        ItemCombo parametro = (ItemCombo) comboParametroBusqueda.getSelectedItem();
        if (parametro == null) return;

        String idParametro = parametro.getId();
        Producto pro = new Producto();
        ArrayList<Producto> resultados = new ArrayList<>();

        switch (idParametro) {
            case "codigo":
                resultados = pro.buscarPorCodigoDP(texto);
                break;
            case "descripcion":
                resultados = pro.buscarPorNombreDP(texto);
                break;
            case "categoria":
                resultados = pro.buscarPorNombreCategoriaDP(texto);
                break;
            case "um_compra":
                resultados = pro.buscarPorNombreUmCompraDP(texto);
                break;
            case "um_venta":
                resultados = pro.buscarPorNombreUmVentaDP(texto);
                break;
        }

        modeloTabla.setRowCount(0);

        for (Producto p : resultados) {
            modeloTabla.addRow(new Object[]{
                p.getCodigo(),
                p.getDescripcion(),
                p.getCategoria(),
                p.getNombreUmCompra(),
                p.getPrecioCompra(),
                p.getNombreUmVenta(),
                p.getPrecioVenta(),
                p.getSaldoIni(),
                p.getSaldoFin()
            });
        }
    }
        
    private void mostrarPopupImagen(String codigo) {
        Producto pro = new Producto();
        Producto encontrado = pro.verificarPorCodigoDP(codigo);
        
        if (encontrado != null && encontrado.getImagen() != null && !encontrado.getImagen().isEmpty()) {
            JDialog dialog = new JDialog(this, "Imagen del Producto " + codigo, true);
            dialog.setLayout(new BorderLayout(10, 10));
            
            try {
                String base = CargadorProperties.obtenerConfigProducto("img.base");
                String rutaCompleta = base + "/" + encontrado.getImagen();
                File archivoImagen = new File(rutaCompleta);
                
                if (archivoImagen.exists()) {
                    BufferedImage img = ImageIO.read(archivoImagen);
                    
                    int max = Integer.parseInt(CargadorProperties.obtenerConfigProducto("img.popup.max"));
                    int maxWidth = max;
                    int maxHeight = max;
                    
                    double scale = Math.min(
                        (double) maxWidth / img.getWidth(),
                        (double) maxHeight / img.getHeight()
                    );
                    
                    int newWidth = (int) (img.getWidth() * scale);
                    int newHeight = (int) (img.getHeight() * scale);
                    
                    Image imagenEscalada = img.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
                    
                    JLabel lblImagen = new JLabel(new ImageIcon(imagenEscalada));
                    lblImagen.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                    dialog.add(lblImagen, BorderLayout.CENTER);
                    
                    JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.CENTER));
                    JButton btnCerrar = new JButton("Cerrar");
                    btnCerrar.addActionListener(e -> dialog.dispose());
                    panelBoton.add(btnCerrar);
                    dialog.add(panelBoton, BorderLayout.SOUTH);
                    
                    dialog.pack();
                    dialog.setLocationRelativeTo(this);
                    dialog.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(this,
                        "Archivo de imagen no encontrado",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this,
                    "Error al cargar la imagen",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this,
                "Este producto no tiene imagen asociada",
                "Información",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void limpiarCamposIngresar() {
        comboCategoriaIng.setSelectedIndex(0);
        txtCodigoIng.setText("");
        txtDescripcionIng.setText("");
        comboUmCompraIng.setSelectedIndex(0);
        txtPrecioCompraIng.setText("");
        comboUmVentaIng.setSelectedIndex(0);
        txtPrecioVentaIng.setText("");
        lblImagenPreviewIng.setIcon(null);
        lblImagenPreviewIng.setText("Sin imagen");
        rutaImagenSeleccionadaIng = "";
        
        lblErrorCategoriaIng.setText(" ");
        lblErrorCodigoIng.setText(" ");
        lblErrorDescripcionIng.setText(" ");
        lblErrorUmCompraIng.setText(" ");
        lblErrorPrecioCompraIng.setText(" ");
        lblErrorUmVentaIng.setText(" ");
        lblErrorPrecioVentaIng.setText(" ");
        lblErrorImagenIng.setText(" ");
    }
    
    private void limpiarCamposModificar() {
        txtCodigoMod.setText("");
        txtDescripcionMod.setText("");
        comboUmCompraMod.setSelectedIndex(0);
        txtPrecioCompraMod.setText("");
        comboUmVentaMod.setSelectedIndex(0);
        txtPrecioVentaMod.setText("");
        lblImagenPreviewMod.setIcon(null);
        lblImagenPreviewMod.setText("Sin imagen");
        rutaImagenSeleccionadaMod = "";
        codigoProductoActual = "";
        
        txtCodigoMod.setEnabled(true);
        
        txtDescripcionMod.setEnabled(false);
        comboUmCompraMod.setEnabled(false);
        txtPrecioCompraMod.setEnabled(false);
        comboUmVentaMod.setEnabled(false);
        txtPrecioVentaMod.setEnabled(false);
        btnSeleccionarImagenMod.setEnabled(false);
        
        btnGuardarMod.setEnabled(false);
        lblErrorCodigoMod.setText(" ");
        lblErrorDescripcionMod.setText(" ");
        lblErrorUmCompraMod.setText(" ");
        lblErrorPrecioCompraMod.setText(" ");
        lblErrorUmVentaMod.setText(" ");
        lblErrorPrecioVentaMod.setText(" ");
    }
    
    private void limpiarCamposEliminar() {
        txtCodigoElim.setText("");
        txtDescripcionElim.setText("");
        txtCategoriaElim.setText("");
        txtUmCompraElim.setText("");
        txtPrecioCompraElim.setText("");
        txtUmVentaElim.setText("");
        txtPrecioVentaElim.setText("");
        txtSaldoIniElim.setText("");
        txtSaldoFinElim.setText("");
        lblImagenPreviewElim.setIcon(null);
        lblImagenPreviewElim.setText("Sin imagen");
        
        btnEliminar.setEnabled(false);
    }
}
