# Mini tienda -Documentacion tecnica

## Resumen del proyecto
Mini Tienda es una aplicación de escritorio Java Swing para gestionar un inventario simple y registrar ventas como facturas persistidas en MySQL. Combina lógica en memoria (clase Inventory) con DAOs JDBC para persistencia. Componentes principales en el repositorio:

- UI: InventoryFrame (Swing).
- Domain: Product, Appliance, Food, Category, Inventory, Invoice, InvoiceItem, InvoiceManager (sugerida).
- DAO y persistencia: CategoryDaoImpl, ProductsDAOimple, InvoiceDaoImpl, InvoiceItemDaoImpl.
- Configuración JDBC: TestConnection.
- Esquema SQL: script para crear tablas y datos de ejemplo.
***

## Requistos y instalacion
### Requisitos
- JDK 11 o superior.
- Maven (opcional) o IDE (NetBeans, IntelliJ, Eclipse).
- MySQL 5.7+ o compatible.
- Dependencia JDBC: com.mysql:mysql-connector-j (usar versión compatible, p. ej. 8.x).

### Clonar y preparar proyecto

1. Clonar el repositorio en tu máquina.
2. Abrir el proyecto en tu IDE preferido.
3. Asegurar que la dependencia del conector MySQL está en pom.xml:
```bash
<dependency>
  <groupId>com.mysql</groupId>
  <artifactId>mysql-connector-j</artifactId>
  <version>8.0.33</version>
</dependency>
```
4. Configurar TestConnection con URL, usuario y contraseña de tu servidor MySQL.

***

## Base de datos
Ejecuta este script SQL para crear la base de datos y las tablas requeridas:
```bash CREATE DATABASE IF NOT EXISTS mini_tienda;
USE mini_tienda;

CREATE TABLE categorias (
  id INT AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(100) NOT NULL,
  UNIQUE KEY uk_categorias_nombre (nombre)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE productos (
  id INT AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(150) NOT NULL,
  precio DECIMAL(10,2) NOT NULL DEFAULT 0.00,
  stock INT NOT NULL DEFAULT 0,
  categoria_id INT NULL,
  CONSTRAINT fk_productos_categoria FOREIGN KEY (categoria_id)
    REFERENCES categorias(id) ON DELETE SET NULL ON UPDATE CASCADE,
  INDEX idx_productos_nombre (nombre),
  INDEX idx_productos_categoria (categoria_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE facturas (
  id INT AUTO_INCREMENT PRIMARY KEY,
  fecha TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  total_general DECIMAL(12,2) NOT NULL DEFAULT 0.00,
  observaciones VARCHAR(255) NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE factura_items (
  id INT AUTO_INCREMENT PRIMARY KEY,
  factura_id INT NOT NULL,
  producto_id INT NULL,
  nombre_producto VARCHAR(150) NOT NULL,
  cantidad INT NOT NULL,
  precio_unitario DECIMAL(10,2) NOT NULL,
  subtotal DECIMAL(12,2) NOT NULL,
  CONSTRAINT fk_factura_items_factura FOREIGN KEY (factura_id)
    REFERENCES facturas(id) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT fk_factura_items_producto FOREIGN KEY (producto_id)
    REFERENCES productos(id) ON DELETE SET NULL ON UPDATE CASCADE,
  INDEX idx_factura_items_factura (factura_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```
### Datos de ejemplo
``` bash 
INSERT INTO categorias (nombre) VALUES ('Electrodoméstico'), ('Alimento');

INSERT INTO productos (nombre, precio, stock, categoria_id)
VALUES 
  ('Licuadora', 120.00, 10, (SELECT id FROM categorias WHERE nombre = 'Electrodoméstico')),
  ('Arroz', 3.50, 100, (SELECT id FROM categorias WHERE nombre = 'Alimento'));
```
***
## Estrucutura del codigo y responsabilidades
### UI
- InventoryFrame: formulario Swing con controles para Agregar, Buscar, Comprar, Estadísticas, Salir.

- Al pulsar Salir, se genera el ticket con inventory.generateTicket() y se persiste en BD mediante InvoiceDaoImpl.saveTicket(...).

### Domain
- Product, Appliance, Food: modelos de producto en memoria.

- Category: modelo categoría.

- Inventory: almacenamiento en memoria, métodos existentes como productAdd, listProducts, productBuy, generateTicket, getProducts, getStock, getProductByName.

- Invoice: cabecera de factura con lista de InvoiceItem y recálculo automático de total.

- InvoiceItem: item de factura basado en nombreProducto, cantidad, precioUnitario y subtotal. Diseñado para no exigir productId.

- InvoiceManager (opcional): orquesta validación en memoria, construcción de Invoice y llamada al DAO.

### DAO
- TestConnection: obtiene conexiones JDBC.

- CategoryDaoImpl: buscar/insertar categoría (insert devuelve categoría con id).

- ProductsDAOimple: insertar producto (recomendado: usar RETURN_GENERATED_KEYS y setear id en instancia).

- InvoiceItemDaoImpl: insertar items dentro de una transacción (usa Connection proporcionada).

- InvoiceDaoImpl: método transaccional saveTicket(Invoice) que:

    - Inserta cabecera en facturas.

    - Inserta items en factura_items (producto_id = NULL si no está disponible).
    - Intenta actualizar stock en productos por nombre; si el nombre no existe registra y continúa, si existe pero la actualización no afecta filas lanza SQLException (posible stock insuficiente).
    - Commit o rollback según resultado.
***
## Guía de uso
### Agregar producto
1. Completa Categoria, Producto, Precio, Stock en InventoryFrame.

2. Presiona Agregar.

3. Flujo:
   - Validación en memoria.
   - Crear categoría en BD si no existe (CategoryDaoImpl.insert).
   - Llamada a ProductsDAOimple.create(producto).
   - Actualización de la tabla UI.

### Comprar y generar factura desde UI
1. Completa Producto y Cantidad en sección de compra.

2. Presiona Comprar o Generar ticket.

3. Flujo recomendado:
  - Validar stock en Inventory (memoria).


  - Crear Invoice y InvoiceItem(s) para cada producto vendido (usar nombreProducto y precioUnitario desde Inventory).


  - Llamar InvoiceDaoImpl.saveTicket(invoice) (transaccional).


  - Si devuelve id correctamente, sincronizar memoria con inventory.productBuy(...) y actualizar tabla UI.

### Guardar ticket al salir

El botón Salir (jButton5) ahora:

- Llama inventory.generateTicket() y lo muestra.

- Construye un Invoice con la observación igual al texto del resumen.


- Extrae un total aproximado del texto y crea un InvoiceItem resumen (nombre "Venta sesión") con ese total.

- Llama InvoiceDaoImpl.saveTicket(invoice).

- Cierra la ventana.

Framento de ejemplo: 
``` bash String summary = inventory.generateTicket();
Invoice invoice = new Invoice();
invoice.setObservaciones(summary);
BigDecimal total = extractTotalFromSummary(summary); // función de parsing
invoice.setTotalGeneral(total);
InvoiceItem summaryItem = new InvoiceItem("Venta sesión", 1, total);
invoice.addItem(summaryItem);
int idFactura = new InvoiceDaoImpl().saveTicket(invoice);
```
***
## Mantenimiento y recomendaciones
### Recomendaciones técnicas
- Preferir identificar productos por id en lugar de por nombre para actualizar stock con seguridad. Cambios sugeridos:
  - Hacer que ProductsDAOimple.create(product) devuelva el id generado y setee product.id..
  - Extender InvoiceItem para guardar productoId cuando esté disponible y persistir columna producto_id.
  - Cambiar UPDATE stock para usar producto_id.
- Normalizar nombres al persistir (trim, case) para evitar discrepancias entre memoria y BD.

### Manejo de errores
- InvoiceDaoImpl realiza rollback completo en caso de SQLException.
- Caso especial actual: ítem resumen "Venta sesión" no existe en productos y por diseño actual se omite la actualización de stock para nombres inexistentes; ajustar la política según necesidad (rollback, crear placeholder, o rechazar guardar resumen).
### Pruebas
- Escribir pruebas unitarias para:
  - CategoryDaoImpl.searchByName e insert.
  - ProductsDAOimple.create con validación de generated keys.
  - InvoiceDaoImpl.saveTicket: casos exitosos y fallos (producto inexistente, stock insuficiente).

### Seguridad y despliegue
- No embebas credenciales en código. Usar variables de entorno o archivo de configuración excluido del control de versiones.

- Habilitar backups periódicos de la base de datos.
***

## Pasos siguientes recomendados

1. Problemas conocidos y soluciones rápidas
2. Modificar InvoiceItem para almacenar productoId cuando esté disponible y persistir producto_id.
3. Cambiar actualizaciones de stock para usar producto_id.
4. Añadir pruebas unitarias JUnit para DAOs y transacciones.
5. Reemplazar System.err.println por logging estructurado (SLF4J / Logback).

## Autor
**Nombre:** SAntiago Ortega

**Rol:** Desarrollador principal / Arquitecto del proyecto 

**Email:** santiago59782@gmail.com 

**GitHub:** [github.com/santiago](https://github.com/santiago60-1/mini-tienda-con-j-frame.git)-ortega 

**Resumen profesional:** Desarrollador Java con experiencia en diseño orientado a objetos, arquitectura en capas, JDBC y aplicaciones de escritorio Swing; enfocado en código mantenible, pruebas y buenas prácticas.

**Responsabilidades en este proyecto:** diseño del dominio, implementación de la UI en NetBeans, validación de inventario en memoria, y adaptación de persistencia (DAOs JDBC).