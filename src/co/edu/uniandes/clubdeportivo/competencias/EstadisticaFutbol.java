package co.edu.uniandes.clubdeportivo.competencias;

import co.edu.uniandes.clubdeportivo.usuarios.Socio;

public class EstadisticaFutbol extends EstadisticaJugador {
    private int goles;
    private int asistencias;
    private int tarjetasAmarillas;
    private int tarjetasRojas;

    public EstadisticaFutbol(Socio socio, Partido partido) {
        super(socio, partido);
    }

    public void registrarGol() {
        goles++;
    }

    public void registrarAsistencia() {
        asistencias++;
    }

    public void registrarTarjetaAmarilla() {
        tarjetasAmarillas++;
    }

    public void registrarTarjetaRoja() {
        tarjetasRojas++;
    }

    public int getGoles() {
        return goles;
    }

    public int getAsistencias() {
        return asistencias;
    }

    public int getTarjetasAmarillas() {
        return tarjetasAmarillas;
    }

    public int getTarjetasRojas() {
        return tarjetasRojas;
    }
}
