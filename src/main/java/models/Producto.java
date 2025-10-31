package com.acustica.modelos;

public class Producto {

    private int id;
    private String nombre;
    private String descripcion; // 🔑 Nuevo Campo
    private int cantidad;
    private double precioUnitario; // 🔑 Nombre Actualizado (antes costo)
    private String proveedor;

    // Constructor completo
    public Producto(int id, String nombre, String descripcion, int cantidad, double precioUnitario, String proveedor) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.proveedor = proveedor;
    }

    // Constructor para productos nuevos (sin ID)
    public Producto(String nombre, String descripcion, int cantidad, double precioUnitario, String proveedor) {
        this(0, nombre, descripcion, cantidad, precioUnitario, proveedor); // ID 0 temporal
    }

    // Getters y Setters (Asegúrate de incluirlos todos en tu archivo real)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    } // 🔑 Getter de Descripción

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    } // 🔑 Getter de Precio Unitario

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public String getProveedor() {
        return proveedor;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }
}
