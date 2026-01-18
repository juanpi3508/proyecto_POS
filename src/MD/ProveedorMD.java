package MD;

import DP.Proveedor;
import GUI.ItemCombo;
import java.sql.Connection;
import util.CargadorProperties;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class ProveedorMD {
    private Connection conexion;
    
    public ProveedorMD() {
        this.conexion = ConexionBD.getConexion();
    }
    
    public boolean insertar(Proveedor prv) {
        String sql = CargadorProperties.obtenerConfigProveedor("prv.insertar");
        
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, prv.getNombre());
            ps.setString(2, prv.getCedRuc());
            ps.setString(3, prv.getTelefono());
            ps.setString(4, prv.getEmail());     // prv_mail
            ps.setString(5, prv.getIdCiudad());  // id_ciudad
            ps.setString(6, prv.getCelular());   // prv_celular
            ps.setString(7, prv.getDireccion()); // prv_direccion
            int filas = ps.executeUpdate();
            ps.close();
            return filas > 0;
        } catch (SQLException e) {
            System.out.println(CargadorProperties.obtenerMessages("PV_E_002"));
            e.printStackTrace();
            JOptionPane.showMessageDialog(
                null,
                CargadorProperties.obtenerMessages("PV_E_002"),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
            return false;
        }
    }
    
    public boolean modificar(Proveedor prv) {
        String sql = CargadorProperties.obtenerConfigProveedor("prv.modificar");
        
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, prv.getNombre());
            ps.setString(2, prv.getTelefono());
            ps.setString(3, prv.getEmail());     // prv_mail
            ps.setString(4, prv.getIdCiudad());  // id_ciudad
            ps.setString(5, prv.getCelular());   // prv_celular
            ps.setString(6, prv.getDireccion()); // prv_direccion
            ps.setString(7, prv.getCedRuc());    // WHERE prv_ruc_ced = ?
            
            int filas = ps.executeUpdate();
            ps.close();
            return filas > 0;
        } catch (SQLException e) {
            System.out.println(CargadorProperties.obtenerMessages("PV_E_004"));
            e.printStackTrace();
            JOptionPane.showMessageDialog(
                null,
                CargadorProperties.obtenerMessages("PV_E_004"),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
            return false;
        }
    }
    
    public boolean eliminar(Proveedor prv) {
        String sql = CargadorProperties.obtenerConfigProveedor("prv.eliminar");
        
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, prv.getCedRuc());
            
            int filas = ps.executeUpdate();
            ps.close();
            return filas > 0;
        } catch (SQLException e) {
            System.out.println(CargadorProperties.obtenerMessages("PV_E_005"));
            e.printStackTrace();
            JOptionPane.showMessageDialog(
                null,
                CargadorProperties.obtenerMessages("PV_E_005"),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
            return false;
        }
    }
    
    public boolean reactivar(Proveedor prv) {
        String sql = CargadorProperties.obtenerConfigProveedor("prv.reactivar");
        
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, prv.getNombre());
            ps.setString(2, prv.getTelefono());
            ps.setString(3, prv.getEmail());      // prv_mail
            ps.setString(4, prv.getIdCiudad());   // id_ciudad
            ps.setString(5, prv.getCelular());    // prv_celular
            ps.setString(6, prv.getDireccion());  // prv_direccion
            ps.setString(7, prv.getCedRuc());     // WHERE prv_ruc_ced = ?
            
            int filas = ps.executeUpdate();
            ps.close();
            return filas > 0;
        } catch (SQLException e) {
            System.out.println(CargadorProperties.obtenerMessages("PV_E_007"));
            e.printStackTrace();
            JOptionPane.showMessageDialog(
                null,
                CargadorProperties.obtenerMessages("PV_E_007"),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
            return false;
        }
    }
    
    public Proveedor verificarMD(String cedRuc) {
        String sql = CargadorProperties.obtenerConfigProveedor("prv.verificar");
        Proveedor prv = null;
        
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, cedRuc);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                prv = proveedorCompleto(rs);
            }
            
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(CargadorProperties.obtenerMessages("PV_E_003"));
            e.printStackTrace();
            JOptionPane.showMessageDialog(
                null,
                CargadorProperties.obtenerMessages("PV_E_003"),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
        
        return prv;
    }
    
    public ArrayList<Proveedor> buscarPorCedula(String textoBusqueda) {
        String sql = CargadorProperties.obtenerConfigProveedor("prv.buscar.cedula");
        ArrayList<Proveedor> lista = new ArrayList<>();
        
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, "%" + textoBusqueda + "%");
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                lista.add(proveedorSimple(rs));
            }
            
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(CargadorProperties.obtenerMessages("PV_E_003"));
            e.printStackTrace();
            JOptionPane.showMessageDialog(
                null,
                CargadorProperties.obtenerMessages("PV_E_003"),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
        
        return lista;
    }
    
    public ArrayList<Proveedor> buscarPorNombre(String textoBusqueda) {
        String sql = CargadorProperties.obtenerConfigProveedor("prv.buscar.nombre");
        ArrayList<Proveedor> lista = new ArrayList<>();
        
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, "%" + textoBusqueda + "%");
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                lista.add(proveedorSimple(rs));
            }
            
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(CargadorProperties.obtenerMessages("PV_E_003"));
            e.printStackTrace();
            JOptionPane.showMessageDialog(
                null,
                CargadorProperties.obtenerMessages("PV_E_003"),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
        
        return lista;
    }
    
    public ArrayList<Proveedor> buscarPorTelefono(String textoBusqueda) {
        String sql = CargadorProperties.obtenerConfigProveedor("prv.buscar.telefono");
        ArrayList<Proveedor> lista = new ArrayList<>();
        
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, "%" + textoBusqueda + "%");
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                lista.add(proveedorSimple(rs));
            }
            
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(CargadorProperties.obtenerMessages("PV_E_003"));
            e.printStackTrace();
            JOptionPane.showMessageDialog(
                null,
                CargadorProperties.obtenerMessages("PV_E_003"),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
        
        return lista;
    }
    
    public ArrayList<Proveedor> buscarPorCelular(String textoBusqueda) {
        String sql = CargadorProperties.obtenerConfigProveedor("prv.buscar.celular");
        ArrayList<Proveedor> lista = new ArrayList<>();
        
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, "%" + textoBusqueda + "%");
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                lista.add(proveedorSimple(rs));
            }
            
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(CargadorProperties.obtenerMessages("PV_E_003"));
            e.printStackTrace();
            JOptionPane.showMessageDialog(
                null,
                CargadorProperties.obtenerMessages("PV_E_003"),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
        
        return lista;
    }
    
    public ArrayList<Proveedor> buscarPorEmail(String textoBusqueda) {
        String sql = CargadorProperties.obtenerConfigProveedor("prv.buscar.email");
        ArrayList<Proveedor> lista = new ArrayList<>();
        
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, "%" + textoBusqueda + "%");
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                lista.add(proveedorSimple(rs));
            }
            
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(CargadorProperties.obtenerMessages("PV_E_003"));
            e.printStackTrace();
            JOptionPane.showMessageDialog(
                null,
                CargadorProperties.obtenerMessages("PV_E_003"),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
        
        return lista;
    }
    
    public ArrayList<Proveedor> buscarPorCiudad(String ciudad) {
        String sql = CargadorProperties.obtenerConfigProveedor("prv.buscar.ciudad");
        ArrayList<Proveedor> lista = new ArrayList<>();
        
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, "%" + ciudad + "%");
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                lista.add(proveedorSimple(rs));
            }
            
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(CargadorProperties.obtenerMessages("PV_E_003"));
            e.printStackTrace();
            JOptionPane.showMessageDialog(
                null,
                CargadorProperties.obtenerMessages("PV_E_003"),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
        
        return lista;
    }
    
    public ArrayList<Proveedor> buscarPorDireccion(String textoBusqueda) {
        String sql = CargadorProperties.obtenerConfigProveedor("prv.buscar.direccion");
        ArrayList<Proveedor> lista = new ArrayList<>();
        
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, "%" + textoBusqueda + "%");
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                lista.add(proveedorSimple(rs));
            }
            
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(CargadorProperties.obtenerMessages("PV_E_003"));
            e.printStackTrace();
            JOptionPane.showMessageDialog(
                null,
                CargadorProperties.obtenerMessages("PV_E_003"),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
        
        return lista;
    }
    
    public ArrayList<Proveedor> consultarTodos() {
        String sql = CargadorProperties.obtenerConfigProveedor("prv.consultar.todos");
        ArrayList<Proveedor> lista = new ArrayList<>();
        
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                lista.add(proveedorSimple(rs));
            }
            
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(CargadorProperties.obtenerMessages("PV_E_006"));
            e.printStackTrace();
            JOptionPane.showMessageDialog(
                null,
                CargadorProperties.obtenerMessages("PV_E_006"),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
        
        return lista;
    }
    
    private Proveedor proveedorCompleto(ResultSet rs) throws SQLException {
        Proveedor prv = new Proveedor();
        prv.setNombre(rs.getString(1));        // prv_nombre
        prv.setCedRuc(rs.getString(2));        // prv_ruc_ced
        prv.setTelefono(rs.getString(3));      // prv_telefono
        prv.setCelular(rs.getString(4));       // prv_celular
        prv.setEmail(rs.getString(5));         // prv_mail
        prv.setIdCiudad(rs.getString(6));      // id_ciudad
        prv.setDireccion(rs.getString(7));     // prv_direccion
        prv.setEstado(rs.getString(8));        // estado_prv
        prv.setCiudad(rs.getString(9));        // ciu_descripcion
        prv.setIdProveedor(rs.getString(10));  // id_proveedor
        return prv;
    }
    
    private Proveedor proveedorSimple(ResultSet rs) throws SQLException {
        Proveedor prv = new Proveedor();
        prv.setNombre(rs.getString(1));       // prv_nombre
        prv.setCedRuc(rs.getString(2));       // prv_ruc_ced
        prv.setTelefono(rs.getString(3));     // prv_telefono
        prv.setCelular(rs.getString(4));      // prv_celular
        prv.setEmail(rs.getString(5));        // prv_mail
        prv.setIdCiudad(rs.getString(6));     // id_ciudad
        prv.setCiudad(rs.getString(7));       // ciu_descripcion
        prv.setDireccion(rs.getString(8));    // prv_direccion
        prv.setIdProveedor(rs.getString(9));  // id_proveedor 
        return prv;
    }
    
    public ArrayList<ItemCombo> obtenerCiudades() {
        String sql = CargadorProperties.obtenerConfigProveedor("prv.ciudades");
        ArrayList<ItemCombo> ciudades = new ArrayList<>();
        
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                ItemCombo item = new ItemCombo(
                    rs.getString("id_ciudad"),
                    rs.getString("ciu_descripcion")
                );
                ciudades.add(item);
            }
            
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(
                null,
                "Error al cargar las ciudades",
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
        
        return ciudades;
    }
    
    public Proveedor verificarPorIdMD(String idProveedor) {
        String sql = CargadorProperties.obtenerConfigProveedor("prv.verificar.porId");
        Proveedor prv = null;
        
        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, idProveedor);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                prv = proveedorCompleto(rs);
            }
            
            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(CargadorProperties.obtenerMessages("PV_E_003"));
            e.printStackTrace();
        }
        
        return prv;
    }
}

