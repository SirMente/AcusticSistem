<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="models.Proyecto"%>
<%@page import="models.Proforma"%>
<%@page import="models.Empleado"%>
<%@page import="java.text.SimpleDateFormat"%>
<%
    // Definimos el formato de fecha para mostrar
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    // Obtenemos las listas del Controller
    List<Proyecto> listaProyectos = (List<Proyecto>) request.getAttribute("listaProyectos");
    List<Proforma> proformasDisponibles = (List<Proforma>) request.getAttribute("proformasDisponibles");
    List<Empleado> personalDisponible = (List<Empleado>) request.getAttribute("personalDisponible");
%>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">

        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css" crossorigin="anonymous"/>
        <link rel="stylesheet" href="<%= request.getContextPath()%>/assets/css/Encabezado.css">
        <link rel="stylesheet" href="<%= request.getContextPath()%>/assets/css/GestionCliente.css"> <%-- Reutilizamos el CSS general --%>
        <link rel="stylesheet" href="https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css">
        <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;600;700&display=swap" rel="stylesheet">

        <title>Gestión de Proyectos</title>

        <style>
            /* Fuente Inter para una apariencia moderna */
            body {
                font-family: 'Inter', sans-serif;
                background-color: #f4f7fa; /* Fondo suave */
            }

            /* --- Contenedor Principal (para asegurar buen padding) --- */
            .content-area {
                padding: 20px;
                max-width: 1400px;
                margin: 0 auto;
            }

            /* --- GRID Y CARTAS DE PROYECTOS (Mejoras menores) --- */

            .project-grid {
                display: grid;
                grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
                gap: 25px; /* Más espacio entre tarjetas */
                padding: 20px 0;
            }

            .project-card {
                background: #fff;
                border-radius: 12px;
                box-shadow: 0 4px 18px rgba(0, 0, 0, 0.05);
                padding: 25px;
                border-left: 6px solid; /* Usamos border-left para el color de estado */
                transition: transform 0.2s, box-shadow 0.2s;
            }

            .project-card:hover {
                transform: translateY(-8px); /* Más énfasis en el hover */
                box-shadow: 0 12px 25px rgba(0, 0, 0, 0.1);
            }
            
            .card-header {
                display: flex;
                justify-content: space-between;
                align-items: flex-start;
                margin-bottom: 15px;
            }

            .card-title {
                font-size: 1.4rem;
                font-weight: 700;
                color: #2c3e50;
            }

            .card-subtitle {
                font-size: 0.85rem;
                color: #95a5a6;
                margin-top: 5px;
            }

            .card-actions {
                display: flex;
                gap: 15px;
            }

            .card-actions .action-icon {
                font-size: 1.3rem;
                color: #bdc3c7;
                cursor: pointer;
                transition: color 0.2s;
            }
            
            .card-actions .action-icon:hover {
                color: #3498db;
            }

            .card-info {
                margin-bottom: 20px;
            }

            .card-info-item {
                font-size: 0.95rem;
                color: #555;
                margin-top: 8px;
            }

            .card-info-item i {
                color: #3498db; /* Azul consistente para iconos de info */
                margin-right: 5px;
            }

            /* ------------------------------------- */
            /* --- ESTILOS DE BARRA DE PROGRESO --- */
            /* ------------------------------------- */

            .progress-bar {
                width: 100%;
                background-color: #ecf0f1; /* Fondo claro de la barra */
                border-radius: 5px;
                height: 10px;
                margin: 10px 0;
                overflow: hidden; /* Importante para el fill */
            }

            .progress-bar-fill {
                height: 100%;
                transition: width 0.4s ease; /* Animación suave */
                border-radius: 5px;
            }
            
            .progress-text {
                font-size: 0.9rem;
                color: #7f8c8d;
                margin-top: 5px;
            }

            /* Colores de Borde por Estado */
            .estado-Planificacion {
                border-left-color: #f1c40f;
            } /* Amarillo */
            .estado-Instalacion {
                border-left-color: #3498db;
            } /* Azul */
            .estado-Pruebas {
                border-left-color: #2ecc71;
            } /* Verde */
            .estado-Entrega {
                border-left-color: #e74c3c;
            } /* Rojo/Finalizado */

            /* Colores para la barra de progreso */
            .estado-Planificacion .progress-bar-fill {
                background-color: #f1c40f;
            }
            .estado-Instalacion .progress-bar-fill {
                background-color: #3498db;
            }
            .estado-Pruebas .progress-bar-fill {
                background-color: #2ecc71;
            }
            .estado-Entrega .progress-bar-fill {
                background-color: #e74c3c;
            }


            /* ------------------------------------- */
            /* --- MEJORAS EN EL DISEÑO DEL MODAL ---*/
            /* ------------------------------------- */

            .modal {
                display: none; /* Oculto por defecto */
                position: fixed;
                z-index: 1000;
                left: 0;
                top: 0;
                width: 100%;
                height: 100%;
                background-color: rgba(0, 0, 0, 0.6); /* Fondo semi-transparente oscuro */
                justify-content: center;
                align-items: center;
                padding: 20px;
                box-sizing: border-box;
            }

            .modal-content {
                background-color: #fff;
                padding: 40px;
                border-radius: 15px;
                box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
                width: 100%;
                max-width: 500px;
                position: relative;
                animation: fadeIn 0.3s ease-out;
            }

            @keyframes fadeIn {
                from {
                    opacity: 0;
                    transform: scale(0.95) translateY(-20px);
                }
                to {
                    opacity: 1;
                    transform: scale(1) translateY(0);
                }
            }

            .modal-title {
                font-size: 1.75rem;
                font-weight: 700;
                color: #2c3e50;
                margin-bottom: 25px;
                border-bottom: 2px solid #ecf0f1;
                padding-bottom: 10px;
            }

            .modal-form input[type="text"],
            .modal-form input[type="date"],
            .modal-form select {
                width: 100%;
                padding: 12px;
                margin-bottom: 20px;
                border: 1px solid #dfe6e9;
                border-radius: 8px;
                box-sizing: border-box;
                font-size: 1rem;
                transition: border-color 0.3s, box-shadow 0.3s;
                background-color: #f8f9fa;
            }

            /* Estilo específico para el select deshabilitado en edición */
            .modal-form select:disabled {
                background-color: #e9ecef;
                color: #6c757d;
                cursor: not-allowed;
            }


            .modal-form input:focus,
            .modal-form select:focus {
                border-color: #3498db;
                box-shadow: 0 0 0 3px rgba(52, 152, 219, 0.2);
                outline: none;
                background-color: #fff;
            }

            .modal-label {
                display: block;
                font-weight: 600;
                color: #34495e;
                margin-bottom: 5px;
                font-size: 0.9rem;
            }

            .close-btn {
                position: absolute;
                top: 15px;
                right: 25px;
                color: #aaa;
                font-size: 30px;
                font-weight: bold;
                cursor: pointer;
                transition: color 0.2s;
            }

            .close-btn:hover,
            .close-btn:focus {
                color: #e74c3c;
                text-decoration: none;
            }

            .btn-agregar, .btn-agregar-cliente {
                /* Usamos el mismo estilo para consistencia */
                background-color: #3498db;
                color: white;
                padding: 12px 25px;
                border: none;
                border-radius: 8px;
                cursor: pointer;
                font-size: 1rem;
                font-weight: 600;
                transition: background-color 0.3s ease, transform 0.1s;
                box-shadow: 0 4px 10px rgba(52, 152, 219, 0.3);
            }

            .btn-agregar:hover, .btn-agregar-cliente:hover {
                background-color: #2980b9;
                transform: translateY(-2px);
            }

            /* Responsive ajustes para el modal en pantallas pequeñas */
            @media (max-width: 600px) {
                .modal-content {
                    margin: 10px;
                    padding: 25px;
                }
                .modal-title {
                    font-size: 1.4rem;
                }
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
                    <li><a href="<%= request.getContextPath()%>/GestionPersonal">Personal</a></li>
                    <li><a href="<%= request.getContextPath()%>/ServicioController" >Servicios</a></li>
                    <li><a href="<%= request.getContextPath()%>/InventarioController">Inventario</a></li>
                    <li><a href="<%= request.getContextPath()%>/Finanzas">Finanzas</a></li>
                    <li><a href="<%= request.getContextPath()%>/GestionProformas">Proformas</a></li>
                    <li class="active"><a href="<%= request.getContextPath()%>/Proyectos" class="active">Proyectos</a></li>
                </ul>
            </nav>

            <div class="main">
                <i class='bx bxs-notification'></i>
                <img src="<%= request.getContextPath()%>/Imagenes/03.jpg" alt="Usuario">
                <i class='bx bx-menu' id="menu-icon"></i>
            </div>
        </header>

        <main class="content-area">
            <div style="padding-top: 100px;"></div>

            <%-- MENSAJES (Se mantiene el estilo para feedback rápido) --%>
            <%
                String mensajeExito = request.getParameter("mensaje");
                String mensajeError = (String) request.getAttribute("error");

                if (request.getParameter("error") != null) {
                    if (request.getParameter("error").equals("id_invalido")) {
                        mensajeError = "Error: El ID del proyecto no es válido.";
                    } else if (request.getParameter("error").equals("bd_eliminar")) {
                        mensajeError = "Error de BD al eliminar el proyecto.";
                    } // Añadimos el mensaje de error de edición conocido
                    else if (request.getParameter("error").equals("idproforma_null")) {
                        mensajeError = "Error al actualizar proyecto: La Proforma asociada no puede ser nula. Intenta de nuevo.";
                    }
                }

                if (mensajeExito != null) {
                    String textoMensaje = "";
                    if (mensajeExito.equals("agregado_exitoso"))
                        textoMensaje = "Proyecto agregado exitosamente.";
                    else if (mensajeExito.equals("editado_exitoso"))
                        textoMensaje = "Proyecto editado exitosamente.";
                    else if (mensajeExito.equals("eliminado_exitoso"))
                        textoMensaje = "Proyecto eliminado exitosamente.";
            %>
            <div style="color: #27ae60; padding: 15px; border: 1px solid #2ecc71; background-color: #e8f9e8; margin-bottom: 25px; border-radius: 8px;">
                <i class='bx bx-check-circle'></i> <%= textoMensaje%>
            </div>
            <% } else if (mensajeError != null) {%>
            <div style="color: #c0392b; padding: 15px; border: 1px solid #e74c3c; background-color: #fde8e8; margin-bottom: 25px; border-radius: 8px;">
                <i class='bx bx-error-alt'></i> Error: <%= mensajeError%>
            </div>
            <% } %>

            <div class="header-table">
                <h1 class="page-title">Gestión de Proyectos</h1>
                <button class="btn-agregar-cliente" id="btn-agregar-proyecto" data-action="agregar">
                    <i class='bx bx-plus-circle'></i> Nuevo Proyecto
                </button>
            </div>

            <div class="project-grid">
                <% if (listaProyectos != null && !listaProyectos.isEmpty()) {
                        for (Proyecto proyecto : listaProyectos) {
                            // Determinar el porcentaje y la clase de estado
                            int progreso = 0;
                            String estadoClase = "";
                            switch (proyecto.getEstado()) {
                                case "Planificacion":
                                    progreso = 25;
                                    estadoClase = "estado-Planificacion";
                                    break;
                                case "Instalacion":
                                    progreso = 50;
                                    estadoClase = "estado-Instalacion";
                                    break;
                                case "Pruebas":
                                    progreso = 75;
                                    estadoClase = "estado-Pruebas";
                                    break;
                                case "Entrega":
                                    progreso = 100;
                                    estadoClase = "estado-Entrega";
                                    break;
                                default:
                                    progreso = 0;
                                    estadoClase = "estado-Planificacion";
                                    break;
                            }
                %>

                <div class="project-card <%= estadoClase%>">
                    <div class="card-header">
                        <div>
                            <div class="card-title"><%= proyecto.getNombreProyecto()%></div>
                            <div class="card-subtitle">Proforma ID: <strong>#<%= proyecto.getIdProforma()%></strong></div>
                        </div>
                        <div class="card-actions">
                            <a href="#" class="edit-btn-proyecto"
                               data-id="<%= proyecto.getIdProyecto()%>"
                               data-proforma="<%= proyecto.getIdProforma()%>"
                               data-nombre="<%= proyecto.getNombreProyecto()%>"
                               data-inicio="<%= proyecto.getFechaInicio()%>"
                               data-fin="<%= proyecto.getFechaFinEstimada()%>"
                               data-estado="<%= proyecto.getEstado()%>"
                               data-dni="<%= proyecto.getDniEncargado()%>">
                                <i class='bx bx-pencil action-icon' title="Editar Proyecto"></i>
                            </a>

                            <a href="<%= request.getContextPath()%>/Proyectos?operacion=eliminar&id=<%= proyecto.getIdProyecto()%>" title="Eliminar Proyecto" 
                               onclick="return confirm('¿Está seguro de que desea eliminar el proyecto <%= proyecto.getNombreProyecto()%>? Esta acción es irreversible.');">
                                <i class='bx bx-trash action-icon'></i>
                            </a>
                        </div>
                    </div>

                    <div class="card-info">
                        <div class="card-info-item">
                            <i class='bx bx-user'></i>
                            Encargado: <strong><%= proyecto.getNombreEncargado() != null ? proyecto.getNombreEncargado() : "N/A"%></strong>
                        </div>
                        <div class="card-info-item">
                            <i class='bx bx-id-card'></i>
                            Cliente RUC: <strong><%= proyecto.getRucCliente() == 0 ? "N/A" : proyecto.getRucCliente()%></strong>
                        </div>
                        <div class="card-info-item">
                            <i class='bx bx-calendar-alt'></i>
                            Inicio: <%= proyecto.getFechaInicio() != null ? sdf.format(proyecto.getFechaInicio()) : "N/A"%>
                        </div>
                        <div class="card-info-item">
                            <i class='bx bx-calendar-star'></i>
                            Fin Estimado: <%= proyecto.getFechaFinEstimada() != null ? sdf.format(proyecto.getFechaFinEstimada()) : "N/A"%>
                        </div>
                    </div>

                    <%-- Lógica y Estructura de la Barra de Progreso --%>
                    <div class="progress-bar">
                        <div class="progress-bar-fill" style="width: <%= progreso%>%;"></div>
                    </div>
                    <div class="progress-text">Progreso: <strong><%= proyecto.getEstado()%> (<%= progreso%>%)</strong></div>

                </div>

                <% }
                } else { %>
                <div class="project-card" style="grid-column: 1 / -1; text-align: center; border-left-color: #95a5a6;">
                    <p style="color: #777; padding: 20px;">
                        <i class='bx bx-folder-open' style="font-size: 2rem; color: #95a5a6;"></i>
                        <br>
                        No hay proyectos registrados o disponibles. Presiona "Nuevo Proyecto" para empezar.
                    </p>
                </div>
                <% }%>
            </div>

            <div style="height: 50px;"></div>
        </main>

        <%-- MODAL DE AGREGAR/EDITAR PROYECTO --%>
        <div id="modal-proyecto" class="modal">
            <div class="modal-content">
                <span class="close-btn">&times;</span>
                <h2 class="modal-title" id="modal-title-action">Crear Nuevo Proyecto</h2>

                <form action="<%= request.getContextPath()%>/Proyectos" method="POST" id="form-proyecto-action" class="modal-form">

                    <%-- Campo oculto para el ID EXISTENTE (se usa para UPDATE) --%>
                    <input type="hidden" id="id-proyecto-existente" name="id_proyecto_existente" value="0">

                    <label for="nombre-proyecto" class="modal-label">Nombre del Proyecto</label>
                    <input type="text" id="nombre-proyecto" name="nombre_proyecto" placeholder="Ej: Instalación RUC 12345678" required>

                    <%-- SELECT DE PROFORMA --%>
                    <label for="id-proforma" class="modal-label">Proforma Asociada (Solo 'PAGADA_PARCIAL')</label>
                    <select id="id-proforma" name="id_proforma" required>
                        <option value="" disabled selected>Seleccione Proforma</option>
                        <% if (proformasDisponibles != null && !proformasDisponibles.isEmpty()) {
                                for (Proforma proforma : proformasDisponibles) {%>
                        <option value="<%= proforma.getIdProforma()%>">
                            ID: <%= proforma.getIdProforma()%> | Total: S/<%= proforma.getTotal()%>
                        </option>
                        <% }
                            } else { %>
                        <option value="" disabled>No hay proformas en estado 'PAGADA_PARCIAL'</option>
                        <% } %>
                    </select>

                    <%-- SOLUCIÓN AL ERROR DE EDICIÓN: CAMPO OCULTO PARA MANTENER EL VALOR DE LA PROFORMA --%>
                    <input type="hidden" id="id-proforma-hidden" name="id_proforma_hidden">


                    <label for="fecha-inicio" class="modal-label">Fecha de Inicio:</label>
                    <input type="date" id="fecha-inicio" name="fecha_inicio" required>

                    <label for="fecha-fin-estimada" class="modal-label">Fecha de Fin Estimada:</label>
                    <input type="date" id="fecha-fin-estimada" name="fecha_fin_estimada" required>

                    <%-- SELECT DE PERSONAL ENCARGADO --%>
                    <label for="dni-encargado" class="modal-label">Personal Encargado:</label>
                    <select id="dni-encargado" name="dni_encargado" required>
                        <option value="" disabled selected>Seleccione Personal Encargado</option>
                        <% if (personalDisponible != null && !personalDisponible.isEmpty()) {
                                for (Empleado empleado : personalDisponible) {%>
                        <option value="<%= empleado.getDni()%>">
                            <%= empleado.getNombre()%> <%= empleado.getApellido()%> (DNI: <%= empleado.getDni()%>)
                        </option>
                        <% }
                            } else { %>
                        <option value="" disabled>No hay personal disponible</option>
                        <% }%>
                    </select>

                    <%-- CAMPO DE ESTADO (Visible solo en EDITAR) --%>
                    <div id="estado-group" style="display:none;">
                        <label for="estado-proyecto" class="modal-label">Estado del Proyecto:</label>
                        <select id="estado-proyecto" name="estado_proyecto">
                            <option value="Planificacion">Planificación</option>
                            <option value="Instalacion">Instalación</option>
                            <option value="Pruebas">Pruebas</option>
                            <option default>Entrega</option>
                        </select>
                    </div>

                    <button type="submit" id="btn-submit-action" class="btn-agregar">Crear Proyecto</button>
                </form>

            </div>
        </div>


        <script>
            document.addEventListener('DOMContentLoaded', () => {

                const modal = document.getElementById('modal-proyecto');
                const form = document.getElementById('form-proyecto-action');
                const tituloModal = document.getElementById('modal-title-action');
                const btnSubmit = document.getElementById('btn-submit-action');
                const estadoGroup = document.getElementById('estado-group');

                // Campos del modal
                const inputIdExistente = document.getElementById('id-proyecto-existente');
                const selectProforma = document.getElementById('id-proforma');
                const inputProformaHidden = document.getElementById('id-proforma-hidden'); // Nuevo campo oculto
                const inputNombre = document.getElementById('nombre-proyecto');
                const inputInicio = document.getElementById('fecha-inicio');
                const inputFin = document.getElementById('fecha-fin-estimada');
                const selectEncargado = document.getElementById('dni-encargado');
                const selectEstado = document.getElementById('estado-proyecto');

                // Función auxiliar para formatear la fecha para el input type="date" (YYYY-MM-DD)
                function formatDate(dateString) {
                    if (!dateString)
                        return '';
                    // El formato de la DB es YYYY-MM-DD HH:MM:SS, solo tomamos la parte de la fecha
                    return dateString.split(' ')[0];
                }

                // --- ABRIR MODAL PARA AGREGAR ---
                document.getElementById('btn-agregar-proyecto').addEventListener('click', () => {
                    form.reset();

                    // Configuración para INSERT
                    inputIdExistente.value = '0'; // Indica INSERT

                    tituloModal.textContent = 'Crear Nuevo Proyecto';
                    btnSubmit.textContent = 'Crear Proyecto';

                    estadoGroup.style.display = 'none'; // Ocultar campo de estado en creación
                    selectEstado.removeAttribute('required'); // No es requerido en creación

                    // Habilitar y requerir Proforma
                    selectProforma.disabled = false;
                    selectProforma.setAttribute('required', 'required');
                    selectProforma.name = 'id_proforma'; // Aseguramos que el SELECT se envíe con este nombre
                    selectProforma.value = ""; // Asegurar que el placeholder esté seleccionado al crear

                    // Desactivar el campo oculto para que no interfiera en la inserción
                    inputProformaHidden.name = 'id_proforma_hidden_temp';

                    modal.style.display = 'flex';
                });

                // --- ABRIR MODAL PARA EDITAR ---
                document.querySelectorAll('.edit-btn-proyecto').forEach(button => {
                    button.addEventListener('click', e => {
                        e.preventDefault();

                        // 1. Cargar datos desde los data-atributos
                        inputIdExistente.value = button.getAttribute('data-id');
                        inputNombre.value = button.getAttribute('data-nombre');
                        inputInicio.value = formatDate(button.getAttribute('data-inicio'));
                        inputFin.value = formatDate(button.getAttribute('data-fin'));

                        // 2. Seleccionar Proforma y ENVIARLA POR CAMPO OCULTO
                        const proformaId = button.getAttribute('data-proforma');
                        selectProforma.value = proformaId;
                        selectProforma.disabled = true; // No se debe cambiar la proforma asociada
                        selectProforma.removeAttribute('required');
                        selectProforma.name = 'id_proforma_disabled'; // Cambiamos el nombre para que no se envíe

                        // SOLUCIÓN: Usamos el campo oculto para enviar el valor de la proforma
                        inputProformaHidden.name = 'id_proforma'; // El Servlet espera este nombre
                        inputProformaHidden.value = proformaId;


                        // 3. Seleccionar Encargado y Estado
                        selectEncargado.value = button.getAttribute('data-dni');
                        selectEstado.value = button.getAttribute('data-estado');

                        // 4. Ajustar el título y el botón
                        tituloModal.textContent = 'Editar Proyecto ID: ' + button.getAttribute('data-id');
                        btnSubmit.textContent = 'Guardar Cambios';

                        // 5. Mostrar campo de estado en edición
                        estadoGroup.style.display = 'block';
                        selectEstado.setAttribute('required', 'required');


                        modal.style.display = 'flex';
                    });
                });

                // --- Lógica de cierre de modal ---
                modal.querySelector('.close-btn').addEventListener('click', () => {
                    modal.style.display = 'none';
                });

                window.addEventListener('click', event => {
                    if (event.target === modal)
                        modal.style.display = 'none';
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