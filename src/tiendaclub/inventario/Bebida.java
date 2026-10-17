package tiendaclub.inventario;

public class Bebida extends Producto {
    private final boolean caliente;

    public Bebida(String nombre, double precio, boolean caliente) {
        super(nombre, precio);
        this.caliente = caliente;
    }

    public boolean isCaliente() {
        return caliente;
    }

    @Override
    public String toString() {
        return super.toString()
                + (caliente
                        ? " - caliente"
                        : " - fría");
    }
}