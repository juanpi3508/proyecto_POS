package util;

import DP.Proveedor;

public class ValidacionesProveedor {

    /**
     * Devuelve:
     *  - null                             → todo bien
     *  - mensaje de error (PV_A_XXX)      → error de validación
     *  - "PROVEEDOR_INACTIVO"             → existe en BD con estado INA
     */
    public static String validarCedula(String cedRuc, boolean esModificar) {
    if (cedRuc == null || cedRuc.trim().isEmpty()) {
        return CargadorProperties.obtenerMessages("PV_A_004"); // obligatorio
    }

    if (!cedRuc.matches("\\d{10}|\\d{13}")) {
        return CargadorProperties.obtenerMessages("PV_A_005"); // 10 o 13 dígitos
    }

    // Si es RUC (13 dígitos), debe terminar en 001
    if (cedRuc.length() == 13 && !cedRuc.endsWith("001")) {
        return CargadorProperties.obtenerMessages("PV_A_015");
    }

    // SOLO en ingreso (no modificar) revisamos si ya existe
    if (!esModificar) {
        Proveedor prv = new Proveedor();
        Proveedor existe = prv.verificarDP(cedRuc);

        if (existe != null) {
            if ("INA".equals(existe.getEstado())) {
                // Proveedor encontrado pero INACTIVO: devolvemos un código especial
                // NO es mensaje para el usuario, es una "bandera" para la GUI
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

    /**
     * Teléfono (label) → celular ecuatoriano:
     * - Obligatorio
     * - Debe tener exactamente 10 dígitos numéricos
     * - Debe iniciar con 09
     */
    public static String validarTelefono(String telefono) {
        if (telefono == null || telefono.trim().isEmpty()) {
            return CargadorProperties.obtenerMessages("PV_A_008"); // obligatorio
        }

        if (!telefono.matches("\\d{10}")) {
            return CargadorProperties.obtenerMessages("PV_A_016"); // exactamente 10 dígitos
        }

        if (!telefono.startsWith("09")) {
            return CargadorProperties.obtenerMessages("PV_A_014"); // debe iniciar con 09
        }

        return null;
    }

    /**
     * Campo "celular" que usas como teléfono fijo por provincias:
     * - Obligatorio
     * - Debe tener exactamente 10 dígitos numéricos
     * - Debe iniciar con 02, 03, 04, 05, 06 o 07
     */
    public static String validarCelular(String celular) {
        if (celular == null || celular.trim().isEmpty()) {
            return CargadorProperties.obtenerMessages("PV_A_009"); // obligatorio
        }

        if (!celular.matches("\\d{10}")) {
            return CargadorProperties.obtenerMessages("PV_A_017"); // exactamente 10 dígitos
        }

        String prefijo = celular.substring(0, 2);
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
        // Para no aceptar inactivos como "válidos" en este helper
        if ("PROVEEDOR_INACTIVO".equals(errCed)) {
            return false;
        }

        return errCed == null &&
               validarNombre(nombre) == null &&
               validarTelefono(telefono) == null &&
               validarCelular(celular) == null &&
               validarEmail(email) == null &&
               validarCiudad(ciudad) == null &&
               validarDireccion(direccion) == null;
    }
}
