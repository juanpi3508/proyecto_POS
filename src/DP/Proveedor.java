package DP;

import GUI.ItemCombo;
import MD.ProveedorMD;
import java.util.ArrayList;

public class Proveedor {
    private String idProveedor;
    private String nombre;
    private String cedRuc;
    private String telefono;
    private String celular;
    private String email;
    private String idCiudad;
    private String ciudad;
    private String direccion;
    private String estado;
    
    public Proveedor() {
        this.estado = "ACT";
    }
    
    // Getters
    public String getIdProveedor() {
        return idProveedor;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public String getCedRuc() {
        return cedRuc;
    }
    
    public String getTelefono() {
        return telefono;
    }
    
    public String getCelular() {
        return celular;
    }
    
    public String getEmail() {
        return email;
    }
    
    public String getIdCiudad() {
        return idCiudad;
    }
    
    public String getCiudad() {
        return ciudad;
    }
    
    public String getDireccion() {
        return direccion;
    }
    
    public String getEstado() {
        return estado;
    }
    
    // Setters
    public void setIdProveedor(String idProveedor) {
        this.idProveedor = idProveedor;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public void setCedRuc(String cedRuc) {
        this.cedRuc = cedRuc;
    }
    
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    
    public void setCelular(String celular) {
        this.celular = celular;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public void setIdCiudad(String idCiudad) {
        this.idCiudad = idCiudad;
    }
    
    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }
    
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    
    public void setEstado(String estado) {
        this.estado = estado;
    }
    
    // ==== Métodos de negocio (DP → MD) ====
    
    public Proveedor verificarDP(String cedRuc) {
        ProveedorMD prvMD = new ProveedorMD();
        return prvMD.verificarMD(cedRuc);
    }
    
    public boolean eliminarDP() {
        ProveedorMD prvMD = new ProveedorMD();
        return prvMD.eliminar(this);
    }
    
    public boolean reactivarDP() {
        ProveedorMD prvMD = new ProveedorMD();
        return prvMD.reactivar(this);
    }
    
    public boolean grabarDP() {
        ProveedorMD prvMD = new ProveedorMD();
        Proveedor existe = prvMD.verificarMD(this.cedRuc);
        
        if (existe == null) {
            return prvMD.insertar(this);
        } else {
            return prvMD.modificar(this);
        }
    }
    
    public ArrayList<Proveedor> buscarPorCedulaDP(String textoBusqueda) {
        ProveedorMD prvMD = new ProveedorMD();
        return prvMD.buscarPorCedula(textoBusqueda);
    }
    
    public ArrayList<Proveedor> buscarPorNombreDP(String textoBusqueda) {
        ProveedorMD prvMD = new ProveedorMD();
        return prvMD.buscarPorNombre(textoBusqueda);
    }
    
    public ArrayList<Proveedor> buscarPorTelefonoDP(String textoBusqueda) {
        ProveedorMD prvMD = new ProveedorMD();
        return prvMD.buscarPorTelefono(textoBusqueda);
    }
    
    public ArrayList<Proveedor> buscarPorCelularDP(String textoBusqueda) {
        ProveedorMD prvMD = new ProveedorMD();
        return prvMD.buscarPorCelular(textoBusqueda);
    }
    
    public ArrayList<Proveedor> buscarPorEmailDP(String textoBusqueda) {
        ProveedorMD prvMD = new ProveedorMD();
        return prvMD.buscarPorEmail(textoBusqueda);
    }
    
    public ArrayList<Proveedor> buscarPorCiudadDP(String idCiudad) {
        ProveedorMD prvMD = new ProveedorMD();
        return prvMD.buscarPorCiudad(idCiudad);
    }
    
    public ArrayList<Proveedor> buscarPorDireccionDP(String textoBusqueda) {
        ProveedorMD prvMD = new ProveedorMD();
        return prvMD.buscarPorDireccion(textoBusqueda);
    }
    
    public ArrayList<Proveedor> consultarTodos() {
        ProveedorMD prvMD = new ProveedorMD();
        return prvMD.consultarTodos();
    }
    
    public ArrayList<ItemCombo> obtenerCiudadesDP() {
        ProveedorMD prvMD = new ProveedorMD();
        return prvMD.obtenerCiudades();
    }
    
    // Adicional para consulta específica (factura, etc.)
    public Proveedor verificarPorIdDP(String idProveedor) {
        ProveedorMD prvMD = new ProveedorMD();
        return prvMD.verificarPorIdMD(idProveedor);
    }
}
