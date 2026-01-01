package util;

import DP.Cliente;
import DP.Factura;
import DP.Producto;

public class ValidacionesFactura {
    
    /**
     * Valida la cédula o RUC del cliente (debe existir en BD)
     */
    public static String validarCedRucCliente(String cedRuc) {
        if (cedRuc == null || cedRuc.trim().isEmpty()) {
            return CargadorProperties.obtenerMessages("FC_A_009");
        }
        
        // Debe ser 10 dígitos (cédula) o 13 (RUC)
        if (!cedRuc.matches("\\d{10}|\\d{13}")) {
            return CargadorProperties.obtenerMessages("FC_E_001");
        }
        
        // Verificar que el cliente exista
        Cliente cli = new Cliente();
        Cliente existe = cli.verificarDP(cedRuc);
        if (existe == null) {
            return CargadorProperties.obtenerMessages("FC_A_009");
        }
        
        return null;
    }
    
    /**
     * Valida el tipo de factura
     */
    public static String validarTipo(String tipo) {
        if (tipo == null || tipo.trim().isEmpty()) {
            return CargadorProperties.obtenerMessages("FC_A_011"); // ← NUEVO
        }
        
        // Solo "POS" o "ECO"
        if (!tipo.equals("POS") && !tipo.equals("ECO")) {
            return CargadorProperties.obtenerMessages("FC_A_011"); // ← NUEVO
        }
        
        return null;
    }
    
    /**
     * Valida que la factura tenga al menos un producto
     */
    public static String validarProductos(Factura factura) {
        if (factura.getProductos() == null || factura.getProductos().isEmpty()) {
            return CargadorProperties.obtenerMessages("FC_A_005"); // ← YA LO TIENES
        }
        
        return null;
    }
    
    /**
     * Valida el código de producto (debe existir)
     */
    public static String validarCodigoProducto(String codigoProd) {
        if (codigoProd == null || codigoProd.trim().isEmpty()) {
            return CargadorProperties.obtenerMessages("FC_A_010"); // ← NUEVO
        }
        
        // Verificar que el producto exista
        Producto prod = new Producto();
        Producto existe = prod.verificarDP(codigoProd);
        if (existe == null) {
            return CargadorProperties.obtenerMessages("FC_A_010"); // ← NUEVO
        }
        
        return null;
    }
    
    /**
     * Valida la cantidad de producto
     */
    public static String validarCantidad(int cantidad) {
        if (cantidad <= 0) {
            return CargadorProperties.obtenerMessages("FC_A_003"); // ← YA LO TIENES
        }
        
        return null;
    }
    
    /**
     * Valida la cantidad de producto (sobrecarga para String)
     */
    public static String validarCantidad(String cantidadStr) {
        if (cantidadStr == null || cantidadStr.trim().isEmpty()) {
            return CargadorProperties.obtenerMessages("FC_A_003");
        }
        
        try {
            int cantidad = Integer.parseInt(cantidadStr);
            return validarCantidad(cantidad);
        } catch (NumberFormatException e) {
            return CargadorProperties.obtenerMessages("FC_A_003");
        }
    }
    
    /**
     * Valida todos los campos de una factura
     */
    public static boolean validarFacturaCompleta(Factura factura) {
        return validarCedRucCliente(factura.getCedRucCliente()) == null &&
               validarTipo(factura.getTipo()) == null &&
               validarProductos(factura) == null;
    }
    
    /**
     * Valida un producto individual para agregar a la factura
     */
    public static boolean validarProductoParaAgregar(String codigoProd, int cantidad) {
        return validarCodigoProducto(codigoProd) == null &&
               validarCantidad(cantidad) == null;
    }
}