/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.codeup.mini.tienda.dao;

import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author Coder
 */
public interface RepositoryDAO<T> {
    
    void create (T t) throws SQLException;
    
    T searchId(int id) throws SQLException;
    
    List<T> searchAll() throws SQLException;
    
    void update(T t) throws SQLException;
    
    void delete(int id) throws SQLException;
}