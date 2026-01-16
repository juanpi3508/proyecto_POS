package MD;

import DP.Cliente;
import DP.Factura;
import DP.ProxFac;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
    
    //Inserta la cabecera en la base de datos, devuelve el codigo de la factura generada
    public String insertar(Factura fac) {
        String sql = CargadorProperties.obtenerConfigFactura("fac.insertar");
        String codigoGenerado = null;

        try {
            codigoGenerado = generarCodigoFactura();

            if (codigoGenerado == null) {
                System.out.println(CargadorProperties.obtenerMessages("FC_E_007"));
                return null;
            }

            PreparedStatement ps = conexion.prepareStatement(sql);

            // 8 parámetros (SIN datos de empresa)
            ps.setString(1, codigoGenerado);                              //id_Factura
            ps.setString(2, fac.getCodigoCliente());                      //id_cliente
            ps.setTimestamp(3, Timestamp.valueOf(fac.getFechaHora()));    //fac_Fecha_Hora
            ps.setDouble(4, fac.getSubtotal());                           //fac_Subtotal
            ps.setDouble(5, fac.getIva());                                //fac_IVA
            ps.setDouble(6, fac.getTotal());                              //fac_Total
            ps.setString(7, fac.getTipo());                               //fac_Tipo
            ps.setString(8, fac.getEstado());                             //ESTADO_FAC

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
    
    //Modifica
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
    
    //Elimina
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
    
    //Inserta la factura completa (cabecera y detalle)
    //Devuelce el codigo de la factura
    public String insertarFacturaCompleta(Factura fac) {
        String codigoGenerado = null;
        
        try {
            conexion.setAutoCommit(false);
            
            codigoGenerado = insertar(fac);
            
            if (codigoGenerado == null) {
                conexion.rollback();
                conexion.setAutoCommit(true);
                return null;
            }

            fac.setCodigo(codigoGenerado);
            
            String sqlProducto = CargadorProperties.obtenerConfigFactura("pxf.insertar");
            
            for (ProxFac producto : fac.getProductos()) {
                PreparedStatement ps = conexion.prepareStatement(sqlProducto);
                ps.setString(1, codigoGenerado);
                ps.setString(2, producto.getCodigoProd());
                ps.setInt(3, producto.getCantidad());
                ps.setDouble(4, producto.getPrecioVenta());
                ps.setDouble(5, producto.getSubtotalProducto());
                
                int filas = ps.executeUpdate();
                ps.close();
                
                if (filas == 0) {
                    conexion.rollback();
                    conexion.setAutoCommit(true);
                    System.out.println(CargadorProperties.obtenerMessages("FC_E_002"));
                    return null;
                }
            }
            
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
    
    //Consulta todas las facturas activas
    public ArrayList<Factura> consultarTodos() {
        String sql = CargadorProperties.obtenerConfigFactura("fac.consultar.todos");
        ArrayList<Factura> lista = new ArrayList<>();

        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Factura fac = new Factura();
                fac.setCodigo(rs.getString(CargadorProperties.obtenerConfigFactura("fac.1")));           //id_Factura
                fac.setCodigoCliente(rs.getString(CargadorProperties.obtenerConfigFactura("fac.2")));    //id_cliente
                fac.setFechaHora(rs.getTimestamp(CargadorProperties.obtenerConfigFactura("fac.3")).toLocalDateTime()); //fac_Fecha_Hora
                fac.setSubtotal(rs.getDouble(CargadorProperties.obtenerConfigFactura("fac.4")));         //fac_Subtotal
                fac.setIva(rs.getDouble(CargadorProperties.obtenerConfigFactura("fac.5")));              //fac_IVA
                fac.setTotal(rs.getDouble(CargadorProperties.obtenerConfigFactura("fac.6")));            //fac_Total
                fac.setTipo(rs.getString(CargadorProperties.obtenerConfigFactura("fac.7")));             //fac_Tipo
                fac.setEstado(rs.getString(CargadorProperties.obtenerConfigFactura("fac.8")));           //ESTADO_FAC

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

    //Consulta factura asociadas a un cliente
    public ArrayList<Factura> consultarPorParametro(Cliente cli) {
        String sql = CargadorProperties.obtenerConfigFactura("fac.consultar.porCliente");
        ArrayList<Factura> lista = new ArrayList<>();

        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, cli.getCedRuc());  //Busca por cédula en JOIN

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Factura fac = new Factura();
                fac.setCodigo(rs.getString(CargadorProperties.obtenerConfigFactura("fac.1")));           //id_Factura
                fac.setCodigoCliente(rs.getString(CargadorProperties.obtenerConfigFactura("fac.2")));    //id_cliente
                fac.setFechaHora(rs.getTimestamp(CargadorProperties.obtenerConfigFactura("fac.3")).toLocalDateTime()); //fac_Fecha_Hora
                fac.setSubtotal(rs.getDouble(CargadorProperties.obtenerConfigFactura("fac.4")));         //fac_Subtotal
                fac.setIva(rs.getDouble(CargadorProperties.obtenerConfigFactura("fac.5")));              //fac_IVA
                fac.setTotal(rs.getDouble(CargadorProperties.obtenerConfigFactura("fac.6")));            //fac_Total
                fac.setTipo(rs.getString(CargadorProperties.obtenerConfigFactura("fac.7")));             //fac_Tipo
                fac.setEstado(rs.getString(CargadorProperties.obtenerConfigFactura("fac.8")));           //ESTADO_FAC

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
    
    //Consulta una factura en especifico
    public Factura consultarPorCodigo(Factura facParam) {
        String sql = CargadorProperties.obtenerConfigFactura("fac.consultar.porCodigo");
        Factura fac = null;

        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, facParam.getCodigo());
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                fac = new Factura();
                fac.setCodigo(rs.getString(CargadorProperties.obtenerConfigFactura("fac.1")));
                fac.setCodigoCliente(rs.getString(CargadorProperties.obtenerConfigFactura("fac.2")));
                fac.setFechaHora(rs.getTimestamp(CargadorProperties.obtenerConfigFactura("fac.3")).toLocalDateTime());
                fac.setSubtotal(rs.getDouble(CargadorProperties.obtenerConfigFactura("fac.4")));
                fac.setIva(rs.getDouble(CargadorProperties.obtenerConfigFactura("fac.5")));
                fac.setTotal(rs.getDouble(CargadorProperties.obtenerConfigFactura("fac.6")));
                fac.setTipo(rs.getString(CargadorProperties.obtenerConfigFactura("fac.7")));
                fac.setEstado(rs.getString(CargadorProperties.obtenerConfigFactura("fac.8")));
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(CargadorProperties.obtenerMessages("FC_E_003"));
            e.printStackTrace();
        }

        return fac;
    }

    //Utiliza procedure de la BD para generar el codigo de la factura
    public String generarCodigoFactura() {
        String codigo = null;

        try {
            CallableStatement cs = conexion.prepareCall("{? = call GenerarCodigoFactura()}");
            cs.registerOutParameter(1, Types.CHAR);
            cs.execute();
            codigo = cs.getString(1);

            if (codigo != null) {
                codigo = codigo.trim();
            }

            cs.close();
        } catch (SQLException e) {
            System.out.println(CargadorProperties.obtenerMessages("FC_E_007"));
            e.printStackTrace();
        }

        return codigo;
    }
    
    //Consulta TODAS las facturas (APR y ANU) asociadas a un cliente
    public ArrayList<Factura> consultarTodasPorParametro(Cliente cli) {
        String sql = CargadorProperties.obtenerConfigFactura("fac.consultar.porCliente.todas");
        ArrayList<Factura> lista = new ArrayList<>();

        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, cli.getCedRuc());  //Busca por cédula en JOIN

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Factura fac = new Factura();
                fac.setCodigo(rs.getString(CargadorProperties.obtenerConfigFactura("fac.1")));
                fac.setCodigoCliente(rs.getString(CargadorProperties.obtenerConfigFactura("fac.2")));
                fac.setFechaHora(rs.getTimestamp(CargadorProperties.obtenerConfigFactura("fac.3")).toLocalDateTime());
                fac.setSubtotal(rs.getDouble(CargadorProperties.obtenerConfigFactura("fac.4")));
                fac.setIva(rs.getDouble(CargadorProperties.obtenerConfigFactura("fac.5")));
                fac.setTotal(rs.getDouble(CargadorProperties.obtenerConfigFactura("fac.6")));
                fac.setTipo(rs.getString(CargadorProperties.obtenerConfigFactura("fac.7")));
                fac.setEstado(rs.getString(CargadorProperties.obtenerConfigFactura("fac.8")));

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
    //Consulta factura SIN filtro de estado (para mostrar detalle en consulta específica)
    public Factura consultarPorCodigoDetalle(Factura facParam) {
        String sql = CargadorProperties.obtenerConfigFactura("fac.consultar.porCodigo.detalle");
        Factura fac = null;

        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, facParam.getCodigo());
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                fac = new Factura();
                fac.setCodigo(rs.getString(CargadorProperties.obtenerConfigFactura("fac.1")));
                fac.setCodigoCliente(rs.getString(CargadorProperties.obtenerConfigFactura("fac.2")));
                fac.setFechaHora(rs.getTimestamp(CargadorProperties.obtenerConfigFactura("fac.3")).toLocalDateTime());
                fac.setSubtotal(rs.getDouble(CargadorProperties.obtenerConfigFactura("fac.4")));
                fac.setIva(rs.getDouble(CargadorProperties.obtenerConfigFactura("fac.5")));
                fac.setTotal(rs.getDouble(CargadorProperties.obtenerConfigFactura("fac.6")));
                fac.setTipo(rs.getString(CargadorProperties.obtenerConfigFactura("fac.7")));
                fac.setEstado(rs.getString(CargadorProperties.obtenerConfigFactura("fac.8")));
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(CargadorProperties.obtenerMessages("FC_E_003"));
            e.printStackTrace();
        }

        return fac;
    }
    
    public boolean aprobarFactura(Factura facParam) {
        String sql = CargadorProperties.obtenerConfigFactura("fac.aprobar");
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, facParam.getCodigo());
            ResultSet rs = ps.executeQuery();

            boolean resultado = false;
            if (rs.next()) {
                resultado = rs.getBoolean(1);
            }

            rs.close();
            ps.close();
            return resultado;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
}
