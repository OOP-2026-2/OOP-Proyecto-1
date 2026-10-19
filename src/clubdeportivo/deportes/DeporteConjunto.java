package clubdeportivo.deportes;

import java.util.ArrayList;
import java.util.List;

public class DeporteConjunto {
    private String nombre;
    private int minimoJugadores;
    private int puntosVictoria;
    private int puntosEmpate;
    private boolean permiteEmpate;
    private List<Equipo> equipos;
    private List<Posicion> posiciones;

    public DeporteConjunto(String nombre, int minimoJugadores, int puntosVictoria,
            int puntosEmpate, boolean permiteEmpate) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "El nombre del deporte es obligatorio");
        }

        if (minimoJugadores <= 0) {
            throw new IllegalArgumentException(
                    "El mínimo de jugadores debe ser mayor que cero");
        }

        if (puntosVictoria < 0 || puntosEmpate < 0) {
            throw new IllegalArgumentException(
                    "Los puntos de la tabla no pueden ser negativos");
        }

        this.nombre = nombre;
        this.minimoJugadores = minimoJugadores;
        this.puntosVictoria = puntosVictoria;
        this.puntosEmpate = puntosEmpate;
        this.permiteEmpate = permiteEmpate;
        this.equipos = new ArrayList<Equipo>();
        this.posiciones = new ArrayList<Posicion>();
    }

    public void agregarEquipo(Equipo equipo) {

        if (equipo == null) {
            throw new IllegalArgumentException(
                    "El equipo no puede ser nulo");
        }

        if (!equipos.contains(equipo)) {
            equipos.add(equipo);
        }
    }

    public void agregarPosicion(Posicion posicion) {

        if (posicion == null) {
            throw new IllegalArgumentException(
                    "La posición no puede ser nula");
        }

        if (!posiciones.contains(posicion)) {
            posiciones.add(posicion);
        }
    }

    public boolean admitePosicion(Posicion posicion) {

        if (posicion == null) {
            return false;
        }

        return posiciones.contains(posicion);
    }

    public boolean cumpleMinimoJugadores(
            int cantidadJugadores) {

        return cantidadJugadores >= minimoJugadores;
    }

    public int calcularPuntos(int puntosFavor, int puntosContra) {
        if (puntosFavor > puntosContra) {
            return puntosVictoria;
        }

        if (puntosFavor == puntosContra && permiteEmpate) {
            return puntosEmpate;
        }

        return 0;
    }

    public String getNombre() {
        return nombre;
    }

    public int getMinimoJugadores() {
        return minimoJugadores;
    }

    public int getPuntosVictoria() {
        return puntosVictoria;
    }

    public int getPuntosEmpate() {
        return puntosEmpate;
    }

    public boolean isPermiteEmpate() {
        return permiteEmpate;
    }

    public List<Equipo> getEquipos() {

        return new ArrayList<Equipo>(equipos);
    }

    public List<Posicion> getPosiciones() {

        return new ArrayList<Posicion>(posiciones);
    }

    @Override
    public String toString() {
        return nombre;
    }
}