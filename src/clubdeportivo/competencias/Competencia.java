package clubdeportivo.competencias;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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

    private List<InscripcionCompetencia> inscripciones;

    private List<PartidoOficial> partidos;

    private List<PosicionTabla> tablaPosiciones;

    // Partidos que ya se contaron en la tabla, para no contarlos dos veces
    private List<PartidoOficial> partidosEnTabla;

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

        this.tablaPosiciones = new ArrayList<PosicionTabla>();

        this.partidosEnTabla = new ArrayList<PartidoOficial>();
    }

    public InscripcionCompetencia inscribirEquipo(Equipo equipo) {

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
        return inscripcion;
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

    public void actualizarTabla(PartidoOficial partido) {
        if (partido == null) {
            throw new IllegalArgumentException(
                    "El partido no puede ser nulo");
        }

        if (partido.getCompetencia() != this) {
            throw new IllegalArgumentException(
                    "El partido pertenece a otra competencia");
        }

        if (!partido.estaFinalizado()) {
            throw new IllegalStateException(
                    "El partido debe estar finalizado");
        }

        if (partidosEnTabla.contains(partido)) {
            throw new IllegalStateException(
                    "El partido ya fue contado en la tabla de posiciones");
        }

        int[] marcador = leerMarcador(partido.getMarcadorFinal());
        int puntosFavor = marcador[0];
        int puntosContra = marcador[1];

        buscarPosicion(partido.getEquipo().getNombre())
                .registrarResultado(puntosFavor, puntosContra, deporte);

        buscarPosicion(partido.getInstitucionRival())
                .registrarResultado(puntosContra, puntosFavor, deporte);

        partidosEnTabla.add(partido);
    }

    // Lo usa PartidoOficial antes de guardar el marcador
    public void validarMarcador(String marcador) {
        leerMarcador(marcador);
    }

    // El marcador se guarda con el formato "favor-contra", por ejemplo "3-1"
    private int[] leerMarcador(String marcador) {
        if (marcador == null) {
            throw new IllegalArgumentException(
                    "El marcador es obligatorio");
        }

        String[] partes = marcador.split("-");

        if (partes.length != 2) {
            throw new IllegalArgumentException(
                    "El marcador debe tener el formato 3-1");
        }

        int[] puntos = new int[2];

        try {
            puntos[0] = Integer.parseInt(partes[0].trim());
            puntos[1] = Integer.parseInt(partes[1].trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "El marcador debe tener el formato 3-1");
        }

        if (puntos[0] == puntos[1] && !deporte.isPermiteEmpate()) {
            throw new IllegalArgumentException(
                    "El deporte " + deporte.getNombre()
                            + " no admite empates");
        }

        return puntos;
    }

    private PosicionTabla buscarPosicion(String institucion) {
        for (PosicionTabla posicion : tablaPosiciones) {
            if (posicion.getInstitucion().equalsIgnoreCase(institucion)) {
                return posicion;
            }
        }

        PosicionTabla nueva = new PosicionTabla(institucion);
        tablaPosiciones.add(nueva);
        return nueva;
    }

    public List<PosicionTabla> obtenerTablaPosiciones() {
        List<PosicionTabla> tabla = new ArrayList<PosicionTabla>(tablaPosiciones);

        // Orden por puntos, de mayor a menor
        for (int i = 0; i < tabla.size() - 1; i++) {
            for (int j = i + 1; j < tabla.size(); j++) {
                if (tabla.get(j).getPuntos() > tabla.get(i).getPuntos()) {
                    PosicionTabla auxiliar = tabla.get(i);
                    tabla.set(i, tabla.get(j));
                    tabla.set(j, auxiliar);
                }
            }
        }

        return tabla;
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

    public List<InscripcionCompetencia> getInscripciones() {

        return new ArrayList<InscripcionCompetencia>(
                inscripciones);
    }

    public List<PartidoOficial> getPartidos() {

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