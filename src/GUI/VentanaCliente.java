package GUI;

import DP.Cliente;
import util.CargadorProperties;
import util.ValidacionesCliente;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.Timer;

public class VentanaCliente extends JFrame {

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
    private static final Font FUENTE_TITULO = new Font("Poppins", Font.BOLD, 32);
    private static final Font FUENTE_SUBTITULO = new Font("Poppins", Font.BOLD, 24);
    private static final Font FUENTE_BASE = new Font("Poppins", Font.PLAIN, 14);
    private static final Font FUENTE_LABEL = new Font("Poppins", Font.PLAIN, 13);
    private static final Font FUENTE_BOTON = new Font("Poppins", Font.BOLD, 13);

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

        JLabel lblTitulo = new JLabel(CargadorProperties.obtenerComponentes("ventana.clientes.titulo.vacio"));
        lblTitulo.setFont(new Font("Poppins", Font.BOLD, 48));
        lblTitulo.setForeground(COLOR_PRIMARIO);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(lblTitulo, gbc);

        return panel;
    }

    private JPanel crearPanelIngresar() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(COLOR_FONDO_CENTRAL);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 12, 0, 12);
        gbc.anchor = GridBagConstraints.WEST;

        // Configuración de campos
        txtCedulaIng = new JTextField();
        estilizarCampoTexto(txtCedulaIng);
        txtCedulaIng.setPreferredSize(new Dimension(350, 32));
        txtCedulaIng.setMinimumSize(new Dimension(350, 32));
        txtCedulaIng.setMaximumSize(new Dimension(350, 32));

        txtNombreIng = new JTextField();
        estilizarCampoTexto(txtNombreIng);
        txtNombreIng.setPreferredSize(new Dimension(350, 32));
        txtNombreIng.setMinimumSize(new Dimension(350, 32));
        txtNombreIng.setMaximumSize(new Dimension(350, 32));

        txtTelefonoIng = new JTextField();
        estilizarCampoTexto(txtTelefonoIng);
        txtTelefonoIng.setPreferredSize(new Dimension(350, 32));
        txtTelefonoIng.setMinimumSize(new Dimension(350, 32));
        txtTelefonoIng.setMaximumSize(new Dimension(350, 32));

        txtEmailIng = new JTextField();
        estilizarCampoTexto(txtEmailIng);
        txtEmailIng.setPreferredSize(new Dimension(350, 32));
        txtEmailIng.setMinimumSize(new Dimension(350, 32));
        txtEmailIng.setMaximumSize(new Dimension(350, 32));

        comboCiudadIng = new JComboBox<>();
        estilizarComboBox(comboCiudadIng);
        comboCiudadIng.setPreferredSize(new Dimension(350, 32));
        comboCiudadIng.setMinimumSize(new Dimension(350, 32));
        comboCiudadIng.setMaximumSize(new Dimension(350, 32));

        txtDireccionIng = new JTextField();
        estilizarCampoTexto(txtDireccionIng);
        txtDireccionIng.setPreferredSize(new Dimension(350, 32));
        txtDireccionIng.setMinimumSize(new Dimension(350, 32));
        txtDireccionIng.setMaximumSize(new Dimension(350, 32));

        // Labels de error
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

        // Cargar ciudades y aplicar validaciones
        cargarCiudades(comboCiudadIng);
        configurarValidacionesIngresar();

        int fila = 0;

        // COLUMNA IZQUIERDA

        // Cedula o RUC
        gbc.gridx = 0;
        gbc.gridy = fila;
        JLabel lblCedula = new JLabel(CargadorProperties.obtenerComponentes("label.cedula.ruc"));
        lblCedula.setFont(FUENTE_LABEL);
        lblCedula.setForeground(COLOR_TEXTO);
        panel.add(lblCedula, gbc);

        gbc.gridx = 1;
        panel.add(txtCedulaIng, gbc);

        fila++;
        gbc.gridx = 1;
        gbc.gridy = fila;
        gbc.insets = new Insets(0, 10, 0, 10);
        panel.add(lblErrorCedulaIng, gbc);
        gbc.insets = new Insets(5, 10, 0, 10);

        fila++;

        // Nombre
        gbc.gridx = 0;
        gbc.gridy = fila;
        JLabel lblNombre = new JLabel(CargadorProperties.obtenerComponentes("label.nombre"));
        lblNombre.setFont(FUENTE_LABEL);
        lblNombre.setForeground(COLOR_TEXTO);
        panel.add(lblNombre, gbc);

        gbc.gridx = 1;
        panel.add(txtNombreIng, gbc);

        fila++;
        gbc.gridx = 1;
        gbc.gridy = fila;
        gbc.insets = new Insets(0, 10, 0, 10);
        panel.add(lblErrorNombreIng, gbc);
        gbc.insets = new Insets(5, 10, 0, 10);

        fila++;

        // Telefono
        gbc.gridx = 0;
        gbc.gridy = fila;
        JLabel lblTelefono = new JLabel(CargadorProperties.obtenerComponentes("label.telefono"));
        lblTelefono.setFont(FUENTE_LABEL);
        lblTelefono.setForeground(COLOR_TEXTO);
        panel.add(lblTelefono, gbc);

        gbc.gridx = 1;
        panel.add(txtTelefonoIng, gbc);

        fila++;
        gbc.gridx = 1;
        gbc.gridy = fila;
        gbc.insets = new Insets(0, 10, 0, 10);
        panel.add(lblErrorTelefonoIng, gbc);
        gbc.insets = new Insets(5, 10, 0, 10);

        fila++;

        // Email
        gbc.gridx = 0;
        gbc.gridy = fila;
        JLabel lblEmail = new JLabel(CargadorProperties.obtenerComponentes("label.email"));
        lblEmail.setFont(FUENTE_LABEL);
        lblEmail.setForeground(COLOR_TEXTO);
        panel.add(lblEmail, gbc);

        gbc.gridx = 1;
        panel.add(txtEmailIng, gbc);

        fila++;
        gbc.gridx = 1;
        gbc.gridy = fila;
        gbc.insets = new Insets(0, 10, 0, 10);
        panel.add(lblErrorEmailIng, gbc);
        gbc.insets = new Insets(5, 10, 0, 10);

        // COLUMNA DERECHA

        fila = 0;

        // Ciudad
        gbc.gridx = 2;
        gbc.gridy = fila;
        gbc.insets = new Insets(5, 10, 0, 10);
        JLabel lblCiudadIng = new JLabel(CargadorProperties.obtenerComponentes("label.ciudad"));
        lblCiudadIng.setFont(FUENTE_LABEL);
        lblCiudadIng.setForeground(COLOR_TEXTO);
        panel.add(lblCiudadIng, gbc);

        gbc.gridx = 3;
        panel.add(comboCiudadIng, gbc);

        fila++;
        gbc.gridx = 3;
        gbc.gridy = fila;
        gbc.insets = new Insets(0, 10, 0, 10);
        panel.add(lblErrorCiudadIng, gbc);
        gbc.insets = new Insets(5, 10, 0, 10);

        fila++;

        // Direccion
        gbc.gridx = 2;
        gbc.gridy = fila;
        JLabel lblDireccionIng = new JLabel(CargadorProperties.obtenerComponentes("label.direccion"));
        lblDireccionIng.setFont(FUENTE_LABEL);
        lblDireccionIng.setForeground(COLOR_TEXTO);
        panel.add(lblDireccionIng, gbc);

        gbc.gridx = 3;
        panel.add(txtDireccionIng, gbc);

        fila++;
        gbc.gridx = 3;
        gbc.gridy = fila;
        gbc.insets = new Insets(0, 10, 0, 10);
        panel.add(lblErrorDireccionIng, gbc);

        // Botón Guardar
        fila++;
        gbc.gridx = 3;
        gbc.gridy = fila;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(10, 10, 0, 10);
        JButton btnGuardar = new JButton(CargadorProperties.obtenerComponentes("boton.guardar"));
        estilizarBotonPrimario(btnGuardar);
        btnGuardar.setPreferredSize(new Dimension(120, 35));
        btnGuardar.addActionListener(e -> guardarCliente());
        panel.add(btnGuardar, gbc);

        return panel;
    }

    private JPanel crearPanelModificar() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(COLOR_FONDO_CENTRAL);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 12, 0, 12);
        gbc.anchor = GridBagConstraints.WEST;

        // Coniguracion de campos
        txtCedulaMod = new JTextField();
        estilizarCampoTexto(txtCedulaMod);
        txtCedulaMod.setPreferredSize(new Dimension(350, 32));
        txtCedulaMod.setMinimumSize(new Dimension(350, 32));
        txtCedulaMod.setMaximumSize(new Dimension(350, 32));

        txtNombreMod = new JTextField();
        estilizarCampoTexto(txtNombreMod);
        txtNombreMod.setPreferredSize(new Dimension(350, 32));
        txtNombreMod.setMinimumSize(new Dimension(350, 32));
        txtNombreMod.setMaximumSize(new Dimension(350, 32));

        txtTelefonoMod = new JTextField();
        estilizarCampoTexto(txtTelefonoMod);
        txtTelefonoMod.setPreferredSize(new Dimension(350, 32));
        txtTelefonoMod.setMinimumSize(new Dimension(350, 32));
        txtTelefonoMod.setMaximumSize(new Dimension(350, 32));

        txtEmailMod = new JTextField();
        estilizarCampoTexto(txtEmailMod);
        txtEmailMod.setPreferredSize(new Dimension(350, 32));
        txtEmailMod.setMinimumSize(new Dimension(350, 32));
        txtEmailMod.setMaximumSize(new Dimension(350, 32));

        comboCiudadMod = new JComboBox<>();
        estilizarComboBox(comboCiudadMod);
        comboCiudadMod.setPreferredSize(new Dimension(350, 32));
        comboCiudadMod.setMinimumSize(new Dimension(350, 32));
        comboCiudadMod.setMaximumSize(new Dimension(350, 32));

        txtDireccionMod = new JTextField();
        estilizarCampoTexto(txtDireccionMod);
        txtDireccionMod.setPreferredSize(new Dimension(350, 32));
        txtDireccionMod.setMinimumSize(new Dimension(350, 32));
        txtDireccionMod.setMaximumSize(new Dimension(350, 32));

        // Labels de error
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

        // Cargar ciudades y aplicar validaciones
        cargarCiudades(comboCiudadMod);
        configurarValidacionesModificar();

        // Deshabilitar campos inicialmente
        txtNombreMod.setEnabled(false);
        txtTelefonoMod.setEnabled(false);
        txtEmailMod.setEnabled(false);
        comboCiudadMod.setEnabled(false);
        txtDireccionMod.setEnabled(false);

        // Configurar búsqueda por cédula
        txtCedulaMod.addActionListener(e -> buscarClienteModificar());

        // COLUMNA IZQUIERDA
        int fila = 0;

        // Cedula o RUC
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.gridwidth = 1;
        JLabel lblCedulaMod = new JLabel(CargadorProperties.obtenerComponentes("label.cedula.ruc"));
        lblCedulaMod.setFont(FUENTE_LABEL);
        lblCedulaMod.setForeground(COLOR_TEXTO);
        panel.add(lblCedulaMod, gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 1;
        panel.add(txtCedulaMod, gbc);

        gbc.gridx = 2;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(8, 0, 0, 12);
        JLabel lblLupa = new JLabel(CargadorProperties.obtenerComponentes("emoji.lupa"));
        lblLupa.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        lblLupa.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lblLupa.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                buscarClienteModificar();
            }
        });
        panel.add(lblLupa, gbc);
        gbc.insets = new Insets(8, 12, 0, 12);

        fila++;
        gbc.gridx = 1;
        gbc.gridy = fila;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(0, 12, 0, 12);
        panel.add(lblErrorCedulaMod, gbc);
        gbc.insets = new Insets(8, 12, 0, 12);

        fila++;

        // Nombre
        gbc.gridx = 0;
        gbc.gridy = fila;
        JLabel lblNombreMod = new JLabel(CargadorProperties.obtenerComponentes("label.nombre"));
        lblNombreMod.setFont(FUENTE_LABEL);
        lblNombreMod.setForeground(COLOR_TEXTO);
        panel.add(lblNombreMod, gbc);
        gbc.gridx = 1;
        
        panel.add(txtNombreMod, gbc);

        fila++;
        gbc.gridx = 1;
        gbc.gridy = fila;
        gbc.insets = new Insets(0, 10, 0, 10);
        panel.add(lblErrorNombreMod, gbc);
        gbc.insets = new Insets(5, 10, 0, 10);

        fila++;

        // Telefono
        gbc.gridx = 0;
        gbc.gridy = fila;
        JLabel lblTelefonoMod = new JLabel(CargadorProperties.obtenerComponentes("label.telefono"));
        lblTelefonoMod.setFont(FUENTE_LABEL);
        lblTelefonoMod.setForeground(COLOR_TEXTO);
        panel.add(lblTelefonoMod, gbc);

        gbc.gridx = 1;
        panel.add(txtTelefonoMod, gbc);

        fila++;
        gbc.gridx = 1;
        gbc.gridy = fila;
        gbc.insets = new Insets(0, 10, 0, 10);
        panel.add(lblErrorTelefonoMod, gbc);
        gbc.insets = new Insets(5, 10, 0, 10);

        fila++;

        // Email
        gbc.gridx = 0;
        gbc.gridy = fila;
        JLabel lblEmailMod = new JLabel(CargadorProperties.obtenerComponentes("label.email"));
        lblEmailMod.setFont(FUENTE_LABEL);
        lblEmailMod.setForeground(COLOR_TEXTO);
        panel.add(lblEmailMod, gbc);

        gbc.gridx = 1;
        panel.add(txtEmailMod, gbc);

        fila++;
        gbc.gridx = 1;
        gbc.gridy = fila;
        gbc.insets = new Insets(0, 10, 0, 10);
        panel.add(lblErrorEmailMod, gbc);
        gbc.insets = new Insets(5, 10, 0, 10);

        // COLUMNA DERECHA
        // Ciudad
        fila = 2;

        gbc.gridx = 2;
        gbc.gridy = fila;
        gbc.insets = new Insets(5, 10, 0, 10);
        JLabel lblCiudadMod = new JLabel(CargadorProperties.obtenerComponentes("label.ciudad"));
        lblCiudadMod.setFont(FUENTE_LABEL);
        lblCiudadMod.setForeground(COLOR_TEXTO);
        panel.add(lblCiudadMod, gbc);

        gbc.gridx = 3;
        panel.add(comboCiudadMod, gbc);

        fila++;
        gbc.gridx = 3;
        gbc.gridy = fila;
        gbc.insets = new Insets(0, 10, 0, 10);
        panel.add(lblErrorCiudadMod, gbc);
        gbc.insets = new Insets(5, 10, 0, 10);

        fila++;

        // Direccion
        gbc.gridx = 2;
        gbc.gridy = fila;
        JLabel lblDireccionMod = new JLabel(CargadorProperties.obtenerComponentes("label.direccion"));
        lblDireccionMod.setFont(FUENTE_LABEL);
        lblDireccionMod.setForeground(COLOR_TEXTO);
        panel.add(lblDireccionMod, gbc);

        gbc.gridx = 3;
        panel.add(txtDireccionMod, gbc);

        fila++;
        gbc.gridx = 3;
        gbc.gridy = fila;
        gbc.insets = new Insets(0, 10, 0, 10);
        panel.add(lblErrorDireccionMod, gbc);

        // Botón Guardar
        fila++;
        gbc.gridx = 3;
        gbc.gridy = fila;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(10, 10, 10, 10);
        btnGuardarMod = new JButton(CargadorProperties.obtenerComponentes("boton.guardar"));
        btnGuardarMod.setPreferredSize(new Dimension(120, 35));
        estilizarBotonPrimario(btnGuardarMod);
        
        btnGuardarMod.setEnabled(false);
        btnGuardarMod.addActionListener(e -> modificarCliente());
        panel.add(btnGuardarMod, gbc);

        return panel;
    }

    private JPanel crearPanelEliminar() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(COLOR_FONDO_CENTRAL);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 12, 15, 12);
        gbc.anchor = GridBagConstraints.WEST;

        // Configurar Campos
        txtCedulaElim = new JTextField();
        estilizarCampoTexto(txtCedulaElim);
        txtCedulaElim.setPreferredSize(new Dimension(350, 32));
        txtCedulaElim.setMinimumSize(new Dimension(350, 32));
        txtCedulaElim.setMaximumSize(new Dimension(350, 32));

        txtNombreElim = new JTextField();
        estilizarCampoTexto(txtNombreElim);
        txtNombreElim.setPreferredSize(new Dimension(350, 32));
        txtNombreElim.setMinimumSize(new Dimension(350, 32));
        txtNombreElim.setMaximumSize(new Dimension(350, 32));

        txtTelefonoElim = new JTextField();
        estilizarCampoTexto(txtTelefonoElim);
        txtTelefonoElim.setPreferredSize(new Dimension(350, 32));
        txtTelefonoElim.setMinimumSize(new Dimension(350, 32));
        txtTelefonoElim.setMaximumSize(new Dimension(350, 32));

        txtEmailElim = new JTextField();
        estilizarCampoTexto(txtEmailElim);
        txtEmailElim.setPreferredSize(new Dimension(350, 32));
        txtEmailElim.setMinimumSize(new Dimension(350, 32));
        txtEmailElim.setMaximumSize(new Dimension(350, 32));

        comboCiudadElim = new JComboBox<>();
        estilizarComboBox(comboCiudadElim);
        comboCiudadElim.setPreferredSize(new Dimension(350, 32));
        comboCiudadElim.setMinimumSize(new Dimension(350, 32));
        comboCiudadElim.setMaximumSize(new Dimension(350, 32));

        txtDireccionElim = new JTextField();
        estilizarCampoTexto(txtDireccionElim);
        txtDireccionElim.setPreferredSize(new Dimension(350, 32));
        txtDireccionElim.setMinimumSize(new Dimension(350, 32));
        txtDireccionElim.setMaximumSize(new Dimension(350, 32));

        // Cargar ciudades
        cargarCiudades(comboCiudadElim);

        // Deshabilitar todos los campos
        txtNombreElim.setEnabled(false);
        txtTelefonoElim.setEnabled(false);
        txtEmailElim.setEnabled(false);
        comboCiudadElim.setEnabled(false);
        txtDireccionElim.setEnabled(false);

        // Configurar búsqueda por cédula
        txtCedulaElim.addActionListener(e -> buscarClienteEliminar());

        int fila = 0;

        // COLUMNA IZQUIERDA
        // Cédula
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.gridwidth = 1;
        JLabel lblCedulaEli = new JLabel(CargadorProperties.obtenerComponentes("label.cedula.ruc"));
        lblCedulaEli.setFont(FUENTE_LABEL);
        lblCedulaEli.setForeground(COLOR_TEXTO);
        panel.add(lblCedulaEli, gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 1;
        panel.add(txtCedulaElim, gbc);

        gbc.gridx = 2;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(8, 0, 15, 12);
        JLabel lblLupa = new JLabel(CargadorProperties.obtenerComponentes("emoji.lupa"));
        lblLupa.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        lblLupa.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lblLupa.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                buscarClienteEliminar();
            }
        });
        panel.add(lblLupa, gbc);
        gbc.insets = new Insets(8, 12, 15, 12);

        fila++;

        // Nombre
        gbc.gridx = 0;
        gbc.gridy = fila;
        JLabel lblNombreEli = new JLabel(CargadorProperties.obtenerComponentes("label.nombre"));
        lblNombreEli.setFont(FUENTE_LABEL);
        lblNombreEli.setForeground(COLOR_TEXTO);
        panel.add(lblNombreEli, gbc);

        gbc.gridx = 1;
        panel.add(txtNombreElim, gbc);

        fila++;

        // Telefono
        gbc.gridx = 0;
        gbc.gridy = fila;
        JLabel lblTelefonoEli = new JLabel(CargadorProperties.obtenerComponentes("label.telefono"));
        lblTelefonoEli.setFont(FUENTE_LABEL);
        lblTelefonoEli.setForeground(COLOR_TEXTO);
        panel.add(lblTelefonoEli, gbc);

        gbc.gridx = 1;
        panel.add(txtTelefonoElim, gbc);

        fila++;

        // email
        gbc.gridx = 0;
        gbc.gridy = fila;
        JLabel lblEmailEli = new JLabel(CargadorProperties.obtenerComponentes("label.email"));
        lblEmailEli.setFont(FUENTE_LABEL);
        lblEmailEli.setForeground(COLOR_TEXTO);
        panel.add(lblEmailEli, gbc);

        gbc.gridx = 1;
        panel.add(txtEmailElim, gbc);

        // COLUMNA DERECHA
        fila = 1;

        // Ciudad
        gbc.gridx = 2;
        gbc.gridy = fila;
        gbc.insets = new Insets(5, 10, 0, 10);
        JLabel lblCiudadEli = new JLabel(CargadorProperties.obtenerComponentes("label.ciudad"));
        lblCiudadEli.setFont(FUENTE_LABEL);
        lblCiudadEli.setForeground(COLOR_TEXTO);
        panel.add(lblCiudadEli, gbc);

        gbc.gridx = 3;
        panel.add(comboCiudadElim, gbc);

        fila++;

        // Direccion
        gbc.gridx = 2;
        gbc.gridy = fila;
        JLabel lblDireccionEli = new JLabel(CargadorProperties.obtenerComponentes("label.direccion"));
        lblDireccionEli.setFont(FUENTE_LABEL);
        lblDireccionEli.setForeground(COLOR_TEXTO);
        panel.add(lblDireccionEli, gbc);

        gbc.gridx = 3;
        panel.add(txtDireccionElim, gbc);

        // Botón Eliminar
        fila++;
        gbc.gridx = 3;
        gbc.gridy = fila;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(10, 10, 0, 10);
        btnEliminar = new JButton(CargadorProperties.obtenerComponentes("boton.eliminar"));
        btnEliminar.setPreferredSize(new Dimension(120, 35));
        estilizarBotonEliminar(btnEliminar);
        btnEliminar.setEnabled(false);
        btnEliminar.addActionListener(e -> eliminarCliente());
        panel.add(btnEliminar, gbc);

        return panel;
    }

    private JPanel crearPanelConsultar() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(COLOR_FONDO_CENTRAL);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel panelTipo = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        panelTipo.setBackground(COLOR_FONDO_CENTRAL);
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

        JPanel panelCentral = new JPanel(new BorderLayout(5, 5));
        panelCentral.setBackground(COLOR_FONDO_CENTRAL);

        // Configurar elementos de busqueda
        panelBusqueda = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        panelBusqueda.setBackground(COLOR_FONDO_CENTRAL);
        txtBusqueda = new JTextField(20);
        estilizarCampoTexto(txtBusqueda);
        txtBusqueda.setPreferredSize(new Dimension(250, 30));
        txtBusqueda.setToolTipText(CargadorProperties.obtenerComponentes("tooltip.busqueda"));
        comboParametroBusqueda = new JComboBox<>(new String[] {
                CargadorProperties.obtenerComponentes("combo.param.nombre"),
                CargadorProperties.obtenerComponentes("combo.param.cedula.ruc"),
                CargadorProperties.obtenerComponentes("combo.param.telefono"),
                CargadorProperties.obtenerComponentes("combo.param.email"),
                CargadorProperties.obtenerComponentes("combo.param.ciudad.desc"),
                CargadorProperties.obtenerComponentes("combo.param.direccion")
        });
        estilizarComboBox(comboParametroBusqueda);
        comboParametroBusqueda.setPreferredSize(new Dimension(150, 30));

        JLabel lblBuscar = new JLabel(CargadorProperties.obtenerComponentes("label.buscar"));
        lblBuscar.setFont(FUENTE_LABEL);
        lblBuscar.setForeground(COLOR_TEXTO);
        panelBusqueda.add(lblBuscar);
        panelBusqueda.add(txtBusqueda);
        panelBusqueda.add(comboParametroBusqueda);
        JLabel lblLupaBusq = new JLabel(CargadorProperties.obtenerComponentes("emoji.lupa"));
        lblLupaBusq.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        lblLupaBusq.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lblLupaBusq.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                realizarBusqueda();
            }
        });
        panelBusqueda.add(lblLupaBusq);
        panelBusqueda.setVisible(false);

        panelCentral.add(panelBusqueda, BorderLayout.NORTH);

        // Configuracion tabla
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
        tablaResultados.setFont(FUENTE_BASE);
        tablaResultados.setForeground(COLOR_TEXTO_CAMPO);

        // Estilizar header de la tabla
        tablaResultados.getTableHeader().setFont(new Font("Poppins", Font.BOLD, 13));
        tablaResultados.getTableHeader().setBackground(COLOR_ENFASIS); // #ffa41c
        tablaResultados.getTableHeader().setForeground(COLOR_TEXTO); // #4C57A9
        tablaResultados.getTableHeader().setOpaque(true);

        scrollTabla = new JScrollPane(tablaResultados);
        scrollTabla.setVisible(false);

        panelCentral.add(scrollTabla, BorderLayout.CENTER);

        panel.add(panelCentral, BorderLayout.CENTER);

        configurarBusquedaTiempoReal();

        return panel;
    }

    // Errores en rojo
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

    // Validaciones para el ingreso
    private void configurarValidacionesIngresar() {
        txtCedulaIng.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                validarCedulaIng();
            }

            public void removeUpdate(DocumentEvent e) {
                validarCedulaIng();
            }

            public void changedUpdate(DocumentEvent e) {
                validarCedulaIng();
            }
        });

        txtNombreIng.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                validarNombreIng();
            }

            public void removeUpdate(DocumentEvent e) {
                validarNombreIng();
            }

            public void changedUpdate(DocumentEvent e) {
                validarNombreIng();
            }
        });

        txtTelefonoIng.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                validarTelefonoIng();
            }

            public void removeUpdate(DocumentEvent e) {
                validarTelefonoIng();
            }

            public void changedUpdate(DocumentEvent e) {
                validarTelefonoIng();
            }
        });

        txtEmailIng.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                validarEmailIng();
            }

            public void removeUpdate(DocumentEvent e) {
                validarEmailIng();
            }

            public void changedUpdate(DocumentEvent e) {
                validarEmailIng();
            }
        });

        comboCiudadIng.addActionListener(e -> validarCiudadIng());

        txtDireccionIng.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                validarDireccionIng();
            }

            public void removeUpdate(DocumentEvent e) {
                validarDireccionIng();
            }

            public void changedUpdate(DocumentEvent e) {
                validarDireccionIng();
            }
        });
    }

    // Validaciones en tiempo real
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

    private void mostrarError(JLabel label, String mensaje) {
        if (mensaje != null) {
            label.setText(mensaje);
        } else {
            label.setText(" ");
        }
    }

    // Validaciones para la modificacion
    private void configurarValidacionesModificar() {

        txtCedulaMod.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                validarCedulaMod();
            }

            public void removeUpdate(DocumentEvent e) {
                validarCedulaMod();
            }

            public void changedUpdate(DocumentEvent e) {
                validarCedulaMod();
            }
        });

        txtNombreMod.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                validarNombreMod();
            }

            public void removeUpdate(DocumentEvent e) {
                validarNombreMod();
            }

            public void changedUpdate(DocumentEvent e) {
                validarNombreMod();
            }
        });

        txtTelefonoMod.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                validarTelefonoMod();
            }

            public void removeUpdate(DocumentEvent e) {
                validarTelefonoMod();
            }

            public void changedUpdate(DocumentEvent e) {
                validarTelefonoMod();
            }
        });

        txtEmailMod.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                validarEmailMod();
            }

            public void removeUpdate(DocumentEvent e) {
                validarEmailMod();
            }

            public void changedUpdate(DocumentEvent e) {
                validarEmailMod();
            }
        });

        comboCiudadMod.addActionListener(e -> validarCiudadMod());

        txtDireccionMod.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                validarDireccionMod();
            }

            public void removeUpdate(DocumentEvent e) {
                validarDireccionMod();
            }

            public void changedUpdate(DocumentEvent e) {
                validarDireccionMod();
            }
        });
    }

    // Validaciones en tiempo real
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

    // Carga de ComboBox
    private void cargarCiudades(JComboBox<ItemCombo> combo) {
        combo.removeAllItems();
        combo.addItem(new ItemCombo("", CargadorProperties.obtenerComponentes("combo.ciudad.seleccione")));

        Cliente cli = new Cliente();
        ArrayList<ItemCombo> ciudades = cli.obtenerCiudadesDP();

        for (ItemCombo ciudad : ciudades) {
            combo.addItem(ciudad);
        }
    }

    // Metodo para cargar el panel segun la elección
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

    // Vuelve al panel vacío
    private void volverAlMenu() {
        this.dispose();
        new MenuPrincipal().setVisible(true);
    }

    // Caso especial de carga para reactivaion de un cliente
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

    // Metodos CRUD
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
            String mensaje = !txtCedulaIng.isEnabled() ? CargadorProperties.obtenerMessages("CL_I_007")
                    : CargadorProperties.obtenerMessages("CL_I_001");

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
                CargadorProperties.obtenerMessages("CL_C_001") + txtNombreElim.getText()
                        + CargadorProperties.obtenerMessages("CL_C_002"),
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
            modeloTabla.addRow(new Object[] {
                    c.getCedRuc(),
                    c.getNombre(),
                    c.getTelefono(),
                    c.getEmail(),
                    c.getCiudad(),
                    c.getDireccion()
            });
        }
    }

    // Busqueda en tiempo real para la consulta por parámetro
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
            modeloTabla.addRow(new Object[] {
                    c.getCedRuc(),
                    c.getNombre(),
                    c.getTelefono(),
                    c.getEmail(),
                    c.getCiudad(),
                    c.getDireccion()
            });
        }
    }

    // Métods para limpiar campos
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
