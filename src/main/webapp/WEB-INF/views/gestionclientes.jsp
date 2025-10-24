<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/7.0.1/css/all.min.css" integrity="sha512-2SwdPD6INVrV/lHTZbO2nodKhrnDdJK9/kg2XD1r9uGqPo1cUbujc+IYdlYdEErWNu69gVcYgdxlmVmzTWnetw==" crossorigin="anonymous" referrerpolicy="no-referrer" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/GestionCliente.css">
    <link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css' rel='stylesheet'>
    <title>Gestión de Clientes</title>
</head>
<body>
    <header>
        <div class="brand">
            <img src="${pageContext.request.contextPath}/Imagenes/01.jpg" alt="Logo">
            <span>Acustica</span>
        </div>
        <nav>
            <ul class="navbar" id="navbar">
            <li><a href="${pageContext.request.contextPath}/dashboard" class="active">Inicio</a></li>
            <li><a href="${pageContext.request.contextPath}/inventario">Inventario</a></li>
            <li><a href="${pageContext.request.contextPath}/finanzas">Finanzas</a></li>
            <li><a href="${pageContext.request.contextPath}/proyectos">Proyectos</a></li>
        </ul>
        </nav>
        <div class="main">
            <i class='bx bx-search'></i>
            <input type="search" placeholder="Buscar">
            <i class='bx bxs-notification'></i>
            <img src="${pageContext.request.contextPath}/Imagenes/03.jpg" alt="Usuario">
            <i class='bx bx-menu' id="menu-icon"></i>
        </div>
    </header>
    <main class="content-area">
        <div style="padding-top: 100px;"></div> 

        <div class="header-table">
            <h1 class="page-title">Clientes y Cotizaciones</h1>
            <button class="btn-agregar-cliente">
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

            <div class="table-row">
                <div class="col-cliente">Isabella Rossi</div>
                <div class="col-empresa">Tech Solutions Inc.</div>
                <div class="col-telefono">+1-555-123-4567</div>
                <div class="col-email">isabella.rossi@techsolutions.com</div>
                <div class="col-acciones">
                    <i class='bx bx-pencil action-icon'></i>
                    <i class='bx bx-trash action-icon'></i>
                </div>
            </div>
            
            <div class="table-row">
                <div class="col-cliente">Carlos Ramirez</div>
                <div class="col-empresa">Global Innovations LLC</div>
                <div class="col-telefono">+1-555-987-6543</div>
                <div class="col-email">carlos.ramirez@globalinnovations.com</div>
                <div class="col-acciones">
                    <i class='bx bx-pencil action-icon'></i>
                    <i class='bx bx-trash action-icon'></i>
                </div>
            </div>
            </div>
        <button class="btn-proforma-rapida">
            <i class='bx bxs-file-doc'></i> Proforma Rápida
        </button>
    </main>
    <script src="${pageContext.request.contextPath}/JavaScript/Script1.js"></script>
</body>
</html>