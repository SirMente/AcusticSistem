<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css" integrity="sha512-2SwdPD6INVrV/lHTZbO2nodKhrnDdJK9/kg2XD1r9uGqPo1cUbujc+IYdlYdEErWNu69gVcYgdxlmVmzTWnetw==" crossorigin="anonymous" referrerpolicy="no-referrer" />
        <link rel="stylesheet" href="./assets/css/Dashboard.css">
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
                    <img src="./Imagenes/01.jpg" alt="Logo">
                    <span>Acustica</span>
                </div>
            </div>
            <div class="menu-container">
                <div class="search">
                    <i class='bx bx-search'></i>
                    <input type="search" placeholder="Buscar">
                </div>
                <ul class="menu">
                    <li class="menu-item menu-item-static active">
                        <a href="../HTML/Dashboard.html" class="menu-link">
                            <i class='bx bx-home-alt-2' ></i>
                            <span>Dashboard</span>
                        </a>
                    </li>
                    <li class="menu-item menu-item-static">
                        <a href="${pageContext.request.contextPath}/clientes" class="menu-link"> 
                            <i class='bx bx-user'></i>
                            <span>Clientes</span>
                        </a>
                    </li>
                    <li class="menu-item menu-item-static">
                        <a href="../HTML/Inventario.html" class="menu-link">
                            <i class='bx bx-folder-minus'></i>
                            <span>Inventario</span>
                        </a>
                    </li>
                    <li class="menu-item menu-item-static">
                        <a href="../HTML/Proyectos.html" class="menu-link">
                            <i class='bx bx-slideshow'></i>
                            <span>Proyectos</span>
                        </a>
                    </li>
                    <li class="menu-item menu-item-static">
                        <a href="../HTML/Finanzas.html" class="menu-link">
                            <i class='bx bx-money-withdraw'></i>
                            <span>Finanzas</span>
                        </a>
                    </li>
                </ul>
            </div>
            <div class="footer">
                <a href="/logout" class="btn-cerrar-sesion">
                    <i class='bx bx-subdirectory-right'></i>
                    <span>Cerrar</span>
                </a>
            </div>
        </div>
        <div class="main-content">
            <h1>Dashboard</h1>
            <div class="stats-grid">
                <div class="stat-card">
                    <p class="stat-title">Proyectos Activos</p>
                    <p class="stat-number">12</p> </div>

                <div class="stat-card">
                    <p class="stat-title">Márgenes de Ganancia</p>
                    <p class="stat-number">25%</p>
                </div>

                <div class="stat-card stat-card-alert">
                    <p class="stat-title">Stock Bajo</p>
                    <p class="stat-number">5</p>
                </div>

                <div class="stat-card">
                    <p class="stat-title">Cotizaciones Pendientes</p>
                    <p class="stat-number">8</p>
                </div>
            </div>
        </div>
        <script src="../JavaScript/Script1.js"></script>
    </body>
</html>
