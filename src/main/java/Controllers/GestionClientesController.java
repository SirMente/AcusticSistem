package controllers;

import models.DAO.ClienteDAO;
import models.Cliente;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.SQLException;

@WebServlet("/GestionClientes")
public class GestionClientesController extends HttpServlet {

    private ClienteDAO clienteDAO = new ClienteDAO();

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

        if ("eliminar".equals(operacion)) {
            eliminarCliente(request, response);
            return;
        }

        // Si no hay acción → listar
        listarClientes(request, response);
    }

    private void listarClientes(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<Cliente> listaClientes = clienteDAO.obtenerTodosLosClientes();
            request.setAttribute("listaClientes", listaClientes);
            request.getRequestDispatcher("views/gestionClientes.jsp").forward(request, response);

        } catch (SQLException e) {
            System.err.println("Error SQL en listarClientes: " + e.getMessage());
            request.setAttribute("error", "Error al cargar los clientes: " + e.getMessage());
            request.getRequestDispatcher("views/gestionClientes.jsp").forward(request, response);
        }
    }

    private void eliminarCliente(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Se usa 'dni' en la URL, que internamente se mapea a 'docu' en el DAO
        String dniParam = request.getParameter("dni");

        if (dniParam != null) {
            try {
                // LIMPIEZA: Eliminar espacios en blanco
                String dniLimpio = dniParam.trim();

                // CONVERSIÓN
                long idCliente = Long.parseLong(dniLimpio);

                clienteDAO.eliminarCliente(idCliente);

                response.sendRedirect(request.getContextPath() + "/GestionClientes?mensaje=eliminado_exitoso");
                return;

            } catch (NumberFormatException e) {
                response.sendRedirect(request.getContextPath() + "/GestionClientes?error=dni_invalido");
                return;

            } catch (SQLException e) {
                System.err.println("Error SQL al eliminar: " + e.getMessage());
                response.sendRedirect(request.getContextPath() + "/GestionClientes?error=bd_eliminar");
                return;
            }
        }

        response.sendRedirect(request.getContextPath() + "/GestionClientes");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (request.getSession().getAttribute("usuarioLogueado") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // --- DATOS DEL FORMULARIO ---
        // dni_cliente es el docu del cliente a EDITAR (si aplica)
        String docuParam = request.getParameter("docu_cliente");
        String nombre = request.getParameter("nombre");
        String direccion = request.getParameter("direccion");
        String telefono = request.getParameter("telefono");
        String email = request.getParameter("email");

        // Validación
        if (nombre == null || nombre.trim().isEmpty()
                || email == null || email.trim().isEmpty()) {

            request.setAttribute("error", "El nombre y el email son obligatorios.");
            listarClientes(request, response);
            return;
        }

        try {
            long docuExistente = 0;
            String docuParamLimpio = request.getParameter("docu_cliente") != null
                    ? request.getParameter("docu_cliente").trim() : null;

            // --- Lógica de Edición (UPDATE) ---
            if (docuParamLimpio != null && !docuParamLimpio.isEmpty()) {
                docuExistente = Long.parseLong(docuParamLimpio);
            }

            if (docuExistente > 0) {
                Cliente clienteEdit = new Cliente(docuExistente, nombre, direccion, telefono, email);
                clienteDAO.editarCliente(clienteEdit);

                response.sendRedirect(request.getContextPath() + "/GestionClientes?mensaje=editado_exitoso");
                return;
            }

            // --- Lógica de Inserción (INSERT) ---
            // Obtener y limpiar el DNI/RUC para un nuevo registro
            String docuNuevoStr = request.getParameter("docu");
            if (docuNuevoStr == null || docuNuevoStr.trim().isEmpty()) {
                throw new NumberFormatException("El número de documento no puede estar vacío.");
            }
            long nuevoDocu = Long.parseLong(docuNuevoStr.trim()); // Limpieza

            Cliente nuevoCliente = new Cliente(nuevoDocu, nombre, direccion, telefono, email);
            clienteDAO.agregarCliente(nuevoCliente);

            response.sendRedirect(request.getContextPath() + "/GestionClientes?mensaje=agregado_exitoso");

        } catch (NumberFormatException e) {
            // Si falla el parseo (ej. RUC tiene un espacio)
            request.setAttribute("error", "Formato de documento inválido: Asegúrese de que es solo numérico y tiene el largo correcto.");
            listarClientes(request, response);

        } catch (SQLException e) {
            System.err.println("Error SQL en doPost: " + e.getMessage());
            request.setAttribute("error", "Error de BD al guardar/actualizar: " + e.getMessage());
            listarClientes(request, response);
        }
    }
}
