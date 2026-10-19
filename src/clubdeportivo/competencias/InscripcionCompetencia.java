package clubdeportivo.competencias;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import clubdeportivo.deportes.Equipo;
import clubdeportivo.deportes.EstadoInscripcion;
import usuarios.tipos.Socio;

public class InscripcionCompetencia {
    private Competencia competencia;
    private Equipo equipo;
    private LocalDate fechaInscripcion;
    private List<Socio> nomina;
    private EstadoInscripcion estado;

    public InscripcionCompetencia(LocalDate fechaInscripcion, Equipo equipo, Competencia competencia) {
        if (fechaInscripcion == null) {
            throw new IllegalArgumentException(
                    "La fecha de inscripción es obligatoria");
        }

        if (equipo == null) {
            throw new IllegalArgumentException(
                    "El equipo es obligatorio");
        }

        if (competencia == null) {
            throw new IllegalArgumentException(
                    "La competencia es obligatoria");
        }

        if (equipo.getDeporte() != competencia.getDeporte()) {

            throw new IllegalArgumentException(
                    "El equipo y la competencia deben "
                            + "pertenecer al mismo deporte");
        }

        if (equipo.getCategoria() != competencia.getCategoria()) {

            throw new IllegalArgumentException(
                    "El equipo no pertenece a la categoría "
                            + "de la competencia");
        }

        this.fechaInscripcion = fechaInscripcion;
        this.equipo = equipo;
        this.competencia = competencia;
        this.nomina = new ArrayList<Socio>();
        this.estado = EstadoInscripcion.ACTIVA;
    }

    public void agregarJugadorNomina(Socio socio) {
        if (socio == null) {
            throw new IllegalArgumentException(
                    "El socio no puede ser nulo");
        }

        if (!equipo.getJugadoresHabilitados().contains(socio)) {
            throw new IllegalArgumentException(
                    "El socio no es un jugador habilitado del equipo");
        }

        if (estaEnNomina(socio)) {
            throw new IllegalArgumentException(
                    "El socio ya está en la nómina");
        }

        nomina.add(socio);
    }

    public boolean estaEnNomina(Socio socio) {
        return nomina.contains(socio);
    }

    public boolean estaActiva() {
        return estado == EstadoInscripcion.ACTIVA;
    }

    public void suspender() {

        if (estado == EstadoInscripcion.RETIRADA) {
            throw new IllegalStateException(
                    "No se puede suspender una "
                            + "inscripción retirada");
        }

        estado = EstadoInscripcion.SUSPENDIDA;
    }

    public void reactivar() {
        if (estado == EstadoInscripcion.RETIRADA) {
            throw new IllegalStateException(
                    "No se puede reactivar una inscripción retirada");
        }

        if (estado == EstadoInscripcion.ACTIVA) {
            return;
        }

        if (!equipo.isActivo()) {
            throw new IllegalStateException(
                    "No se puede reactivar la inscripción porque el equipo está inactivo");
        }

        if (!competencia.cumpleMinimoJugadores(equipo)) {
            throw new IllegalStateException(
                    "El equipo no cumple el mínimo de jugadores habilitados");
        }

        estado = EstadoInscripcion.ACTIVA;
    }

    public void retirar() {
        estado = EstadoInscripcion.RETIRADA;
    }

    public LocalDate getFechaInscripcion() {
        return fechaInscripcion;
    }

    public Equipo getEquipo() {
        return equipo;
    }

    public Competencia getCompetencia() {
        return competencia;
    }

    public List<Socio> getNomina() {
        return new ArrayList<Socio>(nomina);
    }

    public EstadoInscripcion getEstado() {
        return estado;
    }

    @Override
    public String toString() {

        return equipo.getNombre()
                + " - "
                + competencia.getNombre()
                + " - "
                + estado;
    }
}
