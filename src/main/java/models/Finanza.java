package models;

import java.util.Date;

public class Finanza {

    private int id;
    private Date fecha;
    private String categoria;
    private String tipo; // ingreso o gasto
    private String descripcion;
    private double monto;

    public Finanza() {
    }

    public Finanza(int id, Date fecha, String categoria, String tipo, String descripcion, double monto) {
        this.id = id;
        this.fecha = fecha;
        this.categoria = categoria;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.monto = monto;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }
}
