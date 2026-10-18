package consola.subconsolas;

import clubdeportivo.ClubDeportivo;
import clubdeportivo.competencias.Competencia;
import clubdeportivo.competencias.FormatoCompetencia;
import clubdeportivo.competencias.Partidos.*;
import clubdeportivo.competencias.TipoCompetencia;
import clubdeportivo.deportes.*;
import consola.Subconsola;
import usuarios.Usuario;
import usuarios.tipos.Socio;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import clubdeportivo.competencias.Estadisticas.*;

import static consola.InputHandler.*;

public class ConsolaCompetencias extends Subconsola {
    public ConsolaCompetencias(ClubDeportivo club) {
        super(club);
    }

    @Override
    public String getTitulo() {
        return "Competencias y partidos";
    }

    @Override
    protected String[] getOpciones() {
        return new String[] {
                "Registrar competencia",
                "Inscribir equipo en competencia",
                "Programar partido oficial",
                "Programar partido interno",
                "Programar partido de práctica",
                "Registrar resultado de partido",
                "Ver competencias y partidos",
                "Registrar estadística de jugador",
                "Ver estadísticas de partidos"
        };
    }

    @Override
    protected void ejecutarOpcion(int opcion) {
        switch (opcion) {
            case 1:
                registrarCompetencia();
                break;
            case 2:
                inscribirEquipoEnCompetencia();
                break;
            case 3:
                programarPartidoOficial();
                break;
            case 4:
                programarPartidoInterno();
                break;
            case 5:
                programarPartidoPractica();
                break;
            case 6:
                registrarResultadoPartido();
                break;
            case 7:
                verCompetenciasYPartidos();
                break;
            case 8:
                registrarEstadisticaJugador();
                break;
            case 9:
                verEstadisticasPartidos();
                break;
        }
    }

    private Partido seleccionarPartido() {
        if (club.getPartidos().isEmpty()) {
            System.out.println(
                    "No hay partidos registrados.");
            return null;
        }

        System.out.println("Partidos disponibles:");

        int numero = 1;
        for (Partido partido : club.getPartidos()) {
            System.out.println(
                    numero + ". "
                            + describirPartido(partido)
                            + " | "
                            + partido.getFecha()
                            + " | "
                            + partido.getEstado());

            numero++;
        }

        int seleccion = readInt(
                "Número del partido: ");

        if (seleccion < 1
                || seleccion > club.getPartidos().size()) {

            System.out.println(
                    "El partido seleccionado no es válido.");
            return null;
        }

        return club.getPartidos().get(
                seleccion - 1);
    }
    private Competencia seleccionarCompetencia() {
        if (club.getCompetencias().isEmpty()) {
            System.out.println(
                    "No hay competencias registradas.");
            return null;
        }

        System.out.println("Competencias disponibles:");

        int numero = 1;
        for (Competencia competencia : club.getCompetencias()) {
            System.out.println(numero + ". " + competencia);
            numero++;
        }

        int seleccion = readInt(
                "Número de la competencia: ");

        if (seleccion < 1
                || seleccion > club.getCompetencias().size()) {

            System.out.println(
                    "La competencia seleccionada no es válida.");
            return null;
        }

        return club.getCompetencias().get(
                seleccion - 1);
    }

    private String describirPartido(Partido partido) {
        if (partido instanceof PartidoOficial) {
            PartidoOficial oficial = (PartidoOficial) partido;

            return "Oficial: "
                    + oficial.getEquipo().getNombre()
                    + " contra "
                    + oficial.getInstitucionRival();
        }

        if (partido instanceof PartidoInterno) {
            PartidoInterno interno = (PartidoInterno) partido;

            return "Interno: "
                    + interno.getEquipoLocal().getNombre()
                    + " contra "
                    + interno.getEquipoVisitante().getNombre();
        }

        if (partido instanceof PartidoPractica) {
            PartidoPractica practica = (PartidoPractica) partido;

            return "Práctica: "
                    + practica.getDeporte().getNombre()
                    + " - "
                    + practica.getModalidad();
        }

        return partido.getClass().getSimpleName();
    }

    private void verCompetenciasYPartidos() {
        System.out.println();
        System.out.println("--- COMPETENCIAS Y PARTIDOS ---");

        if (club.getCompetencias().isEmpty()) {
            System.out.println(
                    "No hay competencias registradas.");
        } else {
            System.out.println("Competencias:");

            for (Competencia competencia : club.getCompetencias()) {
                System.out.println(
                        "- " + competencia
                                + " | deporte: "
                                + competencia.getDeporte().getNombre()
                                + " | categoría: "
                                + competencia.getCategoria()
                                + " | equipos inscritos: "
                                + competencia.getInscripciones().size());
            }
        }

        if (club.getPartidos().isEmpty()) {
            System.out.println(
                    "No hay partidos registrados.");
            return;
        }

        System.out.println();
        System.out.println("Partidos:");

        int numero = 1;

        for (Partido partido : club.getPartidos()) {
            System.out.println(
                    numero + ". "
                            + describirPartido(partido)
                            + " | fecha: "
                            + partido.getFecha()
                            + " | estado: "
                            + partido.getEstado()
                            + " | marcador: "
                            + (partido.getMarcadorFinal() == null
                            ? "Sin resultado"
                            : partido.getMarcadorFinal()));

            numero++;
        }
    }

    private void registrarResultadoPartido() {
        System.out.println();
        System.out.println("--- REGISTRAR RESULTADO ---");

        Partido partido = seleccionarPartido();

        if (partido == null) {
            return;
        }

        String marcador = readString("Marcador final: ");

        try {
            partido.registrarResultado(marcador);
            partido.finalizar();

            System.out.println(
                    "Resultado registrado y partido finalizado.");
        } catch (IllegalArgumentException
                 | IllegalStateException e) {

            System.out.println(
                    "No fue posible registrar el resultado: "
                            + e.getMessage());
        }
    }

    private void registrarCompetencia() {
        System.out.println();
        System.out.println("--- REGISTRAR COMPETENCIA ---");

        String nombreDeporte = readString(
                "Nombre del deporte de conjunto: ");

        DeporteConjunto deporte = club.buscarDeporteConjunto(
                nombreDeporte);

        if (deporte == null) {
            System.out.println(
                    "No existe ese deporte de conjunto.");
            return;
        }

        String nombre = readString("Nombre de la competencia: ");
        String organizador = readString("Organizador: ");
        int anio = readInt("Año: ");

        System.out.println("Categorías disponibles:");
        for (Categoria categoria : Categoria.values()) {
            System.out.println("- " + categoria);
        }

        String categoriaTexto = readString(
                "Categoría: ").toUpperCase();

        System.out.println("Tipos disponibles:");
        for (TipoCompetencia tipo : TipoCompetencia.values()) {
            System.out.println("- " + tipo);
        }

        String tipoTexto = readString(
                "Tipo: ").toUpperCase();

        System.out.println("Formatos disponibles:");
        for (FormatoCompetencia formato : FormatoCompetencia.values()) {
            System.out.println("- " + formato);
        }

        String formatoTexto = readString(
                "Formato: ").toUpperCase();

        try {
            Categoria categoria = Categoria.valueOf(
                    categoriaTexto);

            TipoCompetencia tipo = TipoCompetencia.valueOf(
                    tipoTexto);

            FormatoCompetencia formato = FormatoCompetencia.valueOf(
                    formatoTexto);

            Competencia competencia = new Competencia(
                    nombre, organizador, anio,
                    categoria, tipo, formato, deporte);

            club.registrarCompetencia(competencia);

            System.out.println(
                    "Competencia registrada correctamente.");
        } catch (IllegalArgumentException e) {
            System.out.println(
                    "No fue posible registrar la competencia: "
                            + e.getMessage());
        }
    }

    private void inscribirEquipoEnCompetencia() {
        System.out.println();
        System.out.println(
                "--- INSCRIBIR EQUIPO EN COMPETENCIA ---");

        Competencia competencia = seleccionarCompetencia();

        if (competencia == null) {
            return;
        }

        String nombreEquipo = readString(
                "Nombre del equipo: ");

        Equipo equipo = club.buscarEquipo(nombreEquipo);

        if (equipo == null) {
            System.out.println(
                    "No existe un equipo con ese nombre.");
            return;
        }

        try {
            competencia.inscribirEquipo(equipo);

            System.out.println(
                    "Equipo inscrito correctamente.");
        } catch (IllegalArgumentException
                 | IllegalStateException e) {

            System.out.println(
                    "No fue posible inscribir el equipo: "
                            + e.getMessage());
        }
    }

    private void programarPartidoOficial() {
        System.out.println();
        System.out.println("--- PROGRAMAR PARTIDO OFICIAL ---");

        Competencia competencia = seleccionarCompetencia();

        if (competencia == null) {
            return;
        }

        String nombreEquipo = readString(
                "Nombre del equipo: ");

        Equipo equipo = club.buscarEquipo(nombreEquipo);

        if (equipo == null) {
            System.out.println(
                    "No existe un equipo con ese nombre.");
            return;
        }

        String fechaTexto = readString(
                "Fecha del partido (AAAA-MM-DD): ");

        String institucionRival = readString(
                "Institución rival: ");

        String lugar = readString("Lugar: ");

        try {
            PartidoOficial partido = new PartidoOficial(
                    LocalDate.parse(fechaTexto),
                    equipo,
                    competencia,
                    institucionRival,
                    lugar);

            club.registrarPartido(partido);

            System.out.println(
                    "Partido oficial programado correctamente.");
        } catch (DateTimeParseException e) {
            System.out.println(
                    "Fecha inválida. Use AAAA-MM-DD.");
        } catch (IllegalArgumentException
                 | IllegalStateException e) {

            System.out.println(
                    "No fue posible programar el partido: "
                            + e.getMessage());
        }
    }

    private void programarPartidoInterno() {
        System.out.println();
        System.out.println("--- PROGRAMAR PARTIDO INTERNO ---");

        String nombreLocal = readString(
                "Nombre del equipo local: ");

        String nombreVisitante = readString(
                "Nombre del equipo visitante: ");

        Equipo equipoLocal = club.buscarEquipo(nombreLocal);
        Equipo equipoVisitante = club.buscarEquipo(nombreVisitante);

        if (equipoLocal == null
                || equipoVisitante == null) {

            System.out.println(
                    "Uno de los equipos no está registrado.");
            return;
        }

        String fechaTexto = readString(
                "Fecha del partido (AAAA-MM-DD): ");

        System.out.println("Propósitos disponibles:");
        for (PropositoPartido proposito : PropositoPartido.values()) {
            System.out.println("- " + proposito);
        }

        String propositoTexto = readString(
                "Propósito: ").toUpperCase();

        try {
            PartidoInterno partido = new PartidoInterno(
                    LocalDate.parse(fechaTexto),
                    equipoLocal,
                    equipoVisitante,
                    PropositoPartido.valueOf(
                            propositoTexto));

            club.registrarPartido(partido);

            System.out.println(
                    "Partido interno programado correctamente.");
        } catch (DateTimeParseException e) {
            System.out.println(
                    "Fecha inválida. Use AAAA-MM-DD.");
        } catch (IllegalArgumentException
                 | IllegalStateException e) {

            System.out.println(
                    "No fue posible programar el partido: "
                            + e.getMessage());
        }
    }

    private void programarPartidoPractica() {
        System.out.println();
        System.out.println("--- PROGRAMAR PARTIDO DE PRÁCTICA ---");

        String nombreDeporte = readString(
                "Nombre del deporte individual: ");

        DeporteIndividual deporte = club.buscarDeporteIndividual(
                nombreDeporte);

        if (deporte == null) {
            System.out.println(
                    "No existe ese deporte individual.");
            return;
        }

        String fechaTexto = readString(
                "Fecha del partido (AAAA-MM-DD): ");

        String modalidadTexto = readString(
                "Modalidad (SENCILLOS/DOBLES): ")
                .toUpperCase();

        try {
            Modalidad modalidad = Modalidad.valueOf(
                    modalidadTexto);

            PartidoPractica partido = new PartidoPractica(
                    LocalDate.parse(fechaTexto),
                    deporte,
                    modalidad);

            int cantidadParticipantes = modalidad == Modalidad.SENCILLOS
                    ? 2
                    : 4;

            for (int i = 1; i <= cantidadParticipantes; i++) {
                String idSocio = readString(
                        "ID del participante #" + i + ": ");

                Usuario usuario = club.buscarUsuario(idSocio);

                if (!(usuario instanceof Socio)) {
                    throw new IllegalArgumentException(
                            "No existe el socio " + idSocio);
                }

                partido.agregarParticipante(
                        (Socio) usuario);
            }

            club.registrarPartido(partido);

            System.out.println(
                    "Partido de práctica programado correctamente.");
        } catch (DateTimeParseException e) {
            System.out.println(
                    "Fecha inválida. Use AAAA-MM-DD.");
        } catch (IllegalArgumentException
                 | IllegalStateException e) {

            System.out.println(
                    "No fue posible programar el partido: "
                            + e.getMessage());
        }
    }

    private void registrarEstadisticaJugador() {
        System.out.println();
        System.out.println("--- REGISTRAR ESTADÍSTICA DE JUGADOR ---");

        Partido partido = seleccionarPartido();

        if (partido == null) {
            return;
        }

        Socio socio = seleccionarSocio("ID del jugador: ");

        if (socio == null) {
            return;
        }

        System.out.println("Tipos de estadística disponibles:");
        System.out.println("- FUTBOL");
        System.out.println("- BALONCESTO");
        System.out.println("- VOLEIBOL");
        System.out.println("- TENIS_MESA");
        System.out.println("- TENIS_PADEL");

        String tipo = readString(
                "Tipo de estadística: ").toUpperCase();

        try {
            EstadisticaJugador estadistica = crearEstadistica(
                    tipo,
                    socio,
                    partido);

            partido.agregarEstadistica(estadistica);

            System.out.println(
                    "Estadística registrada correctamente.");
        } catch (IllegalArgumentException
                 | IllegalStateException e) {

            System.out.println(
                    "No fue posible registrar la estadística: "
                            + e.getMessage());
        }
    }

    private EstadisticaJugador crearEstadistica(String tipo, Socio socio, Partido partido) {

        if (tipo.equals("FUTBOL")) {
            EstadisticaFutbol estadistica = new EstadisticaFutbol(
                    socio, partido);

            int goles = readNonNegativeInt("Goles: ");
            int asistencias = readNonNegativeInt("Asistencias: ");
            int amarillas = readNonNegativeInt(
                    "Tarjetas amarillas: ");
            int rojas = readNonNegativeInt(
                    "Tarjetas rojas: ");

            for (int i = 0; i < goles; i++) {
                estadistica.registrarGol();
            }
            for (int i = 0; i < asistencias; i++) {
                estadistica.registrarAsistencia();
            }
            for (int i = 0; i < amarillas; i++) {
                estadistica.registrarTarjetaAmarilla();
            }
            for (int i = 0; i < rojas; i++) {
                estadistica.registrarTarjetaRoja();
            }

            return estadistica;
        }

        if (tipo.equals("BALONCESTO")) {
            EstadisticaBaloncesto estadistica = new EstadisticaBaloncesto(
                    socio, partido);

            int puntos = readNonNegativeInt("Puntos: ");
            int rebotes = readNonNegativeInt("Rebotes: ");

            estadistica.agregarPuntos(puntos);

            for (int i = 0; i < rebotes; i++) {
                estadistica.registrarRebote();
            }

            return estadistica;
        }

        if (tipo.equals("VOLEIBOL")) {
            EstadisticaVoleibol estadistica = new EstadisticaVoleibol(
                    socio, partido);

            int aces = readNonNegativeInt("Aces: ");
            int bloqueos = readNonNegativeInt("Bloqueos: ");

            for (int i = 0; i < aces; i++) {
                estadistica.registrarAce();
            }
            for (int i = 0; i < bloqueos; i++) {
                estadistica.registrarBloqueo();
            }

            return estadistica;
        }

        if (tipo.equals("TENIS_MESA")) {
            EstadisticaTenisMesa estadistica = new EstadisticaTenisMesa(
                    socio, partido);

            int sets = readNonNegativeInt(
                    "Sets ganados: ");
            int puntos = readNonNegativeInt(
                    "Puntos totales: ");

            for (int i = 0; i < sets; i++) {
                estadistica.registrarSetGanado();
            }

            estadistica.agregarPuntos(puntos);
            return estadistica;
        }

        if (tipo.equals("TENIS_PADEL")) {
            EstadisticaTenisPadel estadistica = new EstadisticaTenisPadel(
                    socio, partido);

            int sets = readNonNegativeInt(
                    "Sets ganados: ");
            int games = readNonNegativeInt(
                    "Games ganados: ");
            int aces = readNonNegativeInt("Aces: ");
            int errores = readNonNegativeInt(
                    "Errores no forzados: ");

            for (int i = 0; i < sets; i++) {
                estadistica.registrarSetGanado();
            }
            for (int i = 0; i < games; i++) {
                estadistica.registrarGameGanado();
            }
            for (int i = 0; i < aces; i++) {
                estadistica.registrarAce();
            }
            for (int i = 0; i < errores; i++) {
                estadistica.registrarErrorNoForzado();
            }

            return estadistica;
        }

        throw new IllegalArgumentException(
                "El tipo de estadística no es válido");
    }

    private void verEstadisticasPartidos() {
        System.out.println();
        System.out.println("--- ESTADÍSTICAS DE PARTIDOS ---");

        if (club.getPartidos().isEmpty()) {
            System.out.println("No hay partidos registrados.");
            return;
        }

        int numero = 1;

        for (Partido partido : club.getPartidos()) {
            System.out.println();
            System.out.println(
                    "Partido #" + numero
                            + ": "
                            + describirPartido(partido));

            if (partido.getEstadisticas().isEmpty()) {
                System.out.println(
                        "No tiene estadísticas registradas.");
            } else {
                for (EstadisticaJugador estadistica : partido.getEstadisticas()) {
                    System.out.println(
                            "- "
                                    + estadistica.getJugador().getNombre()
                                    + " | "
                                    + describirEstadistica(estadistica));
                }
            }

            numero++;
        }
    }

    private String describirEstadistica(EstadisticaJugador estadistica) {

        if (estadistica instanceof EstadisticaFutbol) {
            EstadisticaFutbol dato = (EstadisticaFutbol) estadistica;

            return "Fútbol | goles: " + dato.getGoles()
                    + " | asistencias: " + dato.getAsistencias()
                    + " | amarillas: " + dato.getTarjetasAmarillas()
                    + " | rojas: " + dato.getTarjetasRojas();
        }

        if (estadistica instanceof EstadisticaBaloncesto) {
            EstadisticaBaloncesto dato = (EstadisticaBaloncesto) estadistica;

            return "Baloncesto | puntos: " + dato.getPuntos()
                    + " | rebotes: " + dato.getRebotes();
        }

        if (estadistica instanceof EstadisticaVoleibol) {
            EstadisticaVoleibol dato = (EstadisticaVoleibol) estadistica;

            return "Voleibol | aces: " + dato.getAces()
                    + " | bloqueos: " + dato.getBloqueos();
        }

        if (estadistica instanceof EstadisticaTenisMesa) {
            EstadisticaTenisMesa dato = (EstadisticaTenisMesa) estadistica;

            return "Tenis de mesa | sets: " + dato.getSetsGanados()
                    + " | puntos: " + dato.getPuntosTotales();
        }

        if (estadistica instanceof EstadisticaTenisPadel) {
            EstadisticaTenisPadel dato = (EstadisticaTenisPadel) estadistica;

            return "Tenis/Pádel | sets: " + dato.getSetsGanados()
                    + " | games: " + dato.getGamesGanados()
                    + " | aces: " + dato.getAces()
                    + " | errores no forzados: "
                    + dato.getErroresNoForzados();
        }

        return estadistica.getClass().getSimpleName();
    }
}
