package clubdeportivo.competencias.Estadisticas;

import clubdeportivo.competencias.Partidos.Partido;
import usuarios.tipos.Socio;

public abstract class EstadisticaJugador {
    private Socio jugador;
    private Partido partido;

    public EstadisticaJugador(Socio jugador, Partido partido) {
        if (jugador == null) {
            throw new IllegalArgumentException(
                    "El jugador es obligatorio");
        }

        if (partido == null) {
            throw new IllegalArgumentException(
                    "El partido es obligatorio");
        }

        this.jugador = jugador;
        this.partido = partido;
    }

    public boolean correspondeA(Socio jugador) {
        return this.jugador == jugador;
    }

    // Por defecto la estadística es de un deporte de conjunto; las de tenis la sobrescriben
    public boolean esDeDeporteIndividual() {
        return false;
    }

    public Socio getJugador() {
        return jugador;
    }

    public Partido getPartido() {
        return partido;
    }
}