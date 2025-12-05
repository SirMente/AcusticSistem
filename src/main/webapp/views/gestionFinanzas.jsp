<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="models.Finanza" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Finanzas</title>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css" integrity="sha512-2SwdPD6INVrV/lHTZbO2nodKhrnDdJK9/kg2XD1r9uGqPo1cUbujc+IYdlYdEErWNu69gVcYgdxlmVmzTWnetw==" crossorigin="anonymous" referrerpolicy="no-referrer" />
        <link rel="stylesheet" href="<%= request.getContextPath()%>/assets/css/Encabezado.css">
        <link rel="stylesheet" href="<%= request.getContextPath()%>/assets/css/Finanzas.css">
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
                    <li><a href="<%= request.getContextPath()%>/Proveedores" >Proveedores</a></li>
                    <li><a href="<%= request.getContextPath()%>/GestionClientes">Clientes</a></li>
                    <li><a href="<%= request.getContextPath()%>/ServicioController" >Servicios</a></li>
                    <li><a href="<%= request.getContextPath()%>/GestionPersonal" >Personal</a></li>
                    <li><a href="<%= request.getContextPath()%>/InventarioController">Inventario</a></li>
                    <li><a href="<%= request.getContextPath()%>/Finanzas" class="active">Finanzas</a></li>
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

        <main class="contenido">
            <h1>Finanzas</h1>

            <section class="resumen">
                <div class="card">
                    <h3>Ingresos vs. Gastos</h3>
                    <h1 id="totalBalance">
                        <%-- Aquí puedes calcular el total dinámicamente --%>
                        $<%= request.getAttribute("totalBalance") != null ? request.getAttribute("totalBalance") : "0"%>
                    </h1>
                    <p>Este mes <span id="variacion" class="positivo">
                            <%= request.getAttribute("variacion") != null ? request.getAttribute("variacion") : "↑0%"%>
                        </span></p>
                    <div class="barras">
                        <div class="ingreso"></div>
                        <div class="gasto"></div>
                    </div>
                </div>
                <div class="card">
                    <h3>Márgenes de Ganancia</h3>
                    <h1 id="margenGanancia">
                        <%= request.getAttribute("margenGanancia") != null ? request.getAttribute("margenGanancia") : "0%"%>
                    </h1>
                    <p>Este mes <span class="negativo">↓5%</span></p>
                    <div class="circulo">
                        <span id="porcentaje">0%</span>
                    </div>
                </div>
                <button id="btnNuevo" class="nuevo-registro">Nuevo registro</button>
            </section>

            <section class="reportes">
                <h2>Reportes</h2>
                <button class="btn-excel" onclick="window.location.href = 'ExportExcelServlet'">
                    <i class="fa-solid fa-file-excel"></i> Exportar a Excel
                </button>
                <table id="tablaFinanzas">
                    <thead>
                        <tr>
                            <th>Fecha</th>
                            <th>Categoría</th>
                            <th>Tipo</th>
                            <th>Descripción</th>
                            <th>Monto</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%
                            List<Finanza> listaFinanzas = (List<Finanza>) request.getAttribute("listaFinanzas");
                            if (listaFinanzas != null) {
                                for (Finanza f : listaFinanzas) {
                        %>
                        <tr>
                            <td><%= f.getFecha()%></td>
                            <td><%= f.getCategoria()%></td>
                            <td><%= f.getTipo()%></td>
                            <td><%= f.getDescripcion()%></td>
                            <td><%= f.getMonto()%></td>
                        </tr>
                        <%
                                }
                            }
                        %>
                    </tbody>
                </table>
            </section>
        </main>

        <!-- Modal -->
        <div id="modal" class="modal">
            <div class="modal-content">
                <h2>Nuevo registro financiero</h2>
                <form id="formRegistro" action="Finanzas" method="post">
                    <input type="date" id="fecha" name="fecha" required>
                    <select id="tipo" name="tipo" required>
                        <option value="">Selecciona Ingreso o Gasto</option>
                        <option value="ingreso">Ingreso</option>
                        <option value="gasto">Gasto</option>
                    </select>
                    <select id="categoria" name="categoria" required>
                        <option value="">Selecciona categoría</option>
                        <option value="Ventas">Ventas</option>
                        <option value="Marketing">Marketing</option>
                        <option value="Operaciones">Operaciones</option>
                    </select>
                    <input type="text" id="descripcion" name="descripcion" placeholder="Descripción" required>
                    <input type="number" id="monto" name="monto" placeholder="Precio" required>
                    <div class="acciones">
                        <button type="submit">Guardar registro</button>
                        <button type="button" id="cerrarModal">Cancelar</button>
                    </div>
                </form>
            </div>
        </div>

        <script src="<%= request.getContextPath()%>/JavaScript/Finanzas.js"></script>
    </body>
</html>
