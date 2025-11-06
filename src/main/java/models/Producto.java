package models;

public class Producto {

    private int id;
    private String nombre;
    private String descripcion; 
    private int cantidad;
    private double precioUnitario; 
    private String proveedor;
    private String imagenUrl;

    // CONSTRUCTOR VACÍO 
    public Producto() {
        // Necesario para que el ProductoDAO pueda hacer: Producto p = new Producto();
    }

    // Constructor completo
    public Producto(int id, String nombre, String descripcion, int cantidad, double precioUnitario, String proveedor, String imagenUrl) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.proveedor = proveedor;
        this.imagenUrl = imagenUrl;
    }

    // Constructor para productos nuevos (sin ID)
    public Producto(String nombre, String descripcion, int cantidad, double precioUnitario, String proveedor, String imagenUrl) {
        this(0, nombre, descripcion, cantidad, precioUnitario, proveedor, imagenUrl); // ID 0 temporal
    }

    // Getters y Setters
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
    }

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
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public String getProveedor() {
        return proveedor;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }
}
