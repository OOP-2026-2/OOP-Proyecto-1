package co.edu.uniandes.clubdeportivo.entrenamientos;

import java.time.LocalDate;
import java.time.LocalTime;

import co.edu.uniandes.clubdeportivo.deportes.DeporteIndividual;
import co.edu.uniandes.clubdeportivo.deportes.Equipo;
import co.edu.uniandes.clubdeportivo.instalaciones.Instalacion;
import co.edu.uniandes.clubdeportivo.usuarios.Entrenador;

public class SesionEntrenamiento {
    private LocalDate fecha;
    private LocalTime horaInicio;
    private int duracionMinutos;
    private Entrenador entrenador;
    private Instalacion instalacion;
    private Equipo equipo;
    private DeporteIndividual deporteIndividual;

    public SesionEntrenamiento(LocalDate fecha, LocalTime horaInicio, int duracionMinutos, Entrenador entrenador,
            Instalacion instalacion) {
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.duracionMinutos = duracionMinutos;
        this.entrenador = entrenador;
        this.instalacion = instalacion;
    }

    public void asignarEquipo(Equipo equipo) {
        this.equipo = equipo;
        this.deporteIndividual = null;
    }

    public void asignarDeporteIndividual(DeporteIndividual deporte) {
        this.deporteIndividual = deporte;
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

    public LocalTime calcularHoraFin() {
        return horaInicio.plusMinutes(duracionMinutos);
    }
}