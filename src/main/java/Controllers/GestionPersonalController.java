package controllers;

import models.DAO.EmpleadoDAO;
import models.Empleado;

import java.io.IOException;
import java.util.List;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/GestionPersonal")
public class GestionPersonalController extends HttpServlet {

    private EmpleadoDAO empleadoDAO = new EmpleadoDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // --- PROTEGER RUTA ---
        if (request.getSession().getAttribute("usuarioLogueado") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // --- ACCIONES GET ---
        String operacion = request.getParameter("operacion");
        String dniParam = request.getParameter("dni");

        if ("eliminar".equals(operacion) && dniParam != null) {
            eliminarEmpleado(request, response, dniParam);
            return;
        }

        // Si no hay acción específica → listar
        listarEmpleados(request, response);
    }

    /**
     * Obtiene la lista de empleados y la reenvía a la vista JSP.
     */
    private void listarEmpleados(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<Empleado> listaEmpleados = empleadoDAO.obtenerTodosLosEmpleados();
            request.setAttribute("listaPersonal", listaEmpleados);
            request.getRequestDispatcher("views/gestionPersonal.jsp").forward(request, response);

        } catch (SQLException e) {
            System.err.println("Error SQL en listarEmpleados: " + e.getMessage());
            request.setAttribute("error", "Error al cargar el personal: " + e.getMessage());
            request.getRequestDispatcher("views/gestionPersonal.jsp").forward(request, response);
        }
    }

    /**
     * Procesa la eliminación de un empleado.
     */
    private void eliminarEmpleado(HttpServletRequest request, HttpServletResponse response, String dniParam)
            throws ServletException, IOException {

        try {
            long dniEmpleado = Long.parseLong(dniParam);
            empleadoDAO.eliminarEmpleado(dniEmpleado);

            response.sendRedirect(request.getContextPath() + "/GestionPersonal?mensaje=eliminado_exitoso");
            return;

        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/GestionPersonal?error=dni_invalido");
            return;

        } catch (SQLException e) {
            System.err.println("Error SQL al eliminar empleado: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/GestionPersonal?error=bd_eliminar");
            return;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // --- PROTECCIÓN DE RUTA ---
        if (request.getSession().getAttribute("usuarioLogueado") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // --- Extracción de Parámetros ---
        // dni_existente es el DNI del empleado a EDITAR (viene de un campo oculto)
        String dniExistenteParam = request.getParameter("dni_existente");

        // dni_nuevo es el DNI para un empleado NUEVO (viene de un input visible)
        String dniNuevoParam = request.getParameter("dni_nuevo");

        String nombre = request.getParameter("nombre");
        String apellido = request.getParameter("apellido");
        String telefono = request.getParameter("telefono");
        String email = request.getParameter("email");
        String puesto = request.getParameter("puesto");

        // Determinar si es una operación de edición (UPDATE) o inserción (INSERT)
        boolean esEdicion = dniExistenteParam != null && !dniExistenteParam.trim().isEmpty()
                && !dniExistenteParam.equals("0"); // Uso '0' como valor por defecto/indicador de nuevo

        // Validación de campos obligatorios
        if (nombre == null || nombre.trim().isEmpty()
                || email == null || email.trim().isEmpty()
                || puesto == null || puesto.trim().isEmpty()) {

            request.setAttribute("error", "El Nombre, Email y Puesto son obligatorios.");
            listarEmpleados(request, response);
            return;
        }

        try {
            long dniFinal;

            if (esEdicion) {
                // --- UPDATE ---
                dniFinal = Long.parseLong(dniExistenteParam);
                Empleado empleadoEditado = new Empleado(dniFinal, nombre, apellido, telefono, email, puesto);
                empleadoDAO.editarEmpleado(empleadoEditado);

                response.sendRedirect(request.getContextPath() + "/GestionPersonal?mensaje=editado_exitoso");
                return;

            } else {
                // --- INSERT ---
                if (dniNuevoParam == null || dniNuevoParam.trim().isEmpty()) {
                    request.setAttribute("error", "El DNI es obligatorio para un nuevo empleado.");
                    listarEmpleados(request, response);
                    return;
                }

                dniFinal = Long.parseLong(dniNuevoParam);

                // Constructor: dni, nombre, apellido, telefono, email, puesto
                Empleado nuevoEmpleado = new Empleado(dniFinal, nombre, apellido, telefono, email, puesto);
                empleadoDAO.agregarEmpleado(nuevoEmpleado);

                response.sendRedirect(request.getContextPath() + "/GestionPersonal?mensaje=agregado_exitoso");
                return;
            }

        } catch (NumberFormatException e) {
            request.setAttribute("error", "Formato de DNI inválido. Asegúrate de ingresar solo números.");
            listarEmpleados(request, response);

        } catch (SQLException e) {
            System.err.println("Error SQL en doPost Personal: " + e.getMessage());
            String msg = e.getMessage().contains("Duplicate entry")
                    ? "Error: Ya existe un empleado con ese DNI o Email."
                    : "Error de Base de Datos al guardar/actualizar: " + e.getMessage();

            request.setAttribute("error", msg);
            listarEmpleados(request, response);
        }
    }
}
