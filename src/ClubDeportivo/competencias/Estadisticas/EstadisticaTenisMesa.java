package ClubDeportivo.competencias.Estadisticas;

import ClubDeportivo.competencias.Partidos.Partido;
import ClubDeportivo.usuarios.Socio;

public class EstadisticaTenisMesa extends EstadisticaJugador {
    private int setsGanados;
    private int puntosTotales;

    public EstadisticaTenisMesa(Socio socio, Partido partido) {
        super(socio, partido);
    }

    public void registrarSetGanado() {
        setsGanados++;
    }

    public void agregarPuntos(int cantidad) {
        if (cantidad > 0) {
            puntosTotales += cantidad;
        }
    }

    public int getSetsGanados() {
        return setsGanados;
    }

    public int getPuntosTotales() {
        return puntosTotales;
    }
}
