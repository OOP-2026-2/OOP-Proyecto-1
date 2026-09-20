package ClubDeportivo.entrenamientos;

import java.time.LocalDate;
import java.time.LocalTime;
import ClubDeportivo.deportes.DeporteIndividual;
import ClubDeportivo.deportes.Equipo;
import ClubDeportivo.instalaciones.Instalacion;
import ClubDeportivo.usuarios.Entrenador;

public class SesionEntrenamiento {
    private LocalDate fecha;
    private LocalTime horaInicio;
    private int duracionMinutos;
    private Entrenador entrenador;
    private Instalacion instalacion;
    private Equipo equipo;
    private DeporteIndividual deporteIndividual;
    private TipoSesion tipo;

    public SesionEntrenamiento(LocalDate fecha, LocalTime horaInicio, int duracionMinutos,
            Entrenador entrenador, Instalacion instalacion) {
        this(fecha, horaInicio, duracionMinutos, entrenador, instalacion, TipoSesion.GRUPAL);
    }

    public SesionEntrenamiento(LocalDate fecha, LocalTime horaInicio, int duracionMinutos,
            Entrenador entrenador, Instalacion instalacion, TipoSesion tipo) {
        if (fecha == null || horaInicio == null || entrenador == null || instalacion == null || tipo == null) {
            throw new IllegalArgumentException("Los datos de la sesión son obligatorios");
        }
        if (duracionMinutos <= 0) {
            throw new IllegalArgumentException("La duración debe ser mayor que cero");
        }
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.duracionMinutos = duracionMinutos;
        this.entrenador = entrenador;
        this.instalacion = instalacion;
        this.tipo = tipo;
    }

    public void asignarEquipo(Equipo equipo) {
        if (equipo == null) {
            throw new IllegalArgumentException("El equipo es obligatorio");
        }
        this.equipo = equipo;
        this.deporteIndividual = null;
        this.tipo = TipoSesion.GRUPAL;
    }

    public void asignarDeporteIndividual(DeporteIndividual deporteIndividual) {
        if (deporteIndividual == null) {
            throw new IllegalArgumentException("El deporte individual es obligatorio");
        }
        this.deporteIndividual = deporteIndividual;
        this.equipo = null;
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

    public Entrenador getEntrenador() {
        return entrenador;
    }

    public Instalacion getInstalacion() {
        return instalacion;
    }

    public Equipo getEquipo() {
        return equipo;
    }

    public DeporteIndividual getDeporteIndividual() {
        return deporteIndividual;
    }

    public TipoSesion getTipo() {
        return tipo;
    }

    public LocalTime calcularHoraFin() {
        return horaInicio.plusMinutes(duracionMinutos);
    }
}