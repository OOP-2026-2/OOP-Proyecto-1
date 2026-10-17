package clubdeportivo.instalaciones;

import java.time.LocalDate;
import java.time.LocalTime;
import clubdeportivo.deportes.Modalidad;
import usuarios.tipos.Socio;

public class Reserva {
    private final Socio socio;
    private final Instalacion instalacion;
    private final LocalDate fecha;
    private final LocalTime horaInicio;
    private final int duracionMinutos;
    private final int numeroJugadores;
    private final Modalidad modalidad;

    public Reserva(Socio socio, Instalacion instalacion, LocalDate fecha, LocalTime horaInicio, int duracionMinutos,
            int numeroJugadores, Modalidad modalidad) {
        if (socio == null) {
            throw new IllegalArgumentException(
                    "El socio es obligatorio");
        }

        if (instalacion == null) {
            throw new IllegalArgumentException(
                    "La instalación es obligatoria");
        }

        if (fecha == null || horaInicio == null) {
            throw new IllegalArgumentException(
                    "La fecha y la hora de inicio son obligatorias");
        }

        if (duracionMinutos <= 0) {
            throw new IllegalArgumentException(
                    "La duración debe ser mayor que cero");
        }

        if (modalidad == null) {
            throw new IllegalArgumentException(
                    "La modalidad es obligatoria");
        }

        this.socio = socio;
        this.instalacion = instalacion;
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.duracionMinutos = duracionMinutos;
        this.numeroJugadores = numeroJugadores;
        this.modalidad = modalidad;

        if (!esFechaValida()) {
            throw new IllegalArgumentException(
                    "No se pueden registrar reservas en fechas pasadas");
        }

        if (!cantidadJugadoresValida()) {
            throw new IllegalArgumentException(
                    "La cantidad de jugadores no corresponde "
                            + "al tipo de instalación y modalidad");
        }
    }

    public LocalTime calcularHoraFin() {
        return horaInicio.plusMinutes(duracionMinutos);
    }

    public boolean esFechaValida() {
        return !fecha.isBefore(LocalDate.now());
    }

    public boolean cantidadJugadoresValida() {

        if (!instalacion.admiteJugadores(numeroJugadores)) {
            return false;
        }

        switch (instalacion.getTipo()) {

            case CANCHA_TENIS:
            case MESA_TENIS_MESA:

                if (modalidad == Modalidad.SENCILLOS) {
                    return numeroJugadores == 2;
                }

                if (modalidad == Modalidad.DOBLES) {
                    return numeroJugadores == 4;
                }

                return false;

            case CANCHA_PADEL:

                return modalidad == Modalidad.DOBLES
                        && numeroJugadores == 4;

            case CANCHA_CONJUNTO:

                return numeroJugadores > 0
                        && numeroJugadores <= instalacion.getCapacidadMaxima();

            default:
                return false;
        }
    }

    public boolean seCruzaCon(Reserva otraReserva) {

        if (otraReserva == null) {
            return false;
        }

        boolean mismaInstalacion = instalacion == otraReserva.getInstalacion();

        boolean mismaFecha = fecha.equals(otraReserva.getFecha());

        boolean horariosSuperpuestos = horaInicio.isBefore(otraReserva.calcularHoraFin())
                && otraReserva.getHoraInicio().isBefore(calcularHoraFin());

        return mismaInstalacion
                && mismaFecha
                && horariosSuperpuestos;
    }

    public Socio getSocio() {
        return socio;
    }

    public Instalacion getInstalacion() {
        return instalacion;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public int getDuracionMinutos() {
        return duracionMinutos;
    }

    public int getNumeroJugadores() {
        return numeroJugadores;
    }

    public Modalidad getModalidad() {
        return modalidad;
    }

    @Override
    public String toString() {
        return fecha
                + " " + horaInicio
                + " - " + calcularHoraFin()
                + " - " + instalacion.getId()
                + " - " + modalidad;
    }
}