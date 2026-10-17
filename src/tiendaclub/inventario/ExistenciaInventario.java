package tiendaclub.inventario;

import utilidades.excepciones.InventarioInsuficienteException;

public class ExistenciaInventario {
    private final Producto producto;
    private final UbicacionInventario ubicacion;
    private int cantidad;

    public ExistenciaInventario(Producto producto, UbicacionInventario ubicacion, int cantidadInicial) {
        if (producto == null) {
            throw new IllegalArgumentException(
                    "El producto es obligatorio");
        }

        if (ubicacion == null) {
            throw new IllegalArgumentException(
                    "La ubicación es obligatoria");
        }

        if (cantidadInicial < 0) {
            throw new IllegalArgumentException(
                    "La cantidad inicial no puede ser negativa");
        }

        this.producto = producto;
        this.ubicacion = ubicacion;
        this.cantidad = cantidadInicial;
    }

    public void aumentarCantidad(int cantidad) {

        if (cantidad <= 0) {
            throw new IllegalArgumentException(
                    "La cantidad debe ser mayor que cero");
        }

        this.cantidad += cantidad;
    }

    public void disminuirCantidad(int cantidad) {

        if (cantidad <= 0) {
            throw new IllegalArgumentException(
                    "La cantidad debe ser mayor que cero");
        }

        if (!tieneCantidad(cantidad)) {
            throw new InventarioInsuficienteException(
                    "No hay suficientes unidades disponibles");
        }

        this.cantidad -= cantidad;
    }

    public boolean tieneCantidad(int cantidad) {
        return cantidad > 0
                && this.cantidad >= cantidad;
    }

    /*
     * Estos dos métodos se conservan temporalmente
     * para no dañar el código anterior.
     */

    public void agregarUnidades(int unidades) {
        aumentarCantidad(unidades);
    }

    public void retirarUnidades(int unidades) {
        disminuirCantidad(unidades);
    }

    public Producto getProducto() {
        return producto;
    }

    public UbicacionInventario getUbicacion() {
        return ubicacion;
    }

    public int getCantidad() {
        return cantidad;
    }

    @Override
    public String toString() {
        return producto.getNombre()
                + " en "
                + ubicacion.getNombre()
                + ": "
                + cantidad;
    }
}