package MD;

import DP.Compra;
import DP.Proveedor;
import DP.ProxOc;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import util.CargadorProperties;
import java.sql.CallableStatement;
import java.sql.Types;

public class CompraMD {

    private Connection conexion;

    public CompraMD() {
        this.conexion = ConexionBD.getConexion();
    }

    public String insertar(Compra comp) {
        String sql = CargadorProperties.obtenerConfigCompra("comp.insertar");
        String codigoGenerado = null;

        try {
            codigoGenerado = generarCodigoCompra();

            if (codigoGenerado == null) {
                System.out.println(CargadorProperties.obtenerMessages("CP_E_007"));
                return null;
            }

            PreparedStatement ps = conexion.prepareStatement(sql);

            ps.setString(1, codigoGenerado);                               // id_compra
            ps.setString(2, comp.getCodigoProveedor());                   // id_proveedor
            ps.setTimestamp(3, Timestamp.valueOf(comp.getFechaHora()));   // oc_fecha_hora
            ps.setDouble(4, comp.getSubtotal());                          // oc_subtotal
            ps.setDouble(5, comp.getIva());                               // oc_iva
            ps.setDouble(6, comp.getTotal());                             // oc_total
            ps.setString(7, comp.getEstado());                            // estado_oc

            int filas = ps.executeUpdate();
            ps.close();

            return filas > 0 ? codigoGenerado : null;

        } catch (SQLException e) {
            System.out.println(CargadorProperties.obtenerMessages("CP_E_002"));
            e.printStackTrace();
            return null;
        }
    }

    public boolean modificar(Compra comp) {
        String sql = CargadorProperties.obtenerConfigCompra("comp.modificar");

        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setDouble(1, comp.getSubtotal());
            ps.setDouble(2, comp.getIva());
            ps.setDouble(3, comp.getTotal());
            ps.setString(4, comp.getCodigo());

            int filas = ps.executeUpdate();
            ps.close();
            return filas > 0;
        } catch (SQLException e) {
            System.out.println(CargadorProperties.obtenerMessages("CP_E_004"));
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminar(Compra comp) {
        String sql = CargadorProperties.obtenerConfigCompra("comp.eliminar");

        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, comp.getCodigo());

            int filas = ps.executeUpdate();
            ps.close();
            return filas > 0;
        } catch (SQLException e) {
            System.out.println(CargadorProperties.obtenerMessages("CP_E_005"));
            e.printStackTrace();
            return false;
        }
    }

    public String insertarCompraCompleta(Compra comp) {
        String codigoGenerado = null;

        try {
            conexion.setAutoCommit(false);

            codigoGenerado = insertar(comp);

            if (codigoGenerado == null) {
                conexion.rollback();
                conexion.setAutoCommit(true);
                return null;
            }

            comp.setCodigo(codigoGenerado);

            String sqlDetalle = CargadorProperties.obtenerConfigCompra("pxo.insertar");

            for (ProxOc prod : comp.getProductos()) {
                PreparedStatement ps = conexion.prepareStatement(sqlDetalle);
                ps.setString(1, codigoGenerado);
                ps.setString(2, prod.getCodigoProducto());
                ps.setInt(3, prod.getCantidad());
                ps.setDouble(4, prod.getPrecioCompra());
                ps.setDouble(5, prod.getSubtotalProducto());

                int filas = ps.executeUpdate();
                ps.close();

                if (filas == 0) {
                    conexion.rollback();
                    conexion.setAutoCommit(true);
                    System.out.println(CargadorProperties.obtenerMessages("CP_E_002"));
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
            System.out.println(CargadorProperties.obtenerMessages("CP_E_002"));
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<Compra> consultarTodos() {
        String sql = CargadorProperties.obtenerConfigCompra("comp.consultar.todos");
        ArrayList<Compra> lista = new ArrayList<>();

        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Compra comp = new Compra();
                comp.setCodigo(rs.getString(CargadorProperties.obtenerConfigCompra("comp.1")));
                comp.setCodigoProveedor(rs.getString(CargadorProperties.obtenerConfigCompra("comp.2")));
                comp.setFechaHora(rs.getTimestamp(CargadorProperties.obtenerConfigCompra("comp.3")).toLocalDateTime());
                comp.setSubtotal(rs.getDouble(CargadorProperties.obtenerConfigCompra("comp.4")));
                comp.setIva(rs.getDouble(CargadorProperties.obtenerConfigCompra("comp.5")));
                comp.setTotal(rs.getDouble(CargadorProperties.obtenerConfigCompra("comp.6")));
                comp.setEstado(rs.getString(CargadorProperties.obtenerConfigCompra("comp.7")));

                lista.add(comp);
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(CargadorProperties.obtenerMessages("CP_E_006"));
            e.printStackTrace();
        }

        return lista;
    }

    public ArrayList<Compra> consultarPorParametro(Proveedor prov) {
        String sql = CargadorProperties.obtenerConfigCompra("comp.consultar.porProveedor");
        ArrayList<Compra> lista = new ArrayList<>();

        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, prov.getCedRuc());

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Compra comp = new Compra();
                comp.setCodigo(rs.getString(CargadorProperties.obtenerConfigCompra("comp.1")));
                comp.setCodigoProveedor(rs.getString(CargadorProperties.obtenerConfigCompra("comp.2")));
                comp.setFechaHora(rs.getTimestamp(CargadorProperties.obtenerConfigCompra("comp.3")).toLocalDateTime());
                comp.setSubtotal(rs.getDouble(CargadorProperties.obtenerConfigCompra("comp.4")));
                comp.setIva(rs.getDouble(CargadorProperties.obtenerConfigCompra("comp.5")));
                comp.setTotal(rs.getDouble(CargadorProperties.obtenerConfigCompra("comp.6")));
                comp.setEstado(rs.getString(CargadorProperties.obtenerConfigCompra("comp.7")));

                lista.add(comp);
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(CargadorProperties.obtenerMessages("CP_E_003"));
            e.printStackTrace();
        }

        return lista;
    }

    public Compra consultarPorCodigo(Compra compParam) {
        String sql = CargadorProperties.obtenerConfigCompra("comp.consultar.porCodigo");
        Compra comp = null;

        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, compParam.getCodigo());
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                comp = new Compra();
                comp.setCodigo(rs.getString(CargadorProperties.obtenerConfigCompra("comp.1")));
                comp.setCodigoProveedor(rs.getString(CargadorProperties.obtenerConfigCompra("comp.2")));
                comp.setFechaHora(rs.getTimestamp(CargadorProperties.obtenerConfigCompra("comp.3")).toLocalDateTime());
                comp.setSubtotal(rs.getDouble(CargadorProperties.obtenerConfigCompra("comp.4")));
                comp.setIva(rs.getDouble(CargadorProperties.obtenerConfigCompra("comp.5")));
                comp.setTotal(rs.getDouble(CargadorProperties.obtenerConfigCompra("comp.6")));
                comp.setEstado(rs.getString(CargadorProperties.obtenerConfigCompra("comp.7")));
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(CargadorProperties.obtenerMessages("CP_E_003"));
            e.printStackTrace();
        }

        return comp;
    }

    public String generarCodigoCompra() {
        String codigo = null;

        try {
            CallableStatement cs = conexion.prepareCall("{? = call GenerarCodigoCompra()}");
            cs.registerOutParameter(1, Types.CHAR);
            cs.execute();
            codigo = cs.getString(1);

            if (codigo != null) {
                codigo = codigo.trim();
            }

            cs.close();
        } catch (SQLException e) {
            System.out.println(CargadorProperties.obtenerMessages("CP_E_007"));
            e.printStackTrace();
        }

        return codigo;
    }

    public boolean aprobarCompra(Compra compParam) {
        String sql = CargadorProperties.obtenerConfigCompra("comp.aprobar");

        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, compParam.getCodigo());
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
