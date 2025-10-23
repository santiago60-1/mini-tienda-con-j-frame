/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.codeup.mini.tienda.domain;

public class Appliance extends Product {

    public Appliance(int id, String name, double price, int stock) {
        super(name, price, id, stock);
    }

    // Constructor sin id (para inserts nuevos en BD)
    public Appliance(String name, double price, int stock) {
        super(name, price, stock);
    }

    @Override
    public String getDescription() {
        return "Electrodoméstico";
    }
    
}