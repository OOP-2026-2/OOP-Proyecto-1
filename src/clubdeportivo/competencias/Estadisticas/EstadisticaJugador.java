package clubdeportivo.competencias.Estadisticas;

import clubdeportivo.competencias.Partidos.Partido;
import usuarios.tipos.Socio;

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
