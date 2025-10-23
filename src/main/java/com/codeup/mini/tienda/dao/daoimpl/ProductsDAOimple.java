package com.codeup.mini.tienda.dao.daoimpl;

import com.codeup.mini.tienda.config.TestConnection;
import com.codeup.mini.tienda.dao.RepositoryDAO;
import com.codeup.mini.tienda.domain.Product;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductsDAOimple implements RepositoryDAO<Product> {

    @Override
    public void create(Product producto) throws SQLException {
        String sql = "INSERT INTO productos (nombre, precio, stock, categoria_id) VALUES (?, ?, ?, ?)";
        try (Connection conn = TestConnection.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, producto.getName());
            stmt.setDouble(2, producto.getPrice());
            stmt.setInt(3, producto.getStock());
            stmt.setInt(4, /* aquí iría el idCategoria */ 1);
            stmt.executeUpdate();
        }
    }

    @Override
    public Product searchId(int id) throws SQLException {
        String sql = "SELECT * FROM productos WHERE id=?";
        try (Connection conn = TestConnection.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Product(
                                        rs.getString("nombre"),
                    rs.getDouble("precio"), rs.getInt("id"),
                    rs.getInt("stock")
                ) {
                    @Override
                    public String getDescription() {
                        return "Producto genérico";
                    }
                };
            }
        }
        return null;
    }

    @Override
    public List<Product> searchAll() throws SQLException {
        List<Product> lista = new ArrayList<>();
        String sql = "SELECT * FROM productos";
        try (Connection conn = TestConnection.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(new Product(
                                        rs.getString("nombre"),
                    rs.getDouble("precio"), rs.getInt("id"),
                    rs.getInt("stock")
                ) {
                    @Override
                    public String getDescription() {
                        return "Producto genérico";
                    }
                });
            }
        }
        return lista;
    }

    @Override
    public void update(Product producto) throws SQLException {
        String sql = "UPDATE productos SET nombre=?, precio=?, stock=? WHERE id=?";
        try (Connection conn = TestConnection.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, producto.getName());
            stmt.setDouble(2, producto.getPrice());
            stmt.setInt(3, producto.getStock());
            stmt.setInt(4, producto.getId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM productos WHERE id=?";
        try (Connection conn = TestConnection.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }


}