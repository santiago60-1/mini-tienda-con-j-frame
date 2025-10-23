/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.codeup.mini.tienda.domain;

/**
 *
 * @author Coder
 */
public class Category {
    
     private int id;
    private String nombre;

    public Category(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public Category(String nombre) {
        this(0, nombre);
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if(nombre != null && !nombre.isBlank()) {
            this.nombre = nombre;
        }
    }

    @Override
    public String toString() {
        return "Categoria{id=" + id + ", nombre='" + nombre + "'}";
    }
}