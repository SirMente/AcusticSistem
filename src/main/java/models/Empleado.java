package models;

// Clase POJO para la entidad Personal/Empleado
public class Empleado {

    private long dni; // Clave Primaria
    private String nombre;
    private String apellido;
    private String telefono;
    private String email;
    private String puesto;

    // Constructor vacío
    public Empleado() {
    }

    // Constructor completo
    public Empleado(long dni, String nombre, String apellido, String telefono, String email, String puesto) {
        this.dni = dni;
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.email = email;
        this.puesto = puesto;
    }

    // GETTERS y SETTERS
    public long getDni() {
        return dni;
    }

    public void setDni(long dni) {
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPuesto() {
        return puesto;
    }

    public void setPuesto(String puesto) {
        this.puesto = puesto;
    }
}
