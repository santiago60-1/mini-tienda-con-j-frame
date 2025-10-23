package com.codeup.mini.tienda.dao.daoimpl;

import com.codeup.mini.tienda.domain.Category;
import com.codeup.mini.tienda.config.TestConnection;
import com.codeup.mini.tienda.dao.RepositoryDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDaoImpl implements RepositoryDAO<Category> {

    @Override
    public void create(Category categoria) throws SQLException {
        String sql = "INSERT INTO categorias (nombre) VALUES (?)";
        try (Connection conn = TestConnection.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, categoria.getNombre());
            stmt.executeUpdate();
        }
    }

    /**
     * Inserta la categoría y devuelve la instancia con el id generado.
     * Útil cuando necesitas el id inmediatamente después de crearla.
     */
    public Category insert(Category categoria) throws SQLException {
        String sql = "INSERT INTO categorias (nombre) VALUES (?)";
        try (Connection conn = TestConnection.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, categoria.getNombre());
            int affected = stmt.executeUpdate();
            if (affected == 0) {
                throw new SQLException("La inserción de la categoría no afectó filas");
            }
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    return new Category(keys.getInt(1), categoria.getNombre());
                } else {
                    throw new SQLException("No se pudo obtener el id generado para la categoría");
                }
            }
        }
    }

    @Override
    public Category searchId(int id) throws SQLException {
        String sql = "SELECT * FROM categorias WHERE id=?";
        try (Connection conn = TestConnection.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Category(rs.getInt("id"), rs.getString("nombre"));
                }
            }
        }
        return null;
    }

    /**
     * Busca una categoría por nombre (case-insensitive). Retorna null si no existe.
     */
    public Category searchByName(String nombre) throws SQLException {
        String sql = "SELECT * FROM categorias WHERE LOWER(nombre) = LOWER(?) LIMIT 1";
        try (Connection conn = TestConnection.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nombre);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Category(rs.getInt("id"), rs.getString("nombre"));
                }
            }
        }
        return null;
    }

    @Override
    public List<Category> searchAll() throws SQLException {
        List<Category> lista = new ArrayList<>();
        String sql = "SELECT * FROM categorias";
        try (Connection conn = TestConnection.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(new Category(rs.getInt("id"), rs.getString("nombre")));
            }
        }
        return lista;
    }

    @Override
    public void update(Category categoria) throws SQLException {
        String sql = "UPDATE categorias SET nombre=? WHERE id=?";
        try (Connection conn = TestConnection.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, categoria.getNombre());
            stmt.setInt(2, categoria.getId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM categorias WHERE id=?";
        try (Connection conn = TestConnection.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}
