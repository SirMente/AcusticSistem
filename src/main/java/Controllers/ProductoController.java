package controllers;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import models.Producto;
import models.Proveedor; // << IMPORTAR MODELO PROVEEDOR
import models.DAO.ProductoDAO;
import models.DAO.ProveedorDAO; // << IMPORTAR DAO PROVEEDOR

@WebServlet("/InventarioController")
public class ProductoController extends HttpServlet {

    private ProductoDAO productoDAO;
    private ProveedorDAO proveedorDAO; // << DECLARACIÓN DEL DAO DE PROVEEDOR

    @Override
    public void init() throws ServletException {
        this.productoDAO = new ProductoDAO();
        this.proveedorDAO = new ProveedorDAO(); // << INICIALIZACIÓN
    }

    // --- MANEJO DE LA VISTA (LISTAR, ELIMINAR, ALERTAS) ---
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");

        try {
            if ("eliminar".equals(accion)) {
                eliminarProducto(request, response);
            } else if ("editar".equals(accion)) {
                mostrarFormularioEdicion(request, response);
            } else if ("alertas".equals(accion)) {
                mostrarAlertasStock(request, response);
            } else {
                listarProductos(request, response);
            }
        } catch (SQLException e) {
            // Al ocurrir un error de BD (SQLException), no intentes listar productos de nuevo.
            System.err.println("Error de BD en doGet/Inventario: " + e.getMessage());
            request.setAttribute("error", "Error de base de datos: " + e.getMessage());

            // Simplemente reenvía a la vista
            request.getRequestDispatcher("views/inventario.jsp").forward(request, response);
        }
    }

    private void listarProductos(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        // 1. Obtener lista de productos
        List<Producto> listaProductos = productoDAO.listarProductos();
        request.setAttribute("listaProductos", listaProductos);

        // 2. << CAMBIO CLAVE: Obtener lista de proveedores y enviarla al JSP
        List<Proveedor> listaProveedores = proveedorDAO.listarProveedores();
        request.setAttribute("proveedores", listaProveedores);

        // 3. Redirigir a la vista
        request.getRequestDispatcher("views/inventario.jsp").forward(request, response);
    }

    private void mostrarAlertasStock(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        List<Producto> alertas = productoDAO.obtenerAlertasStockBajo();
        request.setAttribute("alertasStock", alertas);
        request.getRequestDispatcher("views/alertas_stock.jsp").forward(request, response);
    }

    private void mostrarFormularioEdicion(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        int idProducto = Integer.parseInt(request.getParameter("id"));
        Producto producto = productoDAO.obtenerProductoPorId(idProducto);

        // También se deben cargar los proveedores para el formulario de edición
        List<Proveedor> listaProveedores = proveedorDAO.listarProveedores();
        request.setAttribute("proveedores", listaProveedores);

        request.setAttribute("producto", producto);
        request.getRequestDispatcher("views/producto_form.jsp").forward(request, response);
        // NOTA: Si usas el modal en inventario.jsp para la edición, debes hacer forward a inventario.jsp aquí.
    }

    private void eliminarProducto(HttpServletRequest request, HttpServletResponse response)
            throws IOException, SQLException {
        int idProducto = Integer.parseInt(request.getParameter("id"));

        String mensaje;
        if (productoDAO.eliminarProducto(idProducto)) {
            mensaje = "eliminado_exitoso";
        } else {
            mensaje = "bd_eliminar_fallo";
        }

        // Redirección al controlador principal
        response.sendRedirect(request.getContextPath() + "/InventarioController?mensaje=" + mensaje);
    }

    // --- MANEJO DE LA INSERCIÓN y EDICIÓN (AGREGAR/ACTUALIZAR) ---
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String mensajeExito = "agregado_exitoso";

        // Parámetros de control
        String idParam = request.getParameter("id_producto");
        String accionUpdate = request.getParameter("accion_update");

        try {
            // 1. Obtener y parsear datos del formulario
            int idProducto = (idParam != null && !idParam.isEmpty()) ? Integer.parseInt(idParam) : 0;
            String nombre = request.getParameter("nombre");
            String descripcion = request.getParameter("descripcion");
            String marca = request.getParameter("marca");
            String modelo = request.getParameter("modelo");
            int cantidad = Integer.parseInt(request.getParameter("cantidad"));
            int stockMinimo = Integer.parseInt(request.getParameter("stock_minimo"));
            BigDecimal precioUnitario = new BigDecimal(request.getParameter("precio_unitario"));
            String imagenUrl = request.getParameter("imagen_url");

            // RUC
            String rucParam = request.getParameter("ruc_proveedor");
            Long rucProveedor = (rucParam != null && !rucParam.isEmpty()) ? Long.parseLong(rucParam) : null;

            // 2. Crear objeto Producto
            Producto producto;
            if (idProducto > 0) {
                producto = new Producto(idProducto, nombre, descripcion, marca, modelo, cantidad, stockMinimo, precioUnitario, imagenUrl, rucProveedor);
                mensajeExito = "editado_exitoso";
            } else {
                producto = new Producto(nombre, descripcion, marca, modelo, cantidad, stockMinimo, precioUnitario, imagenUrl, rucProveedor);
            }

            // 3. Ejecutar acción en el DAO
            boolean exito;
            if (idProducto > 0 || "actualizar".equals(accionUpdate)) {
                exito = productoDAO.actualizarProducto(producto);
            } else {
                exito = productoDAO.agregarProducto(producto);
            }

            if (exito) {
                response.sendRedirect(request.getContextPath() + "/InventarioController?mensaje=" + mensajeExito);
            } else {
                request.setAttribute("error", "Error de BD al guardar o actualizar el producto. Ninguna fila fue afectada.");
                // Si falla, volvemos a listar (usando doGet)
                doGet(request, response);
            }

        } catch (NumberFormatException e) {
            request.setAttribute("error", "Error de formato en datos numéricos (Cantidad, Precio, Stock Mínimo o RUC): " + e.getMessage());
            doGet(request, response);
        } catch (SQLException e) {
            System.err.println("Error SQL al intentar AGREGAR/ACTUALIZAR producto: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Error interno al guardar el producto. Causa: " + e.getMessage());
            doGet(request, response);
        }
    }
}
