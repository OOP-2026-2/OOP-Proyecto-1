package clubdeportivo.competencias.Partidos;

import java.time.LocalDate;
import clubdeportivo.competencias.Competencia;
import clubdeportivo.competencias.InscripcionCompetencia;
import clubdeportivo.deportes.Equipo;

public class PartidoOficial extends Partido {
    private Equipo equipo;
    private Competencia competencia;
    private String institucionRival;
    private String lugar;

    public PartidoOficial(LocalDate fecha, Equipo equipo, Competencia competencia,
            String institucionRival, String lugar) {
        super(fecha);

        if (equipo == null) {
            throw new IllegalArgumentException(
                    "El equipo es obligatorio");
        }

        if (!equipo.isActivo()) {
            throw new IllegalArgumentException(
                    "El equipo debe estar activo");
        }

        if (competencia == null) {
            throw new IllegalArgumentException(
                    "La competencia es obligatoria");
        }

        if (institucionRival == null
                || institucionRival.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "La institución rival es obligatoria");
        }

        if (lugar == null
                || lugar.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "El lugar es obligatorio");
        }

        if (equipo.getDeporte() != competencia.getDeporte()) {

            throw new IllegalArgumentException(
                    "El equipo y la competencia deben "
                            + "corresponder al mismo deporte");
        }

        if (equipo.getCategoria() != competencia.getCategoria()) {

            throw new IllegalArgumentException(
                    "El equipo y la competencia deben "
                            + "corresponder a la misma categoría");
        }

        InscripcionCompetencia inscripcion = competencia.buscarInscripcion(equipo);

        if (inscripcion == null
                || !inscripcion.estaActiva()) {

            throw new IllegalArgumentException(
                    "El equipo debe tener una inscripción "
                            + "activa en la competencia");
        }

        this.equipo = equipo;
        this.competencia = competencia;
        this.institucionRival = institucionRival;
        this.lugar = lugar;
    }

    public void registrarMarcador(String marcador) {
        registrarResultado(marcador);
    }

    public Equipo getEquipo() {
        return equipo;
    }

    public Competencia getCompetencia() {
        return competencia;
    }

    public String getInstitucionRival() {
        return institucionRival;
    }

    public String getLugar() {
        return lugar;
    }
}