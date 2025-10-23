package com.codeup.mini.tienda.domain;

import java.math.BigDecimal;

public class InvoiceItem {
    private int id;
    private int facturaId;
    private String nombreProducto;
    private int cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;

    public InvoiceItem() {}

    public InvoiceItem(String nombreProducto, int cantidad, BigDecimal precioUnitario) {
        this.nombreProducto = nombreProducto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario == null ? BigDecimal.ZERO : precioUnitario;
        this.recalcSubtotal();
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getFacturaId() { return facturaId; }
    public void setFacturaId(int facturaId) { this.facturaId = facturaId; }

    public String getNombreProducto() { return nombreProducto; }
    public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; recalcSubtotal(); }

    public BigDecimal getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(BigDecimal precioUnitario) { this.precioUnitario = precioUnitario == null ? BigDecimal.ZERO : precioUnitario; recalcSubtotal(); }

    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }

    private void recalcSubtotal() {
        if (precioUnitario == null) precioUnitario = BigDecimal.ZERO;
        subtotal = precioUnitario.multiply(BigDecimal.valueOf(Math.max(0, cantidad)));
    }
}
