package ClubDeportivo.competencias.Partidos;

import java.time.LocalDate;

import ClubDeportivo.deportes.Modalidad;

public class PartidoPractica extends Partido {
    private Modalidad modalidad;

    public PartidoPractica(LocalDate fecha, Modalidad modalidad) {
        super(fecha);
        this.modalidad = modalidad;
    }

    public Modalidad getModalidad() {
        return modalidad;
    }
}
