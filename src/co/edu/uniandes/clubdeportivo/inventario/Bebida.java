package co.edu.uniandes.clubdeportivo.inventario;

public class Bebida extends Producto {
    private boolean caliente;

    public Bebida(String nombre, double precio, boolean caliente) {
        super(nombre, precio);
        this.caliente = caliente;
    }

    public boolean isCaliente() {
        return caliente;
    }
}
