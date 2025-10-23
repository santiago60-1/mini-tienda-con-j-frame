/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.codeup.mini.tienda.domain;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Santiago Ortega
 */
public class Inventory {
    private ArrayList <Product> products = new ArrayList();
    private HashMap<String, Integer> stock = new HashMap();
    private double[] pryces = new double[100];
    private ArrayList<String> ticket = new ArrayList<>();
    
    public void productAdd(Product p, int quantity){
        products.add(p);
        stock.put(p.getName(), quantity);
        if (products.size() > pryces.length){
            
        }
        pryces[products.size() -1] = p.getPrice();
    }
    
    public void expandPrices(){
        double[] nuevo = new double[ pryces.length * 2];
        System.arraycopy(pryces, 0, nuevo, 0, pryces.length);
        pryces = nuevo;
    }
    
    public ArrayList<Object[]> listProducts(){
        
        ArrayList<Object[]> list = new ArrayList<>();
        for (Product p : products){
            int quantity = stock.getOrDefault(p.getName(), 0);
            list.add(new Object[]{p.getName(),p.getPrice(), quantity, p.getDescription()});
        }
        
        return list;
    }
    
    public boolean productBuy(String name, int quantity){
        for (Product p : products){
            if (p.getName().equalsIgnoreCase(name)) {
                int available = stock.getOrDefault(name, 0);
                if (available >= quantity){
                    stock.put(name, available-quantity);
                    double total = p.getPrice()* quantity;
                    ticket.add("Producto: " + name +
                               " | Cantidad: " + quantity +
                               " | Precio unitario: $" + p.getPrice()+
                               " | Subtotal: $" + total);
                    return true;
                }
            }
        }
        return false;
    }
    
    public String generateTicket(){
        if (ticket.isEmpty()) return "No se realizaron compras";
        StringBuilder summary = new StringBuilder("======= FACTURA DE COMPRAS =======\\");
        double totalGeneral = 0;
        for (String line : ticket){
            summary.append(line).append("\n");
            int index = line.lastIndexOf("$");
            if (index != 1){
                String totalStr = line.substring(index + 1).trim();
                try {
                    totalGeneral += Double.parseDouble(totalStr);
                } catch(NumberFormatException ignored){
                    
                }

            }
}
        summary.append("\nTOTAL GENERAL: $").append(totalGeneral);
        summary.append("\nGracias por su compra!\n==================================");
        return summary.toString();
    }
    
    public ArrayList<Product> getProducts(){
        return products;
    }
    
    public HashMap<String, Integer> getStock(){
        return stock;
    }
}