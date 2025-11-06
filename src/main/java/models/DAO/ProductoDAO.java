/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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

    // 🔑 Consultas SQL CRUD (Asumiendo que la tabla se llama 'Producto' o 'productos' con el campo 'id_producto')
    private static final String SQL_SELECT = "SELECT id_producto, nombre, descripcion, cantidad, precio_unitario, proveedor, imagen_url FROM Producto ORDER BY nombre ASC";
    private static final String SQL_INSERT = "INSERT INTO Producto (nombre, descripcion, cantidad, precio_unitario, proveedor, imagen_url) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SQL_SELECT_BY_ID = "SELECT id_producto, nombre, descripcion, cantidad, precio_unitario, proveedor, imagen_url FROM Producto WHERE id_producto = ?";
    private static final String SQL_UPDATE = "UPDATE Producto SET nombre = ?, descripcion = ?, cantidad = ?, precio_unitario = ?, proveedor = ?, imagen_url = ? WHERE id_producto = ?";
    private static final String SQL_DELETE = "DELETE FROM Producto WHERE id_producto = ?";
    
    // --- Método de Mapeo (Helper) ---
    private Producto mapearProducto(ResultSet rs) throws SQLException {
        Producto producto = new Producto();
        producto.setId(rs.getInt("id_producto"));
        producto.setNombre(rs.getString("nombre"));
        producto.setDescripcion(rs.getString("descripcion"));
        producto.setCantidad(rs.getInt("cantidad"));
        producto.setPrecioUnitario(rs.getDouble("precio_unitario"));
        producto.setProveedor(rs.getString("proveedor"));
        producto.setImagenUrl(rs.getString("imagen_url")); // 👈 NUEVO CAMPO
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
            
            ps.setString(1, producto.getNombre());
            ps.setString(2, producto.getDescripcion());
            ps.setInt(3, producto.getCantidad());
            ps.setDouble(4, producto.getPrecioUnitario());
            ps.setString(5, producto.getProveedor());
            ps.setString(6, producto.getImagenUrl()); // 👈 INSERCIÓN DEL CAMPO DE IMAGEN
            
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
            
            ps.setString(1, producto.getNombre());
            ps.setString(2, producto.getDescripcion());
            ps.setInt(3, producto.getCantidad());
            ps.setDouble(4, producto.getPrecioUnitario());
            ps.setString(5, producto.getProveedor());
            ps.setString(6, producto.getImagenUrl()); // 👈 ACTUALIZACIÓN DEL CAMPO DE IMAGEN
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