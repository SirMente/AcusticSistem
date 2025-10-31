<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="models.Cliente"%>
<%@page import="models.Proforma"%>
<%-- Se asume que necesitas los imports para iterar sobre proformas y clientes --%>

<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Gestión de Proformas - Acustica</title>

        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css" integrity="sha512-2SwdPD6INVrV/lHTZbO2nodKhrnDdJK9/kg2XD1r9uGqPo1cUbujc+IYdlYdEErWNu69gVcYgdxlmVmzTWnetw==" crossorigin="anonymous" referrerpolicy="no-referrer" />
        <link rel="stylesheet" href="<%= request.getContextPath()%>/assets/css/Encabezado.css">
        <link rel="stylesheet" href="<%= request.getContextPath()%>/assets/css/Proforma.css">
        <link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css' rel='stylesheet'>
    </head>
    <body>

        <header>
            <div class="brand">
                <img src="<%= request.getContextPath()%>/Imagenes/01.jpg" alt="Logo">
                <span>Acustica</span>
            </div>
            <nav>
                <ul class="navbar" id="navbar">
                    <li><a href="<%= request.getContextPath()%>/Dashboard">Inicio</a></li>
                    <li><a href="<%= request.getContextPath()%>/InventarioController">Inventario</a></li>
                    <li><a href="<%= request.getContextPath()%>/Finanzas" class="active">Finanzas</a></li>
                    <li><a href="<%= request.getContextPath()%>/Proyectos">Proyectos</a></li>
                    <li><a href="<%= request.getContextPath()%>/GestionProformas">Proformas</a></li>
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

        <main class="main-content">
            <div class="header-content">
                <h1 class="main-title">Proformas</h1>
                <button id="new-proforma-btn" class="new-proforma-btn">
                    <i class='bx bx-plus text-lg'></i>
                    <span>Nueva Proforma</span>
                </button>
            </div>

            <%
                String mensajeError = (String) request.getAttribute("error");
                if (mensajeError != null) {
            %>
            <div class="alert alert-error"><%= mensajeError%></div>
            <%
                }
                String mensajeExito = request.getParameter("mensaje");
                if ("proforma_agregada".equals(mensajeExito)) {
            %>
            <div class="alert alert-success">Proforma agregada con éxito.</div>
            <%
                }
            %>
            <div class="table-container">
                <table class="proformas-table">
                    <thead>
                        <tr>
                            <th scope="col">Número de Proforma</th>
                            <th scope="col">Cliente</th>
                            <th scope="col">Fecha de Emisión</th>
                            <th scope="col">Total</th>
                            <th scope="col">Estado</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%
                            // Suponemos que tu Controller adjunta una lista con la clave "listaProformas"
                            List<Proforma> listaProformas = (List<Proforma>) request.getAttribute("listaProformas");

                            if (listaProformas != null && !listaProformas.isEmpty()) {
                                for (Proforma proforma : listaProformas) {
                                    // **NOTA:** La lógica de obtener el nombre del cliente y el estado 
                                    // se mantiene simple aquí (como estaba), asumiendo que el Controller/DAO 
                                    // puede necesitar mejorar esto para datos reales.

                                    String estadoClass = "status-pendiente"; // Clase CSS basada en el estado
                                    String nombreCliente = "ID: " + proforma.getIdCliente(); // Temporal si no se puede obtener el nombre
                                    String estadoProforma = "Pendiente"; // Temporal si no viene del modelo
%>
                        <tr>
                            <td><%= "PF-2024-" + proforma.getIdProforma()%></td>
                            <td><%= nombreCliente%></td>
                            <td><%= proforma.getFechaEmision()%></td>
                            <%-- Usamos String.format para asegurar un formato de dos decimales para el dinero --%>
                            <td><%= String.format("$%.2f", proforma.getPresupuesto())%></td>
                            <td>
                                <span class="status-tag <%= estadoClass%>"><%= estadoProforma%></span>
                            </td>
                        </tr>
                        <%
                            } // Fin del for
                        } else {
                            // **MENSAJE CUANDO NO HAY DATOS EN LA BASE DE DATOS (REEMPLAZA FILAS ESTÁTICAS)**
                        %>
                        <tr>
                            <td colspan="5" style="text-align: center; color: #777; padding: 20px;">
                                No hay proformas registradas en la base de datos.
                            </td>
                        </tr>
                        <%
                } // Fin del if
%>
                    </tbody>
                </table>
            </div>

            <div id="proforma-modal" class="modal">
                <div class="modal-content">
                    <div class="modal-header">
                        <h2>Proforma Rápida</h2>
                        <span class="close">&times;</span>
                    </div>
                    <div class="modal-body">
                        <form id="proforma-form" action="<%= request.getContextPath()%>/GestionProformas" method="POST">
                            <div class="form-section">
                                <h3>Datos del cliente</h3>
                                <div class="form-row">
                                    <div class="form-group">
                                        <label for="id_cliente_input">ID Cliente</label>
                                        <input type="number" id="id_cliente_input" name="id_cliente" placeholder="ID Cliente" required onchange="cargarDatosCliente()">
                                    </div>
                                    <div class="form-group">
                                        <label for="cliente_nombre">Nombres</label>
                                        <input type="text" id="cliente_nombre" placeholder="Nombres" disabled>
                                    </div>
                                </div>
                                <div class="form-row">
                                    <div class="form-group">
                                        <label for="cliente_empresa">Empresa/RazónSocial</label>
                                        <input type="text" id="cliente_empresa" placeholder="Empresa/RazónSocial" disabled>
                                    </div>
                                    <div class="form-group">
                                        <label for="cliente_telefono">Teléfono</label>
                                        <input type="tel" id="cliente_telefono" placeholder="Teléfono" disabled>
                                    </div>
                                </div>
                                <div class="form-row">
                                    <div class="form-group full-width">
                                        <label for="cliente_email">Email</label>
                                        <input type="email" id="cliente_email" placeholder="Email" disabled>
                                    </div>
                                </div>
                            </div>

                            <div class="form-section">
                                <h3>Detalles de cotización</h3>
                                <div class="form-row">
                                    <div class="form-group">
                                        <label for="descripcion_servicio">Producto/Servicio</label>
                                        <input type="text" id="descripcion_servicio" name="descripcion_servicio" required>
                                    </div>

                                </div>
                                <div class="form-row">
                                    <div class="form-group">
                                        <label for="presupuesto">Presupuesto</label>
                                        <input type="number" id="presupuesto" name="presupuesto" min="0" step="0.01">
                                    </div>
                                    <div class="form-group">
                                        <label for="fecha_emision">Fecha de Emisión *</label>
                                        <input type="date" id="fecha_emision" name="fecha_emision" required>
                                    </div>
                                </div>
                                <div class="form-row">
                                    <div class="form-group full-width">
                                        <label for="descripcion">Descripción Adicional</label>
                                        <textarea id="descripcion" name="descripcion" rows="4"></textarea>
                                    </div>
                                </div>
                            </div>
                        </form>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn-cancelar">Cancelar</button>
                        <button type="submit" form="proforma-form" class="btn-guardar">Guardar Proforma</button>
                    </div>
                </div>
            </div>
        </main>

        <script src="<%= request.getContextPath()%>/JavaScript/Proforma.js"></script>

        <script>
                                    // Debes incluir la función cargarDatosCliente() y el código del ClienteDataServlet aquí
                                    function cargarDatosCliente() {
                                        // ... (Función de AJAX que busca el cliente por ID) ...
                                        console.log("Buscando datos del cliente...");
                                    }
        </script>
    </body>
</html>