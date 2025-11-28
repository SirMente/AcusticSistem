package models.DAO;

import models.Proyecto;
import models.Proforma;
import models.Empleado;
import utils.ConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal; 

/**
 * Clase Data Access Object (DAO) para manejar las operaciones CRUD
 * de la entidad Proyecto en la base de datos.
 */
public class ProyectoDAO {

    Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    /**
     * Obtiene una lista de Proformas cuyo estado es 'PAGADA_PARCIAL'.
     * Estas son las únicas proformas válidas para iniciar un proyecto.
     * @return Lista de Proformas con estado 'PAGADA_PARCIAL'.
     */
    public List<Proforma> listarProformasDisponibles() {
        List<Proforma> lista = new ArrayList<>();
        // Estado actualizado a 'PAGADA_PARCIAL'. Se selecciona ruc_cliente y total.
        String sql = "SELECT id_proforma, ruc_cliente, total FROM Proforma WHERE estado = 'PAGADA_PARCIAL'";

        try {
            con = ConexionBD.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Proforma p = new Proforma();
                // idProforma es String
                p.setIdProforma(rs.getString("id_proforma"));
                
                // Mapear el ruc_cliente (DB) al setIdCliente() (modelo Proforma)
                p.setIdCliente(rs.getLong("ruc_cliente"));
                
                // Usar getBigDecimal() para coincidir con el tipo del modelo Proforma
                p.setTotal(rs.getBigDecimal("total"));
                
                lista.add(p);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar proformas disponibles: " + e.getMessage());
        } finally {
            closeResources();
        }
        return lista;
    }
    
    /**
     * Obtiene una lista de todo el personal (empleados) para el select de encargado.
     * @return Lista de Empleado.
     */
    public List<Empleado> listarPersonal() { 
        List<Empleado> lista = new ArrayList<>(); 
        String sql = "SELECT dni, nombre, apellido FROM personal";

        try {
            con = ConexionBD.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Empleado p = new Empleado(); 
                p.setDni(rs.getLong("dni"));
                p.setNombre(rs.getString("nombre"));
                p.setApellido(rs.getString("apellido"));
                lista.add(p);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar personal: " + e.getMessage());
        } finally {
            closeResources();
        }
        return lista;
    }

    // --- Operación de Listar Proyectos (READ ALL) ---
    public List<Proyecto> listarProyectos() {
        List<Proyecto> lista = new ArrayList<>();
        // Se unen las tablas Proyectos, Proforma (actualizada a RUC BIGINT) y Personal
        String sql = "SELECT p.idProyecto, p.idProforma, p.nombreProyecto, p.fechaInicio, p.fechaFinEstimada, p.estado, " +
                     "p.dniEncargado, pr.ruc_cliente, CONCAT(per.nombre, ' ', per.apellido) AS nombreEncargado " +
                     "FROM proyectos p " +
                     "JOIN Proforma pr ON p.idProforma = pr.id_proforma " +
                     "JOIN personal per ON p.dniEncargado = per.dni " +
                     "ORDER BY p.idProyecto DESC";

        try {
            con = ConexionBD.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Proyecto p = new Proyecto();
                p.setIdProyecto(rs.getInt("idProyecto"));
                p.setIdProforma(rs.getString("idProforma")); // String
                p.setNombreProyecto(rs.getString("nombreProyecto"));
                p.setFechaInicio(rs.getDate("fechaInicio"));
                p.setFechaFinEstimada(rs.getDate("fechaFinEstimada"));
                p.setEstado(rs.getString("estado"));
                p.setDniEncargado(rs.getLong("dniEncargado"));
                p.setRucCliente(rs.getLong("ruc_cliente")); 
                p.setNombreEncargado(rs.getString("nombreEncargado"));
                lista.add(p);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar proyectos: " + e.getMessage());
        } finally {
            closeResources();
        }
        return lista;
    }

    // --- Operación de Buscar Proyecto por ID (READ ONE) ---
    /**
     * Busca un proyecto específico por su ID.
     * @param idProyecto ID del proyecto a buscar.
     * @return El objeto Proyecto si se encuentra, o null.
     */
    public Proyecto buscarProyectoPorId(int idProyecto) {
        Proyecto proyecto = null;
        String sql = "SELECT p.idProyecto, p.idProforma, p.nombreProyecto, p.fechaInicio, p.fechaFinEstimada, p.estado, " +
                     "p.dniEncargado, pr.ruc_cliente, CONCAT(per.nombre, ' ', per.apellido) AS nombreEncargado " +
                     "FROM proyectos p " +
                     "JOIN Proforma pr ON p.idProforma = pr.id_proforma " +
                     "JOIN personal per ON p.dniEncargado = per.dni " +
                     "WHERE p.idProyecto = ?";

        try {
            con = ConexionBD.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, idProyecto);
            rs = ps.executeQuery();

            if (rs.next()) {
                proyecto = new Proyecto();
                proyecto.setIdProyecto(rs.getInt("idProyecto"));
                proyecto.setIdProforma(rs.getString("idProforma"));
                proyecto.setNombreProyecto(rs.getString("nombreProyecto"));
                proyecto.setFechaInicio(rs.getDate("fechaInicio"));
                proyecto.setFechaFinEstimada(rs.getDate("fechaFinEstimada"));
                proyecto.setEstado(rs.getString("estado"));
                proyecto.setDniEncargado(rs.getLong("dniEncargado"));
                proyecto.setRucCliente(rs.getLong("ruc_cliente"));
                proyecto.setNombreEncargado(rs.getString("nombreEncargado"));
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar proyecto por ID: " + e.getMessage());
        } finally {
            closeResources();
        }
        return proyecto;
    }


    // --- Operación de Agregar (CREATE) ---
    public boolean agregarProyecto(Proyecto proyecto) {
        boolean agregado = false;
        // El estado por defecto es 'Planificacion'
        String sql = "INSERT INTO proyectos (idProforma, nombreProyecto, fechaInicio, fechaFinEstimada, estado, dniEncargado) VALUES (?, ?, ?, ?, 'Planificacion', ?)";
        
        try {
            con = ConexionBD.getConnection();
            // Permite obtener las claves generadas automáticamente (como idProyecto)
            ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            ps.setString(1, proyecto.getIdProforma()); // String
            ps.setString(2, proyecto.getNombreProyecto());
            ps.setDate(3, proyecto.getFechaInicio());
            ps.setDate(4, proyecto.getFechaFinEstimada());
            // ps.setString(5, 'Planificacion') es fijo en el SQL
            ps.setLong(5, proyecto.getDniEncargado());

            int filasAfectadas = ps.executeUpdate();
            if (filasAfectadas > 0) {
                agregado = true;
                System.out.println("Proyecto agregado exitosamente para Proforma ID: " + proyecto.getIdProforma());
            }
        } catch (SQLException e) {
            System.err.println("Error al agregar proyecto: " + e.getMessage());
        } finally {
            closeResources();
        }
        return agregado;
    }

    // --- Operación de Actualizar (UPDATE) ---
    public boolean actualizarProyecto(Proyecto proyecto) {
        boolean actualizado = false;
        // Solo permitimos actualizar campos clave, incluyendo el estado
        String sql = "UPDATE proyectos SET idProforma=?, nombreProyecto=?, fechaInicio=?, fechaFinEstimada=?, estado=?, dniEncargado=? WHERE idProyecto=?";
        
        try {
            con = ConexionBD.getConnection();
            ps = con.prepareStatement(sql);
            
            ps.setString(1, proyecto.getIdProforma()); // String
            ps.setString(2, proyecto.getNombreProyecto());
            ps.setDate(3, proyecto.getFechaInicio());
            ps.setDate(4, proyecto.getFechaFinEstimada());
            ps.setString(5, proyecto.getEstado()); // Aquí se actualiza el estado
            ps.setLong(6, proyecto.getDniEncargado());
            ps.setInt(7, proyecto.getIdProyecto()); // Condición WHERE

            int filasAfectadas = ps.executeUpdate();
            if (filasAfectadas > 0) {
                actualizado = true;
                System.out.println("Proyecto ID " + proyecto.getIdProyecto() + " actualizado exitosamente.");
            }
        } catch (SQLException e) {
            System.err.println("Error al actualizar proyecto: " + e.getMessage());
        } finally {
            closeResources();
        }
        return actualizado;
    }
    
    // --- Operación de Eliminar (DELETE) ---
    public boolean eliminarProyecto(int idProyecto) {
        boolean eliminado = false;
        String sql = "DELETE FROM proyectos WHERE idProyecto = ?";
        try {
            con = ConexionBD.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, idProyecto);
            
            int filasAfectadas = ps.executeUpdate();
            if (filasAfectadas > 0) {
                eliminado = true;
                System.out.println("Proyecto ID " + idProyecto + " eliminado exitosamente.");
            }
        } catch (SQLException e) {
            System.err.println("Error al eliminar proyecto: " + e.getMessage());
        } finally {
            closeResources();
        }
        return eliminado;
    }

    // --- Cierre de Recursos ---
    private void closeResources() {
        try {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (con != null) con.close();
        } catch (SQLException e) {
            System.err.println("Error al cerrar recursos: " + e.getMessage());
        }
    }
}