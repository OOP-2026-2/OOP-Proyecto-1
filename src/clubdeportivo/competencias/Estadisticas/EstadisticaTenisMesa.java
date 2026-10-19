package clubdeportivo.competencias.Estadisticas;

import clubdeportivo.competencias.Partidos.Partido;
import usuarios.tipos.Socio;

public class EstadisticaTenisMesa extends EstadisticaJugador {
    private int setsGanados;
    private int puntosTotales;

    public EstadisticaTenisMesa(Socio socio, Partido partido) {
        super(socio, partido);
    }

    @Override
    public boolean esDeDeporteIndividual() {
        return true;
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
