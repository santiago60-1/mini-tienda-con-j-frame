/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.codeup.mini.tienda.service;

import com.codeup.mini.tienda.domain.Category;
import com.codeup.mini.tienda.domain.Product;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author Coder
 */
public interface ServiceInventory {
    
    void addProduct(String categoria, String nombre, double precio, int stock) throws SQLException;
    void updatePrice(int idProducto, double nuevoPrecio) throws SQLException;
    void updateStock(int idProducto, int nuevoStock) throws SQLException;
    void deleteProduct(int idProducto) throws SQLException;
    Product SearchByName(String nombre) throws SQLException;
    List<Product> listInventory() throws SQLException;
    boolean buyProduct(String nombre, int cantidad) throws SQLException;
    String generateTicket();
    String summaryOperations();
    
}