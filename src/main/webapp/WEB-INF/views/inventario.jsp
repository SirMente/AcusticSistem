<%@page import="models.Producto"%>
<%@page import="models.Proveedor"%>
<%@page import="java.util.List"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="java.nio.charset.StandardCharsets"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%!
// Bloque de DECLARACIÓN: Se usa para declarar métodos para usar en el JSP.
    String encode(String s) {
        if (s == null) return "";
        try {
            // Se usa para sanitizar valores dentro de HTML attributes/JS data-*
            return URLEncoder.encode(s, StandardCharsets.UTF_8.toString())
                             .replace("+", "%20");
        } catch (Exception e) {
            return s; 
        }
    }
%>

<%
    String contextPath = request.getContextPath();
    List<Producto> productos = (List<Producto>) request.getAttribute("productos");
    List<Proveedor> proveedores = (List<Proveedor>) request.getAttribute("proveedores");

    // Inicializar las listas si son nulas
    if (productos == null) {
        productos = java.util.Collections.emptyList();
    }
    if (proveedores == null) {
        proveedores = java.util.Collections.emptyList();
    }
%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">

        <%-- Rutas CSS --%>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css" integrity="sha512-2SwdPD6INVrV/lHTZbO2nodKhrnDdJK9/kg2XD1r9uGqPo1cUbujc+IYdlYdEErWNu69gVcYgdxlmVmzTWnetw==" crossorigin="anonymous" referrerpolicy="no-referrer" />
        <link rel="stylesheet" href="<%= contextPath%>/assets/css/Encabezado.css">
        <link rel="stylesheet" href="<%= contextPath%>/assets/css/Inventario.css">
        <link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css' rel='stylesheet'>

        <title>Inventario | Acustica</title>
    </head>
    <body>
        <header>
            <div class="brand">
                <img src="<%= contextPath%>/assets/Imagenes/01.jpg" alt="Logo">
                <span>Acustica</span>
            </div>
            <nav>
                <ul class="navbar" id="navbar">
                    <li><a href="<%= contextPath%>/Dashboard">Inicio</a></li>
                    <li><a href="<%= contextPath%>/InventarioController" class="active">Inventario</a></li>
                    <li><a href="<%= contextPath%>/Finanzas">Finanzas</a></li>
                    <li><a href="<%= contextPath%>/Proyectos">Proyectos</a></li>
                    <li><a href="<%= contextPath%>/GestionProformas">Proformas</a></li>
                    <li><a href="<%= contextPath%>/Proveedores">Proveedores</a></li>
                </ul>
            </nav>
            <div class="main">
                <i class='bx bx-search'></i>
                <input type="search" placeholder="Buscar">
                <i class='bx bxs-notification'></i>
                <img src="<%= contextPath%>/assets/Imagenes/03.jpg" alt="Usuario">
                <i class='bx bx-menu' id="menu-icon"></i>
            </div>
        </header>

        <main class="inventory-container">
            <div class="inventory-header">
                <h2>Inventario</h2>
                <button class="add-btn" id="open-modal-btn"><i class='bx bx-plus'></i> Agregar Artículo</button>
            </div>

            <div class="search-box">
                <input type="text" placeholder="Buscar artículos por nombre, SKU, etc.">
            </div>

            <table class="inventory-table" id="tabla-inventario">
                <thead>
                    <tr>
                        <th>Artículo</th>
                        <th>Descripción</th>
                        <th>Cantidad en Stock</th>
                        <th>Precio Unitario</th>
                        <th>Proveedor</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        if (!productos.isEmpty()) {
                            for (Producto producto : productos) {
                    %>
                    <tr <% if (producto.getCantidad() < 5) { %>class="low-stock"<% } %>>
                        <td>
                            <%= producto.getNombre()%>
                            <% if (producto.getCantidad() < 5) { %>
                                <span class="stock-badge">Stock Bajo</span>
                            <% } %>
                        </td>
                        <td><%= producto.getDescripcion()%></td>
                        <td><%= producto.getCantidad()%></td>
                        <td>$<%= String.format("%,.2f", producto.getPrecioUnitario())%></td>
                        <td><%= producto.getProveedor()%></td>
                        <td class="action-buttons">
                            <%-- Botón para EDITAR: Usamos encode() para manejar caracteres especiales en data-* --%>
                            <a href="#" class="edit-btn" title="Editar"
                               data-id="<%= producto.getId()%>"
                               data-nombre="<%= encode(producto.getNombre())%>"
                               data-descripcion="<%= encode(producto.getDescripcion())%>"
                               data-cantidad="<%= producto.getCantidad()%>"
                               data-precio="<%= producto.getPrecioUnitario()%>"
                               data-proveedor="<%= encode(producto.getProveedor())%>">
                                 <i class='bx bx-pencil action-icon'></i>
                            </a>

                            <%-- Formulario para ELIMINAR --%>
                            <form action="<%= contextPath%>/InventarioController" method="POST" style="display: inline;">
                                <input type="hidden" name="action" value="eliminar">
                                <input type="hidden" name="id" value="<%= producto.getId()%>">
                                <button type="submit" class="action-btn delete-btn" title="Eliminar Producto" 
                                        onclick="return confirm('¿Estás seguro de que quieres eliminar <%= producto.getNombre()%>?');">
                                    <i class='bx bx-trash'></i>
                                </button>
                            </form>
                        </td>
                    </tr>
                    <%
                        }
                    } else {
                    %>
                    <tr>
                        <td colspan="6" style="text-align: center; padding: 20px;">
                            No hay artículos en el inventario. ¡Agrega uno!
                        </td>
                    </tr>
                    <%
                        }
                    %>
                </tbody>
            </table>

            <%-- 🪟 MODAL --%>
            <div id="add-item-modal" class="modal-overlay">
                <div class="modal-content">
                    <div class="modal-header">
                        <h3 id="modal-title">Agregar Nuevo Artículo</h3>
                        <button class="close-btn" id="close-modal-btn" aria-label="Cerrar modal"><i class='bx bx-x'></i></button>
                    </div>

                    <form class="modal-body" id="add-item-form" action="<%= contextPath%>/InventarioController" method="POST">
                        <input type="hidden" id="producto-id" name="id" value="0">
                        <input type="hidden" name="action" id="action-type" value="agregar">
                        
                        <%-- Campo imagen_url: Asumimos que se maneja de forma oculta o con valor por defecto --%>
                        <input type="hidden" id="imagen-url" name="imagenUrl" value=""> 

                        <label for="articulo-nombre">Nombre del Artículo:</label>
                        <input type="text" id="articulo-nombre" name="nombre" required>

                        <label for="articulo-descripcion">Descripción:</label>
                        <textarea id="articulo-descripcion" name="descripcion" rows="3"></textarea>

                        <label for="articulo-cantidad">Cantidad en Stock:</label>
                        <input type="number" id="articulo-cantidad" name="cantidad" min="0" required>

                        <label for="articulo-precio">Precio Unitario:</label>
                        <input type="number" id="articulo-precio" name="precio" step="0.01" min="0" required> 

                        <label for="articulo-proveedor">Proveedor:</label>
                        <select id="articulo-proveedor" name="proveedor" required>
                            <option value="">-- Seleccione un proveedor --</option>
                            <%
                                for (Proveedor prov : proveedores) {
                            %>
                            <option value="<%= prov.getNombre()%>">
                                <%= prov.getNombre()%>
                            </option>
                            <%
                                }
                            %>
                        </select>

                        <button type="submit" class="submit-btn" id="submit-btn">Guardar Artículo</button>
                    </form>
                </div>
            </div>
        </main>

        <script src="<%= contextPath%>/JavaScript/Inventario.js"></script>
    </body>
</html>