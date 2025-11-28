package models;

public class Cliente {

    private long docu; // CAMBIADO: Antes 'dni', ahora 'docu'
    private String nombre;
    private String telefono;
    private String email;
    private String direccion;

    public Cliente() {
    }

    public Cliente(long docu, String nombre, String direccion, String telefono, String email) {
        this.docu = docu; // CAMBIADO
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.email = email;
    }

    // GETTERS Y SETTERS

    public long getDocu() { // CAMBIADO: Antes getDni()
        return docu;
    }

    public void setDocu(long docu) { // CAMBIADO: Antes setDni()
        this.docu = docu;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
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
}