package tiendaclub.inventario;

public abstract class Producto {
    private final String nombre;
    private double precio;

    public Producto(String nombre, double precio) {
        if (nombre == null
                || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "El nombre del producto es obligatorio");
        }

        if (precio < 0) {
            throw new IllegalArgumentException(
                    "El precio no puede ser negativo");
        }

        this.nombre = nombre;
        this.precio = precio;
    }

    public void cambiarPrecio(double nuevoPrecio) {

        if (nuevoPrecio < 0) {
            throw new IllegalArgumentException(
                    "El precio no puede ser negativo");
        }

        this.precio = nuevoPrecio;
    }

    public String getNombre() {
        return nombre;
    }

    public double getPrecio() {
        return precio;
    }

    @Override
    public String toString() {
        return nombre + " - $" + precio;
    }
}
