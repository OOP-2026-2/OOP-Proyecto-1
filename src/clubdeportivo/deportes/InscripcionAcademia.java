package clubdeportivo.deportes;

import java.time.LocalDate;
import usuarios.tipos.Socio;

public class InscripcionAcademia {
    private Socio socio;
    private DeporteIndividual deporte;
    private Nivel nivel;
    private double puntajeRanking;
    private LocalDate fechaInscripcion;
    private EstadoInscripcion estado;

    public InscripcionAcademia(Socio socio, DeporteIndividual deporte, Nivel nivel) {

        if (socio == null) {
            throw new IllegalArgumentException(
                    "El socio es obligatorio");
        }

        if (deporte == null) {
            throw new IllegalArgumentException(
                    "El deporte es obligatorio");
        }

        if (nivel == null) {
            throw new IllegalArgumentException(
                    "El nivel es obligatorio");
        }

        for (InscripcionAcademia existente : deporte.getInscripciones()) {

            boolean mismoSocio = existente.getSocio() == socio;

            if (mismoSocio
                    && existente.estaActiva()) {

                throw new IllegalArgumentException(
                        "El socio ya tiene una inscripción "
                                + "activa en este deporte");
            }
        }

        this.socio = socio;
        this.deporte = deporte;
        this.nivel = nivel;
        this.puntajeRanking = 0;
        this.fechaInscripcion = LocalDate.now();
        this.estado = EstadoInscripcion.ACTIVA;
    }

    public boolean estaActiva() {
        return estado == EstadoInscripcion.ACTIVA;
    }

    public void actualizarPuntaje(
            double nuevoPuntaje) {

        if (nuevoPuntaje < 0) {
            throw new IllegalArgumentException(
                    "El puntaje no puede ser negativo");
        }

        this.puntajeRanking = nuevoPuntaje;
    }

    public void cambiarNivel(Nivel nuevoNivel) {

        if (nuevoNivel == null) {
            throw new IllegalArgumentException(
                    "El nivel no puede ser nulo");
        }

        this.nivel = nuevoNivel;
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
                    "No se puede reactivar una "
                            + "inscripción retirada");
        }

        estado = EstadoInscripcion.ACTIVA;
    }

    public void retirar() {
        estado = EstadoInscripcion.RETIRADA;
    }

    public Socio getSocio() {
        return socio;
    }

    public DeporteIndividual getDeporte() {
        return deporte;
    }

    public Nivel getNivel() {
        return nivel;
    }

    public double getPuntajeRanking() {
        return puntajeRanking;
    }

    public LocalDate getFechaInscripcion() {
        return fechaInscripcion;
    }

    public EstadoInscripcion getEstado() {
        return estado;
    }

    @Override
    public String toString() {
        return socio.getNombre()
                + " - "
                + deporte.getNombre()
                + " - "
                + nivel
                + " - "
                + estado;
    }
}