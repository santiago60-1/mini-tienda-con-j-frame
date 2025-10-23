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
public class Products {
    
    
    protected ArrayList<String> productName = new ArrayList<>();
    protected double[] prices = new double[100];
    protected HashMap<String, Integer> stock = new HashMap<>();
    private ArrayList<String> ticket = new ArrayList<>();
    
    public Products(){
        //construtor por defecto
    }
    
    public void addProduct(String name, double price, int quantity){
        productName.add(name);
        prices[productName.size() -1 ] = price;
        stock.put(name, quantity);
    }
    
    public void expandPrecios(){
        double[] newPrice = new double[prices.length * 2];
        
        //copiamos los precios actuales al nuevo arreglo
        System.arraycopy(prices, 0, newPrice, 0, prices.length);
        
        //Actualizamos los precios
        prices=newPrice;
    }
    
    public String getProductInfo(String search){
        int index = productName.indexOf(search);
        
        if (index != -1){
            double precio = prices[index];
            int cantidad = stock.getOrDefault(search, 0);
            
            return "producto: " +search +
                    "\nPrecio: $" + precio +
                    "\nStock: " + cantidad + " unidades";        
    }
    return "Producto no encontrado.";
    
    }
    
    public ArrayList<Object[]> listarProductos(){
        ArrayList<Object[]> lista = new ArrayList<>();
        
        for (int i =0; i < productName.size(); i++ ){
            String name = productName.get(i);
            double precio = prices[i];
            int cantidad = stock.getOrDefault(name, 0);
            lista.add(new Object[]{name, precio,cantidad});
            
        }
        return lista;
    }
    
    public boolean buyProduct(String name, int quantity){
        
        for (int i = 0 ; i < productName.size(); i++){
            String nameProduct = productName.get(i);
            
            if (nameProduct.equalsIgnoreCase(name)){
                
                int stockAvailable = stock.getOrDefault(nameProduct, 0);
                
                if (stockAvailable >= quantity){
                    stock.put(nameProduct, stockAvailable-quantity);
                    return true;
                }
                break;
            }
        }
        return false;
    }
    
    public ArrayList<Object[]> getPrices(){
        ArrayList<Object[]> resultado = new ArrayList<>();
        if (productName.isEmpty()) return resultado;    
        
        int priceB = 0;
        int priceC = 0;
        
        for (int i = 1;i  < productName.size(); i++ ){
            if (prices[i] < prices[priceB]) priceB = i;
            if (prices[i] > prices[priceC]) priceC= i;
        }
        
        String nameB = productName.get(priceB);
        String nameC = productName.get(priceC);
        
        resultado.add(new Object[]{nameB, prices[priceB], stock.getOrDefault(nameB, 0)});
        
        if (priceB != priceC){
            resultado.add(new Object[]{nameC, prices[priceC], stock.getOrDefault(nameC, 0)});
        }
        
        return resultado;
    }
    
    public boolean generateTickect(String nameBuy, int quantityBuy){
        
        for (int i = 0; i < productName.size(); i++){
            String nameProduct = productName.get(i);
            if (nameProduct.equalsIgnoreCase(nameBuy)){
                int StockAvailable = stock.getOrDefault(nameProduct, 0);
                
                if (StockAvailable >= quantityBuy) {
                    stock.put(nameProduct, StockAvailable - quantityBuy);
                    double priceBuy = prices[i];
                    double total = priceBuy * quantityBuy;
                    String register = "Producto: " + nameProduct +
                                  " | Cantidad: " + quantityBuy +
                                  " | Precio unitario: $" + priceBuy +
                                  " | Subotal: $" + total;
                    ticket.add(register);
                    return true;
                }
                break;
                
            }
        }
        return false;
    }
    
        public String mostrarTicket() {
        if (ticket.isEmpty()) return "No se realizaron compras.";

        StringBuilder resumen = new StringBuilder();
        resumen.append("======= FACTURA DE COMPRAS =======\n");
        double totalGeneral = 0;

        for (String linea : ticket) {
            resumen.append(linea).append("\n");

            int index = linea.lastIndexOf("$");
            if (index != -1) {
                String totalStr = linea.substring(index + 1).trim();
                try {
                    totalGeneral += Double.parseDouble(totalStr);
                } catch (NumberFormatException ignored) {}
            }
        }

        resumen.append("\nTOTAL GENERAL: $").append(totalGeneral);
        resumen.append("\nGracias por su compra!");
        resumen.append("\n==================================");

        return resumen.toString();
    }
}