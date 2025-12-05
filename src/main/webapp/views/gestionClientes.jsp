<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="models.Cliente"%>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">

        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css" crossorigin="anonymous"/>
        <link rel="stylesheet" href="<%= request.getContextPath()%>/assets/css/Encabezado.css">
        <link rel="stylesheet" href="<%= request.getContextPath()%>/assets/css/GestionCliente.css">
        <link rel="stylesheet" href="https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css">

        <title>Gestión de Clientes</title>
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
                    <li class="active"><a href="<%= request.getContextPath()%>/Proveedores" >Proveedores</a></li>
                    <li><a href="<%= request.getContextPath()%>/GestionClientes" class="active">Clientes</a></li>
                    <li><a href="<%= request.getContextPath()%>/ServicioController" >Servicios</a></li>
                    <li><a href="<%= request.getContextPath()%>/GestionPersonal" >Personal</a></li>
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

                // El controlador ahora maneja 'docu' pero el mensaje sigue usando 'dni' para mantener consistencia visual si es necesario
                if (request.getParameter("error") != null) {
                    mensajeError = request.getParameter("error").equals("dni_invalido")
                            ? "Error: El Documento del cliente no es válido."
                            : (request.getParameter("error").equals("bd_eliminar")
                            ? "Error de BD al eliminar el cliente." : null);
                }

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

            <div class="header-table">
                <h1 class="page-title">Clientes y Cotizaciones</h1>
                <button class="btn-agregar-cliente" id="btn-agregar-cliente" data-action="agregar">
                    <i class='bx bx-plus'></i> Agregar Cliente
                </button>
            </div>

            <div class="table-card">
                <div class="table-header">
                    <div class="col-cliente">NOMBRE DEL CLIENTE</div>
                    <div class="col-empresa">DIRECCIÓN</div> <div class="col-telefono">TELÉFONO</div>
                    <div class="col-email">EMAIL</div>
                    <div class="col-acciones">ACTIONS</div>
                </div>

                <% List<Cliente> clientes = (List<Cliente>) request.getAttribute("listaClientes");

                    if (clientes != null && !clientes.isEmpty()) {
                        for (Cliente cliente : clientes) {%>

                <div class="table-row">
                    <div class="col-cliente"><%= cliente.getNombre()%></div>
                    <div class="col-empresa"><%= cliente.getDireccion()%></div> <div class="col-telefono"><%= cliente.getTelefono()%></div>
                    <div class="col-email"><%= cliente.getEmail()%></div>

                    <div class="col-acciones">

                        <a href="#" class="edit-btn"
                           data-docu="<%= cliente.getDocu()%>"              data-nombre="<%= cliente.getNombre()%>"
                           data-direccion="<%= cliente.getDireccion()%>"    data-telefono="<%= cliente.getTelefono()%>"
                           data-email="<%= cliente.getEmail()%>">

                            <i class='bx bx-pencil action-icon' title="Editar Documento <%= cliente.getDocu()%>"></i> </a>

                        <a href="<%= request.getContextPath()%>/GestionClientes?operacion=eliminar&dni=<%= cliente.getDocu()%>" onclick="return confirm('¿Está seguro de eliminar a <%= cliente.getNombre()%>? Esta acción es irreversible.');">

                            <i class='bx bx-trash action-icon' title="Eliminar Documento <%= cliente.getDocu()%>"></i> </a>
                    </div>
                </div>

                <% }
                } else { %>

                <div class="table-row" style="grid-template-columns: 1fr;">
                    <div class="col-cliente" style="text-align: center; padding: 20px; color: #777;">
                        No hay clientes registrados o hubo un error.
                    </div>
                </div>

                <% }%>
            </div>

            <button class="btn-proforma-rapida" id="btn-proforma-rapida">
                <i class='bx bxs-file-doc'></i> Proforma Rápida
            </button>
        </main>

        <div id="modal-agregar-cliente" class="modal">
            <div class="modal-content">
                <h2 class="modal-title" id="modal-title-action">Agregar cliente</h2>

                <form action="<%= request.getContextPath()%>/GestionClientes" method="POST" id="form-cliente-action" class="modal-form">

                    <input type="hidden" id="cliente-id" name="docu_cliente" value="0"> <input type="text" id="docu-insert" name="docu" placeholder="Documento (DNI/RUC)" required> <input type="text" id="nombre" name="nombre" placeholder="Nombre" required>
                    <input type="text" id="direccion" name="direccion" placeholder="Dirección" required> <input type="tel" id="telefono" name="telefono" placeholder="Teléfono" required>
                    <input type="email" id="email" name="email" placeholder="Email" required>

                    <button type="submit" id="btn-submit-action" class="btn-agregar">Agregar</button>
                </form>

                <span class="close-btn">&times;</span>
            </div>
        </div>

        <div id="modal-proforma-rapida" class="modal">
            <div class="modal-content large-modal">
                <h2 class="modal-title">Proforma rápida</h2>

                <form action="<%= request.getContextPath()%>/GestionProformas" method="POST" class="proforma-form" id="proforma-form-id">

                    <input type="hidden" name="id_proforma" value="0">

                    <div class="form-columns">

                        <div class="column-left">
                            <h3 class="column-title">Datos del cliente</h3>

                            <select id="docu_cliente_select" name="docu_cliente" required> <option value="">-- Selecciona un cliente --</option>
                                <%
                                    // Aquí se sigue usando getDocu() para el valor
                                    List<Cliente> clientesSelect = (List<Cliente>) request.getAttribute("listaClientes");
                                    if (clientesSelect != null) {
                                        for (Cliente c : clientesSelect) {
                                %>
                                <option value="<%= c.getDocu()%>"><%= c.getNombre()%></option> <%
                                        }
                                    }
                                %>
                            </select>

                            <input type="text" id="cliente_nombre" disabled placeholder="Nombres">
                            <input type="text" id="cliente_direccion" disabled placeholder="Dirección"> <input type="tel" id="cliente_telefono" disabled placeholder="Teléfono">
                            <input type="email" id="cliente_email" disabled placeholder="Email">
                        </div>

                        <div class="column-right">
                            <h3 class="column-title">Detalles de cotización</h3>

                            <input type="text" name="descripcion_servicio" placeholder="Producto/Servicio" required>
                            <label for="fecha_emision">Fecha de Emisión *</label>
                            <input type="date" id="fecha_emision" name="fecha_emision" required>
                            <input type="number" step="0.01" name="presupuesto" placeholder="Presupuesto" required>
                            <textarea name="descripcion_adicional" placeholder="Descripción Adicional"></textarea>
                        </div>
                    </div>

                    <button type="submit" class="btn-generar">Generar</button>
                </form>

                <span class="close-btn proforma-close">&times;</span>
            </div>
        </div>


        <script>
            // LÓGICA PARA CARGAR DATOS DEL CLIENTE EN EL MODAL PROFORMA (Vía AJAX)
            document.addEventListener('DOMContentLoaded', () => {
                const selectCliente = document.getElementById('docu_cliente_select'); // CAMBIADO
                const inputNombre = document.getElementById('cliente_nombre');
                const inputDireccion = document.getElementById('cliente_direccion'); // CAMBIADO
                const inputTelefono = document.getElementById('cliente_telefono');
                const inputEmail = document.getElementById('cliente_email');
                const contextPath = '<%= request.getContextPath()%>';

                selectCliente.addEventListener('change', () => {
                    const docu = selectCliente.value; // CAMBIADO

                    if (!docu) {
                        inputNombre.value = '';
                        inputDireccion.value = ''; // CAMBIADO
                        inputTelefono.value = '';
                        inputEmail.value = '';
                        return;
                    }

                    // Se llama al ClienteDataServlet, pasando 'docu' como parámetro
                    // Nota: Asegúrate de que tu ClienteDataServlet use 'docu=' como parámetro en la URL
                    fetch(contextPath + '/ClienteData?docu=' + docu) // CAMBIADO: dni a docu
                            .then(response => {
                                if (!response.ok)
                                    throw new Error('Cliente no encontrado');
                                return response.json();
                            })
                            .then(data => {
                                inputNombre.value = data.nombre;
                                // Nota: Asumimos que ClienteDataServlet devuelve JSON con clave 'direccion'
                                inputDireccion.value = data.direccion; // CAMBIADO: data.empresa a data.direccion
                                inputTelefono.value = data.telefono;
                                inputEmail.value = data.email;
                            })
                            .catch(err => {
                                alert('Error: ' + err.message);
                                inputNombre.value = '';
                                inputDireccion.value = ''; // CAMBIADO
                                inputTelefono.value = '';
                                inputEmail.value = '';
                            });
                });
            });
        </script>

        <script>
            // LÓGICA PARA EL MODAL DE AGREGAR/EDITAR CLIENTE
            document.addEventListener('DOMContentLoaded', () => {

                const modalCliente = document.getElementById('modal-agregar-cliente');
                const formCliente = document.getElementById('form-cliente-action');
                const tituloModalCliente = document.getElementById('modal-title-action');
                const btnSubmitCliente = document.getElementById('btn-submit-action');

                // Campos del modal
                const inputId = document.getElementById('cliente-id'); // docu para UPDATE
                const inputDocuInsert = document.getElementById('docu-insert'); // docu para INSERT
                const inputNombre = document.getElementById('nombre');
                const inputDireccion = document.getElementById('direccion'); // CAMBIADO
                const inputTelefono = document.getElementById('telefono');
                const inputEmail = document.getElementById('email');

                // --- ABRIR MODAL PARA AGREGAR ---
                document.getElementById('btn-agregar-cliente').addEventListener('click', () => {
                    inputId.value = '0';
                    tituloModalCliente.textContent = 'Agregar cliente';
                    btnSubmitCliente.textContent = 'Agregar';
                    inputDocuInsert.style.display = 'block'; // Mostrar el campo DOCU para agregar
                    formCliente.reset();
                    modalCliente.style.display = 'flex';
                });

                // --- ABRIR MODAL PARA EDITAR ---
                document.querySelectorAll('.edit-btn').forEach(button => {
                    button.addEventListener('click', e => {
                        e.preventDefault();

                        // 1. Cargar datos desde los data-atributos
                        inputId.value = button.getAttribute('data-docu'); // CAMBIADO: data-dni a data-docu
                        inputNombre.value = button.getAttribute('data-nombre');
                        inputDireccion.value = button.getAttribute('data-direccion'); // CAMBIADO: data-empresa a data-direccion
                        inputTelefono.value = button.getAttribute('data-telefono');
                        inputEmail.value = button.getAttribute('data-email');

                        // 2. Ocultar el campo de DOCU para que no se pueda cambiar en la edición
                        // El DOCU original se envía en el campo oculto 'cliente-id'
                        inputDocuInsert.style.display = 'none';
                        inputDocuInsert.removeAttribute('required'); // evita validación
                        inputDocuInsert.value = ''; // campo dummy vacío

                        // 3. Ajustar el título y el botón
                        tituloModalCliente.textContent = 'Editar cliente';
                        btnSubmitCliente.textContent = 'Guardar Cambios';

                        modalCliente.style.display = 'flex';
                    });
                });

                // --- Lógica de cierre de modales ---

                // Cierre del modal Cliente
                modalCliente.querySelector('.close-btn').addEventListener('click', () => {
                    modalCliente.style.display = 'none';
                });

                window.addEventListener('click', event => {
                    if (event.target === modalCliente)
                        modalCliente.style.display = 'none';
                });


                // Cierre del modal Proforma
                const btnProforma = document.getElementById('btn-proforma-rapida');
                const modalProforma = document.getElementById('modal-proforma-rapida');
                const closeProforma = modalProforma.querySelector('.proforma-close');

                btnProforma.addEventListener('click', () => {
                    modalProforma.style.display = 'flex';
                });

                closeProforma.addEventListener('click', () => {
                    modalProforma.style.display = 'none';
                });

                window.addEventListener('click', event => {
                    if (event.target === modalProforma)
                        modalProforma.style.display = 'none';
                });
            });
        </script>

    </body>
</html>