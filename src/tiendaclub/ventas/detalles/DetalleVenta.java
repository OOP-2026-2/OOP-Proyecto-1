package tiendaclub.ventas.detalles;

import tiendaclub.inventario.Producto;

public class DetalleVenta {
    private final Producto producto;
    private final int cantidad;
    private final double precioUnitario;
    private final double subtotal;

    public DetalleVenta(Producto producto, int cantidad) {
        if (producto == null) {
            throw new IllegalArgumentException("El producto es obligatorio");
        }

        if (cantidad <= 0) {
            throw new IllegalArgumentException(
                    "La cantidad debe ser mayor que cero");
        }

        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnitario = producto.getPrecio();
        this.subtotal = calcularSubtotal();
    }

    public double calcularSubtotal() {
        return precioUnitario * cantidad;
    }

    public Producto getProducto() {
        return producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public double getSubtotal() {
        return subtotal;
    }

    @Override
    public String toString() {
        return producto.getNombre()
                + " x" + cantidad
                + " - $" + subtotal;
    }
}