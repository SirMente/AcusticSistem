package models;

// Importar BigDecimal para manejar tipos de moneda
import java.math.BigDecimal;

/**
 * Clase que representa la entidad Servicio en la base de datos. Mapea la tabla
 * Servicio (id_servicio, nombre, descripcion, tarifa_base).
 */
public class Servicio {

    private int id_servicio;
    private String nombre;
    private String descripcion;
    private BigDecimal tarifa_base; // Usamos BigDecimal para precisión en moneda

    // Constructor vacío
    public Servicio() {
    }

    // Constructor completo
    public Servicio(int id_servicio, String nombre, String descripcion, BigDecimal tarifa_base) {
        this.id_servicio = id_servicio;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.tarifa_base = tarifa_base;
    }

    // Getters y Setters
    public int getId_servicio() {
        return id_servicio;
    }

    public void setId_servicio(int id_servicio) {
        this.id_servicio = id_servicio;
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

    public BigDecimal getTarifa_base() {
        return tarifa_base;
    }

    public void setTarifa_base(BigDecimal tarifa_base) {
        this.tarifa_base = tarifa_base;
    }

    @Override
    public String toString() {
        return "Servicio{"
                + "id_servicio=" + id_servicio
                + ", nombre='" + nombre + '\''
                + ", descripcion='" + descripcion + '\''
                + ", tarifa_base=" + tarifa_base
                + '}';
    }
}
