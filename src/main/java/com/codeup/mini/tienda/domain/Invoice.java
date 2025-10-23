package com.codeup.mini.tienda.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Invoice {
    private int id;
    private LocalDateTime fecha;
    private BigDecimal totalGeneral;
    private String observaciones;
    private final List<InvoiceItem> items = new ArrayList<>();

    public Invoice() {
        this.fecha = LocalDateTime.now();
        this.totalGeneral = BigDecimal.ZERO;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
    public BigDecimal getTotalGeneral() { return totalGeneral; }
    public void setTotalGeneral(BigDecimal totalGeneral) { this.totalGeneral = totalGeneral; }
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
    public List<InvoiceItem> getItems() { return items; }

    public void addItem(InvoiceItem item) {
        items.add(item);
        recalcTotal();
    }

    public void addAllItems(List<InvoiceItem> itemsToAdd) {
        items.addAll(itemsToAdd);
        recalcTotal();
    }

    private void recalcTotal() {
        this.totalGeneral = items.stream()
            .map(InvoiceItem::getSubtotal)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
