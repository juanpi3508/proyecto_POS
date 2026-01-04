package util;

import DP.Producto;

public class ValidacionesProducto {
    
    public static String validarCodigo(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) {
            return CargadorProperties.obtenerMessages("PD_A_004");
        }
        
        if (!codigo.matches("^[A-Z]{3}-\\d{4}$")) {
            return CargadorProperties.obtenerMessages("PD_A_005");
        }
        
        return null;
    }
    
    public static String validarDescripcion(String descripcion, boolean esModificar) {
        if (descripcion == null || descripcion.trim().isEmpty()) {
            return CargadorProperties.obtenerMessages("PD_A_006");
        }
        
        if (descripcion.trim().length() < 2 || descripcion.trim().length() > 60) {
            return CargadorProperties.obtenerMessages("PD_A_006");
        }
        
        // Verificar si ya existe (solo en nuevo producto)
        if (!esModificar) {
            Producto pro = new Producto();
            Producto existe = pro.verificarPorNombreDP(descripcion.trim());
            if (existe != null) {
                return CargadorProperties.obtenerMessages("PD_A_001");
            }
        }
        
        return null;
    }
    
    public static String validarCategoria(String idCategoria) {
        if (idCategoria == null || idCategoria.trim().isEmpty()) {
            return CargadorProperties.obtenerMessages("PD_A_007");
        }
        
        return null;
    }
    
    public static String validarUnidadMedida(String idUnidad, String tipo) {
        if (idUnidad == null || idUnidad.trim().isEmpty()) {
            return CargadorProperties.obtenerMessages("PD_A_008");
        }
        
        return null;
    }
    
    public static String validarPrecioCompra(String precioStr) {
        if (precioStr == null || precioStr.trim().isEmpty()) {
            return CargadorProperties.obtenerMessages("PD_A_009");
        }
        
        try {
            double precio = Double.parseDouble(precioStr.trim());
            
            if (precio < 0) {
                return CargadorProperties.obtenerMessages("PD_A_009");
            }
            
            String[] partes = precioStr.trim().split("\\.");
            if (partes.length > 1 && partes[1].length() > 2) {
                return CargadorProperties.obtenerMessages("PD_A_009");
            }
            
        } catch (NumberFormatException e) {
            return CargadorProperties.obtenerMessages("PD_A_009");
        }
        
        return null;
    }
    
    public static String validarPrecioVenta(String precioVentaStr, String precioCompraStr) {
        if (precioVentaStr == null || precioVentaStr.trim().isEmpty()) {
            return CargadorProperties.obtenerMessages("PD_A_009");
        }
        
        try {
            double precioVenta = Double.parseDouble(precioVentaStr.trim());
            
            if (precioVenta < 0) {
                return CargadorProperties.obtenerMessages("PD_A_009");
            }
            
            String[] partes = precioVentaStr.trim().split("\\.");
            if (partes.length > 1 && partes[1].length() > 2) {
                return CargadorProperties.obtenerMessages("PD_A_009");
            }
            
            if (precioCompraStr != null && !precioCompraStr.trim().isEmpty()) {
                double precioCompra = Double.parseDouble(precioCompraStr.trim());
                if (precioVenta < precioCompra) {
                    return CargadorProperties.obtenerMessages("PD_A_010");
                }
            }
            
        } catch (NumberFormatException e) {
            return CargadorProperties.obtenerMessages("PD_A_009");
        }
        
        return null;
    }
    
    public static String validarImagen(String imagen) {
        if (imagen == null || imagen.trim().isEmpty()) {
            return "La imagen es obligatoria.";
        }
        
        if (imagen.trim().length() < 2 || imagen.trim().length() > 260) {
            return "La ruta de la imagen debe tener entre 2 y 260 caracteres.";
        }
        
        return null;
    }
    
    public static boolean validarTodoInsertar(String codigo, String descripcion, 
                                              String idCategoria, String idUmCompra, 
                                              String precioCompra, String idUmVenta, 
                                              String precioVenta, String imagen) {
        return validarCodigo(codigo) == null &&
               validarDescripcion(descripcion, false) == null &&
               validarCategoria(idCategoria) == null &&
               validarUnidadMedida(idUmCompra, "compra") == null &&
               validarPrecioCompra(precioCompra) == null &&
               validarUnidadMedida(idUmVenta, "venta") == null &&
               validarPrecioVenta(precioVenta, precioCompra) == null &&
               validarImagen(imagen) == null;
    }
    
    public static boolean validarTodoModificar(String descripcion, 
                                               String idUmCompra, String precioCompra, 
                                               String idUmVenta, String precioVenta) {
        return validarDescripcion(descripcion, true) == null &&
               validarUnidadMedida(idUmCompra, "compra") == null &&
               validarPrecioCompra(precioCompra) == null &&
               validarUnidadMedida(idUmVenta, "venta") == null &&
               validarPrecioVenta(precioVenta, precioCompra) == null;
    }
}
