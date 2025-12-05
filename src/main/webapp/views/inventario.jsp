<%@page import="models.Producto"%>
<%@page import="models.Proveedor"%>
<%@page import="java.util.List"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="java.nio.charset.StandardCharsets"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%!
// Bloque de DECLARACIÓN: Se usa para declarar métodos para usar en el JSP.
    String encode(String s) {
        if (s == null) {
            return "";
        }
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
    // 🔑 CORRECCIÓN: El Controller envía la lista como "listaProductos"
    List<Producto> productos = (List<Producto>) request.getAttribute("listaProductos");

    // Lista de proveedores (asumimos que esta lista viene de un ProveedorDAO en el Controller)
    List<Proveedor> proveedores = (List<Proveedor>) request.getAttribute("proveedores");

    // Inicializar las listas si son nulas
    if (productos == null) {
        productos = java.util.Collections.emptyList();
    }
    if (proveedores == null) {
        proveedores = java.util.Collections.emptyList();
    }

    // Manejo de mensajes de error/éxito del Controller
    String mensaje = request.getParameter("mensaje");
    String error = (String) request.getAttribute("error");
%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Inventario | Acustica</title>

        <%-- **AGREGAR RUTAS CSS AQUÍ** (Se omiten para concisión) --%>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css">
        <link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css' rel='stylesheet'>
        <link rel="stylesheet" href="<%= contextPath%>/assets/css/Encabezado.css">
        <link rel="stylesheet" href="<%= contextPath%>/assets/css/Inventario.css">
    </head>
    <body>

        <%-- *** ENCABEZADO (Header) *** --%>
        <header>
            <div class="brand">
                <img src="<%= request.getContextPath()%>/Imagenes/01.jpg" alt="Logo">
                <span>Acústica</span>
            </div>

            <nav>
                <ul class="navbar" id="navbar">
                    <li><a href="<%= request.getContextPath()%>/dashboard">Inicio</a></li>
                    <li><a href="<%= request.getContextPath()%>/Proveedores" >Proveedores</a></li>
                    <li><a href="<%= request.getContextPath()%>/GestionClientes">Clientes</a></li>
                    <li><a href="<%= request.getContextPath()%>/ServicioController" >Servicios</a></li>
                    <li><a href="<%= request.getContextPath()%>/GestionPersonal" >Personal</a></li>
                    <li><a href="<%= request.getContextPath()%>/InventarioController" class="active">Inventario</a></li>
                    <li><a href="<%= request.getContextPath()%>/Finanzas">Finanzas</a></li>
                    <li><a href="<%= request.getContextPath()%>/GestionProformas">Proformas</a></li>
                    <li><a href="<%= request.getContextPath()%>/Proyectos">Proyectos</a></li>
                </ul>
            </nav>

            <div class="main">
                <i class='bx bxs-notification'></i>

                <img src="<%= request.getContextPath()%>/Imagenes/03.jpg" alt="Usuario">

                <!-- 🔥 Botón de Cerrar Sesión -->
                <a href="<%= request.getContextPath()%>/logout" 
                   class="btn-logout" 
                   style="margin-left: 15px; color: #e74c3c; font-weight: bold; text-decoration: none;">
                    Cerrar sesión
                </a>

                <i class='bx bx-menu' id="menu-icon"></i>
            </div>
        </header>

        <main class="inventory-container">
            <div class="inventory-header">
                <h2>Inventario</h2>
                <button class="add-btn" id="open-modal-btn"><i class='bx bx-plus'></i> Agregar Artículo</button>
            </div>

            <%-- MENSAJES --%>
            <%
                String mensajeExito = request.getParameter("mensaje");
                String mensajeError = (String) request.getAttribute("error");

                if (mensajeExito != null) {
                    String textoMensaje = "";
                    if (mensajeExito.equals("agregado_exitoso"))
                        textoMensaje = "Cliente agregado exitosamente.";
                    else if (mensajeExito.equals("editado_exitoso"))
                        textoMensaje = "Cliente editado exitosamente.";
                    else if (mensajeExito.equals("eliminado_exitoso"))
                        textoMensaje = "Cliente eliminado exitosamente.";
            %>
            <div style="color: green; padding: 10px; border: 1px solid green; background-color: #e6ffe6; margin-bottom: 20px; border-radius: 5px;">
                ✅ <%= textoMensaje%>
            </div>
            <% } else if (mensajeError != null) {%>
            <div style="color: red; padding: 10px; border: 1px solid red; background-color: #ffe6e6; margin-bottom: 20px; border-radius: 5px;">
                ❌ Error: <%= mensajeError%>
            </div>
            <% } %>

            <div class="search-box">
                <input type="text" placeholder="Buscar artículos por nombre, SKU, etc.">
            </div>

            <table class="inventory-table" id="tabla-inventario">
                <thead>
                    <tr>
                        <th>Artículo</th>
                        <th>Imagen</th>
                        <th>Marca / Modelo</th>
                        <th>Stock Actual</th>
                        <th>Stock Mínimo</th>
                        <th>Precio Unitario</th>
                        <th>RUC Proveedor</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        if (!productos.isEmpty()) {
                            for (Producto producto : productos) {
                    %>
                    <tr <% if (producto.getCantidad() <= producto.getStock_minimo()) { %>class="low-stock"<% }%>>
                        <td>
                            <%= producto.getNombre()%></td>
                        <td>
                            <% if (producto.getImagen_url() != null) {%>
                            <img src="<%= contextPath + "/" + producto.getImagen_url()%>" 
                                 style="width: 60px; height: 60px; object-fit: cover; border-radius: 8px;">
                            <% } else { %>
                            No imagen
                            <% }%>
                        </td>
                        <td><%= producto.getMarca()%> / <%= producto.getModelo()%></td>
                        <td><%= producto.getCantidad()%>
                            <% if (producto.getCantidad() <= producto.getStock_minimo()) { %>
                            <span class="stock-badge">BAJO</span>
                            <% }%>
                        </td>
                        <td><%= producto.getStock_minimo()%></td>
                        <%-- 🔑 CORRECCIÓN: Usar getPrecio_unitario() --%>
                        <td>$<%= String.format("%,.2f", producto.getPrecio_unitario())%></td>
                        <%-- 🔑 CORRECCIÓN: Mostrar ruc_proveedor --%>
                        <td><%= producto.getRuc_proveedor() != null ? producto.getRuc_proveedor() : "N/A"%></td>
                        <td class="action-buttons">
                            <%-- Botón para EDITAR --%>
                            <a href="#" class="edit-btn" title="Editar"
                               data-id="<%= producto.getId_producto()%>"
                               data-nombre="<%= encode(producto.getNombre())%>"
                               data-descripcion="<%= encode(producto.getDescripcion())%>"
                               data-marca="<%= encode(producto.getMarca())%>"
                               data-modelo="<%= encode(producto.getModelo())%>"
                               data-cantidad="<%= producto.getCantidad()%>"
                               data-stock-minimo="<%= producto.getStock_minimo()%>"
                               data-precio="<%= producto.getPrecio_unitario()%>"
                               data-ruc-proveedor="<%= producto.getRuc_proveedor() != null ? producto.getRuc_proveedor() : ""%>"
                               data-imagen-url="<%= encode(producto.getImagen_url())%>">
                                <i class='bx bx-pencil action-icon'></i>
                            </a>

                            <%-- Formulario para ELIMINAR --%>
                            <%-- Formulario para ELIMINAR (CORREGIDO) --%>
                            <form action="<%= contextPath%>/InventarioController" method="POST" style="display: inline;">
                                <input type="hidden" name="accion" value="eliminar">
                                <input type="hidden" name="id_producto" value="<%= producto.getId_producto()%>">
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
                        <td colspan="7" style="text-align: center; padding: 20px;">
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

                    <form class="modal-body" id="add-item-form" action="<%= contextPath%>/InventarioController" method="POST" enctype="multipart/form-data">
                        <input type="hidden" id="producto-id" name="id_producto" value="0">
                        <input type="hidden" name="accion_update" id="action-type" value="agregar">
                        <input type="hidden" id="imagen-url" name="imagen_url" value=""> 

                        <label for="articulo-nombre">Nombre del Artículo:</label>
                        <input type="text" id="articulo-nombre" name="nombre" required>

                        <label for="imagen">Imagen del producto:</label>
                        <input type="file" id="imagen" name="imagen" accept="image/*">

                        <!-- PREVIEW DE IMAGEN -->
                        <img id="preview-img" 
                             src="" 
                             style="display: none; width: 120px; height: 120px; object-fit: cover; margin-top: 10px; border-radius: 8px;">

                        <label for="articulo-marca">Marca:</label>
                        <input type="text" id="articulo-marca" name="marca" required>

                        <label for="articulo-modelo">Modelo:</label>
                        <input type="text" id="articulo-modelo" name="modelo" required>

                        <label for="articulo-descripcion">Descripción:</label>
                        <textarea id="articulo-descripcion" name="descripcion" rows="3"></textarea>

                        <label for="articulo-stock-minimo">Stock Mínimo (Alerta):</label>
                        <input type="number" id="articulo-stock-minimo" name="stock_minimo" min="1" required>

                        <label for="articulo-cantidad">Cantidad en Stock:</label>
                        <input type="number" id="articulo-cantidad" name="cantidad" min="0" required>

                        <label for="articulo-precio">Precio Unitario:</label>
                        <%-- 🔑 CORRECCIÓN: Nombre del campo a 'precio_unitario' para coincidir con el Controller --%>
                        <input type="number" id="articulo-precio" name="precio_unitario" step="0.01" min="0" required> 

                        <label for="ruc-proveedor">RUC Proveedor:</label>
                        <%-- Usamos un <select> para proveedores si la lista viene del Controller --%>
                        <select id="ruc-proveedor" name="ruc_proveedor">
                            <option value="">-- Sin Proveedor --</option>
                            <%
                                for (Proveedor prov : proveedores) {
                            %>
                            <option value="<%= prov.getRuc()%>">
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

        <script>
                            const contextPath = "<%= request.getContextPath()%>";
        </script>
        <script src="<%= contextPath%>/JavaScript/Inventario.js"></script>
    </body>
</html>