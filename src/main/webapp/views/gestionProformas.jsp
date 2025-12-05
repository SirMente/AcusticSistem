<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="models.Cliente"%>
<%@page import="models.Producto"%>
<%@page import="models.Servicio"%>
<%@page import="models.Proforma"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Gestión de Proformas - Acustica</title>

        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css" />
        <link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css' rel='stylesheet'>

        <link rel="stylesheet" href="<%= request.getContextPath()%>/assets/css/Encabezado.css">
        <%-- Aquí se incluye el CSS de la tabla y estado --%>
        <link rel="stylesheet" href="<%= request.getContextPath()%>/assets/css/Proforma.css">
        <style>
            
            /* Estilos básicos de ejemplo para el modal */
    .modal {
        display: none; 
        position: fixed; 
        z-index: 1000; 
        left: 0;
        top: 0;
        width: 100%; 
        height: 100%; 
        overflow: auto; 
        background-color: rgba(0,0,0,0.4); 
    }
    .modal-content {
        background-color: #fefefe;
        margin: 10% auto; 
        padding: 20px;
        border: 1px solid #888;
        width: 80%;
        max-width: 400px;
        border-radius: 8px;
        box-shadow: 0 4px 8px rgba(0,0,0,0.2);
    }
    .modal-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        border-bottom: 1px solid #eee;
        padding-bottom: 10px;
        margin-bottom: 15px;
    }
    .modal-header h2 {
        margin: 0;
        font-size: 1.25rem;
    }
    .close-estado {
        color: #aaa;
        font-size: 28px;
        font-weight: bold;
        cursor: pointer;
    }
    .close-estado:hover,
    .close-estado:focus {
        color: #000;
        text-decoration: none;
    }
    .modal-section {
        margin-bottom: 20px;
    }
    .modal-footer {
        display: flex;
        justify-content: flex-end;
        gap: 10px;
        padding-top: 15px;
        border-top: 1px solid #eee;
    }
    .btn-guardar, .btn-cancelar {
        padding: 8px 15px;
        border: none;
        border-radius: 4px;
        cursor: pointer;
        font-weight: bold;
    }
    .btn-guardar {
        background-color: #4CAF50;
        color: white;
    }
    .btn-cancelar {
        background-color: #f44336;
        color: white;
    }
            /*
             * ⚠️ ATENCIÓN: Por simplicidad y para que funcione con el cambio,
             * el CSS mejorado se ha insertado aquí. Deberías mover este CSS
             * a tu archivo Proforma.css.
            */

            /* --- Estilos Mejorados para la Tabla --- */
            .table-container {
                box-shadow: 0 4px 8px rgba(0, 0, 0, 0.05);
                border-radius: 10px;
                overflow-x: auto; /* Para tablas que exceden el ancho */
                background-color: #fff;
                margin-top: 20px;
            }

            .proformas-table {
                width: 100%;
                border-collapse: collapse;
                table-layout: fixed; /* Ayuda a controlar el ancho de las columnas */
            }

            .proformas-table thead tr {
                background-color: #007bff; /* Color de encabezado primario */
                color: white;
            }

            .proformas-table th {
                padding: 15px 12px;
                text-align: left;
                font-weight: 600;
                font-size: 14px;
                letter-spacing: 0.5px;
                border-bottom: 2px solid #0056b3;
            }

            .proformas-table td {
                padding: 12px;
                border-bottom: 1px solid #eee;
                font-size: 14px;
                color: #333;
            }

            .proformas-table tbody tr:nth-child(even) {
                background-color: #f9f9f9; /* Zebra striping */
            }

            .proformas-table tbody tr:hover {
                background-color: #f1f1f1;
                transition: background-color 0.3s;
            }

            .proformas-table td:last-child {
                text-align: center;
            }

            /* Estilos para los botones de acción */
            .action-buttons {
                display: flex;
                gap: 5px;
                justify-content: center;
            }

            .action-buttons button {
                padding: 8px 12px;
                border: none;
                border-radius: 5px;
                cursor: pointer;
                transition: background-color 0.2s, transform 0.1s;
                font-size: 14px;
                display: flex;
                align-items: center;
                gap: 5px;
            }

            .btn-ver {
                background-color: #28a745; /* Verde para Ver */
                color: white;
            }

            .btn-ver:hover {
                background-color: #1e7e34;
                transform: translateY(-1px);
            }

            .btn-estado {
                background-color: #ffc107; /* Amarillo para Cambiar Estado */
                color: #333;
            }

            .btn-estado:hover {
                background-color: #e0a800;
                transform: translateY(-1px);
            }

            /* Estilo para el botón de descarga PDF */
            .btn-pdf-download {
                background-color: #dc3545;      /* Rojo "danger" */
                color: #fff;
                padding: 8px 12px;
                border: none;
                border-radius: 5px;
                cursor: pointer;
                transition: background-color 0.2s, transform 0.1s;
                font-size: 14px;
                display: flex;
                align-items: center;
                gap: 5px;
                text-decoration: none;          /* Importante si se usa en <a> */
            }

            .btn-pdf-download:hover {
                background-color: #c82333;      /* Rojo más oscuro */
                transform: translateY(-1px);
            }


            /* Estilos para los tags de estado */
            .status-tag {
                display: inline-block;
                padding: 5px 10px;
                border-radius: 20px;
                font-size: 12px;
                font-weight: 700;
                text-transform: uppercase;
                text-align: center;
                min-width: 90px;
            }

            .status-pendiente {
                background-color: #ffe0b2;
                color: #fb8c00; /* Naranja oscuro */
            }

            .status-pagada {
                background-color: #c8e6c9;
                color: #388e3c; /* Verde oscuro */
            }

            .status-parcial {
                background-color: #bbdefb;
                color: #1976d2; /* Azul oscuro */
            }

            .status-cancelada {
                background-color: #ffcdd2;
                color: #d32f2f; /* Rojo oscuro */
            }


            /* --- Estilos Mejorados para Modal de Estado --- */
            .small-modal {
                max-width: 400px !important; /* Más pequeño para edición de estado */
            }

            #modal-estado-proforma .modal-section {
                padding: 20px;
                border-bottom: 1px solid #eee;
            }

            #modal-estado-proforma .modal-section p {
                margin-bottom: 15px;
                font-size: 15px;
            }

            #modal-estado-proforma select {
                width: 100%;
                padding: 10px;
                border: 1px solid #ccc;
                border-radius: 5px;
                margin-top: 5px;
                box-sizing: border-box;
                font-size: 14px;
            }

            #modal-estado-proforma .modal-footer {
                display: flex;
                justify-content: flex-end;
                gap: 10px;
                padding: 15px 20px;
            }

            #modal-estado-proforma .btn-cancelar {
                background-color: #6c757d;
                color: white;
                padding: 10px 15px;
                border: none;
                border-radius: 5px;
                cursor: pointer;
                transition: background-color 0.2s;
            }
            #modal-estado-proforma .btn-guardar {
                background-color: #007bff;
                color: white;
                padding: 10px 15px;
                border: none;
                border-radius: 5px;
                cursor: pointer;
                transition: background-color 0.2s;
            }

            #modal-estado-proforma .btn-cancelar:hover {
                background-color: #5a6268;
            }
            #modal-estado-proforma .btn-guardar:hover {
                background-color: #0056b3;
            }
        </style>
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
                    <li><a href="<%= request.getContextPath()%>/GestionPersonal" >Personal</a></li>
                    <li><a href="<%= request.getContextPath()%>/InventarioController">Inventario</a></li>
                    <li><a href="<%= request.getContextPath()%>/Finanzas">Finanzas</a></li>
                    <li><a href="<%= request.getContextPath()%>/GestionProformas" class="active">Proformas</a></li>
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

        <main class="main-content">

            <div class="header-content">
                <h1 class="main-title">Proformas</h1>
                <button id="new-proforma-btn" class="new-proforma-btn">
                    <i class='bx bx-plus'></i> Nueva Proforma
                </button>
            </div>

            <%-- MENSAJES --%>
            <%
                String mensajeExito = request.getParameter("mensaje");
                String mensajeError = (String) request.getAttribute("error");
                if (mensajeExito != null) {
                    String textoMensaje = "";
                    if (mensajeExito.equals("agregado_exitoso")) {
                        textoMensaje = "Proforma agregada exitosamente."; // Cambié "Cliente" por "Proforma"
                    } else if (mensajeExito.equals("editado_exitoso")) {
                        textoMensaje = "Proforma editada exitosamente."; // Cambié "Cliente" por "Proforma"
                    } else if (mensajeExito.equals("eliminado_exitoso")) {
                        textoMensaje = "Proforma eliminada exitosamente."; // Cambié "Cliente" por "Proforma"
                    } else if (mensajeExito.equals("estado_actualizado")) { // Nuevo mensaje para estado
                        textoMensaje = "Estado de Proforma actualizado exitosamente.";
                    }

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
                <input type="text" placeholder="Buscar proformas por Nro, DNI/RUC, etc.">
            </div>

            <%-- TABLA MEJORADA --%>
            <div class="table-container">
                <table class="proformas-table">
                    <thead>
                        <tr>
                            <th style="width: 15%;">Número Proforma</th>
                            <th style="width: 25%;">Cliente (DNI/RUC)</th>
                            <th style="width: 15%;">Fecha Emisión</th>
                            <th style="width: 15%; text-align: right;">Total</th>
                            <th style="width: 15%; text-align: center;">Estado</th>
                            <th style="width: 15%; text-align: center;">Acciones</th>
                        </tr>
                    </thead>

                    <tbody>
                        <%
                            List<Proforma> listaProformas = (List<Proforma>) request.getAttribute("listaProformas");
                            if (listaProformas != null && !listaProformas.isEmpty()) {
                                for (Proforma proforma : listaProformas) {
                                    String dniCliente = proforma.getDniCliente();
                                    String estadoBD = proforma.getEstado();
                                    String estadoClass;
                                    String estadoNombre;
                                    switch (estadoBD) {
                                        case "PAGADA_TOTAL":
                                            estadoClass = "status-pagada";
                                            estadoNombre = "Pagada";
                                            break;
                                        case "PAGADA_PARCIAL":
                                            estadoClass = "status-parcial";
                                            estadoNombre = "Pago Parcial";
                                            break;
                                        case "CANCELADA":
                                            estadoClass = "status-cancelada";
                                            estadoNombre = "Cancelada";
                                            break;
                                        case "PENDIENTE":
                                        default:
                                            estadoClass = "status-pendiente";
                                            estadoNombre = "Pendiente";
                                            break;
                                    }
                        %>

                        <tr>
                            <td>#<%= proforma.getIdProforma()%></td>  <td><%= dniCliente != null ? dniCliente : "N/A"%></td>
                            <td><%= proforma.getFechaProforma()%></td>  
                            <td style="text-align: right;">S/ <%= String.format("%,.2f", proforma.getTotal())%></td>  <td style="text-align: center;">
                                <span class="status-tag <%= estadoClass%>"><%= estadoNombre%></span>
                            </td>
                            <td>
                                <div class="action-buttons">
                                    <button 
                                        class="btn-ver"
                                        title="Ver Detalles"
                                        onclick="verProforma('<%= proforma.getIdProforma()%>')">
                                        <i class="fa-solid fa-eye"></i>
                                    </button>

                                    <button 
                                        class="btn-estado"
                                        title="Cambiar Estado"
                                        onclick="abrirModalEstado('<%= proforma.getIdProforma()%>', '<%= estadoBD%>')">
                                        <i class="fa-solid fa-pen-to-square"></i>
                                    </button>

                                    <%-- Botón de Descarga Condicional --%>
                                    <% if (estadoBD.equals("PAGADA_PARCIAL") || estadoBD.equals("PAGADA_TOTAL")) {%>
                                    <a href="<%= request.getContextPath()%>/GenerarPDF?id=<%= proforma.getIdProforma()%>&estado=<%= estadoBD%>"
                                       title="Descargar Comprobante de Pago"
                                       class="btn-pdf-download">
                                        <i class="fa-solid fa-file-pdf"></i>
                                    </a>
                                    <% } %>

                                </div>
                            </td>

                        </tr>

                        <%  }
                        } else { %>

                        <tr>
                            <td colspan="6" style="text-align:center; padding:30px; color:#999; font-style: italic;">
                                <i class="fa-solid fa-box-open"></i> No hay proformas registradas.
                            </td>
                        </tr>

                        <% }%>
                    </tbody>
                </table>
            </div>


            <%-- MODAL AGREGAR PROFORMA (SIN CAMBIOS) --%>
            <div id="proforma-modal" class="modal">
                <%-- Contenido del modal agregar proforma (No Modificado) --%>
                <div class="modal-content large-modal">
                    <div class="modal-header">
                        <h2><i class="fa-solid fa-file-invoice"></i> Nueva Proforma</h2>
                        <span class="close">&times;</span>
                    </div>
                    <form id="proforma-form" action="<%= request.getContextPath()%>/GestionProformas" method="POST">
                        <input type="hidden" name="accion" value="agregar">
                        <div class="modal-section">
                            <h3><i class="fa-solid fa-user"></i> Datos del Cliente</h3>
                            <div class="form-grid-2">
                                <div class="form-group large">
                                    <label>Seleccionar Cliente</label>
                                    <select id="docu_cliente_select" name="id_cliente" required onchange="cargarDatosCliente()">
                                        <option value="" disabled selected>--- Seleccione Cliente ---</option>
                                        <%
                                            List<Cliente> listaClientes = (List<Cliente>) request.getAttribute("listaClientes");
                                            if (listaClientes != null) {
                                                for (Cliente c : listaClientes) {%>
                                        <option value="<%= c.getDocu()%>">
                                            <%= c.getDocu()%> - <%= c.getNombre()%>
                                        </option>
                                        <% }
                                            }%>
                                    </select>
                                </div>
                                <div class="form-group small">
                                    <label>Fecha Emisión</label>
                                    <input type="date" id="fecha_emision" name="fecha_emision" required value="<%= java.time.LocalDate.now().toString()%>">
                                </div>
                            </div>
                            <div class="form-grid-3 margin-top">
                                <div class="form-group">
                                    <label>Nombre</label>
                                    <input type="text" id="cliente_nombre" readonly>
                                </div>
                                <div class="form-group">
                                    <label>Dirección/Empresa</label> <input type="text" id="cliente_empresa" readonly>
                                </div>
                                <div class="form-group">
                                    <label>Email</label>
                                    <input type="text" id="cliente_email" readonly>
                                </div>
                            </div>
                        </div>
                        <div class="modal-section">
                            <h3><i class="fa-solid fa-cart-plus"></i> Agregar Ítems (Productos/Servicios)</h3>
                            <div class="form-grid-4">
                                <div class="form-group">
                                    <label>Servicio</label>
                                    <select id="servicio_select">
                                        <option value="">--- Seleccionar Servicio ---</option>
                                        <%
                                            List<Servicio> listaServicios = (List<Servicio>) request.getAttribute("listaServicios");
                                            if (listaServicios != null) {
                                                for (Servicio s : listaServicios) {%>
                                        <option value="<%= s.getId_servicio()%>" data-precio="<%= s.getTarifa_base()%>">
                                            <%= s.getNombre()%> - S/ <%= String.format("%.2f", s.getTarifa_base())%>
                                        </option>
                                        <% }
                                            } %>
                                    </select>
                                </div>
                                <div class="form-group">
                                    <label>Producto</label>
                                    <select id="producto_select">
                                        <option value="">--- Seleccionar Producto ---</option>
                                        <%
                                            List<Producto> listaProductos = (List<Producto>) request.getAttribute("listaProductos");
                                            if (listaProductos != null) {
                                                for (Producto p : listaProductos) {%>
                                        <option value="<%= p.getId_producto()%>" data-precio="<%= p.getPrecio_unitario()%>">
                                            <%= p.getNombre()%> - S/ <%= String.format("%.2f", p.getPrecio_unitario())%>
                                        </option>
                                        <% }
                                            }%>
                                    </select>
                                </div>
                                <div class="form-group small">
                                    <label>Cantidad</label>
                                    <input type="number" id="cantidad_input" min="1" value="1">
                                </div>
                                <div class="form-group center">
                                    <button type="button" class="btn-add-item" onclick="agregarItem()">
                                        <i class="fa-solid fa-plus"></i> Añadir
                                    </button>
                                </div>
                            </div>
                            <div class="table-container margin-top">
                                <table class="item-detail-table">
                                    <thead>
                                        <tr>
                                            <th>Tipo</th>
                                            <th>Descripción</th>
                                            <th style="text-align: right;">Precio Unit.</th>
                                            <th style="text-align: center;">Cantidad</th>
                                            <th style="text-align: right;">Subtotal (con IGV)</th>
                                            <th></th>
                                        </tr>
                                    </thead>
                                    <tbody id="items-body">
                                    </tbody>
                                </table>
                            </div>
                        </div>
                        <div class="modal-section totals-section">
                            <h3><i class="fa-solid fa-calculator"></i> Totales</h3>
                            <div class="totals-grid">
                                <div class="form-group grid-total-row">
                                    <label>Subtotal (Base Imponible):</label>  
                                    <input type="text" id="subtotal_display" readonly value="0.00">
                                </div>
                                <div class="form-group grid-total-row">
                                    <label>IGV (18%):</label>
                                    <input type="text" id="impuestos_display" readonly value="0.00">
                                </div>
                                <div class="form-group grid-total-row final-total-group">  
                                    <label>TOTAL:</label>
                                    <input type="text" id="total_display" readonly value="0.00">
                                    <input type="hidden" name="presupuesto" id="presupuesto_hidden_input">
                                </div>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn-cancelar" onclick="cerrarModal()">
                                <i class="fa-solid fa-xmark"></i> Cancelar
                            </button>
                            <button type="submit" form="proforma-form" class="btn-guardar" id="btn-submit-proforma" disabled>
                                <i class="fa-solid fa-floppy-disk"></i> Guardar Proforma
                            </button>
                        </div>
                    </form>
                </div>
            </div>

            <%-- INICIO DEL MODAL DE CAMBIO DE ESTADO MEJORADO --%>
<div id="modal-estado-proforma" class="modal">
    <div class="modal-content small-modal">

        <div class="modal-header">
            <h2><i class="fa-solid fa-pen-to-square"></i> Cambiar Estado</h2>
            <span class="close-estado">&times;</span>
        </div>

        <%-- AÑADIDO ID AL FORMULARIO PARA INTERCEPTAR EL ENVÍO --%>
        <form id="form-cambiar-estado" action="<%= request.getContextPath()%>/GestionProformas" method="POST">
            <input type="hidden" name="accion" value="cambiarEstado">
            <input type="hidden" id="estado_id_proforma" name="idProforma">

            <div class="modal-section">
                <p><b>Proforma:</b> <span id="estado_display_id"></span></p>

                <label for="nuevo_estado">Selecciona el nuevo estado:</label>
                <select name="nuevoEstado" id="nuevo_estado" required>
                    <option value="PENDIENTE">Pendiente</option>
                    <option value="PAGADA_TOTAL">Pagada</option>
                    <option value="PAGADA_PARCIAL">Pago Parcial</option>
                    <option value="CANCELADA">Cancelada</option>
                </select>
            </div>

            <div class="modal-footer">
                <button type="button" class="btn-cancelar" onclick="cerrarModalEstado()">Cancelar</button>
                <%-- CAMBIADO A type="submit" para que el event listener lo intercepte --%>
                <button type="submit" class="btn-guardar" id="btn-guardar-estado">Guardar Estado</button>
            </div>
        </form>

    </div>
</div>
<%-- FIN DEL MODAL --%>


        </main>

        <script>
            // Definimos una variable global de JS con la ruta del contexto
            const CONTEXT_PATH = "<%= request.getContextPath()%>";
        </script>

        <script src="<%= request.getContextPath()%>/JavaScript/Proforma.js"></script>  


        <script>
    // URL base simulada para el contexto de la aplicación JSP.
    // Reemplaza esto con tu ruta real si es necesario.
    const CONTEXT_PATH = "<%= request.getContextPath() %>";

    // Funciones específicas para el Modal de Estado
    const modalEstado = document.getElementById("modal-estado-proforma");
    const nuevoEstadoSelect = document.getElementById("nuevo_estado");

    function abrirModalEstado(idProforma, estadoActual) {
        document.getElementById('estado_id_proforma').value = idProforma;
        document.getElementById('estado_display_id').textContent = '#' + idProforma; // Mostrar con #

        // Selecciona la opción actual
        nuevoEstadoSelect.value = estadoActual;

        modalEstado.style.display = "block";
    }

    function cerrarModalEstado() {
        modalEstado.style.display = "none";
    }

    // Cierra el modal de estado al hacer clic en la X
    document.querySelector(".close-estado").onclick = cerrarModalEstado;

    // Cierra el modal de estado al hacer clic fuera
    window.onclick = e => {
        if (e.target === modalEstado) {
            cerrarModalEstado();
        } 
        // Se asume que existe una función cerrarModal() y un modal "proforma-modal"
        /* else if (e.target === document.getElementById("proforma-modal")) {
            cerrarModal();
        }
        */
    };
    
    // =======================================================================
    // NUEVA LÓGICA PARA DESCARGAR PDF Y CERRAR MODAL AL GUARDAR ESTADO
    // =======================================================================

    /**
     * Activa la descarga del PDF de la proforma usando la URL del servlet GenerarPDF.
     * @param {string} idProforma - El ID de la proforma a descargar.
     * @param {string} nuevoEstado - El estado que se usará para el PDF (aunque a menudo no es necesario, se mantiene por tu código original).
     */
    function descargarPdfInmediatamente(idProforma, nuevoEstado) {
        // Construye la URL del servlet GenerarPDF
        const urlPDF = CONTEXT_PATH + "/GenerarPDF?id=" + idProforma + "&estado=" + nuevoEstado;
        
        // Crea un elemento <a> oculto y simula un clic para forzar la descarga
        const link = document.createElement('a');
        link.href = urlPDF;
        link.target = '_blank'; // Abrir en nueva pestaña
        link.style.display = 'none'; 
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
    }
    
    const formCambiarEstado = document.getElementById("form-cambiar-estado");

    if (formCambiarEstado) {
        formCambiarEstado.addEventListener('submit', async function(e) {
            e.preventDefault(); // <-- **CLAVE:** Evita la recarga de la página por el formulario

            // Obtiene los valores necesarios ANTES de la llamada asíncrona
            const idProforma = document.getElementById('estado_id_proforma').value;
            const nuevoEstado = document.getElementById('nuevo_estado').value;
            
            // Crea los datos del formulario (FormData) para el envío
            const formData = new FormData(this);
            
            // Simula la llamada al servidor para cambiar el estado (Asíncrona)
            try {
                const response = await fetch(this.action, {
                    method: 'POST',
                    body: formData,
                    // Si tu servlet devuelve una respuesta JSON, puedes añadir: headers: { 'Accept': 'application/json' }
                });

                if (response.ok || response.status === 200) {
                    // 1. Estado cambiado con éxito
                    
                    // 2. Cerrar el modal
                    cerrarModalEstado();
                    
                    // 3. Descargar el PDF inmediatamente
                    descargarPdfInmediatamente(idProforma, nuevoEstado);

                    // Opcional: Si es necesario, refresca la tabla de proformas o la página.
                    // location.reload(); 
                } else {
                    // Manejar el error del servidor (e.g., mostrar un mensaje de error en el UI)
                    console.error("Error al cambiar el estado de la proforma. Código de estado:", response.status);
                    alert("Hubo un error al actualizar el estado. Inténtalo de nuevo.");
                }
            } catch (error) {
                console.error("Error de red durante el cambio de estado:", error);
                alert("Error de conexión. Revisa tu red.");
            }
        });
    }

    // =======================================================================
    // Lógica para descarga por parámetros de URL (Original, ahora no necesaria)
    // Se mantiene comentada si quieres usar solo la lógica del submit.
    // Si esta lógica ya no se usa, la puedes eliminar para limpiar el código.
    // =======================================================================
    /*
    const urlParams = new URLSearchParams(window.location.search);
    const descargarPDF = urlParams.get('descargarPDF');
    const pdfId = urlParams.get('pdfId');
    const pdfEstado = urlParams.get('pdfEstado');

    if (descargarPDF === 'true' && pdfId && pdfEstado) {
        descargarPdfInmediatamente(pdfId, pdfEstado);
        // Limpiar los parámetros de la URL después de la descarga
        history.replaceState(null, '', location.pathname + location.search.replace(/&descargarPDF=true&pdfId=[^&]*&pdfEstado=[^&]*/, ''));
    }
    */
</script>

    </body>
</html>