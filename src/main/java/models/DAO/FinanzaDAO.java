package models.DAO;

import models.Finanza;
import utils.ConexionBD;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class FinanzaDAO {

    public boolean agregarFinanza(Finanza f) {
        String sql = "INSERT INTO finanzas(fecha, categoria, tipo, descripcion, monto) VALUES(?,?,?,?,?)";
        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDate(1, new java.sql.Date(f.getFecha().getTime()));
            ps.setString(2, f.getCategoria());
            ps.setString(3, f.getTipo());
            ps.setString(4, f.getDescripcion());
            ps.setDouble(5, f.getMonto());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Finanza> listarFinanzas() {
        List<Finanza> lista = new ArrayList<>();
        String sql = "SELECT * FROM finanzas ORDER BY fecha DESC";

        try (Connection con = ConexionBD.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Finanza f = new Finanza();
                f.setId(rs.getInt("id"));
                f.setFecha(rs.getDate("fecha"));
                f.setCategoria(rs.getString("categoria"));
                f.setTipo(rs.getString("tipo"));
                f.setDescripcion(rs.getString("descripcion"));
                f.setMonto(rs.getDouble("monto"));
                lista.add(f);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public double calcularTotalIngresos() {
        double total = 0;
        String sql = "SELECT SUM(monto) as total FROM finanzas WHERE tipo='ingreso'";
        try (Connection con = ConexionBD.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) total = rs.getDouble("total");
        } catch (SQLException e) { e.printStackTrace(); }
        return total;
    }

    public double calcularTotalGastos() {
        double total = 0;
        String sql = "SELECT SUM(monto) as total FROM finanzas WHERE tipo='gasto'";
        try (Connection con = ConexionBD.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) total = rs.getDouble("total");
        } catch (SQLException e) { e.printStackTrace(); }
        return total;
    }
    
    public void registrarProformaComoIngreso(String idProforma) {
    String sqlProforma = "SELECT * FROM proformas WHERE id_proforma=? AND estado='PAGADA_TOTAL'";
    try (Connection con = ConexionBD.getConnection();
         PreparedStatement ps = con.prepareStatement(sqlProforma)) {

        ps.setString(1, idProforma);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            // Crear registro en finanzas
            String insertFinanza = "INSERT INTO finanzas(fecha, categoria, tipo, descripcion, monto) VALUES(?,?,?,?,?)";
            try (PreparedStatement psFin = con.prepareStatement(insertFinanza)) {
                psFin.setDate(1, rs.getDate("fecha_proforma"));
                psFin.setString(2, "Ventas");
                psFin.setString(3, "ingreso");
                psFin.setString(4, "Proforma ID: " + idProforma);
                psFin.setDouble(5, rs.getDouble("total"));
                psFin.executeUpdate();
            }
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }
}
    
    public Map<String, Double> obtenerIngresosMensuales() {
    Map<String, Double> datos = new LinkedHashMap<>();

    String sql = """
        SELECT DATE_FORMAT(fecha, '%Y-%m') AS mes, SUM(monto) 
        FROM finanzas 
        WHERE tipo = 'INGRESO'
        GROUP BY mes
        ORDER BY mes;
    """;

    try (Connection con = ConexionBD.getConnection();
         PreparedStatement ps = con.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            datos.put(rs.getString("mes"), rs.getDouble(2));
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return datos;
}

    public Map<String, Double> obtenerGastosMensuales() {
    Map<String, Double> datos = new LinkedHashMap<>();

    String sql = """
        SELECT DATE_FORMAT(fecha, '%Y-%m') AS mes, SUM(monto) 
        FROM finanzas 
        WHERE tipo = 'GASTO'
        GROUP BY mes
        ORDER BY mes;
    """;

    try (Connection con = ConexionBD.getConnection();
         PreparedStatement ps = con.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            datos.put(rs.getString("mes"), rs.getDouble(2));
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return datos;
}

    
}
