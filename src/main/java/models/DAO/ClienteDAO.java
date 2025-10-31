package models.DAO;

import models.Cliente;
import utils.ConexionBD; // Tu clase de conexión
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    // ---------------------------------------------
    // 1. OBTENER TODOS LOS CLIENTES (SELECT para listar)
    // ---------------------------------------------
    public List<Cliente> obtenerTodosLosClientes() throws SQLException {
        List<Cliente> listaClientes = new ArrayList<>();
        // 🔑 Asegúrate que los nombres de las columnas coincidan con tu tabla
        String sql = "SELECT id, nombre, empresa, telefono, email FROM clientes";

        // Usamos try-with-resources para cerrar Connection, PreparedStatement y ResultSet
        try (Connection conn = ConexionBD.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Cliente c = new Cliente(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("empresa"),
                        rs.getString("telefono"),
                        rs.getString("email")
                );
                listaClientes.add(c);
            }
        }
        return listaClientes;
    }

    // ---------------------------------------------
    // 2. AGREGAR CLIENTE (INSERT para guardar)
    // ---------------------------------------------
    public void agregarCliente(Cliente cliente) throws SQLException {
        // 🔑 Asegúrate que los nombres de las columnas coincidan con tu tabla
        String sql = "INSERT INTO clientes (nombre, empresa, telefono, email) VALUES (?, ?, ?, ?)";

        // Usamos try-with-resources para cerrar Connection y PreparedStatement
        try (Connection conn = ConexionBD.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            // 🔑 El orden de los parámetros es CRÍTICO
            ps.setString(1, cliente.getNombre());
            ps.setString(2, cliente.getEmpresa());
            ps.setString(3, cliente.getTelefono());
            ps.setString(4, cliente.getEmail());

            ps.executeUpdate();

        } catch (SQLException e) {
            // Lanza la excepción para que el Controller la capture y maneje el error
            throw e;
        }
    }

    // ---------------------------------------------
    // 3. EDITAR CLIENTE (UPDATE)
    // ---------------------------------------------
    public void editarCliente(Cliente cliente) throws SQLException {
        // 🔑 La cláusula WHERE id=? es CRÍTICA para solo actualizar el cliente deseado
        String sql = "UPDATE clientes SET nombre=?, empresa=?, telefono=?, email=? WHERE id=?";

        try (Connection conn = ConexionBD.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, cliente.getNombre());
            ps.setString(2, cliente.getEmpresa());
            ps.setString(3, cliente.getTelefono());
            ps.setString(4, cliente.getEmail());
            // El último parámetro es el ID para la cláusula WHERE
            ps.setInt(5, cliente.getId());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw e;
        }
    }

    // ---------------------------------------------
    // 4. ELIMINAR CLIENTE (DELETE)
    // ---------------------------------------------
    public void eliminarCliente(int idCliente) throws SQLException {
        // 🔑 El ID es el único dato necesario para eliminar
        String sql = "DELETE FROM clientes WHERE id=?";

        try (Connection conn = ConexionBD.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idCliente);

            ps.executeUpdate();

        } catch (SQLException e) {
            throw e;
        }
    }

    public List<Cliente> obtenerClientesParaSelect() throws SQLException {
        List<Cliente> listaClientes = new ArrayList<>();
        // Solo traemos las columnas necesarias
        String sql = "SELECT id, nombre FROM clientes ORDER BY nombre ASC";

        try (Connection conn = ConexionBD.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Cliente c = new Cliente();
                // Solo necesitamos establecer ID y Nombre para el <select>
                c.setId(rs.getInt("id"));
                c.setNombre(rs.getString("nombre"));

                listaClientes.add(c);
            }
        } // try-with-resources cierra automáticamente

        return listaClientes;
    }
    
    // ---------------------------------------------
// 6. OBTENER CLIENTE POR ID (SELECT simple)
// ---------------------------------------------
public Cliente obtenerClientePorId(int idCliente) throws SQLException {
    Cliente cliente = null;
    // 🔑 Verifica: 'clientes' es el nombre exacto de tu tabla
    String sql = "SELECT id, nombre, empresa, telefono, email FROM clientes WHERE id = ?"; 

    try (Connection conn = ConexionBD.getConnection(); 
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setInt(1, idCliente);
        
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                // 🔑 Verifica: Este constructor de 5 parámetros debe existir en Cliente.java
                cliente = new Cliente(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("empresa"),
                    rs.getString("telefono"),
                    rs.getString("email")
                );
            }
        }
    } 
    // Si rs.next() es false (no hay resultados), se devuelve null, lo cual es correcto.
    return cliente; 
}

}
