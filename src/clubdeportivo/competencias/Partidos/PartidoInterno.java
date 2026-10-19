package clubdeportivo.competencias.Partidos;

import java.time.LocalDate;
import clubdeportivo.deportes.Equipo;
import usuarios.tipos.Socio;

public class PartidoInterno extends Partido {
    private Equipo equipoLocal;
    private Equipo equipoVisitante;
    private PropositoPartido proposito;

    public PartidoInterno(LocalDate fecha, Equipo equipoLocal, Equipo equipoVisitante,
            PropositoPartido proposito) {
        super(fecha);

        if (equipoLocal == null
                || equipoVisitante == null) {

            throw new IllegalArgumentException(
                    "Los dos equipos son obligatorios");
        }

        if (!equipoLocal.isActivo()
                || !equipoVisitante.isActivo()) {

            throw new IllegalArgumentException(
                    "Los dos equipos deben estar activos");
        }

        if (equipoLocal == equipoVisitante) {
            throw new IllegalArgumentException(
                    "Los equipos deben ser diferentes");
        }

        if (equipoLocal.getDeporte() != equipoVisitante.getDeporte()) {

            throw new IllegalArgumentException(
                    "Los equipos deben pertenecer "
                            + "al mismo deporte");
        }

        if (equipoLocal.getCategoria() != equipoVisitante.getCategoria()) {

            throw new IllegalArgumentException(
                    "Los equipos deben pertenecer "
                            + "a la misma categoría");
        }

        if (proposito == null) {
            throw new IllegalArgumentException(
                    "El propósito del partido es obligatorio");
        }

        this.equipoLocal = equipoLocal;
        this.equipoVisitante = equipoVisitante;
        this.proposito = proposito;
    }

    public boolean equiposDiferentes() {
        return equipoLocal != equipoVisitante;
    }

    @Override
    protected boolean participo(Socio jugador) {
        return equipoLocal.buscarInscripcion(jugador) != null
                || equipoVisitante.buscarInscripcion(jugador) != null;
    }

    public Equipo getEquipoLocal() {
        return equipoLocal;
    }

    public Equipo getEquipoVisitante() {
        return equipoVisitante;
    }

    public PropositoPartido getProposito() {
        return proposito;
    }
}