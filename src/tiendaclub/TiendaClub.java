package tiendaclub;

import usuarios.tipos.Empleado;
import usuarios.tipos.Socio;
import tiendaclub.inventario.*;
import tiendaclub.ventas.DetalleVenta;
import tiendaclub.ventas.Venta;
import utilidades.excepciones.InventarioInsuficienteException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TiendaClub {
    private ArrayList<Producto> productos;
    private ArrayList<ExistenciaInventario> existencias;
    private ArrayList<Venta> ventas;
    private ArrayList<MovimientoInventario> movimientosInventario;


    public TiendaClub(){
        productos = new ArrayList<Producto>();
        existencias = new ArrayList<ExistenciaInventario>();
        ventas = new ArrayList<Venta>();
        movimientosInventario = new ArrayList<MovimientoInventario>();
    }

    public void registrarProducto(Producto producto) {
        if (producto == null) {
            throw new IllegalArgumentException(
                    "El producto no puede ser nulo");
        }

        productos.add(producto);
    }

    public void registrarExistencia(ExistenciaInventario existencia) {

        if (existencia == null) {
            throw new IllegalArgumentException(
                    "La existencia no puede ser nula");
        }

        existencias.add(existencia);
    }

    public void registrarVenta(Venta venta) {
        if (venta == null) {
            throw new IllegalArgumentException(
                    "La venta no puede ser nula");
        }

        ventas.add(venta);
    }

    public void registrarMovimientoInventario(MovimientoInventario movimiento) {

        if (movimiento == null) {
            throw new IllegalArgumentException(
                    "El movimiento no puede ser nulo");
        }

        movimientosInventario.add(movimiento);
    }

    public ArrayList<Producto> getProductos() {
        return new ArrayList<Producto>(productos);
    }

    public ArrayList<ExistenciaInventario> getExistencias() {
        return new ArrayList<ExistenciaInventario>(
                existencias);
    }

    public ArrayList<Venta> getVentas() {
        return new ArrayList<Venta>(ventas);
    }

    public ArrayList<MovimientoInventario> getMovimientosInventario() {

        return new ArrayList<MovimientoInventario>(
                movimientosInventario);
    }

    public ExistenciaInventario buscarExistencia(Producto producto, UbicacionInventario ubicacion) {

        for (ExistenciaInventario existencia : existencias) {

            if (existencia.getProducto() == producto
                    && existencia.getUbicacion() == ubicacion) {
                return existencia;
            }
        }

        return null;
    }

    public void reabastecerProducto(Producto producto, UbicacionInventario ubicacion, int cantidad) {

        if (producto == null || ubicacion == null) {
            throw new IllegalArgumentException(
                    "El producto y la ubicación son obligatorios");
        }

        if (cantidad <= 0) {
            throw new IllegalArgumentException(
                    "La cantidad debe ser mayor que cero");
        }

        ExistenciaInventario existencia = buscarExistencia(producto, ubicacion);

        if (existencia == null) {
            existencia = new ExistenciaInventario(
                    producto,
                    ubicacion,
                    cantidad);

            existencias.add(existencia);
        } else {
            existencia.agregarUnidades(cantidad);
        }

        MovimientoInventario movimiento = new MovimientoInventario(
                LocalDate.now(),
                cantidad,
                TipoMovimiento.REABASTECIMIENTO,
                producto,
                null,
                ubicacion);

        movimientosInventario.add(movimiento);
    }

    public void trasladarInventario(Producto producto, UbicacionInventario origen, UbicacionInventario destino,
                                    int cantidad) {

        if (producto == null
                || origen == null
                || destino == null) {
            throw new IllegalArgumentException(
                    "El producto y las ubicaciones son obligatorios");
        }

        if (origen == destino) {
            throw new IllegalArgumentException(
                    "Las ubicaciones deben ser diferentes");
        }

        if (cantidad <= 0) {
            throw new IllegalArgumentException(
                    "La cantidad debe ser mayor que cero");
        }

        ExistenciaInventario existenciaOrigen = buscarExistencia(producto, origen);

        if (existenciaOrigen == null
                || existenciaOrigen.getCantidad() < cantidad) {
            throw new InventarioInsuficienteException(
                    "Inventario insuficiente para: "
                            + producto.getNombre());
        }

        existenciaOrigen.retirarUnidades(cantidad);

        ExistenciaInventario existenciaDestino = buscarExistencia(producto, destino);

        if (existenciaDestino == null) {
            existenciaDestino = new ExistenciaInventario(
                    producto,
                    destino,
                    cantidad);

            existencias.add(existenciaDestino);
        } else {
            existenciaDestino.agregarUnidades(cantidad);
        }

        MovimientoInventario movimiento = new MovimientoInventario(
                LocalDate.now(),
                cantidad,
                TipoMovimiento.TRASLADO,
                producto,
                origen,
                destino);

        movimientosInventario.add(movimiento);
    }

    public void procesarVenta(Venta venta, UbicacionInventario ubicacion) {

        if (venta == null || ubicacion == null) {
            throw new IllegalArgumentException(
                    "La venta y la ubicación son obligatorias");
        }

        if (venta.getDetalles().isEmpty()) {
            throw new IllegalArgumentException(
                    "La venta debe contener al menos un producto");
        }

        Map<Producto, Integer> cantidades = new HashMap<Producto, Integer>();

        for (DetalleVenta detalle : venta.getDetalles()) {

            Producto producto = detalle.getProducto();
            Integer cantidadActual = cantidades.get(producto);

            if (cantidadActual == null) {
                cantidadActual = 0;
            }

            cantidades.put(
                    producto,
                    cantidadActual
                            + detalle.getCantidad());
        }

        for (Map.Entry<Producto, Integer> entrada : cantidades.entrySet()) {

            ExistenciaInventario existencia = buscarExistencia(
                    entrada.getKey(),
                    ubicacion);

            if (existencia == null
                    || existencia.getCantidad() < entrada.getValue()) {
                throw new InventarioInsuficienteException(
                        "Inventario insuficiente para: "
                                + entrada.getKey().getNombre());
            }
        }

        for (Map.Entry<Producto, Integer> entrada : cantidades.entrySet()) {

            Producto producto = entrada.getKey();
            int cantidad = entrada.getValue();

            ExistenciaInventario existencia = buscarExistencia(
                    producto,
                    ubicacion);

            existencia.retirarUnidades(cantidad);

            MovimientoInventario movimiento = new MovimientoInventario(
                    LocalDate.now(),
                    cantidad,
                    TipoMovimiento.SALIDA,
                    producto,
                    ubicacion,
                    null);

            movimientosInventario.add(movimiento);
        }

        if (venta.getComprador() instanceof Socio) {
            Socio socio = (Socio) venta.getComprador();

            socio.agregarPuntos(
                    venta.getPuntosGenerados());
        } else if (venta.getComprador() instanceof Empleado) {

            Empleado empleado = (Empleado) venta.getComprador();

            empleado.agregarPuntos(
                    venta.getPuntosGenerados());
        }

        ventas.add(venta);
    }

}
