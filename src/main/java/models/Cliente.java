package models;

public class Cliente {

    private int id;
    private String nombre;
    private String empresa;
    private String telefono;
    private String email;

    // Constructor completo (necesario para el SELECT)
    public Cliente(int id, String nombre, String empresa, String telefono, String email) {
        this.id = id;
        this.nombre = nombre;
        this.empresa = empresa;
        this.telefono = telefono;
        this.email = email;
    }

    // Constructor para INSERT (donde ID es 0 o se ignora)
    public Cliente(String nombre, String empresa, String telefono, String email) {
        this.nombre = nombre;
        this.empresa = empresa;
        this.telefono = telefono;
        this.email = email;
    }

    public Cliente() {
         }

    // --- GETTERS Y SETTERS ---
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

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
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
