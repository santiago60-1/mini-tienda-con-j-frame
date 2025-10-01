/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.codeup.mini.tienda;

import com.codeup.mini.tienda.database.TestConnection;
import java.sql.Connection;

/**
 *
 * @author Santiago Ortega
 */
public class MiniTienda {

    public static void main(String[] args) {
        System.out.println("Hello World!");
        Connection conn = TestConnection.getConexion();
if (conn != null) {
    System.out.println("✅ Conexión exitosa a la base de datos.");
} else {
    System.out.println("❌ No se pudo conectar.");
}
    }
}