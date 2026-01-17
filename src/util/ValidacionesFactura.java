package util;

import DP.Cliente;

public class ValidacionesFactura {

    // Valida la cédula o RUC del cliente (debe existir en BD)
    public static String validarCedRucCliente(String cedRuc) {
        if (cedRuc == null || cedRuc.trim().isEmpty()) {
            return CargadorProperties.obtenerMessages("FC_A_009");
        }

        if (!cedRuc.matches("\\d{10}|\\d{13}")) {
            return CargadorProperties.obtenerMessages("FC_E_001");
        }

        Cliente cli = new Cliente();
        Cliente existe = cli.verificarDP(cedRuc);
        if (existe == null) {
            return CargadorProperties.obtenerMessages("FC_A_009");
        }

        return null;
    }

    // Valida la cantidad de producto
    public static String validarCantidad(int cantidad) {
        if (cantidad <= 0) {
            return CargadorProperties.obtenerMessages("FC_A_023");
        }

        return null;
    }

    // Valida la cantidad de producto (sobrecarga para String)
    public static String validarCantidad(String cantidadStr) {
        if (cantidadStr == null || cantidadStr.trim().isEmpty()) {
            return CargadorProperties.obtenerMessages("FC_A_023");
        }

        String textoLimpio = cantidadStr.trim();

        // Detectar números negativos específicamente
        if (textoLimpio.startsWith("-")) {
            return CargadorProperties.obtenerMessages("FC_A_023");
        }

        // Validar que solo contenga dígitos (sin puntos ni comas)
        if (!textoLimpio.matches("\\d+")) {
            return CargadorProperties.obtenerMessages("FC_A_011");
        }

        try {
            int cantidad = Integer.parseInt(textoLimpio);
            return validarCantidad(cantidad);
        } catch (NumberFormatException e) {
            // El número es demasiado grande para un int - no mostrar mensaje de error
            return null;
        }
    }

    // Validar cédula en tiempo real (permisivo)
    public static String validarCedulaInput(String texto) {
        if (!texto.matches("\\d*")) {
            return CargadorProperties.obtenerMessages("FC_A_011"); // "Solo números"
        }
        if (texto.length() > 13) {
            return CargadorProperties.obtenerMessages("CL_A_005"); // "Máximo 13 dígitos"
        }
        if (texto.length() == 10 || texto.length() == 13) {
            return validarCedRucCliente(texto);
        }
        return null;
    }

    // Validar stock disponible
    public static String validarStock(int cantidad, int stockDisponible) {
        if (cantidad > stockDisponible) {
            return String.format(CargadorProperties.obtenerMessages("FC_A_025"), stockDisponible);
        }
        return null;
    }

    // Validar stock acumulado (cuando ya hay cantidad en la factura)
    public static String validarStockAcumulado(int cantidadNueva, int cantidadExistente, int stockDisponible) {
        int total = cantidadNueva + cantidadExistente;
        if (total > stockDisponible) {
            return String.format(CargadorProperties.obtenerMessages("FC_A_025"), stockDisponible);
        }
        return null;
    }
}
