package Controllers;

 // Asegúrate de que este sea el path correcto
import models.Producto;
import models.DAO.ProductoDAO; // Asegúrate de que este sea el path correcto
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/InventarioController")
public class InventarioController extends HttpServlet {
    
    // Instancia del DAO para interactuar con la DB
    private ProductoDAO productoDAO = new ProductoDAO(); 
    
    // --- DO GET: Mostrar la lista de productos ---
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            // 1. Obtener la lista de productos
            List<Producto> productos = productoDAO.listarProductos();
            
            // 2. Adjuntar la lista al objeto request para que el JSP la muestre
            request.setAttribute("productos", productos);
            
            // 3. Reenviar (forward) a la vista JSP
            request.getRequestDispatcher("WEB-INF/views/inventario.jsp").forward(request, response);
            
        } catch (SQLException e) {
            System.err.println("Error de DB al listar inventario: " + e.getMessage());
            request.setAttribute("error", "Error al cargar el inventario: " + e.getMessage());
            request.getRequestDispatcher("WEB-INF/views/inventario.jsp").forward(request, response);
        }
    }

    // --- DO POST: Manejar acciones (Agregar, Editar, Eliminar) ---
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // 🔑 Obtener la acción del campo oculto 'action' del formulario
        String action = request.getParameter("action");
        
        if (action == null) {
            response.sendRedirect(request.getContextPath() + "/InventarioController");
            return;
        }

        switch (action) {
            case "agregar":
                agregarOActualizarProducto(request, response, "agregar");
                break;
            case "editar":
                agregarOActualizarProducto(request, response, "editar");
                break;
            case "eliminar":
                eliminarProducto(request, response);
                break;
            default:
                // Acción desconocida
                response.sendRedirect(request.getContextPath() + "/InventarioController");
                break;
        }
    }
    
    // --- Lógica Central para Agregar o Actualizar Producto ---
    private void agregarOActualizarProducto(HttpServletRequest request, HttpServletResponse response, String tipoAccion)
            throws IOException {
        
        // 1. Obtener parámetros del formulario
        String idStr = request.getParameter("id");
        String nombre = request.getParameter("nombre");
        String descripcion = request.getParameter("descripcion");
        String cantidadStr = request.getParameter("cantidad");
        String precioStr = request.getParameter("precio");
        String proveedor = request.getParameter("proveedor");
        String imagenUrl = request.getParameter("imagenUrl"); // 👈 Campo de la imagen

        try {
            // 2. Conversión de tipos
            int id = (idStr != null && !idStr.isEmpty() && !idStr.equals("0")) ? Integer.parseInt(idStr) : 0;
            int cantidad = Integer.parseInt(cantidadStr);
            double precio = Double.parseDouble(precioStr);
            
            // 3. Crear y configurar el objeto Producto
            Producto producto = new Producto();
            producto.setId(id);
            producto.setNombre(nombre);
            producto.setDescripcion(descripcion);
            producto.setCantidad(cantidad);
            producto.setPrecioUnitario(precio);
            producto.setProveedor(proveedor);
            producto.setImagenUrl(imagenUrl);
            
            // 4. Llamar al DAO
            boolean exito;
            if (tipoAccion.equals("agregar") || id == 0) {
                exito = productoDAO.agregarProducto(producto);
            } else {
                exito = productoDAO.actualizarProducto(producto);
            }
            
            // 5. Redirección (POST-Redirect-GET)
            if (exito) {
                String mensaje = (tipoAccion.equals("agregar") ? "agregado" : "actualizado");
                response.sendRedirect(request.getContextPath() + "/InventarioController?status=success&msg=Producto+" + mensaje + "+exitosamente.");
            } else {
                response.sendRedirect(request.getContextPath() + "/InventarioController?status=error&msg=Fallo+la+operación+de+" + tipoAccion + "+en+la+Base+de+Datos.");
            }
            
        } catch (NumberFormatException e) {
            System.err.println("Error de formato de número o dato faltante: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/InventarioController?status=error&msg=Datos+numéricos+inválidos+o+faltantes.");
        } catch (SQLException e) {
            System.err.println("Error de Base de Datos en " + tipoAccion + ": " + e.getMessage());
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/InventarioController?status=error&msg=Error+de+Base+de+Datos+al+" + tipoAccion);
        }
    }
    
    // --- Lógica para Eliminar Producto ---
    private void eliminarProducto(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        
        String idStr = request.getParameter("id");
        
        try {
            int id = Integer.parseInt(idStr);
            boolean exito = productoDAO.eliminarProducto(id);
            
            if (exito) {
                response.sendRedirect(request.getContextPath() + "/InventarioController?status=success&msg=Producto+eliminado+exitosamente.");
            } else {
                response.sendRedirect(request.getContextPath() + "/InventarioController?status=error&msg=No+se+pudo+eliminar+el+producto+con+ID:" + id);
            }
            
        } catch (NumberFormatException | SQLException e) {
            System.err.println("Error al eliminar producto: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/InventarioController?status=error&msg=Error+interno+al+eliminar+el+producto.");
        }
    }
}