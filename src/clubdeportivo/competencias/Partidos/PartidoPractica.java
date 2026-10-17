package clubdeportivo.competencias.Partidos;

import java.time.LocalDate;
import java.util.ArrayList;
import clubdeportivo.deportes.DeporteIndividual;
import clubdeportivo.deportes.InscripcionAcademia;
import clubdeportivo.deportes.Modalidad;
import usuarios.tipos.Socio;

public class PartidoPractica extends Partido {
    private DeporteIndividual deporte;
    private ArrayList<Socio> participantes;
    private Modalidad modalidad;

    public PartidoPractica(LocalDate fecha, DeporteIndividual deporte, Modalidad modalidad) {
        super(fecha);
        if (deporte == null) {
            throw new IllegalArgumentException(
                    "El deporte es obligatorio");
        }

        if (modalidad == null) {
            throw new IllegalArgumentException(
                    "La modalidad es obligatoria");
        }

        this.deporte = deporte;
        this.modalidad = modalidad;
        this.participantes = new ArrayList<Socio>();
    }

    public void agregarParticipante(Socio socio) {

        if (socio == null) {
            throw new IllegalArgumentException(
                    "El socio no puede ser nulo");
        }

        if (participantes.contains(socio)) {
            throw new IllegalArgumentException(
                    "El socio ya participa en el partido");
        }

        if (!estaHabilitado(socio)) {
            throw new IllegalArgumentException(
                    "El socio no tiene una inscripción "
                            + "activa en este deporte");
        }

        int maximoParticipantes = modalidad == Modalidad.SENCILLOS
                ? 2
                : 4;

        if (participantes.size() >= maximoParticipantes) {

            throw new IllegalStateException(
                    "El partido ya tiene el número máximo "
                            + "de participantes");
        }

        participantes.add(socio);
    }

    private boolean estaHabilitado(Socio socio) {

        for (InscripcionAcademia inscripcion : deporte.getInscripciones()) {

            if (inscripcion.getSocio() == socio
                    && inscripcion.estaActiva()) {

                return true;
            }
        }

        return false;
    }

    public boolean cantidadParticipantesValida() {

        if (modalidad == Modalidad.SENCILLOS) {
            return participantes.size() == 2;
        }

        if (modalidad == Modalidad.DOBLES) {
            return participantes.size() == 4;
        }

        return false;
    }

    public DeporteIndividual getDeporte() {
        return deporte;
    }

    public ArrayList<Socio> getParticipantes() {

        return new ArrayList<Socio>(
                participantes);
    }

    public Modalidad getModalidad() {
        return modalidad;
    }
}
