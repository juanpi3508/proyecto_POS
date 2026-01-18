package DP;

import MD.CompraMD;
import MD.ProxOcMD;
import util.CargadorProperties;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Compra {

    private String codigo;
    private String codigoProveedor; // Equivalente a id_proveedor
    private LocalDateTime fechaHora;
    private double subtotal;
    private double iva;
    private double total;
    private String estado;
    private ArrayList<ProxOc> productos;

    public Compra() {
        this.estado = CargadorProperties.obtenerConfigCompra("comp.estado"); // Abierta por defecto
        this.productos = new ArrayList<>();
        this.fechaHora = LocalDateTime.now();
    }


    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getCodigoProveedor() { return codigoProveedor; }
    public void setCodigoProveedor(String codigoProveedor) { this.codigoProveedor = codigoProveedor; }

    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }

    public double getSubtotal() { return subtotal; }
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }

    public double getIva() { return iva; }
    public void setIva(double iva) { this.iva = iva; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public ArrayList<ProxOc> getProductos() { return productos; }
    public void setProductos(ArrayList<ProxOc> productos) {
        this.productos = productos;
        recalcularTotales();
    }


    public void calcularSubtotal() {
        this.subtotal = 0.0;
        for (ProxOc pxo : productos) {
            this.subtotal += pxo.getSubtotalProducto();
        }
    }

    public void calcularIVA() {
        double porcentajeIVA = Double.parseDouble(
                CargadorProperties.obtenerConfigCompra("comp.iva")
        );
        this.iva = Math.round(this.subtotal * porcentajeIVA * 100.0) / 100.0;
    }


    public void calcularTotal() {
        this.total = this.subtotal + this.iva;
    }

    public void recalcularTotales() {
        calcularSubtotal();
        calcularIVA();
        calcularTotal();
    }

    public void agregarProducto(ProxOc producto) {
        this.productos.add(producto);
        recalcularTotales();
    }

    public static String generarCodigo() {
        CompraMD compMD = new CompraMD();
        return compMD.generarCodigoCompra();
    }

    public String insertar() {
        recalcularTotales();
        CompraMD compMD = new CompraMD();
        String codigoGenerado = compMD.insertarCompraCompleta(this);

        if (codigoGenerado != null) {
            this.codigo = codigoGenerado;
        }
        return codigoGenerado;
    }

    public ArrayList<Compra> consultarTodos() {
        CompraMD compMD = new CompraMD();
        return compMD.consultarTodos();
    }

    public Compra consultarPorParametro(Compra comp) {
        CompraMD compMD = new CompraMD();
        return compMD.consultarPorCodigo(comp);
    }

    public ArrayList<Compra> consultarPorParametro(Proveedor prov) {
        CompraMD compMD = new CompraMD();
        return compMD.consultarPorParametro(prov);
    }

    public boolean modificarTotalesSoloCabecera() {
        CompraMD compMD = new CompraMD();
        return compMD.modificar(this);
    }

    public boolean modificar() {
        recalcularTotales();
        CompraMD compMD = new CompraMD();

        try {
            if (!compMD.modificar(this)) {
                return false;
            }

            ProxOc pxoConsulta = new ProxOc();
            pxoConsulta.setCodigoCompra(this.codigo);

            ProxOcMD pxoMD = new ProxOcMD();
            ArrayList<ProxOc> productosEnBD = pxoMD.consultarPorCompra(pxoConsulta);

            for (ProxOc prodBD : productosEnBD) {
                boolean existe = false;
                for (ProxOc prodActual : this.productos) {
                    if (prodBD.getCodigoProducto().equals(prodActual.getCodigoProducto())) {
                        existe = true;
                        break;
                    }
                }
                if (!existe) {
                    prodBD.eliminar();
                }
            }

            for (ProxOc pxo : this.productos) {
                pxo.setCodigoCompra(this.codigo);
                if (!pxoMD.actualizarOInsertar(pxo)) {
                    return false;
                }
            }

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminar() {
        try {
            ProxOc pxo = new ProxOc();
            pxo.setCodigoCompra(this.codigo);

            ProxOcMD pxoMD = new ProxOcMD();
            boolean productosEliminados = pxoMD.eliminarPorCompra(pxo);

            CompraMD compMD = new CompraMD();
            boolean compraEliminada = compMD.eliminar(this);

            return productosEliminados && compraEliminada;

        } catch (Exception e) {
            System.out.println("Error al eliminar la compra: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public ArrayList<Compra> consultarTodasPorParametro(Proveedor prov) {
        CompraMD compMD = new CompraMD();
        return compMD.consultarHistorialPorProveedor(prov);
    }

    public Compra consultarPorCodigoDetalle(Compra compParam) {
        CompraMD compMD = new CompraMD();
        return compMD.consultarPorCodigo(compParam);
    }


    public boolean aprobar() {
        CompraMD compMD = new CompraMD();
        return compMD.aprobarCompra(this);
    }


}
