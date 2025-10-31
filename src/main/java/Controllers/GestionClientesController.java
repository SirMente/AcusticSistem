package Controllers;

import models.DAO.ClienteDAO;
import models.Cliente;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException; 

@WebServlet("/GestionClientes")
public class GestionClientesController extends HttpServlet {

    private ClienteDAO clienteDAO = new ClienteDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // --- 1. PROTEGER RUTA ---
        if (request.getSession().getAttribute("usuarioLogueado") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // --- 2. MANEJAR ACCIONES GET (Eliminar) ---
        String operacion = request.getParameter("operacion");
        
        if ("eliminar".equals(operacion)) {
            eliminarCliente(request, response);
            return;
        }
        
        // Si no hay acción específica, simplemente LISTAR
        listarClientes(request, response);
    }
    
    private void listarClientes(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        try {
            List<Cliente> listaClientes = clienteDAO.obtenerTodosLosClientes();
            request.setAttribute("listaClientes", listaClientes);
            request.getRequestDispatcher("WEB-INF/views/gestionClientes.jsp").forward(request, response);
        } catch (SQLException e) {
            System.err.println("Error SQL en listarClientes (SELECT): " + e.getMessage());
            request.setAttribute("error", "Error al cargar los clientes: " + e.getMessage());
            request.getRequestDispatcher("WEB-INF/views/gestionClientes.jsp").forward(request, response);
        }
    }
    
    private void eliminarCliente(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String idParam = request.getParameter("id");
        
        if (idParam != null) {
            try {
                int idCliente = Integer.parseInt(idParam);
                clienteDAO.eliminarCliente(idCliente);
                
                response.sendRedirect(request.getContextPath() + "/GestionClientes?mensaje=eliminado_exitoso");
                return;
            } catch (NumberFormatException e) {
                response.sendRedirect(request.getContextPath() + "/GestionClientes?error=id_invalido");
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

        // 🔑 CLAVE: Obtener el ID oculto del formulario
        String idParam = request.getParameter("id_cliente"); 
        String nombre = request.getParameter("nombre");
        String empresa = request.getParameter("empresa");
        String telefono = request.getParameter("telefono");
        String email = request.getParameter("email");
        
        // Validación básica
        if (nombre == null || nombre.trim().isEmpty() || email == null || email.trim().isEmpty()) {
            request.setAttribute("error", "El nombre y el email son campos obligatorios.");
            listarClientes(request, response);
            return;
        }

        try {
            int idCliente = 0;
            if (idParam != null && !idParam.trim().isEmpty()) {
                 // Convertir el ID, si no es válido, lanzará NumberFormatException
                idCliente = Integer.parseInt(idParam); 
            }
            
            // 🔑 LÓGICA CORREGIDA: Si idCliente > 0, es Edición (UPDATE)
            if (idCliente > 0) {
                // ES EDICIÓN
                Cliente clienteEditado = new Cliente(idCliente, nombre, empresa, telefono, email);
                clienteDAO.editarCliente(clienteEditado);
                response.sendRedirect(request.getContextPath() + "/GestionClientes?mensaje=editado_exitoso");
            } else {
                // ES AGREGAR (idCliente es 0)
                Cliente nuevoCliente = new Cliente(nombre, empresa, telefono, email);
                clienteDAO.agregarCliente(nuevoCliente);
                response.sendRedirect(request.getContextPath() + "/GestionClientes?mensaje=agregado_exitoso");
            }
        } catch (NumberFormatException e) {
            // Error si el ID enviado no era un número
            request.setAttribute("error", "Error interno: El ID de cliente enviado no es válido.");
            listarClientes(request, response); 
        } catch (SQLException e) {
            System.err.println("Error SQL en doPost: " + e.getMessage());
            request.setAttribute("error", "Error de la BD al guardar/actualizar: " + e.getMessage());
            listarClientes(request, response); 
        }
    }
}