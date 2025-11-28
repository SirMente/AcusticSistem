package controllers;

import models.DAO.ServicioDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Servicio;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

/**
 * Servlet para manejar las peticiones CRUD de la entidad Servicio.
 */
@WebServlet("/ServicioController") // <--- URL CLAVE
public class ServicioController extends HttpServlet {

    private ServicioDAO servicioDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        // Inicializa el DAO (ajusta si necesitas pasar la conexión de otra forma)
        this.servicioDAO = new ServicioDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Por defecto, la acción es listar
        String accion = request.getParameter("accion");
        if (accion == null) {
            accion = "listar";
        }

        switch (accion) {
            case "listar":
                listarServicios(request, response);
                break;
            default:
                listarServicios(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Maneja las acciones de agregar, actualizar, eliminar
        String accion = request.getParameter("accion");

        if ("agregar".equals(accion)) {
            agregarServicio(request, response);
        } else if ("actualizar".equals(accion)) {
            actualizarServicio(request, response);
        } else if ("eliminar".equals(accion)) {
            eliminarServicio(request, response);
        } else {
            // Si la acción no es reconocida o no se envía, listar
            listarServicios(request, response);
        }
    }

    // --- Lógica de la aplicación ---
    private void listarServicios(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Servicio> listaServicios = servicioDAO.listarServicios();

        // Pasa la lista al JSP
        request.setAttribute("listaServicios", listaServicios);

        // Redirige al JSP de servicios
        request.getRequestDispatcher("views/servicios.jsp").forward(request, response);
    }

    private void agregarServicio(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // 1. Obtener parámetros del formulario
            String nombre = request.getParameter("nombre");
            String descripcion = request.getParameter("descripcion");
            BigDecimal tarifaBase = new BigDecimal(request.getParameter("tarifa_base"));

            // 2. Crear objeto Servicio
            Servicio nuevoServicio = new Servicio(0, nombre, descripcion, tarifaBase);

            // 3. Insertar en la base de datos
            boolean exito = servicioDAO.agregarServicio(nuevoServicio);

            if (exito) {
                request.setAttribute("mensaje", "Servicio agregado exitosamente.");
            } else {
                request.setAttribute("error", "Error al agregar el servicio.");
            }

        } catch (NumberFormatException e) {
            request.setAttribute("error", "Error de formato en la tarifa base.");
            e.printStackTrace();
        }

        // Vuelve a listar para mostrar el resultado y la tabla actualizada
        listarServicios(request, response);
    }

    private void actualizarServicio(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // 1. Obtener parámetros (incluyendo el ID)
            int id = Integer.parseInt(request.getParameter("id_servicio"));
            String nombre = request.getParameter("nombre");
            String descripcion = request.getParameter("descripcion");
            BigDecimal tarifaBase = new BigDecimal(request.getParameter("tarifa_base"));

            // 2. Crear objeto Servicio
            Servicio servicioAActualizar = new Servicio(id, nombre, descripcion, tarifaBase);

            // 3. Actualizar en la base de datos
            boolean exito = servicioDAO.actualizarServicio(servicioAActualizar);

            if (exito) {
                request.setAttribute("mensaje", "Servicio actualizado exitosamente.");
            } else {
                request.setAttribute("error", "Error al actualizar el servicio.");
            }

        } catch (NumberFormatException e) {
            request.setAttribute("error", "Error de formato en ID o tarifa base.");
            e.printStackTrace();
        }

        // Vuelve a listar para mostrar el resultado y la tabla actualizada
        listarServicios(request, response);
    }

    private void eliminarServicio(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int id = Integer.parseInt(request.getParameter("id_servicio"));

            boolean exito = servicioDAO.eliminarServicio(id);

            if (exito) {
                request.setAttribute("mensaje", "Servicio eliminado exitosamente.");
            } else {
                request.setAttribute("error", "Error al eliminar el servicio.");
            }

        } catch (NumberFormatException e) {
            request.setAttribute("error", "ID de servicio inválido para eliminar.");
            e.printStackTrace();
        }

        // Vuelve a listar para mostrar el resultado y la tabla actualizada
        listarServicios(request, response);
    }
}
