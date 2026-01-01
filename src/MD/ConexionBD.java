/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MD;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import util.CargadorProperties;

/**
 *
 * @author danab
 */

public class ConexionBD {
    private static Connection conexion = null;
    
    // Constructor privado para Singleton
    private ConexionBD() {}
    
    public static Connection getConexion() {
        if (conexion == null) {
            try {
                // Cargar credenciales desde properties
                String url = CargadorProperties.obtenerConfigBD("db.url");
                String usuario = CargadorProperties.obtenerConfigBD("db.usuario");
                String password = CargadorProperties.obtenerConfigBD("db.password");
                String driver = CargadorProperties.obtenerConfigBD("db.driver");
                
                // Cargar el driver de PostgreSQL
                Class.forName(driver);
                
                // Establecer conexión
                conexion = DriverManager.getConnection(url, usuario, password);
                
                System.out.println("Conexión exitosa a la base de datos");
                
            } catch (ClassNotFoundException e) {
                System.err.println("Error: Driver de PostgreSQL no encontrado");
                e.printStackTrace();
            } catch (SQLException e) {
                System.err.println("Error al conectar con la base de datos");
                e.printStackTrace();
            }
        }
        return conexion;
    }

    public static void cerrarConexion() {
        if (conexion != null) {
            try {
                conexion.close();
                conexion = null;
                System.out.println("Conexión cerrada correctamente");
            } catch (SQLException e) {
                System.err.println("Error al cerrar la conexión");
                e.printStackTrace();
            }
        }
    }
}