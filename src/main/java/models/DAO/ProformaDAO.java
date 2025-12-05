package models.DAO;

import models.Proforma;
import utils.ConexionBD;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;
import java.math.RoundingMode;
import models.DetalleItem;
import models.DetalleProforma;

public class ProformaDAO {

    // Método para obtener la conexión
    private Connection getConnection() throws SQLException {
        Connection conn = ConexionBD.getConnection();
        if (conn == null) {
            throw new SQLException("No se pudo establecer la conexión a la base de datos.");
        }
        return conn;
    }

    // Método para generar un ID de Proforma (se mantiene igual)
    private String generarNuevoId() throws SQLException {
        String newId = null;
        String query = "SELECT MAX(id_proforma) FROM Proforma WHERE id_proforma LIKE CONCAT('PF-', YEAR(CURDATE()), '-%')";

        try (Connection conn = getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {

            int contador = 1;
            if (rs.next() && rs.getString(1) != null) {
                String ultimoId = rs.getString(1);
                String[] partes = ultimoId.split("-");
                if (partes.length == 3) {
                    contador = Integer.parseInt(partes[2]) + 1;
                }
            }
            newId = String.format("PF-%d-%03d", java.time.Year.now().getValue(), contador);
        }
        return newId;
    }

    // ----------------------------------------------------------------------------------
    // CORRECCIÓN 1: Renombrar y usar 'docu' como ID
    // ----------------------------------------------------------------------------------
    /**
     * Busca el id_cliente (docu/Long) por su DNI/RUC (docuStr). ASUMIMOS que el
     * ID del cliente es el mismo campo 'docu' (BIGINT).
     */
    private Long buscarIdClientePorDocu(String docuStr) throws SQLException {
        // Query CORREGIDA: Usamos la tabla 'clientes' y la columna 'docu' como ID.
        String sql = "SELECT docu FROM clientes WHERE docu = ?";

        Long idCliente = null;
        long docu = 0; // Se usará para el setLong

        try {
            // Convertimos el DNI/RUC (String) a Long
            docu = Long.parseLong(docuStr);
        } catch (NumberFormatException e) {
            throw new SQLException("El formato del DNI/RUC proporcionado no es válido. Debe ser un número entero.", e);
        }

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, docu); // Usamos el Long convertido

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // El ID del cliente es el mismo campo 'docu'
                    idCliente = rs.getLong("docu");
                }
            }
        }
        return idCliente;
    }

    /**
     * Registra una nueva Proforma (AJUSTADO para usar docu como ID/Long).
     */
    public boolean agregarProforma(Proforma proforma) throws SQLException {

        String docuClienteStr = proforma.getDniCliente(); // Paso 1: Obtener DNI/RUC (llamado 'dniCliente' en el modelo Proforma, pero contiene el docu)

        // Paso 2: Buscar el id_cliente (docu/Long)
        Long idCliente = buscarIdClientePorDocu(docuClienteStr); // <--- CAMBIO DE MÉTODO

        if (idCliente == null) {
            // Cliente no existe, lanzamos error para el controlador
            throw new SQLException("El cliente con DNI/RUC " + docuClienteStr + " no está registrado.");
        }

        proforma.setIdCliente(idCliente); // Establecer el FK (Long) en el objeto

        // 3. Generar ID y calcular impuestos/subtotal
        proforma.setIdProforma(generarNuevoId());

        BigDecimal total = proforma.getTotal();
        BigDecimal div = new BigDecimal("1.18");
        BigDecimal subtotal = total.divide(div, 2, RoundingMode.HALF_UP);
        BigDecimal totalImpuestos = total.subtract(subtotal);

        // 4. Insertar en la tabla Proforma
        String sql = "INSERT INTO Proforma (id_proforma, ruc_cliente, fecha_proforma, estado, subtotal, impuestos, total) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, proforma.getIdProforma());
            ps.setLong(2, proforma.getIdCliente()); // <-- Se inserta el docu/id_cliente (Long)
            ps.setDate(3, proforma.getFechaProforma());
            ps.setString(4, "PENDIENTE");
            ps.setBigDecimal(5, subtotal);
            ps.setBigDecimal(6, totalImpuestos);
            ps.setBigDecimal(7, total);

            return ps.executeUpdate() > 0;

        }
    }

    // ----------------------------------------------------------------------------------
    // CORRECCIÓN 2: Usar 'docu' en el SELECT y en la Proforma
    // ----------------------------------------------------------------------------------
    /**
     * Obtiene todas las proformas para la tabla de la JSP. Hace JOIN para
     * obtener el DNI/RUC (docu) y mostrarlo.
     */
    public List<Proforma> obtenerTodasLasProformas() throws SQLException {
        List<Proforma> lista = new ArrayList<>();
        // JOIN con clientes para obtener el DOCU que pide la vista
        // La columna en clientes es 'docu' y la columna FK en Proforma es 'id_cliente'
        String sql = "SELECT p.id_proforma, c.docu, p.fecha_proforma, p.total, p.estado " // <--- CAMBIO: SELECCIONAR 'c.docu'
                + "FROM Proforma p JOIN clientes c ON p.ruc_cliente = c.docu " // <--- CAMBIO: JOIN CON 'c.docu' (PK del cliente)
                + "ORDER BY p.fecha_proforma DESC";

        try (Connection conn = getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Proforma p = new Proforma(
                        rs.getString("id_proforma"),
                        rs.getString("docu"), // <--- CAMBIO: Usamos el "docu" del JOIN
                        rs.getDate("fecha_proforma"),
                        rs.getBigDecimal("total"),
                        rs.getString("estado")
                );
                lista.add(p);
            }
        }
        return lista;
    }

    private final String SQL_REDUCIR_STOCK = "UPDATE Producto SET cantidad = cantidad - ? WHERE id_producto = ?";

    public boolean agregarProformaConDetalles(Proforma proforma, List<DetalleItem> detalles) throws SQLException {
        Connection conn = null;
        boolean exito = false;

        // Asumo que tienes una tabla llamada 'detalle_proforma'
        final String SQL_INSERT_DETALLE = "INSERT INTO detalle_proforma (id_proforma, tipo, id_producto_o_servicio, cantidad, precio_unitario, subtotal) VALUES (?, ?, ?, ?, ?, ?)";

        try {
            conn = getConnection();
            conn.setAutoCommit(false); // 1. Iniciar Transacción

            // 1.1. Obtener la Proforma ID y calcular Subtotal/Impuestos
            String newId = generarNuevoId();
            proforma.setIdProforma(newId);

            // 1.2. Buscar el id_cliente (docu/Long)
            Long idCliente = buscarIdClientePorDocu(proforma.getDniCliente());
            if (idCliente == null) {
                throw new SQLException("El cliente con DNI/RUC " + proforma.getDniCliente() + " no está registrado.");
            }
            proforma.setIdCliente(idCliente);

            // 1.3. Calcular Subtotal e Impuestos
            BigDecimal total = proforma.getTotal().setScale(2, RoundingMode.HALF_UP);
            BigDecimal div = new BigDecimal("1.18");
            BigDecimal subtotal = total.divide(div, 2, RoundingMode.HALF_UP);
            BigDecimal totalImpuestos = total.subtract(subtotal);

            // 2. Insertar Proforma principal
            String sqlProforma = "INSERT INTO Proforma (id_proforma, ruc_cliente, fecha_proforma, estado, subtotal, impuestos, total) VALUES (?, ?, ?, ?, ?, ?, ?)";
            int filasAfectadasProforma;

            try (PreparedStatement psProforma = conn.prepareStatement(sqlProforma)) {
                psProforma.setString(1, proforma.getIdProforma());
                psProforma.setLong(2, proforma.getIdCliente());
                psProforma.setDate(3, proforma.getFechaProforma());
                psProforma.setString(4, "PENDIENTE"); // Estado inicial
                psProforma.setBigDecimal(5, subtotal);
                psProforma.setBigDecimal(6, totalImpuestos);
                psProforma.setBigDecimal(7, total);

                filasAfectadasProforma = psProforma.executeUpdate();
            }

            if (filasAfectadasProforma == 0) {
                conn.rollback();
                return false;
            }

            // 3. Insertar Detalles de la Proforma (Lote 1) y Reducir Stock (Lote 2)
            try (PreparedStatement psDetalle = conn.prepareStatement(SQL_INSERT_DETALLE); PreparedStatement psStock = conn.prepareStatement(SQL_REDUCIR_STOCK)) { // Nuevo PreparedStatement para el stock

                for (DetalleItem item : detalles) {

                    // --- LÓGICA DE INSERCIÓN DE DETALLE (Paso 3.1) ---
                    // (Tu código para calcular itemSubtotal y setear los 6 parámetros del psDetalle.addBatch())
                    // El subtotal del item es el (PrecioUnitario * Cantidad)
                    BigDecimal itemSubtotal = BigDecimal.valueOf(item.getPrecioUnitario())
                            .multiply(BigDecimal.valueOf(item.getCantidad()))
                            .setScale(2, RoundingMode.HALF_UP);

                    psDetalle.setString(1, proforma.getIdProforma());
                    psDetalle.setInt(2, item.getTipo()); // 0=Servicio, 1=Producto
                    psDetalle.setInt(3, item.getId());
                    psDetalle.setInt(4, item.getCantidad());
                    psDetalle.setBigDecimal(5, BigDecimal.valueOf(item.getPrecioUnitario()));
                    psDetalle.setBigDecimal(6, itemSubtotal);

                    psDetalle.addBatch(); // Lote 1: Agregar detalle

                    // --- LÓGICA DE REDUCCIÓN DE STOCK (Paso 3.2 - SOLO PARA PRODUCTOS) ---
                    if (item.getTipo() == 1) { // 1 = Producto
                        // Reducir la cantidad de stock
                        psStock.setInt(1, item.getCantidad());  // Parámetro 1: Cantidad a restar
                        psStock.setInt(2, item.getId());        // Parámetro 2: id_producto
                        psStock.addBatch(); // Lote 2: Agregar reducción de stock
                    }
                    // Si es Servicio (tipo=0), no se toca el stock.
                }

                // 4. Ejecutar los lotes
                psDetalle.executeBatch(); // Ejecuta todas las inserciones de detalle (Lote 1)

                // ⚠️ Importante: Verificar si hay productos para actualizar el stock.
                int[] resultadosStock = psStock.executeBatch(); // Ejecuta todas las actualizaciones de stock (Lote 2)

                // 5. Verificar que ambas operaciones fueron exitosas
                // 5.1. Verificar Detalles
                // (Tu código de verificación de resultadosLote es correcto, se aplicaría al Lote 1)
                // Ya que psDetalle.executeBatch() no devuelve los resultados directamente aquí, 
                // lo haremos implícitamente asumiendo que el método executeBatch lanza SQLException si algo falla
                // o lo verificamos si lo guardas en una variable: 
                // int[] resultadosLote = psDetalle.executeBatch();
                // (Si usas la verificación de lotes, hazlo aquí para el Lote 1 y 2)
                // 5.2. Verificar Stock (Opcional, pero recomendado si el stock es crítico)
                // El tamaño de resultadosStock debe coincidir con el número de ítems de tipo Producto
                for (int resultado : resultadosStock) {
                    if (resultado <= 0 && resultado != Statement.SUCCESS_NO_INFO) {
                        // Esto significa que un UPDATE afectó 0 filas, quizás el id_producto no existía
                        throw new SQLException("Fallo al actualizar el stock de un producto. Posible ID no encontrado.");
                    }
                }

            }

            // 4. Si todo OK, confirmar cambios
            conn.commit();
            exito = true;

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // 5. Si hay error, deshacer cambios
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            throw e;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true); // Restaurar el modo autocommit
                    conn.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }

        return exito;
    }

    public boolean actualizarEstadoProforma(String idProforma, String nuevoEstado) throws SQLException {
        // Asegúrate de que el estado proporcionado es uno de los válidos (opcionalmente puedes validarlo aquí, pero es mejor hacerlo en el Controller/Service)
        String sql = "UPDATE Proforma SET estado = ? WHERE id_proforma = ?";

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nuevoEstado);
            ps.setString(2, idProforma);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al actualizar el estado de la proforma " + idProforma + ": " + e.getMessage());
            throw e;
        }
    }

    public Proforma obtenerProformaConDetalles(String idProforma) throws SQLException {
        Proforma proforma = null;

        String sqlProforma = "SELECT p.id_proforma, c.docu, p.fecha_proforma, p.total, p.estado "
                + "FROM Proforma p "
                + "JOIN clientes c ON p.ruc_cliente = c.docu "
                + "WHERE p.id_proforma = ?";

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sqlProforma)) {

            ps.setString(1, idProforma);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    proforma = new Proforma(
                            rs.getString("id_proforma"),
                            rs.getString("docu"),
                            rs.getDate("fecha_proforma"),
                            rs.getBigDecimal("total"),
                            rs.getString("estado")
                    );
                } else {
                    return null; // No existe la proforma
                }
            }
        }

        // Obtener los detalles
        String sqlDetalles = "SELECT id_detalle, id_producto_o_servicio, tipo, cantidad, precio_unitario "
                + "FROM detalle_proforma WHERE id_proforma = ?";

        List<DetalleProforma> detalles = new ArrayList<>();

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sqlDetalles)) {

            ps.setString(1, idProforma);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    DetalleProforma detalle = new DetalleProforma();
                    detalle.setIdDetalle(rs.getLong("id_detalle"));
                    detalle.setIdProforma(idProforma);

                    int tipo = rs.getInt("tipo");
                    if (tipo == 0) { // Servicio
                        detalle.setIdServicio(rs.getInt("id_producto_o_servicio"));
                    } else { // Producto
                        detalle.setIdProducto(rs.getInt("id_producto_o_servicio"));
                    }

                    detalle.setCantidad(rs.getInt("cantidad"));
                    detalle.setPrecioVenta(rs.getBigDecimal("precio_unitario"));
                    detalles.add(detalle);
                }
            }
        }

        proforma.setDetalles(detalles);
        return proforma;
    }

    public boolean actualizarEstadoYRegistrarIngreso(String idProforma, String nuevoEstado) throws SQLException {
        String sql = "UPDATE Proforma SET estado = ? WHERE id_proforma = ?";

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nuevoEstado);
            ps.setString(2, idProforma);

            int filasAfectadas = ps.executeUpdate();

            // ⚡ Si se actualizó correctamente y el estado es PAGADA_TOTAL, registrar ingreso en finanzas
            if (filasAfectadas > 0 && "PAGADA_TOTAL".equals(nuevoEstado)) {
                FinanzaDAO finanzaDAO = new FinanzaDAO();
                finanzaDAO.registrarProformaComoIngreso(idProforma);
            }

            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("Error al actualizar el estado de la proforma " + idProforma + ": " + e.getMessage());
            throw e;
        }
    }

}
