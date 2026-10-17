package clubdeportivo.competencias;

import java.time.LocalDate;
import java.util.ArrayList;
import clubdeportivo.competencias.Partidos.PartidoOficial;
import clubdeportivo.deportes.Categoria;
import clubdeportivo.deportes.DeporteConjunto;
import clubdeportivo.deportes.Equipo;

public class Competencia {
    private String nombre;
    private String organizador;
    private int anio;
    private Categoria categoria;
    private TipoCompetencia tipo;
    private FormatoCompetencia formato;
    private DeporteConjunto deporte;

    private ArrayList<InscripcionCompetencia> inscripciones;

    private ArrayList<PartidoOficial> partidos;

    public Competencia(String nombre, String organizador, int anio, Categoria categoria, TipoCompetencia tipo,
            FormatoCompetencia formato, DeporteConjunto deporte) {

        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "El nombre de la competencia es obligatorio");
        }

        if (organizador == null
                || organizador.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "El organizador es obligatorio");
        }

        if (anio <= 0) {
            throw new IllegalArgumentException(
                    "El año no es válido");
        }

        if (categoria == null) {
            throw new IllegalArgumentException(
                    "La categoría es obligatoria");
        }

        if (tipo == null) {
            throw new IllegalArgumentException(
                    "El tipo de competencia es obligatorio");
        }

        if (formato == null) {
            throw new IllegalArgumentException(
                    "El formato de competencia es obligatorio");
        }

        if (deporte == null) {
            throw new IllegalArgumentException(
                    "El deporte es obligatorio");
        }

        this.nombre = nombre;
        this.organizador = organizador;
        this.anio = anio;
        this.categoria = categoria;
        this.tipo = tipo;
        this.formato = formato;
        this.deporte = deporte;

        this.inscripciones = new ArrayList<InscripcionCompetencia>();

        this.partidos = new ArrayList<PartidoOficial>();
    }

    public void inscribirEquipo(Equipo equipo) {

        if (equipo == null) {
            throw new IllegalArgumentException(
                    "El equipo no puede ser nulo");
        }

        if (!equipo.isActivo()) {
            throw new IllegalArgumentException(
                    "El equipo no se encuentra activo");
        }

        if (equipo.getDeporte() != deporte) {
            throw new IllegalArgumentException(
                    "El equipo pertenece a otro deporte");
        }

        if (equipo.getCategoria() != categoria) {
            throw new IllegalArgumentException(
                    "El equipo pertenece a otra categoría");
        }

        if (buscarInscripcion(equipo) != null) {
            throw new IllegalArgumentException(
                    "El equipo ya está inscrito "
                            + "en la competencia");
        }

        if (!cumpleMinimoJugadores(equipo)) {
            throw new IllegalArgumentException(
                    "El equipo no cumple el mínimo "
                            + "de jugadores habilitados");
        }

        InscripcionCompetencia inscripcion = new InscripcionCompetencia(
                LocalDate.now(),
                equipo,
                this);

        inscripciones.add(inscripcion);
    }

    public InscripcionCompetencia buscarInscripcion(
            Equipo equipo) {

        if (equipo == null) {
            return null;
        }

        for (InscripcionCompetencia inscripcion : inscripciones) {

            if (inscripcion.getEquipo() == equipo) {
                return inscripcion;
            }
        }

        return null;
    }

    public void programarPartido(
            PartidoOficial partido) {

        if (partido == null) {
            throw new IllegalArgumentException(
                    "El partido no puede ser nulo");
        }

        if (partido.getCompetencia() != this) {
            throw new IllegalArgumentException(
                    "El partido pertenece a otra competencia");
        }

        InscripcionCompetencia inscripcion = buscarInscripcion(partido.getEquipo());

        if (inscripcion == null
                || !inscripcion.estaActiva()) {

            throw new IllegalArgumentException(
                    "El equipo no tiene una inscripción "
                            + "activa en la competencia");
        }

        if (!partidos.contains(partido)) {
            partidos.add(partido);
        }
    }

    public boolean cumpleMinimoJugadores(
            Equipo equipo) {

        if (equipo == null) {
            return false;
        }

        int cantidadHabilitados = equipo.getJugadoresHabilitados().size();

        return deporte.cumpleMinimoJugadores(
                cantidadHabilitados);
    }

    public String getNombre() {
        return nombre;
    }

    public String getOrganizador() {
        return organizador;
    }

    public int getAnio() {
        return anio;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public TipoCompetencia getTipo() {
        return tipo;
    }

    public FormatoCompetencia getFormato() {
        return formato;
    }

    public DeporteConjunto getDeporte() {
        return deporte;
    }

    public ArrayList<InscripcionCompetencia> getInscripciones() {

        return new ArrayList<InscripcionCompetencia>(
                inscripciones);
    }

    public ArrayList<PartidoOficial> getPartidos() {

        return new ArrayList<PartidoOficial>(
                partidos);
    }

    @Override
    public String toString() {

        return nombre
                + " - "
                + tipo
                + " - "
                + formato
                + " - "
                + anio;
    }
}