package DP;

import MD.ProxOcMD;
import java.util.ArrayList;

/**
 *
 * @author danab
 */
public class ProxOc {

    private String codigoCompra;
    private String codigoProducto;
    private int cantidad;
    private double precioCompra;
    private double subtotalProducto;
    private String estado;

    public ProxOc() {
        this.estado = "APR"; // Estado por defecto
    }

    // ==========================
    // Cálculo de subtotal
    // ==========================
    public void calcularSubtotal() {
        this.subtotalProducto = this.cantidad * this.precioCompra;
    }

    // ==========================
    // Getters y Setters
    // ==========================

    public String getCodigoCompra() { return codigoCompra; }
    public void setCodigoCompra(String codigoCompra) { this.codigoCompra = codigoCompra; }

    public String getCodigoProducto() { return codigoProducto; }
    public void setCodigoProducto(String codigoProducto) { this.codigoProducto = codigoProducto; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
        calcularSubtotal();
    }

    public double getPrecioCompra() { return precioCompra; }
    public void setPrecioCompra(double precioCompra) {
        this.precioCompra = precioCompra;
        calcularSubtotal();
    }

    public double getSubtotalProducto() { return subtotalProducto; }
    public void setSubtotalProducto(double subtotalProducto) {
        this.subtotalProducto = subtotalProducto;
    }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    // ==========================
    // Métodos de conexión con MD
    // ==========================

    public boolean insertar() {
        ProxOcMD pxoMD = new ProxOcMD();
        return pxoMD.insertar(this);
    }

    public boolean modificar() {
        ProxOcMD pxoMD = new ProxOcMD();
        return pxoMD.modificar(this);
    }

    public ArrayList<ProxOc> consultarPorCompra(ProxOc pxo) {
        ProxOcMD pxoMD = new ProxOcMD();
        return pxoMD.consultarPorCompra(pxo);
    }

    public ArrayList<ProxOc> consultarPorCompraABI(ProxOc pxo) {
        ProxOcMD pxoMD = new ProxOcMD();
        return pxoMD.consultarPorCompraABI(pxo);
    }

    // Elimina un producto específico de la compra
    public boolean eliminar() {
        ProxOcMD pxoMD = new ProxOcMD();
        return pxoMD.eliminarProducto(this);
    }

    // Elimina todos los productos de la compra
    public boolean eliminarPorCompra() {
        ProxOcMD pxoMD = new ProxOcMD();
        return pxoMD.eliminarPorCompra(this);
    }
}
