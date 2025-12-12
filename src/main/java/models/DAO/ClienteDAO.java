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
        // docu, nombre, direccion, telefono, email
        String sql = "SELECT docu, nombre, direccion, telefono, email FROM clientes";

        try (Connection conn = ConexionBD.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Cliente c = new Cliente(
                        rs.getLong("docu"),
                        rs.getString("nombre"),
                        rs.getString("direccion"),
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
        String sql = "INSERT INTO clientes (docu, nombre, direccion, telefono, email) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConexionBD.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            // El orden de los parámetros es CRÍTICO
            ps.setLong(1, cliente.getDocu()); // CAMBIADO: cliente.getDocu()
            ps.setString(2, cliente.getNombre());
            ps.setString(3, cliente.getDireccion());
            ps.setString(4, cliente.getTelefono());
            ps.setString(5, cliente.getEmail());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw e;
        }
    }

    // ---------------------------------------------
    // 3. EDITAR CLIENTE (UPDATE)
    // ---------------------------------------------
    public void editarCliente(Cliente cliente) throws SQLException {
        String sql = "UPDATE clientes SET nombre=?, direccion=?, telefono=?, email=? WHERE docu=?";

        try (Connection conn = ConexionBD.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, cliente.getNombre());
            ps.setString(2, cliente.getDireccion());
            ps.setString(3, cliente.getTelefono());
            ps.setString(4, cliente.getEmail());
            // El último parámetro es el docu para la cláusula WHERE
            ps.setLong(5, cliente.getDocu()); // CAMBIADO: cliente.getDocu()

            ps.executeUpdate();

        } catch (SQLException e) {
            throw e;
        }
    }

    // ---------------------------------------------
    // 4. ELIMINAR CLIENTE (DELETE)
    // ---------------------------------------------
    public void eliminarCliente(long idCliente) throws SQLException {
        String sql = "DELETE FROM clientes WHERE docu=?";

        try (Connection conn = ConexionBD.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, idCliente);

            ps.executeUpdate();

        } catch (SQLException e) {
            throw e;
        }
    }

    // ---------------------------------------------
    // 5. OBTENER CLIENTES para un <select>
    // ---------------------------------------------
    public List<Cliente> obtenerClientesParaSelect() throws SQLException {
        List<Cliente> listaClientes = new ArrayList<>();
        String sql = "SELECT docu, nombre FROM clientes ORDER BY nombre ASC";

        try (Connection conn = ConexionBD.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Cliente c = new Cliente();
                // Solo necesitamos establecer ID (docu) y Nombre
                c.setDocu(rs.getLong("docu")); // CAMBIADO: c.setDocu()
                c.setNombre(rs.getString("nombre"));

                listaClientes.add(c);
            }
        }

        return listaClientes;
    }

    // ---------------------------------------------
    // 6. OBTENER CLIENTE POR ID (SELECT simple)
    // ---------------------------------------------
    public Cliente obtenerClientePorId(long idCliente) throws SQLException {
        Cliente cliente = null;
        String sql = "SELECT docu, nombre, direccion, telefono, email FROM clientes WHERE docu = ?";

        try (Connection conn = ConexionBD.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, idCliente);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    cliente = new Cliente(
                            rs.getLong("docu"),
                            rs.getString("nombre"),
                            rs.getString("direccion"),
                            rs.getString("telefono"),
                            rs.getString("email")
                    );
                }
            }
        }
        return cliente;
    }

    public Cliente buscarClientePorDni(String docuStr) throws SQLException {
        Cliente cliente = null;
        String sql = "SELECT docu, nombre, direccion, telefono, email FROM clientes WHERE docu = ?";
        String docuLimpio = docuStr.trim();

        long docu;
        try {
            // CONVERSIÓN
            docu = Long.parseLong(docuLimpio);
        } catch (NumberFormatException e) {
            // *** SOLUCIÓN APLICADA ***
            // Si no es un número, devolvemos null y el Servlet no falla.
            System.out.println("Advertencia: El DNI/RUC no es un número válido.");
            return null; // <--- Cesa la ejecución aquí y devuelve null.
        }

        // El resto de la lógica SQL permanece igual
        try (Connection conn = ConexionBD.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, docu);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    cliente = new Cliente();
                    cliente.setDocu(rs.getLong("docu"));
                    cliente.setNombre(rs.getString("nombre"));
                    cliente.setDireccion(rs.getString("direccion"));
                    cliente.setTelefono(rs.getString("telefono"));
                    cliente.setEmail(rs.getString("email"));
                }
            }
        } // Si hay error aquí, SÍ lanza la SQLException, y el Servlet la captura (500).

        return cliente;
    }
    
    public int contarClientes() {
    String sql = "SELECT COUNT(*) FROM clientes";
    try (Connection con = ConexionBD.getConnection();
         PreparedStatement ps = con.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        if (rs.next()) {
            return rs.getInt(1);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return 0;
}


}
