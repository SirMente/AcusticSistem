// models/DetalleItem.java (o donde manejes tus modelos)
package models;

public class DetalleItem {

    // 0 = Servicio, 1 = Producto (Según la lógica que establecimos en el JSP)
    private int tipo;

    // ID del Servicio o Producto
    private int id;

    private int cantidad;

    // Precio Unitario (Precio base antes de calcular el subtotal por cantidad)
    private double precioUnitario;

    // --- Constructor (Opcional, pero recomendado) ---
    public DetalleItem() {
    }

    // --- Getters y Setters ---
    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}
