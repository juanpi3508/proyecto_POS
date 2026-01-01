package DP;

import MD.FacturaMD;
import MD.ProxFacMD;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Factura {
    private String codigo;
    private String codigoCliente;  // ← MANTENIDO como cedulaCliente
    private String nombreEmpresa;
    private String emailEmpresa;
    private String telefonoEmpresa;
    private LocalDateTime fechaHora;
    private double subtotal;
    private double iva;
    private double total;
    private String tipo;
    private String estado;
    private ArrayList<ProxFac> productos;
    
    public Factura() {
        this.nombreEmpresa = "KoKo Market";
        this.emailEmpresa = "ventas@kokomarket.com.ec";
        this.telefonoEmpresa = "022876543";
        this.tipo = "POS";
        this.estado = "APR";
        this.productos = new ArrayList<>();
        this.fechaHora = LocalDateTime.now();
    }
    
    // ========== MÉTODOS DE CÁLCULO ==========
    /**
     * Calcula el subtotal sumando los productos
     */
    public void calcularSubtotal() {
        this.subtotal = 0.0;
        for (ProxFac pxf : productos) {
            this.subtotal += pxf.getSubtotalProducto();
        }
    }
    
    /**
     * Calcula IVA (15%)
     */
    public void calcularIVA() {
        this.iva = Math.round(this.subtotal * 0.15 * 100.0) / 100.0;
    }
    
    /**
     * Calcula Total
     */
    public void calcularTotal() {
        this.total = this.subtotal + this.iva;
    }
    
    /**
     * Recalcula todos los totales (subtotal, IVA, total)
     */
    public void recalcularTotales() {
        calcularSubtotal();
        calcularIVA();
        calcularTotal();
    }
    
    /**
     * Agrega un producto a la factura
     */
    public void agregarProducto(ProxFac producto) {
        this.productos.add(producto);
        recalcularTotales();
    }
    
    // ========== MÉTODOS DP ==========
    
    /**
     * Consulta todas las facturas activas
     */
    public ArrayList<Factura> consultarTodos() {
        FacturaMD facMD = new FacturaMD();
        return facMD.consultarTodos();
    }
    
    /**
     * Graba la factura completa (cabecera + productos)
     */
    public boolean grabarDP() {
        recalcularTotales();

        // Llamar directamente a FacturaMD
        FacturaMD facMD = new FacturaMD();
        String codigoGenerado = facMD.insertarFacturaCompleta(this);

        if (codigoGenerado != null) {
            // Actualizar el código en este objeto
            this.codigo = codigoGenerado;
            return true;
        } else {
            return false;
        }
    }
        /**
    * Elimina la factura (inactiva tanto la cabecera como los productos)
    */
   public boolean eliminar(Factura fac) {
       try {
           // 1. Primero eliminar (inactivar) todos los productos de la factura
           ProxFac pxf = new ProxFac();
           pxf.setCodigoFac(fac.getCodigo());

           ProxFacMD pxfMD = new ProxFacMD();
           boolean productosEliminados = pxfMD.eliminarPorFactura(pxf);

           // 2. Luego eliminar (inactivar) la cabecera de la factura
           FacturaMD facMD = new FacturaMD();
           boolean facturaEliminada = facMD.eliminar(fac);

           // Retorna true solo si ambas operaciones fueron exitosas
           return productosEliminados && facturaEliminada;

       } catch (Exception e) {
           System.out.println("Error al eliminar la factura: " + e.getMessage());
           e.printStackTrace();
           return false;
       }
   }
   
   /**
    * Consulta facturas asociadas a un cliente específico
    */
   public ArrayList<Factura> consultarPorParametro(Cliente cli) {
       FacturaMD facMD = new FacturaMD();
       return facMD.consultarPorParametro(cli);
   }
   
    // ========== GETTERS Y SETTERS ==========
    
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    
    public String getCedRucCliente() { return codigoCliente; }

    public void setCedRucCliente(String cedRucCliente) {  this.codigoCliente = cedRucCliente;
    }
    
    public String getNombreEmpresa() { return nombreEmpresa; }
    public void setNombreEmpresa(String nombreEmpresa) { this.nombreEmpresa = nombreEmpresa; }
    
    public String getEmailEmpresa() { return emailEmpresa; }
    public void setEmailEmpresa(String emailEmpresa) { this.emailEmpresa = emailEmpresa; }
    
    public String getTelefonoEmpresa() { return telefonoEmpresa; }
    public void setTelefonoEmpresa(String telefonoEmpresa) { this.telefonoEmpresa = telefonoEmpresa; }
    
    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }
    
    public double getSubtotal() { return subtotal; }
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }
    
    public double getIva() { return iva; }
    public void setIva(double iva) { this.iva = iva; }
    
    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }
    
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    
    public ArrayList<ProxFac> getProductos() { return productos; }
    public void setProductos(ArrayList<ProxFac> productos) { 
        this.productos = productos;
        recalcularTotales();
    }
}