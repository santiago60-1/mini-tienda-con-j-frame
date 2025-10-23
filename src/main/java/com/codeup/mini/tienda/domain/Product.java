/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.codeup.mini.tienda.domain;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
     * @author SAntiago Ortega
 */

public abstract class Product {
    int id; // clave primaria en BD
    String name;
    private double price;
    private int stock;

    public Product(String name, double price, int id, int stock) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    // Constructor sin id (para inserts)
    public Product(String name, double price, int stock) {
        this(name, price, 0, stock);
    }

    // Getters y setters...
    public int getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getStock() { return stock; }

    public void setName(String name) { if(name!=null && !name.isBlank()) this.name = name; }
    public void setPrice(double price) { if(price>=0) this.price = price; }
    public void setStock(int stock) { if(stock>=0) this.stock = stock; }

    public abstract String getDescription();
    
    public String toString(){
        return String.format("Product{id=%d, name=%s, price=%s, stock=%s}", 
                                            id,name, price, stock);
    }
}