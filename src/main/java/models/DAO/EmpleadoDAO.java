package models.DAO;

import models.Empleado;
import utils.ConexionBD; // <-- CORREGIDO: Importando la clase de conexión del paquete utils
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) para la gestión de la entidad Empleado/Personal.
 */
public class EmpleadoDAO {

    private static final String SQL_INSERT = "INSERT INTO personal (dni, nombre, apellido, telefono, email, puesto) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SQL_SELECT_ALL = "SELECT dni, nombre, apellido, telefono, email, puesto FROM personal";
    private static final String SQL_DELETE = "DELETE FROM personal WHERE dni = ?";
    private static final String SQL_UPDATE = "UPDATE personal SET nombre = ?, apellido = ?, telefono = ?, email = ?, puesto = ? WHERE dni = ?";
    private static final String SQL_SELECT_BY_DNI = "SELECT dni, nombre, apellido, telefono, email, puesto FROM personal WHERE dni = ?";

    /**
     * Agrega un nuevo empleado a la base de datos.
     *
     * @param empleado El objeto Empleado a agregar.
     * @throws SQLException Si ocurre un error de SQL (e.g., DNI duplicado).
     */
    public void agregarEmpleado(Empleado empleado) throws SQLException {
        // Usando ConexionBD.getConnection()
        try (Connection conn = ConexionBD.getConnection(); PreparedStatement stmt = conn.prepareStatement(SQL_INSERT)) {

            stmt.setLong(1, empleado.getDni());
            stmt.setString(2, empleado.getNombre());
            stmt.setString(3, empleado.getApellido());
            stmt.setString(4, empleado.getTelefono());
            stmt.setString(5, empleado.getEmail());
            stmt.setString(6, empleado.getPuesto());

            stmt.executeUpdate();
        }
    }

    /**
     * Obtiene todos los empleados.
     *
     * @return Una lista de objetos Empleado.
     * @throws SQLException Si ocurre un error de SQL.
     */
    public List<Empleado> obtenerTodosLosEmpleados() throws SQLException {
        List<Empleado> empleados = new ArrayList<>();
        // Usando ConexionBD.getConnection()
        try (Connection conn = ConexionBD.getConnection(); PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_ALL); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                empleados.add(extraerEmpleadoDeResultSet(rs));
            }
        }
        return empleados;
    }

    /**
     * Elimina un empleado por su DNI.
     *
     * @param dni El DNI del empleado a eliminar.
     * @throws SQLException Si ocurre un error de SQL.
     */
    public void eliminarEmpleado(long dni) throws SQLException {
        // Usando ConexionBD.getConnection()
        try (Connection conn = ConexionBD.getConnection(); PreparedStatement stmt = conn.prepareStatement(SQL_DELETE)) {

            stmt.setLong(1, dni);
            stmt.executeUpdate();
        }
    }

    /**
     * Edita un empleado existente.
     *
     * @param empleado El objeto Empleado con los datos actualizados.
     * @throws SQLException Si ocurre un error de SQL.
     */
    public void editarEmpleado(Empleado empleado) throws SQLException {
        // Usando ConexionBD.getConnection()
        try (Connection conn = ConexionBD.getConnection(); PreparedStatement stmt = conn.prepareStatement(SQL_UPDATE)) {

            stmt.setString(1, empleado.getNombre());
            stmt.setString(2, empleado.getApellido());
            stmt.setString(3, empleado.getTelefono());
            stmt.setString(4, empleado.getEmail());
            stmt.setString(5, empleado.getPuesto());
            // El DNI va al final para la cláusula WHERE
            stmt.setLong(6, empleado.getDni());

            stmt.executeUpdate();
        }
    }

    /**
     * Obtiene un empleado por su DNI.
     *
     * @param dni El DNI del empleado.
     * @return El objeto Empleado encontrado, o null.
     * @throws SQLException Si ocurre un error de SQL.
     */
    public Empleado obtenerEmpleadoPorDni(long dni) throws SQLException {
        // Usando ConexionBD.getConnection()
        try (Connection conn = ConexionBD.getConnection(); PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_BY_DNI)) {

            stmt.setLong(1, dni);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extraerEmpleadoDeResultSet(rs);
                }
            }
        }
        return null;
    }

    /**
     * Método auxiliar para mapear un ResultSet a un objeto Empleado.
     */
    private Empleado extraerEmpleadoDeResultSet(ResultSet rs) throws SQLException {
        long dni = rs.getLong("dni");
        String nombre = rs.getString("nombre");
        String apellido = rs.getString("apellido");
        String telefono = rs.getString("telefono");
        String email = rs.getString("email");
        String puesto = rs.getString("puesto");

        return new Empleado(dni, nombre, apellido, telefono, email, puesto);
    }
}
