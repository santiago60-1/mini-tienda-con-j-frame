# Mini tienda - Historia de usuario 1

## Resumen del proyecto
Mini Tienda Console es una pequeña aplicación Java Swing que gestiona un inventario en memoria y genera tickets de venta. El código entregado consiste en dos clases principales:

- InventanrioFrame (UI Swing): ventana principal con botones para agregar, buscar, comprar, ver estadísticas y salir.

- products (dominio): gestión en memoria de nombres, precios, stock y ticket; contiene la lógica de negocio central.

Esta documentación explica cómo instalar y ejecutar el proyecto, describe la estructura del código, identifica y corrige errores y malos olores en el código original, ofrece instrucciones de uso paso a paso y deja recomendaciones de mantenimiento y mejoras priorizadas.
***

## Requistos y instalacion
### Requisitos
- JDK 11 o superior instalado y configurado en PATH.
- Maven (opcional) o IDE (NetBeans, IntelliJ, Eclipse).
### Instalación local en NetBeans
1. Crear un nuevo proyecto Java en NetBeans o abrir la carpeta donde colocarás los .java.

2. Copiar las clases recibidas en la estructura de paquetes:
  - com.codeup.mini.tienda.ui.console.InventanrioFrame.java
  - com.codeup.mini.tienda.domain.products.java

3. En NetBeans, Build → Clean and Build Project.

4. Ejecutar la clase con método main: InventanrioFrame.

### Notas de configuracion
- No hay dependencias externas.

- Recomiendo configurar encoding UTF-8 en el IDE para evitar problemas con caracteres acentuados.
***

## Estructura del código y responsabilidades

### InventanrioFrame
- Interfaz Swing generada por NetBeans GUI Builder.

- Componentes principales:
  - Campos: lblProduct, spinnerPrice, lblStock, searchProduct, lblBuyProduct, lblQuantity.
  
  - Tabla: tableInventario (modelo DefaultTableModel).

  - Botones: Agregar producto, Buscar producto, Comprar producto, Estadisticas, Salir.
- Responsabilidades:
  - Validaciones de entrada mínimas (ej.: nombre vacío, precio = 0).

  - Llamadas a productManager (instancia de products) para operaciones CRUD en memoria.

  - Actualización de la tabla UI mediante listarProductos().


### Products
- Estructuras internas:
  - productName: ArrayList<String> nombres de producto.

  - prices: double[] arreglo de precios con capacidad inicial fija y método expandPrecios() para duplicar tamaño.

  - stock: HashMap<String,Integer> mapea nombre a cantidad.

  - ticket: ArrayList<String> almacena líneas de ticket en formato textual.

- Métodos principales:

  - addProduct(name, price, quantity)

  - listarProductos() → ArrayList<Object[]> para poblar la tabla.

  - buyProduct(name, quantity) → boolean valida y reduce stock.

  - generateTickect(name, quantity) → registra línea de ticket y reduce stock (duplicado funcional con buyProduct).

  - mostrarTicket() → genera resumen textual con total general.

  - getPrices() → devuelve producto más barato y más caro.

- Diseño: lógica en memoria, no persistencia.

***

## Guía de uso paso a paso
### Ejecutar la aplicación
1. Abrir el proyecto en NetBeans y ejecutar la clase InventanrioFrame.

2. La ventana muestra formulario y tabla vacía.

### Agregar producto
1. Escribir Producto, Precio y Cantidad.

2. Presionar Agregar producto.

3. Ver fila añadida en la tabla.

### Buscar producto
1. Escribir nombre en campo de búsqueda.

2. Presionar Buscar producto.

3. Mensaje indica si el producto existe.

### Comprar producto
1. En la sección Comprar, ingresar Producto y Cantidad.

2. Presionar Comprar producto.

3. Si hay stock suficiente, la compra se registra y la tabla se actualiza; de lo contrario, se muestra error.

### Ver ticket y salir

1. Presionar Salir.

2. Se mostrará el resumen del ticket (si existe) con el total general.

3. La ventana se cierra.
***
## Prioridad crítico

1. Reemplazar ticket de strings por lista de objetos TicketLine con campos numéricos y BigDecimal para evitar parsing y errores de formato.

2. Añadir validaciones robustas en UI (trim, checks >= 0, manejo de excepciones con mensajes claros).

3. Inicializar el modelo de la tabla en el constructor llamando inicializarTabla().
### Prioridad opcional

1. Añadir persistencia (MySQL) con DAOs y TestConnection para guardar productos y facturas.

2. Introducir IDs únicos para productos en lugar de indexación por nombre.

3. Añadir pruebas unitarias JUnit para lógica de Products y flujos críticos.

## Autor
**Nombre:** SAntiago Ortega

**Rol:** Desarrollador principal / Arquitecto del proyecto 

**Email:** santiago59782@gmail.com 

**GitHub:** [github.com/santiago](https://github.com/santiago60-1/mini-tienda-con-j-frame.git)-ortega 

**Resumen profesional:** Desarrollador Java con experiencia en diseño orientado a objetos, arquitectura en capas, JDBC y aplicaciones de escritorio Swing; enfocado en código mantenible, pruebas y buenas prácticas.

**Responsabilidades en este proyecto:** diseño del dominio, implementación de la UI en NetBeans, validación de inventario en memoria, y adaptación de persistencia (DAOs JDBC).
