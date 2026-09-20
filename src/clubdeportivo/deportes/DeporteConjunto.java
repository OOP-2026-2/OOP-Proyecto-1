package clubdeportivo.deportes;

import java.util.ArrayList;

public class DeporteConjunto {
    private String nombre;
    private int minimoJugadores;
    private ArrayList<Equipo> equipos;

    public DeporteConjunto(String nombre, int minimoJugadores) {
        this.nombre = nombre;
        this.minimoJugadores = minimoJugadores;
        this.equipos = new ArrayList<Equipo>();
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

    public void agregarEquipo(Equipo equipo) {
        if (equipo != null) {
            equipos.add(equipo);
        }
    }
}
