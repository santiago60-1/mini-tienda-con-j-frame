package com.codeup.mini.tienda.dao.daoimpl;

import com.codeup.mini.tienda.config.TestConnection;
import com.codeup.mini.tienda.domain.Invoice;
import com.codeup.mini.tienda.domain.InvoiceItem;

import java.sql.*;
import java.math.BigDecimal;
import java.util.List;

public class InvoiceDaoImpl {

    private final InvoiceItemDaoImpl itemDao = new InvoiceItemDaoImpl();

    /**
     * Guarda la factura y sus items en una sola transacción.
     * Actualiza stock en la tabla productos usando el nombre del producto cuando exista.
     * Devuelve el id de factura generado.
     */
    public int saveTicket(Invoice invoice) throws SQLException {
        String insertFactura = "INSERT INTO facturas (total_general, observaciones) VALUES (?, ?)";
        String insertItem = "INSERT INTO factura_items (factura_id, producto_id, nombre_producto, cantidad, precio_unitario, subtotal) VALUES (?, NULL, ?, ?, ?, ?)";
        String updateStockByName = "UPDATE productos SET stock = stock - ? WHERE nombre = ?";
        String selectProductByName = "SELECT id FROM productos WHERE nombre = ? LIMIT 1";

        Connection conn = null;
        try {
            conn = TestConnection.getConexion();
            conn.setAutoCommit(false);

            // 1) Insertar cabecera factura
            int facturaId;
            try (PreparedStatement stmtF = conn.prepareStatement(insertFactura, Statement.RETURN_GENERATED_KEYS)) {
                BigDecimal total = invoice.getTotalGeneral() == null ? BigDecimal.ZERO : invoice.getTotalGeneral();
                stmtF.setBigDecimal(1, total);
                stmtF.setString(2, invoice.getObservaciones());
                int affected = stmtF.executeUpdate();
                if (affected == 0) throw new SQLException("Crear factura falló, no se afectaron filas.");

                try (ResultSet rs = stmtF.getGeneratedKeys()) {
                    if (rs.next()) {
                        facturaId = rs.getInt(1);
                        invoice.setId(facturaId);
                    } else {
                        throw new SQLException("No se pudo obtener el id generado de factura.");
                    }
                }
            }

            // 2) Insertar items y actualizar stock (si existe producto)
            for (InvoiceItem item : invoice.getItems()) {
                item.setFacturaId(facturaId);

                // Insertar item en factura_items (producto_id = NULL)
                try (PreparedStatement stmtI = conn.prepareStatement(insertItem, Statement.RETURN_GENERATED_KEYS)) {
                    stmtI.setInt(1, item.getFacturaId());
                    stmtI.setString(2, item.getNombreProducto());
                    stmtI.setInt(3, item.getCantidad());
                    stmtI.setBigDecimal(4, item.getPrecioUnitario());
                    stmtI.setBigDecimal(5, item.getSubtotal());
                    int aff = stmtI.executeUpdate();
                    if (aff == 0) throw new SQLException("Crear invoice item falló para: " + item.getNombreProducto());
                    try (ResultSet keys = stmtI.getGeneratedKeys()) {
                        if (keys.next()) item.setId(keys.getInt(1));
                    }
                }

                // Intentar actualizar stock por nombre; si no existe, registrar y continuar
                try (PreparedStatement stmtStock = conn.prepareStatement(updateStockByName)) {
                    stmtStock.setInt(1, item.getCantidad());
                    stmtStock.setString(2, item.getNombreProducto());
                    int rows = stmtStock.executeUpdate();

                    if (rows == 0) {
                        // No se actualizó; comprobar si el producto existe
                        try (PreparedStatement stmtSel = conn.prepareStatement(selectProductByName)) {
                            stmtSel.setString(1, item.getNombreProducto());
                            try (ResultSet rs = stmtSel.executeQuery()) {
                                if (rs.next()) {
                                    // Producto existe pero UPDATE no afectó filas (posible stock insuficiente)
                                    throw new SQLException("Actualizar stock falló para producto existente: " + item.getNombreProducto());
                                } else {
                                    // Producto no existe en la tabla productos: registrar y continuar
                                    System.err.println("Info: producto no encontrado en DB, se omite actualización de stock para: " + item.getNombreProducto());
                                }
                            }
                        }
                    }
                }
            }

            conn.commit();
            return facturaId;

        } catch (SQLException e) {
            if (conn != null) try { conn.rollback(); } catch (SQLException ignore) {}
            throw e;
        } finally {
            if (conn != null) try { conn.setAutoCommit(true); conn.close(); } catch (SQLException ignore) {}
        }
    }

    /**
     * Recupera una factura y sus items por id.
     */
    public Invoice findById(int facturaId) throws SQLException {
        String sqlFactura = "SELECT id, fecha, total_general, observaciones FROM facturas WHERE id = ?";
        String sqlItems = "SELECT id, producto_id, nombre_producto, cantidad, precio_unitario, subtotal FROM factura_items WHERE factura_id = ?";

        try (Connection conn = TestConnection.getConexion();
             PreparedStatement stmtF = conn.prepareStatement(sqlFactura)) {
            stmtF.setInt(1, facturaId);
            try (ResultSet rsF = stmtF.executeQuery()) {
                if (!rsF.next()) return null;
                Invoice inv = new Invoice();
                inv.setId(rsF.getInt("id"));
                inv.setFecha(rsF.getTimestamp("fecha").toLocalDateTime());
                inv.setTotalGeneral(rsF.getBigDecimal("total_general"));
                inv.setObservaciones(rsF.getString("observaciones"));

                try (PreparedStatement stmtI = conn.prepareStatement(sqlItems)) {
                    stmtI.setInt(1, facturaId);
                    try (ResultSet rsI = stmtI.executeQuery()) {
                        while (rsI.next()) {
                            InvoiceItem item = new InvoiceItem();
                            item.setId(rsI.getInt("id"));
                            int pid = rsI.getInt("producto_id");
                            if (rsI.wasNull()) {
                                // producto_id null, ignorar
                            } else {
                                // no tenemos campo productoId en InvoiceItem para usarlo en lógica actual, se deja como está
                            }
                            item.setNombreProducto(rsI.getString("nombre_producto"));
                            item.setCantidad(rsI.getInt("cantidad"));
                            item.setPrecioUnitario(rsI.getBigDecimal("precio_unitario"));
                            item.setSubtotal(rsI.getBigDecimal("subtotal"));
                            inv.addItem(item);
                        }
                    }
                }
                return inv;
            }
        }
    }
}
