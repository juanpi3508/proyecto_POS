package DP;

import GUI.ItemCombo;
import MD.ClienteMD;
import java.util.ArrayList;
        
public class Cliente {
    private String idCliente;
    private String nombre;
    private String cedRuc;
    private String telefono;
    private String email;
    private String idCiudad;
    private String ciudad;
    private String direccion;
    private String estado;
    
    public Cliente(){
        this.estado = "ACT";
    }
    
    //Getters
    public String getIdCliente(){
        return idCliente;
    }
    
    public String getNombre(){
        return nombre;
    }
    
    public String getCedRuc(){
        return cedRuc;
    }
    
    public String getTelefono(){
        return telefono;
    }
    
    public String getEmail(){
        return email;
    }
    
    public String getIdCiudad(){
        return idCiudad;
    }
    
    public String getCiudad(){
        return ciudad;
    }
    
    public String getDireccion(){
        return direccion;
    }
    
    public String getEstado(){
        return estado;
    }
    
    //Setters
    public void setIdCliente(String idCliente){
        this.idCliente = idCliente;
    }
    
    public void setNombre(String nombre){
        this.nombre = nombre;
    }
    
    public void setCedRuc(String cedRuc){
        this.cedRuc = cedRuc;
    }
    
    public void setTelefono(String telefono){
        this.telefono = telefono;
    }
    
    public void setEmail(String email){
        this.email = email;
    }
    
    public void setIdCiudad(String idCiudad){
        this.idCiudad = idCiudad;
    }
    
    public void setCiudad(String ciudad){
        this.ciudad = ciudad;
    }
    
    public void setDireccion(String direccion){
        this.direccion = direccion;
    }
    
    public void setEstado(String estado){
        this.estado = estado;
    }
    
    public Cliente verificarDP (String cedRuc){
        ClienteMD cliMD = new ClienteMD();
        return cliMD.verificarMD(cedRuc);
    }
    
    public boolean eliminarDP(){
        ClienteMD cliMD = new ClienteMD();
        return cliMD.eliminar(this);
    }
        
    public boolean reactivarDP() {
        ClienteMD cliMD = new ClienteMD();
        return cliMD.reactivar(this);
    }
    
    public boolean grabarDP(){
        ClienteMD cliMD = new ClienteMD();
        Cliente existe = cliMD.verificarMD(this.cedRuc);
        
        if (existe == null){
            return cliMD.insertar(this);
        } else {
            return cliMD.modificar(this);
        }
    }
    
    public ArrayList<Cliente> buscarPorCedulaDP(String textoBusqueda){
        ClienteMD cliMD = new ClienteMD();
        return cliMD.buscarPorCedula(textoBusqueda);
    }
    
    public ArrayList<Cliente> buscarPorNombreDP(String textoBusqueda){
        ClienteMD cliMD = new ClienteMD();
        return cliMD.buscarPorNombre(textoBusqueda);
    }
    
    public ArrayList<Cliente> buscarPorTelefonoDP(String textoBusqueda){
        ClienteMD cliMD = new ClienteMD();
        return cliMD.buscarPorTelefono(textoBusqueda);
    }
    
    public ArrayList<Cliente> buscarPorEmailDP(String textoBusqueda){
        ClienteMD cliMD = new ClienteMD();
        return cliMD.buscarPorEmail(textoBusqueda);
    }
    
    public ArrayList<Cliente> buscarPorCiudadDP(String idCiudad){
        ClienteMD cliMD = new ClienteMD();
        return cliMD.buscarPorCiudad(idCiudad);
    }
   
    public ArrayList<Cliente> buscarPorDireccionDP(String textoBusqueda){
        ClienteMD cliMD = new ClienteMD();
        return cliMD.buscarPorDireccion(textoBusqueda);
    }
    
    public ArrayList<Cliente> consultarTodos(){
        ClienteMD cliMD = new ClienteMD();
        return cliMD.consultarTodos();
    }
    
    public ArrayList<ItemCombo> obtenerCiudadesDP() {
        ClienteMD cliMD = new ClienteMD();
        return cliMD.obtenerCiudades();
    }
    
    //Adicional para consulta especifica factura
    public Cliente verificarPorIdDP(String idCliente){
        ClienteMD cliMD = new ClienteMD();
        return cliMD.verificarPorIdMD(idCliente);
    }
}
