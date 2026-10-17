package tiendaclub.inventario;

import java.time.LocalDate;

public class MovimientoInventario {
    private final LocalDate fecha;
    private final Producto producto;
    private final UbicacionInventario origen;
    private final UbicacionInventario destino;
    private final int cantidad;
    private final TipoMovimiento tipo;

    public MovimientoInventario(LocalDate fecha, int cantidad, TipoMovimiento tipo, Producto producto,
            UbicacionInventario origen, UbicacionInventario destino) {

        if (fecha == null) {
            throw new IllegalArgumentException(
                    "La fecha es obligatoria");
        }

        if (tipo == null) {
            throw new IllegalArgumentException(
                    "El tipo de movimiento es obligatorio");
        }

        if (producto == null) {
            throw new IllegalArgumentException(
                    "El producto es obligatorio");
        }

        if (cantidad <= 0) {
            throw new IllegalArgumentException(
                    "La cantidad debe ser mayor que cero");
        }

        this.fecha = fecha;
        this.cantidad = cantidad;
        this.tipo = tipo;
        this.producto = producto;
        this.origen = origen;
        this.destino = destino;

        if (!esValido()) {
            throw new IllegalArgumentException(
                    "Las ubicaciones no corresponden "
                            + "al tipo de movimiento");
        }
    }

    public boolean esValido() {

        switch (tipo) {

            case REABASTECIMIENTO:
                return origen == null
                        && destino != null;

            case SALIDA:
                return origen != null
                        && destino == null;

            case TRASLADO:
                return origen != null
                        && destino != null
                        && origen != destino;

            default:
                return false;
        }
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public Producto getProducto() {
        return producto;
    }

    public UbicacionInventario getOrigen() {
        return origen;
    }

    public UbicacionInventario getDestino() {
        return destino;
    }

    public int getCantidad() {
        return cantidad;
    }

    public TipoMovimiento getTipo() {
        return tipo;
    }

    @Override
    public String toString() {

        String ubicaciones;

        if (tipo == TipoMovimiento.REABASTECIMIENTO) {

            ubicaciones = "destino: " + destino.getNombre();

        } else if (tipo == TipoMovimiento.SALIDA) {

            ubicaciones = "origen: " + origen.getNombre();

        } else {

            ubicaciones = "origen: " + origen.getNombre()
                    + ", destino: "
                    + destino.getNombre();
        }

        return fecha
                + " - " + tipo
                + " - " + producto.getNombre()
                + " - cantidad: " + cantidad
                + " - " + ubicaciones;
    }
}