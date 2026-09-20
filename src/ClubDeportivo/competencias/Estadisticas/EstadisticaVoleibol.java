package ClubDeportivo.competencias.Estadisticas;

import ClubDeportivo.competencias.Partidos.Partido;
import ClubDeportivo.usuarios.Socio;

public class EstadisticaVoleibol extends EstadisticaJugador {
    private int aces;
    private int bloqueos;

    public EstadisticaVoleibol(Socio socio, Partido partido) {
        super(socio, partido);
    }

    public void registrarAce() {
        aces++;
    }

    public void registrarBloqueo() {
        bloqueos++;
    }

    public int getAces() {
        return aces;
    }

    public int getBloqueos() {
        return bloqueos;
    }
}
