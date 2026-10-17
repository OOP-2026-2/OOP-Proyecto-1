package clubdeportivo.deportes;

import java.util.ArrayList;

public class DeporteConjunto {
    private String nombre;
    private int minimoJugadores;
    private ArrayList<Equipo> equipos;
    private ArrayList<Posicion> posiciones;

    public DeporteConjunto(String nombre, int minimoJugadores) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "El nombre del deporte es obligatorio");
        }

        if (minimoJugadores <= 0) {
            throw new IllegalArgumentException(
                    "El mínimo de jugadores debe ser mayor que cero");
        }

        this.nombre = nombre;
        this.minimoJugadores = minimoJugadores;
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

    public String getNombre() {
        return nombre;
    }

    public int getMinimoJugadores() {
        return minimoJugadores;
    }

    public ArrayList<Equipo> getEquipos() {

        return new ArrayList<Equipo>(equipos);
    }

    public ArrayList<Posicion> getPosiciones() {

        return new ArrayList<Posicion>(posiciones);
    }
}