package ClubDeportivo.instalaciones;

import java.time.LocalDate;
import java.time.LocalTime;
import ClubDeportivo.deportes.Modalidad;
import ClubDeportivo.usuarios.Socio;

public class Reserva {
    private Socio socio;
    private Instalacion instalacion;
    private LocalDate fecha;
    private LocalTime horaInicio;
    private int duracionMinutos;
    private int numeroJugadores;
    private Modalidad modalidad;

    public Reserva(Socio socio, Instalacion instalacion, LocalDate fecha, LocalTime horaInicio, int duracionMinutos,
            int numeroJugadores, Modalidad modalidad) {
        this.socio = socio;
        this.instalacion = instalacion;
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.duracionMinutos = duracionMinutos;
        this.numeroJugadores = numeroJugadores;
        this.modalidad = modalidad;
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

    public LocalTime calcularHoraFin() {
        return horaInicio.plusMinutes(duracionMinutos);
    }

    @Override
    public String toString() {
        return fecha + " " + horaInicio + " - " + instalacion.getId();
    }
}
