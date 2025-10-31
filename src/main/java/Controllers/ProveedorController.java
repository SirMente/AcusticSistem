package Controllers;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Proveedor;
import models.DAO.ProveedorDAO;

@WebServlet("/Proveedores")
public class ProveedorController extends HttpServlet {

    private ProveedorDAO proveedorDAO;

    @Override
    public void init() throws ServletException {
        this.proveedorDAO = new ProveedorDAO();
    }

    // --- MANEJO DE LA VISTA (LISTAR y ELIMINAR) ---
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String accion = request.getParameter("accion");

        if ("eliminar".equals(accion)) {
            // Manejo de la acción de ELIMINAR
            eliminarProveedor(request, response);
        } else {
            // Lógica de LISTAR (predeterminada)
            try {
                List<Proveedor> listaProveedores = proveedorDAO.listarProveedores();
                request.setAttribute("listaProveedores", listaProveedores);
                
                request.getRequestDispatcher("WEB-INF/views/proveedores.jsp").forward(request, response);
                
            } catch (Exception e) {
                System.err.println("Error FATAL al cargar la vista de proveedores: " + e.getMessage());
                e.printStackTrace(); 
                
                request.setAttribute("error", "Error al cargar los proveedores: " + e.getMessage());
                request.getRequestDispatcher("WEB-INF/views/proveedores.jsp").forward(request, response);
            }
        }
    }

    // --- Método Auxiliar para ELIMINAR ---
    private void eliminarProveedor(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        
        String idParam = request.getParameter("id");
        int idProveedor = -1;
        String mensaje = "error";

        try {
            idProveedor = Integer.parseInt(idParam);
            boolean exito = proveedorDAO.eliminarProveedor(idProveedor);
            
            if (exito) {
                mensaje = "eliminado_exitoso";
            } else {
                mensaje = "bd_eliminar";
            }
            
        } catch (NumberFormatException e) {
            mensaje = "id_invalido";
        } catch (Exception e) {
            System.err.println("Error al eliminar proveedor: " + e.getMessage());
            mensaje = "bd_eliminar";
        }
        
        // Redireccionar al GET principal con mensaje de estado
        response.sendRedirect(request.getContextPath() + "/Proveedores?mensaje=" + mensaje);
    }

    // --- MANEJO DE LA INSERCIÓN y EDICIÓN (AGREGAR/ACTUALIZAR) ---
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        
        try {
            // 1. Obtener parámetros y el ID de acción
            String idProveedorParam = request.getParameter("id_proveedor");
            String nombre = request.getParameter("nombre");
            String email = request.getParameter("email");
            String telefono = request.getParameter("telefono");
            String tipoProducto = request.getParameter("tipo_servicio");
            
            int idProveedor = (idProveedorParam != null && !idProveedorParam.isEmpty()) ? Integer.parseInt(idProveedorParam) : 0;

            Proveedor proveedor = new Proveedor(nombre, email, telefono, tipoProducto);
            proveedor.setIdProveedor(idProveedor); // Asignar ID para la actualización

            boolean exito = false;
            String mensajeExito = "";

            if (idProveedor == 0) {
                // Lógica de AGREGAR
                exito = proveedorDAO.agregarProveedor(proveedor);
                mensajeExito = "agregado_exitoso";
            } else {
                // Lógica de ACTUALIZAR
                exito = proveedorDAO.actualizarProveedor(proveedor);
                mensajeExito = "editado_exitoso";
            }
            
            if (exito) {
                // Redirigir al GET para mostrar la lista actualizada
                response.sendRedirect(request.getContextPath() + "/Proveedores?mensaje=" + mensajeExito);
            } else {
                 request.setAttribute("error", "Error de BD al guardar o actualizar el proveedor. Ninguna fila fue afectada.");
                 doGet(request, response); 
            }

        } catch (NumberFormatException e) {
             request.setAttribute("error", "Error de formato de ID. Verifique los datos enviados.");
             doGet(request, response);
        } catch (Exception e) {
            System.err.println("Error SQL al intentar AGREGAR/ACTUALIZAR proveedor: " + e.getMessage());
            e.printStackTrace();
            
            request.setAttribute("error", "Error interno al guardar el proveedor. Causa: " + e.getMessage());
            doGet(request, response);
        }
    }
}