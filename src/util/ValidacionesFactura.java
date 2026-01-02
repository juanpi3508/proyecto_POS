package util;

import DP.Cliente;
import DP.Factura;
import DP.Producto;

public class ValidacionesFactura {
    
    //Valida la cédula o RUC del cliente (debe existir en BD)
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
    

    
    //Valida la cantidad de producto
    public static String validarCantidad(int cantidad) {
        if (cantidad <= 0) {
            return CargadorProperties.obtenerMessages("FC_A_003");
        }
        
        return null;
    }
    
    //Valida la cantidad de producto (sobrecarga para String)
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
}
