package models;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

public class Proforma {

    // Campos basados en la tabla Proforma
    private String idProforma;
    private Long idCliente; // Usado como FK
    private String dniCliente; // Campo extra para la vista/lógica de negocio
    private Date fechaProforma;
    private String estado; // ENUM: PENDIENTE, PAGADA_PARCIAL, PAGADA_TOTAL, CANCELADA
    private BigDecimal subtotal;
    private BigDecimal impuestos;
    private BigDecimal total;

    // Campos de la vista (para creación rápida)
    private String descripcionServicio;

    private List<DetalleProforma> detalles; // NUEVO

    // Constructor vacío
    public Proforma() {
    }

    // Constructor para mostrar la lista (se requieren datos del cliente)
    public Proforma(String idProforma, String dniCliente, Date fechaProforma, BigDecimal total, String estado) {
        this.idProforma = idProforma;
        this.dniCliente = dniCliente;
        this.fechaProforma = fechaProforma;
        this.total = total;
        this.estado = estado;
    }

    // --- Getters y Setters ---
    public String getIdProforma() {
        return idProforma;
    }

    public void setIdProforma(String idProforma) {
        this.idProforma = idProforma;
    }

    public Long getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Long idCliente) {
        this.idCliente = idCliente;
    }

    public String getDniCliente() {
        return dniCliente;
    }

    public void setDniCliente(String dniCliente) {
        this.dniCliente = dniCliente;
    }

    public Date getFechaProforma() {
        return fechaProforma;
    }

    public void setFechaProforma(Date fechaProforma) {
        this.fechaProforma = fechaProforma;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getImpuestos() {
        return impuestos;
    }

    public void setImpuestos(BigDecimal impuestos) {
        this.impuestos = impuestos;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getDescripcionServicio() {
        return descripcionServicio;
    }

    public void setDescripcionServicio(String descripcionServicio) {
        this.descripcionServicio = descripcionServicio;
    }

    public List<DetalleProforma> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleProforma> detalles) {
        this.detalles = detalles;
    }
}
