package clubdeportivo.competencias.Partidos;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import clubdeportivo.competencias.Estadisticas.EstadisticaJugador;
import usuarios.tipos.Socio;

public abstract class Partido {
    private LocalDate fecha;
    private EstadoPartido estado;
    private String marcadorFinal;
    private List<EstadisticaJugador> estadisticas;

    public Partido(LocalDate fecha) {
        if (fecha == null) {
            throw new IllegalArgumentException(
                    "La fecha del partido es obligatoria");
        }

        this.fecha = fecha;
        this.estado = EstadoPartido.PROGRAMADO;
        this.marcadorFinal = null;
        this.estadisticas = new ArrayList<EstadisticaJugador>();
    }

    public void registrarResultado(String marcador) {
        if (estado == EstadoPartido.CANCELADO) {
            throw new IllegalStateException(
                    "No se puede registrar el resultado "
                            + "de un partido cancelado");
        }

        if (estado == EstadoPartido.FINALIZADO) {
            throw new IllegalStateException(
                    "El partido ya está finalizado");
        }

        if (marcador == null
                || marcador.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "El marcador es obligatorio");
        }

        this.marcadorFinal = marcador;
    }

    public void finalizar() {
        if (estado == EstadoPartido.CANCELADO) {
            throw new IllegalStateException(
                    "No se puede finalizar un partido cancelado");
        }

        if (estado == EstadoPartido.FINALIZADO) {
            throw new IllegalStateException(
                    "El partido ya está finalizado");
        }

        if (marcadorFinal == null) {
            throw new IllegalStateException(
                    "Debe registrarse el resultado "
                            + "antes de finalizar");
        }

        estado = EstadoPartido.FINALIZADO;
    }

    public void cancelar() {
        if (estado == EstadoPartido.FINALIZADO) {
            throw new IllegalStateException(
                    "No se puede cancelar un partido finalizado");
        }

        if (estado == EstadoPartido.CANCELADO) {
            throw new IllegalStateException(
                    "El partido ya está cancelado");
        }

        estado = EstadoPartido.CANCELADO;
    }

    public boolean estaFinalizado() {
        return estado == EstadoPartido.FINALIZADO;
    }

    public void agregarEstadistica(
            EstadisticaJugador estadistica) {

        if (estadistica == null) {
            throw new IllegalArgumentException(
                    "La estadística no puede ser nula");
        }

        if (!estaFinalizado()) {
            throw new IllegalStateException(
                    "Las estadísticas solo pueden registrarse "
                            + "cuando el partido esté finalizado");
        }

        if (estadistica.getPartido() != this) {
            throw new IllegalArgumentException(
                    "La estadística pertenece a otro partido");
        }

        Socio jugador = estadistica.getJugador();

        if (!participo(jugador)) {
            throw new IllegalArgumentException(
                    "El jugador no participó en este partido");
        }

        if (estadistica.esDeDeporteIndividual() != esDeporteIndividual()) {
            throw new IllegalArgumentException(
                    "La estadística no corresponde al deporte del partido");
        }

        if (buscarEstadistica(jugador) != null) {
            throw new IllegalArgumentException(
                    "El jugador ya tiene estadísticas "
                            + "registradas en este partido");
        }

        estadisticas.add(estadistica);
    }

    // Cada tipo de partido indica quiénes jugaron
    protected abstract boolean participo(Socio jugador);

    // Solo los partidos de práctica son de deportes individuales
    protected boolean esDeporteIndividual() {
        return false;
    }

    public EstadisticaJugador buscarEstadistica(
            Socio jugador) {

        if (jugador == null) {
            return null;
        }

        for (EstadisticaJugador estadistica : estadisticas) {
            if (estadistica.correspondeA(jugador)) {
                return estadistica;
            }
        }

        return null;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public EstadoPartido getEstado() {
        return estado;
    }

    public String getMarcadorFinal() {
        return marcadorFinal;
    }

    public List<EstadisticaJugador> getEstadisticas() {
        return new ArrayList<EstadisticaJugador>(
                estadisticas);
    }
}