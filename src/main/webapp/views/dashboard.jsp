<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>


<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <!-- ICONOS -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css">

    <!-- TU CSS -->
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/Dashboard.css">

    <title>Dashboard</title>
</head>

<body>

<!-- ================== SIDEBAR ================== -->
<div class="sidebar" id="sidebar">

    <div class="header">
        <div class="menu-btn" id="menu-btn">
            <i class="fa-solid fa-bars"></i>
        </div>

        <div class="brand">
            <img src="<%= request.getContextPath()%>/Imagenes/01.jpg" alt="">
            <span>Acústica</span>
        </div>
    </div>

    <div class="menu-container">

        <ul class="menu">
            <li class="menu-item active">
                <a href="<%= request.getContextPath()%>/dashboard" class="menu-link">
                    <i class="fa-solid fa-house"></i>
                    <span>Inicio</span>
                </a>
            </li>

            <li class="menu-item">
                <a href="<%= request.getContextPath()%>/GestionClientes" class="menu-link">
                    <i class="fa-solid fa-users"></i>
                    <span>Clientes</span>
                </a>
            </li>

            <li class="menu-item">
                <a href="<%= request.getContextPath()%>/InventarioController" class="menu-link">
                    <i class="fa-solid fa-box"></i>
                    <span>Inventario</span>
                </a>
            </li>

            <li class="menu-item">
                <a href="<%= request.getContextPath()%>/Proyectos" class="menu-link">
                    <i class="fa-solid fa-diagram-project"></i>
                    <span>Proyectos</span>
                </a>
            </li>

            <li class="menu-item">
                <a href="<%= request.getContextPath()%>/Finanzas" class="menu-link">
                    <i class="fa-solid fa-chart-line"></i>
                    <span>Finanzas</span>
                </a>
            </li>

            <li class="menu-item">
                <a href="<%= request.getContextPath()%>/GestionPersonal" class="menu-link">
                    <i class="fa-solid fa-user-tie"></i>
                    <span>Personal</span>
                </a>
            </li>
        </ul>

    </div>

    <div class="footer">
        <a class="btn-cerrar-sesion" href="<%= request.getContextPath()%>/Logout">
            <i class="fa-solid fa-right-from-bracket"></i>
            <span>Cerrar sesión</span>
        </a>
    </div>

</div>

<!-- ================== CONTENIDO PRINCIPAL ================== -->
<div class="main-content">

    <h1>Dashboard</h1>

    <%-- Datos dinámicos --%>
    <%
        int proyectos = request.getAttribute("proyectos") != null ? (Integer) request.getAttribute("proyectos") : 0;
        int clientes = request.getAttribute("clientes") != null ? (Integer) request.getAttribute("clientes") : 0;
        double margen = request.getAttribute("margen") != null ? (double) request.getAttribute("margen") : 0;
        int alertasStock = request.getAttribute("stock_bajo") != null ? (Integer) request.getAttribute("stock_bajo") : 0;
    %>

    <!-- ================== TARJETAS KPI ================== -->
    <div class="stats-grid">

        <div class="stat-card">
            <div class="stat-title">Clientes registrados</div>
            <div class="stat-number"><%= clientes %></div>
        </div>

        <div class="stat-card">
            <div class="stat-title">Proyectos activos</div>
            <div class="stat-number"><%= proyectos %></div>
        </div>

        <div class="stat-card">
            <div class="stat-title">Margen de Ganancia</div>
            <div class="stat-number"><%= margen %> %</div>
        </div>

        <div class="stat-card <%= alertasStock > 0 ? "stat-card-alert" : "" %>">
            <div class="stat-title">Productos con poco stock</div>
            <div class="stat-number"><%= alertasStock %></div>
        </div>

    </div>

    <!-- ================== GRÁFICOS ================== -->
    <section class="graficos-seccion">
        <div class="grafico-card">
            <h3>Ingresos vs Gastos</h3>
            <canvas id="graficoFinanzas"></canvas>
        </div>

        <div class="grafico-card">
            <h3>Proyectos por Estado</h3>
            <canvas id="graficoProyectos"></canvas>
        </div>
    </section>

    <!-- ================== ACCESOS RÁPIDOS ================== -->
    <section class="accesos-rapidos">
        <h2 style="color:white;">Accesos rápidos</h2>

        <div class="accesos-grid">
            <a class="btn-acceso" href="<%= request.getContextPath()%>/GestionClientes"><i class="fa-solid fa-users"></i>Clientes</a>
            <a class="btn-acceso" href="<%= request.getContextPath()%>/InventarioController"><i class="fa-solid fa-box"></i>Inventario</a>
            <a class="btn-acceso" href="<%= request.getContextPath()%>/Proyectos"><i class="fa-solid fa-diagram-project"></i>Proyectos</a>
            <a class="btn-acceso" href="<%= request.getContextPath()%>/Finanzas"><i class="fa-solid fa-money-bill-trend-up"></i>Finanzas</a>
        </div>
    </section>

</div>

<!-- ================== SCRIPTS ================== -->
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<script src="<%= request.getContextPath()%>/JavaScript/dashboardGraficos.js"></script>

<!-- Toggle del menú lateral -->
<script>
    const sidebar = document.getElementById("sidebar");
    const btn = document.getElementById("menu-btn");

    btn.addEventListener("click", () => {
        sidebar.classList.toggle("minimize");
    });
</script>

</body>
</html>
