package models.DAO;

import models.Producto;
import utils.ConexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

public class ProductoDAO {

    // Comandos SQL
    private static final String SQL_INSERT = "INSERT INTO Producto (nombre, descripcion, marca, modelo, cantidad, stock_minimo, precio_unitario, imagen_url, ruc_proveedor) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_SELECT_ALL = "SELECT * FROM Producto";
    private static final String SQL_SELECT_BY_ID = "SELECT * FROM Producto WHERE id_producto = ?";
    private static final String SQL_UPDATE = "UPDATE Producto SET nombre=?, descripcion=?, marca=?, modelo=?, cantidad=?, stock_minimo=?, precio_unitario=?, imagen_url=?, ruc_proveedor=? WHERE id_producto=?";
    private static final String SQL_DELETE = "DELETE FROM Producto WHERE id_producto = ?";

    // SQL para la lógica de stock mínimo (Alertas)
    public static final String SQL_ALERTAS_STOCK = "SELECT * FROM Producto WHERE cantidad <= stock_minimo";

    // --- MÉTODOS CRUD ---
    /**
     * Agrega un nuevo producto a la base de datos.
     */
    public boolean agregarProducto(Producto producto) throws SQLException {
        try (Connection conn = ConexionBD.getConnection(); PreparedStatement ps = conn.prepareStatement(SQL_INSERT)) {

            ps.setString(1, producto.getNombre());
            ps.setString(2, producto.getDescripcion());
            ps.setString(3, producto.getMarca());
            ps.setString(4, producto.getModelo());
            ps.setInt(5, producto.getCantidad());
            ps.setInt(6, producto.getStock_minimo());
            ps.setBigDecimal(7, producto.getPrecio_unitario()); // Usar setBigDecimal
            ps.setString(8, producto.getImagen_url());

            // Usar setLong para ruc_proveedor (BIGINT)
            if (producto.getRuc_proveedor() != null) {
                ps.setLong(9, producto.getRuc_proveedor());
            } else {
                ps.setNull(9, Types.BIGINT); // Permite NULL
            }

            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Lista todos los productos.
     */
    public List<Producto> listarProductos() throws SQLException {
        List<Producto> lista = new ArrayList<>();
        try (Connection conn = ConexionBD.getConnection(); Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(SQL_SELECT_ALL)) {

            while (rs.next()) {
                lista.add(extraerProducto(rs));
            }
        }
        return lista;
    }

    /**
     * Obtiene un producto por su ID.
     */
    public Producto obtenerProductoPorId(int id) throws SQLException {
        Producto producto = null;
        try (Connection conn = ConexionBD.getConnection(); PreparedStatement ps = conn.prepareStatement(SQL_SELECT_BY_ID)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    producto = extraerProducto(rs);
                }
            }
        }
        return producto;
    }

    /**
     * Actualiza la información de un producto existente.
     */
    public boolean actualizarProducto(Producto producto) throws SQLException {
        try (Connection conn = ConexionBD.getConnection(); PreparedStatement ps = conn.prepareStatement(SQL_UPDATE)) {

            ps.setString(1, producto.getNombre());
            ps.setString(2, producto.getDescripcion());
            ps.setString(3, producto.getMarca());
            ps.setString(4, producto.getModelo());
            ps.setInt(5, producto.getCantidad());
            ps.setInt(6, producto.getStock_minimo());
            ps.setBigDecimal(7, producto.getPrecio_unitario());
            ps.setString(8, producto.getImagen_url());

            if (producto.getRuc_proveedor() != null) {
                ps.setLong(9, producto.getRuc_proveedor());
            } else {
                ps.setNull(9, Types.BIGINT);
            }

            ps.setInt(10, producto.getId_producto()); // WHERE clause

            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Elimina un producto por su ID.
     */
    public boolean eliminarProducto(int id) throws SQLException {
        try (Connection conn = ConexionBD.getConnection(); PreparedStatement ps = conn.prepareStatement(SQL_DELETE)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    // --- LÓGICA DE NEGOCIO Y UTILIDAD ---
    /**
     * Obtiene una lista de productos con stock bajo (cantidad <= stock_minimo).
     */
    public List<Producto> obtenerAlertasStockBajo() throws SQLException {
        List<Producto> lista = new ArrayList<>();
        try (Connection conn = ConexionBD.getConnection(); Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(SQL_ALERTAS_STOCK)) {

            while (rs.next()) {
                lista.add(extraerProducto(rs));
            }
        }
        return lista;
    }

    /**
     * Método auxiliar para extraer un Producto de un ResultSet.
     */
    private Producto extraerProducto(ResultSet rs) throws SQLException {
        Long rucProveedor = rs.getLong("ruc_proveedor");
        if (rs.wasNull()) {
            rucProveedor = null; // Asignar null si el campo es NULL en la BD
        }

        return new Producto(
                rs.getInt("id_producto"),
                rs.getString("nombre"),
                rs.getString("descripcion"),
                rs.getString("marca"),
                rs.getString("modelo"),
                rs.getInt("cantidad"),
                rs.getInt("stock_minimo"),
                rs.getBigDecimal("precio_unitario"), // Usar getBigDecimal
                rs.getString("imagen_url"),
                rucProveedor
        );
    }
}
