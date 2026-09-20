package ClubDeportivo.deportes;

import ClubDeportivo.usuarios.Socio;

public class InscripcionAcademia {
    private Socio socio;
    private DeporteIndividual deporte;
    private Nivel nivel;
    private double puntajeRanking;

    public InscripcionAcademia(Socio socio, DeporteIndividual deporte, Nivel nivel) {
        this.socio = socio;
        this.deporte = deporte;
        this.nivel = nivel;
        this.puntajeRanking = 0;
    }

    public Socio getSocio() {
        return socio;
    }

    public DeporteIndividual getDeporte() {
        return deporte;
    }

    public Nivel getNivel() {
        return nivel;
    }

    public double getPuntajeRanking() {
        return puntajeRanking;
    }

    public void actualizarPuntajeRanking(double puntajeRanking) {
        this.puntajeRanking = puntajeRanking;
    }

    public void cambiarNivel(Nivel nivel) {
        this.nivel = nivel;
    }
}
