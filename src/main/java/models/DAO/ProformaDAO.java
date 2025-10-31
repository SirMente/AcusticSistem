package models.DAO;

import models.Proforma;
import utils.ConexionBD; // Asumo que esta es tu clase de conexión
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ProformaDAO {

    /**
     * Agrega una nueva proforma a la base de datos.
     *
     * @param proforma El objeto Proforma a agregar.
     * @return true si la operación fue exitosa, false en caso contrario.
     */
    // Método para obtener una lista de todas las proformas de la base de datos
    public List<Proforma> listarProformas() throws SQLException {
        List<Proforma> listaProformas = new ArrayList<>();
        // 🔑 Verifica que "Proformas" y los nombres de las columnas sean correctos
        String SQL_SELECT = "SELECT id_proforma, id_cliente, descripcion_servicio, presupuesto, fecha_emision FROM proforma ORDER BY fecha_emision DESC";

        try (Connection conn = ConexionBD.getConnection(); // <--- ¡Asegúrate que esto funcione!
                 PreparedStatement ps = conn.prepareStatement(SQL_SELECT); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Proforma proforma = new Proforma();

                // 🔑 Los nombres de las columnas DEBEN COINCIDIR EXACTAMENTE con la base de datos
                proforma.setIdProforma(rs.getInt("id_proforma"));
                proforma.setIdCliente(rs.getInt("id_cliente"));
                proforma.setDescripcionServicio(rs.getString("descripcion_servicio"));
                proforma.setPresupuesto(rs.getBigDecimal("presupuesto"));
                proforma.setFechaEmision(rs.getDate("fecha_emision"));

                listaProformas.add(proforma);
            }
            // Agrega una impresión de prueba aquí para ver el resultado en el servidor
            System.out.println("DAO DEBUG: Filas encontradas: " + listaProformas.size());

        } catch (SQLException e) {
            // ... manejo de error ...
            throw e;
        }
        return listaProformas;
    }

    public boolean agregarProforma(Proforma proforma) {
        // 🔑 Asegúrate que los nombres de las columnas coincidan con tu tabla
        String sql = "INSERT INTO Proforma (id_cliente, descripcion_servicio, presupuesto, fecha_emision) VALUES (?, ?, ?, ?)";

        Connection conn = null;
        PreparedStatement ps = null;

        try {
            // 1. Obtener la conexión
            conn = ConexionBD.getConnection(); // O tu método de conexión
            ps = conn.prepareStatement(sql);

            // 2. Establecer los parámetros
            ps.setInt(1, proforma.getIdCliente());
            ps.setString(2, proforma.getDescripcionServicio());
            ps.setBigDecimal(3, proforma.getPresupuesto());
            ps.setDate(4, proforma.getFechaEmision());

            // 3. Ejecutar la inserción
            int filasAfectadas = ps.executeUpdate();

            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("Error al agregar la proforma: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            // 4. Cerrar recursos (código omitido por brevedad, pero necesario)
        }
    }
    // Aquí irían otros métodos como obtenerTodasProformas(), editarProforma(), etc.
}
