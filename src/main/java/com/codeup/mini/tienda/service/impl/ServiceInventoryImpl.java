package com.codeup.mini.tienda.service.impl;

import com.codeup.mini.tienda.dao.daoimpl.CategoryDaoImpl;
import com.codeup.mini.tienda.dao.daoimpl.ProductsDAOimple;
import com.codeup.mini.tienda.domain.Category;
import com.codeup.mini.tienda.domain.Product;
import com.codeup.mini.tienda.service.ServiceInventory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ServiceInventoryImpl implements ServiceInventory {

    private final ProductsDAOimple productDao = new ProductsDAOimple();
    private final CategoryDaoImpl categoryDao = new CategoryDaoImpl();

    private final List<String> ticket = new ArrayList<>();
    private int operaciones = 0;

    @Override
    public void addProduct(String categoria, String nombre, double precio, int stock) throws SQLException {
        Category cat = categoryDao.searchByName(categoria);
        if (cat == null) {
            cat = new Category(categoria);
            categoryDao.insert(cat);
            // si CategoryDaoImpl asigna id al insertar, cat.getId() debe reflejarlo
        }

        Product nuevo = new Product(nombre, precio, stock) {
            @Override
            public String getDescription() {
                return categoria;
            }
        };

        productDao.create(nuevo); // ProductsDAOimple.create espera los campos del Product
        operaciones++;
    }

    @Override
    public void updatePrice(int idProducto, double nuevoPrecio) throws SQLException {
        if (nuevoPrecio <= 0) throw new IllegalArgumentException("El precio debe ser mayor a 0");
        Product p = productDao.searchId(idProducto);
        if (p != null) {
            p.setPrice(nuevoPrecio);
            productDao.update(p);
            operaciones++;
        }
    }

    @Override
    public void updateStock(int idProducto, int nuevoStock) throws SQLException {
        if (nuevoStock < 0) throw new IllegalArgumentException("El stock no puede ser negativo");
        Product p = productDao.searchId(idProducto);
        if (p != null) {
            p.setStock(nuevoStock);
            productDao.update(p);
            operaciones++;
        }
    }

    @Override
    public void deleteProduct(int idProducto) throws SQLException {
        productDao.delete(idProducto);
        operaciones++;
    }

    @Override
    public Product SearchByName(String nombre) throws SQLException {
        // ProductsDAOimple no tiene método específico searchByName en el código que mostraste.
        // Si existe, cámbialo por productDao.searchByName(nombre). Si no, recorremos searchAll().
        List<Product> all = productDao.searchAll();
        for (Product p : all) {
            if (p.getName().equalsIgnoreCase(nombre)) return p;
        }
        return null;
    }

    @Override
    public List<Product> listInventory() throws SQLException {
        return productDao.searchAll();
    }

    @Override
    public boolean buyProduct(String nombre, int cantidad) throws SQLException {
        // Usamos SearchByName para obtener el producto (puedes reemplazar por productDao.searchByName si existe)
        Product p = SearchByName(nombre);
        if (p != null && p.getStock() >= cantidad) {
            int nuevoStock = p.getStock() - cantidad;
            p.setStock(nuevoStock);
            productDao.update(p);

            double total = p.getPrice() * cantidad;
            ticket.add("Producto: " + nombre +
                       " | Cantidad: " + cantidad +
                       " | Precio unitario: $" + p.getPrice() +
                       " | Subtotal: $" + total);
            operaciones++;
            return true;
        }
        return false;
    }

    @Override
    public String generateTicket() {
        if (ticket.isEmpty()) return "No se realizaron compras";
        StringBuilder summary = new StringBuilder("======= FACTURA DE COMPRAS =======\n");
        double totalGeneral = 0;
        for (String line : ticket) {
            summary.append(line).append("\n");
            int index = line.lastIndexOf("$");
            if (index != -1) {
                try {
                    totalGeneral += Double.parseDouble(line.substring(index + 1).trim());
                } catch (NumberFormatException ignored) {}
            }
        }
        summary.append("\nTOTAL GENERAL: $").append(totalGeneral);
        summary.append("\nGracias por su compra!\n==================================");
        return summary.toString();
    }

    @Override
    public String summaryOperations() {
        return "Operaciones realizadas: " + operaciones;
    }
}
