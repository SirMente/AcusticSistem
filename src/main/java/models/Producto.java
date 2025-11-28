package models;

import java.math.BigDecimal;

public class Producto {

    private int id_producto;
    private String nombre;
    private String descripcion;
    private String marca;
    private String modelo;
    private int cantidad;
    private int stock_minimo;
    private BigDecimal precio_unitario; // Usar BigDecimal para precisión de dinero
    private String imagen_url;
    private Long ruc_proveedor; // Usar Long (clase envoltorio) para aceptar NULL, o long si usas 0 como default.

    // --------------------------------------------------
    // CONSTRUCTOR COMPLETO (Usado para Edición o Listado)
    // --------------------------------------------------
    public Producto(int id_producto, String nombre, String descripcion, String marca, String modelo, int cantidad,
            int stock_minimo, BigDecimal precio_unitario, String imagen_url, Long ruc_proveedor) {
        this.id_producto = id_producto;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.marca = marca;
        this.modelo = modelo;
        this.cantidad = cantidad;
        this.stock_minimo = stock_minimo;
        this.precio_unitario = precio_unitario;
        this.imagen_url = imagen_url;
        this.ruc_proveedor = ruc_proveedor;
    }

    // --------------------------------------------------
    // CONSTRUCTOR PARA INSERCIÓN (id_producto es AUTO_INCREMENT)
    // --------------------------------------------------
    public Producto(String nombre, String descripcion, String marca, String modelo, int cantidad,
            int stock_minimo, BigDecimal precio_unitario, String imagen_url, Long ruc_proveedor) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.marca = marca;
        this.modelo = modelo;
        this.cantidad = cantidad;
        this.stock_minimo = stock_minimo;
        this.precio_unitario = precio_unitario;
        this.imagen_url = imagen_url;
        this.ruc_proveedor = ruc_proveedor;
    }

    // --------------------------------------------------
    // GETTERS Y SETTERS
    // --------------------------------------------------
    public int getId_producto() {
        return id_producto;
    }

    public void setId_producto(int id_producto) {
        this.id_producto = id_producto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getStock_minimo() {
        return stock_minimo;
    }

    public void setStock_minimo(int stock_minimo) {
        this.stock_minimo = stock_minimo;
    }

    public BigDecimal getPrecio_unitario() {
        return precio_unitario;
    }

    public void setPrecio_unitario(BigDecimal precio_unitario) {
        this.precio_unitario = precio_unitario;
    }

    public String getImagen_url() {
        return imagen_url;
    }

    public void setImagen_url(String imagen_url) {
        this.imagen_url = imagen_url;
    }

    public Long getRuc_proveedor() {
        return ruc_proveedor;
    }

    public void setRuc_proveedor(Long ruc_proveedor) {
        this.ruc_proveedor = ruc_proveedor;
    }
}
