package models.DAO;

import models.Servicio;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import utils.ConexionBD; // <-- Importación de tu clase de conexión

/**
 * Clase DAO para manejar las operaciones CRUD de la entidad Servicio.
 */
public class ServicioDAO {
    // El método getConnection() de placeholder ha sido eliminado.
    // Ahora usamos directamente utils.ConexionBD.getConnection() en cada operación.

    // --- C: Crear/Agregar ---
    public boolean agregarServicio(Servicio servicio) {
        String SQL = "INSERT INTO Servicio (nombre, descripcion, tarifa_base) VALUES (?, ?, ?)";
        // Uso de tu clase de conexión
        try (Connection conn = ConexionBD.getConnection(); PreparedStatement ps = conn.prepareStatement(SQL)) {

            ps.setString(1, servicio.getNombre());
            ps.setString(2, servicio.getDescripcion());
            ps.setBigDecimal(3, servicio.getTarifa_base());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // --- R: Leer/Listar todos ---
    public List<Servicio> listarServicios() {
        List<Servicio> lista = new ArrayList<>();
        String SQL = "SELECT id_servicio, nombre, descripcion, tarifa_base FROM Servicio ORDER BY nombre";

        // Uso de tu clase de conexión
        try (Connection conn = ConexionBD.getConnection(); PreparedStatement ps = conn.prepareStatement(SQL); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Servicio s = new Servicio();
                s.setId_servicio(rs.getInt("id_servicio"));
                s.setNombre(rs.getString("nombre"));
                s.setDescripcion(rs.getString("descripcion"));
                s.setTarifa_base(rs.getBigDecimal("tarifa_base"));
                lista.add(s);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    // --- U: Actualizar/Editar ---
    public boolean actualizarServicio(Servicio servicio) {
        String SQL = "UPDATE Servicio SET nombre=?, descripcion=?, tarifa_base=? WHERE id_servicio=?";
        // Uso de tu clase de conexión
        try (Connection conn = ConexionBD.getConnection(); PreparedStatement ps = conn.prepareStatement(SQL)) {

            ps.setString(1, servicio.getNombre());
            ps.setString(2, servicio.getDescripcion());
            ps.setBigDecimal(3, servicio.getTarifa_base());
            ps.setInt(4, servicio.getId_servicio());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // --- D: Eliminar ---
    public boolean eliminarServicio(int id) {
        String SQL = "DELETE FROM Servicio WHERE id_servicio=?";
        // Uso de tu clase de conexión
        try (Connection conn = ConexionBD.getConnection(); PreparedStatement ps = conn.prepareStatement(SQL)) {

            ps.setInt(1, id);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // --- R: Leer por ID (Necesario para Editar) ---
    public Servicio obtenerServicioPorId(int id) {
        Servicio servicio = null;
        String SQL = "SELECT id_servicio, nombre, descripcion, tarifa_base FROM Servicio WHERE id_servicio = ?";

        // Uso de tu clase de conexión
        try (Connection conn = ConexionBD.getConnection(); PreparedStatement ps = conn.prepareStatement(SQL)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    servicio = new Servicio();
                    servicio.setId_servicio(rs.getInt("id_servicio"));
                    servicio.setNombre(rs.getString("nombre"));
                    servicio.setDescripcion(rs.getString("descripcion"));
                    servicio.setTarifa_base(rs.getBigDecimal("tarifa_base"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return servicio;
    }
}
