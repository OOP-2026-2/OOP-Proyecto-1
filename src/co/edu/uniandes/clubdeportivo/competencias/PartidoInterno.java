package co.edu.uniandes.clubdeportivo.competencias;

import java.time.LocalDate;
import co.edu.uniandes.clubdeportivo.deportes.Equipo;

public class PartidoInterno extends Partido {
    private Equipo equipoLocal;
    private Equipo equipoVisitante;

    public PartidoInterno(LocalDate fecha, Equipo equipoLocal, Equipo equipoVisitante) {
        super(fecha);
        this.equipoLocal = equipoLocal;
        this.equipoVisitante = equipoVisitante;
    }

    public Equipo getEquipoLocal() {
        return equipoLocal;
    }

    public Equipo getEquipoVisitante() {
        return equipoVisitante;
    }
}
