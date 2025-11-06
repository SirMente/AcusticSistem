package models;

public class Producto {

    private int id;
    private String nombre;
    private String descripcion;
    private int cantidad;
    private double precioUnitario;

    // 🔑 Campo para mostrar el NOMBRE del proveedor en el JSP
    private String proveedor;

    // 🔑 Campo para manejar la CLAVE FORÁNEA (id_proveedor en la DB)
    private int idProveedor;

    private String imagenUrl;

    // CONSTRUCTOR VACÍO 
    public Producto() {
    }

    // Constructor completo
    public Producto(int id, String nombre, String descripcion, int cantidad, double precioUnitario, String proveedor, int idProveedor, String imagenUrl) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.proveedor = proveedor;
        this.idProveedor = idProveedor;
        this.imagenUrl = imagenUrl;
    }

    // Constructor para productos nuevos (sin ID y sin ID Proveedor)
    public Producto(String nombre, String descripcion, int cantidad, double precioUnitario, String proveedor, String imagenUrl) {
        this(0, nombre, descripcion, cantidad, precioUnitario, proveedor, 0, imagenUrl);
    }

    // --- Getters y Setters ---
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

    // 🔑 Nuevos Getter/Setter para el ID del Proveedor
    public int getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(int idProveedor) {
        this.idProveedor = idProveedor;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }
}
