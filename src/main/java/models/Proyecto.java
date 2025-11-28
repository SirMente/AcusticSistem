package models;

import java.sql.Date;

/**
 * Clase que representa la entidad (modelo) de la tabla 'proyectos'.
 * Actualizada para reflejar idProforma como String.
 */
public class Proyecto {
    private int idProyecto;
    private String idProforma; // Cambiado a String
    private String nombreProyecto;
    private Date fechaInicio;
    private Date fechaFinEstimada;
    private String estado; // 'Planificacion', 'Instalacion', 'Pruebas', 'Entrega'
    private long dniEncargado;

    // Campos de apoyo para la vista (no son columnas de la tabla 'proyectos')
    private long rucCliente; // Ruc del cliente asociado a la proforma
    private String nombreEncargado;

    // Constructor vacío
    public Proyecto() {}

    // Constructor completo (sin idProyecto para INSERT)
    public Proyecto(String idProforma, String nombreProyecto, Date fechaInicio, Date fechaFinEstimada, long dniEncargado) {
        this.idProforma = idProforma;
        this.nombreProyecto = nombreProyecto;
        this.fechaInicio = fechaInicio;
        this.fechaFinEstimada = fechaFinEstimada;
        this.dniEncargado = dniEncargado;
        this.estado = "Planificacion"; // Valor por defecto al crear
    }

    // --- Getters y Setters ---

    public int getIdProyecto() {
        return idProyecto;
    }

    public void setIdProyecto(int idProyecto) {
        this.idProyecto = idProyecto;
    }

    public String getIdProforma() {
        return idProforma;
    }

    public void setIdProforma(String idProforma) {
        this.idProforma = idProforma;
    }

    public String getNombreProyecto() {
        return nombreProyecto;
    }

    public void setNombreProyecto(String nombreProyecto) {
        this.nombreProyecto = nombreProyecto;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFinEstimada() {
        return fechaFinEstimada;
    }

    public void setFechaFinEstimada(Date fechaFinEstimada) {
        this.fechaFinEstimada = fechaFinEstimada;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public long getDniEncargado() {
        return dniEncargado;
    }

    public void setDniEncargado(long dniEncargado) {
        this.dniEncargado = dniEncargado;
    }

    public long getRucCliente() {
        return rucCliente;
    }

    public void setRucCliente(long rucCliente) {
        this.rucCliente = rucCliente;
    }

    public String getNombreEncargado() {
        return nombreEncargado;
    }

    public void setNombreEncargado(String nombreEncargado) {
        this.nombreEncargado = nombreEncargado;
    }
}