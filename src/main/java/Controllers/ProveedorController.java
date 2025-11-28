package controllers;

import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.SQLException;

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
            eliminarProveedor(request, response);
        } else {
            // Lógica de LISTAR (predeterminada)
            try {
                // El método listarProveedores del DAO ahora debe retornar List<Proveedor> con long en el modelo
                List<Proveedor> listaProveedores = proveedorDAO.listarProveedores(); 
                request.setAttribute("listaProveedores", listaProveedores);

                request.getRequestDispatcher("views/proveedores.jsp").forward(request, response);

            } catch (SQLException e) { 
                System.err.println("Error FATAL al cargar la vista de proveedores: " + e.getMessage());
                e.printStackTrace();

                request.setAttribute("error", "Error al cargar los proveedores (BD): " + e.getMessage());
                request.getRequestDispatcher("views/proveedores.jsp").forward(request, response);
            }
        }
    }

    // --- Método Auxiliar para ELIMINAR ---
    private void eliminarProveedor(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        // El parámetro de la URL debe ser 'ruc'
        String rucParam = request.getParameter("ruc"); 
        long rucProveedor = -1; // ✅ CAMBIO: De int a long
        String mensaje = "error";

        try {
            // ✅ CAMBIO: De Integer.parseInt a Long.parseLong
            rucProveedor = Long.parseLong(rucParam); 
            // El método eliminarProveedor del DAO ahora debe aceptar long
            boolean exito = proveedorDAO.eliminarProveedor(rucProveedor); 

            if (exito) {
                mensaje = "eliminado_exitoso";
            } else {
                mensaje = "bd_eliminar";
            }

        } catch (NumberFormatException e) {
            // Esto capturará si el RUC es demasiado largo o no es un número válido
            mensaje = "ruc_invalido"; 
        } catch (SQLException e) { 
            System.err.println("Error al eliminar proveedor: " + e.getMessage());
            mensaje = "bd_eliminar";
        }

        response.sendRedirect(request.getContextPath() + "/Proveedores?mensaje=" + mensaje);
    }

    // --- MANEJO DE LA INSERCIÓN y EDICIÓN (AGREGAR/ACTUALIZAR) ---
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        try {
            // 1. Obtener RUC y otros parámetros
            String rucParam = request.getParameter("ruc"); 
            String nombre = request.getParameter("nombre");
            String email = request.getParameter("email");
            String telefono = request.getParameter("telefono");
            String tipoProducto = request.getParameter("tipo_producto"); 
            String direccion = request.getParameter("direccion"); 

            long ruc = 0; // ✅ CAMBIO: De int a long

            if (rucParam != null && !rucParam.isEmpty()) {
                // ✅ CAMBIO: De Integer.parseInt a Long.parseLong
                ruc = Long.parseLong(rucParam); 
            } else {
                throw new NumberFormatException("El RUC es obligatorio.");
            }

            // 2. Crear objeto Proveedor
            // El constructor de Proveedor ahora debe aceptar long para el RUC
            Proveedor proveedor = new Proveedor(ruc, nombre, tipoProducto, email, telefono, direccion);

            boolean exito = false;
            String mensajeExito = "";

            String accionUpdate = request.getParameter("accion_update"); 

            if ("actualizar".equals(accionUpdate)) {
                // Lógica de ACTUALIZAR
                // El DAO.actualizarProveedor debe aceptar Proveedor con long RUC
                exito = proveedorDAO.actualizarProveedor(proveedor);
                mensajeExito = "editado_exitoso";
            } else {
                // Lógica de AGREGAR
                // El DAO.agregarProveedor debe aceptar Proveedor con long RUC
                exito = proveedorDAO.agregarProveedor(proveedor);
                mensajeExito = "agregado_exitoso";
            }

            if (exito) {
                response.sendRedirect(request.getContextPath() + "/Proveedores?mensaje=" + mensajeExito);
            } else {
                request.setAttribute("error", "Error de BD al guardar o actualizar el proveedor. Ninguna fila fue afectada.");
                doGet(request, response);
            }

        } catch (NumberFormatException e) {
            request.setAttribute("error", "Error de formato de RUC. Verifique los datos enviados: " + e.getMessage());
            doGet(request, response);
        } catch (SQLException e) {
            System.err.println("Error SQL al intentar AGREGAR/ACTUALIZAR proveedor: " + e.getMessage());
            e.printStackTrace();

            String errorMsg = e.getMessage();
            if (errorMsg != null && errorMsg.contains("Duplicate entry")) {
                request.setAttribute("error", "Error: El RUC o Email ya existe en la base de datos.");
            } else {
                request.setAttribute("error", "Error interno al guardar el proveedor. Causa: " + e.getMessage());
            }
            doGet(request, response);
        }
    }
}