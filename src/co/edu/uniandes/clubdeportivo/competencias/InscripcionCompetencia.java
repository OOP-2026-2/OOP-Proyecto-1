package co.edu.uniandes.clubdeportivo.competencias;

import java.time.LocalDate;
import co.edu.uniandes.clubdeportivo.deportes.Equipo;

public class InscripcionCompetencia {
    private LocalDate fechaInscripcion;
    private boolean habilitada;
    private Equipo equipo;
    private Competencia competencia;

    public InscripcionCompetencia(LocalDate fechaInscripcion, Equipo equipo, Competencia competencia) {
        this.fechaInscripcion = fechaInscripcion;
        this.equipo = equipo;
        this.competencia = competencia;
        this.habilitada = true;
    }

    public LocalDate getFechaInscripcion() {
        return fechaInscripcion;
    }

    public boolean isHabilitada() {
        return habilitada;
    }

    public Equipo getEquipo() {
        return equipo;
    }

    public Competencia getCompetencia() {
        return competencia;
    }

    public void deshabilitar() {
        habilitada = false;
    }

    public void habilitar() {
        habilitada = true;
    }
}
