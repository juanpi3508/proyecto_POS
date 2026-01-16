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
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class VentanaProducto extends JFrame {

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
    private static final Color COLOR_TEXTO_CAMPO = Color.BLACK; // Negro para texto en campos

    // Fuente principal - Poppins
    private static Font FUENTE_TITULO;
    private static Font FUENTE_SUBTITULO;
    private static Font FUENTE_BASE;
    private static Font FUENTE_LABEL;
    private static Font FUENTE_BOTON;

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
        FUENTE_TITULO = cargarFuente("/resources/fonts/Poppins-Bold.ttf", Font.BOLD, 32f);
        FUENTE_SUBTITULO = cargarFuente("/resources/fonts/Poppins-Bold.ttf", Font.BOLD, 24f);
        FUENTE_BASE = cargarFuente("/resources/fonts/Poppins-Regular.ttf", Font.PLAIN, 14f);
        FUENTE_LABEL = cargarFuente("/resources/fonts/Poppins-Regular.ttf", Font.PLAIN, 13f);
        FUENTE_BOTON = cargarFuente("/resources/fonts/Poppins-Bold.ttf", Font.BOLD, 13f);

        configurarVentana();
        inicializarComponentes();
        configurarLayout();
    }

    private void configurarVentana() {
        setTitle(CargadorProperties.obtenerComponentes("ventana.productos.titulo"));
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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

        comboOpciones = new JComboBox<>(new String[] {
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
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        panelSuperior.setBackground(COLOR_PRIMARIO);

        JPanel panelFilaCombo = new JPanel(new BorderLayout());
        panelFilaCombo.setBackground(COLOR_PRIMARIO);

        JPanel panelIzquierda = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        panelIzquierda.setBackground(COLOR_PRIMARIO);
        try {
            ImageIcon iconoOriginal = new ImageIcon(getClass().getResource("/resources/img/logo.jpg"));
            Image imagenEscalada = iconoOriginal.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
            JLabel lblLogo = new JLabel(new ImageIcon(imagenEscalada));
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
        JButton btnVolver = new JButton(CargadorProperties.obtenerComponentes("boton.volver"));
        estilizarBotonSecundario(btnVolver);
        btnVolver.addActionListener(e -> volverAlMenu());
        panelInferior.add(btnVolver);
        add(panelInferior, BorderLayout.SOUTH);
    }

    private JPanel crearPanelVacio() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(COLOR_FONDO_CENTRAL);
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel lblTitulo = new JLabel(CargadorProperties.obtenerComponentes("ventana.productos.titulo.vacio"));
        lblTitulo.setFont(new Font("Poppins", Font.BOLD, 48));
        lblTitulo.setForeground(COLOR_PRIMARIO);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(lblTitulo, gbc);

        return panel;
    }

    private JPanel crearPanelIngresar() {
        // Wrapper centrado
        JPanel panelWrapper = new JPanel(new GridBagLayout());
        GridBagConstraints gw = new GridBagConstraints();
        gw.gridx = 0;
        gw.gridy = 0;
        gw.insets = new Insets(0, 0, 0, 0);
        gw.anchor = GridBagConstraints.NORTH;

        // Panel con 2 columnas
        JPanel panelPrincipal = new JPanel(new GridBagLayout());
        GridBagConstraints gp = new GridBagConstraints();
        gp.insets = new Insets(0, 0, 0, 0);
        gp.anchor = GridBagConstraints.NORTHWEST;

        // Panel Izquierdo para los campos
        JPanel panelCampos = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 0, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Componentes
        comboCategoriaIng = new JComboBox<>();
        estilizarComboBox(comboCategoriaIng);
        comboCategoriaIng.setPreferredSize(new Dimension(350, 32));
        comboCategoriaIng.setMinimumSize(new Dimension(350, 32));
        comboCategoriaIng.setMaximumSize(new Dimension(350, 32));

        txtCodigoIng = new JTextField();
        estilizarCampoTexto(txtCodigoIng);
        txtCodigoIng.setPreferredSize(new Dimension(350, 32));
        txtCodigoIng.setMinimumSize(new Dimension(350, 32));
        txtCodigoIng.setMaximumSize(new Dimension(350, 32));

        txtDescripcionIng = new JTextField();
        estilizarCampoTexto(txtDescripcionIng);
        txtDescripcionIng.setPreferredSize(new Dimension(350, 32));
        txtDescripcionIng.setMinimumSize(new Dimension(350, 32));
        txtDescripcionIng.setMaximumSize(new Dimension(350, 32));

        comboUmCompraIng = new JComboBox<>();
        estilizarComboBox(comboUmCompraIng);
        comboUmCompraIng.setPreferredSize(new Dimension(350, 32));
        comboUmCompraIng.setMinimumSize(new Dimension(350, 32));
        comboUmCompraIng.setMaximumSize(new Dimension(350, 32));

        txtPrecioCompraIng = new JTextField();
        estilizarCampoTexto(txtPrecioCompraIng);
        txtPrecioCompraIng.setPreferredSize(new Dimension(350, 32));
        txtPrecioCompraIng.setMinimumSize(new Dimension(350, 32));
        txtPrecioCompraIng.setMaximumSize(new Dimension(350, 32));

        comboUmVentaIng = new JComboBox<>();
        estilizarComboBox(comboUmVentaIng);
        comboUmVentaIng.setPreferredSize(new Dimension(350, 32));
        comboUmVentaIng.setMinimumSize(new Dimension(350, 32));
        comboUmVentaIng.setMaximumSize(new Dimension(350, 32));

        txtPrecioVentaIng = new JTextField();
        estilizarCampoTexto(txtPrecioVentaIng);
        txtPrecioVentaIng.setPreferredSize(new Dimension(350, 32));
        txtPrecioVentaIng.setMinimumSize(new Dimension(350, 32));
        txtPrecioVentaIng.setMaximumSize(new Dimension(350, 32));

        lblImagenPreviewIng = new JLabel(CargadorProperties.obtenerComponentes("imagen.sin.imagen"),
                SwingConstants.CENTER);
        lblImagenPreviewIng.setPreferredSize(new Dimension(200, 234));
        lblImagenPreviewIng.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        btnSeleccionarImagenIng = new JButton(CargadorProperties.obtenerComponentes("boton.seleccionar.imagen"));
        estilizarBotonPrimario(btnSeleccionarImagenIng);
        btnSeleccionarImagenIng.addActionListener(e -> seleccionarImagenIngresar());

        // Labels error
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

        // Metodos de carga y validación
        cargarCategorias(comboCategoriaIng);
        cargarUnidadesMedida(comboUmCompraIng);
        cargarUnidadesMedida(comboUmVentaIng);

        configurarValidacionesIngresar();
        comboCategoriaIng.addActionListener(e -> generarCodigoAutomatico());

        // Columna Izquierda
        int fila = 0;

        // Categoría
        gbc.gridx = 0;
        gbc.gridy = fila;
        JLabel lblCategoriaIng = new JLabel(CargadorProperties.obtenerComponentes("label.categoria"));
        lblCategoriaIng.setFont(FUENTE_LABEL);
        lblCategoriaIng.setForeground(COLOR_TEXTO);
        panelCampos.add(lblCategoriaIng, gbc);
        gbc.gridx = 1;
        panelCampos.add(comboCategoriaIng, gbc);

        fila++;
        gbc.gridx = 1;
        gbc.gridy = fila;
        gbc.insets = new Insets(0, 10, 0, 10);
        panelCampos.add(lblErrorCategoriaIng, gbc);
        gbc.insets = new Insets(5, 10, 0, 10);

        // Codigo
        fila++;
        gbc.gridx = 0;
        gbc.gridy = fila;
        JLabel lblCodigoIng = new JLabel(CargadorProperties.obtenerComponentes("label.codigo"));
        lblCodigoIng.setFont(FUENTE_LABEL);
        lblCodigoIng.setForeground(COLOR_TEXTO);
        panelCampos.add(lblCodigoIng, gbc);
        gbc.gridx = 1;
        txtCodigoIng.setEnabled(false); 
        panelCampos.add(txtCodigoIng, gbc);

        fila++;
        gbc.gridx = 1;
        gbc.gridy = fila;
        gbc.insets = new Insets(0, 10, 0, 10);
        panelCampos.add(lblErrorCodigoIng, gbc);
        gbc.insets = new Insets(5, 10, 0, 10);

        // Descripcion
        fila++;
        gbc.gridx = 0;
        gbc.gridy = fila;
        JLabel lblDescripciónIng = new JLabel(CargadorProperties.obtenerComponentes("label.descripcion"));
        lblDescripciónIng.setFont(FUENTE_LABEL);
        lblDescripciónIng.setForeground(COLOR_TEXTO);
        panelCampos.add(lblDescripciónIng, gbc);
        gbc.gridx = 1;
        panelCampos.add(txtDescripcionIng, gbc);

        fila++;
        gbc.gridx = 1;
        gbc.gridy = fila;
        gbc.insets = new Insets(0, 10, 0, 10);
        panelCampos.add(lblErrorDescripcionIng, gbc);
        gbc.insets = new Insets(5, 10, 0, 10);

        // Unidad compra
        fila++;
        gbc.gridx = 0;
        gbc.gridy = fila;
        JLabel lblUMCompraIng = new JLabel(CargadorProperties.obtenerComponentes("label.um.compra"));
        lblUMCompraIng.setFont(FUENTE_LABEL);
        lblUMCompraIng.setForeground(COLOR_TEXTO);
        panelCampos.add(lblUMCompraIng, gbc);
        gbc.gridx = 1;
        panelCampos.add(comboUmCompraIng, gbc);

        fila++;
        gbc.gridx = 1;
        gbc.gridy = fila;
        gbc.insets = new Insets(0, 10, 0, 10);
        panelCampos.add(lblErrorUmCompraIng, gbc);
        gbc.insets = new Insets(5, 10, 0, 10);

        // Precio compra
        fila++;
        gbc.gridx = 0;
        gbc.gridy = fila;
        JLabel lblPrecioCompraIng = new JLabel(CargadorProperties.obtenerComponentes("label.precio.compra"));
        lblPrecioCompraIng.setFont(FUENTE_LABEL);
        lblPrecioCompraIng.setForeground(COLOR_TEXTO);
        panelCampos.add(lblPrecioCompraIng, gbc);
        gbc.gridx = 1;
        panelCampos.add(txtPrecioCompraIng, gbc);

        fila++;
        gbc.gridx = 1;
        gbc.gridy = fila;
        gbc.insets = new Insets(0, 10, 0, 10);
        panelCampos.add(lblErrorPrecioCompraIng, gbc);
        gbc.insets = new Insets(5, 10, 0, 10);

        // Unidad venta
        fila++;
        gbc.gridx = 0;
        gbc.gridy = fila;
        JLabel lblUMVentaIng = new JLabel(CargadorProperties.obtenerComponentes("label.um.venta"));
        lblUMVentaIng.setFont(FUENTE_LABEL);
        lblUMVentaIng.setForeground(COLOR_TEXTO);
        panelCampos.add(lblUMVentaIng, gbc);
        gbc.gridx = 1;
        panelCampos.add(comboUmVentaIng, gbc);

        fila++;
        gbc.gridx = 1;
        gbc.gridy = fila;
        gbc.insets = new Insets(0, 10, 0, 10);
        panelCampos.add(lblErrorUmVentaIng, gbc);
        gbc.insets = new Insets(5, 10, 0, 10);

        // Precio venta
        fila++;
        gbc.gridx = 0;
        gbc.gridy = fila;
        JLabel lblPrecioVentaIng = new JLabel(CargadorProperties.obtenerComponentes("label.precio.venta"));
        lblPrecioVentaIng.setFont(FUENTE_LABEL);
        lblPrecioVentaIng.setForeground(COLOR_TEXTO);
        panelCampos.add(lblPrecioVentaIng, gbc);
        gbc.gridx = 1;
        panelCampos.add(txtPrecioVentaIng, gbc);

        fila++;
        gbc.gridx = 1;
        gbc.gridy = fila;
        gbc.insets = new Insets(0, 10, 0, 10);
        panelCampos.add(lblErrorPrecioVentaIng, gbc);
        gbc.insets = new Insets(5, 10, 0, 10);

        // Columna Derecha
        JPanel panelImagen = new JPanel(new GridBagLayout());
        GridBagConstraints gi = new GridBagConstraints();
        gi.insets = new Insets(5, 10, 0, 10);
        gi.anchor = GridBagConstraints.WEST;

        // Imagen
        gi.gridx = 0;
        gi.gridy = 0;
        JLabel lblImagenIng = new JLabel(CargadorProperties.obtenerComponentes("label.imagen"));
        lblImagenIng.setFont(FUENTE_LABEL);
        lblImagenIng.setForeground(COLOR_TEXTO);
        panelImagen.add(lblImagenIng, gi);
        gi.gridx = 1;
        gi.gridy = 0;
        panelImagen.add(btnSeleccionarImagenIng, gi);

        gi.gridx = 1;
        gi.gridy = 1;
        gi.insets = new Insets(0, 10, 0, 10);
        panelImagen.add(lblErrorImagenIng, gi);
        gi.insets = new Insets(10, 10, 0, 10);

        gi.gridx = 1;
        gi.gridy = 2;
        panelImagen.add(lblImagenPreviewIng, gi);

        // Guardar
        JButton btnGuardar = new JButton(CargadorProperties.obtenerComponentes("boton.guardar"));
        estilizarBotonPrimario(btnGuardar);
        btnGuardar.setPreferredSize(new Dimension(120, 35));
        btnGuardar.addActionListener(e -> guardarProducto());

        gi.gridx = 1;
        gi.gridy = 3;
        gi.anchor = GridBagConstraints.EAST;
        gi.insets = new Insets(30, 10, 0, 10);
        panelImagen.add(btnGuardar, gi);

        // Combinar columnas
        gp.gridx = 0;
        gp.gridy = 0;
        gp.weightx = 1;
        panelPrincipal.add(panelCampos, gp);

        gp.gridx = 1;
        gp.gridy = 0;
        gp.weightx = 0;
        gp.insets = new Insets(0, 40, 0, 0);
        panelPrincipal.add(panelImagen, gp);

        // Centrar con el wrapper
        panelWrapper.add(panelPrincipal, gw);

        return panelWrapper;
    }

    private JPanel crearPanelModificar() {
        // Wrapper centrado
        JPanel panelWrapper = new JPanel(new GridBagLayout());
        GridBagConstraints gw = new GridBagConstraints();
        gw.gridx = 0;
        gw.gridy = 0;
        gw.insets = new Insets(0, 0, 0, 0);
        gw.anchor = GridBagConstraints.NORTH;

        // Panel con 2 columnas
        JPanel panelPrincipal = new JPanel(new GridBagLayout());
        GridBagConstraints gp = new GridBagConstraints();
        gp.insets = new Insets(0, 0, 0, 0);
        gp.anchor = GridBagConstraints.NORTHWEST;

        // Panel Izquierdo para los campos
        JPanel panelCampos = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 0, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Componentes
        txtCodigoMod = new JTextField();
        estilizarCampoTexto(txtCodigoMod);
        txtCodigoMod.setPreferredSize(new Dimension(350, 32));
        txtCodigoMod.setMinimumSize(new Dimension(350, 32));
        txtCodigoMod.setMaximumSize(new Dimension(350, 32));

        txtDescripcionMod = new JTextField();
        estilizarCampoTexto(txtDescripcionMod);
        txtDescripcionMod.setPreferredSize(new Dimension(350, 32));
        txtDescripcionMod.setMinimumSize(new Dimension(350, 32));
        txtDescripcionMod.setMaximumSize(new Dimension(350, 32));
        txtDescripcionMod.setEnabled(false);

        comboUmCompraMod = new JComboBox<>();
        estilizarComboBox(comboUmCompraMod);
        comboUmCompraMod.setPreferredSize(new Dimension(350, 32));
        comboUmCompraMod.setMinimumSize(new Dimension(350, 32));
        comboUmCompraMod.setMaximumSize(new Dimension(350, 32));
        comboUmCompraMod.setEnabled(false);

        txtPrecioCompraMod = new JTextField();
        estilizarCampoTexto(txtPrecioCompraMod);
        txtPrecioCompraMod.setPreferredSize(new Dimension(350, 32));
        txtPrecioCompraMod.setMinimumSize(new Dimension(350, 32));
        txtPrecioCompraMod.setMaximumSize(new Dimension(350, 32));
        txtPrecioCompraMod.setEnabled(false);

        comboUmVentaMod = new JComboBox<>();
        estilizarComboBox(comboUmVentaMod);
        comboUmVentaMod.setPreferredSize(new Dimension(350, 32));
        comboUmVentaMod.setMinimumSize(new Dimension(350, 32));
        comboUmVentaMod.setMaximumSize(new Dimension(350, 32));
        comboUmVentaMod.setEnabled(false);

        txtPrecioVentaMod = new JTextField();
        estilizarCampoTexto(txtPrecioVentaMod);
        txtPrecioVentaMod.setPreferredSize(new Dimension(350, 32));
        txtPrecioVentaMod.setMinimumSize(new Dimension(350, 32));
        txtPrecioVentaMod.setMaximumSize(new Dimension(350, 32));
        txtPrecioVentaMod.setEnabled(false);

        lblImagenPreviewMod = new JLabel(CargadorProperties.obtenerComponentes("imagen.sin.imagen"),
                SwingConstants.CENTER);
        lblImagenPreviewMod.setPreferredSize(new Dimension(200, 245));
        lblImagenPreviewMod.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        btnSeleccionarImagenMod = new JButton(CargadorProperties.obtenerComponentes("boton.cambiar.imagen"));
        estilizarBotonPrimario(btnSeleccionarImagenMod);
        btnSeleccionarImagenMod.setEnabled(false);
        btnSeleccionarImagenMod.addActionListener(e -> seleccionarImagenModificar());

        // Labels error
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

        // Metodos de carga y validación
        cargarUnidadesMedida(comboUmCompraMod);
        cargarUnidadesMedida(comboUmVentaMod);
        configurarValidacionesModificar();

        // Columna Izquierda
        int fila = 0;

        // Código con botón Buscar
        gbc.gridx = 0;
        gbc.gridy = fila;
        JLabel lblCodigoMod = new JLabel(CargadorProperties.obtenerComponentes("label.codigo"));
        lblCodigoMod.setFont(FUENTE_LABEL);
        lblCodigoMod.setForeground(COLOR_TEXTO);
        panelCampos.add(lblCodigoMod, gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 3;
        JPanel panelCodigo = new JPanel(new BorderLayout(5, 0));
        panelCodigo.add(txtCodigoMod, BorderLayout.CENTER);

        JLabel lblLupa = new JLabel(CargadorProperties.obtenerComponentes("emoji.lupa"));
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
        gbc.gridx = 1;
        gbc.gridy = fila;
        gbc.insets = new Insets(0, 10, 0, 10);
        panelCampos.add(lblErrorCodigoMod, gbc);
        gbc.insets = new Insets(5, 10, 0, 10);

        // Descripción
        fila++;
        gbc.gridx = 0;
        gbc.gridy = fila;
        JLabel lblDescripciónMod = new JLabel(CargadorProperties.obtenerComponentes("label.descripcion"));
        lblDescripciónMod.setFont(FUENTE_LABEL);
        lblDescripciónMod.setForeground(COLOR_TEXTO);
        panelCampos.add(lblDescripciónMod, gbc);
        gbc.gridx = 1;
        panelCampos.add(txtDescripcionMod, gbc);

        fila++;
        gbc.gridx = 1;
        gbc.gridy = fila;
        gbc.insets = new Insets(0, 10, 0, 10);
        panelCampos.add(lblErrorDescripcionMod, gbc);
        gbc.insets = new Insets(5, 10, 0, 10);

        // Unidad compra
        fila++;
        gbc.gridx = 0;
        gbc.gridy = fila;
        JLabel lblUMCompraMod = new JLabel(CargadorProperties.obtenerComponentes("label.um.compra"));
        lblUMCompraMod.setFont(FUENTE_LABEL);
        lblUMCompraMod.setForeground(COLOR_TEXTO);
        panelCampos.add(lblUMCompraMod, gbc);
        gbc.gridx = 1;
        panelCampos.add(comboUmCompraMod, gbc);

        fila++;
        gbc.gridx = 1;
        gbc.gridy = fila;
        gbc.insets = new Insets(0, 10, 0, 10);
        panelCampos.add(lblErrorUmCompraMod, gbc);
        gbc.insets = new Insets(5, 10, 0, 10);

        // Precio compra
        fila++;
        gbc.gridx = 0;
        gbc.gridy = fila;
        JLabel lblPrecioCompraMod = new JLabel(CargadorProperties.obtenerComponentes("label.precio.compra"));
        lblPrecioCompraMod.setFont(FUENTE_LABEL);
        lblPrecioCompraMod.setForeground(COLOR_TEXTO);
        panelCampos.add(lblPrecioCompraMod, gbc);
        gbc.gridx = 1;
        panelCampos.add(txtPrecioCompraMod, gbc);

        fila++;
        gbc.gridx = 1;
        gbc.gridy = fila;
        gbc.insets = new Insets(0, 10, 0, 10);
        panelCampos.add(lblErrorPrecioCompraMod, gbc);
        gbc.insets = new Insets(5, 10, 0, 10);

        // Unidad venta
        fila++;
        gbc.gridx = 0;
        gbc.gridy = fila;
        JLabel lblUMVentaMod = new JLabel(CargadorProperties.obtenerComponentes("label.um.venta"));
        lblUMVentaMod.setFont(FUENTE_LABEL);
        lblUMVentaMod.setForeground(COLOR_TEXTO);
        panelCampos.add(lblUMVentaMod, gbc);
        gbc.gridx = 1;
        panelCampos.add(comboUmVentaMod, gbc);

        fila++;
        gbc.gridx = 1;
        gbc.gridy = fila;
        gbc.insets = new Insets(0, 10, 0, 10);
        panelCampos.add(lblErrorUmVentaMod, gbc);
        gbc.insets = new Insets(5, 10, 0, 10);

        // Precio venta
        fila++;
        gbc.gridx = 0;
        gbc.gridy = fila;
        JLabel lblPrecioVentaMod = new JLabel(CargadorProperties.obtenerComponentes("label.precio.venta"));
        lblPrecioVentaMod.setFont(FUENTE_LABEL);
        lblPrecioVentaMod.setForeground(COLOR_TEXTO);
        panelCampos.add(lblPrecioVentaMod, gbc);
        gbc.gridx = 1;
        panelCampos.add(txtPrecioVentaMod, gbc);

        fila++;
        gbc.gridx = 1;
        gbc.gridy = fila;
        gbc.insets = new Insets(0, 10, 0, 10);
        panelCampos.add(lblErrorPrecioVentaMod, gbc);
        gbc.insets = new Insets(5, 10, 0, 10);

        // Columna Derecha
        JPanel panelImagen = new JPanel(new GridBagLayout());
        GridBagConstraints gi = new GridBagConstraints();
        gi.insets = new Insets(5, 10, 0, 10);
        gi.anchor = GridBagConstraints.WEST;

        // Imagen
        gi.gridx = 0;
        gi.gridy = 0;
        JLabel lblImagenMod = new JLabel(CargadorProperties.obtenerComponentes("label.imagen"));
        lblImagenMod.setFont(FUENTE_LABEL);
        lblImagenMod.setForeground(COLOR_TEXTO);
        panelImagen.add(lblImagenMod, gi);
        gi.gridx = 1;
        gi.gridy = 0;
        panelImagen.add(btnSeleccionarImagenMod, gi);

        gi.gridx = 1;
        gi.gridy = 1;
        gi.insets = new Insets(10, 10, 0, 10);
        panelImagen.add(lblImagenPreviewMod, gi);

        // Guardar
        btnGuardarMod = new JButton(CargadorProperties.obtenerComponentes("boton.guardar"));
        estilizarBotonPrimario(btnGuardarMod);
        btnGuardarMod.setPreferredSize(new Dimension(120, 35));
        btnGuardarMod.setEnabled(false);
        btnGuardarMod.addActionListener(e -> modificarProducto());

        gi.gridx = 1;
        gi.gridy = 2;
        gi.anchor = GridBagConstraints.EAST;
        gi.insets = new Insets(30, 10, 0, 10);
        panelImagen.add(btnGuardarMod, gi);

        // Combinar columnas
        gp.gridx = 0;
        gp.gridy = 0;
        gp.weightx = 1;
        panelPrincipal.add(panelCampos, gp);

        gp.gridx = 1;
        gp.gridy = 0;
        gp.weightx = 0;
        gp.insets = new Insets(0, 40, 0, 0);
        panelPrincipal.add(panelImagen, gp);

        // Centrar con el wrapper
        panelWrapper.add(panelPrincipal, gw);

        return panelWrapper;
    }

    private JPanel crearPanelEliminar() {
        // Wrapper centrado
        JPanel panelWrapper = new JPanel(new GridBagLayout());
        GridBagConstraints gw = new GridBagConstraints();
        gw.gridx = 0;
        gw.gridy = 0;
        gw.insets = new Insets(0, 0, 0, 0);
        gw.anchor = GridBagConstraints.NORTH;

        // Panel con 2 columnas
        JPanel panelPrincipal = new JPanel(new GridBagLayout());
        GridBagConstraints gp = new GridBagConstraints();
        gp.insets = new Insets(0, 0, 0, 0);
        gp.anchor = GridBagConstraints.NORTHWEST;

        // Panel Izquierdo para los campos
        JPanel panelCampos = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Componentes
        txtCodigoElim = new JTextField();
        estilizarCampoTexto(txtCodigoElim);
        txtCodigoElim.setPreferredSize(new Dimension(350, 32));
        txtCodigoElim.setMinimumSize(new Dimension(350, 32));
        txtCodigoElim.setMaximumSize(new Dimension(350, 32));

        txtDescripcionElim = new JTextField();
        estilizarCampoTexto(txtDescripcionElim);
        txtDescripcionElim.setPreferredSize(new Dimension(350, 32));
        txtDescripcionElim.setMinimumSize(new Dimension(350, 32));
        txtDescripcionElim.setMaximumSize(new Dimension(350, 32));
        txtDescripcionElim.setEnabled(false);

        txtCategoriaElim = new JTextField();
        estilizarCampoTexto(txtCategoriaElim);
        txtCategoriaElim.setPreferredSize(new Dimension(350, 32));
        txtCategoriaElim.setMinimumSize(new Dimension(350, 32));
        txtCategoriaElim.setMaximumSize(new Dimension(350, 32));
        txtCategoriaElim.setEnabled(false);

        txtUmCompraElim = new JTextField();
        estilizarCampoTexto(txtUmCompraElim);
        txtUmCompraElim.setPreferredSize(new Dimension(350, 32));
        txtUmCompraElim.setMinimumSize(new Dimension(350, 32));
        txtUmCompraElim.setMaximumSize(new Dimension(350, 32));
        txtUmCompraElim.setEnabled(false);

        txtPrecioCompraElim = new JTextField();
        estilizarCampoTexto(txtPrecioCompraElim);
        txtPrecioCompraElim.setPreferredSize(new Dimension(350, 32));
        txtPrecioCompraElim.setMinimumSize(new Dimension(350, 32));
        txtPrecioCompraElim.setMaximumSize(new Dimension(350, 32));
        txtPrecioCompraElim.setEnabled(false);

        txtUmVentaElim = new JTextField();
        estilizarCampoTexto(txtUmVentaElim);
        txtUmVentaElim.setPreferredSize(new Dimension(350, 32));
        txtUmVentaElim.setMinimumSize(new Dimension(350, 32));
        txtUmVentaElim.setMaximumSize(new Dimension(350, 32));
        txtUmVentaElim.setEnabled(false);

        txtPrecioVentaElim = new JTextField();
        estilizarCampoTexto(txtPrecioVentaElim);
        txtPrecioVentaElim.setPreferredSize(new Dimension(350, 32));
        txtPrecioVentaElim.setMinimumSize(new Dimension(350, 32));
        txtPrecioVentaElim.setMaximumSize(new Dimension(350, 32));
        txtPrecioVentaElim.setEnabled(false);

        txtSaldoIniElim = new JTextField();
        estilizarCampoTexto(txtSaldoIniElim);
        txtSaldoIniElim.setPreferredSize(new Dimension(350, 32));
        txtSaldoIniElim.setMinimumSize(new Dimension(350, 32));
        txtSaldoIniElim.setMaximumSize(new Dimension(350, 32));
        txtSaldoIniElim.setEnabled(false);

        txtSaldoFinElim = new JTextField();
        estilizarCampoTexto(txtSaldoFinElim);
        txtSaldoFinElim.setPreferredSize(new Dimension(350, 32));
        txtSaldoFinElim.setMinimumSize(new Dimension(350, 32));
        txtSaldoFinElim.setMaximumSize(new Dimension(350, 32));
        txtSaldoFinElim.setEnabled(false);

        lblImagenPreviewElim = new JLabel(CargadorProperties.obtenerComponentes("imagen.sin.imagen"),
                SwingConstants.CENTER);
        lblImagenPreviewElim.setPreferredSize(new Dimension(230, 290));
        lblImagenPreviewElim.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        // Columna Izquierda
        int fila = 0;

        // Código con lupa
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.gridwidth = 1;
        JLabel lblCodigoEli = new JLabel(CargadorProperties.obtenerComponentes("label.codigo"));
        lblCodigoEli.setFont(FUENTE_LABEL);
        lblCodigoEli.setForeground(COLOR_TEXTO);
        panelCampos.add(lblCodigoEli, gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 3;
        JPanel panelCodigo = new JPanel(new BorderLayout(5, 0));
        panelCodigo.add(txtCodigoElim, BorderLayout.CENTER);

        JLabel lblLupa = new JLabel(CargadorProperties.obtenerComponentes("emoji.lupa"));
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
        gbc.gridx = 0;
        gbc.gridy = fila;
        JLabel lblDescripciónEli = new JLabel(CargadorProperties.obtenerComponentes("label.descripcion"));
        lblDescripciónEli.setFont(FUENTE_LABEL);
        lblDescripciónEli.setForeground(COLOR_TEXTO);
        panelCampos.add(lblDescripciónEli, gbc);
        gbc.gridx = 1;
        panelCampos.add(txtDescripcionElim, gbc);

        // Categoría
        fila++;
        gbc.gridx = 0;
        gbc.gridy = fila;
        JLabel lblCategoriaEli = new JLabel(CargadorProperties.obtenerComponentes("label.categoria"));
        lblCategoriaEli.setFont(FUENTE_LABEL);
        lblCategoriaEli.setForeground(COLOR_TEXTO);
        panelCampos.add(lblCategoriaEli, gbc);
        gbc.gridx = 1;
        panelCampos.add(txtCategoriaElim, gbc);

        // Unidad compra
        fila++;
        gbc.gridx = 0;
        gbc.gridy = fila;
        JLabel lblUMCompraEli = new JLabel(CargadorProperties.obtenerComponentes("label.um.compra"));
        lblUMCompraEli.setFont(FUENTE_LABEL);
        lblUMCompraEli.setForeground(COLOR_TEXTO);
        panelCampos.add(lblUMCompraEli, gbc);
        gbc.gridx = 1;
        panelCampos.add(txtUmCompraElim, gbc);

        // Precio compra
        fila++;
        gbc.gridx = 0;
        gbc.gridy = fila;
        JLabel lblPrecioCompraEli = new JLabel(CargadorProperties.obtenerComponentes("label.precio.compra"));
        lblPrecioCompraEli.setFont(FUENTE_LABEL);
        lblPrecioCompraEli.setForeground(COLOR_TEXTO);
        panelCampos.add(lblPrecioCompraEli, gbc);
        gbc.gridx = 1;
        panelCampos.add(txtPrecioCompraElim, gbc);

        // Unidad venta
        fila++;
        gbc.gridx = 0;
        gbc.gridy = fila;
        JLabel lblUMVentaEli = new JLabel(CargadorProperties.obtenerComponentes("label.um.venta"));
        lblUMVentaEli.setFont(FUENTE_LABEL);
        lblUMVentaEli.setForeground(COLOR_TEXTO);
        panelCampos.add(lblUMVentaEli, gbc);
        gbc.gridx = 1;
        panelCampos.add(txtUmVentaElim, gbc);

        // Precio venta
        fila++;
        gbc.gridx = 0;
        gbc.gridy = fila;
        JLabel lblPrecioVentaEli = new JLabel(CargadorProperties.obtenerComponentes("label.precio.venta"));
        lblPrecioVentaEli.setFont(FUENTE_LABEL);
        lblPrecioVentaEli.setForeground(COLOR_TEXTO);
        panelCampos.add(lblPrecioVentaEli, gbc);
        gbc.gridx = 1;
        panelCampos.add(txtPrecioVentaElim, gbc);

        // Saldo Inicial
        fila++;
        gbc.gridx = 0;
        gbc.gridy = fila;
        JLabel lblSaldoIniMod = new JLabel(CargadorProperties.obtenerComponentes("label.saldo.inicial"));
        lblSaldoIniMod.setFont(FUENTE_LABEL);
        lblSaldoIniMod.setForeground(COLOR_TEXTO);
        panelCampos.add(lblSaldoIniMod, gbc);
        gbc.gridx = 1;
        panelCampos.add(txtSaldoIniElim, gbc);

        // Saldo Final
        fila++;
        gbc.gridx = 0;
        gbc.gridy = fila;
        JLabel lblSaldoFinMod = new JLabel(CargadorProperties.obtenerComponentes("label.saldo.final"));
        lblSaldoFinMod.setFont(FUENTE_LABEL);
        lblSaldoFinMod.setForeground(COLOR_TEXTO);
        panelCampos.add(lblSaldoFinMod, gbc);
        gbc.gridx = 1;
        panelCampos.add(txtSaldoFinElim, gbc);

        // Columna Derecha
        JPanel panelImagen = new JPanel(new GridBagLayout());
        GridBagConstraints gi = new GridBagConstraints();
        gi.insets = new Insets(5, 10, 0, 10);
        gi.anchor = GridBagConstraints.WEST;

        // Imagen
        gi.gridx = 0;
        gi.gridy = 0;
        JLabel lblImagenEli = new JLabel(CargadorProperties.obtenerComponentes("label.imagen"));
        lblImagenEli.setFont(FUENTE_LABEL);
        lblImagenEli.setForeground(COLOR_TEXTO);
        panelImagen.add(lblImagenEli, gi);
        gi.gridx = 0;
        gi.gridy = 1;
        gi.insets = new Insets(10, 10, 0, 10);
        panelImagen.add(lblImagenPreviewElim, gi);

        // Eliminar
        btnEliminar = new JButton(CargadorProperties.obtenerComponentes("boton.eliminar"));
        estilizarBotonEliminar(btnEliminar);
        btnEliminar.setPreferredSize(new Dimension(120, 35));
        btnEliminar.setEnabled(false);
        btnEliminar.addActionListener(e -> eliminarProducto());

        gi.gridx = 0;
        gi.gridy = 2;
        gi.anchor = GridBagConstraints.EAST;
        gi.insets = new Insets(30, 10, 0, 10);
        panelImagen.add(btnEliminar, gi);

        // Combinar columnas
        gp.gridx = 0;
        gp.gridy = 0;
        gp.weightx = 1;
        panelPrincipal.add(panelCampos, gp);

        gp.gridx = 1;
        gp.gridy = 0;
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
        comboTipoConsulta = new JComboBox<>(new String[] {
                CargadorProperties.obtenerComponentes("combo.consulta.seleccione"),
                CargadorProperties.obtenerComponentes("combo.consulta.general"),
                CargadorProperties.obtenerComponentes("combo.consulta.parametro")
        });
        estilizarComboBox(comboTipoConsulta);
        comboTipoConsulta.setPreferredSize(new Dimension(250, 30));
        comboTipoConsulta.addActionListener(e -> cambiarTipoConsulta());
        panelTipo.add(comboTipoConsulta);
        panel.add(panelTipo, BorderLayout.NORTH);

        // Panel central (contiene búsqueda y tabla)
        JPanel panelCentral = new JPanel(new BorderLayout(5, 5));

        // Panel de búsqueda CENTRADO
        panelBusqueda = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        txtBusqueda = new JTextField(20);
        estilizarCampoTexto(txtBusqueda);
        txtBusqueda.setPreferredSize(new Dimension(250, 25));
        txtBusqueda.setToolTipText(CargadorProperties.obtenerComponentes("tooltip.busqueda"));

        comboParametroBusqueda = new JComboBox<>();
        estilizarComboBox(comboParametroBusqueda);
        comboParametroBusqueda.setPreferredSize(new Dimension(200, 25));

        panelBusqueda.add(new JLabel(CargadorProperties.obtenerComponentes("label.buscar")));
        panelBusqueda.add(txtBusqueda);
        panelBusqueda.add(comboParametroBusqueda);
        panelBusqueda.setVisible(false);

        panelCentral.add(panelBusqueda, BorderLayout.NORTH);

        // Tabla de resultados
        String[] columnas = {
                CargadorProperties.obtenerComponentes("tabla.col.codigo"),
                CargadorProperties.obtenerComponentes("tabla.col.descripcion"),
                CargadorProperties.obtenerComponentes("tabla.col.categoria"),
                CargadorProperties.obtenerComponentes("tabla.col.um.compra"),
                CargadorProperties.obtenerComponentes("tabla.col.precio.compra"),
                CargadorProperties.obtenerComponentes("tabla.col.um.venta"),
                CargadorProperties.obtenerComponentes("tabla.col.precio.venta"),
                CargadorProperties.obtenerComponentes("tabla.col.saldo.inicial"),
                CargadorProperties.obtenerComponentes("tabla.col.saldo.final")
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
        tablaResultados.setFont(FUENTE_BASE);
        tablaResultados.setForeground(COLOR_TEXTO_CAMPO);

        // Estilizar header de la tabla
        tablaResultados.getTableHeader().setFont(new Font("Poppins", Font.BOLD, 13));
        tablaResultados.getTableHeader().setBackground(COLOR_ENFASIS); // #ffa41c
        tablaResultados.getTableHeader().setForeground(COLOR_TEXTO); // #4C57A9
        tablaResultados.getTableHeader().setOpaque(true);

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
        scrollTabla.setBorder(new LineBorder(COLOR_BORDE, 1));
        scrollTabla.getViewport().setBackground(COLOR_BLANCO);
        scrollTabla.setVisible(false);

        panelCentral.add(scrollTabla, BorderLayout.CENTER);

        panel.add(panelCentral, BorderLayout.CENTER);

        configurarBusquedaTiempoReal();
        cargarParametrosBusqueda();

        return panel;
    }

    private void cambiarPanel() {
        int seleccion = comboOpciones.getSelectedIndex();

        switch (seleccion) {
            case 1: // Ingresar
                cardLayout.show(panelContenedor, PANEL_INGRESAR);
                lblTituloSuperior.setText(CargadorProperties.obtenerComponentes("ventana.productos.titulo.ingresar"));
                limpiarCamposIngresar();
                break;
            case 2: // Modificar
                cardLayout.show(panelContenedor, PANEL_MODIFICAR);
                lblTituloSuperior.setText(CargadorProperties.obtenerComponentes("ventana.productos.titulo.modificar"));
                limpiarCamposModificar();
                break;
            case 3: // Eliminar
                cardLayout.show(panelContenedor, PANEL_ELIMINAR);
                lblTituloSuperior.setText(CargadorProperties.obtenerComponentes("ventana.productos.titulo.eliminar"));
                limpiarCamposEliminar();
                break;
            case 4: // Consultar
                cardLayout.show(panelContenedor, PANEL_CONSULTAR);
                lblTituloSuperior.setText(CargadorProperties.obtenerComponentes("ventana.productos.titulo.consultar"));
                comboTipoConsulta.setSelectedIndex(0);
                break;
            default:
                cardLayout.show(panelContenedor, PANEL_VACIO);
                lblTituloSuperior.setText(CargadorProperties.obtenerComponentes("ventana.productos.titulo"));
                break;
        }
    }

    // Vuelve al panel vacío
    private void volverAlMenu() {
        this.dispose();
        new MenuPrincipal().setVisible(true);
    }

    private JLabel crearLabelError() {
        JLabel lbl = new JLabel(" ");
        lbl.setForeground(COLOR_ACENTO);
        lbl.setFont(new Font("Poppins", Font.PLAIN, 11));
        lbl.setPreferredSize(new Dimension(350, 20));
        lbl.setMinimumSize(new Dimension(350, 20));
        lbl.setMaximumSize(new Dimension(350, 20));
        lbl.setVerticalAlignment(SwingConstants.BOTTOM);
        return lbl;
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
        comboParametroBusqueda
                .addItem(new ItemCombo("codigo", CargadorProperties.obtenerComponentes("combo.param.codigo")));
        comboParametroBusqueda.addItem(
                new ItemCombo("descripcion", CargadorProperties.obtenerComponentes("combo.param.descripcion")));
        comboParametroBusqueda
                .addItem(new ItemCombo("categoria", CargadorProperties.obtenerComponentes("combo.param.categoria")));
        comboParametroBusqueda
                .addItem(new ItemCombo("um_compra", CargadorProperties.obtenerComponentes("combo.param.um.compra")));
        comboParametroBusqueda
                .addItem(new ItemCombo("um_venta", CargadorProperties.obtenerComponentes("combo.param.um.venta")));
    }

    private void generarCodigoAutomatico() {
        ItemCombo categoriaSeleccionada = (ItemCombo) comboCategoriaIng.getSelectedItem();

        if (categoriaSeleccionada != null && !categoriaSeleccionada.getId().isEmpty()) {
            Producto pro = new Producto();
            pro.setIdCategoria(categoriaSeleccionada.getId());
            String codigoGenerado = pro.generarCodigoDP();
            txtCodigoIng.setText(codigoGenerado);
            lblErrorCodigoIng.setText(" ");
        } else {
            txtCodigoIng.setText("");
        }
    }

    private void mostrarError(JLabel label, String error) {
        if (error != null) {
            label.setText(error);
        } else {
            label.setText(" ");
        }
    }

    private void configurarValidacionesIngresar() {
        comboCategoriaIng.addActionListener(e -> validarCategoriaIngresar());

        txtDescripcionIng.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                validarDescripcionIngresar();
            }

            public void removeUpdate(DocumentEvent e) {
                validarDescripcionIngresar();
            }

            public void changedUpdate(DocumentEvent e) {
                validarDescripcionIngresar();
            }
        });

        comboUmCompraIng.addActionListener(e -> validarUmCompraIngresar());

        txtPrecioCompraIng.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                validarPrecioCompraIngresar();
            }

            public void removeUpdate(DocumentEvent e) {
                validarPrecioCompraIngresar();
            }

            public void changedUpdate(DocumentEvent e) {
                validarPrecioCompraIngresar();
            }
        });

        comboUmVentaIng.addActionListener(e -> validarUmVentaIngresar());

        txtPrecioVentaIng.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                validarPrecioVentaIngresar();
            }

            public void removeUpdate(DocumentEvent e) {
                validarPrecioVentaIngresar();
            }

            public void changedUpdate(DocumentEvent e) {
                validarPrecioVentaIngresar();
            }
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
                txtPrecioCompraIng.getText());

        if (error != null) {
            lblErrorPrecioVentaIng.setText(error);
        } else {
            lblErrorPrecioVentaIng.setText(" ");
        }
    }

    private void configurarValidacionesModificar() {
        txtDescripcionMod.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                validarDescripcionModificar();
            }

            public void removeUpdate(DocumentEvent e) {
                validarDescripcionModificar();
            }

            public void changedUpdate(DocumentEvent e) {
                validarDescripcionModificar();
            }
        });

        comboUmCompraMod.addActionListener(e -> validarUmCompraModificar());

        txtPrecioCompraMod.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                validarPrecioCompraModificar();
            }

            public void removeUpdate(DocumentEvent e) {
                validarPrecioCompraModificar();
            }

            public void changedUpdate(DocumentEvent e) {
                validarPrecioCompraModificar();
            }
        });

        comboUmVentaMod.addActionListener(e -> validarUmVentaModificar());

        txtPrecioVentaMod.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                validarPrecioVentaModificar();
            }

            public void removeUpdate(DocumentEvent e) {
                validarPrecioVentaModificar();
            }

            public void changedUpdate(DocumentEvent e) {
                validarPrecioVentaModificar();
            }
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
                txtPrecioCompraMod.getText());

        if (error != null) {
            lblErrorPrecioVentaMod.setText(error);
        } else {
            lblErrorPrecioVentaMod.setText(" ");
        }
    }

    private void seleccionarImagenIngresar() {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                CargadorProperties.obtenerComponentes("file.chooser.filtro.imagenes"), "jpg", "jpeg", "png", "gif");
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
                CargadorProperties.obtenerComponentes("file.chooser.filtro.imagenes"), "jpg", "jpeg", "png", "gif");
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
            lblPreview.setText(CargadorProperties.obtenerComponentes("imagen.error.cargar"));
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
                lblPreview.setText(CargadorProperties.obtenerComponentes("imagen.sin.imagen"));
            }
        } catch (IOException e) {
            lblPreview.setIcon(null);
            lblPreview.setText(CargadorProperties.obtenerComponentes("imagen.error.cargar"));
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
            personalizarPopup();
            JOptionPane.showMessageDialog(this,
                    CargadorProperties.obtenerMessages("PD_A_016"),
                    CargadorProperties.obtenerMessages("FC_C_004"),
                    JOptionPane.ERROR_MESSAGE);
            restaurarEstilosPopup();
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

        String errorCategoria = ValidacionesProducto.validarCategoria(idCategoria);
        String errorCodigo = ValidacionesProducto.validarCodigo(codigo);
        String errorDescripcion = ValidacionesProducto.validarDescripcion(descripcion, false);
        String errorUmCompra = ValidacionesProducto.validarUnidadMedida(idUmCompra, "compra");
        String errorPrecioCompra = ValidacionesProducto.validarPrecioCompra(precioCompra);
        String errorUmVenta = ValidacionesProducto.validarUnidadMedida(idUmVenta, "venta");
        String errorPrecioVenta = ValidacionesProducto.validarPrecioVenta(precioVenta, precioCompra);
        String errorImagen = ValidacionesProducto.validarImagen(rutaImagenSeleccionadaIng);

        mostrarError(lblErrorCategoriaIng, errorCategoria);
        mostrarError(lblErrorCodigoIng, errorCodigo);
        mostrarError(lblErrorDescripcionIng, errorDescripcion);
        mostrarError(lblErrorUmCompraIng, errorUmCompra);
        mostrarError(lblErrorPrecioCompraIng, errorPrecioCompra);
        mostrarError(lblErrorUmVentaIng, errorUmVenta);
        mostrarError(lblErrorPrecioVentaIng, errorPrecioVenta);
        mostrarError(lblErrorImagenIng, errorImagen);

        boolean hayErrores = errorCategoria != null ||
                errorCodigo != null ||
                errorDescripcion != null ||
                errorUmCompra != null ||
                errorPrecioCompra != null ||
                errorUmVenta != null ||
                errorPrecioVenta != null ||
                errorImagen != null;

        if (hayErrores) {
            personalizarPopup();
            JOptionPane.showMessageDialog(this,
                    CargadorProperties.obtenerMessages("PD_A_013"),
                    CargadorProperties.obtenerMessages("FC_C_005"),
                    JOptionPane.WARNING_MESSAGE);
            restaurarEstilosPopup();
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
            personalizarPopup();
            JOptionPane.showMessageDialog(this,
                    CargadorProperties.obtenerMessages("PD_I_001"),
                    CargadorProperties.obtenerMessages("FC_C_003"),
                    JOptionPane.INFORMATION_MESSAGE);
            restaurarEstilosPopup();
            limpiarCamposIngresar();
        } else {
            personalizarPopup();
            JOptionPane.showMessageDialog(this,
                    CargadorProperties.obtenerMessages("PD_E_001"),
                    CargadorProperties.obtenerMessages("FC_C_004"),
                    JOptionPane.ERROR_MESSAGE);
            restaurarEstilosPopup();
        }
    }

    private void buscarProductoModificar() {
        String codigo = txtCodigoMod.getText().trim();

        if (codigo.isEmpty()) {
            personalizarPopup();
            JOptionPane.showMessageDialog(this,
                    CargadorProperties.obtenerMessages("PD_A_014"),
                    CargadorProperties.obtenerMessages("FC_C_005"),
                    JOptionPane.WARNING_MESSAGE);
            restaurarEstilosPopup();
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
            personalizarPopup();
            JOptionPane.showMessageDialog(this,
                    CargadorProperties.obtenerMessages("PD_A_015"),
                    CargadorProperties.obtenerMessages("FC_C_006"),
                    JOptionPane.INFORMATION_MESSAGE);
            restaurarEstilosPopup();
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
            personalizarPopup();
            JOptionPane.showMessageDialog(this,
                    CargadorProperties.obtenerMessages("PD_A_013"),
                    CargadorProperties.obtenerMessages("FC_C_005"),
                    JOptionPane.WARNING_MESSAGE);
            restaurarEstilosPopup();
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
            personalizarPopup();
            JOptionPane.showMessageDialog(this,
                    CargadorProperties.obtenerMessages("PD_I_002"),
                    CargadorProperties.obtenerMessages("FC_C_003"),
                    JOptionPane.INFORMATION_MESSAGE);
            restaurarEstilosPopup();
            limpiarCamposModificar();
        } else {
            personalizarPopup();
            JOptionPane.showMessageDialog(this,
                    CargadorProperties.obtenerMessages("PD_E_003"),
                    CargadorProperties.obtenerMessages("FC_C_004"),
                    JOptionPane.ERROR_MESSAGE);
            restaurarEstilosPopup();
        }
    }

    private void buscarProductoEliminar() {
        String codigo = txtCodigoElim.getText().trim();

        if (codigo.isEmpty()) {
            personalizarPopup();
            JOptionPane.showMessageDialog(this,
                    CargadorProperties.obtenerMessages("PD_A_014"),
                    CargadorProperties.obtenerMessages("FC_C_005"),
                    JOptionPane.WARNING_MESSAGE);
            restaurarEstilosPopup();
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
            personalizarPopup();
            JOptionPane.showMessageDialog(this,
                    CargadorProperties.obtenerMessages("PD_A_015"),
                    CargadorProperties.obtenerMessages("FC_C_006"),
                    JOptionPane.INFORMATION_MESSAGE);
            restaurarEstilosPopup();
        }
    }

    private void eliminarProducto() {
        int confirm = JOptionPane.showConfirmDialog(this,
                CargadorProperties.obtenerMessages("PD_C_001") + txtDescripcionElim.getText()
                        + CargadorProperties.obtenerMessages("PD_C_002"),
                CargadorProperties.obtenerMessages("FC_A_008"),
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            Producto pro = new Producto();
            pro.setCodigo(txtCodigoElim.getText().trim());

            if (pro.eliminarDP()) {
                personalizarPopup();
                JOptionPane.showMessageDialog(this,
                        CargadorProperties.obtenerMessages("PD_I_003"),
                        CargadorProperties.obtenerMessages("FC_C_003"),
                        JOptionPane.INFORMATION_MESSAGE);
                restaurarEstilosPopup();
                limpiarCamposEliminar();
            } else {
                personalizarPopup();
                JOptionPane.showMessageDialog(this,
                        CargadorProperties.obtenerMessages("PD_E_004"),
                        CargadorProperties.obtenerMessages("FC_C_004"),
                        JOptionPane.ERROR_MESSAGE);
                restaurarEstilosPopup();
            }
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
        
        panelContenedor.revalidate();
        panelContenedor.repaint();
    }

    private void consultarGeneral() {
        Producto pro = new Producto();
        ArrayList<Producto> productos = pro.consultarTodos();

        modeloTabla.setRowCount(0);

        if (productos.isEmpty()) {
            personalizarPopup();
            JOptionPane.showMessageDialog(this,
                    CargadorProperties.obtenerMessages("PD_I_004"),
                    CargadorProperties.obtenerMessages("FC_C_006"),
                    JOptionPane.INFORMATION_MESSAGE);
            restaurarEstilosPopup();
            return;
        }

        for (Producto p : productos) {
            modeloTabla.addRow(new Object[] {
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
            public void insertUpdate(DocumentEvent e) {
                reiniciarTimer();
            }

            public void removeUpdate(DocumentEvent e) {
                reiniciarTimer();
            }

            public void changedUpdate(DocumentEvent e) {
                reiniciarTimer();
            }
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
        if (parametro == null)
            return;

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

        if (resultados.isEmpty()) {
            personalizarPopup();
            JOptionPane.showMessageDialog(this,
                    CargadorProperties.obtenerMessages("PD_I_005"),
                    CargadorProperties.obtenerMessages("FC_C_006"),
                    JOptionPane.INFORMATION_MESSAGE);
            restaurarEstilosPopup();
            return;
        }

        modeloTabla.setRowCount(0);

        for (Producto p : resultados) {
            modeloTabla.addRow(new Object[] {
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
            JDialog dialog = new JDialog(this, CargadorProperties.obtenerComponentes("popup.imagen.titulo") + codigo,
                    true);
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
                            (double) maxHeight / img.getHeight());

                    int newWidth = (int) (img.getWidth() * scale);
                    int newHeight = (int) (img.getHeight() * scale);

                    Image imagenEscalada = img.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);

                    JLabel lblImagen = new JLabel(new ImageIcon(imagenEscalada));
                    lblImagen.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                    dialog.add(lblImagen, BorderLayout.CENTER);

                    JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.CENTER));
                    JButton btnCerrar = new JButton(CargadorProperties.obtenerComponentes("boton.cerrar"));
                    btnCerrar.addActionListener(e -> dialog.dispose());
                    panelBoton.add(btnCerrar);
                    dialog.add(panelBoton, BorderLayout.SOUTH);

                    dialog.pack();
                    dialog.setLocationRelativeTo(this);
                    dialog.setVisible(true);
                } else {
                    personalizarPopup();
                    JOptionPane.showMessageDialog(this,
                            CargadorProperties.obtenerMessages("PD_A_018"),
                            CargadorProperties.obtenerMessages("FC_C_004"),
                            JOptionPane.ERROR_MESSAGE);
                    restaurarEstilosPopup();
                }
            } catch (IOException e) {
                personalizarPopup();
                JOptionPane.showMessageDialog(this,
                        CargadorProperties.obtenerMessages("PD_A_017"),
                        CargadorProperties.obtenerMessages("FC_C_004"),
                        JOptionPane.ERROR_MESSAGE);
                restaurarEstilosPopup();
                e.printStackTrace();
            }
        } else {
            personalizarPopup();
            JOptionPane.showMessageDialog(this,
                    CargadorProperties.obtenerMessages("PD_A_019"),
                    CargadorProperties.obtenerMessages("FC_C_006"),
                    JOptionPane.INFORMATION_MESSAGE);
            restaurarEstilosPopup();
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
        lblImagenPreviewIng.setText(CargadorProperties.obtenerComponentes("imagen.sin.imagen"));
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
        lblImagenPreviewMod.setText(CargadorProperties.obtenerComponentes("imagen.sin.imagen"));
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
        lblImagenPreviewElim.setText(CargadorProperties.obtenerComponentes("imagen.sin.imagen"));

        btnEliminar.setEnabled(false);
    }

    // Métodos de estilización
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

                // Dibujar el fondo
                g2.setColor(button.getBackground());
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
                btn.setBackground(new Color(82, 121, 54));
            }

            public void mouseExited(MouseEvent evt) {
                btn.setBackground(COLOR_SECUNDARIO);
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
    }

    private void estilizarCampoTexto(JTextField campo) {
        campo.setFont(FUENTE_BASE);
        campo.setForeground(COLOR_TEXTO_CAMPO); // Negro para texto en campos
        campo.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(COLOR_BORDE, 1, true),
                new EmptyBorder(4, 8, 4, 8)));
    }

    private void estilizarComboBox(JComboBox<?> combo) {
        combo.setFont(FUENTE_BASE);
        combo.setForeground(COLOR_TEXTO); // #4C57A9 para labels
        combo.setBackground(COLOR_BLANCO);
        combo.setBorder(new LineBorder(COLOR_BORDE, 1, true));
    }

    // Método para estilizar botón eliminar (rojo)
    private void estilizarBotonEliminar(JButton btn) {
        btn.setFont(FUENTE_BOTON);
        btn.setBackground(COLOR_ACENTO);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(COLOR_ACENTO, 0, true),
                new EmptyBorder(8, 16, 8, 16)));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Configurar el UI para forzar el color del texto
        btn.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                JButton button = (JButton) c;
                Graphics2D g2 = (Graphics2D) g.create();

                // Dibujar el fondo
                g2.setColor(button.getBackground());
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
                btn.setBackground(new Color(170, 15, 0));
            }

            public void mouseExited(MouseEvent evt) {
                btn.setBackground(COLOR_ACENTO);
            }
        });
    }

    // ============================================
    // MÉTODOS PARA PERSONALIZAR POPUPS (JOptionPane)
    // ============================================

    /**
     * Personaliza el JOptionPane con la paleta de colores y tipografía del sistema
     * Debe llamarse ANTES de mostrar cualquier JOptionPane
     */
    private void personalizarPopup() {
        // Configurar fuente Poppins para todos los componentes del popup
        UIManager.put("OptionPane.messageFont", FUENTE_BASE);
        UIManager.put("OptionPane.buttonFont", FUENTE_BOTON);

        // Configurar colores del panel principal
        UIManager.put("OptionPane.background", COLOR_FONDO_CENTRAL);
        UIManager.put("Panel.background", COLOR_FONDO_CENTRAL);

        // Configurar colores de los botones
        UIManager.put("Button.background", COLOR_SECUNDARIO);
        UIManager.put("Button.foreground", COLOR_BLANCO);
        UIManager.put("Button.select", new Color(82, 121, 54)); // Hover color
        UIManager.put("Button.focus", new Color(82, 121, 54));

        // Configurar texto del mensaje
        UIManager.put("OptionPane.messageForeground", COLOR_TEXTO_CAMPO);
    }

    /**
     * Restaura los valores por defecto de UIManager después de mostrar el popup
     */
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

    private Font cargarFuente(String ruta, int estilo, float tamaño) {
        try {
            Font fuente = Font.createFont(Font.TRUETYPE_FONT,
                    getClass().getResourceAsStream(ruta));
            return fuente.deriveFont(estilo, tamaño);
        } catch (Exception e) {
            System.err.println("No se pudo cargar la fuente Poppins desde: " + ruta);
            e.printStackTrace();
            // Fuente de respaldo si Poppins no se carga
            return new Font("SansSerif", estilo, (int) tamaño);
        }
    }
}
