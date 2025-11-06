package models.DAO;

// Asegúrate de que este sea el path correcto a tu modelo Producto
import models.Producto;
import utils.ConexionBD; // Asegúrate de que este sea el path correcto a tu clase de conexión
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {

    // Instancia del ProveedorDAO para buscar el ID
    private ProveedorDAO proveedorDAO = new ProveedorDAO(); 

    // 🔑 CONSULTA SELECT CORREGIDA: Usa JOIN para obtener el NOMBRE del proveedor
    private static final String SQL_SELECT = 
            "SELECT P.id_producto, P.nombre, P.descripcion, P.cantidad, P.precio_unitario, P.imagen_url, " +
            "PR.nombre AS nombre_proveedor, PR.id_proveedor " + 
            "FROM Producto P " +
            "JOIN Proveedores PR ON P.id_proveedor = PR.id_proveedor " + // Nota: Asumo que la tabla se llama 'Proveedores'
            "ORDER BY P.nombre ASC";
    
    // 🔑 Consulta INSERT CORREGIDA: Usa id_proveedor
    private static final String SQL_INSERT = 
            "INSERT INTO Producto (nombre, descripcion, cantidad, precio_unitario, id_proveedor, imagen_url) " +
            "VALUES (?, ?, ?, ?, ?, ?)";
    
    // 🔑 Consulta SELECT BY ID CORREGIDA: Asegura que el nombre del proveedor se cargue
    private static final String SQL_SELECT_BY_ID = 
            "SELECT P.id_producto, P.nombre, P.descripcion, P.cantidad, P.precio_unitario, P.imagen_url, " +
            "PR.nombre AS nombre_proveedor, PR.id_proveedor " +
            "FROM Producto P " +
            "JOIN Proveedores PR ON P.id_proveedor = PR.id_proveedor " +
            "WHERE P.id_producto = ?";
    
    // 🔑 Consulta UPDATE CORREGIDA: Usa id_proveedor
    private static final String SQL_UPDATE = 
            "UPDATE Producto SET nombre = ?, descripcion = ?, cantidad = ?, precio_unitario = ?, id_proveedor = ?, imagen_url = ? " +
            "WHERE id_producto = ?";
    
    private static final String SQL_DELETE = "DELETE FROM Producto WHERE id_producto = ?";
    
    // --- Método de Mapeo (Helper) ---
    private Producto mapearProducto(ResultSet rs) throws SQLException {
        Producto producto = new Producto();
        producto.setId(rs.getInt("id_producto"));
        producto.setNombre(rs.getString("nombre"));
        producto.setDescripcion(rs.getString("descripcion"));
        producto.setCantidad(rs.getInt("cantidad"));
        producto.setPrecioUnitario(rs.getDouble("precio_unitario"));
        
        // El alias 'nombre_proveedor' viene del JOIN en SQL_SELECT
        producto.setProveedor(rs.getString("nombre_proveedor")); 
        
        // Mapeamos el ID del proveedor (necesario si quieres actualizar el proveedor)
        producto.setIdProveedor(rs.getInt("id_proveedor"));
        
        producto.setImagenUrl(rs.getString("imagen_url")); 
        return producto;
    }

    // --- LISTAR Productos (READ) ---
    public List<Producto> listarProductos() throws SQLException {
        List<Producto> lista = new ArrayList<>();
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_SELECT);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                lista.add(mapearProducto(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar productos: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
        return lista;
    }
    
    // --- OBTENER Por ID (READ) ---
    public Producto obtenerProductoPorId(int id) throws SQLException {
        Producto producto = null;
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_SELECT_BY_ID)) {
            
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    producto = mapearProducto(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener producto por ID: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
        return producto;
    }
    
    // --- AGREGAR Producto (CREATE) ---
    public boolean agregarProducto(Producto producto) throws SQLException {
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_INSERT)) {
            
            // 🔑 CONVERSIÓN: Obtener el ID del proveedor usando su nombre
            int idProveedor = proveedorDAO.obtenerIdPorNombre(producto.getProveedor());

            if (idProveedor == 0) {
                System.err.println("Error de FK: No se encontró el ID del proveedor con nombre: " + producto.getProveedor());
                return false; 
            }

            ps.setString(1, producto.getNombre());
            ps.setString(2, producto.getDescripcion());
            ps.setInt(3, producto.getCantidad());
            ps.setDouble(4, producto.getPrecioUnitario());
            ps.setInt(5, idProveedor); // Usamos el ID del proveedor
            ps.setString(6, producto.getImagenUrl()); 
            
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al agregar producto: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    // --- ACTUALIZAR Producto (UPDATE) ---
    public boolean actualizarProducto(Producto producto) throws SQLException {
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_UPDATE)) {
            
            // 🔑 CONVERSIÓN: Obtener el ID del proveedor usando su nombre
            int idProveedor = proveedorDAO.obtenerIdPorNombre(producto.getProveedor());
            
            if (idProveedor == 0) {
                System.err.println("Error de FK: No se encontró el ID del proveedor con nombre: " + producto.getProveedor());
                return false;
            }

            ps.setString(1, producto.getNombre());
            ps.setString(2, producto.getDescripcion());
            ps.setInt(3, producto.getCantidad());
            ps.setDouble(4, producto.getPrecioUnitario());
            ps.setInt(5, idProveedor); // Usamos el ID del proveedor
            ps.setString(6, producto.getImagenUrl());
            ps.setInt(7, producto.getId());
            
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar producto: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    
    // --- ELIMINAR Producto (DELETE) ---
    public boolean eliminarProducto(int id) throws SQLException {
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_DELETE)) {
            
            ps.setInt(1, id);
            
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar producto: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
}