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

    // Consultas actualizadas: ruc es la PK, incluye direccion
    private final String SQL_COLUMNS = "ruc, nombre, tipo_producto, email, telefono, direccion";
    private final String SQL_SELECT = "SELECT " + SQL_COLUMNS + " FROM proveedores ORDER BY nombre ASC";
    private final String SQL_INSERT = "INSERT INTO proveedores (ruc, nombre, tipo_producto, email, telefono, direccion) VALUES (?, ?, ?, ?, ?, ?)";
    private final String SQL_SELECT_BY_ID = "SELECT " + SQL_COLUMNS + " FROM proveedores WHERE ruc = ?";
    private final String SQL_UPDATE = "UPDATE proveedores SET nombre = ?, tipo_producto = ?, email = ?, telefono = ?, direccion = ? WHERE ruc = ?";
    private final String SQL_DELETE = "DELETE FROM proveedores WHERE ruc = ?";

    // NUEVA Consulta (ajustada a la nueva PK ruc)
    private static final String SQL_SELECT_ID_BY_NOMBRE = "SELECT ruc FROM proveedores WHERE nombre = ?";

    // --- Método LISTAR ---
    public List<Proveedor> listarProveedores() throws SQLException {
        List<Proveedor> listaProveedores = new ArrayList<>();

        try (Connection conn = ConexionBD.getConnection(); PreparedStatement ps = conn.prepareStatement(SQL_SELECT); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                // Constructor completo
                Proveedor proveedor = new Proveedor(
                        rs.getLong("ruc"),
                        rs.getString("nombre"),
                        rs.getString("tipo_producto"),
                        rs.getString("email"),
                        rs.getString("telefono"),
                        rs.getString("direccion") // Nuevo campo
                );
                listaProveedores.add(proveedor);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar proveedores: " + e.getMessage());
            throw e;
        }
        return listaProveedores;
    }

    // --- Método OBTENER POR ID ---
    public Proveedor obtenerProveedorPorId(long ruc) throws SQLException { // CAMBIADO: id a ruc
        Proveedor proveedor = null;
        try (Connection conn = ConexionBD.getConnection(); PreparedStatement ps = conn.prepareStatement(SQL_SELECT_BY_ID)) {

            ps.setLong(1, ruc); // CAMBIADO: id a ruc
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Usando el constructor completo
                    proveedor = new Proveedor(
                            rs.getInt("ruc"),
                            rs.getString("nombre"),
                            rs.getString("tipo_producto"),
                            rs.getString("email"),
                            rs.getString("telefono"),
                            rs.getString("direccion") // Nuevo campo
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener proveedor por RUC: " + e.getMessage());
            throw e;
        }
        return proveedor;
    }

    // --- Método INSERTAR ---
    public boolean agregarProveedor(Proveedor proveedor) throws SQLException {
        try (Connection conn = ConexionBD.getConnection(); PreparedStatement ps = conn.prepareStatement(SQL_INSERT)) {

            // El RUC debe ser insertado porque NO es autoincrementable
            ps.setLong(1, proveedor.getRuc()); // NUEVO: RUC
            ps.setString(2, proveedor.getNombre());
            ps.setString(3, proveedor.getTipoProducto());
            ps.setString(4, proveedor.getEmail());
            ps.setString(5, proveedor.getTelefono());
            ps.setString(6, proveedor.getDireccion()); // NUEVO: Direccion

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("Error al agregar proveedor: " + e.getMessage());
            throw e;
        }
    }

    // --- Método ACTUALIZAR ---
    public boolean actualizarProveedor(Proveedor proveedor) throws SQLException {
        try (Connection conn = ConexionBD.getConnection(); PreparedStatement ps = conn.prepareStatement(SQL_UPDATE)) {

            ps.setString(1, proveedor.getNombre());
            ps.setString(2, proveedor.getTipoProducto());
            ps.setString(3, proveedor.getEmail());
            ps.setString(4, proveedor.getTelefono());
            ps.setString(5, proveedor.getDireccion()); // NUEVO: Direccion
            ps.setLong(6, proveedor.getRuc()); // CAMBIADO: getRuc() y va al final (WHERE)

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("Error al actualizar proveedor: " + e.getMessage());
            throw e;
        }
    }

    // --- Método ELIMINAR ---
    public boolean eliminarProveedor(long ruc) throws SQLException { // CAMBIADO: id a ruc
        try (Connection conn = ConexionBD.getConnection(); PreparedStatement ps = conn.prepareStatement(SQL_DELETE)) {

            ps.setLong(1, ruc); // CAMBIADO: id a ruc

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("Error al eliminar proveedor: " + e.getMessage());
            throw e;
        }
    }

    // --- Método OBTENER RUC a partir del Nombre ---
    public long obtenerRucPorNombre(String nombreProveedor) throws SQLException { // CAMBIADO: obtenerIdPorNombre a obtenerRucPorNombre
        long ruc = 0; // CAMBIADO: idProveedor a ruc
        try (Connection conn = ConexionBD.getConnection(); PreparedStatement ps = conn.prepareStatement(SQL_SELECT_ID_BY_NOMBRE)) {

            ps.setString(1, nombreProveedor);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ruc = rs.getLong("ruc"); // CAMBIADO: id_proveedor a ruc
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener RUC del proveedor por nombre: " + e.getMessage());
            throw e;
        }
        return ruc;
    }
}
