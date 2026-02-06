# Microservicio: ms-books-catalogue

Este documento detalla la interfaz técnica del microservicio de catálogo de libros, diseñado bajo principios de arquitectura RESTful.

## Tecnologías y Estándares
- **Protocolo:** HTTP/1.1
- **Formato de datos:** JSON
- **Arquitectura:** REST (Representational State Transfer)

## Modelo de Datos y Validaciones

| Campo | Tipo | Validaciones | Descripción |
| :--- | :--- | :--- | :--- |
| **id** | Long | Autoincremental | Identificador único interno. |
| **title** | String | `@NotBlank`, `Size(max=200)` | Título del libro. |
| **author** | String | `@NotBlank` | Nombre del autor. |
| **editorial** | String | `@NotBlank` | Sello editorial. |
| **pages** | Integer | `@Min(1)` | Número de páginas. |
| **genres** | List<String>| `@NotEmpty` | Lista de categorías/géneros. |
| **publishedDate**| LocalDate | `@PastOrPresent` | Fecha de publicación (YYYY-MM-DD). |
| **rating** | Double | `@Min(1.0)`, `@Max(5.0)` | Valoración media. |
| **price** | Double | `@DecimalMin("0.0")` | Precio en moneda local. |
| **coverImage** | String | `@URL` | URL de la imagen de portada. |
| **dimensions** | Object | `@Valid`, `@NotNull` | Objeto con `height` y `width`. |
| **visible** | Boolean | `@NotNull` | Disponibilidad en el catálogo. |

---

# Documentación Detallada de Endpoints: ms-books-catalogue

Esta sección describe el comportamiento técnico y la lógica de negocio de cada operación del microservicio.

---

## 1. Buscar Libros (Buscador Dinámico)
Permite localizar libros combinando múltiples criterios de búsqueda.

- **Método:** `GET`
- **Ruta:** `/books`
- **Parámetros de Consulta (Query Params):**
    - `title` (String): Búsqueda parcial.
    - `author` (String): Búsqueda parcial.
    - `editorial` (String): Búsqueda exacta.
    - `genre` (String): Filtra libros que contengan este género en su lista.
    - `ratingMin` (Double): Libros con nota mayor o igual a este valor.
    - `visible` (Boolean): Filtrar por estado de visibilidad.
- **Lógica:** Si no se envían parámetros, el sistema retorna todos los libros con `visible: true`.
- **Respuestas:**
    - `200 OK`: Retorna un array de objetos. Si no hay resultados, retorna `[]`.

---

## 2. Obtener Detalle por ID
Recupera toda la información de un libro específico para la vista de detalle.

- **Método:** `GET`
- **Ruta:** `/books/{id}`
- **Lógica:** Busca el registro por su llave primaria única.
- **Respuestas:**
    - `200 OK`: Retorna el objeto JSON completo (incluyendo dimensiones y géneros).
    - `404 Not Found`: Si el identificador no existe en la base de datos.

---

## 3. Crear Nuevo Libro
Registra un nuevo libro en el catálogo validando la integridad de los datos.

- **Método:** `POST`
- **Ruta:** `/books`
- **Cuerpo (JSON):** Objeto libro (sin el campo `id`).
- **Lógica:** El sistema valida que el `rating` sea decimal (1.0 - 5.0) y que los campos obligatorios como `title` y `author` no estén vacíos.
- **Respuestas:**
    - `201 Created`: Retorna el objeto creado con su ID generado.
    - `400 Bad Request`: Error de validación en los campos enviados.

---

## 4. Actualización Total (Reemplazo)
Sustituye toda la información de un libro existente.

- **Método:** `PUT`
- **Ruta:** `/books/{id}`
- **Cuerpo (JSON):** Objeto libro completo.
- **Lógica:** Se utiliza para ediciones generales. Si un campo opcional no se envía, se actualizará como nulo en la base de datos.
- **Respuestas:**
    - `200 OK`: Retorna el objeto actualizado.
    - `400 Bad Request`: Datos de entrada inválidos.
    - `404 Not Found`: El ID no existe.

---

## 5. Actualización Parcial
Modifica únicamente los campos específicos indicados en la petición.

- **Método:** `PATCH`
- **Ruta:** `/books/{id}`
- **Cuerpo (JSON):** Solo los campos a cambiar (ej: `{"price": 39.99, "visible": false}`).
- **Lógica:** Ideal para cambios rápidos desde el dashboard (ej: cambiar solo el rating o precio). El resto de campos permanecen intactos.
- **Respuestas:**
    - `200 OK`: Retorna el objeto con los cambios aplicados.
    - `404 Not Found`: El ID no existe.

---

## 6. Eliminar Libro
Elimina el registro de la base de datos de forma permanente.

- **Método:** `DELETE`
- **Ruta:** `/books/{id}`
- **Lógica:** Se recomienda para mantenimiento del catálogo.
- **Respuestas:**
    - `204 No Content`: Eliminación exitosa (sin cuerpo de respuesta).
    - `404 Not Found`: El libro no existe o ya fue eliminado anteriormente.

## Matriz de Operaciones REST

| Método Http | Endpoint | Query Params | Cuerpo JSON de la petición | Respuesta JSON de la petición | Códigos HTTP posibles |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **POST** | `/books` | | `{ "title": "string", "author": "string", "editorial": "string", "dimensions": { "height": "string", "width": "string" }, "pages": "integer", "genres": ["string"], "publishedDate": "string", "rating": "double", "price": "double", "coverImage": "string", "visible": "boolean" }` | `{ "id": "integer", "title": "string", "author": "string", ... }` | 201 Created, 400 Bad Request |
| **GET** | `/books/{id}` | | | `{ "id": "integer", "title": "string", "author": "string", "editorial": "string", "dimensions": { "height": "string", "width": "string" }, "pages": "integer", "genres": ["string"], "publishedDate": "string", "rating": "double", "price": "double", "coverImage": "string", "visible": "boolean" }` | 200 OK, 404 Not Found |
| **PUT** | `/books/{id}` | | `{ "title": "string", "author": "string", "editorial": "string", "dimensions": { "height": "string", "width": "string" }, "pages": "integer", "genres": ["string"], "publishedDate": "string", "rating": "double", "price": "double", "coverImage": "string", "visible": "boolean" }` | `{ "id": "integer", "title": "string", "author": "string", ... }` | 200 OK, 400 Bad Request, 404 Not Found |
| **PATCH** | `/books/{id}` | | `{ "rating": "double", "visible": "boolean", "price": "double" }` <br>*(campos opcionales)* | `{ "id": "integer", "title": "string", "author": "string", ... }` | 200 OK, 400 Bad Request, 404 Not Found |
| **DELETE** | `/books/{id}` | | | *(Vacío)* | 204 No Content, 404 Not Found |
| **GET** | `/books` | `title`, `author`, `editorial`, `genre`, `ratingMin`, `visible` | | `[ { "id": "integer", "title": "string", "author": "string", "editorial": "string", ... } ]` | 200 OK |
