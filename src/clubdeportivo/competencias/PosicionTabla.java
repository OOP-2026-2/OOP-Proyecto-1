package clubdeportivo.competencias;

import clubdeportivo.deportes.DeporteConjunto;

public class PosicionTabla {
    private String institucion;
    private int partidosJugados;
    private int victorias;
    private int empates;
    private int derrotas;
    private int puntos;

    public PosicionTabla(String institucion) {
        if (institucion == null || institucion.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "La institución es obligatoria");
        }

        this.institucion = institucion;
        this.partidosJugados = 0;
        this.victorias = 0;
        this.empates = 0;
        this.derrotas = 0;
        this.puntos = 0;
    }

    public void registrarResultado(int puntosFavor, int puntosContra, DeporteConjunto deporte) {
        if (deporte == null) {
            throw new IllegalArgumentException(
                    "El deporte es obligatorio");
        }

        if (puntosFavor == puntosContra && !deporte.isPermiteEmpate()) {
            throw new IllegalArgumentException(
                    "El deporte no admite empates");
        }

        partidosJugados++;

        if (puntosFavor > puntosContra) {
            victorias++;
        } else if (puntosFavor < puntosContra) {
            derrotas++;
        } else {
            empates++;
        }

        puntos += deporte.calcularPuntos(puntosFavor, puntosContra);
    }

    public String getInstitucion() {
        return institucion;
    }

    public int getPartidosJugados() {
        return partidosJugados;
    }

    public int getVictorias() {
        return victorias;
    }

    public int getEmpates() {
        return empates;
    }

    public int getDerrotas() {
        return derrotas;
    }

    public int getPuntos() {
        return puntos;
    }

    @Override
    public String toString() {
        return institucion
                + " - PJ " + partidosJugados
                + " - G " + victorias
                + " - E " + empates
                + " - P " + derrotas
                + " - Pts " + puntos;
    }
}
