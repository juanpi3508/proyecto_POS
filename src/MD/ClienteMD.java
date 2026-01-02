package MD;

import DP.Cliente;
import GUI.ItemCombo;
import java.sql.Connection;
import util.CargadorProperties;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class ClienteMD {
    private Connection conexion;
    
    public ClienteMD(){
        this.conexion = ConexionBD.getConexion();
    }
    
    public boolean insertar(Cliente cli){
        String sql = CargadorProperties.obtenerConfigCliente("cli.insertar");
        
        try{
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, cli.getNombre());
            ps.setString(2, cli.getCedRuc());
            ps.setString(3, cli.getTelefono());
            ps.setString(4, cli.getEmail());
            ps.setString(5, cli.getIdCiudad());
            ps.setString(6, cli.getDireccion());
            
            int filas = ps.executeUpdate();
            ps.close();
            return filas > 0;
        } catch (SQLException e){
            System.out.println(CargadorProperties.obtenerMessages("CL_E_002"));
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                CargadorProperties.obtenerMessages("CL_E_002"), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    public boolean modificar(Cliente cli){
        String sql = CargadorProperties.obtenerConfigCliente("cli.modificar");
        
        try{
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, cli.getNombre());
            ps.setString(2, cli.getTelefono());
            ps.setString(3, cli.getEmail());
            ps.setString(4, cli.getIdCiudad());
            ps.setString(5, cli.getDireccion());
            ps.setString(6, cli.getCedRuc());
            
            int filas = ps.executeUpdate();
            ps.close();
            return filas > 0;
        } catch (SQLException e){
            System.out.println(CargadorProperties.obtenerMessages("CL_E_004"));
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                CargadorProperties.obtenerMessages("CL_E_004"), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    public boolean eliminar(Cliente cli){
        String sql = CargadorProperties.obtenerConfigCliente("cli.eliminar");
        
        try{
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, cli.getCedRuc());
            
            int filas = ps.executeUpdate();
            ps.close();
            return filas > 0;
        } catch (SQLException e){
            System.out.println(CargadorProperties.obtenerMessages("CL_E_005"));
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                CargadorProperties.obtenerMessages("CL_E_005"), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    public Cliente verificarMD(String cedRuc){
        String sql = CargadorProperties.obtenerConfigCliente("cli.verificar");
        Cliente cli = null;

        try{
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, cedRuc);

            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                cli = clienteCompleto(rs);
            }

            rs.close();
            ps.close();
        } catch (SQLException e){
            System.out.println(CargadorProperties.obtenerMessages("CL_E_003"));
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                CargadorProperties.obtenerMessages("CL_E_003"), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }

        return cli;
    }
    
    public ArrayList<Cliente> buscarPorCedula(String textoBusqueda){
        String sql = CargadorProperties.obtenerConfigCliente("cli.buscar.cedula");
        ArrayList<Cliente> lista = new ArrayList<>();
        
        try{
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, "%" + textoBusqueda + "%");
            
            ResultSet rs = ps.executeQuery();
            
            while(rs.next()){
                lista.add(clienteSimple(rs));
            }
            
            rs.close();
            ps.close();
        } catch (SQLException e){
            System.out.println(CargadorProperties.obtenerMessages("CL_E_003"));
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                CargadorProperties.obtenerMessages("CL_E_003"), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
        
        return lista;
    }
    
    public ArrayList<Cliente> buscarPorNombre(String textoBusqueda){
        String sql = CargadorProperties.obtenerConfigCliente("cli.buscar.nombre");
        ArrayList<Cliente> lista = new ArrayList<>();
        
        try{
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, "%" + textoBusqueda + "%");
            
            ResultSet rs = ps.executeQuery();
            
            while(rs.next()){
                lista.add(clienteSimple(rs));
            }
            
            rs.close();
            ps.close();
        } catch (SQLException e){
            System.out.println(CargadorProperties.obtenerMessages("CL_E_003"));
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                CargadorProperties.obtenerMessages("CL_E_003"), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
        
        return lista;
    }
    
    public ArrayList<Cliente> buscarPorTelefono(String textoBusqueda){
        String sql = CargadorProperties.obtenerConfigCliente("cli.buscar.telefono");
        ArrayList<Cliente> lista = new ArrayList<>();
        
        try{
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, "%" + textoBusqueda + "%");
            
            ResultSet rs = ps.executeQuery();
            
            while(rs.next()){
                lista.add(clienteSimple(rs));
            }
            
            rs.close();
            ps.close();
        } catch (SQLException e){
            System.out.println(CargadorProperties.obtenerMessages("CL_E_003"));
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                CargadorProperties.obtenerMessages("CL_E_003"), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
        
        return lista;
    }
    
    public ArrayList<Cliente> buscarPorEmail(String textoBusqueda){
        String sql = CargadorProperties.obtenerConfigCliente("cli.buscar.email");
        ArrayList<Cliente> lista = new ArrayList<>();
        
        try{
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, "%" + textoBusqueda + "%");
            
            ResultSet rs = ps.executeQuery();
            
            while(rs.next()){
                lista.add(clienteSimple(rs));
            }
            
            rs.close();
            ps.close();
        } catch (SQLException e){
            System.out.println(CargadorProperties.obtenerMessages("CL_E_003"));
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                CargadorProperties.obtenerMessages("CL_E_003"), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
        
        return lista;
    }
    
    public ArrayList<Cliente> buscarPorCiudad(String ciudad){
        String sql = CargadorProperties.obtenerConfigCliente("cli.buscar.ciudad");
        ArrayList<Cliente> lista = new ArrayList<>();
        
        try{
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, "%" + ciudad + "%");
            
            ResultSet rs = ps.executeQuery();
            
            while(rs.next()){
                lista.add(clienteSimple(rs));
            }
            
            rs.close();
            ps.close();
        } catch (SQLException e){
            System.out.println(CargadorProperties.obtenerMessages("CL_E_003"));
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                CargadorProperties.obtenerMessages("CL_E_003"), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
        
        return lista;
    }
    
    public ArrayList<Cliente> buscarPorDireccion(String textoBusqueda){
        String sql = CargadorProperties.obtenerConfigCliente("cli.buscar.direccion");
        ArrayList<Cliente> lista = new ArrayList<>();
        
        try{
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, "%" + textoBusqueda + "%");
            
            ResultSet rs = ps.executeQuery();
            
            while(rs.next()){
                lista.add(clienteSimple(rs));
            }
            
            rs.close();
            ps.close();
        } catch (SQLException e){
            System.out.println(CargadorProperties.obtenerMessages("CL_E_003"));
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                CargadorProperties.obtenerMessages("CL_E_003"), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
        
        return lista;
    }
    
    public ArrayList<Cliente> consultarTodos(){
        String sql = CargadorProperties.obtenerConfigCliente("cli.consultar.todos");
        ArrayList<Cliente> lista = new ArrayList<>();
        
        try{
            PreparedStatement ps = conexion.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            
            while(rs.next()){
                lista.add(clienteSimple(rs));
            }
            
            rs.close();
            ps.close();
        } catch (SQLException e){
            System.out.println(CargadorProperties.obtenerMessages("CL_E_006"));
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                CargadorProperties.obtenerMessages("CL_E_006"), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
        
        return lista;
    }
    
    private Cliente clienteCompleto(ResultSet rs) throws SQLException {
        Cliente cli = new Cliente();
        cli.setNombre(rs.getString(1));           
        cli.setCedRuc(rs.getString(2));           
        cli.setTelefono(rs.getString(3));         
        cli.setEmail(rs.getString(4));            
        cli.setIdCiudad(rs.getString(5));         
        cli.setDireccion(rs.getString(6));        
        cli.setEstado(rs.getString(7));           
        cli.setCiudad(rs.getString(8));
        cli.setIdCliente(rs.getString(9));         
        return cli;
    }
    
    private Cliente clienteSimple(ResultSet rs) throws SQLException {
        Cliente cli = new Cliente();
        cli.setNombre(rs.getString(1));           
        cli.setCedRuc(rs.getString(2));           
        cli.setTelefono(rs.getString(3));         
        cli.setEmail(rs.getString(4));            
        cli.setIdCiudad(rs.getString(5));         
        cli.setCiudad(rs.getString(6));           
        cli.setDireccion(rs.getString(7));        
        return cli;
    }
    
    public ArrayList<ItemCombo> obtenerCiudades() {
        String sql = CargadorProperties.obtenerConfigCliente("cli.ciudades");
        ArrayList<ItemCombo> ciudades = new ArrayList<>();

        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
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
            JOptionPane.showMessageDialog(null, 
                "Error al cargar las ciudades", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }

        return ciudades;
    }
    
    //Adicional para consulta especifica factura
    
    //Verifica cliente por id_cliente (CLI0001)
    public Cliente verificarPorIdMD(String idCliente) {
        String sql = CargadorProperties.obtenerConfigCliente("cli.verificar.porId");
        Cliente cli = null;

        try {
            PreparedStatement ps = conexion.prepareStatement(sql);
            ps.setString(1, idCliente);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                cli = clienteCompleto(rs);
            }

            rs.close();
            ps.close();
        } catch (SQLException e) {
            System.out.println(CargadorProperties.obtenerMessages("CL_E_003"));
            e.printStackTrace();
        }

        return cli;
    }
}
