package DP;

import MD.ProductoMD;
import GUI.ItemCombo;
import java.util.ArrayList;

public class Producto {
    private String codigo;
    private String descripcion;
    private String idCategoria;
    private String categoria;
    private String umCompra;
    private String nombreUmCompra; 
    private String umVenta;
    private String nombreUmVenta;
    private double precioCompra;
    private double precioVenta;
    private int saldoIni;
    private int qtyIngresos;
    private int qtyEgresos;
    private int saldoFin;
    private String imagen;
    private String estado;
    
    public Producto(){
        this.estado = "ACT";
        this.qtyEgresos = 0;
        this.qtyIngresos = 0;
        this.saldoIni = 0;
        this.saldoFin = 0;
    }
    
    //Getters
    public String getCodigo(){
        return codigo;
    }
    
    public String getDescripcion(){
        return descripcion;
    }
    
    public String getIdCategoria(){
        return idCategoria;
    }
    
    public String getCategoria(){
        return categoria;
    }
    
    public String getUmCompra(){
        return umCompra;
    }
    
    public String getNombreUmCompra() {
        return nombreUmCompra;
    }
    
    public String getUmVenta(){
        return umVenta;
    }
    
    public String getNombreUmVenta() {
        return nombreUmVenta;
    }
    
    public double getPrecioCompra(){
        return precioCompra;
    }
    
    public double getPrecioVenta(){
        return precioVenta;
    }
    
    public int getSaldoIni(){
        return saldoIni;
    }
    
    public int getQtyIngresos(){
        return qtyIngresos;
    }
    
    public int getQtyEgresos(){
        return qtyEgresos;
    }
    
    public int getSaldoFin(){
        return saldoFin;
    }
    
    public String getImagen(){
        return imagen;
    }
    
    public String getEstado(){
        return estado;
    }
    
    //Setters
    public void setCodigo(String codigo){
        this.codigo = codigo;
    }
   
    public void setDescripcion(String descripcion){
        this.descripcion = descripcion;
    }
    
    public void setIdCategoria(String idCategoria){
        this.idCategoria = idCategoria;
    }
    
    public void setCategoria(String categoria){
        this.categoria = categoria;
    }
    
    public void setUmCompra(String umCompra){
        this.umCompra = umCompra;
    }
    
    public void setNombreUmCompra(String nombreUmCompra) {
        this.nombreUmCompra = nombreUmCompra;
    }
    
    public void setUmVenta(String umVenta) {
        this.umVenta = umVenta;
    }
    
    public void setNombreUmVenta(String nombreUmVenta) {
        this.nombreUmVenta = nombreUmVenta;
    }
    
    public void setPrecioCompra (double precioCompra){
        this.precioCompra = precioCompra;
    }
    
    public void setPrecioVenta (double precioVenta){
        this.precioVenta = precioVenta;
    }
    
    public void setSaldoIni (int saldoIni){
        this.saldoIni = saldoIni;
    }
    
    public void setQtyIngresos (int qtyIngresos){
        this.qtyIngresos = qtyIngresos;
    }
    
    public void setQtyEgresos (int qtyEgresos){
        this.qtyEgresos = qtyEgresos;
    }
    
    public void setSaldoFin (int saldoFin){
        this.saldoFin = saldoFin;
    }
    
    public void setImagen (String imagen){
        this.imagen = imagen;
    }
    
    public void setEstado (String estado){
        this.estado = estado;
    }
    
    public String generarCodigoDP() {
        ProductoMD proMD = new ProductoMD();
        return proMD.generarCodigo(this.idCategoria);
    }
    
    public Producto verificarPorNombreDP(String descripcion) {
        ProductoMD proMD = new ProductoMD();
        return proMD.verificarPorNombre(descripcion);
    }
    
    public Producto verificarPorCodigoDP(String codigo) {
        ProductoMD proMD = new ProductoMD();
        return proMD.verificarPorCodigo(codigo);
    }
    
    public ArrayList<Producto> buscarPorCodigoDP(String textoBusqueda) {
        ProductoMD proMD = new ProductoMD();
        return proMD.buscarPorCodigo(textoBusqueda);
    }
    
    public ArrayList<Producto> buscarPorNombreDP(String textoBusqueda) {
        ProductoMD proMD = new ProductoMD();
        return proMD.buscarPorNombre(textoBusqueda);
    }
    
    public ArrayList<Producto> buscarPorCategoriaDP(String idCategoria) {
        ProductoMD proMD = new ProductoMD();
        return proMD.buscarPorCategoria(idCategoria);
    }
    
    public ArrayList<Producto> buscarPorUmCompraDP(String idUnidad) {
        ProductoMD proMD = new ProductoMD();
        return proMD.buscarPorUmCompra(idUnidad);
    }
    
    public ArrayList<Producto> buscarPorUmVentaDP(String idUnidad) {
        ProductoMD proMD = new ProductoMD();
        return proMD.buscarPorUmVenta(idUnidad);
    }
    
    public boolean eliminarDP() {
        ProductoMD proMD = new ProductoMD();
        return proMD.eliminar(this);
    }
    
    public boolean grabarDP() {
        ProductoMD proMD = new ProductoMD();

        if (this.codigo != null && !this.codigo.trim().isEmpty()) {
            Producto existePorCodigo = proMD.verificarPorCodigo(this.codigo);
            if (existePorCodigo != null) {
                return proMD.modificar(this);
            } else {
                Producto existePorNombre = proMD.verificarPorNombre(this.descripcion);
                if (existePorNombre != null) return false;

                return proMD.insertar(this);
            }
        }
        Producto existe = proMD.verificarPorNombre(this.descripcion);
        if (existe != null) return false;

        this.codigo = proMD.generarCodigo(this.idCategoria);
        return proMD.insertar(this);
    }

    
    public ArrayList<Producto> consultarTodos() {
        ProductoMD proMD = new ProductoMD();
        return proMD.consultarTodos();
    }
    
    public ArrayList<ItemCombo> obtenerCategoriasDP() {
        ProductoMD proMD = new ProductoMD();
        return proMD.obtenerCategorias();
    }
    
    public ArrayList<ItemCombo> obtenerUnidadesMedidaDP() {
        ProductoMD proMD = new ProductoMD();
        return proMD.obtenerUnidadesMedida();
    }
    
    public ArrayList<Producto> buscarPorNombreCategoriaDP(String nombreCategoria) {
        ProductoMD proMD = new ProductoMD();
        ArrayList<ItemCombo> categorias = proMD.obtenerCategorias();

        for (ItemCombo cat : categorias) {
            if (cat.getDescripcion().toLowerCase().contains(nombreCategoria.toLowerCase())) {
                return proMD.buscarPorCategoria(cat.getId());
            }
        }

        return new ArrayList<>();
    }

    public ArrayList<Producto> buscarPorNombreUmCompraDP(String nombreUm) {
        ProductoMD proMD = new ProductoMD();
        ArrayList<ItemCombo> unidades = proMD.obtenerUnidadesMedida();

        for (ItemCombo um : unidades) {
            if (um.getDescripcion().toLowerCase().contains(nombreUm.toLowerCase())) {
                return proMD.buscarPorUmCompra(um.getId());
            }
        }

        return new ArrayList<>();
    }

    public ArrayList<Producto> buscarPorNombreUmVentaDP(String nombreUm) {
        ProductoMD proMD = new ProductoMD();
        ArrayList<ItemCombo> unidades = proMD.obtenerUnidadesMedida();

        for (ItemCombo um : unidades) {
            if (um.getDescripcion().toLowerCase().contains(nombreUm.toLowerCase())) {
                return proMD.buscarPorUmVenta(um.getId());
            }
        }

        return new ArrayList<>();
    }
}
