package util;

import DP.Proveedor;

public class ValidacionesProveedor {


    public static String validarCedula(String cedRuc, boolean esModificar) {
    if (cedRuc == null || cedRuc.trim().isEmpty()) {
        return CargadorProperties.obtenerMessages("PV_A_004"); // obligatorio
    }

    if (!cedRuc.matches("\\d{10}|\\d{13}")) {
        return CargadorProperties.obtenerMessages("PV_A_005"); // 10 o 13 dígitos
    }

 
    if (cedRuc.length() == 13 && !cedRuc.endsWith("001")) {
        return CargadorProperties.obtenerMessages("PV_A_015");
    }

  
    if (!esModificar) {
        Proveedor prv = new Proveedor();
        Proveedor existe = prv.verificarDP(cedRuc);

        if (existe != null) {
            if ("INA".equals(existe.getEstado())) {
            
                return "PROVEEDOR_INACTIVO";
            } else {
                // Ya existe y está activo
                return CargadorProperties.obtenerMessages("PV_A_001");
            }
        }
    }

    return null;
}

    public static String validarNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return CargadorProperties.obtenerMessages("PV_A_006");
        }
        if (!nombre.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+")) {
            return CargadorProperties.obtenerMessages("PV_A_006");
        }
        if (nombre.trim().length() < 2 || nombre.trim().length() > 40) {
            return CargadorProperties.obtenerMessages("PV_A_007");
        }
        return null;
    }

    public static String validarCelular(String celular) {
        if (celular == null || celular.trim().isEmpty()) {
            return CargadorProperties.obtenerMessages("PV_A_008"); // obligatorio
        }

        if (!celular.matches("\\d{10}")) {
            return CargadorProperties.obtenerMessages("PV_A_016"); // exactamente 10 dígitos
        }

        if (!celular.startsWith("09")) {
            return CargadorProperties.obtenerMessages("PV_A_014"); // debe iniciar con 09
        }

        return null;
    }

  
    public static String validarTelefono(String telefono) {
        if (telefono == null || telefono.trim().isEmpty()) {
            return CargadorProperties.obtenerMessages("PV_A_009"); // obligatorio
        }

        if (!telefono.matches("\\d{9}")) {
            return CargadorProperties.obtenerMessages("PV_A_017"); // exactamente 10 dígitos
        }

        String prefijo = telefono.substring(0, 2);
        if (!prefijo.matches("02|03|04|05|06|07")) {
            return CargadorProperties.obtenerMessages("PV_A_013"); // código provincia inválido
        }

        return null;
    }

    public static String validarEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return CargadorProperties.obtenerMessages("PV_A_010");
        }
        if (!email.contains("@") || !email.contains(".")) {
            return CargadorProperties.obtenerMessages("PV_A_010");
        }
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            return CargadorProperties.obtenerMessages("PV_A_010");
        }
        if (email.length() > 50) {
            return CargadorProperties.obtenerMessages("PV_A_010");
        }
        return null;
    }

    public static String validarCiudad(String ciudad) {
        if (ciudad == null || ciudad.trim().isEmpty()) {
            return CargadorProperties.obtenerMessages("PV_A_011");
        }
        return null;
    }

    public static String validarDireccion(String direccion) {
        if (direccion == null || direccion.trim().isEmpty()) {
            return CargadorProperties.obtenerMessages("PV_A_012");
        }
        if (direccion.trim().length() < 2 || direccion.trim().length() > 60) {
            return CargadorProperties.obtenerMessages("PV_A_012");
        }
        return null;
    }

    public static boolean validarTodo(String cedRuc,
                                      String nombre,
                                      String telefono,
                                      String celular,
                                      String email,
                                      String ciudad,
                                      String direccion,
                                      boolean esModificar) {
        String errCed = validarCedula(cedRuc, esModificar);
       
        if ("PROVEEDOR_INACTIVO".equals(errCed)) {
            return false;
        }

        return errCed == null &&
               validarNombre(nombre) == null &&
               validarTelefono(celular) == null &&
               validarCelular(celular) == null &&
               validarEmail(email) == null &&
               validarCiudad(ciudad) == null &&
               validarDireccion(direccion) == null;
    }
}
