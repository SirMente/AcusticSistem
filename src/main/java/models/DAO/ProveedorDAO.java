package models.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import models.Proveedor;
import utils.ConexionBD;

public class ProveedorDAO {

    // 🔑 Consulta SQL Actualizada
    private final String SQL_SELECT = "SELECT id_proveedor, nombre, email, telefono, tipo_producto FROM Proveedores ORDER BY nombre ASC";
    private final String SQL_INSERT = "INSERT INTO Proveedores (nombre, email, telefono, tipo_producto) VALUES (?, ?, ?, ?)";
    private final String SQL_SELECT_BY_ID = "SELECT id_proveedor, nombre, email, telefono, tipo_producto FROM Proveedores WHERE id_proveedor = ?";
    private final String SQL_UPDATE = "UPDATE Proveedores SET nombre = ?, email = ?, telefono = ?, tipo_producto = ? WHERE id_proveedor = ?";
    private final String SQL_DELETE = "DELETE FROM Proveedores WHERE id_proveedor = ?";

    // --- Método LISTAR ---
    public List<Proveedor> listarProveedores() throws SQLException {
        List<Proveedor> listaProveedores = new ArrayList<>();

        try (Connection conn = ConexionBD.getConnection(); PreparedStatement ps = conn.prepareStatement(SQL_SELECT); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Proveedor proveedor = new Proveedor();

                proveedor.setIdProveedor(rs.getInt("id_proveedor"));
                proveedor.setNombre(rs.getString("nombre"));
                // 🔑 Mapeo de Contactos
                proveedor.setEmail(rs.getString("email"));
                proveedor.setTelefono(rs.getString("telefono"));
                proveedor.setTipoProducto(rs.getString("tipo_producto"));

                listaProveedores.add(proveedor);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar proveedores: " + e.getMessage());
            throw e;
        }
        return listaProveedores;
    }

    public Proveedor obtenerProveedorPorId(int id) throws SQLException {
        Proveedor proveedor = null;
        try (Connection conn = ConexionBD.getConnection(); PreparedStatement ps = conn.prepareStatement(SQL_SELECT_BY_ID)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    proveedor = new Proveedor();
                    proveedor.setIdProveedor(rs.getInt("id_proveedor"));
                    proveedor.setNombre(rs.getString("nombre"));
                    proveedor.setEmail(rs.getString("email"));
                    proveedor.setTelefono(rs.getString("telefono"));
                    proveedor.setTipoProducto(rs.getString("tipo_producto"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener proveedor por ID: " + e.getMessage());
            throw e;
        }
        return proveedor;
    }

    // --- Método INSERTAR ---
    public boolean agregarProveedor(Proveedor proveedor) throws SQLException {
        try (Connection conn = ConexionBD.getConnection(); PreparedStatement ps = conn.prepareStatement(SQL_INSERT)) {

            ps.setString(1, proveedor.getNombre());
            // 🔑 Inserción de Contactos
            ps.setString(2, proveedor.getEmail());
            ps.setString(3, proveedor.getTelefono());
            ps.setString(4, proveedor.getTipoProducto());

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("Error al agregar proveedor: " + e.getMessage());
            throw e;
        }
    }

    // --- Nuevo Método: ACTUALIZAR ---
    public boolean actualizarProveedor(Proveedor proveedor) throws SQLException {
        try (Connection conn = ConexionBD.getConnection(); PreparedStatement ps = conn.prepareStatement(SQL_UPDATE)) {

            ps.setString(1, proveedor.getNombre());
            ps.setString(2, proveedor.getEmail());
            ps.setString(3, proveedor.getTelefono());
            ps.setString(4, proveedor.getTipoProducto());
            ps.setInt(5, proveedor.getIdProveedor()); // Clave de actualización

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("Error al actualizar proveedor: " + e.getMessage());
            throw e;
        }
    }

    // --- Nuevo Método: ELIMINAR ---
    public boolean eliminarProveedor(int id) throws SQLException {
        try (Connection conn = ConexionBD.getConnection(); PreparedStatement ps = conn.prepareStatement(SQL_DELETE)) {

            ps.setInt(1, id);

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("Error al eliminar proveedor: " + e.getMessage());
            throw e;
        }
    }
}
