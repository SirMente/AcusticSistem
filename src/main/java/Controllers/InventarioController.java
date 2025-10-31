package Controllers;

// ... (imports) ...
import com.acustica.modelos.Producto;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/InventarioController")
public class InventarioController extends HttpServlet {
    private List<Producto> listaProductos = new ArrayList<>();
    private int nextId = 1;

    @Override
    public void init() throws ServletException {
        // Inicializar con datos de prueba ACTUALIZADOS
        listaProductos.add(new Producto(nextId++, "Cable HDMI Pro", "Cable de alta velocidad y 5 metros", 50, 15.50, "Proveedor A"));
        listaProductos.add(new Producto(nextId++, "Micrófono Condensador XM", "Micrófono de estudio de diafragma grande", 15, 120.00, "Audio Corp"));
        listaProductos.add(new Producto(nextId++, "Panel Acústico Difusor", "Panel de madera para control de ecos", 30, 45.99, "Acustik Labs"));
    }

    // ... (doGet se mantiene igual, ya que solo lista) ...

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8"); // Asegurar codificación
        String action = request.getParameter("action");

        if ("eliminar".equals(action)) {
            // Lógica de ELIMINAR (se mantiene igual)
            try {
                int idAEliminar = Integer.parseInt(request.getParameter("id"));
                listaProductos.removeIf(p -> p.getId() == idAEliminar);
            } catch (NumberFormatException e) {}
        } else {
            // --- Lógica de AGREGAR (ACTUALIZADA) ---
            String nombre = request.getParameter("nombre");
            String descripcion = request.getParameter("descripcion"); // 🔑 Nuevo Parámetro
            
            // Usamos un try/catch para convertir los números
            try {
                int cantidad = Integer.parseInt(request.getParameter("cantidad"));
                // El campo del formulario se llama 'precio' o 'precioUnitario'
                double precioUnitario = Double.parseDouble(request.getParameter("precio")); 
                String proveedor = request.getParameter("proveedor");
            
                // Crear producto con el constructor actualizado
                Producto nuevoProducto = new Producto(nextId++, nombre, descripcion, cantidad, precioUnitario, proveedor);
                listaProductos.add(nuevoProducto);
            } catch (NumberFormatException e) {
                 // Manejar error si la cantidad o el precio no son números válidos
            }
        }

        response.sendRedirect(request.getContextPath() + "/InventarioController");
    }
}