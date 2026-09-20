package clubdeportivo;

import java.util.ArrayList;

import clubdeportivo.entrenamientos.SesionEntrenamiento;
import utilidades.excepciones.ReservaNoDisponibleException;
import clubdeportivo.instalaciones.CanchaPadel;
import clubdeportivo.instalaciones.Instalacion;
import clubdeportivo.instalaciones.MesaTenisMesa;
import clubdeportivo.instalaciones.Reserva;
import tiendaclub.inventario.Bebida;
import tiendaclub.inventario.Producto;
import usuarios.tipos.Empleado;
import usuarios.tipos.empleados.Entrenador;
import usuarios.tipos.empleados.Fisioterapeuta;
import usuarios.turnos.Turno;
import usuarios.Usuario;
import tiendaclub.ventas.PagoMensualidad;

public class ClubDeportivo {
    private ArrayList<Usuario> usuarios;
    private ArrayList<Reserva> reservas;
    private ArrayList<SesionEntrenamiento> sesiones;
    private ArrayList<PagoMensualidad> pagosMensualidad;
    private ArrayList<Instalacion> instalaciones;

    public ClubDeportivo() {
        usuarios = new ArrayList<Usuario>();
        reservas = new ArrayList<Reserva>();
        sesiones = new ArrayList<SesionEntrenamiento>();
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

    public void registrarSesion(SesionEntrenamiento sesion) {

        if (sesion == null) {
            throw new IllegalArgumentException(
                    "La sesión no puede ser nula");
        }

        sesiones.add(sesion);
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


    public ArrayList<PagoMensualidad> getPagosMensualidad() {

        return new ArrayList<PagoMensualidad>(
                pagosMensualidad);
    }


    public void validarIngresoProducto(Producto producto, Instalacion instalacion) {

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

        if (esCanchaPadel || esMesaTenisMesaTechada) {
            throw new IllegalArgumentException(
                    "No se permiten bebidas calientes en esta instalación");
        }
    }

    public boolean cumpleCoberturaMinima(Turno turno) {

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