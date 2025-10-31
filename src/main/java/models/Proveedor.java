package models;

public class Proveedor {
    
    private int idProveedor;
    private String nombre;
    private String email;      // 🔑 Campo de Contacto: Email
    private String telefono;   // 🔑 Campo de Contacto: Teléfono
    private String tipoProducto;

    // Constructor vacío
    public Proveedor() {}

    // Constructor con campos (actualizado)
    public Proveedor(String nombre, String email, String telefono, String tipoProducto) {
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
        this.tipoProducto = tipoProducto;
    }

    // Getters y Setters (Nuevos/Modificados)
    public int getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(int idProveedor) {
        this.idProveedor = idProveedor;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getTipoProducto() {
        return tipoProducto;
    }

    public void setTipoProducto(String tipoProducto) {
        this.tipoProducto = tipoProducto;
    }
}