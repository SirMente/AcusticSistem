<%@page import="models.Proveedor"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Proveedores - Acústica</title>

        <%-- Rutas CSS usando getContextPath() --%>
        <link rel="stylesheet" href="<%= request.getContextPath()%>/assets/css/Encabezado.css">
        <link rel="stylesheet" href="<%= request.getContextPath()%>/assets/css/Proveedores.css">
        <link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css' rel='stylesheet'>
    </head>
    <body>
        <header>
            <div class="brand">
                <img src="<%= request.getContextPath()%>/Imagenes/01.jpg" alt="Logo">
                <span>Acústica</span>
            </div>

            <nav>
                <ul class="navbar" id="navbar">
                    <li><a href="<%= request.getContextPath()%>/dashboard">Inicio</a></li>
                    <li class="active"><a href="<%= request.getContextPath()%>/Proveedores" class="active">Proveedores</a></li>
                    <li><a href="<%= request.getContextPath()%>/GestionClientes">Clientes</a></li>
                    <li><a href="<%= request.getContextPath()%>/ServicioController">Servicios</a></li>
                    <li><a href="<%= request.getContextPath()%>/InventarioController">Inventario</a></li>
                    <li><a href="<%= request.getContextPath()%>/Finanzas">Finanzas</a></li>
                    <li><a href="<%= request.getContextPath()%>/GestionProformas">Proformas</a></li>
                    <li><a href="<%= request.getContextPath()%>/Proyectos">Proyectos</a></li>
                </ul>
            </nav>

            <div class="main">
                <i class='bx bxs-notification'></i>
                <img src="<%= request.getContextPath()%>/Imagenes/03.jpg" alt="Usuario">
                <i class='bx bx-menu' id="menu-icon"></i>
            </div>
        </header>

        <main class="proveedores-container">
            <div class="proveedores-header">
                <h2>Proveedores</h2>
                <button class="add-btn" id="openModalBtn"><i class='bx bx-plus'></i> Añadir Proveedor</button>
            </div>

            <div class="search-box">
                <input type="text" placeholder="Buscar proveedor por nombre o tipo...">
            </div>

            <%-- BLOQUE DE MENSAJES DE ÉXITO Y ERROR --%>
            <%
                String mensajeExito = request.getParameter("mensaje");
                String mensajeError = (String) request.getAttribute("error");

                // CAMBIO: Actualizar mensajes de error basados en 'ruc'
                if (request.getParameter("error") != null && mensajeError == null) {
                    mensajeError = request.getParameter("error").equals("ruc_invalido") // CAMBIO
                            ? "Error: El RUC del proveedor no es válido."
                            : (request.getParameter("error").equals("bd_eliminar")
                            ? "Error de BD al eliminar el proveedor." : "Error desconocido.");
                }

                if (mensajeExito != null) {
                    String textoMensaje = "";
                    if (mensajeExito.equals("agregado_exitoso"))
                        textoMensaje = "Proveedor agregado exitosamente.";
                    else if (mensajeExito.equals("editado_exitoso"))
                        textoMensaje = "Proveedor editado exitosamente.";
                    else if (mensajeExito.equals("eliminado_exitoso"))
                        textoMensaje = "Proveedor eliminado exitosamente.";
            %>
            <div style="color: green; padding: 10px; border: 1px solid green; background-color: #e6ffe6; margin-bottom: 20px; border-radius: 5px;">
                ✅ Éxito: <%= textoMensaje%>
            </div>
            <%
            } else if (mensajeError != null) {
            %>
            <div style="color: red; padding: 10px; border: 1px solid red; background-color: #ffe6e6; margin-bottom: 20px; border-radius: 5px;">
                ❌ Error: <%= mensajeError%>
            </div>
            <%
                }
            %>
            <%-- FIN BLOQUE DE MENSAJES --%>

            <table class="proveedores-table" id="tabla-proveedores">
                <thead>
                    <tr>
                        <th>RUC</th> <th>Nombre</th>
                        <th>Tipo de Producto/Servicio</th> <th>Email</th>
                        <th>Teléfono</th>
                        <th>Dirección</th> <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        List<Proveedor> listaProveedores = (List<Proveedor>) request.getAttribute("listaProveedores");

                        if (listaProveedores != null && !listaProveedores.isEmpty()) {
                            for (Proveedor proveedor : listaProveedores) {
                    %>
                    <tr>
                        <td><%= proveedor.getRuc()%></td> <td><%= proveedor.getNombre()%></td>
                        <td><%= proveedor.getTipoProducto()%></td>
                        <td><%= proveedor.getEmail()%></td>
                        <td><%= proveedor.getTelefono()%></td>
                        <td><%= proveedor.getDireccion()%></td> <td>
                            <%-- ✏️ Botón Editar con Icono (Abre el Modal) --%>
                            <a href="#" class="edit-btn" 
                               data-ruc="<%= proveedor.getRuc()%>" data-nombre="<%= proveedor.getNombre()%>"
                               data-email="<%= proveedor.getEmail()%>"
                               data-telefono="<%= proveedor.getTelefono()%>"
                               data-tipo="<%= proveedor.getTipoProducto()%>"
                               data-direccion="<%= proveedor.getDireccion()%>"> <i class='bx bx-pencil action-icon' title="Editar proveedor <%= proveedor.getNombre()%>"></i>
                            </a>

                            <%-- 🗑️ Enlace Eliminar con Icono (Llama al Controller) --%>
                            <a href="<%= request.getContextPath()%>/Proveedores?accion=eliminar&ruc=<%= proveedor.getRuc()%>"
                               onclick="return confirm('¿Seguro que quieres eliminar a <%= proveedor.getNombre()%>? Esta acción es irreversible.');">
                                <i class='bx bx-trash action-icon' title="Eliminar proveedor"></i>
                            </a>
                        </td>
                    </tr>
                    <%
                        }
                    } else {
                    %>
                    <tr>
                        <td colspan="7" style="text-align: center; color: #777; padding: 15px;"> No hay proveedores registrados.
                        </td>
                    </tr>
                    <%
                        }
                    %>
                </tbody>
            </table>
        </main>

        <%-- 🪟 MODAL PARA AÑADIR/EDITAR PROVEEDOR --%>
        <div id="modalProveedor" class="modal">
            <div class="modal-content">
                <h3 id="modalTitle">Añadir Proveedor</h3>

                <%-- FORMULARIO: Action apunta al Controller --%>
                <form id="proveedorForm" action="<%= request.getContextPath()%>/Proveedores" method="POST">

                    <%-- Campo oculto para indicar si es una actualización --%>
                    <input type="hidden" id="accionUpdate" name="accion_update" value="">

                    <label>RUC</label>
                    <input type="number" id="rucProveedor" name="ruc" required> <label>Nombre</label>
                    <input type="text" id="nombreProveedor" name="nombre" required>

                    <label>Tipo de Producto o Servicio</label>
                    <input type="text" id="tipoProveedor" name="tipo_producto" required> <label>Email de Contacto</label>
                    <input type="email" id="emailProveedor" name="email" required>

                    <label>Teléfono de Contacto</label>
                    <input type="tel" id="telefonoProveedor" name="telefono" required>

                    <label>Dirección</label>
                    <input type="text" id="direccionProveedor" name="direccion" required> <div class="modal-buttons">
                        <button type="submit" class="save-btn" id="btnGuardar">Guardar</button>
                        <button type="button" class="cancel-btn" id="cancelModalBtn">Cancelar</button>
                    </div>
                </form>
            </div>
        </div>

        <%-- JAVASCRIPT: Lógica de Modales y Edición --%>
        <script>
            document.addEventListener('DOMContentLoaded', function () {
                const modal = document.getElementById('modalProveedor');
                const openModalBtn = document.getElementById('openModalBtn');
                const cancelModalBtn = document.getElementById('cancelModalBtn');
                const modalTitle = document.getElementById('modalTitle');
                const form = document.getElementById('proveedorForm');
                const btnGuardar = document.getElementById('btnGuardar');

                // Campos del formulario
                const inputRuc = document.getElementById('rucProveedor'); // CAMBIO
                const inputNombre = document.getElementById('nombreProveedor');
                const inputEmail = document.getElementById('emailProveedor');
                const inputTelefono = document.getElementById('telefonoProveedor');
                const inputTipo = document.getElementById('tipoProveedor');
                const inputDireccion = document.getElementById('direccionProveedor'); // NUEVO
                const inputAccionUpdate = document.getElementById('accionUpdate'); // NUEVO

                // 1. Abrir modal (AÑADIR)
                openModalBtn.addEventListener('click', function () {
                    modalTitle.textContent = 'Añadir Proveedor';
                    btnGuardar.textContent = 'Guardar';

                    inputRuc.readOnly = false; // RUC editable para insertar
                    inputAccionUpdate.value = ''; // Indica INSERT

                    form.reset();
                    modal.style.display = 'flex';
                });

                // 2. Abrir modal (EDITAR)
                document.querySelectorAll('.edit-btn').forEach(button => {
                    button.addEventListener('click', function () {
                        modalTitle.textContent = 'Editar Proveedor';
                        btnGuardar.textContent = 'Actualizar';

                        inputRuc.readOnly = true; // RUC no editable al actualizar
                        inputAccionUpdate.value = 'actualizar'; // Indica UPDATE

                        // Cargar datos al formulario usando data-attributes
                        inputRuc.value = this.getAttribute('data-ruc'); // CAMBIO: data-ruc
                        inputNombre.value = this.getAttribute('data-nombre');
                        inputEmail.value = this.getAttribute('data-email');
                        inputTelefono.value = this.getAttribute('data-telefono');
                        inputTipo.value = this.getAttribute('data-tipo');
                        inputDireccion.value = this.getAttribute('data-direccion'); // NUEVO

                        modal.style.display = 'flex';
                    });
                });

                // 3. Cerrar modal (Botón Cancelar)
                cancelModalBtn.addEventListener('click', function () {
                    modal.style.display = 'none';
                });

                // 4. Cerrar modal (Clic fuera)
                window.addEventListener('click', function (event) {
                    if (event.target === modal) {
                        modal.style.display = 'none';
                    }
                });
            });
        </script>

        
    </body>
</html>