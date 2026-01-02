/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MD;

import DP.ProxFac;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import util.CargadorProperties;

/**
 *
 * @author danab
 */

public class ProxFacMD {
    private Connection conexion;
    
    public ProxFacMD() {
        this.conexion = ConexionBD.getConexion();
    }
    
    //Verifica si existe un producto en el detalle de una factura
    private ProxFac verificar(String codigoFac, String codigoProd) {
        String sql = CargadorProperties.obtenerConfigFactura("pxf.verificar");
        ProxFac pxf = null;
        
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, codigoFac);
            ps.setString(2, codigoProd);
            
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                pxf = new ProxFac();
                pxf.setCodigoFac(rs.getString(CargadorProperties.obtenerConfigFactura("pxf.1")));
                pxf.setCodigoProd(rs.getString(CargadorProperties.obtenerConfigFactura("pxf.2")));
                pxf.setCantidad(rs.getInt(CargadorProperties.obtenerConfigFactura("pxf.3")));
                pxf.setPrecioVenta(rs.getDouble(CargadorProperties.obtenerConfigFactura("pxf.4")));
                pxf.setSubtotalProducto(rs.getDouble(CargadorProperties.obtenerConfigFactura("pxf.5")));
                pxf.setEstado(rs.getString(CargadorProperties.obtenerConfigFactura("pxf.6")));
            }
            
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(CargadorProperties.obtenerMessages("FC_E_003"));
            e.printStackTrace();
        }
        
        return pxf;
    }
    
    //Inserta un nuevo producto en el detalle de factura
    private boolean insertar(ProxFac pxf) {
        String sql = CargadorProperties.obtenerConfigFactura("pxf.insertar");
        
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, pxf.getCodigoFac());
            ps.setString(2, pxf.getCodigoProd());
            ps.setInt(3, pxf.getCantidad());
            ps.setDouble(4, pxf.getPrecioVenta());
            ps.setDouble(5, pxf.getSubtotalProducto());
            ps.setString(6, pxf.getEstado());
            
            int filas = ps.executeUpdate();
            ps.close();
            return filas > 0;
        } catch (SQLException e) {
            System.out.println(CargadorProperties.obtenerMessages("FC_E_002"));
            e.printStackTrace();
            return false;
        }
    }
    
    //Actualiza un producto existente en el detalle
    private boolean actualizar(ProxFac pxf) {
        String sql = CargadorProperties.obtenerConfigFactura("pxf.actualizar");
        
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, pxf.getCantidad());
            ps.setDouble(2, pxf.getPrecioVenta());
            ps.setDouble(3, pxf.getSubtotalProducto());
            ps.setString(4, pxf.getCodigoFac());
            ps.setString(5, pxf.getCodigoProd());
            
            int filas = ps.executeUpdate();
            ps.close();
            return filas > 0;
        } catch (SQLException e) {
            System.out.println(CargadorProperties.obtenerMessages("FC_E_004"));
            e.printStackTrace();
            return false;
        }
    }
    
    //Actualiza si existe, inserta si no existe
    public boolean actualizarOInsertar(ProxFac pxf) {
        ProxFac existe = verificar(pxf.getCodigoFac(), pxf.getCodigoProd());
        
        if (existe == null) {
            return insertar(pxf);
        } else {
            return actualizar(pxf);
        }
    }
    
    //Consulta todos los productos asociados a una factura
    public ArrayList<ProxFac> consultarPorFactura(ProxFac pxf) {
        String sql = CargadorProperties.obtenerConfigFactura("pxf.consultar.porFactura");
        ArrayList<ProxFac> lista = new ArrayList<>();
        
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, pxf.getCodigoFac());
            
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                ProxFac producto = new ProxFac();
                producto.setCodigoFac(rs.getString(CargadorProperties.obtenerConfigFactura("pxf.1")));
                producto.setCodigoProd(rs.getString(CargadorProperties.obtenerConfigFactura("pxf.2")));
                producto.setCantidad(rs.getInt(CargadorProperties.obtenerConfigFactura("pxf.3")));
                producto.setPrecioVenta(rs.getDouble(CargadorProperties.obtenerConfigFactura("pxf.4")));
                producto.setSubtotalProducto(rs.getDouble(CargadorProperties.obtenerConfigFactura("pxf.5")));
                producto.setEstado(rs.getString(CargadorProperties.obtenerConfigFactura("pxf.6")));
                
                lista.add(producto);
            }
            
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(CargadorProperties.obtenerMessages("FC_E_003"));
            e.printStackTrace();
        }
        
        return lista;
    }
    
    //Elimina logicamente todos los productos de una factura
    public boolean eliminarPorFactura(ProxFac pxf) {
        String sql = CargadorProperties.obtenerConfigFactura("pxf.eliminar.porFactura");
        
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, pxf.getCodigoFac());
            
            int filas = ps.executeUpdate();
            ps.close();
            return filas > 0;
        } catch (SQLException e) {
            System.out.println(CargadorProperties.obtenerMessages("FC_E_005"));
            e.printStackTrace();
            return false;
        }
    }
    
    //Elimina logicamente un solo producto específico de una factura
     public boolean eliminarProducto(ProxFac pxf) {
        String sql = CargadorProperties.obtenerConfigFactura("pxf.eliminar.porProducto");

        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, pxf.getCodigoFac());   // id_factura
            ps.setString(2, pxf.getCodigoProd());   // id_producto

            int filas = ps.executeUpdate();
            ps.close();
            return filas > 0;
        } catch (SQLException e) {
            System.out.println(CargadorProperties.obtenerMessages("FC_E_005"));
            e.printStackTrace();
            return false;
        }
    }
}
