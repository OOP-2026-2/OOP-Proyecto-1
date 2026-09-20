package clubdeportivo.competencias.Partidos;

import java.time.LocalDate;

public abstract class Partido {
    private LocalDate fecha;
    private String marcadorFinal;

    public Partido(LocalDate fecha) {
        this.fecha = fecha;
        this.marcadorFinal = null;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public String getMarcadorFinal() {
        return marcadorFinal;
    }

    public void registrarMarcadorFinal(String marcadorFinal) {
        this.marcadorFinal = marcadorFinal;
    }

    public boolean estaFinalizado() {
        return marcadorFinal != null;
    }
}