package MD;

import DP.Cliente;
import DP.Factura;
import DP.ProxFac;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import util.CargadorProperties;
import java.sql.CallableStatement;
import java.sql.Types;

public class FacturaMD {
    private Connection conexion;
    
    public FacturaMD() {
        this.conexion = ConexionBD.getConexion();
    }
    
    /**
     * Inserta solo la cabecera de la factura (sin productos)
     * Retorna el código generado por la BD
     */
    public String insertar(Factura fac) {
        String sql = CargadorProperties.obtenerConfigFactura("fac.insertar");
        String codigoGenerado = null;

        try {
            codigoGenerado = generarCodigoFactura();

            if (codigoGenerado == null) {
                System.out.println("Error: No se pudo generar código de factura");
                return null;
            }

            PreparedStatement ps = conexion.prepareStatement(sql);

            ps.setString(1, codigoGenerado);  // id_Factura
            ps.setString(2, fac.getCedRucCliente());  // id_cliente ← USA getCedRucCliente()
            ps.setString(3, fac.getNombreEmpresa());  // fac_Nombre_Empresa
            ps.setString(4, fac.getEmailEmpresa());  // fac_Email_Empresa
            ps.setString(5, fac.getTelefonoEmpresa());  // fac_Telefono_Empresa
            ps.setTimestamp(6, Timestamp.valueOf(fac.getFechaHora()));  // fac_Fecha_Hora
            ps.setDouble(7, fac.getSubtotal());  // fac_Subtotal
            ps.setDouble(8, fac.getIva());  // fac_IVA
            ps.setDouble(9, fac.getTotal());  // fac_Total
            ps.setString(10, fac.getTipo());  // fac_Tipo
            ps.setString(11, fac.getEstado());  // ESTADO_FAC

            int filas = ps.executeUpdate();
            ps.close();

            if (filas > 0) {
                return codigoGenerado;
            } else {
                return null;
            }

        } catch (SQLException e) {
            System.out.println(CargadorProperties.obtenerMessages("FC_E_002"));
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Modifica la cabecera de una factura existente
     */
    public boolean modificar(Factura fac) {
        String sql = CargadorProperties.obtenerConfigFactura("fac.modificar");
        
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setDouble(1, fac.getSubtotal());
            ps.setDouble(2, fac.getIva());
            ps.setDouble(3, fac.getTotal());
            ps.setString(4, fac.getTipo());
            ps.setString(5, fac.getCodigo());
            
            int filas = ps.executeUpdate();
            ps.close();
            return filas > 0;
        } catch (SQLException e) {
            System.out.println(CargadorProperties.obtenerMessages("FC_E_004"));
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Elimina (inactiva) una factura
     */
    public boolean eliminar(Factura fac) {
        String sql = CargadorProperties.obtenerConfigFactura("fac.eliminar");
        
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, fac.getCodigo());
            
            int filas = ps.executeUpdate();
            ps.close();
            return filas > 0;
        } catch (SQLException e) {
            System.out.println(CargadorProperties.obtenerMessages("FC_E_005"));
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Inserta la factura completa (cabecera + detalle de productos)
     * Usa transacción y retorna el código generado
     */
    public String insertarFacturaCompleta(Factura fac) {
        String codigoGenerado = null;
        
        try {
            // Desactivar auto-commit para manejar transacción
            conexion.setAutoCommit(false);
            
            // 1. Insertar cabecera de factura y obtener código generado
            codigoGenerado = insertar(fac);
            
            if (codigoGenerado == null) {
                conexion.rollback();
                conexion.setAutoCommit(true);
                return null;
            }
            
            // Actualizar el código en el objeto Factura
            fac.setCodigo(codigoGenerado);
            
            // 2. Insertar detalle de productos
            String sqlProducto = CargadorProperties.obtenerConfigFactura("pxf.insertar");
            
            for (ProxFac producto : fac.getProductos()) {
                PreparedStatement ps = conexion.prepareStatement(sqlProducto);
                ps.setString(1, codigoGenerado);
                ps.setString(2, producto.getCodigoProd());
                ps.setInt(3, producto.getCantidad());
                ps.setDouble(4, producto.getPrecioVenta());
                ps.setDouble(5, producto.getSubtotalProducto());
                ps.setString(6, producto.getEstado());
                
                int filas = ps.executeUpdate();
                ps.close();
                
                if (filas == 0) {
                    conexion.rollback();
                    conexion.setAutoCommit(true);
                    System.out.println(CargadorProperties.obtenerMessages("FC_E_002"));
                    return null;
                }
            }
            
            // 3. Si todo salió bien, confirmar transacción
            conexion.commit();
            conexion.setAutoCommit(true);
            return codigoGenerado;
            
        } catch (SQLException e) {
            try {
                conexion.rollback();
                conexion.setAutoCommit(true);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            System.out.println(CargadorProperties.obtenerMessages("FC_E_002"));
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Consulta todas las facturas activas
     */
    public ArrayList<Factura> consultarTodos() {
     String sql = CargadorProperties.obtenerConfigFactura("fac.consultar.todos");
     ArrayList<Factura> lista = new ArrayList<>();

     try {
         PreparedStatement ps = conexion.prepareStatement(sql);
         ResultSet rs = ps.executeQuery();

         while (rs.next()) {
             Factura fac = new Factura();
             fac.setCodigo(rs.getString(CargadorProperties.obtenerConfigFactura("fac.1")));
             fac.setCedRucCliente(rs.getString(CargadorProperties.obtenerConfigFactura("fac.2")));  // ← id_cliente
             fac.setNombreEmpresa(rs.getString(CargadorProperties.obtenerConfigFactura("fac.3")));
             fac.setEmailEmpresa(rs.getString(CargadorProperties.obtenerConfigFactura("fac.4")));
             fac.setTelefonoEmpresa(rs.getString(CargadorProperties.obtenerConfigFactura("fac.5")));
             fac.setFechaHora(rs.getTimestamp(CargadorProperties.obtenerConfigFactura("fac.6")).toLocalDateTime());
             fac.setSubtotal(rs.getDouble(CargadorProperties.obtenerConfigFactura("fac.7")));
             fac.setIva(rs.getDouble(CargadorProperties.obtenerConfigFactura("fac.8")));
             fac.setTotal(rs.getDouble(CargadorProperties.obtenerConfigFactura("fac.9")));
             fac.setTipo(rs.getString(CargadorProperties.obtenerConfigFactura("fac.10")));
             fac.setEstado(rs.getString(CargadorProperties.obtenerConfigFactura("fac.11")));

             lista.add(fac);
         }

         rs.close();
         ps.close();
     } catch (SQLException e) {
         System.out.println(CargadorProperties.obtenerMessages("FC_E_006"));
         e.printStackTrace();
     }

     return lista;
 }

    /**
     * Consulta facturas por cédula de cliente
     */
    public ArrayList<Factura> consultarPorParametro(Cliente cli) {
        String sql = CargadorProperties.obtenerConfigFactura("fac.consultar.porCliente");
        ArrayList<Factura> lista = new ArrayList<>();

        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, cli.getCedRuc());  // ← Busca por cédula en JOIN

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Factura fac = new Factura();
                fac.setCodigo(rs.getString(CargadorProperties.obtenerConfigFactura("fac.1")));
                fac.setCedRucCliente(rs.getString(CargadorProperties.obtenerConfigFactura("fac.2")));  // ← id_cliente
                fac.setNombreEmpresa(rs.getString(CargadorProperties.obtenerConfigFactura("fac.3")));
                fac.setEmailEmpresa(rs.getString(CargadorProperties.obtenerConfigFactura("fac.4")));
                fac.setTelefonoEmpresa(rs.getString(CargadorProperties.obtenerConfigFactura("fac.5")));
                fac.setFechaHora(rs.getTimestamp(CargadorProperties.obtenerConfigFactura("fac.6")).toLocalDateTime());
                fac.setSubtotal(rs.getDouble(CargadorProperties.obtenerConfigFactura("fac.7")));
                fac.setIva(rs.getDouble(CargadorProperties.obtenerConfigFactura("fac.8")));
                fac.setTotal(rs.getDouble(CargadorProperties.obtenerConfigFactura("fac.9")));
                fac.setTipo(rs.getString(CargadorProperties.obtenerConfigFactura("fac.10")));
                fac.setEstado(rs.getString(CargadorProperties.obtenerConfigFactura("fac.11")));

                lista.add(fac);
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(CargadorProperties.obtenerMessages("FC_E_003"));
            e.printStackTrace();
        }

        return lista;
    }
    
    public String generarCodigoFactura() {
        String codigo = null;

        try {
            CallableStatement cs = conexion.prepareCall("{? = call GenerarCodigoFactura()}");
            cs.registerOutParameter(1, Types.CHAR); // ← CAMBIAR A Types.CHAR
            cs.execute();
            codigo = cs.getString(1);

            if (codigo != null) {
                codigo = codigo.trim(); // ← AGREGAR .trim() para eliminar espacios
            }

            cs.close();
        } catch (SQLException e) {
            System.out.println("Error al generar código de factura");
            e.printStackTrace();
        }

        return codigo;
    }
}