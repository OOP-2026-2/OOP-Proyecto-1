package co.edu.uniandes.clubdeportivo.competencias;

import co.edu.uniandes.clubdeportivo.usuarios.Socio;

public class EstadisticaBaloncesto extends EstadisticaJugador {
    private int puntos;
    private int rebotes;

    public EstadisticaBaloncesto(Socio socio, Partido partido) {
        super(socio, partido);
    }

    public void agregarPuntos(int cantidad) {
        if (cantidad > 0) {
            puntos += cantidad;
        }
    }

    public void registrarRebote() {
        rebotes++;
    }

    public int getPuntos() {
        return puntos;
    }

    public int getRebotes() {
        return rebotes;
    }
}