<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="models.Empleado"%>
<%-- No necesitamos Cliente, pero mantengo la estructura de importaciones --%>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">

        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css" crossorigin="anonymous"/>
        <link rel="stylesheet" href="<%= request.getContextPath()%>/assets/css/Encabezado.css">
        <%-- Reutilizamos el mismo CSS de Gestión de Cliente, asumiendo un estilo similar --%>
        <link rel="stylesheet" href="<%= request.getContextPath()%>/assets/css/GestionCliente.css"> 
        <link rel="stylesheet" href="https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css">

        <title>Gestión de Personal</title>
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
                    <li><a href="<%= request.getContextPath()%>/Proveedores" >Proveedores</a></li>
                    <li><a href="<%= request.getContextPath()%>/GestionClientes">Clientes</a></li>
                    <li><a href="<%= request.getContextPath()%>/ServicioController" >Servicios</a></li>
                    <li><a href="<%= request.getContextPath()%>/GestionPersonal" class="active">Personal</a></li>
                    <li><a href="<%= request.getContextPath()%>/InventarioController">Inventario</a></li>
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

        <main class="content-area">
            <div style="padding-top: 100px;"></div>

            <%-- MENSAJES --%>
            <%
                String mensajeExito = request.getParameter("mensaje");
                String mensajeError = (String) request.getAttribute("error");

                if (request.getParameter("error") != null) {
                    mensajeError = request.getParameter("error").equals("dni_invalido")
                            ? "Error: El DNI del empleado no es válido."
                            : (request.getParameter("error").equals("bd_eliminar")
                            ? "Error de BD al eliminar el empleado." : null);
                }

                if (mensajeExito != null) {
                    String textoMensaje = "";
                    if (mensajeExito.equals("agregado_exitoso"))
                        textoMensaje = "Empleado agregado exitosamente.";
                    else if (mensajeExito.equals("editado_exitoso"))
                        textoMensaje = "Empleado editado exitosamente.";
                    else if (mensajeExito.equals("eliminado_exitoso"))
                        textoMensaje = "Empleado eliminado exitosamente.";
            %>
            <div style="color: green; padding: 10px; border: 1px solid green; background-color: #e6ffe6; margin-bottom: 20px; border-radius: 5px;">
                ✅ <%= textoMensaje%>
            </div>
            <% } else if (mensajeError != null) {%>
            <div style="color: red; padding: 10px; border: 1px solid red; background-color: #ffe6e6; margin-bottom: 20px; border-radius: 5px;">
                ❌ Error: <%= mensajeError%>
            </div>
            <% } %>

            <div class="header-table">
                <h1 class="page-title">Gestión de Personal</h1>
                <button class="btn-agregar-cliente" id="btn-agregar-personal" data-action="agregar">
                    <i class='bx bx-plus'></i> Agregar Empleado
                </button>
            </div>

            <div class="table-card">
                <div class="table-header" style="grid-template-columns: 1.5fr 1fr 1fr 1fr 0.5fr;">
                    <div class="col-nombre">NOMBRE COMPLETO</div>
                    <div class="col-puesto">PUESTO</div> 
                    <div class="col-telefono">TELÉFONO</div>
                    <div class="col-email">EMAIL</div>
                    <div class="col-acciones">ACTIONS</div>
                </div>

                <% List<Empleado> personal = (List<Empleado>) request.getAttribute("listaPersonal");

                    if (personal != null && !personal.isEmpty()) {
                        for (Empleado empleado : personal) {%>

                <div class="table-row" style="grid-template-columns: 1.5fr 1fr 1fr 1fr 0.5fr;">
                    <div class="col-nombre"><%= empleado.getNombre()%> <%= empleado.getApellido()%></div>
                    <div class="col-puesto"><%= empleado.getPuesto()%></div> 
                    <div class="col-telefono"><%= empleado.getTelefono()%></div>
                    <div class="col-email"><%= empleado.getEmail()%></div>

                    <div class="col-acciones">

                        <a href="#" class="edit-btn-personal"
                           data-dni="<%= empleado.getDni()%>"
                           data-nombre="<%= empleado.getNombre()%>"
                           data-apellido="<%= empleado.getApellido()%>"
                           data-telefono="<%= empleado.getTelefono()%>"
                           data-email="<%= empleado.getEmail()%>"
                           data-puesto="<%= empleado.getPuesto()%>">

                            <i class='bx bx-pencil action-icon' title="Editar Empleado <%= empleado.getDni()%>"></i> </a>

                        <a href="<%= request.getContextPath()%>/GestionPersonal?operacion=eliminar&dni=<%= empleado.getDni()%>" onclick="return confirm('¿Está seguro de eliminar a <%= empleado.getNombre()%> <%= empleado.getApellido()%>? Esta acción es irreversible.');">

                            <i class='bx bx-trash action-icon' title="Eliminar Empleado <%= empleado.getDni()%>"></i> </a>
                    </div>
                </div>

                <% }
                } else { %>

                <div class="table-row" style="grid-template-columns: 1fr;">
                    <div class="col-cliente" style="text-align: center; padding: 20px; color: #777;">
                        No hay personal registrado o hubo un error.
                    </div>
                </div>

                <% }%>
            </div>

            <%-- Quitamos el botón de proforma rápida ya que no aplica al módulo de Personal --%>
            <div style="height: 50px;"></div>
        </main>

        <%-- MODAL DE AGREGAR/EDITAR PERSONAL --%>
        <div id="modal-agregar-personal" class="modal">
            <div class="modal-content">
                <h2 class="modal-title" id="modal-title-action">Agregar Empleado</h2>

                <form action="<%= request.getContextPath()%>/GestionPersonal" method="POST" id="form-personal-action" class="modal-form">

                    <%-- Campo oculto para el DNI EXISTENTE (se usa para UPDATE) --%>
                    <input type="hidden" id="dni-existente" name="dni_existente" value="0">

                    <%-- Campo visible para el DNI NUEVO (se usa para INSERT) --%>
                    <input type="text" id="dni-nuevo" name="dni_nuevo" placeholder="Documento (DNI)" required>

                    <input type="text" id="nombre" name="nombre" placeholder="Nombre" required>
                    <input type="text" id="apellido" name="apellido" placeholder="Apellido">
                    <input type="text" id="puesto" name="puesto" placeholder="Puesto" required>
                    <input type="tel" id="telefono" name="telefono" placeholder="Teléfono">
                    <input type="email" id="email" name="email" placeholder="Email" required>

                    <button type="submit" id="btn-submit-action" class="btn-agregar">Agregar</button>
                </form>

                <span class="close-btn">&times;</span>
            </div>
        </div>


        <script>
            // LÓGICA PARA EL MODAL DE AGREGAR/EDITAR EMPLEADO
            document.addEventListener('DOMContentLoaded', () => {

                const modalPersonal = document.getElementById('modal-agregar-personal');
                const formPersonal = document.getElementById('form-personal-action');
                const tituloModalPersonal = document.getElementById('modal-title-action');
                const btnSubmitPersonal = document.getElementById('btn-submit-action');

                // Campos del modal
                const inputDniExistente = document.getElementById('dni-existente'); // DNI para UPDATE
                const inputDniNuevo = document.getElementById('dni-nuevo'); // DNI para INSERT
                const inputNombre = document.getElementById('nombre');
                const inputApellido = document.getElementById('apellido');
                const inputPuesto = document.getElementById('puesto');
                const inputTelefono = document.getElementById('telefono');
                const inputEmail = document.getElementById('email');

                // --- ABRIR MODAL PARA AGREGAR ---
                document.getElementById('btn-agregar-personal').addEventListener('click', () => {
                    formPersonal.reset();

                    // Configuración para INSERT
                    inputDniExistente.value = '0'; // Valor que indica INSERT
                    inputDniNuevo.style.display = 'block'; // Mostrar DNI para el nuevo registro
                    inputDniNuevo.setAttribute('required', 'required'); // Hacer obligatorio el DNI nuevo

                    tituloModalPersonal.textContent = 'Agregar Empleado';
                    btnSubmitPersonal.textContent = 'Agregar';

                    modalPersonal.style.display = 'flex';
                });

                // --- ABRIR MODAL PARA EDITAR ---
                document.querySelectorAll('.edit-btn-personal').forEach(button => {
                    button.addEventListener('click', e => {
                        e.preventDefault();

                        // 1. Cargar datos desde los data-atributos
                        inputDniExistente.value = button.getAttribute('data-dni'); // DNI se carga aquí (se usará para WHERE)
                        inputNombre.value = button.getAttribute('data-nombre');
                        inputApellido.value = button.getAttribute('data-apellido');
                        inputPuesto.value = button.getAttribute('data-puesto');
                        inputTelefono.value = button.getAttribute('data-telefono');
                        inputEmail.value = button.getAttribute('data-email');

                        // 2. Ocultar y deshabilitar el campo de DNI para que no se pueda cambiar en la edición
                        inputDniNuevo.style.display = 'none';
                        inputDniNuevo.removeAttribute('required');
                        inputDniNuevo.value = ''; // campo dummy vacío

                        // 3. Ajustar el título y el botón
                        tituloModalPersonal.textContent = 'Editar Empleado';
                        btnSubmitPersonal.textContent = 'Guardar Cambios';

                        modalPersonal.style.display = 'flex';
                    });
                });

                // --- Lógica de cierre de modal ---

                // Cierre del modal Personal
                modalPersonal.querySelector('.close-btn').addEventListener('click', () => {
                    modalPersonal.style.display = 'none';
                });

                window.addEventListener('click', event => {
                    if (event.target === modalPersonal)
                        modalPersonal.style.display = 'none';
                });

                // Lógica para el menú móvil (copiada de tu JSP original)
                const menuIcon = document.getElementById('menu-icon');
                const navbar = document.getElementById('navbar');
                if (menuIcon) {
                    menuIcon.onclick = () => {
                        menuIcon.classList.toggle('bx-x');
                        navbar.classList.toggle('open');
                    };
                }
            });
        </script>

    </body>
</html>