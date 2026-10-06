# Sistema de Gestión de Biblioteca Escolar

Sistema de escritorio desarrollado en Java para la gestión de una biblioteca escolar. Permite administrar libros, estudiantes, categorías y préstamos, además de generar reportes y controlar el acceso según el rol del usuario.

## Descripción

El sistema permite gestionar los principales procesos de una biblioteca escolar mediante una interfaz gráfica desarrollada con Java Swing y una base de datos MySQL.

Entre sus principales funcionalidades se encuentran:

* Inicio de sesión de usuarios.
* Control de acceso según rol.
* Gestión de libros.
* Gestión de estudiantes.
* Gestión de categorías.
* Registro y devolución de préstamos.
* Control automático del stock de libros.
* Consulta de préstamos activos e historial.
* Generación de reportes.
* Persistencia de información mediante MySQL.
* Manejo de concurrencia y sincronización en operaciones de préstamo y devolución.

## Tecnologías utilizadas

* Java JDK 25
* IntelliJ IDEA
* Java Swing
* JDBC
* MySQL 8
* MySQL Connector/J
* Git y GitHub

## Arquitectura del proyecto

El proyecto utiliza una estructura basada en el patrón MVC, complementada con DAO y Singleton.

```text
BibliotecaEscolar
├── src
│   ├── modelo
│   ├── dao
│   ├── controlador
│   ├── vista
│   └── main
│
├── lib
│   └── mysql-connector-j-26.7.0.jar
│
├── BibliotecaEscolar.iml
└── README.md
```

### Modelo

Contiene las clases que representan las entidades y conceptos principales del sistema.

Entre ellas:

* `Usuario`
* `UsuarioBiblioteca`
* `Bibliotecario`
* `EstudianteUsuario`
* `Estudiante`
* `Libro`
* `Categoria`
* `Prestamo`
* `GestionPrestamos`

### DAO

Contiene las clases encargadas de interactuar directamente con la base de datos.

* `DatabaseConnection`
* `UsuarioDAO`
* `LibroDAO`
* `EstudianteDAO`
* `CategoriaDAO`
* `PrestamoDAO`
* `ReporteDAO`

### Controladores

Contiene la lógica de negocio y conecta las vistas con los DAO.

* `LoginController`
* `LibroController`
* `EstudianteController`
* `CategoriaController`
* `PrestamoController`
* `ReporteController`

### Vista

Contiene las interfaces gráficas desarrolladas con Java Swing.

* `LoginFrame`
* `MenuPrincipal`
* `LibrosFrame`
* `EstudiantesFrame`
* `CategoriasFrame`
* `PrestamosFrame`
* `ReportesFrame`

### Main

La ejecución principal del sistema comienza desde:

```text
main.Main
```

Este inicia la aplicación y abre la ventana de inicio de sesión.

## Funcionalidades

### Inicio de sesión

El sistema permite iniciar sesión utilizando correo y contraseña.

Existen dos roles:

* Bibliotecario
* Estudiante

Los permisos de cada usuario se determinan según su rol.

### Bibliotecario

El bibliotecario puede acceder a:

* Libros
* Estudiantes
* Categorías
* Préstamos
* Reportes

### Estudiante

El estudiante puede acceder a:

* Préstamos
* Reportes

Las funciones administrativas quedan restringidas para este rol.

## Gestión de libros

El sistema permite realizar CRUD completo sobre los libros:

* Registrar libros.
* Consultar libros.
* Actualizar libros.
* Eliminar libros.

Cada libro contiene información como:

* Título
* Autor
* ISBN
* Editorial
* Stock
* Categoría

## Gestión de estudiantes

El sistema permite realizar CRUD completo sobre los estudiantes:

* Registrar estudiantes.
* Consultar estudiantes.
* Actualizar estudiantes.
* Eliminar estudiantes.

Los estudiantes contienen:

* Nombre
* RUT
* Curso
* Correo

## Gestión de categorías

El sistema permite realizar CRUD completo sobre las categorías:

* Registrar categorías.
* Consultar categorías.
* Actualizar categorías.
* Eliminar categorías.

Las categorías permiten clasificar los libros dentro de la biblioteca.

## Gestión de préstamos

El sistema permite:

* Registrar préstamos.
* Consultar préstamos.
* Registrar devoluciones.
* Consultar préstamos activos.
* Consultar historial de préstamos.
* Detectar préstamos atrasados.

Al registrar un préstamo, el stock disponible del libro disminuye automáticamente.

Al registrar una devolución, el stock vuelve a aumentar.

## Concurrencia y sincronización

Las operaciones de préstamo y devolución utilizan concurrencia mediante hilos para evitar bloquear la interfaz gráfica.

Además, `PrestamoController` utiliza sincronización mediante `synchronized` y transacciones JDBC para mantener la consistencia de los datos.

Durante un préstamo, el sistema bloquea el registro del libro mediante:

```sql
SELECT stock
FROM libros
WHERE id = ?
FOR UPDATE
```

Esto permite evitar inconsistencias cuando se realizan operaciones simultáneas sobre el stock.

Las operaciones utilizan transacciones mediante:

```java
connection.setAutoCommit(false);
```

y finalizan mediante `commit()` o `rollback()` dependiendo del resultado de la operación.

## Programación Orientada a Objetos

El proyecto utiliza distintos conceptos de programación orientada a objetos.

### Herencia

Se utiliza una clase abstracta:

```text
UsuarioBiblioteca
```

de la cual heredan diferentes tipos de usuario.

### Clase abstracta

`UsuarioBiblioteca` define características comunes y un método abstracto:

```java
public abstract String obtenerTipoUsuario();
```

### Polimorfismo

El sistema utiliza referencias del tipo `UsuarioBiblioteca` que pueden representar diferentes implementaciones, como:

```text
Bibliotecario
EstudianteUsuario
```

### Interfaz

La interfaz:

```text
GestionPrestamos
```

define operaciones relacionadas con préstamos y devoluciones.

Las clases correspondientes implementan esta interfaz.

## Patrones de diseño

### MVC

El proyecto separa sus responsabilidades en:

```text
Modelo
Vista
Controlador
```

Esto permite mantener separada la interfaz gráfica de la lógica de negocio y de los datos.

### DAO

Las operaciones de acceso a la base de datos se encuentran encapsuladas en clases DAO.

Esto evita colocar consultas SQL directamente dentro de las ventanas de la aplicación.

### Singleton

La conexión a MySQL se administra mediante:

```text
DatabaseConnection
```

Esta clase implementa el patrón Singleton para mantener una única instancia de la conexión utilizada por el sistema.

## Base de datos

El sistema utiliza una base de datos MySQL llamada:

```text
biblioteca
```

Las principales tablas son:

```text
usuarios
estudiantes
categorias
libros
prestamos
```

Relaciones principales:

```text
categorias
    |
    └── libros
           |
           └── prestamos
                  |
                  └── estudiantes
```

El script de creación y población de la base de datos se encuentra en el archivo SQL incluido en la entrega.

## Configuración de la base de datos

Antes de ejecutar el sistema se debe tener instalado y ejecutándose MySQL.

La conexión utiliza:

```text
Host: localhost
Puerto: 3306
Base de datos: biblioteca
Usuario: root
```

La contraseña debe configurarse localmente en `DatabaseConnection`.

Por motivos de seguridad, no se incluye ninguna contraseña real dentro del repositorio.

## Instalación y ejecución

### 1. Clonar el repositorio

Clonar el repositorio desde GitHub.

### 2. Abrir el proyecto

Abrir la carpeta del proyecto utilizando IntelliJ IDEA.

### 3. Configurar MySQL

Crear la base de datos `biblioteca` ejecutando el script SQL incluido en el proyecto.

### 4. Configurar la conexión

Verificar las credenciales de MySQL en:

```text
src/dao/DatabaseConnection.java
```

### 5. Verificar el conector JDBC

El proyecto incluye:

```text
lib/mysql-connector-j-26.7.0.jar
```

Este archivo corresponde al conector utilizado para establecer la conexión entre Java y MySQL.

### 6. Ejecutar el sistema

Ejecutar:

```text
src/main/Main.java
```

La aplicación comenzará mostrando la pantalla de inicio de sesión.

## Usuarios de prueba

Para probar el sistema se pueden utilizar los usuarios incluidos en la base de datos:

### Bibliotecario

```text
Correo: antonia@correo.cl
Contraseña: clave123
Rol: bibliotecario
```

### Estudiante

```text
Correo: carlos@correo.cl
Contraseña: clave123
Rol: estudiante
```

## Manejo de errores

El sistema incorpora validaciones de entrada y manejo de excepciones mediante `try-catch`, especialmente en las operaciones relacionadas con JDBC y la base de datos.

También se utilizan mensajes de advertencia, confirmación y error dentro de la interfaz gráfica para informar al usuario sobre el resultado de las operaciones.

## Autor

Proyecto desarrollado como actividad académica individual para la implementación de un sistema de gestión de biblioteca escolar utilizando programación orientada a objetos, JDBC, MySQL, arquitectura MVC y patrones de diseño.
