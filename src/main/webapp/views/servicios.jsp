<%@page import="models.Servicio"%>
<%@page import="java.util.List"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="java.nio.charset.StandardCharsets"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%!
// Bloque de DECLARACIÓN: Se usa para declarar métodos para usar en el JSP.
// Función para codificar URLs para atributos data-* (similar a tu encode original)
    String encode(String s) {
        if (s == null) {
            return "";
        }
        try {
            return URLEncoder.encode(s, StandardCharsets.UTF_8.toString())
                    .replace("+", "%20");
        } catch (Exception e) {
            return s;
        }
    }
%>

<%
    String contextPath = request.getContextPath();
    // 🔑 Obtener la lista de servicios del Controller
    List<Servicio> servicios = (List<Servicio>) request.getAttribute("listaServicios");

    // Inicializar la lista si es nula
    if (servicios == null) {
        servicios = java.util.Collections.emptyList();
    }

    // Manejo de mensajes de error/éxito del Controller
    String mensaje = (String) request.getAttribute("mensaje");
    String error = (String) request.getAttribute("error");

    // URL del controlador de servicios
    final String CONTROLLER_URL = contextPath + "/ServicioController";
%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Gestión de Servicios | Acustica</title>

        <%-- **AJUSTA ESTAS RUTAS SEGÚN TU PROYECTO** --%>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css">
        <link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css' rel='stylesheet'>
        <link rel="stylesheet" href="<%= contextPath%>/assets/css/Encabezado.css">
        <link rel="stylesheet" href="<%= contextPath%>/assets/css/Inventario.css"> 
        <%-- Usamos Inventario.css para mantener el estilo --%>
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
                    <li><a href="<%= request.getContextPath()%>/ServicioController" class="active">Servicios</a></li>
                    <li><a href="<%= request.getContextPath()%>/GestionPersonal" >Personal</a></li>
                    <li><a href="<%= request.getContextPath()%>/InventarioController">Inventario</a></li>
                    <li><a href="<%= request.getContextPath()%>/Finanzas" >Finanzas</a></li>
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
                <h2>Gestión de Servicios</h2>
                <button class="add-btn" id="open-modal-btn"><i class='bx bx-plus'></i> Agregar Servicio</button>
            </div>

            <%-- MUESTRA DE MENSAJES --%>
            <% if (mensaje != null) {%>
            <div style="color: green; padding: 10px; border: 1px solid green; background-color: #e6ffe6; margin-bottom: 20px; border-radius: 5px; >Operación exitosa: <%= mensaje%></div>
            <% } %>
            <% if (error != null) {%>
            <div style="color: red; padding: 10px; border: 1px solid red; background-color: #ffe6e6; margin-bottom: 20px; border-radius: 5px;">Error: <%= error%></div>
            <% } %>

            <div class="search-box">
                <input type="text" placeholder="Buscar servicios por nombre, descripción, etc.">
            </div>

            <table class="inventory-table" id="tabla-servicios">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Nombre del Servicio</th>
                        <th>Descripción</th>
                        <th>Tarifa Base</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        if (!servicios.isEmpty()) {
                            for (Servicio servicio : servicios) {
                    %>
                    <tr>
                        <td><%= servicio.getId_servicio()%></td>
                        <td><%= servicio.getNombre()%></td>
                        <td><%= servicio.getDescripcion() != null && !servicio.getDescripcion().isEmpty() ? servicio.getDescripcion() : "N/A"%></td>

                        <%-- Formatear la tarifa como moneda --%>
                        <td>$<%= String.format("%,.2f", servicio.getTarifa_base())%></td>

                        <td class="action-buttons">
                            <%-- Botón para EDITAR: Usa data-attributes para pasar los datos --%>
                            <a href="#" class="edit-btn" title="Editar"
                               data-id="<%= servicio.getId_servicio()%>"
                               data-nombre="<%= encode(servicio.getNombre())%>"
                               data-descripcion="<%= encode(servicio.getDescripcion())%>"
                               data-tarifa="<%= servicio.getTarifa_base()%>">
                                <i class='bx bx-pencil action-icon'></i>
                            </a>

                            <%-- Formulario para ELIMINAR --%>
                            <form action="<%= CONTROLLER_URL%>" method="POST" style="display: inline;">
                                <input type="hidden" name="accion" value="eliminar">
                                <input type="hidden" name="id_servicio" value="<%= servicio.getId_servicio()%>">
                                <button type="submit" class="action-btn delete-btn" title="Eliminar Servicio" 
                                        onclick="return confirm('¿Estás seguro de que quieres eliminar el servicio: <%= servicio.getNombre()%>?');">
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
                        <td colspan="5" style="text-align: center; padding: 20px;">
                            No hay servicios registrados. ¡Agrega uno!
                        </td>
                    </tr>
                    <%
                        }
                    %>
                </tbody>
            </table>

            <%-- 🪟 MODAL DE AGREGAR/EDITAR SERVICIO --%>
            <div id="add-item-modal" class="modal-overlay">
                <div class="modal-content">
                    <div class="modal-header">
                        <h3 id="modal-title">Agregar Nuevo Servicio</h3>
                        <button class="close-btn" id="close-modal-btn" aria-label="Cerrar modal"><i class='bx bx-x'></i></button>
                    </div>

                    <form class="modal-body" id="add-item-form" action="<%= CONTROLLER_URL%>" method="POST">
                        <input type="hidden" id="servicio-id" name="id_servicio" value="0">
                        <input type="hidden" name="accion" id="action-type" value="agregar">

                        <label for="servicio-nombre">Nombre del Servicio:</label>
                        <input type="text" id="servicio-nombre" name="nombre" required>

                        <label for="servicio-descripcion">Descripción:</label>
                        <textarea id="servicio-descripcion" name="descripcion" rows="3"></textarea>

                        <label for="servicio-tarifa">Tarifa Base ($):</label>
                        <%-- El nombre debe coincidir con el Controller: "tarifa_base" --%>
                        <input type="number" id="servicio-tarifa" name="tarifa_base" step="0.01" min="0" required> 

                        <button type="submit" class="submit-btn" id="submit-btn">Guardar Servicio</button>
                    </form>
                </div>
            </div>
        </main>

        <script>
            // JavaScript para manejar el modal (similar a Inventario.js, adaptado para Servicios)
            document.addEventListener('DOMContentLoaded', function () {
                const modal = document.getElementById('add-item-modal');
                const form = document.getElementById('add-item-form');
                const tituloModal = document.getElementById('modal-title');
                const btnSubmit = document.getElementById('submit-btn');

                const btnOpenModal = document.getElementById('open-modal-btn');
                const btnCloseModal = document.getElementById('close-modal-btn');

                // CAMPOS DE DATOS DEL SERVICIO
                const inputId = document.getElementById('servicio-id');
                const inputNombre = document.getElementById('servicio-nombre');
                const inputDescripcion = document.getElementById('servicio-descripcion');
                const inputTarifa = document.getElementById('servicio-tarifa');
                const inputAccion = document.getElementById('action-type');

                // --- Funciones del Modal (Coincide con la corrección anterior) ---
                function openModal() {
                    modal.classList.add('active');
                }

                function closeModal() {
                    modal.classList.remove('active');
                }

                // 1. Botón AGREGAR (Abrir modal limpio)
                btnOpenModal.addEventListener('click', function () {
                    // Resetear el formulario para AGREGAR
                    inputId.value = '0';
                    inputAccion.value = 'agregar';
                    tituloModal.textContent = 'Agregar Nuevo Servicio';
                    btnSubmit.textContent = 'Guardar Servicio';
                    form.reset();
                    openModal();
                });

                // 2. Botones EDITAR (Pre-llenar el formulario)
                document.querySelectorAll('.edit-btn').forEach(button => {
                    button.addEventListener('click', function (e) {
                        e.preventDefault();

                        // Llenar el formulario con los datos del data-attribute
                        inputId.value = this.getAttribute('data-id');
                        inputNombre.value = decodeURIComponent(this.getAttribute('data-nombre'));
                        inputDescripcion.value = decodeURIComponent(this.getAttribute('data-descripcion'));
                        inputTarifa.value = this.getAttribute('data-tarifa');

                        // Cambiar la UI para EDITAR y la acción del Controller
                        inputAccion.value = 'actualizar';
                        tituloModal.textContent = 'Editar Servicio';
                        btnSubmit.textContent = 'Guardar Cambios';

                        openModal();
                    });
                });

                // 3. Lógica para cerrar el modal
                btnCloseModal.addEventListener('click', function () {
                    closeModal();
                });

                // Cerrar si se clickea fuera del modal
                window.addEventListener('click', function (event) {
                    if (event.target === modal) {
                        closeModal();
                    }
                });
            });
        </script>
    </body>
</html>