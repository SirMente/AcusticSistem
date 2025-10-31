<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="models.Cliente"%>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">

        <%-- Font Awesome (Versión más estable) --%>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css" integrity="sha512-DTOQO9RWCH3ppGqcWaEA1BIZOC6xxalwEsw9c2QQeAIftl+Vegovlnee1c9QX4TctnWMn13TZye+giMm8e2LwA==" crossorigin="anonymous" referrerpolicy="no-referrer" />

        <%-- CSS: Usando getContextPath() para ruta absoluta --%>
        <link rel="stylesheet" href="<%= request.getContextPath()%>/assets/css/Encabezado.css">
        <link rel="stylesheet" href="<%= request.getContextPath()%>/assets/css/GestionCliente.css">

        <%-- Boxicons --%>
        <link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css' rel='stylesheet'>

        <title>Gestión de Clientes</title>
    </head>
    <body>
        <header>
            <div class="brand">
                <img src="<%= request.getContextPath()%>/Imagenes/01.jpg" alt="Logo">
                <span>Acustica</span>
            </div>
            <nav>
                <ul class="navbar" id="navbar">
                    <li><a href="<%= request.getContextPath()%>/dashboard">Inicio</a></li>
                    <li><a href="<%= request.getContextPath()%>/Inventario">Inventario</a></li>
                    <li><a href="<%= request.getContextPath()%>/Finanzas" class="active">Finanzas</a></li>
                    <li><a href="<%= request.getContextPath()%>/Proyectos">Proyectos</a></li>
                    <li><a href="<%= request.getContextPath()%>/ProformaController">Proformas</a></li>
                    <li><a href="<%= request.getContextPath()%>/Proveedores">Proveedores</a></li>
                </ul>
            </nav>
            <div class="main">
                <i class='bx bx-search'></i>
                <input type="search" placeholder="Buscar">
                <i class='bx bxs-notification'></i>
                <img src="<%= request.getContextPath()%>/Imagenes/03.jpg" alt="Usuario">
                <i class='bx bx-menu' id="menu-icon"></i>
            </div>
        </header>

        <main class="content-area">
            <div style="padding-top: 100px;"></div> 

            <%-- BLOQUE DE MENSAJES DE ÉXITO Y ERROR --%>
            <%
                String mensajeExito = request.getParameter("mensaje");
                String mensajeError = (String) request.getAttribute("error");

                if (request.getParameter("error") != null) {
                    mensajeError = request.getParameter("error").equals("id_invalido")
                            ? "Error: El ID del cliente no es válido."
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

            <div class="header-table">
                <h1 class="page-title">Clientes y Cotizaciones</h1>
                <button class="btn-agregar-cliente" id="btn-agregar-cliente" data-action="agregar">
                    <i class='bx bx-plus'></i> Agregar Cliente
                </button>
            </div>

            <div class="table-card">
                <div class="table-header">
                    <div class="col-cliente">NOMBRE DEL CLIENTE</div>
                    <div class="col-empresa">EMPRESA</div>
                    <div class="col-telefono">TELÉFONO</div>
                    <div class="col-email">EMAIL</div>
                    <div class="col-acciones"></div>
                </div>

                <%-- BLOQUE DINÁMICO: LLAMA DATOS DE LA BD --%>
                <%
                    List<Cliente> clientes = (List<Cliente>) request.getAttribute("listaClientes");

                    if (clientes != null && !clientes.isEmpty()) {
                        for (Cliente cliente : clientes) {
                %>
                <div class="table-row">
                    <div class="col-cliente"><%= cliente.getNombre()%></div>
                    <div class="col-empresa"><%= cliente.getEmpresa()%></div>
                    <div class="col-telefono"><%= cliente.getTelefono()%></div>
                    <div class="col-email"><%= cliente.getEmail()%></div>
                    <div class="col-acciones">

                        <%-- ENLACE DE EDICIÓN (Usando data-attributes para el JS) --%>
                        <a href="#" class="edit-btn" 
                           data-id="<%= cliente.getId()%>"
                           data-nombre="<%= cliente.getNombre()%>"
                           data-empresa="<%= cliente.getEmpresa()%>"
                           data-telefono="<%= cliente.getTelefono()%>"
                           data-email="<%= cliente.getEmail()%>">
                            <i class='bx bx-pencil action-icon' title="Editar ID <%= cliente.getId()%>"></i>
                        </a>

                        <%-- ENLACE DE ELIMINAR --%>
                        <a href="<%= request.getContextPath()%>/GestionClientes?operacion=eliminar&id=<%= cliente.getId()%>" 
                           onclick="return confirm('¿Está seguro de eliminar a <%= cliente.getNombre()%>? Esta acción es irreversible.');">
                            <i class='bx bx-trash action-icon' title="Eliminar ID <%= cliente.getId()%>"></i>
                        </a>
                    </div>
                </div>
                <%
                    } // Fin del for
                } else {
                %>
                <div class="table-row" style="grid-template-columns: 1fr;">
                    <div class="col-cliente" style="text-align: center; padding: 20px; color: #777;">
                        No hay clientes registrados en la base de datos o hubo un error al cargar.
                    </div>
                </div>
                <%
                    }
                %>
                <%-- FIN DEL BLOQUE DINÁMICO --%>

            </div>

            <button class="btn-proforma-rapida" id="btn-proforma-rapida">
                <i class='bx bxs-file-doc'></i> Proforma Rápida
            </button>
        </main>

        <%-- MODAL ÚNICO PARA AGREGAR Y EDITAR CLIENTE --%>
        <div id="modal-agregar-cliente" class="modal">
            <div class="modal-content">
                <h2 class="modal-title" id="modal-title-action">Agregar cliente</h2>

                <form action="<%= request.getContextPath()%>/GestionClientes" method="POST" id="form-cliente-action" class="modal-form"> 

                    <input type="hidden" id="cliente-id" name="id_cliente" value="0"> 

                    <input type="text" id="nombre" name="nombre" placeholder="Nombre" required>
                    <input type="text" id="empresa" name="empresa" placeholder="Empresa" required>
                    <input type="tel" id="telefono" name="telefono" placeholder="Teléfono" required>
                    <input type="email" id="email" name="email" placeholder="Email" required>
                    <button type="submit" id="btn-submit-action" class="btn-agregar">Agregar</button>
                </form>
                <span class="close-btn">&times;</span> 
            </div>
        </div>

        <%-- MODAL ÚNICO PARA profrapidaE --%>
        <div id="modal-proforma-rapida" class="modal">
            <div class="modal-content large-modal">
                <h2 class="modal-title">Proforma rápida</h2>

                <form action="<%= request.getContextPath()%>/GestionProformas" method="POST" class="proforma-form" id="proforma-form-id">

                    <input type="hidden" name="id_proforma" value="0">

                    <div class="form-columns">
                        <div class="column-left">
                            <h3 class="column-title">Datos del cliente</h3>

                            <input type="number" id="id_cliente_input" name="id_cliente" placeholder="ID Cliente (Buscar)" required onchange="cargarDatosCliente()">

                            <input type="text" id="cliente_nombre" placeholder="Nombres" disabled>
                            <input type="text" id="cliente_empresa" placeholder="Empresa/RazónSocial" disabled>
                            <input type="tel" id="cliente_telefono" placeholder="Teléfono" disabled>
                            <input type="email" id="cliente_email" placeholder="Email" disabled>
                        </div>

                        <div class="column-right">
                            <h3 class="column-title">Detalles de cotización</h3>

                            <input type="text" name="descripcion_servicio" placeholder="Producto/Servicio" required>

                            <label for="fecha_emision" style="display: block; margin-top: 10px;">Fecha de Emisión *</label>
                            <input type="date" id="fecha_emision" name="fecha_emision" required>

                            <input type="number" step="0.01" name="presupuesto" placeholder="Presupuesto" required>

                            <textarea placeholder="Descripción Adicional"></textarea>
                        </div>
                    </div>

                    <button type="submit" class="btn-generar">Generar</button>
                </form>
                <span class="close-btn proforma-close">&times;</span> 
            </div>
        </div>

        <script>
            function cargarDatosCliente() {
                const idCliente = document.getElementById('id_cliente_input').value;
                const contextPath = '<%= request.getContextPath()%>';

                // ... (limpieza de campos) ...

                if (!idCliente) {
                    return;
                }

                // 🔑 Verifica: Esta URL debe ser alcanzable por el navegador
                fetch(contextPath + '/ClienteData?id=' + idCliente)
                        .then(response => {
                            if (response.status === 404) {
                                throw new Error('Cliente con ID ' + idCliente + ' no existe.');
                            }
                            if (!response.ok) {
                                // Manejar otros errores 400, 500 que devuelve el Servlet
                                return response.json().then(data => {
                                    throw new Error(data.error || 'Error desconocido.');
                                });
                            }
                            return response.json();
                        })
                        .then(data => {
                            // 🔑 Verifica: Estos IDs deben coincidir con los IDs en tu JSP
                            if (data && data.id) {
                                document.getElementById('cliente_nombre').value = data.nombre || '';
                                document.getElementById('cliente_empresa').value = data.empresa || '';
                                document.getElementById('cliente_telefono').value = data.telefono || '';
                                document.getElementById('cliente_email').value = data.email || '';
                            } else {
                                // Esto maneja el caso donde el JSON es vacío o no tiene 'id'
                                alert("Cliente no encontrado.");
                            }
                        })
                        .catch(error => {
                            alert("Búsqueda fallida: " + error.message);
                            console.error('Error al cargar cliente:', error);
                        });
            }
        </script>

        <%-- JAVASCRIPT --%>
        <script src="<%= request.getContextPath()%>/JavaScript/Script3.js"></script>

        <script>
            document.addEventListener('DOMContentLoaded', function () {
                // --- ELEMENTOS MODAL CLIENTE ---
                const modalCliente = document.getElementById('modal-agregar-cliente');
                const formCliente = document.getElementById('form-cliente-action');
                const tituloModalCliente = document.getElementById('modal-title-action');
                const btnSubmitCliente = document.getElementById('btn-submit-action');
                const inputId = document.getElementById('cliente-id');
                const inputNombre = document.getElementById('nombre');
                const inputEmpresa = document.getElementById('empresa');
                const inputTelefono = document.getElementById('telefono');
                const inputEmail = document.getElementById('email');

                // 1. Lógica para botón AGREGAR (Nuevo Cliente)
                document.getElementById('btn-agregar-cliente').addEventListener('click', function () {
                    inputId.value = '0';
                    tituloModalCliente.textContent = 'Agregar cliente';
                    btnSubmitCliente.textContent = 'Agregar';
                    formCliente.reset();
                    modalCliente.style.display = 'flex';
                });

                // 2. Lógica para botones EDITAR (Pre-llenar el formulario)
                document.querySelectorAll('.edit-btn').forEach(button => {
                    button.addEventListener('click', function (e) {
                        e.preventDefault();

                        inputId.value = this.getAttribute('data-id');
                        inputNombre.value = this.getAttribute('data-nombre');
                        inputEmpresa.value = this.getAttribute('data-empresa');
                        inputTelefono.value = this.getAttribute('data-telefono');
                        inputEmail.value = this.getAttribute('data-email');

                        tituloModalCliente.textContent = 'Editar Cliente';
                        btnSubmitCliente.textContent = 'Guardar Cambios';

                        modalCliente.style.display = 'flex';
                    });
                });

                // Lógica para cerrar el modal de Cliente (botón 'x')
                if (modalCliente) {
                    modalCliente.querySelector('.close-btn').addEventListener('click', function () {
                        modalCliente.style.display = 'none';
                    });
                }

                // Lógica para cerrar el modal de Cliente (clic fuera)
                window.addEventListener('click', function (event) {
                    if (event.target == modalCliente) {
                        modalCliente.style.display = 'none';
                    }
                });

                // ---------------------------------------------
                // 3. Lógica para el botón PROFORMA RÁPIDA 🚀
                // ---------------------------------------------
                const btnProforma = document.getElementById('btn-proforma-rapida');
                const modalProforma = document.getElementById('modal-proforma-rapida');
                const closeProforma = modalProforma ? modalProforma.querySelector('.proforma-close') : null;

                if (btnProforma && modalProforma) {
                    // Abrir el modal al hacer clic en el botón
                    btnProforma.addEventListener('click', function () {
                        modalProforma.style.display = 'flex'; // Usamos 'flex' para centrar
                    });

                    // Cerrar el modal con el botón 'x'
                    if (closeProforma) {
                        closeProforma.addEventListener('click', function () {
                            modalProforma.style.display = 'none';
                        });
                    }

                    // Cerrar el modal al hacer clic fuera
                    window.addEventListener('click', function (event) {
                        if (event.target == modalProforma) {
                            modalProforma.style.display = 'none';
                        }
                    });
                }

            });
        </script>
    </body>
</html>