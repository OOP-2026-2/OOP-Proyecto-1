package ClubDeportivo.competencias.Estadisticas;

import ClubDeportivo.competencias.Partidos.Partido;
import ClubDeportivo.usuarios.Socio;

public abstract class EstadisticaJugador {
    private Socio socio;
    private Partido partido;

    public EstadisticaJugador(Socio socio, Partido partido) {
        this.socio = socio;
        this.partido = partido;
    }

    public Socio getSocio() {
        return socio;
    }

    public Partido getPartido() {
        return partido;
    }
}
