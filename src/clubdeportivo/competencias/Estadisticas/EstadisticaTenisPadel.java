package clubdeportivo.competencias.Estadisticas;

import clubdeportivo.competencias.Partidos.Partido;
import usuarios.tipos.Socio;

public class EstadisticaTenisPadel extends EstadisticaJugador {
    private int setsGanados;
    private int gamesGanados;
    private int aces;
    private int erroresNoForzados;

    public EstadisticaTenisPadel(Socio socio, Partido partido) {
        super(socio, partido);
    }

    public void registrarSetGanado() {
        setsGanados++;
    }

    public void registrarGameGanado() {
        gamesGanados++;
    }

    public void registrarAce() {
        aces++;
    }

    public void registrarErrorNoForzado() {
        erroresNoForzados++;
    }

    public int getSetsGanados() {
        return setsGanados;
    }

    public int getGamesGanados() {
        return gamesGanados;
    }

    public int getAces() {
        return aces;
    }

    public int getErroresNoForzados() {
        return erroresNoForzados;
    }
}
