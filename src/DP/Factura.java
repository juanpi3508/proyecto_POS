package DP;

import MD.FacturaMD;
import MD.ProxFacMD;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Factura {
    private String codigo;
    private String codigoCliente; //Equivalente a id Cliente
    private LocalDateTime fechaHora;
    private double subtotal;
    private double iva;
    private double total;
    private String tipo;
    private String estado;
    private ArrayList<ProxFac> productos;
    
    public Factura() {
        this.tipo = "POS"; //Directamente Point of Sale
        this.estado = "APR"; //Se crean en aporbadas
        this.productos = new ArrayList<>();
        this.fechaHora = LocalDateTime.now();
    }
    
    //Setters y Getters
    
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    
    public String getCodigoCliente() { return codigoCliente; }
    public void setCodigoCliente(String cedRucCliente) { this.codigoCliente = cedRucCliente; }
    
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
    
    //Metodos de calculo
    
    //Subtotal
    public void calcularSubtotal() {
        this.subtotal = 0.0;
        for (ProxFac pxf : productos) {
            this.subtotal += pxf.getSubtotalProducto();
        }
    }
    
    //IVA (el 15%)
    public void calcularIVA() {
        this.iva = Math.round(this.subtotal * 0.15 * 100.0) / 100.0;
    }
    
    //Total
    public void calcularTotal() {
        this.total = this.subtotal + this.iva;
    }
    
    //Recalcula todo
    public void recalcularTotales() {
        calcularSubtotal();
        calcularIVA();
        calcularTotal();
    }
    
    
    //Metodos para GUI (conectar con MD)
    
    //Agregar producto
    public void agregarProducto(ProxFac producto) {
        this.productos.add(producto);
        recalcularTotales();
    }
    
    //Trae el codigo de la factura (F#####...)
    public static String generarCodigo() {
        FacturaMD facMD = new FacturaMD();
        return facMD.generarCodigoFactura();
    }
    
    //Inserta factura completa (cabecera y detalle)
    public String insertar() {
        recalcularTotales();
        FacturaMD facMD = new FacturaMD();
        String codigoGenerado = facMD.insertarFacturaCompleta(this);
        
        if (codigoGenerado != null) {
            this.codigo = codigoGenerado;
        }
        return codigoGenerado;
    }
    
    //Consulta todas las facturas aprobadas
    public ArrayList<Factura> consultarTodos() {
        FacturaMD facMD = new FacturaMD();
        return facMD.consultarTodos();
    }
    
    //Consulta una factura en específico (para mostrar el detalle en modificar)
    public Factura consultarPorParametro(Factura fac) {
        FacturaMD facMD = new FacturaMD();
        return facMD.consultarPorCodigo(fac);
    }
    
    //Consulta especifica (solo cliente)
    public ArrayList<Factura> consultarPorParametro(Cliente cli) {
        FacturaMD facMD = new FacturaMD();
        return facMD.consultarPorParametro(cli);
    }
    
    //Modifica cabecera y actualiza detalle
    public boolean modificar() {
        recalcularTotales();
        FacturaMD facMD = new FacturaMD();

        try {

            if (!facMD.modificar(this)) {
                return false;
            }

            ProxFac pxfConsulta = new ProxFac();
            pxfConsulta.setCodigoFac(this.codigo);
            ProxFacMD pxfMD = new ProxFacMD();
            ArrayList<ProxFac> productosEnBD = pxfMD.consultarPorFactura(pxfConsulta);

            for (ProxFac prodBD : productosEnBD) {
                boolean existe = false;
                for (ProxFac prodActual : this.productos) {
                    if (prodBD.getCodigoProd().equals(prodActual.getCodigoProd())) {
                        existe = true;
                        break;
                    }
                }

                if (!existe) {
                    prodBD.eliminar();
                }
            }

            for (ProxFac pxf : this.productos) {
                pxf.setCodigoFac(this.codigo);
                if (!pxfMD.actualizarOInsertar(pxf)) {
                    return false;
                }
            }

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    //Eliminado logico (primero detalle después cabecera)
    public boolean eliminar() {
        try {
            ProxFac pxf = new ProxFac();
            pxf.setCodigoFac(this.codigo);
            
            ProxFacMD pxfMD = new ProxFacMD();
            boolean productosEliminados = pxfMD.eliminarPorFactura(pxf);
            
            FacturaMD facMD = new FacturaMD();
            boolean facturaEliminada = facMD.eliminar(this);
            
            return productosEliminados && facturaEliminada;
            
        } catch (Exception e) {
            System.out.println("Error al eliminar la factura: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    //Consulta TODAS las facturas (APR y ANU) de un cliente
    public ArrayList<Factura> consultarTodasPorParametro(Cliente cli) {
        FacturaMD facMD = new FacturaMD();
        return facMD.consultarTodasPorParametro(cli);
    }
}
