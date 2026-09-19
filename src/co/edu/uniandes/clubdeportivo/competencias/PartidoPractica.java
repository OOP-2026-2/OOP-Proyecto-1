package co.edu.uniandes.clubdeportivo.competencias;

import java.time.LocalDate;
import co.edu.uniandes.clubdeportivo.deportes.Modalidad;

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
