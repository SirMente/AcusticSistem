package controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.DAO.ProyectoDAO;
import models.Proyecto;
import models.Proforma;
import models.Empleado; // CORREGIDO: Cambiado de Personal a Empleado
import utils.ConexionBD;

import java.io.IOException;
import java.sql.Date;
import java.util.List;

@WebServlet(name = "ProyectoController", urlPatterns = {"/Proyectos"})
public class ProyectoController extends HttpServlet {

    private ProyectoDAO proyectoDAO = new ProyectoDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String operacion = request.getParameter("operacion");
        
        if (operacion != null && operacion.equals("eliminar")) {
            eliminarProyecto(request, response);
        } else {
            // Cargar datos para la vista principal
            cargarVista(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Es importante establecer la codificación para recibir correctamente los acentos y caracteres especiales
        request.setCharacterEncoding("UTF-8");
        
        String idProyectoExistente = request.getParameter("id_proyecto_existente");
        
        if (idProyectoExistente == null || idProyectoExistente.isEmpty() || idProyectoExistente.equals("0")) {
            agregarProyecto(request, response);
        } else {
            actualizarProyecto(request, response);
        }
    }
    
    private void cargarVista(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 1. Obtener lista de proyectos para mostrar en las tarjetas
        List<Proyecto> listaProyectos = proyectoDAO.listarProyectos();
        request.setAttribute("listaProyectos", listaProyectos);

        // 2. Obtener proformas en estado 'PAGADA_PARCIAL' para el SELECT del formulario de creación
        List<Proforma> proformasDisponibles = proyectoDAO.listarProformasDisponibles();
        request.setAttribute("proformasDisponibles", proformasDisponibles);
        
        // 3. Obtener el personal disponible para el SELECT del encargado
        List<Empleado> personalDisponible = proyectoDAO.listarPersonal(); // CORREGIDO: Uso de Empleado
        request.setAttribute("personalDisponible", personalDisponible);

        request.getRequestDispatcher("/views/GestionProyectos.jsp").forward(request, response);
    }
    
    private void agregarProyecto(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            String idProforma = request.getParameter("id_proforma"); // Cambiado a String
            String nombreProyecto = request.getParameter("nombre_proyecto");
            Date fechaInicio = Date.valueOf(request.getParameter("fecha_inicio"));
            Date fechaFinEstimada = Date.valueOf(request.getParameter("fecha_fin_estimada"));
            long dniEncargado = Long.parseLong(request.getParameter("dni_encargado"));
            
            // El estado se inicializa como 'Planificacion' en el modelo/DAO
            Proyecto nuevoProyecto = new Proyecto(idProforma, nombreProyecto, fechaInicio, fechaFinEstimada, dniEncargado);
            
            if (proyectoDAO.agregarProyecto(nuevoProyecto)) {
                response.sendRedirect(request.getContextPath() + "/Proyectos?mensaje=agregado_exitoso");
            } else {
                request.setAttribute("error", "Error de BD al crear el proyecto.");
                cargarVista(request, response);
            }
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Datos numéricos o de fecha inválidos (DNI/Fechas).");
            cargarVista(request, response);
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", "Formato de fecha incorrecto.");
            cargarVista(request, response);
        }
    }
    
    private void actualizarProyecto(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            int idProyecto = Integer.parseInt(request.getParameter("id_proyecto_existente"));
            String idProforma = request.getParameter("id_proforma"); // Cambiado a String
            String nombreProyecto = request.getParameter("nombre_proyecto");
            Date fechaInicio = Date.valueOf(request.getParameter("fecha_inicio"));
            Date fechaFinEstimada = Date.valueOf(request.getParameter("fecha_fin_estimada"));
            String estado = request.getParameter("estado_proyecto"); // Nuevo campo para editar el estado
            long dniEncargado = Long.parseLong(request.getParameter("dni_encargado"));
            
            Proyecto proyecto = new Proyecto();
            proyecto.setIdProyecto(idProyecto);
            proyecto.setIdProforma(idProforma);
            proyecto.setNombreProyecto(nombreProyecto);
            proyecto.setFechaInicio(fechaInicio);
            proyecto.setFechaFinEstimada(fechaFinEstimada);
            proyecto.setEstado(estado);
            proyecto.setDniEncargado(dniEncargado);
            
            if (proyectoDAO.actualizarProyecto(proyecto)) {
                response.sendRedirect(request.getContextPath() + "/Proyectos?mensaje=editado_exitoso");
            } else {
                request.setAttribute("error", "Error de BD al actualizar el proyecto.");
                cargarVista(request, response);
            }
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Datos numéricos o de fecha inválidos en la edición (ID Proyecto/DNI/Fechas).");
            cargarVista(request, response);
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", "Formato de fecha incorrecto en la edición.");
            cargarVista(request, response);
        }
    }
    
    private void eliminarProyecto(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            int idProyecto = Integer.parseInt(request.getParameter("id"));
            
            if (proyectoDAO.eliminarProyecto(idProyecto)) {
                response.sendRedirect(request.getContextPath() + "/Proyectos?mensaje=eliminado_exitoso");
            } else {
                response.sendRedirect(request.getContextPath() + "/Proyectos?error=bd_eliminar");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/Proyectos?error=id_invalido");
        }
    }
}