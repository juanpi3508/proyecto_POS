package MD;

import DP.ProxOc;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import util.CargadorProperties;

public class ProxOcMD {

    private Connection conexion;

    public ProxOcMD() {
        this.conexion = ConexionBD.getConexion();
    }

    // ==========================================
    // Verificar existencia producto en la OC
    // ==========================================
    private ProxOc verificar(String codigoCompra, String codigoProducto) {
        String sql = CargadorProperties.obtenerConfigCompra("pxo.verificar");
        ProxOc pxo = null;

        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, codigoCompra);
            ps.setString(2, codigoProducto);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                pxo = new ProxOc();
                pxo.setCodigoCompra(rs.getString(CargadorProperties.obtenerConfigCompra("pxo.1")));
                pxo.setCodigoProducto(rs.getString(CargadorProperties.obtenerConfigCompra("pxo.2")));
                pxo.setCantidad(rs.getInt(CargadorProperties.obtenerConfigCompra("pxo.3")));
                pxo.setPrecioCompra(rs.getDouble(CargadorProperties.obtenerConfigCompra("pxo.4")));
                pxo.setSubtotalProducto(rs.getDouble(CargadorProperties.obtenerConfigCompra("pxo.5")));
                pxo.setEstado(rs.getString(CargadorProperties.obtenerConfigCompra("pxo.6")));
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(CargadorProperties.obtenerMessages("CP_E_003"));
            e.printStackTrace();
        }

        return pxo;
    }

    // ==========================================
    // Insertar producto
    // ==========================================
    public boolean insertar(ProxOc pxo) {
        String sql = CargadorProperties.obtenerConfigCompra("pxo.insertar");
        System.out.println("SQL REAL pxo.insertar => " + sql);

        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, pxo.getCodigoCompra());
            ps.setString(2, pxo.getCodigoProducto());
            ps.setInt(3, pxo.getCantidad());
            ps.setDouble(4, pxo.getPrecioCompra());
            ps.setDouble(5, pxo.getSubtotalProducto());

            int filas = ps.executeUpdate();
            ps.close();
            return filas > 0;
        } catch (SQLException e) {
            System.out.println(CargadorProperties.obtenerMessages("CP_E_002"));
            e.printStackTrace();
            return false;
        }
    }

    // ==========================================
    // Modificar producto
    // ==========================================
    public boolean modificar(ProxOc pxo) {
        String sql = CargadorProperties.obtenerConfigCompra("pxo.actualizar");

        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setInt(1, pxo.getCantidad());
            ps.setDouble(2, pxo.getPrecioCompra());
            ps.setDouble(3, pxo.getSubtotalProducto());
            ps.setString(4, pxo.getCodigoCompra());
            ps.setString(5, pxo.getCodigoProducto());

            int filas = ps.executeUpdate();
            ps.close();
            return filas > 0;
        } catch (SQLException e) {
            System.out.println(CargadorProperties.obtenerMessages("CP_E_004"));
            e.printStackTrace();
            return false;
        }
    }

    // ==========================================
    // Insertar o actualizar
    // ==========================================
    public boolean actualizarOInsertar(ProxOc pxo) {
        ProxOc existe = verificar(pxo.getCodigoCompra(), pxo.getCodigoProducto());
        return (existe == null) ? insertar(pxo) : modificar(pxo);
    }

    // ==========================================
    // Consultar detalle por compra
    // ==========================================
    public ArrayList<ProxOc> consultarPorCompra(ProxOc pxo) {
        String sql = CargadorProperties.obtenerConfigCompra("pxo.consultar.porCompra");
        ArrayList<ProxOc> lista = new ArrayList<>();

        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, pxo.getCodigoCompra());

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ProxOc producto = new ProxOc();
                producto.setCodigoCompra(rs.getString(CargadorProperties.obtenerConfigCompra("pxo.1")));
                producto.setCodigoProducto(rs.getString(CargadorProperties.obtenerConfigCompra("pxo.2")));
                producto.setCantidad(rs.getInt(CargadorProperties.obtenerConfigCompra("pxo.3")));
                producto.setPrecioCompra(rs.getDouble(CargadorProperties.obtenerConfigCompra("pxo.4")));
                producto.setSubtotalProducto(rs.getDouble(CargadorProperties.obtenerConfigCompra("pxo.5")));
                producto.setEstado(rs.getString(CargadorProperties.obtenerConfigCompra("pxo.6")));

                lista.add(producto);
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(CargadorProperties.obtenerMessages("CP_E_003"));
            e.printStackTrace();
        }

        return lista;
    }

    // ==========================================
    // Consultar detalle ABI
    // ==========================================
    public ArrayList<ProxOc> consultarPorCompraABI(ProxOc pxo) {
        String sql = CargadorProperties.obtenerConfigCompra("pxo.consultar.porCompra.ABI");
        ArrayList<ProxOc> lista = new ArrayList<>();

        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, pxo.getCodigoCompra());

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ProxOc producto = new ProxOc();
                producto.setCodigoCompra(rs.getString(CargadorProperties.obtenerConfigCompra("pxo.1")));
                producto.setCodigoProducto(rs.getString(CargadorProperties.obtenerConfigCompra("pxo.2")));
                producto.setCantidad(rs.getInt(CargadorProperties.obtenerConfigCompra("pxo.3")));
                producto.setPrecioCompra(rs.getDouble(CargadorProperties.obtenerConfigCompra("pxo.4")));
                producto.setSubtotalProducto(rs.getDouble(CargadorProperties.obtenerConfigCompra("pxo.5")));
                producto.setEstado(rs.getString(CargadorProperties.obtenerConfigCompra("pxo.6")));

                lista.add(producto);
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(CargadorProperties.obtenerMessages("CP_E_003"));
            e.printStackTrace();
        }

        return lista;
    }

    // ==========================================
    // Eliminado lógico por compra
    // ==========================================
    public boolean eliminarPorCompra(ProxOc pxo) {
        String sql = CargadorProperties.obtenerConfigCompra("pxo.eliminar.porCompra");

        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, pxo.getCodigoCompra());

            int filas = ps.executeUpdate();
            ps.close();
            return filas >= 0;
        } catch (SQLException e) {
            System.out.println(CargadorProperties.obtenerMessages("CP_E_005"));
            e.printStackTrace();
            return false;
        }
    }

    // ==========================================
    // Eliminar producto específico
    // ==========================================
    public boolean eliminarProducto(ProxOc pxo) {
        String sql = CargadorProperties.obtenerConfigCompra("pxo.eliminar.porProducto");

        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, pxo.getCodigoCompra());
            ps.setString(2, pxo.getCodigoProducto());

            int filas = ps.executeUpdate();
            ps.close();
            return filas > 0;
        } catch (SQLException e) {
            System.out.println(CargadorProperties.obtenerMessages("CP_E_005"));
            e.printStackTrace();
            return false;
        }
    }
}
