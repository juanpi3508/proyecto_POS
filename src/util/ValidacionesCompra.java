package util;

import DP.Proveedor;

public class ValidacionesCompra {

    // Valida RUC del proveedor (debe existir en BD)
    public static String validarRucProveedor(String ruc) {
        if (ruc == null || ruc.trim().isEmpty()) {
            return CargadorProperties.obtenerMessages("CP_A_009"); // Debe buscar un proveedor primero
        }

        if (!ruc.matches("\\d{10}|\\d{13}")) {
            return CargadorProperties.obtenerMessages("CP_E_001"); // Ruc inválido
        }

        Proveedor prov = new Proveedor();
        Proveedor existe = prov.verificarDP(ruc);
        if (existe == null) {
            return CargadorProperties.obtenerMessages("CP_PROVEEDOR_NO_ENCONTRADO"); // O una advertencia específica "C_A_009"
        }

        return null; // Null significa válido
    }

    // Valida la cantidad de producto
    public static String validarCantidad(int cantidad) {
        if (cantidad <= 0) {
            return CargadorProperties.obtenerMessages("CP_A_012"); // Debe ingresar numero positivo
        }
        return null;
    }

    // Valida la cantidad de producto (sobrecarga para String)
    public static String validarCantidad(String cantidadStr) {
        if (cantidadStr == null || cantidadStr.trim().isEmpty()) {
            return CargadorProperties.obtenerMessages("CP_A_011"); // Ingrese la cantidad
        }

        try {
            int cantidad = Integer.parseInt(cantidadStr);
            return validarCantidad(cantidad);
        } catch (NumberFormatException e) {
            return CargadorProperties.obtenerMessages("CP_A_008"); // Solo numeros enteros
        }
    }

    // Validar RUC en tiempo real (permisivo para input)
    public static String validarRucInput(String texto) {
        if (!texto.matches("\\d*")) {
            return CargadorProperties.obtenerMessages("CP_A_008"); // "Solo números enteros" (reusing message or finding better one)
        }
        if (texto.length() > 13) {
            return CargadorProperties.obtenerMessages("PV_A_005"); // "10 o 13 digitos" warning from Proveedor
        }
        // No validamos existencia aquí, solo formato básico mientras escribe
        return null;
    }
}
