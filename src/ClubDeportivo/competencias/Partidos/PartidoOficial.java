package ClubDeportivo.competencias.Partidos;

import java.time.LocalDate;

import ClubDeportivo.deportes.Equipo;

public class PartidoOficial extends Partido {
    private Equipo equipo;
    private String institucionRival;
    private String lugar;

    public PartidoOficial(LocalDate fecha, Equipo equipo, String institucionRival, String lugar) {
        super(fecha);
        this.equipo = equipo;
        this.institucionRival = institucionRival;
        this.lugar = lugar;
    }

    public Equipo getEquipo() {
        return equipo;
    }

    public String getInstitucionRival() {
        return institucionRival;
    }

    public String getLugar() {
        return lugar;
    }
}
