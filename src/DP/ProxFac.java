/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DP;

import MD.ProxFacMD;
import java.util.ArrayList;
/**
 *
 * @author danab
 */

public class ProxFac {
    private String codigoFac;
    private String codigoProd;
    private int cantidad;
    private double precioVenta;
    private double subtotalProducto;
    private String estado;
    
    public ProxFac() {
        this.estado = "APR";  // ← Estado por defecto
    }
    
    // Método para calcular subtotal del producto
    public void calcularSubtotal() {
        this.subtotalProducto = this.cantidad * this.precioVenta;
    }
    
    // Getters y Setters
    public String getCodigoFac() { return codigoFac; }
    public void setCodigoFac(String codigoFac) { this.codigoFac = codigoFac; }
    
    public String getCodigoProd() { return codigoProd; }
    public void setCodigoProd(String codigoProd) { this.codigoProd = codigoProd; }
    
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { 
        this.cantidad = cantidad;
        calcularSubtotal();
    }
    
    public double getPrecioVenta() { return precioVenta; }
    public void setPrecioVenta(double precioVenta) { 
        this.precioVenta = precioVenta;
        calcularSubtotal();
    }
    
    public double getSubtotalProducto() { return subtotalProducto; }
    public void setSubtotalProducto(double subtotalProducto) { 
        this.subtotalProducto = subtotalProducto; 
    }
    
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    
    /**
    * Consulta todos los productos asociados a esta factura
    */
    public ArrayList<ProxFac> consultarPorFactura(ProxFac pxf) {
       ProxFacMD pxfMD = new ProxFacMD();
       return pxfMD.consultarPorFactura(pxf);
    }
   
    //Elimina un producto en especifico
    public boolean eliminar() {
        ProxFacMD pxfMD = new ProxFacMD();
        return pxfMD.eliminarProducto(this);
    }
    
    //Elimina todos los productos de la factura
    public boolean eliminarPorFactura() {
        ProxFacMD pxfMD = new ProxFacMD();
        return pxfMD.eliminarPorFactura(this);
    }
}
