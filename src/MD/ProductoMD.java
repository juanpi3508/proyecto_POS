package MD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import util.CargadorProperties;
import DP.Producto;
import GUI.ItemCombo;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class ProductoMD {
    private Connection conexion;
    
    public ProductoMD(){
        this.conexion = ConexionBD.getConexion();
    }
    
    public String generarCodigo(String idCategoria){
        int ultimo = 0;
        
        try {
            String sql = CargadorProperties.obtenerConfigProducto("pro.ultimo.secuencial");
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, idCategoria + "-%");
            
            ResultSet rs = ps.executeQuery();
            
            if(rs.next()){
                ultimo = rs.getInt("ultimo");
            }
            
            rs.close();
            ps.close();
            
            int nuevo = ultimo + 1;
            return String.format("%s-%04d", idCategoria, nuevo);
        } catch(SQLException e){
            System.out.println("Error al generar codigo de producto");
            e.printStackTrace();
            return null;
        }
    }
    
    public boolean insertar(Producto pro){
        String sql = CargadorProperties.obtenerConfigProducto("pro.insertar");
        
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, pro.getCodigo());
            ps.setString(2, pro.getDescripcion());
            ps.setString(3, pro.getIdCategoria());
            ps.setString(4, pro.getUmCompra());
            ps.setDouble(5, pro.getPrecioCompra());
            ps.setString(6, pro.getUmVenta());
            ps.setDouble(7, pro.getPrecioVenta());
            ps.setString(8, pro.getImagen());
            
            int filas = ps.executeUpdate();
            ps.close();
            return filas > 0;
        } catch (SQLException e) {
            System.out.println(CargadorProperties.obtenerMessages("PD_E_001"));
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                CargadorProperties.obtenerMessages("PD_E_001"), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    public boolean modificar(Producto pro){
        String sql = CargadorProperties.obtenerConfigProducto("pro.modificar");
        
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, pro.getDescripcion());
            ps.setString(2, pro.getUmCompra());
            ps.setDouble(3, pro.getPrecioCompra());
            ps.setString(4, pro.getUmVenta());
            ps.setDouble(5, pro.getPrecioVenta());
            ps.setString(6, pro.getImagen());
            ps.setString(7, pro.getCodigo());
            
            int filas = ps.executeUpdate();
            ps.close();
            return filas > 0;
        } catch (SQLException e) {
            System.out.println(CargadorProperties.obtenerMessages("PD_E_003"));
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                CargadorProperties.obtenerMessages("PD_E_003"), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    public boolean eliminar (Producto pro){
        String sql = CargadorProperties.obtenerConfigProducto("pro.eliminar");
        
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, pro.getCodigo());
            
            int filas = ps.executeUpdate();
            ps.close();
            return filas > 0;
        } catch (SQLException e) {
            System.out.println(CargadorProperties.obtenerMessages("PD_E_004"));
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                CargadorProperties.obtenerMessages("PD_E_004"), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    public Producto verificarPorCodigo(String codigo) {
        String sql = CargadorProperties.obtenerConfigProducto("pro.verificar.codigo");
        Producto pro = null;
        
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, codigo);
            
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                pro = productoCompleto(rs);
            }
            
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(CargadorProperties.obtenerMessages("PD_E_002"));
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                CargadorProperties.obtenerMessages("PD_E_002"), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
        
        return pro;
    }
    
    public Producto verificarPorNombre(String descripcion) {
        String sql = CargadorProperties.obtenerConfigProducto("pro.verificar.nombre");
        Producto pro = null;
        
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, descripcion);
            
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                pro = productoCompleto(rs);
            }
            
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(CargadorProperties.obtenerMessages("PD_E_002"));
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                CargadorProperties.obtenerMessages("PD_E_002"), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
        
        return pro;
    }
    
    public ArrayList<Producto> buscarPorCodigo(String textoBusqueda) {
        String sql = CargadorProperties.obtenerConfigProducto("pro.buscar.codigo");
        ArrayList<Producto> lista = new ArrayList<>();
        
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, "%" + textoBusqueda + "%");
            
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                lista.add(productoParaTabla(rs));
            }
            
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(CargadorProperties.obtenerMessages("PD_E_002"));
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                CargadorProperties.obtenerMessages("PD_E_002"), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
        
        return lista;
    }
    
    public ArrayList<Producto> buscarPorNombre(String textoBusqueda) {
        String sql = CargadorProperties.obtenerConfigProducto("pro.buscar.nombre");
        ArrayList<Producto> lista = new ArrayList<>();
        
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, "%" + textoBusqueda + "%");
            
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                lista.add(productoParaTabla(rs));
            }
            
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(CargadorProperties.obtenerMessages("PD_E_002"));
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                CargadorProperties.obtenerMessages("PD_E_002"), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
        
        return lista;
    }
    
    public ArrayList<Producto> buscarPorCategoria(String idCategoria) {
        String sql = CargadorProperties.obtenerConfigProducto("pro.buscar.categoria");
        ArrayList<Producto> lista = new ArrayList<>();
        
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, idCategoria);
            
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                lista.add(productoParaTabla(rs));
            }
            
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(CargadorProperties.obtenerMessages("PD_E_002"));
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                CargadorProperties.obtenerMessages("PD_E_002"), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
        
        return lista;
    }
    
    public ArrayList<Producto> buscarPorUmCompra(String idUnidad) {
        String sql = CargadorProperties.obtenerConfigProducto("pro.buscar.um.compra");
        ArrayList<Producto> lista = new ArrayList<>();
        
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, idUnidad);
            
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                lista.add(productoParaTabla(rs));
            }
            
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(CargadorProperties.obtenerMessages("PD_E_002"));
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                CargadorProperties.obtenerMessages("PD_E_002"), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
        
        return lista;
    }
    
    public ArrayList<Producto> buscarPorUmVenta(String idUnidad) {
        String sql = CargadorProperties.obtenerConfigProducto("pro.buscar.um.venta");
        ArrayList<Producto> lista = new ArrayList<>();
        
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, idUnidad);
            
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                lista.add(productoParaTabla(rs));
            }
            
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(CargadorProperties.obtenerMessages("PD_E_002"));
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                CargadorProperties.obtenerMessages("PD_E_002"), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
        
        return lista;
    }
    
    public ArrayList<Producto> consultarTodos() {
        String sql = CargadorProperties.obtenerConfigProducto("pro.consultar.todos");
        ArrayList<Producto> lista = new ArrayList<>();
        
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                lista.add(productoParaTabla(rs));
            }
            
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(CargadorProperties.obtenerMessages("PD_E_005"));
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                CargadorProperties.obtenerMessages("PD_E_005"), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
        
        return lista;
    }
    
    public ArrayList<ItemCombo> obtenerCategorias() {
        String sql = CargadorProperties.obtenerConfigProducto("pro.categorias");
        ArrayList<ItemCombo> categorias = new ArrayList<>();

        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                ItemCombo item = new ItemCombo(
                    rs.getString("id_categoria"),
                    rs.getString("cat_descripcion")
                );
                categorias.add(item);
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                "Error al cargar las categorías", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }

        return categorias;
    }
    
    public ArrayList<ItemCombo> obtenerUnidadesMedida() {
        String sql = CargadorProperties.obtenerConfigProducto("pro.unidades");
        ArrayList<ItemCombo> unidades = new ArrayList<>();

        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                ItemCombo item = new ItemCombo(
                    rs.getString("id_unidad_medida"),
                    rs.getString("um_descripcion")
                );
                unidades.add(item);
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                "Error al cargar las unidades de medida", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }

        return unidades;
    }
    
    private Producto productoCompleto(ResultSet rs) throws SQLException {
        Producto pro = new Producto();
        pro.setCodigo(rs.getString(1));
        pro.setDescripcion(rs.getString(2));
        pro.setIdCategoria(rs.getString(3));
        pro.setCategoria(rs.getString(4));
        pro.setUmCompra(rs.getString(5));
        pro.setNombreUmCompra(rs.getString(6));
        pro.setPrecioCompra(rs.getDouble(7));
        pro.setUmVenta(rs.getString(8));
        pro.setNombreUmVenta(rs.getString(9));
        pro.setPrecioVenta(rs.getDouble(10));
        pro.setSaldoIni(rs.getInt(11));
        pro.setQtyIngresos(rs.getInt(12));
        pro.setQtyEgresos(rs.getInt(13));
        pro.setSaldoFin(rs.getInt(14));
        pro.setImagen(rs.getString(15));
        pro.setEstado(rs.getString(16));
        return pro;
    }
    
    private Producto productoParaTabla(ResultSet rs) throws SQLException {
        Producto pro = new Producto();
        pro.setCodigo(rs.getString(1));              // id_producto
        pro.setDescripcion(rs.getString(2));          // pro_descripcion
        pro.setCategoria(rs.getString(3));            // cat_descripcion
        pro.setNombreUmCompra(rs.getString(4));       // um_compra
        pro.setPrecioCompra(rs.getDouble(5));         // pro_precio_compra
        pro.setNombreUmVenta(rs.getString(6));        // um_venta
        pro.setPrecioVenta(rs.getDouble(7));          // pro_precio_venta
        pro.setSaldoIni(rs.getInt(8));                // pro_saldo_ini
        pro.setSaldoFin(rs.getInt(9));                // pro_saldo_fin
        return pro;
    }
}