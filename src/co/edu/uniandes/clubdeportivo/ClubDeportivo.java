package co.edu.uniandes.clubdeportivo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import co.edu.uniandes.clubdeportivo.entrenamientos.SesionEntrenamiento;
import co.edu.uniandes.clubdeportivo.excepciones.InventarioInsuficienteException;
import co.edu.uniandes.clubdeportivo.excepciones.ReservaNoDisponibleException;
import co.edu.uniandes.clubdeportivo.instalaciones.CanchaPadel;
import co.edu.uniandes.clubdeportivo.instalaciones.Instalacion;
import co.edu.uniandes.clubdeportivo.instalaciones.MesaTenisMesa;
import co.edu.uniandes.clubdeportivo.instalaciones.Reserva;
import co.edu.uniandes.clubdeportivo.inventario.Bebida;
import co.edu.uniandes.clubdeportivo.inventario.ExistenciaInventario;
import co.edu.uniandes.clubdeportivo.inventario.MovimientoInventario;
import co.edu.uniandes.clubdeportivo.inventario.Producto;
import co.edu.uniandes.clubdeportivo.inventario.TipoMovimiento;
import co.edu.uniandes.clubdeportivo.inventario.UbicacionInventario;
import co.edu.uniandes.clubdeportivo.usuarios.Empleado;
import co.edu.uniandes.clubdeportivo.usuarios.Entrenador;
import co.edu.uniandes.clubdeportivo.usuarios.Fisioterapeuta;
import co.edu.uniandes.clubdeportivo.usuarios.Socio;
import co.edu.uniandes.clubdeportivo.usuarios.Turno;
import co.edu.uniandes.clubdeportivo.usuarios.Usuario;
import co.edu.uniandes.clubdeportivo.ventas.DetalleVenta;
import co.edu.uniandes.clubdeportivo.ventas.PagoMensualidad;
import co.edu.uniandes.clubdeportivo.ventas.Venta;

public class ClubDeportivo {
    private ArrayList<Usuario> usuarios;
    private ArrayList<Producto> productos;
    private ArrayList<ExistenciaInventario> existencias;
    private ArrayList<Reserva> reservas;
    private ArrayList<SesionEntrenamiento> sesiones;
    private ArrayList<Venta> ventas;
    private ArrayList<MovimientoInventario> movimientosInventario;
    private ArrayList<PagoMensualidad> pagosMensualidad;
    private ArrayList<Instalacion> instalaciones;

    public ClubDeportivo() {
        usuarios = new ArrayList<Usuario>();
        productos = new ArrayList<Producto>();
        existencias = new ArrayList<ExistenciaInventario>();
        reservas = new ArrayList<Reserva>();
        sesiones = new ArrayList<SesionEntrenamiento>();
        ventas = new ArrayList<Venta>();
        movimientosInventario = new ArrayList<MovimientoInventario>();
        pagosMensualidad = new ArrayList<PagoMensualidad>();
        instalaciones = new ArrayList<Instalacion>();
    }

    public void registrarUsuario(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException(
                    "El usuario no puede ser nulo");
        }

        if (buscarUsuario(usuario.getId()) != null) {
            throw new IllegalArgumentException(
                    "Ya existe un usuario con ese ID");
        }

        usuarios.add(usuario);
    }

    public Usuario buscarUsuario(String id) {
        if (id == null) {
            return null;
        }

        for (Usuario usuario : usuarios) {
            if (usuario.getId().equalsIgnoreCase(id)) {
                return usuario;
            }
        }

        return null;
    }

    public void registrarProducto(Producto producto) {
        if (producto == null) {
            throw new IllegalArgumentException(
                    "El producto no puede ser nulo");
        }

        productos.add(producto);
    }

    public void registrarExistencia(
            ExistenciaInventario existencia) {

        if (existencia == null) {
            throw new IllegalArgumentException(
                    "La existencia no puede ser nula");
        }

        existencias.add(existencia);
    }

    public void registrarInstalacion(Instalacion instalacion) {
        if (instalacion == null) {
            throw new IllegalArgumentException(
                    "La instalación no puede ser nula");
        }

        if (buscarInstalacion(instalacion.getId()) != null) {
            throw new IllegalArgumentException(
                    "Ya existe una instalación con ese ID");
        }

        instalaciones.add(instalacion);
    }

    public Instalacion buscarInstalacion(String id) {
        if (id == null) {
            return null;
        }

        for (Instalacion instalacion : instalaciones) {
            if (instalacion.getId().equalsIgnoreCase(id)) {
                return instalacion;
            }
        }

        return null;
    }

    public void registrarReserva(Reserva reserva) {
        if (reserva == null) {
            throw new IllegalArgumentException(
                    "La reserva no puede ser nula");
        }

        if (reserva.getNumeroJugadores() > reserva.getInstalacion().getCapacidadMaxima()) {
            throw new IllegalArgumentException(
                    "La cantidad de jugadores supera la capacidad");
        }

        for (Reserva existente : reservas) {
            boolean mismaInstalacion = existente.getInstalacion() == reserva.getInstalacion();

            boolean mismaFecha = existente.getFecha()
                    .equals(reserva.getFecha());

            boolean seSuperponen = reserva.getHoraInicio()
                    .isBefore(existente.calcularHoraFin())
                    && existente.getHoraInicio()
                            .isBefore(reserva.calcularHoraFin());

            if (mismaInstalacion
                    && mismaFecha
                    && seSuperponen) {
                throw new ReservaNoDisponibleException(
                        "La instalación ya está reservada en ese horario");
            }
        }

        reservas.add(reserva);
    }

    public void registrarSesion(
            SesionEntrenamiento sesion) {

        if (sesion == null) {
            throw new IllegalArgumentException(
                    "La sesión no puede ser nula");
        }

        sesiones.add(sesion);
    }

    public void registrarVenta(Venta venta) {
        if (venta == null) {
            throw new IllegalArgumentException(
                    "La venta no puede ser nula");
        }

        ventas.add(venta);
    }

    public void registrarMovimientoInventario(
            MovimientoInventario movimiento) {

        if (movimiento == null) {
            throw new IllegalArgumentException(
                    "El movimiento no puede ser nulo");
        }

        movimientosInventario.add(movimiento);
    }

    public void registrarPagoMensualidad(
            PagoMensualidad pago) {

        if (pago == null) {
            throw new IllegalArgumentException(
                    "El pago no puede ser nulo");
        }

        pagosMensualidad.add(pago);
    }

    public ArrayList<Usuario> getUsuarios() {
        return new ArrayList<Usuario>(usuarios);
    }

    public ArrayList<Producto> getProductos() {
        return new ArrayList<Producto>(productos);
    }

    public ArrayList<ExistenciaInventario> getExistencias() {
        return new ArrayList<ExistenciaInventario>(
                existencias);
    }

    public ArrayList<Reserva> getReservas() {
        return new ArrayList<Reserva>(reservas);
    }

    public ArrayList<Instalacion> getInstalaciones() {
        return new ArrayList<Instalacion>(instalaciones);
    }

    public ArrayList<SesionEntrenamiento> getSesiones() {
        return new ArrayList<SesionEntrenamiento>(
                sesiones);
    }

    public ArrayList<Venta> getVentas() {
        return new ArrayList<Venta>(ventas);
    }

    public ArrayList<MovimientoInventario> getMovimientosInventario() {

        return new ArrayList<MovimientoInventario>(
                movimientosInventario);
    }

    public ArrayList<PagoMensualidad> getPagosMensualidad() {

        return new ArrayList<PagoMensualidad>(
                pagosMensualidad);
    }

    public ExistenciaInventario buscarExistencia(
            Producto producto,
            UbicacionInventario ubicacion) {

        for (ExistenciaInventario existencia : existencias) {

            if (existencia.getProducto() == producto
                    && existencia.getUbicacion() == ubicacion) {
                return existencia;
            }
        }

        return null;
    }

    public void reabastecerProducto(
            Producto producto,
            UbicacionInventario ubicacion,
            int cantidad) {

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

    public void trasladarInventario(
            Producto producto,
            UbicacionInventario origen,
            UbicacionInventario destino,
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

    public void procesarVenta(
            Venta venta,
            UbicacionInventario ubicacion) {

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

    public void validarIngresoProducto(
            Producto producto,
            Instalacion instalacion) {

        if (producto == null || instalacion == null) {
            throw new IllegalArgumentException(
                    "El producto y la instalación son obligatorios");
        }

        if (!(producto instanceof Bebida)) {
            return;
        }

        Bebida bebida = (Bebida) producto;

        if (!bebida.isCaliente()) {
            return;
        }

        boolean esCanchaPadel = instalacion instanceof CanchaPadel;

        boolean esMesaTenisMesaTechada = instalacion instanceof MesaTenisMesa
                && instalacion.isTechada();

        if (esCanchaPadel
                || esMesaTenisMesaTechada) {
            throw new IllegalArgumentException(
                    "No se permiten bebidas calientes en esta instalación");
        }
    }

    public boolean cumpleCoberturaMinima(
            Turno turno) {

        if (turno == null) {
            return false;
        }

        int cantidadEntrenadores = 0;
        int cantidadFisioterapeutas = 0;

        for (Empleado empleado : turno.getEmpleados()) {

            if (empleado instanceof Entrenador) {
                cantidadEntrenadores++;
            } else if (empleado instanceof Fisioterapeuta) {
                cantidadFisioterapeutas++;
            }
        }

        return cantidadEntrenadores >= 2
                && cantidadFisioterapeutas >= 1;
    }
}