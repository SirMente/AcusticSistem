package models;

public class Proveedor {

    // Cambiado: La clave primaria es RUC (INT)
    private long ruc;
    private String nombre;
    private String tipoProducto; // Mapea a tipo_producto
    private String email;
    private String telefono;
    private String direccion; // Nuevo campo

    // Constructor vacío
    public Proveedor() {
    }

    // Constructor completo para SELECT (con RUC/PK)
    public Proveedor(long ruc, String nombre, String tipoProducto, String email, String telefono, String direccion) {
        this.ruc = ruc;
        this.nombre = nombre;
        this.tipoProducto = tipoProducto;
        this.email = email;
        this.telefono = telefono;
        this.direccion = direccion;
    }

    // Constructor para INSERT (sin RUC/PK si la base de datos lo genera, pero lo necesitamos para la validación si se ingresa manualmente)
    // Usaremos el constructor completo para mantener la coherencia.
    // GETTERS y SETTERS
    // CAMBIADO: getIdProveedor() a getRuc()
    public long getRuc() {
        return ruc;
    }

    // CAMBIADO: setIdProveedor() a setRuc()
    public void setRuc(long ruc) {
        this.ruc = ruc;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipoProducto() {
        return tipoProducto;
    }

    public void setTipoProducto(String tipoProducto) {
        this.tipoProducto = tipoProducto;
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

    // NUEVO: Getter y Setter para Dirección
    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
}
