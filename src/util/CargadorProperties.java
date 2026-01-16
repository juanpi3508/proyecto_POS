package util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class CargadorProperties {
    private static Properties mensajes;
    private static Properties propiedadesCliente;
    private static Properties propiedadesFactura;
    private static Properties propiedadesProducto;
    private static Properties propiedadesBD;
    private static Properties propiedadesComponentes;
    private static Properties propiedadesProveedor;
    
    static {
        mensajes = cargar("resources/messages.properties");
        propiedadesCliente = cargar("resources/configCliente.properties");
        propiedadesFactura = cargar("resources/configFactura.properties");
        propiedadesProducto = cargar("resources/configProducto.properties");
        propiedadesProveedor = cargar("resources/configProveedor.properties");

        propiedadesBD = cargar("resources/configBD.properties");
        propiedadesComponentes = cargar("resources/componentes.properties");
    }
    
    private static Properties cargar(String archivo){
        Properties p = new Properties();
        try (InputStream input = CargadorProperties.class.getClassLoader()
                .getResourceAsStream(archivo)){
            if (input == null){
                System.err.println("No se pudo encontrar: " + archivo);
                return p;
            }
            p.load(input);
        } catch (IOException e){
            System.err.println("Error cargando " + archivo + ": " + e.getMessage());
        }
        return p;
    }
    
    public static String obtenerMessages (String clave) {
        return mensajes.getProperty(clave);
    }
    
    public static String obtenerConfigCliente (String clave) {
        return propiedadesCliente.getProperty(clave);
    }
    
    public static String obtenerConfigFactura (String clave) {
        return propiedadesFactura.getProperty(clave);
    }
    
    public static String obtenerConfigProducto (String clave) {
        return propiedadesProducto.getProperty(clave);
    }
    
     public static String obtenerConfigProveedor (String clave) {
        return propiedadesProveedor.getProperty(clave);
    }
    
    public static String obtenerConfigBD (String clave) {
        return propiedadesBD.getProperty(clave);
    }
    
    public static String obtenerComponentes (String clave) {
        return propiedadesComponentes.getProperty(clave);
    }
}
