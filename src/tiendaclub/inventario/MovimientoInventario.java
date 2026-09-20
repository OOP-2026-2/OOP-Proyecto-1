package tiendaclub.inventario;

import java.time.LocalDate;

public class MovimientoInventario {
    private LocalDate fecha;
    private int cantidad;
    private TipoMovimiento tipo;
    private Producto producto;
    private UbicacionInventario origen;
    private UbicacionInventario destino;

    public MovimientoInventario(LocalDate fecha, int cantidad, TipoMovimiento tipo,
            Producto producto, UbicacionInventario origen, UbicacionInventario destino) {
        if (fecha == null || tipo == null || producto == null) {
            throw new IllegalArgumentException("La fecha, el tipo y el producto son obligatorios");
        }
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero");
        }
        validarUbicaciones(tipo, origen, destino);
        this.fecha = fecha;
        this.cantidad = cantidad;
        this.tipo = tipo;
        this.producto = producto;
        this.origen = origen;
        this.destino = destino;
    }

    private void validarUbicaciones(TipoMovimiento tipo, UbicacionInventario origen,
            UbicacionInventario destino) {
        if (tipo == TipoMovimiento.REABASTECIMIENTO && destino == null) {
            throw new IllegalArgumentException("El reabastecimiento necesita una ubicación de destino");
        }
        if (tipo == TipoMovimiento.SALIDA && origen == null) {
            throw new IllegalArgumentException("La salida necesita una ubicación de origen");
        }
        if (tipo == TipoMovimiento.TRASLADO) {
            if (origen == null || destino == null) {
                throw new IllegalArgumentException("El traslado necesita origen y destino");
            }
            if (origen == destino) {
                throw new IllegalArgumentException("El origen y el destino deben ser diferentes");
            }
        }
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public int getCantidad() {
        return cantidad;
    }

    public TipoMovimiento getTipo() {
        return tipo;
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
}
