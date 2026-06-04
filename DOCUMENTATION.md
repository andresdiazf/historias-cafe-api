# Documentación del Backend - Historias de Café

## Arquitectura General

Este es un backend Spring Boot para un e-commerce de café. Sigue una arquitectura en capas típica:
- **Controller**: Maneja las peticiones HTTP
- **Service**: Contiene la lógica de negocio
- **Repository**: Acceso a datos (JPA)
- **Model**: Entidades de la base de datos
- **DTO**: Objetos de transferencia de datos
- **Config**: Configuraciones de la aplicación
- **Security**: Autenticación y autorización

---

## Paquetes y Archivos

### 📁 Paquete Raíz

#### `BackendApplication.java`
- **Propósito**: Punto de entrada de la aplicación Spring Boot
- **Función**: Inicializa el contexto de Spring y arranca la aplicación
- **Anotaciones**: `@SpringBootApplication`

---

### 📁 config/

#### `SecurityConfig.java`
- **Propósito**: Configuración de seguridad de Spring Security
- **Función**:
  - Define qué endpoints requieren autenticación
  - Configura el filtro JWT
  - Establece el proveedor de autenticación
  - Configura el encoder de contraseñas (BCrypt)
- **Permisos**:
  - GET: Público para todos los endpoints
  - POST/PUT/PATCH/DELETE: Requiere rol ADMIN
  - POST orders/payments: Requiere rol ADMIN o CLIENT

#### `CorsConfig.java`
- **Propósito**: Configuración de CORS (Cross-Origin Resource Sharing)
- **Función**: Permite que el frontend acceda al backend desde diferentes orígenes
- **Orígenes permitidos**:
  - localhost:5503, 127.0.0.1:5503, 127.0.0.1:5500 (desarrollo)
  - localhost:5173, 127.0.0.1:5173 (Vite/Vue)
  - https://historiasdecafe.github.io (GitHub Pages)
  - https://proyecto-historiasdecafe-frontend-vue-1.onrender.com (Render)

---

### 📁 security/

#### `JwtService.java`
- **Propósito**: Servicio para generar y validar tokens JWT
- **Función**:
  - Genera tokens JWT con claims personalizados
  - Extrae el username del token
  - Valida si el token es válido y no ha expirado
  - Configuración: 24 horas de expiración por defecto

#### `JwtAuthenticationFilter.java`
- **Propósito**: Filtro que intercepta cada petición para validar JWT
- **Función**:
  - Extrae el token del header `Authorization: Bearer <token>`
  - Valida el token y carga el usuario en el contexto de seguridad
  - Si el token es inválido, continúa sin autenticar
- **Extiende**: `OncePerRequestFilter`

#### `CustomUserDetailsService.java`
- **Propósito**: Implementación personalizada de UserDetailsService
- **Función**:
  - Carga el usuario desde la base de datos por email
  - Convierte el rol del usuario a authorities de Spring Security
  - Maneja usuarios no encontrados
- **Debug**: Incluye logs para depuración de JWT

---

### 📁 controller/

#### `AuthController.java`
- **Propósito**: Maneja endpoints de autenticación
- **Endpoints**:
  - `POST /auth/register`: Registro de nuevos usuarios
  - `POST /auth/login`: Login y generación de token JWT

#### `UserController.java`
- **Propósito**: Maneja operaciones CRUD de usuarios
- **Endpoints**:
  - `GET /users`: Listar todos los usuarios
  - `POST /users`: Crear usuario
  - `GET /users/{id}`: Obtener usuario por ID
  - `PUT /users/{id}`: Actualizar usuario completo
  - `PATCH /users/{id}`: Actualización parcial (campos específicos)
  - `DELETE /users/{id}`: Eliminar usuario

#### `ProductController.java`
- **Propósito**: Maneja operaciones CRUD de productos
- **Endpoints**:
  - `POST /products`: Crear producto (requiere ADMIN)
  - `GET /products/{id}`: Obtener producto por ID
  - `GET /products`: Listar todos los productos activos
  - `PUT /products/{id}`: Actualizar producto (requiere ADMIN)
  - `DELETE /products/{id}`: Eliminar producto (soft delete, requiere ADMIN)

#### `CategoriesController.java`
- **Propósito**: Maneja operaciones CRUD de categorías
- **Endpoints**:
  - `GET /categories`: Listar todas las categorías
  - `GET /categories/{id}`: Obtener categoría por ID
  - `POST /categories`: Crear categoría (requiere ADMIN)
  - `PUT /categories/{id}`: Actualizar categoría (requiere ADMIN)
  - `DELETE /categories/{id}`: Eliminar categoría (requiere ADMIN)

#### `OrderController.java`
- **Propósito**: Maneja operaciones de pedidos
- **Endpoints**:
  - `POST /orders`: Crear pedido (ADMIN o CLIENT)
  - `GET /orders/{id}`: Obtener pedido por ID
  - `GET /orders`: Listar todos los pedidos
  - `PATCH /orders/{id}/state`: Actualizar estado del pedido (requiere ADMIN)
  - `DELETE /orders/{id}`: Eliminar pedido (requiere ADMIN)

#### `PaymentController.java`
- **Propósito**: Maneja operaciones de pagos (integración con Mercado Pago)
- **Endpoints**:
  - `POST /payments`: Crear preferencia de pago
  - `GET /payments/{id}`: Obtener pago por ID
  - `GET /payments/order/{orderId}`: Obtener pago por orden
  - `PATCH /payments/{id}/status`: Actualizar estado del pago

---

### 📁 service/

#### `AuthService.java`
- **Propósito**: Lógica de negocio para autenticación
- **Función**:
  - `register()`: Crea nuevo usuario con contraseña encriptada
  - `login()`: Autentica credenciales y genera token JWT
- **Detalles**:
  - Email normalizado a minúsculas
  - Rol por defecto: CLIENT
  - Contraseña encriptada con BCrypt

#### `UserService.java`
- **Propósito**: Lógica de negocio para usuarios
- **Función**:
  - CRUD completo de usuarios
  - `patch()`: Actualización parcial usando Map<String, Object>
- **Detalles**:
  - Maneja conversión de String a Role en patch

#### `ProductService.java`
- **Propósito**: Lógica de negocio para productos
- **Función**:
  - CRUD de productos
  - `delete()`: Soft delete (marca active=false)
  - `getAll()`: Solo retorna productos activos
- **Detalles**:
  - Convierte entre Entity y DTO
  - Valida existencia de categoría

#### `CategoriesService.java`
- **Propósito**: Lógica de negocio para categorías
- **Función**: CRUD de categorías
- **Detalles**: Convierte entre Entity y DTO

#### `OrderService.java`
- **Propósito**: Lógica de negocio para pedidos
- **Función**:
  - Crear pedidos con detalles
  - Calcular subtotal y total
  - Actualizar estado de pedidos
- **Detalles**: Maneja relación OneToMany con OrderDetail

#### `PaymentService.java`
- **Propósito**: Lógica de negocio para pagos
- **Función**:
  - Crear preferencias de pago (Mercado Pago)
  - Actualizar estado de pagos
  - Consultar pagos por orden
- **Detalles**: Integración con API de Mercado Pago

---

### 📁 repository/

#### `UserRepository.java`
- **Propósito**: Repositorio JPA para usuarios
- **Función**: Extiende `JpaRepository<User, Long>`
- **Métodos personalizados**:
  - `findByEmail()`: Buscar usuario por email

#### `ProductRepository.java`
- **Propósito**: Repositorio JPA para productos
- **Función**: Extiende `JpaRepository<Product, Long>`
- **Métodos personalizados**:
  - `findByActiveTrue()`: Productos activos
  - `findByActive()`: Productos por estado

#### `CategoriesRepository.java`
- **Propósito**: Repositorio JPA para categorías
- **Función**: Extiende `JpaRepository<Categories, Integer>`

#### `OrderRepository.java`
- **Propósito**: Repositorio JPA para pedidos
- **Función**: Extiende `JpaRepository<Order, Long>`

#### `PaymentRepository.java`
- **Propósito**: Repositorio JPA para pagos
- **Función**: Extiende `JpaRepository<Payment, Long>`

---

### 📁 model/

#### `User.java`
- **Propósito**: Entidad de usuario
- **Campos**:
  - id, name, email, passwordHash
  - role (enum: ADMIN, CLIENT)
  - creationDate, stateActive
- **Anotaciones**: `@Enumerated(EnumType.STRING)` para role

#### `Role.java`
- **Propósito**: Enum de roles de usuario
- **Valores**: ADMIN, CLIENT
- **Anotaciones**:
  - `@JsonCreator`: Deserialización flexible (acepta "CLIENTE")
  - `@JsonValue`: Serialización como string

#### `Product.java`
- **Propósito**: Entidad de producto
- **Campos**:
  - id, name, description, price, stock
  - categories (ManyToOne)
  - imagen, origen, tostado (enum)
  - active (soft delete)
- **Relaciones**: Muchos productos pertenecen a una categoría

#### `Categories.java`
- **Propósito**: Entidad de categoría
- **Campos**:
  - id, toastingType, regionOrigin, presentation
- **Descripción**: Clasificación de productos por tipo de tostado, región y presentación

#### `Order.java`
- **Propósito**: Entidad de pedido
- **Campos**:
  - id, stateOrder, subtotal, total, orderDate
  - user (ManyToOne)
  - details (OneToMany con OrderDetail)
- **Relaciones**: Un pedido pertenece a un usuario, tiene muchos detalles

#### `OrderDetail.java`
- **Propósito**: Detalle de línea de pedido
- **Campos**: id, quantity, price, product, order
- **Relaciones**: Pertenece a un pedido y un producto

#### `Payment.java`
- **Propósito**: Entidad de pago
- **Campos**: id, status, transactionNumber, order
- **Relaciones**: Un pago está asociado a una orden

#### `Tostado.java`
- **Propósito**: Enum de tipos de tostado de café
- **Valores**: Define los tipos de tostado disponibles

---

### 📁 DTO/

#### `AuthRequestDTO.java`
- **Propósito**: DTO para login
- **Campos**: email, password

#### `AuthResponseDTO.java`
- **Propósito**: DTO para respuesta de login
- **Campos**: token, user (UserResponseDTO)

#### `RegisterRequestDTO.java`
- **Propósito**: DTO para registro
- **Campos**: name, email, password, role (opcional)

#### `UserRequestDTO.java`
- **Propósito**: DTO para crear/actualizar usuarios
- **Campos**: name, email, password, role, stateActive

#### `UserResponseDTO.java`
- **Propósito**: DTO para respuesta de usuario
- **Campos**: id, name, email, role, creationDate, stateActive
- **Método**: `from(User)` - Convierte Entity a DTO

#### `ProductRequestDTO.java`
- **Propósito**: DTO para crear/actualizar productos
- **Campos**: name, description, price, stock, categoryId, imagen, origen, tostado
- **Validaciones**: @NotBlank, @NotNull, @Positive, @Min

#### `ProductResponseDTO.java`
- **Propósito**: DTO para respuesta de producto
- **Campos**: id, name, description, price, stock, categoryId, categoryPresentation, imagen, origen, tostado

#### `CategoriesRequestDTO.java`
- **Propósito**: DTO para crear/actualizar categorías
- **Campos**: toastingType, regionOrigin, presentation

#### `CategoriesResponseDTO.java`
- **Propósito**: DTO para respuesta de categoría
- **Campos**: id, toastingType, regionOrigin, presentation

#### `OrderRequestDto.java`
- **Propósito**: DTO para crear pedidos
- **Campos**: userId, details (lista de OrderDetailRequestDto)

#### `OrderResponseDto.java`
- **Propósito**: DTO para respuesta de pedido
- **Campos**: id, stateOrder, subtotal, total, orderDate, userId, details

#### `OrderDetailRequestDto.java`
- **Propósito**: DTO para detalle de pedido
- **Campos**: productId, quantity, price

#### `OrderDetailResponseDto.java`
- **Propósito**: DTO para respuesta de detalle
- **Campos**: id, productId, productName, quantity, price

#### `PaymentRequestDto.java`
- **Propósito**: DTO para crear pago
- **Campos**: orderId, amount

#### `PaymentResponseDto.java`
- **Propósito**: DTO para respuesta de pago
- **Campos**: id, status, transactionNumber, orderId, preferenceId (Mercado Pago)

---

## Flujo de Autenticación

1. **Registro**: 
   - Cliente envía `POST /auth/register`
   - `AuthService.register()` crea usuario con contraseña encriptada
   - Retorna `UserResponseDTO`

2. **Login**:
   - Cliente envía `POST /auth/login` con credenciales
   - `AuthService.login()` autentica con `AuthenticationManager`
   - `JwtService` genera token JWT con el rol del usuario
   - Retorna `AuthResponseDTO` con token y usuario

3. **Peticiones Autenticadas**:
   - Cliente incluye header `Authorization: Bearer <token>`
   - `JwtAuthenticationFilter` intercepta la petición
   - Valida token y carga usuario en `SecurityContext`
   - `SecurityConfig` verifica permisos según rol

---

## Flujo de E-commerce

1. **Productos**:
   - Admin crea productos con `POST /products`
   - Clientes listan productos con `GET /products` (solo activos)

2. **Pedidos**:
   - Cliente crea pedido con `POST /orders`
   - `OrderService` calcula totales y crea detalles
   - Admin puede actualizar estado con `PATCH /orders/{id}/state`

3. **Pagos**:
   - Cliente crea preferencia con `POST /payments`
   - `PaymentService` se integra con Mercado Pago
   - Webhook actualiza estado con `PATCH /payments/{id}/status`

---

## Notas Importantes

- **Soft Delete**: Productos no se eliminan físicamente, se marca `active=false`
- **Passwords**: Siempre encriptados con BCrypt
- **Emails**: Normalizados a minúsculas
- **Roles**: ADMIN tiene acceso completo, CLIENT puede crear pedidos y pagos
- **Transacciones**: Services usan `@Transactional` para consistencia
- **Validaciones**: DTOs usan anotaciones de Jakarta Validation
