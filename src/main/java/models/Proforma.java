package models;

// Importaciones necesarias para manejar fechas y decimales
import java.sql.Date; 
import java.sql.Timestamp;
import java.math.BigDecimal; // Recomendado para manejar dinero (presupuesto)

public class Proforma {
    private int idProforma;
    private int idCliente; // Para la clave foránea del cliente
    private String descripcionServicio;
    private BigDecimal presupuesto; // Usamos BigDecimal para manejar valores monetarios
    private Date fechaEmision;
    private String estado;
    private Timestamp fechaCreacion;

    // Constructor vacío
    public Proforma() {
    }

    // Constructor con campos obligatorios para AGREGAR (sin ID y sin fecha_creacion)
    public Proforma(int idCliente, String descripcionServicio, BigDecimal presupuesto, Date fechaEmision) {
        this.idCliente = idCliente;
        this.descripcionServicio = descripcionServicio;
        this.presupuesto = presupuesto;
        this.fechaEmision = fechaEmision;
    }
    
    // Constructor completo (usado generalmente al obtener datos de la BD)
    public Proforma(int idProforma, int idCliente, String descripcionServicio, BigDecimal presupuesto, Date fechaEmision, String estado, Timestamp fechaCreacion) {
        this.idProforma = idProforma;
        this.idCliente = idCliente;
        this.descripcionServicio = descripcionServicio;
        this.presupuesto = presupuesto;
        this.fechaEmision = fechaEmision;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
    }

    // --- Getters y Setters ---

    public int getIdProforma() {
        return idProforma;
    }

    public void setIdProforma(int idProforma) {
        this.idProforma = idProforma;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getDescripcionServicio() {
        return descripcionServicio;
    }

    public void setDescripcionServicio(String descripcionServicio) {
        this.descripcionServicio = descripcionServicio;
    }

    public BigDecimal getPresupuesto() {
        return presupuesto;
    }

    public void setPresupuesto(BigDecimal presupuesto) {
        this.presupuesto = presupuesto;
    }

    public Date getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Timestamp getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Timestamp fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}