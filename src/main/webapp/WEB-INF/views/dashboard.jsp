<%-- Directiva de página básica JSP --%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <%-- Font Awesome (Asegúrate de que la URL es correcta) --%>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css" integrity="sha512-DTOQO9RWCH3ppGqcWaEA1BIZOC6xxalwEsw9c2QQeAIftl+Vegovlnee1c9QX4TctnWMn13TZye+giMm8e2LwA==" crossorigin="anonymous" referrerpolicy="no-referrer" />
    
    <%-- CSS: Usando getContextPath() para ruta absoluta desde la raíz de la aplicación --%>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/Dashboard.css">
    
    <%-- Boxicons --%>
    <link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css' rel='stylesheet'>
    
    <title>Dashboard</title>
</head>
<body>
    <div class="sidebar" id="sidebar">
        <div class="header">
             <div class="menu-btn" id="menu-btn">
             <i class='bx bx-left-arrow-alt'></i>
             </div>
             <div class="brand">
                <%-- IMAGEN: Usando getContextPath() --%>
                <img src="<%= request.getContextPath() %>/Imagenes/01.jpg" alt="Logo">
                <span>Acustica</span>
             </div>
        </div>
        <div class="menu-container">
            <div class="search">
                <i class='bx bx-search'></i>
                <input type="search" placeholder="Buscar">
            </div>
            <ul class="menu">
            <%-- ENLACES DEL MENÚ: Cambiados a JSP (o Servlets) --%>
            <li class="menu-item menu-item-static active">
                <a href="<%= request.getContextPath() %>/Dashboard" class="menu-link">
                    <i class='bx bx-home-alt-2' ></i>
                    <span>Dashboard</span>
                </a>
            </li>
            <li class="menu-item menu-item-static">
                <a href="<%= request.getContextPath() %>/GestionClientes" class="menu-link">
                    <i class='bx bx-user'></i>
                    <span>Clientes</span>
                </a>
            </li>
             <li class="menu-item menu-item-static">
                <a href="<%= request.getContextPath() %>/Inventario" class="menu-link">
                   <i class='bx bx-folder-minus'></i>
                   <span>Inventario</span>
                </a>
            </li>
            <li class="menu-item menu-item-static">
                <a href="<%= request.getContextPath() %>/Proyectos" class="menu-link">
                   <i class='bx bx-slideshow'></i>
                   <span>Proyectos</span>
                </a>
            </li>
            <li class="menu-item menu-item-static">
                <a href="<%= request.getContextPath() %>/Finanzas" class="menu-link">
                    <i class='bx bx-money-withdraw'></i>
                    <span>Finanzas</span>
                </a>
            </li>
        </ul>
        </div>
        <div class="footer">
            <a href="<%= request.getContextPath() %>/logout" class="btn-cerrar-sesion">
                <i class='bx bx-subdirectory-right'></i>
                <span>Cerrar</span>
            </a>
        </div>
    </div>
    
    <div class="main-content">
        <h1>Dashboard</h1>
        <div class="stats-grid">
            
            <%-- EJEMPLO DE CÓDIGO JSP DINÁMICO --%>
            <% 
                // Simulamos obtener datos de un JavaBean o Servlet
                int proyectosActivos = 15;
                String ganancia = "32%";
                int stockBajo = 2; // ¡Alerta!
            %>
            
            <div class="stat-card">
                <p class="stat-title">Proyectos Activos</p>
                <%-- Se inserta el valor dinámicamente --%>
                <p class="stat-number"><%= proyectosActivos %></p>
            </div>
            
            <div class="stat-card">
                <p class="stat-title">Márgenes de Ganancia</p>
                <%-- Se inserta el valor dinámicamente --%>
                <p class="stat-number"><%= ganancia %></p>
            </div>
            
            <%-- Lógica JSP para cambiar la clase si el stock es muy bajo --%>
            <div class="stat-card <%= (stockBajo < 5) ? "stat-card-alert" : "" %>">
                <p class="stat-title">Stock Bajo</p>
                <p class="stat-number"><%= stockBajo %></p>
            </div>
            
            <div class="stat-card">
                <p class="stat-title">Cotizaciones Pendientes</p>
                <p class="stat-number">8</p>
            </div>
        </div>
    </div>
    
    <%-- JAVASCRIPT: Usando getContextPath() --%>
    <script src="<%= request.getContextPath() %>/JavaScript/Script1.js"></script>
</body>
</html>