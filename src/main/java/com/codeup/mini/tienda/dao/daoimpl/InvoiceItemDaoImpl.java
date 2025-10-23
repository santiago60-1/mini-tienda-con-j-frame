package com.codeup.mini.tienda.dao.daoimpl;

import com.codeup.mini.tienda.domain.InvoiceItem;

import java.sql.*;

public class InvoiceItemDaoImpl {

    // Inserta un item usando la conexión transaccional proporcionada
    public int create(Connection conn, InvoiceItem item) throws SQLException {
        String sql = "INSERT INTO factura_items (factura_id, producto_id, nombre_producto, cantidad, precio_unitario, subtotal) VALUES (?, NULL, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, item.getFacturaId());
            stmt.setString(2, item.getNombreProducto());
            stmt.setInt(3, item.getCantidad());
            stmt.setBigDecimal(4, item.getPrecioUnitario());
            stmt.setBigDecimal(5, item.getSubtotal());

            int affected = stmt.executeUpdate();
            if (affected == 0) throw new SQLException("Crear invoice item falló, no se afectaron filas.");

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    int id = keys.getInt(1);
                    item.setId(id);
                    return id;
                }
            }
        }
        return -1;
    }
}
