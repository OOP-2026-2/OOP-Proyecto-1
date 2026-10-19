package clubdeportivo.competencias.Partidos;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import clubdeportivo.deportes.DeporteIndividual;
import clubdeportivo.deportes.InscripcionAcademia;
import clubdeportivo.deportes.Modalidad;
import usuarios.Usuario;
import usuarios.tipos.Socio;
import usuarios.tipos.empleados.Entrenador;

public class PartidoPractica extends Partido {
    private DeporteIndividual deporte;
    private List<Usuario> participantes;
    private Modalidad modalidad;

    public PartidoPractica(LocalDate fecha, DeporteIndividual deporte,
            Modalidad modalidad) {
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
        this.participantes = new ArrayList<Usuario>();
    }

    public void agregarParticipante(Usuario participante) {
        if (participante == null) {
            throw new IllegalArgumentException(
                    "El participante no puede ser nulo");
        }

        if (getEstado() != EstadoPartido.PROGRAMADO) {
            throw new IllegalStateException(
                    "No se pueden agregar participantes "
                            + "a un partido cerrado");
        }

        if (participantes.contains(participante)) {
            throw new IllegalArgumentException(
                    "El participante ya participa en el partido");
        }

        // Solo juegan socios con inscripción activa o entrenadores
        if (participante instanceof Socio) {
            if (!estaHabilitado((Socio) participante)) {
                throw new IllegalArgumentException(
                        "El socio no tiene una inscripción "
                                + "activa en este deporte");
            }
        } else if (!(participante instanceof Entrenador)) {
            throw new IllegalArgumentException(
                    "Solo pueden participar socios habilitados "
                            + "o entrenadores");
        }

        int maximoParticipantes = modalidad == Modalidad.SENCILLOS
                ? 2
                : 4;

        if (participantes.size() >= maximoParticipantes) {
            throw new IllegalStateException(
                    "El partido ya tiene el número máximo "
                            + "de participantes");
        }

        participantes.add(participante);
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

    @Override
    protected boolean participo(Socio jugador) {
        return participantes.contains(jugador);
    }

    @Override
    protected boolean esDeporteIndividual() {
        return true;
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

    @Override
    public void finalizar() {
        if (!cantidadParticipantesValida()) {
            throw new IllegalStateException(
                    "La cantidad de participantes "
                            + "no corresponde a la modalidad");
        }

        super.finalizar();
    }

    public DeporteIndividual getDeporte() {
        return deporte;
    }

    public List<Usuario> getParticipantes() {
        return new ArrayList<Usuario>(
                participantes);
    }

    public Modalidad getModalidad() {
        return modalidad;
    }
}