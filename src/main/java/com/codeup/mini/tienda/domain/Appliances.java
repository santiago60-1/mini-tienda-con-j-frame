/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.codeup.mini.tienda.domain;

/**
 *
 * @author Coder
 */
public class Appliances extends Products{
    public Appliances(String name, double pryce){
        super(name, pryce);
    }
    
    @Override
    public String getDescription(){
        return "Electrodomestico";
    }
}